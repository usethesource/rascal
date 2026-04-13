/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import io.usethesource.vallang.ISourceLocation;

public interface ITestResultListener {
	void start(String context, int count);
	void report(boolean successful, String test, ISourceLocation loc, String message, Throwable exception);
	void ignored(String test, ISourceLocation loc);
	void done();
}

