/*
 * @(#)TextTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;
import org.jhotdraw.util.FloatingTextField;
import org.jhotdraw.util.UndoableAdapter;
import org.jhotdraw.util.Undoable;
import java.awt.*;
import java.awt.event.*;

/**
 * Tool to create new or edit existing text figures.
 * The editing behavior is implemented by overlaying the
 * Figure providing the text with a FloatingTextField.<p>
 * A tool interaction is done once a Figure that is not
 * a TextHolder is clicked.
 *
 * @see TextHolder
 * @see FloatingTextField
 *
 * @version <$CURRENT_VERSION$>
 */
public class TextTool extends CreationTool {

	private FloatingTextField   myTextField;
	private TextHolder  myTypingTarget;

	/**
	 * The selected figure is different from the TextHolder as the TextHolder
	 * may be included in a DecoratorFigure. Thus, the DecoratorFigure is selected
	 * while the TextFigure is edited.
	 */
	private Figure mySelectedFigure;

	public TextTool(DrawingEditor newDrawingEditor, Figure prototype) {
		super(newDrawingEditor, prototype);
	}

	/**
	 * If the pressed figure is a TextHolder it can be edited otherwise
	 * a new text figure is created.
	 */
	public void mouseDown(MouseEvent e, int x, int y)
	{
		setView((DrawingView)e.getSource());

		if (getTypingTarget() != null) {
			editor().toolDone();
			return;
		}

		TextHolder textHolder = null;
		Figure pressedFigure = drawing().findFigureInside(x, y);
		if (pressedFigure != null) {
			textHolder = pressedFigure.getTextHolder();
			setSelectedFigure(pressedFigure);
		}

		if ((textHolder != null) && textHolder.acceptsTyping()) {
			// do not create a new TextFigure but edit existing one
			beginEdit(textHolder);
		}
		else {
			super.mouseDown(e, x, y);
			// update view so the created figure is drawn before the floating text
			// figure is overlaid. (Note, fDamage should be null in StandardDrawingView
			// when the overlay figure is drawn because a JTextField cannot be scrolled)
			view().checkDamage();
			beginEdit(getCreatedFigure().getTextHolder());
		}
	}

	public void mouseDrag(MouseEvent e, int x, int y) {
	}

	public void mouseUp(MouseEvent e, int x, int y) {
		if (!isActive()) {
			editor().toolDone();
		}
	}

	/**
	 * Terminates the editing of a text figure.
	 */
	public void deactivate() {
		endEdit();
        super.deactivate();
	}

	/**
	 * Sets the text cursor.
	 */
	public void activate() {
		super.activate();
		// JDK1.1 TEXT_CURSOR has an incorrect hot spot
		//view().setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
	}

	/**
	 * Test whether the text tool is currently activated and is displaying
	 * a overlay TextFigure for accepting input.
	 *
	 * @return true, if the text tool has a accepting target TextFigure for its input, false otherwise
	 */
	public boolean isActive() {
		return (getTypingTarget() != null);
	}

	protected void beginEdit(TextHolder figure) {
		if (getFloatingTextField() == null) {
			setFloatingTextField(createFloatingTextField());
		}

		if (figure != getTypingTarget() && getTypingTarget() != null) {
			endEdit();
		}

		getFloatingTextField().createOverlay((Container)view(), figure.getFont());
		getFloatingTextField().setBounds(fieldBounds(figure), figure.getText());

		setTypingTarget(figure);
	}

