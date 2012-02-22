package org.suren.test.wait;

public class TestAccount{
	
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		
		Account account = new Account("SuRen", 100);
		
		int total = 30;
		
		for(int i = 0; i < total; i++)
		{
			TestAccount_1 a = new TestAccount_1(account);
			new Thread(a).start();
		}
		
		for(int i = 0; i < total; i++)
		{
			TestAccount_1 a = new TestAccount_1(account);
			new Thread(a).start();
		}
		
		for(int i = 0; i < total; i++)
		{
			TestAccount_1 a = new TestAccount_1(account);
			new Thread(a).start();
		}
		
		Thread.currentThread().wait();
		
		Thread.yield();
	}
}

class TestAccount_1 implements Runnable{
	
	private Account account;
	
	public TestAccount_1(Account account)
	{
		this.account = account;
	}

	@Override
	public void run() {
		account.deposit(30);
	}
}

