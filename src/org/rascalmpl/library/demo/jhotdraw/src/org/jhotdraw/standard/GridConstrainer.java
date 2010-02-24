/*
 * @(#)GridConstrainer.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.framework.PointConstrainer;
import java.awt.*;
import java.io.Serializable;

/**
 * Constrains a point such that it falls on a grid.
 *
 * @see org.jhotdraw.framework.DrawingView
 *
 * @version <$CURRENT_VERSION$>
 */
public class GridConstrainer implements PointConstrainer, Serializable {

	private int fGridX;
	private int fGridY;

	public GridConstrainer(int x, int y) {
		fGridX = Math.max(1, x);
		fGridY = Math.max(1, y);
	}

	/**
	 * Constrains the given point.
	 * @return constrained point.
	 */
	public Point constrainPoint(Point p) {
		p.x = ((p.x+fGridX/2) / fGridX) * fGridX;
		p.y = ((p.y+fGridY/2) / fGridY) * fGridY;
		return p;
	}

	/**
	 * Gets the x offset to move an object.
	 */
	public int getStepX() {
		return fGridX;
	}

	/**
	 * Gets the y offset to move an object.
	 */
	public int getStepY() {
		return fGridY;
	}
}
