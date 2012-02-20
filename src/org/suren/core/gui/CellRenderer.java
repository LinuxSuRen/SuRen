/**
 * 
 */
package org.suren.core.gui;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.suren.core.net.Host;

/**
 * @author SuRen
 */
public class CellRenderer extends JCheckBox implements ListCellRenderer
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2882230093964586788L;

	public Component getListCellRendererComponent(JList list, Object value, int index,
			boolean isSelected, boolean cellHasFocus)
	{

		Host host = (Host) value;

		this.setSelected(isSelected);
		this.setText(host.getName());
		this.setToolTipText(host.getIp());

		return this;
	}

}
