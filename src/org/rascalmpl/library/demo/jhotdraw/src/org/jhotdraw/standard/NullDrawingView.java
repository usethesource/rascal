/*
 * @(#)NullDrawingView.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import java.awt.*;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.jhotdraw.framework.*;

/**
 * This DrawingView provides a very basic implementation. It does not perform any
 * functionality apart from keeping track of its state represented by some important
 * fields. It is a Null-value object and is used instead of a null reference to
 * avoid null pointer exception. This concept is known as the Null-value object
 * bug pattern.
 *
 * @author  Wolfram Kaiser <mrfloppy@users.sourceforge.net>
 * @version <$CURRENT_VERSION$>
 */
public class NullDrawingView extends JPanel implements DrawingView {

	private DrawingEditor myDrawingEditor;
	private Drawing myDrawing;
	private Painter myUpdateStrategy;
	private Color myBackgroundColor;

	private static Hashtable drawingViewManager = new Hashtable();

	protected NullDrawingView(DrawingEditor editor) {
		setEditor(editor);
		setDrawing(new StandardDrawing());
	}

	/**
	 * Sets the view's editor.
	 */
	public void setEditor(DrawingEditor editor) {
		myDrawingEditor = editor;
	}

	/**
	 * Gets the current tool.
	 */
	public Tool tool() {
		return editor().tool();
	}

	/**
	 * Gets the drawing.
	 */
	public Drawing drawing() {
		return myDrawing;
	}

	/**
	 * Sets and installs another drawing in the view.
	 */
	public void setDrawing(Drawing d) {
		myDrawing = d;
	}

	/**
	 * Gets the editor.
	 */
	public DrawingEditor editor() {
		return myDrawingEditor;
	}

	/**
	 * Adds a figure to the drawing.
	 * @return the added figure.
	 */
	public Figure add(Figure figure) {
		return figure;
	}

	/**
	 * Removes a figure from the drawing.
	 * @return the removed figure
	 */
	public Figure remove(Figure figure) {
		return figure;
	}

	/**
	 * Adds a collection of figures to the drawing.
	 */
	public void addAll(Collection figures) {
		// ignore: do nothing
	}

	/**
	 * Gets the size of the drawing.
	 */
	public Dimension getSize() {
		return new Dimension();
	}

	/**
	 * Gets the minimum dimension of the drawing.
	 */
	public Dimension getMinimumSize() {
		return new Dimension();
	}

	/**
	 * Gets the preferred dimension of the drawing..
	 */
	public Dimension getPreferredSize() {
		return new Dimension();
	}

	/**
	 * Sets the current display update strategy.
	 * @see Painter
	 */
	public void setDisplayUpdate(Painter newUpdateStrategy) {
		myUpdateStrategy = newUpdateStrategy;
	}

	/**
	 * Gets the current display update strategy.
	 * @see Painter
	 */
	public Painter getDisplayUpdate() {
		return myUpdateStrategy;
	}

	/**
	 * Gets an enumeration over the currently selected figures.
	 * The selection is a snapshot of the current selection
	 * which does not get changed anymore
	 *
	 * @return an enumeration with the currently selected figures.
	 */
	public FigureEnumeration selection() {
		return FigureEnumerator.getEmptyEnumeration();
	}

	/**
	 * Gets the currently seleced figures in Z order.
	 * @see #selection
	 * @return a FigureEnumeration with the selected figures. This enumeration
	 * represents a snapshot of the current selection.
	 */
	public FigureEnumeration selectionZOrdered() {
		return FigureEnumerator.getEmptyEnumeration();
	}

	/**
	 * Gets the number of selected figures.
	 */
	public int selectionCount() {
		return 0;
	}

	/**
	 * Test whether a given figure is selected.
	 */
	public boolean isFigureSelected(Figure checkFigure) {
		return false;
	}

	/**
	 * Adds a figure to the current selection.
	 */
	public void addToSelection(Figure figure) {
		// ignore: do nothing
	}

	/**
	 * Adds a Collection of figures to the current selection.
	 */
	public void addToSelectionAll(Collection figures) {
		// ignore: do nothing
	}

	/**
	 * Adds a FigureEnumeration to the current selection.
	 */
	public void addToSelectionAll(FigureEnumeration fe) {
		// ignore: do nothing
	}

	/**
	 * Removes a figure from the selection.
	 */
	public void removeFromSelection(Figure figure) {
		// ignore: do nothing
	}

	/**
	 * If a figure isn't selected it is added to the selection.
	 * Otherwise it is removed from the selection.
	 */
	public void toggleSelection(Figure figure) {
		// ignore: do nothing
	}

	/**
	 * Clears the current selection.
	 */
	public void clearSelection() {
		// ignore: do nothing
	}

	/**
	 * Gets the current selection as a FigureSelection. A FigureSelection
	 * can be cut, copied, pasted.
	 */
	public FigureSelection getFigureSelection() {
		return new StandardFigureSelection(selection(), 0);
	}

