package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ITree;
import org.meta_environment.uptr.TreeAdapter;

public class AbstractAST implements IVisitable {
	protected ISourceLocation location;
	protected ITree tree;

	@Override
	public String toString() {
		return new TreeAdapter(tree).yield();
	}

}
