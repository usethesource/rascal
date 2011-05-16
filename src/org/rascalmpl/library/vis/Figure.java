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

import java.util.HashMap;
import java.util.Vector;

import org.rascalmpl.library.vis.containers.HScreen;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.library.vis.properties.Measure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;
import org.rascalmpl.library.vis.properties.descriptions.ColorProp;
import org.rascalmpl.library.vis.properties.descriptions.DimensionalProp;
import org.rascalmpl.library.vis.properties.descriptions.FigureProp;
import org.rascalmpl.library.vis.properties.descriptions.HandlerProp;
import org.rascalmpl.library.vis.properties.descriptions.IntProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.Dimension;


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

	public static final double AUTO_SIZE = -1.0;
	public static int sequencer = 0; // to impose arbitrary ordering on figures
	public int sequenceNr;
	@SuppressWarnings("unused")
	private final boolean debug = false;
	public IFigureApplet fpa;
	protected HashMap<String, Double> axisScales;

	public PropertyManager properties;

	
	private double left;		// coordinates of top left corner of
	private double top;		// the element's bounding box
	public double width;			// width of element
	public double height;		// height of element
	                            // When this figure is used as mouseOver or inner figure, point back
	                            // to generating Figure
	protected double scaleX, scaleY;
		
	protected Figure(IFigureApplet fpa, PropertyManager properties){
		this.fpa = fpa;
		this.properties = properties;
		String id = properties.getStringProperty(StrProp.ID);
		if(id != null)
			fpa.registerId(id, this);
		scaleX = scaleY = 1.0f;
		sequenceNr = sequencer;
		sequencer++;
	}
	
	protected void setLeft(double left) {
		this.left = left;
	}
	

	public double getLeft() {
		return left;
	}

	protected void setTop(double top) {
		this.top = top;
	}

	public double getTop() {
		return top;
	}
	
	public double getCenterX(){
		return getLeft() + width/2;
	}

	public double getCenterY(){
		return getTop() + height/2;
	}

	public static double max(double a, double b) {
		return a > b ? a : b;
	}

	public static double min(double a, double b) {
		return a < b ? a : b;
	}

	public double abs(double a) {
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
	

	// TODO: irregular

	public boolean isVisible() {
		return true;
		// return fpa.isVisible(properties.getDOI());
	}

	public boolean isNextVisible() {
		return true;
		// return fpa.isVisible(properties.getDOI() + 1);
	}
	
	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		
	}

	
	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		if(horizontal && getMeasureProperty(DimensionalProp.WIDTH).axisName.equals(axisId)){
			double val = getMeasureProperty(DimensionalProp.WIDTH).value;
			return new Extremes(offset - getHAlignProperty() * val, offset + (1-getHAlignProperty()) * val);
		} else if( !horizontal && getMeasureProperty(DimensionalProp.HEIGHT).axisName.equals(axisId)){
			double val = getMeasureProperty(DimensionalProp.HEIGHT).value;
			return new Extremes(offset - getVAlignProperty() * val, offset + (1-getVAlignProperty()) * val);
		} else {
			return new Extremes();
		}
	}
	
	public double getOffsetForAxis(String axisId, double offset, boolean horizontal){
		if(horizontal && getMeasureProperty(DimensionalProp.WIDTH).axisName.equals(axisId)){
			return offset;
		} else if( !horizontal && getMeasureProperty(DimensionalProp.HEIGHT).axisName.equals(axisId)){
			return offset;
		} else {
			return Double.MAX_VALUE;
		}
	}
	
	public Extremes getHorizontalBorders(){
		return new Extremes(0,width);
	}
	
	public Extremes getVerticalBorders(){
		return new Extremes(0,height);
	}

	/*
	 * Compare two Figures using an arbitrary ordering
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Figure o){
		return sequenceNr - o.sequenceNr;
	}
	

	/*
	 * Compare two Figures according to their surface and aspect ratio
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/*
	public int compareTo(Figure o) {
		double r = (height > width) ? height / width : width / height;
		double or = (o.height > o.width) ? o.height / o.width : o.width
				/ o.height;

		if (r < 2f && or < 2f) {
			double s = height * width;
			double os = o.height * o.width;
			return s < os ? 1 : (s == os ? 0 : -1);
		}
		return r < or ? 1 : (r == or ? 0 : -1);
	}
	*/

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
	
	public
	void bbox(){
		bbox(AUTO_SIZE,AUTO_SIZE);
	}

	public
	void bbox(BoundingBox b){
		bbox(b.getWidth(),b.getHeight());
	}
	
	public abstract void bbox(double desiredWidth, double desiredHeight);

	/**
	 * Draw element with explicitly left, top corner of its bounding box
	 * 
	 * @param left
	 *            x-coordinate of corner
	 * @param top
	 *            y-coordinate of corner
	 */

	public abstract void draw(double left, double top);
	
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
	public void connectArrowFrom(double left, double top, double X, double Y,
			double fromX, double fromY, Figure toArrow) {
		if (fromX == X)
			fromX += 0.00001;
		double s = (fromY - Y) / (fromX - X);

		double theta = FigureApplet.atan(s);
		if (theta < 0) {
			if (fromX < X)
				theta += FigureApplet.PI;
		} else {
			if (fromX < X)
				theta += FigureApplet.PI;
		}
		double IX;
		double IY;

		double h2 = height / 2;
		double w2 = width / 2;

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
		double h2 = height / 2;
		double w2 = width / 2;

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
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		result.add(this); return true;
	}
	
	public void executeMouseOverOffHandlers(HandlerProp prop) {
		if(isHandlerPropertySet(prop)){
			executeHandlerProperty(prop);
		}
	}
	
	public void executeMouseOverHandlers(){
		executeMouseOverOffHandlers(HandlerProp.ON_MOUSEOVER);
	}
	
	public void executeMouseOffHandlers(){
		executeMouseOverOffHandlers(HandlerProp.ON_MOUSEOFF);
	}

	public boolean mouseInside(double mouseX, double mouseY){
		return  (mouseX >= getLeft()  && mouseX <= getLeft() + width) &&
		             (mouseY >= getTop()  && mouseY <= getTop() + height);
	}


	/**
	 * @param key  
	 * @param keyCode 
	 */
	public boolean keyPressed(int key, int keyCode) {
		return false;
	}
	
	public void propagateScaling(double scaleX,double scaleY, HashMap<String,Double> axisScales){
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.axisScales = axisScales;
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
	public double getRealProperty(RealProp property){
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
	
	public boolean isMeasurePropertySet(DimensionalProp property){
		return properties.isMeasurePropertySet(property);
	}
	public Measure getMeasureProperty(DimensionalProp property) {
		return properties.getMeasureProperty(property);
	}
	
	public boolean isMeasurePropertySet(DimensionalProp.TranslateDimensionToProp prop, Dimension d){
		return properties.isMeasurePropertySet(prop, d);
	}
	
	public Measure getMeasureProperty(DimensionalProp.TranslateDimensionToProp prop, Dimension d){
		return properties.getMeasureProperty(prop, d);
	}
	
	public double getScaledMeasureProperty(DimensionalProp.TranslateDimensionToProp prop, Dimension d){
		return getScaled(prop.getDimensionalProp(d), d);
	}
	
	public double getScaledMeasureProperty(DimensionalProp prop){
		return getScaled(prop, prop.getDimension());
	}
	
	public boolean isHandlerPropertySet(HandlerProp property){
		return properties.isHandlerPropertySet(property);
	}
	public boolean isStandardHandlerPropertySet(HandlerProp property){
		return isStandardHandlerPropertySet(property);
	}
	
	public boolean isStandardDefaultHandlerPropertySet(HandlerProp property){
		return isStandardDefaultHandlerPropertySet(property);
	}
	
	public void executeHandlerProperty(HandlerProp property) {
		properties.executeHandlerProperty(property);
	}
	
	public boolean isFigurePropertySet(FigureProp property){
		return properties.isFigurePropertySet(property);
	}
	public Figure getFigureProperty(FigureProp property) {
		return properties.getFigureProperty(property);
	}
	
	public boolean isDraggable(){
		return properties.isDraggable();
	}
	public Figure getMouseOver(){
		return properties.getMouseOver();
	}
	
	public Figure getToArrow(){
		return properties.getToArrow();
	}
	
	public Figure getFromArrow(){
		return properties.getFromArrow();
	}
		
	public Figure getLabel(){
		return properties.getLabel();
	}
	
	double getScaled(Measure m, Dimension dimension){
		return getScaled(m,dimension,null);
	}
	
	double getScaled(Measure m, Dimension dimension,DimensionalProp prop){
		double scale;
		switch(dimension){
		case X: scale = scaleX; break;
		case Y: scale = scaleY; break;
		default : throw new Error("Unkown dimension!");
		}
		if(axisScales != null && axisScales.containsKey(m.axisName) && !m.axisName.equals("")){
			
			scale*= axisScales.get(m.axisName);
			//System.out.printf("Getting on %s axis %s prop %s scale %f value %f scaledValued %f\n" , this, m.axisName, prop, scale, m.value, m.value*scale );
		}
		return m.value * scale;
	}
	
	double getScaled(DimensionalProp prop,  Dimension dimension){
		Measure m = getMeasureProperty(prop);
		return getScaled(m,dimension,prop);
	}
	
	// Anchors

	public double leftAlign() {
		double res= (getRealProperty(RealProp.HALIGN) * width);
		return res;
	}

	public double rightAlign() {
		double res =  (width - getRealProperty(RealProp.HALIGN) * width);
		return res;
	}

	public double topAlign() {
		return (getRealProperty(RealProp.VALIGN) * height);
	}

	public double bottomAlign() {
		return (height - getRealProperty(RealProp.VALIGN) * height);
	}
	
	public double leftAlign(boolean flip) {
		if(flip){
			return bottomAlign();
		} else {
			return leftAlign();
		}
	}

	public double rightAlign(boolean flip) {
		if(flip){
			return topAlign();
		} else {
			return rightAlign();
		}
	}

	public double topAlign(boolean flip) {
		if(flip){
			return rightAlign();
		} else {
			return topAlign();
		}
	}

	public double bottomAlign(boolean flip) {
		if(flip){
			return leftAlign();
		} else {
			return bottomAlign();
		}
	}
	
	// short-hand functions for selected properties(boilerplate)
	public boolean getClosedProperty(){ return getBooleanProperty(BoolProp.SHAPE_CLOSED);}
	public boolean getCurvedProperty(){ return getBooleanProperty(BoolProp.SHAPE_CURVED);}
	public boolean getConnectedProperty(){ return getBooleanProperty(BoolProp.SHAPE_CONNECTED);}
	public String getIdProperty(){return getStringProperty(StrProp.ID);}
	public String getDirectionProperty(){return getStringProperty(StrProp.DIRECTION);}
	public String getLayerProperty(){return getStringProperty(StrProp.LAYER);}
	public boolean isHeightPropertySet(){return isMeasurePropertySet(DimensionalProp.HEIGHT);}
	public boolean isHGapPropertySet(){return isMeasurePropertySet(DimensionalProp.HGAP);}
	public boolean isVGapPropertySet(){return isMeasurePropertySet(DimensionalProp.VGAP);}
	// below are convience functions for measures, which are scaled (text and linewidth are not scaled)
	public boolean isWidthPropertySet(){return isMeasurePropertySet(DimensionalProp.WIDTH);}
	public double getWidthProperty(){ return getScaledMeasureProperty(DimensionalProp.WIDTH);}
	public double getHeightProperty(){return  getScaledMeasureProperty(DimensionalProp.HEIGHT);}
	public double getHGapProperty(){return getScaledMeasureProperty(DimensionalProp.HGAP);}
	public double getVGapProperty(){return getScaledMeasureProperty(DimensionalProp.VGAP);}
	// TODO: how to scale wedges!
	public double getInnerRadiusProperty(){return getRealProperty(RealProp.INNERRADIUS);}
	
	public boolean isHGapFactorPropertySet(){return isRealPropertySet(RealProp.HGAP_FACTOR);}
	public double getHGapFactorProperty() { return getRealProperty(RealProp.HGAP_FACTOR);}
	public boolean isVGapFactorPropertySet(){return isRealPropertySet(RealProp.VGAP_FACTOR);}
	public double getVGapFactorProperty() { return getRealProperty(RealProp.VGAP_FACTOR);}
	public double getHAlignProperty(){return getRealProperty(RealProp.HALIGN);}
	public double getVAlignProperty(){return getRealProperty(RealProp.VALIGN);}
	public double getLineWidthProperty(){return getRealProperty(RealProp.LINE_WIDTH);}
	public double getTextAngleProperty(){return getRealProperty(RealProp.TEXT_ANGLE);}
	public double getFromAngleProperty(){return getRealProperty(RealProp.FROM_ANGLE);}
	public double getToAngleProperty(){return getRealProperty(RealProp.TO_ANGLE);}
	public int getFillColorProperty(){return getColorProperty(ColorProp.FILL_COLOR);}
	public int getFontColorProperty(){return getColorProperty(ColorProp.FONT_COLOR);}
	public boolean getStartGapProperty(){ return getBooleanProperty(BoolProp.START_GAP);}
	public boolean getEndGapProperty(){ return getBooleanProperty(BoolProp.END_GAP);}
	
	
	public boolean isWidthPropertySet(boolean flip) { return isMeasurePropertySet(DimensionalProp.TranslateDimensionToProp.DIMENSION, Dimension.flip(flip,Dimension.X)); }
	public double getWidthProperty(boolean flip) { return getScaledMeasureProperty(DimensionalProp.TranslateDimensionToProp.DIMENSION, Dimension.flip(flip,Dimension.X)); }
	public boolean isHeightPropertySet(boolean flip) { return isMeasurePropertySet(DimensionalProp.TranslateDimensionToProp.DIMENSION, Dimension.flip(flip,Dimension.Y)); }
	public double getHeightProperty(boolean flip) { return getScaledMeasureProperty(DimensionalProp.TranslateDimensionToProp.DIMENSION, Dimension.flip(flip,Dimension.Y));}
	public boolean isHGapPropertySet(boolean flip) { return isMeasurePropertySet(DimensionalProp.TranslateDimensionToProp.GAP, Dimension.flip(flip,Dimension.X)); }
	public double getHGapProperty(boolean flip) { return getScaledMeasureProperty(DimensionalProp.TranslateDimensionToProp.GAP, Dimension.flip(flip,Dimension.X)); }
	public boolean isVGapPropertySet(boolean flip) { return isMeasurePropertySet(DimensionalProp.TranslateDimensionToProp.GAP, Dimension.flip(flip,Dimension.Y)); }
	public double getVGapProperty(boolean flip) { return getScaledMeasureProperty(DimensionalProp.TranslateDimensionToProp.GAP, Dimension.flip(flip,Dimension.Y)); }
	
	public boolean isMouseOverSet() { return isFigurePropertySet(FigureProp.MOUSE_OVER);}
	public Figure getMouseOverProperty() { return getFigureProperty(FigureProp.MOUSE_OVER); }
	public boolean isOnClickPropertySet() { return isHandlerPropertySet(HandlerProp.MOUSE_CLICK);}
	public void executeOnClick() { executeHandlerProperty(HandlerProp.MOUSE_CLICK); }
	
	public boolean isHGapFactorPropertySet(boolean flip){
		if(flip){
			return isVGapFactorPropertySet();
		} else {
			return isHGapFactorPropertySet();
		}
	}
	public double getHGapFactorProperty(boolean flip){
		if(flip){
			return getVGapFactorProperty();
		} else {
			return getHGapFactorProperty();
		}
	}
	public boolean isVGapFactorPropertySet(boolean flip){
		if(flip){
			return isHGapFactorPropertySet();
		} else {
			return isVGapFactorPropertySet();
		}
	}
	public double getVGapFactorProperty(boolean flip){
		if(flip){
			return getHGapFactorProperty();
		} else {
			return getVGapFactorProperty();
		}
	}
	
	public double getHAlignProperty(boolean flip){
		if(flip){
			return getVAlignProperty();
		} else {
			return getHAlignProperty();
		}
	}
	
	public double getVAlignProperty(boolean flip) {
		if(flip){
			return getHAlignProperty();
		} else {
			return getVAlignProperty();
		}
	}
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return height;
	}

	public double getWidth(boolean flip){
		if(flip) return getHeight();
		else return getWidth();
	}
	
	public double getHeight(boolean flip){
		if(flip) return getWidth();
		else return getHeight();
	}
}
