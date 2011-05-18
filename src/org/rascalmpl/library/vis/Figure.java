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
import org.rascalmpl.library.vis.properties.Measure;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
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

public abstract class Figure implements Comparable<Figure> {

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
		properties.computeProperties();
		
	
		scaleX = scaleY = 1.0f;
		sequenceNr = sequencer;
		sequencer++;
	}
	
	public void registerNames(){
		String id = properties.getStringProperty(Properties.ID);
		if(id!=null && !id.equals(""))
			fpa.registerId(id, this);
	}
	

	public void computeFiguresAndProperties(){
		properties.computeProperties();
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

	public void applyProperties() {
		fpa.fill(getColorProperty(Properties.FILL_COLOR));
		fpa.stroke(getColorProperty(Properties.LINE_COLOR));
		fpa.strokeWeight(getRealProperty(Properties.LINE_WIDTH));
		fpa.textSize(getIntegerProperty(Properties.FONT_SIZE));
	}

	public void applyFontProperties() {
		fpa.textFont(fpa.createFont(getStringProperty(Properties.FONT),
				getIntegerProperty(Properties.FONT_SIZE)));
		fpa.textColor(getColorProperty(Properties.FONT_COLOR));
	}
	
	public void gatherProjections(double left, double top, Vector<HScreen.ProjectionPlacement> projections, boolean first, String screenId, boolean horizontal){
		
	}

	
	public Extremes getExtremesForAxis(String axisId, double offset, boolean horizontal){
		if(horizontal && getMeasureProperty(Properties.WIDTH).axisName.equals(axisId)){
			double val = getMeasureProperty(Properties.WIDTH).value;
			return new Extremes(offset - getHAlignProperty() * val, offset + (1-getHAlignProperty()) * val);
		} else if( !horizontal && getMeasureProperty(Properties.HEIGHT).axisName.equals(axisId)){
			double val = getMeasureProperty(Properties.HEIGHT).value;
			return new Extremes(offset - getVAlignProperty() * val, offset + (1-getVAlignProperty()) * val);
		} else {
			return new Extremes();
		}
	}
	
	public double getOffsetForAxis(String axisId, double offset, boolean horizontal){
		if(horizontal && getMeasureProperty(Properties.WIDTH).axisName.equals(axisId)){
			return offset;
		} else if( !horizontal && getMeasureProperty(Properties.HEIGHT).axisName.equals(axisId)){
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
		fpa.stroke(255, 0, 0);
		fpa.strokeWeight(1);
		fpa.noFill();
		fpa.rect(getLeft(), getTop(), width, height);
	}
	
	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		result.add(this); return true;
	}
	
	public void executeMouseOverOffHandlers(Properties prop) {
		if(isHandlerPropertySet(prop)){
			executeHandlerProperty(prop);
		}
	}
	
	public void executeMouseOverHandlers(){
		executeMouseOverOffHandlers(Properties.ON_MOUSEOVER);
	}
	
	public void executeMouseOffHandlers(){
		executeMouseOverOffHandlers(Properties.ON_MOUSEOFF);
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
	public boolean isBooleanPropertySet(Properties property){
		return properties.isBooleanPropertySet(property);
	}
	public boolean getBooleanProperty(Properties property){
		return properties.getBooleanProperty(property);
	}
	public boolean isIntegerPropertySet(Properties property){
		return properties.isIntegerPropertySet(property);
	}
	public int getIntegerProperty(Properties property) {
		return properties.getIntegerProperty(property);
	}
	public boolean isRealPropertySet(Properties property){
		return properties.isRealPropertySet(property);
	}
	public double getRealProperty(Properties property){
		return properties.getRealProperty(property);
	}
	public boolean isStringPropertySet(Properties property){
		return properties.isStringPropertySet(property);
	}
	public String getStringProperty(Properties property){
		return properties.getStringProperty(property);
	}
	public boolean isColorPropertySet(Properties property){
		return properties.isColorPropertySet(property);
	}
	public int getColorProperty(Properties property) {
		return properties.getColorProperty(property);
	}
	
	public boolean isMeasurePropertySet(Properties property){
		return properties.isMeasurePropertySet(property);
	}
	public Measure getMeasureProperty(Properties property) {
		return properties.getMeasureProperty(property);
	}
	
	public double getScaledMeasureProperty(Properties prop){
		return getScaled(prop, prop.dimension);
	}
	
	public boolean isHandlerPropertySet(Properties property){
		return properties.isHandlerPropertySet(property);
	}
	public boolean isStandardHandlerPropertySet(Properties property){
		return isStandardHandlerPropertySet(property);
	}
	
	public boolean isStandardDefaultHandlerPropertySet(Properties property){
		return isStandardDefaultHandlerPropertySet(property);
	}
	
	public void executeHandlerProperty(Properties property) {
		properties.executeHandlerProperty(property);
	}
	
	public boolean isFigurePropertySet(Properties property){
		return properties.isFigurePropertySet(property);
	}
	public Figure getFigureProperty(Properties property) {
		return properties.getFigureProperty(property);
	}
	
	public Figure getToArrow(){
		return getFigureProperty(Properties.TO_ARROW);
	}
	
	public Figure getFromArrow(){
		return getFigureProperty(Properties.FROM_ARROW);
	}
		
	public Figure getLabel(){
		return getFigureProperty(Properties.LABEL);
	}
	
	protected double getScaled(Measure m, Dimension dimension){
		return getScaled(m,dimension,null);
	}
	
	double getScaled(Measure m, Dimension dimension,Properties prop){
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
	
	double getScaled(Properties prop,  Dimension dimension){
		Measure m = getMeasureProperty(prop);
		return getScaled(m,dimension,prop);
	}
	
	// Anchors

	public double leftAlign() {
		double res= (getRealProperty(Properties.HALIGN) * width);
		return res;
	}

	public double rightAlign() {
		double res =  (width - getRealProperty(Properties.HALIGN) * width);
		return res;
	}

	public double topAlign() {
		return (getRealProperty(Properties.VALIGN) * height);
	}

	public double bottomAlign() {
		return (height - getRealProperty(Properties.VALIGN) * height);
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
	public boolean getClosedProperty(){ return getBooleanProperty(Properties.SHAPE_CLOSED);}
	public boolean getCurvedProperty(){ return getBooleanProperty(Properties.SHAPE_CURVED);}
	public boolean getConnectedProperty(){ return getBooleanProperty(Properties.SHAPE_CONNECTED);}
	public String getIdProperty(){return getStringProperty(Properties.ID);}
	public String getDirectionProperty(){return getStringProperty(Properties.DIRECTION);}
	public String getLayerProperty(){return getStringProperty(Properties.LAYER);}
	public boolean isHeightPropertySet(){return isMeasurePropertySet(Properties.HEIGHT);}
	public boolean isHGapPropertySet(){return isMeasurePropertySet(Properties.HGAP);}
	public boolean isVGapPropertySet(){return isMeasurePropertySet(Properties.VGAP);}
	// below are convience functions for measures, which are scaled (text and linewidth are not scaled)
	public boolean isWidthPropertySet(){return isMeasurePropertySet(Properties.WIDTH);}
	public double getWidthProperty(){ return getScaledMeasureProperty(Properties.WIDTH);}
	public double getHeightProperty(){return  getScaledMeasureProperty(Properties.HEIGHT);}
	public double getHGapProperty(){return getScaledMeasureProperty(Properties.HGAP);}
	public double getVGapProperty(){return getScaledMeasureProperty(Properties.VGAP);}
	// TODO: how to scale wedges!
	public double getInnerRadiusProperty(){return getRealProperty(Properties.INNERRADIUS);}
	
	public boolean isHGapFactorPropertySet(){return isRealPropertySet(Properties.HGAP_FACTOR);}
	public double getHGapFactorProperty() { return getRealProperty(Properties.HGAP_FACTOR);}
	public boolean isVGapFactorPropertySet(){return isRealPropertySet(Properties.VGAP_FACTOR);}
	public double getVGapFactorProperty() { return getRealProperty(Properties.VGAP_FACTOR);}
	public double getHAlignProperty(){return getRealProperty(Properties.HALIGN);}
	public double getVAlignProperty(){return getRealProperty(Properties.VALIGN);}
	public double getLineWidthProperty(){return getRealProperty(Properties.LINE_WIDTH);}
	public double getTextAngleProperty(){return getRealProperty(Properties.TEXT_ANGLE);}
	public double getFromAngleProperty(){return getRealProperty(Properties.FROM_ANGLE);}
	public double getToAngleProperty(){return getRealProperty(Properties.TO_ANGLE);}
	public int getFillColorProperty(){return getColorProperty(Properties.FILL_COLOR);}
	public int getFontColorProperty(){return getColorProperty(Properties.FONT_COLOR);}
	public boolean getStartGapProperty(){ return getBooleanProperty(Properties.START_GAP);}
	public boolean getEndGapProperty(){ return getBooleanProperty(Properties.END_GAP);}
	
	
	public boolean isWidthPropertySet(boolean flip) {
		if(flip) return isMeasurePropertySet(Properties.HEIGHT);
		else return isMeasurePropertySet(Properties.WIDTH);
	}
	public double getWidthProperty(boolean flip) {
		if(flip) return getScaledMeasureProperty(Properties.HEIGHT);
		else return getScaledMeasureProperty(Properties.WIDTH);
	}
	public boolean isHeightPropertySet(boolean flip) {
		if(flip) return isMeasurePropertySet(Properties.WIDTH);
		else return isMeasurePropertySet(Properties.HEIGHT);
	}
	public double getHeightProperty(boolean flip) {
		if(flip) return getScaledMeasureProperty(Properties.WIDTH);
		else return getScaledMeasureProperty(Properties.HEIGHT);
	}
	public boolean isHGapPropertySet(boolean flip) {
		if(flip) return isMeasurePropertySet(Properties.VGAP);
		else return isMeasurePropertySet(Properties.HGAP);
	}
	public double getHGapProperty(boolean flip) { 
		if(flip) return getScaledMeasureProperty(Properties.VGAP);
		else return getScaledMeasureProperty(Properties.HGAP);
	}
	public boolean isVGapPropertySet(boolean flip) { 
		if(flip) return isMeasurePropertySet(Properties.HGAP);
		else return isMeasurePropertySet(Properties.VGAP);
	}
	public double getVGapProperty(boolean flip) { 
		if(flip) return getScaledMeasureProperty(Properties.HGAP);
		else return getScaledMeasureProperty(Properties.VGAP);
	}
	
	public boolean isMouseOverSet() { return isFigurePropertySet(Properties.MOUSE_OVER);}
	public Figure getMouseOverProperty() { return getFigureProperty(Properties.MOUSE_OVER); }
	public boolean isOnClickPropertySet() { return isHandlerPropertySet(Properties.MOUSE_CLICK);}
	public void executeOnClick() { executeHandlerProperty(Properties.MOUSE_CLICK); }
	
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
