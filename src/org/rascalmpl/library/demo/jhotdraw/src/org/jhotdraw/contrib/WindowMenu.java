/*
 * @(#)WindowMenu.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.jhotdraw.framework.DrawingEditor;
import org.jhotdraw.standard.AbstractCommand;
import org.jhotdraw.util.Command;
import org.jhotdraw.util.CommandMenu;

/**
 * Menu component that handles the functionality expected of a standard
 * "Windows" menu for MDI applications.
 *
 * @author Wolfram Kaiser (adapted from an article in JavaWorld)
 * @version <$CURRENT_VERSION$>
 */
public class WindowMenu extends CommandMenu {
	private MDIDesktopPane desktop;
	private Command cascadeCommand;
	private Command tileCommand;

	public WindowMenu(String newText, MDIDesktopPane newDesktop, DrawingEditor newEditor) {
		super(newText);
		this.desktop = newDesktop;
		cascadeCommand = new AbstractCommand("Cascade", newEditor) {
			public void execute() {
				WindowMenu.this.desktop.cascadeFrames();
			}
			public boolean isExecutable() {
				return super.isExecutable() && (WindowMenu.this.desktop.getAllFrames().length > 0);
			}
		};
		tileCommand = new AbstractCommand("Tile", newEditor) {
			public void execute() {
				WindowMenu.this.desktop.tileFramesHorizontally();
			}
			public boolean isExecutable() {
				return super.isExecutable() && (WindowMenu.this.desktop.getAllFrames().length > 0);
			}
		};
		addMenuListener(new MenuListener() {
			public void menuCanceled (MenuEvent e) {}

			public void menuDeselected (MenuEvent e) {
				removeAll();
			}

			public void menuSelected (MenuEvent e) {
				buildChildMenus();
			}
		});
	}

	/* Sets up the children menus depending on the current desktop state */
	private void buildChildMenus() {
		ChildMenuItem menu;
		JInternalFrame[] array = desktop.getAllFrames();

		add(new CommandMenuItem(cascadeCommand));
		add(new CommandMenuItem(tileCommand));
		if (array.length > 0) {
			addSeparator();
		}
//		cascade.setEnabled(array.length > 0);
//		tile.setEnabled(array.length > 0);

		for (int i = 0; i < array.length; i++) {
			menu = new ChildMenuItem(array[i]);
			menu.setState(i == 0);
			menu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					JInternalFrame frame = ((ChildMenuItem)ae.getSource()).getFrame();
					frame.moveToFront();
					try {
						frame.setSelected(true);
					}
					catch (PropertyVetoException e) {
						e.printStackTrace();
					}
				}
			});
			menu.setIcon(array[i].getFrameIcon());
			add(menu);
		}
	}

	/* This JCheckBoxMenuItem descendant is used to track the child frame that corresponds
	   to a give menu. */
	class ChildMenuItem extends JCheckBoxMenuItem {
		private JInternalFrame frame;

		public ChildMenuItem(JInternalFrame newFrame) {
			super(newFrame.getTitle());
			frame=newFrame;
		}

		public JInternalFrame getFrame() {
			return frame;
		}
	}
}