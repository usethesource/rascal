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
package org.rascalmpl.values.iterators;

import java.util.Iterator;
import java.util.Stack;

import io.usethesource.capsule.Set;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.type.Type;

import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

public class DescendantReader implements Iterator<IValue> {

	Stack<Object> spine = new Stack<Object>();
	IValue nextValue;

	private Set.Transient<ITree> visitedAmbs = Set.Transient.of();

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

	private void findNext() {
		do {
			nextValue = null;

			if (spine.size() == 0) {
				return;
			}

			Object next = spine.peek();
			if (next instanceof Iterator) {
				Iterator<?> iter = (Iterator<?>) next;
				if (!iter.hasNext()) {
					spine.pop();
					continue;
				}

				push((IValue) iter.next());
				continue;
			}

			nextValue = (IValue) spine.pop();

		} while (nextValue == null);
	}

	public boolean hasNext() {
		if (nextValue == null) {
			findNext();
		}
		return nextValue != null;
	}
	
	public IValue next() {
		IValue next = nextValue;
		findNext();
		return next;
	}
	
	private void push(IValue v, Iterator<IValue> children){
		spine.push(v);
		spine.push(children);
	}

	private void pushAmb(ITree amb) {
		if (!visitedAmbs.contains(amb)) {
			visitedAmbs.__insert(amb);
			spine.push(amb);
			for (IValue alt : TreeAdapter.getAlternatives(amb)) {
				push(alt);
			}
		}
	}

	private void push(IValue v){
		Type type = v.getType();
		if (type.isNode() || type.isConstructor() || type.isAbstractData()) {
			if (type.isSubtypeOf(RascalValueFactory.Tree)) {
				ITree tree = (ITree) v;

				if (TreeAdapter.isAmb(tree)) {
					pushAmb(tree);
					return;
				}

				if (interpretTree) {
					pushConcreteSyntaxNode((ITree) tree);
					return;
				}
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
		
		if (TreeAdapter.isChar(tree) || TreeAdapter.isCycle(tree) || TreeAdapter.isLiteral(tree) || TreeAdapter.isCILiteral(tree)) {
			spine.push(tree);
			return; // do not recurse
		}
		
		if (TreeAdapter.isAmb(tree)) {
			pushAmb(tree);
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
