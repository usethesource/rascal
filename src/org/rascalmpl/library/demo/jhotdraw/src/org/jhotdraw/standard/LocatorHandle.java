/*
 * @(#)LocatorHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.Point;

import org.jhotdraw.framework.Cursor;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.Locator;

/**
 * A LocatorHandle implements a Handle by delegating the location requests to
 * a Locator object.
 *
 * @see LocatorHandle
 *
 * @version <$CURRENT_VERSION$>
 */
public class LocatorHandle extends AbstractHandle {

	private Locator       fLocator;

	/**
	 * Initializes the LocatorHandle with the given Locator.
	 */
	public LocatorHandle(Figure owner, Locator l) {
		super(owner);
		fLocator = l;
	}
	/**
	 * This should be cloned or it gives the receiver the opportunity to alter
	 * our internal behavior.
	 */
	public Locator getLocator() {
		return fLocator;
	}

	/**
	 * Locates the handle on the figure by forwarding the request
	 * to its figure.
	 */
	public Point locate() {
		return fLocator.locate(owner());
	}

	/**
	 * @see org.jhotdraw.framework.Handle#getCursor()
	 */
	public Cursor getCursor() {
		Cursor c = super.getCursor();
		if (getLocator() instanceof RelativeLocator) {
			RelativeLocator rl = (RelativeLocator) getLocator();
			if (rl.equals( RelativeLocator.north())) {
				c = new AWTCursor(java.awt.Cursor.N_RESIZE_CURSOR);
			}
			else if (rl.equals(RelativeLocator.northEast())) {
				c = new AWTCursor(java.awt.Cursor.NE_RESIZE_CURSOR);
			}
			else if (rl.equals(RelativeLocator.east())) {
				c = new AWTCursor(java.awt.Cursor.E_RESIZE_CURSOR);
			}
			else if (rl.equals(RelativeLocator.southEast())) {
				c = new AWTCursor(java.awt.Cursor.SE_RESIZE_CURSOR);
			}
			else if (rl.equals(RelativeLocator.south())) {
				c = new AWTCursor(java.awt.Cursor.S_RESIZE_CURSOR);
			}
			else if (rl.equals(RelativeLocator.southWest())) {
				c = new AWTCursor(java.awt.Cursor.SW_RESIZE_CURSOR);
			}
			else if (rl.equals(RelativeLocator.west())) {
				c = new AWTCursor(java.awt.Cursor.W_RESIZE_CURSOR);
			}
			else if (rl.equals(RelativeLocator.northWest())) {
				c = new AWTCursor(java.awt.Cursor.NW_RESIZE_CURSOR);
			}
		}
		return c;
	}

}
