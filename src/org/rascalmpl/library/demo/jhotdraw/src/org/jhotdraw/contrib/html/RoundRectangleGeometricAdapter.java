/*
 * @(#)RoundRectangleGeometricAdapter.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import org.jhotdraw.figures.RoundRectangleFigure;

/**
 * Geometric adapter for the RoundRectangleFigure
 *
 * @author  Eduardo Francos - InContext
 * @created 4 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class RoundRectangleGeometricAdapter extends RoundRectangleFigure
		 implements GeometricFigure {

	/**
	 * Constructor for the RoundRectangleGeometricAdapter object
	 */
	public RoundRectangleGeometricAdapter() {
		super();
	}

	/**
	 *Constructor for the RoundRectangleGeometricAdapter object
	 *
	 * @param origin  Description of the Parameter
	 * @param corner  Description of the Parameter
	 */
	public RoundRectangleGeometricAdapter(Point origin, Point corner) {
		super(origin, corner);
	}

	/**
	 * Gets the shape attribute of the TriangleFigure object
	 *
	 * @return   The shape value
	 */
	public Shape getShape() {
		Point arc = getArc();
		Rectangle dspBox = displayBox();
		RoundRectangle2D.Float roundRectangle = new RoundRectangle2D.Float(
				dspBox.x, dspBox.y, dspBox.width, dspBox.height,
				arc.x, arc.y);

		return roundRectangle;
	}
}
