/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.values.uptr.visitors;

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
	public IConstructor visitTreeCycle(IConstructor arg) throws VisitorException {
		return arg;
	}
}
