package org.rascalmpl.library.vis.interaction;

import org.eclipse.imp.pdb.facts.IString;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;
import processing.core.PConstants;

public class StrControl extends Figure {
	
	private String name;
	
	String currentInput = new String();
	
	private static boolean debug = true;
	private float topAnchor = 0;
	private float bottomAnchor = 0;

	public StrControl(FigurePApplet fpa, IPropertyManager properties, IString name, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.name = name.getValue();
	}
	
	@Override
	public
	void bbox(){
		applyFontProperties();
		topAnchor = fpa.textAscent() ;
		bottomAnchor = fpa.textDescent();
		
		height = getHeightProperty();
		width = getWidthProperty();
		if(debug){
			System.err.printf("strControl.bbox: font=%s, ascent=%f, descent=%f\n", fpa.getFont(), fpa.textAscent(), fpa.textDescent() );
			System.err.printf("strControl.bbox: txt=\"%s\", width=%f, height=%f angle =%f\n", currentInput, width, height, getTextAngleProperty());
		}
		if(getTextAngleProperty() != 0){
			float angle = PApplet.radians(getTextAngleProperty());
			float sina = PApplet.sin(angle);
			float cosa = PApplet.cos(angle);
			float h1 = abs(width * sina);
			float w1 = abs(width * cosa);
			float h2 = abs(height *  cosa);
			float w2 = abs(height *  sina);
			
			width = w1 + w2;
			height = h1 + h2;
			//TODO adjust anchors
			
			if(debug)System.err.printf("bbox strControl: height=%f, width=%f, h1=%f h2=%f w1=%f w2=%f\n", height, width, h1, h2, w1, w2);
		}
	}
	
	@Override
	public
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		
		applyProperties();
		applyFontProperties();
	
		if(debug)System.err.printf("strControl.draw: %s, font=%s, left=%f, top=%f, width=%f, height=%f\n", currentInput, fpa.getFont(), left, top, width, height);
		if(height > 0 && width > 0){
			float angle = getTextAngleProperty();

			fpa.textAlign(PConstants.CENTER,PConstants.CENTER);
			if(angle != 0){
				fpa.pushMatrix();
				fpa.translate(left + width/2, top + height/2);
				fpa.rotate(PApplet.radians(angle));
				fpa.text(currentInput, 0, 0);
				fpa.popMatrix();
			} else {
				fpa.text(currentInput, left + width/2, top + height/2);
//				vlp.rectMode(PConstants.CORNERS);
//				vlp.text(txt, left, top, left+width, top+height);
			}
		}
	}
	
	@Override
	public float topAnchor(){
		return topAnchor;
	}
	
	@Override
	public float bottomAnchor(){
		return bottomAnchor;
	}
	@Override
	public boolean keyPressed(int key, int keyCode) {
		System.err.println("strControl, key = " + key);
		if (key == PApplet.ENTER || key == PApplet.RETURN) {
			currentInput = currentInput + key;
		} else if (key == PApplet.BACKSPACE && currentInput.length() > 0) {
			currentInput = currentInput.substring(0, currentInput.length() - 1);
		} else {
			currentInput = currentInput + Character.toString((char) key);
		}
		fpa.setStrControl(name, currentInput);
		System.err.println("currentInput = " + currentInput);
		return true;
	}
	
}
