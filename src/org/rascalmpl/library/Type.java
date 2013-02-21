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
package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeReifier;

public class Type {
	private final IValueFactory vf;

	public Type(IValueFactory vf) {
		this.vf = vf;
		
	}
	
	public IValue typeOf(IValue v, IEvaluatorContext ctx) {
		return ((IConstructor) new TypeReifier(vf).typeToValue(v.getType(), ctx).getValue()).get("symbol");
	}
	
	public IBool eq(IValue x, IValue y) {
	  return vf.bool(x.isEqual(y));
	}
	
}
