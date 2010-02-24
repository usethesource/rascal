/*
 * @(#)LineConnection.java
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
import org.jhotdraw.standard.FigureEnumerator;
import org.jhotdraw.standard.SingleFigureEnumerator;
import org.jhotdraw.standard.AbstractFigure;
import org.jhotdraw.standard.HandleEnumerator;

import java.awt.*;

/**
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public class NullFigure extends AbstractFigure {

	private Rectangle myDisplayBox;

	/**
	 * Moves the figure. This is the
	 * method that subclassers override. Clients usually
	 * call displayBox.
	 * @see #moveBy
	 */
	protected void basicMoveBy(int dx, int dy) {
		myDisplayBox.translate(dx, dy);
	}

	/**
	 * Changes the display box of a figure. This method is
	 * always implemented in figure subclasses.
	 * It only changes
	 * the displaybox and does not announce any changes. It
	 * is usually not called by the client. Clients typically call
	 * displayBox to change the display box.
	 * @param origin the new origin
	 * @param corner the new corner
	 * @see #displayBox
	 */
	public void basicDisplayBox(Point origin, Point corner) {
		myDisplayBox = new Rectangle(origin);
		myDisplayBox.add(corner);
	}

	/**
	 * Gets the display box of a figure
	 * @see #basicDisplayBox
	 */
	public Rectangle displayBox() {
		return new Rectangle(myDisplayBox);
	}

	/**
	 * Draws the figure.
	 * @param g the Graphics to draw into
	 */
	public void draw(Graphics g) {
		// A NullFigure cannot be drawn: it has no graphical representation
	}

	/**
	 * Returns the handles used to manipulate
	 * the figure. Handles is a Factory Method for
	 * creating handle objects.
	 *
	 * @return an type-safe iterator of handles
	 * @see Handle
	 */
	public HandleEnumeration handles() {
		return HandleEnumerator.getEmptyEnumeration();
	}

	/**
	 * Checks if the Figure should be considered as empty.
	 */
	public boolean isEmpty() {
		return true;
	}

	/**
	 * Returns an Enumeration of the figures contained in this figure
	 */
	public FigureEnumeration figures() {
		return FigureEnumerator.getEmptyEnumeration();
	}

	/**
	 * Returns the figure that contains the given point.
	 */
	public Figure findFigureInside(int x, int y) {
		// A NullFigure does not contain other figures
		return null;
	}

	/**
	 * Returns a Clone of this figure
	 */
	public Object clone() {
		return super.clone();
	}

	/**
	 * Checks whether the given figure is contained in this figure.
	 */
	public boolean includes(Figure figure) {
		// A NullFigure does not contain another figure
		return false;
	}

	/**
	 * Decomposes a figure into its parts. A figure is considered
	 * as a part of itself.
	 */
	public FigureEnumeration decompose() {
		return new SingleFigureEnumerator(this);
	}

	/**
	 * Releases a figure's resources. Release is called when
	 * a figure is removed from a drawing. Informs the listeners that
	 * the figure is removed by calling figureRemoved.
	 */
	public void release() {
		// A NullFigure does not occupy any resources
	}

	/**
	 * Invalidates the figure. This method informs its listeners
	 * that its current display box is invalid and should be
	 * refreshed.
	 */
	public void invalidate() {
		// A NullFigure does not have a display box
	}

	/**
	 * Returns the named attribute or null if a
	 * a figure doesn't have an attribute.
	 * All figures support the attribute names
	 * FillColor and FrameColor
	 *
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name) {
		// A NullFigure does not have attributes: it cannot do anything with them
		return null;
	}

	/**
	 * Returns the named attribute or null if a
	 * a figure doesn't have an attribute.
	 * All figures support the attribute names
	 * FillColor and FrameColor
	 */
	public Object getAttribute(FigureAttributeConstant attributeConstant) {
		// A NullFigure does not have attributes: it cannot do anything with them
		return null;
	}

	/**
	 * Sets the named attribute to the new value
	 *
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value) {
		// A NullFigure does not have attributes: it cannot do anything with them
	}

	/**
	 * Sets the named attribute to the new value
	 */
	public void setAttribute(FigureAttributeConstant attributeConstant, Object value) {
		// A NullFigure does not have attributes: it cannot do anything with them
	}
}
