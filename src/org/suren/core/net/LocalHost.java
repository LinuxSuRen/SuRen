/**
 * 
 */
package org.suren.core.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.suren.core.gui.ConfigurationDialog;
import org.suren.util.net.InetUtil;

/**
 * @author SuRen
 */
public class LocalHost
{

	private Host	host;

	public LocalHost() {
		try
		{
			InetAddress inet = InetAddress.getLocalHost();

			host = new Host();
			host.setName(inet.getHostName());

			// nic info
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			List<NetworkInterface> nic = new ArrayList<NetworkInterface>();
			Map<String, NetworkInterface> nics = new HashMap<String, NetworkInterface>();

			while (interfaces.hasMoreElements())
			{
				NetworkInterface face = interfaces.nextElement();

				nic.add(face);
				nics.put(face.getName(), face);
			}

			host.setNics(nics);
			host.setNic(nic);

			// os info
			Properties pro = System.getProperties();
			host.setOs(pro.getProperty("os.name"));
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @return the host
	 */
	public Host getHost()
	{
		return host;
	}

	public String getName()
	{
		return host.getName();
	}

	public String getIP() throws SocketException
	{
		String ip = null;

		List<NetworkInterface> nic = host.getNic();

		if (nic.size() > 0)
		{
			for (NetworkInterface net : nic)
			{
				if (!net.isLoopback())
				{
					Enumeration<InetAddress> addr = net.getInetAddresses();

					while (addr.hasMoreElements())
					{
						InetAddress add = addr.nextElement();
						ip = add.getHostAddress();

						if (ip.length() > 15) continue;
						break;
					}
				}
			}
		}

		ip = "192.168.10.26";

		return ip;
	}

	public Map<String, String> getIPv4(String name)
	{
		NetworkInterface nic = host.getNics().get(name);
		Map<String, String> info = new HashMap<String, String>();
		String[] nicInfo = ConfigurationDialog.NICINFO;

		info.put(nicInfo[0], nic.getName());
		info.put(nicInfo[1], nic.getDisplayName());

		Enumeration<InetAddress> addrs = nic.getInetAddresses();
		String ipv4 = "";
		while (addrs.hasMoreElements())
		{
			ipv4 += addrs.nextElement().getHostAddress() + ",";
		}
		info.put(nicInfo[2], ipv4.length() > 0 ? ipv4.substring(0, ipv4.length() - 1) : ipv4);
		info.put(nicInfo[3], "");
		try
		{
			info.put(nicInfo[4], InetUtil.toMAC(nic.getHardwareAddress()));
			info.put(nicInfo[5], String.valueOf(nic.getMTU()));
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}

		return info;
	}
}
