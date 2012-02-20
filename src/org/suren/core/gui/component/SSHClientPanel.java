/**
 * 
 */
package org.suren.core.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.suren.core.gui.Body;
import org.suren.core.gui.common.MissingComponentException;
import org.suren.core.gui.common.TextPopup;
import org.suren.core.net.UserInfoSimple;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author SuRen
 */
public class SSHClientPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 6488836392995421074L;
	private static final String		NAME				= "SSHClient";
//	private static final String		USERNAME			= System.getProperty("user.name");
	private static final String[]	action				= { "Connect", "Disconnect" };

	private boolean					inited				= false;
	private JTextArea				cmdArea;
	private int						position;
	private JLabel					status;
	private JSch					jsch;
	private Session					session;
	private InputStream		is;
	private String suren;

	public SSHClientPanel() {
		this.setName(NAME);

		regListener();
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

	private void init() throws MissingComponentException
	{
		if (inited) return;
		inited = true;

		this.setLayout(new BorderLayout());

		cmdLineArea();

		if (cmdArea != null) this.add(new JScrollPane(cmdArea), BorderLayout.CENTER);

		JToolBar toolBar = new JToolBar();

		JLabel labelConnect = new JLabel("Host:");
		final JComboBox comboConnect = new JComboBox();
		JButton buttonAction = new JButton(action[0]);

		labelConnect.setLabelFor(comboConnect);

		toolBar.add(labelConnect);
		toolBar.add(comboConnect);
		toolBar.add(buttonAction);

		comboConnect.setEditable(true);
		comboConnect.addItem("root@192.168.10.223");
		// comboConnect.addItem(USERNAME + "@localhost");

		buttonAction.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0)
			{
				JButton source = (JButton) arg0.getSource();
				String cmd = arg0.getActionCommand();
				String hostStr = comboConnect.getSelectedItem().toString();

				if (hostStr == null)
				{
					return;
				}

				String[] hostArr = hostStr.split("@");

				if (hostArr.length != 2) return;

				if (action[0].equals(cmd))
				{
					// connect ssh server
					jsch = jsch == null ? new JSch() : jsch;
					try
					{
						session = jsch.getSession(hostArr[0], hostArr[1]);
						session.setUserInfo(new UserInfoSimple());
						session.connect();

						Channel channel = session.openChannel("shell");

						PrintStream ps = new PrintStream(new OutputStream() {

							public void write(int b) throws IOException
							{
							}

							public void write(byte[] b, int off, int len)
							{
								cmdArea.append(new String(b, off, len));
								cmdArea.setCaretPosition(cmdArea.getDocument().getLength());
							}

						});

						System.setOut(ps);
						System.setErr(ps);
						
						 is = new InputStream() {

							public int read() throws IOException
							{
								return 0;
							}
						};
						
//						System.setIn(is);

						channel.setOutputStream(System.out);
						channel.setInputStream(System.in);
						
						channel.connect();
						
						cmdArea.setEnabled(true);
						cmdArea.requestFocus();
						source.setText(action[1]);
					}
					catch (JSchException e)
					{
						e.printStackTrace();
					}
				}
				else if (action[1].equals(cmd))
				{
					// disconnect ssh server
					session.disconnect();
					cmdArea.setEnabled(false);
					source.setText(action[0]);
					comboConnect.requestFocus();
				}
			}

		});

		status = new JLabel("status:");

		this.add(toolBar, BorderLayout.NORTH);
		this.add(status, BorderLayout.SOUTH);

		cmdArea.requestFocus();

		this.updateUI();
	}

	private void cmdLineArea() throws MissingComponentException
	{
		cmdArea = new JTextArea();
		cmdArea.setEnabled(false);

		new TextPopup(cmdArea);

		cmdArea.addCaretListener(new CaretListener() {

			public void caretUpdate(CaretEvent e)
			{
				int dot = e.getDot();

				if (dot < position) cmdArea.setCaretPosition(position);
			}
		});

		cmdArea.addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e)
			{
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
				{
					suren = "ls -al\r";
					try
					{
						is.read(suren.getBytes(), 0, suren.length());
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
				}
			}

			public void keyReleased(KeyEvent e)
			{
			}

			public void keyPressed(KeyEvent e)
			{
			}
		});

	}
}
