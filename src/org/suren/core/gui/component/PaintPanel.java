/**
 * 
 */
package org.suren.core.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.suren.core.gui.Body;

/**
 * @author SuRen
 */
public class PaintPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7767513926435293395L;
	private static final String	NAME				= "Paint";
	private boolean				inited				= false;
	private Point				point;
	private JLabel				label;

	public PaintPanel() {
		this.setName(NAME);

		label = new JLabel();
		label.setBackground(Color.PINK);

		this.setLayout(new BorderLayout());
		this.add(label, BorderLayout.CENTER);

		regListener();
	}

	private void init()
	{
		if (inited) return;
		inited = true;

		this.updateUI();
	}

	private void regListener()
	{
		label.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e)
			{
				point = e.getPoint();

				Graphics g = label.getGraphics();

				g.drawLine(point.x, point.y, point.x + 100, point.y);
				g.drawLine(point.x, point.y, point.x - 100, point.y);
				g.drawLine(point.x, point.y, point.x, point.y + 100);
				g.drawLine(point.x, point.y, point.x, point.y - 100);
			}

			public void mouseDragged(MouseEvent e)
			{
				Graphics g = label.getGraphics();

				if (point == null) return;

				g.drawLine(point.x, point.y, e.getX(), e.getY());
			}

			public void mouseReleased(MouseEvent e)
			{
				Graphics g = label.getGraphics();

				if (point == null) return;

				g.drawLine(point.x, point.y, e.getX(), e.getY());
			}

		});

		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e)
			{
				init();
				Body.showing = NAME;
			}

		});
	}
}
