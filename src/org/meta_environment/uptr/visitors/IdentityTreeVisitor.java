package org.meta_environment.uptr.visitors;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public abstract class IdentityTreeVisitor extends TreeVisitor {

	@Override
	public ITree visitTreeAmb(INode arg) throws VisitorException {
		return arg;
	}

	@Override
	public ITree visitTreeAppl(INode arg) throws VisitorException  {
		return arg;
	}

	@Override
	public IInteger visitTreeCharacter(IInteger arg) throws VisitorException {
		return arg;
	}

	@Override
	public ITree visitTreeCycle(INode arg)  throws VisitorException {
		return arg;
	}

}
