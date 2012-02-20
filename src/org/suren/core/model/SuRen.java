/**
 * 
 */
package org.suren.core.model;

import java.awt.Dimension;
import java.awt.Point;

/**
 * @author SuRen
 */
public class SuRen
{

	private String		iconImage;
	private Point		location;
	private String		trayIcon;
	private Dimension	size;
	private Boolean		resize;
	private String		title;
	private String		show;

	/**
	 * @return the iconImage
	 */
	public String getIconImage()
	{
		return iconImage;
	}

	/**
	 * @param iconImage
	 *            the iconImage to set
	 */
	public void setIconImage(String iconImage)
	{
		this.iconImage = iconImage;
	}

	/**
	 * @return the location
	 */
	public Point getLocation()
	{
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Point location)
	{
		this.location = location;
	}

	/**
	 * @return the trayIcon
	 */
	public String getTrayIcon()
	{
		return trayIcon;
	}

	/**
	 * @param trayIcon
	 *            the trayIcon to set
	 */
	public void setTrayIcon(String trayIcon)
	{
		this.trayIcon = trayIcon;
	}

	/**
	 * @return the size
	 */
	public Dimension getSize()
	{
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Dimension size)
	{
		this.size = size;
	}

	public Boolean getResize()
	{
		return resize;
	}

	public void setResize(Boolean resize)
	{
		this.resize = resize;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the show
	 */
	public String getShow()
	{
		return show;
	}

	/**
	 * @param show
	 *            the show to set
	 */
	public void setShow(String show)
	{
		this.show = show;
	}
}
