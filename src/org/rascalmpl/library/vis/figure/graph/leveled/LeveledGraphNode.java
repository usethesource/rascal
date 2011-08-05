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
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.leveled;

import java.util.Arrays;
import java.util.LinkedList;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.util.NameResolver;

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
public class LeveledGraphNode implements Comparable<LeveledGraphNode> {
	
	protected String name;
	protected Figure figure;
	protected double x = -1;
	protected double y = -1;
	
	private double[] xs = {-1f, -1f, -1f, -1f};
	
	protected LinkedList<LeveledGraphNode> in;
	protected LinkedList<LeveledGraphNode> out;
	protected LinkedList<LeveledGraphNode> inShadowed;
	protected LinkedList<LeveledGraphNode> outShadowed;
	private boolean shadowing = false;
//	private static boolean debug = false;
	int  label = -1;
	int layer = -1;
	int pos = -1;
	boolean marked = false;
	LeveledGraphNode root;
	LeveledGraphNode align;
	LeveledGraphNode sink;
	final int INFINITY = 1000000;
	double shift = INFINITY;
	public double layerHeight;
	public double blockWidth;
	

	LeveledGraphNode(String name, Figure fig){
		this.name = name;
		this.figure = fig;
		in = new LinkedList<LeveledGraphNode>();
		out = new LinkedList<LeveledGraphNode>();
		root = align = sink = this;
	}
	
	public void reset() {
		in = new LinkedList<LeveledGraphNode>();
		out = new LinkedList<LeveledGraphNode>();
		delShadowConnections();
		// root = align = sink = this;
	}
	
	public void print(){

		System.err.println("*** " + name);
		System.err.printf("\tin    = ");
		for(LeveledGraphNode gin : in){
			System.err.printf("%s ", gin.name);
		}
		System.err.println("");
		System.err.printf("\tout   = ");
		for(LeveledGraphNode gout : out){
			System.err.printf("%s ", gout.name);
		}
		System.err.println("");
		System.err.println("\tlabel = " + label);
		System.err.println("\tlayer = " + layer);
		System.err.println("\tpos   = " + pos);
		System.err.println("\tlayerHeight   = " + layerHeight);
		System.err.println("\troot  = " + (root  != null ? root.name:  "null"));
		System.err.println("\talign = " + (align != null ? align.name: "null"));
		System.err.println("\tsink  = " +  (sink != null ? sink.name:  "null"));
		System.err.println("\tsink.shift  = " +  (sink != null ? sink.shift :  -1));
		System.err.println("\tthis.X= " +  x);
		System.err.println("\tthis.Y= " +  y);
		System.err.println("\tblockWidth = " + blockWidth);
		System.err.println("\tthis.shift  = " +  shift);
	}
	
	/* Elementary operations on nodes */
	
	public boolean isVirtual(){
		return figure == null;
	}
	
	public void addIn(LeveledGraphNode n){
		if(!in.contains(n))
			in.add(n);
	}
	
	public void addOut(LeveledGraphNode n){
		if(!out.contains(n))
			out.add(n);
	}
	
	public boolean isAbove(LeveledGraphNode g){
		return layer < g.layer;
	}
	
	public boolean isBelow(LeveledGraphNode g){
		return layer > g.layer;
	}
	
	public void delIn(LeveledGraphNode n){
		if(in.contains(n))
			in.remove(n);
	}
	
	public void delOut(LeveledGraphNode n){
		if(out.contains(n))
			out.remove(n);
	}
	
	public boolean hasVirtualOutTo(LeveledGraphNode other){
		String vname = "_" + other.name + "[";
		for(LeveledGraphNode g : out){
			if(g.isVirtual() && g.name.contains(vname))
				return true;
		}
		return false;
	}
	
	public int degree(){
		return in.size() + out.size();
	}
	
	public void exchangeWidthAndHeight(){
		if(figure != null){
			double tmp = figure.minSize.getWidth(); 
			figure.minSize.setWidth(figure.minSize.getHeight()); 
			figure.minSize.setHeight(tmp);
		}
	}
	
	public double baryCenter(LinkedList<LeveledGraphNode> above,LinkedList<LeveledGraphNode> below){
		int sum = 0;
		LinkedList<LeveledGraphNode> aboveG = getAllConnectedNeighbours(above);
		LinkedList<LeveledGraphNode> belowG = getAllConnectedNeighbours(below);
		for(LeveledGraphNode ag : aboveG){
			sum += ag.x;
		}
		for(LeveledGraphNode bg : belowG){
			sum += bg.x;
		}
		int degree = aboveG.size() + belowG.size();
		return degree > 0 ? sum/degree : 0;
	}
	
