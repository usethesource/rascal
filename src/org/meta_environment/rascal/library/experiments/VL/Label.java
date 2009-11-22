package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Label extends VELEM {

	public Label(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
	}
	
	@Override
	BoundingBox bbox(){
		String txt = getText();
		height = PApplet.round(vlp.textAscent() - vlp.textDescent());
		width = PApplet.round(vlp.textWidth(txt));
		return new BoundingBox(width, height);
	}

	@Override
	void draw(int l, int b) {
		applyProperties();
		String txt = getText();
		left = l;
		bottom = b;
		System.err.println("label: " + txt + ", width = " + width + ", height = " + height);
		if(height > 0 && width > 0){
			vlp.text(txt, left, bottom);
		}
	}

}
