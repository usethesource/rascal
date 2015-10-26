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

import org.rascalmpl.interpreter.StackTrace;
import org.rascalmpl.value.ISourceLocation;

public class InterruptException extends ControlException {
	private static final long serialVersionUID = -6244185056015873062L;
	private final StackTrace stackTrace;
	private final ISourceLocation loc;
	private final String message;
	
	public InterruptException(StackTrace stackTrace, ISourceLocation loc) {
		this.stackTrace = stackTrace;
		this.loc = loc;
		this.message = null;
	}

	public InterruptException(String message, ISourceLocation loc) {
		this.stackTrace = null;
		this.loc = loc;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return toString();
	}
	
	public StackTrace getRascalStackTrace() {
		return stackTrace;
	}
	
	public ISourceLocation getLocation() {
		return loc;
	}
	
	@Override
	public String toString() {
		String str = "interrupted";
		if(message != null) {
			str += ": " + message;
		}
		if(stackTrace != null) {
			str += ":\n" + stackTrace.toString();
		}
		return str;
	}
}
