/*
 * @(#)PolyLineFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

// JUnitDoclet begin import
import org.jhotdraw.framework.Connector;
import org.jhotdraw.framework.FigureAttributeConstant;
import org.jhotdraw.framework.HandleEnumeration;
import org.jhotdraw.framework.Locator;
import org.jhotdraw.standard.AbstractFigure;
import org.jhotdraw.standard.HandleEnumerator;
import org.jhotdraw.util.CollectionsFactory;
import org.jhotdraw.util.Geom;
import org.jhotdraw.util.StorableInput;
import org.jhotdraw.util.StorableOutput;
// JUnitDoclet end import

/**
 * A poly line figure consists of a list of points.
 * It has an optional line decoration at the start and end.
 *
 * @see LineDecoration
 *
 * @version <$CURRENT_VERSION$>
 */
public  class PolyLineFigure extends AbstractFigure {

	public final static int ARROW_TIP_NONE  = 0;
	public final static int ARROW_TIP_START = 1;
	public final static int ARROW_TIP_END   = 2;
	public final static int ARROW_TIP_BOTH  = 3;

	protected List                fPoints;
	protected LineDecoration      fStartDecoration = null;
	protected LineDecoration      fEndDecoration = null;
	protected Color               fFrameColor = Color.black;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -7951352179906577773L;
	private int polyLineFigureSerializedDataVersion = 1;

	public PolyLineFigure() {
		this(4);
	}

	public PolyLineFigure(int size) {
		fPoints = CollectionsFactory.current().createList(size);
	}

	public PolyLineFigure(int x, int y) {
		fPoints = CollectionsFactory.current().createList();
		fPoints.add(new Point(x, y));
	}

	public Rectangle displayBox() {
		Iterator iter = points();
		if (iter.hasNext()) {
			// avoid starting with origin 0,0 because that would lead to a too large rectangle
			Rectangle r = new Rectangle((Point)iter.next());

			while (iter.hasNext()) {
				r.add((Point)iter.next());
			}

			return r;
		}
		else {
			return new Rectangle();
		}
	}

	public boolean isEmpty() {
		return (size().width < 3) && (size().height < 3);
	}

	public HandleEnumeration handles() {
		List handles = CollectionsFactory.current().createList(fPoints.size());
		for (int i = 0; i < fPoints.size(); i++) {
			handles.add(new PolyLineHandle(this, locator(i), i));
		}
		return new HandleEnumerator(handles);
	}

	public void basicDisplayBox(Point origin, Point corner) {
	}

	/**
	 * Adds a node to the list of points.
	 */
	public void addPoint(int x, int y) {
		fPoints.add(new Point(x, y));
		changed();
	}

	public Iterator points() {
		return fPoints.iterator();
	}

	public int pointCount() {
		return fPoints.size();
	}

	protected void basicMoveBy(int dx, int dy) {
		Iterator iter = points();
		while (iter.hasNext()) {
			((Point)iter.next()).translate(dx, dy);
		}
	}

	/**
	 * Changes the position of a node.
	 */
	public void setPointAt(Point p, int i) {
		willChange();
		fPoints.set(i, p);
		changed();
	}

	/**
	 * Insert a node at the given point.
	 */
	public void insertPointAt(Point p, int i) {
		fPoints.add(i, p);
		changed();
	}

	public void removePointAt(int i) {
		willChange();
		fPoints.remove(i);
		changed();
	}

	/**
	 * Splits the segment at the given point if a segment was hit.
	 * @return the index of the segment or -1 if no segment was hit.
	 */
	public int splitSegment(int x, int y) {
		int i = findSegment(x, y);
		if (i != -1) {
			insertPointAt(new Point(x, y), i+1);
		}
		return i+1;
	}

	public Point pointAt(int i) {
		return (Point)fPoints.get(i);
	}

	/**
	 * Joins to segments into one if the given point hits a node
	 * of the polyline.
	 * @return true if the two segments were joined.
	 */
	public boolean joinSegments(int x, int y) {
		for (int i= 1; i < fPoints.size()-1; i++) {
			Point p = pointAt(i);
			if (Geom.length(x, y, p.x, p.y) < 3) {
				removePointAt(i);
				return true;
			}
		}
		return false;
	}

	public Connector connectorAt(int x, int y) {
		return new PolyLineConnector(this);
	}

	/**
	 * Sets the start decoration.
	 */
	public void setStartDecoration(LineDecoration l) {
		fStartDecoration = l;
	}

	/**
	 * Returns the start decoration.
	 */
	public LineDecoration getStartDecoration() {
		return fStartDecoration;
	}

	/**
	 * Sets the end decoration.
	 */
	public void setEndDecoration(LineDecoration l) {
		fEndDecoration = l;
	}

	/**
	 * Returns the end decoration.
	 */
	public LineDecoration getEndDecoration() {
		return fEndDecoration;
	}

	public void draw(Graphics g) {
		g.setColor(getFrameColor());
		Point p1, p2;
		for (int i = 0; i < fPoints.size()-1; i++) {
			p1 = pointAt(i);
			p2 = pointAt(i+1);
			drawLine(g, p1.x, p1.y, p2.x, p2.y);
		}
		decorate(g);
	}

