package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Graph extends VELEM {
	protected ArrayList<GraphNode> nodes;
	protected ArrayList<GraphEdge> edges;
	

	Graph(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);		
		this.nodes = new ArrayList<GraphNode>();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			VELEM ve = VELEMFactory.make(vlp, c, properties, ctx);
			String name = ve.getIdProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			GraphNode part = new GraphNode(name, ve);
			part.x = vlp.random(400);
			part.y = vlp.random(400);
			this.nodes.add(part);
			vlp.register(name, part);
		}
		this.edges = new ArrayList<GraphEdge>();
		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			this.edges.add((GraphEdge) VELEMFactory.make(vlp, c, properties, ctx));
		}
	}
	
	@Override
	void bbox(float left, float top) {
		this.left = left;
		this.top = top;
		for(GraphNode n : nodes)
			n.velem.bbox();
		for(int i = 0; i < 1000; i++){
			for(GraphEdge e : edges)
				e.relax();
			for(GraphNode n : nodes)
				n.relax(nodes);
			for(GraphNode n : nodes)
				n.update();
			for(GraphNode n : nodes){
				System.err.println(n.name + ": " + n.velem.left + ", " + n.velem.top);
			}
		}
	}

	@Override
	void draw() {
		applyProperties();
		for(GraphEdge e : edges)
			e.draw();
		for(GraphNode n : nodes){
			n.draw();
		}
	}

}
