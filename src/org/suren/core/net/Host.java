/**
 * 
 */
package org.suren.core.net;

import java.net.NetworkInterface;
import java.util.List;
import java.util.Map;

import org.suren.util.net.InetUtil;

/**
 * @author SuRen
 */
public class Host implements Comparable<Object>
{

	private String							name;

	private List<NetworkInterface>			nic;

	private Map<String, NetworkInterface>	nics;

	// default ip addr.
	private String							ip;

	// default nic
	private String							eth;

	// default port
	private Integer							port;

	private String							os;

	private List<Integer>					ports;

	public Host() {

	}

	public Host(String ip) {
		this.setIp(ip);
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the nic
	 */
	public List<NetworkInterface> getNic()
	{
		return nic;
	}

	/**
	 * @param nic
	 *            the nic to set
	 */
	public void setNic(List<NetworkInterface> nic)
	{
		this.nic = nic;
	}

	/**
	 * @return the nics
	 */
	public Map<String, NetworkInterface> getNics()
	{
		return nics;
	}

	/**
	 * @param nics
	 *            the nics to set
	 */
	public void setNics(Map<String, NetworkInterface> nics)
	{
		this.nics = nics;
	}

	/**
	 * @return the ip
	 */
	public String getIp()
	{
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}

	/**
	 * @return the eth
	 */
	public String getEth()
	{
		return eth;
	}

	/**
	 * @param eth
	 *            the eth to set
	 */
	public void setEth(String eth)
	{
		this.eth = eth;
	}

	/**
	 * @return the port
	 */
	public Integer getPort()
	{
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(Integer port)
	{
		this.port = port;
	}

	/**
	 * @return the os
	 */
	public String getOs()
	{
		return os;
	}

	/**
	 * @param os
	 *            the os to set
	 */
	public void setOs(String os)
	{
		this.os = os;
	}

	/**
	 * @return the ports
	 */
	public List<Integer> getPorts()
	{
		return ports;
	}

	/**
	 * @param ports
	 *            the ports to set
	 */
	public void setPorts(List<Integer> ports)
	{
		this.ports = ports;
	}

	public int compareTo(Object o)
	{
		Host host = (Host) o;

		return InetUtil.isSmaller(host.getIp(), this.getIp());
	}
}
