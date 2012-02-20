/**
 * 
 */
package org.suren.core.model;

/**
 * @author SuRen
 */
public class Menu
{

	private String					text;
	private String					tip;
	private String					icon;
	private char					key;
	private java.util.List<Menu>	items;

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * @return the tip
	 */
	public String getTip()
	{
		return tip;
	}

	/**
	 * @param tip
	 *            the tip to set
	 */
	public void setTip(String tip)
	{
		this.tip = tip;
	}

	/**
	 * @return the icon
	 */
	public String getIcon()
	{
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	/**
	 * @return the key
	 */
	public char getKey()
	{
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(char key)
	{
		this.key = key;
	}

	/**
	 * @return the items
	 */
	public java.util.List<Menu> getItems()
	{
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(java.util.List<Menu> items)
	{
		this.items = items;
	}
}
