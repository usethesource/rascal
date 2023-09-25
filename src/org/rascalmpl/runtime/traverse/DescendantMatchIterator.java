package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.traverse;

import java.util.Iterator;
import java.util.Stack;

import org.rascalmpl.types.DefaultRascalTypeVisitor;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalType;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IList;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.INode;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ITuple;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.type.Type;

import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.iterators.MapKeyValueIterator;
import org.rascalmpl.values.iterators.TupleElementIterator;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;
import org.rascalmpl.values.parsetrees.TreeAdapter;

public class DescendantMatchIterator implements Iterator<IValue>, Iterable<IValue> {

	final Stack<Object> spine = new Stack<Object>();

	private final boolean debug = false;
	private final IDescendantDescriptor descriptor;

	private final boolean concreteMatch; // true when matching concrete patterns
	
	public DescendantMatchIterator(IValue val, IDescendantDescriptor descriptor){
		if (debug) {
			System.err.println("DescendantReader: " + val);
		}
		this.concreteMatch = descriptor.isConcreteMatch();
		this.descriptor = descriptor;
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
	
	private void push(final IValue v, final Iterator<IValue> children){
		spine.push(v);
		spine.push(children);
	}
	
	private void push(final IValue v){
		if(concreteMatch){
			if(v.getType().isSubtypeOf(RascalValueFactory.Tree)){
				pushConcreteSyntaxNode((ITree) v);
			}
			return;
		}
		if(descriptor.shouldDescentInAbstractValue(v).getValue()){	
			if(v.getType().accept(new DefaultRascalTypeVisitor<Boolean,RuntimeException>(false) {
				@Override
				public Boolean visitList(final Type type) throws RuntimeException {
					IList lst = (IList) v;
					int len = lst.length();
					if(len == 0){
						spine.push(lst);
					} else if(len == 1){
						spine.push(lst);
						push(lst.get(0));
					} else {
						push(lst, lst.iterator()); 
					}
					return true;
				}
				@Override
				public Boolean visitSet(final Type type) throws RuntimeException {
					ISet set = (ISet) v;
					if(set.isEmpty()){
						spine.push(set);
					} else {
						push(set, set.iterator()); 
					}
					return true;
				}
				@Override
				public Boolean visitMap(final Type type) throws RuntimeException {
					IMap map = (IMap) v;
					if(map.isEmpty()){
						spine.push(map);
					} else {
						push(map, new MapKeyValueIterator(map));
					}
					return true;
				}
				@Override
				public Boolean visitTuple(final Type type) throws RuntimeException {
					push(v, new TupleElementIterator((ITuple) v));  
					return true;
				}
				@Override
				public Boolean visitNode(final Type type) throws RuntimeException {
					INode node = (INode) v;
					int arity = node.arity();
					if(arity == 0){
						spine.push(node);
					} else if(arity == 1){
						spine.push(node);
						push(node.get(0));
					} else {
						push(node, node.getChildren().iterator());
					}

					if (node.mayHaveKeywordParameters()) {
						IWithKeywordParameters<? extends INode> nodeKW = node.asWithKeywordParameters();
						for (String name : nodeKW.getParameterNames()) {
							push(nodeKW.getParameter(name));
						}
					} 
					return true;
				}
				@Override
				public Boolean visitConstructor(final Type type) throws RuntimeException {
					return visitNode(type);
				}
				@Override
				public Boolean visitAbstractData(final Type type) throws RuntimeException {
					return visitNode(type);
				}
				@Override
				public Boolean visitNonTerminal(final RascalType type) throws RuntimeException {
					return visitNode(type);
				}
			})){
				return;
			}
		} 
		spine.push(v);
	}

	private void pushConcreteSyntaxNode(final ITree tree){
		if (debug) System.err.println("pushConcreteSyntaxNode: " + tree);

		if (TreeAdapter.isAmb(tree)) {
			// only recurse
			for (IValue alt : TreeAdapter.getAlternatives(tree)) {
				pushConcreteSyntaxNode((ITree) alt);
			}
			return;
		}
		if(descriptor.shouldDescentInConcreteValue(tree).getValue()){
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
					pushConcreteSyntaxNode((ITree) applArgs.get(i));
				}
			}
		} else {
			spine.push(tree);
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("remove from DescendantMatchIterator");
	}

	@Override
	public Iterator<IValue> iterator() {
		return this;
	}
}
