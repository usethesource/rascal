/*
 * @(#)Bounds.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/** 
 * This class is a rectangle with floating point
 * dimensions and location.  This class provides
 * many convenient geometrical methods related to
 * rectangles.  Basically, this class is like
 * java.awt.geom.Rectangle2D with some extra
 * functionality.
 *
 * @author WMG (28.02.1999)
 * @version <$CURRENT_VERSION$>
 */
public class Bounds implements Serializable {

	//_________________________________________________________VARIABLES

	protected double   _dX1 = 0;
	protected double   _dY1 = 0;
	protected double   _dX2 = 0;
	protected double   _dY2 = 0;

	//______________________________________________________CONSTRUCTORS

	public Bounds(double x, double y) {
		_dX1 = x;
		_dX2 = x;
		_dY1 = y;
		_dY2 = y;
	}

	public Bounds(double x1, double y1, double x2, double y2) {
		_dX1 = Math.min(x1, x2);
		_dX2 = Math.max(x1, x2);
		_dY1 = Math.min(y1, y2);
		_dY2 = Math.max(y1, y2);
	}

	public Bounds(Point2D aPoint2D) {
		this(aPoint2D.getX(), aPoint2D.getY());
	}

	public Bounds(Point2D firstPoint2D, Point2D secondPoint2D) {
		this(firstPoint2D.getX(), firstPoint2D.getY(),
		secondPoint2D.getX(), secondPoint2D.getY());
	}

	public Bounds(Bounds aBounds) {
		this(aBounds.getLesserX(), aBounds.getLesserY(),
		aBounds.getGreaterX(), aBounds.getGreaterY());
	}

	public Bounds(Rectangle2D aRectangle2D) {
		_dX1 = aRectangle2D.getMinX();
		_dX2 = aRectangle2D.getMaxX();
		_dY1 = aRectangle2D.getMinY();
		_dY2 = aRectangle2D.getMaxY();
	}

	public Bounds(Point2D centerPoint2D, double dWidth, double dHeight) {
		_dX1 = centerPoint2D.getX() - (dWidth / 2.0);
		_dX2 = centerPoint2D.getX() + (dWidth / 2.0);
		_dY1 = centerPoint2D.getY() - (dHeight / 2.0);
		_dY2 = centerPoint2D.getY() + (dHeight / 2.0);
	}

	public Bounds(Dimension aDimension) {
		this(0, 0, aDimension.width, aDimension.height);
	}

	protected Bounds() {
		// empty constructor
	}

	//____________________________________________________PUBLIC METHODS

	public double getLesserX() {
		return _dX1;
	}

	public double getGreaterX() {
		return _dX2;
	}

	public double getLesserY() {
		return _dY1;
	}

	public double getGreaterY() {
		return _dY2;
	}

	public double getWest() {
		return _dX1;
	}

	public double getEast() {
		return _dX2;
	}

	public double getSouth() {
		return _dY1;
	}

	public double getNorth() {
		return _dY2;
	}

	public double getWidth() {
		return _dX2 - _dX1;
	}

	public double getHeight() {
		return _dY2 - _dY1;
	}

	public Rectangle2D asRectangle2D() {
		return new Rectangle2D.Double(getLesserX(), getLesserY(),
		getWidth(), getHeight());
	}

	public void setCenter(Point2D centerPoint2D) {
		if(centerPoint2D == null) {
			throw new IllegalArgumentException();
		}
		Point2D currentCenterPoint2D = getCenter();
		double dDeltaX = centerPoint2D.getX() - currentCenterPoint2D.getX();
		double dDeltaY = centerPoint2D.getY() - currentCenterPoint2D.getY();
		offset(dDeltaX, dDeltaY);
	}

	public Point2D getCenter() {
		return new Point2D.Double((_dX1 + _dX2) / 2.0, (_dY1 + _dY2) / 2.0);
	}

	public void zoomBy(double dRatio) {
		double dWidth = _dX2 - _dX1;
		double dHeight = _dY2 - _dY1;
		double dNewWidth = (dWidth * dRatio);
		double dNewHeight = (dHeight * dRatio);
		Point2D centerPoint2D = getCenter();
		_dX1 = centerPoint2D.getX() - (dNewWidth / 2.0);
		_dY1 = centerPoint2D.getY() - (dNewHeight / 2.0);
		_dX2 = centerPoint2D.getX() + (dNewWidth / 2.0);
		_dY2 = centerPoint2D.getY() + (dNewHeight / 2.0);
	}

