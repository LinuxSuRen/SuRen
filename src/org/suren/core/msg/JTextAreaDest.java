/**
 * 
 */
package org.suren.core.msg;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

/**
 * @author SuRen
 */
public class JTextAreaDest implements Destination
{

	/**
	 * 
	 */
	private static final long						serialVersionUID	= 7268063086989345345L;
	private static final String						FORMAT				= "HH:mm:ss";
	private static final java.text.SimpleDateFormat	format				= new java.text.SimpleDateFormat(
																				FORMAT);

	private JTextArea								dest;

	public JTextAreaDest(JTextArea dest) {
		this.dest = dest;
	}

	public void put(String msg)
	{

		try
		{
			put(msg, InetAddress.getLocalHost());
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

	public void put(String msg, InetAddress addr)
	{

		StringBuffer buffer = new StringBuffer();

		buffer.append("\n");
		buffer.append(addr.getHostAddress());
		buffer.append(" : ");
		buffer.append(format.format(new java.util.Date()));
		buffer.append("\n");
		buffer.append(msg);

		dest.append(buffer.toString());
		dest.moveCaretPosition(dest.getText().length());
	}

}
