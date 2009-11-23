package org.meta_environment.rascal.interpreter.result;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.interpreter.Accumulator;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.TypeEvaluator;
import org.meta_environment.rascal.interpreter.control_exceptions.Failure;
import org.meta_environment.rascal.interpreter.control_exceptions.Return;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.staticErrors.MissingReturnError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnguardedFailError;
import org.meta_environment.rascal.interpreter.types.FunctionType;
import org.meta_environment.rascal.interpreter.utils.Names;

public class RascalFunction extends NamedFunction {
	private final List<Statement> body;
	private final boolean isVoidFunction;
	private final Stack<Accumulator> accumulators;
			
	public RascalFunction(Evaluator eval, FunctionDeclaration func, boolean varargs, Environment env,
				Stack<Accumulator> accumulators) {
		this(func, eval,
				(FunctionType) new TypeEvaluator(env).eval(func.getSignature()),
				varargs,
				func.getBody().getStatements(), env, accumulators);
		this.name = Names.name(func.getSignature().getName());
	}
	
	@SuppressWarnings("unchecked")
	public RascalFunction(AbstractAST ast, Evaluator eval, FunctionType functionType,
			boolean varargs, List<Statement> body, Environment env, Stack<Accumulator> accumulators) {
		super(ast, eval, functionType, null, varargs, env);
		this.body = body;
		this.isVoidFunction = this.functionType.getReturnType().isSubtypeOf(TF.voidType());
		this.accumulators = (Stack<Accumulator>) accumulators.clone();
	}
	
	public boolean isAnonymous() {
		return getName() == null;
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals) {
		Map<Type,Type> bindings = declarationEnvironment.getTypeBindings();
		Type instantiatedFormals = getFormals().instantiate(declarationEnvironment.getStore(), bindings);
		
		if (callTracing) {
			printStartTrace();
		}

		Type actualTypesTuple;
		if (hasVarArgs) {
			actualTypesTuple = computeVarArgsActualTypes(actualTypes, instantiatedFormals);
		}
		else {
			actualTypesTuple = TF.tupleType(actualTypes);
		}

		Environment old = ctx.getCurrentEnvt();
		Stack<Accumulator> oldAccus = ctx.getAccumulators();
		
		try {
			ctx.setCurrentEnvt(new Environment(declarationEnvironment, ctx.getCurrentEnvt(), ctx.getCurrentAST().getLocation(), ast.getLocation(), isAnonymous()?"Anonymous Function":name));
			ctx.setAccumulators(accumulators);
			ctx.pushEnv();
			
			bindTypeParameters(actualTypesTuple, instantiatedFormals, ctx.getCurrentEnvt());

			if (hasVarArgs) {
				actuals = computeVarArgsActuals(actuals, getFormals());
			}

			assignFormals(actuals, ctx.getCurrentEnvt());

			for (Statement stat: body) {
				eval.setCurrentAST(stat);
				stat.accept(eval);
			}
			
			if(!isVoidFunction){
				throw new MissingReturnError(ast);
			}

			return ResultFactory.makeResult(TF.voidType(), null, eval);
		}
		catch (Return e) {
			Result<IValue> result = e.getValue();

			Type returnType = getReturnType();
			Type instantiatedReturnType = returnType.instantiate(ctx.getCurrentEnvt().getStore(), ctx.getCurrentEnvt().getTypeBindings());

			if(!result.getType().isSubtypeOf(instantiatedReturnType)){
				throw new UnexpectedTypeError(returnType, result.getType(), e.getLocation());
			}
			
			if (!returnType.isVoidType() && result.getType().isVoidType()) {
				throw new UnexpectedTypeError(returnType, result.getType(), e.getLocation());
			}

			return ResultFactory.makeResult(instantiatedReturnType, result.getValue(), eval);
		} 
		catch (Failure e) {
			throw new UnguardedFailError(ast);
		}
		finally {
			if (callTracing) {
				printEndTrace();
			}
			ctx.setCurrentEnvt(old);
			ctx.setAccumulators(oldAccus);
		}
	}
	
	private void assignFormals(IValue[] actuals, Environment env) {
		Type formals = getFormals();
		for (int i = 0; i < formals.getArity(); i++) {
			Type formal = formals.getFieldType(i).instantiate(env.getStore(), env.getTypeBindings());
			
			Result<IValue> result;
			if (actuals[i] instanceof AbstractFunction) {
				result = (AbstractFunction)actuals[i];
			}
			else {	
				result = ResultFactory.makeResult(formal, actuals[i], eval);
			}
			
			env.declareVariable(formals.getFieldType(i), formals.getFieldName(i));
			env.storeVariable(formals.getFieldName(i), result);
		}
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToRascalFunction(this);
	}
	
	@Override
	public <U extends IValue> Result<U> equalToRascalFunction(RascalFunction that) {
		return ResultFactory.bool((this == that), ctx);
	}
}
