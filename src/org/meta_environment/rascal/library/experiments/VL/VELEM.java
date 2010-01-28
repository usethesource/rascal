package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.values.ValueFactoryFactory;

/**
 * Visual elements are the foundation of Rascal visualization. They are based on a bounding box + anchor model. 
 * The bounding box defines the maximal dimensions of the element. The anchor defines its alignment properties.
 * 
 * Each element has an associated property manager whose values can be accessed via this class.
 * 
 * @author paulk
 */

/*
 * Still looking for a good name for "visual elements" Candidates are:
 * - Figure
 * - Picture
 * - Pictorial
 * - Diagram
 * - Shape
 * - Mark
 * - Visual
 * - Sketch
 */

public abstract class VELEM implements Comparable<VELEM> {
	
	protected VLPApplet vlp;
	
	protected IValueFactory vf;
	
	protected PropertyManager properties;
	
	protected float left;         // coordinates of top left corner of
	protected float top; 			// the element's bounding box
	protected float width;		// width of element
	protected float height;		// height of element
	
	VELEM(VLPApplet vlp, IEvaluatorContext ctx){
		this(vlp, null,ValueFactoryFactory.getValueFactory().list(), ctx);
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
	
	protected float getHeightProperty(){
		return properties.height;
	}
	
	protected float getWidthProperty(){
		return properties.width;
	}
	
	protected float getHGapProperty(){
		return properties.hgap;
	}
	
	protected float getVGapProperty(){
		return properties.vgap;
	}
	
	protected int getFillColorProperty(){
		return properties.fillColor;
	}
	
	protected int getLineColorProperty(){
		return properties.lineColor;
	}
	
	protected int getLineWidthProperty(){
		return properties.lineWidth;
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
		return properties.closed;
	}
	
	protected boolean isConnected(){
		return properties.connected;
	}
	
	protected boolean isCurved(){
		return properties.curved;
	}
	
	protected float getFromAngleProperty(){
		return properties.fromAngle;
	}
	
	protected float getToAngleProperty(){
		return properties.toAngle;
	}
	
	protected float getInnerRadiusProperty(){
		return properties.innerRadius;
	}
	
	protected String getIdProperty(){
		return properties.id;
	}
	
	protected String getFontProperty(){
		return properties.font;
	}
	
	protected int getFontSizeProperty(){
		return properties.fontSize;
	}
	
	protected int getFontColorProperty(){
		return properties.fontColor;
	}
	
	protected float getTextAngleProperty(){
		return properties.textAngle;
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
	abstract void bbox(float left, float top);
	
	/**
	 * Draw element with left, top corner that was computed before by bbox.
	 */
	abstract void draw();
		
	/**
	 * Draw element with explicitly left, top corner of its bounding box
	 * @param left	x-coordinate of corner
	 * @param top	y-coordinate of corner
	 */
	void draw(float left, float top){
		this.left = left;
		this.top = top;
		draw();
	}
	
//	/**
//	 * Draw element at its anchor position. Intended to avoid round-off errors due to repeated computation.
//	 * @param ax	x-coordinate of anchor
//	 * @param ay	y-coordinate of anchor
//	 */
//	
//	void drawAnchor(float ax, float ay){
//		draw(ax - leftAnchor(), ay - topAnchor());
//	}
	
	/**
	 * Compute effect of a mouseOver on this element
	 * @param mousex	x-coordinate of mouse
	 * @param mousey	y-coordinate of mouse
	 * @return			true if element was affected.
	 */
	
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
