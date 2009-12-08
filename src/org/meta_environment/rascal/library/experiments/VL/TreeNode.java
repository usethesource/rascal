package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class TreeNode extends VELEM {
	
	VELEM velemNode;
	private ArrayList<TreeNode> children;
	private ArrayList<PropertyManager> edgeProperties;
	private boolean hasParent = false;
	
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
		edgeProperties.add(new PropertyManager(inheritedProps, props, ctx));
	}
	
	public void setParent(){
		if(hasParent){
			System.err.println("NOT A TREE");
		}
		hasParent = true;
	}
	
	public boolean hasParent(){
		return hasParent;
	}
	
	@Override
	public void bbox(float left, float top){
		shapeTree(left, top);
	}
	
	float shapeTree(float left, float top) {
		velemNode.bbox(left,top);
		this.left = left;
		this.top = top;
		int gap = getGapProperty();
		float position = SimpleTree.raster.leftMostPosition(left, top, velemNode.width, velemNode.height, left > 0 ? gap : 0);
		
		int nChildren = children.size();
		height = velemNode.height;
		float heightChildren = 0;
		if(nChildren > 0){
			for(TreeNode child : children){
				child.velemNode.bbox();
				heightChildren = max(heightChildren, child.height);
			}
			height += heightChildren + gap;
			if(nChildren > 1){
				width = (children.get(0).velemNode.width + children.get(nChildren-1).velemNode.width)/2 +
				        (nChildren-1) * gap;
				for(int i = 1; i < nChildren - 1; i++){
					width += children.get(i).velemNode.width;
				}
			} else {
				width = 0;
			}
			float branchPosition = position - width/2;
			
			float leftPosition = children.get(0).shapeTree(branchPosition, top + velemNode.height + gap);
			
			float rightPosition = leftPosition;
			
			for(int i = 1; i < nChildren; i++){
				branchPosition += gap + (children.get(i-1).velemNode.width + 
						                  children.get(i).velemNode.width)/2;
				rightPosition = children.get(i).shapeTree(branchPosition, top + velemNode.height + gap);
			}
			
			position = (leftPosition + rightPosition)/2;
		} else
			width = velemNode.width;
	
		SimpleTree.raster.add(position, top, velemNode.width, velemNode.height);
		position -= velemNode.width/2;
		this.left = position;
		velemNode.left = position;
		return position;
	}

	@Override
	void draw() {
		applyProperties();
		int n = children.size();
		
		System.err.printf("TreeNode.draw: %f, %f, width=%f, height=%f\n", left, top, width, height);
		float nodeBottomX = velemNode.left + velemNode.width/2;
		float nodeBottomY = top + velemNode.height;
		
		velemNode.draw();
		
		if(n > 0){
			final float childTop = nodeBottomY + getGapProperty();
			
			for(TreeNode child : children){
				vlp.line(nodeBottomX, nodeBottomY, child.velemNode.left + child.velemNode.width/2, childTop);
				child.draw();
			}
		}
	}

}
