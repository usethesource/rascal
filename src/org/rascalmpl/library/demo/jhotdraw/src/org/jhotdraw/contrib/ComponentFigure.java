/*
 * @(#)ComponentFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.contrib;

import org.jhotdraw.figures.AttributeFigure;
import org.jhotdraw.standard.BoxHandleKit;
import org.jhotdraw.standard.HandleEnumerator;
import org.jhotdraw.framework.*;
import org.jhotdraw.util.CollectionsFactory;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Graphics;
import java.util.List;

/**
 * @author  Ming Fang
 * @version <$CURRENT_VERSION$>
 */
public class ComponentFigure extends AttributeFigure {
	private Rectangle bounds;

	/** Holds value of property component. */
	private Component component;

	private ComponentFigure() {
		bounds = new Rectangle();
	}

	/**
	 * @param newComponent a lightweight component
	 */
	public ComponentFigure(Component newComponent) {
		this();
		setComponent(newComponent);
	}

	/**
	 * Changes the display box of a figure. This method is
	 * always implemented in figure subclasses.
	 * It only changes
	 * the displaybox and does not announce any changes. It
	 * is usually not called by the client. Clients typically call
	 * displayBox to change the display box.
	 *
	 * @param origin the new origin
	 * @param corner the new corner
	 * @see #displayBox
	 */
	public void basicDisplayBox(Point origin, Point corner) {
		bounds = new Rectangle(origin);
		bounds.add(corner);
	}

	/**
	 * Moves the figure. This is the
	 * method that subclassers override. Clients usually
	 * call displayBox.
	 * @see #moveBy
	 */
	protected void basicMoveBy(int dx, int dy) {
		bounds.translate(dx, dy);
	}

	/**
	 * Gets the display box of a figure
	 * @see #basicDisplayBox
	 */
	public Rectangle displayBox() {
		return new Rectangle(bounds);
	}

	/**
	 * Returns the handles used to manipulate
	 * the figure. Handles is a Factory Method for
	 * creating handle objects.
	 *
	 * @return a type-safe iterator of handles
	 * @see Handle
	 */
	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList();
		BoxHandleKit.addHandles(this, handles);
		return new HandleEnumerator(handles);
	}

	/**
	 * Getter for property component.
	 * @return Value of property component.
	 */
	public Component getComponent() {
		return this.component;
	}

	/**
	 * Setter for property component.
	 *
	 * @param newComponent New value of property component.
	 */
	protected void setComponent(Component newComponent) {
		this.component = newComponent;
	}

	/**
	 * Draws the figure.
	 * @param g the Graphics to draw into
	 */
	public void draw(Graphics g) {
		// AWT code
		getComponent().setBounds(displayBox());
		//must create a new graphics with a different cordinate
		Graphics componentG = g.create(bounds.x, bounds.y, bounds.width, bounds.height);
		getComponent().paint(componentG);
	}
}
