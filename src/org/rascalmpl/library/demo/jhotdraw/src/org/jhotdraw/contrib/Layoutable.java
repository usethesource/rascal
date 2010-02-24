/*
 * @(#)Layouter.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.framework.Figure;

/**
 * A Layoutable is a target for a Layouter who lays out the Layoutable
 * according to its layout algorithm
 *
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public interface Layoutable extends Figure {

	/**
	 * Layout the figure
	 */
	public void layout();

	/**
	 * Set the Layouter for this Layoutable
	 *
	 * @param newLayouter layouter
	 */
	public void setLayouter(Layouter newLayouter);
	
	/**
	 * Return the Layouter for this Layoutable
	 *
	 * @return layouter
	 */
	public Layouter getLayouter();
}