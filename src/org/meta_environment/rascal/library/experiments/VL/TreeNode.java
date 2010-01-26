package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

import processing.core.PApplet;

public class TreeNode extends VELEM {
	
	VELEM velemNode;
	private ArrayList<TreeNode> children;
	private ArrayList<PropertyManager> edgeProperties;
	private static boolean debug = false;
	
	public TreeNode(VLPApplet vlp, PropertyManager inheritedProps, IList props,
			VELEM ve, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		velemNode = ve;
		children = new ArrayList<TreeNode>();
		edgeProperties = new ArrayList<PropertyManager>();
	}
	
	public void addChild(PropertyManager inheritedProps, IList props,
			TreeNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
		edgeProperties.add(new PropertyManager(null, inheritedProps, props, ctx));
	}
	
	@Override
	public void bbox(float left, float top){
	}
	
	float shapeTree(float left, float top, TreeNodeRaster raster) {
		int ileft = PApplet.round(left);
		int itop = PApplet.round(top);
		velemNode.bbox(ileft,itop);
		this.left = ileft;
		this.top = itop;
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		float position = left + velemNode.width/2; // x position of center of node!
		position = raster.leftMostPosition(position, top, velemNode.width, velemNode.height, hgap);
		
		int nChildren = children.size();
		height = velemNode.height;
		float heightChildren = 0;
		if(nChildren > 0){
			for(TreeNode child : children){
				child.velemNode.bbox();
				heightChildren = max(heightChildren, child.height);
			}
			height += heightChildren + vgap;
			if(nChildren > 1){
				width = (children.get(0).velemNode.width + children.get(nChildren-1).velemNode.width)/2 +
				        (nChildren-1) * hgap;
				for(int i = 1; i < nChildren - 1; i++){
					width += children.get(i).velemNode.width;
				}
			} else {
				width = 0;
			}
			float branchPosition = position - width/2;
			
			float leftPosition = children.get(0).shapeTree(branchPosition, top + velemNode.height + hgap, raster);
			
			float rightPosition = leftPosition;
			
			for(int i = 1; i < nChildren; i++){
				branchPosition += hgap + (children.get(i-1).velemNode.width + 
						                  children.get(i).velemNode.width)/2;
				rightPosition = children.get(i).shapeTree(branchPosition, top + velemNode.height + hgap, raster);
			}
			
			position = (leftPosition + rightPosition)/2;
		} else
			width = velemNode.width;
	
		raster.add(position, top, velemNode.width, velemNode.height);
		this.left = velemNode.left = PApplet.round(position - velemNode.width/2);
		return position;
	}

	@Override
	void draw() {
		boolean squareStyle = true;
		applyProperties();
		velemNode.draw();
		
		int n = children.size();
		
		if(n > 0){
			float nodeBottomX = velemNode.left + velemNode.width/2;
			float nodeBottomY = top + velemNode.height;
			float vgap = getVGapProperty();
			final float childTop = nodeBottomY + vgap;
			float horLineY = nodeBottomY + vgap/2;
			if(squareStyle){
				
				vlp.line(nodeBottomX, nodeBottomY, nodeBottomX, horLineY);
				
				if(n > 1){
					VELEM leftVE = children.get(0).velemNode;
					VELEM rightVE = children.get(n-1).velemNode;
					vlp.line(leftVE.left + leftVE.width/2, horLineY, rightVE.left + rightVE.width/2, horLineY);
				}
			}
			// TODO line style!
			for(TreeNode child : children){
				if(!squareStyle)
					vlp.line(nodeBottomX, nodeBottomY, child.velemNode.left + child.velemNode.width/2, childTop);
				float midChild = child.velemNode.left + child.velemNode.width/2;
				
				vlp.line(midChild, child.top, midChild, horLineY);
				child.draw();
			}
		}
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		if(debug)System.err.printf("TreeNode.mouseover: %d, %d\n", mousex, mousey);
		return velemNode.mouseOver(mousex, mousey);
	}

}
