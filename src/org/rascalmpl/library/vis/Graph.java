package org.rascalmpl.library.vis;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

import processing.core.PApplet;

/**
 * Graph layout. Given a list of nodes and edges a graph layout is computed with given size.
 * We use a spring layout approach as described in 
 * 
 * 		Fruchterman, T. M. J., & Reingold, E. M. (1991). 
 * 		Graph Drawing by Force-Directed Placement. 
 * 		Software: Practice and Experience, 21(11).
 * 
 * @author paulk
 *
 */
public class Graph extends Figure {
	protected ArrayList<GraphNode> nodes;
	protected ArrayList<GraphEdge> edges;
	protected float springConstant;
	protected float springConstant2;
	protected int temperature;
	private static boolean debug = false;

	Graph(FigurePApplet fpa, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(fpa, inheritedProps, props, ctx);		
		this.nodes = new ArrayList<GraphNode>();
		width = getWidthProperty();
		height = getHeightProperty();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			Figure ve = FigureFactory.make(fpa, c, properties, ctx);
			String name = ve.getIdProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			GraphNode node = new GraphNode(this, name, ve);
			this.nodes.add(node);
			fpa.register(name, node);
		}
	
		this.edges = new ArrayList<GraphEdge>();
		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			GraphEdge e = FigureFactory.makeGraphEdge(this, fpa, c, properties, ctx);
			this.edges.add(e);
			e.from.addOut(e.to);
			e.to.addIn(e.from);
		}
		
		//float connectivity = edges.length()/nodes.length();
		springConstant = // (connectivity > 1 ? 0.5f : 0.3f) * 
		                 PApplet.sqrt((width * height)/nodes.length());
		if(debug)System.err.printf("springConstant = %f\n", springConstant);
		springConstant2 = springConstant * springConstant;
		initialPlacement();
	}
	
	
	private void initialPlacement(){
		GraphNode root = null;
//		for(GraphNode n : nodes){
//			if(n.in.isEmpty()){
//				root = n;
//				break;
//			}
//		}
//		if(root != null){
//			root.x = width/2;
//			root.y = height/2;
//		}
		for(GraphNode n : nodes){
			if(n != root){
				n.x = fpa.random(width);
				n.y = fpa.random(height);
			}
		}
	}
	
	protected float attract(float d){
		return (d * d) / springConstant;
	}
	
	protected float repel(float d){
		return springConstant2 / d;
	}
	
	@Override
	void bbox() {
	
		temperature = 50;
		for(int iter = 0; iter < 150; iter++){
		for(GraphNode n : nodes)
			n.figure.bbox();
		
			for(GraphNode n : nodes)
				n.relax();
			for(GraphEdge e : edges)
				e.relax(this);
			for(GraphNode n : nodes)
				n.update(this);
			if(iter % 4 == 0 && temperature > 0)
				temperature--;
		}
		
		// Now scale (back or up) to the desired width x height frame
		float minx = Float.MAX_VALUE;
		float maxx = Float.MIN_VALUE;
		float miny = Float.MAX_VALUE;
		float maxy = Float.MIN_VALUE;
		
		for(GraphNode n : nodes){
			float w2 = n.figure.width/2;
			float h2 = n.figure.height/2;
			if(n.x - w2 < minx)
				minx = n.x - w2;
			if(n.x + w2 > maxx)
				maxx = n.x + w2;
			
			if(n.y - h2 < miny)
				miny = n.y - h2;
			if(n.y + h2 > maxy)
				maxy = n.y + h2;
		}
		
		float scalex = width / (maxx - minx);
		float scaley = height / (maxy - miny);
		
		for(GraphNode n : nodes){
			n.x = n.x - minx; n.x *= scalex;
			n.y = n.y - miny; n.y *= scaley;
		}
	}

	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		left += leftDragged;
		top  += topDragged;
		applyProperties();
		for(GraphEdge e : edges)
			e.draw(left, top);
		for(GraphNode n : nodes){
			n.draw(left, top);
		}
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		for(GraphNode n : nodes){
			if(n.mouseOver(mousex, mousey))
				return true;
		}
		return super.mouseOver(mousex, mousey);
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey){
		for(GraphNode n : nodes){
			if(n.mousePressed(mousex, mousey))
				return true;
		}
		return super.mouseOver(mousex, mousey);
	}
}

