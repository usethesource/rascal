/*
 *  @(#)TextAreaTool.java
 *
 *  Project:     JHotdraw - a GUI framework for technical drawings
 *               http://www.jhotdraw.org
 *               http://jhotdraw.sourceforge.net
 *  Copyright:   © by the original author(s) and all contributors
 *  License:     Lesser GNU Public License (LGPL)
 *               http://www.opensource.org/licenses/lgpl-license.html
 */
package org.jhotdraw.contrib;

import org.jhotdraw.framework.DrawingEditor;
import org.jhotdraw.framework.DrawingView;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.FigureEnumeration;
import org.jhotdraw.standard.CreationTool;
import org.jhotdraw.standard.SingleFigureEnumerator;
import org.jhotdraw.standard.TextHolder;
import org.jhotdraw.util.Undoable;
import org.jhotdraw.util.UndoableAdapter;

import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * A TextAreaTool creates TextAreaFigures.<br>
 * To create a new text area, the user drags a rectangle on the drawing on
 * a free spot.<br>
 * When releasing the mouse the tool calls the area's editor to enter the text.<br>
 * If the tool is clicked on an existing area the tool simply calls the
 * area's editor.<br>
 * When creating a new area, if the user leaves the text empty, the newly created
 * area figure is discarded.
 *
 * @author    Eduardo Francos - InContext
 * @created   29 april 2002
 * @version   1.0
 */
public class TextAreaTool extends CreationTool {
	/** The field used for editing */
	protected FloatingTextArea fTextField;

	/** The typing target */
	protected TextHolder fTypingTarget;
	/** The edited figure */
	protected Figure fEditedFigure;

	/**
	 * Constructor for the TextAreaTool object
	 *
	 * @param newDrawingEditor  the managing drawing editor
	 * @param prototype         the prototype for the figure
	 */
	public TextAreaTool(DrawingEditor newDrawingEditor, Figure prototype) {
		super(newDrawingEditor, prototype);
	}

	/**
	 * If the pressed figure is a TextHolder and it accepts editing it can be edited.<br>
	 * If there is no pressed figure a new text figure is created.
	 *
	 * @param e  Description of the Parameter
	 * @param x  Description of the Parameter
	 * @param y  Description of the Parameter
	 */
	public void mouseDown(MouseEvent e, int x, int y) {
		setView((DrawingView)e.getSource());
		Figure pressedFigure = drawing().findFigureInside(x, y);
		TextHolder textHolder = null;
		if (pressedFigure != null) {
			textHolder = pressedFigure.getTextHolder();
		}

		if ((textHolder != null) && (textHolder.acceptsTyping())) {
			beginEdit(textHolder, pressedFigure);
			return;
		}
		if (getTypingTarget() != null) {
			endEdit();

			if (getCreatedFigure() != null && getCreatedFigure().isEmpty()) {
				drawing().remove(getAddedFigure());
				// nothing to undo
				setUndoActivity(null);
			}
			else {
//				// use undo activity from paste command...
//				setUndoActivity(createUndoActivity());
//
//				// put created figure into a figure enumeration
//				getUndoActivity().setAffectedFigures(new SingleFigureEnumerator(getAddedFigure()));
			}
			setTypingTarget(null);
			setCreatedFigure(null);
			setEditedFigure(null);
			setAddedFigure(null);
			editor().toolDone();
		}
		else {
			super.mouseDown(e, x, y);
		}
	}

	/**
	 * Drags to set the initial text area display box
	 *
	 * @param e  Description of the Parameter
	 * @param x  Description of the Parameter
	 * @param y  Description of the Parameter
	 */
	public void mouseDrag(MouseEvent e, int x, int y) {
		// if not creating just ignore
		if (getCreatedFigure() == null) {
			return;
		}
		super.mouseDrag(e, x, y);
	}

