/*
 * @(#)AbstractTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.Undoable;

/**
 * Default implementation support for Tools.
 *
 * @see DrawingView
 * @see Tool
 *
 * @version <$CURRENT_VERSION$>
 */

public abstract class AbstractTool implements Tool {

	private DrawingEditor     myDrawingEditor;

	/**
	 * The position of the initial mouse down.
	 * The anchor point is usually the first mouse click performed with this tool.
	 */
    private int myAnchorX;
    private int myAnchorY;

	/**
	 * A tool can have a drawing view on which it operates
	 * independingly of the currently active drawing view.
	 * For example, if a tool can be used
	 */
	private DrawingView     myDrawingView;

	private Undoable myUndoActivity;
	private AbstractTool.EventDispatcher myEventDispatcher;

	private boolean myIsUsable;

	/**
	 * Flag to indicate whether to perform usable checks or not
	 */
	private boolean myIsEnabled;

	/**
	 * Constructs a tool for the given view.
	 */
	public AbstractTool(DrawingEditor newDrawingEditor) {
		setEditor(newDrawingEditor);
		setEventDispatcher(createEventDispatcher());
		setEnabled(true);
		checkUsable();
		editor().addViewChangeListener(createViewChangeListener());
	}

	/**
	 * Activates the tool for use on the given view. This method is called
	 * whenever the user switches to this tool. Use this method to
	 * reinitialize a tool.
	 * Since tools will be disabled unless it is useable, there will always
	 * be an active view when this is called. based on isUsable()
	 * Tool should never be activated if the view is null.
	 * Ideally, the dditor should take care of that.
	 */
	public void activate() {
		if (getActiveView() != null) {
			getActiveView().clearSelection();
			getActiveView().checkDamage();
			getEventDispatcher().fireToolActivatedEvent();
		}
	}

	/**
	 * Deactivates the tool. This method is called whenever the user
	 * switches to another tool. Use this method to do some clean-up
	 * when the tool is switched. Subclassers should always call
	 * super.deactivate.
	 * An inactive tool should never be deactivated
	 */
	public void deactivate() {
		if (isActive()) {
			if (getActiveView() != null) {
				getActiveView().setCursor(new AWTCursor(java.awt.Cursor.DEFAULT_CURSOR));
			}
			getEventDispatcher().fireToolDeactivatedEvent();
		}
	}

	/**
	 * Fired when the selected view changes.
	 * Subclasses should always call super.  ViewSelectionChanged() this allows
	 * the tools state to be updated and referenced to the new view.
	 */
	protected void viewSelectionChanged(DrawingView oldView, DrawingView newView) {
		if (isActive()) {
			deactivate();
			activate();
		}

		checkUsable();
	}

	/**
	 * Sent when a new view is created
	 */
	protected void viewCreated(DrawingView view) {
	}

	/**
	 * Send when an existing view is about to be destroyed.
	 */
	protected void viewDestroying(DrawingView view) {
	}

	/**
	 * Handles mouse down events in the drawing view.
	 */
	public void mouseDown(MouseEvent e, int x, int y) {
        setAnchorX(x);
        setAnchorY(y);
		setView((DrawingView)e.getSource());
	}

	/**
	 * Handles mouse drag events in the drawing view.
	 */
	public void mouseDrag(MouseEvent e, int x, int y) {
	}

	/**
	 * Handles mouse up in the drawing view.
	 */
	public void mouseUp(MouseEvent e, int x, int y) {
//		setView(null);//This must be fixed!!! the view should not be held onto after mouse up??
//unlike super.mousedown which is usually called immediately after a sub classes mouse down
//method starts, super.mouseup should probably be called last before the method ends?
//it must if its going to set the view to null.  getting messy.
	}

	/**
	 * Handles mouse moves (if the mouse button is up).
	 */
	public void mouseMove(MouseEvent evt, int x, int y) {
	}

	/**
	 * Handles key down events in the drawing view.
	 */
	public void keyDown(KeyEvent evt, int key) {
	}

	/**
	 * Gets the tool's drawing.
	 */
	public Drawing drawing() {
		return view().drawing();
	}

	public Drawing getActiveDrawing() {
	    return getActiveView().drawing();
	}

	/**
	 * Gets the tool's editor.
	 */
	public DrawingEditor editor() {
		return myDrawingEditor;
	}

	protected void setEditor(DrawingEditor newDrawingEditor) {
		myDrawingEditor = newDrawingEditor;
	}

	/**
	 * Gets the tool's view (convienence method).
	 */
	public DrawingView view() {
		return myDrawingView;
	}

	protected void setView(DrawingView newDrawingView) {
		myDrawingView = newDrawingView;
	}

	public DrawingView getActiveView() {
		return editor().view();
	}

	/**
	 * Tests if the tool can be used or "executed."
	 */
	public boolean isUsable() {
		return isEnabled() && myIsUsable;
	}

