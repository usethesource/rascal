/*
 * @(#)Geom.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import java.awt.Rectangle;
import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * Some geometric utilities.
 *
 * @version <$CURRENT_VERSION$>
 */
public class Geom {

	private Geom() {} // never instantiated

	/**
	 * Tests if a point is on a line.
	 */
	static public boolean lineContainsPoint(int x1, int y1,
							int x2, int y2,
							int px, int py) {

		Rectangle r = new Rectangle(new Point(x1, y1));
		r.add(x2, y2);
		r.grow(2, 2);
		if (! r.contains(px,py)) {
			return false;
		}

		double a, b, x, y;

		if (x1 == x2) {
			return (Math.abs(px - x1) < 3);
		}
		if (y1 == y2) {
			return (Math.abs(py - y1) < 3);
		}

		a = (y1 - y2) / (x1 - x2);
		b = y1 - a * x1;
		x = (py - b) / a;
		y = a * px + b;

		return (Math.min(Math.abs(x - px), Math.abs(y - py)) < 4);
	}

	static public final int NORTH = 1;
	static public final int SOUTH = 2;
	static public final int WEST = 3;
	static public final int EAST = 4;

	/**
	 * Returns the direction NORTH, SOUTH, WEST, EAST from
	 * one point to another one.
	 */
	static public int direction(int x1, int y1, int x2, int y2) {
		int direction = 0;
		int vx = x2 - x1;
		int vy = y2 - y1;

		if (vy < vx && vx > -vy) {
			direction = EAST;
		}
		else if (vy > vx && vy > -vx) {
			direction = NORTH;
		}
		else if (vx < vy && vx < -vy) {
			direction = WEST;
		}
		else {
			direction = SOUTH;
		}
		return direction;
	}

	static public Point south(Rectangle r) {
		return new Point(r.x + r.width /2, r.y + r.height);
	}

	static public Point center(Rectangle r) {
		return new Point(r.x + r.width /2, r.y + r.height/2);
	}

	static public Point west(Rectangle r) {
		return new Point(r.x, r.y + r.height/ 2);
	}

	static public Point east(Rectangle r) {
		return new Point(r.x+r.width, r.y + r.height/ 2);
	}

	static public Point north(Rectangle r) {
		return new Point(r.x+r.width/2, r.y);
	}

	/**
	 * Returns the corner (bottom right) of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
	public static Point corner(Rectangle r)
	{
		return new Point((int)r.getMaxX(), (int)r.getMaxY());
	}


	/**
	 * Returns the top left corner of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
	public static Point topLeftCorner(Rectangle r)
	{
		return r.getLocation();
	}


	/**
	 * Returns the top right corner of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
	public static Point topRightCorner(Rectangle r)
	{
		return new Point((int)r.getMaxX(), (int)r.getMinY());
	}


	/**
	 * Returns the bottom left corner of the rectangle
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
	public static Point bottomLeftCorner(Rectangle r)
	{
		return new Point((int)r.getMinX(), (int)r.getMaxY());
	}


	/**
	 * Returns the bottom right corner of the rectangle.
	 * Same as corner, added for naming coherence with the other
	 * corner extracting methods
	 *
	 * @param r  the rectangle
	 * @return   the corner
	 */
	public static Point bottomRightCorner(Rectangle r)
	{
		return corner(r);
	}

	/**
	 * Constains a value to the given range.
	 * @return the constrained value
	 */
	static public int range(int min, int max, int value) {
		if (value < min) {
			value = min;
		}
		if (value > max) {
			value = max;
		}
		return value;
	}

	/**
	 * Gets the square distance between two points.
	 */
	static public long length2(int x1, int y1, int x2, int y2) {
		return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
	}

	/**
	 * Gets the distance between to points
	 */
	static public long length(int x1, int y1, int x2, int y2) {
		return (long)Math.sqrt(length2(x1, y1, x2, y2));
	}

	/**
	 * Gets the angle of a point relative to a rectangle.
	 */
	static public double pointToAngle(Rectangle r, Point p) {
		int px = p.x - (r.x + r.width/2);
		int py = p.y - (r.y + r.height/2);
		return Math.atan2(py*r.width, px*r.height);
	}

	/**
	 * Gets the point on a rectangle that corresponds to the given angle.
	 */
	static public Point angleToPoint(Rectangle r, double angle) {
		double si = Math.sin(angle);
		double co = Math.cos(angle);
		double e = 0.0001;

		int x= 0, y= 0;
		if (Math.abs(si) > e) {
			x= (int) ((1.0 + co/Math.abs(si))/2.0 * r.width);
			x= range(0, r.width, x);
		}
		else if (co >= 0.0) {
			x= r.width;
		}
		if (Math.abs(co) > e) {
			y= (int) ((1.0 + si/Math.abs(co))/2.0 * r.height);
			y= range(0, r.height, y);
		}
		else if (si >= 0.0) {
			y= r.height;
		}
		return new Point(r.x + x, r.y + y);
	}

	/**
	 * Converts a polar to a point
	 */
	static public Point polarToPoint(double angle, double fx, double fy) {
		double si = Math.sin(angle);
		double co = Math.cos(angle);
		return new Point((int)(fx*co+0.5), (int)(fy*si+0.5));
	}

