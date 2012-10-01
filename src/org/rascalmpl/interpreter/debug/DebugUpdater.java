/*******************************************************************************
 * Copyright (c) 2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.debug;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.values.uptr.ProductionAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;
import org.rascalmpl.values.uptr.visitors.TreeVisitor;

public class DebugUpdater {

	/**
	 * Transforms an <code>tree</tree> by pushing 'deferred breakable' annotations 
	 * from an production to the application of the corresponding child.
	 * 
	 * @param tree a parse tree
	 * @return tree with pushed-down attributes, unmodified tree in case of error
	 */
	public static IConstructor pushDownAttributes(IConstructor tree) {
		IConstructor result = tree;
				
		try {
			result = ((IConstructor) tree.accept(new PushDownTreeVisitor(new String[]{})));
		} catch (VisitorException e) {
			// ignore
		}
		
		return result;
	}
	
	private static class PushDownTreeVisitor extends TreeVisitor {
		
		final static private IValueFactory VF = ValueFactoryFactory.getValueFactory();
		
		final private String[] _pushDownProductions;
		
		public PushDownTreeVisitor(final String[] pushDownProductions) {
			_pushDownProductions = pushDownProductions; 
		}
		
		@Override
		public IConstructor visitTreeCycle(IConstructor arg)
				throws VisitorException {
			return arg;
		}
		
		@Override
		public IConstructor visitTreeChar(IConstructor arg) throws VisitorException {
			return arg;
		}

		@Override
		public IConstructor visitTreeAmb(IConstructor arg) throws VisitorException {
			return arg;
		}
		
		@Override
		public IConstructor visitTreeAppl(IConstructor arg) throws VisitorException {
			IConstructor prod = TreeAdapter.getProduction(arg);
			String[] newPushDownProductions = {};
			
			// 1. check if current node is in old push-down production names
			if (_pushDownProductions.length != 0) {
				String sortName = ProductionAdapter.getSortName(prod);
				
				boolean contains = false;
				for (String pdpName : _pushDownProductions) {
					contains = contains || ProductionAdapter.getSymbols(prod) != null && ProductionAdapter.getSymbols(prod).contains(VF.constructor(Factory.Symbol_Label, VF.string(pdpName), VF.constructor(Factory.Symbol_Sort, VF.string(sortName))));
				}
				
				if (contains) {				
					// update current tree with 'breakable' annotation
					prod = addBreakable(prod);
					arg = arg.set("prod", prod);
				}
			}
			
			// 2. query push-down production names		
			if (TreeAdapter.isAppl(arg)) {				
				if (!ProductionAdapter.isLexical(prod)) {
				
					newPushDownProductions = getChildProductionsNamesForPushDown((IConstructor) prod);				
												
					// update children by recursively applying this visitor.
					IListWriter writer = Factory.Args.writer(VF);
					
					for (IValue kid : TreeAdapter.getArgs(arg)) {
						IValue newKid = kid.accept(new PushDownTreeVisitor(newPushDownProductions));
						writer.append(newKid);
					}
	
					IList children = writer.done(); 
	
					// update current tree with processed children
					arg = TreeAdapter.setArgs(arg, children);
				}
			}
			
			return arg;
		}			
	
		// for production
		private static IConstructor addBreakable(IConstructor tree) {
			IValue attr = Factory.Attr_Tag.make(VF, VF.node("breakable"));

			if (tree.has("attributes")) {
				ISet oldSet = ProductionAdapter.getAttributes(tree);
				ISet newSet = oldSet.insert(attr);

				return tree.set("attributes", newSet);
			} else {
				return tree;
			}
		}		
				
		private String[] getChildProductionsNamesForPushDown(IConstructor production) {
			ISet attributes = ProductionAdapter.getAttributes(production);
			
			String[] tagStrings = {}; 
			for (IValue v : attributes) {
				if (v.getType().isAbstractDataType() && !v.getType().isVoidType()) {
					IConstructor vc = (IConstructor)v;
					if (vc.getName().equals("tag")) {
						for (IValue vcChild : vc.getChildren()) {
							INode ncChild = (INode) vcChild;
							
							if (ncChild.getName().equals("breakable")) {

								if (ncChild.getChildren().iterator().hasNext()) {
									String rawTagString = ((IString) ncChild.get(0)).getValue();
									String tagString = rawTagString.substring(1, rawTagString.length() - 1);
									tagStrings = tagString.split(",");
								}
							}
						}
					}
				}
			}
			
			return tagStrings;
		}
		
	}
	
}