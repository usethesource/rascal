package org.rascalmpl.library.vis.properties;

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

	

}
