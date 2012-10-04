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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
			result = ((IConstructor) tree.accept(new PushDownTreeVisitor(false)));
		} catch (VisitorException e) {
			// ignore
		}
		
		return result;
	}
		
	private static class PushDownTreeVisitor extends TreeVisitor {
		
		final static private IValueFactory VF = ValueFactoryFactory.getValueFactory();
		
		final private boolean addBreakable;
		
		public PushDownTreeVisitor(final boolean addBreakable) {
			this.addBreakable = addBreakable; 
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
			
			if (TreeAdapter.isAppl(arg) 
					&& !ProductionAdapter.isLayout(prod)
					&& !ProductionAdapter.isLiteral(prod) 
					&& !ProductionAdapter.isCILiteral(prod)
					&& !ProductionAdapter.isLexical(prod)) {
				
				boolean isList = ProductionAdapter.isSeparatedList(prod) || ProductionAdapter.isList(prod);

				// 1: does current production application need an annotation?
				if (hasBreakableAttributeTag(prod) || addBreakable && !isList) {
										
					final Map<String,IValue> annotations = arg.getAnnotations();
					annotations.put("breakable", VF.bool(true));			
					
					arg = arg.setAnnotations(annotations);					
				}
				
				// 2: push-down deferred production names.
				Set<Integer> pushdownPositions = getChildProductionPositionsForPushdown(prod);				
											
				// update children by recursively applying this visitor.
				IListWriter writer = Factory.Args.writer(VF);

				Iterator<IValue> iter = TreeAdapter.getArgs(arg).iterator();
				for (Integer pos = 0; iter.hasNext(); pos++) {
					
					boolean isDeferred = pushdownPositions.contains(pos) || addBreakable && isList;
					
					IValue oldKid = iter.next();
					IValue newKid = oldKid.accept(new PushDownTreeVisitor(isDeferred));
					
					writer.append(newKid);
				}

				IList children = writer.done(); 

				// update current tree with processed children
				arg = TreeAdapter.setArgs(arg, children);
			}
		
			return arg;
		}			
		
		/**
		 * @return <code>true</code> if has breakable tag, otherwise <code>false</code>
		 */
		private static boolean hasBreakableAttributeTag(IConstructor production) {
			ISet attributes = ProductionAdapter.getAttributes(production);
			return attributes != null
					&& attributes.contains(VF.constructor(Factory.Attr_Tag,VF.node("breakable")));
		}		
			
		private static String[] getChildProductionNamesForPushDown(IConstructor production) {
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
		
		private static Set<Integer> getChildProductionPositionsForPushdown(IConstructor production) {
			Set<Integer> result = new HashSet<Integer>();

			Set<String> pushDownProductionNames = new HashSet<String>(
					Arrays.asList(getChildProductionNamesForPushDown(production)));

			if (!pushDownProductionNames.isEmpty()) {
			
				Iterator<IValue> iter = ProductionAdapter.getSymbols(production).iterator();
				for (Integer pos = 0; iter.hasNext(); pos++) {
	
					IValue kidValue = iter.next();
	
					if (kidValue.getType().isAbstractDataType() && !kidValue.getType().isVoidType()) {
						IConstructor kidConstructor = (IConstructor)kidValue;
						
						if (kidConstructor.getName().equals("label")) {
							
							String labelName = ((IString) kidConstructor.get(0)).getValue();
							
							if (pushDownProductionNames.contains(labelName)) {
								result.add(pos);
							}
						}
					}
				}
			}			
			return result;
		}
		
	}
	
}