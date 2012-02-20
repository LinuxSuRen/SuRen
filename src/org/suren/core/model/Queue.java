package org.suren.core.model;

import java.io.File;

import javax.swing.JTable;

import org.suren.core.os.UnixFile;

public class Queue
{

	private UnixFile		unixFile;
	private File			file;
	private TransferViewID	viewID;
	private String			dest;
	private JTable			table;

	public Queue(TransferViewID viewID, JTable table, UnixFile unixFile, String dest) {
		setViewID(viewID);
		setTable(table);
		setUnixFile(unixFile);
		setDest(dest);
	}

	public Queue(TransferViewID viewID, JTable table, File file, String dest) {
		setViewID(viewID);
		setTable(table);
		setFile(file);
		setDest(dest);
	}

	public TransferViewID getViewID()
	{
		return viewID;
	}

	public void setViewID(TransferViewID viewID)
	{
		this.viewID = viewID;
	}

	public String getDest()
	{
		return dest;
	}

	public void setDest(String dest)
	{
		this.dest = dest;
	}

	public JTable getTable()
	{
		return table;
	}

	public void setTable(JTable table)
	{
		this.table = table;
	}

	public UnixFile getUnixFile()
	{
		return unixFile;
	}

	public void setUnixFile(UnixFile unixFile)
	{
		this.unixFile = unixFile;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}
}
