/*
 * @(#)DragTracker.java
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

/**
 * An UndoRedoActivity can be used to turn a UndoActivity into a RedoActivity.
 * In this case, the redo() method of an encapsulated activity is called when
 * the undo() is executed, and undo() when redo() is executed.
 *
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class UndoRedoActivity implements Undoable {
	private Undoable myReversedActivity;
	
	protected UndoRedoActivity(Undoable newReversedActivity) {
		setReversedActivity(newReversedActivity);
	}

	/**
	 * Undo the activity
	 * @return true if the activity could be undone, false otherwise
	 */
	public boolean undo() {
		if (isRedoable()) {
			return getReversedActivity().redo();
		}
		
		return false;
	}

	/*
	 * Redo the activity
	 * @return true if the activity could be redone, false otherwise
	 */
	public boolean redo() {
		if (isUndoable()) {
			return getReversedActivity().undo();
		}
		
		return false;
	}

	/**
	 * Dispatch to isRedoable of the activity to be reversed.
	 */
	public boolean isUndoable() {
		return getReversedActivity().isRedoable();
	}

	/**
	 * Dispatch to setRedoable of the activity to be reversed.
	 */
	public void setUndoable(boolean newIsUndoable) {
		getReversedActivity().setRedoable(newIsUndoable);
	}

	/**
	 * Dispatch to isUndoable of the activity to be reversed.
	 */
	public boolean isRedoable() {
		return getReversedActivity().isUndoable();
	}
	
	/**
	 * Dispatch to setUndoable of the activity to be reversed.
	 */
	public void setRedoable(boolean newIsRedoable) {
		getReversedActivity().setUndoable(newIsRedoable);
	}
	
	public void setAffectedFigures(FigureEnumeration newAffectedFigures) {
		getReversedActivity().setAffectedFigures(newAffectedFigures);
	}

	public FigureEnumeration getAffectedFigures() {
		return getReversedActivity().getAffectedFigures();
	}
	
	public int getAffectedFiguresCount() {
		return getReversedActivity().getAffectedFiguresCount();
	}

	public DrawingView getDrawingView() {
		return getReversedActivity().getDrawingView();
	}
	
	public void release() {
		getReversedActivity().release();
	}
			
	protected void setReversedActivity(Undoable newReversedActivity) {
		myReversedActivity = newReversedActivity;
	}
	
	public Undoable getReversedActivity() {
		return myReversedActivity;
	}
	
	public static Undoable createUndoRedoActivity(Undoable toBeReversed) {
		// instead of reversing the reversed activity just return the original activity
		if (toBeReversed instanceof UndoRedoActivity) {
			return ((UndoRedoActivity)toBeReversed).getReversedActivity();
		}
		else {
			return new UndoRedoActivity(toBeReversed);
		}
	}
}
