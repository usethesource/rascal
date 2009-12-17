package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class GraphEdge extends VELEM {
	GraphNode from;
	GraphNode to;
	float len;
	
	public GraphEdge(VLPApplet vlp, PropertyManager inheritedProps, IList props, IString fromName, IString toName, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		from = vlp.getRegistered(fromName.getValue());
		// TODO Generate exceptions for null cases
		if(from == null){
			System.err.println("No node " + fromName.getValue());
		}
		to = vlp.getRegistered(toName.getValue());
		if(to == null){
			System.err.println("No node " + toName.getValue());
		}
		this.len = 50;
		
		System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue());
	}
	
	void relax(Graph G){
		float deltax = to.x - from.x;
		float deltay = to.y - from.y;
		float dlen = PApplet.mag(deltax, deltay);
		if(dlen > 0){
			float attract = G.attract(dlen);
			float dx = (deltax / dlen) * attract;
			float dy = (deltay / dlen) * attract;
//			float f = (len - dlen) / (dlen * 3);
//			float dx = f * deltax;
//			float dy = f * deltay;
			to.dispx += dx;
			to.dispy += dy;
			from.dispx -= dx;
			from.dispy -= dy;
//			System.err.printf("edge: %s -> %s, attract=%f, deltax=%f, deltay=%f, change by %f, %f\n", from.name, to.name, attract, deltax, deltay, dx, dy);
		} else {
			System.err.printf("edge: dlen=0 %s -> %s\n", from.name, to.name);
			to.dispx -= vlp.random(G.springConstant2);
			to.dispy -= vlp.random(G.springConstant2);
			from.dispx += vlp.random(G.springConstant2);
			from.dispy += vlp.random(G.springConstant2);
		}
	}

	@Override
	void bbox(int left, int top) {
	}

	@Override
	void draw() {
		applyProperties();
		System.err.println("edge: (" + from.name + ": " + from.x + "," + from.y + ") -> (" + 
									   to.name + ": " + to.x + "," + to.y + ")");
		vlp.line(from.x, from.y, to.x, to.y);
	}

}
