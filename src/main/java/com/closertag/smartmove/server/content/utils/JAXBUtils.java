package com.closertag.smartmove.server.content.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.attachment.AttachmentMarshaller;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;

/**
 * 
 * TODO: have one set of caches; determine by class name or package name if a
 * context, marshaller, or unmarshaller exists to handle a given class
 * 
 * Class to hold utility methods for use with JAXB.
 * 
 * Code that needs to marshal and unmarshal should use unmarshal(T,
 * Class&lt;T$gt;) and marshalTo*(Object). These methods cache JAXBContexts,
 * marshallers, and unmarshallers intelligently for performance, and take care
 * of thread-safety issues.
 * 
 * getMarshaller() and getUnmarshaller() are also available, though their use is
 * discouraged in favor of marshal() and unmarshal(). If you do need direct
 * access to a Marshaller or Unmarshaller, remember to synchronize calls to
 * Marshaller.marshal() and Unmarshaller.unmarshal() if appropriate, as these
 * methods are not thread-safe by default.
 * 
 * Caching works as follows: Package names are mapped one-to-one to Sets of
 * Classes, which are mapped one-to-one to JAXBContexts. That is, a package, and
 * a subset of its classes, maps to a JAXBContext. This records the classes and
 * package that the cached JAXBContexts handle for later lookups. JAXBContexts
 * each have at most one associated Marshaller and Unmarshaller instance.
 */
public abstract class JAXBUtils {
	private static final Logger log = Logger.getLogger(JAXBUtils.class);

	private static final boolean DEBUG = log.isDebugEnabled();

	private static final Map<String, JAXBContext> packageNameContextCache = Util
			.makeHashMap();
	private static final Map<String, Marshaller> packageNameMarshallerCache = Util
			.makeHashMap();
	private static final Map<String, Unmarshaller> packageNameUnmarshallerCache = Util
			.makeHashMap();

	private static final Map<String, CacheEntry> contextCache = Util
			.makeHashMap();
	private static final Map<JAXBContext, Marshaller> marshallerCache = Util
			.makeHashMap();
	private static final Map<JAXBContext, Unmarshaller> unmarshallerCache = Util
			.makeHashMap();

