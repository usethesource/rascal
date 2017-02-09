/*******************************************************************************
 * Copyright (c) 2011-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *
*******************************************************************************/
package org.rascalmpl.interpreter.matching;

import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public class RegExpVar implements IVarPattern {
	private String name;
	private static final Type stringType = TypeFactory.getInstance().stringType();

	public RegExpVar(String name){
		this.name = name;
		
	}
	@Override
	public boolean isVarIntroducing() {
		return true;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public Type getType() {
		return stringType;
	}

}
