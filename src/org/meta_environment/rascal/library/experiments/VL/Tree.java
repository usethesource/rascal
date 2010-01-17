package org.meta_environment.rascal.library.experiments.VL;

import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;
import org.meta_environment.values.ValueFactoryFactory;

import processing.core.PApplet;

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
		
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		IList emptyList = vf.list();

		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			int iFrom;
			int iTo;
			IList edgeProperties;
			if(c.arity() == 3){
				edgeProperties = (IList) c.get(0);
				iFrom = 1;
				iTo = 2;
			} else {
				edgeProperties = emptyList;
				iFrom = 0;
				iTo = 1;
			}
			String from = ((IString)c.get(iFrom)).getValue();

			TreeNode fromNode = nodeMap.get(from);
			if(fromNode == null)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			String to = ((IString)c.get(iTo)).getValue();
			TreeNode toNode = nodeMap.get(to);
			if(toNode == null)
				throw RuntimeExceptionFactory.illegalArgument(v, ctx.getCurrentAST(), ctx.getStackTrace());
			if(hasParent.contains(toNode))
				System.err.println("NOT A TREE");
			hasParent.add(toNode);
			fromNode.addChild(properties, edgeProperties, toNode, ctx);
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
	void bbox(int left, int top) {
		raster.clear();
		root.shapeTree(left, top, raster);
	}
	
	@Override
	void draw() {
		root.draw();
	}
	
	@Override
	void draw(float left, float top) {
		this.left = PApplet.round(left);
		this.top = PApplet.round(top);
		applyProperties();
		root.draw();
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey){
		for(TreeNode node : nodeMap.values()){
			if(node.mouseOver(mousex, mousey))
				return true;
		}
		return false;
	}

}
