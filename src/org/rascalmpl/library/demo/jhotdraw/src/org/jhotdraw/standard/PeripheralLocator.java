/*
 * @(#)PeripheralLocator.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.Point;
import org.jhotdraw.framework.Figure;
import java.awt.*;
import java.lang.Math;

/**
 * @author  C.L.Gilbert <dnoyeb@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class PeripheralLocator extends AbstractLocator {
	private static int CORNERSPACE = 1;
	private Figure fOwner;
	private int fPPS;
	private int fIndex;

	private PeripheralLocator() {
		// do nothing: for JDO-compliance only
	}

	public PeripheralLocator(int pointsPerSide, int index) {
		fPPS = pointsPerSide;
		fIndex = index;
		if (index >= pointsPerSide *4) {
			throw new IllegalArgumentException("Index must be within the range of points starting with index = 0.");
		}
	}

    public Point locate(Figure parm1) {
		Rectangle r = parm1.displayBox();
		/* calculate total length for spacing */
		//int circumference = r.width*2 + r.height*2;
		/* subtrace corners spacing */
		//int insets = 2 * 4 * CORNERSPACE;

		//int spacing = circumference/(fPPS*4 +4);
		float hSpacing = (float)r.width / (fPPS +1);
		float vSpacing = (float)r.height / (fPPS +1);

		int x = 0;
		int y = 0;
		if (fIndex < fPPS) {
			//north
			x = Math.round((fIndex + 1.0f ) * hSpacing);
			y = 0;
		}
		else if (fIndex < (fPPS*2)) {
			//east
			x = Math.round((fPPS + 1 ) * hSpacing) ;//r.width;
			y = Math.round((fIndex +1 - fPPS) * vSpacing);  //should be negative?
		}
		else if (fIndex < (fPPS*3)) {
			//south
			x = Math.round(((fPPS + 1 ) - (fIndex +1 - fPPS*2))* hSpacing);
			y = Math.round((fPPS + 1 )* vSpacing) /*r.height*/;
		}
		else {
			//west
			x = 0;
			y = Math.round(((fPPS +1) - (fIndex +1 - fPPS*3))*vSpacing);
		}
		x = x+r.x;
		y = y+r.y;
		return new Point((int)x, (int)y);
    }
}