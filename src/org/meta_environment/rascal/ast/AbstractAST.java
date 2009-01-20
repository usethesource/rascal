package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ITree;
import org.meta_environment.uptr.TreeAdapter;

public abstract class AbstractAST implements IVisitable {
	protected ISourceLocation location;
	protected ITree tree;

	abstract public <T> T accept(IASTVisitor<T> v);

	@Override
	public boolean equals(Object obj) {
		if (getClass() == obj.getClass()) {
			return ((AbstractAST) obj).tree.equals(tree);
		}
		return false;
	}

	public ITree getTree() {
		return tree;
	}

	@Override
	public int hashCode() {
		return tree.hashCode();
	}

	@Override
	public String toString() {
		return new TreeAdapter((INode) tree).yield();
	}
}