	/**
	 * If creating a figure it ends the creation process and calls the editor
	 *
	 * @param e  Description of the Parameter
	 * @param x  Description of the Parameter
	 * @param y  Description of the Parameter
	 */
	public void mouseUp(MouseEvent e, int x, int y) {
		// if not creating just ignore
		if (getCreatedFigure() == null) {
			return;
		}

		// update view so the created figure is drawn before the floating text
		// figure is overlaid. (Note, fDamage should be null in StandardDrawingView
		// when the overlay figure is drawn because a JTextField cannot be scrolled)
		view().checkDamage();
		TextHolder textHolder = (TextHolder)getCreatedFigure();
		if (textHolder.acceptsTyping()) {
			beginEdit(textHolder, getCreatedFigure());
		}
		else {
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
	 * Activates the figure's editor
	 */
	public void activate() {
		super.activate();
		getActiveView().clearSelection();
	}

	/**
	 * Test whether the text tool is currently activated and is displaying
	 * a overlay TextFigure for accepting input.
	 *
	 * @return   true, if the text tool has a accepting target TextFigure for its input, false otherwise
	 */
	public boolean isActivated() {
		return getTypingTarget() != null;
	}

	/**
	 * Begins editing the figure's text
	 *
	 * @param figure          the typing target
	 * @param selectedFigure  the edited figure
	 */
	protected void beginEdit(TextHolder figure, Figure selectedFigure) {
		if (fTextField == null) {
			fTextField = new FloatingTextArea();
		}

		if (figure != getTypingTarget() && getTypingTarget() != null) {
			endEdit();
		}

		fTextField.createOverlay((Container)view(), getFont(figure));
		fTextField.setBounds(fieldBounds(figure), figure.getText());

		setTypingTarget(figure);
		setEditedFigure(selectedFigure);
		setUndoActivity(createUndoActivity());
	}

	/**
	 * Gets the font to be used for editing the figure
	 *
	 * @param figure  the figure
	 * @return        The font
	 */
	protected Font getFont(TextHolder figure) {
		return figure.getFont();
	}

	/** Ends editing of the figure's text */
	protected void endEdit() {
		if ((getTypingTarget() != null) && (fTextField != null)) {
			if (fTextField.getText().length() > 0) {
				getTypingTarget().setText(fTextField.getText());
				// put created figure into a figure enumeration
				getUndoActivity().setAffectedFigures(
						new SingleFigureEnumerator(getEditedFigure()));
				((TextAreaTool.UndoActivity)getUndoActivity()).setBackupText(
						getTypingTarget().getText());
			}
			else {
				drawing().orphan(getAddedFigure());
				// nothing to undo
//	            setUndoActivity(null);
			}

			fTextField.endOverlay();
			fTextField = null;
//			view().checkDamage();
		}
	}

	/**
	 * Returns the bounds fo the figure
	 *
	 * @param figure  the edited figure
	 * @return        Description of the Return Value
	 */
	private Rectangle fieldBounds(TextHolder figure) {
		return figure.textDisplayBox();
	}

	/**
	 * Sets the typingTarget attribute of the TextAreaTool
	 *
	 * @param newTypingTarget  The new typingTarget value
	 */
	protected void setTypingTarget(TextHolder newTypingTarget) {
		fTypingTarget = newTypingTarget;
	}

	/**
	 * Gets the editedFigure attribute of the TextAreaTool
	 *
	 * @return   The editedFigure value
	 */
	protected Figure getEditedFigure() {
		return fEditedFigure;
	}

	/**
	 * Sets the editedFigure attribute of the TextAreaTool
	 *
	 * @param figure  The new editedFigure value
	 */
	protected void setEditedFigure(Figure figure) {
		fEditedFigure = figure;
	}

	/**
	 * Gets the typingTarget attribute of the TextAreaTool
	 *
	 * @return   The typingTarget value
	 */
	protected TextHolder getTypingTarget() {
		return fTypingTarget;
	}

	/**
	 * Factory method for undo activity
	 *
	 * @return   Description of the Return Value
	 */
	protected Undoable createUndoActivity() {
		return new TextAreaTool.UndoActivity(view(), getTypingTarget().getText());
	}

	/**
	 * Handles undo/redo for text areas
	 *
	 * @author    gualo
	 */
	public static class UndoActivity extends UndoableAdapter {
		/** The original text */
		private String myOriginalText;
		/** The backup text */
		private String myBackupText;

		/**
		 * Constructor for the UndoActivity object
		 *
		 * @param newDrawingView   Description of the Parameter
		 * @param newOriginalText  Description of the Parameter
		 */
		public UndoActivity(DrawingView newDrawingView, String newOriginalText) {
			super(newDrawingView);
			setOriginalText(newOriginalText);
			setUndoable(true);
			setRedoable(true);
		}

		/*
		 *  Undo the activity
		 *  @return true if the activity could be undone, false otherwise
		 */
		/**
		 * Undo the activity
		 *
		 * @return   Description of the Return Value
		 */
		public boolean undo() {
			if (!super.undo()) {
				return false;
			}

			getDrawingView().clearSelection();

			if (!isValidText(getOriginalText())) {
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().drawing().orphan(fe.nextFigure());
				}
			}
			// add text figure if it has been removed (no backup text)
			else if (!isValidText(getBackupText())) {
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().add(fe.nextFigure());
				}
				setText(getOriginalText());
			}
			else {
				setText(getOriginalText());
			}

			return true;
		}

		/*
		 *  Redo the activity
		 *  @return true if the activity could be redone, false otherwise
		 */
		/**
		 * Redo the activity
		 *
		 * @return   Description of the Return Value
		 */
		public boolean redo() {
			if (!super.redo()) {
				return false;
			}

			getDrawingView().clearSelection();

			// the text figure did exist but was remove
			if (!isValidText(getBackupText())) {
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().drawing().orphan(fe.nextFigure());
				}
			}
			// the text figure didn't exist before
			else if (!isValidText(getOriginalText())) {
				FigureEnumeration fe = getAffectedFigures();
				while (fe.hasNextFigure()) {
					getDrawingView().drawing().add(fe.nextFigure());
					setText(getBackupText());
				}
			}
			else {
				setText(getBackupText());
			}

			return true;
		}

