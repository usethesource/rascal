package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Graph extends VELEM {
	protected ArrayList<Part> parts;
	protected ArrayList<Edge> edges;
	

	Graph(VLPApplet vlp, HashMap<String,IValue> inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);		
		this.parts = new ArrayList<Part>();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			VELEM ve = VELEMFactory.make(vlp, c, properties, ctx);
			String name = ve.getNameProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			Part nd = new Part(name, ve);
			nd.x = vlp.random(400);
			nd.y = vlp.random(400);
			this.parts.add(nd);
			vlp.register(name, nd);
		}
		this.edges = new ArrayList<Edge>();
		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			this.edges.add(VELEMFactory.makeEdge(vlp, c, properties, ctx));
		}
	}
	
	@Override
	BoundingBox bbox() {
		for(Part n : parts)
			n.velem.bbox();
		return new BoundingBox(width, height);
	}

	@Override
	void draw(float x, float y) {
		applyProperties();
		this.x = x;
		this.y = y;
		for(Edge e : edges)
			e.relax();
		for(Part n : parts)
			n.relax(parts);
		for(Part n : parts)
			n.update();
		for(Part n : parts){
			System.err.println(n.name + ": " + n.velem.x + ", " + n.velem.y);
		}
		for(Edge e : edges)
			e.draw();
		for(Part n : parts){
			n.draw();
		}
	}

}
