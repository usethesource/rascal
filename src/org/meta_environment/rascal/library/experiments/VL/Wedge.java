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
	
	@Override
	void drawAnchor(float cx, float cy) {
		if(debug)System.err.printf("wedge.drawAnchor: %f, %f\n", cx, cy);
		
		applyProperties();
		vlp.ellipseMode(PConstants.CENTER);
		
		float toAngle1 = fromAngle < toAngle ? toAngle : toAngle +2 * PApplet.PI;
		
		float delta = min(0.5f * PApplet.PI, toAngle1 - fromAngle);
		
		float Ax = cx + radius*cosFrom;  // start of outer arc
		float Ay = cy + radius*sinFrom;
		
//		float Bx = cx + radius*cosTo;    // end of outer arc
//		float By = cy + radius*sinTo;
		
		float Cx = cx + innerRadius*cosTo;  // start of inner arc
		float Cy = cy + innerRadius*sinTo;
		
//		float Dx = cx + innerRadius*cosFrom; // end of inner arc
//		float Dy = cy + innerRadius*sinFrom;
		
		// Empirical values for Bezier controls
		float s;
		if(delta <= PApplet.PI/4)
			s = 1f;
		else if(delta <= PApplet.PI/2)
			s = 1.4f;
		else if(delta <= PApplet.PI)
			s = 2f;
		else if (delta <= 1.5f + PApplet.PI)
			s = 2.5f;
		else
			s = 3f;
			
		vlp.beginShape();
		vlp.vertex(Ax, Ay);
		System.err.printf("toAngle1=%f, delta=%f\n", toAngle1, delta);
		
		for(float angle = fromAngle; angle  < toAngle1; angle += delta){
			
			float next = min(angle + delta, toAngle1);
			float middle = angle + (next-angle)/2;
			System.err.printf("wedge.outer arc: angle=%f, middle=%f, next=%f\n", angle, middle, next);
			
			vlp.bezierVertex(cx + radius * PApplet.cos(angle), cy + radius * PApplet.sin(angle), 
				 		 	 cx + s*radius * PApplet.cos(middle), cy + s*radius * PApplet.sin(middle),
				 		 	 cx + radius * PApplet.cos(next), cy + radius * PApplet.sin(next));
		}
				
		vlp.vertex(Cx, Cy);
//		vlp.bezierVertex(Cx, Cy, cx + s*innerRadius * PApplet.cos(middle), cy + s*innerRadius * PApplet.sin(middle),
//					Dx, Dy);

		for(float angle = toAngle1; angle > fromAngle; angle -= delta){
			float next = max(angle - delta, fromAngle);
			float middle = angle - (angle - next)/2;
			
			System.err.printf("wedge.inner arc: angle=%f, middle=%f, next=%f\n", angle, middle, next);
			vlp.bezierVertex(cx + innerRadius * PApplet.cos(angle), cy + innerRadius * PApplet.sin(angle), 
				 		 	 cx + s*innerRadius * PApplet.cos(middle), cy + s*innerRadius * PApplet.sin(middle),
				 		 	 cx + innerRadius * PApplet.cos(next), cy + innerRadius * PApplet.sin(next));
		}
		vlp.vertex(Ax,Ay);
		vlp.endShape();
		
//		vlp.beginShape();
//		vlp.vertex(cx + innerRadius*cosFrom, cy + innerRadius*sinFrom);
//		vlp.vertex(cx + radius*cosFrom, cy + radius*sinFrom);
//		vlp.curveVertex(cx + radius*cosFrom, cy + radius*sinFrom);
//		vlp.curveVertex(cx + radius*cosFrom, cy + radius*sinFrom);
//		for(float angle = fromAngle + delta; angle < toAngle1; angle += delta)
//			vlp.curveVertex(cx + radius * PApplet.cos(angle), cy + radius * PApplet.sin(angle));
//		vlp.curveVertex(cx + radius*cosTo,   cy + radius*sinTo);
//		vlp.curveVertex(cx + radius*cosTo,   cy + radius*sinTo);
//				
//		
//		vlp.vertex(cx + innerRadius*cosTo, cy + innerRadius*sinTo);
//		vlp.curveVertex(cx + innerRadius*cosTo, cy + innerRadius*sinTo);
//		vlp.curveVertex(cx + innerRadius*cosTo, cy + innerRadius*sinTo);
//		for(float angle = toAngle1 - delta; angle > fromAngle; angle -= delta)
//			vlp.curveVertex(cx + innerRadius * PApplet.cos(angle), cy + innerRadius * PApplet.sin(angle));
//		vlp.curveVertex(cx + innerRadius*cosFrom,   cy + innerRadius*sinFrom);
//		vlp.curveVertex(cx + innerRadius*cosFrom,   cy + innerRadius*sinFrom);
//		vlp.endShape();
//		
		
		
//		// Outer arc and radials
//		System.err.printf("arc(cx=%f,cy=%f,w=%f,h=%f, fromAngle=%f,toAngle=%f)\n", cx, cy, 2*radius,2*radius, fromAngle, toAngle1);
//		vlp.arc(cx, cy, 2*radius, 2*radius, fromAngle, toAngle1);
//		vlp.line(cx + innerRadius*cosFrom, cy + innerRadius*sinFrom, cx + radius*cosFrom, cy + radius*sinFrom);
//		vlp.line(cx + innerRadius*cosTo,   cy + innerRadius*sinTo,   cx + radius*cosTo,   cy + radius*sinTo);
//		
//		// Inner arc with background fill color
//		if(innerRadius > 0){
//			vlp.fill(255);
//
//			vlp.arc(cx, cy, 2*innerRadius, 2*innerRadius, fromAngle + lwAngleCorrection, toAngle1 - lwAngleCorrection);
//		}
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
