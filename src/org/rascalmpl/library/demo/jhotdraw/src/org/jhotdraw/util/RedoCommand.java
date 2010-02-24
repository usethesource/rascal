/*
 * @(#)RedoCommand.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.util;

import org.jhotdraw.standard.*;
import org.jhotdraw.framework.*;

/**
 * Command to redo the latest undone change in the drawing.
 *
 * @version <$CURRENT_VERSION$>
 */ 
public class RedoCommand extends AbstractCommand {

	/**
	 * Constructs a properties command.
	 * @param name the command name
	 * @param newDrawingEditor the DrawingEditor which manages the views
	 */
	public RedoCommand(String name, DrawingEditor newDrawingEditor) {
		super(name, newDrawingEditor);
	}

	public void execute() {
		super.execute();
		UndoManager um = getDrawingEditor().getUndoManager();
		if ((um == null) || !um.isRedoable()) {
			return;
		}
		
		Undoable lastRedoable = um.popRedo();
		// Execute redo
		boolean hasBeenUndone = lastRedoable.redo();
		// Add to undo stack
		if (hasBeenUndone && lastRedoable.isUndoable()) {
			um.pushUndo(lastRedoable);
		}
			
		lastRedoable.getDrawingView().checkDamage();

		getDrawingEditor().figureSelectionChanged(lastRedoable.getDrawingView());
	}
  
	/**
	 * Used in enabling the redo menu item.
	 * Redo menu item will be enabled only when there is at least one redoable
	 * activity in the UndoManager.
	 */
	public boolean isExecutableWithView() {
		UndoManager um = getDrawingEditor().getUndoManager();
		if ((um != null) && (um.getRedoSize() > 0)) {
			return true;
		}

	    return false;
	}
}
