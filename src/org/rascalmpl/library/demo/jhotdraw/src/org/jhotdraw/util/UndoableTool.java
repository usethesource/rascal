/*
 * @(#)UndoableTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.AbstractTool;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;

/**
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class UndoableTool implements Tool, ToolListener {

	private Tool myWrappedTool;
	private AbstractTool.EventDispatcher myEventDispatcher;

	public UndoableTool(Tool newWrappedTool) {
		setEventDispatcher(createEventDispatcher());
		setWrappedTool(newWrappedTool);
		getWrappedTool().addToolListener(this);
	}

	/**
	 * Activates the tool for the given view. This method is called
	 * whenever the user switches to this tool. Use this method to
	 * reinitialize a tool.
	 */
	public void activate() {
		getWrappedTool().activate();
	}

	/**
	 * Deactivates the tool. This method is called whenever the user
	 * switches to another tool. Use this method to do some clean-up
	 * when the tool is switched. Subclassers should always call
	 * super.deactivate.
	 */
	public void deactivate() {
		getWrappedTool().deactivate();
		Undoable undoActivity = getWrappedTool().getUndoActivity();
		if ((undoActivity != null) && (undoActivity.isUndoable())) {
			editor().getUndoManager().pushUndo(undoActivity);
			editor().getUndoManager().clearRedos();
			// update menus
			editor().figureSelectionChanged(getActiveView());
		}
	}

	/**
	 * Handles mouse down events in the drawing view.
	 */
	public void mouseDown(MouseEvent e, int x, int y) {
		getWrappedTool().mouseDown(e, x, y);
	}

	/**
	 * Handles mouse drag events in the drawing view.
	 */
	public void mouseDrag(MouseEvent e, int x, int y) {
		getWrappedTool().mouseDrag(e, x, y);
	}

	/**
	 * Handles mouse up in the drawing view. After the mouse button
	 * has been released, the associated tool activity can be undone
	 * if the associated tool supports the undo operation from the Undoable interface.
	 *
	 * @see org.jhotdraw.util.Undoable
	 */
	public void mouseUp(MouseEvent e, int x, int y) {
		getWrappedTool().mouseUp(e, x, y);
	}

	/**
	 * Handles mouse moves (if the mouse button is up).
	 */
	public void mouseMove(MouseEvent evt, int x, int y) {
		getWrappedTool().mouseMove(evt, x, y);
	}

	/**
	 * Handles key down events in the drawing view.
	 */
	public void keyDown(KeyEvent evt, int key) {
		getWrappedTool().keyDown(evt, key);
	}

	public boolean isUsable() {
		return getWrappedTool().isUsable();
	}

	public boolean isActive() {
		// do not delegate but test whether this undoable tool is active
		return editor().tool() == this;
	}

	public boolean isEnabled() {
		return getWrappedTool().isEnabled();
	}

	public void setUsable(boolean newIsUsable) {
		getWrappedTool().setUsable(newIsUsable);
	}

	public void setEnabled(boolean newIsEnabled) {
		getWrappedTool().setEnabled(newIsEnabled);
	}

	protected void setWrappedTool(Tool newWrappedTool) {
		myWrappedTool = newWrappedTool;
	}

	protected Tool getWrappedTool() {
		return myWrappedTool;
	}

	public DrawingEditor editor() {
		return getWrappedTool().editor();
	}

	public DrawingView view() {
		return editor().view();
	}

	public Undoable getUndoActivity() {
		return new UndoableAdapter(view());
	}

	public void setUndoActivity(Undoable newUndoableActivity) {
		// do nothing: always return default UndoableAdapter
	}

	public void toolUsable(EventObject toolEvent) {
		getEventDispatcher().fireToolUsableEvent();
	}

	public void toolUnusable(EventObject toolEvent) {
		getEventDispatcher().fireToolUnusableEvent();
	}

	public void toolActivated(EventObject toolEvent) {
		getEventDispatcher().fireToolActivatedEvent();
	}

	public void toolDeactivated(EventObject toolEvent) {
		getEventDispatcher().fireToolDeactivatedEvent();
	}

	public void toolEnabled(EventObject toolEvent) {
		getEventDispatcher().fireToolEnabledEvent();
	}

	public void toolDisabled(EventObject toolEvent) {
		getEventDispatcher().fireToolDisabledEvent();
	}

	public void addToolListener(ToolListener newToolListener) {
		getEventDispatcher().addToolListener(newToolListener);
	}

	public void removeToolListener(ToolListener oldToolListener) {
		getEventDispatcher().removeToolListener(oldToolListener);
	}

	private void setEventDispatcher(AbstractTool.EventDispatcher newEventDispatcher) {
		myEventDispatcher = newEventDispatcher;
	}

	protected AbstractTool.EventDispatcher getEventDispatcher() {
		return myEventDispatcher;
	}

	public AbstractTool.EventDispatcher createEventDispatcher() {
		return new AbstractTool.EventDispatcher(this);
	}

	public DrawingView getActiveView() {
		return editor().view();
	}
}
