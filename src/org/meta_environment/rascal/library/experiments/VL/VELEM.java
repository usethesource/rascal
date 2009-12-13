package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.library.experiments.VL.PropertyManager.Property;
import org.meta_environment.values.ValueFactoryFactory;


/**
 * @author paulk
 *
 */

public abstract class VELEM implements Comparable<VELEM> {
	
	protected VLPApplet vlp;
	
	protected IValueFactory vf;
	
	protected PropertyManager properties;
	
	protected float left;             // coordinates of top left corner of
	protected float top; 				// the element's bounding box
	protected float width = 0;		// width of element
	protected float height = 0;		// height picture
	
	VELEM(VLPApplet vlp, IEvaluatorContext ctx){
		this.vlp = vlp;
		vf = ValueFactoryFactory.getValueFactory();
		properties = new PropertyManager(vlp, null, vf.list(), ctx);
	}
	
	VELEM(VLPApplet vlp, PropertyManager inheritedProps, IList props, IEvaluatorContext ctx){
		this.vlp = vlp;
		properties = new PropertyManager(vlp, inheritedProps, props, ctx);
		vf = ValueFactoryFactory.getValueFactory();
	}
	
	public float max(float a, float b){
		return a > b ? a : b;
	}
	
	public void applyProperties(){
		properties.applyProperties();
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
		return properties.getInt(Property.FILL_COLOR);
	}
	
	protected int getLineColorProperty(){
		return properties.getInt(Property.LINE_COLOR);
	}
	
	protected int getLineWidthProperty(){
		return properties.getInt(Property.LINE_WIDTH);
	}
	
	protected boolean isVertical(){
		return properties.getBool(Property.VERTICAL);
	}
	
	protected boolean isHorizontal(){
		return properties.getBool(Property.HORIZONTAL);
	}
	
	protected boolean isCenterAligned(){
		return properties.getBool(Property.CENTER);
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
	
	protected boolean isClosed(){
		return properties.getBool(Property.CLOSED);
	}
	
	protected boolean isCurved(){
		return properties.getBool(Property.CURVED);
	}
	
	protected int getFromAngleProperty(){
		return properties.getInt(Property.FROM_ANGLE);
	}
	
	protected int getToAngleProperty(){
		return properties.getInt(Property.TO_ANGLE);
	}
	
	protected int getInnerRadiusProperty(){
		return properties.getInt(Property.INNER_RADIUS);
	}
	
	protected String getIdProperty(){
		return properties.getStr(Property.ID);
	}
	
	protected String getFontProperty(){
		return properties.getStr(Property.FONT);
	}
	
	protected int getFontSizeProperty(){
		return properties.getInt(Property.FONT_SIZE);
	}
	
	protected int getTextAngleProperty(){
		return properties.getInt(Property.TEXT_ANGLE);
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
	 * Draw element with given left, top corner of its bounding box
	 * @param left	x-coordinate of corner
	 * @param top	y-coordinate of corner
	 */
	abstract void draw();
		
	/**
	 * Draw element with given left, top corner of its bounding box
	 * @param left	x-coordinate of corner
	 * @param top	y-coordinate of corner
	 */
	void draw(float left, float top){
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
