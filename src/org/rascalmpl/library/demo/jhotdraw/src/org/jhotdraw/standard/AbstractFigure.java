/*
 * @(#)AbstractFigure.java
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
import java.util.List;
import java.io.*;

/**
 * AbstractFigure provides default implementations for
 * the Figure interface.
 *
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld036.htm>Template Method</a></b><br>
 * Template Methods implement default and invariant behavior for
 * figure subclasses.
 * <hr>
 *
 * @see Figure
 * @see Handle
 *
 * @version <$CURRENT_VERSION$>
 */
public abstract class AbstractFigure implements Figure {

	/**
	 * The listeners for a figure's changes.
	 * It is only one listener but this one can be a (chained) MultiCastFigureChangeListener
	 * @see #invalidate
	 * @see #changed
	 * @see #willChange
	 */
	private transient FigureChangeListener fListener;

	/**
	 * The dependend figures which have been added to this container.
	 */
	private List myDependendFigures;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -10857585979273442L;
	private int abstractFigureSerializedDataVersion = 1;
	private int _nZ;

	protected AbstractFigure() {
		 myDependendFigures = CollectionsFactory.current().createList();
	}

	/**
	 * Moves the figure by the given offset.
	 */
	public void moveBy(int dx, int dy) {
		willChange();
		basicMoveBy(dx, dy);
		changed();
	}

	/**
	 * Moves the figure. This is the
	 * method that subclassers override. Clients usually
	 * call displayBox.
	 * @see #moveBy
	 */
	protected abstract void basicMoveBy(int dx, int dy);

	/**
	 * Changes the display box of a figure. Clients usually
	 * call this method. It changes the display box
	 * and announces the corresponding change.
	 * @param origin the new origin
	 * @param corner the new corner
	 * @see #displayBox
	 */
	public void displayBox(Point origin, Point corner) {
		willChange();
		basicDisplayBox(origin, corner);
		changed();
	}

	/**
	 * Sets the display box of a figure. This is the
	 * method that subclassers override. Clients usually
	 * call displayBox.
	 * @see #displayBox
	 */
	public abstract void basicDisplayBox(Point origin, Point corner);

	/**
	 * Gets the display box of a figure.
	 */
	public abstract Rectangle displayBox();

	/**
	 * Returns the handles of a Figure that can be used
	 * to manipulate some of its attributes.
	 * @return a type-safe iterator of handles
	 * @see Handle
	 */
	public abstract HandleEnumeration handles();

	/**
	 * Returns an Enumeration of the figures contained in this figure.
	 * @see CompositeFigure
	 */
	public FigureEnumeration figures() {
		return FigureEnumerator.getEmptyEnumeration();
	}

	/**
	 * Gets the size of the figure. A convenience method.
	 */
	public Dimension size() {
		return new Dimension(displayBox().width, displayBox().height);
	}

	/**
	 * Checks if the figure is empty. The default implementation returns
	 * true if the width or height of its display box is < 3
	 * @see Figure#isEmpty
	 */
	public boolean isEmpty() {
		return (size().width < 3) || (size().height < 3);
	}

	/**
	 * Returns the figure that contains the given point.
	 * In contrast to containsPoint it returns its
	 * innermost figure that contains the point.
	 *
	 * @see #containsPoint
	 */
	public Figure findFigureInside(int x, int y) {
		if (containsPoint(x, y)) {
			return this;
		}
		return null;
	}

	/**
	 * Checks if a point is inside the figure.
	 */
	public boolean containsPoint(int x, int y) {
		return displayBox().contains(x, y);
	}

	/**
	 * Changes the display box of a figure. This is a
	 * convenience method. Implementors should only
	 * have to override basicDisplayBox
	 * @see #displayBox
	 */
	public void displayBox(Rectangle r) {
		displayBox(new Point(r.x, r.y), new Point(r.x+r.width, r.y+r.height));
	}

	/**
	 * Checks whether the given figure is contained in this figure.
	 */
	public boolean includes(Figure figure) {
		return figure == this;
	}

	/**
	 * Decomposes a figure into its parts. It returns a FigureEnumeration
	 * that contains itself.
	 * @return an Enumeration with itself as the only element.
	 */
	public FigureEnumeration decompose() {
		List figures = CollectionsFactory.current().createList(1);
		figures.add(this);
		return new FigureEnumerator(figures);
	}

	/**
	 * Sets the Figure's container and registers the container
	 * as a figure change listener. A figure's container can be
	 * any kind of FigureChangeListener. A figure is not restricted
	 * to have a single container.
	 */
	public void addToContainer(FigureChangeListener c) {
		addFigureChangeListener(c);
		invalidate();
	}

	/**
	 * Removes a figure from the given container and unregisters
	 * it as a change listener.
	 */
	public void removeFromContainer(FigureChangeListener c) {
		invalidate();
		removeFigureChangeListener(c);
	}

	/**
	 * Adds a listener for this figure.
	 */
	public synchronized void addFigureChangeListener(FigureChangeListener l) {
		fListener = FigureChangeEventMulticaster.add(listener(), l);
	}

	/**
	 * Removes a listener for this figure.
	 */
	public synchronized void removeFigureChangeListener(FigureChangeListener l) {
		fListener = FigureChangeEventMulticaster.remove(listener(), l);
	}

	/**
	 * Gets the figure's listners.
	 */
	public synchronized FigureChangeListener listener() {
		return fListener;
	}

	/**
	 * A figure is released from the drawing. You never call this
	 * method directly. Release notifies its listeners.
	 * @see Figure#release
	 */
	public void release() {
		if (listener() != null) {
			listener().figureRemoved(new FigureChangeEvent(this));
		}
	}

