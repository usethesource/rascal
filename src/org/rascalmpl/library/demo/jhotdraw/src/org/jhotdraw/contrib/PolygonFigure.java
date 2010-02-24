/*
 * @(#)PolygonFigure.java
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
import org.jhotdraw.util.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.figures.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.IOException;

/**
 * A scalable, rotatable polygon with an arbitrary number of points
 * Based on PolyLineFigure
 *
 * @author Doug Lea  (dl at gee, Fri Feb 28 07:47:05 1997)
 * @version <$CURRENT_VERSION$>
 */
public  class PolygonFigure extends AttributeFigure {

	/**
	 * Distance threshold for smoothing away or locating points
	 **/
	static final int TOO_CLOSE = 2;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 6254089689239215026L;
	private int polygonFigureSerializedDataVersion = 1;

	private Polygon fPoly;

	public PolygonFigure() {
		super();
		setInternalPolygon(new Polygon());
	}

	public PolygonFigure(int x, int y) {
		this();
		getInternalPolygon().addPoint(x, y);
	}

	public PolygonFigure(Polygon p) {
		setInternalPolygon(new Polygon(p.xpoints, p.ypoints, p.npoints));
	}

	public Rectangle displayBox() {
		return bounds(getInternalPolygon());
	}


	public boolean isEmpty() {
		return ((pointCount() < 3) ||
				((size().width < TOO_CLOSE) && (size().height < TOO_CLOSE)));
	}

	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList(pointCount());
		for (int i = 0; i < pointCount(); i++) {
			handles.add(new PolygonHandle(this, locator(i), i));
		}
		handles.add(new PolygonScaleHandle(this));
		return new HandleEnumerator(handles);
	}


	public void basicDisplayBox(Point origin, Point corner) {
		Rectangle r = displayBox();
		int dx = origin.x - r.x;
		int dy = origin.y - r.y;
		getInternalPolygon().translate(dx, dy);
		r = displayBox();
		Point oldCorner = new Point(r.x + r.width, r.y + r.height);
		scaleRotate(oldCorner, getInternalPolygon(), corner);
	}

	/**
	 * @return a copy of the internal polygon
	 **/
	public Polygon getPolygon() {
		return new Polygon(fPoly.xpoints, fPoly.ypoints, fPoly.npoints);
	}

	protected void setInternalPolygon(Polygon newPolygon) {
		fPoly = newPolygon;
	}

	public Polygon getInternalPolygon() {
		return fPoly;
	}

	public Point center() {
		return center(getInternalPolygon());
	}

	public Iterator points() {
		List pts = CollectionsFactory.current().createList(pointCount());
		for (int i = 0; i < pointCount(); ++i) {
			pts.add(new Point(getInternalPolygon().xpoints[i],
									 getInternalPolygon().ypoints[i]));
		}
		return pts.iterator();
	}

	public int pointCount() {
		return getInternalPolygon().npoints;
	}

	public void basicMoveBy(int dx, int dy) {
		getInternalPolygon().translate(dx, dy);
	}

	public void drawBackground(Graphics g) {
		g.fillPolygon(getInternalPolygon());
	}

	public void drawFrame(Graphics g) {
		g.drawPolygon(getInternalPolygon());
	}

	public boolean containsPoint(int x, int y) {
		return getInternalPolygon().contains(x, y);
	}

	public Connector connectorAt(int x, int y) {
		return new ChopPolygonConnector(this);
	}

	/**
	 * Adds a node to the list of points.
	 */
	public void addPoint(int x, int y) {
		getInternalPolygon().addPoint(x, y);
		changed();
	}


	/**
	 * Changes the position of a node.
	 */
	public void setPointAt(Point p, int i) {
		willChange();
		getInternalPolygon().xpoints[i] = p.x;
		getInternalPolygon().ypoints[i] = p.y;
		changed();
	}

	/**
	 * Insert a node at the given point.
	 */
	public void insertPointAt(Point p, int i) {
		willChange();
		int n = pointCount() + 1;
		int[] xs = new int[n];
		int[] ys = new int[n];
		for (int j = 0; j < i; ++j) {
			xs[j] = getInternalPolygon().xpoints[j];
			ys[j] = getInternalPolygon().ypoints[j];
		}
		xs[i] = p.x;
		ys[i] = p.y;
		for (int j = i; j < pointCount(); ++j) {
			xs[j + 1] = getInternalPolygon().xpoints[j];
			ys[j + 1] = getInternalPolygon().ypoints[j];
		}

		setInternalPolygon(new Polygon(xs, ys, n));
		changed();
	}

	public void removePointAt(int i) {
		willChange();
		int n = pointCount() - 1;
		int[] xs = new int[n];
		int[] ys = new int[n];
		for (int j = 0; j < i; ++j) {
			xs[j] = getInternalPolygon().xpoints[j];
			ys[j] = getInternalPolygon().ypoints[j];
		}
		for (int j = i; j < n; ++j) {
			xs[j] = getInternalPolygon().xpoints[j + 1];
			ys[j] = getInternalPolygon().ypoints[j + 1];
		}
		setInternalPolygon(new Polygon(xs, ys, n));
		changed();
	}

	/**
	 * Scale and rotate relative to anchor
	 **/
	public  void scaleRotate(Point anchor, Polygon originalPolygon, Point p) {
		willChange();

		// use center to determine relative angles and lengths
		Point ctr = center(originalPolygon);
		double anchorLen = Geom.length(ctr.x, ctr.y, anchor.x, anchor.y);

		if (anchorLen > 0.0) {
			double newLen = Geom.length(ctr.x, ctr.y, p.x, p.y);
			double ratio = newLen / anchorLen;

			double anchorAngle = Math.atan2(anchor.y - ctr.y, anchor.x - ctr.x);
			double newAngle = Math.atan2(p.y - ctr.y, p.x - ctr.x);
			double rotation = newAngle - anchorAngle;

			int n = originalPolygon.npoints;
			int[] xs = new int[n];
			int[] ys = new int[n];

			for (int i = 0; i < n; ++i) {
				int x = originalPolygon.xpoints[i];
				int y = originalPolygon.ypoints[i];
				double l = Geom.length(ctr.x, ctr.y, x, y) * ratio;
				double a = Math.atan2(y - ctr.y, x - ctr.x) + rotation;
				xs[i] = (int)(ctr.x + l * Math.cos(a) + 0.5);
				ys[i] = (int)(ctr.y + l * Math.sin(a) + 0.5);
			}
			setInternalPolygon(new Polygon(xs, ys, n));
		}
		changed();
	}


	/**
	 * Remove points that are nearly colinear with others
	 **/
	public void smoothPoints() {
		willChange();
		boolean removed = false;
		int n = pointCount();
		do {
			removed = false;
			int i = 0;
			while (i < n && n >= 3) {
				int nxt = (i + 1) % n;
				int prv = (i - 1 + n) % n;

				if ((Geom.distanceFromLine(getInternalPolygon().xpoints[prv],
						getInternalPolygon().ypoints[prv],
						getInternalPolygon().xpoints[nxt],
						getInternalPolygon().ypoints[nxt],
						getInternalPolygon().xpoints[i],
						getInternalPolygon().ypoints[i]) < TOO_CLOSE)) {
					removed = true;
					--n;
					for (int j = i; j < n; ++j) {
						getInternalPolygon().xpoints[j] = getInternalPolygon().xpoints[j + 1];
						getInternalPolygon().ypoints[j] = getInternalPolygon().ypoints[j + 1];
					}
				}
				else {
					++i;
				}
			}
		} while(removed);
		if (n != pointCount()) {
			setInternalPolygon(new Polygon(getInternalPolygon().xpoints,
											getInternalPolygon().ypoints, n));
		}
		changed();
	}

	/**
	 * Splits the segment at the given point if a segment was hit.
	 * @return the index of the segment or -1 if no segment was hit.
	 */
	public int splitSegment(int x, int y) {
		int i = findSegment(x, y);
		if (i != -1) {
			insertPointAt(new Point(x, y), i+1);
			return i + 1;
		}
		else {
			return -1;
		}
	}

	public Point pointAt(int i) {
		return new Point(getInternalPolygon().xpoints[i],
						 getInternalPolygon().ypoints[i]);
	}

	/**
	 * Return the point on the polygon that is furthest from the center
	 **/
	public Point outermostPoint() {
		Point ctr = center();
		int outer = 0;
		long dist = 0;

		for (int i = 0; i < pointCount(); ++i) {
			long d = Geom.length2(ctr.x, ctr.y,
								getInternalPolygon().xpoints[i],
								getInternalPolygon().ypoints[i]);
			if (d > dist) {
				dist = d;
				outer = i;
			}
		}

		return new Point(getInternalPolygon().xpoints[outer], getInternalPolygon().ypoints[outer]);
	}


	/**
	 * Gets the segment that is hit by the given point.
	 * @return the index of the segment or -1 if no segment was hit.
	 */
	public int findSegment(int x, int y) {
		double dist = TOO_CLOSE;
		int best = -1;

		for (int i = 0; i < pointCount(); i++) {
			int n = (i + 1) % pointCount();
			double d =  Geom.distanceFromLine(getInternalPolygon().xpoints[i],
							getInternalPolygon().ypoints[i],
							getInternalPolygon().xpoints[n],
							getInternalPolygon().ypoints[n], x, y);
			if (d < dist) {
				dist = d;
				best = i;
			}
		}
		return best;
	}

	public Point chop(Point p) {
		return chop(getInternalPolygon(), p);
	}

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(pointCount());
		for (int i = 0; i < pointCount(); ++i) {
			dw.writeInt(getInternalPolygon().xpoints[i]);
			dw.writeInt(getInternalPolygon().ypoints[i]);
		}
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		int size = dr.readInt();
		int[] xs = new int[size];
		int[] ys = new int[size];
		for (int i = 0; i < size; i++) {
			xs[i] = dr.readInt();
			ys[i] = dr.readInt();
		}
		setInternalPolygon(new Polygon(xs, ys, size));
	}

	/**
	 * Creates a locator for the point with the given index.
	 */
	public static Locator locator(final int pointIndex) {
		return new AbstractLocator() {
			public Point locate(Figure owner) {
				PolygonFigure plf = (PolygonFigure)owner;
				// guard against changing PolygonFigures -> temporary hack
				if (pointIndex < plf.pointCount()) {
					return ((PolygonFigure)owner).pointAt(pointIndex);
				}
				return new Point(-1, -1);
			}
		};
	}

	/**
	 * replacement for builtin Polygon.getBounds that doesn't always update?
	 */
	public static Rectangle bounds(Polygon p) {
		int minx = Integer.MAX_VALUE;
		int miny = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE;
		int maxy = Integer.MIN_VALUE;
		int n = p.npoints;
		for (int i = 0; i < n; i++) {
			int x = p.xpoints[i];
			int y = p.ypoints[i];
			if (x > maxx) {
				maxx = x;
			}
			if (x < minx) {
				minx = x;
			}
			if (y > maxy) {
				maxy = y;
			}
			if (y < miny) {
				miny = y;
			}
		}

		return new Rectangle(minx, miny, maxx - minx, maxy - miny);
	}

	public static Point center(Polygon p) {
		long sx = 0;
		long sy = 0;
		int n = p.npoints;
		for (int i = 0; i < n; i++) {
			sx += p.xpoints[i];
			sy += p.ypoints[i];
		}

		return new Point((int)(sx / n), (int)(sy / n));
	}

	public static Point chop(Polygon poly, Point p) {
		Point ctr = center(poly);
		int cx = -1;
		int cy = -1;
		long len = Long.MAX_VALUE;

		// Try for points along edge

		for (int i = 0; i < poly.npoints; ++i) {
			int nxt = (i + 1) % poly.npoints;
			Point chop = Geom.intersect(poly.xpoints[i],
										 poly.ypoints[i],
										 poly.xpoints[nxt],
										 poly.ypoints[nxt],
										 p.x,
										 p.y,
										 ctr.x,
										 ctr.y);
			if (chop != null) {
				long cl = Geom.length2(chop.x, chop.y, p.x, p.y);
				if (cl < len) {
					len = cl;
					cx = chop.x;
					cy = chop.y;
				}
			}
		}
		// if none found, pick closest vertex
		//if (len ==  Long.MAX_VALUE) {
		{ // try anyway
			for (int i = 0; i < poly.npoints; ++i) {
				long l = Geom.length2(poly.xpoints[i], poly.ypoints[i], p.x, p.y);
				if (l < len) {
					len = l;
					cx = poly.xpoints[i];
					cy = poly.ypoints[i];
				}
			}
		}
		return new Point(cx, cy);
	}
}
