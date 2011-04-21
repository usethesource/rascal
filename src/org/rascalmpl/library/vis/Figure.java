/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis;

import java.util.Vector;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.library.vis.containers.Chart;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;
import org.rascalmpl.library.vis.properties.descriptions.ColorProp;
import org.rascalmpl.library.vis.properties.descriptions.FigureProp;
import org.rascalmpl.library.vis.properties.descriptions.HandlerProp;
import org.rascalmpl.library.vis.properties.descriptions.IntProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;
import org.rascalmpl.values.ValueFactoryFactory;


/**
 * Figures are the foundation of Rascal visualization. They are based on a
 * bounding box + anchor model. The bounding box defines the maximal dimensions
 * of the element. The anchor defines its alignment properties.
 * 
 * Each figure has an associated property manager whose values can be accessed
 * via this class.
 * 
 * @author paulk
 */

public abstract class Figure implements Comparable<Figure>,IPropertyManager {

	public static final float AUTO_SIZE = -1.0f;
	
	@SuppressWarnings("unused")
	private final boolean debug = false;
	public IFigureApplet fpa;
	protected static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public PropertyManager properties;

	
	private float left;		// coordinates of top left corner of
	private float top;		// the element's bounding box
	public float width;			// width of element
	public float height;		// height of element
	                            // When this figure is used as mouseOver or inner figure, point back
	                            // to generating Figure
	protected float scaleX, scaleY;
	
	private boolean visibleInMouseOver = false;
	private float leftDragged;
	private float topDragged;
		
	protected Figure(IFigureApplet fpa, PropertyManager properties){
		this.fpa = fpa;
		this.properties = properties;
		String id = properties.getStringProperty(StrProp.ID);
		if(id != null)
			fpa.registerId(id, this);
		scaleX = scaleY = 1.0f;
	}
	
	protected void setLeft(float left) {
		this.left = left;
	}
	

	public float getLeft() {
		return left + getLeftDragged();
	}

	protected void setTop(float top) {
		this.top = top + getTopDragged();
	}

	public float getTop() {
		return top;
	}
	
	public float getCenterX(){
		return getLeft() + width/2;
	}
	
	public float getCenterY(){
		return getTop() + height/2;
	}

	public float max(float a, float b) {
		return a > b ? a : b;
	}

	public float min(float a, float b) {
		return a < b ? a : b;
	}

	public float abs(float a) {
		return a >= 0 ? a : -a;
	}

	public void applyProperties() {
		fpa.fill(getColorProperty(ColorProp.FILL_COLOR));
		fpa.stroke(getColorProperty(ColorProp.LINE_COLOR));
		fpa.strokeWeight(getRealProperty(RealProp.LINE_WIDTH));
		fpa.textSize(getIntegerProperty(IntProp.FONT_SIZE));
	}

	public void applyFontProperties() {
		fpa.textFont(fpa.createFont(getStringProperty(StrProp.FONT),
				getIntegerProperty(IntProp.FONT_SIZE)));
		fpa.textColor(getColorProperty(ColorProp.FONT_COLOR));
	}
	/*
	public float leftAlign() {
		float res= (getRealProperty(RealProp.HALIGN) * width);
		return res;
	}

	public float rightAlign() {
		float res =  (width - getRealProperty(RealProp.HALIGN) * width);
		return res;
	}

	public float topAlign() {
		return (getRealProperty(RealProp.VALIGN) * height);
	}

	public float bottomAlign() {
		return (height - getRealProperty(RealProp.VALIGN)  * height);
	}
	*/
	
	// Anchors

	public float leftAlign() {
		float res= (getRealProperty(RealProp.HALIGN) * width);
		return res;
	}

	public float rightAlign() {
		float res =  (width - getRealProperty(RealProp.HALIGN) * width);
		return res;
	}

	public float topAlign() {
		return (getRealProperty(RealProp.VALIGN) * height);
	}

	public float bottomAlign() {
		return (height - getRealProperty(RealProp.VALIGN) * height);
	}

	// TODO: irregular

	public boolean isVisible() {
		return true;
		// return fpa.isVisible(properties.getDOI());
	}

