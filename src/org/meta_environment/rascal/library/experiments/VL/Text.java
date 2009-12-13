package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;
import processing.core.PConstants;

public class Text extends VELEM {

	private String txt;

	public Text(VLPApplet vlp, PropertyManager inheritedProps, IList props, IString text, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		this.txt = text.getValue();
		System.err.printf("Text: %s\n", txt);
	}
	
	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		vlp.textSize(getFontSizeProperty());
		height = vlp.textAscent() + vlp.textDescent();
		height += 0.3 * height;
		width = vlp.textWidth(txt);
		System.err.printf("text.bbox: font=%s, ascent=%f, descent=%f\n", vlp.getFont(), vlp.textAscent(), vlp.textDescent() );
		System.err.printf("text.bbox: txt=\"%s\", width=%f, height=%f angle =%d\n", txt, width, height, getTextAngleProperty());
		if(getTextAngleProperty() != 0){
			float angle = PApplet.radians(getTextAngleProperty());
			float sina = PApplet.sin(angle);
			float cosa = PApplet.cos(angle);
			float h1 = PApplet.abs(width * sina);
			float w1 =  PApplet.abs(width * cosa);
			float h2 =  PApplet.abs(height *  cosa);
			float w2 =  PApplet.abs(height *  sina);
			
			width = w1 + w2;
			height = h1 + h2;
			System.err.printf("bbox text: height=%f, width=%f, h1=%f h2=%f w1=%f w2=%f\n", height, width, h1, h2, w1, w2);
		}
	}
	
	@Override
	void draw() {
		
		applyProperties();
	
		System.err.printf("text.draw: %s, left=%f, top=%f, width=%f, height=%f\n", txt, left, top, width, height);
		if(height > 0 && width > 0){
			int angle = getTextAngleProperty();

			vlp.textAlign(PConstants.CENTER,PConstants.CENTER);
			if(angle != 0){
				vlp.pushMatrix();
				vlp.translate(left + width/2, top + height/2);
				vlp.rotate(PApplet.radians(angle));
				vlp.text(txt, 0, 0);
				vlp.popMatrix();
			} else {
				vlp.text(txt, left + width/2, top + height/2);
			}
		}
	}
}
