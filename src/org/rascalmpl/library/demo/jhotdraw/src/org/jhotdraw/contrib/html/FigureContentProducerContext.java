/*
 * @(#)FigureContentProducerContext.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib.html;

import java.awt.Font;

import java.awt.Rectangle;

/**
 * FigureContentProducerContext defines the interface required of clients
 * requesting contents for Figure oriented ContentProducers.<br>
 *
 * @author  Eduardo Francos - InContext
 * @created 7 mai 2002
 * @version <$CURRENT_VERSION$>
 */
public interface FigureContentProducerContext extends ContentProducerContext {

	/**
	 * Returns the display box of the figure
	 *
	 * @return   the display box
	 */
	public Rectangle displayBox();

	/**
	 * Gets the font of the figure
	 *
	 * @return   The font
	 */
	public Font getFont();
}
