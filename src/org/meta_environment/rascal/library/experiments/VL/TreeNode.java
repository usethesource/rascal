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
	void bbox(float left, float top) {
		velemNode.bbox();
		this.left = left;
		this.top = top;
		int gap = getGapProperty();
		float cleft = SimpleTree.raster.leftMostPosition(left, top, velemNode.width, velemNode.height, gap);
		velemNode.bbox(cleft,top);

		if(children.size() > 0){
			
			float heightChildren = 0;
			float widthChildren = 0;
			float topChildren = velemNode.height + gap;
			for(int i = 0; i < children.size(); i++){
				TreeNode child = children.get(i);
				child.bbox(widthChildren, topChildren);
				float cleft2 = SimpleTree.raster.leftMostPosition(widthChildren, topChildren, child.width, child.height, i > 0 ? gap : 0);
				child.bbox(cleft2, topChildren);
				SimpleTree.raster.add(child);
				widthChildren = child.left + child.width;
				heightChildren = max(heightChildren, child.height);
			}
			width = max(widthChildren, velemNode.width);
			height = velemNode.height + gap + heightChildren;
			velemNode.left = left + (width - velemNode.width) / 2;
			velemNode.top = top;
		} else {
			width = velemNode.width;
			height = velemNode.height;
		}
		System.err.printf("bbox: %f, %f\n", width, height);
	}

	@Override
	void draw() {
		applyProperties();
		int n = children.size();
		
		System.err.printf("draw: %f, %f, width=%f, height=%f\n", left, top, width, height);
		float nodeBottomX;
		if(velemNode.width < width && n > 0){
			TreeNode rmChild = children.get(n-1);
			nodeBottomX = left + (rmChild.left + rmChild.width)/2;
		} else {
			nodeBottomX = left + velemNode.width/2;
		}
		float nodeBottomY = top + velemNode.height;
		
		velemNode.draw();
		
		if(n > 0){
			final float childTop = top + velemNode.height + getGapProperty();
			
			for(TreeNode child : children){
				vlp.line(nodeBottomX, nodeBottomY, child.left + child.width/2, childTop);
				child.draw();
			}
		}

	}

}
