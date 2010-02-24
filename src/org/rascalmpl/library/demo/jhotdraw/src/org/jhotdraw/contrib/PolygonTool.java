/*
 * @(#)PolygonTool.java
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
import org.jhotdraw.util.Undoable;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Based on ScribbleTool
 *
 * @author Doug Lea  (dl at gee) - Fri Feb 28 07:47:05 1997
 * @version <$CURRENT_VERSION$>
 */
public class PolygonTool extends AbstractTool {

	private PolygonFigure  fPolygon;
	private int            fLastX, fLastY;

	/**
	 * the figure that was actually added
	 * Note, this can be a different figure from the one which has been created.
	 */
	private Figure myAddedFigure;

	public PolygonTool(DrawingEditor newDrawingEditor) {
		super(newDrawingEditor);
	}

	public void activate() {
		super.activate();
		fPolygon = null;
	}

	public void deactivate() {
		if (fPolygon != null) {
			fPolygon.smoothPoints();
			if (fPolygon.pointCount() < 3 ||
					fPolygon.size().width < 4 || fPolygon.size().height < 4) {
				getActiveView().drawing().remove(fPolygon);
				// nothing to undo
				setUndoActivity(null);
			}
		}
		fPolygon = null;
		super.deactivate();
	}

	private void addPoint(int x, int y) {
		if (fPolygon == null) {
			fPolygon = new PolygonFigure(x, y);
			setAddedFigure(view().add(fPolygon));
			fPolygon.addPoint(x, y);
		}
		else if (fLastX != x || fLastY != y) {
			fPolygon.addPoint(x, y);
		}

		fLastX = x;
		fLastY = y;
	}

	public void mouseDown(MouseEvent e, int x, int y) {
		super.mouseDown(e,x,y);
		// replace pts by actual event pts
		x = e.getX();
		y = e.getY();

		if (e.getClickCount() >= 2) {
			if (fPolygon != null) {
				fPolygon.smoothPoints();

				// use undo activity from paste command...
				setUndoActivity(createUndoActivity());

				// put created figure into a figure enumeration
				getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(getAddedFigure()));

				editor().toolDone();
			}
			fPolygon = null;

		}
		else {
			// use original event coordinates to avoid
			// supress that the scribble is constrained to
			// the grid
			addPoint(e.getX(), e.getY());
		}
	}

	public void mouseMove(MouseEvent e, int x, int y) {
		if (e.getSource() == getActiveView()) {
			if (fPolygon != null) {
				if (fPolygon.pointCount() > 1) {
					fPolygon.setPointAt(new Point(x, y), fPolygon.pointCount()-1);
					getActiveView().checkDamage();
				}
			}
		}
	}

	public void mouseDrag(MouseEvent e, int x, int y) {
		// replace pts by actual event pts
		x = e.getX();
		y = e.getY();
		addPoint(x, y);
	}

	public void mouseUp(MouseEvent e, int x, int y) {
	}

	/**
	 * Gets the figure that was actually added
	 * Note, this can be a different figure from the one which has been created.
	 */
	protected Figure getAddedFigure() {
		return myAddedFigure;
	}

	private void setAddedFigure(Figure newAddedFigure) {
		myAddedFigure = newAddedFigure;
	}

	/**
	 * Factory method for undo activity
	 */
	protected Undoable createUndoActivity() {
		return new PasteCommand.UndoActivity(view());
	}
}
