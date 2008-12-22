package org.meta_environment.rascal.interpreter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

public class ITreeReader implements Iterator<IValue> {

	Stack<Object> spine = new Stack<Object>();
	
	private boolean bottomup;
	
	ITreeReader(ITree tree, boolean bottomup){
		this.bottomup = bottomup;
		initSpine(tree);
	}
	
	private void initSpine(IValue t){
		if(bottomup) {
			spine.push(t);
		}
		if(t.getType().isTreeType()){
			Iterator<IValue> children = ((ITree) t).getChildren().iterator();
			spine.push(children);
		}
		// TODO add more types here
		if(!bottomup) {
			spine.push(t);
		}
	}

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
		if(v.getType().isTreeType()){
			return insertAndNext(v,  ((ITree) v).getChildren().iterator());
		}
		if(v.getType().isListType()){
			return insertAndNext(v, ((IList) v).iterator());
		}
		if(v.getType().isSetType()){
			return insertAndNext(v, ((ISet) v).iterator());
		}
		if(v.getType().isMapType()){
			return insertAndNext(v, ((IMap) v).iterator());
		}
		if(v.getType().isTupleType()){
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

	public IValue next() {
		if(spine.peek() instanceof Iterator){
			Iterator<Object> iter = (Iterator<Object>) spine.peek();
			if(!iter.hasNext()){
				spine.pop();
				return next();
			}
			return expand((IValue) iter.next());
		} else {
			return (IValue) spine.pop();
		}
	}

	public void remove() {
		return;
	}
}
