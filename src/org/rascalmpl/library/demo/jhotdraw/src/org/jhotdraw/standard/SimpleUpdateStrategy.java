/*
 * @(#)SimpleUpdateStrategy.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.*;
import org.jhotdraw.framework.*;

/**
 * The SimpleUpdateStrategy implements an update
 * strategy that directly redraws a DrawingView.
 *
 * @see DrawingView
 *
 * @version <$CURRENT_VERSION$>
 */
public  class SimpleUpdateStrategy implements Painter {

	/*
	 * Serialization support. In JavaDraw only the Drawing is serialized.
	 * However, for beans support SimpleUpdateStrategy supports
	 * serialization
	 */
	private static final long serialVersionUID = -7539925820692134566L;

	/**
	* Draws the view contents.
	*/
	public void draw(Graphics g, DrawingView view) {
		view.drawAll(g);
	}
}
