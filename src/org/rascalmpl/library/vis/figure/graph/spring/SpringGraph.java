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
import java.util.List;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.Animation;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
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
	private final ICallbackEnv env;
	private Animation currentAnimation;

	public SpringGraph(IFigureConstructionEnv fpa, PropertyManager properties, IList nodes,	IList edges) {
		super(properties);
		this.nodes = new ArrayList<SpringGraphNode>();
		this.env = fpa.getCallBackEnv();
		registered = new HashMap<String,SpringGraphNode>();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			Figure fig = FigureFactory.make(fpa, c, properties, null);
			String name = fig.prop.getStr(ID);

			if(name.length() == 0)
				throw RuntimeExceptionFactory.figureException("Id property should be defined", v, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());

			SpringGraphNode node = new SpringGraphNode(this, name, fig);
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
		
		// Connect nodes with zero in edges to the each other.
		
		ArrayList<SpringGraphNode> zeroIn = new ArrayList<SpringGraphNode>();
		for(SpringGraphNode n : this.nodes){
			if(n.in.size() == 0)
				zeroIn.add(n);
		}
		
		if(zeroIn.size() > 1){
			IValueFactory values = ValueFactory.getInstance();
			for(int i = 0; i < zeroIn.size(); i++){
				SpringGraphNode z = zeroIn.get(i);
				SpringGraphNode from = zeroIn.get((i + 1) % zeroIn.size());
				IString zName = values.string(z.name);
				IString fromName = values.string(from.name);
				SpringGraphEdge e = new SpringGraphEdge(this, fpa, properties, fromName, zName);
				e.setVisible(false);
				this.edges.add(e);
				z.addIn(from);
				from.addIn(z);
				
				System.err.println("Edge = " + fromName + " -> " + zName);
			}
		}
		
		int nChildren = this.nodes.size() + this.edges.size();
		int nedges = this.edges.size();
		this.children = new Figure[nChildren];
		
		for(int i = 0; i < nedges; i++)
			this.children[i] = this.edges.get(i);
		
		for(int i = 0; i < this.nodes.size(); i++)
			this.children[nedges + i] = this.nodes.get(i);		
		
		
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
	public static int MAX_ROUNDS = 2000;

	public void printValues() {
		System.err.println("#NODES                 = " + nodes.size());
		System.err.println("#EDGES                 = " + edges.size());
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
			n.setCenterX(FigureMath.random(n.figure.minSize.getX()/2, 500 - n.figure.minSize.getX()/2));
			n.setCenterY(FigureMath.random(n.figure.minSize.getY()/2, 500 - n.figure.minSize.getY()/2));
			
			System.err.printf("Initial: node %s, width=%f, height=%f, x=%f, y=%f\n", n.name, n.figure.minSize.getX(), n.figure.minSize.getY(), n.getCenterX(), n.getCenterY());
		}
	}
	
	private void scale(){

		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;

		for(SpringGraphNode n : nodes){
			double w2 = n.width()/2;
			double h2 = n.height()/2;			

			if(n.getCenterX() - w2 < minX)
				minX = n.getCenterX() - w2;
			if (n.getCenterX() + w2 > maxX)
				maxX = n.getCenterX() + w2;

			if (n.getCenterY() - h2 < minY)
				minY = n.getCenterY() - h2;
			if (n.getCenterY() + h2 > maxY)
				maxY = n.getCenterY() + h2;
		}
		double scaleX = size.getX()/(maxX - minX);
		double scaleY = size.getY()/(maxY - minY);
//		minSize.set(maxX - minX, maxY - minY);
		
		for(SpringGraphNode n : nodes){
			n.setCenterX(scaleX * (n.getCenterX() - n.width()/2 - minX) + n.width()/2 );
			n.setCenterY(scaleY * (n.getCenterY() - n.height()/2 - minY) + n.height()/2);
			n.setElementPosition();
			System.err.println(n);
		}		
	}
 
	 
	 public Vector2D getBaryCenter(){
		 double cx = 0;
		 double cy = 0;
		 for (SpringGraphNode n : nodes){
			 cx += n.getCenterX();
			 cy += n.getCenterY();
		 }
		 return new Vector2D(cx / nodes.size(), cy / nodes.size());
	 }
	
	 @Override
	 public void computeMinSize(){
		// localLocation.set(0,0);
			 double maxw = 0;
			 double maxh = 0;
			 for(SpringGraphNode nd : nodes){
				 if(nd.width() > maxw)
					 maxw = nd.width();
				 if(nd.height() > maxh)
					 maxh = nd.height();
			 }
			 minSize.set(nodes.size() * maxw, nodes.size() * maxh);
	 }
	 


			
	 
	
	@Override
	public void resizeElement(Rectangle view) {
		localLocation.set(0,0);
		 long n = Math.round(Math.sqrt(size.getX() * size.getY()) / nodes.size());
		 EDGE_LENGTH = n;
		 EDGE_LENGTH_2 =  n * n;
		 RAND_DISTURB = EDGE_LENGTH/4;
		 for (SpringGraphNode nd : nodes){
				nd.init();
			}
		 //initialPlacement();
		currentAnimation = new AnimateForces();
		 MIN_GLOBAL_TEMPERATURE = 0.005 * MAX_LOCAL_TEMPERATURE/ (1 +nodes.size());
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		
		if(currentAnimation != null){
			env.registerAnimation(currentAnimation);
		}
	}
	 // The global temperature is the sum of all node temperatures.
	class AnimateForces implements Animation{
		int iteration;
		
		public AnimateForces() {
			 iteration = 0;
			 printValues();

		}
		
		@Override
		public boolean moreFrames() {
			return iteration < MAX_ROUNDS && globalTemperature() > MIN_GLOBAL_TEMPERATURE;
		}

		@Override
		public void animate() {
			 if(debug)System.err.println("\nITERATION: " + iteration + ", total temp: "+ globalTemperature());
			Collections.shuffle(nodes);
			for (SpringGraphNode nd : nodes){
				nd.step();				 
			}
			iteration++;
			if(!moreFrames()){
				scale();
			}
		}
		
	}
	 public double globalTemperature(){
		 double result = 0;
		 for (SpringGraphNode n : nodes){
			 result += n.temperature;
		 }
		 return result;
	 }

	 @Override
	public void destroyElement(IFigureConstructionEnv env) { 
		 env.getCallBackEnv().unregisterAnimation(currentAnimation);
	 }

}
