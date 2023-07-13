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
package org.rascalmpl.exceptions;

public class JavaCompilation extends RuntimeException {
	private static final long serialVersionUID = 3200356264732532487L;
	private final String source;
	private final String classpath;

	public JavaCompilation(String message, String source, String classpath, Exception cause) {
		super("Java compilation failed due to " + message, cause);
		this.classpath = classpath;
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public String getClasspath() {
		return classpath;
	}
}
