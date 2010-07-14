package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.TraversalEvaluator;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.values.uptr.Factory;

public class ConstructorFunction extends NamedFunction {
	private Type constructorType;
	private TraversalEvaluator te;

	public ConstructorFunction(AbstractAST ast, Evaluator eval, Environment env, Type constructorType) {
		super(ast, eval, (FunctionType) RascalTypeFactory.getInstance().functionType(constructorType.getAbstractDataType(), constructorType.getFieldTypes()), constructorType.getName(), false, env, true);
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
		Type formalTypeParameters = constructorType.getAbstractDataType().getTypeParameters();
		Type instantiated = constructorType;
		
		if (!formalTypeParameters.isVoidType()) {
			for (Type field : formalTypeParameters) {
				if (!bindings.containsKey(field)) {
					bindings.put(field, TF.voidType());
				}
			}
			instantiated = constructorType.instantiate(bindings);
		}
		
		// TODO: the actual construction of the tree before applying rules should be avoided here!
		return makeResult(instantiated, te.applyRules(instantiated, instantiated.make(getValueFactory(), ctx.getCurrentEnvt().getStore(), actuals)), ctx);
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
