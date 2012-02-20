/**
 * 
 */
package org.suren.core.gui.common;

import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author SuRen
 */
public class RowHeaderView extends JTextArea
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6514271397300842864L;

	private JTextArea			area;

	public RowHeaderView(JTextArea area) {
		this.area = area;
		this.setColumns(2);
		this.setBackground(Color.PINK);
		this.setFocusable(false);

		init();
	}

	private void init()
	{
		final JTextArea header = this;

		area.getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(e);
			}

			public void insertUpdate(DocumentEvent e)
			{
				header.setText("");

				for (int i = 1; i <= area.getLineCount(); i++)
					header.append(i + "\n");
			}

			public void changedUpdate(DocumentEvent e)
			{
			}
		});
	}

}
