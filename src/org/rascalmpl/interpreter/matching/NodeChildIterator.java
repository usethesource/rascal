package org.rascalmpl.interpreter.matching;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;

class NodeChildIterator implements Iterator<IValue> {
	private INode node;
	private int index;
	
	NodeChildIterator(INode node){
		this.node = node;
		index = 0;
	}

	public boolean hasNext() {
		return index < node.arity();
	}

	public IValue next() {
		return node.get(index++);
	}

	public void remove() {
		throw new UnsupportedOperationException("remove in NodeChildGenerator");
	}
}