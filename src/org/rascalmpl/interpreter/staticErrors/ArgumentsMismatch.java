/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import java.util.Arrays;
import java.util.List;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;

public class ArgumentsMismatch extends StaticError {
	private static final long serialVersionUID = -641438732779898646L;

	public ArgumentsMismatch(String name,
			List<AbstractFunction> candidates, Type[] argTypes,
			AbstractAST ast) {
		super(computeMessage(name, candidates, argTypes), ast);
	}
	
	public ArgumentsMismatch(String message, AbstractAST ast) {
		super(message, ast);
	}

	public ArgumentsMismatch(Result<IValue> function, Type[] argTypes, AbstractAST caller) {
    super(computeMessage(function, argTypes), caller);
  }

  private static String computeMessage(Result<IValue> function, Type[] argTypes) {
    if (function instanceof OverloadedFunction) {
      OverloadedFunction of = (OverloadedFunction) function;
      return computeMessage(of.getName(), of.getFunctions(), argTypes);
    }
    else {
      AbstractFunction func = (AbstractFunction) function;
      return computeMessage(func.getName(), Arrays.<AbstractFunction>asList(func), argTypes);
    }
  }

  private static String computeMessage(String name,
			List<AbstractFunction> candidates, Type[] argTypes) {
		StringBuilder b = new StringBuilder();
		
		b.append("The called signature: " + name);
		b.append('(');
		argumentTypes(TypeFactory.getInstance().tupleType(argTypes), b);
		b.append(')');
		if (candidates.size() == 1) {
			b.append(",\ndoes not match the declared signature:");
		}
		else {
			b.append(",\ndoes not match any of the declared (overloaded) signature patterns:\n");
		}
		for (AbstractFunction c : candidates) {
			b.append('\t');
			b.append(c.toString());
			b.append('\n');
		}
		
		return b.toString();
		
	}

	private static void argumentTypes(Type argTypes, StringBuilder b) {
		int i = 0;
		for (Type arg : argTypes) {
			if (i != 0) b.append(", ");
			b.append(arg);
			if (argTypes.hasFieldNames()) {
				b.append(' ');
				b.append(argTypes.getFieldName(i));
			}
			i++;
		}
	}

}
