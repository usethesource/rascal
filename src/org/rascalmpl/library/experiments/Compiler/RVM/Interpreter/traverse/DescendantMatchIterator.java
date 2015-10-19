package org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.traverse;

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
import org.rascalmpl.interpreter.matching.MapKeyValueIterator;
import org.rascalmpl.interpreter.matching.TupleElementIterator;
import org.rascalmpl.interpreter.types.DefaultRascalTypeVisitor;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.types.RascalType;
import org.rascalmpl.library.experiments.Compiler.RVM.Interpreter.ToplevelType;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;
import org.rascalmpl.values.uptr.SymbolAdapter;
import org.rascalmpl.values.uptr.TreeAdapter;

public class DescendantMatchIterator implements Iterator<IValue> {

	final Stack<Object> spine = new Stack<Object>();

	private final boolean debug = false;
	private final DescendantDescriptor descriptor;

	private final boolean concreteMatch; // true when matching concrete patterns
	
	public DescendantMatchIterator(IValue val, DescendantDescriptor descriptor){
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
					push(v, ((IList) v).iterator()); 
					return true;
				}
				@Override
				public Boolean visitSet(final Type type) throws RuntimeException {
					push(v, ((ISet) v).iterator()); 
					return true;
				}
				@Override
				public Boolean visitMap(final Type type) throws RuntimeException {
					push(v, new MapKeyValueIterator((IMap) v));  
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
					push(v, node.getChildren().iterator());
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
			} else {
				spine.push(v);
				return;
			}
			
			
			/*
			switch (ToplevelType.getToplevelType(v.getType())){
			
			case CONSTRUCTOR:
			case NODE:
			case ADT:
				INode node = (INode) v;
				push(v, node.getChildren().iterator());
				if (node.mayHaveKeywordParameters()) {
					IWithKeywordParameters<? extends INode> nodeKW = node.asWithKeywordParameters();
					for (String name : nodeKW.getParameterNames()) {
						push(nodeKW.getParameter(name));
					}
				}
				return;
			case LIST:
				push(v, ((IList) v).iterator()); 
				return;
			case SET:
				push(v, ((ISet) v).iterator()); 
				return;
			case MAP:
				push(v, new MapKeyValueIterator((IMap) v)); 
				return;
			case TUPLE:
				push(v, new TupleElementIterator((ITuple) v)); 
				return;
			default:
				spine.push(v);
				return;
			}
			*/
		}
	}

	private void pushConcreteSyntaxNode(final ITree tree){
		if (debug) System.err.println("pushConcreteSyntaxNode: " + tree);

//		if (/*TreeAdapter.isChar(tree) || */TreeAdapter.isCycle(tree) || TreeAdapter.isLiteral(tree) || TreeAdapter.isCILiteral(tree)) {
//			spine.push(tree);
//			return; // do not recurse
//		}

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
}
