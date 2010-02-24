/*
 * @(#)EllipseFigureGeometricAdapter.java
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
import java.awt.geom.Ellipse2D;

import org.jhotdraw.figures.EllipseFigure;

/**
 * Geometric adapter for the EllipseFigure
 *
 * @author  Eduardo Francos - InContext
 * @created 1 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class EllipseFigureGeometricAdapter extends EllipseFigure
		 implements GeometricFigure {

	/**
	 * Constructor for the EllipseFigureExt object
	 */
	public EllipseFigureGeometricAdapter() {
		super();
	}

	/**
	 *Constructor for the EllipseFigureGeometricAdapter object
	 *
	 * @param origin  Description of the Parameter
	 * @param corner  Description of the Parameter
	 */
	public EllipseFigureGeometricAdapter(Point origin, Point corner) {
		super(origin, corner);
	}

	/**
	 * Gets the shape attribute of the EllipseFigure object
	 *
	 * @return   The shape value
	 */
	public Shape getShape() {
		Rectangle rect = displayBox();
		Ellipse2D.Float ellipse = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		return ellipse;
	}
}