	public void shiftBy(int nXPercentage, int nYPercentage) {
		double dWidth = _dX2 - _dX1;
		double dHeight = _dY2 - _dY1;
		double dDeltaX = (dWidth * nXPercentage) / 100.0;
		double dDeltaY = (dHeight * nYPercentage) / 100.0;
		offset(dDeltaX, dDeltaY);
	}

	public void offset(double dDeltaX, double dDeltaY) {
		_dX1 += dDeltaX;
		_dX2 += dDeltaX;
		_dY1 += dDeltaY;
		_dY2 += dDeltaY;
	}

	/**
	 * This will cause the bounds to grow until the given ratio
	 * is satisfied. The Ration is calculated by
	 * <code> getWidth() / getHeight() </code>
	 **/
	public void expandToRatio(double dRatio) {
		double dCurrentRatio = getWidth() / getHeight();
		if (dCurrentRatio < dRatio) {
			double dNewWidth = dRatio * getHeight();
			double dCenterX = (_dX1 + _dX2) / 2.0;
			double dDelta = dNewWidth / 2.0;
			_dX1 = dCenterX - dDelta;
			_dX2 = dCenterX + dDelta;
		}
		if (dCurrentRatio > dRatio) {
			double dNewHeight = getWidth() / dRatio;
			double dCenterY = (_dY1 + _dY2) / 2.0;
			double dDelta = dNewHeight / 2.0;
			_dY1 = dCenterY - dDelta;
			_dY2 = dCenterY + dDelta;
		}
	}

	public void includeXCoordinate(double x) {
		_dX1 = min(_dX1, _dX2, x);
		_dX2 = max(_dX1, _dX2, x);
	}

	public void includeYCoordinate(double y) {
		_dY1 = min(_dY1, _dY2, y);
		_dY2 = max(_dY1, _dY2, y);
	}

	public void includePoint(double x, double y) {
		includeXCoordinate(x);
		includeYCoordinate(y);
	}

	public void includePoint(Point2D aPoint2D) {
		includePoint(aPoint2D.getX(), aPoint2D.getY());
	}

	public void includeLine(double x1, double y1, double x2, double y2) {
		includePoint(x1, y1);
		includePoint(x2, y2);
	}

	public void includeLine(Point2D onePoint2D, Point2D twoPoint2D) {
		includeLine(onePoint2D.getX(), onePoint2D.getY(),
		twoPoint2D.getX(), twoPoint2D.getY());
	}

	public void includeBounds(Bounds aBounds) {
		includeXCoordinate(aBounds.getLesserX());
		includeXCoordinate(aBounds.getGreaterX());
		includeYCoordinate(aBounds.getLesserY());
		includeYCoordinate(aBounds.getGreaterY());
	}

	public void includeRectangle2D(Rectangle2D aRectangle2D) {
		includeXCoordinate(aRectangle2D.getMinX());
		includeXCoordinate(aRectangle2D.getMaxX());
		includeYCoordinate(aRectangle2D.getMinY());
		includeYCoordinate(aRectangle2D.getMaxY());
	}

	public void intersect(Bounds aBounds) {
		_dX1 = Math.max(_dX1, aBounds.getLesserX());
		_dY1 = Math.max(_dY1, aBounds.getLesserY());
		_dX2 = Math.min(_dX2, aBounds.getGreaterX());
		_dY2 = Math.min(_dY2, aBounds.getGreaterY());

		if (_dX1 > _dX2) {
			_dX1 = _dX2;
		}
		if (_dY1 > _dY2) {
			_dY1 = _dY2;
		}
	}

	public boolean intersectsPoint(double x, double y) {
		return ((_dX1 <= x) && (x <= _dX2) && (_dY1 <= y) && (y <= _dY2));
	}

	public boolean intersectsPoint(Point2D aPoint2D) {
		return intersectsPoint(aPoint2D.getX(), aPoint2D.getY());
	}


