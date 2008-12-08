package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.IValue;

public class ITreeIterator implements Iterator<IValue> {
	private ITree tree;
	List<IValue> subtrees = new ArrayList<IValue>();
	ITree current;
	
	Iterator<IValue> iter;
	
	ITreeIterator(ITree tree, boolean topdown){
		this.tree = tree;
		addSubTrees(tree, topdown);
		for(int i = 0; i < subtrees.size(); i++){
			System.err.println("subtree " + i + ": " + subtrees.get(i));
		}
		iter = subtrees.iterator();
	}
	
	private void addSubTrees(IValue t, boolean topdown){
		//TODO need a better implementation that does not generate a list
		if(topdown){
			subtrees.add(t);
			if(t.getType().isTreeType()){
				for(IValue c : ((ITree) t).getChildren()){
					subtrees.add(c);
					addSubTrees(c, topdown);
				}
			}
		} else {
			if(t.getType().isTreeType()){
				for(IValue c : ((ITree) t).getChildren()){
					System.err.println("add child: " + c);
					addSubTrees(c, topdown);
					subtrees.add(c);
				}
			}
			subtrees.add(t);
		}
	}

	public boolean hasNext() {
		return iter.hasNext();
	}

	public IValue next() {
		return iter.next();
	}

	public void remove() {
		iter.remove();
	}
}