	/**
	 * Can be overriden in subclasses to draw different types of lines
	 * (e.g. dotted lines)
	 */
	protected void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
	}

	public boolean containsPoint(int x, int y) {
		Rectangle bounds = displayBox();
		bounds.grow(4,4);
		if (!bounds.contains(x, y)) {
			return false;
		}

		for (int i = 0; i < fPoints.size()-1; i++) {
			Point p1 = pointAt(i);
			Point p2 = pointAt(i+1);
			if (Geom.lineContainsPoint(p1.x, p1.y, p2.x, p2.y, x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the segment of the polyline that is hit by
	 * the given point.
	 * @return the index of the segment or -1 if no segment was hit.
	 */
	public int findSegment(int x, int y) {
		for (int i = 0; i < fPoints.size()-1; i++) {
			Point p1 = pointAt(i);
			Point p2 = pointAt(i+1);
			if (Geom.lineContainsPoint(p1.x, p1.y, p2.x, p2.y, x, y)) {
				return i;
			}
		}
		return -1;
	}

	private void decorate(Graphics g) {
		if (getStartDecoration() != null) {
			Point p1 = pointAt(0);
			Point p2 = pointAt(1);
			getStartDecoration().draw(g, p1.x, p1.y, p2.x, p2.y);
		}
		if (getEndDecoration() != null) {
			Point p3 = pointAt(fPoints.size()-2);
			Point p4 = pointAt(fPoints.size()-1);
			getEndDecoration().draw(g, p4.x, p4.y, p3.x, p3.y);
		}
	}

	/**
	 * Gets the attribute with the given name.
	 * PolyLineFigure maps "ArrowMode"to a
	 * line decoration.
	 *
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name) {
		return getAttribute(FigureAttributeConstant.getConstant(name));
	}

	/**
	 * Gets the attribute with the given name.
	 * PolyLineFigure maps "ArrowMode"to a
	 * line decoration.
	 */
	public Object getAttribute(FigureAttributeConstant attributeConstant) {
		if (attributeConstant.equals(FigureAttributeConstant.FRAME_COLOR)) {
			return getFrameColor();
		}
		else if (attributeConstant.equals(FigureAttributeConstant.ARROW_MODE)) {
			int value = 0;
			if (getStartDecoration() != null) {
				value |= ARROW_TIP_START;
			}
			if (getEndDecoration() != null) {
				value |= ARROW_TIP_END;
			}
			return new Integer(value);
		}
		return super.getAttribute(attributeConstant);
	}

	/**
	 * Sets the attribute with the given name.
	 * PolyLineFigure interprets "ArrowMode"to set
	 * the line decoration.
	 *
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value) {
		setAttribute(FigureAttributeConstant.getConstant(name), value);
	}

	/**
	 * Sets the attribute with the given name.
	 * PolyLineFigure interprets "ArrowMode"to set
	 * the line decoration.
	 */
	public void setAttribute(FigureAttributeConstant attributeConstant, Object value) {
		if (attributeConstant.equals(FigureAttributeConstant.FRAME_COLOR)) {
			setFrameColor((Color)value);
			changed();
		}
		else if (attributeConstant.equals(FigureAttributeConstant.ARROW_MODE)) {
			Integer intObj = (Integer)value;
			if (intObj != null) {
				int decoration = intObj.intValue();
				if ((decoration & ARROW_TIP_START) != 0) {
					setStartDecoration(new ArrowTip());
				}
				else {
					setStartDecoration(null);
				}
				if ((decoration & ARROW_TIP_END) != 0) {
					setEndDecoration(new ArrowTip());
				}
				else {
					setEndDecoration(null);
				}
			}
			changed();
		}
		else {
			super.setAttribute(attributeConstant, value);
		}
	}

	public void write(StorableOutput dw) {
		super.write(dw);
		dw.writeInt(fPoints.size());
		Iterator iter = points();
		while (iter.hasNext()) {
			Point p = (Point)iter.next();
			dw.writeInt(p.x);
			dw.writeInt(p.y);
		}
		dw.writeStorable(fStartDecoration);
		dw.writeStorable(fEndDecoration);
		dw.writeColor(fFrameColor);
	}

	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		int size = dr.readInt();
		fPoints = CollectionsFactory.current().createList(size);
		for (int i=0; i<size; i++) {
			int x = dr.readInt();
			int y = dr.readInt();
			fPoints.add(new Point(x,y));
		}
		setStartDecoration((LineDecoration)dr.readStorable());
		setEndDecoration((LineDecoration)dr.readStorable());
		fFrameColor = dr.readColor();
	}

	/**
	 * Creates a locator for the point with the given index.
	 */
	public static Locator locator(int pointIndex) {
		return new PolyLineLocator(pointIndex);
	}

	protected Color getFrameColor() {
		return fFrameColor;
	}

	protected void setFrameColor(Color c) {
		fFrameColor = c;
	}

	/**
	 * Hook method to change the rectangle that will be invalidated
	 */
	protected Rectangle invalidateRectangle(Rectangle r) {
		// SF-bug id: 533953: provide this method to customize invalidated rectangle
		Rectangle parentR = super.invalidateRectangle(r);
		if (getStartDecoration() != null) {
			parentR.add(getStartDecoration().displayBox());
		}
		if (getEndDecoration() != null) {
			parentR.add(getEndDecoration().displayBox());
		}
		return parentR;
	}
}
