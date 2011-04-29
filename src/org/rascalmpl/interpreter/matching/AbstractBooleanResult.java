/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - emilie.balland@inria.fr (INRIA)
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;

public abstract class AbstractBooleanResult implements IBooleanResult {
	protected boolean initialized = false;
	protected boolean hasNext = true;
	protected final TypeFactory tf = TypeFactory.getInstance();
	protected IEvaluatorContext ctx;
	
	public void init(IEvaluatorContext ctx) {
		this.ctx = ctx;
		this.initialized = true;
		this.hasNext = true;
	}
	
	public boolean hasNext() {
		return initialized && hasNext;
	}
	
	abstract public boolean next();
}