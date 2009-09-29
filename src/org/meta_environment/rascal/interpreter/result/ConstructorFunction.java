package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.TraversalEvaluator;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.types.FunctionType;
import org.meta_environment.rascal.interpreter.types.RascalTypeFactory;
import org.meta_environment.uptr.Factory;

public class ConstructorFunction extends NamedFunction {
	private Type constructorType;
	private TraversalEvaluator te;

	public ConstructorFunction(AbstractAST ast, Evaluator eval, Environment env, Type constructorType) {
		super(ast, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes()), constructorType.getName(), false, env);
		this.constructorType = constructorType;
		this.te = new TraversalEvaluator(eval);
	}

	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals) {
		if (constructorType == Factory.Tree_Appl) {
			return new ConcreteConstructorFunction(ast, eval, declarationEnvironment).call(actualTypes, actuals);
		}
		
		Map<Type,Type> bindings = new HashMap<Type,Type>();
		constructorType.getFieldTypes().match(TF.tupleType(actualTypes), bindings);
		Type instantiated = constructorType.instantiate(ctx.getCurrentEnvt().getStore(), bindings);
		
		// TODO: the actual construction of the tree before applying rules should be avoided here!
		return makeResult(instantiated, te.applyRules(instantiated, instantiated.make(getValueFactory(), actuals)), ctx);
	}
	
	@Override
	public int hashCode() {
		return constructorType.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConstructorFunction) {
			return constructorType == ((ConstructorFunction) obj).constructorType;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return constructorType.toString();
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToConstructorFunction(this);
	}
	
	@Override
	public <U extends IValue> Result<U> equalToConstructorFunction(ConstructorFunction that) {
		return ResultFactory.bool((constructorType == that.constructorType), ctx);
	}
}
