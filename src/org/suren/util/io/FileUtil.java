package org.suren.util.io;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.tools.Tool;

public class FileUtil
{
	public static final String[]	suffix		= { ".txt", ".java", ".xml", ".html", ".jsp", ".js" };
	public static final int			TEXT		= 1;

	// 计算机
	public static final String		COMPUTER	= "::{20D04FE0-3AEA-1069-A2D8-08002B30309D}";
	// 网络
	public static final String		NETWORK		= "::{F02C1A0D-BE21-4350-88B0-7367FC96EF3C}";
	// 库(Windows 7)
	public static final String		LIBRARY		= "::{031E4825-7B94-4DC3-B131-E946B44C8DD5}";

	public static void delFile(String path)
	{
		if (path == null) return;

		delFile(new java.io.File(path));
	}

	public static void delFile(java.io.File file)
	{
		if (file == null) return;

		if (file.isDirectory())
		{
			java.io.File[] files = file.listFiles();

			for (java.io.File f : files)
				delFile(f);

			file.delete();
		}
		else
		{
			file.delete();
		}
	}

	public static String allContent(java.io.File file, String charset)
	{
		StringBuffer buffer = new StringBuffer();

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
					file), charset));

			String line = reader.readLine();

			while (line != null)
			{
				buffer.append(line);

				line = reader.readLine();
			}

			reader.close();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static String allContent(java.io.File file)
	{
		String charset = "utf-8";
		return allContent(file, charset);
	}

	public static List<File> allFile(File dir, int mode)
	{
		File[] files = dir.listFiles();
		List<File> fileList = new ArrayList<File>();

		if (files == null) return fileList;

		for (File file : files)
		{
			if (file.isFile())
			{
				if (mode == TEXT && isText(file.getName()))
				{
					fileList.add(file);
				}
			}
			else if (file.isDirectory())
			{
				fileList.addAll(allFile(file, mode));
			}
		}

		return fileList;
	}

	public static boolean isText(String name)
	{
		boolean flag = false;

		for (String suf : suffix)
		{
			if (name.toLowerCase().endsWith(suf))
			{
				flag = true;
				break;
			}
		}

		return flag;
	}

	/**
	 * 打开特殊的文件夹
	 * 
	 * @param shell
	 */
	public static boolean open(String shell)
	{
		try
		{
			if (COMPUTER.equals(shell))
			{
				java.lang.Runtime.getRuntime().exec("explorer");
			}
			else if (NETWORK.equals(shell))
			{
//				java.lang.Runtime.getRuntime().exec("explorer");
			}
			else if (LIBRARY.equals(shell))
			{
				java.lang.Runtime.getRuntime().exec("explorer");
			}
			else
			{
				return false;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
}
