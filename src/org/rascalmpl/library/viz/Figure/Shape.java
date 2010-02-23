package org.rascalmpl.library.viz.Figure;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PConstants;


/**
 * Arbitrary shape built from Vertices.
 * 
 * Relevant properties:
 * connected:	connect vertices with lines
 * closed:		make a closed shape
 * curved:		connect vertices with a spline
 * 
 * @author paulk
 *
 */
public class Shape extends Compose {
	static boolean debug = false;
	float leftAnchor;
	float rightAnchor;
	float topAnchor;
	float bottomAnchor;

	Shape(FigurePApplet vlp, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(){
		leftAnchor = rightAnchor = topAnchor = bottomAnchor = 0;

		for (Figure ve : velems){
			ve.bbox();
			leftAnchor = max(leftAnchor, ve.leftAnchor());
			rightAnchor = max(rightAnchor, ve.rightAnchor());
			topAnchor = max(topAnchor, ve.topAnchor());
			bottomAnchor = max(bottomAnchor, ve.bottomAnchor());
		}
		width = leftAnchor + rightAnchor;
		height = topAnchor + bottomAnchor;
		if(debug)System.err.printf("bbox.shape: width = %f (%f, %f), height = %f (%f, %f)\n", 
				width, leftAnchor, rightAnchor, height, topAnchor, bottomAnchor);
	}
	
	@Override
	void draw(float left, float top){
		
		this.left = left;
		this.top = top;
		
		applyProperties();
		float bottom = top + height - bottomAnchor;
		boolean closed = isClosed();
		boolean curved = isCurved();
		boolean connected = isConnected() || curved;
		
		if(connected){
			vlp.noFill();
			vlp.beginShape();
		}
		
		/*
		 * We present to the user a coordinate system
		 * with origin at the bottom left corner!
		 * Therefore we subtract deltay from bottom
		 */
		
		Vertex next = (Vertex)velems[0];
		float nextLeft = left + leftAnchor + next.deltax;
		float nextTop = bottom - next.deltay;
		if(connected && closed){
			// Add a vertex at origin
			vlp.vertex(left + leftAnchor, bottom);
			vlp.vertex(nextLeft, nextTop);
		}
		if(connected && curved)
			vlp.curveVertex(nextLeft, nextTop);
		
		for(Figure ve : velems){
			next = (Vertex)ve;
			nextLeft = left + leftAnchor + next.deltax;
			nextTop = bottom - next.deltay;
			if(debug)System.err.printf("vertex(%f,%f)\n", nextLeft, nextTop);
			applyProperties();
			if(connected){
				if(!closed)
						vlp.noFill();
				if(curved)
					vlp.curveVertex(nextLeft,nextTop);
				else
					vlp.vertex(nextLeft,nextTop);
			}
		}
		if(connected){
			if(curved){
				next = (Vertex)velems[velems.length-1];
				vlp.curveVertex(left + leftAnchor + next.deltax, bottom - next.deltay);
			}
			if(closed){
				vlp.vertex(nextLeft, bottom);
				vlp.endShape(PConstants.CLOSE);
			} else 
				vlp.endShape();
		}
		
		for(Figure ve : velems){
			Vertex p = (Vertex) ve;
			p.draw(left + leftAnchor + p.deltax, bottom - p.deltay);
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
