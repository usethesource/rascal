package org.rascalmpl.library.vis.tree;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.values.ValueFactoryFactory;

/**
 * Tree layout. Given a tree consisting of a list of nodes and edges, place them in a space conserving layout.
 * We use the algorithm described in:
 * E. M. Reingold, J. S. Tilford, Tidier Drawings of Trees, 
 * IEEE Transactions on Software Engineering, Volume 7 ,  Issue 2  (March 1981), Pages: 223-228  
 * 
 * and the improved algorithm described in:
 * A. Bloesch, Aesthetic Layout of Generalized Trees,
 * Software practice and Experience, Vol. 23(8), 817--827 (1993).
 * 
 * @author paulk
 *
 */
public class Tree extends Figure {
	protected HashMap<String,TreeNode> nodeMap;
	private HashSet<TreeNode> hasParent;
	private TreeNodeRaster raster;
	TreeNode root = null;
	
	public Tree(FigurePApplet fpa, IPropertyManager properties, IList nodes, IList edges, IEvaluatorContext ctx) {
		super(fpa, properties);		
		nodeMap = new HashMap<String,TreeNode>();
		hasParent = new HashSet<TreeNode>();
		raster = new TreeNodeRaster();
		
		// Construct TreeNodes
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			Figure fig = FigureFactory.make(fpa, c, properties, ctx);
			String name = fig.getIdProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.figureException("Tree: Missing id property in node", v, ctx.getCurrentAST(), ctx.getStackTrace());
			TreeNode tn = new TreeNode(fpa, properties, fig);
			nodeMap.put(name, tn);
		}
		
		// Construct Edges
		IValueFactory vf = ValueFactoryFactory.getValueFactory();
		IList emptyList = vf.list();

		for(IValue v : edges){
			IConstructor c = (IConstructor) v;
			int iFrom = 0;
			int iTo = 1;
			IList edgeProperties = c.arity() == 3 ?  (IList) c.get(2) : emptyList;
		
			String from = ((IString)c.get(iFrom)).getValue();

			TreeNode fromNode = nodeMap.get(from);
			if(fromNode == null)
				throw RuntimeExceptionFactory.figureException("Tree: edge uses non-existing node id " + from, v, ctx.getCurrentAST(), ctx.getStackTrace());
			String to = ((IString)c.get(iTo)).getValue();
			TreeNode toNode = nodeMap.get(to);
			if(toNode == null)
				throw RuntimeExceptionFactory.figureException("Tree: edge uses non-existing node id " + to, v, ctx.getCurrentAST(), ctx.getStackTrace());
			if(hasParent.contains(toNode))
				throw RuntimeExceptionFactory.figureException("Tree: node " + to + " has multiple parents", v, ctx.getCurrentAST(), ctx.getStackTrace());
			hasParent.add(toNode);
			fromNode.addChild(properties, edgeProperties, toNode, ctx);
		}
		
		root = null;
		for(TreeNode n : nodeMap.values())
			if(!hasParent.contains(n)){
				if(root != null)
					throw RuntimeExceptionFactory.figureException("Tree: multiple roots found: " + root.rootFigure.getIdProperty() + " and " + n.rootFigure.getIdProperty(),
																  edges, ctx.getCurrentAST(), ctx.getStackTrace());
				root = n;
			}
		if(root == null)
			throw RuntimeExceptionFactory.figureException("Tree: no root found", edges, ctx.getCurrentAST(), ctx.getStackTrace());
	}
	
	@Override
	public
	void bbox() {
		//System.err.printf("Tree.bbox()\n");
		raster.clear();
		root.shapeTree(0, 0, raster);
		width = root.width;
		height = root.height;
	}
	
	@Override
	public
	void draw(float left, float top) {
		if(!isNextVisible())
			return;
		this.setLeft(left);
		this.setTop(top);
		
		//System.err.printf("Tree.draw(%f,%f)\n", left, top);
		applyProperties();
		root.draw(left, top);
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey){
		return root.mouseInside(mousex, mousey) || 
		       super.mouseInside(mousex, mousey);
	}
	
	@Override
	public boolean mouseInside(int mousex, int mousey, float centerX, float centerY){
		return root.mouseInside(mousex, mousey, centerX, centerY) || 
		       super.mouseInside(mousex, mousey, centerX, centerY);
	}
	
	@Override
	public boolean mouseOver(int mousex, int mousey, float centerX, float centerY, boolean mouseInParent){
		return root.mouseOver(mousex, mousey, centerX, centerY, false) ||
		       super.mouseOver(mousex, mousey, centerX, centerY, mouseInParent);
	}
	
	@Override
	public boolean mousePressed(int mousex, int mousey, MouseEvent e){
		//System.err.printf("Tree.mousePressed: %s, %d, %d\n", root.rootFigure.getIdProperty(), mousex, mousey);
		return root.mousePressed(mousex, mousey, e) ||
			   super.mousePressed(mousex, mousey, e);
	}
	
//	@Override
//	public boolean mouseDragged(int mousex, int mousey){
//		return root.mouseDragged(mousex, mousey) ||
//		       super.mouseDragged(mousex, mousey);
//	}
}
