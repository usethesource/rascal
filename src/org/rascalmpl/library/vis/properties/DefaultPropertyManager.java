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
	/*
	 * p

	public Figure getMouseOver();
	public IValue getOnClick();
	public boolean isShapeClosed();
	public boolean isShapeConnected();
	public boolean isShapeCurved();
	
	public boolean isDraggable();
	 */

	public int getIntegerProperty(Property property) {
		// TODO Auto-generated method stub
		switch(property){
		case DOI:		return getDOI();
		case FILLCOLOR: return getFillColor();
		case FONTCOLOR:	return getFontColor();
		case FONTSIZE: 	return getFontSize();
		case LINECOLOR:	return getLineColor();
		default:
						return -1;
		}
	}

	public float getRealProperty(Property property) {
		switch(property){
		case FROMANGLE: 	return getFromAngle();
		case HANCHOR:		return getHanchor();
		case HGAP:			return getHGap();
		case HEIGHT:		return getHeight();
		case INNERRADIUS:	return getInnerRadius();
		case LINEWIDTH:		return getLineWidth();
		case TEXTANGLE:		return getTextAngle();
		case TOANGLE:		return getToAngle();
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
		default:
					return null;
		}
	}

}
