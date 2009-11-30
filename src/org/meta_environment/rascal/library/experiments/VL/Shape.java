package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;


public class Shape extends Compose {

	Shape(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(){
		width = 0;
		height = 0;
		for (VELEM ve : velems){
			width = max(width, ve.width);
			height = max(height, ve.height);
		}
		System.err.printf("bbox.lines: %f, %f)\n", width, height);
	}
	
	@Override
	void draw(float left, float top){
		applyProperties();
		this.left = left;
		this.top = top;
		float bottom = top + height;
		boolean  closed = isClosed();
		boolean curved = isCurved();
		
		vlp.noFill();
		vlp.beginShape();
		
		/*
		 * We present to the user a coordinate system
		 * with origin at the bottom left corner!
		 * Therefore we subtract deltay from bottom
		 */
		
		Vertex next = (Vertex)velems[0];
		float nextLeft = left + next.deltax;
		float nextTop = bottom - next.deltay;
		if(closed){
			vlp.vertex(left, bottom);
			vlp.vertex(nextLeft, nextTop);
		}
		if(curved)
			vlp.curveVertex(nextLeft, nextTop);
		
		for(VELEM ve : velems){
			next = (Vertex)ve;
			nextLeft = left + next.deltax;
			nextTop = bottom - next.deltay;
			System.err.printf("vertex(%f,%f)\n", nextLeft, nextTop);
			applyProperties();
			if(!closed)
					vlp.noFill();
			if(curved)
				vlp.curveVertex(nextLeft,nextTop);
			else
				vlp.vertex(nextLeft,nextTop);
		}
		if(curved){
			next = (Vertex)velems[velems.length-1];
			vlp.curveVertex(left + next.deltax, bottom - next.deltay);
		}
		if(closed){
			vlp.vertex(nextLeft, bottom);
			vlp.endShape(PApplet.CLOSE);
		} else 
			vlp.endShape();
		
		for(VELEM ve : velems){
			Vertex p = (Vertex) ve;
			p.draw(left + p.deltax, bottom - p.deltay);
		}
	}
}
