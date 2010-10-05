package org.rascalmpl.library.vis;

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

	Shape(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IList elems, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, elems, ctx);
	}
	
	@Override
	void bbox(){
		leftAnchor = rightAnchor = topAnchor = bottomAnchor = 0;

		for (Figure ve : figures){
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
			fpa.noFill();
			fpa.beginShape();
		}
		
		/*
		 * We present to the user a coordinate system
		 * with origin at the bottom left corner!
		 * Therefore we subtract deltay from bottom
		 */
		
		Vertex next = (Vertex)figures[0];
		float nextLeft = left + leftAnchor + next.deltax;
		float nextTop = bottom - next.deltay;
		if(connected && closed){
			// Add a vertex at origin
			fpa.vertex(left + leftAnchor, bottom);
			fpa.vertex(nextLeft, nextTop);
		}
		if(connected && curved)
			fpa.curveVertex(nextLeft, nextTop);
		
		for(Figure ve : figures){
			next = (Vertex)ve;
			nextLeft = left + leftAnchor + next.deltax;
			nextTop = bottom - next.deltay;
			if(debug)System.err.printf("vertex(%f,%f)\n", nextLeft, nextTop);
			applyProperties();
			if(connected){
				if(!closed)
						fpa.noFill();
				if(curved)
					fpa.curveVertex(nextLeft,nextTop);
				else
					fpa.vertex(nextLeft,nextTop);
			}
		}
		if(connected){
			if(curved){
				next = (Vertex)figures[figures.length-1];
				fpa.curveVertex(left + leftAnchor + next.deltax, bottom - next.deltay);
			}
			if(closed){
				fpa.vertex(nextLeft, bottom);
				fpa.endShape(PConstants.CLOSE);
			} else 
				fpa.endShape();
		}
		
		for(Figure ve : figures){
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
