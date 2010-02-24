/*
 * @(#)FloatingTextField.java
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
import java.awt.*;
import java.awt.event.*;

/**
 * A text field overlay that is used to edit a TextFigure.
 * A FloatingTextField requires a two step initialization:
 * In a first step the overlay is created and in a
 * second step it can be positioned.
 *
 * @see org.jhotdraw.figures.TextFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public  class FloatingTextField {

	private JTextField   fEditWidget;
	private Container   fContainer;

	public FloatingTextField() {
		fEditWidget = new JTextField(20);
	}

	/**
	 * Creates the overlay for the given Component.
	 */
	public void createOverlay(Container container) {
		createOverlay(container, null);
	}

	/**
	 * Creates the overlay for the given Container using a
	 * specific font.
	 */
	public void createOverlay(Container container, Font font) {
		container.add(fEditWidget, 0);
		if (font != null) {
			fEditWidget.setFont(font);
		}
		fContainer = container;
	}

	/**
	 * Adds an action listener
	 */
	public void addActionListener(ActionListener listener) {
		fEditWidget.addActionListener(listener);
	}

	/**
	 * Remove an action listener
	 */
	public void removeActionListener(ActionListener listener) {
		fEditWidget.removeActionListener(listener);
	}

	/**
	 * Positions the overlay.
	 */
	public void setBounds(Rectangle r, String text) {
		fEditWidget.setText(text);
		fEditWidget.setLocation(r.x, r.y);
		fEditWidget.setSize(r.width, r.height);
		fEditWidget.setVisible(true);
		fEditWidget.selectAll();
		fEditWidget.requestFocus();
	}

	/**
	 * Gets the text contents of the overlay.
	 */
	public String getText() {
		return fEditWidget.getText();
	}

	/**
	 * Gets the preferred size of the overlay.
	 */
	public Dimension getPreferredSize(int cols) {
		fEditWidget.setColumns(cols);
		return fEditWidget.getPreferredSize();
	}

	/**
	 * Removes the overlay.
	 */
	public void endOverlay() {
		fContainer.requestFocus();
		if (fEditWidget != null) {
			fEditWidget.setVisible(false);
			fContainer.remove(fEditWidget);

			Rectangle bounds = fEditWidget.getBounds();
			fContainer.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}
}

