package org.rascalmpl.interpreter.result;

import java.util.Map;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

/**
 * This class wraps any function result implementation with a more specific (inferred)
 * function type than the original (possibly generic) function definition has.
 * It forwards all methods to the wrapped instance, but returns a more precise type
 * on getType
 */
public final class FunctionResultFacade extends Result<IValue> {
	private final Result<IValue> wrapped;
	private final Type functionType;
    
    protected FunctionResultFacade(Type type, Result<IValue> result, IEvaluatorContext ctx) {
        super(TypeFactory.getInstance().valueType(), result.getValue(), ctx);
		assert type.comparable(result.getType());
		assert result instanceof ICallableValue;
		this.wrapped = result;
		this.functionType = type;
    }
    
    public Type getType() { 
		return functionType;
	}
	
	@Override
	public boolean hasNext(){
		return wrapped.hasNext();
	}
	
	@Override
	public Result<IValue> next(){
		return wrapped.next();
	}

	@Override
	public void remove() {
		wrapped.remove();
	}
	

    @Override
	public Result<IValue> call(Type[] argTypes, IValue[] argValues, Map<String, IValue> keyArgValues) throws MatchFailed {
        return wrapped.call(argTypes, argValues, keyArgValues);
    }
    
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return wrapped.compose(right);
	}
	
	public <V extends IValue> Result<IBool> equals(Result<V> that) {
		return wrapped.equals(that);
	}
	
	public <V extends IValue> Result<IBool> nonEquals(Result<V> that) { 
		return wrapped.nonEquals(that);
	}
	
	protected <U extends IValue> Result<U> composeRelation(RelationResult that) {
		return wrapped.composeRelation(that);
	}
	
	protected <U extends IValue> Result<U> composeListRelation(ListRelationResult that) {
		return wrapped.composeListRelation(that);
	}
	
	public <U extends IValue> Result<U> composeMap(MapResult that) {
		return wrapped.composeMap(that);
	}
	
	public <U extends IValue> Result<U> addFunctionNonDeterministic(AbstractFunction that) {
		return wrapped.addFunctionNonDeterministic(that);
	}
	
	public <U extends IValue> Result<U> addFunctionNonDeterministic(OverloadedFunction that) {
		return wrapped.addFunctionNonDeterministic(that);
	}
	
	public <U extends IValue> Result<U> addFunctionNonDeterministic(ComposedFunctionResult that) {
		return wrapped.addFunctionNonDeterministic(that);
	}
	
	public <U extends IValue> Result<U> composeFunction(AbstractFunction that) {
		return wrapped.composeFunction(that);
	}
	
	public <U extends IValue> Result<U> composeFunction(OverloadedFunction that) {
		return wrapped.composeFunction(that);
	}
	
	public <U extends IValue> Result<U> composeFunction(ComposedFunctionResult that) {
		return wrapped.composeFunction(that);
	}
	
	public Result<IBool> equalToOverloadedFunction(OverloadedFunction that) {
		return wrapped.equalToOverloadedFunction(that);
	}
	
	public Result<IBool> equalToConstructorFunction(ConstructorFunction that) {
		return wrapped.equalToConstructorFunction(that);
	}
	
	public Result<IBool> equalToRascalFunction(RascalFunction that) {
		return wrapped.equalToRascalFunction(that);
	}

	
	public boolean isPublic() {
		return wrapped.isPublic();
	}
	
	public void setPublic(boolean isPublic) {
		wrapped.setPublic(isPublic);
	}

	public void setInferredType(boolean inferredType) {
		wrapped.setInferredType(inferredType);
	}
	
	public boolean hasInferredType() {
		return wrapped.hasInferredType();
	}
	
	public Type getKeywordArgumentTypes(Environment env) {
		return wrapped.getKeywordArgumentTypes(env);
	}
}