	public boolean isNextVisible() {
		return true;
		// return fpa.isVisible(properties.getDOI() + 1);
	}
	
	public void gatherProjections(float left, float top, Vector<Chart.Projection> projections){
		if(properties.isFigurePropertySet(FigureProp.PROJECTX)){
			projections.add(new Chart.Projection(left + properties.getRealProperty(RealProp.PROJECT_HALIGN) * width,
					 properties.getRealProperty(RealProp.PROJECTX_GAP),
					 properties.getFigureProperty(FigureProp.PROJECTX),true));
		} 
		if(properties.isFigurePropertySet(FigureProp.PROJECTY)){
			projections.add(new Chart.Projection(top +  properties.getRealProperty(RealProp.PROJECT_VALIGN) * height,
					properties.getRealProperty(RealProp.PROJECTY_GAP),
					properties.getFigureProperty(FigureProp.PROJECTY),false));
		}
	}


	/*
	 * Compare two Figures according to their surface and aspect ratio
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Figure o) {
		float r = (height > width) ? height / width : width / height;
		float or = (o.height > o.width) ? o.height / o.width : o.width
				/ o.height;

		if (r < 2f && or < 2f) {
			float s = height * width;
			float os = o.height * o.width;
			return s < os ? 1 : (s == os ? 0 : -1);
		}
		return r < or ? 1 : (r == or ? 0 : -1);
	}

	/**
	 * Drawing proceeds in two stages: - determine the bounding box of the
	 * element (using bbox) - draw it (using draw) with left and top argument
	 * for placement.
	 */

	/**
	 * Compute the bounding box of the element. Should be called before draw
	 * since, the computed width and height are stored in the element itself.
	 * @param desiredWidth TODO
	 * @param desiredHeight TODO
	 */

	public abstract void bbox(float desiredWidth, float desiredHeight);

	/**
	 * Draw element with explicitly left, top corner of its bounding box
	 * 
	 * @param left
	 *            x-coordinate of corner
	 * @param top
	 *            y-coordinate of corner
	 */

	public abstract void draw(float left, float top);
	
	/**
	 * Draw an arrow from an external position (fromX, fromY) directed to the center
	 * (X,Y) of the current figure. The arrow is placed at At the intersection with the border of the
	 * current figure and it is appropriately rotated.
	 * 
	 * @param left
	 *            X of left corner
	 * @param top
	 *            Y of left corner
	 * @param X
	 *            X of center of current figure
	 * @param Y
	 *            Y of center of current figure
	 * @param fromX
	 *            X of center of figure from which connection is to be drawn
	 * @param fromY
	 *            Y of center of figure from which connection is to be drawn
	 * @param toArrow
	 *            the figure to be used as arrow
	 */
	public void connectArrowFrom(float left, float top, float X, float Y,
			float fromX, float fromY, Figure toArrow) {
		if (fromX == X)
			fromX += 0.00001;
		float s = (fromY - Y) / (fromX - X);

		float theta = FigureApplet.atan(s);
		if (theta < 0) {
			if (fromX < X)
				theta += FigureApplet.PI;
		} else {
			if (fromX < X)
				theta += FigureApplet.PI;
		}
		float IX;
		float IY;

		float h2 = height / 2;
		float w2 = width / 2;

		if ((-h2 <= s * w2) && (s * w2 <= h2)) {
			if (fromX > X) { // right
				IX = X + w2;
				IY = Y + s * w2;
			} else { // left
				IX = X - w2;
				IY = Y - s * w2;
			}
		} else {
			if (fromY > Y) { // bottom
				IX = X + h2 / s;
				IY = Y + h2;
			} else { // top
				IX = X - h2 / s;
				IY = Y - h2;
			}
		}
/*
		//fpa.line(left + fromX, top + fromY, left + IX, top + IY);
*/
		if (toArrow != null) {
			toArrow.bbox(AUTO_SIZE, AUTO_SIZE);
			fpa.pushMatrix();
			fpa.translate(left + IX, top + IY);
			fpa.rotate(FigureApplet.radians(-90) + theta);
			toArrow.draw(-toArrow.width / 2, 0);
			fpa.popMatrix();
		}
	}

