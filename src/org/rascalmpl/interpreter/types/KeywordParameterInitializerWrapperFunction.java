package org.rascalmpl.interpreter.types;

import java.util.Map;

import org.eclipse.imp.pdb.facts.IKeywordParameterInitializer;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.primitive.ExternalValue;
import org.eclipse.imp.pdb.facts.type.ExternalType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.util.AbstractSpecialisedImmutableMap;
import org.eclipse.imp.pdb.facts.util.ImmutableMap;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.values.uptr.Factory;

public class KeywordParameterInitializerWrapperFunction extends ExternalValue implements ICallableValue {
	private final IKeywordParameterInitializer initializer;
	private final IEvaluatorContext ctx;

	public KeywordParameterInitializerWrapperFunction(IKeywordParameterInitializer initializer, IEvaluatorContext ctx) {
		super((ExternalType) Factory.InitializerMap.getValueType());
		this.ctx = ctx;
		this.initializer = initializer;
	}

	public IKeywordParameterInitializer getInitializer() {
		return initializer;
	}
	
	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public boolean hasVarArgs() {
		return false;
	}

	@Override
	public boolean hasKeywordArgs() {
		return false;
	}

	@Override
	public Result<IValue> call(IRascalMonitor monitor,Type[] argTypes, IValue[] argValues,	Map<String, IValue> keyArgValues) {
		return call(argTypes, argValues, keyArgValues);
	}

	private Type getReturnType() {
		return ((FunctionType) getType()).getReturnType();
	}

	@Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues,
			Map<String, IValue> keyArgValues) {
		assert argTypes[0].isMap();
		IMap env = (IMap) argValues[0];
		ImmutableMap<String, IValue> argEnv = AbstractSpecialisedImmutableMap.mapOf();
		
		for (IValue key : env) {
			argEnv = argEnv.__put(((IString) key).getValue(), env.get(key));
		}
		
		return ResultFactory.makeResult(getReturnType(), initializer.initialize(argEnv), ctx);
	}

	@Override
	public ICallableValue cloneInto(Environment env) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public IEvaluator<Result<IValue>> getEval() {
		return ctx.getEvaluator();
	}
	
	@Override
	public String toString() {
		return "value () { return " + initializer + "; }";
	}
}
