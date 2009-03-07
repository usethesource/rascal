package org.meta_environment.uptr.visitors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IdentityVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.meta_environment.rascal.interpreter.exceptions.ImplementationException;
import org.meta_environment.uptr.Factory;


public abstract class TreeVisitor extends IdentityVisitor {
	
	@Override
	public INode visitConstructor(IConstructor o) throws VisitorException {
		if (o.getType() == Factory.Tree) {
			Type alt = o.getConstructorType();
			
			if (alt == Factory.Tree_Appl) {
				return visitTreeAppl(o);
			}
			else if (alt == Factory.Tree_Amb) {
				return visitTreeAmb(o);
			}
			else if (alt == Factory.Tree_Char) {
				return visitTreeChar(o);
			}
			else if (alt == Factory.Tree_Cycle) {
				return visitTreeCycle(o);
			}
			else {
				throw new ImplementationException("TreeVisitor does not implement: " + alt);
			}
		}
		else {
			return o;
		}
	}
	
	public abstract IConstructor visitTreeAppl(IConstructor arg) throws VisitorException;
	public abstract IConstructor visitTreeAmb(IConstructor arg) throws VisitorException;
	public abstract IConstructor visitTreeChar(IConstructor arg) throws VisitorException;
	public abstract IConstructor visitTreeCycle(IConstructor arg) throws VisitorException;
}
