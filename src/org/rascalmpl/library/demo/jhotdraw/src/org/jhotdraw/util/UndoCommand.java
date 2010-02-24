/*
 * @(#)UndoCommand.java
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
 * Command to undo the latest change in the drawing.
 * Undo activities can be undone only once, therefore they
 * are not added to the undo stack again (redo activities
 * can be added to the redo stack again, because they can
 * be redone several times, every time pushing a corresponding
 * undo activity as well).
 *
 * @author  Wolfram Kaiser <mrfloppy@sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */ 
public class UndoCommand extends AbstractCommand {

	/**
	 * Constructs a properties command.
	 * @param name the command name
	 * @param newDrawingEditor the DrawingEditor which manages the views
	 */
	public UndoCommand(String name, DrawingEditor newDrawingEditor) {
		super(name, newDrawingEditor);
	}

	public void execute() {
		super.execute();
		UndoManager um = getDrawingEditor().getUndoManager();

		if ((um == null) || !um.isUndoable()) {
			return;
		}
		
		Undoable lastUndoable = um.popUndo();
		// Execute undo
		boolean hasBeenUndone = lastUndoable.undo();

		// Add to redo stack
		if (hasBeenUndone && lastUndoable.isRedoable()) {
			um.pushRedo(lastUndoable);
		}
		lastUndoable.getDrawingView().checkDamage();
		
		getDrawingEditor().figureSelectionChanged(lastUndoable.getDrawingView());
	}
  
	/**
	 * Used in enabling the undo menu item.
	 * Undo menu item will be enabled only when there is atleast one undoable
	 * activity registered with UndoManager.
	 */
	public boolean isExecutableWithView() {
		UndoManager um = getDrawingEditor().getUndoManager();

		if ((um != null) && (um.getUndoSize() > 0)) {
			return true;
		}

		return false;
	}
}
