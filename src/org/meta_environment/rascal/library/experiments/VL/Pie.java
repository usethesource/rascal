package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;
import processing.core.PConstants;

public class Pie extends Compose {
	float fromAngle = 0;
	float toAngle = PApplet.PI;
	float innerRadius = 50;
	float toRadius;
	float angle[];
	float gapAngle = 0;
	static private boolean debug = false;

	Pie(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
		angle = new float[elems.length()];
	}
	
	@Override
	void bbox(int left, int top) {
		this.left = left;
		this.top = top;
		fromAngle = PApplet.radians(getFromAngleProperty());
		toAngle= PApplet.radians(getToAngleProperty());
		innerRadius = getInnerRadiusProperty();
		float lw = getLineWidthProperty();
		float perimeter = 0f;
		toRadius = innerRadius;
		for(VELEM ve : velems){
			ve.bbox();
			perimeter += ve.width;
			toRadius = max(toRadius, innerRadius + ve.height);
		} 
		float hgap = getHGapProperty();
		float gaps = (velems.length - 1) * hgap;
		perimeter += gaps;
		gapAngle = (hgap/perimeter) * (toAngle - fromAngle);
		if(debug)System.err.printf("bbox: perimeter = %f\n", perimeter);
		for(int i = 0; i < velems.length; i++){
			angle[i] = ((velems[i].width)/perimeter) * (toAngle - fromAngle);
		}
		width = height = 2 * toRadius + lw;
	}

	@Override
	void draw() {
		
		applyProperties();
		vlp.ellipseMode(PConstants.CENTER);
		float a1 = fromAngle;
		float cx = left + width/2;
		float cy = top + height/2;
		for(int i = 0; i < velems.length; i++){
		
			VELEM ve = velems[i];
			ve.applyProperties();

			float a2 = a1 + angle[i];
			float sina1 = PApplet.sin(a1);
			float cosa1 = PApplet.cos(a1);
			
			float sina2 = PApplet.sin(a2);
			float cosa2 = PApplet.cos(a2);
			
			float wh = (toRadius - ve.height);
			// Outer arc and radials
			vlp.arc(cx, cy, 2*toRadius, 2*toRadius, a1, a2);
			vlp.line(cx + wh*cosa1, cy + wh*sina1, cx + toRadius*cosa1, cy + toRadius*sina1);
			vlp.line(cx + wh*cosa2, cy + wh*sina2, cx + toRadius*cosa2, cy + toRadius*sina2);
			
			// Inner arc with background fill color
			vlp.fill(255);
			//vlp.stroke(255);
			//vlp.strokeWeight(5);

			vlp.arc(cx, cy, 2*wh, 2*wh, a1, a2);
			if(debug)System.err.printf("i = %d, a1 = %f, a2 = %f, wh = n\n", i, a1, a2, wh);
			
			a1 = a2 + gapAngle;
		}
	}

}
