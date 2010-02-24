/*
 * @(#)DrawingView.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.Collection;

/**
 * DrawingView renders a Drawing and listens to its changes.
 * It receives user input and delegates it to the current tool.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld026.htm>Observer</a></b><br>
 * DrawingView observes drawing for changes via the DrawingListener interface.<br>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld032.htm>State</a></b><br>
 * DrawingView plays the role of the StateContext in
 * the State pattern. Tool is the State.<br>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld034.htm>Strategy</a></b><br>
 * DrawingView is the StrategyContext in the Strategy pattern
 * with regard to the UpdateStrategy. <br>
 * DrawingView is the StrategyContext for the PointConstrainer.
 *
 * @see Drawing
 * @see Painter
 * @see Tool
 *
 * @version <$CURRENT_VERSION$>
 */
public interface DrawingView extends ImageObserver, DrawingChangeListener {

	/**
	 * Sets the view's editor.
	 */
	public void setEditor(DrawingEditor editor);

	/**
	 * Gets the current tool.
	 */
	public Tool tool();

	/**
	 * Gets the drawing.
	 */
	public Drawing drawing();

	/**
	 * Sets and installs another drawing in the view.
	 */
	public void setDrawing(Drawing d);

	/**
	 * Gets the editor.
	 */
	public DrawingEditor editor();

	/**
	 * Adds a figure to the drawing.
	 * @return the added figure.
	 */
	public Figure add(Figure figure);

	/**
	 * Removes a figure from the drawing.
	 * @return the removed figure
	 */
	public Figure remove(Figure figure);

	/**
	 * Adds a collections of figures to the drawing.
	 */
	public void addAll(Collection figures);

	/**
	 * Gets the size of the drawing.
	 */
	public Dimension getSize();

	/**
	 * Gets the minimum dimension of the drawing.
	 */
	public Dimension getMinimumSize();

	/**
	 * Gets the preferred dimension of the drawing..
	 */
	public Dimension getPreferredSize();

	/**
	 * Sets the current display update strategy.
	 * @see Painter
	 */
	public void setDisplayUpdate(Painter updateStrategy);

	/**
	 * Gets the current display update strategy.
	 * @see Painter
	 */
	public Painter getDisplayUpdate();

	/**
	 * Gets an enumeration over the currently selected figures.
	 * The selection is a snapshot of the current selection
	 * which does not get changed anymore
	 *
	 * @return an enumeration with the currently selected figures.
	 */
	public FigureEnumeration selection();

	/**
	 * Gets the currently seleced figures in Z order.
	 * The selection is a snapshot of the current selection
	 * which does not get changed anymore
	 *
	 * @see #selection
	 * @return an enumeration with the currently selected figures.
	 */
	public FigureEnumeration selectionZOrdered();

	/**
	 * Gets the number of selected figures.
	 */
	public int selectionCount();

	/**
	 * Test whether a given figure is selected.
	 */
	public boolean isFigureSelected(Figure checkFigure);

	/**
	 * Adds a figure to the current selection.
	 */
	public void addToSelection(Figure figure);

	/**
	 * Adds a collections of figures to the current selection.
	 */
	public void addToSelectionAll(Collection figures);

	/**
	 * Adds a FigureEnumeration to the current selection.
	 */
	public void addToSelectionAll(FigureEnumeration fe);

	/**
	 * Removes a figure from the selection.
	 */
	public void removeFromSelection(Figure figure);

	/**
	 * If a figure isn't selected it is added to the selection.
	 * Otherwise it is removed from the selection.
	 */
	public void toggleSelection(Figure figure);

	/**
	 * Clears the current selection.
	 */
	public void clearSelection();

	/**
	 * Gets the current selection as a FigureSelection. A FigureSelection
	 * can be cut, copied, pasted.
	 */
	public FigureSelection getFigureSelection();

	/**
	 * Finds a handle at the given coordinates.
	 * @return the hit handle, null if no handle is found.
	 */
	public Handle findHandle(int x, int y);

	/**
	 * Gets the position of the last click inside the view.
	 */
	public Point lastClick();

	/**
	 * Sets the current point constrainer.
	 */
	public void setConstrainer(PointConstrainer p);

	/**
	 * Gets the current grid setting.
	 */
	public PointConstrainer getConstrainer();

	/**
	 * Checks whether the drawing has some accumulated damage
	 */
	public void checkDamage();

	/**
	 * Repair the damaged area
	 */
	public void repairDamage();

	/**
	 * Paints the drawing view. The actual drawing is delegated to
	 * the current update strategy.
	 * @see Painter
	 */
	public void paint(Graphics g);

	/**
	 * Creates an image with the given dimensions
	 */
	public Image createImage(int width, int height);

	/**
	 * Gets a graphic to draw into
	 */
	public Graphics getGraphics();

	/**
	 * Gets the background color of the DrawingView
	 */
	public Color getBackground();

	/**
	 * Sets the background color of the DrawingView
	 */
	public void setBackground(Color c);

	/**
	 * Draws the contents of the drawing view.
	 * The view has three layers: background, drawing, handles.
	 * The layers are drawn in back to front order.
	 */
	public void drawAll(Graphics g);

	/**
	 * Draws the given figures.
	 * The view has three layers: background, drawing, handles.
	 * The layers are drawn in back to front order.
	 */
	public void draw(Graphics g, FigureEnumeration fe);

	/**
	 * Draws the currently active handles.
	 */
	public void drawHandles(Graphics g);

	/**
	 * Draws the drawing.
	 */
	public void drawDrawing(Graphics g);

	/**
	 * Draws the background. If a background pattern is set it
	 * is used to fill the background. Otherwise the background
	 * is filled in the background color.
	 */
	public void drawBackground(Graphics g);

	/**
	 * Sets the cursor of the DrawingView
	 */
	public void setCursor(Cursor c);

	/**
	 * Freezes the view by acquiring the drawing lock.
	 * @see Drawing#lock
	 */
	public void freezeView();

	/**
	 * Unfreezes the view by releasing the drawing lock.
	 * @see Drawing#unlock
	 */
	public void unfreezeView();

	/**
	 * Add a listener for selection changes in this DrawingView.
	 * @param fsl jhotdraw.framework.FigureSelectionListener
	 */
	public void addFigureSelectionListener(FigureSelectionListener fsl);

	/**
	 * Remove a listener for selection changes in this DrawingView.
	 * @param fsl jhotdraw.framework.FigureSelectionListener
	 */
	public void removeFigureSelectionListener(FigureSelectionListener fsl);

	/**
	 * Returns a FigureEnumeration of connection figures
	 */
	public FigureEnumeration getConnectionFigures(Figure inFigure);

	/**
	 * Inserts figures in a drawing at given offset. Optional check for connection figures
	 *
	 *  @return enumeration which has been added to the drawing. The figures in the enumeration
	 *          can have changed during adding them (e.g. they could have been decorated).
	 */
	public FigureEnumeration insertFigures(FigureEnumeration inFigures, int dx, int dy, boolean bCheck);

	/**
	 * Check whether the DrawingView is interactive, i.e. whether it accepts user input
	 * and whether it can display a drawing.
	 */
	public boolean isInteractive();
}
