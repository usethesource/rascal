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

import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.visitors.IdentityVisitor;
import org.rascalmpl.values.uptr.ITree;
import org.rascalmpl.values.uptr.RascalValueFactory;

public abstract class TreeVisitor<E extends Throwable> extends IdentityVisitor<E> {
	
	@Override
	public INode visitConstructor(IConstructor o) throws E {
		if (o.getType().isSubtypeOf(RascalValueFactory.Tree)) {
			Type alt = o.getConstructorType();
			
			if(alt == RascalValueFactory.Tree_Appl){
				return visitTreeAppl((ITree) o);
			}else if (alt == RascalValueFactory.Tree_Amb){
				return visitTreeAmb((ITree)o);
			}else if (alt == RascalValueFactory.Tree_Char){
				return visitTreeChar((ITree)o);
			}else if (alt == RascalValueFactory.Tree_Cycle){
				return visitTreeCycle((ITree)o);
			}else{
				throw new ImplementationError("TreeVisitor does not implement: " + alt);
			}
		}
		
		return o;
	}
	
	public abstract ITree visitTreeAppl(ITree arg) throws E;
	public abstract ITree visitTreeAmb(ITree arg) throws E;
	public abstract ITree visitTreeChar(ITree arg) throws E;
	public abstract ITree visitTreeCycle(ITree arg) throws E;
}
