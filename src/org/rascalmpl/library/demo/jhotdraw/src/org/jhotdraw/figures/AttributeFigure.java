/*
 * @(#)AttributeFigure.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.figures;

import org.jhotdraw.util.*;
import org.jhotdraw.framework.*;
import org.jhotdraw.standard.*;

import java.awt.*;
import java.io.*;

/**
 * A figure that can keep track of an open ended set of attributes.
 * The attributes are stored in a dictionary implemented by
 * FigureAttributes.
 *
 * @see Figure
 * @see Handle
 * @see FigureAttributes
 *
 * @version <$CURRENT_VERSION$>
 */
public abstract class AttributeFigure extends AbstractFigure {

	/**
	 * The attributes of a figure. Each figure can have
	 * an open ended set of attributes. Attributes are
	 * identified by name.
	 * @see #getAttribute
	 * @see #setAttribute
	 */
	private FigureAttributes        fAttributes;

	/**
	 * The default attributes associated with a figure.
	 * If a figure doesn't have an attribute set, a default
	 * value from this shared attribute set is returned.
	 * @see #getAttribute
	 * @see #setAttribute
	 */
	private static FigureAttributes fgDefaultAttributes = null;

	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -10857585979273442L;
	private int attributeFigureSerializedDataVersion = 1;

	protected AttributeFigure() { }

	/**
	 * Draws the figure in the given graphics. Draw is a template
	 * method calling drawBackground followed by drawFrame.
	 */
	public void draw(Graphics g) {
		Color fill = getFillColor();
		if (!ColorMap.isTransparent(fill)) {
			g.setColor(fill);
			drawBackground(g);
		}
		Color frame = getFrameColor();
		if (!ColorMap.isTransparent(frame)) {
			g.setColor(frame);
			drawFrame(g);
		}
	}

	/**
	 * Draws the background of the figure.
	 * @see #draw
	 */
	protected void drawBackground(Graphics g) {
	}

	/**
	 * Draws the frame of the figure.
	 * @see #draw
	 */
	protected void drawFrame(Graphics g) {
	}

	/**
	 * Gets the fill color of a figure. This is a convenience
	 * method.
	 * @see #getAttribute
	 */
	public Color getFillColor() {
		return (Color) getAttribute(FigureAttributeConstant.FILL_COLOR);
	}

	/**
	 * Gets the frame color of a figure. This is a convenience
	 * method.
	 * @see #getAttribute
	 */
	public Color getFrameColor() {
		return (Color) getAttribute(FigureAttributeConstant.FRAME_COLOR);
	}

	//---- figure attributes ----------------------------------

	private static void initializeAttributes() {
		fgDefaultAttributes = new FigureAttributes();
		fgDefaultAttributes.set(FigureAttributeConstant.FRAME_COLOR, Color.black);
		fgDefaultAttributes.set(FigureAttributeConstant.FILL_COLOR,  new Color(0x70DB93));
		fgDefaultAttributes.set(FigureAttributeConstant.TEXT_COLOR,  Color.black);
		fgDefaultAttributes.set(FigureAttributeConstant.ARROW_MODE,  new Integer(0));
		fgDefaultAttributes.set(FigureAttributeConstant.FONT_NAME,  "Helvetica");
		fgDefaultAttributes.set(FigureAttributeConstant.FONT_SIZE,   new Integer(12));
		fgDefaultAttributes.set(FigureAttributeConstant.FONT_STYLE,  new Integer(Font.PLAIN));
	}

	/**
	 * Sets or adds a default value for a named attribute
	 * @see #getAttribute
	 */
	public static Object setDefaultAttribute(String name, Object value) {
		// save current value to return it
		Object currentValue = getDefaultAttribute(name);

		fgDefaultAttributes.set(FigureAttributeConstant.getConstant(name), value);
		return currentValue;
	}

	/**
	 * Initializes a  default value for a named attribute
	 * The difference between this method and setDefaultAttribute is that
	 * if the attribute is already set then it will not be changed.<BR>
	 * The purpose is to allow more than one source requiring the attribute
	 * to initialize it, but only the first initialization will be used.
	 *
	 * @see #getAttribute
	 * @see #setDefaultAttribute
	 */
	public static Object initDefaultAttribute(String name, Object value) {
		// get current value
		Object currentValue = getDefaultAttribute(name);

		// if it's already there skip the setting
		if (currentValue != null) {
			return currentValue;
		}

		fgDefaultAttributes.set(FigureAttributeConstant.getConstant(name), value);
		return null;
	}

	/**
	 * Gets a the default value for a named attribute
	 * @see #getAttribute
	 */
	public static Object getDefaultAttribute(String name) {
		if (fgDefaultAttributes == null) {
			initializeAttributes();
		}
		return fgDefaultAttributes.get(FigureAttributeConstant.getConstant(name));
	}

	public static Object getDefaultAttribute(FigureAttributeConstant attributeConstant) {
		if (fgDefaultAttributes == null) {
			initializeAttributes();
		}
		return fgDefaultAttributes.get(attributeConstant);
	}
	/**
	 * Returns the named attribute or null if a
	 * a figure doesn't have an attribute.
	 * All figures support the attribute names
	 * FillColor and FrameColor
	 * @deprecated use getAttribute(FigureAttributeConstant) instead
	 */
	public Object getAttribute(String name) {
		return getAttribute(FigureAttributeConstant.getConstant(name));
	}

	public Object getAttribute(FigureAttributeConstant attributeConstant) {
		if (fAttributes != null) {
			if (fAttributes.hasDefined(attributeConstant)) {
				return fAttributes.get(attributeConstant);
			}
		}
		return getDefaultAttribute(attributeConstant);
	}

	/**
	 * Sets the named attribute to the new value
	 * @deprecated use setAttribute(FigureAttributeConstant, Object) instead
	 */
	public void setAttribute(String name, Object value) {
		setAttribute(FigureAttributeConstant.getConstant(name), value);
	}

	public void setAttribute(FigureAttributeConstant attributeConstant, Object value) {
		if (fAttributes == null) {
			fAttributes = new FigureAttributes();
		}
		fAttributes.set(attributeConstant, value);
		changed();
	}

	/**
	 * Stores the Figure to a StorableOutput.
	 */
	public void write(StorableOutput dw) {
		super.write(dw);
		if (fAttributes == null) {
			dw.writeString("no_attributes");
		}
		else {
			dw.writeString("attributes");
			fAttributes.write(dw);
		}
	}

	/**
	 * Reads the Figure from a StorableInput.
	 */
	public void read(StorableInput dr) throws IOException {
		super.read(dr);
		String s = dr.readString();
		if (s.toLowerCase().equals("attributes")) {
			fAttributes = new FigureAttributes();
			fAttributes.read(dr);
		}
	}

	private void writeObject(ObjectOutputStream o) throws IOException {
		// Filter out Popup menu: cannot serialize any associated action-Listeners
		// Work-around for Java-Bug: 4240860
		Object associatedMenu = getAttribute(Figure.POPUP_MENU);
		if (associatedMenu != null) {
			setAttribute(Figure.POPUP_MENU, null);
		}

		o.defaultWriteObject();

		if (associatedMenu != null) {
			setAttribute(Figure.POPUP_MENU, associatedMenu);
		}
	}
}
