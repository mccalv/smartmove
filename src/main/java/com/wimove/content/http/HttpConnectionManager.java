/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 30 Nov 2009
 * mccalv
 * HttpConnectionManager
 */
package com.wimove.content.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Manages HTTP connections to the PHP/JSAM server.
 * 
 * @author jope
 */
public class HttpConnectionManager {
	private static final Log log = LogFactory
			.getLog(HttpConnectionManager.class);

	/**
	 * Content type expected by the server when POSTing Xml.
	 */
	private static final String CONTENT_TYPE_XML = "text/xml; charset=UTF-8";

	/**
	 * Default value for the maximum number of connections that will be created
	 * for any particular HostConfiguration.
	 */
	private final static int DEFAULT_MAX_SERVER_CONNECTIONS = 200;

	/**
	 * The default value for the maximum allowed request time in ms.
	 */
	private final static int DEFAULT_MAX_REQUEST_TIMEOUT = 30 * 1000;

	/**
	 * The target server entry point
	 */
	private URL serverUrl;
	/**
	 * Allowed amount of time to complete a request. Or the sum of the different
	 * timeout values : - get connection from the pool - connect to the server -
	 * socket timeout (reading or writing)
	 */
	private int maxRequestTimeout = DEFAULT_MAX_REQUEST_TIMEOUT;

	/*
	 * Ideally this value should match the number of worker threads but also the
	 * target server should allow this many requests, if not we'll have some
	 * threads waiting for the target server to accept connections.
	 */
	private int maxServerConnections = DEFAULT_MAX_SERVER_CONNECTIONS;

	private final MultiThreadedHttpConnectionManager connectionManager;

	private final HttpClient client;

	/**
	 * a package access constructor is enough for CGLIB proxies...
	 */
	HttpConnectionManager() {

		/*
		 * CGLIB class proxies delegate to the correct object so its safe to do
		 */
		connectionManager = null;
		client = null;
	}

	/**
	 * @param url
	 * @throws MalformedURLException
	 *             If the serverUrl is not configured properly
	 */
	public HttpConnectionManager(String url, boolean useTimeouts)
			throws MalformedURLException {

		connectionManager = new MultiThreadedHttpConnectionManager();

		client = new HttpClient(connectionManager);

		// remove any trailing "/"
		if (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		serverUrl = new URL(url);

		HttpConnectionManagerParams params = connectionManager.getParams();

		client.getParams().setContentCharset("UTF-8");

		/*
		 * Prevent sending/receiving cookies automatically. We want to handle
		 * the cookies manually in each request.
		 */
		client.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);

		if (useTimeouts) {

			/*
			 * Set time out values trying to comply with the global
			 * maxRequestTimeout.
			 * 
			 * poolTimeout: get connection from the pool. If the number of
			 * connections is equal to the number of worker threads this can be
			 * set a small timeout (3 seconds??)
			 * 
			 * socketTimeout: timeout when reading or writing on the socket. The
			 * server is in close to us (not via internet) so we can use a low
			 * value.
			 * 
			 * connectionTimeout: timeout when connecting to the server. The
			 * server is usually busy so we should keep this value higher than
			 * the rest.
			 */

			int poolTimeout = 10000;
			int socketTimeout = maxRequestTimeout / 3 - poolTimeout;
			int connectionTimeout = maxRequestTimeout - socketTimeout;

			log.info("timeouts: pool " + poolTimeout + " socket "
					+ socketTimeout + " connection " + connectionTimeout);

			client.getParams().setConnectionManagerTimeout(poolTimeout);

			// client.getParams().setHttpElementCharset(charset);
			params.setSoTimeout(socketTimeout);
			params.setConnectionTimeout(connectionTimeout);

			/* Maximun connections allowed */

			params.setMaxTotalConnections(maxServerConnections);

		}
		params.setMaxConnectionsPerHost(
				HostConfiguration.ANY_HOST_CONFIGURATION, maxServerConnections);

		client.getHostConfiguration().setHost(serverUrl.getHost(),
				serverUrl.getPort());
	}

	/**
	 * Call the server to PUT data and return the response as a int
	 * 
	 * @param resource
	 *            The targert URI
	 * @param headers
	 *            The headers to send with the request.
	 * @param body
	 *            The data to be sent as request body in the PUT as bytes
	 * @return Returns the the integer status of the PUT (eg 200 or 404 etc)
	 */
	public int getResponseCode(String resource, Map<String, String> headers,
			byte[] body) throws HttpException, IOException {

		PutMethod method = new PutMethod(serverUrl + resource);

		if (!ArrayUtils.isEmpty(body)) {
			method.setRequestEntity(new ByteArrayRequestEntity(body));
		}

		setHeaders(method, headers);

		return client.executeMethod(client.getHostConfiguration(), method);
	}

