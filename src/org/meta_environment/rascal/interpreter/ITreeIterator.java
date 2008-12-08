package org.meta_environment.rascal.interpreter;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	public IValue next() {
		if(spine.get(0) instanceof Iterator){
			Iterator<Object> iter = (Iterator<Object>) spine.get(0);
			if(!iter.hasNext()){
				spine.remove(0);
				return next();
			}
			IValue res = (IValue) iter.next();
			if(res instanceof ITree){
				Iterator<IValue> children = ((ITree) res).getChildren().iterator();
				if(topdown){
					spine.add(0, children);
					spine.add(0, res);
				} else {
					spine.add(0, res);
					spine.add(0, children);
				}
				res = next();
			}
			return res;
			
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
