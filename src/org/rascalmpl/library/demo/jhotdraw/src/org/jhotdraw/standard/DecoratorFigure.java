/*
 * @(#)DecoratorFigure.java
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
import java.io.*;

/**
 * DecoratorFigure can be used to decorate other figures with
 * decorations like borders. Decorator forwards all the
 * methods to their contained figure. Subclasses can selectively
 * override these methods to extend and filter their behavior.
 * <hr>
 * <b>Design Patterns</b><P>
 * <img src="images/red-ball-small.gif" width=6 height=6 alt=" o ">
 * <b><a href=../pattlets/sld014.htm>Decorator</a></b><br>
 * DecoratorFigure is a decorator.
 *
 * @see Figure
 *
 * @version <$CURRENT_VERSION$>
 */

public abstract class DecoratorFigure
				extends AbstractFigure
				implements FigureChangeListener {

	/**
	 * The decorated figure.
	 */
	private Figure myDecoratedFigure;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 8993011151564573288L;
	private int decoratorFigureSerializedDataVersion = 1;

	public DecoratorFigure() {
		initialize();
	}

	/**
	 * Constructs a DecoratorFigure and decorates the passed in figure.
	 */
	public DecoratorFigure(Figure figure) {
		initialize();
		decorate(figure);
	}

	/**
	 * Performs additional initialization code before the figure is decorated.
	 * Subclasses may override this method.
	 */
	protected void initialize() {
	}

	/**
	 * Forwards the connection insets to its contained figure..
	 */
	public Insets connectionInsets() {
		return getDecoratedFigure().connectionInsets();
	}

	/**
	 * Forwards the canConnect to its contained figure..
	 */
	public boolean canConnect() {
		return getDecoratedFigure().canConnect();
	}

	/**
	 * Forwards containsPoint to its contained figure.
	 */
	public boolean containsPoint(int x, int y) {
		return getDecoratedFigure().containsPoint(x, y);
	}

	/**
	 * Decorates the given figure.
	 */
	public void decorate(Figure figure) {
		setDecoratedFigure(figure);
		getDecoratedFigure().addToContainer(this);
		//addDependendFigure(getDecoratedFigure());
	}

	/**
	 * Removes the decoration from the contained figure.
	 */
	public Figure peelDecoration() {
		getDecoratedFigure().removeFromContainer(this); //??? set the container to the listener()?
		removeDependendFigure(getDecoratedFigure());
		return getDecoratedFigure();
	}

	public void setDecoratedFigure(Figure newDecoratedFigure) {
		myDecoratedFigure = newDecoratedFigure;
	}

	public Figure getDecoratedFigure() {
		return myDecoratedFigure;
	}

	/**
	 * Forwards displayBox to its contained figure.
	 */
	public Rectangle displayBox() {
		return getDecoratedFigure().displayBox();
	}

	/**
	 * Forwards basicDisplayBox to its contained figure.
	 */
	public void basicDisplayBox(Point origin, Point corner) {
		getDecoratedFigure().basicDisplayBox(origin, corner);
	}

	/**
	 * Forwards draw to its contained figure.
	 */
	public void draw(Graphics g) {
		getDecoratedFigure().draw(g);
	}

	/**
	 * Forwards findFigureInside to its contained figure.
	 */
	public Figure findFigureInside(int x, int y) {
		Figure foundFigure = getDecoratedFigure().findFigureInside(x, y);
		// if the found figure is the same as the one the DecoratorFigure decorates
		// then do not peel of the decoration
		if ((foundFigure != null) && (foundFigure == getDecoratedFigure())) {
			return this;
		}
		else {
			return foundFigure;
		}
	}

	/**
	 * Forwards handles to its contained figure.
	 */
	public HandleEnumeration handles() {
		return getDecoratedFigure().handles();
	}

	/**
	 * Forwards includes to its contained figure.
	 */
	public boolean includes(Figure figure) {
		return (super.includes(figure) || getDecoratedFigure().includes(figure));
	}

	/**
	 * Forwards moveBy to its contained figure.
	 */
	public void moveBy(int x, int y) {
		getDecoratedFigure().moveBy(x, y);
	}

	/**
	 * Forwards basicMoveBy to its contained figure.
	 */
	protected void basicMoveBy(int x, int y) {
		// this will never be called
	}

	/**
	 * Releases itself and the contained figure.
	 */
	public void release() {
		super.release();
		getDecoratedFigure().removeFromContainer(this);
		getDecoratedFigure().release();
	}

	/**
	 * Propagates invalidate up the container chain.
	 * @see FigureChangeListener
	 */
	public void figureInvalidated(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureInvalidated(e);
		}
	}

	public void figureChanged(FigureChangeEvent e) {
	}

	public void figureRemoved(FigureChangeEvent e) {
	}

	/**
	 * Propagates figureRequestUpdate up the container chain.
	 * @see FigureChangeListener
	 */
	public  void figureRequestUpdate(FigureChangeEvent e) {
		if (listener() != null) {
			listener().figureRequestUpdate(e);
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
	 * Forwards figures to its contained figure.
	 */
	public FigureEnumeration figures() {
		return getDecoratedFigure().figures();
	}

	/**
	 * Forwards decompose to its contained figure.
	 */
	public FigureEnumeration decompose() {
		return getDecoratedFigure().decompose();
	}

	/**
	 * Forwards setAttribute to its contained figure.
	 *
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value) {
		getDecoratedFigure().setAttribute(name, value);
	}

	/**
	 * Forwards setAttribute to its contained figure.
	 */
	public void setAttribute(FigureAttributeConstant attributeConstant, Object value) {
		getDecoratedFigure().setAttribute(attributeConstant, value);
	}

	/**
	 * Forwards getAttribute to its contained figure.
	 *
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name) {
		return getDecoratedFigure().getAttribute(name);
	}

	/**
	 * Forwards getAttribute to its contained figure.
	 */
	public Object getAttribute(FigureAttributeConstant attributeConstant) {
		return getDecoratedFigure().getAttribute(attributeConstant);
	}

	/**
	 * Returns the locator used to located connected text.
	 */
	public Locator connectedTextLocator(Figure text) {
		return getDecoratedFigure().connectedTextLocator(text);
	}

	/**
	 * Returns the Connector for the given location.
	 */
	public Connector connectorAt(int x, int y) {
		return getDecoratedFigure().connectorAt(x, y);
	}

	/**
	 * Forwards the connector visibility request to its component.
	 */
	public void connectorVisibility(boolean isVisible, ConnectionFigure courtingConnection) {
		getDecoratedFigure().connectorVisibility(isVisible, null);
	}

	/**
	 * Writes itself and the contained figure to the StorableOutput.
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeStorable(getDecoratedFigure());
	}

	/**
	 * Reads itself and the contained figure from the StorableInput.
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		decorate((Figure)dr.readStorable());
	}

	private void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {

		s.defaultReadObject();

		getDecoratedFigure().addToContainer(this);
	}
/*
	public void visit(FigureVisitor visitor) {
		super.visit(visitor);
//		getDecoratedFigure().visit(visitor);
	}
*/
	public TextHolder getTextHolder() {
		return getDecoratedFigure().getTextHolder();
	}

	public synchronized FigureEnumeration getDependendFigures() {
		return getDecoratedFigure().getDependendFigures();
	}

	public synchronized void addDependendFigure(Figure newDependendFigure) {
		getDecoratedFigure().addDependendFigure(newDependendFigure);
	}

	public synchronized void removeDependendFigure(Figure oldDependendFigure) {
		getDecoratedFigure().removeDependendFigure(oldDependendFigure);
	}
}
