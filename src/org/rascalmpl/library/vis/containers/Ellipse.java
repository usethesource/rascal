package org.rascalmpl.library.vis.containers;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.PropertyManager;

import processing.core.PApplet;
import processing.core.PConstants;


/**
 * 
 * Ellipse that can act as container
 *
 * @author paulk
 *
 */
public class Ellipse extends Container {

	public Ellipse(FigurePApplet fpa, PropertyManager properties, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, properties, inside, ctx);
	}
	
	@Override
	void drawContainer(){
		fpa.ellipseMode(PConstants.CORNERS);
		fpa.ellipse(left, top, left + width, top + height);
	}
	
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
	@Override
	public void connectFrom(float left, float top, float X, float Y, float fromX, float fromY,
			Figure toArrow){
		
		if(fromX == X)
			fromX += 0.00001;
        float theta = PApplet.atan((fromY - Y) / (fromX - X));
        if(theta < 0){
        	if(fromX < X )
        		theta += PApplet.PI;
        } else {
        	if(fromX < X )
        		theta += PApplet.PI;
        }
        float sint = PApplet.sin(theta);
        float cost = PApplet.cos(theta);
        float r = height * width / (4 *  PApplet.sqrt((height*height*cost*cost + width*width*sint*sint)/4));
        float IX = X + r * cost;
        float IY = Y + r * sint;
        
        fpa.line(left + fromX, top + fromY, left + IX, top + IY);
        
        if(toArrow != null){
        	toArrow.bbox();
        	fpa.pushMatrix();
        	fpa.translate(left + IX, top + IY);
        	fpa.rotate(PApplet.radians(-90) + theta);
        	toArrow.draw(-toArrow.width/2, 0);
        	fpa.popMatrix();
        }
	}

}
