/*
 * @(#)FigureAttributeConstant.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.jhotdraw.framework;

import java.io.Serializable;

/**
 * A FigureAttribute is a constant for accessing a special figure attribute. It
 * does not contain a value but just defines a unique attribute ID. Therefore,
 * they provide a type-safe way of defining attribute constants.
 * (SourceForge feature request ID: <>)
 *
 * @author Wolfram Kaiser
 * @version <$CURRENT_VERSION$>
 */
public class FigureAttributeConstant implements Serializable, Cloneable {

	public static final String FRAME_COLOR_STR = "FrameColor";
	public static final FigureAttributeConstant FRAME_COLOR = new FigureAttributeConstant(FRAME_COLOR_STR, 1);

	public static final String FILL_COLOR_STR = "FillColor";
	public static final FigureAttributeConstant FILL_COLOR = new FigureAttributeConstant(FILL_COLOR_STR, 2);

	public static final String TEXT_COLOR_STR = "TextColor";
	public static final FigureAttributeConstant TEXT_COLOR = new FigureAttributeConstant(TEXT_COLOR_STR, 3);

	public static final String ARROW_MODE_STR = "ArrowMode";
	public static final FigureAttributeConstant ARROW_MODE = new FigureAttributeConstant(ARROW_MODE_STR, 4);

	public static final String FONT_NAME_STR = "FontName";
	public static final FigureAttributeConstant FONT_NAME = new FigureAttributeConstant(FONT_NAME_STR, 5);

	public static final String FONT_SIZE_STR = "FontSize";
	public static final FigureAttributeConstant FONT_SIZE = new FigureAttributeConstant(FONT_SIZE_STR, 6);

	public static final String FONT_STYLE_STR = "FontStyle";
	public static final FigureAttributeConstant FONT_STYLE = new FigureAttributeConstant(FONT_STYLE_STR, 7);

	public static final String URL_STR = "URL";
	public static final FigureAttributeConstant URL = new FigureAttributeConstant(URL_STR, 8);

	public static final String LOCATION_STR = "Location";
	public static final FigureAttributeConstant LOCATION = new FigureAttributeConstant(LOCATION_STR, 9);
	
	public static final String XALIGNMENT_STR = "XAlignment";
	public static final FigureAttributeConstant XALIGNMENT = new FigureAttributeConstant(XALIGNMENT_STR, 10);
	
	public static final String YALIGNMENT_STR = "YAlignment";
	public static final FigureAttributeConstant YALIGNMENT = new FigureAttributeConstant(YALIGNMENT_STR, 11);
	
	public static final String TOP_MARGIN_STR = "TopMargin";
	public static final FigureAttributeConstant TOP_MARGIN = new FigureAttributeConstant(TOP_MARGIN_STR, 12);
	
	public static final String RIGHT_MARGIN_STR = "RightMargin";
	public static final FigureAttributeConstant RIGHT_MARGIN = new FigureAttributeConstant(RIGHT_MARGIN_STR, 13);
	
	public static final String BOTTOM_MARGIN_STR = "BottomMargin";
	public static final FigureAttributeConstant BOTTOM_MARGIN = new FigureAttributeConstant(BOTTOM_MARGIN_STR, 14);
	
	public static final String LEFT_MARGIN_STR = "LeftMargin";
	public static final FigureAttributeConstant LEFT_MARGIN = new FigureAttributeConstant(LEFT_MARGIN_STR, 15);
	
	public static final String POPUP_MENU_STR = "PopupMenu";
	public static final FigureAttributeConstant POPUP_MENU = new FigureAttributeConstant(POPUP_MENU_STR, 16);
	
	private static FigureAttributeConstant[] attributeConstants;

	private int myID;
	private String myName;

	private FigureAttributeConstant(String newName, int newID) {
		setName(newName);
		setID(newID);
		addConstant(this);
	}

	public FigureAttributeConstant(String newName) {
		this(newName, attributeConstants.length+1);
	}

	private void setName(String newName) {
		myName = newName;
	}

	public String getName() {
		return myName;
	}

	private void setID(int newID) {
		myID = newID;
	}

	public int getID() {
		return myID;
	}

	public boolean equals(Object compareObject) {
		if (compareObject == null) {
			return false;
		}
		if (!(compareObject instanceof FigureAttributeConstant)) {
			return false;
		}
		FigureAttributeConstant compareAttribute = (FigureAttributeConstant)compareObject;

		if (compareAttribute.getID() != getID()) {
			return false;
		}

		if ((compareAttribute.getName() == null) && (getName() == null)) {
			return true;
		}
		if ((compareAttribute.getName() != null) && (getName() != null)) {
			return getName().equals(compareAttribute.getName());
		}

		return false;
	}

	public int hashCode() {
		return getID();
	}

	/**
	 * Constants are put into the place according to their ID, thus, it is
	 * recommended to have subsequent attribute IDs.
	 */
	private static void addConstant(FigureAttributeConstant newConstant) {
		int idPos = newConstant.getID() - 1;
		// attribute IDs must be unique, thus no two attributes
		// with the same ID can be added
		if ((idPos < attributeConstants.length) && (attributeConstants[idPos] != null)) {
			throw new JHotDrawRuntimeException("No unique FigureAttribute ID: " + newConstant.getID());
		}
		// increase capacity if necessary
		if (idPos >= attributeConstants.length) {
			FigureAttributeConstant[] tempStrs = new FigureAttributeConstant[idPos + 1];
			System.arraycopy(attributeConstants, 0, tempStrs, 0, attributeConstants.length);
			attributeConstants = tempStrs;
		}
		attributeConstants[idPos] = newConstant;
	}

	/**
	 * @return an existing constant for a given name or create a new one
	 */
	public static FigureAttributeConstant getConstant(String constantName) {
		for (int i = 0; i < attributeConstants.length; i++) {
			FigureAttributeConstant currentAttr = getConstant(i);
			if ((currentAttr != null) && (currentAttr.getName() != null) && (currentAttr.getName().equals(constantName))) {
				return currentAttr;
			}
		}
		return new FigureAttributeConstant(constantName);
	}

	public static FigureAttributeConstant getConstant(int constantId) {
		return attributeConstants[constantId];
	}

	{
		// use static initializer to create List before any constant is created
		// initialize List only for the first constant (during debugging it
		// appeared that the static initializer is invoked for any constant)
		if (attributeConstants == null) {
			attributeConstants = new FigureAttributeConstant[64];
		}
	}
}