		/**
		 * Validates the text in the undo activity
		 *
		 * @param toBeChecked  Description of the Parameter
		 * @return             The validText value
		 */
		protected boolean isValidText(String toBeChecked) {
			return ((toBeChecked != null) && (toBeChecked.length() > 0));
		}

		/**
		 * Sets the text attribute of the UndoActivity
		 *
		 * @param newText  The new text value
		 */
		protected void setText(String newText) {
			FigureEnumeration fe = getAffectedFigures();
			while (fe.hasNextFigure()) {
				Figure currentFigure = fe.nextFigure();
				if (currentFigure.getTextHolder() != null) {
					currentFigure.getTextHolder().setText(newText);
				}
			}
		}

		/**
		 * Sets the backupText attribute of the UndoActivity
		 *
		 * @param newBackupText  The new backupText value
		 */
		public void setBackupText(String newBackupText) {
			myBackupText = newBackupText;
		}

		/**
		 * Gets the backupText attribute of the UndoActivity
		 *
		 * @return   The backupText value
		 */
		public String getBackupText() {
			return myBackupText;
		}

		/**
		 * Sets the originalText attribute of the UndoActivity
		 *
		 * @param newOriginalText  The new originalText value
		 */
		public void setOriginalText(String newOriginalText) {
			myOriginalText = newOriginalText;
		}

		/**
		 * Gets the originalText attribute of the UndoActivity
		 *
		 * @return   The originalText value
		 */
		public String getOriginalText() {
			return myOriginalText;
		}
	}
}
