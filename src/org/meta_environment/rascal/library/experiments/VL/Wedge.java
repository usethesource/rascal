package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;
import processing.core.PConstants;


public class Wedge extends VELEM {
	@SuppressWarnings("unused")
	private VELEM inside;
	private float fromAngle;
	private float toAngle;
	private float radius;
	private int innerRadius;
	private float leftAnchor;
	private float rightAnchor;
	private float topAnchor;
	private float bottomAnchor;
	
	float sinFrom;
	float cosFrom;
	float sinTo;
	float cosTo;
	
	private static boolean debug = true;

	public Wedge(VLPApplet vlp, PropertyManager inheritedProps, IList props, IConstructor inside, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		if(inside != null)
			this.inside = VELEMFactory.make(vlp, inside, this.properties, ctx);
	}
	
	// Determine quadrant of angle according to numbering scheme:
	//  3  |  4
	//  2  |  1
	
	private int quadrant(double angle){
		System.err.printf("angle 1 = %f\n", angle);
		//System.err.printf("angle 2 = %f\n", angle);
		if( angle <=  PApplet.PI/2)
			return 1;
		if( angle <=  PApplet.PI)
			return 2;
		if( angle <=  1.5 * PApplet.PI)
			return 3;
		return 4;
	}

	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		
		radius = getHeightProperty();
		float lw = getLineWidthProperty();
		innerRadius = getInnerRadiusProperty();

		fromAngle = PApplet.radians(getFromAngleProperty());
		toAngle= PApplet.radians(getToAngleProperty());
		
		sinFrom = PApplet.sin(fromAngle);
		cosFrom = PApplet.cos(fromAngle);
		sinTo = PApplet.sin(toAngle);
		cosTo = PApplet.cos(toAngle);
		
		float rsinFrom = radius * Math.abs(sinFrom);
		float rcosFrom = radius * Math.abs(cosFrom);
		
		float rsinTo = radius * Math.abs(sinTo);
		float rcosTo = radius * Math.abs(cosTo);
		
		int qFrom = quadrant(fromAngle);
		int qTo = quadrant(toAngle);
		
		System.err.printf("qFrom=%d, qTo=%d\n", qFrom, qTo);
		
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
	
	@Override
	void draw() {
		if(debug)System.err.printf("wedge.draw: %f, %f\n", left, top);
		drawAnchor(left + leftAnchor, top + topAnchor);
	}
	
	void arcVertex(float cx, float cy, float r, float fromAngle, float toAngle){
		System.err.printf("arcvertex: fromAngle=%f, toAngle=%f\n", fromAngle, toAngle);
	    if(abs(toAngle - fromAngle) < PApplet.PI/2){
			float middleAngle = (toAngle - fromAngle)/2;
			float middleR = r / PApplet.cos(middleAngle);
			
			float Mx = cx + middleR * PApplet.cos(fromAngle + middleAngle);
			float My = cy + middleR * PApplet.sin(fromAngle + middleAngle);
			float Fx = cx + r * PApplet.cos(fromAngle);
			float Fy = cy + r * PApplet.sin(fromAngle);
			
			float Tx = cx + r * PApplet.cos(toAngle);
			float Ty = cy + r * PApplet.sin(toAngle);
			System.err.printf("arcVertex: fromAngle=%f, middleAngle=%f, toAngle=%f, r=%f, middleR=%f\n", 
							fromAngle, middleAngle, toAngle, r, middleR);
			System.err.printf("arcVertex: Fx=%f, Fy=%f, Mx=%f, My=%f, Tx=%f, Ty=%f\n",
								Fx, Fy, Mx, My, Tx, Ty);
			vlp.bezierVertex(Fx, Fy, Mx, My, Tx, Ty);
	    } else {
	    	float medium = (toAngle - fromAngle)/2;
	    	arcVertex(cx, cy, r, fromAngle, fromAngle + medium);
	    	arcVertex(cx, cy, r, fromAngle + medium, toAngle);
	    }
	}
	
	@Override
	void drawAnchor(float cx, float cy) {
		if(debug)System.err.printf("wedge.drawAnchor: %f, %f\n", cx, cy);
		
		applyProperties();
		
		float Ax = cx + radius*cosFrom;  // start of outer arc
		float Ay = cy + radius*sinFrom;
		
//		float Bx = cx + radius*cosTo;    // end of outer arc
//		float By = cy + radius*sinTo;
		
		float Cx = cx + innerRadius*cosTo;  // start of inner arc
		float Cy = cy + innerRadius*sinTo;
		
//		float Dx = cx + innerRadius*cosFrom; // end of inner arc
//		float Dy = cy + innerRadius*sinFrom;
			
		vlp.beginShape();
		vlp.vertex(Ax, Ay);
		System.err.printf("outer arc\n");
		arcVertex(cx, cy, radius, fromAngle, toAngle);
		vlp.vertex(Cx, Cy);
		System.err.printf("inner arc\n");
		arcVertex(cx, cy, innerRadius, toAngle, fromAngle);
		vlp.vertex(Ax,Ay);
		vlp.endShape();
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
}
