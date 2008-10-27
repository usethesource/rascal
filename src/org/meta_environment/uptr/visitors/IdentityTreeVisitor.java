package org.meta_environment.uptr.visitors;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public abstract class IdentityTreeVisitor extends TreeVisitor {

	@Override
	public ITree visitTreeAmb(ITree arg) throws VisitorException {
		return arg;
	}

	@Override
	public ITree visitTreeAppl(ITree arg) throws VisitorException  {
		return arg;
	}

	@Override
	public ITree visitTreeCharacter(ITree arg) throws VisitorException {
		return arg;
	}

	@Override
	public ITree visitTreeCycle(ITree arg)  throws VisitorException {
		return arg;
	}

}
