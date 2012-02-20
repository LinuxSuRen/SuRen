/**
 * Create Date: 2011-8-18<br>
 * File Name: SearcherPanel.java
 */
package org.suren.core.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.suren.core.gui.Body;
import org.suren.core.model.Document;
import org.suren.lucene.Creator;
import org.suren.lucene.CreatorException;
import org.suren.lucene.Searcher;

/**
 * @author SuRen<br>
 *         Create Time: 10:08:58<br>
 */
public class SearcherPanel extends JPanel
{

	private static final long	serialVersionUID	= -6272585302014956634L;

	private static final String	NAME				= "Seacher";
	private boolean				inited				= false;
	private JTable				table;
	private JTextField			text;

	public SearcherPanel() {
		this.setName(NAME);

		regListener();
	}

	private void init()
	{
		if (inited) return;
		inited = true;

		this.setLayout(new BorderLayout());

		table = new JTable();
		JScrollPane scroll = new JScrollPane(table);

		
		JToolBar toolBar = new JToolBar();
		text = new JTextField();
		JButton create = new JButton("Create");
		
		toolBar.add(text);
		toolBar.add(fsRoot(create));
		toolBar.add(create);
		
		JToolBar statusBar = new JToolBar();
		JLabel status = new JLabel("status:");
		
		statusBar.add(status);
		
		this.add(toolBar, BorderLayout.NORTH);
		this.add(scroll, BorderLayout.CENTER);
		this.add(statusBar, BorderLayout.SOUTH);

		addAction();
	}

	private void regListener()
	{
		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e)
			{
				init();
				Body.showing = NAME;
			}

		});
	}

	private void addAction()
	{
		text.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Searcher searcher = new Searcher();

				try
				{
					List<Document> docs = searcher.search(e.getActionCommand());

					DefaultTableModel model = (DefaultTableModel) table.getModel();

					model.setRowCount(docs.size());
					model.setColumnCount(1);

					for (int i = 0; i < docs.size(); i++)
					{
						model.setValueAt(docs.get(i).getPath(), i, 0);
					}
				}
				catch (CreatorException e1)
				{
					e1.printStackTrace();
				}
				catch (CorruptIndexException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				catch (ParseException e1)
				{
					e1.printStackTrace();
				}
			}
		});
	}
	
	private JComboBox fsRoot(JButton create)
	{
		final JComboBox root = new JComboBox(File.listRoots());
		
		create.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e)
			{
				File dir = (File) root.getSelectedItem();
				try
				{
					new Creator().createFor(dir);
				}
				catch (CreatorException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		
		return root;
	}

}