	protected void endEdit() {
		if (getTypingTarget() != null) {
			if (getAddedFigure() != null) {
				if (!isDeleteTextFigure()) {
					// figure has been created and not immediately deleted
					setUndoActivity(createPasteUndoActivity());
					getUndoActivity().setAffectedFigures(
							new SingleFigureEnumerator(getAddedFigure())
					);
					getTypingTarget().setText(getFloatingTextField().getText());
				}
			}
			else if (isDeleteTextFigure()) {
				// delete action
				setUndoActivity(createDeleteUndoActivity());
				getUndoActivity().setAffectedFigures(
						new SingleFigureEnumerator(getSelectedFigure())
				);
				// perform delete operation of DeleteCommand.UndoActivity
				getUndoActivity().redo();
			}
			else {
				// put affected figure into a figure enumeration
				setUndoActivity(createUndoActivity());
				getUndoActivity().setAffectedFigures(
					new SingleFigureEnumerator(getTypingTarget().getRepresentingFigure()));
				getTypingTarget().setText(getFloatingTextField().getText());
				((TextTool.UndoActivity)getUndoActivity()).setBackupText(getTypingTarget().getText());
			}

			setTypingTarget(null);
			getFloatingTextField().endOverlay();
		}
		else {
			setUndoActivity(null);
		}
		setAddedFigure(null);
		setCreatedFigure(null);
		setSelectedFigure(null);
	}

	protected boolean isDeleteTextFigure() {
		return getFloatingTextField().getText().length() == 0;
	}

	private Rectangle fieldBounds(TextHolder figure) {
		Rectangle box = figure.textDisplayBox();
		int nChars = figure.overlayColumns();
		Dimension d = getFloatingTextField().getPreferredSize(nChars);
		return new Rectangle(box.x, box.y, d.width, d.height);
	}

	protected void setTypingTarget(TextHolder newTypingTarget) {
		myTypingTarget = newTypingTarget;
	}

	protected TextHolder getTypingTarget() {
		return myTypingTarget;
	}

	private void setSelectedFigure(Figure newSelectedFigure) {
		mySelectedFigure = newSelectedFigure;
	}

	protected Figure getSelectedFigure() {
		return mySelectedFigure;
	}

	private FloatingTextField createFloatingTextField() {
		return new FloatingTextField();
	}

	private void setFloatingTextField(FloatingTextField newFloatingTextField) {
		myTextField = newFloatingTextField;
	}

	protected FloatingTextField getFloatingTextField() {
		return myTextField;
	}

	protected Undoable createDeleteUndoActivity() {
		FigureTransferCommand cmd = new DeleteCommand("Delete", editor());
		return new DeleteCommand.UndoActivity(cmd);
	}

	protected Undoable createPasteUndoActivity() {
		return new PasteCommand.UndoActivity(view());
	}

	/**
	 * Factory method for undo activity
	 */
	protected Undoable createUndoActivity() {
		return new TextTool.UndoActivity(view(), getTypingTarget().getText());
	}

	public static class UndoActivity extends UndoableAdapter {
		private String myOriginalText;
		private String myBackupText;

		public UndoActivity(DrawingView newDrawingView, String newOriginalText) {
			super(newDrawingView);
			setOriginalText(newOriginalText);
			setUndoable(true);
			setRedoable(true);
		}

		/*
		 * Undo the activity
		 * @return true if the activity could be undone, false otherwise
		 */
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			getDrawingView().clearSelection();
			setText(getOriginalText());

			return true;
		}

		/*
		 * Redo the activity
		 * @return true if the activity could be redone, false otherwise
		 */
		public boolean redo() {
			if (!super.redo()) {
				return false;
			}

			getDrawingView().clearSelection();
			setText(getBackupText());

			return true;
		}

		protected boolean isValidText(String toBeChecked) {
			return ((toBeChecked != null) && (toBeChecked.length() > 0));
		}

		protected void setText(String newText) {
			FigureEnumeration fe = getAffectedFigures();
			while (fe.hasNextFigure()) {
				Figure currentFigure = fe.nextFigure();
				if (currentFigure.getTextHolder() != null) {
					currentFigure.getTextHolder().setText(newText);
				}
			}
		}

		public void setBackupText(String newBackupText) {
			myBackupText = newBackupText;
		}

		public String getBackupText() {
			return myBackupText;
		}

		public void setOriginalText(String newOriginalText) {
			myOriginalText = newOriginalText;
		}

		public String getOriginalText() {
			return myOriginalText;
		}
	}
}

