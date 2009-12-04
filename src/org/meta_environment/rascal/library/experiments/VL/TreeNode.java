package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class TreeNode extends VELEM {
	
	private VELEM velemNode;
	private ArrayList<TreeNode> children;
	private ArrayList<PropertyManager> edgeProperties;
	private boolean hasParent = false;
	float widthLeftSiblings;
	
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
	void bbox() {
		velemNode.bbox();
		if(children.size() > 0){
			float widthChildren = widthLeftSiblings = 0;
			int gap = getGapProperty();
			float heightChildren = 0;
			TreeNode leftSibling = null;
			for(TreeNode child: children){
				child.bbox();
				if(leftSibling != null){
					float w = leftSibling.widthAtHeight(child.height);
					System.err.printf("w=%f, leftSibling.width=%f\n", w, leftSibling.width);
					widthChildren += w - leftSibling.width + gap;
					child.widthLeftSiblings = widthChildren;
					widthChildren += child.width;
				} else {
					child.widthLeftSiblings = 0;
					widthChildren += child.width;
				}
				heightChildren = max(heightChildren, child.height);
				leftSibling = child;
			}
			if(leftSibling != null)
				leftSibling.widthLeftSiblings = widthChildren - leftSibling.width;
	
			width = max(widthChildren, velemNode.width);
			height = velemNode.height + gap + heightChildren;
		} else {
			width = velemNode.width;
			height = velemNode.height;
			widthLeftSiblings = 0;
		}
		System.err.printf("bbox: %f, %f, widthLeftSiblings=%f\n", width, height, widthLeftSiblings);
	}
	
	public float widthAtHeight(float h){
		System.err.printf("widthAtHeight(%f)\n", h);
		int n = children.size();
		if(h <= velemNode.height || n == 0)
			if(velemNode.width >= width)
				return widthLeftSiblings + velemNode.width;
			else
				return widthLeftSiblings + (width - velemNode.width)/ 2 + velemNode.width;
		
		TreeNode rmChild = children.get(n-1);
		float widthDirectChildren = rmChild.widthLeftSiblings + rmChild.width;
		
		if(velemNode.width > widthDirectChildren)
			return widthLeftSiblings + velemNode.width;
		return rmChild.widthAtHeight(h - velemNode.height - getGapProperty());
	}

	@Override
	void draw(float left, float top) {
		
		this.left = left;
		this.top = top;
		applyProperties();
		int n = children.size();
		
		System.err.printf("draw %s: %f, %f, width=%f, height=%f, widthLeftSiblings=%f\n", getNameProperty(), left, top, width, height, widthLeftSiblings);
		float nodeBottomX;
		float nodeBottomY = top + velemNode.height;
		if(velemNode.width < width && n > 0){
			TreeNode rmChild = children.get(n-1);
			float widthDirectChildren = rmChild.widthLeftSiblings + rmChild.width;
			velemNode.draw(left + (widthDirectChildren - velemNode.width)/2, top);
			nodeBottomX = left + widthDirectChildren/2;
		} else {
			velemNode.draw(left, top);
			nodeBottomX = left + velemNode.width/2;
		}
		if(n > 0){
			int gap = getGapProperty();
	
			float childTop = top + velemNode.height + gap;
			
			for(TreeNode child : children){
				float childLeft = left + child.widthLeftSiblings;
				vlp.line(nodeBottomX, nodeBottomY, childLeft + child.width/2, childTop);
				child.draw(childLeft, childTop);
			}
		}

	}

}
