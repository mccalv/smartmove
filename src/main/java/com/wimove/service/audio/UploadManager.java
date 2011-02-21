/**
 * 
 */
package com.wimove.service.audio;
/**
 * 
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * A g for the list of &quot;uploadSpec&quot;
 * 
 * @author mccalv
 * 
 */
@SuppressWarnings("unchecked")
public class UploadManager {

	private static final String UPLOAD_XML = "upload.xml";
	private static Map<String, String> uploadSpec = new HashMap<String, String>();
	private static String destinationFolder;
	private static String virtualPath;
	private List<FileItem> items;

	/**
	 * Static block to parse the file called &quot;uploadSpec.xml&quot; on the
	 * same package of this class and populates Map of the allowed contentType,
	 * the destination folder of the file and the virtual path to be linked.
	 * 
	 * <pre>
	 * &lt;root&gt;
	 * 	&lt;exts&gt;
	 * 		&lt;ext suf=&quot;doc&quot;&gt;application/msword&lt;/ext&gt;
	 * 		&lt;ext suf=&quot;pdf&quot;&gt;application/pdf&lt;/ext&gt;
	 * 	&lt;/exts&gt;	
	 * 	
	 * 	&lt;destinationfolder&gt;/home/mccalv/Temp/&lt;/destinationfolder&gt;
	 * 	&lt;virtualpath&gt;/site-root/${folder}/${name}&lt;/virtualpath&gt;
	 * </root>
	 */
	static {

		SAXBuilder parser = new SAXBuilder();
		try {
			Document doc = parser.build(UploadManager.class
					.getResourceAsStream(UPLOAD_XML));

			/* Populate the destination folder */
			destinationFolder = doc.getRootElement().getChild(
					"destinationfolder").getTextTrim();
			virtualPath = doc.getRootElement().getChild("virtualpath")
					.getTextTrim();

			List<Element> children = doc.getRootElement().getChild("exts")
					.getChildren();
			for (Element element : children) {

				String suf = element.getAttribute("suf").getValue();
				String contentType = element.getText();
				uploadSpec.put(contentType, suf);

			}

			/*
			 * Create and unmodifiableMap
			 */
			uploadSpec = Collections.unmodifiableMap(uploadSpec);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws FileUploadException
	 * 
	 */
	public UploadManager(HttpServletRequest request)
			throws FileUploadException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		items = upload.parseRequest(request);
	}

	/**
	 * Returns a list of files whom extension are not supported according to the
	 * spec
	 * 
	 * @return a list of {@link FileItem}
	 * 
	 */
	public List<FileItem> getInvalidItems() {
		List<FileItem> errorFileList = new ArrayList<FileItem>();
		for (FileItem item : items) {

			if (!item.isFormField()) {
				if (uploadSpec.get(item.getContentType().toLowerCase()) == null) {
					errorFileList.add(item);
				}

			}

		}
		return errorFileList;
	}

	

	/**
	 * Check the file path to return the last chunk containing just the name
	 * without the extension
	 * 
	 * @param item
	 * @return
	 */
	private String getRealFileName(FileItem item) {
		String fileName = item.getName();
		int lastSlash = fileName.lastIndexOf(File.pathSeparator);
		if (lastSlash != -1)
			fileName = fileName.substring(lastSlash + 1);

		int lastDot = fileName.lastIndexOf('.');
		if (lastDot != -1)
			fileName = fileName.substring(0, lastDot);
		return fileName;
	}

	private static String generateFileName(String name) {
		String result = name.replaceAll("[^a-zA-Z0-9_-]", "-");
		if (result.length() > 50)
			result = result.substring(0, 50);
		return result + new Random().nextInt(1000);
	}

	/**
	 * 
	 * @param filePath
	 * @param folder
	 * @param name
	 * @return
	 */
	private String getVirtualPathAdjusted(String folder, String name,
			FileItem item) {
		return new StringBuilder(StringUtils.replace(StringUtils.replace(
				virtualPath, "${folder}", folder), "${name}", name))
				.append(".").append(uploadSpec.get(item.getContentType()))
				.toString();
	}

	private String getDestinationPathAdjusted(String subfolder, String name,
			FileItem item) {
		return new StringBuilder(destinationFolder).append(subfolder).append("/").append(
				name).append(".").append(uploadSpec.get(item.getContentType()))
				.toString();

	}

	/**
	 * Returns the {@link #uploadSpec} unmodifiable object
	 * 
	 * @return
	 */
	public static Map<String, String> getUploadSpec() {
		return uploadSpec;

	}

	/**
	 * @return the destinationFolder
	 */
	public static String getDestinationFolder() {
		return destinationFolder;
	}

	public static String getSupportedExtensions() {
		return uploadSpec.values().toString();

	}

}

