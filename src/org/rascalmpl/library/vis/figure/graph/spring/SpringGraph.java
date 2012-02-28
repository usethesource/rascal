/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.spring;

import static org.rascalmpl.library.vis.properties.Properties.HSIZE;
import static org.rascalmpl.library.vis.properties.Properties.ID;
import static org.rascalmpl.library.vis.properties.Properties.VSIZE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.FigureMath;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.library.vis.util.vector.Vector2D;

/**

 * Force-directed (spring) Graph layout. Given a list of nodes and edges a graph layout is computed with given size.
 * 
 * We use a spring layout approach as described in 
 * 
 * 		Fruchterman, T. M. J., & Reingold, E. M. (1991). 
 * 		Graph Drawing by Force-Directed Placement. 
 * 		Software: Practice and Experience, 21(11).
 * 
 * with the extensions described here:
 * 
 * 		Arne Frick, Andreas Ludwig, Heiko Mehldau
 * 		A Fast Adaptive Layout Algorithm for Undirected Graphs
 * 		Proceedings of Graph Drawing '94, 
 * 		Volume 894 of Lecture Notes in Computer Science, 
 * 		edited by Roberto Tamassia and Ioannis Tollis, 
 * 		Springer-Verlag, 1995.
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
	
	// Fields for force layout
	protected int temperature;
	private static boolean debug = false;
	IFigureConstructionEnv fpa;

	public SpringGraph(IFigureConstructionEnv fpa, PropertyManager properties, IList nodes,	IList edges) {
		super(properties);
		this.nodes = new ArrayList<SpringGraphNode>();
		minSize.setX(prop.getReal(HSIZE));
		minSize.setY(prop.getReal(VSIZE));
		registered = new HashMap<String,SpringGraphNode>();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			Figure ve = FigureFactory.make(fpa, c, properties, null);
			String name = ve.prop.getStr(ID);

			if(name.length() == 0)
				throw RuntimeExceptionFactory.figureException("Id property should be defined", v, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());

			SpringGraphNode node = new SpringGraphNode(this, name, ve);
			this.nodes.add(node);
			register(name, node);
		}

		this.edges = new ArrayList<SpringGraphEdge>();
		for (IValue v : edges) {
			IConstructor c = (IConstructor) v;
			SpringGraphEdge e = FigureFactory.makeSpringGraphEdge(this, fpa, c, properties);
			this.edges.add(e);
			e.getFrom().addOut(e.getTo());
			e.getTo().addIn(e.getFrom());
		}
		
		int nChildren = nodes.length() + edges.length();
		int nedges = edges.length();
		this.children = new Figure[nChildren];
		
		for(int i = 0; i < nedges; i++)
			this.children[i] = this.edges.get(i);
		
		for(int i = 0; i < nodes.length(); i++)
			this.children[nedges + i] = this.nodes.get(i);		
	
		//for(Figure fig : children)
		//	fig.computeMinSize();
		
		long n = 2 * Math.round(Math.sqrt(minSize.getX() * minSize.getY()) / nodes.length());
		EDGE_LENGTH = n;
		EDGE_LENGTH_2 =  n * n;
		RAND_DISTURB = EDGE_LENGTH/4;
	}
	
	public void register(String name, SpringGraphNode nd){
		registered.put(name, nd);
	}

	public SpringGraphNode getRegistered(String name) {
		return registered.get(name);
	}
	
	
	public double EDGE_LENGTH;       		 // Set on Graph creation
	public double EDGE_LENGTH_2;             // Set on Graph creation
	public double RAND_DISTURB;              // Set on Graph creation.
	public double MIN_GLOBAL_TEMPERATURE;    // Set in computeMinSize.
	
	public double MAX_LOCAL_TEMPERATURE = 256.0;
	public double ATTRACT = 1.0;
	public double REPEL = 1.0;
	public double UPDATE_STEP = 1;
	public double OSCILLATION = 1.0;
	public double SKEW = 1.0;
	public double ROTATION = 1.0;
	public double GRAVITY = 1.0 / 16.0;

	public void setConstants(IReal C1, IReal C2, IReal C3, IReal C4,
			IReal C5, IReal C6, IReal C7, IReal C8, IReal CRand) {
		ATTRACT = C1.doubleValue();
		EDGE_LENGTH = C2.doubleValue();
		EDGE_LENGTH_2 = C3.doubleValue();
		UPDATE_STEP = C4.doubleValue();
		OSCILLATION = C5.doubleValue();
		SKEW = C6.doubleValue();
		ROTATION = C7.doubleValue();
		GRAVITY = C8.doubleValue();
		RAND_DISTURB = CRand.doubleValue();
	}

	public void printConstants() {
		System.err.println("ATTRACT                = " + ATTRACT);
		System.err.println("EDGE_LENGTH            = " + EDGE_LENGTH);
		System.err.println("EDGE_LENGTH_2          = " + EDGE_LENGTH_2);
		System.err.println("UPDATE_STEP            = " + UPDATE_STEP);
		System.err.println("OSCILLATION            = " + OSCILLATION);
		System.err.println("SKEW                   = " + SKEW);
		System.err.println("ROTATION               = " + ROTATION);
		System.err.println("GRAVITY                = " + GRAVITY);
		System.err.println("RAND_DISTURB           = " + RAND_DISTURB);
		System.err.println("MAX_LOCAL_TEMPERATURE  = " + MAX_LOCAL_TEMPERATURE);
		System.err.println("MIN_GLOBAL_TEMPERATURE = " + MIN_GLOBAL_TEMPERATURE);
	}

	
	public void initialPlacement() {
		
		for (SpringGraphNode n : nodes) {
			n.setX(FigureMath.random(n.figure.minSize.getX()/2, 500 - n.figure.minSize.getX()/2));
			n.setY(FigureMath.random(n.figure.minSize.getY()/2, 500 - n.figure.minSize.getY()/2));
			
			System.err.printf("Initial: node %s, width=%f, height=%f, x=%f, y=%f\n", n.name, n.figure.minSize.getX(), n.figure.minSize.getY(), n.getX(), n.getY());
		}
	}
	
	private void actualMinSize(){
		// Now compute the actual mininal size

		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;

		for(SpringGraphNode n : nodes){
			double w2 = n.width()/2;
			double h2 = n.height()/2;

			if(n.getX() - w2 < minX)
				minX = n.getX() - w2;
			if (n.getX() + w2 > maxX)
				maxX = n.getX() + w2;

			if (n.getY() - h2 < minY)
				minY = n.getY() - h2;
			if (n.getY() + h2 > maxY)
				maxY = n.getY() + h2;
		}

		for(SpringGraphNode n : nodes){
			n.setX(n.getX() - minX);
			n.setY(n.getY() - minY);
		}

		minSize.set(maxX - minX, maxY - minY);
		//size.set(maxX - minX, maxY - minY);
	}
	
	@Override
	public void resizeElement(Rectangle view) {
		localLocation.set(0,0);
		size.set(view.getSize().getX(), view.getSize().getY());
	}
	
	//------------------------
	
	 public static int MAX_ROUNDS = 10000;
	 
	 public Vector2D getBaryCenter(){
		 double cx = 0;
		 double cy = 0;
		 for (SpringGraphNode n : nodes){
			 cx += n.getX();
			 cy += n.getY();
		 }
		 return new Vector2D(cx / nodes.size(), cy / nodes.size());
	 }
	
	 @Override
	 public void computeMinSize(){
		 if(debug) printConstants();
		 
		 minSize.set(512, 512);
		 resizable.set(false, false);
		
		 //initialPlacement();
		 for (SpringGraphNode n : nodes){
				n.init();
			}
		 MIN_GLOBAL_TEMPERATURE = 0.005 * MAX_LOCAL_TEMPERATURE/ (1 +nodes.size());
		 int iteration = 0;
		 for(;iteration < MAX_ROUNDS && globalTemperature() > MIN_GLOBAL_TEMPERATURE; iteration++){
			 if(debug)System.err.println("\nITERATION: " + iteration + ", total temp: "+ globalTemperature());
			Collections.shuffle(nodes);
			 for (SpringGraphNode n : nodes){
				 n.update();				 
			 }
		 }
		 System.err.println("\nTerminated: " + iteration + ", total temp: "+ globalTemperature());
		 actualMinSize();
	 }
			
	 // The global temperature is the sum of all node temperatures.

	 public double globalTemperature(){
		 double result = 0;
		 for (SpringGraphNode n : nodes){
			 result += n.temperature;
		 }
		 return result;
	 }


}
