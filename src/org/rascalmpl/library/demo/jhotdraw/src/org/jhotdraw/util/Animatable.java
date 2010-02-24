/*
 * @(#)Animatable.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

/**
 * Animatable defines a simple animation interface
 *
 * @version <$CURRENT_VERSION$>
 */
public interface Animatable {
	/**
	 * Perform a step of the animation.
	 */
	void animationStep();
}
