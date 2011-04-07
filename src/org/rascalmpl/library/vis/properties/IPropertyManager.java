/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;

@SuppressWarnings("serial")
public interface IPropertyManager {
	
	static final HashMap<String, Property> propertyNames = new HashMap<String, Property>() {
		{
			put("align",			Property.ALIGN);
			put("alignAnchors",		Property.ALIGN_ANCHORS);
			put("anchor",			Property.ANCHOR);
			put("direction",        Property.DIRECTION);
			put("doi",       		Property.DOI);
			put("fillColor", 		Property.FILL_COLOR);
			put("font", 			Property.FONT);
			put("fontColor", 		Property.FONT_COLOR);
			put("fontSize", 		Property.FONT_SIZE);
			put("fromAngle", 		Property.FROM_ANGLE);
			put("gap", 				Property.GAP);
			put("halign",			Property.HALIGN);
			put("hanchor", 			Property.HANCHOR);
			put("height", 			Property.HEIGHT);
			put("hgap",				Property.HGAP);                  // Only used internally
			put("hint", 			Property.HINT);
			put("id", 				Property.ID);
			put("innerRadius", 		Property.INNERRADIUS);
			put("layer", 			Property.LAYER);
			put("lineColor", 		Property.LINE_COLOR);
			put("lineWidth", 		Property.LINE_WIDTH);
			put("mouseOver", 		Property.MOUSEOVER);
			put("onClick",			Property.ONCLICK);
			put("shapeClosed", 		Property.SHAPE_CLOSED);
			put("shapeConnected", 	Property.SHAPE_CONNECTED);
			put("shapeCurved", 		Property.SHAPE_CURVED);
			put("size", 			Property.SIZE);
			put("textAngle", 		Property.TEXT_ANGLE);
			put("toAngle", 			Property.TO_ANGLE);
			put("valign", 			Property.VALIGN);
			put("vanchor", 			Property.VANCHOR);
			put("vgap", 			Property.VGAP);                 // Only used internally
			put("width", 			Property.WIDTH);
		}
	};
	
	public boolean getBooleanProperty(Property property);
	
	public int getIntegerProperty(Property property);
	public float getRealProperty(Property property);
	public String getStringProperty(Property property);
	
	public IFigureApplet getFPA();
	
	public boolean getAlignAnchors();
	public String getDirection();
	public int getDOI();
	public int getFillColor();
	public String getFont();
	public int getFontColor();
	public int getFontSize();
	public float getFromAngle();
	public float getHalign();
	public float getHanchor();
	public float getHGap();
	public float getHeight();
	public String getHint();
	public String getId();
	public float getInnerRadius();
	public String getLayer();
	public int getLineColor();
	public float getLineWidth();
	public Figure getMouseOver();
	public IValue getOnClick();
	public boolean isShapeClosed();
	public boolean isShapeConnected();
	public boolean isShapeCurved();
	public float getTextAngle();
	public float getToAngle();
	public float getValign();
	public float getVanchor();
	public float getVGap();
	public float getWidth();
	
	public boolean isDraggable();
}
