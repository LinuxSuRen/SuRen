package org.suren.util.common;

public class NumberUtil
{

	/**
	 * desc
	 * 
	 * @param time
	 *            ms
	 * @return format time string
	 */
	public static String timeToFit(Long time)
	{
		if (time == null) return "";

		float r = time / 1000;

		if (r > 1)
		{
			if (r / 60 > 1) return (r / 60) + "min";
			return r + "s";
		}

		return time + "ms";
	}

	public static String bitToFit(Long bit)
	{
		if (bit == null) return "";

		float r = bit / 1024f;

		if (r > 1)
		{
			if (r / 1024f > 1) return (r / 1024f) + "M";
			return r + "K";
		}

		return bit + "B";
	}

}