/*
 * @(#)StandardDrawing.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.CollectionsFactory;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.*;

/**
 * The standard implementation of the Drawing interface.
 *
 * @see Drawing
 *
 * @version <$CURRENT_VERSION$>
 */

public class StandardDrawing extends CompositeFigure implements Drawing {


	/**
	 * the registered listeners
	 */
	private transient List              fListeners;

	/**
	 * boolean that serves as a condition variable
	 * to lock the access to the drawing.
	 * The lock is recursive and we keep track of the current
	 * lock holder.
	 */
	private transient Thread    fDrawingLockHolder = null;
	private String				myTitle;

	/*
	 * Serialization support
	 */
	private static final long serialVersionUID = -2602151437447962046L;
	private int drawingSerializedDataVersion = 1;

	/**
	 * Constructs the Drawing.
	 */
	public StandardDrawing() {
		super();
		fListeners = CollectionsFactory.current().createList(2);
		init(new Rectangle(-500, -500, 2000, 2000));
	}

	/**
	 * Adds a listener for this drawing.
	 */
	public void addDrawingChangeListener(DrawingChangeListener listener) {
		if (fListeners == null) {
			fListeners = CollectionsFactory.current().createList(2);
		}
		fListeners.add(listener);
	}

	/**
	 * Removes a listener from this drawing.
	 */
	public void removeDrawingChangeListener(DrawingChangeListener listener) {
		fListeners.remove(listener);
	}

	/**
	 * Gets an enumeration with all listener for this drawing.
	 */
	public Iterator drawingChangeListeners() {
		return fListeners.iterator();
	}

	/**
	 * Removes a figure from the figure list, but
	 * doesn't release it. Use this method to temporarily
	 * manipulate a figure outside of the drawing.
	 *
	 * @param figure that is part of the drawing and should be added
	 */
	public synchronized Figure orphan(Figure figure) {
		Figure orphanedFigure = super.orphan(figure);
		// ensure that we remove the top level figure in a drawing
		if (orphanedFigure.listener() != null) {
			Rectangle rect = invalidateRectangle(displayBox());
			orphanedFigure.listener().figureRequestRemove(new FigureChangeEvent(orphanedFigure, rect));
		}
		return orphanedFigure;
	}

	public synchronized Figure add(Figure figure) {
		Figure addedFigure = super.add(figure);
		if (addedFigure.listener() != null) {
			Rectangle rect = invalidateRectangle(displayBox());
			addedFigure.listener().figureRequestUpdate(new FigureChangeEvent(figure, rect));
			return addedFigure;
		}
		return addedFigure;
	}

	/**
	 * Invalidates a rectangle and merges it with the
	 * existing damaged area.
	 * @see FigureChangeListener
	 */
	public void figureInvalidated(FigureChangeEvent e) {
		if (fListeners != null) {
			for (int i = 0; i < fListeners.size(); i++) {
				DrawingChangeListener l = (DrawingChangeListener)fListeners.get(i);
				l.drawingInvalidated(new DrawingChangeEvent(this, e.getInvalidatedRectangle()));
			}
		}
	}

	/**
	 * Forces an update of the drawing change listeners.
	 */
	public void fireDrawingTitleChanged() {
		if (fListeners != null) {
			for (int i = 0; i < fListeners.size(); i++) {
				DrawingChangeListener l = (DrawingChangeListener)fListeners.get(i);
				l.drawingTitleChanged(new DrawingChangeEvent(this, null));
			}
		}
	}

	/**
	 * Forces an update of the drawing change listeners.
	 */
	public void figureRequestUpdate(FigureChangeEvent e) {
		if (fListeners != null) {
			for (int i = 0; i < fListeners.size(); i++) {
				DrawingChangeListener l = (DrawingChangeListener)fListeners.get(i);
				l.drawingRequestUpdate(new DrawingChangeEvent(this, null));
			}
		}
	}

	/**
	 * Return's the figure's handles. This is only used when a drawing
	 * is nested inside another drawing.
	 */
	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		handles.add(new NullHandle(this, RelativeLocator.northWest()));
		handles.add(new NullHandle(this, RelativeLocator.northEast()));
		handles.add(new NullHandle(this, RelativeLocator.southWest()));
		handles.add(new NullHandle(this, RelativeLocator.southEast()));
		return new HandleEnumerator(handles);
	}

	/**
	 * Gets the display box. This is the union of all figures.
	 */
	public Rectangle displayBox() {
		if (fFigures.size() > 0) {
			FigureEnumeration fe = figures();

			Rectangle r = fe.nextFigure().displayBox();

			while (fe.hasNextFigure()) {
				r.add(fe.nextFigure().displayBox());
			}
			return r;
		}
		return new Rectangle(0, 0, 0, 0);
	}

	public void basicDisplayBox(Point p1, Point p2) {
	}

	/**
	 * Acquires the drawing lock.
	 */
	public synchronized void lock() {
		// recursive lock
		Thread current = Thread.currentThread();
		if (fDrawingLockHolder == current) {
			return;
		}
		while (fDrawingLockHolder != null) {
			try {
				wait();
			}
			catch (InterruptedException ex) { }
		}
		fDrawingLockHolder = current;
	}

	/**
	 * Releases the drawing lock.
	 */
	public synchronized void unlock() {
		if (fDrawingLockHolder != null) {
			fDrawingLockHolder = null;
			notify();
		}
	}

	private void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {

		s.defaultReadObject();

		fListeners = CollectionsFactory.current().createList(2);
	}

	public String getTitle() {
		return myTitle;
	}

	public void setTitle(String newTitle) {
		myTitle = newTitle;
	}
}
