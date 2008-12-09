package org.meta_environment.rascal.interpreter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;

public class ITreeIterator implements Iterator<IValue> {

	List<Object> spine = new LinkedList<Object>();
	private boolean topdown;
	
	ITreeIterator(ITree tree, boolean topdown){
		this.topdown = topdown;
		initSpine(tree);
	}
	
	private void initSpine(IValue t){
		if(!topdown) {
			spine.add(0,t);
		}
		if(t.getType().isTreeType()){
			Iterator<IValue> children = ((ITree) t).getChildren().iterator();
			spine.add(0, children);
		}
		if(topdown) {
			spine.add(0,t);
		}
	}

	public boolean hasNext() {
		while((spine.size() > 0) && 
			   (spine.get(0) instanceof Iterator && !((Iterator<Object>) spine.get(0)).hasNext())){
			spine.remove(0);
		}		
		return spine.size() > 0;
	}
	
	private IValue insertAndNext(IValue v, Iterator<IValue> children){
		if(topdown){
			spine.add(0, children);
			spine.add(0, v);
		} else {
			spine.add(0, v);
			spine.add(0, children);
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
		return v;
	}

	public IValue next() {
		if(spine.get(0) instanceof Iterator){
			Iterator<Object> iter = (Iterator<Object>) spine.get(0);
			if(!iter.hasNext()){
				spine.remove(0);
				return next();
			}
			return expand((IValue) iter.next());
		} else {
			IValue res = (IValue) spine.get(0);
			spine.remove(0);
			return res;
		}
	}

	public void remove() {
		return;
	}
}
