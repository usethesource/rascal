/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.graph.spring;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;

/**

 * Force-directed (spring) Graph layout. Given a list of nodes and edges a graph layout is computed with given size.
 * 
 * We use a spring layout approach as described in 
 * 
 * 		Fruchterman, T. M. J., & Reingold, E. M. (1991). 
 * 		Graph Drawing by Force-Directed Placement. 
 * 		Software: Practice and Experience, 21(11).
 * 
 *  Spring layout is activated by the property: hint("spring")
 * 
 * @author paulk
 * 
 */
public class SpringGraph extends Figure {
	protected ArrayList<SpringGraphNode> nodes;
	protected ArrayList<SpringGraphEdge> edges;
	private HashMap<String, SpringGraphNode> registered;
	IEvaluatorContext ctx;
	
	// Fields for force layout
	protected float springConstant;
	protected float springConstant2;
	protected int temperature;
	private static boolean debug = true;

	
	public SpringGraph(FigurePApplet fpa, IPropertyManager properties, IList nodes,
			IList edges, IEvaluatorContext ctx) {
		super(fpa, properties);
		this.nodes = new ArrayList<SpringGraphNode>();
		this.ctx = ctx;
		width = getWidthProperty();
		height = getHeightProperty();

		registered = new HashMap<String,SpringGraphNode>();
		for(IValue v : nodes){

			IConstructor c = (IConstructor) v;
			Figure ve = FigureFactory.make(fpa, c, properties, ctx);
			String name = ve.getIdProperty();

			if(name.length() == 0)
				throw RuntimeExceptionFactory.figureException("Id property should be defined", v, ctx.getCurrentAST(), ctx.getStackTrace());

			SpringGraphNode node = new SpringGraphNode(this, name, ve);
			this.nodes.add(node);
			register(name, node);
		}

		this.edges = new ArrayList<SpringGraphEdge>();
		for (IValue v : edges) {
			IConstructor c = (IConstructor) v;
			SpringGraphEdge e = FigureFactory.makeSpringGraphEdge(this, fpa, c, properties,
					ctx);
			this.edges.add(e);
			e.getFrom().addOut(e.getTo());
			e.getTo().addIn(e.getFrom());
		}

		// float connectivity = edges.length()/nodes.length();
		springConstant = // (connectivity > 1 ? 0.5f : 0.3f) *
		                 PApplet.sqrt((width * height) / nodes.length());
		if (debug)
			System.err.printf("springConstant = %f\n", springConstant);
		springConstant2 = springConstant * springConstant;
		
		System.err.println("SpringGraph created");
	}
	
	public void register(String name, SpringGraphNode nd){
		registered.put(name, nd);
	}

	public SpringGraphNode getRegistered(String name) {
		return registered.get(name);
	}
	
	private void initialPlacement(){

		
		SpringGraphNode root = null;
//		 for(SpringGraphNode n : nodes){
//		 if(n.in.isEmpty()){
//		 root = n;
//		 break;
//		 }
//		 }
//		 if(root != null){
//		 root.setX(width/2);
//		 root.setY(height/2);
//		 }
		for (SpringGraphNode n : nodes) {
			n.figure.bbox();
			if (n != root) {
				n.setX(fpa.random(n.figure.width/2,  width  - n.figure.width/2));
				n.setY(fpa.random(n.figure.height/2, height - n.figure.height/2));
			}
			
			System.err.printf("Initial: node %s, width=%f, height=%f, x=%f, y=%f\n", n.name, n.figure.width, n.figure.height, n.getX(), n.getY());
		}
	}

	protected float attract(float d) {
		return (d * d) / springConstant;
	}

	protected float repel(float d) {
		return springConstant2 / d;
	}

	@Override
	public
	void bbox() {

		initialPlacement();
			

		temperature = 50;
		for (int iter = 0; iter < 150; iter++) {
			System.err.println("iter = " + iter);

			for (SpringGraphNode n : nodes)
				n.relax();
			for (SpringGraphEdge e : edges)
				e.relax(this);
			for (SpringGraphNode n : nodes)
				n.update(this);
			if (iter % 4 == 0 && temperature > 0)
				temperature--;
		}


		// Now scale (back or up) to the desired width x height frame
//		float minx = Float.MAX_VALUE;
//		float maxx = Float.MIN_VALUE;
//		float miny = Float.MAX_VALUE;
//		float maxy = Float.MIN_VALUE;
//
//		for(SpringGraphNode n : nodes){
//			float w2 = n.width()/2;
//			float h2 = n.height()/2;
//			if(n.x - w2 < minx)
//
//				minx = n.x - w2;
//			if (n.x + w2 > maxx)
//				maxx = n.x + w2;
//
//			if (n.y - h2 < miny)
//				miny = n.y - h2;
//			if (n.y + h2 > maxy)
//				maxy = n.y + h2;
//		}
//
//		float scalex = width / (maxx - minx);
//		float scaley = height / (maxy - miny);
//
//		for (SpringGraphNode n : nodes) {
//			n.x = n.x - minx;
//			n.x *= scalex;
//			n.y = n.y - miny;
//			n.y *= scaley;
//		}
	}

	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);

		applyProperties();
		
		for (SpringGraphEdge e : edges)
			e.draw(left, top);
		
		for (SpringGraphNode n : nodes)
			n.draw(left, top);
		
	}

	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX, float centerY, boolean mouseInParent) {
		for (SpringGraphNode n : nodes) {
			if (n.mouseOver(mousex, mousey, mouseInParent))
				return true;
		}
		return super.mouseOver(mousex, mousey, centerX, centerY, mouseInParent);
	}

	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e) {
		for (SpringGraphNode n : nodes) {
			if (n.mousePressed(mousex, mousey, e))
				return true;
		}
		return super.mouseOver(mousex, mousey, false);
	}

}
