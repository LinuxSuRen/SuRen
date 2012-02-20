/**
 * 
 */
package org.suren.core.gui;

import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.suren.SuRenProperties;
import org.suren.core.gui.component.ChatPanel;
import org.suren.core.gui.component.CmdPanel;
import org.suren.core.gui.component.PaintPanel;
import org.suren.core.gui.component.SSHClientPanel;
import org.suren.core.gui.component.SSHFileTransferPanel;
import org.suren.core.gui.component.SearcherPanel;
import org.suren.core.model.Menu;
import org.suren.core.model.SuRen;
import org.suren.main.StartUp;
import org.suren.util.io.Stream2Byte;

/**
 * @author SuRen<br>
 * Create Time: 04:08:12<br>
 */
public class Body extends JFrame implements WindowListener
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 6601479377621989758L;

	private static final boolean	VISIBLE				= true;
	private List<JPanel>			items;
	private CardLayout				layout;
	public static JPanel			rootPanel;
	private SystemTray				tray;

	public static boolean			saveState			= true;
	public static String			showing;

	public Body() {
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}

		init();
	}

	public List<JPanel> getItems()
	{
		return items;
	}

	/**
	 * @return
	 */
	private void init()
	{
		rootPanel = new JPanel();
		rootPanel.setBackground(Color.pink);

		layout = new CardLayout();
		rootPanel.setLayout(layout);
		this.add(rootPanel);

		SuRen suren = StartUp.properties.getSuRen();

		compose(suren);

		Image icon = Toolkit.getDefaultToolkit().createImage(image(suren.getIconImage()));

		this.setIconImage(icon);
		this.setSize(suren.getSize());
		this.setResizable(suren.getResize());
		this.setLocation(suren.getLocation());
		this.setTitle(suren.getTitle());
		this.setVisible(VISIBLE);
		this.addWindowListener(this);
	}

	private byte[] image(String name)
	{
		InputStream is = null;

		if (StartUp.jarModel)
		{
			JarFile jarFile = StartUp.jarFile;

			try
			{
				ZipEntry entry = jarFile.getEntry(name);

				if (entry != null) is = jarFile.getInputStream(entry);

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				is = new java.io.FileInputStream(new File(StartUp.root.getAbsolutePath()
						+ File.separator + name));
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}

		return Stream2Byte.toByte(is);
	}

	private void compose(SuRen suren)
	{
		CmdPanel cmd = new CmdPanel();
		ChatPanel chat = new ChatPanel();
		PaintPanel paint = new PaintPanel();
		SSHClientPanel sshClient = new SSHClientPanel();
		SSHFileTransferPanel fileTransfer = new SSHFileTransferPanel();
		SearcherPanel searcher = new SearcherPanel();

		items = new ArrayList<JPanel>();
		items.add(paint);
		items.add(sshClient);
		items.add(chat);
		items.add(cmd);
		items.add(fileTransfer);
		items.add(searcher);

		for (JPanel p : items)
		{
			rootPanel.add(p, p.getName());
		}

		layout.show(rootPanel, suren.getShow());

		menu();

		try
		{
			sysTray();
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	private void menu()
	{
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		SuRenProperties suren = new SuRenProperties();
		List<Menu> menus = suren.getMenus();

		for (Menu menu : menus)
		{
			JMenu jmenu = new JMenu();

			jmenu.setText(menu.getText());
			jmenu.setMnemonic(menu.getKey());

			List<Menu> items = menu.getItems();
			if (items != null)
			{
				for (Menu item : items)
				{
					JMenuItem jitem = new JMenuItem();

					jitem.setText(item.getText());
					jitem.setAccelerator(KeyStroke.getKeyStroke(item.getKey()));

					jmenu.add(jitem);
				}
			}

			menuBar.add(jmenu);
		}

		menuAction();
	}

	private void sysTray() throws AWTException
	{
		boolean supported = SystemTray.isSupported();

		if (!supported) return;

		tray = SystemTray.getSystemTray();

		SuRen suren = StartUp.properties.getSuRen();

		TrayIcon icon = new TrayIcon(new ImageIcon(image(suren.getTrayIcon())).getImage(),
				this.getTitle());
		final JFrame frame = this;

		tray.add(icon);

		icon.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e)
			{

				if (e.getClickCount() == 2)
				{
					frame.setVisible(true);
					frame.setExtendedState(JFrame.NORMAL);
				}
			}
		});
	}

	private void menuAction()
	{
		JMenuBar menuBar = this.getJMenuBar();
		final CardLayout card = layout;
		final JPanel parent = rootPanel;
		final Body frame = this;

		JMenu file = menuBar.getMenu(0);
		file.getItem(0).addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				Body.saveState = false;

				try
				{
					new SuRenProperties().init();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				frame.dispose();
			}
		});
		file.getItem(1).addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				new ConfigurationDialog(frame);
			}

		});

		JMenu window = menuBar.getMenu(1);
		int menuItems1 = window.getItemCount();
		for (int i = 0; i < menuItems1; i++)
		{
			window.getItem(i).addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					card.show(parent, e.getActionCommand());
				}

			});
		}

	}

	public void windowOpened(WindowEvent e)
	{
	}

	public void windowClosing(WindowEvent e)
	{

		new Thread() {

			public void run()
			{
				exitSystem();
			}

		}.start();
	}

	public void windowClosed(WindowEvent e)
	{
		exitSystem();
	}

	public void windowIconified(WindowEvent e)
	{
		this.setVisible(false);
	}

	public void windowDeiconified(WindowEvent e)
	{
	}

	public void windowActivated(WindowEvent e)
	{
	}

	public void windowDeactivated(WindowEvent e)
	{
	}

	private void exitSystem()
	{
		if (saveState) saveState();
		System.exit(0);
	}

	private void saveState()
	{
		SuRen suren = new SuRen();

		suren.setLocation(this.getLocation());
		suren.setSize(this.getSize());
		suren.setShow(showing);

		new SuRenProperties().updateSuRen(suren);
	}
}
