/*
 * @(#)PaletteListener.java
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
 * Interface for handling palette events.
 *
 * @see PaletteButton
 *
 * @version <$CURRENT_VERSION$>
 */
public interface PaletteListener {
	/**
	 * The user selected a palette entry. The selected button is
	 * passed as an argument.
	 */
	public void paletteUserSelected(PaletteButton button);

	/**
	 * The user moved the mouse over the palette entry.
	 */
	public void paletteUserOver(PaletteButton button, boolean inside);
}
