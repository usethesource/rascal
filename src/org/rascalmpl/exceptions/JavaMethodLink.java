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

public class JavaMethodLink extends RuntimeException {
	private static final long serialVersionUID = 3867556518416718308L;

	public JavaMethodLink(String name, String message, Throwable cause) {
		super("Cannot link method " + name + " because: " + message, cause);
	}
}
