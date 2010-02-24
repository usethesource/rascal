/*
 * @(#)ElbowHandle.java
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
import java.awt.*;

/**
 * A Handle to move an ElbowConnection left/right or up/down.
 *
 * @version <$CURRENT_VERSION$>
 */
public class ElbowHandle extends AbstractHandle {

	private int fSegment;
	private int fLastX, fLastY;      // previous mouse position

	public ElbowHandle(LineConnection owner, int segment) {
		super(owner);
		fSegment = segment;
	}

	public void invokeStart(int  x, int  y, DrawingView view) {
		fLastX = x;
		fLastY = y;
	}

	public void invokeStep (int x, int y, int anchorX, int anchorY, DrawingView view) {
		LineConnection line = ownerConnection();
		Point p1 = line.pointAt(fSegment);
		Point p2 = line.pointAt(fSegment+1);
		int ddx = x - fLastX;
		int ddy = y - fLastY;

		Point np1;
		Point np2;
		if (isVertical(p1, p2)) {
			int cx = constrainX(p1.x + ddx);
			np1 = new Point(cx, p1.y);
			np2 = new Point(cx, p2.y);
		}
		else {
			int cy = constrainY(p1.y + ddy);
			np1 = new Point(p1.x, cy);
			np2 = new Point(p2.x, cy);
		}
		line.setPointAt(np1, fSegment);
		line.setPointAt(np2, fSegment+1);
		fLastX = x;
		fLastY = y;
	}

	private boolean isVertical(Point p1, Point p2) {
		return p1.x == p2.x;
	}

	public Point locate() {
		LineConnection line = ownerConnection();
		int segment = Math.min(fSegment, line.pointCount()-2);
		Point p1 = line.pointAt(segment);
		Point p2 = line.pointAt(segment+1);
		return new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
	}

	public void draw(Graphics g) {
		Rectangle r = displayBox();

		g.setColor(Color.yellow);
		g.fillOval(r.x, r.y, r.width, r.height);

		g.setColor(Color.black);
		g.drawOval(r.x, r.y, r.width, r.height);
	}

	private int constrainX(int x) {
		LineConnection line = ownerConnection();
		Figure startFigure = line.getStartConnector().owner();
		Figure endFigure = line.getEndConnector().owner();
		Rectangle start = startFigure.displayBox();
		Rectangle end = endFigure.displayBox();
		Insets i1 = startFigure.connectionInsets();
		Insets i2 = endFigure.connectionInsets();

		int r1x, r1width, r2x, r2width;
		r1x = start.x + i1.left;
		r1width = start.width - i1.left - i1.right-1;

		r2x = end.x + i2.left;
		r2width = end.width - i2.left - i2.right-1;

		if (fSegment == 0) {
			x = Geom.range(r1x, r1x + r1width, x);
		}
		if (fSegment == line.pointCount()-2) {
			x = Geom.range(r2x, r2x + r2width, x);
		}
		return x;
	}

	private int constrainY(int y) {
		LineConnection line = ownerConnection();
		Figure startFigure = line.getStartConnector().owner();
		Figure endFigure = line.getEndConnector().owner();
		Rectangle start = startFigure.displayBox();
		Rectangle end = endFigure.displayBox();
		Insets i1 = startFigure.connectionInsets();
		Insets i2 = endFigure.connectionInsets();

		int r1y, r1height, r2y, r2height;
		r1y = start.y + i1.top;
		r1height = start.height - i1.top - i1.bottom-1;
		r2y = end.y + i2.top;
		r2height = end.height - i2.top - i2.bottom-1;

		if (fSegment == 0) {
			y = Geom.range(r1y, r1y + r1height, y);
		}
		if (fSegment == line.pointCount()-2) {
			y = Geom.range(r2y, r2y + r2height, y);
		}
		return y;
	}

	private LineConnection ownerConnection() {
		return (LineConnection)owner();
	}
}
