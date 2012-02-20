package org.suren.util.net;

public class InetUtil
{

	/*
	 * if ipA is bigger than ipB, return true;
	 */
	public static int isBigger(String ipA, String ipB)
	{
		String[] a = ipA.split("\\.");
		String[] b = ipB.split("\\.");

		int i = 0;
		for (; i < 3; i++)
		{
			if (Integer.parseInt(a[i]) > Integer.parseInt(b[i])) return 1;
		}

		return Integer.parseInt(a[i]) - Integer.parseInt(b[i]);
	}

	public static int isSmaller(String ipA, String ipB)
	{
		return -isBigger(ipA, ipB);
	}

	public static String toMAC(byte[] mac)
	{
		if (mac == null) return "";
		String addr = "";

		for (byte b : mac)
			addr += (Integer.toHexString(b < 0 ? b + 256 : b) + ":");

		return addr.substring(0, addr.length() - 1);
	}

}
