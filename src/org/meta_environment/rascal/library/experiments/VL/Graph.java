package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

import processing.core.PApplet;

public class Graph extends VELEM {
	protected ArrayList<GraphNode> nodes;
	protected ArrayList<GraphEdge> edges;
	private float springConstant;
	private float springConstant2;
	protected int temperature;
	

	Graph(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);		
		this.nodes = new ArrayList<GraphNode>();
		width = getWidthProperty();
		height = getHeightProperty();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			VELEM ve = VELEMFactory.make(vlp, c, properties, ctx);
			String name = ve.getIdProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			GraphNode node = new GraphNode(name, ve);
			node.x = vlp.random(width);
			node.y = vlp.random(height);
			this.nodes.add(node);
			vlp.register(name, node);
		}
		this.edges = new ArrayList<GraphEdge>();
		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			this.edges.add((GraphEdge) VELEMFactory.make(vlp, c, properties, ctx));
		}
		springConstant = PApplet.sqrt((width * height)/nodes.length());
		springConstant2 = springConstant * springConstant;
	}
	
	protected float attract(float z){
		return (z * z) / springConstant;
	}
	
	protected float repel(float z){
		return - springConstant2 / z;
	}
	
	@Override
	void bbox(int left, int top) {
		this.left = left;
		this.top = top;
		for(GraphNode n : nodes)
			n.velem.bbox();
		
		temperature = (int) max(width, height);
		for(int i = 0; i < 1000; i++){
			for(GraphEdge e : edges)
				e.relax(this);
			for(GraphNode n : nodes)
				n.relax(this);
			for(GraphNode n : nodes)
				n.update(this);
			temperature--;
		}
		for(GraphNode n : nodes){
			System.err.println(n.name + ": " + n.x + ", " + n.y);
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
