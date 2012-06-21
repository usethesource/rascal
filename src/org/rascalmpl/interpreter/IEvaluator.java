/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Emilie Balland - (CWI)
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter;

import org.rascalmpl.ast.AbstractAST;

/**
 * TODO: This interface was used by the
 * {@link org.rascalmpl.interpreter.debug.DebuggingDecorator}, which is now
 * removed. This interface should be reiterated together with
 * {@link org.rascalmpl.interpreter.IEvaluatorContext}.
 */
public interface IEvaluator<T> extends IEvaluatorContext {

	/* (non-Javadoc)
	 * Event handling callback, managing the current debugging state.
	 */
	public void suspend(AbstractAST currentAST);
	
}
