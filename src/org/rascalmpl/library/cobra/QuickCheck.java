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
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.types.FunctionType;

public class QuickCheck {

	private static class InstanceHolder {
		public static final QuickCheck sInstance = new QuickCheck();
	}

	static final HashMap<Type, ICallableValue> generators = new HashMap<Type, ICallableValue>();

	public static QuickCheck getInstance() {
		return InstanceHolder.sInstance;
	}

	public IValue arbitrary(Type type, int depthLimit, Environment env,
			IValueFactory vf) {

		RandomValueTypeVisitor visitor = new RandomValueTypeVisitor(vf,
				(ModuleEnvironment) env, depthLimit, generators);

		IValue result = visitor.generate(type);
		if (result == null) {
			throw new IllegalArgumentException("No construction possible at this depth or less.");
		}
		return result;
	}


	private boolean generatorExists(Type t) {
		return generators.containsKey(t);
	}

	public IValue getGenerator(IValue t, IEvaluatorContext eval) {
		Type reified = Cobra.reifyType(t);
		if (generatorExists(reified)) {
			return generators.get(reified);
		}

		return new DynamicGenerator(eval.getEvaluator(), reified,
				eval.getCurrentEnvt(), generators);
	}

	public boolean quickcheck(AbstractFunction function, int maxDepth,
			int tries, boolean verbose, PrintWriter out) {

		Environment declEnv = function.getEnv();
		IValueFactory vf = function.getEval().getValueFactory();
		Type formals = function.getFormals();

		Type[] types = new Type[formals.getArity()];
		IValue[] values = new IValue[formals.getArity()];

		for (int n = 0; n < formals.getArity(); n++) {
			types[n] = formals.getFieldType(n);
		}

		if (formals.getArity() == 0) {
			tries = 1;
		}

		for (int i = 0; i < tries; i++) {
			values = new IValue[formals.getArity()];

			for (int n = 0; n < formals.getArity(); n++) {
				values[n] = arbitrary(types[n], maxDepth, declEnv.getRoot(), vf);
			}

			try {
				IValue result = function.call(types, values).getValue();
				if (!((IBool) result).getValue()) {
					out.println("FAILED with " + Arrays.toString(values));
					return false;
				} else if (verbose) {
					out.println((i + 1) + ": Checked with " + Arrays.toString(values) + ": true");
				}
			} catch (Throwable e) {
				out.println("FAILED with " + Arrays.toString(values));
				out.println(e.getMessage());
				return false;
			}

		}

		out.println("Not refuted after " + tries + " tries with maximum depth " + maxDepth);

		return true;

	}

	public void resetGenerator(IValue type) {
		Type reified = Cobra.reifyType(type);
		generators.remove(reified);
	}

	public void setGenerator(IValue f) {
		FunctionType functionType = (FunctionType) f.getType();
		Type returnType = functionType.getReturnType();

		ICallableValue generator = (ICallableValue) f;

		generators.put(returnType, generator);
	}

}
