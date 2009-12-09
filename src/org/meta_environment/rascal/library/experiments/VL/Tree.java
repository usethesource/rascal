package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Tree extends VELEM {
	protected HashMap<String,TreeNode> nodeMap;
	private HashSet<TreeNode> hasParent;
	private TreeNodeRaster raster;
	TreeNode root = null;
	
	Tree(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);		
		nodeMap = new HashMap<String,TreeNode>();
		hasParent = new HashSet<TreeNode>();
		raster = new TreeNodeRaster();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			VELEM ve = VELEMFactory.make(vlp, c, properties, ctx);
			String name = ve.getIdProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			TreeNode tn = new TreeNode(vlp, inheritedProps, props, ve, ctx);
			nodeMap.put(name, tn);
		}

		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			String from = ((IString)c.get(1)).getValue();
			
			TreeNode fromNode = nodeMap.get(from);
			if(fromNode == null)
					throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			String to = ((IString)c.get(2)).getValue();
			TreeNode toNode = nodeMap.get(to);
			if(toNode == null)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			if(hasParent.contains(toNode))
				System.err.println("NOT A TREE");
			hasParent.add(toNode);
			fromNode.addChild(properties, (IList) c.get(0), toNode, ctx);
		}
		root = null;
		for(TreeNode node : nodeMap.values()){
			if(!hasParent.contains(node)){
				if(root != null)
					System.err.println("TREE HAS MULTIPLE ROOTS");
				root = node;
			}
		}
	}
	
	@Override
	void bbox() {
		raster.clear();
		root.shapeTree(0, 0, raster);
	}

	@Override
	void bbox(float left, float top) {
		raster.clear();
		root.shapeTree(left, top, raster);
	}
	
	@Override
	void draw() {
		root.draw();
	}
	
	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		applyProperties();
		root.draw();
	}

}
