/*
 * @(#)RadiusHandle.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.Undoable;
import org.jhotdraw.util.UndoableAdapter;
import java.awt.*;

/**
 * A Handle to manipulate the radius of a round corner rectangle.
 *
 * @version <$CURRENT_VERSION$>
 */
class RadiusHandle extends AbstractHandle {

	private static final int OFFSET = 4;

	public RadiusHandle(RoundRectangleFigure owner) {
		super(owner);
	}

	public void invokeStart(int  x, int  y, DrawingView view) {
		setUndoActivity(createUndoActivity(view));
		getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(owner()));
		((RadiusHandle.UndoActivity)getUndoActivity()).
			setOldRadius(((RoundRectangleFigure)owner()).getArc());
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		int dx = x-anchorX;
		int dy = y-anchorY;
		RoundRectangleFigure owner = (RoundRectangleFigure)owner();
		Rectangle r = owner.displayBox();
		Point originalRadius = ((RadiusHandle.UndoActivity)getUndoActivity()).getOldRadius();
		int rx = Geom.range(0, r.width, 2*(originalRadius.x/2 + dx));
		int ry = Geom.range(0, r.height, 2*(originalRadius.y/2 + dy));
		owner.setArc(rx, ry);
	}

	public void invokeEnd(int x, int y, int anchorX, int anchorY, DrawingView view) {
		Point currentRadius = ((RoundRectangleFigure)owner()).getArc();
		Point originalRadius = ((RadiusHandle.UndoActivity)getUndoActivity()).getOldRadius();
		// there has been no change so there is nothing to undo
		if ((currentRadius.x == originalRadius.x) && (currentRadius.y == originalRadius.y)) {
			setUndoActivity(null);
		}
	}

	public Point locate() {
		RoundRectangleFigure owner = (RoundRectangleFigure)owner();
		Point radius = owner.getArc();
		Rectangle r = owner.displayBox();
		return new Point(r.x+radius.x/2+OFFSET, r.y+radius.y/2+OFFSET);
	}

	public void draw(Graphics g) {
		Rectangle r = displayBox();

		g.setColor(Color.yellow);
		g.fillOval(r.x, r.y, r.width, r.height);

		g.setColor(Color.black);
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	/**
	 * Factory method for undo activity. To be overriden by subclasses.
	 */
	protected Undoable createUndoActivity(DrawingView newView) {
		return new RadiusHandle.UndoActivity(newView);
	}
	
	public static class UndoActivity extends UndoableAdapter {
		private Point myOldRadius;
		
		public UndoActivity(DrawingView newView) {
			super(newView);
			setUndoable(true);
			setRedoable(true);
		}
		
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			return resetRadius();
		}
	
		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (!isRedoable()) {
				return false;
			}

			return resetRadius();
		}

		protected boolean resetRadius() {
			FigureEnumeration fe = getAffectedFigures();
			if (!fe.hasNextFigure()) {
				return false;
			}
			RoundRectangleFigure currentFigure = (RoundRectangleFigure)fe.nextFigure();
			Point figureRadius = currentFigure.getArc();
			currentFigure.setArc(getOldRadius().x, getOldRadius().y);
			setOldRadius(figureRadius);
			return true;
		}
		
		protected void setOldRadius(Point newOldRadius) {
			myOldRadius = newOldRadius;
		}

		public Point getOldRadius() {
			return myOldRadius;
		}
	}
}
