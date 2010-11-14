package org.rascalmpl.library.vis.graph.lattice;

import java.util.LinkedList;

import org.rascalmpl.library.vis.Figure;

/**
 * A GraphNode is created for each "node" constructor that occurs in the graph.
 * 
 * @author paulk
 *
 */
public class LatticeGraphNode {
	
	int  label = -1;
	int layer = -1;
	private LatticeGraph G;
	String name;
	Figure figure;
	LinkedList<LatticeGraphNode> in;
	LinkedList<LatticeGraphNode> out;
	float x;
	float y;
	public int rank;
	public int rankTop;
	public int rankBottom;
	
//	protected float force[];
	
	LatticeGraphNode(LatticeGraph latticeGraph, String name, Figure fig){
		this.G = latticeGraph;
		this.name = name;
		this.figure = fig;
		in = new LinkedList<LatticeGraphNode>();
		out = new LinkedList<LatticeGraphNode>();
	}
	
	public boolean isVirtual(){
		return figure == null;
	}
	
	public float xdistance(LatticeGraphNode other){
		float vx = x - other.x;
		return vx;
//		if(vx > 0){
//			return PApplet.max(vx - (velem.width/2 + other.velem.width/2), 0.01f);
//		} else {
//			return PApplet.min(vx + (velem.width/2 + other.velem.width/2), -0.01f);
//		}
	}
	
	public float ydistance(LatticeGraphNode other){
		float vy = y - other.y ;
		return vy;
//		if(vy > 0){
//			return PApplet.max(vy - (velem.height/2 + other.velem.height/2), 0.01f);
//		} else {
//			return PApplet.min(vy + (velem.height/2 + other.velem.height/2), -0.01f);
//		}
	}
	
//	public float getMass(){
//		return 1.0f;
//	}
	
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
	
	public boolean mouseOver(int mousex, int mousey){
		if(figure.mouseInside(mousex, mousey)){
			figure.fpa.registerFocus(figure);
			return true;
		}
		return false;
	}
	
	public boolean mousePressed(int mousex, int mousey){
		if(figure.mouseInside(mousex, mousey)){
			figure.fpa.registerFocus(figure);
			return true;
		}
		return false;
	}

	public void addIn(LatticeGraphNode n){
		if(!in.contains(n))
			in.add(n);
	}
	
	public void addOut(LatticeGraphNode n){
		if(!out.contains(n))
			out.add(n);
	}
}
