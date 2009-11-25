package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Dot extends VELEM {

	public Dot(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
	}

	@Override
	BoundingBox bbox(){
		int s = getSizeProperty();
		width = getWidthProperty() + s;
		height = getHeightProperty() + s;
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(float x, float y) {
		this.x = x;
		this.y = y;
		applyProperties();
		int s = getSizeProperty();
		//System.err.println("line: h =" + h + ", w = " + w);
		//System.err.println("line: " + left + ", " + (bottom-h) + ", " + (left+w) + ", " + bottom);
		vlp.ellipse(x- width/2, y-height/2, s, s);
	}

}
