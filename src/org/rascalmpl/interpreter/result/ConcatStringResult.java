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

import java.util.ArrayList;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.type.Type;

public class ConcatStringResult extends StringResult {
	private final int length;
	private final ArrayList<StringResult> components;
	
	/*package*/ ConcatStringResult(Type type, StringResult left, StringResult right, IEvaluatorContext ctx) {
		super(type, null, ctx);
		if(left instanceof ConcatStringResult){
			components = ((ConcatStringResult) left).components;
			if(right instanceof ConcatStringResult){
				components.addAll(((ConcatStringResult) right).components);
			} else {
				components.add(right);
			}
		} else if(left instanceof ConcatStringResult){
			components = ((ConcatStringResult) right).components;
			components.add(0, left);
		} else {
			components = new ArrayList<StringResult>();
			components.add(left);
			components.add(right);
		}
		this.length = left.length() + right.length();
	}
	
	@Override
	protected int length() {
		return length;
	}
	
	@Override
	protected void yield(StringBuilder b) {
		for(StringResult r : components){
			r.yield(b);
		}
	}

	@Override
	public IString getValue() {
		StringBuilder builder = new StringBuilder(length);
		yield(builder);
		return getValueFactory().string(builder.toString());
	}

	
	
}
