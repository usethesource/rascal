/*
 * @(#)CreationTool.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;

import org.jhotdraw.framework.DrawingEditor;
import org.jhotdraw.framework.Figure;
import org.jhotdraw.framework.JHotDrawRuntimeException;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.Undoable;

/**
 * A tool to create new figures. The figure to be
 * created is specified by a prototype.
 *
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld029.htm>Prototype</a></b><br>
 * CreationTool creates new figures by cloning a prototype.
 * <hr>
 *
 * @see Figure
 * @see Object#clone
 *
 * @version <$CURRENT_VERSION$>
 */
public class CreationTool extends AbstractTool {

	/**
	 * the list of currently added figures
	 * by: ricardo_padilha.
	 * description: This has been added to provide support for creation tools that
	 *              insert more than one figure to the drawing, for example, by
	 *              maintaining SHIFT down and clicking. However, this class still
	 *              maintains its normal behavior of creating only one figure. 
	 */
	private List    fAddedFigures;

	/**
	 * the currently created figure
	 */
	private Figure  fCreatedFigure;

	/**
	 * the figure that was actually added
	 * Note, this can be a different figure from the one which has been created.
	 */
	private Figure myAddedFigure;

	/**
	 * the prototypical figure that is used to create new figuresthe prototypical figure that is used to create new figures.
	 */
	private Figure  myPrototypeFigure;


	/**
	 * Initializes a CreationTool with the given prototype.
	 */
	public CreationTool(DrawingEditor newDrawingEditor, Figure prototype) {
		super(newDrawingEditor);
		setPrototypeFigure(prototype);
	}

	/**
	 * Constructs a CreationTool without a prototype.
	 * This is for subclassers overriding createFigure.
	 */
	protected CreationTool(DrawingEditor newDrawingEditor) {
		this(newDrawingEditor, null);
	}

	/**
	 * Sets the cross hair cursor.
	 */
	public void activate() {
		super.activate();
		if (isUsable()) {
			getActiveView().setCursor(new AWTCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
		}
		setAddedFigures(CollectionsFactory.current().createList());
	}

	/**
	 * @see org.jhotdraw.framework.Tool#deactivate()
	 */
	public void deactivate() {
		setCreatedFigure(null);
		setAddedFigure(null);
		setAddedFigures(null);
		super.deactivate();
	}

	/**
	 * Creates a new figure by cloning the prototype.
	 */
	public void mouseDown(MouseEvent e, int x, int y) {
		super.mouseDown(e, x, y);
		setCreatedFigure(createFigure());
		setAddedFigure(getActiveView().add(getCreatedFigure()));
		getAddedFigure().displayBox(new Point(getAnchorX(), getAnchorY()), new Point(getAnchorX(), getAnchorY()));
	}

	/**
	 * Creates a new figure by cloning the prototype.
	 */
	protected Figure createFigure() {
		if (getPrototypeFigure() == null) {
			throw new JHotDrawRuntimeException("No protoype defined");
		}
		return (Figure)getPrototypeFigure().clone();
	}

	/**
	 * Adjusts the extent of the created figure
	 */
	public void mouseDrag(MouseEvent e, int x, int y) {
		if (getAddedFigure() != null) {
			getAddedFigure().displayBox(new Point(getAnchorX(), getAnchorY()), new Point(x, y));
		}
	}

	/**
	 * Checks if the created figure is empty. If it is, the figure
	 * is removed from the drawing.
	 * @see Figure#isEmpty
	 */
	public void mouseUp(MouseEvent e, int x, int y) {
		if (getAddedFigure() != null && !getCreatedFigure().isEmpty()) {
			getAddedFigures().add(getAddedFigure());
		}
		else {
			getActiveView().remove(getAddedFigure());
		}

		if (getAddedFigures().isEmpty()) {
			setUndoActivity(null);
		}
		else {
			// use undo activity from paste command...
			setUndoActivity(createUndoActivity());
			// put created figure into a figure enumeration
			getUndoActivity().setAffectedFigures(new FigureEnumerator(getAddedFigures()));
		}
		editor().toolDone();
	}

	/**
	 * As the name suggests this CreationTool uses the Prototype design pattern.
	 * Thus, the prototype figure which is used to create new figures of the same
	 * type by cloning the original prototype figure.
	 * @param newPrototypeFigure figure to be cloned to create new figures
	 */
	protected void setPrototypeFigure(Figure newPrototypeFigure) {
		myPrototypeFigure = newPrototypeFigure;
	}

	/**
	 * As the name suggests this CreationTool uses the Prototype design pattern.
	 * Thus, the prototype figure which is used to create new figures of the same
	 * type by cloning the original prototype figure.
	 * @return figure to be cloned to create new figures
	 */
	protected Figure getPrototypeFigure() {
		return myPrototypeFigure;
	}

	/**
	 * Gets the list of currently added figure
	 */
	protected List getAddedFigures() {
		return fAddedFigures;
	}

	/**
	 * Sets the addedFigures attribute of the CreationTool object
	 */
	protected void setAddedFigures(List newAddedFigures) {
		fAddedFigures = newAddedFigures;
	}

	/**
	 * Gets the currently created figure
	 */
	protected Figure getCreatedFigure() {
		return fCreatedFigure;
	}

	/**
	 * Sets the createdFigure attribute of the CreationTool object
	 */
	protected void setCreatedFigure(Figure newCreatedFigure) {
		fCreatedFigure = newCreatedFigure;
	}

	/**
	 * Gets the figure that was actually added
	 * Note, this can be a different figure from the one which has been created.
	 */
	protected Figure getAddedFigure() {
		return myAddedFigure;
	}

	/**
	 * Sets the addedFigure attribute of the CreationTool object
	 */
	protected void setAddedFigure(Figure newAddedFigure) {
		myAddedFigure = newAddedFigure;
	}

	/**
	 * Factory method for undo activity
	 */
	protected Undoable createUndoActivity() {
		return new PasteCommand.UndoActivity(getActiveView());
	}
}