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
		height = PApplet.round(vlp.textAscent() + vlp.textDescent());
		width = PApplet.round(vlp.textWidth(txt));
		System.err.printf("bbox label: font=%s, ascent=%f, descent=%f\nn", vlp.getFont(), vlp.textAscent(), vlp.textDescent() );
		System.err.printf("bbox label: txt=\"%s\", width=%d, height=%d angle =%d\n", txt, width, height, getTextAngle());
		if(getTextAngle() != 0){
			float angle = PApplet.radians(getTextAngle());
			float sa = PApplet.sin(angle);
			float h1 = PApplet.abs(width * sa);
			float w1 =  PApplet.abs(width * (1 - sa));
			float sp2a = PApplet.sin(PApplet.PI/2 - angle);
			float h2 =  PApplet.abs(height *  sp2a);
			float w2 =  PApplet.abs(height *  (1 - sp2a));
			System.err.printf("bbox label: h1=%f h2=%f w1=%f w2=%f\n", h1, h2, w1, w2);
			width = PApplet.round(w1 + w2);
			height = PApplet.round(h1 + h2);
		}
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
			int angle = getTextAngle();
			if(isRight())
				vlp.textAlign(PApplet.RIGHT);
			else if(isCenter())
				vlp.textAlign(PApplet.CENTER);
			else
				vlp.textAlign(PApplet.LEFT);
			if(angle != 0){
				vlp.pushMatrix();
				int x = left + width/2;
				int y = bottom - height/2;
				vlp.translate(x, y);
				vlp.rotate(PApplet.radians(angle));
				vlp.text(txt, 0, 0);
				vlp.popMatrix();
			} else {
				vlp.text(txt, left, bottom);
			}
		}
	}

}
