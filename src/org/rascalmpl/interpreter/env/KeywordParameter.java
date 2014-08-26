/*******************************************************************************
 * Copyright (c) 2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *
*******************************************************************************/
package org.rascalmpl.interpreter.env;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.interpreter.result.Result;

public class KeywordParameter {

	private final String name;
	private final Type type;
	private final Expression def;

	public KeywordParameter(String name, Type type, Expression def){
		this.name = name;
		this.type = type;
		this.def = def;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public Expression getDefault() {
		return def;
	}
}
