/*
 * @(#)ArrowTip.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.io.*;
import java.awt.*;

import org.jhotdraw.util.*;

/**
 * An arrow tip line decoration.
 *
 * @see PolyLineFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public  class ArrowTip extends AbstractLineDecoration {

	private double  fAngle;         // pointiness of arrow
	private double  fOuterRadius;
	private double  fInnerRadius;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -3459171428373823638L;
	private int arrowTipSerializedDataVersion = 1;

	public ArrowTip() {
		this(0.40, 8, 8);  // this(0.35, 15, 12);
	}

   /**
	* Constructs an arrow tip with the given angle and radius.
	*/
	public ArrowTip(double angle, double outerRadius, double innerRadius) {
		setAngle(angle);
		setOuterRadius(outerRadius);
		setInnerRadius(innerRadius);
	}
	
   /**
	* Calculates the outline of an arrow tip.
	*/
	public Polygon outline(int x1, int y1, int x2, int y2) {
		double dir = Math.PI/2 - Math.atan2(x2 - x1, y2 - y1);
		return outline(x1, y1, dir);
	}

	private Polygon outline(int x, int y, double direction) {
		Polygon shape = new Polygon();

		shape.addPoint(x, y);
		addPointRelative(shape, x, y, getOuterRadius(), direction - getAngle());
		addPointRelative(shape, x, y, getInnerRadius(), direction);
		addPointRelative(shape, x, y, getOuterRadius(), direction + getAngle());
		shape.addPoint(x,y); // Closing the polygon (TEG 97-04-23)
		return shape;
	}

	private void addPointRelative(Polygon shape, int x, int y, double radius, double angle) {
		shape.addPoint(
			x + (int) (radius * Math.cos(angle)),
			y + (int) (radius * Math.sin(angle)));
	}

	/**
	 * Stores the arrow tip to a StorableOutput.
	 */
	public void write(StorableOutput dw) {
		dw.writeDouble(getAngle());
		dw.writeDouble(getOuterRadius());
		dw.writeDouble(getInnerRadius());
		super.write(dw);
	}

	/**
	 * Reads the arrow tip from a StorableInput.
	 */
	public void read(StorableInput dr) throws IOException {
		setAngle(dr.readDouble());
		setOuterRadius(dr.readDouble());
		setInnerRadius(dr.readDouble());
		super.read(dr);
	}

	/**
	 * Sets point angle of arrow. A smaller angle leads to a pointier arrow.
	 * The angle is measured between the arrow line and one of the points
	 * at the side of the arrow. Thus, the total angle at the arrow tip
	 * is the double of the angle specified.
	 */
	protected void setAngle(double newAngle) {
		fAngle = newAngle;
	}
	
	/**
	 * Returns point angle of arrow. A smaller angle leads to a pointier arrow.
	 * The angle is measured between the arrow line and one of the points
	 * at the side of the arrow. Thus, the total angle at the arrow tip
	 * is the double of the angle specified.
	 */
	protected double getAngle() {
		return fAngle;
	}

	/**
	 * Sets the inner radius
	 */
	protected void setInnerRadius(double newInnerRadius) {
		fInnerRadius = newInnerRadius;
	}

	/**
	 * Returns the inner radius
	 */        
	protected double getInnerRadius() {
		return fInnerRadius;
	}

	/**
	 * Sets the outer radius
	 */
	protected void setOuterRadius(double newOuterRadius) {
		fOuterRadius = newOuterRadius;
	}

	/**
	 * Returns the outer radius
	 */
	protected double getOuterRadius() {
		return fOuterRadius;
	}
}
