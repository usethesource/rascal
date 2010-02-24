/*
 * @(#)DrawingEditor.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import org.jhotdraw.util.UndoManager;

/**
 * DrawingEditor defines the interface for coordinating
 * the different objects that participate in a drawing editor.
 *
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld022.htm>Mediator</a></b><br>
 * DrawingEditor is the mediator. It decouples the participants
 * of a drawing editor.
 *
 * @see Tool
 * @see DrawingView
 * @see Drawing
 *
 * @version <$CURRENT_VERSION$>
 */
public interface DrawingEditor extends FigureSelectionListener {

	/**
	 * Gets the editor's drawing view.
	 */
	public DrawingView view();

	public DrawingView[] views();
	/**
	 * Gets the editor's drawing.
	 */
//	public Drawing drawing();

	/**
	 * Gets the editor's current tool.
	 */
	public Tool tool();

	/**
	 * Informs the editor that a tool has done its interaction.
	 * This method can be used to switch back to the default tool.
	 */
	public void toolDone();

	/**
	 * Informs that the current figure selection has changed.
	 * Override this method to handle selection changes.
	 */
	public void figureSelectionChanged(DrawingView view);

	public void addViewChangeListener(ViewChangeListener vsl);
	public void removeViewChangeListener(ViewChangeListener vsl);

	/**
	 * Shows a status message in the editor's user interface
	 */
	public void showStatus(String string);

	public UndoManager getUndoManager();
}
