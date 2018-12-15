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
package org.rascalmpl.core.library.lang.rascalcore.compile.runtime.utils;

import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;

public class JavaCompilerException extends Exception {
	private static final long serialVersionUID = -2691959003690821895L;
	private final Set<String> keySet;
	private final DiagnosticCollector<JavaFileObject> diagnostics;

	public JavaCompilerException(String message, Set<String> keySet,
			DiagnosticCollector<JavaFileObject> diagnostics) {
		super(message);
		this.keySet = keySet;
		this.diagnostics = diagnostics;
	}

	public JavaCompilerException(Set<String> keySet, Throwable cause,
			DiagnosticCollector<JavaFileObject> diagnostics) {
		super("compiler error", cause);
		this.keySet = keySet;
		this.diagnostics = diagnostics;
	}

	public Set<String> getKeySet() {
		return keySet;
	}

	public DiagnosticCollector<JavaFileObject> getDiagnostics() {
		return diagnostics;
	}

}
