package org.suren.core.gui.common;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.suren.core.gui.component.SSHFileTransferPanel;
import org.suren.core.net.SftpProgressMonitorSuRen;
import org.suren.util.ClipboardUtil;
import org.suren.util.swing.JTableUtil;

public class TransferPopup extends SuRenJPopup
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8525660719233863615L;

	public TransferPopup(JComponent comp) throws MissingComponentException {
		super(comp, new String[] { "Del Compelet", "Del Select", "Copy Line" });
	}

	@Override
	public void MenuAction(ActionEvent e)
	{
		String cmd = e.getActionCommand();

		JTable table = (JTable) this.comp;

		if (ITEMS[0].equals(cmd))
		{
			JTableUtil.deleteRow(table, SSHFileTransferPanel.TRANSFER[5],
					SftpProgressMonitorSuRen.done);
		}
		else if (ITEMS[1].equals(cmd))
		{
			JTableUtil.deleteSelectRow(table);
		}
		else if (ITEMS[2].equals(cmd))
		{
			ClipboardUtil.send2Clipboard(JTableUtil.getRowValue(table));
		}
	}

}
