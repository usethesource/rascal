/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.values.uptr.visitors;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.visitors.IdentityVisitor;
import org.eclipse.imp.pdb.facts.visitors.VisitorException;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.values.uptr.Factory;

public abstract class TreeVisitor extends IdentityVisitor {
	
	@Override
	public INode visitConstructor(IConstructor o) throws VisitorException {
		if (o.getType() == Factory.Tree) {
			Type alt = o.getConstructorType();
			
			if(alt == Factory.Tree_Appl){
				return visitTreeAppl(o);
			}else if (alt == Factory.Tree_Amb){
				return visitTreeAmb(o);
			}else if (alt == Factory.Tree_Char){
				return visitTreeChar(o);
			}else if (alt == Factory.Tree_Cycle){
				return visitTreeCycle(o);
			}else{
				throw new ImplementationError("TreeVisitor does not implement: " + alt);
			}
		}
		
		return o;
	}
	
	public abstract IConstructor visitTreeAppl(IConstructor arg) throws VisitorException;
	public abstract IConstructor visitTreeAmb(IConstructor arg) throws VisitorException;
	public abstract IConstructor visitTreeChar(IConstructor arg) throws VisitorException;
	public abstract IConstructor visitTreeCycle(IConstructor arg) throws VisitorException;
}
