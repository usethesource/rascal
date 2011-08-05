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
package org.rascalmpl.library.vis.figure;


import java.util.Vector;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.swt.SWT;
import org.rascalmpl.library.vis.figure.compose.Overlay;
import org.rascalmpl.library.vis.figure.keys.HAxis;
import org.rascalmpl.library.vis.graphics.FontStyle;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.SWTFontsAndColors;
import org.rascalmpl.library.vis.swt.zorder.ISWTZOrdering;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.Rectangle;

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

	public static final boolean[] BOTH_DIMENSIONS = { false ,true };
	public static final double AUTO_SIZE = -1.0;
	public static int sequencer = 0; // to impose arbitrary ordering on figures
	public int sequenceNr;
	@SuppressWarnings("unused")
	private static final boolean debug = false;


	public PropertyManager properties;
	public BoundingBox minSize;
	public BoundingBox size;
	public Coordinate globalLocation; // TODO: set this everywere
	public boolean resizableX, resizableY;

	protected Figure () {
		// TODO: needed for overlay.. remove this
	}
	
	public Figure(PropertyManager properties) {
		this.properties = properties;
		minSize = new BoundingBox();
		size = new BoundingBox();
		globalLocation = new Coordinate();
		sequenceNr = sequencer;
		sequencer++;
	}

	public void init(){}
	
	public void registerNames(NameResolver resolver) {
		resolver.register(this);
	}
	
	public void registerValues(NameResolver resolver){
		properties.registerMeasures(resolver);
	}
	
	public void getLikes(NameResolver resolver){
		properties.getLikes(resolver);
	}


	public void computeFiguresAndProperties(ICallbackEnv env) {
		properties.computeProperties(env);
	}
	
	public void finalize(){}

	public double getLeft() {
		return globalLocation.getX();
	}


	public double getTop() {
		return globalLocation.getY();
	}

	public double getCenterX() {
		return getLeft() + minSize.getWidth() / 2;
	}

	public double getCenterY() {
		return getTop() + minSize.getHeight() / 2;
	}
	
	public void setToMinSize(){
		size.set(minSize);
	}

	
	public void applyProperties(GraphicsContext gc) {
		gc.fill(getColorProperty(Properties.FILL_COLOR));
		gc.stroke(getColorProperty(Properties.LINE_COLOR));
		gc.strokeWeight(getRealProperty(Properties.LINE_WIDTH));
		gc.strokeStyle(getLineStyleProperty());
		gc.textSize(getIntegerProperty(Properties.FONT_SIZE));
		
		boolean shadow = getBooleanProperty(Properties.SHADOW);
		gc.setShadow(shadow);
		if (shadow) {
			gc.setShadowColor(getColorProperty(Properties.SHADOW_COLOR));
			gc.setShadowLeft(getRealProperty(Properties.SHADOWLEFT));
			gc.setShadowTop(getRealProperty(Properties.SHADOWTOP));
		}
		gc.setFont(getStringProperty(Properties.FONT), getIntegerProperty(Properties.FONT_SIZE), FontStyle.NORMAL);
		gc.font(properties.getColorProperty(Properties.FONT_COLOR));
	}

	/*
	 * Compare two Figures using an arbitrary ordering
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Figure o) {
		return sequenceNr - o.sequenceNr;
	}
	/**
	 * Drawing proceeds in two stages: - determine the bounding box of the
	 * element (using bbox) - draw it (using draw) with left and top argument
	 * for placement.
	 */

	/**
	 * Compute the bounding box of the element. Should be called before draw
	 * since, the computed width and height are stored in the element itself.
	 * @param env TODO
	 * 
	 */

	// top down compute minimum size
	public void bbox(){
		if(!properties.isConverted(Properties.WIDTH)){
			minSize.setWidth(Math.max(minSize.getWidth(),getWidthProperty()));
		} else {
			minSize.setWidth(getWidthProperty());
		}
		if(!properties.isConverted(Properties.HEIGHT)){
			minSize.setHeight(Math.max(minSize.getHeight(),getHeightProperty()));
		} else {
			minSize.setHeight(getHeightProperty());
		}
		for(boolean flip : BOTH_DIMENSIONS){
			setResizableX(flip, getResizableX(flip) && getHResizableProperty(flip));
			if(!getResizableX(flip)){
				size.setWidth(flip,minSize.getWidth(flip));
			}
		}
	}
	
	public BoundingBox getMinViewingSize() {
		BoundingBox minViewSize = new BoundingBox();
		minViewSize.set(minSize.getWidth() / getHGrowProperty(), minSize.getHeight() / getVGrowProperty() );
		return minViewSize;
	}
	
	// distribute actual available size, using size as the available size
	public abstract void layout();
	
	public void setLocationOfChildren() {} 
	
	
	public void setSWTZOrder(ISWTZOrdering zorder){}


	/**
	 * Draw element with explicitly left, top corner of its bounding box
	 * @param gc
	 */

	public abstract void draw(GraphicsContext gc);
	
	public void drawPart(Rectangle r,GraphicsContext gc){
			draw(gc);
	}
	
	public boolean isVisible(){
		return true;
	}
	
	public boolean overlapsWith(Rectangle r){
		return r.overlapWith(globalLocation, size);
	}
	
	public boolean isContainedIn(Rectangle r){
		return r.contains(globalLocation,size);
	}
	
	public Rectangle getRectangle(){
		return new Rectangle(globalLocation, size);
	}
	
	public Rectangle getRectangleIncludingOuterLines(){
		double hlw = 0.5 * getLineWidthProperty();
		return new Rectangle(globalLocation.getX() - hlw, globalLocation.getY() -hlw , size.getWidth() + 2*hlw, size.getHeight() + 2*hlw);
	}
	
	public double getX(){
		return globalLocation.getX();
	}
	
	public double getY(){
		return globalLocation.getY();
	}
	
	public double getX(boolean flip){
		if(flip) return getY();
		else return getX();
	}
	
	public double getWidth(){
		return size.getWidth();
	}
	
	public double getHeight(){
		return size.getHeight();
	}
	
	public double getWidth(boolean flip){
		if(flip) return getHeight();
		else return getWidth();
	}
	
	public double getXRight(){
		return getX() + getWidth();
	}
	
	public double getYDown(){
		return getY() + getHeight();
	}
	
	public double getXRight(boolean flip){
		if(flip) return getYDown();
		else return getXRight();
	}

	/**
	 * Draw an arrow from an external position (fromX, fromY) directed to the
	 * center (X,Y) of the current figure. The arrow is placed at At the
	 * intersection with the border of the current figure and it is
	 * appropriately rotated.
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
	public void connectArrowFrom(double left, double top, double X, double Y,
			double fromX, double fromY, Figure toArrow, GraphicsContext gc) {
		if (fromX == X)
			fromX += 0.00001;
		double s = (fromY - Y) / (fromX - X);

		double theta = Math.atan(s);
		if (theta < 0) {
			if (fromX < X)
				theta += FigureMath.PI;
		} else {
			if (fromX < X)
				theta += FigureMath.PI;
		}
		double IX;
		double IY;

		double h2 = minSize.getHeight() / 2;
		double w2 = minSize.getWidth() / 2;

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
		 * //fpa.line(left + fromX, top + fromY, left + IX, top + IY);
		 */
		if (toArrow != null) {
			gc.pushMatrix();
			gc.translate(left + IX, top + IY);
			gc.rotate(FigureMath.radians(-90) + theta);
			toArrow.draw(gc);
			gc.popMatrix();
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
	private double yLine(double slope, double X1, double Y1, double X) {
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
	private double xLine(double slope, double X1, double Y1, double Y) {
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
	public boolean intersects(double X, double Y, double fromX, double fromY,
			double toX, double toY) {
		double s = (fromY - toY) / (fromX - toX);
		double h2 = minSize.getHeight() / 2;
		double w2 = minSize.getWidth() / 2;

		double ly = yLine(s, fromX, fromY, X - w2);
		if (ly > Y - h2 && ly < Y + h2)
			return true;

		double ry = yLine(s, fromX, fromY, X + w2);
		if (ry > Y - h2 && ry < Y + h2)
			return true;

		double tx = xLine(s, fromX, fromY, Y - h2);
		if (tx > X - w2 && tx < X + w2)
			return true;

		double bx = xLine(s, fromX, fromY, Y + h2);
		if (bx > X - w2 && tx < X + w2)
			return true;
		return false;
	}

	public boolean getFiguresUnderMouse(Coordinate c, Vector<Figure> result) {
		if (!mouseInside(c.getX(), c.getY()))
			return false;
		result.add(this);
		return true;
	}
	
	
	public boolean executeKeyHandlers(ICallbackEnv env,IValue keySym, IBool keyDown, IMap modifiers){
		Type[] types = {keySym.getType(),keyDown.getType(),modifiers.getType()};
		IValue[] args = {keySym,keyDown,modifiers};
		if(isHandlerPropertySet(Properties.ON_KEY)){
			return ((IBool)properties.executeHandlerProperty(env, Properties.ON_KEY, types, args)).getValue();
		}
		return false;
	}

	public boolean executeMouseMoveHandlers(ICallbackEnv env, IBool enter, Properties prop) {
		Type[] types = {enter.getType()};
		IValue[] args = {enter};
		if(isHandlerPropertySet(Properties.ON_KEY)){
			return ((IBool)properties.executeHandlerProperty(env, Properties.ON_MOUSEMOVE, types, args)).getValue();
		}
		return false;
	}


	public boolean mouseInside(double mouseX, double mouseY) {
		//System.out.printf("mouse over %s %f %f %f %f %f %f\n", this, mouseX, getLeft(),mouseY,getTop(),size.getWidth(), size.getHeight());
		return (mouseX >= getLeft() && mouseX <= getLeft() + size.getWidth())
				&& (mouseY >= getTop() && mouseY <= getTop() + size.getHeight());
	}


	/**
	 * Give a figure the opportunity to remove allocated components, etc.
	 */
	public void destroy() {
	}
	
	public boolean getResizableX(boolean flip){
		if(flip) return resizableY;
		else return resizableX;
	}
	
	public void setResizableX(boolean flip, boolean resizable){
		if(flip) resizableY = resizable;
		else resizableX = resizable;
	}
	
	public void setNonResizable(){
		resizableX = resizableY = false;
	}
	
	public void setResizable(){
		resizableX = resizableY = true;
	}

	public void takeDesiredWidth(double width){
		if(properties.isConverted(Properties.WIDTH)){
			size.setWidth(properties.getRealProperty(Properties.WIDTH));
			properties.getKey(Properties.WIDTH).registerOffset(globalLocation.getX());
			properties.getKey(Properties.WIDTH).registerOffset(globalLocation.getX()+size.getWidth());
		} else if(resizableX){
			size.setWidth(width);
		} else {
			size.setWidth(minSize.getWidth());
		}
	}
	
	public void takeDesiredHeight(double height){
		
		if(properties.isConverted(Properties.HEIGHT)){
			size.setHeight(properties.getRealProperty(Properties.HEIGHT));
			properties.getKey(Properties.HEIGHT).registerOffset(globalLocation.getY());
			properties.getKey(Properties.HEIGHT).registerOffset(globalLocation.getY()+size.getHeight());
		} else if(resizableY){
			size.setHeight(height);
		}
		else{
			size.setHeight(minSize.getHeight());
		}
	}
	
	public void takeDesiredHeight(boolean flip,double height){
		if(flip) takeDesiredWidth(height);
		else takeDesiredHeight(height);
	}
	
	public void takeDesiredWidth(boolean flip, double width){
		if(flip) takeDesiredHeight(width);
		else takeDesiredWidth(width);
	}
	
	// IPropertyManager implementation (boilerplate)
	public boolean isBooleanPropertySet(Properties property) {
		return properties.isBooleanPropertySet(property);
	}

	public boolean getBooleanProperty(Properties property) {
		return properties.getBooleanProperty(property);
	}

	public boolean isIntegerPropertySet(Properties property) {
		return properties.isIntegerPropertySet(property);
	}

	public int getIntegerProperty(Properties property) {
		return properties.getIntegerProperty(property);
	}

	public boolean isRealPropertySet(Properties property) {
		return properties.isRealPropertySet(property);
	}

	public double getRealProperty(Properties property) {
		return properties.getRealProperty(property);
	}

	public boolean isStringPropertySet(Properties property) {
		return properties.isStringPropertySet(property);
	}

	public String getStringProperty(Properties property) {
		return properties.getStringProperty(property);
	}

	public boolean isColorPropertySet(Properties property) {
		return properties.isColorPropertySet(property);
	}

	public int getColorProperty(Properties property) {
		return properties.getColorProperty(property);
	}

	public boolean isHandlerPropertySet(Properties property) {
		return properties.isHandlerPropertySet(property);
	}

	public boolean isStandardHandlerPropertySet(Properties property) {
		return isStandardHandlerPropertySet(property);
	}

	public boolean isStandardDefaultHandlerPropertySet(Properties property) {
		return isStandardDefaultHandlerPropertySet(property);
	}

	public void executeHandlerProperty(ICallbackEnv env,Properties property) {
		properties.executeHandlerProperty(env,property);
	}

	public boolean isFigurePropertySet(Properties property) {
		return properties.isFigurePropertySet(property);
	}

	public Figure getFigureProperty(Properties property) {
		return properties.getFigureProperty(property);
	}

	public Figure getToArrow() {
		return getFigureProperty(Properties.TO_ARROW);
	}

	public Figure getFromArrow() {
		return getFigureProperty(Properties.FROM_ARROW);
	}

	public Figure getLabel() {
		return getFigureProperty(Properties.LABEL);
	}
	// Anchors

	public double leftAlign() {
		double res = (getRealProperty(Properties.HALIGN) * minSize.getWidth());
		return res;
	}

	public double rightAlign() {
		double res = (minSize.getWidth() - getRealProperty(Properties.HALIGN) * minSize.getWidth());
		return res;
	}

	public double topAlign() {
		return (getRealProperty(Properties.VALIGN) * minSize.getHeight());
	}

	public double bottomAlign() {
		return (minSize.getHeight() - getRealProperty(Properties.VALIGN) * minSize.getHeight());
	}

	public double leftAlign(boolean flip) {
		if (flip) {
			return bottomAlign();
		} else {
			return leftAlign();
		}
	}

	public double rightAlign(boolean flip) {
		if (flip) {
			return topAlign();
		} else {
			return rightAlign();
		}
	}

	public double topAlign(boolean flip) {
		if (flip) {
			return rightAlign();
		} else {
			return topAlign();
		}
	}

	public double bottomAlign(boolean flip) {
		if (flip) {
			return leftAlign();
		} else {
			return bottomAlign();
		}
	}

	// short-hand functions for selected properties(boilerplate)
	public boolean getClosedProperty() {
		return getBooleanProperty(Properties.SHAPE_CLOSED);
	}

	public boolean getCurvedProperty() {
		return getBooleanProperty(Properties.SHAPE_CURVED);
	}

	public boolean getConnectedProperty() {
		return getBooleanProperty(Properties.SHAPE_CONNECTED);
	}

	public String getIdProperty() {
		return getStringProperty(Properties.ID);
	}

	public String getDirectionProperty() {
		return getStringProperty(Properties.DIRECTION);
	}

	public String getLayerProperty() {
		return getStringProperty(Properties.LAYER);
	}

	public boolean isHeightPropertySet() {
		return isRealPropertySet(Properties.HEIGHT);
	}

	public boolean isHGapPropertySet() {
		return isRealPropertySet(Properties.HGAP);
	}

	public boolean isVGapPropertySet() {
		return isRealPropertySet(Properties.VGAP);
	}

	// below are convience functions for measures, which are scaled (text and
	// linewidth are not scaled)
	public boolean isWidthPropertySet() {
		return isRealPropertySet(Properties.WIDTH);
	}

	public double getWidthProperty() {
		return getRealProperty(Properties.WIDTH);
	}

	public double getHeightProperty() {
		return getRealProperty(Properties.HEIGHT);
	}

	public double getHGapProperty() {
		return getRealProperty(Properties.HGAP);
	}

	public double getVGapProperty() {
		return getRealProperty(Properties.VGAP);
	}

	// TODO: how to scale wedges!
	public double getInnerRadiusProperty() {
		return getRealProperty(Properties.INNERRADIUS);
	}


	public double getHAlignProperty() {
		if(properties.isConverted(Properties.WIDTH) && properties.getKey(Properties.WIDTH) instanceof HAxis){
			return 0.0;
		}
		return getRealProperty(Properties.HALIGN);
	}

	public double getVAlignProperty() {
		if(properties.isConverted(Properties.HEIGHT) && properties.getKey(Properties.HEIGHT) instanceof HAxis){
			return 1.0;
		}
		if(properties.isConverted(Properties.HEIGHT) && properties.getKey(Properties.HEIGHT) instanceof Overlay.LocalOffsetKey ){
			return 0.0;
		}
		return getRealProperty(Properties.VALIGN);
	}

	public double getLineWidthProperty() {
		return getRealProperty(Properties.LINE_WIDTH);
	}
	
	 public int getLineStyleProperty() {
         String s =  getStringProperty(Properties.LINE_STYLE);
         if (s.equals("dash")) return SWT.LINE_DASH;
         if (s.equals("dot")) return SWT.LINE_DOT;
         if (s.equals("dashdot")) return SWT.LINE_DASHDOT;
         if (s.equals("dashdotdot")) return SWT.LINE_DASHDOTDOT;
         return SWT.LINE_SOLID;
     }



	public double getTextAngleProperty() {
		return getRealProperty(Properties.TEXT_ANGLE);
	}

	public double getFromAngleProperty() {
		return getRealProperty(Properties.FROM_ANGLE);
	}

	public double getToAngleProperty() {
		return getRealProperty(Properties.TO_ANGLE);
	}

	public int getFillColorProperty() {
		return getColorProperty(Properties.FILL_COLOR);
	}

	public int getFontColorProperty() {
		return getColorProperty(Properties.FONT_COLOR);
	}

	public boolean isWidthPropertySet(boolean flip) {
		if (flip)
			return isRealPropertySet(Properties.HEIGHT);
		else
			return isRealPropertySet(Properties.WIDTH);
	}

	public double getWidthProperty(boolean flip) {
		if (flip)
			return getRealProperty(Properties.HEIGHT);
		else
			return getRealProperty(Properties.WIDTH);
	}

	public boolean isHeightPropertySet(boolean flip) {
		if (flip)
			return isRealPropertySet(Properties.WIDTH);
		else
			return isRealPropertySet(Properties.HEIGHT);
	}

	public double getHeightProperty(boolean flip) {
		if (flip)
			return getRealProperty(Properties.WIDTH);
		else
			return getRealProperty(Properties.HEIGHT);
	}

	public boolean isHGapPropertySet(boolean flip) {
		if (flip)
			return isRealPropertySet(Properties.VGAP);
		else
			return isRealPropertySet(Properties.HGAP);
	}

	public double getHGapProperty(boolean flip) {
		if (flip)
			return getRealProperty(Properties.VGAP);
		else
			return getRealProperty(Properties.HGAP);
	}

	public boolean isVGapPropertySet(boolean flip) {
		if (flip)
			return isRealPropertySet(Properties.HGAP);
		else
			return isRealPropertySet(Properties.VGAP);
	}

	public double getVGapProperty(boolean flip) {
		if (flip)
			return getRealProperty(Properties.HGAP);
		else
			return getRealProperty(Properties.VGAP);
	}
	
	public boolean isOnClickPropertySet() {
		return isHandlerPropertySet(Properties.MOUSE_CLICK);
	}

	public void executeOnClick(ICallbackEnv env) {
		if(isOnClickPropertySet())
			executeHandlerProperty(env,Properties.MOUSE_CLICK);
	}

	public double getHAlignProperty(boolean flip) {
		if (flip) {
			return getVAlignProperty();
		} else {
			return getHAlignProperty();
		}
	}

	public double getVAlignProperty(boolean flip) {
		if (flip) {
			return getHAlignProperty();
		} else {
			return getVAlignProperty();
		}
	}
	
	public boolean isHGrowPropertySet(){
		return properties.isRealPropertySet(Properties.HGROW);
	}
	
	public boolean isVGrowPropertySet(){
		return properties.isRealPropertySet(Properties.VGROW);
	}
	
	public boolean isHGrowPropertySet(boolean flip){
		if(flip) return isVGrowPropertySet();
		else return isHGrowPropertySet();
	}
	
	public boolean isVGrowPropertySet(boolean flip){
		if(flip) return isHGrowPropertySet();
		else return isVGrowPropertySet();
	}
	
	public double getHGrowProperty() {
		return properties.getRealProperty(Properties.HGROW);
	}
	
	public double getVGrowProperty() {
		return properties.getRealProperty(Properties.VGROW);
	}
	
	public double getHGrowProperty(boolean flip) {
		if(flip) return getVGrowProperty();
		else return getHGrowProperty();
	}
	

	public double getVGrowProperty(boolean flip) {
		if(flip) return getHGrowProperty();
		else return getVGrowProperty();
	}
	

	public boolean isHShrinkPropertySet(){
		return properties.isRealPropertySet(Properties.HSHRINK);
	}
	
	public boolean isVShrinkPropertySet(){
		return properties.isRealPropertySet(Properties.VSHRINK);
	}
	
	public boolean isHShrinkPropertySet(boolean flip){
		if(flip) return isVShrinkPropertySet();
		else return isHShrinkPropertySet();
	}
	
	public double getHShrinkProperty() {
		return properties.getRealProperty(Properties.HSHRINK);
	}
	
	public double getVShrinkProperty() {
		return properties.getRealProperty(Properties.VSHRINK);
	}
	
	public double getHShrinkProperty(boolean flip) {
		if(flip) return getVShrinkProperty();
		else return getHShrinkProperty();
	}
	

	public double getVShrinkProperty(boolean flip) {
		if(flip) return getHShrinkProperty();
		else return getVShrinkProperty();
	}
	
	public boolean isHStartGapPropertySet(){
		return properties.isBooleanPropertySet(Properties.HSTART_GAP);
	}
	
	public boolean isHEndGapPropertySet(){
		return properties.isBooleanPropertySet(Properties.HEND_GAP);
	}
	
	public boolean isVStartGapPropertySet(){
		return properties.isBooleanPropertySet(Properties.VSTART_GAP);
	}
	
	public boolean isVEndGapPropertySet(){
		return properties.isBooleanPropertySet(Properties.VEND_GAP);
	}
	
	public boolean isHStartGapPropertySet(boolean flip){
		if(flip) return isVStartGapPropertySet();
		else return isHStartGapPropertySet();
	}
	
	public boolean isHEndGapPropertySet(boolean flip){
		if(flip) return isVEndGapPropertySet();
		else return isHEndGapPropertySet();
	}
	
	public boolean getHStartGapProperty() {
		return properties.getBooleanProperty(Properties.HSTART_GAP);
	}
	
	public boolean getHEndGapProperty() {
		return properties.getBooleanProperty(Properties.HEND_GAP);
	}
	
	public boolean getVStartGapProperty() {
		return properties.getBooleanProperty(Properties.VSTART_GAP);
	}
	
	public boolean getVEndGapProperty() {
		return properties.getBooleanProperty(Properties.VEND_GAP);
	}
	
	public boolean getHResizableProperty() {
		return properties.getBooleanProperty(Properties.HRESIZABLE);
	}
	
	public boolean getVResizableProperty() {
		return properties.getBooleanProperty(Properties.VRESIZABLE);
	}
	
	public boolean getHStartGapProperty(boolean flip) {
		if(flip) return getVStartGapProperty();
		else return getHStartGapProperty();
	}
	
	public boolean getHEndGapProperty(boolean flip) {
		if(flip) return getVEndGapProperty();
		else return getHEndGapProperty();
	}
	
	public boolean getHResizableProperty(boolean flip) {
		if(flip) return getVResizableProperty();
		else return getHResizableProperty();
	}
	
	public int getShadowColorProperty() {
		return properties.getColorProperty(Properties.SHADOW_COLOR);
	}
	
	public String getKeyIdForWidth(boolean flip){
		if(flip) return properties.getKeyId(Properties.HEIGHT);
		else return properties.getKeyId(Properties.WIDTH);
	}
	
	public String getKeyIdForHeight(boolean flip){
		if(flip) return properties.getKeyId(Properties.WIDTH);
		else return properties.getKeyId(Properties.HEIGHT);
	}
	
	public String getKeyIdForHLoc(boolean flip){
		if(flip) return properties.getKeyId(Properties.VLOC);
		else return properties.getKeyId(Properties.HLOC);
	}
	
	public String getKeyIdForVLoc(boolean flip){
		if(flip) return properties.getKeyId(Properties.HLOC);
		else return properties.getKeyId(Properties.VLOC);
	}
	
	public double getHLocProperty(){
		return properties.getRealProperty(Properties.HLOC);
	}
	
	public double getVLocProperty(){
		return properties.getRealProperty(Properties.VLOC);
	}
	
	public double getHLocProperty(boolean flip){
		if(flip) return properties.getRealProperty(Properties.VLOC);
		else return properties.getRealProperty(Properties.HLOC);
	}
	
	public double getVLocProperty(boolean flip){
		if(flip) return properties.getRealProperty(Properties.HLOC);
		else return properties.getRealProperty(Properties.VLOC);
	}
	
	public IValue getHLocPropertyUnconverted(boolean flip){
		if(flip) return properties.getUnconverted(Properties.VLOC);
		else return properties.getUnconverted(Properties.HLOC);
	}
	
	public IValue getVLocPropertyUnconverted(boolean flip){
		if(flip) return properties.getUnconverted(Properties.HLOC);
		else return properties.getUnconverted(Properties.VLOC);
	}
	
	public double getHConnectProperty(){
		return properties.getRealProperty(Properties.HCONNECT);
	}
	
	public double getVConnectProperty(){
		return properties.getRealProperty(Properties.VCONNECT);
	}
	
	public boolean isHLocPropertySet(){
		return properties.isPropertySet(Properties.HLOC);
	}
	
	public boolean isVLocPropertySet(){
		return properties.isPropertySet(Properties.VLOC);
	}
	

	public boolean isHLocPropertySet(boolean flip){
		if(flip) return isVLocPropertySet();
		else return isHLocPropertySet();
	}
	
	public boolean isVLocPropertySet(boolean flip){
		if(flip) return isHLocPropertySet();
		else return isVLocPropertySet();
	}
	
	public boolean isHLocPropertyConverted(){
		return properties.isConverted(Properties.HLOC);
	}
	
	public boolean isVLocPropertyConverted(){
		return properties.isConverted(Properties.VLOC);
	}

	public boolean isHLocPropertyConverted(boolean flip){
		if(flip) return isVLocPropertyConverted();
		else return isHLocPropertyConverted();
	}
	
	public boolean isVLocPropertyConverted(boolean flip){
		if(flip) return isHLocPropertyConverted();
		else return isVLocPropertyConverted();
	}
	

	
	public double getTextAscent(){
		return SWTFontsAndColors.textAscent(
				properties.getStringProperty(Properties.FONT),
				properties.getIntegerProperty(Properties.FONT_SIZE));
	}
	
	public double getTextDescent(){
		return SWTFontsAndColors.textDescent(
				properties.getStringProperty(Properties.FONT),
				properties.getIntegerProperty(Properties.FONT_SIZE));
	}
	
	public double getTextWidth(String s){
		return SWTFontsAndColors.textWidth(s, 
				properties.getStringProperty(Properties.FONT),
				properties.getIntegerProperty(Properties.FONT_SIZE));
	}
}
