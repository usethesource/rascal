package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Rotate extends Figure {
	private Figure figure;
	private float angle;
	private float leftAnchor;
	private float rightAnchor;
	private float topAnchor;
	private float bottomAnchor;
	private static boolean debug = true;
	
	Rotate(FigurePApplet fpa, PropertyManager inherited, IValue rangle, IConstructor c, IEvaluatorContext ctx) {
		super(fpa, ctx);
		float a = rangle.getType().isIntegerType() ? ((IInteger) rangle).intValue()
				                                    : ((IReal) rangle).floatValue();
		angle = PApplet.radians(a);
		figure = FigureFactory.make(fpa, c, properties, ctx);
	}

	@Override
	void bbox() {
		
		figure.bbox();
		
		float sa = abs(PApplet.sin(angle));
		float ca = abs(PApplet.cos(angle));
		
		float la = figure.properties.hanchor;
		float va = figure.properties.vanchor;
		
		float w = figure.width;
		float h = figure.height;
		
		width  = h * sa + w * ca;
		height = h * ca + w * sa;
		leftAnchor = la * width;
		rightAnchor = (1-la) * width;
		
		topAnchor = va * height;
		bottomAnchor = (1-va) * height;
		
//		leftAnchor = figure.leftAnchor() * ca + figure.bottomAnchor() * sa;
//		rightAnchor = figure.topAnchor() * sa + figure.rightAnchor() * ca;
		
//		width = leftAnchor + rightAnchor;
		
//		topAnchor = figure.leftAnchor() * sa + figure.topAnchor() * ca;
//		bottomAnchor = figure.rightAnchor() * sa + figure.bottomAnchor() * ca;
		
//		height = topAnchor + bottomAnchor;
		
		if(debug)System.err.printf("rotate.bbox: width=%f (%f, %f), height=%f (%f, %f)\n", 
				   width, leftAnchor, rightAnchor, height, topAnchor, bottomAnchor);
	}

	@Override
	void draw(float left, float top) {
		if(debug)System.err.printf("rotate.draw: %f, %f\n", left, top);
		fpa.pushMatrix();
		if(debug)System.err.printf("rotate.translate: %f, %f\n", (left + figure.leftAnchor()), (top + figure.topAnchor()));
		fpa.translate((left + figure.leftAnchor()), (top + figure.topAnchor()));
		if(debug)System.err.printf("rotate.translate: %f\n",angle);
		fpa.rotate(angle);
		//if(debug)System.err.printf("rotate.translate: %f, %f\n", -(left + figure.leftAnchor()), -(top + figure.topAnchor()));
		//fpa.translate( -(left + figure.leftAnchor()), -(top + figure.topAnchor()));
		if(debug)System.err.printf("rotate.drawing at: %f, %f\n", -(left + figure.leftAnchor()), -(top + figure.topAnchor()));
		figure.draw( -(left + figure.leftAnchor()), -(top + figure.topAnchor()));
		fpa.popMatrix();
		
//		fpa.pushMatrix();
//		fpa.translate((left + figure.leftAnchor()), (top + figure.topAnchor()));
//		//vlp.translate(-left, -top);
//		fpa.rotate(angle);
//		//vlp.translate(left, top);
//		fpa.translate(-(left + figure.leftAnchor()), -(top + figure.topAnchor()));
//		//vlp.translate(-leftAnchor, -topAnchor);
//		figure.draw(-figure.leftAnchor(), -figure.topAnchor());
//		//velem.draw(0,0);
		
		
	}
	
	@Override
	public float leftAnchor(){
		return leftAnchor;
	}
	
	@Override
	public float rightAnchor(){
		return rightAnchor;
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
	public void drawFocus(){
		if(isVisible()){
			fpa.stroke(255, 0,0);
			fpa.noFill();
			fpa.rect(left, top, width, height);
		}
	}
}
