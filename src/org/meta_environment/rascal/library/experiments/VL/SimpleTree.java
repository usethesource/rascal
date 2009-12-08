package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class SimpleTree extends VELEM {
	protected HashMap<String,TreeNode> nodeMap;
	static protected TreeNodeRaster raster = new TreeNodeRaster();
	TreeNode root = null;
	
	SimpleTree(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(vlp, inheritedProps, props, ctx);		
		nodeMap = new HashMap<String,TreeNode>();
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			VELEM ve = VELEMFactory.make(vlp, c, properties, ctx);
			String name = ve.getNameProperty();
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
			toNode.setParent();
			fromNode.addChild(properties, (IList) c.get(0), toNode, ctx);
		}
		
		for(TreeNode node : nodeMap.values()){
			// TODO check for multiple roots
			if(!node.hasParent()){
				root = node;
				break;
			}
		}
	}
	
	@Override
	void bbox() {
		root.bbox(0, 0);
	}

	@Override
	void bbox(float left, float top) {
		root.bbox(left, top);
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
