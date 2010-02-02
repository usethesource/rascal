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

/**
 * Tree layout. Given a tree consisting of a list of nodes and edges, place them in a space conserving layout.
 * We use the algorithm described in:
 * E. M. Reingold, J. S. Tilford, Tidier Drawings of Trees, 
 * IEEE Transactions on Software Engineering, Volume 7 ,  Issue 2  (March 1981), Pages: 223-228  
 * 
 * @author paulk
 *
 */
public class Tree extends VELEM {
	protected HashMap<String,TreeNode> nodeMap;
	private HashSet<TreeNode> hasParent;
	private TreeNodeRaster raster;
	TreeNode root = null;
	
	Tree(VLPApplet vlp, PropertyManager inheritedProps, IList props, IList nodes, IList edges, IString rootName, IEvaluatorContext ctx) {
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
		
		root = nodeMap.get(rootName.getValue());
		
		if(root == null)
			throw RuntimeExceptionFactory.illegalArgument(rootName, ctx.getCurrentAST(), ctx.getStackTrace());
//		root = null;
//		for(TreeNode node : nodeMap.values()){
//			if(!hasParent.contains(node)){
//				if(root != null)
//					System.err.println("TREE HAS MULTIPLE ROOTS");
//				root = node;
//			}
//		}
	}
	
	@Override
	void bbox() {
		raster.clear();
		root.shapeTree(left, top, raster);
	}
	
	@Override
	void bbox(float left, float top) {
		raster.clear();
		root.shapeTree(left, top, raster);
	}
	
	@Override
	void draw(float left, float top) {
		this.left = left;
		this.top = top;
		applyProperties();
		root.draw(left, top);
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