	public boolean intersectsLine(double x1, double y1, double x2, double y2) {
		if (intersectsPoint(x1, y1)) {
			return true;
		}
		if (intersectsPoint(x2, y2)) {
			return true;
		}
		if ((x1 < _dX1) && (x2 < _dX1)) {
			return false;
		}
		if ((x1 > _dX2) && (x2 > _dX2)) {
			return false;
		}
		if ((y1 < _dY1) && (y2 < _dY1)) {
			return false;
		}
		if ((y1 > _dY2) && (y2 > _dY2)) {
			return false;
		}
		if (((_dX1 <= x1) && (x1 <= _dX2)) && ((_dX1 <= x2) && (x2 <= _dX2))) {
			return true;
		}
		if (((_dY1 <= y1) && (y1 <= _dY2)) && ((_dY1 <= y2) && (y2 <= _dY2))) {
			return true;
		}
	
		double dSlope = (y2-y1) / (x2-x1);
		double _dYIntersectionAtX1 = dSlope * (_dX1 - x1) + y1;
		double _dYIntersectionAtX2 = dSlope * (_dX2 - x1) + y1;
		double _dXIntersectionAtY1 = (_dY1 - y1) / dSlope +  x1;
		double _dXIntersectionAtY2 = (_dY2 - y1) / dSlope +  x1;

		return (intersectsPoint(_dX1, _dYIntersectionAtX1)) ||
			(intersectsPoint(_dX2, _dYIntersectionAtX2)) ||
			(intersectsPoint(_dXIntersectionAtY1, _dY1)) ||
			(intersectsPoint(_dXIntersectionAtY2, _dY2));
	}

	public boolean intersectsLine(Point2D onePoint2D, Point2D twoPoint2D) {
		return intersectsLine(onePoint2D.getX(), onePoint2D.getY(),
			twoPoint2D.getX(), twoPoint2D.getY());
	}

	//use K-map to simplify
	public boolean intersectsBounds(Bounds aBounds) {
		double dLesserX = aBounds.getLesserX();
		double dGreaterX = aBounds.getGreaterX();
		double dLesserY = aBounds.getLesserY();
		double dGreaterY = aBounds.getGreaterY();

		if (dLesserX < _dX1) {
			if (dLesserY < _dY1) {
				return ((dGreaterX >= _dX1) && (dGreaterY >= _dY1));
			}
			else {
				return ((dGreaterX >= _dX1) && (dLesserY <= _dY2));
			}
		}
		else {
			if (dLesserY < _dY1) {
				return ((dLesserX <= _dX2) && (dGreaterY >= _dY1));
			}
			else {
				return ((dLesserX <= _dX2) && (dLesserY <= _dY2));
			}
		}
	}

	public boolean completelyContainsLine(double x1, double y1, double x2, double y2) {
		return (_dX1 > Math.min(x1, x2)) &&
			(_dX2 < Math.max(x1, x2)) &&
			(_dY1 > Math.min(y1, y2)) &&
			(_dY2 < Math.max(y1, y2));
	}

	public boolean isCompletelyInside(Bounds aBounds) {
		return (_dX1 > aBounds.getLesserX()) &&
			(_dX2 < aBounds.getGreaterX()) &&
			(_dY1 > aBounds.getLesserY()) &&
			(_dY2 < aBounds.getGreaterY());
	}

