package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

/**
 * Vertex: a point that is part of a shape.
 * TODO: subclass from container?
 * 
 * @author paulk
 *
 */
public class Vertex extends VELEM {
	VELEM marker;
	float deltax;
	float deltay;
	float leftAnchor;
	float rightAnchor;
	float topAnchor;
	float bottomAnchor;
	private static boolean debug = false;

	private float getIntOrReal(IValue v){
		if(v.getType().isIntegerType())
			return ((IInteger) v).intValue();
		if(v.getType().isRealType())
			return ((IReal) v).floatValue();
		return 0;
		
	}
	public Vertex(VLPApplet vlp, IValue dx, IValue dy, IEvaluatorContext ctx) {
		super(vlp, ctx);
		deltax = getIntOrReal(dx);
		deltay = getIntOrReal(dy);
	}
	
	public Vertex(VLPApplet vlp, IValue dx, IValue dy, IConstructor marker, IEvaluatorContext ctx) {
		super(vlp, ctx);
		deltax = getIntOrReal(dx);
		deltay = getIntOrReal(dy);
		if(marker != null)
			this.marker = VELEMFactory.make(vlp, marker, properties, ctx);
		if(debug)System.err.println("Point with : " + marker);
	}

	@Override
	void bbox(float left, float top){
		this.left = left;
		this.top = top;
		if(marker != null){
			//TODO is this ok?
			marker.bbox();
			if(marker.width > deltax){
				leftAnchor = deltax - marker.width;
				width = marker.width;
				rightAnchor = width - leftAnchor;
			} else {
				leftAnchor = 0;
				width = deltax + marker.rightAnchor();
				rightAnchor = width;
			}
			
			if(marker.height > deltay){
				bottomAnchor = deltay - marker.height;
				height = marker.height;
				topAnchor = height - bottomAnchor;
			} else {
				bottomAnchor = 0;
				height = deltay = marker.topAnchor();
				topAnchor = height;
			}
			
		} else {
			width = deltax;
			height = deltay;
			leftAnchor = bottomAnchor = 0;
			rightAnchor = width;
			topAnchor = height;
		}
		if(debug)System.err.printf("bbox.point: %f, %f)\n", width, height);
	}
	
	@Override
	void draw() {
		
		applyProperties();
		if(debug){
			System.err.println("Point: marker = " + marker);
			System.err.printf("Point: marker at %d, %d\n", left, top);
		}
		if(marker != null){
			marker.bbox();
			marker.draw(left-marker.width/2, top-marker.height/2);
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
	
	@Override
	public float topAnchor(){
		return topAnchor;
	}
	
	@Override
	public float bottomAnchor(){
		return bottomAnchor;
	}

}