	/**
	 * Compute Y value for given X and line through (X1,Y1) and given slope
	 * 
	 * @param slope
	 * @param X1
	 * @param Y1
	 * @param X
	 * @return Y value
	 */
	private float yLine(float slope, float X1, float Y1, float X) {
		return slope * (X - X1) + Y1;
	}

	/**
	 * Compute X value for given Y and line through (X1,Y1) and given slope
	 * 
	 * @param slope
	 * @param X1
	 * @param Y1
	 * @param Y
	 * @return X value
	 */
	private float xLine(float slope, float X1, float Y1, float Y) {
		return X1 + (Y - Y1) / slope;
	}

	/**
	 * Intersects line (fromX,fromY) to (toX,toY) with this figure when placed
	 * at (X,Y)?
	 * 
	 * @param X
	 * @param Y
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 * @return true when line and figure intersect
	 */
	public boolean intersects(float X, float Y, float fromX, float fromY,
			float toX, float toY) {
		float s = (fromY - toY) / (fromX - toX);
		float h2 = height / 2;
		float w2 = width / 2;

		float ly = yLine(s, fromX, fromY, X - w2);
		if (ly > Y - h2 && ly < Y + h2)
			return true;

		float ry = yLine(s, fromX, fromY, X + w2);
		if (ry > Y - h2 && ry < Y + h2)
			return true;

		float tx = xLine(s, fromX, fromY, Y - h2);
		if (tx > X - w2 && tx < X + w2)
			return true;

		float bx = xLine(s, fromX, fromY, Y + h2);
		if (bx > X - w2 && tx < X + w2)
			return true;
		return false;
	}

	/**
	 * Draw focus around this figure
	 */
	public void drawFocus() {
		if (isVisible()) {
			fpa.stroke(255, 0, 0);
			fpa.strokeWeight(1);
			fpa.noFill();
			fpa.rect(getLeft(), getTop(), width, height);
		}
	}

	public boolean isVisibleInMouseOver() {
		return visibleInMouseOver;
	}

	public void setVisibleInMouseOver(boolean b) {
		visibleInMouseOver = b;
		// System.err.println("setVisibleInMouseOver(" + b + ")" + " : " +
		// this);
	}

	public void clearVisibleInMouseOver() {
		visibleInMouseOver = false;
	}

	/**
	 * Draw the mouseOver figure associated with this figure (if any)
	 * 
	 * @param left
	 *            left corner op enclosing figure
	 * @param top
	 *            top corner of enclosing figure
	 */
	public void drawWithMouseOver(float left, float top) {
		draw(left, top);
		if (hasMouseOverFigure()) {
			Figure mo = getMouseOver();
			mo.bbox(AUTO_SIZE, AUTO_SIZE);
			mo.drawWithMouseOver(max(0, left + (width - mo.width) / 2f),
					max(0, top + (height - mo.height) / 2));
		}
	}


	public boolean mouseInside(int mouseX, int mouseY){
		boolean b =  (mouseX > getLeft()  && mouseX < getLeft() + width) &&
		             (mouseY > getTop()  && mouseY < getTop() + height);
		//System.err.println("mouseInside1: [" + mouseX + ", " + mouseY + "]: "+ b + "; " + this);
		//System.err.printf("left %f right %f top %f bottom %f\n", getLeft(),getTop(),getLeft() + width, getTop() + height);
		return b;
	}

	public boolean mouseInside(int mouseX, int mouseY, float centerX,
			float centerY) {
		float left = max(0, centerX - width / 2);
		float top = max(0, centerY - height / 2);
		boolean b = (mouseX > left && mouseX < left + width)
				&& (mouseY > top && mouseY < top + height);

		  //System.err.println("mouseInside2: [" + mouseX + ", " + mouseY +
		 // "]: "+ b + "; " + this);
		return b;
	}

