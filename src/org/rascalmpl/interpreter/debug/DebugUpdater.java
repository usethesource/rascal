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

import java.util.Map;

import org.eclipse.imp.pdb.facts.*;
import org.eclipse.imp.pdb.facts.io.*;
import org.eclipse.imp.pdb.facts.visitors.*;
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
		
		final private String[] __pushDownProductionsNames;
		
		public PushDownTreeVisitor(final String[] pushDownProductionNames) {
			__pushDownProductionsNames = pushDownProductionNames; 
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
			
			// 1: does current production application need an annotation?
			if (isBreakableDueTag(prod) || isBreakableDuePushdown(prod)) {
				
				final Map<String,IValue> annotations = arg.getAnnotations();
				annotations.put("breakable", VF.bool(true));			
				
				arg = arg.setAnnotations(annotations);
			}
			
			// 2: push-down deferred production names.
			if (TreeAdapter.isAppl(arg)) {				
				if (!ProductionAdapter.isLexical(prod)) {
				
					String[] newPushDownProductions = getChildProductionsNamesForPushDown(prod);				
												
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
		
		/**
		 * @return <code>true</code> if has breakable tag, otherwise <code>false</code>
		 */
		private boolean isBreakableDueTag(IConstructor production) {
			ISet attributes = ProductionAdapter.getAttributes(production);
			return attributes != null
					&& attributes.contains(VF.constructor(Factory.Attr_Tag,VF.node("breakable")));
		}		
		
		/**
		 * @return <code>true</code> if is in pushed-down list, otherwise <code>false</code>
		 */
		private boolean isBreakableDuePushdown(IConstructor production) {
			boolean result = false;
			final IList symbols = ProductionAdapter.getSymbols(production);

			if (__pushDownProductionsNames.length != 0 
					&& symbols != null && !symbols.isEmpty()) {
				String sortName = ProductionAdapter.getSortName(production);
				
				for (String productionName : __pushDownProductionsNames) {
					final IValue deferredBreakable = VF.constructor(
							Factory.Symbol_Label,
							VF.string(productionName),
							VF.constructor(Factory.Symbol_Sort,VF.string(sortName)));
					
					result = result || ProductionAdapter.getSymbols(production).contains(deferredBreakable);
				}
			}		
			
			return result;
		}
		
		private String[] getChildProductionsNamesForPushDown(IConstructor production) {
			ISet attributes = ProductionAdapter.getAttributes(production);

			String[] result = {};
			for (IValue attributeValue : attributes) {
				
				if (attributeValue.getType().isAbstractDataType() && !attributeValue.getType().isVoidType()) {
					IConstructor attributeConstructor = (IConstructor)attributeValue;

					if (attributeConstructor.getName().equals("tag")) {
				
						for (IValue childValue : attributeConstructor.getChildren()) {
							INode childNode = (INode)childValue;

							// non-empty breakable tag?
							if (childNode.getName().equals("breakable") 
									&& childNode.getChildren().iterator().hasNext()) {

								String c = ((IString)childNode.get(0)).getValue();
								String s = c.substring(1,c.length()-1);
								result = s.split(",");
							}
						}
					}
				}
			}
			return result;
		}
		
	}
	
}