/*
 * @(#)UndoManager.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import org.jhotdraw.framework.DrawingView;

import java.util.List;
import java.util.Iterator;

/**
 * This class manages all the undoable commands. It keeps track of all
 * the modifications done through user interactions.
 *
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class UndoManager {
	/**
	 * Maximum default buffer size for undo and redo stack
	 */
	public static final int DEFAULT_BUFFER_SIZE = 20;

	/**
	 * Collection of undo activities
	 */
	private List redoStack;

	/**
	 * Collection of undo activities
	 */
	private List undoStack;
	private int maxStackCapacity;

	public UndoManager() {
		this(DEFAULT_BUFFER_SIZE);
	}

	public UndoManager(int newUndoStackSize) {
		maxStackCapacity = newUndoStackSize;
		undoStack = CollectionsFactory.current().createList(maxStackCapacity);
		redoStack = CollectionsFactory.current().createList(maxStackCapacity);
	}

	public void pushUndo(Undoable undoActivity) {
		if (undoActivity.isUndoable()) {
			removeFirstElementInFullList(undoStack);
			undoStack.add(undoActivity);
		}
		else {
			// a not undoable activity clears the stack because
			// the last activity does not correspond with the
			// last undo activity
			undoStack = CollectionsFactory.current().createList(maxStackCapacity);
		}
	}

	public void pushRedo(Undoable redoActivity) {
		if (redoActivity.isRedoable()) {

			removeFirstElementInFullList(redoStack);

			// add redo activity only if it is not already the last
			// one in the buffer
			if ((getRedoSize() == 0) || (peekRedo() != redoActivity)) {
				redoStack.add(redoActivity);
			}
		}
		else {
			// a not undoable activity clears the tack because
			// the last activity does not correspond with the
			// last undo activity
			redoStack = CollectionsFactory.current().createList(maxStackCapacity);
		}
	}

	/**
	 * If buffersize exceeds, remove the oldest command
	 */
	private void removeFirstElementInFullList(List l) {
		if (l.size() >= maxStackCapacity) {
			Undoable removedActivity = (Undoable)l.remove(0);
			removedActivity.release();
		}
	}

	private Undoable getLastElement(List l) {
		if (l.size() > 0) {
			return (Undoable)l.get(l.size() - 1);
		}
		else {
			return null;
		}
	}
	public boolean isUndoable() {
		if (getUndoSize() > 0) {
			return getLastElement(undoStack).isUndoable();
		}
		else {
			return false;
		}
	}

	public boolean isRedoable() {
		if (getRedoSize() > 0) {
			return getLastElement(redoStack).isRedoable();
		}
		else {
			return false;
		}
	}

	protected Undoable peekUndo() {
		if (getUndoSize() > 0) {
			return getLastElement(undoStack);
		}
		else {
			return null;
		}
	}

	protected Undoable peekRedo() {
		if (getRedoSize() > 0) {
			return getLastElement(redoStack);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the current size of undo buffer.
	 */
	public int getUndoSize() {
		return undoStack.size();
	}

	/**
	 * Returns the current size of redo buffer.
	 */
	public int getRedoSize() {
		return redoStack.size();
	}

	/**
	 * Throw NoSuchElementException if there is none
	 */
	public Undoable popUndo() {
		// Get the last element - throw NoSuchElementException if there is none
		Undoable lastUndoable = peekUndo();

		// Remove it from undo collection
		undoStack.remove(getUndoSize() - 1);

		return lastUndoable;
	}

	/**
	 * Throw NoSuchElementException if there is none
	 */
	public Undoable popRedo() {
		// Get the last element - throw NoSuchElementException if there is none
		Undoable lastUndoable = peekRedo();

		// Remove it from undo collection
		redoStack.remove(getRedoSize() - 1);

		return lastUndoable;
	}

	public void clearUndos() {
		clearStack(undoStack);
	}

	public void clearRedos() {
		clearStack(redoStack);
	}

	protected void clearStack(List clearStack) {
		clearStack.clear();
	}

	/**
	 * Removes all undo activities that operate on the given DrawingView.
	 * @param checkDV DrawingView which is compared undo's DrawingView
	 */
	public void clearUndos(DrawingView checkDV) {
		Iterator iter = undoStack.iterator();
		while (iter.hasNext()) {
			Undoable currentUndo = (Undoable)iter.next();
			if (currentUndo.getDrawingView() == checkDV) {
				iter.remove();
			}
		}
	}

	/**
	 * Removes all redo activities that operate on the given DrawingView.
	 * @param checkDV DrawingView which is compared redo's DrawingView
	 */
	public void clearRedos(DrawingView checkDV) {
		Iterator iter = redoStack.iterator();
		while (iter.hasNext()) {
			Undoable currentRedo = (Undoable)iter.next();
			if (currentRedo.getDrawingView() == checkDV) {
				iter.remove();
			}
		}
	}
}
