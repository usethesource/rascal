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

import org.rascalmpl.values.uptr.ITree;

public abstract class IdentityTreeVisitor<E extends Throwable> extends TreeVisitor<E> {

	@Override
	public ITree visitTreeAmb(ITree arg) throws E {
		return arg;
	}
	
	@Override
	public ITree visitTreeAppl(ITree arg) throws E  {
		return arg;
	}

	@Override
	public ITree visitTreeChar(ITree arg) throws E {
		return arg;
	}

	@Override
	public ITree visitTreeCycle(ITree arg) throws E {
		return arg;
	}
}
