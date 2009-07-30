package org.meta_environment.rascal.interpreter.result;

import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.TraversalEvaluator;
import org.meta_environment.rascal.interpreter.env.Environment;

public class ConstructorFunction extends Lambda {
	private Type constructorType;
	private TraversalEvaluator te;

	public ConstructorFunction(AbstractAST ast, Evaluator eval, Environment env, Type constructorType) {
		super(ast, eval, constructorType.getAbstractDataType(), constructorType.getName(), constructorType.getFieldTypes(), false, null, env);
		this.constructorType = constructorType;
		this.te = new TraversalEvaluator(eval.vf, eval);
	}

	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals,
			IEvaluatorContext ctx) {
		return te.applyRules(makeResult(constructorType, constructorType.make(getValueFactory(), actuals), ctx), ctx);
	}
}
