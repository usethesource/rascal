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
package org.rascalmpl.library.vis.graph.layered;

import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import org.rascalmpl.library.vis.Figure;

/**
 * A GraphNode is created for each "node" constructor that occurs in the graph.
 * 
 * @author paulk
 *
 */
/**
 * @author paulklint
 *
 */
public class LayeredGraphNode implements Comparable<LayeredGraphNode> {
	
	protected String name;
	protected Figure figure;
	protected float x = -1;
	protected float y = -1;
	
	private float[] xs = {-1f, -1f, -1f, -1f};
	
	protected LinkedList<LayeredGraphNode> in;
	protected LinkedList<LayeredGraphNode> out;
	protected LinkedList<LayeredGraphNode> inShadowed;
	protected LinkedList<LayeredGraphNode> outShadowed;
	private boolean shadowing = false;
//	private static boolean debug = false;
	int  label = -1;
	int layer = -1;
	int pos = -1;
	boolean marked = false;
	LayeredGraphNode root;
	LayeredGraphNode align;
	LayeredGraphNode sink;
	final int INFINITY = 1000000;
	float shift = INFINITY;
	public float layerHeight;
	
	LayeredGraphNode(String name, Figure fig){
		this.name = name;
		this.figure = fig;
		in = new LinkedList<LayeredGraphNode>();
		out = new LinkedList<LayeredGraphNode>();
		root = align = sink = this;
	}
	
	public void print(){

		System.err.println("*** " + name);
		System.err.printf("\tin    = ");
		for(LayeredGraphNode gin : in){
			System.err.printf("%s ", gin.name);
		}
		System.err.println("");
		System.err.printf("\tout   = ");
		for(LayeredGraphNode gout : out){
			System.err.printf("%s ", gout.name);
		}
		System.err.println("");
		System.err.println("\tlabel = " + label);
		System.err.println("\tlayer = " + layer);
		System.err.println("\tpos   = " + pos);
		System.err.println("\troot  = " + (root  != null ? root.name:  "null"));
		System.err.println("\talign = " + (align != null ? align.name: "null"));
		System.err.println("\tsink  = " +  (sink != null ? sink.name:  "null"));
		System.err.println("\tsink.shift  = " +  (sink != null ? sink.shift :  -1));
		System.err.println("\tthis.X= " +  x);
		System.err.println("\tthis.shift  = " +  shift);
	}
	
	/* Elementary operations on nodes */
	
	public boolean isVirtual(){
		return figure == null;
	}
	
	public void addIn(LayeredGraphNode n){
		if(!in.contains(n))
			in.add(n);
	}
	
	public void addOut(LayeredGraphNode n){
		if(!out.contains(n))
			out.add(n);
	}
	
	public boolean isAbove(LayeredGraphNode g){
		return layer < g.layer;
	}
	
	public boolean isBelow(LayeredGraphNode g){
		return layer > g.layer;
	}
	
	public void delIn(LayeredGraphNode n){
		if(in.contains(n))
			in.remove(n);
	}
	
	public void delOut(LayeredGraphNode n){
		if(out.contains(n))
			out.remove(n);
	}
	
	/* Methods for ordering and cycle removal */
	
