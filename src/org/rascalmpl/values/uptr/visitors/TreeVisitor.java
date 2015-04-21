/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.values.uptr.RascalValueFactory;

public abstract class TreeVisitor<E extends Throwable> extends IdentityVisitor<E> {
	
	@Override
	public INode visitConstructor(IConstructor o) throws E {
		if (o.getType() == RascalValueFactory.Tree) {
			Type alt = o.getConstructorType();
			
			if(alt == RascalValueFactory.Tree_Appl){
				return visitTreeAppl(o);
			}else if (alt == RascalValueFactory.Tree_Amb){
				return visitTreeAmb(o);
			}else if (alt == RascalValueFactory.Tree_Char){
				return visitTreeChar(o);
			}else if (alt == RascalValueFactory.Tree_Cycle){
				return visitTreeCycle(o);
			}else{
				throw new ImplementationError("TreeVisitor does not implement: " + alt);
			}
		}
		
		return o;
	}
	
	public abstract IConstructor visitTreeAppl(IConstructor arg) throws E;
	public abstract IConstructor visitTreeAmb(IConstructor arg) throws E;
	public abstract IConstructor visitTreeChar(IConstructor arg) throws E;
	public abstract IConstructor visitTreeCycle(IConstructor arg) throws E;
}
