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
package org.rascalmpl.library.vis.figure.compose;

import static org.rascalmpl.library.vis.properties.Properties.HGAP;

import java.util.Arrays;
import java.util.Comparator;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;
/**
 * Pack a list of elements as dense as possible in a space of given size. 
 * 
 * Pack is implemented using lightmaps as described at http://www.blackpawn.com/texts/lightmaps/
 * 
 * @author paulk
 *
 */

//TODO: fix me for resizing!
public class Pack extends WidthDependsOnHeight {
	
	public Pack(Dimension major, Figure[] figures, PropertyManager properties) {
		super(major, figures, properties);
	}

	Node root;
	boolean fits = true;
	static protected boolean debug =true;
	boolean initialized = false;
	/*
	 * Compare two Figures according to their surface and aspect ratio
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	
	public class CompareAspectSize implements Comparator<Figure>{

		public int compare(Figure o1, Figure o2) {
			BoundingBox lhs = o1.minSize;
			BoundingBox rhs = o2.minSize;
			double r = (lhs.getY() > lhs.getX()) ? lhs.getY() / lhs.getX() : lhs.getX() / lhs.getY();
			double or = (rhs.getY() > rhs.getX()) ? rhs.getY() / rhs.getX() : rhs.getX() / rhs.getY();
					  
			if (r < 2.0 && or < 2.0) { 
				double s = lhs.getX() * lhs.getY(); 
				double os = rhs.getX() * rhs.getY();
				return s < os ? 1 : (s == os ? 0 : -1); 
			} 
			return r < or ? 1 : (r == or ? 0 : -1); 
		}
		
	}
	
	@Override
	public void resizeElement(Rectangle view) {
		System.out.printf("Trying to fit in %s",size);
		//if(true)return;
		Node.hgap = prop.getReal(HGAP);
		Node.vgap = prop.getReal(HGAP);
		for(Figure v : children){
			v.size.set(v.minSize);
		}
		/* double surface = 0;
		double maxw = 0;
		double maxh = 0;
		double ratio = 1;
		for(Figure fig : figures){
			//fig.bbox();
			maxw = Math.max(maxw, fig.minSize.getX());
			maxh = Math.max(maxh, fig.minSize.getY());
			surface += fig.minSize.getX() * fig.minSize.getY();
			ratio = (ratio +fig.minSize.getY()/fig.minSize.getX())/2;
		} */
//		double opt = FigureApplet.sqrt(surface);
//		minSize.setWidth(opt);
//		minSize.setHeight(ratio * opt);

		//width = opt/maxw < 1.2 ? 1.2f * maxw : 1.2f*opt;
	//	height = opt/maxh < 1.2 ? 1.2f * maxh : 1.2f*opt;
		
		//if(debug)System.err.printf("pack: ratio=%f, maxw=%f, maxh=%f, opt=%f, width=%f, height=%f\n", ratio, maxw, maxh, opt, minSize.getX(), minSize.getY());
		Arrays.sort(children, new CompareAspectSize());
		if(debug){
			System.err.println("SORTED ELEMENTS!:");
			for(Figure v : children){
				System.err.printf("\t%s, width=%f, height=%f\n", v, v.minSize.getX(), v.minSize.getY());
			}
		}
		fits = false;
		while(!fits){
			fits = true;
	
			root = new Node(0, 0, size.getX(), size.getY());
			
			for(Figure fig : children){
				Node nd = root.insert(fig);
				if(nd == null){
					System.err.printf("**** PACK: NOT ENOUGH ROOM ***** %s (minSize=%s)\n",size, minSize);
					fits = false;
					size.set(minor,size.get(minor) * 2.0);
					break;
				}
				nd.figure = fig;
				nd.figure.location.set(nd.left,nd.right);
			}
		}
	}
}

class Node {
	static double hgap;
	static double vgap;
	Node lnode;
	Node rnode;
	Figure figure;
	double left;
	double top;
	double right;
	double bottom;
	
	Node (double left, double top, double right, double bottom){
		lnode  = rnode = null;
		figure = null;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		if(Pack.debug) System.err.printf("Create Node(%f,%f,%f,%f)\n", left, top, right, bottom);
	}
	
	boolean leaf(){
		return (lnode == null);
	}
	
	public Node insert(Figure fig){
		//if(Pack.debug)System.err.printf("insert: %s: %f, %f\n", id, fig.minSize.getX(), fig.minSize.getY());
		if(!leaf()){
			// Not a leaf, try to insert in left child
			//if(Pack.debug)System.err.printf("insert:%s in left child\n", id);
			Node newNode = lnode.insert(fig);
			if(newNode != null){
				//if(Pack.debug)System.err.printf("insert: %s in left child succeeded\n", id);
				return newNode;
			}
			// No room, try it in right child
			//if(Pack.debug)System.err.printf("insert: %s in left child failed, try right child\n", id);
			return rnode.insert(fig);
		}
		
		// We are a leaf, if there is already a velem return
		if(figure != null){
			//if(Pack.debug)System.err.printf("insert: %s: Already occupied\n", id);
			return null;
		}
		
		double width = right - left;
		double height = bottom - top;
		
		// If we are too small return
		
		if(width <= 0.01f || height <= 0.01f)
			return null;
		
		double dw = width - fig.minSize.getX();
        double dh = height - fig.minSize.getY();
        
     //  if(Pack.debug)System.err.printf("%s: dw=%f, dh=%f\n", id, dw, dh);
		
		if ((dw < hgap) || (dh < vgap))
			return null;
		
		// If we are exactly right return
		if((dw  <= 2 * hgap) && (dh <= 2 * vgap)){
			//if(Pack.debug)System.err.printf("insert: %s FITS!\n", id);
			return this;
		}
		
		// Create two children and decide how to split

        if(dw > dh) {
        	//if(Pack.debug)System.err.printf("%s: case dw > dh\n", id);
        	lnode = new Node(left,                 top, left + fig.minSize.getX() + hgap, bottom);
        	rnode = new Node(left + fig.minSize.getX() + hgap, top, right,                bottom);
        } else {
        	//if(Pack.debug)System.err.printf("%s: case dw <= dh\n", id);
           	lnode = new Node(left, top,                  right, top + fig.minSize.getY() + vgap);
        	rnode = new Node(left, top + fig.minSize.getY() + vgap, right, bottom);
        }
        
        // insert the figure in left most child
        
        return lnode.insert(fig);
	}
}