	/**
	 * Finds a handle at the given coordinates.
	 * @return the hit handle, null if no handle is found.
	 */
	public Handle findHandle(int x, int y) {
		return null;
	}

	/**
	 * Gets the position of the last click inside the view.
	 */
	public Point lastClick() {
		return new Point();
	}

	/**
	 * Sets the current point constrainer.
	 */
	public void setConstrainer(PointConstrainer p) {
		// ignore: do nothing
	}

	/**
	 * Gets the current grid setting.
	 */
	public PointConstrainer getConstrainer() {
		return null;
	}

	/**
	 * Checks whether the drawing has some accumulated damage
	 */
	public void checkDamage() {
		// ignore: do nothing
	}

	/**
	 * Repair the damaged area
	 */
	public void repairDamage() {
		// ignore: do nothing
	}

	/**
	 * Paints the drawing view. The actual drawing is delegated to
	 * the current update strategy.
	 * @see Painter
	 */
	public void paint(Graphics g) {
		// ignore: do nothing
	}

	/**
	 * Creates an image with the given dimensions
	 */
	public Image createImage(int width, int height) {
		return null;
	}

	/**
	 * Gets a graphic to draw into
	 */
	public Graphics getGraphics() {
		return null;
	}

	/**
	 * Gets the background color of the DrawingView
	 */
	public Color getBackground() {
		return myBackgroundColor;
	}

	/**
	 * Sets the background color of the DrawingView
	 */
	public void setBackground(Color c) {
		myBackgroundColor = c;
	}

	/**
	 * Draws the contents of the drawing view.
	 * The view has three layers: background, drawing, handles.
	 * The layers are drawn in back to front order.
	 */
	public void drawAll(Graphics g) {
		// ignore: do nothing
	}

	/**
	 * Draws the given figures.
	 * The view has three layers: background, drawing, handles.
	 * The layers are drawn in back to front order.
	 */
	public void draw(Graphics g, FigureEnumeration fe) {
		// ignore: do nothing
	}

	/**
	 * Draws the currently active handles.
	 */
	public void drawHandles(Graphics g) {
		// ignore: do nothing
	}

	/**
	 * Draws the drawing.
	 */
	public void drawDrawing(Graphics g) {
		// ignore: do nothing
	}

	/**
	 * Draws the background. If a background pattern is set it
	 * is used to fill the background. Otherwise the background
	 * is filled in the background color.
	 */
	public void drawBackground(Graphics g) {
		// ignore: do nothing
	}

	/**
	 * Sets the cursor of the DrawingView
	 */
	public void setCursor(org.jhotdraw.framework.Cursor c) {
		// ignore: do nothing
	}

	/**
	 * Freezes the view by acquiring the drawing lock.
	 * @see Drawing#lock
	 */
	public void freezeView() {
		// ignore: do nothing
	}

	/**
	 * Unfreezes the view by releasing the drawing lock.
	 * @see Drawing#unlock
	 */
	public void unfreezeView() {
		// ignore: do nothing
	}

	/**
	 * Add a listener for selection changes in this DrawingView.
	 * @param fsl jhotdraw.framework.FigureSelectionListener
	 */
	public void addFigureSelectionListener(FigureSelectionListener fsl) {
		// ignore: do nothing
	}

	/**
	 * Remove a listener for selection changes in this DrawingView.
	 * @param fsl jhotdraw.framework.FigureSelectionListener
	 */
	public void removeFigureSelectionListener(FigureSelectionListener fsl) {
		// ignore: do nothing
	}

	/**
	 * Returns a FigureEnumeration of connection figures
	 */
	public FigureEnumeration getConnectionFigures(Figure inFigure) {
		return FigureEnumerator.getEmptyEnumeration();
	}

	/**
	 * Inserts figures in a drawing at given offset. Optional check for connection figures
	 *
	 *  @return enumeration which has been added to the drawing. The figures in the enumeration
	 *          can have changed during adding them (e.g. they could have been decorated).
	 */
	public FigureEnumeration insertFigures(FigureEnumeration inFigures, int dx, int dy, boolean bCheck) {
		return FigureEnumerator.getEmptyEnumeration();
	}

	public void drawingInvalidated(DrawingChangeEvent e) {
		// ignore: do nothing
	}

	public void drawingRequestUpdate(DrawingChangeEvent e) {
		// ignore: do nothing
	}

	public void drawingTitleChanged(DrawingChangeEvent e) {
		// ignore: do nothing        
	}

	public boolean isInteractive() {
		return false;
	}

	public synchronized static DrawingView getManagedDrawingView(DrawingEditor editor) {
		if (drawingViewManager.containsKey(editor)) {
			return (DrawingView)drawingViewManager.get(editor);
		}
		else {
			DrawingView newDrawingView = new NullDrawingView(editor);
			drawingViewManager.put(editor, newDrawingView);
			return newDrawingView;
		}
	}
}
