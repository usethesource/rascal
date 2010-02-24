package org.rascalmpl.library.viz.Figure;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;

import processing.core.PApplet;

/**
 * A TreeNode is created for each "node" constructor that occurs in the tree.
 * 
 * @author paulk
 *
 */
public class TreeNode extends Figure {
	
	Figure figureNode;
	private ArrayList<TreeNode> children;
	private ArrayList<PropertyManager> edgeProperties;
	private static boolean debug = false;
	
	public TreeNode(FigurePApplet vlp, PropertyManager inheritedProps, IList props,
			Figure ve, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		figureNode = ve;
		children = new ArrayList<TreeNode>();
		edgeProperties = new ArrayList<PropertyManager>();
	}
	
	public void addChild(PropertyManager inheritedProps, IList props,
			TreeNode toNode, IEvaluatorContext ctx) {
		children.add(toNode);
		edgeProperties.add(new PropertyManager(null, inheritedProps, props, ctx));
	}
	
	
	float shapeTree(float left, float top, TreeNodeRaster raster) {
		this.left = left;
		this.top = top;
		figureNode.bbox(left, top);
		float hgap = getHGapProperty();
		float vgap = getVGapProperty();
		float position = left + figureNode.width/2; // x position of center of node!
		position = raster.leftMostPosition(position, top, figureNode.width, figureNode.height, hgap);
		
		int nChildren = children.size();
		height = figureNode.height;
		float heightChildren = 0;
		if(nChildren > 0){
			for(TreeNode child : children){
				child.figureNode.bbox();
				heightChildren = max(heightChildren, child.height);
			}
			height += heightChildren + vgap;
			if(nChildren > 1){
				width = (children.get(0).figureNode.width + children.get(nChildren-1).figureNode.width)/2 +
				        (nChildren-1) * hgap;
				for(int i = 1; i < nChildren - 1; i++){
					width += children.get(i).figureNode.width;
				}
			} else {
				width = 0;
			}
			float branchPosition = position - width/2;
			
			float leftPosition = children.get(0).shapeTree(branchPosition, top + figureNode.height + hgap, raster);
			
			float rightPosition = leftPosition;
			
			for(int i = 1; i < nChildren; i++){
				branchPosition += hgap + (children.get(i-1).figureNode.width + 
						                  children.get(i).figureNode.width)/2;
				rightPosition = children.get(i).shapeTree(branchPosition, top + figureNode.height + hgap, raster);
			}
			
			position = (leftPosition + rightPosition)/2;
		} else
			width = figureNode.width;
	
		raster.add(position, top, figureNode.width, figureNode.height);
		this.left = figureNode.left = PApplet.round(position - figureNode.width/2);
		return position;
	}
	
	@Override
	void bbox() {
		// TODO Auto-generated method stub
	}

	@Override
	void draw(float left, float top) {
		boolean squareStyle = true;
		applyProperties();
		figureNode.draw();
		
		int n = children.size();
		
		if(n > 0){
			float nodeBottomX = figureNode.left + figureNode.width/2;
			float nodeBottomY = top + figureNode.height;
			float vgap = getVGapProperty();
			final float childTop = nodeBottomY + vgap;
			float horLineY = nodeBottomY + vgap/2;
			if(squareStyle){
				
				vlp.line(nodeBottomX, nodeBottomY, nodeBottomX, horLineY);
				
				if(n > 1){
					Figure leftVE = children.get(0).figureNode;
					Figure rightVE = children.get(n-1).figureNode;
					vlp.line(leftVE.left + leftVE.width/2, horLineY, rightVE.left + rightVE.width/2, horLineY);
				}
			}
			// TODO line style!
			for(TreeNode child : children){
				if(!squareStyle)
					vlp.line(nodeBottomX, nodeBottomY, child.figureNode.left + child.figureNode.width/2, childTop);
				float midChild = child.figureNode.left + child.figureNode.width/2;
				
				vlp.line(midChild, child.top, midChild, horLineY);
				child.draw();
			}
		}
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		if(debug)System.err.printf("TreeNode.mouseover: %d, %d\n", mousex, mousey);
		return figureNode.mouseOver(mousex, mousey);
	}

	

}
