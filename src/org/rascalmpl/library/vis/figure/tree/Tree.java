/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.library.vis.figure.tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;
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
	IFigureConstructionEnv fpa;
	
	public Tree(IFigureConstructionEnv fpa, PropertyManager properties, IList nodes, IList edges) {
		super(properties);		
		this.fpa = fpa;
		nodeMap = new HashMap<String,TreeNode>();
		hasParent = new HashSet<TreeNode>();
		raster = new TreeNodeRaster();
		
		// Construct TreeNodes
		for(IValue v : nodes){
			IConstructor c = (IConstructor) v;
			Figure fig = FigureFactory.make(fpa, c, properties, null);
			String name = fig.getIdProperty();
			if(name.length() == 0)
				throw RuntimeExceptionFactory.figureException("Tree: Missing id property in node", v, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
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
				throw RuntimeExceptionFactory.figureException("Tree: edge uses non-existing node id " + from, v, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
			String to = ((IString)c.get(iTo)).getValue();
			TreeNode toNode = nodeMap.get(to);
			if(toNode == null)
				throw RuntimeExceptionFactory.figureException("Tree: edge uses non-existing node id " + to, v, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
			if(hasParent.contains(toNode))
				throw RuntimeExceptionFactory.figureException("Tree: node " + to + " has multiple parents", v, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
			hasParent.add(toNode);
			fromNode.addChild(properties, edgeProperties, toNode, fpa.getRascalContext());
		}
		
		root = null;
		for(TreeNode n : nodeMap.values())
			if(!hasParent.contains(n)){
				if(root != null)
					throw RuntimeExceptionFactory.figureException("Tree: multiple roots found: " + root.rootFigure.getIdProperty() + " and " + n.rootFigure.getIdProperty(),
																  edges, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
				root = n;
			}
		if(root == null)
			throw RuntimeExceptionFactory.figureException("Tree: no root found", edges, fpa.getRascalContext().getCurrentAST(), fpa.getRascalContext().getStackTrace());
	}
	
	@Override
	public
	void bbox() {
		//System.err.printf("Tree.bbox()\n");
		raster.clear();
		root.shapeTree(0, 0, raster);
		minSize.setWidth(root.minSize.getWidth());
		minSize.setHeight(root.minSize.getHeight());
		setNonResizable();
	}
	
	@Override
	public
	void draw(GraphicsContext gc) {
		
		//System.err.printf("Tree.draw(%f,%f)\n", left, top);
		applyProperties(gc);
		root.draw(gc);
	}
	
	@Override
	public boolean mouseInside(double mousex, double mousey){
		return root.mouseInside(mousex, mousey) || 
		       super.mouseInside(mousex, mousey);
	}
	

	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		boolean ret = false;
		if(root!=null){
			ret = root.getFiguresUnderMouse(c, result);
		}
		if(mouseInside(c.getX(), c.getY())){
			result.add(this);
			ret=true;
		}
		return ret;
	}
	

	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		if(root!=null) root.registerNames(resolver);
	}

	@Override
	public void layout() {
		size.set(minSize);
		if(root!=null){
			root.setToMinSize();
			root.layout();
		}
	}
}
