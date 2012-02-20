/**
 * 
 */
package org.suren;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.suren.core.model.Lucene;
import org.suren.core.model.Menu;
import org.suren.core.model.SuRen;
import org.suren.core.net.Host;
import org.suren.main.StartUp;

/**
 * @author fish init sys properties for xml file.
 */
public class SuRenProperties
{

	public static final String	SUREN	= StartUp.configFile;

	// for menu attr and so on.
	public static final String	KEY		= "key";
	public static final String	Remark	= "Remark";

	// for system attr
	public static final String	TITLE	= "title";
	public static final String	SHOW	= "show";
	public static final String	PATH	= "path";
	public static final String	X		= "x";
	public static final String	Y		= "y";
	// if tray is "1", SystemTry will on.
	public static final String	TRAY	= "tray";
	public static final String	ICON	= "icon";
	// if exit is "1", when closing put sys into tray.
	public static final String	EXIT	= "exit";
	public static final String	WIDTH	= "width";
	public static final String	HEIGHT	= "height";
	// if resize is "1", allow to resize the frame.
	public static final String	RESIZE	= "resize";
	public static final String	PORT	= "port";
	public static final String	NIC		= "Nic";

	public static final String	INDEX	= "index";

	private Document			document;
	private Element				root;

	public SuRenProperties() {
	}

	public void init() throws IOException
	{
		document = DocumentHelper.createDocument();

		root = document.addElement("SuRen");

		// init by order
		menuInit();
		sysInit();
		network();
		lucene();

		// writer infomation into file
		write();
	}

	private void write() throws IOException
	{
		File file = new File(SUREN);
		FileWriter fileWriter = new FileWriter(file);
		XMLWriter writer = new XMLWriter(fileWriter);

		writer.write(document);
		writer.close();
	}

	// about menu info.
	private void menuInit()
	{
		Element menu = root.addElement("Menu");

		// menu for file
		Element file_menu = menu.addElement("File");
		file_menu.addAttribute(KEY, "f");
		file_menu.addAttribute(Remark, "here is some o.");

		Element exit_file = file_menu.addElement("Exit");
		exit_file.addAttribute(KEY, "e");

		Element properties_file = file_menu.addElement("Properties");
		properties_file.addAttribute(KEY, "p");

		// menu for window
		Element window_menu = menu.addElement("Window");
		window_menu.addAttribute(KEY, "w");

		Element char_window = window_menu.addElement("Chat");
		char_window.addAttribute(KEY, "r");

		Element cmd_window = window_menu.addElement("Cmd");
		cmd_window.addAttribute(KEY, "c");

		Element paint_window = window_menu.addElement("Paint");
		paint_window.addAttribute(KEY, "t");

		Element sshClient_window = window_menu.addElement("SSHClient");
		sshClient_window.addAttribute(KEY, "s");

		Element fileTransfer_window = window_menu.addElement("SSHFileTransfer");
		fileTransfer_window.addAttribute(KEY, "f");

		Element search_window = window_menu.addElement("Seacher");
		search_window.addAttribute(KEY, "h");

		// menu for help
		Element help_menu = menu.addElement("Help");
		help_menu.addAttribute(KEY, "h");

		Element welcome_help = help_menu.addElement("Welcome");
		welcome_help.addAttribute(KEY, "l");

		Element about_help = help_menu.addElement("About");
		about_help.addAttribute(KEY, "a");
	}

	// here will inited some properties for system.
	private void sysInit()
	{
		Element sys = root.addElement("System");
		sys.addAttribute(TITLE, "SuRen Application.");
		sys.addAttribute(SHOW, "Paint");

		Element icon_sys = sys.addElement("Icon");
		icon_sys.addAttribute(PATH, "org/suren/image/face.gif");

		Element location_sys = sys.addElement("Location");
		location_sys.addAttribute(X, "1");
		location_sys.addAttribute(Y, "1");

		Element tray_sys = sys.addElement("SystemTray");
		tray_sys.addAttribute(TRAY, "1");
		tray_sys.addAttribute(ICON, "org/suren/image/tray.gif");

		Element exit_sys = sys.addElement("ExitAction");
		exit_sys.addAttribute(EXIT, "1");

		Element size_sys = sys.addElement("Size");
		size_sys.addAttribute(WIDTH, "900");
		size_sys.addAttribute(HEIGHT, "600");

		Element resize_sys = sys.addElement("ReSize");
		resize_sys.addAttribute(RESIZE, "true");
	}

	// network config infomation
	private void network()
	{
		Element network = root.addElement("Network");

		Element chat_network = network.addElement("Chat");
		chat_network.addAttribute(PORT, "1234");

		Element eth_network = network.addElement("Nic");
		eth_network.addAttribute(NIC, "eth3");
	}

	private void lucene()
	{
		Element lucene = root.addElement("Lucene");

		lucene.addAttribute(INDEX, "d:/luceneIndex");
	}

