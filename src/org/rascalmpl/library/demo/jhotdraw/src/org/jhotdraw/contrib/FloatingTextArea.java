/*
 *  @(#)FloatingTextArea.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib;

import java.awt.Color;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/**
 * A FloatingTextArea overlays an editor on top of an area in a drawing<br>
 * @author Eduardo Francos - InContext
 * @version <$CURRENT_VERSION$>
 * @todo By default JEditorPane uses a tab size of 8.
 * I couldn't find how to change this.
 * If anybody knows please tell me.
 */
public class FloatingTextArea {
	/**
	 * A scroll pane to allow for vertical scrolling while editing
	 */
	protected JScrollPane fEditScrollContainer;
	/**
	 * The actual editor
	 */
	protected JEditorPane fEditWidget;
	/**
	 * The container within which the editor is created
	 */
	protected Container fContainer;


	/**
	 * Constructor for the FloatingTextArea object
	 */
	public FloatingTextArea() {
		fEditWidget = new JEditorPane();
		fEditScrollContainer = new JScrollPane(fEditWidget,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		fEditScrollContainer.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		fEditScrollContainer.setBorder(BorderFactory.createLineBorder(Color.black));
	}


	/**
	 * Creates the overlay within the given container.
	 * @param container the container
	 */
	public void createOverlay(Container container) {
		createOverlay(container, null);
	}


	/**
	 * Creates the overlay for the given Container using a
	 * specific font.
	 * @param container the container
	 * @param font the font
	 */
	public void createOverlay(Container container, Font font) {
		container.add(fEditScrollContainer, 0);
		if (font != null) {
			fEditWidget.setFont(font);
		}
		fContainer = container;
	}


	/**
	 * Positions and sizes the overlay.
	 * @param r the bounding rectangle for the overlay
	 * @param text the text to edit
	 */
	public void setBounds(Rectangle r, String text) {
		fEditWidget.setText(text);
		fEditScrollContainer.setBounds(r.x, r.y, r.width, r.height);
		fEditScrollContainer.setVisible(true);
		fEditWidget.setCaretPosition(0);
		fEditWidget.requestFocus();
	}


	/**
	 * Gets the text contents of the overlay.
	 * @return The text value
	 */
	public String getText() {
		return fEditWidget.getText();
	}


	/**
	 * Gets the preferred size of the overlay.
	 * @param cols Description of the Parameter
	 * @return The preferredSize value
	 */
	public Dimension getPreferredSize(int cols) {
		return new Dimension(fEditWidget.getWidth(), fEditWidget.getHeight());
	}


	/**
	 * Removes the overlay.
	 */
	public void endOverlay() {
		fContainer.requestFocus();
		if (fEditScrollContainer != null) {
			fEditScrollContainer.setVisible(false);
			fContainer.remove(fEditScrollContainer);

			Rectangle bounds = fEditScrollContainer.getBounds();
			fContainer.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}
}
