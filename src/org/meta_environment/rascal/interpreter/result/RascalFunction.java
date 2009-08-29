package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.types.FunctionType;
import org.meta_environment.rascal.interpreter.utils.Names;

public class RascalFunction extends AnonymousFunction {
	private final String name;

	public RascalFunction(Evaluator eval, FunctionDeclaration func, boolean varargs, Environment env) {
		super(func, eval,
				(FunctionType) TE.eval(func.getSignature(), env),
				varargs,
				func.getBody().getStatements(), env);
		this.name = Names.name(func.getSignature().getName());
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(
			Result<V> that, IEvaluatorContext ctx) {
		return that.equalToRascalFunction(this, ctx);
	}
	
	@Override
	public <U extends IValue> Result<U> equalToRascalFunction(
			RascalFunction that, IEvaluatorContext ctx) {
		return ResultFactory.bool(this == that);
	}
}
