package org.meta_environment.rascal.library.experiments.VL;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class Edge extends VELEM {
	GraphNode from;
	GraphNode to;
	float len;
	
	public Edge(VLPApplet vlp, PropertyManager inheritedProps, IList props, IString fromName, IString toName, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		from = vlp.getRegistered(fromName.getValue());
		to = vlp.getRegistered(toName.getValue());
		len = 50;
		// TODO null case;
		System.err.println("edge: " + fromName.getValue() + " -> " + toName.getValue());
	}
	
	void relax(){
		float vx = to.x - from.x;
		float vy = to.y - from.y;
		float d = PApplet.mag(vx, vy);
		if(d > 0){
			float f = (len - d) / (d * 3);
			float dx = f * vx;
			float dy = f * vy;
			to.dx += dx;
			to.dy += dy;
			from.dx -= dx;
			from.dy -= dy;
		}
	}

	@Override
	void bbox() {
		// TODO Auto-generated method stub
	}

	@Override
	void draw(float x, float y) {
		applyProperties();
		vlp.line(from.x, from.y, to.y, to.y);
	}
	
	void draw() {
		applyProperties();
		System.err.println("edge: (" + from.name + ": " + from.x + "," + from.y + ") -> (" + 
									   to.name + ": " + to.x + "," + to.y + ")");
		vlp.line(from.x, from.y, to.x, to.y);
	}

}
