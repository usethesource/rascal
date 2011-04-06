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

import java.net.URI;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.rascalmpl.ast.Test;

public class FailedTestError extends ControlException {
	private static final long serialVersionUID = 8282771874859604692L;
	private String trace = null;
	private ISourceLocation loc;

	public FailedTestError(Test.Labeled t) {
		super("test " + t.getLabeled().toString() + " failed.");
		this.loc = t.getLocation();
	}
	
	public FailedTestError(Test.Unlabeled t) {
		super("test failed");
		this.loc = t.getLocation();
	}

	public FailedTestError(Test.Labeled t, Throw tr) {
		super("test " + t.getLabeled() + " failed due to unexpected Rascal exception: " + tr.getMessage());
		this.loc = tr.getLocation();
	}
	
	public FailedTestError(Test.Unlabeled t, Throw tr) {
		super("test failed due to unexpected Rascal exception: " + tr.getMessage());
		this.loc = tr.getLocation();
	}
	
	public FailedTestError(Test.Labeled t, Throwable e) {
		super("test " + t.getLabeled() + " failed due to unexpected Java exception: " + e.getMessage(), e);
		this.loc = t.getLocation();
	}
	
	public FailedTestError(Test.Unlabeled t, Throwable e) {
		super("test failed due to unexpected Java exception: " + e.getMessage(), e);
		this.loc = t.getLocation();
	}
	
	@Override
	public String getMessage() {
		URI url = loc.getURI();

		return (url.getScheme().equals("file") ? (url.getAuthority() + url.getPath()) : url) 
		+ ":" + loc.getBeginLine() 
		+ "," + loc.getBeginColumn() 
		+ ": " + super.getMessage()
		+ ((trace != null) ? trace : "");
	}
}
