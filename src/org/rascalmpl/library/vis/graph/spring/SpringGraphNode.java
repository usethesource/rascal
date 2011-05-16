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
package org.rascalmpl.library.vis.graph.spring;

import java.util.LinkedList;

import org.rascalmpl.library.vis.Figure;

import org.rascalmpl.library.vis.FigureApplet;

/**
 * A SpringGraphNode is created for each "node" constructor that occurs in the graph.
 * 
 * @author paulk
 *
 */
public class SpringGraphNode {
	
	private final SpringGraph G;
	protected final String name;
	protected final Figure figure;
	private double x;
	private double y;

	protected double dispx = 0f;
	protected double dispy = 0f;
	
	protected LinkedList<SpringGraphNode> in;
	protected LinkedList<SpringGraphNode> out;
	private static boolean debug = true;
	
	SpringGraphNode(SpringGraph springGraph, String name, Figure fig){
		this.G = springGraph;
		this.name = name;
		this.figure = fig;
		in = new LinkedList<SpringGraphNode>();
		out = new LinkedList<SpringGraphNode>();
	}
	
	public void addIn(SpringGraphNode n){
		if(!in.contains(n))
			in.add(n);
	}
	
	public void addOut(SpringGraphNode n){
		if(!out.contains(n))
			out.add(n);
	}
	
	public double xdistance(SpringGraphNode other){
		double vx = getX() - other.getX();
//		return vx;
		if(vx > 0){
			return FigureApplet.max(vx - (figure.width/2 + other.figure.width/2), 0.01f);
		}
		return FigureApplet.min(vx + (figure.width/2 + other.figure.width/2), -0.01f);	
	}
	
	public double ydistance(SpringGraphNode other){
		double vy = getY() - other.getY() ;
//		return vy;
		if(vy > 0){
			return FigureApplet.max(vy - (figure.height/2 + other.figure.height/2), 0.01f);
		}
		return FigureApplet.min(vy + (figure.height/2 + other.figure.height/2), -0.01f);
	}
	
//	public double getMass(){
//		return 1.0f;
//	}
	
	private void repulsion(double vx, double vy){
		// Inline version of repel(d) = SpringCon^2/d
		
		double dlensq = vx * vx + vy * vy;
		
		if(FigureApplet.abs(dlensq) < 1){
			dlensq = dlensq < 0 ? -0.01f : 0.01f;
			double r1 = (double) Math.random();
			double r2 = (double) Math.random();
			
			vx = vx > 0 ? vx + r1 : vx - r1;
			vy = vy > 0 ? vy + r2 : vy - r2;
		}
		
		dispx += vx * G.springConstant2 / dlensq;
		dispy += vy * G.springConstant2 / dlensq;
	}
	
	public void relax(){
		
		dispx = dispy = 0;
		
		for(SpringGraphNode n : G.nodes){
			if(n != this){
				repulsion(xdistance(n), ydistance(n));
			}
		}
		
		// Consider the repulsion of the 4 walls of the surrounding frame
		repulsion(getX(), G.height/2); repulsion(G.width - getX(), G.height/2);
		repulsion(G.width/2, getY()); repulsion(G.width/2, G.height - getY());
		
		
		for(SpringGraphEdge e : G.edges){
			SpringGraphNode from = e.getFrom();
			SpringGraphNode to = e.getTo();
			if(from != this && to != this){
				double vlen = FigureApplet.dist(from.getX(), from.getY(), to.getX(), to.getY());
				double lenToFrom = FigureApplet.dist(getX(), getY(), from.getX(), from.getY());
				double lenToTo = FigureApplet.dist(getX(), getY(), to.getX(), to.getY());
				if(lenToFrom + lenToTo - vlen < 1f){
					dispx += 1;
					dispy += 1;
					from.dispx -= 1;
					from.dispy -= 1;
					to.dispx -= 1;
					to.dispy -= 1;
				}
			}
		}
		
		if(debug)System.err.printf("Node %s (%f,%f), dispx = %f, dispy =%f\n", name, getX(), getY(), dispx, dispy);
	}
	
	void update(SpringGraph G){
		double dlen = FigureApplet.mag(dispx, dispy);
		if(dlen > 0){
			if(debug)System.err.printf("update %s, dispx=%f, dispy=%f, from %f, %f\n", name, dispx, dispy, getX(), getY());
			double cdispx = FigureApplet.constrain(dispx, -G.temperature, G.temperature);
			double cdispy = FigureApplet.constrain(dispy, -G.temperature, G.temperature);
			System.err.printf("cdispx=%f, cdispy=%f\n", cdispx, cdispy);
			setX(FigureApplet.constrain (getX() + cdispx, figure.width/2, G.width-figure.width/2));
			setY(FigureApplet.constrain (getY() + cdispy, figure.height/2, G.height-figure.height/2));
			System.err.printf("Updated node %s: %f, %f\n", name, getX(), getY());
		}
	}
	
	public double figX(){
		return getX();
	}
	
	public double figY(){
		return getY();
	}
	
	void bbox(){
		if(figure != null){
			figure.bbox(Figure.AUTO_SIZE, Figure.AUTO_SIZE);
		}
	}
	
	double width(){
		return figure != null ? figure.width : 0;
	}
	
	double height(){
		return figure != null ? figure.height : 0;
	}

	void draw(double left, double top) {
		if(figure != null){
			figure.draw(getX() + left - figure.width/2, getY() + top - figure.height/2);
		}
	}

	protected void setX(double x) {
		if(x < figure.width/2 || x > G.width - figure.width/2)
			System.err.printf("ERROR: node %s, x outside boundary: %f\n", name, x);
		this.x = x;
	}

	protected double getX() {
		return x;
	}

	protected void setY(double y) {
		if(y < figure.height/2 || y > G.height - figure.height/2)
			System.err.printf("ERROR: node %s, y outside boundary: %f\n", name, y);
		this.y = y;
	}

	protected double getY() {
		return y;
	}
}
