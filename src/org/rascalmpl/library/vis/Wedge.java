package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * A wedge is a vee-shaped figure mostly used in pie charts. 
 * With a non-null inner radius wedge becomes a shape that can be used in ring structures.
 * 
 * Relevant properties:
 * - height			radius
 * - innerRadius
 * - fromAngle		starting angle of wedge
 * - toAngle		end angle of wedge
 * 
 * @author paulk
 *
 */

public class Wedge extends Container {
	private float fromAngle;
	private float toAngle;
	private float radius;
	private float innerRadius;
	private float leftAnchor;
	private float rightAnchor;
	private float topAnchor;
	private float bottomAnchor;
	
	private float centerX;
	private float centerY;
	
	private float IX;	// center of inner element, relative to (centerX, centerY)
	private float IY;
	private float WI;
	
	float Ax;	// start of outer arc (relative to center)
	float Ay;
	
	float Bx;	// end of outer arc
	float By;
	
	float Cx;	// start of inner arc
	float Cy;
	
	float Dx;	// end of inner arc
	float Dy;
	
	int qFrom;	// Quadrant of fromAngle;
	int qTo;	// Quadrant of toAngle;
	
	private static boolean debug = false;

	public Wedge(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, inside, ctx);
	}
	
	// Determine quadrant of angle according to numbering scheme:
	//  3  |  4
	//  2  |  1
	
	private int quadrant(double angle){
		if(debug)System.err.printf("angle 1 = %f\n", angle);
		if(angle < 0)
			return quadrant(angle +2 * PConstants.PI);
		if( angle <=  PConstants.PI/2)
			return 1;
		if( angle <=  PConstants.PI)
			return 2;
		if( angle <=  1.5 * PConstants.PI)
			return 3;
		if(angle <= 2 * PConstants.PI)
			return 4;
		return quadrant(angle - 2 * PConstants.PI);
	}

	@Override
	void bbox(){
		radius = getHeightProperty();
		float lw = getLineWidthProperty();
		innerRadius = getInnerRadiusProperty();

		fromAngle = PApplet.radians(getFromAngleProperty());
		toAngle= PApplet.radians(getToAngleProperty());
		
		if(toAngle < fromAngle)
			toAngle += 2 * PConstants.PI;
		
		
		if(inside != null)	// Compute bounding box of inside object.
			inside.bbox();
		
		float sinFrom = PApplet.sin(fromAngle);
		float cosFrom = PApplet.cos(fromAngle);
		float sinTo = PApplet.sin(toAngle);
		float cosTo = PApplet.cos(toAngle);
		
		float rsinFrom = radius * abs(sinFrom);
		float rcosFrom = radius * abs(cosFrom);
		
		float rsinTo = radius * Math.abs(sinTo);
		float rcosTo = radius * Math.abs(cosTo);
		
		Ax = radius*cosFrom;  // start of outer arc
		Ay = radius*sinFrom;
		
		Bx = radius*cosTo;    // end of outer arc
		By = radius*sinTo;
		
		Cx = innerRadius*cosTo;  // start of inner arc
		Cy = innerRadius*sinTo;
		
		Dx = innerRadius*cosFrom; // end of inner arc
		Dy = innerRadius*sinFrom;
		
		// Compute center and max (approximate) width of inner element
		
		float middleAngle = fromAngle + (toAngle-fromAngle)/2;
		
		float raux = innerRadius + 0.6f*(radius-innerRadius);
		
		IX = raux * PApplet.cos(middleAngle);
		IY = raux * PApplet.sin(middleAngle);
		if(debug)System.err.printf("AX=%f,AY=%f, BX=%f,BY=%f,CX=%f,CY=%f,DX=%f,DY=%f,IX=%f,IY=%f\n",
							        Ax,Ay,Bx,By,Cx,Cy,Dx, Dy,IX,IY);
		
		qFrom = quadrant(fromAngle);
		qTo = quadrant(toAngle);
		
		if(debug)System.err.printf("qFrom=%d, qTo=%d\n", qFrom, qTo);
		
		/*
		 * Perform a case analysis to determine the anchor values.
		 * TODO Is there a nicer way to express this?
		 */
		if(qFrom == qTo && qTo < qFrom){
			leftAnchor = rightAnchor = topAnchor = bottomAnchor = radius;
		} else {
		
			switch(qFrom){
			case 1:
				switch(qTo){
				case 1:	leftAnchor = 0; 		rightAnchor = rcosFrom; topAnchor = 0; 		bottomAnchor = rsinTo; break;
				case 2:	leftAnchor = rcosTo; 	rightAnchor = rcosFrom; topAnchor = 0; 		bottomAnchor = radius; break;
				case 3:	leftAnchor = radius;	rightAnchor = rcosFrom;	topAnchor = rsinTo;	bottomAnchor = radius; break;
				case 4:	leftAnchor = radius;	rightAnchor = Math.max(
															  rcosFrom, 
						                                      rcosTo);	topAnchor = radius;	bottomAnchor = radius; break;
				}
				break;
			case 2:
				switch(qTo){
				case 2:	leftAnchor = rcosTo;	rightAnchor = 0; 		topAnchor = 0; 		bottomAnchor = rsinFrom; break;
				case 3:	leftAnchor = radius; 	rightAnchor = 0; 		topAnchor = rsinTo; bottomAnchor = rsinFrom; break;
				case 4:	leftAnchor = radius;	rightAnchor = rcosTo;	topAnchor = radius;	bottomAnchor = rsinFrom; break;
				case 1:	leftAnchor = radius;	rightAnchor = radius;	topAnchor = radius;	bottomAnchor = Math.max(rsinFrom, rsinTo); break;
				}
				break;
			case 3:
				switch(qTo){
				case 3:	leftAnchor = rcosFrom;	rightAnchor = 0; 		topAnchor = rsinTo; bottomAnchor = 0; break;
				case 4:	leftAnchor = rcosFrom; 	rightAnchor = rcosTo; 	topAnchor = radius; bottomAnchor = 0; break;
				case 1:	leftAnchor = rcosFrom;	rightAnchor = radius;	topAnchor = radius;	bottomAnchor = rsinTo; break;
				case 2:	leftAnchor = Math.max(
									 rcosFrom, 
									 rcosTo);	rightAnchor = radius;	topAnchor = radius;	bottomAnchor = radius; break;
				}
				break;
	
			case 4:
				switch(qTo){
				case 4:	leftAnchor = 0;			rightAnchor = rcosTo; 		topAnchor = rsinFrom; 		bottomAnchor = 0; break;
				case 1:	leftAnchor = 0; 		rightAnchor = radius; 		topAnchor = rsinFrom; 		bottomAnchor = rsinTo; break;
				case 2:	leftAnchor = rcosTo;	rightAnchor = radius;		topAnchor = rsinFrom;		bottomAnchor = radius; break;
				case 3:	leftAnchor = radius;	rightAnchor = radius;		topAnchor = Math.max(rsinFrom, 
																								rsinTo);	bottomAnchor = radius; break;
				}
				break;
			}
		}
			
		leftAnchor += lw/2;
		rightAnchor += lw/2;
		topAnchor += lw/2;
		bottomAnchor += lw/2;
		
		width = leftAnchor + rightAnchor;
		height = topAnchor + bottomAnchor;
		if(debug)System.err.printf("bbox.wedge: fromAngle=%f, toAngle=%f, leftAnChor=%f, rightAnchor=%f, topAnchor=%f, bottomAnchor =%f, %f, %f)\n", 
				fromAngle, toAngle, leftAnchor, rightAnchor, topAnchor, bottomAnchor, width, height);
	}
	
	/**
	 * arcVertex: draw an arc as a bezierVertex that is part of a beginShape() ... endShape() sequence
	 * @param r				radius
	 * @param fromAngle		begin angle
	 * @param toAngle		end angle
	 */
	void arcVertex(float r, float fromAngle, float toAngle){
		if(debug)System.err.printf("arcVertex: fromAngle=%f, toAngle=%f\n", fromAngle, toAngle);
	    if(abs(toAngle - fromAngle) < PConstants.PI/2){
			float middleAngle = (toAngle - fromAngle)/2;		// fromAngle + middleAngle == middle of sector
			float middleR = abs(r / PApplet.cos(middleAngle));	// radius of control point M
			
			float Mx = centerX + middleR * PApplet.cos(fromAngle + middleAngle);	// coordinates of M
			float My = centerY + middleR * PApplet.sin(fromAngle + middleAngle);
			
			float Fx = centerX + r * PApplet.cos(fromAngle);	// coordinates of start point
			float Fy = centerY + r * PApplet.sin(fromAngle);
			
			float Tx = centerX + r * PApplet.cos(toAngle);		// coordinates of end point
			float Ty = centerY + r * PApplet.sin(toAngle);
			if(debug){
				System.err.printf("arcVertex: fromAngle=%f, middleAngle=%f, toAngle=%f, r=%f, middleR=%f\n", 
								fromAngle, middleAngle, toAngle, r, middleR);
				System.err.printf("arcVertex: Fx=%f, Fy=%f, Mx=%f, My=%f, Tx=%f, Ty=%f\n",
									Fx, Fy, Mx, My, Tx, Ty);
			}
			/*
			 * Add a bezierVertex between (Fx,Fy) and (Tx,Ty) using (Mx,My) as control point
			 */
			fpa.bezierVertex(Fx, Fy, Mx, My, Tx, Ty);
	    } else {
	    	/*
	    	 * Split when difference is larger than PI/2
	    	 */
	    	float medium = (toAngle - fromAngle)/2;
	    	arcVertex(r, fromAngle, fromAngle + medium);
	    	arcVertex(r, fromAngle + medium, toAngle);
	    }
	}
	
	@Override
	void drawContainer() {
		centerX = left + leftAnchor;
		centerY = top + topAnchor;
		
		if(debug)System.err.printf("wedge.drawContainer: %f, %f\n", centerX, centerY);
		
		applyProperties();
			
		fpa.beginShape();
		fpa.vertex(centerX + Ax, centerY + Ay);
		arcVertex(radius, fromAngle, toAngle);
		fpa.vertex(centerX + Cx, centerY + Cy);
		arcVertex(innerRadius, toAngle, fromAngle);
		fpa.vertex(centerX + Ax, centerY + Ay);
		fpa.endShape();
	}
	
	@Override 
	boolean insideFits(){
		if(debug)System.err.printf("Wedge.insideFits\n");
		return inside.height < radius - innerRadius && inside.width < WI;
	}
	
	/**
	 * If the inside  element fits, draw it.
	 */
	@Override
	void insideDraw(){
		inside.draw(centerX + IX - inside.width/2, centerY + IY -inside.height/2);
	}
	
	@Override
	public float leftAnchor(){
		return leftAnchor;
	}
	
	@Override
	public float rightAnchor(){
		return rightAnchor;
	}
	
	@Override
	public float topAnchor(){
		return topAnchor;
	}
	
	@Override
	public float bottomAnchor(){
		return bottomAnchor;
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey){
		return mousex > centerX - WI/2 && mousex < centerX + WI/2 &&
				mousey > centerY - 10  && mousey < centerY + 10; //TODO replace 10
	}
}
