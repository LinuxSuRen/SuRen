/**
 * Create Date: 2012-2-22<br>
 * File Name: Child.java
 */
package org.suren.test.parent;

/**
 * @author Administrator<br>
 * Create Time: 02:02:23<br>
 */
public class Child extends Parent
{

	/* (non-Javadoc)
	 * @see org.suren.test.parent.Parent#test()
	 */
	@Override
	public void test()
	{
	}
	
	private String name;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
