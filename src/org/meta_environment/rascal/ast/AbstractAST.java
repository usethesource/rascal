package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ITree;
import org.meta_environment.uptr.TreeAdapter;

public abstract class AbstractAST implements IVisitable {
	protected ISourceLocation location;
	protected ITree tree;

	@Override
	public String toString() {
		return new TreeAdapter((INode) tree).yield();
	}
	
	public ITree getTree() {
		return tree;
	}

	abstract public <T> T accept(IASTVisitor<T> v);
}
