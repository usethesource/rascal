package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

/* 
 * Pack implements lightmaps as described at http://www.blackpawn.com/texts/lightmaps/
*/

public class Pack extends Compose {
	
	Node root;
	boolean fits = true;

	Pack(VLPApplet vlp, HashMap<String, IValue> inheritedProps, IList props,
			IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}

	@Override
	BoundingBox bbox() {
		width = getWidthProperty();
		height = getHeightProperty();
		root = new Node(0, 0, width, height);
		Node.gap = 3;
		for(VELEM ve : velems){
			ve.bbox();
			Node nd = root.insert(ve);
			if(nd == null){
				//System.err.println("**** PACK: NOT ENOUGH ROOM *****");
				fits = false;
				break;
			}
			nd.velem = ve;
		}
		return new BoundingBox(width, height);
	}

	@Override
	void draw(float x, float y) {
		System.err.printf("pack.draw: %f, %f\n", x, y);
		this.x = x;
		this.y = y;
		applyProperties();

		if(fits){
			float left = x - width/2;
			float top = y - height/2;
			System.err.printf("pack.draw: left=%f, top=%f\n", left, top);
			root.draw(left, top);
		} else {
			vlp.fill(0);
			vlp.rect(x, y, width, height);
		}
	}
}

class Node {
	static int gap = 0;
	Node lnode;
	Node rnode;
	VELEM velem;
	float left;
	float top;
	float right;
	float bottom;
	
	Node (float left, float top, float right, float bottom){
		lnode  = rnode = null;
		velem = null;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		//System.err.printf("Node(%f,%f,%f,%f)\n", left, top, right, bottom);
	}
	
	boolean leaf(){
		return (lnode == null);
	}
	
	public Node insert(VELEM v){
		//System.err.printf("insert: %f, %f\n", v.width, v.height);
		if(!leaf()){
			// Not a leaf, try to insert in left child
			Node newNode = lnode.insert(v);
			if(newNode != null)
				return newNode;
			// No room, try it in right child
			return rnode.insert(v);
		} else {
			// We are a leaf, if there is already a velem return
			if(velem != null){
				//System.err.println("Already occupied");
				return null;
			}
			
			float width = right - left;
			float height = bottom - top;
			
			//System.err.printf("width=%f, height=%f\n", width, height);
			//System.err.printf("ve.width=%f, ve.height=%f\n", v.width, v.height);
			
			// If we are too small return
			
			float dw = width - v.width;
	        float dh = height - v.height;
	        
	       // System.err.printf("dw=%f, dh=%f\n", dw, dh);
			
			if ((dw < 0) || (dh < 0))
				return null;
			
			// If we are exactly right return
			if((dw  <= gap) && (dh <= gap)){
				//System.err.println("FIT!");
				return this;
			}
			
			// Create two children and decide how to split

	        if(dw > dh) {
	        	//System.err.println("case dw > dh");
	        	lnode = new Node(left,                 top, left + v.width + gap, bottom);
	        	rnode = new Node(left + v.width + gap, top, right,                bottom);
	        } else {
	        	//System.err.println("case dw <= dh");
	        	lnode = new Node(left, top,                  right, top + v.height + gap);
	        	rnode = new Node(left, top + v.height + gap, right, bottom);
	        }
	        
	        // insert the velem in left most child
	        
	        return lnode.insert(v);
		}
	}
	
	void draw(float l, float t){
		if(lnode != null) lnode.draw(l, t);
		if(rnode != null) rnode.draw(l, t);
		if(velem != null){
			float x = l + left + velem.width/2;
			float y = t + top + velem.height/2;
			velem.draw(x,y);
		}
	}
}
