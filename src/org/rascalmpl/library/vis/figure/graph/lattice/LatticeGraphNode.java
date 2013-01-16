/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.lattice;


/**
 * A GraphNode is created for each "node" constructor that occurs in the graph.
 * 
 * @author paulk
 * 
// */
//public class LatticeGraphNode {
//	private final boolean debug = false;
//	int label = -1;
//	int layer = -1;
//	String name;
//	Figure figure;
//	LinkedList<LatticeGraphNode> in;
//	LinkedList<LatticeGraphNode> out;
//	HashSet<LatticeGraphNode> reached = new HashSet<LatticeGraphNode>();
//	double x, cX[]; // Candidates of x
//	double y;
//	public int rank;
//	public int rankTop;
//	public int rankBottom;
//	public boolean mousePressed = false;
//	// private int fromX, fromY;
//
//	LatticeGraphNode(String name, Figure fig) {
//		this.name = name;
//		this.figure = fig;
//		in = new LinkedList<LatticeGraphNode>();
//		out = new LinkedList<LatticeGraphNode>();
//	}
//
//	public double figX() {
//		return x;
//	}
//
//	public double figY() {
//		return y;
//	}
//
//	void bbox() {
//		if (figure != null) {
//			figure.bbox();
//		}
//	}
//
//	double width() {
//		return figure != null ? figure.minSize.getWidth() : 0;
//	}
//
//	double height() {
//		return figure != null ? figure.minSize.getHeight() : 0;
//	}
//
//	void draw(GraphicsContext gc) {
//		if (debug) System.err.println("draw:"+this.getClass()+" "+figure);
//		if (figure != null) {
//			figure.bbox();
//			figure.draw(gc);
//			// if (mousePressed) {
//			// // System.err.println("Pressed");
//			// IFigureExecutionEnvironment fpa = figure.fpa;
//			// fpa.stroke(255, 0, 0);
//			// fpa.strokeWeight(1);
//			// // fpa.noFill();
//			// fpa.rect(fromX + left - 10, fromY + top - 10, 20, 20);
//			// fpa.stroke(0, 0, 0);
//			// }
//		}
//	}
//
//	public boolean mouseInside(double mousex, double mousey) {
//		if (figure.mouseInside(mousex, mousey)) {
//			return  true;
//		}
//		return false;
//	}
//
//	public void addIn(LatticeGraphNode n) {
//		if (!in.contains(n))
//			in.add(n);
//	}
//
//	public void addOut(LatticeGraphNode n) {
//		if (!out.contains(n))
//			out.add(n);
//	}
//	
//	public boolean isConnected(LatticeGraphNode n) {
//		return this.in.contains(n) || this.out.contains(n);
//	}
//	
//
//	public void computeFiguresAndProperties(ICallbackEnv env){
//		if(figure!=null){
//			figure.computeFiguresAndProperties(env);
//		}
//	}
//	
//
//	public void registerNames(NameResolver resolver){
//		if(figure!=null){
//			figure.registerNames(resolver);
//		}
//	}
//	
//	public void layout(){
//		if(figure!=null){
//			if (debug) System.err.println("layout:"+this.getClass()+" "+figure.minSize);
//			figure.setToMinSize();
//			figure.layout();
//		}
//	}
//	
//	
//	
//}
