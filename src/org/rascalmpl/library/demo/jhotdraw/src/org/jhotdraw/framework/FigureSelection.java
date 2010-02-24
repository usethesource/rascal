/*
 * @(#)FigureSelection.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

/**
 * FigureSelection enables to transfer the selected figures
 * to a clipboard.<p>
 * Will soon be converted to the JDK 1.1 Transferable interface.
 *
 * @see org.jhotdraw.util.Clipboard
 *
 * @version <$CURRENT_VERSION$>
 */

public interface FigureSelection {

	/**
	 * Gets the type of the selection.
	 */
	public String getType();

	/**
	 * Gets the data of the selection. The result is returned
     * as a FigureEnumeration of Figures.
	 *
	 * @return a copy of the figure selection.
	 */
	public Object getData(String type);
}

