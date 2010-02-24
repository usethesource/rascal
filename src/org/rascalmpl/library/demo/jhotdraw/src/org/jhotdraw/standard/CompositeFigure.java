/*
 * @(#)CompositeFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.standard;

import org.jhotdraw.util.*;
import org.jhotdraw.framework.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.*;

/**
 * A Figure that is composed of several figures. A CompositeFigure
 * doesn't define any layout behavior. It is up to subclassers to
 * arrange the contained figures.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld012.htm>Composite</a></b><br>
 * CompositeFigure enables to treat a composition of figures like
 * a single figure.<br>
 *
 * @see Figure
 *
 * @version <$CURRENT_VERSION$>
 */

public abstract class CompositeFigure
				extends AbstractFigure
				implements FigureChangeListener {

	/**
	 * The figures that this figure is composed of
	 * @see #add
	 * @see #remove
	 */
	protected List fFigures;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 7408153435700021866L;
	private int compositeFigureSerializedDataVersion = 1;
	private transient QuadTree  _theQuadTree;
	protected int _nLowestZ;
	protected int _nHighestZ;

	protected CompositeFigure() {
		fFigures = CollectionsFactory.current().createList();
		_nLowestZ = 0;
		_nHighestZ = 0;
	}

	/**
	 * Adds a figure to the list of figures. Initializes the
	 * the figure's container.
	 *
	 * @param figure to be added to the drawing
	 * @return the figure that was inserted (might be different from the figure specified).
	 */
	public Figure add(Figure figure) {
		if (!containsFigure(figure)) {
			figure.setZValue(++_nHighestZ);
			fFigures.add(figure);
			figure.addToContainer(this);
			_addToQuadTree(figure);
		}
		return figure;
	}

	/**
	 * Adds a list of figures.
	 *
	 * @see #add
	 * @deprecated use addAll(FigureEnumeration) instead
	 */
	public void addAll(List newFigures) {
		addAll(new FigureEnumerator(newFigures));
	}

	/**
	 * Adds a FigureEnumeration of figures.
	 *
	 * @see #add
	 * @param fe (unused) enumeration containing all figures to be added
	 */
	public void addAll(FigureEnumeration fe) {
		while (fe.hasNextFigure()) {
			add(fe.nextFigure());
		}
	}

	/**
	 * Removes a figure from the composite.
	 *
	 * @param figure that is part of the drawing and should be removed
	 * @return the figure that has been removed (might be different from the figure specified)
	 * @see #removeAll
	 */
	public Figure remove(Figure figure) {
		Figure orphanedFigure = orphan(figure);
		if (orphanedFigure != null) {
			orphanedFigure.release();
		}
		return orphanedFigure;
	}

	/**
	 * Removes a list of figures.
	 *
	 * @see #remove
	 * @deprecated use removeAll(FigureEnumeration) instead
	 */
	public void removeAll(List figures) {
		removeAll(new FigureEnumerator(figures));
	}

	/**
	 * Removes a FigureEnumeration of figures.
	 * @see #remove
	 */
	public void removeAll(FigureEnumeration fe) {
		while (fe.hasNextFigure()) {
			remove(fe.nextFigure());
		}
	}

	/**
	 * Removes all children.
	 * @see #remove
	 */
	public void removeAll() {
		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			figure.removeFromContainer(this);
		}
		fFigures.clear();

		_clearQuadTree();
		_nLowestZ = 0;
		_nHighestZ = 0;
	}

	/**
	 * Removes a figure from the figure list, but
	 * doesn't release it. Use this method to temporarily
	 * manipulate a figure outside of the drawing.
	 *
	 * @param figure that is part of the drawing and should be added
	 */
	public synchronized Figure orphan(Figure figure) {
		figure.removeFromContainer(this);
		fFigures.remove(figure);
		_removeFromQuadTree(figure);
		return figure;
	}

	/**
	 * Removes a list of figures from the figure's list
	 * without releasing the figures.
	 *
	 * @see #orphan
	 * @deprecated use orphanAll(FigureEnumeration) instead
	 */
	public void orphanAll(List newFigures) {
		orphanAll(new FigureEnumerator(newFigures));
	}

	public void orphanAll(FigureEnumeration fe) {
		while (fe.hasNextFigure()) {
			orphan(fe.nextFigure());
		}
	}

	/**
	 * Replaces a figure in the drawing without
	 * removing it from the drawing.
	 *
	 * @param figure figure to be replaced
	 * @param replacement figure that should replace the specified figure
	 * @return the figure that has been inserted (might be different from the figure specified)
	 */
	public synchronized Figure replace(Figure figure, Figure replacement) {
		int index = fFigures.indexOf(figure);
		if (index != -1) {
			replacement.setZValue(figure.getZValue());
			replacement.addToContainer(this);   // will invalidate figure
			figure.removeFromContainer(this);
			fFigures.set(index, replacement);
			figure.changed();
			replacement.changed();
		}
		return replacement;
	}

	/**
	 * Sends a figure to the back of the drawing.
	 *
	 * @param figure that is part of the drawing
	 */
	public synchronized void sendToBack(Figure figure) {
		if (containsFigure(figure)) {
			fFigures.remove(figure);
			fFigures.add(0, figure);
			_nLowestZ--;
			figure.setZValue(_nLowestZ);
			figure.changed();
		}
	}

	/**
	 * Brings a figure to the front.
	 *
	 * @param figure that is part of the drawing
	 */
	public synchronized void bringToFront(Figure figure) {
		if (containsFigure(figure)) {
			fFigures.remove(figure);
			fFigures.add(figure);
			_nHighestZ++;
			figure.setZValue(_nHighestZ);
			figure.changed();
		}
	}

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
	public void sendToLayer(Figure figure, int layerNr) {
		if (containsFigure(figure)) {
			if (layerNr >= fFigures.size()) {
				layerNr = fFigures.size() - 1;
			}
			Figure layerFigure = getFigureFromLayer(layerNr);
			int layerFigureZValue = layerFigure.getZValue();
			int figureLayer = getLayer(figure);
			// move figure forward
			if (figureLayer < layerNr) {
				assignFiguresToPredecessorZValue(figureLayer + 1, layerNr);
			}
			else if (figureLayer > layerNr) {
				assignFiguresToSuccessorZValue(layerNr, figureLayer - 1);
			}

			fFigures.remove(figure);
			fFigures.add(layerNr, figure);
			figure.setZValue(layerFigureZValue);
			figure.changed();
		}
	}

	private void assignFiguresToPredecessorZValue(int lowerBound, int upperBound) {
		// cannot shift figures to a lower layer if the lower bound is
		// already the first layer.
		if (upperBound >= fFigures.size()) {
			upperBound = fFigures.size() - 1;
		}

		for (int i = upperBound; i >= lowerBound; i--) {
			Figure currentFigure = (Figure)fFigures.get(i);
			Figure predecessorFigure = (Figure)fFigures.get(i - 1);
			currentFigure.setZValue(predecessorFigure.getZValue());
		}
	}

	private void assignFiguresToSuccessorZValue(int lowerBound, int upperBound) {
		if (upperBound >= fFigures.size()) {
			upperBound = fFigures.size() - 1;
		}

		for (int i = upperBound; i >= lowerBound; i--) {
			Figure currentFigure = (Figure)fFigures.get(i);
			Figure successorFigure = (Figure)fFigures.get(i + 1);
			currentFigure.setZValue(successorFigure.getZValue());
		}
	}

	/**
	 * Gets the layer for a certain figure (first occurrence). The number
	 * returned is the number of the layer in which the figure is placed.
	 *
	 * @param figure figure to be queried for its layering place
	 * @return number of the layer in which the figure is placed and -1 if the
	 *			figure could not be found.
	 * @see #sendToLayer
	 */
	public int getLayer(Figure figure) {
		if (!containsFigure(figure)) {
			return -1;
		}
		else {
			return fFigures.indexOf(figure);
		}
	}

	/**
	 * Gets the figure from a certain layer.
	 *
	 * @param layerNr number of the layer which figure should be returned
	 * @return figure from the layer specified, null, if the layer nr was outside
	 *			the number of possible layer (0...(number of figures - 1))
	 * @see #sendToLayer
	 */
	public Figure getFigureFromLayer(int layerNr) {
		if ((layerNr >= 0) && (layerNr < fFigures.size())) {
			return (Figure)fFigures.get(layerNr);
		}
		else {
			return null;
		}
	}

	/**
	 * Draws all the contained figures
	 * @see Figure#draw
	 */
	public void draw(Graphics g) {
		draw(g, figures());
	}

	/**
	 * Draws only the given figures
	 * @see Figure#draw
	 */
	public void draw(Graphics g, FigureEnumeration fe) {
		while (fe.hasNextFigure()) {
			fe.nextFigure().draw(g);
		}
	}

	/**
	 * Gets a figure at the given index.
	 */
	public Figure figureAt(int i) {
		return (Figure)fFigures.get(i);
	}

	/**
	 * Returns an Enumeration for accessing the contained figures.
     * The enumeration is a snapshot of the current contained figures
     * and is not a "live" enumeration and does not take subsequent
     * changes of the CompositeFigure into account.
	 * The figures are returned in the drawing order.
	 */
	public FigureEnumeration figures() {
		return new FigureEnumerator(CollectionsFactory.current().createList(fFigures));
	}

	/**
	 * Returns an enumeration to iterate in
	 * Z-order back to front over the figures
	 * that lie within the given bounds.
	 */
	public FigureEnumeration figures(Rectangle viewRectangle) {
		if (_theQuadTree != null) {

			FigureEnumeration fe =
				_theQuadTree.getAllWithin(new Bounds(viewRectangle).asRectangle2D());

			List l2 = CollectionsFactory.current().createList();

			while (fe.hasNextFigure()) {
				Figure f = fe.nextFigure();
				//int z = fFigures.indexOf(f);
				l2.add(new OrderedFigureElement(f, f.getZValue()));
			}

			Collections.sort(l2);

			List l3 = CollectionsFactory.current().createList();

			for (Iterator iter = l2.iterator(); iter.hasNext(); ) {
				OrderedFigureElement ofe = (OrderedFigureElement)iter.next();
				l3.add(ofe.getFigure());
			}

			return new FigureEnumerator(l3);
		}

		return figures();
	}

	/**
	 * Gets number of child figures.
	 */
	public int figureCount() {
		return fFigures.size();
	}

	/**
	 * Check whether a given figure is a child figure of this CompositeFigure.
	 */
	public boolean containsFigure(Figure checkFigure) {
		return fFigures.contains(checkFigure);
	}

    /**
	 * Returns an Enumeration for accessing the contained figures
	 * in the reverse drawing order.
	 */
	public final FigureEnumeration figuresReverse() {
		return new ReverseFigureEnumerator(CollectionsFactory.current().createList(fFigures));
	}

	/**
	 * Finds a top level Figure. Use this call for hit detection that
	 * should not descend into the figure's children.
	 */
	public Figure findFigure(int x, int y) {
		FigureEnumeration fe = figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			if (figure.containsPoint(x, y)) {
				return figure;
			}
		}
		return null;
	}

	/**
	 * Finds a top level Figure that intersects the given rectangle.
	 */
	public Figure findFigure(Rectangle r) {
		FigureEnumeration fe = figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			Rectangle fr = figure.displayBox();
			if (r.intersects(fr)) {
				return figure;
			}
		}
		return null;
	}

	/**
	 * Finds a top level Figure, but supresses the passed
	 * in figure. Use this method to ignore a figure
	 * that is temporarily inserted into the drawing.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param without the figure to be ignored during
	 * the find.
	 */
	public Figure findFigureWithout(int x, int y, Figure without) {
		if (without == null) {
			return findFigure(x, y);
		}

		FigureEnumeration fe = figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			if (figure.containsPoint(x, y) && !figure.includes(without)) {
				return figure;
			}
		}
		return null;
	}

	/**
	 * Finds a top level Figure that intersects the given rectangle.
	 * It supresses the passed
	 * in figure. Use this method to ignore a figure
	 * that is temporarily inserted into the drawing.
	 */
	public Figure findFigure(Rectangle r, Figure without) {
		if (without == null) {
			return findFigure(r);
		}

		FigureEnumeration fe = figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			Rectangle fr = figure.displayBox();
			if (r.intersects(fr) && !figure.includes(without)) {
				return figure;
			}
		}
		return null;
	}

	/**
	 * Finds a figure but descends into a figure's
	 * children. Use this method to implement <i>click-through</i>
	 * hit detection, that is, you want to detect the inner most
	 * figure containing the given point.
	 */
	public Figure findFigureInside(int x, int y) {
		FigureEnumeration fe = figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure().findFigureInside(x, y);
			if (figure != null) {
				return figure;
			}
		}

		// bug-fix: 661878
		if (containsPoint(x, y)) {
			return this;
		}
		else {
			return null;
		}
	}

	/**
	 * Finds a figure but descends into a figure's
	 * children. It supresses the passed
	 * in figure. Use this method to ignore a figure
	 * that is temporarily inserted into the drawing.
	 */
	public Figure findFigureInsideWithout(int x, int y, Figure without) {
		if (without == null) {
			return findFigureInside(x, y);
		}

		FigureEnumeration fe = figuresReverse();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			if (figure != without) {
				Figure found = figure.findFigureInside(x, y);
				if ((found != null) &&  !figure.includes(without)) {
					return found;
				}
			}
		}

		if (containsPoint(x, y)) {
			return this;
		}
		else {
			return null;
		}
	}

	/**
	 * Checks if the composite figure has the argument as one of
	 * its children.
	 * @return true if the figure is part of this CompositeFigure, else otherwise
	 */
	public boolean includes(Figure figure) {
		if (super.includes(figure)) {
			return true;
		}

		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			Figure f = fe.nextFigure();
			if (f.includes(figure)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Moves all the given figures by x and y. Doesn't announce
	 * any changes. Subclassers override
	 * basicMoveBy. Clients usually call moveBy.
	 * @see #moveBy
	 */
	protected void basicMoveBy(int x, int y) {
		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			fe.nextFigure().moveBy(x,y);
		}
	}

	/**
	 * Releases the figure and all its children.
	 */
	public void release() {
		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			figure.release();
		}
		// bug-fix: 661879 (CompositeFigure.release releasing itself before containees)
		super.release();
	}

	/**
	 * Propagates the figureInvalidated event to my listener.
	 * @see FigureChangeListener
	 */
	public void figureInvalidated(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureInvalidated(e);
		}
	}

	/**
	 * Propagates the removeFromDrawing request up to the container.
	 * @see FigureChangeListener
	 */
	public void figureRequestRemove(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureRequestRemove(new FigureChangeEvent(this));
		}
	}

	/**
	 * Propagates the requestUpdate request up to the container.
	 * @see FigureChangeListener
	 */
	public void figureRequestUpdate(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureRequestUpdate(e);
		}
	}

	public void figureChanged(FigureChangeEvent e) {
		_removeFromQuadTree(e.getFigure());
		_addToQuadTree(e.getFigure());
	}

	public void figureRemoved(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureRemoved(e);
		}
	}

	/**
	 * Writes the contained figures to the StorableOutput.
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(figureCount());
		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			dw.writeStorable(fe.nextFigure());
		}
	}

	/**
	 * Reads the contained figures from StorableInput.
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		int size = dr.readInt();
		fFigures = CollectionsFactory.current().createList(size);
		for (int i=0; i<size; i++) {
			add((Figure)dr.readStorable());
		}
		init(displayBox());
	}

	private void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {

		s.defaultReadObject();

		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			Figure figure = fe.nextFigure();
			figure.addToContainer(this);
		}

		init(new Rectangle(0, 0));
	}

	/**
	 * Used to optimize rendering.  Rendering of many objects may
	 * be slow until this method is called.  The view rectangle
	 * should at least approximately enclose the CompositeFigure.
	 * If the view rectangle is too small or too large, performance
	 * may suffer.
	 *
	 * Don't forget to call this after loading or creating a
	 * new CompositeFigure.  If you forget, drawing performance may
	 * suffer.
	 */
	public void init(Rectangle viewRectangle) {
		_theQuadTree = new QuadTree(new Bounds(viewRectangle).asRectangle2D());

		FigureEnumeration fe = figures();
		while (fe.hasNextFigure()) {
			_addToQuadTree(fe.nextFigure());
		}
	}

	private void _addToQuadTree(Figure f) {
		if (_theQuadTree != null) {
			// Bugfix: Make sure the rectangle is not zero width or height.
			// Otherwise, the quadTree search in this.figures(Rectangle)
			// will be incorrect. [John Yu, 2002/05/23]
			Rectangle r = f.displayBox();
			if (r.height == 0) {
				r.grow(0, 1);
			}
			if (r.width == 0) {
				r.grow(1, 0);
			}

			_theQuadTree.add(f, new Bounds(r).asRectangle2D());
		}
	}

	private void _removeFromQuadTree(Figure f) {
		if (_theQuadTree != null) {
			_theQuadTree.remove(f);
		}
	}

	private void _clearQuadTree() {
		if (_theQuadTree != null) {
			_theQuadTree.clear();
		}
	}
}
