package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Ellipse extends VELEM {

	public Ellipse(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
	}

	@Override
	BoundingBox bbox(){
		width = getWidthProperty();
		height = getHeightProperty();
		System.err.printf("bbox.ellipse: %f, %f)\n", width, height);
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(float x, float y) {
		System.err.printf("ellipse.draw: %f, %f\n", x, y);
		this.x = x;
		this.y = y;
		applyProperties();
		vlp.ellipse(x, y, width/2, height/2);
	}

}
