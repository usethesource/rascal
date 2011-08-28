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
import java.util.List;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Rectangle;

/**
 * A TreeNode is created for each "node" constructor that occurs in a Tree.
 * After creation, shapeTree is called to determine position and dimensions.
 * 
 * @author paulk
 *
// */
public class TreeNode extends Figure {
	
	Figure rootFigure;                        // Figure associated with this TreeNode
	private ArrayList<TreeNode> children;     // Child nodes
	private ArrayList<PropertyManager> edgeProperties;
	private double[] childRoot;                // Root position of each child
	private double rootPosition;               // Root position of this TreeNode (= middle of rootFigure)
	private static boolean debug = true;
	
	public TreeNode(IFigureConstructionEnv fpa, PropertyManager properties, Figure fig) {
		super(properties);
		rootFigure = fig;
		children = new ArrayList<TreeNode>();
		edgeProperties = new ArrayList<PropertyManager>();
	}
	
	public void addChild(PropertyManager inheritedProps, IList props,
			TreeNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
		//TODO
		//edgeProperties.add(new PropertyManager(null, inheritedProps, props));
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
		return minSize.getX() - rootPosition;
	}
	
	/**
	 * shapeTree places the current subtree (rooted in this TreeNode)  on the raster
	 * 
	 * @param rootMidX	x coordinate of center of the root figure
	 * @param rootTop	y coordinate of location.getY() of root figure
	 * @param raster	NodeRaster to be used
	 * @return the x position of the center of the root
	 */
	double shapeTree(double rootMidX, double rootTop, TreeNodeRaster raster) {
        String id = rootFigure.prop.getStr(Properties.ID);
		if(debug)System.err.printf("shapeTree: id=%s, rootMidX=%f, rootTop=%f\n", id, rootMidX, rootTop);
		//rootFigure.bbox();
		double hgap = prop.getReal(Properties.HGAP);
		double vgap = prop.getReal(Properties.VGAP);
		
		// Initial placement of figure of this TreeNode
		double position = raster.leftMostPosition(rootMidX, rootTop, rootFigure.minSize.getX(), rootFigure.minSize.getY(), hgap);
		rootPosition = position;
		minSize.setY(rootFigure.minSize.getY());
		minSize.setX(rootFigure.minSize.getX());
		
		int nChildren = children.size();
		
		if(nChildren == 0){
			rootPosition = minSize.getX()/2;
		} else {
			//for(TreeNode child : children){
			//	child.rootFigure.bbox();
			//}
			
			// Compute position of leftmost child
			
			double branchPosition = position;
			
			if(nChildren > 1){
				double widthDirectChildren = (children.get(0).rootFigure.minSize.getX() + children.get(nChildren-1).rootFigure.minSize.getX())/2 +
				                            (nChildren-1) * hgap;
				for(int i = 1; i < nChildren - 1; i++){
					widthDirectChildren += children.get(i).rootFigure.minSize.getX();
				}
				branchPosition = position - widthDirectChildren/2; 		// Position of leftmost child
			}
			
			double childTop = rootTop + rootFigure.minSize.getY() + vgap;         // location.getY() of all children
			 
			childRoot = new double[nChildren];
			
			// Place leftmost child
			double leftPosition = childRoot[0] = children.get(0).shapeTree(branchPosition, childTop, raster);
			double rightPosition = leftPosition;
			double heightChildren = children.get(0).minSize.getY();
			double rightExtentChildren = leftPosition + children.get(0).rightExtent();
			
			for(int i = 1; i < nChildren; i++){
				TreeNode childi = children.get(i);
				branchPosition += hgap + (children.get(i-1).rootFigure.minSize.getX() + childi.rootFigure.minSize.getX())/2;
				rightPosition = childi.shapeTree(branchPosition, childTop, raster);
				rightExtentChildren = Math.max(rightExtentChildren, rightPosition + childi.rightExtent());
				heightChildren = Math.max(heightChildren, childi.minSize.getY());
				childRoot[i] = rightPosition;
			}
			position = (leftPosition + rightPosition)/2;
			minSize.setY(minSize.getY() + vgap + heightChildren);
			minSize.setX(Math.max(rootFigure.minSize.getX(), rightExtentChildren - (leftPosition - children.get(0).rootPosition)));

			// Make child positions and rootPosition relative to this parent
			// TODO: fixme!
			//setLeft(leftPosition - children.get(0).rootPosition);
			
			//for(int i = 0; i < nChildren; i++){
			//	childRoot[i] -= location.getX();
			//}
			rootPosition = position - location.getX();
		}
	
		// After placing all children, we can finally add the current root figure to the raster.
		raster.add(position, rootTop, rootFigure.minSize.getX(), rootFigure.minSize.getY());
		if(debug)System.err.printf("shapeTree(%s, %f, %f) => position=%f, location.getX()=%f, location.getY()=%f, width=%f, height=%f\n", id, rootMidX, rootTop, position, location.getX(), location.getY(), minSize.getX(), minSize.getY());
		return position;
	}
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		
		String id = rootFigure.prop.getStr(Properties.ID);
		int nChildren = children.size();
		
		applyProperties(gc);
		
		double positionRoot = location.getX() + rootPosition;
		double leftRootFig = positionRoot - rootFigure.minSize.getX()/2;
		
		if(debug)System.err.printf("draw %s, %f, %f, rootFig at %f, %f\n", id, location.getX(), location.getY(), leftRootFig, location.getY());
		
		if(nChildren == 0)
			return;
		
		double bottomRootFig = location.getY() + rootFigure.minSize.getY();
		double vgap          = prop.getReal(Properties.VGAP);
		double childTop      = bottomRootFig + vgap; 
		double horLine       = bottomRootFig + vgap/2;
		
		// Vertical line from bottom of root figure to horizontal line
		gc.line(positionRoot, bottomRootFig, positionRoot, horLine);
		
		// Horizontal line connecting all the children
		if(nChildren > 1)
			gc.line(location.getX() + childRoot[0], horLine, location.getX() + childRoot[nChildren-1], horLine);
	
		// TODO line style!
		
		for(int i = 0; i < nChildren; i++){
			TreeNode child = children.get(i);
			double positionChild = location.getX() + childRoot[i];
			if(debug)System.err.printf("draw %s, child %d at posChild=%f, widthChild=%f, posRoot=%f\n", id, i, positionChild, child.minSize.getX(), child.rootPosition, childTop);

			// Vertical line from horizontal line to location.getY() of this child
			gc.line(positionChild, horLine, positionChild, childTop);
		}
		
	}
	
	
//	@Override
//	public boolean mouseInside(double mousex, double mousey){
//		return rootFigure.mouseInside(mousex, mousey);
//	}



	@Override
	public void computeMinSize() {
		resizable.set(false, false);
	}

	@Override
	public void resizeElement(Rectangle view) {
		
		rootFigure.location.setX(rootPosition);
		rootFigure.location.setY(0);
		
		double bottomRootFig = rootFigure.minSize.getY();
		double vgap          = prop.getReal(Properties.VGAP);
		double childTop      = bottomRootFig + vgap; 
		
		for(int i = 0; i < children.size(); i++){
			children.get(i).location.set(childRoot[i], childTop);
			children.get(i).size.set(children.get(i).minSize);
			System.err.printf("resizeElement: %s\n", children.get(i).location);
		}
		
	}

	public void completeChildren() {
		super.children = new Figure[children.size() + 1];
		for(int i = 0; i < children.size(); i++){
			super.children[i] = children.get(i);
			children.get(i).completeChildren();
		}
		super.children[children.size()] = rootFigure;
		
	}
}
