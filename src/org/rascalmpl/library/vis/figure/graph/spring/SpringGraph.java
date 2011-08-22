///*******************************************************************************
// * Copyright (c) 2009-2011 CWI
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
//
// *   * Paul Klint - Paul.Klint@cwi.nl - CWI
//*******************************************************************************/
//package org.rascalmpl.library.vis.figure.graph.spring;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Vector;
//
//import org.eclipse.imp.pdb.facts.IConstructor;
//import org.eclipse.imp.pdb.facts.IList;
//import org.eclipse.imp.pdb.facts.IValue;
//import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
//import org.rascalmpl.library.vis.figure.Figure;
//import org.rascalmpl.library.vis.figure.FigureFactory;
//import org.rascalmpl.library.vis.graphics.GraphicsContext;
//import org.rascalmpl.library.vis.properties.PropertyManager;
//import org.rascalmpl.library.vis.swt.ICallbackEnv;
//import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
//import org.rascalmpl.library.vis.util.FigureMath;
//import org.rascalmpl.library.vis.util.NameResolver;
//import org.rascalmpl.library.vis.util.vector.Coordinate;
//
///**
//
// * Force-directed (spring) Graph layout. Given a list of nodes and edges a graph layout is computed with given size.
// * 
// * We use a spring layout approach as described in 
// * 
// * 		Fruchterman, T. M. J., & Reingold, E. M. (1991). 
// * 		Graph Drawing by Force-Directed Placement. 
// * 		Software: Practice and Experience, 21(11).
// * 
// *  Spring layout is activated by the property: hint("spring")
// * 
// * @author paulk
// * 
// */
//public class SpringGraph extends Figure {
//	protected ArrayList<SpringGraphNode> nodes;
//	protected ArrayList<SpringGraphEdge> edges;
//	private HashMap<String, SpringGraphNode> registered;
//	
//	// Fields for force layout
//	protected double springConstant;
//	protected double springConstant2;
//	protected int temperature;
//	private static boolean debug = false;
//	IFigureConstructionEnv fpa;
//
//	
//	public SpringGraph(IFigureConstructionEnv fpa, PropertyManager properties, IList nodes,
//			IList edges) {
//		super(properties);
//		this.nodes = new ArrayList<SpringGraphNode>();
//		minSize.setWidth(getWidthProperty());
//		minSize.setHeight(getHeightProperty());
//		registered = new HashMap<String,SpringGraphNode>();
//		for(IValue v : nodes){
//
//			IConstructor c = (IConstructor) v;
//			Figure ve = FigureFactory.make(fpa, c, properties, null);
//			String name = ve.getIdProperty();
//
//			if(name.length() == 0)
//				throw RuntimeExceptionFactory.figureException("Id property should be defined", v, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
//
//			SpringGraphNode node = new SpringGraphNode(this, name, ve);
//			this.nodes.add(node);
//			register(name, node);
//		}
//
//		this.edges = new ArrayList<SpringGraphEdge>();
//		for (IValue v : edges) {
//			IConstructor c = (IConstructor) v;
//			SpringGraphEdge e = FigureFactory.makeSpringGraphEdge(this, fpa, c, properties);
//			this.edges.add(e);
//			e.getFrom().addOut(e.getTo());
//			e.getTo().addIn(e.getFrom());
//		}
//
//		// double connectivity = edges.length()/nodes.length();
//		springConstant = // (connectivity > 1 ? 0.5f : 0.3f) *
//		                 FigureMath.sqrt((minSize.getWidth() * minSize.getHeight()) / nodes.length());
//		if (debug)
//			System.err.printf("springConstant = %f\n", springConstant);
//		springConstant2 = springConstant * springConstant;
//		
//		System.err.println("SpringGraph created");
//	}
//	
//	public void register(String name, SpringGraphNode nd){
//		registered.put(name, nd);
//	}
//
//	public SpringGraphNode getRegistered(String name) {
//		return registered.get(name);
//	}
//	
//	private void initialPlacement(){
//
//		
//		SpringGraphNode root = null;
////		 for(SpringGraphNode n : nodes){
////		 if(n.in.isEmpty()){
////		 root = n;
////		 break;
////		 }
////		 }
////		 if(root != null){
////		 root.setX(width/2);
////		 root.setY(height/2);
////		 }
//		for (SpringGraphNode n : nodes) {
//			n.figure.bbox();
//			if (n != root) {
//				n.setX(FigureMath.random(n.figure.minSize.getWidth()/2,  minSize.getWidth()  - n.figure.minSize.getWidth()/2));
//				n.setY(FigureMath.random(n.figure.minSize.getHeight()/2, minSize.getHeight() - n.figure.minSize.getHeight()/2));
//			}
//			
//			System.err.printf("Initial: node %s, width=%f, height=%f, x=%f, y=%f\n", n.name, n.figure.minSize.getWidth(), n.figure.minSize.getHeight(), n.getX(), n.getY());
//		}
//	}
//
//	protected double attract(double d) {
//		return (d * d) / springConstant;
//	}
//
//	protected double repel(double d) {
//		return springConstant2 / d;
//	}
//
//	@Override
//	public
//	void bbox() {
//
//		initialPlacement();
//			
//
//		temperature = 50;
//		for (int iter = 0; iter < 150; iter++) {
//			System.err.println("iter = " + iter);
//
//			for (SpringGraphNode n : nodes)
//				n.relax();
//			for (SpringGraphEdge e : edges)
//				e.relax(this);
//			for (SpringGraphNode n : nodes)
//				n.update(this);
//			if (iter % 4 == 0 && temperature > 0)
//				temperature--;
//		}
//
//
//		// Now scale (back or up) to the desired width x height frame
////		double minx = Double.MAX_VALUE;
////		double maxx = Double.MIN_VALUE;
////		double miny = Double.MAX_VALUE;
////		double maxy = Double.MIN_VALUE;
////
////		for(SpringGraphNode n : nodes){
////			double w2 = n.width()/2;
////			double h2 = n.height()/2;
////			if(n.x - w2 < minx)
////
////				minx = n.x - w2;
////			if (n.x + w2 > maxx)
////				maxx = n.x + w2;
////
////			if (n.y - h2 < miny)
////				miny = n.y - h2;
////			if (n.y + h2 > maxy)
////				maxy = n.y + h2;
////		}
////
////		double scalex = width / (maxx - minx);
////		double scaley = height / (maxy - miny);
////
////		for (SpringGraphNode n : nodes) {
////			n.x = n.x - minx;
////			n.x *= scalex;
////			n.y = n.y - miny;
////			n.y *= scaley;
////		}
//		setNonResizable();
//		super.bbox();
//	}
//
//	@Override
//	public
//	void draw(GraphicsContext gc) {
//
//		applyProperties(gc);
//		
//		for (SpringGraphEdge e : edges)
//			e.draw(gc);
//		
//		for (SpringGraphNode n : nodes)
//			n.draw(gc);
//	}
//	
//	
//	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
//		if(!mouseInside(c.getX(), c.getY())) return false;
//		boolean found = false;
//		for(int i = nodes.size()-1 ; i >= 0 ; i--){
//			if(nodes.get(i).figure != null && nodes.get(i).figure.getFiguresUnderMouse(c, result)){
//				found=true;
//				break;
//			}
//		}
//		if(!found){
//			for(int i = nodes.size()-1 ; i >= 0 ; i--){
//				if(edges.get(i).getFiguresUnderMouse(c, result)){
//					break;
//				}
//			}
//		}
//		addMeAsFigureUnderMouse(result);
//		return true;
//	}
//	
//
//	public void computeFiguresAndProperties(ICallbackEnv env){
//		super.computeFiguresAndProperties(env);
//		for(SpringGraphNode node : nodes){
//			node.computeFiguresAndProperties(env);
//		}
//		for(SpringGraphEdge edge : edges){
//			edge.computeFiguresAndProperties(env);
//		}
//	}
//	
//
//	public void registerNames(NameResolver resolver){
//		super.registerNames(resolver);
//		for(SpringGraphNode node : nodes){
//			node.registerNames(resolver);
//		}
//		for(SpringGraphEdge edge : edges){
//			edge.registerNames(resolver);
//		}
//	}
//
//	@Override
//	public void layout() {
//		size.set(minSize);
//		for(SpringGraphNode node : nodes){
//			node.layout();
//		}
//		for(SpringGraphEdge edge : edges){
//			edge.layout();
//		}
//	}
//
//}
