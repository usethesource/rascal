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
package org.rascalmpl.interpreter.control_exceptions;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public class InterruptException extends ControlException {
	private static final long serialVersionUID = -6244185056015873062L;
	private final String stackTrace;
	private final ISourceLocation loc;
	
	public InterruptException(String stackTrace, ISourceLocation loc) {
		this.stackTrace = stackTrace;
		this.loc = loc;
	}

	@Override
	public String getMessage() {
		return toString();
	}
	
	public String getRascalStackTrace() {
		return stackTrace;
	}
	
	public ISourceLocation getLocation() {
		return loc;
	}
	
	@Override
	public String toString() {
		return "interrupted" + ((stackTrace != null && stackTrace.length() != 0) ? (": " + stackTrace) : "");
	}
}
