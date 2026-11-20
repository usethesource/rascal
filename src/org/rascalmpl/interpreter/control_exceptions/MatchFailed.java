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
package org.rascalmpl.interpreter.control_exceptions;

import io.usethesource.vallang.type.Type;

public class MatchFailed extends ControlException {
	private static final long serialVersionUID = -2730655862233487621L;

	/**
	 * Type of the function against which the match failed. Can be {@code null} when the match didn't happen in the
	 * context of a function, or when it did, but the information wasn't passed yet to this exception.
	 */
	public final Type functionType;

	public MatchFailed() {
		this(null);
	}

	public MatchFailed(Type functionType) {
		this.functionType = functionType;
	}
}
