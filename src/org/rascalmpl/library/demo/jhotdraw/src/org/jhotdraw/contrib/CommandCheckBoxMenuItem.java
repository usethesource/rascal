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

import javax.swing.JCheckBoxMenuItem;
import org.jhotdraw.util.Command;
import javax.swing.Icon;

/**
 * CommandCheckBoxMenuItem implements a command that can be added to a menu
 * as a checkbox menu item
 *
 * @author Eduardo Francos - InContext
 * @version <$CURRENT_VERSION$>
 */

public class CommandCheckBoxMenuItem extends JCheckBoxMenuItem
	   implements CommandHolder
{
	Command fCommand;

	/**
	 * Creates an initially unselected check box menu item
	 * with the specified command
	 */
	public CommandCheckBoxMenuItem(Command command) {
		super(command.name());
		setCommand(command);
	}

	/**
	 * Creates an initially unselected check box menu item with an icon and
	 * the specified command.
	 *
	 * @param icon the icon of the CheckBoxMenuItem.
	 */
	public CommandCheckBoxMenuItem(Command command, Icon icon) {
		super(command.name(), icon);
		setCommand(command);
	}

	/**
	 * Creates a check box menu item with the specified command and selection state.
	 *
	 * @param command the command to be executed upon menu selection
	 * @param b the selected state of the check box menu item
	 */
	public CommandCheckBoxMenuItem(Command command, boolean b) {
		super(command.name(), b);
		setCommand(command);
	}

	/**
	 * Creates a check box menu item with the specified text, icon, and selection state.
	 *
	 * @param command the command to be executed upon menu selection
	 * @param icon the icon of the check box menu item
	 * @param b the selected state of the check box menu item
	 */
	public CommandCheckBoxMenuItem(Command command, Icon icon, boolean b) {
		super(command.name(), icon, b);
		setCommand(command);
	}


	/**
	 * Gets the command attribute of the CommandMenuItem object
	 *
	 * @return   The command value
	 */
	public Command getCommand() {
		return fCommand;
	}


	/**
	 * Sets the command attribute of the CommandMenuItem object
	 *
	 * @param newCommand  The new command value
	 */
	public void setCommand(Command newCommand) {
		fCommand = newCommand;
	}

}