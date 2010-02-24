/*
 * @(#)ChopDiamondConnector.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import java.awt.*;
import org.jhotdraw.framework.*;
import org.jhotdraw.standard.ChopBoxConnector;
import org.jhotdraw.util.Geom;

/**
 * A ChopDiamondConnector locates connection points by choping the
 * connection between the centers of the two figures at the edge of
 * a diamond figure.
 *
 * @see Connector
 *
 * @author Erich Gamma
 * @version <$CURRENT_VERSION$>
 */
public class ChopDiamondConnector extends ChopBoxConnector {
	/**
	 * Serialization support.
	 * Needs to be fixed.
	 */
	private static final long serialVersionUID = -1461450322512395462L;

	public ChopDiamondConnector() {
		// only used for Storable implementation
	}

  public ChopDiamondConnector(Figure owner) {
	  super(owner);
	}

	/**
	 * Return an appropriate connection point on the edge of a diamond figure
	 */
	protected Point chop(Figure target, Point from) {
		Rectangle r = target.displayBox();
		// Center point
		Point c1 = new Point(r.x + r.width/2, r.y + (r.height/2));
		Point p2 = new Point(r.x + r.width/2, r.y + r.height);
		Point p4 = new Point(r.x + r.width/2, r.y);

		// If overlapping, just return the opposite corners
		if (r.contains(from)) {
			if (from.y > r.y && from.y < (r.y +r.height/2)) {
				return p2;
			}
			else {
				return p4;
			}
		}

		// Calculate angle to determine quadrant
		double ang = Geom.pointToAngle(r, from);

		// Dermine line points
		Point p1 = new Point(r.x + r.width  , r.y + (r.height/2));
		Point p3 = new Point(r.x            , r.y + (r.height/2));
		Point rp = null; // This will be returned

		// Get the intersection with edges
		if (ang > 0 && ang < 1.57) {
			rp = Geom.intersect(p1.x, p1.y, p2.x, p2.y, c1.x, c1.y, from.x, from.y);
		}
		else if (ang > 1.575 && ang < 3.14) {
			rp = Geom.intersect(p2.x, p2.y, p3.x, p3.y, c1.x, c1.y, from.x, from.y);
		}
		else if (ang > -3.14 && ang < -1.575) {
		  rp = Geom.intersect(p3.x, p3.y, p4.x, p4.y, c1.x, c1.y, from.x, from.y);
		}
		else if (ang > -1.57 && ang < 0) {
			rp = Geom.intersect(p4.x, p4.y, p1.x, p1.y, c1.x, c1.y, from.x, from.y);
		}

		// No proper edge found, we should send one of four corners
		if (rp == null) {
			rp = Geom.angleToPoint(r, ang);
		}

		return rp;
	}
}
