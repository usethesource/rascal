/*
 * @(#)CutCommand.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.util.List;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.Undoable;
import org.jhotdraw.util.UndoableAdapter;

/**
 * Delete the selection and move the selected figures to
 * the clipboard.
 *
 * @see org.jhotdraw.util.Clipboard
 *
 * @version <$CURRENT_VERSION$>
 */
public class CutCommand extends FigureTransferCommand {

	/**
	 * Constructs a cut command.
	 * @param name the command name
	 * @param newDrawingEditor the DrawingEditor which manages the views
	 */
	public CutCommand(String name, DrawingEditor newDrawingEditor) {
		super(name, newDrawingEditor);
	}

	/**
	 * @see org.jhotdraw.util.Command#execute()
	 */
	public void execute() {
		super.execute();
		setUndoActivity(createUndoActivity());
		/* ricardo_padilha: bugfix for correct delete/undelete behavior
		 * When enumerating the affected figures we must not forget the dependent
		 * figures, since they are deleted as well! 
		 */
		FigureEnumeration fe = view().selection();
		List affected = CollectionsFactory.current().createList();
		Figure f;
		FigureEnumeration dfe;
		while (fe.hasNextFigure()) {
			f = fe.nextFigure();
			affected.add(0, f);
			dfe = f.getDependendFigures();
			if (dfe != null) {
				while (dfe.hasNextFigure()) {
					affected.add(0, dfe.nextFigure());
				}
			}
		}
		fe = new FigureEnumerator(affected);
		getUndoActivity().setAffectedFigures(fe);
		UndoActivity ua = (UndoActivity) getUndoActivity();
		ua.setSelectedFigures(view().selection());
		copyFigures(ua.getSelectedFigures(), ua.getSelectedFiguresCount());
		/* ricardo_padilha: end of bugfix */
		deleteFigures(getUndoActivity().getAffectedFigures());
		view().checkDamage();
	}

	/**
	 * @see org.jhotdraw.standard.AbstractCommand#isExecutableWithView()
	 */
	public boolean isExecutableWithView() {
		return view().selectionCount() > 0;
	}

	/**
	 * Factory method for undo activity
	 * @return Undoable
	 */
	protected Undoable createUndoActivity() {
		return new CutCommand.UndoActivity(this);
	}

	public static class UndoActivity extends UndoableAdapter {

		private FigureTransferCommand myCommand;
		private List mySelectedFigures;

		/**
		 * Constructor for <code>UndoActivity</code>.
		 * @param newCommand
		 */
		public UndoActivity(FigureTransferCommand newCommand) {
			super(newCommand.view());
			myCommand = newCommand;
			setUndoable(true);
			setRedoable(true);
		}

		/**
		 * @see org.jhotdraw.util.Undoable#undo()
		 */
		public boolean undo() {
			if (super.undo() && getAffectedFigures().hasNextFigure()) {
				getDrawingView().clearSelection();
				myCommand.insertFigures(getAffectedFiguresReversed(), 0, 0);
				return true;
			}
			return false;
		}

		/**
		 * @see org.jhotdraw.util.Undoable#redo()
		 */
		public boolean redo() {
			// do not call execute directly as the selection might has changed
			if (isRedoable()) {
				myCommand.copyFigures(getSelectedFigures(), getSelectedFiguresCount());
				myCommand.deleteFigures(getAffectedFigures());
				return true;
			}

			return false;
		}

		/**
		 * Preserve the selection of figures the moment the command was executed.
		 * @param newSelectedFigures
		 */
		public void setSelectedFigures(FigureEnumeration newSelectedFigures) {
			// the enumeration is not reusable therefore a copy is made
			// to be able to undo-redo the command several time
			rememberSelectedFigures(newSelectedFigures);
		}

		/**
		 * Preserve a copy of the enumeration in a private list.
		 * @param toBeRemembered
		 */
		protected void rememberSelectedFigures(FigureEnumeration toBeRemembered) {
			mySelectedFigures = CollectionsFactory.current().createList();
			while (toBeRemembered.hasNextFigure()) {
				mySelectedFigures.add(toBeRemembered.nextFigure());
			}
		}
	
		/**
		 * Returns the selection of figures to perform the command on.
		 * @return
		 */
		public FigureEnumeration getSelectedFigures() {
			return new FigureEnumerator(
				CollectionsFactory.current().createList(mySelectedFigures));
		}

		/**
		 * Returns the size of the selection.
		 * @return
		 */
		public int getSelectedFiguresCount() {
			return mySelectedFigures.size();
		}

		/**
		 * @see org.jhotdraw.util.UndoableAdapter#release()
		 */
		public void release() {
			super.release();
			FigureEnumeration fe = getSelectedFigures();
			while (fe.hasNextFigure()) {
				fe.nextFigure().release();
			}
			setSelectedFigures(FigureEnumerator.getEmptyEnumeration());
		}
	}
}