	private Document getDoc() throws DocumentException
	{
		try
		{

			File suren = new File(SUREN);

			if (!suren.exists()) init();

			SAXReader reader = new SAXReader();
			document = reader.read(suren);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return document;
	}

	private Element getRoot() throws DocumentException
	{
		Document document = getDoc();
		Element root = document.getRootElement();

		return root;
	}

	public SuRen getSuRen()
	{
		SuRen suren = new SuRen();
		Element root;

		try
		{

			root = getRoot();
			root = root.element("System");

			suren.setTitle(root.attributeValue(TITLE));
			suren.setShow(root.attributeValue(SHOW));

			Element location = root.element("Location");
			Element size = root.element("Size");

			suren.setIconImage(root.element("Icon").attributeValue(PATH));
			suren.setTrayIcon(root.element("SystemTray").attributeValue(ICON));
			suren.setLocation(new Point(Integer.parseInt(location.attributeValue(X)), Integer
					.parseInt(location.attributeValue(Y))));
			suren.setResize("true".equals(root.element("ReSize").attributeValue(RESIZE)) ? true
					: false);

			Dimension dim = new Dimension();
			dim.setSize(Double.parseDouble(size.attributeValue(WIDTH)),
					Double.parseDouble(size.attributeValue(HEIGHT)));
			suren.setSize(dim);

		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}

		return suren;
	}

	public void updateSuRen(SuRen suren)
	{
		Element root;

		try
		{

			root = getRoot();

			root = root.element("System");
			Element location = root.element("Location");
			Element size = root.element("Size");
			Element resize = root.element("ReSize");
			Element icon = root.element("Icon");
			Element tray = root.element("SystemTray");

			if (suren.getTitle() != null)
			{
				root.attribute(TITLE).setValue(suren.getTitle());
			}

			if (suren.getShow() != null)
			{
				root.attribute(SHOW).setValue(suren.getShow());
			}

			if (suren.getLocation() != null)
			{
				location.attribute(X).setValue(String.valueOf(suren.getLocation().x));
				location.attribute(Y).setValue(String.valueOf(suren.getLocation().y));
			}

			if (suren.getSize() != null)
			{
				size.attribute(WIDTH).setValue(String.valueOf(suren.getSize().getWidth()));
				size.attribute(HEIGHT).setValue(String.valueOf(suren.getSize().getHeight()));
			}

			if (suren.getResize() != null)
			{
				resize.attribute(RESIZE).setValue(suren.getResize().toString());
			}

			if (suren.getIconImage() != null)
			{
				icon.attribute(PATH).setValue(suren.getIconImage());
			}

			if (suren.getIconImage() != null)
			{
				tray.attribute(ICON).setValue(suren.getTrayIcon());
			}

			write();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Host getNetWork()
	{
		Host host = new Host();
		Element root;

		try
		{
			root = getRoot();
			Element network = root.element("Network");
			Element chat = network.element("Chat");
			Element nic = network.element("Nic");

			host.setEth(nic.attributeValue(NIC));
			host.setPort(Integer.parseInt(chat.attributeValue(PORT)));
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}

		return host;
	}

	public void updateNetwork(Host host)
	{
		Element root;

		try
		{

			root = getRoot();
			Element network = root.element("Network");
			Element chat = network.element("Chat");
			Element nic = network.element("Nic");

			if (host.getEth() != null)
			{
				chat.attribute(PORT).setValue(host.getPort().toString());
			}

			if (host.getPort() != null)
			{
				nic.attribute(NIC).setValue(host.getEth());
			}

			write();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public java.util.List<Menu> getMenus()
	{
		java.util.List<Menu> menus = new java.util.ArrayList<Menu>();
		Element root;

		try
		{

			root = getRoot();
			root = root.element("Menu");
			Iterator<Element> it = root.elementIterator();
			while (it.hasNext())
			{
				Element ele = it.next();
				Menu menu = new Menu();

				menu.setText(ele.getName());
				menu.setKey(ele.attributeValue(KEY) != null ? ele.attributeValue(KEY).charAt(0)
						: ' ');

				Iterator<Element> item = ele.elementIterator();
				if (item.hasNext())
				{
					java.util.List<Menu> items = new java.util.ArrayList<Menu>();

					do
					{
						Element ie = item.next();
						Menu i = new Menu();

						i.setText(ie.getName());
						i.setKey(ie.attributeValue(KEY) != null ? ie.attributeValue(KEY).charAt(0)
								: ' ');
						items.add(i);
					} while (item.hasNext());

					menu.setItems(items);
				}

				menus.add(menu);
			}

		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}

		return menus;
	}

	public Lucene getLucene()
	{
		Lucene lucene = new Lucene();

		Element root;

		try
		{
			root = getRoot();
			Element index = root.element("Lucene");
			
			lucene.setIndex(index.attributeValue(INDEX));
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}

		return lucene;
	}

	public void updateLucene(Lucene lucene)
	{

	}
}
