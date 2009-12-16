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
	protected float springConstant2;
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
	
	protected float attract(float d){
		return (d * d) / springConstant;
	}
	
	protected float repel(float d){
		return springConstant2 / d;
	}
	
	private void printNodes(String s){
		System.err.printf("%s: ", s);
		for(GraphNode n : nodes){
			System.err.printf(n.name + ": " + n.x + ", " + n.y + " ");
		}
		System.err.println("");
	}
	
	@Override
	void bbox(int left, int top) {
		this.left = left;
		this.top = top;
		for(GraphNode n : nodes)
			n.velem.bbox();
		
		printNodes("initial");
		
		temperature = 110;
		for(int i = 1; i < 100; i++){
			
			for(GraphNode n : nodes)
				n.relax(this);
			for(GraphEdge e : edges)
				e.relax(this);
			for(GraphNode n : nodes)
				n.update(this);
			temperature--;
			printNodes("iter t = " + temperature);
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
