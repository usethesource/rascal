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
package org.rascalmpl.interpreter.result;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;

public class ConcatStringResult extends StringResult {
	private final StringResult left;
	private final StringResult right;
	private final int length;
	
	/*package*/ ConcatStringResult(Type type, StringResult left, StringResult right, IEvaluatorContext ctx) {
		super(type, null, ctx);
		this.left = left;
		this.right = right;
		this.length = left.length() + right.length();
	}
	
	@Override
	protected int length() {
		return length;
	}
	
	@Override
	protected void yield(StringBuilder b) {
		left.yield(b);
		right.yield(b);
	}

	@Override
	public IString getValue() {
		StringBuilder builder = new StringBuilder(length);
		yield(builder);
		return getValueFactory().string(builder.toString());
	}

	
	
}
