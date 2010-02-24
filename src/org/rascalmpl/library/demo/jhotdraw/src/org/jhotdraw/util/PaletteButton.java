/*
 * @(#)PaletteButton.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import javax.swing.*;
import java.awt.event.*;

/**
 * A palette button is a three state button. The states are normal
 * pressed and selected. It uses to the palette listener interface
 * to notify about state changes.
 *
 * @see PaletteListener
 * @see PaletteLayout
 *
 * @version <$CURRENT_VERSION$>
*/
public abstract class PaletteButton
				extends JButton
				implements MouseListener, MouseMotionListener {

	protected static final int NORMAL = 1;
	protected static final int PRESSED = 2;
	protected static final int SELECTED = 3;

	private int             fState;
	private int             fOldState;

	private PaletteListener  fListener;

	/**
	 * Constructs a PaletteButton.
	 * @param listener the listener to be notified.
	 */
	public PaletteButton(PaletteListener listener) {
		fListener = listener;
		fState = fOldState = NORMAL;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public Object value() {
		return null;
	}

	public String name() {
		return "";
	}

	public void reset() {
		if (isEnabled()) {
			fState = NORMAL;
			setSelected(false);
			repaint();
		}
	}

	public void select() {
		if (isEnabled()) {
			fState = SELECTED;
			setSelected(true);
			repaint();
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isEnabled()) {
			fOldState = fState;
			fState = PRESSED;
			repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (isEnabled()) {
			if (contains(e.getX(),e.getY())) {
				fState = PRESSED;
			}
			else {
				fState = fOldState;
			}
			repaint();
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isEnabled()) {
			fState = fOldState;
			repaint();
			if (contains(e.getX(),e.getY())) {
				fListener.paletteUserSelected(this);
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		fListener.paletteUserOver(this, true);
	}

	public void mouseExited(MouseEvent e) {
		if (fState == PRESSED) {
			// JDK1.1 on MS-Windows sometimes looses mouse released
			mouseDragged(e);
		}
		fListener.paletteUserOver(this, false);
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}

//  Not necessary anymore in JFC due to the support of Icons in JButton
/*
	public abstract void paintBackground(Graphics g);
	public abstract void paintNormal(Graphics g);
	public abstract void paintPressed(Graphics g);
	public abstract void paintSelected(Graphics g);

	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		paintBackground(g);

		switch (fState) {
		case PRESSED:
			paintPressed(g);
			break;
		case SELECTED:
			paintSelected(g);
			break;
		case NORMAL:
		default:
			paintNormal(g);
			break;
		}
	}
*/
}
