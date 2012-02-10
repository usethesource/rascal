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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.FigureColorUtils;
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
	
	Node root;
	boolean fits = true;
	static protected boolean debug =false;
	boolean initialized = false;
	BoundingBox oldSize;

	public Pack( Figure[] figures, PropertyManager properties) {
		super(Dimension.X, figures, properties);
		oldSize = new BoundingBox();
	}
	
	/*
	 * Compare two Figures according to their surface and aspect ratio
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	
	public class CompareAspectSize implements Comparator<Figure>{

		@Override
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



	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		
		if(!fits){
			String message = "Pack: cannot fit!";
			gc.fill(FigureColorUtils.figureColor(180, 180, 180));
			gc.rect(globalLocation.getX(), globalLocation.getY(), size.getX(), size.getY());
			gc.text(message, globalLocation.getX() + size.getX()/2.0 - getTextWidth(message)/2.0, globalLocation.getY() + size.getY()/2.0 );
		} else {
			super.drawElement(gc, visibleSWTElements);
		}
	}


	@Override
	public void resizeElement(Rectangle view) {
		
		if(size.getX() < minSize.getX()) return;
		if(oldSize.isEq(size)){
			return;
		}
		oldSize.set(size);
		Node.hgap = prop.getReal(Properties.HGAP);
		Node.vgap = prop.getReal(Properties.VGAP);
		for(Figure fig : children){
			fig.size.set(fig.minSize);
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
		int counter = 0;
		fits = false;
		while(!fits && counter < 300){
			fits = true;
			//size.setWidth(size.getX() * 1.2f);
			//size.setHeight(size.getY() * 1.2f);
	
			root = new Node(0, 0, size.getX(), size.getY());
			
			for(Figure fig : children){
				Node nd = root.insert(fig);
				if(nd == null){
					System.err.printf("**** PACK: NOT ENOUGH ROOM ***** %s %s\n",size,minSize);
					fits = false;
					size.setY(size.getY() * 1.2);
					counter++;
					break;
				}
				nd.figure = fig;
				fig.localLocation.setX(nd.left);
				fig.localLocation.setY(nd.top);
				//System.out.printf("Fig locatation %s\n",fig.location);
			}
		}
		realSize.set(size);
		//initialized = true;
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
//		String id = fig.prop.getStr(Properties.ID);
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

