/*
 * @(#)PolygonHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.figures.PolyLineHandle;
import org.jhotdraw.util.Undoable;
import java.awt.Point;

/**
 * A handle for a node on the polygon.
 * Based on PolyLineHandle
 *
 * @author Doug Lea  (dl at gee, Fri Feb 28 07:47:13 1997)
 * @version <$CURRENT_VERSION$>
 */
public class PolygonHandle extends AbstractHandle {
	
	private Locator fLocator;
	private int fIndex;
	
	/**
	* Constructs a polygon handle.
	* @param owner the owning polygon figure.
	* @param l the locator
	* @param index the index of the node associated with this handle.
	*/
	public PolygonHandle(PolygonFigure owner, Locator l, int index) {
		super(owner);
		fLocator = l;
		fIndex = index;
	}

	public void invokeStart(int  x, int  y, DrawingView view) {
		setUndoActivity(createUndoActivity(view, fIndex));
		getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(owner()));
		((PolygonHandle.UndoActivity)getUndoActivity()).setOldPoint(new Point(x, y));
	}

	public void invokeStep(int x, int y, int anchorX, int anchorY, DrawingView view) {
		int index = ((PolyLineHandle.UndoActivity)getUndoActivity()).getPointIndex();
		myOwner().setPointAt(new Point(x, y), index);
	}
	
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		myOwner().smoothPoints();
		if ((x == anchorX) && (y == anchorY)) {
 			setUndoActivity(null);
		}
	}
	
	public Point locate() {
		return fLocator.locate(owner());
	}
	
	private PolygonFigure myOwner() {
		return (PolygonFigure)owner();
	}

	/**
	 * Factory method for undo activity. To be overriden by subclasses.
	 */
	protected Undoable createUndoActivity(DrawingView newView, int newPointIndex) {
		return new PolygonHandle.UndoActivity(newView, newPointIndex);
	}
	
	public static class UndoActivity extends PolyLineHandle.UndoActivity {
		
		public UndoActivity(DrawingView newView, int newPointIndex) {
			super(newView, newPointIndex);
		}
		
		protected boolean movePointToOldLocation() {
			FigureEnumeration fe = getAffectedFigures();
			if (!fe.hasNextFigure()) {
				return false;
			}

			PolygonFigure figure = (PolygonFigure)fe.nextFigure();
			Point backupPoint = figure.pointAt(getPointIndex());
			figure.setPointAt(getOldPoint(), getPointIndex());
			figure.smoothPoints();
			setOldPoint(backupPoint);

			return true;
		}
	}
}
