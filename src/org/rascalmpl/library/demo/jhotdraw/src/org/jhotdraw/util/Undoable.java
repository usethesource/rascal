/**
 * @(#)Undoable.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import org.jhotdraw.framework.FigureEnumeration;
import org.jhotdraw.framework.DrawingView;

/**
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public interface Undoable {
	/**
	 * Undo the activity
	 * @return true if the activity could be undone, false otherwise
	 */
	public boolean undo();

	/*
	 * Redo the activity
	 * @return true if the activity could be redone, false otherwise
	 */
	public boolean redo();
	
	public boolean isUndoable();
	
	public void setUndoable(boolean newIsUndoable);
	
	public boolean isRedoable();
	
	public void setRedoable(boolean newIsRedoable);
	
	/**
	 * Releases all resources related to an undoable activity
	 */
	public void release();

	public DrawingView getDrawingView();
	
	public void setAffectedFigures(FigureEnumeration newAffectedFigures);
	
	public FigureEnumeration getAffectedFigures();
	
	public int getAffectedFiguresCount();
}