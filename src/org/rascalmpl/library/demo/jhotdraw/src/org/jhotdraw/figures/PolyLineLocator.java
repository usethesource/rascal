/*
 * @(#)PolyLineLocator.java
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

/**
 * A poly line figure consists of a list of points.
 * It has an optional line decoration at the start and end.
 *
 * @see LineDecoration
 *
 * @version <$CURRENT_VERSION$>
 */
class PolyLineLocator extends AbstractLocator {
	int fIndex;

	public PolyLineLocator(int index) {
		fIndex = index;
	}

	public Point locate(Figure owner) {
		PolyLineFigure plf = (PolyLineFigure)owner;
		// guard against changing PolyLineFigures -> temporary hack
		if (fIndex < plf.pointCount()) {
			return ((PolyLineFigure)owner).pointAt(fIndex);
		}
		return new Point(0, 0);
	}
}
