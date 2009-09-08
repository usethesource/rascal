package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;

public class VisitableFactory {

	public static Visitable make(IValue iValue) {
		if (iValue instanceof INode) {
			return new VisitableNode((INode) iValue);
		}
		return null;
	}

}
