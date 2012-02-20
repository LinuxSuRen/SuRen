/**
 * Create Date: 2011-8-18<br>
 * File Name: Document.java
 */
package org.suren.core.model;

/**
 * @author SuRen<br>
 *         Create Time: 04:08:34<br>
 */
public class Document
{
	private String	path;
	private String	content;

	/**
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

	/**
	 * @return the content
	 */
	public String getContent()
	{
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content)
	{
		this.content = content;
	}

}
