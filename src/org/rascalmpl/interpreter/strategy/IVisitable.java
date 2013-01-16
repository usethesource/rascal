/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.strategy;

import java.util.List;

import org.eclipse.imp.pdb.facts.IValue;


public interface IVisitable {
	
	public boolean init(IValue v);
	
	public void mark(IValue v);

	public IValue getChildAt(IValue v, int i) throws IndexOutOfBoundsException;

	public int getChildrenNumber(IValue v);

	public <T extends IValue> T setChildren(T v, List<IValue> newchildren)
		throws IndexOutOfBoundsException;

	public <T extends IValue> T setChildAt(T v, int i, IValue newchild)
		throws IndexOutOfBoundsException;
}
