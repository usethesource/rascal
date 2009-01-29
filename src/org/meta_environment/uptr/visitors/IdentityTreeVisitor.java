package org.meta_environment.uptr.visitors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public abstract class IdentityTreeVisitor extends TreeVisitor {

	@Override
	public IConstructor visitTreeAmb(IConstructor arg) throws VisitorException {
		return arg;
	}

	@Override
	public IConstructor visitTreeAppl(IConstructor arg) throws VisitorException  {
		return arg;
	}

	@Override
	public IConstructor visitTreeChar(IConstructor arg) throws VisitorException {
		return arg;
	}

	@Override
	public IConstructor visitTreeCycle(IConstructor arg)  throws VisitorException {
		return arg;
	}

}
