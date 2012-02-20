package org.suren.core.net;

import java.util.List;

import javax.swing.JTable;

import org.suren.core.gui.component.SSHFileTransferPanel;
import org.suren.core.model.Queue;
import org.suren.core.model.TransferViewID;
import org.suren.util.common.NumberUtil;
import org.suren.util.swing.JTableUtil;

import com.jcraft.jsch.SftpProgressMonitor;

public class SftpProgressMonitorSuRen implements SftpProgressMonitor
{

	private JTable			tab;
	private List<Queue>		queues;
	private long			start;
	private TransferViewID	viewID;
	private long			max		= 0;
	private long			count	= 0;
	private Sftp			sftp;
	public static String	done	= "Complete";

	public SftpProgressMonitorSuRen(List<Queue> queues, Sftp current) {
		this.queues = queues;
		this.sftp = current;
	}

	public void init(int op, String src, String dest, long max)
	{
		this.tab = queues.get(0).getTable();
		this.max = max;
		this.viewID = queues.get(0).getViewID();
		this.start = System.currentTimeMillis();

		JTableUtil.setValue(tab, SSHFileTransferPanel.TRANSFER[4],
				SSHFileTransferPanel.TRANSFER[0], viewID, NumberUtil.bitToFit(max));
	}

	public void end()
	{
		queues.remove(0);

		JTableUtil.setValue(tab, SSHFileTransferPanel.TRANSFER[5],
				SSHFileTransferPanel.TRANSFER[0], viewID, done);
		JTableUtil.setValue(tab, SSHFileTransferPanel.TRANSFER[7],
				SSHFileTransferPanel.TRANSFER[0], viewID,
				NumberUtil.timeToFit((System.currentTimeMillis() - start)));

		if (queues.size() > 0)
		{
			if (Sftp.types[0].equals(queues.get(0).getViewID().getType()))
			{
				sftp.put(null, null, null);
			}
			else if (Sftp.types[1].equals(queues.get(0).getViewID().getType()))
			{
				sftp.get(null, null, null);
			}
		}
	}

	public boolean count(final long c)
	{
		count += c;
		long currentTime = System.currentTimeMillis() - start;

		JTableUtil.setValue(tab, SSHFileTransferPanel.TRANSFER[5],
				SSHFileTransferPanel.TRANSFER[0], viewID, (count * 100 / max) + "%");
		JTableUtil.setValue(tab, SSHFileTransferPanel.TRANSFER[6],
				SSHFileTransferPanel.TRANSFER[0], viewID,
				NumberUtil.bitToFit((count / currentTime * 1000)) + "/s");
		JTableUtil.setValue(tab, SSHFileTransferPanel.TRANSFER[7],
				SSHFileTransferPanel.TRANSFER[0], viewID, NumberUtil.timeToFit(currentTime));

		return true;
	}
}
