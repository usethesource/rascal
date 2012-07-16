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
package org.rascalmpl.library.vis.figure.graph.layered;

import static org.rascalmpl.library.vis.properties.Properties.LAYER;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Rectangle;
/**
 * A GraphNode is created for each "node" constructor that occurs in the graph.
 * 
 * @author paulk
 *
 */

public class LayeredGraphNode extends Figure /*implements Comparable<LayeredGraphNode>*/ {
	
	protected String name;
	protected Figure figure;
	protected double x = -1;
	protected double y = -1;
	
	private double[] xs = {-1f, -1f, -1f, -1f};
	
	LayeredGraph graph;
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
	private LayeredGraphNode sink;
	final int INFINITY = Integer.MAX_VALUE;
	final double DINFINITY = Double.MAX_VALUE;
	private double shift = DINFINITY;
	public double layerHeight;
	public double blockWidth;
	private double virtWidth = 20;  // Dimensions of a virtual node
	private double virtHeight = 20;
	
	LayeredGraphNode(LayeredGraph g, String name, Figure fig){
		super(g.prop);
		this.graph = g;
		this.name = name;
		this.figure = fig;
		if(fig != null){
			this.children = new Figure[1];
		    this.children[0] = fig;
		} else {
			this.children = childless;
		}
		in = new LinkedList<LayeredGraphNode>();
		out = new LinkedList<LayeredGraphNode>();
		root = align = sink = this;
		shift = DINFINITY;
	}
	
	LayeredGraphNode(LayeredGraph g, String name, double vw, double vh){
		this(g, name, null);
		virtWidth = vw;
		virtHeight = vh;
	}
	
	public void init(){
		x = y = -1;
		marked = false;
		root = align = sink = this;
	}
	
	public void computeMinSize(){
		//resizable.set(false,false);
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
	
	public boolean hasVirtualOutTo(LayeredGraphNode other){
		String vname = "_" + other.name + "[";
		for(LayeredGraphNode g : out){
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
			double tmp = figure.minSize.getX(); 
			figure.minSize.setX(figure.minSize.getY()); 
			figure.minSize.setY(tmp);
		}
	}
	
	public double baryCenter(LinkedList<LayeredGraphNode> above,LinkedList<LayeredGraphNode> below){
		int sum = 0;
		LinkedList<LayeredGraphNode> aboveG = getAllConnectedNeighbours(above);
		LinkedList<LayeredGraphNode> belowG = getAllConnectedNeighbours(below);
		for(LayeredGraphNode ag : aboveG){
			sum += ag.x;
		}
		for(LayeredGraphNode bg : belowG){
			sum += bg.x;
		}
		int degree = aboveG.size() + belowG.size();
		return degree > 0 ? sum/degree : 0;
	}
	
	public double median(LinkedList<LayeredGraphNode> above){
		LinkedList<LayeredGraphNode> aboveG = getAllConnectedNeighbours(above);
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
			//System.err.println("AllInLabelled: " + g.name + " label = " + g.label);
			if(g.label < 0)
				return false;
		}
		return true;
	}
	
	public boolean AllOutAssignedToLayers(){
		//System.err.printf("AllOutAssignedToLayers %s\n", name);
		for(LayeredGraphNode g : out){
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
		for(LayeredGraphNode g : shadowing ? outShadowed : out){
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
	public LinkedList<LayeredGraphNode> getAllConnectedNeighbours(LinkedList<LayeredGraphNode> layer){
		LinkedList<LayeredGraphNode> connected = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode g : layer){
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
		for(LayeredGraphNode g : in){
			if(g.pos > maxIn)
				maxIn = g.pos;
		}
		return maxIn;
	}
	
	public boolean incidentToInnerSegment(LinkedList<LayeredGraphNode> other){
		int minIn = INFINITY;
		int maxIn = -1;
		for(LayeredGraphNode g : in){
			if(g.pos < minIn)
				minIn = g.pos;
			if(g.pos > maxIn)
				maxIn = g.pos;
		}
		for(LayeredGraphNode g1 : other){
			if(g1.isVirtual()){
				LayeredGraphNode g2 = g1.out.get(0);
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
		root = align = this;
		setSink(this);
		x = -1;
		shift = DINFINITY;
		blockWidth = width();
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
	
	public void setShift(double s){
		shift = s;
	}
	
	public double getShift(){
		return shift;
	}
	
	public void setSink(LayeredGraphNode s){
		sink = s;
	}
	
	public LayeredGraphNode getSink(){
		return sink;
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
	
	double width(){
		return figure != null ? figure.minSize.getX() : virtWidth;
	}
	
	double height(){
		return figure != null ? figure.minSize.getY() : virtHeight;
	}
	
	String getLayer(){
		return figure != null ? figure.prop.getStr(LAYER): "";
	}

	@Override
	public void resizeElement(Rectangle view) {
		localLocation.set(0, 0);
		if(figure != null){
			figure.localLocation.set(x - figure.minSize.getX()/2, y - figure.minSize.getY()/2);
		}
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
	  System.err.println("Drawing node " + name + ": " + figure);
	  if(figure != null)
		  figure.drawElement(gc, visibleSWTElements);
	}
}
