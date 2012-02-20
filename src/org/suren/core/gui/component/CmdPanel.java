/**
 * 
 */
package org.suren.core.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import org.suren.core.gui.Body;
import org.suren.core.gui.common.TextPopup;
import org.suren.core.gui.common.MissingComponentException;
import org.suren.core.gui.common.RowHeaderView;

/**
 * @author SuRen
 */
public class CmdPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6907665421404349670L;
	private static final String	KILL				= "kill";
	private static final String	NAME				= "Cmd";

	private JTextField			line;
	private JTextArea			textArea;
	private Process				process;
	private boolean				inited				= false;

	public CmdPanel() {
		this.setName(NAME);

		regListener();
	}

	private void init() throws MissingComponentException
	{
		if (inited) return;
		inited = true;

		this.setLayout(new BorderLayout());

		textArea = message();
		JScrollPane scrollPane = new JScrollPane(textArea);

		scrollPane.setRowHeaderView(new RowHeaderView(textArea));
		scrollPane.setAutoscrolls(true);

		this.add(scrollPane, BorderLayout.CENTER);
		this.add(cmdLine(), BorderLayout.NORTH);

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

	private JTextArea message() throws MissingComponentException
	{
		JTextArea msg = new JTextArea();

		msg.setEditable(false);
		new TextPopup(msg);

		return msg;
	}

	private JPanel cmdLine() throws MissingComponentException
	{
		JPanel cmdLine = new JPanel();
		cmdLine.setLayout(new BorderLayout());

		JButton kill = new JButton(KILL);
		line = new JTextField();

		kill.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e)
			{
				if (process != null) process.destroy();
			}

		});

		line.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent e)
			{

				// when typed key enter
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				{
					new Thread() {

						public void run()
						{
							execute();
						}

					}.start();
				}
			}

		});

		cmdLine.add(line, BorderLayout.CENTER);
		cmdLine.add(kill, BorderLayout.EAST);

		new TextPopup(line);

		return cmdLine;
	}

	/**
	 * @param area
	 * @param cmd
	 */
	private void execute()
	{
		String cmd = line.getText();

		try
		{
			line.setEditable(false);

			process = Runtime.getRuntime().exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			textArea.append("\n" + cmd + "\n");

			String str = null;
			while ((str = reader.readLine()) != null)
			{
				textArea.append(str + "\n");
				textArea.moveCaretPosition(textArea.getText().length());

				if (textArea.getLineCount() > 150)
				{
					textArea.replaceRange("", 0, textArea.getLineStartOffset(1));
				}
			}

			reader.close();
			line.setEditable(true);
		}
		catch (IOException e)
		{
			textArea.append("\n" + e.getMessage());
		}
		catch (IllegalArgumentException e)
		{
			textArea.append("\n" + e.getMessage());
		}
		catch (BadLocationException e)
		{
			textArea.append("\n" + e.getMessage());
		}
		finally
		{
			line.setEditable(true);
		}
	}
}