	/**
	 * Compute effect of a mouseOver on this element (with not yet known
	 * position)
	 * 
	 * @param mouseX
	 *            x-coordinate of mouse
	 * @param mouseY
	 *            y-coordinate of mouse
	 * @param centerX
	 *            center of parent
	 * @param centerY
	 *            center of parent
	 * @param mouseInParent
	 *            true if mouse inside parent
	 * @return true if element was affected.
	 */
	
	public boolean mouseOver(int mouseX, int mouseY, float centerX, float centerY, boolean mouseInParent){
		// System.err.println("Figure.MouseOver: " + this);
		if(mouseInside(mouseX, mouseY, centerX, centerY)){
		   fpa.registerMouseOver(this);
		   return true;
		}
		return false;
	}

	/**
	 * Compute effect of a mouseOver on this element (with known position)
	 * 
	 * @param mouseX
	 *            x-coordinate of mouse
	 * @param mouseY
	 *            y-coordinate of mouse
	 * @param mouseInParent
	 *            true if mouse inside parent
	 * @return true if element was affected.
	 */

	public boolean mouseOver(int mouseX, int mouseY, boolean mouseInParent){
		return mouseOver(mouseX, mouseY, getCenterX(), getCenterY(), mouseInParent);
	}

	final Type[] argTypes = new Type[0];			// Argument types of callback: list[str]
	final IValue[] argVals = new IValue[0];		// Argument values of callback: argList
	
	/**
	 * Compute the effect
	 * 
	 * @param mouseX
	 *            x-coordinate of mouse
	 * @param mouseY
	 *            y-coordinate of mouse
	 * @param e
	 *            TODO
	 * @return
	 */

	public boolean mousePressed(int mouseX, int mouseY, Object e){
		System.err.println("Figure.mousePressed in " + this + ", handler = " + properties.getOnClick());
		if(mouseInside(mouseX, mouseY)){
			
			if(properties.handlerCanBeExecuted(HandlerProp.MOUSE_CLICK)){
				properties.executeHandlerProperty(HandlerProp.MOUSE_CLICK);
			} else {
				fpa.registerFocus(this);
			}
			return true;
		}
		return false;
	}

	public boolean mouseReleased() {
		return false;
	}

	/**
	 * @param key  
	 * @param keyCode 
	 */
	public boolean keyPressed(int key, int keyCode) {
		return false;
	}
	
	public void drag(float mousex, float mousey){
		System.err.println("Drag to " + mousex + ", " + mousey + ": " + this);
		if(!isDraggable())
			System.err.println("==== ERROR: DRAG NOT ALLOWED ON " + this + " ===");
		setLeftDragged(getLeftDragged() + (mousex - getLeft()));
		setTopDragged(getTopDragged() + (mousey - getTop()));
	}

	public boolean mouseDragged(int mousex, int mousey){
		if(isDraggable() && mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
			drag(mousex, mousey);
			System.err.printf("Figure.mouseDragged: %f,%f\n", getLeftDragged(), getTopDragged());
			return true;
		}
		return false;
	}
	