	public void setUsable(boolean newIsUsable) {
		// perform notification only if the usable state of the tool has changed
		if (isUsable() != newIsUsable) {
			myIsUsable = newIsUsable;
			if (isUsable()) {
				getEventDispatcher().fireToolUsableEvent();
			}
			else {
				getEventDispatcher().fireToolUnusableEvent();
			}
		}
	}

	public void setEnabled(boolean newIsEnabled) {
		// perform notification only if the usable state of the tool has changed
		if (isEnabled() != newIsEnabled) {
			myIsEnabled = newIsEnabled;
			if (isEnabled()) {
				getEventDispatcher().fireToolEnabledEvent();
			}
			else {
				getEventDispatcher().fireToolDisabledEvent();
				setUsable(false);
				deactivate();
			}
		}
	}

	public boolean isEnabled() {
		return myIsEnabled;
	}

	/**
	 * The anchor point is usually the first mouse click performed with this tool.
	 * @see #mouseDown
	 */
	protected void setAnchorX(int newAnchorX) {
		myAnchorX = newAnchorX;
	}

	/**
	 * The anchor point is usually the first mouse click performed with this tool.
	 *
	 * @return the anchor X coordinate for the interaction
	 * @see #mouseDown
	 */
	protected int getAnchorX() {
		return myAnchorX;
	}

	/**
	 * The anchor point is usually the first mouse click performed with this tool.
	 * @see #mouseDown
	 */
	protected void setAnchorY(int newAnchorY) {
		myAnchorY = newAnchorY;
	}

	/**
	 * The anchor point is usually the first mouse click performed with this tool.
	 *
	 * @return the anchor Y coordinate for the interaction
	 * @see #mouseDown
	 */
	protected int getAnchorY() {
		return myAnchorY;
	}

	public Undoable getUndoActivity() {
		return myUndoActivity;
	}

	public void setUndoActivity(Undoable newUndoActivity) {
		myUndoActivity = newUndoActivity;
	}

	public boolean isActive() {
		return (editor().tool() == this) && isUsable();
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

	protected AbstractTool.EventDispatcher createEventDispatcher() {
		return new AbstractTool.EventDispatcher(this);
	}

    protected ViewChangeListener createViewChangeListener() {
        return new ViewChangeListener() {
            public void viewSelectionChanged(DrawingView oldView, DrawingView newView){
	    		AbstractTool.this.viewSelectionChanged(oldView, newView);
		    }
    		public void viewCreated(DrawingView view){
	    		AbstractTool.this.viewCreated(view);
		    }
    		public void viewDestroying(DrawingView view){
	    		AbstractTool.this.viewDestroying(view);
		    }
        };
    }

	protected void checkUsable() {
		if (isEnabled()) {
			setUsable((getActiveView() != null) && getActiveView().isInteractive());
		}
	}

	public static class EventDispatcher {
		private List myRegisteredListeners;
		private Tool myObservedTool;

		public EventDispatcher(Tool newObservedTool) {
			myRegisteredListeners = CollectionsFactory.current().createList();
			myObservedTool = newObservedTool;
		}

		public void fireToolUsableEvent() {
			Iterator iter = myRegisteredListeners.iterator();
			while (iter.hasNext()) {
				((ToolListener)iter.next()).toolUsable(new EventObject(myObservedTool));
			}
		}

		public void fireToolUnusableEvent() {
			Iterator iter = myRegisteredListeners.iterator();
			while (iter.hasNext()) {
				((ToolListener)iter.next()).toolUnusable(new EventObject(myObservedTool));
			}
		}

		public void fireToolActivatedEvent() {
			Iterator iter = myRegisteredListeners.iterator();
			while (iter.hasNext()) {
				((ToolListener)iter.next()).toolActivated(new EventObject(myObservedTool));
			}
		}

		public void fireToolDeactivatedEvent() {
			Iterator iter = myRegisteredListeners.iterator();
			while (iter.hasNext()) {
				((ToolListener)iter.next()).toolDeactivated(new EventObject(myObservedTool));
			}
		}

		public void fireToolEnabledEvent() {
			Iterator iter = myRegisteredListeners.iterator();
			while (iter.hasNext()) {
				((ToolListener)iter.next()).toolEnabled(new EventObject(myObservedTool));
			}
		}

		public void fireToolDisabledEvent() {
			Iterator iter = myRegisteredListeners.iterator();
			while (iter.hasNext()) {
				((ToolListener)iter.next()).toolDisabled(new EventObject(myObservedTool));
			}
		}

		public void addToolListener(ToolListener newToolListener) {
			if (!myRegisteredListeners.contains(newToolListener)) {
				myRegisteredListeners.add(newToolListener);
			}
		}

		public void removeToolListener(ToolListener oldToolListener) {
			if (myRegisteredListeners.contains(oldToolListener)) {
				myRegisteredListeners.remove(oldToolListener);
			}
		}
	}
}
