package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;

import processing.core.PApplet;

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
	protected static IValueFactory vf = ValueFactoryFactory.getValueFactory();
	
	public IPropertyManager properties;
	
	protected float left;		// coordinates of top left corner of
	protected float top;		// the element's bounding box
	public float width;			// width of element
	public float height;		// height of element
		
	////Figure(FigurePApplet vlp, IEvaluatorContext ctx){
	//	this(vlp, null,ctx);
	//}
	
	protected Figure(FigurePApplet vlp, IPropertyManager properties, IEvaluatorContext ctx){
		this.fpa = vlp;
		this.properties = properties;
	}
	
	public int getDOI(){
		return properties.getDOI();
	}
	
	public float getLeft(){
		return left;
	}
	
	public float getMiddle(){
		return left + width/2;
	}
	
	public float getTop(){
		return top;
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
	
	private IPropertyManager getProperties(){
		return properties;
	}
	
	public void applyProperties(){
		IPropertyManager pm = getProperties();
		
		fpa.fill(pm.getFillColor());
		fpa.stroke(pm.getLineColor());
		fpa.strokeWeight(pm.getLineWidth());
		fpa.textSize(pm.getFontSize());
	}
	
	public void applyFontProperties(){
		fpa.textFont(fpa.createFont(properties.getFont(), properties.getFontSize()));
		fpa.fill(properties.getFontColor());
	}
	
	protected float getHeightProperty(){
		return properties.getHeight();
	}
	
	protected float getWidthProperty(){
		return properties.getWidth();
	}
	
	protected float getHGapProperty(){
		return properties.getHGap();
	}
	
	protected float getVGapProperty(){
		return properties.getVGap();
	}
	
	protected int getFillColorProperty(){
		return properties.getFillColor();
	}
	
	protected int getLineColorProperty(){
		return properties.getLineColor();
	}
	
	protected float getLineWidthProperty(){
		return properties.getLineWidth();
	}
	
	public float getHanchor(){
		return properties.getHanchor();
	}
	
	public float getVanchor(){
		return properties.getVanchor();
	}
	
	public float leftAnchor(){
		return (properties.getHanchor() * width);
	}
	
	public float rightAnchor(){
		return (width - properties.getHanchor() * width);
	}
	
	public float topAnchor(){
		return (properties.getVanchor() * height);
	}
	
	public float bottomAnchor(){
		return (height - properties.getVanchor() * height);
	}
	
	public boolean isClosed(){
		return properties.isShapeClosed();
	}
	
	protected boolean isConnected(){
		return properties.isShapeConnected();
	}
	
	protected boolean isCurved(){
		return properties.isShapeCurved();
	}
	
	protected float getFromAngleProperty(){
		return properties.getFromAngle();
	}
	
	protected float getToAngleProperty(){
		return properties.getToAngle();
	}
	
	protected float getInnerRadiusProperty(){
		return properties.getInnerRadius();
	}
	
	//TODO: irregular
	protected boolean getHint(String txt){
		return properties.getHint().contains(txt);
	}
	
	public String getIdProperty(){
		return properties.getId();
	}
	
	protected String getFontProperty(){
		return properties.getFont();
	}
	
	protected int getFontSizeProperty(){
		return properties.getFontSize();
	}
	
	protected int getFontColorProperty(){
		return properties.getFontColor();
	}
	
	protected float getTextAngleProperty(){
		return properties.getTextAngle();
	}
	
	public boolean isVisible(){
		return fpa.isVisible(properties.getDOI());
	}
	
	public boolean isNextVisible(){
		return fpa.isVisible(properties.getDOI() + 1);
	}
	
	public Figure getMouseOverFigure(){
		return properties.getMouseOver();
	}
	
	public boolean hasMouseOverFigure(){
		return properties.getMouseOver() != null;
	}
	
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
	 * Draw a connection from an external position (fromX, fromY) to the center (X,Y) of the current figure.
	 * At the intersection with the border of the current figure, place an arrow that is appropriately rotated.
	 * @param left		X of left corner
	 * @param top		Y of left corner
	 * @param X			X of center of current figure
	 * @param Y			Y of center of current figure
	 * @param fromX		X of center of figure from which connection is to be drawn
	 * @param fromY		Y of center of figure from which connection is to be drawn
	 * @param toArrow	the figure to be used as arrow
	 */
	public void connectFrom(float left, float top, float X, float Y, float fromX, float fromY,
			Figure toArrow){
		if(fromX == X)
			fromX += 0.00001;
        float s = (fromY - Y) / (fromX - X);
  
        float theta = PApplet.atan(s);
		if(theta < 0){
			if(fromX < X )
				theta += PApplet.PI;
		} else {
			if(fromX < X )
				theta += PApplet.PI;
		}
        float IX;
        float IY;
          
        float h2 = height/2;
        float w2 = width/2;
     
        if((- h2 <= s * w2) && (s * w2 <= h2)){
        	if(fromX > X){ // right
        		IX = X + w2; IY = Y + s*w2;
        	} else  {      // left
        		IX = X - w2; IY = Y - s*w2;
        	}
        } else {
        	if(fromY > Y){ // bottom
        		IX = X + h2/s; IY = Y + h2;
        	} else {      // top
        		IX = X - h2/s; IY = Y - h2;
        	}
        }
       
        fpa.line(left + fromX, top + fromY, left + IX, top + IY);
        
        if(toArrow != null){
        	toArrow.bbox();
        	fpa.pushMatrix();
        	fpa.translate(left + IX , top + IY);
        	fpa.rotate(PApplet.radians(-90) + theta);
        	toArrow.draw(-toArrow.width/2, 0);
        	fpa.popMatrix();
        }
	}
	
	/**
	 * Compute Y value for given X and line through (X1,Y1) and given slope
	 * @param slope
	 * @param X1
	 * @param Y1
	 * @param X
	 * @return Y value
	 */
	private float yLine(float slope, float X1, float Y1, float X){
		return slope * (X - X1) + Y1;
	}
	
	/**
	 * Compute X value for given Y and line through (X1,Y1) and given slope
	 * @param slope
	 * @param X1
	 * @param Y1
	 * @param Y
	 * @return X value
	 */
	private float xLine(float slope, float X1, float Y1, float Y){
		return X1 + (Y - Y1)/slope;
	}
	
	/**
	 * Intersects line (fromX,fromY) to (toX,toY) with this figure when placed at (X,Y)?
	 * @param X
	 * @param Y
	 * @param fromX
	 * @param fromY
	 * @param toX
	 * @param toY
	 * @return true when line and figure intersect
	 */
	public boolean intersects(float X, float Y, float fromX, float fromY, float toX, float toY){
		 float s = (fromY - toY) / (fromX - toX);
		 float h2 = height/2;
	     float w2 = width/2;
	     
	     float ly = yLine(s, fromX, fromY, X-w2);
	     if(ly > Y - h2 && ly < Y + h2)
	    	 return true;
	     
	     float ry = yLine(s, fromX, fromY, X+w2);
	     if(ry > Y - h2 && ry < Y + h2)
	    	 return true;
	     
	     float tx = xLine(s, fromX, fromY, Y - h2);
	     if(tx > X - w2 && tx < X + w2)
	    	 return true;
	     
	     float bx = xLine(s, fromX, fromY, Y + h2);
	     if(bx > X - w2 && tx < X + w2)
	    	 return true;
	     return false;	  
	}
	
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
	
	public boolean keyPressed(int key, int keyCode){
		return false;
	}
	
	
}
