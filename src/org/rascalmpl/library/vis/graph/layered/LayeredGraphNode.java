package org.rascalmpl.library.vis.graph.layered;

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
	protected float x;
	protected float y;
	
	protected LinkedList<LayeredGraphNode> in;
	protected LinkedList<LayeredGraphNode> out;
//	private static boolean debug = false;
	int  label = -1;
	int layer = -1;
	public float layerHeight;
	
	LayeredGraphNode(String name, Figure fig){
		this.name = name;
		this.figure = fig;
		in = new LinkedList<LayeredGraphNode>();
		out = new LinkedList<LayeredGraphNode>();
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
	
	public int maxLabel(){
		return maxLabel(out);
	}
	
	private int maxLabel(LinkedList<LayeredGraphNode> a){
		int m = -1;
		for(LayeredGraphNode g : a){
			if(g.label > m)
				m = g.label;
		}
		return m;
	}
	
	public int compareTo(LayeredGraphNode o){
		return compare(out, o.out);
	}
	
	private int compare(LinkedList<LayeredGraphNode> a, LinkedList<LayeredGraphNode> b){
		if(a.size() == 0 && b.size() == 0)
			return 0;
		if(a.size() == 0 && b.size() != 0)
			return -1;
		if(a.size() > 0 && b.size() == 0)
			return 1;
		if(a.size() != 0 && b.size() != 0){
			int maxa = maxLabel(a);
			int maxb = maxLabel(b);
			
			if(maxa < 0)
				return 1;
			if(maxb < 0)
				return -1;
			
			if(maxLabel(a) < maxLabel(b))
				return -1;
			if(maxLabel(a) > maxLabel(b))
				return 1;
		
			LinkedList<LayeredGraphNode> aOut = new LinkedList<LayeredGraphNode>();
			for(LayeredGraphNode g : a){
					if(g.label != maxLabel(a))
						aOut.add(g);
			}
			
			LinkedList<LayeredGraphNode> bOut = new LinkedList<LayeredGraphNode>();
			for(LayeredGraphNode g : b){
					if(g.label != maxLabel(b))
						bOut.add(g);
			}
			return compare(aOut, bOut);
		}
		return 0;
	}
	
	public LinkedList<LayeredGraphNode> sortedOut(){
		Collections.sort(out);
		return out;
	}
	
	public boolean AllInAssignedToLayers(int layer){
		for(LayeredGraphNode g : in){
			if(g.layer < 0 || g.layer > layer){
				System.err.println("AllInAssignedToLayers => false");
				return false;
			}
		}
		System.err.println("AllInAssignedToLayers => true");
		return true;
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
	
	public boolean mousePressed(int mousex, int mousey){
		if(figure != null && figure.mouseInside(mousex, mousey)){

			figure.fpa.registerFocus(figure);
			return true;
		}
		return false;
	}
}
