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
package org.rascalmpl.library.vis.compose;

import java.util.Arrays;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureApplet;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * Pack a list of elements as dense as possible in a space of given size. 
 * 
 * Pack is implemented using lightmaps as described at http://www.blackpawn.com/texts/lightmaps/
 * 
 * @author paulk
 *
 */

//TODO: fix me for resizing!
public class Pack extends Compose {
	
	Node root;
	boolean fits = true;
	static protected boolean debug = false;
	boolean initialized = false;

	public Pack(IFigureApplet fpa, Figure[] figures, PropertyManager properties) {
		super(fpa, figures, properties);
	}

	@Override
	public
	void bbox() {
		if(initialized)
			return;
		minSize.setWidth(getWidthProperty());
		minSize.setHeight(getHeightProperty());

		Node.hgap = getHGapProperty();
		Node.vgap = getVGapProperty();
		double surface = 0;
		double maxw = 0;
		double maxh = 0;
		double ratio = 1;
		for(Figure fig : figures){
			fig.bbox();
			maxw = Math.max(maxw, fig.minSize.getWidth());
			maxh = Math.max(maxh, fig.minSize.getHeight());
			surface += fig.minSize.getWidth() * fig.minSize.getHeight();
			ratio = (ratio +fig.minSize.getHeight()/fig.minSize.getWidth())/2;
		}
		double opt = FigureApplet.sqrt(surface);
		minSize.setWidth(opt);
		minSize.setHeight(ratio * opt);

		//width = opt/maxw < 1.2 ? 1.2f * maxw : 1.2f*opt;
	//	height = opt/maxh < 1.2 ? 1.2f * maxh : 1.2f*opt;
		
		if(debug)System.err.printf("pack: ratio=%f, maxw=%f, maxh=%f, opt=%f, width=%f, height=%f\n", ratio, maxw, maxh, opt, minSize.getWidth(), minSize.getHeight());
			
		Arrays.sort(figures);
		if(debug){
			System.err.println("SORTED ELEMENTS:");
			for(Figure v : figures){
				System.err.printf("\t%s, width=%f, height=%f\n", v.getIdProperty(), v.minSize.getWidth(), v.minSize.getHeight());
			}
		}
		
		fits = false;
		while(!fits){
			fits = true;
			minSize.setWidth(minSize.getWidth() * 1.2f);
			minSize.setHeight(minSize.getHeight() * 1.2f);
			root = new Node(0, 0, minSize.getWidth(), minSize.getHeight());
			
			for(Figure fig : figures){
				Node nd = root.insert(fig);
				if(nd == null){
					//System.err.println("**** PACK: NOT ENOUGH ROOM *****");
					fits = false;
					break;
				}
				nd.figure = fig;
			}
		}
		initialized = true;
		setNonResizable();
		super.bbox();
	}

	@Override
	public
	void draw(double left, double top) {
		if(debug)System.err.printf("pack.draw: %f, %f\n", left, top);
		setLeft(left);
		setTop(top);
		
		applyProperties();

		if(fits){
			if(debug)System.err.printf("pack.draw: left=%f, top=%f\n", left, top);
			root.draw(left, top);
		} else {
			fpa.fill(0);
			fpa.rect(left, top, minSize.getWidth(), minSize.getHeight());
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
		String id = fig.getIdProperty();
		if(Pack.debug)System.err.printf("insert: %s: %f, %f\n", id, fig.minSize.getWidth(), fig.minSize.getHeight());
		if(!leaf()){
			// Not a leaf, try to insert in left child
			if(Pack.debug)System.err.printf("insert:%s in left child\n", id);
			Node newNode = lnode.insert(fig);
			if(newNode != null){
				if(Pack.debug)System.err.printf("insert: %s in left child succeeded\n", id);
				return newNode;
			}
			// No room, try it in right child
			if(Pack.debug)System.err.printf("insert: %s in left child failed, try right child\n", id);
			return rnode.insert(fig);
		}
		
		// We are a leaf, if there is already a velem return
		if(figure != null){
			if(Pack.debug)System.err.printf("insert: %s: Already occupied\n", id);
			return null;
		}
		
		double width = right - left;
		double height = bottom - top;
		
		// If we are too small return
		
		if(width <= 0.01f || height <= 0.01f)
			return null;
		
		double dw = width - fig.minSize.getWidth();
        double dh = height - fig.minSize.getHeight();
        
       if(Pack.debug)System.err.printf("%s: dw=%f, dh=%f\n", id, dw, dh);
		
		if ((dw < hgap) || (dh < vgap))
			return null;
		
		// If we are exactly right return
		if((dw  <= 2 * hgap) && (dh <= 2 * vgap)){
			if(Pack.debug)System.err.printf("insert: %s FITS!\n", id);
			return this;
		}
		
		// Create two children and decide how to split

        if(dw > dh) {
        	if(Pack.debug)System.err.printf("%s: case dw > dh\n", id);
        	lnode = new Node(left,                 top, left + fig.minSize.getWidth() + hgap, bottom);
        	rnode = new Node(left + fig.minSize.getWidth() + hgap, top, right,                bottom);
        } else {
        	if(Pack.debug)System.err.printf("%s: case dw <= dh\n", id);
           	lnode = new Node(left, top,                  right, top + fig.minSize.getHeight() + vgap);
        	rnode = new Node(left, top + fig.minSize.getHeight() + vgap, right, bottom);
        }
        
        // insert the figure in left most child
        
        return lnode.insert(fig);
	}
	
	void draw(double left, double top){
		if(lnode != null) lnode.draw(left, top);
		if(rnode != null) rnode.draw(left, top);
		if(figure != null){
			figure.draw(left + this.left, top + this.top);
		}
	}
}