	/**
	 * Gets the point on an oval that corresponds to the given angle.
	 */
	static public Point ovalAngleToPoint(Rectangle r, double angle) {
		Point center = Geom.center(r);
		Point p = Geom.polarToPoint(angle, r.width/2, r.height/2);
		return new Point(center.x + p.x, center.y + p.y);
	}

	/**
	 * Standard line intersection algorithm
	 * Return the point of intersection if it exists, else null
	 **/
	// from Doug Lea's PolygonFigure
	static public Point intersect(int xa, // line 1 point 1 x
								int ya, // line 1 point 1 y
								int xb, // line 1 point 2 x
								int yb, // line 1 point 2 y
								int xc, // line 2 point 1 x
								int yc, // line 2 point 1 y
								int xd, // line 2 point 2 x
								int yd) { // line 2 point 2 y

		// source: http://vision.dai.ed.ac.uk/andrewfg/c-g-a-faq.html
		// eq: for lines AB and CD
		//     (YA-YC)(XD-XC)-(XA-XC)(YD-YC)
		// r = -----------------------------  (eqn 1)
		//     (XB-XA)(YD-YC)-(YB-YA)(XD-XC)
		//
		//     (YA-YC)(XB-XA)-(XA-XC)(YB-YA)
		// s = -----------------------------  (eqn 2)
		//     (XB-XA)(YD-YC)-(YB-YA)(XD-XC)
		//  XI = XA + r(XB-XA)
		//  YI = YA + r(YB-YA)
	
		double denom = ((xb - xa) * (yd - yc) - (yb - ya) * (xd - xc));
	
		double rnum = ((ya - yc) * (xd - xc) - (xa - xc) * (yd - yc));

		if (denom == 0.0) { // parallel
			if (rnum == 0.0) { // coincident; pick one end of first line
				if ((xa < xb && (xb < xc || xb < xd)) ||
					(xa > xb && (xb > xc || xb > xd))) {
					return new Point(xb, yb);
				}
				else {
					return new Point(xa, ya);
				}
			}
			else {
				return null;
			}
		}

		double r = rnum / denom;
		double snum = ((ya - yc) * (xb - xa) - (xa - xc) * (yb - ya));
		double s = snum / denom;

		if (0.0 <= r && r <= 1.0 && 0.0 <= s && s <= 1.0) {
			int px = (int)(xa + (xb - xa) * r);
			int py = (int)(ya + (yb - ya) * r);
			return new Point(px, py);
		}
		else {
			return null;
		}
	}

	/**
	 * compute distance of point from line segment, or
	 * Double.MAX_VALUE if perpendicular projection is outside segment; or
	 * If pts on line are same, return distance from point
	 **/
	// from Doug Lea's PolygonFigure
	public static double distanceFromLine(int xa, int ya,
										int xb, int yb,
										int xc, int yc) {


		// source:http://vision.dai.ed.ac.uk/andrewfg/c-g-a-faq.html#q7
		//Let the point be C (XC,YC) and the line be AB (XA,YA) to (XB,YB).
		//The length of the
		//      line segment AB is L:
		//
		//                    ___________________
		//                   |        2         2
		//              L = \| (XB-XA) + (YB-YA)
		//and
		//
		//                  (YA-YC)(YA-YB)-(XA-XC)(XB-XA)
		//              r = -----------------------------
		//                              L**2
		//
		//                  (YA-YC)(XB-XA)-(XA-XC)(YB-YA)
		//              s = -----------------------------
		//                              L**2
		//
		//      Let I be the point of perpendicular projection of C onto AB, the
		//
		//              XI=XA+r(XB-XA)
		//              YI=YA+r(YB-YA)
		//
		//      Distance from A to I = r*L
		//      Distance from C to I = s*L
		//
		//      If r < 0 I is on backward extension of AB
		//      If r>1 I is on ahead extension of AB
		//      If 0<=r<=1 I is on AB
		//
		//      If s < 0 C is left of AB (you can just check the numerator)
		//      If s>0 C is right of AB
		//      If s=0 C is on AB

		int xdiff = xb - xa;
		int ydiff = yb - ya;
		long l2 = xdiff * xdiff + ydiff * ydiff;

		if (l2 == 0) {
			return Geom.length(xa, ya, xc, yc);
		}

		double rnum =  (ya - yc) * (ya - yb) - (xa - xc) * (xb - xa);
		double r = rnum / l2;

		if (r < 0.0 || r > 1.0) {
			return Double.MAX_VALUE;
		}

		double xi = xa + r * xdiff;
		double yi = ya + r * ydiff;
		double xd = xc - xi;
		double yd = yc - yi;
		return Math.sqrt(xd * xd + yd * yd);

		/*
			for directional version, instead use
			double snum =  (ya-yc) * (xb-xa) - (xa-xc) * (yb-ya);
			double s = snum / l2;

			double l = Math.sqrt((double)l2);
			return = s * l;
			*/
	}

	/**
	 * compute distance of point from line segment.<br>
	 * Uses AWT Line2D utility methods
	 */
	public static double distanceFromLine2D(int xa, int ya,
										int xb, int yb,
										int xc, int yc) {
		Line2D.Double line = new Line2D.Double(xa, xb, ya, yb);
		return line.ptSegDist(xc, yc);
	}
}
