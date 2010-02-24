/*
 * @(#)PolygonScaleHandle.java
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
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.Undoable;
import org.jhotdraw.util.UndoableAdapter;
import java.awt.*;

/**
 * A Handle to scale and rotate a PolygonFigure
 * Based on RadiusHandle
 *
 * @author Doug Lea  (dl at gee, Sat Mar 1 09:06:09 1997)
 * @version <$CURRENT_VERSION$>
 */
class PolygonScaleHandle extends AbstractHandle {
	
	private Point fCurrent;
	
	public PolygonScaleHandle(PolygonFigure owner) {
		super(owner);
	}
	
	/**
	 * @param x the x position where the interaction started
	 * @param y the y position where the interaction started
	 * @param view the handles container
	 */
	public void invokeStart(int x, int  y, DrawingView view) {
		fCurrent = new Point(x, y);
		PolygonScaleHandle.UndoActivity activity = (PolygonScaleHandle.UndoActivity)createUndoActivity(view);
		setUndoActivity(activity);
		activity.setAffectedFigures(new SingleFigureEnumerator(owner()));
		activity.setPolygon(((PolygonFigure)(owner())).getPolygon());
	}

	/**
	 * Tracks a step of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 */
	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		fCurrent = new Point(x, y);
		Polygon polygon = ((PolygonScaleHandle.UndoActivity)getUndoActivity()).getPolygon();
		((PolygonFigure)(owner())).scaleRotate(new Point(anchorX, anchorY), polygon, fCurrent);
	}

	/**
	 * Tracks the end of the interaction.
	 * @param x the current x position
	 * @param y the current y position
	 * @param anchorX the x position where the interaction started
	 * @param anchorY the y position where the interaction started
	 */
	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		((PolygonFigure)(owner())).smoothPoints();
		if ((fCurrent.x == anchorX) && (fCurrent.y == anchorY)) {
			// there is nothing to undo
			setUndoActivity(null);
		}
		fCurrent = null;
	}
	
	public Point locate() {
		if (fCurrent == null) {
			return getOrigin();
		}
		else {
			return fCurrent;
		}
	}
	
	Point getOrigin() {
		// find a nice place to put handle
		// Need to pick a place that will not overlap with point handle
		// and is internal to polygon
	
		// Try for one HANDLESIZE step away from outermost toward center
	
		Point outer = ((PolygonFigure)(owner())).outermostPoint();
		Point ctr = ((PolygonFigure)(owner())).center();
		double len = Geom.length(outer.x, outer.y, ctr.x, ctr.y);
		if (len == 0) { // best we can do?
			return new Point(outer.x - HANDLESIZE/2, outer.y + HANDLESIZE/2);
		}
	
		double u = HANDLESIZE / len;
		if (u > 1.0) { // best we can do?
			return new Point((outer.x * 3 + ctr.x)/4, (outer.y * 3 + ctr.y)/4);
		}
		else {
			return new Point((int)(outer.x * (1.0 - u) + ctr.x * u),
			(int)(outer.y * (1.0 - u) + ctr.y * u));
		}
	}
	
	public void draw(Graphics g) {
		Rectangle r = displayBox();
	
		g.setColor(Color.yellow);
		g.fillOval(r.x, r.y, r.width, r.height);
	
		g.setColor(Color.black);
		g.drawOval(r.x, r.y, r.width, r.height);
	
		/* for debugging ...
		Point ctr = ((PolygonFigure)(owner())).center();
		g.setColor(Color.blue);
		g.fillOval(ctr.x, ctr.y, r.width, r.height);
		
		g.setColor(Color.black);
		g.drawOval(ctr.x, ctr.y, r.width, r.height);
		
		*/
	}

	/**
	 * Factory method for undo activity. To be overriden by subclasses.
	 */
	protected Undoable createUndoActivity(DrawingView newView) {
		return new PolygonScaleHandle.UndoActivity(newView);
	}
	
	public static class UndoActivity extends UndoableAdapter {
		private Polygon myPolygon;
		
		public UndoActivity(DrawingView newView) {
			super(newView);
			setUndoable(true);
			setRedoable(true);
		}
		
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			return resetPolygon();
		}
	
		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (!isRedoable()) {
				return false;
			}

			return resetPolygon();
		}

		protected boolean resetPolygon() {
			FigureEnumeration fe = getAffectedFigures();
			if (!fe.hasNextFigure()) {
				return false;
			}
			PolygonFigure figure = (PolygonFigure)fe.nextFigure();
			Polygon backupPolygon = figure.getPolygon();
			figure.willChange();
			figure.setInternalPolygon(getPolygon());
			figure.changed();
			setPolygon(backupPolygon);
			return true;
		}
		
		protected void setPolygon(Polygon newPolygon) {
			myPolygon = newPolygon;
		}
		
		public Polygon getPolygon() {
			return myPolygon;
		}
	}
}