	/**
	 * Create a shadow graph for the purpose of cycle removal.
	 * Add a copy of in and out to inShadowed, resp., outShadowed.
	 * These are used during removeCycles and are null before and after
	 * removeCycles has been called.
	 */
	public void addShadowConnections(){
		shadowing = true;
		inShadowed = new LinkedList<LayeredGraphNode>();
		outShadowed = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode i : in)
			inShadowed.add(i);
		for(LayeredGraphNode o : out)
			outShadowed.add(o);
	}
	
	/**
	 * Remove the copies of in and out.
	 */
	public void delShadowConnections(){
		inShadowed = outShadowed = null;
		shadowing = false;
	}

	/**
	 * Disconnect this node from the graph during cycle removal
	 */
	public void disconnect(){
		for(LayeredGraphNode g : inShadowed){
			g.outShadowed.remove(this);
		}
		for(LayeredGraphNode g : outShadowed){
			g.inShadowed.remove(this);
		}
	}
	
	public int maxLabel(){
		return maxLabel(outShadowed);
	}
	
	private int maxLabel(LinkedList<LayeredGraphNode> a){
		int m = -1;
		for(LayeredGraphNode g : a){
			if(g.label > m)
				m = g.label;
		}
		return m;
	}
	
	/**
	 * @return is this node a sink, i.e. has no outgoing edges?
	 * Note: be called during and after shadowing
	 */
	public boolean isSink(){
		return (shadowing ? outShadowed : out).size() == 0;
	}
	
	/**
	 * @return is this node a source, i.e. has no incoming edges?
	 * Note: may be called during and after shadowing.
	 */
	public boolean isSource(){
		return (shadowing ? inShadowed : in).size() == 0;
	}
	
	/**
	 * @return difference between number of outgoing and ingoing edges
	 * Note: may ONLY be called during shadowing
	 */
	public int getOutInDiff(){
		return outShadowed.size() - inShadowed.size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(LayeredGraphNode other){
		return shadowing ? compare(inShadowed, other.inShadowed) : compare(in, other.in);
	}
	
	/**
	 * @param S
	 * @param T
	 * @return -1 (S < T)
	 *          0 (S == T)
	 *          1 (S > T)
	 */
	private int compare(LinkedList<LayeredGraphNode> S, LinkedList<LayeredGraphNode> T){
		if(S.size() == 0 && T.size() == 0)
			return 0;
		if(S.size() == 0 && T.size() != 0)
			return -1;
		if(S.size() != 0 && T.size() == 0)
			return 1;
		
		// S.size() != 0 && T.size() != 0
		int maxS = maxLabel(S);
		int maxT = maxLabel(T);

		if(maxS < 0)
			return 1;
		if(maxT < 0)
			return -1;

		if(maxS < maxT)
			return -1;
		if(maxS > maxT)
			return 1;

		LinkedList<LayeredGraphNode> SOut = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode g : S){
			if(g.label != maxS)
				SOut.add(g);
		}

		LinkedList<LayeredGraphNode> TOut = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode g : T){
			if(g.label != maxT)
				TOut.add(g);
		}
		return compare(SOut, TOut);
	}
	/* Methods for layering */
	

	public LayeredGraphNode lowestIn(){
		LayeredGraphNode low = null;
		for(LayeredGraphNode g : in){
			if(low == null || low.layer < g.layer)
				low = g;
		}
		return low;
	}
	
	public LayeredGraphNode highestOut(){
		LayeredGraphNode high = null;
		for(LayeredGraphNode g : out){
			if(high == null || g.layer < high.layer)
				high = g;
		}
		return high;
	}
	

	public boolean AllInLabelled(){
		for(LayeredGraphNode g : in){
			System.err.println("AllInLabelled: " + g.name + " label = " + g.label);
			if(g.label < 0)
				return false;
		}
		return true;
	}
	
	public boolean AllOutAssignedToLayers(){
		System.err.printf("AllOutAssignedToLayers %s\n", name);
		for(LayeredGraphNode g : out){
			System.err.println("\tconsidering " + g.name + " with layer " + g.layer + " and label " + g.label);
			if(g.layer < 0){
				System.err.printf("AllOutAssignedToLayers %s => false\n", name);
				return false;
			}
		}
		System.err.printf("AllOutAssignedToLayers %s => true\n", name);
		return true;
	}
	
	public boolean AllOutAssignedToLayers(int layer){
		System.err.printf("AllOutAssignedToLayers %s, layer=%d", name, layer);
		for(LayeredGraphNode g : shadowing ? outShadowed : out){
			System.err.println("\tconsidering " + g.name + " with layer " + g.layer + " and label " + g.label);
			if(g.layer < 0 ||  (g.layer >= layer)){
				System.err.printf("AllOutAssignedToLayers %s => false\n", name);
				return false;
			}
		}
		System.err.printf("AllOutAssignedToLayers %s => true\n", name);
		return true;
	}
	
	/* Methods for horizontal ordering */
	
	/**
	 * Get an ordered list of all nodes in layer that are directly connected to this node.
	 * @param layer	a horizontal layer of the graph
	 * @return	ordered list of nodes in layer directly connected to this node
	 */
	public LinkedList<LayeredGraphNode> getConnectedNeighbours(LinkedList<LayeredGraphNode> layer){
		LinkedList<LayeredGraphNode> connected = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode g : layer){
			if(in.contains(g) || out.contains(g))
				connected.add(g);
		}
		return connected;
	}
	
	/* Methods for horizontal placement */
	
	/**
	 * Clear the data for the horizontal alignment computation.
	 * Note that the values in xs[0] ... xs[1] are preserved
	 */
	public void clearHorizontal(){
		root = align = sink = this;
		x = -1;
		shift = INFINITY;
	}
	
	/**
	 * Compute the average median of the four horizontal values that have been computed
	 * for four alignment directions
	 */
	public void averageHorizontal(){
	
		Arrays.sort(xs);
		x = (xs[1] + xs[2])/2;
		//x = (xs[0] + xs[1])/2;
	}
	
	/**
	 * Get x value for given alignment.
	 * @param dir	a given alignment direction
	 * @return The x value for that direction
	 */
	public float getX(Direction dir){
		switch(dir){
		case TOP_LEFT: return xs[0];
		case TOP_RIGHT: return xs[1];
		case BOTTOM_LEFT: return xs[2];
		case BOTTOM_RIGHT: return xs[3];
		}
		return -1;
	}
	
	/**
	 * Set x value for given alignment
	 * @param dir	a given alignment direction
	 * @param x		the x value for that direction
	 */
	public void setX(Direction dir, float x){
		System.err.println("setX: " + name + " => " + x);
		switch(dir){
		case TOP_LEFT: this.x = xs[0] = x; return;
		case TOP_RIGHT: this.x = xs[1] = x; return;
		case BOTTOM_LEFT: this.x = xs[2] = x; return;
		case BOTTOM_RIGHT: this.x = xs[3] = x; return;
		} 
	}
	
	/* Standard figure elements and operations */

	
	public float figX(){
		return x;
	}
	
	public float figY(){
		return y;
	}
	
	void bbox(){
		if(figure != null){
			figure.bbox();
		}
	}
	
	float width(){
		return figure != null ? figure.width : 0;
	}
	
	float height(){
		return figure != null ? figure.height : 0;
	}

	void draw(float left, float top) {
		if(figure != null){
			figure.bbox();
			figure.draw(x + left - figure.width/2, y + top - figure.height/2);
		}
	}
	
	public boolean mouseOver(int mousex, int mousey, boolean mouseInParent){
		if(figure != null)
			return figure.mouseOver(mousex, mousey, mouseInParent);
		return false;
	}
	
	public boolean mousePressed(int mousex, int mousey, MouseEvent e){
		if(figure != null && figure.mousePressed(mousex, mousey, e))
			return true;
		return false;
	}
}
