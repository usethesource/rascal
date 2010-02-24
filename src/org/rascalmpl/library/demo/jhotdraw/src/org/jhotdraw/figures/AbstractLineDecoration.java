/*
 * @(#)AbstractLineDecoration.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import java.io.*;
import java.awt.*;

import org.jhotdraw.framework.*;
import org.jhotdraw.util.*;

/**
 * An standard implementation of a line decoration.
 *
 * @see PolyLineFigure
 *
 * @version <$CURRENT_VERSION$>
 */
public abstract class AbstractLineDecoration implements LineDecoration {

	static final long serialVersionUID = 1577970039258356627L;

	private Color   fFillColor;
	private Color   fBorderColor;
	private transient Rectangle myBounds;

	public AbstractLineDecoration() {
	}

   /**
	* Draws the arrow tip in the direction specified by the given two
	* points.. (template method)
	*/
	public void draw(Graphics g, int x1, int y1, int x2, int y2) {
		// TBD: reuse the Polygon object
		Polygon p = outline(x1, y1, x2, y2);
		myBounds = p.getBounds();
		if (getFillColor() == null) {
			g.fillPolygon(p.xpoints, p.ypoints, p.npoints);
		}
		else {
			Color drawColor = g.getColor();
			g.setColor(getFillColor());
			g.fillPolygon(p.xpoints, p.ypoints, p.npoints);
			g.setColor(drawColor);
		}

		if (getBorderColor() != getFillColor()) {
			Color drawColor = g.getColor();
			g.setColor(getBorderColor());
			g.drawPolygon(p.xpoints, p.ypoints, p.npoints);
			g.setColor(drawColor);
		}
	}

	/**
	 * The LineDecoration has only a displayBox after it has been drawn
	 * at least once. If it has not yet been drawn then a rectangle of size 0
	 * is returned.
	 * @return the display box of a LineDecoration.
	 */
	public Rectangle displayBox() {
		if (myBounds != null) {
			return myBounds;
		}
		else {
			return new Rectangle(0, 0);
		}
	}

   /**
	* Hook method to calculates the outline of an arrow tip.
	*/
	public abstract Polygon outline(int x1, int y1, int x2, int y2);

	/**
	 * Stores the arrow tip to a StorableOutput.
	 */
	public void write(StorableOutput dw) {
		if (getFillColor() != null) {
			FigureAttributes.writeColor(dw, FigureAttributeConstant.FILL_COLOR.getName(), getFillColor());
		}
		else {
			dw.writeString("no" + FigureAttributeConstant.FILL_COLOR.getName());
		}

		if (getBorderColor() != null) {
			FigureAttributes.writeColor(dw, FigureAttributeConstant.FRAME_COLOR.getName(), getBorderColor());
		}
		else {
			dw.writeString("no" + FigureAttributeConstant.FRAME_COLOR.getName());
		}
	}

	/**
	 * Reads the arrow tip from a StorableInput.
	 */
	public void read(StorableInput dr) throws IOException {
		String fillColorId = dr.readString();
		// read color only if one has been written
		if (fillColorId.equals(FigureAttributeConstant.FRAME_COLOR.getName())) {
			setFillColor(FigureAttributes.readColor(dr));
		}
		String borderColorId = dr.readString();
		// read color only if one has been written
		if (borderColorId.equals("BorderColor")
				||  borderColorId.equals(FigureAttributeConstant.FRAME_COLOR.getName())) {
			setBorderColor(FigureAttributes.readColor(dr));
		}
	}

	/**
	 * Sets color with which arrow is filled
	 */
	public void setFillColor(Color fillColor) {
		fFillColor = fillColor;
	}

	/**
	 * Returns color with which arrow is filled
	 */
	public Color getFillColor() {
		return fFillColor;
	}

	/**
	 * Sets color of arrow's border
	 */
	public void setBorderColor(Color borderColor) {
		fBorderColor = borderColor;
	}

	/**
	 * Returns color of arrow's border
	 */
	public Color getBorderColor() {
		return fBorderColor;
	}
}
