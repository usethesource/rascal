/*
 * @(#)CommandButton.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * A Command enabled button. Clicking the button executes the command.
 *
 * @see Command
 *
 * @version <$CURRENT_VERSION$>
 */
public  class CommandButton
		extends JButton implements ActionListener {

	private Command   fCommand;

	/**
	 * Initializes the button with the given command.
	 * The command's name is used as the label.
	 */
	public CommandButton(Command command) {
		super(command.name());
		fCommand = command;
		addActionListener(this);
	}

	/**
	 * Executes the command. If the command's name was changed
	 * as a result of the command the button's label is updated
	 * accordingly.
	 */
	public void actionPerformed(ActionEvent e) {
		fCommand.execute();
		if (!getText().equals(fCommand.name()) ) {
			setText(fCommand.name());
		}
	}
}


