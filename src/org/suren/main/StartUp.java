package org.suren.main;

import java.io.FileNotFoundException;

/**
 * @author SuRen<br>
 *         Create Time: 04:08:39<br>
 */
public class StartUp
{

	public static java.io.File				root		= null;
	public static boolean					jarModel	= true;
	public static boolean					console		= true;
	public static java.util.jar.JarFile		jarFile		= null;
	public static org.suren.SuRenProperties	properties	= null;
	public static String					outFile		= "../logs/suren.log";
	public static String					errorFile	= "../logs/error.log";
	public static String					configFile	= "../conf/suren.xml";

	static
	{
		try
		{
			root = new java.io.File(StartUp.class.getProtectionDomain().getCodeSource()
					.getLocation().getFile());

			if (root != null && root.exists())
			{
				if (root.isDirectory())
				{
					jarModel = false;
				}
				else if (root.isFile() && root.getName().endsWith(".jar"))
				{
					jarFile = new java.util.jar.JarFile(root);
				}
			}

			properties = new org.suren.SuRenProperties();
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            accept arguments.
	 */
	public static void main(String[] args)
	{
		new org.suren.core.gui.Body();
	}

	/**
	 * @return console value
	 * @throws FileNotFoundException
	 */
	public static boolean switchMsgIO() throws java.io.FileNotFoundException
	{
		if (console)
		{
			System.setOut(new java.io.PrintStream(outFile));
			System.setErr(new java.io.PrintStream(errorFile));
			console = false;
		}
		else
		{
			System.setOut(System.out);
			System.setErr(System.err);
			console = true;
		}

		return console;
	}

}
