package org.suren.test.wait;

public class TestAccount{
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		Account account = new Account("SuRen", 100);
		
		for(int i = 0; i < 5; i++)
		{
			TestAccount_1 a = new TestAccount_1(account);
			new Thread(a).start();
//			new Thread(new TestAccount()).start();
		}
		
		Thread.sleep(3000);
		
		System.out.println(account.getAmount() + "=======");
	}
}

class TestAccount_1 implements Runnable{
	
	private Account account;
	private static float total1 = 0;
	
	public TestAccount_1(Account account)
	{
		this.account = account;
	}

	@Override
	public void run() {
		if(account.deposit(30));
		{
			total1 += 30;
		}
		
		System.out.println(total1 + "two" + account.getAmount());
	}
}
