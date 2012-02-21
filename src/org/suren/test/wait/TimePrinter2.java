package org.suren.test.wait;

public class TimePrinter2 implements Runnable {
	
	private int pauseTime;
	private String name;

	public TimePrinter2(int pauseTime, String name)
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
				System.out.println(name + ":" + new java.util.Date(System.currentTimeMillis()));
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
		new Thread(new TimePrinter2(1000, "Fast guy.")).start();
		
		new Thread(new TimePrinter2(3000, "Slow guy.")).start();
	}

}
