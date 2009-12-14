package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class GraphEdge extends VELEM {
	GraphNode from;
	GraphNode to;
	
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
		
		System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue());
	}
	
	void relax(Graph G){
		float deltax = to.x - from.x;
		float deltay = to.y - from.y;
		float dlen = PApplet.dist(from.x, from.y, to.x, to.y);
		if(dlen > 0){
			float attract = G.attract(dlen);
			float dx = (deltax / dlen) * attract;
			float dy = (deltay / dlen) * attract;
			from.dispx -= dx;
			from.dispy -= dy;
			to.dispx += dx;
			to.dispy += dy;
		} else {
			System.err.printf("edge: dlen=0 %s -> %s\n", from.name, to.name);
			from.dispx -= Math.random() * G.width;
			from.dispy -= Math.random() * G.width;
			to.dispx += Math.random() * G.height;
			to.dispy += Math.random() * G.height;
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
