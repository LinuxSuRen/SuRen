package org.suren.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil
{

	public static final String[] suffix = {".txt", ".java", ".xml", ".html", ".jsp", ".js"};
	public static final int TEXT = 1;
	
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
		
		if(files == null) return fileList;
		
		for(File file : files)
		{
			if(file.isFile())
			{
				if(mode == TEXT && isText(file.getName()))
				{
					fileList.add(file);
				}
			}
			else if(file.isDirectory())
			{
				fileList.addAll(allFile(file, mode));
			}
		}
		
		return fileList;
	}
	
	public static boolean isText(String name)
	{
		boolean flag = false;
		
		for(String suf : suffix)
		{
			if(name.toLowerCase().endsWith(suf))
			{
				flag = true;
				break;
			}
		}
		
		return flag;
	}
}
