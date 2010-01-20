package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.library.experiments.VL.PropertyManager.Property;
import org.meta_environment.values.ValueFactoryFactory;

import processing.core.PApplet;


/**
 * @author paulk
 *
 */

/*
 * Still looking for a good name for "visual elements" Candidates are:
 * - Figure
 * - Picture
 * - Diagram
 * - Shape
 * - Mark
 * - Visual
 */

/*
 * Visual elements are based on a bounding box + anchor model. The bounding box defines
 * the maximal dimensions of the element. The anchor defines its alignment properties.
 */

public abstract class VELEM implements Comparable<VELEM> {
	
	protected VLPApplet vlp;
	
	protected IValueFactory vf;
	
	protected PropertyManager properties;
	
	protected int left;                // coordinates of top left corner of
	protected int top; 				// the element's bounding box
	protected float width = 0;		// width of element
	protected float height = 0;		// height picture
	protected int aleft = 0;		    // horizontal distance of left border to anchor
	protected int aright = 0;         // horizontal distance from anchor to right border
	protected int atop = 0;           // vertical distance from top to anchor
	protected int abottom = 0;        // vertical distance from anchor to bottom
	
	VELEM(VLPApplet vlp, IEvaluatorContext ctx){
		this(vlp, null,ValueFactoryFactory.getValueFactory().list(), ctx);
		//this.vlp = vlp;
		//vf = ValueFactoryFactory.getValueFactory();
		//properties = new PropertyManager(vlp, null, vf.list(), ctx);
	}
	
	VELEM(VLPApplet vlp, PropertyManager inheritedProps, IList props, IEvaluatorContext ctx){
		this.vlp = vlp;
		properties = new PropertyManager(vlp, inheritedProps, props, ctx);
		vf = ValueFactoryFactory.getValueFactory();
	}
	
	public float max(float a, float b){
		return a > b ? a : b;
	}
	
	public float min(float a, float b){
		return a < b ? a : b;
	}
	
	public float abs(float a){
		return a >= 0 ? a : -a;
	}
	
	public void applyProperties(){
		properties.applyProperties();
	}
	
	public void applyFontColorProperty(){
		properties.applyFontColorProperty();
	}
	
	protected int getHeightProperty(){
		return properties.getInt(Property.HEIGHT);
	}
	
	protected int getWidthProperty(){
		return properties.getInt(Property.WIDTH);
	}
	
	protected int getHGapProperty(){
		return properties.getInt(Property.HGAP);
	}
	
	protected int getVGapProperty(){
		return properties.getInt(Property.VGAP);
	}
	
	protected int getFillColorProperty(){
		return properties.getInt(Property.FILLCOLOR);
	}
	
	protected int getLineColorProperty(){
		return properties.getInt(Property.LINECOLOR);
	}
	
	protected int getLineWidthProperty(){
		return properties.getInt(Property.LINEWIDTH);
	}
	
	protected boolean isTopAligned(){
		return properties.getBool(Property.TOP);
	}
	
	protected boolean isBottomAligned(){
		return properties.getBool(Property.BOTTOM);
	}
	
	protected boolean isLeftAligned(){
		return properties.getBool(Property.LEFT);
	}
	
	protected boolean isRightAligned(){
		return properties.getBool(Property.RIGHT);
	}
	
	protected float leftAnchor(){
		return (properties.hanchor * width);
	}
	
	protected float rightAnchor(){
		return (width - properties.hanchor * width);
	}
	
	protected float topAnchor(){
		return (properties.vanchor * height);
	}
	
	protected float bottomAnchor(){
		return (height - properties.vanchor * height);
	}
	
	protected boolean isClosed(){
		return properties.getBool(Property.CLOSED);
	}
	
	protected boolean isConnected(){
		return properties.getBool(Property.CONNECTED);
	}
	
	protected boolean isCurved(){
		return properties.getBool(Property.CURVED);
	}
	
	protected int getFromAngleProperty(){
		return properties.getInt(Property.FROMANGLE);
	}
	
	protected int getToAngleProperty(){
		return properties.getInt(Property.TOANGLE);
	}
	
	protected int getInnerRadiusProperty(){
		return properties.getInt(Property.INNERRADIUS);
	}
	
	protected String getIdProperty(){
		return properties.getStr(Property.ID);
	}
	
	protected String getFontProperty(){
		return properties.getStr(Property.FONT);
	}
	
	protected int getFontSizeProperty(){
		return properties.getInt(Property.FONTSIZE);
	}
	
	protected int getFontColorProperty(){
		return properties.getInt(Property.FONTCOLOR);
	}
	
	protected int getTextAngleProperty(){
		return properties.getInt(Property.TEXTANGLE);
	}
	
	public boolean hasInteraction(){
		return properties.mouseOverproperties != null;
	}
	
	public VELEM getInsideForMouseOver(){
		return properties.mouseOverVElem;
	}
	
	/* 
	 * Compare two VELEMs according to their surface and aspect ratio
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(VELEM o){
		float r = (height > width) ? height / width : width / height;
		float or = (o.height > o.width) ? o.height / o.width : o.width / o.height;
		
        if(r < 2f && or < 2f){
        	float s = height * width;
        	float os = o.height * o.width;
        	return s < os ? 1 : (s == os ? 0 : -1);
        }
        return r < or ? 1 : (r == or ? 0 : -1);
	}
	
	/**
	 * Compute the bounding box of the element. Should be called before draw since,
	 * the computed width and height are stored in the element itself.
	 */
	void bbox() {
		bbox(0,0);
	}
	
	/**
	 * Compute the bounding box of the element. Should be called before draw since,
	 * the computed width and height are stored in the element itself.
	 */
	abstract void bbox(int left, int top);
	
	/**
	 * Draw element with given left, top corner of its bounding box
	 */
	abstract void draw();
		
	/**
	 * Draw element with given left, top corner of its bounding box
	 * @param left	x-coordinate of corner
	 * @param top	y-coordinate of corner
	 */
	void draw(float left, float top){
		this.left = PApplet.round(left);
		this.top = PApplet.round(top);
		draw();
	}
	
	/**
	 * Draw element with given left, top corner of its bounding box
	 * @param left	x-coordinate of corner
	 * @param top	y-coordinate of corner
	 */
	void draw(int left, int top){
		this.left = left;
		this.top = top;
		draw();
	}
	
	public boolean mouseOver(int mousex, int mousey){
		if((mousex > left && mousex < left + width) &&
		   (mousey > top  && mousey < top + height)){
		   properties.setMouseOver(true);
		   vlp.registerMouse(this);
		   return true;
		}
		return false;
	}
	
}
