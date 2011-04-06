/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis;

import java.awt.event.MouseEvent;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;

import processing.core.PApplet;
import processing.core.PConstants;

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

public abstract class Figure implements Comparable<Figure> {

	private final boolean debug = false;
	public FigurePApplet fpa;
	protected static IValueFactory vf = ValueFactoryFactory.getValueFactory();

	public IPropertyManager properties;

	
	private float left;		// coordinates of top left corner of
	private float top;		// the element's bounding box
	public float width;			// width of element
	public float height;		// height of element
	                            // When this figure is used as mouseOver or inner figure, point back
	                            // to generating Figure
	
	private boolean visibleInMouseOver = false;
	private float leftDragged;
	private float topDragged;
		
	protected Figure(FigurePApplet fpa, IPropertyManager properties){
		this.fpa = fpa;
		this.properties = properties;
		String id = properties.getId();
		if(id != null)
			fpa.registerId(id, this);
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

	public int getDOI(){
		return properties.getDOI();
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

	private IPropertyManager getProperties() {
		return properties;
	}

	public void applyProperties() {
		IPropertyManager pm = getProperties();

		fpa.fill(pm.getFillColor());
		fpa.stroke(pm.getLineColor());
		fpa.strokeWeight(pm.getLineWidth());
		fpa.textSize(pm.getFontSize());
	}

	public void applyFontProperties() {
		fpa.textFont(fpa.createFont(properties.getFont(),
				properties.getFontSize()));
		fpa.fill(properties.getFontColor());
	}
	
	protected boolean getAlignAnchorsProperty(){
		return properties.getAlignAnchors();
	}

	protected float getHeightProperty() {
		return properties.getHeight();
	}

	protected float getWidthProperty() {
		return properties.getWidth();
	}

	protected float getHGapProperty() {
		return properties.getHGap();
	}

	protected float getVGapProperty() {
		return properties.getVGap();
	}

	protected int getFillColorProperty() {
		return properties.getFillColor();
	}

	protected int getLineColorProperty() {
		return properties.getLineColor();
	}

	protected float getLineWidthProperty() {
		return properties.getLineWidth();
	}
	
	// Alignment
	
	public float getHalignProperty() {
		return properties.getHalign();
	}

	public float getValignProperty() {
		return properties.getValign();
	}
	
	public float leftAlign() {
		float res= (properties.getHalign() * width);
		return res;
	}

	public float rightAlign() {
		float res =  (width - properties.getHalign() * width);
		return res;
	}

	public float topAlign() {
		return (properties.getValign() * height);
	}

	public float bottomAlign() {
		return (height - properties.getValign() * height);
	}
	
	// Anchors

	public float getHanchorProperty() {
		return properties.getHanchor();
	}

	public float getVanchorProperty() {
		return properties.getVanchor();
	}

	public float leftAnchor() {
		float res= (properties.getHanchor() * width);
		return res;
	}

	public float rightAnchor() {
		float res =  (width - properties.getHanchor() * width);
		return res;
	}

	public float topAnchor() {
		return (properties.getVanchor() * height);
	}

	public float bottomAnchor() {
		return (height - properties.getVanchor() * height);
	}

	public boolean isClosed() {
		return properties.isShapeClosed();
	}

	protected boolean isConnected() {
		return properties.isShapeConnected();
	}

	protected boolean isCurved() {
		return properties.isShapeCurved();
	}

	protected float getFromAngleProperty() {
		return properties.getFromAngle();
	}

	protected float getToAngleProperty() {
		return properties.getToAngle();
	}

	protected float getInnerRadiusProperty() {
		return properties.getInnerRadius();
	}

	// TODO: irregular
	protected boolean getHint(String txt) {
		return properties.getHint().contains(txt);
	}

	public String getIdProperty() {
		return properties.getId();
	}

	protected String getFontProperty() {
		return properties.getFont();
	}

	protected int getFontSizeProperty() {
		return properties.getFontSize();
	}

	protected int getFontColorProperty() {
		return properties.getFontColor();
	}

	protected float getTextAngleProperty() {
		return properties.getTextAngle();
	}

	public boolean isVisible() {
		return true;
		// return fpa.isVisible(properties.getDOI());
	}

	public boolean isNextVisible() {
		return true;
		// return fpa.isVisible(properties.getDOI() + 1);
	}

	public Figure getMouseOverFigure() {
		return properties.getMouseOver();
	}

	public boolean hasMouseOverFigure() {
		return properties.getMouseOver() != null;
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
	 */

	public abstract void bbox();

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
	 * Draw a connection from an external position (fromX, fromY) to the center
	 * (X,Y) of the current figure. At the intersection with the border of the
	 * current figure, place an arrow that is appropriately rotated.
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
	public void connectFrom(float left, float top, float X, float Y,
			float fromX, float fromY, Figure toArrow) {
		if (fromX == X)
			fromX += 0.00001;
		float s = (fromY - Y) / (fromX - X);

		float theta = PApplet.atan(s);
		if (theta < 0) {
			if (fromX < X)
				theta += PConstants.PI;
		} else {
			if (fromX < X)
				theta += PConstants.PI;
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

		fpa.line(left + fromX, top + fromY, left + IX, top + IY);

		if (toArrow != null) {
			toArrow.bbox();
			fpa.pushMatrix();
			fpa.translate(left + IX, top + IY);
			fpa.rotate(PApplet.radians(-90) + theta);
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
		// draw(left, top);
		if (hasMouseOverFigure()) {
			Figure mo = getMouseOverFigure();
			mo.bbox();
			mo.drawWithMouseOver(max(0, left + (width - mo.width) / 2f),
					max(0, top + (height - mo.height) / 2));
		}
	}
	
	public boolean mouseInside(int mouseX, int mouseY){
		boolean b =  (mouseX > getLeft()  && mouseX < getLeft() + width) &&
		             (mouseY > getTop()  && mouseY < getTop() + height);
		System.err.println("mouseInside1: [" + mouseX + ", " + mouseY + "]: "+ b + "; " + this);
		return b;
	}

	public boolean mouseInside(int mouseX, int mouseY, float centerX,
			float centerY) {
		float left = max(0, centerX - width / 2);
		float top = max(0, centerY - height / 2);
		boolean b = (mouseX > left && mouseX < left + width)
				&& (mouseY > top && mouseY < top + height);

		 System.err.println("mouseInside2: [" + mouseX + ", " + mouseY +
		 "]: "+ b + "; " + this);
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
		System.err.println("Figure.MouseOver: " + this);
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

	public boolean mousePressed(int mouseX, int mouseY, MouseEvent e){
		System.err.println("Figure.mousePressed in " + this + ", handler = " + properties.getOnClick());
		if(mouseInside(mouseX, mouseY)){
			IValue handler = properties.getOnClick();
			if(handler != null){
				synchronized(fpa){
					if(handler instanceof RascalFunction)
						((RascalFunction) handler).call(argTypes, argVals);
					else
						((OverloadedFunctionResult) handler).call(argTypes, argVals);
				}
				fpa.setComputedValueChanged();
			} else
				fpa.registerFocus(this);
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

	public boolean isDraggable() {
		return properties.isDraggable();
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
	
}
