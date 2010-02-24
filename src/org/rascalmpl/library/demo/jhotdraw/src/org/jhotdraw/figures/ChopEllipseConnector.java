/*
 * @(#)ChopEllipseConnector.java
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
import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.Geom;

/**
 * A ChopEllipseConnector locates a connection point by
 * chopping the connection at the ellipse defined by the
 * figure's display box.
 *
 * @version <$CURRENT_VERSION$>
 */
public class ChopEllipseConnector extends ChopBoxConnector {

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -3165091511154766610L;

	public ChopEllipseConnector() {
	}

	public ChopEllipseConnector(Figure owner) {
		super(owner);
	}

	protected Point chop(Figure target, Point from) {
		Rectangle r = target.displayBox();
		double angle = Geom.pointToAngle(r, from);
		return Geom.ovalAngleToPoint(r, angle);
	}
}

