/*
 * @(#)UndoableAdapter.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	? by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.FigureEnumerator;
import org.jhotdraw.standard.ReverseFigureEnumerator;
import org.jhotdraw.standard.StandardFigureSelection;

import java.util.Collections;
import java.util.List;

/**
 * Most basic implementation for an Undoable activity. Subclasses should override
 * methods to provide specialized behaviour when necessary.
 *
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class UndoableAdapter implements Undoable {

	private List   myAffectedFigures;
	private boolean myIsUndoable;
	private boolean myIsRedoable;
	private DrawingView myDrawingView;

	public UndoableAdapter(DrawingView newDrawingView) {
		setDrawingView(newDrawingView);
	}

	/**
	 * Undo the activity
	 * @return true if the activity could be undone, false otherwise
	 */
	public boolean undo() {
		return isUndoable();
	}

	/**
	 * Redo the activity
	 * @return true if the activity could be redone, false otherwise
	 */
	public boolean redo() {
		return isRedoable();
	}

	public boolean isUndoable() {
		return myIsUndoable;
	}

	public void setUndoable(boolean newIsUndoable) {
		myIsUndoable = newIsUndoable;
	}

	public boolean isRedoable() {
		return myIsRedoable;
	}

	public void setRedoable(boolean newIsRedoable) {
		myIsRedoable = newIsRedoable;
	}

	public void setAffectedFigures(FigureEnumeration newAffectedFigures) {
		// the enumeration is not reusable therefore a copy is made
		// to be able to undo-redo the command several time
		if(newAffectedFigures == null) {
			throw new IllegalArgumentException();
		}
		rememberFigures(newAffectedFigures);
	}

	public FigureEnumeration getAffectedFigures() {
		if(myAffectedFigures == null) {
			return new FigureEnumerator(Collections.EMPTY_LIST);
		}
		else {
			return new FigureEnumerator(CollectionsFactory.current().createList(myAffectedFigures));
		}
	}

	public FigureEnumeration getAffectedFiguresReversed() {
		return new ReverseFigureEnumerator(CollectionsFactory.current().createList(myAffectedFigures));
	}

	public int getAffectedFiguresCount() {
		return myAffectedFigures.size();
	}

	protected void rememberFigures(FigureEnumeration toBeRemembered) {
		myAffectedFigures = CollectionsFactory.current().createList();
		while (toBeRemembered.hasNextFigure()) {
			myAffectedFigures.add(toBeRemembered.nextFigure());
		}
	}

	/**
	 * Releases all resources related to an undoable activity
	 */
	public void release() {
		FigureEnumeration fe = getAffectedFigures();
		while (fe.hasNextFigure()) {
			fe.nextFigure().release();
		}
		setAffectedFigures(FigureEnumerator.getEmptyEnumeration());
	}

	/**
	 * Create new set of affected figures for redo operation because
	 * deleting figures in an undo operation makes them unusable
	 * Especially contained figures have been removed from their
	 * observing container like CompositeFigure or DecoratorFigure.
	 * Duplicating these figures re-establishes the dependencies.
	 */
	protected void duplicateAffectedFigures() {
		setAffectedFigures(StandardFigureSelection.duplicateFigures(
			getAffectedFigures(), getAffectedFiguresCount()));
	}

	public DrawingView getDrawingView() {
		return myDrawingView;
	}

	protected void setDrawingView(DrawingView newDrawingView) {
		myDrawingView = newDrawingView;
	}
}