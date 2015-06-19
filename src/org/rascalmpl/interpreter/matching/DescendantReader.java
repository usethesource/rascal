/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import java.util.Iterator;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class DescendantReader implements Iterator<IValue> {

	Stack<Object> spine = new Stack<Object>();

	private boolean debug = false;

	// is set to true when the descendant reader is allowed to skip layout nodes and do other optimizations
	private boolean interpretTree;
	
	DescendantReader(IValue val, boolean interpretTree){
		if (debug) {
			System.err.println("DescendantReader: " + val);
		}
		this.interpretTree = interpretTree;
		push(val);
	}

	public boolean hasNext() {
		while((spine.size() > 0) 
				&& (spine.peek() instanceof Iterator && !((Iterator<?>) spine.peek()).hasNext())){
			spine.pop();
		}		
		return spine.size() > 0;
	}
	
	public IValue next() {
		if (spine.peek() instanceof Iterator) {
			Iterator<?> iter = (Iterator<?>) spine.peek();
			
			if (!iter.hasNext()) {
				spine.pop();
				return next();
			}
			push((IValue) iter.next());
			return next();
		}
		return (IValue) spine.pop();
	}
	
	private void push(IValue v, Iterator<IValue> children){
		spine.push(v);
		spine.push(children);
	}
	
	private void push(IValue v){
		Type type = v.getType();
		if (type.isNode() || type.isConstructor() || type.isAbstractData()) {
			if (interpretTree && type.isSubtypeOf(RascalValueFactory.Tree)) {
				pushConcreteSyntaxNode((ITree) v);
				return;
			}
			INode node = (INode) v;
			push(v, node.getChildren().iterator());
			if (node.mayHaveKeywordParameters()) {
				pushKeywordParameters(node.asWithKeywordParameters());
			}
		} 
		else if(type.isList()) {
			push(v, ((IList) v).iterator());
		} 
		else if(type.isSet()) {
			push(v, ((ISet) v).iterator());
		} 
		else if(type.isMap()) {
			push(v, new MapKeyValueIterator((IMap) v));
		} 
		else if(type.isTuple()) {
			push(v, new TupleElementIterator((ITuple) v));
		} 
		else {
			spine.push(v);
		}
	}
	
	private void pushKeywordParameters(IWithKeywordParameters<? extends INode> node) {
		for (String name : node.getParameterNames()) {
			push(node.getParameter(name));
		}
	}

	private void pushConcreteSyntaxNode(ITree tree){
		if (debug) System.err.println("pushConcreteSyntaxNode: " + tree);
		
		if (TreeAdapter.isChar(tree) || TreeAdapter.isLiteral(tree) || TreeAdapter.isCILiteral(tree)) {
			spine.push(tree);
			return; // do not recurse
		}
		
		if (TreeAdapter.isAmb(tree)) {
			// only recurse
			for (IValue alt : TreeAdapter.getAlternatives(tree)) {
				pushConcreteSyntaxNode((ITree) alt);
			}
			return;
		}
			
		NonTerminalType ctype = (NonTerminalType) tree.getType();
		if (debug) System.err.println("ctype.getSymbol=" + ctype.getSymbol());
		IConstructor sym = ctype.getSymbol();
		
        if (SymbolAdapter.isAnyList(sym)) {
        	spine.push(tree);
        	
        	int delta = SymbolAdapter.getListSkipDelta(sym);
        	sym = SymbolAdapter.getSymbol(sym);

        	IList listElems = (IList) tree.get(1);
			if (debug) {
				for (int i = 0; i < listElems.length(); i++){
					System.err.println("#" + i + ": " + listElems.get(i));
				}
			}
        	
			for (int i = listElems.length() - 1; i >= 0 ; i -= delta){
				if (debug) System.err.println("adding: " + listElems.get(i));
				pushConcreteSyntaxNode((ITree)listElems.get(i));
			}
		} 
        else if (SymbolAdapter.isStartSort(sym)) {
        	pushConcreteSyntaxNode(TreeAdapter.getStartTop(tree));
        }
        else {
			if (debug) System.err.println("pushConcreteSyntaxNode: appl");
			/*
			 * appl(prod(...), [child0, layout0, child1, ...])
			 */
			spine.push(tree);
			IList applArgs = (IList) tree.get(1);
			int delta = (SymbolAdapter.isLex(sym)) ? 1 : 2;   // distance between elements
			
			for(int i = applArgs.length() - 1; i >= 0 ; i -= delta){
				//spine.push(applArgs.get(i));
				pushConcreteSyntaxNode((ITree) applArgs.get(i));
			}
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("remove from DescendantReader");
	}
}
