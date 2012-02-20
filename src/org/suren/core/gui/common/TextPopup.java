/**
 * 
 */
package org.suren.core.gui.common;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * @author SuRen
 */
public class TextPopup extends SuRenJPopup
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1042038838346742311L;

	private JTextComponent		comp;

	public TextPopup(JComponent comp) throws MissingComponentException {
		super(comp, new String[] { "Select All", "Copy", "Cut", "Paste", "Clear" });
		this.comp = (JTextComponent) comp;
	}

	private void selectAll()
	{
		comp.selectAll();
	}

	private void copy()
	{
		comp.copy();
	}

	private void cut()
	{
		comp.cut();
	}

	private void paste()
	{
		comp.paste();
	}

	private void clear()
	{
		selectAll();
		comp.replaceSelection("");
	}

	@Override
	public void MenuAction(ActionEvent e)
	{
		String cmd = e.getActionCommand();

		if (ITEMS[0].equals(cmd))
		{
			selectAll();
		}
		else if (ITEMS[1].equals(cmd))
		{
			copy();
		}
		else if (ITEMS[2].equals(cmd))
		{
			cut();
		}
		else if (ITEMS[3].equals(cmd))
		{
			paste();
		}
		else if (ITEMS[4].equals(cmd))
		{
			clear();
		}
	}

}
