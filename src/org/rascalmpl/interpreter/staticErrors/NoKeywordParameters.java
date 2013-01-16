/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class NoKeywordParameters extends StaticError {
	public NoKeywordParameters(String name, AbstractAST ast) {
		super("function " + name + " cannot be called with keyword parameters", ast);
	}

	private static final long serialVersionUID = 7512965714991339935L;

}