	/**
	 * Invalidates the figure. This method informs the listeners
	 * that the figure's current display box is invalid and should be
	 * refreshed.
	 */
	public void invalidate() {
		if (listener() != null) {
			Rectangle r = invalidateRectangle(displayBox());
			listener().figureInvalidated(new FigureChangeEvent(this, r));
		}
	}

	/**
	 * Hook method to change the rectangle that will be invalidated
	 */
	protected Rectangle invalidateRectangle(Rectangle r) {
		r.grow(Handle.HANDLESIZE, Handle.HANDLESIZE);
		return r;
	}

	/**
	 * Informes that a figure is about to change something that
	 * affects the contents of its display box.
	 *
	 * @see Figure#willChange
	 */
	public void willChange() {
		// call invalidate before the change occurs to invalidate the old display area
		invalidate();
	}

	/**
	 * Informs that a figure changed the area of its display box.
	 *
	 * @see FigureChangeEvent
	 * @see Figure#changed
	 */
	public void changed() {
		invalidate();
		if (listener() != null) {
			listener().figureChanged(new FigureChangeEvent(this));
		}
	}

	/**
	 * Gets the center of a figure. A convenice
	 * method that is rarely overridden.
	 */
	public Point center() {
		return Geom.center(displayBox());
	}

	/**
	 * Checks if this figure can be connected. By default
	 * AbstractFigures can be connected.
	 */
	public boolean canConnect() {
		return true;
	}

	/**
	 * Returns the connection inset. The connection inset
	 * defines the area where the display box of a
	 * figure can't be connected. By default the entire
	 * display box can be connected.
	 *
	 */
	public Insets connectionInsets() {
		return new Insets(0, 0, 0, 0);
	}

	/**
	 * Returns the Figures connector for the specified location.
	 * By default a ChopBoxConnector is returned.
	 * @see ChopBoxConnector
	 */
	public Connector connectorAt(int x, int y) {
		return new ChopBoxConnector(this);
	}

	/**
	 * Sets whether the connectors should be visible.
	 * By default they are not visible
	 */
	public void connectorVisibility(boolean isVisible, ConnectionFigure connector) {
	}

	/**
	 * Returns the locator used to located connected text.
	 */
	public Locator connectedTextLocator(Figure text) {
		return RelativeLocator.center();
	}

	/**
	 * Returns the named attribute or null if a
	 * a figure doesn't have an attribute.
	 * By default figures don't have any attributes so getAttribute
	 * returns null.
	 *
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name) {
		return null;
	}

	/**
	 * Returns the named attribute or null if a
	 * a figure doesn't have an attribute.
	 * By default figures don't have any attributes getAttribute
	 * returns null.
	 */
	public Object getAttribute(FigureAttributeConstant attributeConstant) {
		return null;
	}

	/**
	 * Sets the named attribute to the new value. By default
	 * figures don't have any attributes and the request is ignored.
	 *
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value) {
	}

	/**
	 * Sets the named attribute to the new value. By default
	 * figures don't have any attributes and the request is ignored.
	 */
	public void setAttribute(FigureAttributeConstant attributeConstant, Object value) {
	}

	/**
	 * Clones a figure. Creates a clone by using the storable
	 * mechanism to flatten the Figure to stream followed by
	 * resurrecting it from the same stream.
	 *
	 * @see Figure#clone
	 */
	public Object clone() {
		Object clone = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream(200);
		try {
			ObjectOutput writer = new ObjectOutputStream(output);
			writer.writeObject(this);
			writer.close();
		}
		catch (IOException e) {
			System.err.println("Class not found: " + e);
		}

		InputStream input = new ByteArrayInputStream(output.toByteArray());
		try {
			ObjectInput reader = new ObjectInputStream(input);
			clone = reader.readObject();
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
		catch (ClassNotFoundException e) {
			System.err.println("Class not found: " + e);
		}
		return clone;
	}

	/**
	 * Stores the Figure to a StorableOutput.
	 */
	public void write(StorableOutput dw) {
	}

	/**
	 * Reads the Figure from a StorableInput.
	 */
	public void read(StorableInput dr) throws IOException {
	}

	/**
	 * Gets the z value (back-to-front ordering) of this figure.
	 */
	public int getZValue() {
	  return _nZ;
	}

	/**
	 * Sets the z value (back-to-front ordering) of this figure.
	 */
	public void setZValue(int z) {
	  _nZ = z;
	}

	public void visit(FigureVisitor visitor) {
		// remember original listener as listeners might be changed by a visitor
		// (e.g. by calling addToContainer() or removeFromContainer())
		//FigureChangeListener originalListener = listener();
		FigureEnumeration fe = getDependendFigures();

		visitor.visitFigure(this);

		FigureEnumeration visitFigures = figures();
		while (visitFigures.hasNextFigure()) {
			visitFigures.nextFigure().visit(visitor);
		}

		HandleEnumeration visitHandles = handles();
		while (visitHandles.hasNextHandle()) {
			visitor.visitHandle(visitHandles.nextHandle());
		}
/*
		originalListener = listener();
		if (originalListener != null) {
			visitor.visitFigureChangeListener(originalListener);
		}
*/

		while (fe.hasNextFigure()) {
			fe.nextFigure().visit(visitor);
			// or visitor.visitDependendFigure(fe.nextFigure());
		}
	}

	public synchronized FigureEnumeration getDependendFigures() {
		return new FigureEnumerator(myDependendFigures);
	}

	public synchronized void addDependendFigure(Figure newDependendFigure) {
		myDependendFigures.add(newDependendFigure);
	}

	public synchronized void removeDependendFigure(Figure oldDependendFigure) {
		myDependendFigures.remove(oldDependendFigure);
	}

	public TextHolder getTextHolder() {
		return null;
	}

	public Figure getDecoratedFigure() {
		return this;
	}	
}
