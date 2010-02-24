/*
 * @(#)PopupMenuFigureSelection.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
 
package org.jhotdraw.contrib;

import org.jhotdraw.framework.*;

/**
 * An interface which allows a popup menu to interact with its Figure to
 * which it is associated.
 *
 * @author      Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public interface PopupMenuFigureSelection {

	/**
	 * Set the figure which was selected when the popup menu was invoked.
	 *
	 * @param   newSelectedFigure   figure which is selected (typically be a SelectionTool)
	 */
	public void setSelectedFigure(Figure newSelectedFigure);

	/**
	 * Get the figure which was selected when the popup menu was invoked.
	 *
	 * @return  figure which is selected (typically be a SelectionTool)
	 */
	public Figure getSelectedFigure();
}
