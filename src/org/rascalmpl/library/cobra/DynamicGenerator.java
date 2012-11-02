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

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.List;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;

public class DynamicGenerator extends AbstractFunction {

	private final HashMap<Type, ICallableValue> generators;

	public DynamicGenerator(IEvaluator<Result<IValue>> eval, Type returnType, Environment env,
			HashMap<Type, ICallableValue> generators) {
		super(null, eval, (FunctionType) RascalTypeFactory.getInstance()
				.functionType(returnType,
						TypeFactory.getInstance().integerType()), false, env);
		this.generators = generators;
	}

	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals) {
		Type returnType = getReturnType();
		Type instantiatedReturnType = returnType.instantiate(ctx
				.getCurrentEnvt().getTypeBindings());

		IInteger maxDepth = (IInteger) actuals[0];

		RandomValueTypeVisitor v = new RandomValueTypeVisitor(
				getValueFactory(), (ModuleEnvironment) getEnv().getRoot(),
				maxDepth.intValue(), generators);

		IValue returnVal = instantiatedReturnType.accept(v);

		return makeResult(instantiatedReturnType, returnVal, eval);

	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals, Result<IValue> self, List<String> selfParams, List<Result<IValue>> selfParamBounds) {
		return call(actualTypes, actuals);
	}

	@Override
	public boolean isDefault() {
		return false;
	}

	@Override
	public boolean isStatic() {
		return false;
	}
	
}
