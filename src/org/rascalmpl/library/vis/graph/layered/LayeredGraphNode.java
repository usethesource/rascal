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
	
	public void addShadowConnections(){
		shadowing = true;
		inShadowed = new LinkedList<LayeredGraphNode>();
		outShadowed = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode i : in)
			inShadowed.add(i);
		for(LayeredGraphNode o : out)
			outShadowed.add(o);
	}
	
	public void delShadowConnections(){
		inShadowed = outShadowed = null;
		shadowing = false;
	}
	
	public void clearHorizontal(){
		root = align = sink = this;
		x = -1;
		shift = INFINITY;
	}
	
	public void averageHorizontal(){
		// compute average median
		System.err.printf("averageHorizontal: " + name);
		System.err.printf(" [%f,%f,%f,%f], ", xs[0], xs[1], xs[2], xs[3]);
	
		Arrays.sort(xs);
		x = (xs[1] + xs[2])/2;
		System.err.printf("sorted; [%f,%f,%f,%f] => %f\n",  xs[0], xs[1], xs[2], xs[3], x);
	}
	
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
	
	public LinkedList<LayeredGraphNode> getInAbove(){
		LinkedList<LayeredGraphNode> inAbove = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode g : in){
			if(g.layer < layer)
				inAbove.add(g);
		}
		return inAbove;
	}
	
	public LinkedList<LayeredGraphNode> getOutBelow(){
		LinkedList<LayeredGraphNode> outBelow = new LinkedList<LayeredGraphNode>();
		for(LayeredGraphNode g : out){
			if(g.layer > layer)
				outBelow.add(g);
		}
		return outBelow;
	}
	
	public void delIn(LayeredGraphNode n){
		if(in.contains(n))
			in.remove(n);
	}
	
	public void delOut(LayeredGraphNode n){
		if(out.contains(n))
			out.remove(n);
	}
	
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
	
	public boolean isSink(){
		return (shadowing ? outShadowed : out).size() == 0;
	}
	
	public boolean isSource(){
		return (shadowing ? inShadowed : in).size() == 0;
	}
	
	public int getOutInDiff(){
		return shadowing ? outShadowed.size() - inShadowed.size() : out.size() - in.size();
	}
	
	public int compareTo(LayeredGraphNode o){
		return shadowing ? compare(outShadowed, o.outShadowed) : compare(out, o.out);
	}
	
	private int compare(LinkedList<LayeredGraphNode> S, LinkedList<LayeredGraphNode> T){
		if(S.size() == 0 && T.size() == 0)
			return 0;
		if(S.size() == 0 && T.size() != 0)
			return -1;
		if(S.size() > 0 && T.size() == 0)
			return 1;
		if(S.size() != 0 && T.size() != 0){
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
		return 0;
	}
	
	public LinkedList<LayeredGraphNode> increasingSortedOut(){
		if(shadowing){
			Collections.sort(outShadowed);
			return outShadowed;
		}
		Collections.sort(out);
		return out;
	}
	
	public boolean AllInAssignedToLayers(int layer){
		System.err.printf("AllInAssignedToLayers %s, layer=%d => ", name, layer);
		for(LayeredGraphNode g : shadowing ? inShadowed : in){
			if(g.layer < 0 || g.layer > layer){
				System.err.println("false");
				return false;
			}
		}
		System.err.println("true");
		return true;
	}
	
	public float getX(Direction dir){
		switch(dir){
		case TOP_LEFT: return xs[0];
		case TOP_RIGHT: return xs[1];
		case BOTTOM_LEFT: return xs[2];
		case BOTTOM_RIGHT: return xs[3];
		}
		return -1;
	}
	
	public void setX(Direction dir, float x){
		switch(dir){
		case TOP_LEFT: this.x = xs[0] = x; return;
		case TOP_RIGHT: this.x = xs[1] = x; return;
		case BOTTOM_LEFT: this.x = xs[2] = x; return;
		case BOTTOM_RIGHT: this.x = xs[3] = x; return;
		} 
	}
	
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
