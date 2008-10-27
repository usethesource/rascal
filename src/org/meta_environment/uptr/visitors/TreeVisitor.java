package org.meta_environment.uptr.visitors;

import org.eclipse.imp.pdb.facts.ITree;
import org.eclipse.imp.pdb.facts.type.FactTypeError;
import org.eclipse.imp.pdb.facts.type.TreeNodeType;
import org.eclipse.imp.pdb.facts.visitors.IdentityVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.uptr.Factory;


public abstract class TreeVisitor extends IdentityVisitor {
	
	@Override
	public ITree visitTree(ITree o) throws VisitorException {
		if (o.getType() == Factory.Tree) {
			TreeNodeType alt = o.getTreeNodeType();
			
			if (alt == Factory.Tree_Appl) {
				return visitTreeAppl(o);
			}
			else if (alt == Factory.Tree_Amb) {
				return visitTreeAmb(o);
			}
			else if (alt == Factory.Tree_Char) {
				return visitTreeCharacter(o);
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
	
	public abstract ITree visitTreeAppl(ITree arg) throws VisitorException;
	public abstract ITree visitTreeAmb(ITree arg) throws VisitorException;
	public abstract ITree visitTreeCharacter(ITree arg) throws VisitorException;
	public abstract ITree visitTreeCycle(ITree arg) throws VisitorException;
}
