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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

import org.jhotdraw.framework.JHotDrawRuntimeException;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.CommandListener;
import org.jhotdraw.util.CommandMenu;

/**
 * A Command enabled menu. Selecting a menu item
 * executes the corresponding command.
 *
 * @author    Eduardo Francos  (adapted from initial implementation by Wolfram Kaiser)
 * @created   2 mai 2002
 * @see       Command
 * @version   <$CURRENT_VERSION$>
 */
public class CTXCommandMenu extends JMenu implements ActionListener, CommandListener {

	public CTXCommandMenu(String name) {
		super(name);
	}

	/**
	 * Adds a command to the menu. The item's label is
	 * the command's name.
	 */
	public synchronized void add(Command command) {
		addMenuItem(new CommandMenuItem(command));
	}

	/**
	 * Adds a command with the given short cut to the menu. The item's label is
	 * the command's name.
	 */
	public synchronized void add(Command command, MenuShortcut shortcut) {
		addMenuItem(new CommandMenuItem(command, shortcut.getKey()));
	}

	/**
	 * Adds a command with the given short cut to the menu. The item's label is
	 * the command's name.
	 */
	public synchronized void addCheckItem(Command command) {
		addMenuItem(new CommandCheckBoxMenuItem(command));
	}

	/**
	 * Adds a command menu item
	 *
	 * @param item  the command menu item
	 */
	public synchronized void add(CommandMenuItem item) {
		addMenuItem(item);
	}

	/**
	 * Adds a command checkbox menu item
	 *
	 * @param checkItem  the checkbox item
	 */
	public synchronized void add(CommandCheckBoxMenuItem checkItem) {
		addMenuItem(checkItem);
	}

	/**
	 * Adds a normal menu item to the menu
	 *
	 * @param m  The menu item
	 */
	protected void addMenuItem(JMenuItem m) {
		m.addActionListener(this);
		add(m);
		((CommandHolder)m).getCommand().addCommandListener(this);
	}

	/**
	 * Removes a command item from the menu
	 *
	 * @param command  the command tor emove
	 */
	public synchronized void remove(Command command) {
		throw new JHotDrawRuntimeException("not implemented");
	}

	/**
	 * Removes an item from the menu
	 *
	 * @param item  the item to remove
	 */
	public synchronized void remove(MenuItem item) {
		throw new JHotDrawRuntimeException("not implemented");
	}

	/**
	 * Changes the enabling/disabling state of a named menu item.
	 *
	 * @param name   Description of the Parameter
	 * @param state  Description of the Parameter
	 */
	public synchronized void enable(String name, boolean state) {
		for (int i = 0; i < getItemCount(); i++) {
			JMenuItem item = getItem(i);
			if (name.equals(item.getText())) {
				item.setEnabled(state);
				return;
			}
		}
	}

	/** Description of the Method */
	public synchronized void checkEnabled() {
		int j = 0;
		for (int i = 0; i < getMenuComponentCount(); i++) {
			JMenuItem currentItem = getItem(i);
			if (currentItem instanceof CommandMenu) {
				((CommandMenu)currentItem).checkEnabled();
			}
			else if (currentItem instanceof CTXCommandMenu) {
				((CTXCommandMenu)currentItem).checkEnabled();
			}
			else if (currentItem instanceof CommandHolder) {
				currentItem.setEnabled(((CommandHolder)currentItem).getCommand().isExecutable());
			}
			else if (currentItem instanceof Command) {
				currentItem.setEnabled(((Command)currentItem).isExecutable());
			}
			j++;
		}
	}

	/**
	 * Executes the command.
	 *
	 * @param e  Description of the Parameter
	 */
	public void actionPerformed(ActionEvent e) {
		int j = 0;
		Object source = e.getSource();
		for (int i = 0; i < getItemCount(); i++) {
			// ignore separators
			// a separator has a hyphen as its label
			if (getMenuComponent(i) instanceof JSeparator) {
				continue;
			}
			JMenuItem item = getItem(i);
			if (source == item) {
				Command cmd = ((CommandHolder)item).getCommand();
				cmd.execute();
				break;
			}
			j++;
		}
	}

	/**
	 * Description of the Method
	 *
	 * @param commandEvent  Description of the Parameter
	 */
	public void commandExecuted(EventObject commandEvent) {
//		checkEnabled();
	}

	/**
	 * Description of the Method
	 *
	 * @param commandEvent  Description of the Parameter
	 */
	public void commandExecutable(EventObject commandEvent) {
//		checkEnabled();
	}

	/**
	 * Description of the Method
	 *
	 * @param commandEvent  Description of the Parameter
	 */
	public void commandNotExecutable(EventObject commandEvent) {
//		checkEnabled();
	}
}

