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

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.ast.AbstractAST;

public class FailedTestError extends ControlException {
	private static final long serialVersionUID = 8282771874859604692L;
	private String trace = null;
	private ISourceLocation loc;

	public FailedTestError(AbstractAST t) {
		super("test failed");
		this.loc = t.getLocation();
	}

	public FailedTestError(AbstractAST t, Throw tr) {
		super("test failed due to unexpected Rascal exception: " + tr.getMessage());
		this.loc = tr.getLocation();
	}
	
	public FailedTestError(AbstractAST t, Throwable e) {
		super("test failed due to unexpected Java exception: " + e.getMessage(), e);
		this.loc = t.getLocation();
	}
	
	@Override
	public String getMessage() {
		return (loc.getScheme().equals("file") ? (loc.getAuthority() + loc.getPath()) : loc.top()) 
		+ ":" + loc.getBeginLine() 
		+ "," + loc.getBeginColumn() 
		+ ": " + super.getMessage()
		+ ((trace != null) ? trace : "");
	}
}
