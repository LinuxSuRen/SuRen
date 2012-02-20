/**
 * 
 */
package org.suren.core.gui.component;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import org.suren.SuRenProperties;
import org.suren.core.gui.Body;
import org.suren.core.gui.CellRenderer;
import org.suren.core.gui.common.TextPopup;
import org.suren.core.gui.common.MissingComponentException;
import org.suren.core.model.SuRen;
import org.suren.core.msg.JTextAreaDest;
import org.suren.core.net.Host;
import org.suren.core.net.LAN;
import org.suren.core.net.MsgClient;
import org.suren.core.net.MsgServer;

/**
 * @author SuRen
 */
public class ChatPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long				serialVersionUID	= 4056807419603030194L;
	private static final String				NAME				= "Chat";
	private static final String				FORMAT				= "HH:mm:ss";
	private static final SimpleDateFormat	format				= new SimpleDateFormat(FORMAT);

	private List<Host>						hosts;
	private JList							hostList;
	private DefaultListModel				listModel;
	private JTextArea						msgEdit;
	private JTextArea						msgHis;
	private JPopupMenu						onLineMenu;
	private MsgServer						msgServer;
	private boolean							inited				= false;
	private boolean							scanning			= false;
	private JLabel							status;

	private static final MsgClient			sender				= new MsgClient();

	public ChatPanel() {
		this.setName(NAME);

		regListener();
	}

	private void init() throws MissingComponentException
	{
		if (inited) return;
		inited = true;

		SuRen suren = new SuRenProperties().getSuRen();
		this.setLayout(new BorderLayout());

		JPanel msgZone = new JPanel();
		msgZone.setLayout(new BorderLayout());

		JTextArea msg = message();
		JScrollPane scrollMsg = new JScrollPane(msg);

		msgZone.add(scrollMsg, BorderLayout.CENTER);
		msgZone.add(sender(), BorderLayout.SOUTH);

		JTextArea msgHistory = history();
		JScrollPane scrollMsgHis = new JScrollPane(msgHistory);

		JSplitPane splitPane = new JSplitPane();

		splitPane.setLeftComponent(scrollMsgHis);
		splitPane.setRightComponent(onLine());
		splitPane.setOneTouchExpandable(true);

		splitPane
				.setDividerLocation(new Long(Math.round(suren.getSize().getWidth())).intValue() * 7 / 10);
		splitPane.setResizeWeight(0.7);

		this.add(splitPane, BorderLayout.CENTER);
		this.add(msgZone, BorderLayout.SOUTH);

		lazyInit();

		this.updateUI();
	}

	private void regListener()
	{
		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e)
			{
				try
				{
					init();
				}
				catch (MissingComponentException e1)
				{
					e1.printStackTrace();
				}
				Body.showing = NAME;
			}

		});
	}

	private void lazyInit()
	{
		new Thread() {

			@Override
			public void run()
			{
			}

		}.start();
	}

	private JTextArea history() throws MissingComponentException
	{
		msgHis = new JTextArea();
		msgHis.setEditable(false);

		new TextPopup(msgHis);

		return msgHis;
	}

	private JTextArea message() throws MissingComponentException
	{
		msgEdit = new JTextArea();
		msgEdit.setRows(3);
		new TextPopup(msgEdit);

		msgEdit.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e)
			{
				// when typed key ctrl + enter
				if (e.getModifiers() == 2 && e.getKeyChar() == KeyEvent.VK_ENTER)
				{
					sendMsg();
				}
			}
		});

		new DropTarget(msgEdit, new DropTargetAdapter() {

			@SuppressWarnings("unchecked")
			public void drop(DropTargetDropEvent dtde)
			{
				dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
				Transferable transfer = dtde.getTransferable();

				try
				{

					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
					{
						List<File> files = (List<File>) transfer
								.getTransferData(DataFlavor.javaFileListFlavor);

						for (File file : files)
						{
							if (file.exists() && file.isFile())
							{
								msgEdit.append(file.getAbsolutePath());
							}
						}
					}
					else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor))
					{
						String text = (String) transfer.getTransferData(DataFlavor.stringFlavor);

						msgEdit.append(text);
					}
				}
				catch (UnsupportedFlavorException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		});

		return msgEdit;
	}

	private JPanel sender()
	{
		JPanel panel = new JPanel();

		JButton send = new JButton("Send");
		JButton clear = new JButton("Clear");
		status = new JLabel("status:");

		panel.add(status);
		panel.add(send);
		panel.add(clear);

		send.setDisplayedMnemonicIndex(0);
		clear.setDisplayedMnemonicIndex(0);

		send.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				sendMsg();
			}

		});

		clear.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				clearMsg(msgEdit);
				msgEdit.requestFocus();
			}

		});

		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.RIGHT);
		panel.setLayout(flow);

		return panel;
	}

	private void sendMsg()
	{
		Object[] objs = hostList.getSelectedValues();

		if (objs.length <= 0)
		{
			updateStatus("You hava not choose any host.");
			return;
		}

		for (Object obj : objs)
		{
			Host host = (Host) obj;

			sendMsg(host);
		}
	}

	private void sendMsg(final Host host)
	{
		new Thread() {

			public void run()
			{
				String msg = msgEdit.getText();

				if (!"".equals(msg))
				{
					msgHis.append("\n" + format.format(new Date()) + "\n\b" + msg);

					msgEdit.replaceRange("", 0, msg.length());
					msgEdit.moveCaretPosition(msgEdit.getText().length());
				}

				try
				{

					int port = new SuRenProperties().getNetWork().getPort();
					sender.sendMsg(msg, InetAddress.getByName(host.getName()), port);
				}
				catch (UnknownHostException e)
				{
					updateStatus(e.getMessage());
				}
				catch (IOException e)
				{
					updateStatus(e.getMessage());
				}
			}

		}.start();

		msgEdit.requestFocus();
	}

	private void clearMsg(JTextComponent jtext)
	{
		if ("".equals(jtext.getText())) return;

		jtext.selectAll();
		jtext.replaceSelection("");
	}

	private JPanel onLine()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		listModel = new DefaultListModel();
		hostList = new JList(listModel);

		JScrollPane listPane = new JScrollPane(hostList);
		panel.add(listPane);

		hostList.setCellRenderer(new CellRenderer());

		final ActionListener loadAction = new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				if (scanning)
				{
					updateStatus("Scanning is running...");
				}
				else
				{
					scan();
				}
			}
		};

		final ActionListener refreshAction = new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				if (hosts == null)
				{
					updateStatus("No sanning.");
					return;
				}

				Collections.sort(hosts);

				listModel.removeAllElements();

				for (Host host : hosts)
				{
					listModel.addElement(host);
				}
			}

		};

		final ActionListener startAction = new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				if (msgServer == null)
				{
					msgServer = new MsgServer();

					int port = new SuRenProperties().getNetWork().getPort();

					msgServer.init(new JTextAreaDest(msgHis), true, port);
				}
			}

		};

		final ActionListener stopAction = new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{

				if (msgServer != null)
				{
					msgServer.shutDown();
					msgServer = null;
				}
			}

		};

		hostList.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e)
			{

				if (e.getButton() == MouseEvent.BUTTON3)
				{
					if (onLineMenu == null)
					{
						onLineMenu = new JPopupMenu();
						JMenuItem load = new JMenuItem("Load");
						JMenuItem refresh = new JMenuItem("Refresh");
						JMenuItem start = new JMenuItem("Start Server");
						JMenuItem stop = new JMenuItem("Stop Server");

						onLineMenu.add(load);
						onLineMenu.add(refresh);
						onLineMenu.add(start);
						onLineMenu.add(stop);

						load.addActionListener(loadAction);
						refresh.addActionListener(refreshAction);
						start.addActionListener(startAction);
						stop.addActionListener(stopAction);
					}

					onLineMenu.show(hostList, e.getX(), e.getY());
				}
			}

		});

		return panel;
	}

	private void scan()
	{
		final LAN lan = new LAN();

		this.hosts = lan.getHost(1);

		updateStatus("Scanning will be started.");

		new Thread() {

			public void run()
			{
				float f = lan.getProgress();

				while (f != 1.0)
				{
					updateStatus("Scanning Progress : " + f * 100 + "% .");
					f = lan.getProgress();
				}

				updateStatus("Scanning end.");

				updateStatus("Found " + hosts.size() + " hosts.");
			}

		}.start();
	}

	private void updateStatus(String msg, int limit)
	{
		status.setText(msg.length() > limit ? msg.substring(0, limit) : msg);
		status.setToolTipText(msg);
	}

	public void updateStatus(String msg)
	{
		this.updateStatus(msg, 45);
	}
}
