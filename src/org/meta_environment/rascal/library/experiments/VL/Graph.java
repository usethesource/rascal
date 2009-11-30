package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Graph extends VELEM {
	protected ArrayList<GraphNode> parts;
	protected ArrayList<Edge> edges;
	

	Graph(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);		
		this.parts = new ArrayList<GraphNode>();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			VELEM ve = VELEMFactory.make(vlp, c, properties, ctx);
			String name = ve.getNameProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			GraphNode part = new GraphNode(name, ve);
			part.x = vlp.random(400);
			part.y = vlp.random(400);
			this.parts.add(part);
			vlp.register(name, part);
		}
		this.edges = new ArrayList<Edge>();
		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			this.edges.add((Edge) VELEMFactory.make(vlp, c, properties, ctx));
		}
	}
	
	@Override
	void bbox() {
		for(GraphNode n : parts)
			n.velem.bbox();
	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		applyProperties();
		for(Edge e : edges)
			e.relax();
		for(GraphNode n : parts)
			n.relax(parts);
		for(GraphNode n : parts)
			n.update();
		for(GraphNode n : parts){
			System.err.println(n.name + ": " + n.velem.left + ", " + n.velem.top);
		}
		for(Edge e : edges)
			e.draw();
		for(GraphNode n : parts){
			n.draw();
		}
	}

}
