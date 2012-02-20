/**
 * 
 */
package org.suren.core.net;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author SuRen
 */
public class MsgClient
{

	public void sendMsg(String msg, InetAddress addr, int port) throws IOException
	{
		Socket socket = new Socket(addr, port);

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		writer.write(msg);

		writer.close();
		socket.close();
	}

}
