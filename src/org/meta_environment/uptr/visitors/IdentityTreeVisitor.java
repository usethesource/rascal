package org.meta_environment.uptr.visitors;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;

public abstract class IdentityTreeVisitor extends TreeVisitor {

	@Override
	public INode visitTreeAmb(IConstructor arg) throws VisitorException {
		return arg;
	}

	@Override
	public INode visitTreeAppl(IConstructor arg) throws VisitorException  {
		return arg;
	}

	@Override
	public IInteger visitTreeCharacter(IInteger arg) throws VisitorException {
		return arg;
	}

	@Override
	public INode visitTreeCycle(IConstructor arg)  throws VisitorException {
		return arg;
	}

}
