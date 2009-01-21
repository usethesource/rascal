package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.INode;
import org.meta_environment.uptr.TreeAdapter;

public abstract class AbstractAST implements IVisitable {
	protected ISourceLocation location;
	protected INode node;

	abstract public <T> T accept(IASTVisitor<T> v);

	@Override
	public boolean equals(Object obj) {
		if (getClass() == obj.getClass()) {
			return ((AbstractAST) obj).node.equals(node);
		}
		return false;
	}

	public INode getTree() {
		return node;
	}

	@Override
	public int hashCode() {
		return node.hashCode();
	}

	@Override
	public String toString() {
		return new TreeAdapter((IConstructor) node).yield();
	}
}
