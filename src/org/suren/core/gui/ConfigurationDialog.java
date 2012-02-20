/**
 * Create Date: 2011-8-10<br>
 * File Name: ConfigurationDialog.java
 */
package org.suren.core.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.NetworkInterface;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.suren.SuRenProperties;
import org.suren.core.model.SuRen;
import org.suren.core.net.Host;
import org.suren.core.net.LocalHost;
import org.suren.util.cmd.StringUtil;

/**
 * @author SuRen<br>
 * Create Time: 04:08:28<br>
 */
public class ConfigurationDialog
{

	private static final String		TITLE	= "Configuration Dialog";
	public static final String[]	NICINFO	= { "Name", "DisplayName", "IPv4", "IPv6", "Mac", "MTU" };
	private JDialog					dialog;
	private SuRen					suren;
	private Host					host;

	public ConfigurationDialog(JFrame owner) {
		if (dialog != null) return;
		dialog = new JDialog(owner, TITLE);

		init(dialog);

		dialog.setLocation(owner.getLocation().x + 100, owner.getLocation().y + 100);
		dialog.setSize(500, 300);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);

		regListener(dialog);
	}

	private void regListener(JDialog dialog)
	{
		dialog.addWindowListener(new WindowAdapter() {

			public void windowClosed(WindowEvent e)
			{
				update();
			}

		});
	}

	/**
	 * update information into SuRenProperties
	 */
	private void update()
	{
		SuRenProperties properties = new SuRenProperties();

		if (suren != null)
		{
			properties.updateSuRen(suren);
		}

		if (host != null)
		{
			properties.updateNetwork(host);
		}
	}

	private void init(JDialog dialog)
	{
		JTabbedPane tab = new JTabbedPane();
		dialog.add(tab);

		netWork(tab);
		menu(tab);
		system(tab);
	}

	private void netWork(JTabbedPane pane)
	{
		JPanel root = new JPanel();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 2));
		panel.setBorder(new TitledBorder("NetWork Setting"));

		// about nic field.
		JComboBox nicList = new JComboBox();
		JLabel interfaces = new JLabel("Nic:");
		interfaces.setLabelFor(nicList);
		JPanel interfacesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		interfacesPanel.add(interfaces);
		interfacesPanel.add(nicList);

		nicList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				if (host == null) host = new Host();

				JComboBox nicList = (JComboBox) e.getSource();

				String eth = nicList.getSelectedItem().toString();
				host.setEth(eth);
			}
		});

		// about bind port field.
		JLabel bindLabel = new JLabel("Bind Port:");
		JTextField bindText = new JTextField(10);
		JPanel bindPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bindPanel.add(bindLabel);
		bindPanel.add(bindText);

		bindText.getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e)
			{
			}

			public void insertUpdate(DocumentEvent e)
			{
				if (host == null) host = new Host();

				Document doc = e.getDocument();
				try
				{

					host.setPort(Integer.parseInt(doc.getText(0, doc.getLength())));
				}
				catch (NumberFormatException e1)
				{
					e1.printStackTrace();
				}
				catch (BadLocationException e1)
				{
					e1.printStackTrace();
				}
			}

			public void changedUpdate(DocumentEvent e)
			{
			}
		});

		panel.add(interfacesPanel);
		panel.add(bindPanel);

		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		table.setEnabled(false);
		table.doLayout();

		netWorkInfo(nicList, model, bindText);

		root.setLayout(new BorderLayout());
		root.add(table, BorderLayout.CENTER);
		root.add(panel, BorderLayout.NORTH);
		pane.add(root, "NetWork");

		pane.addTab("NetWork", root);
	}

	private void netWorkInfo(final JComboBox nicList, final DefaultTableModel model,
			JTextField bindValue)
	{
		final LocalHost local = new LocalHost();
		Host host = local.getHost();
		final List<NetworkInterface> nics = host.getNic();

		for (NetworkInterface nic : nics)
		{
			nicList.addItem(nic.getName());
		}

		model.setRowCount(NICINFO.length);
		model.setColumnCount(2);
		for (int i = 0; i < NICINFO.length; i++)
		{
			model.setValueAt(NICINFO[i], i, 0);
		}

		nicList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				String selected = nicList.getSelectedItem().toString();

				Map<String, String> ipv4 = local.getIPv4(selected);

				updateInfo(model, ipv4);
			}

		});

		Host network = new SuRenProperties().getNetWork();
		String eth = network.getEth();
		if (!StringUtil.isEmpty(eth))
		{
			nicList.setSelectedItem(eth);
		}

		bindValue.setText(String.valueOf(network.getPort()));
	}

	private void updateInfo(DefaultTableModel model, Map<String, String> ipv4)
	{
		for (int i = 0; i < NICINFO.length; i++)
		{
			model.setValueAt(ipv4.get(NICINFO[i]), i, 1);
		}
	}

	private void menu(JTabbedPane pane)
	{
		JPanel root = new JPanel();
		root.setName("Menu Tree");

		pane.addTab(root.getName(), root);
	}

	private void system(JTabbedPane pane)
	{
		JPanel root = new JPanel();
		root.setLayout(new GridLayout(0, 1));
		root.setName("System");
		root.setBorder(new TitledBorder("System Infomation"));

		SuRen su = new SuRenProperties().getSuRen();

		// about the frame is or not resize.
		JCheckBox resize = new JCheckBox("Resize:", su.getResize());
		resize.setHorizontalTextPosition(SwingConstants.LEFT);

		resize.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				if (suren == null) suren = new SuRen();

				JCheckBox check = (JCheckBox) e.getSource();

				suren.setResize(check.isSelected());
			}
		});

		// about title field.
		JTextField titleText = new JTextField(su.getTitle(), 30);
		JLabel titleLabel = new JLabel("Title:");
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		titlePanel.add(titleLabel);
		titlePanel.add(titleText);

		titleText.getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e)
			{
			}

			public void insertUpdate(DocumentEvent e)
			{
				if (suren == null) suren = new SuRen();

				Document doc = e.getDocument();
				try
				{

					suren.setTitle(doc.getText(0, doc.getLength()));
				}
				catch (BadLocationException e1)
				{
					e1.printStackTrace();
				}
			}

			public void changedUpdate(DocumentEvent e)
			{
			}
		});

		root.add(resize);
		root.add(titlePanel);

		pane.addTab(root.getName(), root);
	}
}