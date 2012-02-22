package org.suren.test.wait;

public class Account {

	private String holderName;
	private float amount;
	public Account(String holderName, float amount)
	{
		this.holderName = holderName;
		this.amount = amount;
	}
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	public float getAmount() {
		return amount;
	}
	
	public
//	synchronized
	void deposit(float amt)
	{
		if(this.amount >= amt)
		{
			this.amount -= amt;
		}
		
		synchronized(this)
		{
			System.out.print("=========");
			System.out.println();
		}
	}
	
	public
//	synchronized
	boolean withdrow(float amt)
	{
		if(this.amount >= amt)
		{
			this.amount -= amt;
			return true;
		}
		else
		{
			return false;
		}
	}
}
