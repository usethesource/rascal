/*
 * @(#)AbstractHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.Undoable;

/**
 * AbstractHandle provides defaulf implementation for the Handle interface.
 *
 * @see org.jhotdraw.framework.Figure
 * @see org.jhotdraw.framework.Handle
 * @version <$CURRENT_VERSION$>
 */
public abstract class AbstractHandle implements Handle {

	/**
	 * The standard size of a handle.
	 */
	public static final int HANDLESIZE = 8;

	private Figure fOwner;
	private Undoable myUndoableActivity;
	
	/**
	 * Constructor for <code>AbstractHandle</code>.
	 * Initializes the owner of the figure.
	 * @param owner
	 */
	public AbstractHandle(Figure owner) {
		fOwner = owner;
	}

	/**
	 * @param x the x position where the interaction started
	 * @param y the y position where the interaction started
	 * @param view the handles container
	 * @see org.jhotdraw.framework.Handle#invokeStart(int, int, org.jhotdraw.framework.DrawingView)
	 */
	public void invokeStart(int x, int  y, DrawingView view) {
		invokeStart(x, y, view.drawing());
	}


	/**
	 * Tracks the start of the interaction. The default implementation
	 * does nothing.
	 * @param x the x position where the interaction started
	 * @param y the y position where the interaction started
	 * @see org.jhotdraw.framework.Handle#invokeStart(int, int, org.jhotdraw.framework.Drawing)
	 * @deprecated As of version 4.1,
	 * use invokeStart(x, y, drawingView)
	 */
	public void invokeStart(int  x, int  y, Drawing drawing) { }

	/**
	 * Tracks a step of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 * @see org.jhotdraw.framework.Handle#invokeStep(int, int, int, int, org.jhotdraw.framework.DrawingView)
	 */
	public void invokeStep(int x, int y, int anchorX, int anchorY, DrawingView view) {
		invokeStep(x-anchorX, y-anchorY, view.drawing());
	}

	/**
	 * Tracks a step of the interaction.
	 * @param dx x delta of this step
	 * @param dy y delta of this step
	 * @see org.jhotdraw.framework.Handle#invokeStep(int, int, org.jhotdraw.framework.Drawing)
	 * @deprecated As of version 4.1,
	 * use invokeStep(x, y, anchorX, anchorY, drawingView)
	 */
	public void invokeStep(int dx, int dy, Drawing drawing) { }

	/**
	 * Tracks the end of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 * @see org.jhotdraw.framework.Handle#invokeEnd(int, int, int, int, org.jhotdraw.framework.DrawingView)
	 */
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		invokeEnd(x-anchorX, y-anchorY, view.drawing());
	}

	/**
	 * Tracks the end of the interaction.
	 * @see org.jhotdraw.framework.Handle#invokeEnd(int, int, org.jhotdraw.framework.Drawing)
	 * @deprecated As of version 4.1,
	 * use invokeEnd(x, y, anchorX, anchorY, drawingView).
	 */
	public void invokeEnd(int dx, int dy, Drawing drawing) { }

	/**
	 * Gets the handle's owner.
	 * @see org.jhotdraw.framework.Handle#owner()
	 */
	public Figure owner() {
		return fOwner;
	}

	/**
	 * Gets the display box of the handle.
	 * @see org.jhotdraw.framework.Handle#displayBox()
	 */
	public Rectangle displayBox() {
		Point p = locate();
		return new Rectangle(
				p.x - HANDLESIZE / 2,
				p.y - HANDLESIZE / 2,
				HANDLESIZE,
				HANDLESIZE);
	}

	/**
	 * Tests if a point is contained in the handle.
	 * @see org.jhotdraw.framework.Handle#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return displayBox().contains(x, y);
	}

	/**
	 * Draws this handle.
	 * @see org.jhotdraw.framework.Handle#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		Rectangle r = displayBox();

		g.setColor(Color.white);
		g.fillRect(r.x, r.y, r.width, r.height);

		g.setColor(Color.black);
		g.drawRect(r.x, r.y, r.width, r.height);
	}

	/**
	 * @see org.jhotdraw.framework.Handle#getUndoActivity()
	 */
	public Undoable getUndoActivity() {
		return myUndoableActivity;
	}

	/**
	 * @see org.jhotdraw.framework.Handle#setUndoActivity(org.jhotdraw.util.Undoable)
	 */
	public void setUndoActivity(Undoable newUndoableActivity) {
		myUndoableActivity = newUndoableActivity;
	}

	/**
	 * @see org.jhotdraw.framework.Handle#getCursor()
	 */
	public Cursor getCursor() {
		return new AWTCursor(AWTCursor.DEFAULT_CURSOR);
	}

}
