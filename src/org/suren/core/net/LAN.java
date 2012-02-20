/**
 * 
 */
package org.suren.core.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.suren.SuRenProperties;

public class LAN
{

	private int[]				progress	= { 0, 0 };
	private List<Host>			hosts;

	private static final int	FROM		= 1;
	private static final int	TO			= 256;
	private static final int	DELAY		= 2000;
	private static final int	COUNT		= 10;

	public float getProgress()
	{
		return (float) progress[0] / progress[1];
	}

	public List<Host> getHost(int from, int to, int delay, int count)
	{
		hosts = new ArrayList<Host>();

		if (from < 0) from = 0;
		if (to > 255) to = 255;
		if (from > to)
		{
			int tmp = from;

			from = to;
			to = tmp;
		}
		if (count < 1) count = 1;

		final int timeout = delay < 0 ? 0 : delay;

		progress[1] = to - from + 1;

		int threads = progress[1] / count;
		threads = threads * count < progress[1] ? threads + 1 : threads;

		final int[] zone = new int[threads + 1];
		for (int i = 0; i < threads; i++)
		{
			zone[i] = from;
			from += count;
		}
		zone[threads] = to + 1;

		LocalHost local = new LocalHost();

		try
		{

			final String prefix = local.getIP().substring(0, local.getIP().lastIndexOf(".") + 1);
			if ("".equals(prefix)) return hosts;

			for (int i = 0; i < threads; i++)
			{

				final int index = i;

				new Thread() {

					public void run()
					{

						try
						{

							for (int j = zone[index]; j < zone[index + 1]; j++)
							{
								InetAddress inet;
								inet = InetAddress.getByName(prefix + j);
								boolean reachable = inet.isReachable(timeout);

								if (reachable && scanPort(inet.getHostAddress()))
								{
									Host host = new Host();

									host.setIp(inet.getHostAddress());
									host.setName(inet.getHostName());

									hosts.add(host);
								}

								progress[0]++;
							}
						}
						catch (UnknownHostException e)
						{
							e.printStackTrace();
						}
						catch (SocketException e)
						{
							e.printStackTrace();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}

				}.start();

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return hosts;
	}

	public List<Host> getHost(int from, int to, int delay)
	{
		return getHost(from, to, delay, COUNT);
	}

	public List<Host> getHost(int from, int to)
	{
		return getHost(from, to, DELAY, COUNT);
	}

	public List<Host> getHost(int count)
	{
		return getHost(FROM, TO, DELAY, count);
	}

	public List<Host> getHost()
	{
		return getHost(FROM, TO, DELAY, COUNT);
	}

	public boolean scanPort(String host, int port)
	{
		try
		{
			new Socket(host, port).getInputStream();
		}
		catch (Exception e)
		{
			return false;
		}

		return true;
	}

	public boolean scanPort(String host)
	{
		int port = new SuRenProperties().getNetWork().getPort();

		return scanPort(host, port);
	}

	public void analyze()
	{
	}

	public void saveAnalysis()
	{
	}

	public void statistic()
	{
	}

	public void sendBack()
	{
	}

}
