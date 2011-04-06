/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;

public final class DefaultPropertyManager implements IPropertyManager {
	FigurePApplet fpa;
	 
	public DefaultPropertyManager(FigurePApplet fpa){
		this.fpa = fpa;
	}
	
	public FigurePApplet getFPA() {
		return fpa;
	}
	
	public boolean getAlignAnchors(){
		return false;
	}
	
	public String getDirection(){
		return "TD";
	}
	
	public int getDOI() {
		return 1000000;
	}

	public int getFillColor() {
		return 255;
	}

	public String getFont() {
		return "Helvetica";
	}

	public int getFontColor() {
		return 0;
	}

	public int getFontSize() {
		return 12;
	}

	public float getFromAngle() {
		return 0;
	}
	
	public float getHalign() {
		return 0.5f;
	}
	
	public float getHanchor() {
		return 0.5f;
	}

	public float getHGap() {
		return 0;
	}

	public float getHeight() {
		return 0;
	}

	public String getHint() {
		return "";
	}

	public String getId() {
		return "";
	}

	public float getInnerRadius() {
		return 0;
	}

	public String getLayer(){
		return "";
	}
	
	public int getLineColor() {
		return 0;
	}

	public float getLineWidth() {
		return 1;
	}

	public Figure getMouseOver() {
		return null;
	}
	
	public IValue getOnClick() {
		return null;
	}

	public float getTextAngle() {
		return 0;
	}

	public float getVGap() {
		return 0;
	}

	public float getValign() {
		return 0.5f;
	}

	public float getVanchor() {
		return 0.5f;
	}

	public float getWidth() {
		return 0;
	}

	public boolean isShapeClosed() {
		return false;
	}

	public boolean isShapeConnected() {
		return false;
	}

	public boolean isShapeCurved() {
		return false;
	}

	public float getToAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isDraggable() {
		return false;
	}
	
	public boolean getBooleanProperty(Property property) {
		switch(property){
		case ALIGN_ANCHORS:	return getAlignAnchors();
		case SHAPE_CLOSED:	return isShapeClosed();
		case SHAPE_CONNECTED:return isShapeConnected();
		case SHAPE_CURVED:	return isShapeCurved();
		default:
				return false;
		}
	}

	public int getIntegerProperty(Property property) {
		switch(property){
		case DOI:		return getDOI();
		case FILL_COLOR: return getFillColor();
		case FONT_COLOR:	return getFontColor();
		case FONT_SIZE: 	return getFontSize();
		case LINE_COLOR:	return getLineColor();
		default:
						return -1;
		}
	}

	public float getRealProperty(Property property) {
		switch(property){
		case FROM_ANGLE: 	return getFromAngle();
		case HALIGN:		return getHalign();
		case HANCHOR:		return getHanchor();
		case HGAP:			return getHGap();
		case HEIGHT:		return getHeight();
		case INNERRADIUS:	return getInnerRadius();
		case LINE_WIDTH:		return getLineWidth();
		case TEXT_ANGLE:		return getTextAngle();
		case TO_ANGLE:		return getToAngle();
		case VALIGN:		return getValign();
		case VANCHOR:		return getVanchor();
		case VGAP:			return getVGap();
		case WIDTH:			return getWidth();
		default:
							return -1;		
		}
	}

	public String getStringProperty(Property property) {
		switch(property){
		case FONT:	return getFont();
		case HINT:	return getHint();
		case ID:	return getId();
		case DIRECTION:
					return getDirection();
		case LAYER:	return getLayer();
		default:
					return null;
		}
	}

}
