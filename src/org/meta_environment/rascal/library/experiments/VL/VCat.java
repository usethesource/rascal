package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class VCat extends Compose {
	
	int vgap;
	float leftAnchor = 0;
	float rightAnchor = 0;
	private static boolean debug = true;

	VCat(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		width = 0;
		height = 0;
		leftAnchor = 0;
		rightAnchor = 0;
		vgap = getVGapProperty();
		if(debug)System.err.printf("vertical.bbox: vgap=%d\n", vgap);
		for(VELEM ve : velems){
			ve.bbox();
			leftAnchor = max(leftAnchor, ve.leftAnchor());
			rightAnchor = max(rightAnchor, ve.rightAnchor());
			height = height + ve.height;
		}
		
		width = leftAnchor + rightAnchor;
		int ngaps = (velems.length - 1);
		
		height += ngaps * vgap;
		if(debug)System.err.printf("vcat: width=%f, height=%f, leftAnchor=%f, rightAnchor=%f\n", width, height, leftAnchor, rightAnchor);
	}
	
	@Override
	void draw(){

		applyProperties();

		float bottom = top + height;

		// Draw from top to bottom
		for(int i = velems.length-1; i >= 0; i--){
			if(debug)System.err.printf("vertical.draw: i=%d, vgap=%d, bottom=%f\n", i, vgap, bottom);
			VELEM ve = velems[i];
			float h = ve.height;
			ve.draw(left + leftAnchor - ve.leftAnchor(), bottom - h);
			bottom -= h + vgap;
		}
	}
	
	@Override
	public float leftAnchor(){
		return leftAnchor;
	}
	
	@Override
	public float rightAnchor(){
		return rightAnchor;
	}
}
