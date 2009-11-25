package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;


public class Line extends VELEM {

	public Line(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
	}
	
	@Override
	BoundingBox bbox(){
		width = getWidthProperty();
		height = max(getHeightProperty(), getHeight2Property());
		return new BoundingBox(width, height);
	}

	@Override
	void draw(float x, float y) {
		this.x = x;
		this.y = y;
		applyProperties();
		float h1 = getHeightProperty();
		float h2 = getHeight2Property();
		float w = getWidthProperty();
		float left = x - width/2;
		float bottom = y + height/2;
		//System.err.println("line: h =" + h + ", w = " + w);
		//System.err.println("line: " + left + ", " + (bottom-h) + ", " + (left+w) + ", " + bottom);
		vlp.line(left, bottom - h1, left + w, bottom - h2);
	}

}
