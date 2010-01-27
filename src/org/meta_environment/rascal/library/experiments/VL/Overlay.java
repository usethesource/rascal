package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


/**
 * 
 * Overlay elements by stacking them (aligned around their anchor point).
 * 
 * @author paulk
 *
 */
public class Overlay extends Compose {
	
	private static boolean debug = true;
	float topAnchor = 0;
	float bottomAnchor = 0;
	float leftAnchor = 0;
	float rightAnchor = 0;

	Overlay(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		topAnchor = bottomAnchor = leftAnchor = rightAnchor = 0;
		
		for(VELEM ve : velems){
			ve.bbox();
			topAnchor = max(topAnchor, ve.topAnchor());
			bottomAnchor = max(bottomAnchor, ve.bottomAnchor());
			leftAnchor = max(leftAnchor, ve.leftAnchor());
			rightAnchor = max(rightAnchor, ve.rightAnchor());
		}
		width = leftAnchor + rightAnchor;
		height = topAnchor + bottomAnchor;
		if(debug)System.err.printf("overlay.bbox: width=%f, height=%f\n", width, height);
	}
	
	@Override
	void draw() {
		
		applyProperties();
		if(debug)System.err.printf("overlay.draw: left=%f, top=%f\n", left, top);
		for(VELEM ve : velems){	
			//ve.drawAnchor(left + leftAnchor, top + topAnchor);
			ve.draw(left + leftAnchor - ve.leftAnchor(), top + topAnchor - ve.topAnchor());
		}
	}

}
