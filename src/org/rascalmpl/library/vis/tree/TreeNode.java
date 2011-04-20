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

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

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
	private float[] childRoot;                // Root position of each child
	private float rootPosition;               // Root position of this TreeNode (= middle of rootFigure)
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
	
	public float leftExtent(){
		return rootPosition;
	}
	
	/*
	 * Distance between rootPosition and rightmost border of this node
	 */
	
	public float rightExtent(){
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
	float shapeTree(float rootMidX, float rootTop, TreeNodeRaster raster) {
        String id = rootFigure.getIdProperty();
		if(debug)System.err.printf("shapeTree: id=%s, rootMidX=%f, rootTop=%f\n", id, rootMidX, rootTop);
		rootFigure.bbox(AUTO_SIZE, AUTO_SIZE);
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		
		// Initial placement of figure of this TreeNode
		float position = raster.leftMostPosition(rootMidX, rootTop, rootFigure.width, rootFigure.height, hgap);
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
			
			float branchPosition = position;
			
			if(nChildren > 1){
				float widthDirectChildren = (children.get(0).rootFigure.width + children.get(nChildren-1).rootFigure.width)/2 +
				                            (nChildren-1) * hgap;
				for(int i = 1; i < nChildren - 1; i++){
					widthDirectChildren += children.get(i).rootFigure.width;
				}
				branchPosition = position - widthDirectChildren/2; 		// Position of leftmost child
			}
			
			float childTop = rootTop + rootFigure.height + vgap;         // Top of all children
			 
			childRoot = new float[nChildren];
			
			// Place leftmost child
			float leftPosition = childRoot[0] = children.get(0).shapeTree(branchPosition, childTop, raster);
			float rightPosition = leftPosition;
			float heightChildren = children.get(0).height;
			float rightExtentChildren = leftPosition + children.get(0).rightExtent();
			
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
	void bbox(float desiredWidth, float desiredHeight) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public
	void draw(float left, float top){
		
		this.setLeft(left);
		this.setTop(top);
	
		if(!isVisible())
			return;
		
		String id = rootFigure.getIdProperty();
		int nChildren = children.size();
		
		applyProperties();
		
		float positionRoot = left + rootPosition;
		float leftRootFig = positionRoot - rootFigure.width/2;
		
		if(debug)System.err.printf("draw %s, %f, %f, rootFig at %f, %f\n", id, left, top, leftRootFig, top);
		
		// Draw the root figure
		rootFigure.draw(leftRootFig, top);
		
		if(nChildren == 0 || !isNextVisible())
			return;
		
		fpa.incDepth();
		
		float bottomRootFig = top + rootFigure.height;
		float vgap          = getVGapProperty();
		float childTop      = bottomRootFig + vgap; 
		float horLine       = bottomRootFig + vgap/2;
		
		// Vertical line from bottom of root figure to horizontal line
		fpa.line(positionRoot, bottomRootFig, positionRoot, horLine);
		
		// Horizontal line connecting all the children
		if(nChildren > 1)
			fpa.line(left + childRoot[0], horLine, left + childRoot[nChildren-1], horLine);
	
		// TODO line style!
		
		for(int i = 0; i < nChildren; i++){
			TreeNode child = children.get(i);
			float positionChild = left + childRoot[i];
			if(debug)System.err.printf("draw %s, child %d at posChild=%f, widthChild=%f, posRoot=%f\n", id, i, positionChild, child.width, child.rootPosition, childTop);

			// Vertical line from horizontal line to top of this child
			fpa.line(positionChild, horLine, positionChild, childTop);
			child.draw(positionChild - child.leftExtent(), childTop);
		}
		
		fpa.decDepth();
	}
	
//	@Override
//	public boolean mouseInside(int mousex, int mousey){
//		return mousex > left && mousex < left + width &&
//				mousey > top && mousey < top + height;
//	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey){
		return rootFigure.mouseInside(mousex, mousey);
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey, float centerX, float centerY){
		return rootFigure.mouseInside(mousex, mousey, rootFigure.getCenterX(), rootFigure.getCenterY());
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX, float centerY, boolean mouseInParent){
		if(debug)System.err.printf("TreeNode.mouseover: %d, %d\n", mousex, mousey);
		if(debug)System.err.printf("TreeNode.mouseover: left=%f, top=%f\n", getLeft(), getTop());
		if(rootFigure.mouseOver(mousex, mousey, false))
			return true;
		for(TreeNode child : children)
			if(child.mouseOver(mousex, mousey, false))
				return true;
		return super.mouseOver(mousex, mousey, centerX, centerY, mouseInParent);
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey, Object e){
		if(debug)System.err.printf("TreeNode.mousePressed: %s, %d, %d\n", rootFigure.getIdProperty(), mousex, mousey);
		if(rootFigure.mousePressed(mousex, mousey, e))
			return true;
		for(TreeNode child : children)
			if(child.mousePressed(mousex, mousey, e))
				return true;
		if(debug)System.err.printf("TreeNode.mousePressed: %s, %d, %d, trying outer bounds with left corner: %f, %f\n", rootFigure.getIdProperty(), mousex, mousey, getLeft(), getTop());
		return super.mousePressed(mousex, mousey, e);
	}
	
//	@Override
//	public boolean mouseDragged(int mousex, int mousey){
//		if(debug)System.err.printf("TreeNode.mouseDragged: %d, %d\n", mousex, mousey);
//		for(TreeNode child : children)
//			if(child.mouseDragged(mousex, mousey))
//				return true;
//		if(debug)System.err.println("TreeNode.mouseDragged: children do not match\n");
//		if(mouseInside(mousex, mousey)){
//			fpa.registerFocus(this);
//			drag(mousex, mousey);
//			return true;
//		}
//		return false;
//	}
}
