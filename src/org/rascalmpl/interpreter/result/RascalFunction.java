package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.List;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.staticErrors.MissingReturnError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnguardedFailError;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.utils.Names;

public class RascalFunction extends NamedFunction {
	private final List<Statement> body;
	private final boolean isVoidFunction;
	private final Stack<Accumulator> accumulators;

	public RascalFunction(Evaluator eval, FunctionDeclaration func, boolean varargs, Environment env,
				Stack<Accumulator> accumulators) {
		this(func, eval,
				(FunctionType) func.getSignature().typeOf(env),
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
		Environment old = ctx.getCurrentEnvt();
		AbstractAST oldAST = ctx.getCurrentAST();
		Stack<Accumulator> oldAccus = ctx.getAccumulators();
		
		try {
			String label = isAnonymous()?"Anonymous Function":name;
			Environment environment = new Environment(declarationEnvironment, ctx.getCurrentEnvt(), ctx.getCurrentAST().getLocation(), ast.getLocation(), label);
			ctx.setCurrentEnvt(environment);
			ctx.setAccumulators(accumulators);
			ctx.pushEnv();
			
			if (hasVarArgs) {
				actuals = computeVarArgsActuals(actuals, getFormals());
			}

			assignFormals(actuals, ctx.getCurrentEnvt());

			if (callTracing) {
				printStartTrace();
			}
			
			for (Statement stat: body) {
				eval.setCurrentAST(stat);
				stat.interpret(eval);
			}
			
			if (callTracing) {
				printEndTrace();
			}
			
			if(!isVoidFunction){
				throw new MissingReturnError(ast);
			}

			return makeResult(TF.voidType(), null, eval);
		}
		catch (Return e) {
			Result<IValue> result = e.getValue();

			Type returnType = getReturnType();
			Type instantiatedReturnType = returnType.instantiate(ctx.getCurrentEnvt().getTypeBindings());

			if(!result.getType().isSubtypeOf(instantiatedReturnType)){
				throw new UnexpectedTypeError(instantiatedReturnType, result.getType(), e.getLocation());
			}
			
			if (!returnType.isVoidType() && result.getType().isVoidType()) {
				throw new UnexpectedTypeError(returnType, result.getType(), e.getLocation());
			}

			return makeResult(instantiatedReturnType, result.getValue(), eval);
		} 
		catch (Failure e) {
			throw new UnguardedFailError(ast);
		}
		finally {
			if (callTracing) {
				printFinally();
			}
			ctx.setCurrentEnvt(old);
			ctx.setAccumulators(oldAccus);
			ctx.setCurrentAST(oldAST);
		}
	}
	
	private void assignFormals(IValue[] actuals, Environment env) {
		List<Expression> formals = ((FunctionDeclaration) ast).getSignature().getParameters().getFormals().getFormals();
		// we assume here the list of formals is just as long as the list of actuals
		int size = actuals.length;
		IMatchingResult[] matchers = new IMatchingResult[size];
		PatternEvaluator pe = new PatternEvaluator(ctx);
		Environment[] olds = new Environment[size];
		int i = 0;
		
		if (size == 0) {
			return;
		}
		matchers[0] = formals.get(0).buildMatcher(pe);
		matchers[0].initMatch(makeResult(actuals[0].getType(), actuals[0], ctx));
		olds[0] = ctx.getCurrentEnvt();
		ctx.pushEnv();

		// pattern matching requires backtracking due to list, set and map matching and
		// non-linear use of variables between formal parameters of a function...
		
		while (i >= 0 && i < size) {
			if (ctx.isInterrupted()) {
				throw new InterruptException(ctx.getStackTrace());
			}
			if (matchers[i].hasNext() && matchers[i].next()) {
				if (i == size - 1) {
					// NB: no result handling here.
					return;
				} else {
					i++;
					matchers[i] = formals.get(i).buildMatcher(pe);
					matchers[i].init();
					olds[i] = ctx.getCurrentEnvt();
					ctx.pushEnv();
				}
			} else {
				ctx.unwind(olds[i]);
				i--;
				ctx.pushEnv();
			}
		}
		
		throw new MatchFailed();
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
