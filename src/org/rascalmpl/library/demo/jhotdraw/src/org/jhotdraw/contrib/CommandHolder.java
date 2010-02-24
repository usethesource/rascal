/*
 *  @(#)CommandMenu.java
 *
 *  Project:		JHotdraw - a GUI framework for technical drawings
 *  http://www.jhotdraw.org
 *  http://jhotdraw.sourceforge.net
 *  Copyright:	© by the original author(s) and all contributors
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib;

import org.jhotdraw.util.Command;

/**
 * CommandHolder defines the interface for wrapper objects holding a Command,
 * like menu items or toolbar tools.
 *
 * @author    Eduardo Francos - InContext
 * @created   8 mai 2002
 * @version   <$CURRENT_VERSION$>
 */

public interface CommandHolder {
	/**
	 * Gets the command of the CommandHolder object
	 *
	 * @return   The command value
	 */
	public Command getCommand();


	/**
	 * Sets the command of the CommandHolder object
	 *
	 * @param newCommand  The new command value
	 */
	public void setCommand(Command newCommand);
}
