package org.meta_environment.uptr.visitors;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IdentityVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.uptr.Factory;


public abstract class TreeVisitor extends IdentityVisitor {
	
	@Override
	public ITree visitNode(INode o) throws VisitorException {
		if (o.getType().getSuperType() == Factory.Tree) {
			Type alt = o.getType();
			
			if (alt == Factory.Tree_Appl) {
				return visitTreeAppl(o);
			}
			else if (alt == Factory.Tree_Amb) {
				return visitTreeAmb(o);
			}
			else if (alt == Factory.Tree_Cycle) {
				return visitTreeCycle(o);
			}
			else {
				throw new FactTypeError("TreeVisitor does not implement: " + alt);
			}
		}
		else {
			return o;
		}
	}
	
	@Override
	public IInteger visitInteger(IInteger arg) throws VisitorException {
		return visitTreeCharacter(arg);
	}
	
	public abstract ITree visitTreeAppl(INode arg) throws VisitorException;
	public abstract ITree visitTreeAmb(INode arg) throws VisitorException;
	public abstract IInteger visitTreeCharacter(IInteger arg) throws VisitorException;
	public abstract ITree visitTreeCycle(INode arg) throws VisitorException;
}
