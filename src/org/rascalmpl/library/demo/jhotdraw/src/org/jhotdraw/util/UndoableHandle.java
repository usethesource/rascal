/*
 * @(#)UndoableHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.jhotdraw.framework.*;
import org.jhotdraw.framework.Drawing;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.Handle;

/**
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class UndoableHandle implements Handle {

	private Handle myWrappedHandle;
	private DrawingView myDrawingView;

	/**
	 * Constructor for <code>UndoableHandle</code>.
	 * @param newWrappedHandle
	 */
	public UndoableHandle(Handle newWrappedHandle) {
		setWrappedHandle(newWrappedHandle);
	}

	/**
	 * Constructor for <code>UndoableHandle</code>.
	 * @param newWrappedHandle
	 * @param newDrawingView
	 * @deprecated use the constructor without the DrawingView instead
	 */
	public UndoableHandle(Handle newWrappedHandle, DrawingView newDrawingView) {
		setWrappedHandle(newWrappedHandle);
		setDrawingView(newDrawingView);
	}
	
	/**
	 * Locates the handle on the figure. The handle is drawn
	 * centered around the returned point.
	 * @see org.jhotdraw.framework.Handle#locate()
	 */
	public Point locate() {
		return getWrappedHandle().locate();
	}

	/**
	 * Tracks the start of the interaction. The default implementation
	 * does nothing.
	 * @param x the x position where the interaction started
	 * @param y the y position where the interaction started
	 * @param view the handles container
	 * @see org.jhotdraw.framework.Handle#invokeStart(int, int, org.jhotdraw.framework.DrawingView)
	 */
	public void invokeStart(int x, int y, DrawingView view) {
		getWrappedHandle().invokeStart(x, y, view);
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
	public void invokeStart(int x, int y, Drawing drawing) {
		getWrappedHandle().invokeStart(x, y, drawing);
	}

	/**
	 * Tracks a step of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 * @see org.jhotdraw.framework.Handle#invokeStep(int, int, int, int, org.jhotdraw.framework.DrawingView)
	 */
	public void invokeStep(int x, int y, int anchorX, int anchorY, DrawingView view) {
		getWrappedHandle().invokeStep(x, y, anchorX, anchorY, view);
	}

	/**
	 * Tracks a step of the interaction.
	 * @param dx x delta of this step
	 * @param dy y delta of this step
	 * @see org.jhotdraw.framework.Handle#invokeStep(int, int, org.jhotdraw.framework.Drawing)
	 * @deprecated As of version 4.1,
	 * use invokeStep(x, y, anchorX, anchorY, drawingView)
	 */
	public void invokeStep(int dx, int dy, Drawing drawing) {
		getWrappedHandle().invokeStep(dx, dy, drawing);
	}

	/**
	 * Tracks the end of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 * @see org.jhotdraw.framework.Handle#invokeEnd(int, int, int, int, org.jhotdraw.framework.DrawingView)
	 */
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		getWrappedHandle().invokeEnd(x, y, anchorX, anchorY, view);

		Undoable undoableActivity = getWrappedHandle().getUndoActivity();
		if ((undoableActivity != null) && (undoableActivity.isUndoable())) {
			view.editor().getUndoManager().pushUndo(undoableActivity);
			view.editor().getUndoManager().clearRedos();
		}
	}

	/**
	 * Tracks the end of the interaction.
	 * @see org.jhotdraw.framework.Handle#invokeEnd(int, int, org.jhotdraw.framework.Drawing)
	 * @deprecated As of version 4.1,
	 * use invokeEnd(x, y, anchorX, anchorY, drawingView).
	 */
	public void invokeEnd(int dx, int dy, Drawing drawing) {
		getWrappedHandle().invokeEnd(dx, dy, drawing);
	}

	/**
	 * Gets the handle's owner.
	 * @see org.jhotdraw.framework.Handle#owner()
	 */
	public Figure owner() {
		return getWrappedHandle().owner();
	}

	/**
	 * Gets the display box of the handle.
	 * @see org.jhotdraw.framework.Handle#displayBox()
	 */
	public Rectangle displayBox() {
		return getWrappedHandle().displayBox();
	}

	/**
	 * Tests if a point is contained in the handle.
	 * @see org.jhotdraw.framework.Handle#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return getWrappedHandle().containsPoint(x, y);
	}

	/**
	 * Draws this handle.
	 * @see org.jhotdraw.framework.Handle#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		getWrappedHandle().draw(g);
	}

	protected void setWrappedHandle(Handle newWrappedHandle) {
		myWrappedHandle = newWrappedHandle;
	}
	
	protected Handle getWrappedHandle() {
		return myWrappedHandle;
	}

	/**
	 * @deprecated attribute not required anymore
	 */
	public DrawingView getDrawingView() {
		return myDrawingView;
	}
	
	/**
	 * @deprecated attribute not required anymore
	 */
	protected void setDrawingView(DrawingView newDrawingView) {
		myDrawingView = newDrawingView;
	}

	/**
	 * @see org.jhotdraw.framework.Handle#getUndoActivity()
	 */
	public Undoable getUndoActivity() {
		return new UndoableAdapter(getDrawingView());
	}

	/**
	 * @see org.jhotdraw.framework.Handle#setUndoActivity(org.jhotdraw.util.Undoable)
	 */
	public void setUndoActivity(Undoable newUndoableActivity) {
		// do nothing: always return default UndoableAdapter
	}

	/**
	 * @see org.jhotdraw.framework.Handle#getCursor()
	 */
	public Cursor getCursor() {
		return getWrappedHandle().getCursor();
	}

}
