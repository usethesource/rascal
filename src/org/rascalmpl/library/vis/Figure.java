package org.rascalmpl.library.vis;

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


public abstract class Figure implements Comparable<Figure> {
	
	public FigurePApplet fpa;
	protected IValueFactory vf;
	
	protected PropertyManager properties;
	
	protected float left;         // coordinates of top left corner of
	protected float top; 			// the element's bounding box
	public float width;		// width of element
	public float height;		// height of element
	
//	protected float leftDragged;  // deplacement of left due to dragging
//	protected float topDragged;	// deplacement of top due to dragging
	
	Figure(FigurePApplet vlp, IEvaluatorContext ctx){
		this(vlp, null,ctx);
	}
	
	protected Figure(FigurePApplet vlp, PropertyManager properties, IEvaluatorContext ctx){
		this.fpa = vlp;
		this.properties = properties;
		vf = ValueFactoryFactory.getValueFactory();
//		leftDragged = topDragged = 0;
	}
	
	public int getDOI(){
		return properties.doi;
	}
	
	public float getLeft(){
		return left;
	}
	
//	public float getRealLeft(){
//		return left + leftDragged;
//	}
	
	public float getMiddle(){
		return left + width/2;
	}
//	
//	public float getRealMiddle(){
//		return left + leftDragged + width/2;
//	}
	
	public float getTop(){
		return top;
	}
	
//	public float getRealTop(){
//		return top + topDragged;
//	}
	
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
//		if(vlp.isRegisteredAsFocus(this) && properties.mouseOverproperties != null)
//			return properties.mouseOverproperties;
		return properties;
	}
	
	public void applyProperties(){
		PropertyManager pm = getProperties();
		
		fpa.fill(pm.fillColor);
		fpa.stroke(pm.lineColor);
		fpa.strokeWeight(pm.lineWidth);
		fpa.textSize(pm.fontSize);
	}
	
	public void applyFontProperties(){
		fpa.textFont(fpa.createFont(properties.font, properties.fontSize));
		fpa.fill(properties.fontColor);
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
	
	protected float getLineWidthProperty(){
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
		return properties.shapeClosed;
	}
	
	protected boolean isConnected(){
		return properties.shapeConnected;
	}
	
	protected boolean isCurved(){
		return properties.shapeCurved;
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
	
	protected boolean getHint(String txt){
		return properties.hint.contains(txt);
	}
	
	public String getIdProperty(){
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
	
	public boolean isVisible(){
		return fpa.isVisible(properties.doi);
	}
	
	public boolean isNextVisible(){
		return fpa.isVisible(properties.doi + 1);
	}
	
	public boolean isContentsVisible(){
		return properties.contentsVisible;
	}
	
	public void setContentsVisible(boolean on){
		properties.contentsVisible = on;
	}
	
	public boolean isPinned(){
		return properties.pinned;
	}
	
	public Figure getMouseOverFigure(){
		return properties.mouseOverFigure;
	}
	
	public boolean hasMouseOverFigure(){
		return properties.mouseOverFigure != null;
	}
	
//	public boolean isDragged(){
//		return leftDragged != 0 || topDragged != 0;
//	}
	
	/* 
	 * Compare two Figures according to their surface and aspect ratio
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
	 * - draw it (using draw) with left and top argument for placement.
	 */
	
	/**
	 * Compute the bounding box of the element. Should be called before draw since,
	 * the computed width and height are stored in the element itself.
	 */
	
	public abstract void bbox();

	/**
	 * Draw element with explicitly left, top corner of its bounding box
	 * @param left	x-coordinate of corner
	 * @param top	y-coordinate of corner
	 */
	
	public abstract void draw(float left, float top);
	
	/**
	 * Draw focus around this figure
	 */
	public void drawFocus(){
		if(isVisible()){
			fpa.stroke(255, 0,0);
			fpa.noFill();
			fpa.rect(left, top, width, height);
		}
	}
	
	/**
	 * Draw the mouseOver figure associated with this figure (if any)
	 */
	public void drawMouseOverFigure(){
		if(isVisible() && hasMouseOverFigure()){
			Figure mo = getMouseOverFigure();
			mo.bbox();
			mo.draw(left + (width - mo.width)/2f, top + (height - mo.height)/2);
		}
	}
	
	public boolean mouseInside(int mousex, int mousey){
		boolean cond = (mousex > left  && mousex < left + width) &&
		        (mousey > top  && mousey < top + height);
		//System.err.printf("mouseInside(%d,%d), hor=%f-%f, ver=%f-%f => %s\n", mousex, mousey, left, left+width,top, top+height,cond?"true":"false");
		return cond;
	}
	
	public void drag(float mousex, float mousey){
		//System.err.println("Drag to " + mousex + ", " + mousey + ": " + this);
		//leftDragged = mousex - left;
		//topDragged = mousey - top;
	}
	
	/**
	 * Compute effect of a mouseOver on this element
	 * @param mousex	x-coordinate of mouse
	 * @param mousey	y-coordinate of mouse
	 * @return			true if element was affected.
	 */
	
	public boolean mouseOver(int mousex, int mousey){
		if(isVisible() && mouseInside(mousex, mousey)){
		   //properties.setMouseOver(true);
		   fpa.registerFocus(this);
		   return true;
		}
		return false;
	}
	
	public boolean mousePressed(int mousex, int mousey){
		if(/*isVisible() &&*/ mouseInside(mousex, mousey)){
			fpa.registerFocus(this);
//			leftDragged = leftDragged - (mousex - left);
//			topDragged = topDragged - (mousey - top);
			System.err.printf("Figure.mousePressed: %f, %f\n", left, top);
			return true;
		}
		return false;
	}
	
//	public boolean mouseDragged(int mousex, int mousey){
//		if(!isPinned() && mouseInside(mousex, mousey)){
//			//properties.setMouseOver(true);
//			fpa.registerFocus(this);
//			drag(mousex, mousey);
//			System.err.printf("Figure.mouseDragged: %f,%f\n", leftDragged, topDragged);
//			return true;
//		}
//		return false;
//	}
}
