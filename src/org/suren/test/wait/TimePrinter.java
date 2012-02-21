package org.suren.test.wait;

import java.util.Date;

public class TimePrinter extends Thread {
	
	private int pauseTime;
	private String name;
	
	public TimePrinter(int pauseTime, String name)
	{
		this.pauseTime = pauseTime;
		this.name = name;
	}

	@Override
	public void run() {
		while(true)
		{
			try
			{
				System.out.println(name + ":" + new Date(System.currentTimeMillis()));
				Thread.sleep(pauseTime);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TimePrinter tp1 = new TimePrinter(1000, "Fast Guy.");
		tp1.start();
		
		TimePrinter tp2 = new TimePrinter(3000, "Slow Guy.");
		tp2.start();
	}

}
