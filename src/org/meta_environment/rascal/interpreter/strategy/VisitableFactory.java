package org.meta_environment.rascal.interpreter.strategy;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;

public class VisitableFactory {

	public static Visitable make(IValue iValue) {
		if (iValue instanceof INode) {
			return new VisitableNode((INode) iValue);
		} else if (iValue instanceof ITuple) {
			return new VisitableTuple((ITuple) iValue);
		} else if (iValue instanceof IMap) {
			return new VisitableMap((IMap) iValue);
		} else if (iValue instanceof IRelation) {
			return new VisitableRelation((IRelation) iValue);
		} else if (iValue instanceof IList) {
			return new VisitableList((IList) iValue);
		} else if (iValue instanceof ISet) {
			return new VisitableSet((ISet) iValue);
		} else if (iValue instanceof ISourceLocation || iValue instanceof IExternalValue || iValue instanceof IBool || iValue instanceof IInteger || iValue instanceof ISourceLocation || iValue instanceof IReal || iValue instanceof IString) {
			return new VisitableConstant(iValue);
		}
		return null;
	}

}
