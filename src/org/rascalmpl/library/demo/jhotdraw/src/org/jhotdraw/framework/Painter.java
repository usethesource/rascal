/*
 * @(#)Painter.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import java.awt.*;
import java.io.Serializable;

/**
 * Painter defines the interface for drawing a layer
 * into a DrawingView.<p>
 *
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld034.htm>Strategy</a></b><br>
 * Painter encapsulates an algorithm to render something in
 * the DrawingView. The DrawingView plays the role of the StrategyContext.
 * <hr>
 *
 * @see DrawingView
 *
 * @version <$CURRENT_VERSION$>
 */
public interface Painter extends Serializable {

	/**
	 * Draws into the given DrawingView.
	 */
	public void draw(Graphics g, DrawingView view);

}
