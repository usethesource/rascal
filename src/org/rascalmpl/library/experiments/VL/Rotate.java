package org.rascalmpl.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Rotate extends VELEM {
	private VELEM velem;
	private float angle;
	private float leftAnchor;
	private float rightAnchor;
	private float topAnchor;
	private float bottomAnchor;
	private static boolean debug = true;
	
	Rotate(VLPApplet vlp, PropertyManager inherited, IValue rangle, IConstructor c, IEvaluatorContext ctx) {
		super(vlp, ctx);
		float a = rangle.getType().isIntegerType() ? ((IInteger) rangle).intValue()
				                                    : ((IReal) rangle).floatValue();
		angle = PApplet.radians(a);
		velem = VELEMFactory.make(vlp, c, properties, ctx);
	}

	@Override
	void bbox() {
		
		velem.bbox();
		
		float sa = abs(PApplet.sin(angle));
		float ca = abs(PApplet.cos(angle));
//		
//		width = velem.width * ca + velem.height * sa;
//		height = velem.height * ca + velem.width * sa;
//		
//		leftAnchor = velem.bottomAnchor() *sa + velem.leftAnchor()*ca;
//		rightAnchor = width - leftAnchor;
//		
//		topAnchor = velem.topAnchor()*ca + velem.leftAnchor()*sa;
//		bottomAnchor = height - topAnchor;
		
		leftAnchor = velem.leftAnchor() * ca + velem.bottomAnchor() * sa;
		rightAnchor = velem.topAnchor() * sa + velem.rightAnchor() * ca;
		width = leftAnchor + rightAnchor;
		
		topAnchor = velem.leftAnchor() * sa + velem.topAnchor() * ca;
		bottomAnchor = velem.rightAnchor() * sa + velem.bottomAnchor() * ca;
		
		height = topAnchor + bottomAnchor;
		
		if(debug)System.err.printf("rotate.bbox: width=%f (%f, %f), height=%f (%f, %f)\n", 
				   width, leftAnchor, rightAnchor, height, topAnchor, bottomAnchor);
	}

	@Override
	void draw(float left, float top) {
		vlp.pushMatrix();
		vlp.translate((left + velem.leftAnchor()), (top + velem.topAnchor()));
		//vlp.translate(-left, -top);
		vlp.rotate(angle);
		//vlp.translate(left, top);
		vlp.translate(-(left + velem.leftAnchor()), -(top + velem.topAnchor()));
		//vlp.translate(-leftAnchor, -topAnchor);
		velem.draw(-velem.leftAnchor(), -velem.topAnchor());
		//velem.draw(0,0);
		
		vlp.popMatrix();
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
}
