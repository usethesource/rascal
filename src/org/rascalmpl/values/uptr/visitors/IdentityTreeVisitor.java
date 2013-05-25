/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

public abstract class IdentityTreeVisitor<E extends Throwable> extends TreeVisitor<E> {

	@Override
	public IConstructor visitTreeAmb(IConstructor arg) throws E {
		return arg;
	}
	
	@Override
	public IConstructor visitTreeAppl(IConstructor arg) throws E  {
		return arg;
	}

	@Override
	public IConstructor visitTreeChar(IConstructor arg) throws E {
		return arg;
	}

	@Override
	public IConstructor visitTreeCycle(IConstructor arg) throws E {
		return arg;
	}
}
