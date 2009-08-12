package org.meta_environment.rascal.interpreter.matching;

import java.util.Iterator;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.interpreter.result.ConcreteSyntaxResult;
import org.meta_environment.rascal.interpreter.types.ConcreteSyntaxType;
import org.meta_environment.uptr.Factory;
import org.meta_environment.uptr.SymbolAdapter;

public class NodeReader implements Iterator<IValue> {

	Stack<Object> spine = new Stack<Object>();
	
	private boolean bottomup;
	
	NodeReader(INode node, boolean bottomup){
		this.bottomup = bottomup;
		initSpine(node);
	}
	
	private void initSpine(INode node){
		//System.err.println("initSpine: " + node.getType() + ", " + node);
		
		if(node.getType().getName().equals("Tree")){
			pushConcreteSyntaxNode((IConstructor) node);
		} else {
			if(bottomup) {
				spine.push(node);
			}
			spine.push(node.getChildren().iterator());
			if(!bottomup) {
				spine.push(node);
			}
		}
	}
	
	private void pushConcreteSyntaxNode(IConstructor tree){
		//System.err.println("pushConcreteSyntaxNode: " + t);
			
		ConcreteSyntaxType ctype = new ConcreteSyntaxType(tree);
		SymbolAdapter sym = new SymbolAdapter(ctype.getSymbol());
        if(sym.isAnyList()){
        	sym = sym.getSymbol();
        	int delta = 1;
        	IList listElems = (IList) tree.get(1);
			if(sym.isIterPlus() || sym.isIterStar()){
				//System.err.println("pushConcreteSyntaxChildren: isConcreteCFList");
				delta = 2;
			} else if(sym.isIterPlusSep() || sym.isIterStarSep()){
				//System.err.println("pushConcreteSyntaxChildren: isConcreteListType");
				delta = 4;
			}
        	
			for(int i = listElems.length() - 1; i >= 0 ; i -= delta){
				//System.err.println("adding: " + listElems.get(i));
				spine.push(listElems.get(i));
			}
			return;
		} else {
			//System.err.println("pushConcreteSyntaxNode: appl");
			/*
			 * appl(prod(...), [child0, layout0, child1, ...])
			 */
			INode prod = (IConstructor) tree.get(0);
			IList cfelems = (IList) prod.get(0);
			if(cfelems.length() == 1){
				IConstructor cfelem0 = (IConstructor) cfelems.get(0);
				if(cfelem0.getName().equals("lit")){
					/*
					 * Don't recurse in literals
					 */
					spine.push(tree);
					return;
				}
			}
			if(bottomup) {
				spine.push(tree);
			}
			IList applArgs = (IList) tree.get(1);
			
			for(int i = applArgs.length() - 1; i >= 0 ; i -= 2){
				spine.push(applArgs.get(i));
			}
			if(!bottomup) {
				spine.push(tree);
			}
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean hasNext() {
		while((spine.size() > 0) &&
			   (spine.peek() instanceof Iterator && !((Iterator<Object>) spine.peek()).hasNext())){
			spine.pop();
		}		
		return spine.size() > 0;
	}
	
	private IValue insertAndNext(IValue v, Iterator<IValue> children){
		if(bottomup){
			spine.push(v);
			spine.push(children);
		} else {
			spine.push(children);
			spine.push(v);
		}
		return next();
	}
	
	private IValue expand(IValue v){
		Type type = v.getType();
		if(type.isNodeType() || type.isConstructorType() || type.isAbstractDataType()){
			if(type.getName().equals("Tree")){
				pushConcreteSyntaxNode((IConstructor) v);
				return next();
			} else {
				return insertAndNext(v,  ((INode) v).getChildren().iterator());
			}
		}
		if(type.isListType()){
			return insertAndNext(v, ((IList) v).iterator());
		}
		if(type.isSetType()){
			return insertAndNext(v, ((ISet) v).iterator());
		}
		if(type.isMapType()){
			return insertAndNext(v, ((IMap) v).iterator());
		}
		if(type.isTupleType()){
			ITuple tp = (ITuple) v;
			int arity = tp.arity();
			if(bottomup){
				spine.push(tp);
			}
			for(int i = arity - 1; i >= 0; i--){
				spine.push(tp.get(i));
			}
			if(!bottomup){
				spine.push(tp);
			}
			return next();
		}
		
		return v;
	}

	@SuppressWarnings("unchecked")
	public IValue next() {
		if(spine.peek() instanceof Iterator){
			Iterator<Object> iter = (Iterator<Object>) spine.peek();
			if(!iter.hasNext()){
				spine.pop();
				return next();
			}
			return expand((IValue) iter.next());
		}
		
		return (IValue) spine.pop();
	}

	public void remove() {
		throw new UnsupportedOperationException("remove from INodeReader");
	}
}
