package org.meta_environment.rascal.library.experiments.Processing;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.result.OverloadedFunctionResult;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

import processing.core.PApplet;
import treemap.Treemap;

public class Graph {
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final java.lang.String graphCons = "graph";
	private static final HashMap<INode, RascalGraphModel> graphs = new HashMap<INode, RascalGraphModel>();
	private static int graphCnt = 0;
	
	public static INode graph(IRelation rel, IValue drawNode, IValue drawEdge, IEvaluatorContext ctx){

		Core.checkRascalFunction(drawNode, ctx);
		Core.checkRascalFunction(drawEdge, ctx);
		RascalGraphModel myGraph = new RascalGraphModel(rel, (OverloadedFunctionResult) drawNode,  
															 (OverloadedFunctionResult) drawEdge
														);

		IValue args[] = new IValue[1];
		args[0] = values.integer(graphCnt++);
		INode nd = values.node(graphCons, args);
		graphs.put(nd, myGraph);
		//myGraph.draw();
		
		return nd;
	}
	
	private static RascalGraphModel getGraph(INode PO, IEvaluatorContext ctx){
		if(!PO.getName().equals(graphCons))
			throw RuntimeExceptionFactory.illegalArgument(ctx.getCurrentAST(), ctx.getStackTrace());
		RascalGraphModel g = graphs.get(PO);
		if(g == null)
			throw RuntimeExceptionFactory.noSuchElement(PO, ctx.getCurrentAST(), ctx.getStackTrace());
		return g;
	}
	
	public static void draw(INode PO, IEvaluatorContext ctx){
		RascalGraphModel g = getGraph(PO, ctx);
		g.draw();
	}
}

class RascalGraphModel extends PApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1180550466402865927L;
	
	int nodeCount;
	Node[] nodes = new Node[100];
	HashMap<String,Node> nodeTable = new HashMap<String,Node>();
	private OverloadedFunctionResult myDrawNode;
	private OverloadedFunctionResult myDrawEdge;
	
	int edgeCount;
	Edge[] edges = new Edge[500];
	
	RascalGraphModel(IRelation rel, OverloadedFunctionResult drawNode, OverloadedFunctionResult drawEdge){
		this.myDrawNode = drawNode;
		this.myDrawEdge = drawEdge;
		for(IValue v : rel){
			ITuple tup = (ITuple) v;
			addEdge(tup.get(0).toString(), tup.get(1).toString());
		}
	}
	
	void addEdge(String fromLabel, String toLabel){
		Node from = findNode(fromLabel);
		Node to = findNode(toLabel);
		Edge e = new Edge(from, to, myDrawEdge);
		if(edgeCount == edges.length){
			edges = (Edge[]) PApplet.expand(edges);
		}
		edges[edgeCount++] = e;		
	}
	
	Node findNode(String label){
		label = label.toLowerCase();
		Node n = nodeTable.get(label);
		if(n == null){
			return addNode(label);
		}
		return n;
	}
	
	Node addNode(String label){
		Node n = new Node(label, myDrawNode);
		if(nodeCount == nodes.length){
			nodes = (Node[]) PApplet.expand(nodes);
		}
		nodeTable.put(label, n);
		nodes[nodeCount++] = n;
		return n;
	}
	
	@Override
	public void draw(){
		for(int i = 0; i < edgeCount; i++){
			edges[i].relax();
		}
		for(int i = 0; i < nodeCount; i++){
			nodes[i].relax(nodes, nodeCount);
		}
		for(int i = 0; i < nodeCount; i++){
			nodes[i].update();
		}
		for(int i = 0; i < edgeCount; i++){
			edges[i].draw();
			
		}
		for(int i = 0; i < nodeCount; i++){
			nodes[i].draw();
		}
	}
}

//Code from Visualizing Data, First Edition, Copyright 2008 Ben Fry.
//Based on the GraphLayout example by Sun Microsystems.


class Node {
	float x, y;
	float dx, dy;
	boolean fixed;
	String label;
	int count;
	int width = 600;
	int height = 600;
	private OverloadedFunctionResult myDrawNode;

	Node(String label, OverloadedFunctionResult myDrawNode) {
		this.label = label;
		this.myDrawNode = myDrawNode;
		x = width * (float)Math.random();
		y = height * (float)Math.random();
	}

	void increment() {
		count++;
	}

	void relax(Node[] nodes, int nodeCount) {
		float ddx = 0;
		float ddy = 0;

		for (int j = 0; j < nodeCount; j++) {
			Node n = nodes[j];
			if (n != this) {
				float vx = x - n.x;
				float vy = y - n.y;
				float lensq = vx * vx + vy * vy;
				if (lensq == 0) {
					ddx += Math.random();
					ddy += Math.random();
				} else if (lensq < 100*100) {
					ddx += vx / lensq;
					ddy += vy / lensq;
				}
			}
		}
		float dlen = PApplet.mag(ddx, ddy) / 2;
		if (dlen > 0) {
			dx += ddx / dlen;
			dy += ddy / dlen;
		}
	}


	void update() {
		if (!fixed) {      
			x += PApplet.constrain(dx, -5, 5);
			y += PApplet.constrain(dy, -5, 5);

			x = PApplet.constrain(x, 0, width);
			y = PApplet.constrain(y, 0, height);
		}
		dx /= 2;
		dy /= 2;
	}
	
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();

	
	private static Type[] intargtypes = new Type[]  {types.integerType(), types.integerType(), types.stringType(), types.integerType()};

	private static IValue[] argvals = new IValue[4];
	
	public void draw(){
		argvals[0] = values.integer(Math.round(x));
		argvals[1] = values.integer(Math.round(y));
		argvals[2] = values.string(label);
		argvals[3] = values.integer(count);
		
		myDrawNode.call(intargtypes, argvals);
	}
}


//Code from Visualizing Data, First Edition, Copyright 2008 Ben Fry.
//Based on the GraphLayout example by Sun Microsystems.

class Edge {
	Node from;
	Node to;
	float len;
	int count;
	private OverloadedFunctionResult myDrawEdge;

	Edge(Node from, Node to,  OverloadedFunctionResult myDrawEdge) {
		this.from = from;
		this.to = to;
		this.len = 50;
		this.myDrawEdge = myDrawEdge;
	}

	void increment() {
		count++;
	}

	void relax() {
		float vx = to.x - from.x;
		float vy = to.y - from.y;
		float d = PApplet.mag(vx, vy);
		if (d > 0) {
			float f = (len - d) / (d * 3);
			float dx = f * vx;
			float dy = f * vy;
			to.dx += dx;
			to.dy += dy;
			from.dx -= dx;
			from.dy -= dy;
		}
	}
	
	private static final IValueFactory values = ValueFactoryFactory.getValueFactory();
	private static final TypeFactory types = TypeFactory.getInstance();
	
	private static Type[] intargtypes = new Type[]  {types.integerType(), types.integerType(), types.integerType(), types.integerType()};

	private static IValue[] argvals = new IValue[4];
	
	public void draw(){
		argvals[0] = values.integer(Math.round(from.x));
		argvals[1] = values.integer(Math.round(from.y));
		argvals[2] = values.integer(Math.round(to.x));
		argvals[3] = values.integer(Math.round(to.y));
	
		myDrawEdge.call(intargtypes, argvals);
	}
}

