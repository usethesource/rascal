/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.asserts;

import org.rascalmpl.value.ISourceLocation;


/**
 * Exception used for when the implementation detects that it has a bug.
 * This is a separate class of exceptions from static errors or run-time exceptions.
 */
public final class ImplementationError extends AssertionError {
	private static final long serialVersionUID = -8740312542969306482L;
	private final ISourceLocation location;
	

	public ImplementationError(String message, Throwable cause) {
		super("Unexpected error in Rascal interpreter: " + message + " caused by " + cause.getMessage());
		this.initCause(cause);
		this.location = null;
	}
	
	// TODO replace these by asserts?
	public ImplementationError(String message) {
		super("Unexpected error in Rascal interpreter: " + message);
		this.location = null;
	}

	public ImplementationError(String message, ISourceLocation location) {
		super(message);
		this.location = location;
	}
	
	@Override
	public String getMessage() {
		if (location != null) return location + ":" + super.getMessage();
		
		return super.getMessage();
	}
}
