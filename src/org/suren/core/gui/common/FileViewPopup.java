package org.suren.core.gui.common;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.suren.core.gui.Body;
import org.suren.core.gui.component.SSHFileTransferPanel;
import org.suren.core.net.Sftp;
import org.suren.core.os.UnixFile;
import org.suren.util.io.FileUtil;
import org.suren.util.swing.JTableUtil;

public class FileViewPopup extends SuRenJPopup
{

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 5646471972404647527L;

	private static final String[]	ITEMS				= { "Open", "Delete", "Rename", "Refresh" };
	private Desktop					desktop				= null;
	private JComponent				fileView;

	public FileViewPopup(JComponent comp, JComponent fileView) throws MissingComponentException {
		super(comp, ITEMS);

		this.fileView = fileView;
		if (Desktop.isDesktopSupported()) desktop = Desktop.getDesktop();
	}

	public void MenuAction(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		Object obj = JTableUtil.getValue((JTable) comp, comp.getName());
		if (!(obj instanceof java.io.File)) return;
		boolean unix = obj instanceof UnixFile;

		if (ITEMS[0].equals(cmd))
		{
			java.io.File file = null;

			if (unix)
			{
				file = ((UnixFile) obj).isDir() ? null : new Sftp().getFile(((UnixFile) obj)
						.getRealPath());
				if (file != null) file.deleteOnExit();
			}
			else
			{
				file = (java.io.File) obj;
			}

			if (file != null) try
			{
				if (desktop != null) desktop.open((file));
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		else if (ITEMS[1].equals(cmd))
		{
			if (JOptionPane.OK_OPTION != JOptionPane.showConfirmDialog(Body.rootPanel,
					"Sure to del \"" + obj + "\" ?", "Warning", JOptionPane.OK_CANCEL_OPTION))
			{
				return;
			}
			else if (unix)
			{
				new Sftp().del(((UnixFile) obj).getRealPath());
				refresh((byte) 1);
			}
			else
			{
				FileUtil.delFile((java.io.File) obj);
				refresh((byte) 0);
			}
		}
		else if (ITEMS[2].equals(cmd))
		{
			String newPath = JOptionPane.showInputDialog(Body.rootPanel, "Rename",
					((java.io.File) obj).getName());

			if (newPath == null)
			{
				return;
			}
			else if (unix)
			{
				String realPath = ((UnixFile) obj).getRealPath();
				new Sftp().rename(realPath, realPath.substring(0, realPath.lastIndexOf("/") + 1)
						+ newPath);
				refresh((byte) 1);
			}
			else
			{
				java.io.File file = (java.io.File) obj;
				String abs = file.getAbsolutePath();

				file.renameTo(new java.io.File(
						abs.substring(0, abs.lastIndexOf(File.separator) + 1) + newPath));
				refresh((byte) 0);
			}
		}
		else if (ITEMS[3].equals(cmd))
		{
			refresh((byte) 2);
		}
	}

	private void refresh(byte flag)
	{
		if (fileView instanceof SSHFileTransferPanel)
		{
			switch (flag)
			{
				case 0:
					((SSHFileTransferPanel) fileView).refreshLocal();
					break;
				case 1:
					((SSHFileTransferPanel) fileView).refreshRemote();
					break;
				default:
					((SSHFileTransferPanel) fileView).refreshLocal().refreshRemote();
					break;
			}
		}
	}

}
