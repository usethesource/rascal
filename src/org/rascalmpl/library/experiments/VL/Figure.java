package org.rascalmpl.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.values.ValueFactoryFactory;

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

public abstract class Figure implements Comparable<Figure> {
	
	protected FigurePApplet vlp;
	
	protected IValueFactory vf;
	
	protected PropertyManager properties;
	
	protected float left;         // coordinates of top left corner of
	protected float top; 			// the element's bounding box
	protected float width;		// width of element
	protected float height;		// height of element
	
	Figure(FigurePApplet vlp, IEvaluatorContext ctx){
		this(vlp, null,ValueFactoryFactory.getValueFactory().list(), ctx);
	}
	
	Figure(FigurePApplet vlp, PropertyManager inheritedProps, IList props, IEvaluatorContext ctx){
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
	
//	public void applyProperties(){
//		properties.applyProperties();
//	}
	
	private PropertyManager getProperties(){
		if(vlp.isRegisteredAsMouseOver(this) && properties.mouseOverproperties != null)
			return properties.mouseOverproperties;
		return properties;
	}
	
	public void applyProperties(){
		PropertyManager pm = getProperties();
		
		vlp.fill(pm.fillColor);
		vlp.stroke(pm.lineColor);
		vlp.strokeWeight(pm.lineWidth);
		vlp.textSize(pm.fontSize);
	}
	
	public void applyFontColorProperty(){
		PropertyManager pm = getProperties();
		
		vlp.fill(pm.fontColor);
	}
	
//	public void applyFontColorProperty(){
//		properties.applyFontColorProperty();
//	}
	
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
	
	protected float hanchor(){
		return properties.hanchor;
	}
	
	protected float vanchor(){
		return properties.vanchor;
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
	
	public Figure getInsideForMouseOver(){
		return properties.mouseOverVElem;
	}
	
	public boolean hasMouseOver(){
		return properties.mouseOver;
	}
	
	/* 
	 * Compare two VELEMs according to their surface and aspect ratio
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Figure o){
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
	 * Drawing proceeds in two stages:
	 * - determine the bounding box of the element (using bbox)
	 * - draw it (using draw)
	 * 
	 * There exist two scenarios. The standard scenario is:
	 * - use bbox without parameters just calculates bounding box
	 * - use draw with left and top argument for placement.
	 * 
	 * An alternative scenario: (used by compelxer layouts like tree and graph):
	 * - use bbox with elft and top arguments that are stored in the element
	 * - use draw without argument, reusing the stored left and top values.
	 */
	
	/**
	 * Compute the bounding box of the element. Should be called before draw since,
	 * the computed width and height are stored in the element itself.
	 */
	
	abstract void bbox();
	
	/**
	 * Compute the bounding box of the element. Should be called before draw since,
	 * the computed width and height are stored in the element itself. This varinad also
	 * stores left and top in the element
	 */
	
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		bbox();
	}
		
	/**
	 * Draw element with explicitly left, top corner of its bounding box
	 * @param left	x-coordinate of corner
	 * @param top	y-coordinate of corner
	 */
	
	abstract void draw(float left, float top);
	
	/**
	 * Draw element with left, top corner that was computed before by bbox.
	 */
	void draw(){
		draw(left, top);
	}
	
	/**
	 * Compute effect of a mouseOver on this element
	 * @param mousex	x-coordinate of mouse
	 * @param mousey	y-coordinate of mouse
	 * @return			true if element was affected.
	 */
	
	public boolean mouseOver(int mousex, int mousey){
		if((mousex > left && mousex < left + width) &&
		   (mousey > top  && mousey < top + height)){
		   //properties.setMouseOver(true);
		   vlp.registerMouse(this);
		   return true;
		}
		return false;
	}
}
