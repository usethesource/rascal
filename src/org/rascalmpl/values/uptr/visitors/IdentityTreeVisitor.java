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

import org.rascalmpl.values.uptr.RascalValueFactory.Tree;

public abstract class IdentityTreeVisitor<E extends Throwable> extends TreeVisitor<E> {

	@Override
	public Tree visitTreeAmb(Tree arg) throws E {
		return arg;
	}
	
	@Override
	public Tree visitTreeAppl(Tree arg) throws E  {
		return arg;
	}

	@Override
	public Tree visitTreeChar(Tree arg) throws E {
		return arg;
	}

	@Override
	public Tree visitTreeCycle(Tree arg) throws E {
		return arg;
	}
}
