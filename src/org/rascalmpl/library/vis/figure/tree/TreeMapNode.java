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

package org.rascalmpl.library.vis.figure.tree;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;

/**
 * A TreeMapNode is created for each "node" constructor that occurs in the TreeMap.
 * 
 * @author paulk
 *
 */
public class TreeMapNode extends Figure {
	
	Figure rootFigure;
	TreeMap treemap;
	private ArrayList<TreeMapNode> children;
	private double[] childLeft;
	private double[] childTop;
	private static boolean debug = true;
	
	public TreeMapNode(IFigureConstructionEnv fpa, TreeMap treeMap, PropertyManager properties,
			Figure fig) {
		super(properties);
		this.treemap = treeMap;
		rootFigure = fig;
		children = new ArrayList<TreeMapNode>();
	}
	
	public void addChild(PropertyManager inheritedProps, IList props,
			TreeMapNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
	}
	
	public void place(double width, double height, boolean hor) {
		this.minSize.setWidth(width);
		this.minSize.setHeight(height);
		
		double hgap = getHGapProperty();
		double vgap = getHGapProperty();
		
		String id = rootFigure.getIdProperty();
		if(debug)System.err.printf("%s: %f,%f,%s\n", id, width,height, hor? "hor":"vert");
		
		int n = children.size();
		
		childLeft = new double[n];
		childTop = new double[n];
		double ratio[] = new double[n];
		double chsurf = 0;
		double awidth = width - (n+1) * hgap;
		double aheight = height - (n+1) * vgap;
		for(int i = 0; i < n; i++){
			TreeMapNode child = children.get(i);
			child.bbox();
			chsurf += child.minSize.getWidth() * child.minSize.getHeight();
		}
		for(int i = 0; i < n; i++){
			TreeMapNode child = children.get(i);
			ratio[i] = (child.minSize.getWidth() * child.minSize.getHeight()) / chsurf;
			if(debug)System.err.printf("%s: ratio = %f\n", child.rootFigure.getIdProperty(), ratio[i]);
		}
		if(hor){
			double x = hgap;
			for(int i = 0; i < n; i++){	
				TreeMapNode child = children.get(i);
				double dw = ratio[i] * awidth;
				child.place(dw, height - 2* vgap, !hor);
				childLeft[i] = x;
				childTop[i] = vgap;
				x += dw + hgap;
			}
		} else {
			double y = vgap;
			for(int i = 0; i < n; i++){	
				TreeMapNode child = children.get(i);
				double dh =  ratio[i] * aheight;
				child.place(width - 2 * hgap, dh, !hor);
				childLeft[i] = hgap;
				childTop[i] = y;
				y += dh + vgap;
			}
		}
       return;
	}
	
	@Override
	public
	void bbox() {
		rootFigure.bbox();
		minSize.setWidth(rootFigure.minSize.getWidth());
		minSize.setHeight(rootFigure.minSize.getHeight());
		setNonResizable();
	}
	
	@Override
	public
	void draw(GraphicsContext gc){
		if(debug)System.err.printf("draw: %s at %f, %f \n", 
				          rootFigure.getIdProperty(), getLeft(),  getTop()
				          );
		
		rootFigure.applyProperties(gc);
		gc.rect(getLeft(), getTop(), minSize.getWidth(), minSize.getHeight());
		
		int n = children.size();
		for(int i = 0; i < n; i++){
			TreeMapNode child = children.get(i);
			child.draw(gc);
		}
	}

	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		boolean ret = false;
		if(rootFigure!=null){
			ret = rootFigure.getFiguresUnderMouse(c, result);
		}
		if(mouseInside(c.getX(), c.getY())){
			result.add(this);
			ret=true;
		}
		return ret;
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		for(int i = children.size()-1 ; i >= 0 ; i--){
			children.get(i).registerNames(resolver);
		}
	}

	@Override
	public void layout() {
		size.set(minSize);
		for(int i = children.size()-1 ; i >= 0 ; i--){
			children.get(i).setToMinSize();
			children.get(i).layout();
		}
	}
}
