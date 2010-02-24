/*
 * @(#)BorderDecorator.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.awt.*;

import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;

/**
 * BorderDecorator decorates an arbitrary Figure with
 * a border.
 *
 * @version <$CURRENT_VERSION$>
 */
public  class BorderDecorator extends DecoratorFigure {

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = 1205601808259084917L;
	private int borderDecoratorSerializedDataVersion = 1;

	private Point myBorderOffset;
	private Color myBorderColor;
	private Color myShadowColor;

	public BorderDecorator() {
	}

	public BorderDecorator(Figure figure) {
		super(figure);
	}

	/**
	 * Performs additional initialization code before the figure is decorated
	 * Subclasses may override this method.
	 */
	protected void initialize() {
		setBorderOffset(new Point(3,3));
	}

	public void setBorderOffset(Point newBorderOffset) {
		myBorderOffset = newBorderOffset;
	}
		
	public Point getBorderOffset() {
		if (myBorderOffset == null) {
			return new Point(0,0);
		}
		else {
			return myBorderOffset;
		}
	}

	/**
	 * Draws a the figure and decorates it with a border.
	 */
	public void draw(Graphics g) {
		Rectangle r = displayBox();
		super.draw(g);
		g.setColor(Color.white);
		g.drawLine(r.x, r.y, r.x, r.y + r.height);
		g.drawLine(r.x, r.y, r.x + r.width, r.y);
		g.setColor(Color.gray);
		g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
		g.drawLine(r.x , r.y + r.height, r.x + r.width, r.y + r.height);
	}

	/**
	 * Gets the displaybox including the border.
	 */
	public Rectangle displayBox() {
		Rectangle r = getDecoratedFigure().displayBox();
		r.grow(getBorderOffset().x, getBorderOffset().y);
		return r;
	}

	/**
	 * Invalidates the figure extended by its border.
	 */
	public void figureInvalidated(FigureChangeEvent e) {
		Rectangle rect = e.getInvalidatedRectangle();
		rect.grow(getBorderOffset().x, getBorderOffset().y);
		super.figureInvalidated(new FigureChangeEvent(this, rect, e));
	}

	public Insets connectionInsets() {
		Insets i = super.connectionInsets();
		i.top -= getBorderOffset().y;
		i.bottom -= getBorderOffset().y;
		i.left -= getBorderOffset().x;
		i.right -= getBorderOffset().x;

		return i;
	}
}