	/**
	 * Post a fiel to the server and get the response
	 * 
	 * @param resource
	 * @param file
	 * @param fileFormName
	 * @param contentType
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public InputStream getPostFileResponse(String resource, File file,
			String fileFormName, String contentType) throws HttpException,
			IOException {
		PostMethod method = new PostMethod(resource);

		FilePart filePart = new FilePart(fileFormName, file);
		filePart.setContentType(contentType);
		Part[] parts = { filePart
		// , new StringPart("MAX_FILE_SIZE", "10000"), new
		// StringPart("submit","Invia Il file")
		};
		method.setRequestEntity(new MultipartRequestEntity(parts, method
				.getParams()));

		int retCode = client.executeMethod(client.getHostConfiguration(),
				method);

		if (retCode != HttpStatus.SC_OK) {
			throw new RuntimeException("Server error " + retCode);

		} else {

			return method.getResponseBodyAsStream();
		}
	}

	/**
	 * Call the server to post data with no body and return the response as
	 * bytes. The headers map are added as HTTP headers and the params as
	 * parameters to the URL called in the POST (?blah=foo) etc.
	 * 
	 * @param resource
	 *            The targert URI
	 * @param headers
	 *            The headers to send with the request.
	 * @param params
	 *            The parameters to be sent in the POST request.
	 * @return Returns the bytes of the response or null if no response or
	 *         error.
	 */
	public byte[] getByteArrayResourcePost(String resource,
			Map<String, String> headers, Map<String, String> params)
			throws IOException {
		PostMethod post = new PostMethod(serverUrl + resource);

		if (params != null && !params.isEmpty()) {
			HttpMethodParams paramToPost = new HttpMethodParams();
			for (Iterator<Entry<String, String>> paramIter = params.entrySet()
					.iterator(); paramIter.hasNext();) {
				Entry<String, String> entry = paramIter.next();
				paramToPost.setParameter(entry.getKey(), entry.getValue());
			}
			post.setParams(paramToPost);
		}

		setHeaders(post, headers);

		Integer retCode = client.executeMethod(client.getHostConfiguration(),
				post);
		if (retCode.intValue() == HttpStatus.SC_OK) {
			return IOUtils.toByteArray(post.getResponseBodyAsStream());
		} else {
			String error = IOUtils.toString(post.getResponseBodyAsStream());
			log.warn("Post without body and params failed: " + error);
			return null;
		}
	}

	/**
	 * Call the server to get or post data and return the response as a string
	 * (usually XML). The headers map is emptied and filled with the response
	 * headers.
	 * 
	 * @param resource
	 *            The targert URI
	 * @param headers
	 *            [INOUT] The headers to send with the request. If the request
	 *            is completed the contents are removed and filled with the
	 *            response headers.
	 * @param body
	 *            The data to be sent as request body in a POST
	 * @return Returns the response body or null if no response or if the URI
	 *         could not be built at all
	 */
	public String getResource(String resource, Map<String, String> headers,
			String body) {

		String uri = buildURI(resource);

		if (uri != null) {
			HttpMethodBase method = getRequest(uri, body);

			try {

				InputStream in = getResourceStream(method, headers, body);

				if (in == null) {
					return null;
				} else {
					return IOUtils.toString(new InputStreamReader(in, "utf-8"));
				}

			} catch (IOException exception) {
				throw new RuntimeException(exception);
				// WimoveException.HTTP_IO_ERROR, resource,
				// exception, exception.getMessage());
			} finally {
				method.releaseConnection();
			}
		} else {
			return null;
		}
	}

