package org.suren.core.model;

public class TransferViewID
{

	private long	id;
	private String	type;

	public TransferViewID(long id, String type) {
		setId(id);
		setType(type);
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof TransferViewID)
		{
			if (((TransferViewID) obj).id == id) return true;
			return false;
		}

		return false;
	}

	public long getId()
	{
		return id;
	}

	public String getType()
	{
		return type;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String toString()
	{
		return type;
	}

}
