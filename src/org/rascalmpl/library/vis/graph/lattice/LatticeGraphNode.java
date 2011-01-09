package org.rascalmpl.library.vis.graph.lattice;

import java.awt.event.MouseEvent;
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
	String name;
	Figure figure;
	LinkedList<LatticeGraphNode> in;
	LinkedList<LatticeGraphNode> out;
	float x;
	float y;
	public int rank;
	public int rankTop;
	public int rankBottom;

	
	LatticeGraphNode(String name, Figure fig){
		this.name = name;
		this.figure = fig;
		in = new LinkedList<LatticeGraphNode>();
		out = new LinkedList<LatticeGraphNode>();
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
		return figure.mouseOver(mousex, mousey, mouseInParent);
	}
	
	public boolean mousePressed(int mousex, int mousey, MouseEvent e){
		return figure.mousePressed(mousex, mousey, e);
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
