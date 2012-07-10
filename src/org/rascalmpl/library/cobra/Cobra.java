/*******************************************************************************
 * Copyright (c) 2009-2012 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *******************************************************************************/
package org.rascalmpl.library.cobra;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.OverloadedFunction;
import org.rascalmpl.interpreter.types.ReifiedType;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.cobra.util.NullOutputStream;

public class Cobra {

	public static Type reifyType(IValue type) {
		Type reified = type.getType();
		if (!(reified instanceof ReifiedType)) {
			throw RuntimeExceptionFactory.illegalArgument(type, null, null,
					"A reified type is required instead of " + reified);
		}
		return reified.getTypeParameters().getFieldType(0);
	}


	final IValueFactory vf;

	private final QuickCheck quickcheck;

	public Cobra(IValueFactory vf) {
		this.vf = vf;
		this.quickcheck = QuickCheck.getInstance();
	}

	public IValue _quickcheck(IValue function, IInteger maxDepth, IBool verbose,
			IBool maxVerbose, IInteger tries, IEvaluatorContext eval) {

		PrintWriter out = (verbose.getValue()) ? eval.getStdOut()
				: new PrintWriter(new NullOutputStream());

		ArrayList<AbstractFunction> functions = extractFunctions(function, eval);

		if (!isReturnTypeBool(functions)) {
			throw RuntimeExceptionFactory.illegalArgument(function,
					eval.getCurrentAST(), null, "Return type should be bool.");
		}

		try {
			boolean result = true;
			for (AbstractFunction f : functions) {
				result = result
						&& quickcheck.quickcheck(f, maxDepth.intValue(),
								tries.intValue(), maxVerbose.getValue(), out);
			}
			return eval.getValueFactory().bool(result);

		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(maxDepth,
					eval.getCurrentAST(), null,
					e.getMessage());
		} finally {
			out.flush();
		}

	}

	public IValue arbitrary(IValue type, IInteger depthLimit,
			IEvaluatorContext eval) {
		try {
			IValue result = quickcheck.arbitrary(Cobra.reifyType(type),
					depthLimit.intValue(), eval.getCurrentEnvt().getRoot(),
					eval.getValueFactory());
			return result;
		} catch (IllegalArgumentException e) {
			throw RuntimeExceptionFactory.illegalArgument(depthLimit,
					eval.getCurrentAST(), null,
					"No construction possible at this depth or less.");
		}
	}

	private ArrayList<AbstractFunction> extractFunctions(IValue function,
			IEvaluatorContext eval) {
		ArrayList<AbstractFunction> functions = new ArrayList<AbstractFunction>();
		if (function instanceof AbstractFunction) {
			functions.add((AbstractFunction) function);
		} else if (function instanceof OverloadedFunction) {
			for (AbstractFunction f : ((OverloadedFunction) function)
					.getFunctions()) {
				functions.add(f);
			}
		} else {
			throw RuntimeExceptionFactory.illegalArgument(function, eval.getCurrentAST(), null,
					"Argument should be function.");
		}
		return functions;
	}

	public IValue getGenerator(IValue t, IEvaluatorContext eval) {
		return quickcheck.getGenerator(t, eval);
	}

	private boolean isReturnTypeBool(List<AbstractFunction> functions) {
		for(AbstractFunction f: functions){
			if (!f.getReturnType().isBoolType()) {
				return false;
			}
		}
		return true;
	}

	public void resetGenerator(IValue type) {
		quickcheck.resetGenerator(type);
	}

	public void setGenerator(IValue f) {
		quickcheck.setGenerator(f);
	}



}
