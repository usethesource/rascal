package org.meta_environment.rascal.library.experiments.VL;

import java.util.Arrays;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

/* 
 * Pack implements lightmaps as described at http://www.blackpawn.com/texts/lightmaps/
*/

public class Pack extends Compose {
	
	Node root;
	boolean fits = true;

	Pack(VLPApplet vlp, PropertyManager inheritedProps, IList props,
			IList elems, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, elems, ctx);
	}

	@Override
	void bbox(float left, float top) {
		this.left = left;
		this.top = top;
		//width = getWidthProperty();
		//height = getHeightProperty();

		Node.gap = 3;
		float surface = 0;
		float maxw = 0;
		float maxh = 0;
		float ratio = 1;
		for(VELEM ve : velems){
			ve.bbox();
			maxw = max(maxw, ve.width);
			maxh = max(maxh, ve.height);
			surface += ve.width * ve.height;
			ratio = (ratio +ve.height/ve.width)/2;
		}
		float opt = PApplet.sqrt(surface);
		width = opt;
		height = ratio * opt;
		//width = opt/maxw < 1.2 ? 1.2f * maxw : 1.2f*opt;
	//	height = opt/maxh < 1.2 ? 1.2f * maxh : 1.2f*opt;
		
		System.err.printf("pack: ratio=%f, maxw=%f, maxh=%f, opt=%f, width=%f, height=%f\n", ratio, maxw, maxh, opt, width, height);
			
		Arrays.sort(velems);
		System.err.println("SORTED ELEMENTS:");
		for(VELEM v : velems){
			System.err.printf("\twidth=%f, height=%f\n", v.width, v.height);
		}
		
		fits = false;
		while(!fits){
			fits = true;
			width *= 1.2f;
			height *= 1.2f;
			root = new Node(0, 0, width, height);
			
			for(VELEM ve : velems){
				Node nd = root.insert(ve);
				if(nd == null){
					//System.err.println("**** PACK: NOT ENOUGH ROOM *****");
					fits = false;
					break;
				}
				nd.velem = ve;
			}
		}
	}

	@Override
	void draw() {
		System.err.printf("pack.draw: %f, %f\n", left, top);
		
		applyProperties();

		if(fits){
			System.err.printf("pack.draw: left=%f, top=%f\n", left, top);
			root.draw(left, top);
		} else {
			vlp.fill(0);
			vlp.rect(left, top, width, height);
		}
	}
}

class Node {
	static int gap;
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
		System.err.printf("Node(%f,%f,%f,%f)\n", left, top, right, bottom);
	}
	
	boolean leaf(){
		return (lnode == null);
	}
	
	public Node insert(VELEM v){
		System.err.printf("insert: %f, %f\n", v.width, v.height);
		if(!leaf()){
			// Not a leaf, try to insert in left child
			Node newNode = lnode.insert(v);
			if(newNode != null)
				return newNode;
			// No room, try it in right child
			return rnode.insert(v);
		}
		
		// We are a leaf, if there is already a velem return
		if(velem != null){
			//System.err.println("Already occupied");
			return null;
		}
		
		float width = right - left;
		float height = bottom - top;
		
		System.err.printf("width=%f, height=%f\n", width, height);
		System.err.printf("ve.width=%f, ve.height=%f\n", v.width, v.height);
		
		// If we are too small return
		
		float dw = width - v.width;
        float dh = height - v.height;
        
       System.err.printf("dw=%f, dh=%f\n", dw, dh);
		
		if ((dw <= 0) || (dh <= 0))
			return null;
		
		// If we are exactly right return
		if((dw  <= 2 * gap) && (dh <= 2 * gap)){
			System.err.println("FIT!");
			return this;
		}
		
		// Create two children and decide how to split

        if(dw > dh) {
        	System.err.println("case dw > dh");
        	lnode = new Node(left,                 top, left + v.width + gap, bottom);
        	rnode = new Node(left + v.width + gap, top, right,                bottom);
        } else {
        	System.err.println("case dw <= dh");
        	lnode = new Node(left, top,                  right, top + v.height + gap);
        	rnode = new Node(left, top + v.height + gap, right, bottom);
        }
        
        // insert the velem in left most child
        
        return lnode.insert(v);
	}
	
	void draw(float left, float top){
		if(lnode != null) lnode.draw(left, top);
		if(rnode != null) rnode.draw(left, top);
		if(velem != null){
			velem.draw(left + this.left, top + this.top);
		}
	}
}
