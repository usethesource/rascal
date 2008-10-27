package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.ITree;

public class AbstractAST implements IVisitable {
	protected ITree tree;
	protected ISourceLocation location;

}
