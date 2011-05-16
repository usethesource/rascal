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

package org.rascalmpl.library.vis.tree;

import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.descriptions.HandlerProp;
import org.rascalmpl.library.vis.util.Coordinate;

/**
 * A TreeNode is created for each "node" constructor that occurs in a Tree.
 * After creation, shapeTree is called to determine position and dimensions.
 * 
 * @author paulk
 *
 */
public class TreeNode extends Figure {
	
	Figure rootFigure;                        // Figure associated with this TreeNode
	private ArrayList<TreeNode> children;     // Child nodes
	private ArrayList<PropertyManager> edgeProperties;
	private double[] childRoot;                // Root position of each child
	private double rootPosition;               // Root position of this TreeNode (= middle of rootFigure)
	private static boolean debug = false;
	
	public TreeNode(IFigureApplet fpa, PropertyManager properties, Figure fig) {
		super(fpa, properties);
		rootFigure = fig;
		children = new ArrayList<TreeNode>();
		edgeProperties = new ArrayList<PropertyManager>();
	}
	
	public void addChild(PropertyManager inheritedProps, IList props,
			TreeNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
		//TODO
		edgeProperties.add(new PropertyManager(null, inheritedProps, props, ctx));
	}
	
	/*
	 * Distance between rootPosition and leftmost border of this node
	 */
	
	public double leftExtent(){
		return rootPosition;
	}
	
	/*
	 * Distance between rootPosition and rightmost border of this node
	 */
	
	public double rightExtent(){
		return width - rootPosition;
	}
	
	/**
	 * shapeTree places the current subtree (rooted in this TreeNode)  on the raster
	 * 
	 * @param rootMidX	x coordinate of center of the root figure
	 * @param rootTop	y coordinate of top of root figure
	 * @param raster	NodeRaster to be used
	 * @return the x position of the center of the root
	 */
	double shapeTree(double rootMidX, double rootTop, TreeNodeRaster raster) {
        String id = rootFigure.getIdProperty();
		if(debug)System.err.printf("shapeTree: id=%s, rootMidX=%f, rootTop=%f\n", id, rootMidX, rootTop);
		rootFigure.bbox(AUTO_SIZE, AUTO_SIZE);
		double hgap = getHGapProperty();
		double vgap = getVGapProperty();
		
		// Initial placement of figure of this TreeNode
		double position = raster.leftMostPosition(rootMidX, rootTop, rootFigure.width, rootFigure.height, hgap);
		rootPosition = position;
		height = rootFigure.height;
		width = rootFigure.width;
		
		int nChildren = children.size();
		
		if(nChildren == 0){
			rootPosition = width/2;
		} else {
			for(TreeNode child : children){
				child.rootFigure.bbox(AUTO_SIZE, AUTO_SIZE);
			}
			
			// Compute position of leftmost child
			
			double branchPosition = position;
			
			if(nChildren > 1){
				double widthDirectChildren = (children.get(0).rootFigure.width + children.get(nChildren-1).rootFigure.width)/2 +
				                            (nChildren-1) * hgap;
				for(int i = 1; i < nChildren - 1; i++){
					widthDirectChildren += children.get(i).rootFigure.width;
				}
				branchPosition = position - widthDirectChildren/2; 		// Position of leftmost child
			}
			
			double childTop = rootTop + rootFigure.height + vgap;         // Top of all children
			 
			childRoot = new double[nChildren];
			
			// Place leftmost child
			double leftPosition = childRoot[0] = children.get(0).shapeTree(branchPosition, childTop, raster);
			double rightPosition = leftPosition;
			double heightChildren = children.get(0).height;
			double rightExtentChildren = leftPosition + children.get(0).rightExtent();
			
			for(int i = 1; i < nChildren; i++){
				TreeNode childi = children.get(i);
				branchPosition += hgap + (children.get(i-1).rootFigure.width + childi.rootFigure.width)/2;
				rightPosition = childi.shapeTree(branchPosition, childTop, raster);
				rightExtentChildren = max(rightExtentChildren, rightPosition + childi.rightExtent());
				heightChildren = max(heightChildren, childi.height);
				childRoot[i] = rightPosition;
			}
			position = (leftPosition + rightPosition)/2;
			height += vgap + heightChildren;
			width = max(rootFigure.width, rightExtentChildren - (leftPosition - children.get(0).rootPosition));

			// Make child positions and rootPosition relative to this parent
			setLeft(leftPosition - children.get(0).rootPosition);
			
			for(int i = 0; i < nChildren; i++){
				childRoot[i] -= getLeft();
			}
			rootPosition = position - getLeft();
		}
	
		// After placing all children, we can finally add the current root figure to the raster.
		raster.add(position, rootTop, rootFigure.width, rootFigure.height);
		if(debug)System.err.printf("shapeTree(%s, %f, %f) => position=%f, left=%f, top=%f, width=%f, height=%f\n", id, rootMidX, rootTop, position, getLeft(), getTop(), width, height);
		return position;
	}
	
	@Override
	public
	void bbox(double desiredWidth, double desiredHeight) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public
	void draw(double left, double top){
		
		this.setLeft(left);
		this.setTop(top);
		
		String id = rootFigure.getIdProperty();
		int nChildren = children.size();
		
		applyProperties();
		
		double positionRoot = left + rootPosition;
		double leftRootFig = positionRoot - rootFigure.width/2;
		
		if(debug)System.err.printf("draw %s, %f, %f, rootFig at %f, %f\n", id, left, top, leftRootFig, top);
		
		// Draw the root figure
		rootFigure.draw(leftRootFig, top);
		
		if(nChildren == 0)
			return;
		
		double bottomRootFig = top + rootFigure.height;
		double vgap          = getVGapProperty();
		double childTop      = bottomRootFig + vgap; 
		double horLine       = bottomRootFig + vgap/2;
		
		// Vertical line from bottom of root figure to horizontal line
		fpa.line(positionRoot, bottomRootFig, positionRoot, horLine);
		
		// Horizontal line connecting all the children
		if(nChildren > 1)
			fpa.line(left + childRoot[0], horLine, left + childRoot[nChildren-1], horLine);
	
		// TODO line style!
		
		for(int i = 0; i < nChildren; i++){
			TreeNode child = children.get(i);
			double positionChild = left + childRoot[i];
			if(debug)System.err.printf("draw %s, child %d at posChild=%f, widthChild=%f, posRoot=%f\n", id, i, positionChild, child.width, child.rootPosition, childTop);

			// Vertical line from horizontal line to top of this child
			fpa.line(positionChild, horLine, positionChild, childTop);
			child.draw(positionChild - child.leftExtent(), childTop);
		}
		
	}
	
	
	@Override
	public boolean mouseInside(double mousex, double mousey){
		return rootFigure.mouseInside(mousex, mousey);
	}

	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		for(int i = children.size()-1 ; i >= 0 ; i--){
			if(children.get(i).getFiguresUnderMouse(c, result)){
				break;
			}
		}
		if(rootFigure.getFiguresUnderMouse(c, result)) {
			result.add(rootFigure);
		}
		return true;
	}
}