	private JAXBUtils() {
		super();
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param <T>
	 * @param node
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	public static final <T> T unmarshal(final Node node, final Class<T> clazz)
			throws JAXBException {
		if (DEBUG) {
			log.debug("Unmarshalling '" + node + "' into '"
					+ clazz.getSimpleName() + "'");
		}

		final Unmarshaller unmarshaller = getUnmarshaller(clazz);

		synchronized (unmarshaller) {
			return unmarshaller.unmarshal(node, clazz).getValue();
		}
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param <T>
	 * @param reader
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	public static final <T> T unmarshal(final Reader reader,
			final Class<T> clazz) throws JAXBException {
		if (DEBUG) {
			log.debug("Unmarshalling from Reader into '"
					+ clazz.getSimpleName() + "'");
		}

		final Unmarshaller unmarshaller = getUnmarshaller(clazz);

		synchronized (unmarshaller) {
			return unmarshaller.unmarshal(new StreamSource(reader), clazz)
					.getValue();
		}
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param <T>
	 * @param file
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	public static final <T> T unmarshal(final File file, final Class<T> clazz)
			throws JAXBException, FileNotFoundException {
		return unmarshal(new FileReader(file), clazz);
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param <T>
	 * @param xml
	 * @param clazz
	 * @return
	 * @throws JAXBException
	 */
	public static final <T> T unmarshal(final String xml, final Class<T> clazz)
			throws JAXBException {
		if (DEBUG) {
			log.debug("Unmarshalling from XML into '" + clazz.getSimpleName()
					+ "'");
		}

		return unmarshal(new StringReader(xml), clazz);
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static final String marshalToString(final Object object)
			throws JAXBException {
		return marshalToString(object, null);
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param object
	 * @param cdataElements
	 *            an array of element names to wrap in CDATA blocks; these
	 *            elements are named in a somewhat implementation-specific way: @link
	 *            http://jaxb.dev.java.net/faq/index.html#marshalling_cdata
	 * @link http://jaxb.dev.java.net/faq/JaxbCDATASample.java
	 * @return
	 * @throws JAXBException
	 */
	public static final String marshalToString(final Object object,
			final String[] cdataElements) throws JAXBException {
		final StringWriter result = new StringWriter();

		marshalToWriter(object, result, cdataElements);

		return result.toString();
	}

	/**
	 * 
	 * @author Clint Gilbert
	 * 
	 *         May 21, 2009
	 * 
	 *         Intelligent Health Lab at the Childrens Hospital Informatics
	 *         Program at the Harvard-MIT division of Health Sciences Technology
	 * @link http://www.chip.org/ihl
	 * 
	 *       Initialize-on-demand-holder idiom
	 */
	private static final class TransformerFactoryHolder {
		private static final TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
	}

	/**
	 * Turns a w3c DOM Element into an XML-formatted String.
	 * 
	 * Runs the element through a null (identity)
	 * javax.xml.transform.Transformation to produce a String. I know the Java
	 * w3c DOM API is verbose and hyper-modular, so I wasn't expecting a clean
	 * solution, but this seems Rube Goldberg, even by Java stdlib standards. If
	 * anyone has a better idea that doesn't introduce new dependencies, I'm all
	 * for it. -Clint
	 * 
	 * @link See: http://blogs.oracle.com/reynolds/2007/05/printing_xml.html
	 * @param element
	 * @return The string representation of the passed element, or null if null
	 *         is passed in
	 * @throws JAXBException
	 * 
	 *             NB: I don't know how to make the output pretty-printed.
	 */
	public static final String marshalToString(final Element element)
			throws JAXBException {
		// Throw instead? Return empty string?
		if (element == null) {
			return null;
		}

		try {
			final Transformer nullTransformer;

			synchronized (TransformerFactoryHolder.transformerFactory) {
				nullTransformer = TransformerFactoryHolder.transformerFactory
						.newTransformer();
			}

			final StringWriter writer = new StringWriter();

			nullTransformer.transform(new DOMSource(element), new StreamResult(
					writer));

			return writer.toString();
		} catch (final Exception e) {
			log.error("Error marshalling DOM tree to String", e);

			return null;
		}
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static final void marshalToFile(final Object object, final File file)
			throws JAXBException {
		try {
			marshalToWriter(object, new FileWriter(file));
		} catch (IOException e) {
			throw new JAXBException(e);
		}
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static final void marshalToWriter(final Object object,
			final Writer writer) throws JAXBException {
		marshalToWriter(object, writer, null);
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param object
	 * @param cdataElements
	 *            an array of element names to wrap in CDATA blocks; these
	 *            elements are named in a somewhat implementation-specific way: @link
	 *            http://jaxb.dev.java.net/faq/index.html#marshalling_cdata
	 * @link http://jaxb.dev.java.net/faq/JaxbCDATASample.java
	 * @return
	 * @throws JAXBException
	 */
	public static final void marshalToWriter(final Object object,
			final Writer writer, final String[] cdataElements)
			throws JAXBException {
		final Marshaller marshaller = getMarshaller(object.getClass());

		if (cdataElements != null && cdataElements.length > 0) {
			final ContentHandler contentHandler = getContentHandler(writer,
					cdataElements);

			synchronized (marshaller) {
				marshaller.marshal(object, contentHandler);
			}
		} else {
			synchronized (marshaller) {
				marshaller.marshal(object, writer);
			}
		}
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static final Element marshalToElement(final Object object)
			throws JAXBException {
		return marshalToElement(object, null, null);
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * Gets a marshaller to use based on an optional package name to work around
	 * incompatibilities with JAXB 2.1 and code generated by the
	 * maven-jaxb-plugin (which implicitly uses JAXB/xjc 2.0).
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static final Element marshalToElement(final Object object,
			final String packageName) throws JAXBException {
		return marshalToElement(object, null, packageName);
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * Allows passing in a Marshaller to use.
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	public static final Element marshalToElement(final Object object,
			final Marshaller marshaller) throws JAXBException {
		return marshalToElement(object, marshaller, null);
	}

	/**
	 * Convenience method. Also centralizes thread-safety code.
	 * 
	 * Gets a marshaller to use based on an optional package name to work around
	 * incompatibilities with JAXB 2.1 and code generated by the
	 * maven-jaxb-plugin (which implicitly uses JAXB/xjc 2.0).
	 * 
	 * @param object
	 * @return
	 * @throws JAXBException
	 */
	private static final Element marshalToElement(final Object object,
			final Marshaller marshaller, final String packageName)
			throws JAXBException {
		final Document doc;

		try {
			doc = LazyDocumentBuilder.createDocument();
		} catch (final ParserConfigurationException e) {
			throw new JAXBException(e);
		}

		final Marshaller marshallerToUse;

		if (marshaller != null) {
			marshallerToUse = marshaller;
		} else {
			marshallerToUse = packageName == null ? getMarshaller(object
					.getClass()) : getMarshaller(packageName);
		}

		synchronized (marshallerToUse) {
			marshallerToUse.marshal(object, doc);
		}

		return doc.getDocumentElement();
	}

	/**
	 * Cache Unmarshallers for performance. See the JAXB 2.0 FAQ:
	 * https://jaxb.dev.java.net/faq/index.html#threadSafety
	 * 
	 * "If you really care about the performance, and/or your application is
	 * going to read a lot of small documents, then creating Unmarshaller could
	 * be relatively an expensive operation. In that case, consider pooling
	 * Unmarshaller objects. Different threads may reuse one Unmarshaller
	 * instance, as long as you don't use one instance from two threads at the
	 * same time."
	 * 
	 * @param packageName
	 * @return
	 * @throws JAXBException
	 */
	public static final Unmarshaller getUnmarshaller(final String packageName)
			throws JAXBException {
		synchronized (packageNameUnmarshallerCache) {
			if (!packageNameUnmarshallerCache.containsKey(packageName)) {
				packageNameUnmarshallerCache.put(packageName, JAXBContext
						.newInstance(packageName).createUnmarshaller());
			}
		}

		return packageNameUnmarshallerCache.get(packageName);
	}

	/**
	 * Create a new Unmarshaller instead of getting one from the cache. This may
	 * give better performance where many threads are unmarshalling large blocks
	 * of XML.
	 * 
	 * In general, however, use of this method is discouraged in favor of
	 * getUnmarshaller(). Use this only if you have solid evidence from
	 * profiling and benchmarks that it's necessary. using cached unmarshallers
	 * is almost always faster.
	 * 
	 * @param packageName
	 * @return
	 * @throws JAXBException
	 */
	public static final Unmarshaller getUncachedUnmarshaller(
			final String packageName) throws JAXBException {
		return getContext(packageName).createUnmarshaller();
	}

	private static final Marshaller setMarshallerProperties(
			final Marshaller marshaller) throws JAXBException {
		if (marshaller != null) {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
		}

		return marshaller;
	}

	/**
	 * Cache Marshallers for performance. See the JAXB 2.0 FAQ:
	 * https://jaxb.dev.java.net/faq/index.html#threadSafety
	 * 
	 * "If you really care about the performance, and/or your application is
	 * going to read a lot of small documents, then creating Unmarshaller could
	 * be relatively an expensive operation. In that case, consider pooling
	 * Unmarshaller objects. Different threads may reuse one Unmarshaller
	 * instance, as long as you don't use one instance from two threads at the
	 * same time."
	 * 
	 * @param packageName
	 * @return
	 * @throws JAXBException
	 */
	public static final Marshaller getMarshaller(final String packageName)
			throws JAXBException {
		synchronized (packageNameMarshallerCache) {
			if (!packageNameMarshallerCache.containsKey(packageName)) {
				packageNameMarshallerCache.put(packageName,
						setMarshallerProperties(getContext(packageName)
								.createMarshaller()));
			}
		}

		return packageNameMarshallerCache.get(packageName);
	}

	/**
	 * Create a new Marshaller instead of getting one from the cache. This may
	 * give better performance where many threads are marshalling large blocks
	 * of XML.
	 * 
	 * In general, however, use of this method is discouraged in favor of
	 * getMarshaller(). Use this only if you have solid evidence from profiling
	 * and benchmarks that it's necessary. using cached marshallers is almost
	 * always faster.
	 * 
	 * @param packageName
	 * @return
	 * @throws JAXBException
	 */
	public static final Marshaller getUncachedMarshaller(
			final String packageName) throws JAXBException {
		return setMarshallerProperties(getContext(packageName)
				.createMarshaller());
	}

	/**
	 * Cache JAXBContexts for performance. See the JAXB 2.0 FAQ:
	 * https://jaxb.dev.java.net/faq/index.html#threadSafety
	 * 
	 * "If you really care about the performance, and/or your application is
	 * going to read a lot of small documents, then creating Unmarshaller could
	 * be relatively an expensive operation. In that case, consider pooling
	 * Unmarshaller objects. Different threads may reuse one Unmarshaller
	 * instance, as long as you don't use one instance from two threads at the
	 * same time."
	 * 
	 * @param packageName
	 * @return
	 * @throws JAXBException
	 */
	private static final JAXBContext getContext(final String packageName)
			throws JAXBException {
		synchronized (packageNameContextCache) {
			if (!packageNameContextCache.containsKey(packageName)) {
				packageNameContextCache.put(packageName, JAXBContext
						.newInstance(packageName));
			}
		}

		return packageNameContextCache.get(packageName);
	}

	/**
	 * Cache Unmarshallers for performance. See the JAXB 2.0 FAQ:
	 * https://jaxb.dev.java.net/faq/index.html#threadSafety
	 * 
	 * "If you really care about the performance, and/or your application is
	 * going to read a lot of small documents, then creating Unmarshaller could
	 * be relatively an expensive operation. In that case, consider pooling
	 * Unmarshaller objects. Different threads may reuse one Unmarshaller
	 * instance, as long as you don't use one instance from two threads at the
	 * same time."
	 * 
	 * @param classes
	 * @return
	 * 
	 */
	public static final Unmarshaller getUnmarshaller(final Class<?>... classes)
			throws JAXBException {
		final JAXBContext context = getContext(classes);

		synchronized (unmarshallerCache) {
			if (!unmarshallerCache.containsKey(context)) {
				unmarshallerCache.put(context, context.createUnmarshaller());
			}

			return unmarshallerCache.get(context);
		}
	}

	/**
	 * Create a new Unmarshaller instead of getting one from the cache. This may
	 * give better performance where many threads are unmarshalling large blocks
	 * of XML.
	 * 
	 * In general, however, use of this method is discouraged in favor of
	 * getMarshaller(). Use this only if you have solid evidence from profiling
	 * and benchmarks that it's necessary. using cached marshallers is almost
	 * always faster.
	 * 
	 * @param classes
	 * @return
	 * @throws JAXBException
	 */
	public static final Unmarshaller getUncachedUnmarshaller(
			final Class<?>... classes) throws JAXBException {
		return getContext(classes).createUnmarshaller();
	}

	/**
	 * Cache Marshallers for performance. See the JAXB 2.0 FAQ:
	 * https://jaxb.dev.java.net/faq/index.html#threadSafety
	 * 
	 * "If you really care about the performance, and/or your application is
	 * going to read a lot of small documents, then creating Unmarshaller could
	 * be relatively an expensive operation. In that case, consider pooling
	 * Unmarshaller objects. Different threads may reuse one Unmarshaller
	 * instance, as long as you don't use one instance from two threads at the
	 * same time."
	 * 
	 * @param classes
	 * @return
	 * @throws JAXBException
	 * 
	 */
	public static final Marshaller getMarshaller(final Class<?>... classes)
			throws JAXBException {
		final JAXBContext context = getContext(classes);

		synchronized (marshallerCache) {
			if (!marshallerCache.containsKey(context)) {
				marshallerCache.put(context, setMarshallerProperties(context
						.createMarshaller()));
			}
		}

		return marshallerCache.get(context);
	}

	/**
	 * Create a new Marshaller instead of getting one from the cache. This may
	 * give better performance where many threads are marshalling large blocks
	 * of XML.
	 * 
	 * In general, however, use of this method is discouraged in favor of
	 * getMarshaller(). Use this only if you have solid evidence from profiling
	 * and benchmarks that it's necessary. using cached marshallers is almost
	 * always faster.
	 * 
	 * @param classes
	 * @return
	 * @throws JAXBException
	 */
	public static final Marshaller getUncachedMarshaller(
			final Class<?>... classes) throws JAXBException {
		return setMarshallerProperties(getContext(classes).createMarshaller());
	}

	/**
	 * Cache JAXBContexts for performance. See the JAXB 2.0 FAQ:
	 * https://jaxb.dev.java.net/faq/index.html#threadSafety
	 * 
	 * "If you really care about the performance, and/or your application is
	 * going to read a lot of small documents, then creating Unmarshaller could
	 * be relatively an expensive operation. In that case, consider pooling
	 * Unmarshaller objects. Different threads may reuse one Unmarshaller
	 * instance, as long as you don't use one instance from two threads at the
	 * same time."
	 * 
	 * Creates and caches JAXBContexts. Takes one or more Classes. If the param
	 * array is null, empty, or contains classes from more than one package, a
	 * JAXBException is thrown.
	 * 
	 * If the package name of param class(es) is unknown to the cache, a new
	 * CacheEntry is created with the param set of classes and a JAXBContext to
	 * handle them is made and returned.
	 * 
	 * If the package name of the param classes is known to the cache, the
	 * mapped CacheEntry is checked to see if the param classes are known to the
	 * cached JAXBContext for that package. If they aren't, then the classes are
	 * added to the CacheEntry, the marshaller and unmarshaller, caches for that
	 * package/JAXBContext are invalidated, and a new JAXBContext is created and
	 * returned.
	 * 
	 * If the package name of the param class(es) is known to the cache, and the
	 * cached JAXBContext for that package can handle the param class(es), then
	 * it is returned.
	 * 
	 * @param classes
	 * @return a cached JAXBContext that knows about the param class(es)
	 * @throws JAXBException
	 *             if the param classes are not all in the same package, or if
	 *             the param array is null or empty.
	 * 
	 */
	private static final JAXBContext getContext(final Class<?>... classes)
			throws JAXBException {
		if (classes == null || classes.length < 1) {
			throw new JAXBException("Null or empty list of classes passed in.");
		}

		if (!ClassTools.allSamePackage(classes)) {
			throw new JAXBException(
					"Passed-in classes have different packages.");
		}

		final String packageName = classes[0].getPackage().getName();

		final Set<Class<?>> classSet = Util.asSet(classes);

		synchronized (contextCache) {
			//
			// If the package name isn't known to the cache, make a new
			// Cache entry from the param classes and a new JAXBContext
			// that can handle them
			if (!contextCache.containsKey(packageName)) {
				final CacheEntry cacheEntry = new CacheEntry();

				cacheEntry.classSet = classSet;

				cacheEntry.context = JAXBContext.newInstance(classes);

				contextCache.put(packageName, cacheEntry);
			}
			//
			// If the package name is known to the cache, but the mapped
			// JAXBContext
			// can't handle one or more of the param classes, invalidate the
			// marshaller
			// and unmarshaller caches, create a new JAXB context to handle the
			// previously
			// registered classes plus any new ones, and record the new classes
			// that the
			// newly created JAXBContext can handle.
			else if (!contextCache.get(packageName).classSet
					.containsAll(classSet)) {
				final CacheEntry cacheEntry = contextCache.get(packageName);

				invalidateMarshallerAndUnmarshallerCaches(cacheEntry.context);

				cacheEntry.classSet.addAll(classSet);
				cacheEntry.context = makeJAXBContext(cacheEntry.classSet);
			}
			//
			// return the cached JAXBContext for the given package
			return contextCache.get(packageName).context;
		}
	}

	private static final JAXBContext makeJAXBContext(
			final Set<Class<?>> classSet) throws JAXBException {
		return JAXBContext.newInstance(classSet.toArray(new Class<?>[classSet
				.size()]));
	}

	private static final synchronized void invalidateMarshallerAndUnmarshallerCaches(
			final JAXBContext context) {
		marshallerCache.remove(context);
		unmarshallerCache.remove(context);
	}

	/**
	 * Convenience Method
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	private static final class LazyDocumentBuilder {
		private static DocumentBuilder value;

		private LazyDocumentBuilder() {

		}

		private static synchronized final void initDocumentBuilder()
				throws ParserConfigurationException {
			if (value == null) {
				value = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
			}
		}

		private static final Document createDocument()
				throws ParserConfigurationException {
			initDocumentBuilder();

			synchronized (value) {
				return value.newDocument();
			}
		}
	}

	/**
	 * See links below, required to handle CDATA specific strings
	 * 
	 * @link http://jaxb.dev.java.net/faq/index.html#marshalling_cdata
	 * @link http://jaxb.dev.java.net/faq/JaxbCDATASample.java
	 * 
	 * @return XMLSerializer capable of outputing CDATA elements
	 */
	private static final XMLSerializer getXMLSerializer(final Writer writer,
			final String[] CDATAElements) {
		return new XMLSerializer(writer, makeOutputFormat(CDATAElements));
	}

	/**
	 * See links below, required to handle CDATA specific strings
	 * 
	 * @link http://jaxb.dev.java.net/faq/index.html#marshalling_cdata
	 * @link http://jaxb.dev.java.net/faq/JaxbCDATASample.java
	 * 
	 * @return OutputFormat that instructs an XMLSerializer to output CDATA
	 *         elements
	 */
	private static final OutputFormat makeOutputFormat(
			final String[] CDATAElements) {
		// configure an OutputFormat to handle CDATA
		final OutputFormat result = new OutputFormat();

		// specify which of your elements you want to be handled as CDATA.
		// The use of the '^' between the namespaceURI and the localname
		// seems to be an implementation detail of the xerces code.
		// When processing xml that doesn't use namespaces, simply omit the
		// namespace prefix as shown in the third CDataElement below.
		result.setCDataElements(CDATAElements);

		// set any other options you'd like
		result.setPreserveSpace(true);
		result.setIndenting(true);

		return result;
	}

	/**
	 * Convenience method for getXMLSerializer(...)
	 * 
	 * @see #getXMLSerializer(java.io.Writer, String[])
	 */
	public static ContentHandler getContentHandler(final File file,
			final String[] CDATAElements) throws JAXBException {
		try {
			return getContentHandler(new FileWriter(file), CDATAElements);
		} catch (final Exception e) {
			log.error("Failed to open content handler: ", e);

			throw new JAXBException(e);
		}
	}

	/**
	 * Convenience method for getXMLSerializer(...)
	 * 
	 * @see #getXMLSerializer(java.io.Writer, String[])
	 */
	public static ContentHandler getContentHandler(final Writer writer,
			final String[] CDATAElements) throws JAXBException {
		try {
			return getXMLSerializer(writer, CDATAElements).asContentHandler();
		} catch (final IOException e) {
			throw new JAXBException(e);
		}
	}

	/**
	 * 
	 * @author Clint Gilbert
	 * 
	 *         Sep 17, 2007
	 * 
	 *         Childrens Hospital Informatics Program at the Harvard-MIT
	 *         division of Health Sciences Technology
	 * @link http://www.chip.org
	 * 
	 *       All works licensed by the Lesser GPL
	 * @link http://www.gnu.org/licenses/lgpl.html
	 * 
	 *       Portions of this code donated by Ana Holzbach, David Berkowicz,
	 *       Ahmad Namani, Henry Chueh and the MGH Lab of Computer Science
	 * @link http://www.lcs.mgh.harvard.edu/
	 * 
	 *       VO to represent a one-to-one mapping between a set of classes and a
	 *       JAXBContext marshal/unmarshal them. Or in other words, records
	 *       which classes a JAXBContext can handle.
	 */
	private static final class CacheEntry {
		private Set<Class<?>> classSet;

		private JAXBContext context;

		public CacheEntry() {
			super();
		}
	}

	/**
	 * Returns a wrapper that guarantees that all operations on the passed
	 * Marshaller are synchronized NB: Does not set default marshaller
	 * properties
	 * 
	 * @param marshaller
	 * @return
	 */
	public static final Marshaller synchronizedMarshaller(
			final Marshaller marshaller) {
		return new SynchronizedMarshaller(marshaller);
	}

	/**
	 * Returns a wrapper that guarantees that all operations on the passed
	 * Unmarshaller are synchronized
	 * 
	 * @param unmarshaller
	 * @return
	 */
	public static final Unmarshaller synchronizedUnmarshaller(
			final Unmarshaller unmarshaller) {
		return new SynchronizedUnmarshaller(unmarshaller);
	}

	/**
	 * 
	 * @author Clint Gilbert
	 * 
	 *         Feb 22, 2008
	 * 
	 *         Childrens Hospital Informatics Program at the Harvard-MIT
	 *         division of Health Sciences Technology
	 * @link http://www.chip.org
	 * 
	 *       All works licensed by the Lesser GPL
	 * @link http://www.gnu.org/licenses/lgpl.html
	 * 
	 *       Wrapper class to guarantee that all operations on a Marshaller are
	 *       synchronized
	 */
	private static class SynchronizedMarshaller implements Marshaller {
		private final Marshaller delegate;

		private SynchronizedMarshaller(final Marshaller delegate) {
			super();

			this.delegate = delegate;
		}

		@SuppressWarnings("unchecked")
		public synchronized <A extends XmlAdapter> A getAdapter(
				final Class<A> arg0) {
			return delegate.getAdapter(arg0);
		}

		public synchronized AttachmentMarshaller getAttachmentMarshaller() {
			return delegate.getAttachmentMarshaller();
		}

		public synchronized ValidationEventHandler getEventHandler()
				throws JAXBException {
			return delegate.getEventHandler();
		}

		public synchronized Listener getListener() {
			return delegate.getListener();
		}

		public synchronized Node getNode(final Object arg0)
				throws JAXBException {
			return delegate.getNode(arg0);
		}

		public synchronized Object getProperty(final String arg0)
				throws PropertyException {
			return delegate.getProperty(arg0);
		}

		public synchronized Schema getSchema() {
			return delegate.getSchema();
		}

		public synchronized void marshal(final Object arg0,
				final ContentHandler arg1) throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		public synchronized void marshal(final Object arg0, final File arg1)
				throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		public synchronized void marshal(final Object arg0, final Node arg1)
				throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		public synchronized void marshal(final Object arg0,
				final OutputStream arg1) throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		public synchronized void marshal(final Object arg0, final Result arg1)
				throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		public synchronized void marshal(final Object arg0, final Writer arg1)
				throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		public synchronized void marshal(final Object arg0,
				final XMLEventWriter arg1) throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		public synchronized void marshal(final Object arg0,
				final XMLStreamWriter arg1) throws JAXBException {
			delegate.marshal(arg0, arg1);
		}

		@SuppressWarnings("unchecked")
		public synchronized <A extends XmlAdapter> void setAdapter(
				final Class<A> arg0, final A arg1) {
			delegate.setAdapter(arg0, arg1);
		}

		@SuppressWarnings("unchecked")
		public synchronized void setAdapter(final XmlAdapter arg0) {
			delegate.setAdapter(arg0);
		}

		public synchronized void setAttachmentMarshaller(
				final AttachmentMarshaller arg0) {
			delegate.setAttachmentMarshaller(arg0);
		}

		public synchronized void setEventHandler(
				final ValidationEventHandler arg0) throws JAXBException {
			delegate.setEventHandler(arg0);
		}

		public synchronized void setListener(final Listener arg0) {
			delegate.setListener(arg0);
		}

		public synchronized void setProperty(final String arg0,
				final Object arg1) throws PropertyException {
			delegate.setProperty(arg0, arg1);
		}

		public synchronized void setSchema(final Schema arg0) {
			delegate.setSchema(arg0);
		}
	}

	/**
	 * 
	 * @author Clint Gilbert
	 * 
	 *         Feb 22, 2008
	 * 
	 *         Childrens Hospital Informatics Program at the Harvard-MIT
	 *         division of Health Sciences Technology
	 * @link http://www.chip.org
	 * 
	 *       All works licensed by the Lesser GPL
	 * @link http://www.gnu.org/licenses/lgpl.html
	 * 
	 *       Wrapper class to guarantee that all operations on a Marshaller are
	 *       synchronized
	 */
	private static class SynchronizedUnmarshaller implements Unmarshaller {
		private final Unmarshaller delegate;

		private SynchronizedUnmarshaller(final Unmarshaller delegate) {
			super();

			this.delegate = delegate;
		}

		@SuppressWarnings("unchecked")
		public synchronized <A extends XmlAdapter> A getAdapter(
				final Class<A> arg0) {
			return delegate.getAdapter(arg0);
		}

		public synchronized AttachmentUnmarshaller getAttachmentUnmarshaller() {
			return delegate.getAttachmentUnmarshaller();
		}

		public synchronized ValidationEventHandler getEventHandler()
				throws JAXBException {
			return delegate.getEventHandler();
		}

		public synchronized javax.xml.bind.Unmarshaller.Listener getListener() {
			return delegate.getListener();
		}

		public synchronized Object getProperty(final String arg0)
				throws PropertyException {
			return delegate.getProperty(arg0);
		}

		public synchronized Schema getSchema() {
			return delegate.getSchema();
		}

		public synchronized UnmarshallerHandler getUnmarshallerHandler() {
			return delegate.getUnmarshallerHandler();
		}

		@Deprecated
		public synchronized boolean isValidating() throws JAXBException {
			return delegate.isValidating();
		}

		@SuppressWarnings("unchecked")
		public synchronized <A extends XmlAdapter> void setAdapter(
				final Class<A> arg0, final A arg1) {
			delegate.setAdapter(arg0, arg1);
		}

		@SuppressWarnings("unchecked")
		public synchronized void setAdapter(final XmlAdapter arg0) {
			delegate.setAdapter(arg0);
		}

		public synchronized void setAttachmentUnmarshaller(
				final AttachmentUnmarshaller arg0) {
			delegate.setAttachmentUnmarshaller(arg0);
		}

		public synchronized void setEventHandler(
				final ValidationEventHandler arg0) throws JAXBException {
			delegate.setEventHandler(arg0);
		}

		public synchronized void setListener(
				final javax.xml.bind.Unmarshaller.Listener arg0) {
			delegate.setListener(arg0);
		}

		public synchronized void setProperty(final String arg0,
				final Object arg1) throws PropertyException {
			delegate.setProperty(arg0, arg1);
		}

		public synchronized void setSchema(final Schema arg0) {
			delegate.setSchema(arg0);
		}

		@Deprecated
		public synchronized void setValidating(final boolean arg0)
				throws JAXBException {
			delegate.setValidating(arg0);
		}

		public synchronized Object unmarshal(final File arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized Object unmarshal(final InputSource arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized Object unmarshal(final InputStream arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized <T> JAXBElement<T> unmarshal(final Node arg0,
				final Class<T> arg1) throws JAXBException {
			return delegate.unmarshal(arg0, arg1);
		}

		public synchronized Object unmarshal(final Node arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized Object unmarshal(final Reader arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized <T> JAXBElement<T> unmarshal(final Source arg0,
				final Class<T> arg1) throws JAXBException {
			return delegate.unmarshal(arg0, arg1);
		}

		public synchronized Object unmarshal(final Source arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized Object unmarshal(final URL arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized <T> JAXBElement<T> unmarshal(
				final XMLEventReader arg0, final Class<T> arg1)
				throws JAXBException {
			return delegate.unmarshal(arg0, arg1);
		}

		public synchronized Object unmarshal(final XMLEventReader arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}

		public synchronized <T> JAXBElement<T> unmarshal(
				final XMLStreamReader arg0, final Class<T> arg1)
				throws JAXBException {
			return delegate.unmarshal(arg0, arg1);
		}

		public synchronized Object unmarshal(final XMLStreamReader arg0)
				throws JAXBException {
			return delegate.unmarshal(arg0);
		}
	}
	
	
	
	
}
