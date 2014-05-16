/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI - added type parameters
 *******************************************************************************/
package org.rascalmpl.library.cobra;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.types.FunctionType;

public class QuickCheck {
	
	private final String EXPECT_TAG = "expected";

	private static class InstanceHolder {
		public static final QuickCheck sInstance = new QuickCheck();
	}

	static final HashMap<Type, ICallableValue> generators = new HashMap<Type, ICallableValue>();

	public static QuickCheck getInstance() {
		return InstanceHolder.sInstance;
	}

	public IValue arbitrary(Type type, int depthLimit, Environment env,
			IValueFactory vf, Map<Type, Type> typeParameters) {

		RandomValueTypeVisitor visitor = new RandomValueTypeVisitor(vf,
				(ModuleEnvironment) env, depthLimit, generators, typeParameters);

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

		String fname = function.getName();
	
		Environment declEnv = function.getEnv();
		IValueFactory vf = function.getEval().getValueFactory();
		Type formals = function.getFormals();
		String expected = null;
		
		if(function.hasTag(EXPECT_TAG)){
			expected = function.getTag(EXPECT_TAG);
		}
		

		Type[] types = new Type[formals.getArity()];
		IValue[] values = new IValue[formals.getArity()];

		for (int n = 0; n < formals.getArity(); n++) {
			types[n] = formals.getFieldType(n);
		}

		if (formals.getArity() == 0) {
			tries = 1;
		}

		TypeParameterVisitor tpvisit = new TypeParameterVisitor();
		
		for (int i = 0; i < tries; i++) {
			values = new IValue[formals.getArity()];

			HashMap<Type, Type> tpbindings = tpvisit.bindTypeParameters(formals);
			for (int n = 0; n < formals.getArity(); n++) {
				values[n] = arbitrary(types[n], maxDepth, declEnv.getRoot(), vf, tpbindings);
			}
			boolean expectedThrown = false;
			try {
				Type[] actualTypes = new Type[formals.getArity()];
				for(int j = 0; j < types.length; j ++) {
					actualTypes[j] = types[j].instantiate(tpbindings);
				}
				IValue result = function.call(actualTypes, values, null).getValue();
				function.getEval().getStdOut().flush();
				if (!((IBool) result).getValue()) {
					reportFailed(fname, "Test returns false", tpbindings, formals, values, out);
					return false;
				} else if (verbose && formals.getArity() > 0) {
					out.println((i + 1) + ": Checked with " + Arrays.toString(values) + ": true");
				}
			} catch (Throw e){
				if(expected == null || !((IConstructor)e.getException()).getName().equals(expected)){
					return reportFailed(fname, e.getMessage(), tpbindings, formals, values, out);
				}
				expectedThrown = true;
			}
			catch (Throwable e) {
				if(expected == null || !e.getClass().toString().endsWith("." + expected)){
					return reportFailed(fname, e.getMessage(), tpbindings, formals, values, out);
				}
				expectedThrown = true;
			}
			if(expected != null && !expectedThrown){
				return reportMissingException(fname, expected, out);
			}
		}

		out.println("Test " + fname + (formals.getArity() > 0 ? " not refuted after " + tries + " tries with maximum depth " + maxDepth
				: " succeeded"));

		return true;
	}
	
	private boolean reportFailed(String name, String msg, HashMap<Type, Type> tpbindings, Type formals, IValue[] values, PrintWriter out){
		out.println("Test " + name + " failed due to\n\t" + msg + "\n");
		if(tpbindings.size() > 0){
			out.println("Type parameters:");
			for(Type key : tpbindings.keySet()){
				out.println("\t" + key + " => " + tpbindings.get(key));
			}
		}
		out.println("Actual parameters:");
		for (int i = 0; i < formals.getArity(); i++) {
			IValue arg = values[i];
			out.println("\t" + formals.getFieldType(i) + " " + "=>" + arg);
		}
		out.println();
		return false;
	}
	
	private boolean reportMissingException(String name, String msg, PrintWriter out){
		out.println("Test " + name + " failed due to\n\tmissing exception: " + msg + "\n");
		return false;
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
