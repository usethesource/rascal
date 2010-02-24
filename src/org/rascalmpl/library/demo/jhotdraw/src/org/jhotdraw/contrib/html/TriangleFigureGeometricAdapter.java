/*
 * @(#)TriangleFigureGeometricAdapter.java
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
import java.awt.Shape;
import org.jhotdraw.contrib.TriangleFigure;

/**
 * Geometric adapter for the TriangleFigure
 *
 * @author  Eduardo Francos - InContext
 * @created 4 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public class TriangleFigureGeometricAdapter extends TriangleFigure
		 implements GeometricFigure {

	/**
	 * Constructor for the TriangleFigureGeometricAdapter object
	 */
	public TriangleFigureGeometricAdapter() {
		super();
	}

	/**
	 *Constructor for the TriangleFigureGeometricAdapter object
	 *
	 * @param origin  Description of the Parameter
	 * @param corner  Description of the Parameter
	 */
	public TriangleFigureGeometricAdapter(Point origin, Point corner) {
		super(origin, corner);
	}

	/**
	 * Gets the shape attribute of the TriangleFigure object
	 *
	 * @return   The shape value
	 */
	public Shape getShape() {
		return getPolygon();
	}
}
