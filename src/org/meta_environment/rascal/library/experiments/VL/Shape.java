package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;


public class Shape extends Compose {

	Shape(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	BoundingBox bbox(){
		width = 0;
		height = 0;
		for (VELEM ve : velems){
			BoundingBox bb = ve.bbox();
			width = max(width, bb.getWidth());
			height = max(height, bb.getHeight());
		}
		System.err.printf("bbox.lines: %f, %f)\n", width, height);
		return new BoundingBox(width, height);
	}
	
	@Override
	void draw(float x, float y){
		applyProperties();
		printProperties();
		this.x = x;
		this.y = y;
		float left = x - width/2;
		float bottom = y + height/2;
		boolean  closed = isClosed();
		boolean curved = isCurved();
		
		vlp.noFill();
		vlp.beginShape();
		
		Vertex next = (Vertex)velems.get(0);
		float nx = left + next.deltax;
		float ny = bottom - next.deltay;
		if(closed){
			vlp.vertex(left, bottom);
			vlp.vertex(nx, ny);
		}
		if(curved)
			vlp.curveVertex(nx, ny);
		
		for(VELEM ve : velems){
			next = (Vertex)ve;
			nx = left + next.deltax;
			ny = bottom - next.deltay;
			System.err.printf("vertex(%f,%f)\n", nx, ny);
			applyProperties();
			if(!closed)
					vlp.noFill();
			if(curved)
				vlp.curveVertex(nx,ny);
			else
				vlp.vertex(nx,ny);
		}
		if(curved){
			next = (Vertex)velems.get(velems.size()-1);
			vlp.curveVertex(left + next.deltax, bottom - next.deltay);
		}
		if(closed){
			vlp.vertex(nx, bottom);
			vlp.endShape(PApplet.CLOSE);
		} else 
			vlp.endShape();
		
		for(VELEM ve : velems){
			Vertex p = (Vertex) ve;
			p.draw(left + p.deltax, bottom - p.deltay);
		}
	}
}
