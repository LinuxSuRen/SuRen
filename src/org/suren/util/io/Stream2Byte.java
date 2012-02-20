package org.suren.util.io;

public class Stream2Byte
{

	public static byte[] toByte(java.io.InputStream is)
	{
		byte[] result = new byte[0];
		if (is == null) return result;

		byte[] src = new byte[1024];
		int len = -1;

		try
		{
			while ((len = is.read(src)) != -1)
			{
				byte[] tmp = new byte[len + result.length];

				System.arraycopy(result, 0, tmp, 0, result.length);
				System.arraycopy(src, 0, tmp, result.length, len);

				result = new byte[tmp.length];
				System.arraycopy(tmp, 0, result, 0, tmp.length);
			}
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
		}

		return result;
	}
}
