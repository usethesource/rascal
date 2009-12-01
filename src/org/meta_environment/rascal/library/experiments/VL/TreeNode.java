package org.meta_environment.rascal.library.experiments.VL;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IList;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;

public class TreeNode  extends VELEM {
	
	private VELEM velem;
	private ArrayList<TreeNode> children;
	private ArrayList<PropertyManager> edgeProperties;
	private boolean hasParent = false;
	
	public TreeNode(VLPApplet vlp, PropertyManager inheritedProps, IList props,
			VELEM ve, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);
		velem = ve;
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
		velem.bbox();
		width = 0;
		height = 0;
		for(TreeNode child: children){
			child.bbox();
			width += child.width;
			height = max(height, child.height);
		} 
		int gap = getGapProperty();
		int gaps = (children.size() - 1) * gap;
		width += gaps;
		width = max(width, velem.width);
		if(height > 0){
			height += velem.height + gap;
		}
	}

	@Override
	void draw(float left, float top) {
		// TODO Auto-generated method stub
		
	}


		

}