	public double median(LinkedList<LeveledGraphNode> above){
		LinkedList<LeveledGraphNode> aboveG = getAllConnectedNeighbours(above);
		int nAbove = aboveG.size();
		return nAbove > 0 ? aboveG.get(nAbove/2).x : 0;
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
		inShadowed = new LinkedList<LeveledGraphNode>();
		outShadowed = new LinkedList<LeveledGraphNode>();
		for(LeveledGraphNode i : in) {
			inShadowed.add(i);
		}
		for(LeveledGraphNode o : out) {
			outShadowed.add(o);
		}
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
		for(LeveledGraphNode g : inShadowed){
			g.outShadowed.remove(this);
		}
		for(LeveledGraphNode g : outShadowed){
			g.inShadowed.remove(this);
		}
	}
	
	public int maxLabel(){
		return maxLabel(outShadowed);
	}
	
	private int maxLabel(LinkedList<LeveledGraphNode> a){
		int m = -1;
		for(LeveledGraphNode g : a){
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
	public int compareTo(LeveledGraphNode other){
		return shadowing ? compare(inShadowed, other.inShadowed) : compare(in, other.in);
	}
	
	/**
	 * @param S
	 * @param T
	 * @return -1 (S < T)
	 *          0 (S == T)
	 *          1 (S > T)
	 */
	private int compare(LinkedList<LeveledGraphNode> S, LinkedList<LeveledGraphNode> T){
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

		LinkedList<LeveledGraphNode> SOut = new LinkedList<LeveledGraphNode>();
		for(LeveledGraphNode g : S){
			if(g.label != maxS)
				SOut.add(g);
		}

		LinkedList<LeveledGraphNode> TOut = new LinkedList<LeveledGraphNode>();
		for(LeveledGraphNode g : T){
			if(g.label != maxT)
				TOut.add(g);
		}
		return compare(SOut, TOut);
	}
	/* Methods for layering */

	public LeveledGraphNode lowestIn(){
		LeveledGraphNode low = null;
		for(LeveledGraphNode g : in){
			if(low == null || low.layer < g.layer)
				low = g;
		}
		return low;
	}
	
	public LeveledGraphNode highestOut(){
		LeveledGraphNode high = null;
		for(LeveledGraphNode g : out){
			if(high == null || g.layer < high.layer)
				high = g;
		}
		return high;
	}
	

	public boolean AllInLabelled(){
		for(LeveledGraphNode g : in){
			//System.err.println("AllInLabelled: " + g.name + " label = " + g.label);
			if(g.label < 0)
				return false;
		}
		return true;
	}
	
	public boolean AllOutAssignedToLayers(){
		//System.err.printf("AllOutAssignedToLayers %s\n", name);
		for(LeveledGraphNode g : out)
		{
			//System.err.println("\tconsidering " + g.name + " with layer " + g.layer + " and label " + g.label);
			if(g.layer < 0){
				//System.err.printf("AllOutAssignedToLayers %s => false\n", name);
				return false;
			}
		}
		//System.err.printf("AllOutAssignedToLayers %s => true\n", name);
		return true;
	}
	
	public boolean AllOutAssignedToLayers(int layer){
		//System.err.printf("AllOutAssignedToLayers %s, layer=%d", name, layer);
		for(LeveledGraphNode g : shadowing ? outShadowed : out)
		   {
			//System.err.println("\tconsidering " + g.name + " with layer " + g.layer + " and label " + g.label);
			if(g.layer < 0 ||  (g.layer >= layer)){
				//System.err.printf("AllOutAssignedToLayers %s => false\n", name);
				return false;
			}
		}
		//System.err.printf("AllOutAssignedToLayers %s => true\n", name);
		return true;
	}
	
	/* Methods for horizontal ordering */
	
	/**
	 * Get an ordered list of all nodes in layer that are directly connected to this node.
	 * @param layer	a horizontal layer of the graph
	 * @return	ordered list of nodes in layer directly connected to this node
	 */
	public LinkedList<LeveledGraphNode> getAllConnectedNeighbours(LinkedList<LeveledGraphNode> layer){
		LinkedList<LeveledGraphNode> connected = new LinkedList<LeveledGraphNode>();
		for(LeveledGraphNode g : layer)
		    {
			if(in.contains(g) || out.contains(g))
				connected.add(g);
		}
		return connected;
	}
	
//	/**
//	 * @param layer
//	 * @param k
//	 * @return list of leftmost consecutive neighbors of this node that occur after index k in layer
//	 */
//	public LinkedList<LayeredGraphNode> getAllConnectedNeighboursAfter(LinkedList<LayeredGraphNode> layer, int k){
//		LinkedList<LayeredGraphNode> connected = new LinkedList<LayeredGraphNode>();
//		//boolean inSeq = false;
//		for(int i = k + 1; i < layer.size(); i++){
//			LayeredGraphNode g = layer.get(i);
//			if(in.contains(g) || out.contains(g)){
//				//inSeq = true;
//				connected.add(g);
//			}
//			//else if(inSeq) return connected;
//		}
//		return connected;
//	}
	
//	/**
//	 * @param layer
//	 * @param k
//	 * @return list of rightmost consecutive neighbors of this node that occur before index k in layer
//	 */
//	public LinkedList<LayeredGraphNode> getAllConnectedNeighboursBefore(LinkedList<LayeredGraphNode> layer, int k){
//		LinkedList<LayeredGraphNode> connected = new LinkedList<LayeredGraphNode>();
//		//boolean inSeq = false;
//		for(int i = k - 1; i >= 0 ; i--){
//			LayeredGraphNode g = layer.get(i);
//			if(in.contains(g) || out.contains(g)){
//				//inSeq = true;
//				connected.addFirst(g);
//			}
//			//else if(inSeq) return connected;
//		}
//		return connected;
//	}
	
	public int rightMostConnectedNeighbour(){
		int maxIn = -1;
		for(LeveledGraphNode g : in)
		    {
			if(g.pos > maxIn)
				maxIn = g.pos;
		}
		return maxIn;
	}
	
	public boolean incidentToInnerSegment(LinkedList<LeveledGraphNode> other){
		int minIn = INFINITY;
		int maxIn = -1;
		for(LeveledGraphNode g : in)
		    {
			if(g.pos < minIn)
				minIn = g.pos;
			if(g.pos > maxIn)
				maxIn = g.pos;
		}
		for(LeveledGraphNode g1 : other)
		    {
			if(g1.isVirtual()){
				LeveledGraphNode g2 = g1.out.get(0);
				if(g2.isVirtual())
					if(g1.pos < maxIn && g2.pos > pos || g1.pos > minIn && g2.pos < pos)
						return true;
			}
		}
		return false;
		
	}
	
//	public LayeredGraphNode virtualOutNeighbour(){
//		for(LayeredGraphNode g : out)
//			if(g.isVirtual())
//				return g;
//		return null;
//	}
	
	
	
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
	}
	
	/**
	 * Get x value for given alignment.
	 * @param dir	a given alignment direction
	 * @return The x value for that direction
	 */
	public double getX(Direction dir){
		return xs[Direction.ord(dir)];
	}
	
	/**
	 * Set x value for given alignment
	 * @param dir	a given alignment direction
	 * @param x		the x value for that direction
	 */
	public void setX(Direction dir, double x){
		this.x = xs[Direction.ord(dir)] = x;
	}
	
	public void shiftX(double shift[]){
		for(Direction dir : Direction.dirs){
			int k = Direction.ord(dir);
			xs[k] += shift[k];
		}
	}
	
	/* Standard figure elements and operations */

	
	public double figX(){
		return x;
	}
	
	public double figY(){
		return y;
	}
	
	void bbox(){
		if(figure != null){
			figure.bbox();
			blockWidth = figure.minSize.getWidth();
		}
	}
	
	double width(){
		return figure != null ? figure.minSize.getWidth() : 0;
	}
	
	double height(){
		return figure != null ? figure.minSize.getHeight() : 0;
	}
	
	String getLayer(){
		return figure != null ? figure.getLayerProperty() : "";
	}
	

	void draw( GraphicsContext gc) {
		if(figure != null){
			figure.bbox();
			figure.draw(gc);
		}
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env){
		if(figure!=null)figure.computeFiguresAndProperties(env);
	}

	public void registerNames(NameResolver resolver){
		if(figure!=null)figure.registerNames(resolver);
	}

	public void layout() {
		if(figure!=null){
			figure.setToMinSize();
			figure.layout();
		}
	}
}
