/*
 * @(#)Drawing.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import org.jhotdraw.util.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.Serializable;

/**
 * Drawing is a container for figures.
 * <p>
 * Drawing sends out DrawingChanged events to DrawingChangeListeners
 * whenever a part of its area was invalidated.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld026.htm>Observer</a></b><br>
 * The Observer pattern is used to decouple the Drawing from its views and
 * to enable multiple views.<hr>
 *
 * @see Figure
 * @see DrawingView
 * @see FigureChangeListener
 *
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */

public interface Drawing
		extends Storable, FigureChangeListener, Serializable {

	/**
	 * Releases the drawing and its contained figures.
	 */
	public void release();

	/**
	 * Returns an enumeration to iterate in
	 * Z-order back to front over the figures.
	 */
	public FigureEnumeration figures();

	/**
	 * Returns an enumeration to iterate in
	 * Z-order back to front over the figures
	 * that lie within the absolute bounds.
	 */
	public FigureEnumeration figures(Rectangle viewRectangle);

	/**
	 * Returns an enumeration to iterate in
	 * Z-order front to back over the figures.
	 */
	public FigureEnumeration figuresReverse();

	/**
	 * Finds a top level Figure. Use this call for hit detection that
	 * should not descend into the figure's children.
	 */
	public Figure findFigure(int x, int y);

	/**
	 * Finds a top level Figure that intersects the given rectangle.
	 */
	public Figure findFigure(Rectangle r);

	/**
	 * Finds a top level Figure, but supresses the passed
	 * in figure. Use this method to ignore a figure
	 * that is temporarily inserted into the drawing.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param without the figure to be ignored during
	 * the find.
	 */
	public Figure findFigureWithout(int x, int y, Figure without);

	/**
	 * Finds a top level Figure that intersects the given rectangle.
	 * It supresses the passed
	 * in figure. Use this method to ignore a figure
	 * that is temporarily inserted into the drawing.
	 */
	public Figure findFigure(Rectangle r, Figure without);

	/**
	 * Finds a figure but descends into a figure's
	 * children. Use this method to implement <i>click-through</i>
	 * hit detection, that is, you want to detect the inner most
	 * figure containing the given point.
	 */
	public Figure findFigureInside(int x, int y);

	/**
	 * Finds a figure but descends into a figure's
	 * children. It supresses the passed
	 * in figure. Use this method to ignore a figure
	 * that is temporarily inserted into the drawing.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param without the figure to be ignored during
	 * the find.
	 */
	public Figure findFigureInsideWithout(int x, int y, Figure without);

	/**
	 * Checks if the composite figure has the argument as one of
	 * its children.
	 *
	 * @param figure figure to be searched in all descendants
	 * @return true if the figure is part of this Drawing, else otherwise
	 */
	public boolean includes(Figure figure);

	/**
	 * Check whether a given figure is a (direct) child figure of this CompositeFigure.
	 *
	 * @param figure figure to be searched in all direct descendents
	 * @return true if the figure is a direct child of this Drawing, else otherwise
	 */
	public boolean containsFigure(Figure figure);

	/**
	 * Adds a listener for this drawing.
	 */
	public void addDrawingChangeListener(DrawingChangeListener listener);

	/**
	 * Removes a listener from this drawing.
	 */
	public void removeDrawingChangeListener(DrawingChangeListener listener);

	/**
	 * Gets the listeners of a drawing.
	 *
	 * @return new iterator of all registered change listener
	 */
	public Iterator drawingChangeListeners();

	/**
	 * Adds a figure and sets its container to refer to this drawing.
	 *
	 * @param figure to be added to the drawing
	 * @return the figure that was inserted (might be different from the figure specified).
	 */
	public Figure add(Figure figure);

	/**
	 * Adds a list of figures.
	 *
	 * @deprecated use addAll(FigureEnumeration) instead
	 */
	public void addAll(List newFigures);

	/**
	 * Adds a FigureEnumeration of figures.
	 *
	 * @param fe (unused) enumeration containing all figures to be added
	 * @see #add
	 */
	public void addAll(FigureEnumeration fe);

	/**
	 * Removes the figure from the drawing and releases it.
	 *
	 * @param figure that is part of the drawing and should be removed
	 * @return the figure that has been removed (might be different from the figure specified)
	 */
	public Figure remove(Figure figure);

	/**
	 * Removes a figure from the figure list, but
	 * doesn't release it. Use this method to temporarily
	 * manipulate a figure outside of the drawing.
	 *
	 * @param figure that is part of the drawing and should be added
	 */
	public Figure orphan(Figure figure);

	/**
	 * Removes a list of figures from the figure's list
	 * without releasing the figures.
	 *
	 * @see #orphan
	 * @deprecated use orphanAll(FigureEnumeration) instead
	 */
	public void orphanAll(List orphanFigures);

	/**
	 * Removes a FigureEnumeration of figures from the figure's list
	 * without releasing the figures.
	 * @see #orphan
	 */
	public void orphanAll(FigureEnumeration fe);

	/**
	 * Removes a list of figures .
	 *
	 * @see #remove
	 * @deprecated use removeAll(FigureEnumeration) instead
	 */
	public void removeAll(List figures);

	/**
	 * Removes a FigureEnumeration of figures.
	 * @see #remove
	 */
	public void removeAll(FigureEnumeration fe);

	/**
	 * Replaces a figure in the drawing without removing it from the drawing.
	 * The figure to be replaced must be part of the drawing.
	 *
	 * @param figure figure to be replaced
	 * @param replacement figure that should replace the specified figure
	 * @return the figure that has been inserted (might be different from the figure specified)
	 */
	public Figure replace(Figure figure, Figure replacement);

	/**
	 * Sends a figure to the back of the drawing.
	 *
	 * @param figure that is part of the drawing
	 */
	public void sendToBack(Figure figure);

	/**
	 * Brings a figure to the front.
	 *
	 * @param figure that is part of the drawing
	 */
	public void bringToFront(Figure figure);

	/**
	 * Sends a figure to a certain layer within a drawing. Each figure
	 * lays in a unique layer and the layering order decides which
	 * figure is drawn on top of another figure. Figures with a higher
	 * layer number have usually been added later and may overlay
	 * figures in lower layers. Layers are counted from to (the number
	 * of figures - 1).
	 * The figure is removed from its current layer (if it has been already
	 * part of this drawing) and is transferred to the specified layers after
	 * all figures between the original layer and the new layer are shifted to
	 * one layer below to fill the layer sequence. It is not possible to skip a
	 * layer number and if the figure is sent to a layer beyond the latest layer
	 * it will be added as the last figure to the drawing and its layer number
	 * will be set to the be the one beyond the latest layer so far.
	 *
	 * @param figure figure to be sent to a certain layer
	 * @param layerNr target layer of the figure
	 */
	public void sendToLayer(Figure figure, int layerNr);

	/**
	 * Gets the layer for a certain figure (first occurrence). The number
	 * returned is the number of the layer in which the figure is placed.
	 *
	 * @param figure figure to be queried for its layering place
	 * @return number of the layer in which the figure is placed and -1 if the
	 *			figure could not be found.
	 * @see #sendToLayer
	 */
	public int getLayer(Figure figure);

	/**
	 * Gets the figure from a certain layer.
	 *
	 * @param layerNr number of the layer which figure should be returned
	 * @return figure from the layer specified, null, if the layer nr was outside
	 *			the number of possible layer (0...(number of figures - 1))
	 * @see #sendToLayer
	 */
	public Figure getFigureFromLayer(int layerNr);

	/**
	 * Draws all the figures back to front.
	 */
	public void draw(Graphics g);

	/**
	 * Draws only the given figures.
	 */
	public void draw(Graphics g, FigureEnumeration fe);

	/**
	 * Acquires the drawing lock.
	 */
	public void lock();

	/**
	 * Releases the drawing lock.
	 */
	public void unlock();

	/**
	 * Used to optimize rendering.  Rendering of many objects may
	 * be slow until this method is called.  The view rectangle
	 * should at least approximately enclose the CompositeFigure.
	 * If the view rectangle is too small or too large, performance
	 * may suffer.
	 */
	public void init(Rectangle viewRectangle);

	public String getTitle();
	public void setTitle(String name);
}