	public InputStream getResourceInputStream(String resource, String body) {

		String uri = buildURI(resource);

		if (uri != null) {
			HttpMethodBase method = getRequest(uri, body);

			try {

				InputStream in = getResourceStream(method, null, body);

				if (in == null) {
					return null;
				} else {
					return in;
				}

			} catch (IOException exception) {
				throw new RuntimeException(exception);
				// WimoveException.HTTP_IO_ERROR, resource,
				// exception, exception.getMessage());
			} finally {
				method.releaseConnection();
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets the response stream from executing the HTTP method
	 * 
	 * @param method
	 *            the method to be executed
	 * @param headers
	 *            the headers
	 * @param body
	 *            the body
	 * @return the resource stream
	 * @throws IOException
	 *             if some HTTP Transport exception occurs
	 */
	private InputStream getResourceStream(HttpMethodBase method,
			Map<String, String> headers, String body) throws IOException {

		method.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		setHeaders(method, headers);

		if (method instanceof PostMethod) {
			PostMethod post = (PostMethod) method;
			post.setRequestEntity(new StringRequestEntity(body,
					CONTENT_TYPE_XML, null));
		}

		int retCode = client.executeMethod(client.getHostConfiguration(),
				method);

		if (retCode != HttpStatus.SC_OK) {
			
			throw new RuntimeException(
					"Impossible to execute the query against the url "
							+ getServerUrl()+method.getPath() + ", Error :" + retCode);
			// throw new PhiladelphiaException(
			// PhiladelphiaExceptionTypes.HTTP_RESPONSE_ERROR, method
			// .getURI()
			// + ": " + method.getStatusLine(), method
			// .getResponseBodyAsString());
		} else {
		
			return method.getResponseBodyAsStream();
		}
	}

	/**
	 * Call the server to get or post data and return the response as a byte
	 * array (usually binary data). The headers map is emptied and filled with
	 * the response headers.
	 * 
	 * @param resource
	 *            The targert URI
	 * @param headers
	 *            headers to be sent
	 * @param body
	 *            The data to be sent as request body in a POST
	 * @return returns the response body or null if no response
	 * @see #getResource(String, Map, String)
	 */
	public byte[] getByteArrayResource(String resource,
			Map<String, String> headers, String body) {
		HttpMethodBase method = getRequest(serverUrl + resource, body);

		try {
			InputStream in = getResourceStream(method, headers, body);

			if (in == null) {
				// no body...
				return null;
			}

			return IOUtils.toByteArray(in);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
			// throw new PhiladelphiaException(
			// PhiladelphiaExceptionTypes.HTTP_IO_ERROR, resource,
			// exception, exception.getMessage());
		} finally {
			method.releaseConnection();
		}

	}

	/**
	 * Opens a Post connection and retrieves the Headers as a Map
	 * 
	 * @param resource
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> getPostResponseHeaders(String resource,
			Map<String, String> parameters) throws IOException {

		HttpMethodBase method = new PostMethod(this.serverUrl + resource);

		client.getParams().setConnectionManagerTimeout(
				DEFAULT_MAX_REQUEST_TIMEOUT);

		HttpMethodParams params = new HttpMethodParams();
		for (Map.Entry<String, String> param : parameters.entrySet()) {

			params.setParameter(param.getKey(), param.getValue());
		}
		method.setParams(params);
		try {
			client.executeMethod(method);
			Map<String, String> headers = new HashMap<String, String>();

			Header[] requestHeaders = method.getResponseHeaders();
			for (Header header : requestHeaders) {
				headers.put(header.getName(), header.getValue());
			}
			return headers;

		} catch (Exception e) {

			log.error("Impossible to open a connection to" + this.serverUrl
					+ resource, e);
			return null;

		}

		finally {
			method.releaseConnection();

		}
	}

	

	/**
	 * Returns the {@link PostMethod} or {@link GetMethod} depending on the body
	 * to the request
	 * 
	 * @param resource
	 * @param body
	 * @return
	 */
	private HttpMethodBase getRequest(String resource, String body) {
		return ((body != null) ? new PostMethod(resource) : new GetMethod(
				resource));
	}

	/**
	 * Sets the headers of the connection
	 * 
	 * @param m
	 * @param headers
	 */
	private void setHeaders(HttpMethodBase m, Map<String, String> headers) {
		m.setRequestHeader("Content-Type", CONTENT_TYPE_XML);
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				m.setRequestHeader(header.getKey(), header.getValue());
			}
		}
	}

	/**
	 * Helper to create the URI. The uri below is constructed in the same way
	 * as:
	 * <p/>
	 * String resourcePath = StringUtils.substringBeforeLast(resource,"/");
	 * String idstr =
	 * StringUtils.substringBefore(StringUtils.substringAfterLast(resource,
	 * "/"), "?"); String versionQuery = StringUtils.substringAfter(resource,
	 * "?"); uri = serverUrl + resourcePath + "/" +
	 * URLEncoder.encode(idstr,"UTF-8") + "?" + versionQuery;
	 * <p/>
	 * But we use StringBuilder for efficiency.
	 * 
	 * @param resource
	 * @return
	 */
	private String buildURI(String resource) {
		String uri = null;
		try {
			return new StringBuilder(serverUrl.toString()).append(
					StringUtils.substringBeforeLast(resource, "/")).append('/')
					.append(
							URLEncoder.encode(StringUtils.substringBefore(
									StringUtils.substringAfterLast(resource,
											"/"), "?"), "UTF-8")).append('?')
					.append(StringUtils.substringAfter(resource, "?"))
					.toString();

		} catch (UnsupportedEncodingException e) {
			log.warn("URL Encoding exception for URL: " + uri);
		}
		return uri;
	}

	public int getMaxRequestTimeout() {
		return maxRequestTimeout;
	}

	public void setMaxRequestTimeout(int connectionTimeout) {
		this.maxRequestTimeout = connectionTimeout;
	}

	public URL getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(URL serverUrl) {
		this.serverUrl = serverUrl;
	}

	public int getMaxServerConnections() {
		return maxServerConnections;
	}

	public void setMaxServerConnections(int maxServerConnections) {
		this.maxServerConnections = maxServerConnections;
	}

}
