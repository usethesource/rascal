/*
 * @(#)ElbowConnection.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.awt.*;
import java.util.List;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.*;

/**
 * A LineConnection that constrains a connection to
 * orthogonal lines.
 *
 * @version <$CURRENT_VERSION$>
 */
public  class ElbowConnection extends LineConnection {

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 2193968743082078559L;
	private int elbowConnectionSerializedDataVersion = 1;

	public ElbowConnection() {
		super();
	}

	public void updateConnection() {
		super.updateConnection();
		updatePoints();
	}

	public void layoutConnection() {
	}

	/**
	 * Gets the handles of the figure.
	 */
	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList(fPoints.size()*2);
		handles.add(new ChangeConnectionStartHandle(this));
		for (int i = 1; i < fPoints.size()-1; i++) {
			handles.add(new NullHandle(this, locator(i)));
		}
		handles.add(new ChangeConnectionEndHandle(this));
		for (int i = 0; i < fPoints.size()-1; i++) {
			handles.add(new ElbowHandle(this, i));
		}
		return new HandleEnumerator(handles);
	}

	public Locator connectedTextLocator(Figure f) {
		return new ElbowTextLocator();
	}

	protected void updatePoints() {
		willChange();
		Point start = startPoint();
		Point end = endPoint();
		fPoints.clear();
		fPoints.add(start);

		if (start.x == end.x || start.y == end.y) {
			fPoints.add(end);
		}
		else {

			Rectangle r1 = getStartConnector().owner().displayBox();
			Rectangle r2 = getEndConnector().owner().displayBox();

			int dir = Geom.direction(r1.x + r1.width/2, r1.y + r1.height/2,
						r2.x + r2.width/2, r2.y + r2.height/2);
			if (dir == Geom.NORTH || dir == Geom.SOUTH) {
				fPoints.add(new Point(start.x, (start.y + end.y)/2));
				fPoints.add(new Point(end.x, (start.y + end.y)/2));
			}
			else {
				fPoints.add(new Point((start.x + end.x)/2, start.y));
				fPoints.add(new Point((start.x + end.x)/2, end.y));
			}
			fPoints.add(end);
		}
		changed();
	}
}

class ElbowTextLocator extends AbstractLocator {
	public Point locate(Figure owner) {
		Point p = owner.center();
		return new Point(p.x, p.y-10); // hack
	}
}