	public Point2D[] cropLine(double x1, double y1, double x2, double y2) {
		if (!intersectsLine(x1, y1, x2, y2)) {
			return null;
		}

		Point2D[] resultLine = new Point2D[2];
		Point2D beginPoint2D = new Point2D.Double(x1, y1);
		Point2D endPoint2D = new Point2D.Double(x2, y2);

		if (beginPoint2D.getX() == endPoint2D.getX()) {
			if (beginPoint2D.getY() > _dY2) {
				beginPoint2D.setLocation(beginPoint2D.getX(), _dY2);
			}
			if (endPoint2D.getY() > _dY2) {
				endPoint2D.setLocation(endPoint2D.getX(), _dY2);
			}
			if (beginPoint2D.getY() < _dY1) {
				beginPoint2D.setLocation(beginPoint2D.getX(), _dY1);
			}
			if (endPoint2D.getY() < _dY1) {
				endPoint2D.setLocation(endPoint2D.getX(), _dY1);
			}
		}
		else if (beginPoint2D.getY() == endPoint2D.getY()) {
			if (beginPoint2D.getX() > _dX2) {
				beginPoint2D.setLocation(_dX2, beginPoint2D.getY());
			}
			if (endPoint2D.getX() > _dX2) {
				endPoint2D.setLocation(_dX2, endPoint2D.getY());
			}
			if (beginPoint2D.getX() < _dX1) {
				beginPoint2D.setLocation(_dX1, beginPoint2D.getY());
			}
			if (endPoint2D.getX() < _dX1) {
				endPoint2D.setLocation(_dX1, endPoint2D.getY());
			}
		}
		else {
			double dSlope = (beginPoint2D.getY() - endPoint2D.getY()) 
				/ (beginPoint2D.getX() - endPoint2D.getX());

			if (!intersectsPoint(beginPoint2D)) {
				if (beginPoint2D.getY() > _dY2) {
					double x = ((_dY2 - beginPoint2D.getY()) / dSlope) + beginPoint2D.getX();
					if ((x >= _dX1) && (x <= _dX2)) {
						beginPoint2D.setLocation(x, beginPoint2D.getY());
						beginPoint2D.setLocation(beginPoint2D.getX(), _dY2);
					}
				}
				if (beginPoint2D.getY() < _dY1) {
					double x = ((_dY1 - beginPoint2D.getY()) / dSlope) + beginPoint2D.getX();
					if ((x >= _dX1) && (x <= _dX2)) {
						beginPoint2D.setLocation(x, beginPoint2D.getY());
						beginPoint2D.setLocation(beginPoint2D.getX(), _dY1);
					}
				}
				if (beginPoint2D.getX() > _dX2) {
					double y = dSlope*(_dX2 - beginPoint2D.getX()) + beginPoint2D.getY();
					if ((y >= _dY1) && (y <= _dY2)) {
						beginPoint2D.setLocation(_dX2, beginPoint2D.getY());
						beginPoint2D.setLocation(beginPoint2D.getX(), y);
					}
				}
				if (beginPoint2D.getX() < _dX1) {
					double y = dSlope*(_dX1 - beginPoint2D.getX()) + beginPoint2D.getY();
					if ((y >= _dY1) && (y <= _dY2)) {
						beginPoint2D.setLocation(_dX1, beginPoint2D.getY());
						beginPoint2D.setLocation(beginPoint2D.getX(), y);
					}
				}
			}
			if (!intersectsPoint(endPoint2D)) {
				if (endPoint2D.getY() > _dY2) {
					double x = ((_dY2 - beginPoint2D.getY()) / dSlope) + beginPoint2D.getX();
					if ((x >= _dX1) && (x <= _dX2)) {
						endPoint2D.setLocation(x, endPoint2D.getY());
						endPoint2D.setLocation(endPoint2D.getX(), _dY2);
					}
				}
				if (endPoint2D.getY() < _dY1) {
					double x = ((_dY1 - beginPoint2D.getY()) / dSlope) + beginPoint2D.getX();
					if ((x >= _dX1) && (x <= _dX2)) {
						endPoint2D.setLocation(x, endPoint2D.getY());
						endPoint2D.setLocation(endPoint2D.getX(), _dY1);
					}
				}
				if (endPoint2D.getX() > _dX2) {
					double y = dSlope*(_dX2 - beginPoint2D.getX()) + beginPoint2D.getY();
					if ((y >= _dY1) && (y <= _dY2)) {
						endPoint2D.setLocation(_dX2, endPoint2D.getY());
						endPoint2D.setLocation(endPoint2D.getX(), y);
					}
				}
				if (endPoint2D.getX() < _dX1) {
					double y = dSlope*(_dX1 - beginPoint2D.getX()) + beginPoint2D.getY();
					if ((y >= _dY1) && (y <= _dY2)) {
						endPoint2D.setLocation(_dX1, endPoint2D.getY());
						endPoint2D.setLocation(endPoint2D.getX(), y);
					}
				}
			}
		}

		resultLine[0] = beginPoint2D;
		resultLine[1] = endPoint2D;

		return resultLine;
	}

	public boolean equals(Object anObject) {
		if ((anObject == null) || (!(anObject instanceof Bounds))) {
			return false;
		}
		Bounds aBounds = (Bounds) anObject;

		if ((_dX1 == aBounds.getLesserX()) &&
				(_dX2 == aBounds.getGreaterX()) &&
				(_dY1 == aBounds.getLesserY()) &&
				(_dY2 == aBounds.getGreaterY())) {
			return true;
		}

		return false;
	}

	public int hashCode() {
		double temp = Math.abs(_dX1 + _dX2 +_dY1 + _dY2);
		while ((temp != 0) && (temp < 1)) {
			temp *= 4;
		}

		return (int) temp;
	}

	public String toString() {
		return Double.toString(_dX1) + " " + Double.toString(_dY1)
			+ " " + Double.toString(_dX2) + " " + Double.toString(_dY2);
	}

	private double min( double x1, double x2, double x3 ) {
		return Math.min( Math.min( x1, x2 ), x3 );
	}

	private double max( double x1, double x2, double x3 ) {
		return Math.max( Math.max( x1, x2 ), x3 );
	}
}