	public void propagateScaling(float scaleX,float scaleY){
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	

	public void setLeftDragged(float leftDragged) {
		this.leftDragged = leftDragged;
	}

	public float getLeftDragged() {
		return leftDragged;
	}

	public void setTopDragged(float topDragged) {
		this.topDragged = topDragged;
	}

	public float getTopDragged() {
		return topDragged;
	}
	
	/**
	 * Give a figure the opportunity to remove allocated components, etc.
	 */
	public void destroy(){
	}
	
	// IPropertyManager implementation (boilerplate)
	public boolean isBooleanPropertySet(BoolProp property){
		return properties.isBooleanPropertySet(property);
	}
	public boolean getBooleanProperty(BoolProp property){
		return properties.getBooleanProperty(property);
	}
	public boolean isIntegerPropertySet(IntProp property){
		return properties.isIntegerPropertySet(property);
	}
	public int getIntegerProperty(IntProp property) {
		return properties.getIntegerProperty(property);
	}
	public boolean isRealPropertySet(RealProp property){
		return properties.isRealPropertySet(property);
	}
	public float getRealProperty(RealProp property){
		return properties.getRealProperty(property);
	}
	public boolean isStringPropertySet(StrProp property){
		return properties.isStringPropertySet(property);
	}
	public String getStringProperty(StrProp property){
		return properties.getStringProperty(property);
	}
	public boolean isColorPropertySet(ColorProp property){
		return properties.isColorPropertySet(property);
	}
	public int getColorProperty(ColorProp property) {
		return properties.getColorProperty(property);
	}
	
	public boolean isDraggable(){
		return properties.isDraggable();
	}
	public Figure getMouseOver(){
		return properties.getMouseOver();
	}
	
	private boolean hasMouseOverFigure() {
		return getMouseOver() != null;
	}
	
	// short-hand functions for selected properties(boilerplate)
	public boolean getClosedProperty(){ return getBooleanProperty(BoolProp.SHAPE_CLOSED);}
	public boolean getCurvedProperty(){ return getBooleanProperty(BoolProp.SHAPE_CURVED);}
	public boolean getConnectedProperty(){ return getBooleanProperty(BoolProp.SHAPE_CONNECTED);}
	public String getIdProperty(){return getStringProperty(StrProp.ID);}
	public String getDirectionProperty(){return getStringProperty(StrProp.DIRECTION);}
	public String getLayerProperty(){return getStringProperty(StrProp.LAYER);}
	public boolean isWidthPropertySet(){return isRealPropertySet(RealProp.WIDTH);}
	public boolean isHeightPropertySet(){return isRealPropertySet(RealProp.HEIGHT);}
	public boolean isHGapPropertySet(){return isRealPropertySet(RealProp.HGAP);}
	
	public boolean isVGapPropertySet(){return isRealPropertySet(RealProp.VGAP);}
	// below are convience functions for measures, which are scaled (text and linewidth are not scaled)
	public float getWidthProperty(){ return getRealProperty(RealProp.WIDTH) * scaleX;}
	public float getHeightProperty(){return getRealProperty(RealProp.HEIGHT) * scaleY;}
	public float getHGapProperty(){return getRealProperty(RealProp.HGAP) * scaleX;}
	public float getVGapProperty(){return getRealProperty(RealProp.VGAP) * scaleY;}
	// TODO: how to scale wedges!
	public float getInnerRadiusProperty(){return getRealProperty(RealProp.INNERRADIUS) * max(scaleX,scaleY);}
	public float getProjectXGapProperty(){return getRealProperty(RealProp.PROJECTX_GAP) * scaleX;}
	public float getProjectYGapProperty(){return getRealProperty(RealProp.PROJECTY_GAP) * scaleY;}
	
	public boolean isHGapFactorPropertySet(){return isRealPropertySet(RealProp.HGAP_FACTOR);}
	public float getHGapFactorProperty() { return getRealProperty(RealProp.HGAP_FACTOR);}
	public boolean isVGapFactorPropertySet(){return isRealPropertySet(RealProp.VGAP_FACTOR);}
	public float getVGapFactorProperty() { return getRealProperty(RealProp.VGAP_FACTOR);}
	public float getHAlignProperty(){return getRealProperty(RealProp.HALIGN);}
	public float getVAlignProperty(){return getRealProperty(RealProp.VALIGN);}
	public float getLineWidthProperty(){return getRealProperty(RealProp.LINE_WIDTH);}
	public float getTextAngleProperty(){return getRealProperty(RealProp.TEXT_ANGLE);}
	public float getFromAngleProperty(){return getRealProperty(RealProp.FROM_ANGLE);}
	public float getToAngleProperty(){return getRealProperty(RealProp.TO_ANGLE);}
	public int getFillColorProperty(){return getColorProperty(ColorProp.FILL_COLOR);}
	public int getFontColorProperty(){return getColorProperty(ColorProp.FONT_COLOR);}
	public boolean getStartGapProperty(){ return getBooleanProperty(BoolProp.START_GAP);}
	public boolean getEndGapProperty(){ return getBooleanProperty(BoolProp.END_GAP);}
	public boolean widthExplicitlySet(){return isRealPropertySet(RealProp.WIDTH);}
	public boolean heightExplicitlySet(){return isRealPropertySet(RealProp.HEIGHT);}
	
	
}
