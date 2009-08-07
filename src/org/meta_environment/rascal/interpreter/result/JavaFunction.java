package org.meta_environment.rascal.interpreter.result;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluatorContext;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.control_exceptions.Throw;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.utils.JavaBridge;
import org.meta_environment.rascal.interpreter.utils.Names;


public class JavaFunction extends Lambda {
	private final Method method;
	private FunctionDeclaration func;
	
	@SuppressWarnings("unchecked")
	public JavaFunction(Evaluator eval, FunctionDeclaration func, boolean varargs, Environment env, JavaBridge javaBridge) {
		super(func, eval,
				TE.eval(func.getSignature().getType(),env),
				Names.name(func.getSignature().getName()), 
				TE.eval(func.getSignature().getParameters(), env),
				varargs, Collections.EMPTY_LIST, env);
		this.method = javaBridge.compileJavaMethod(func, env);
		this.func = func;
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals,
			IEvaluatorContext ctx) {
		if (hasVarArgs) {
			actuals = computeVarArgsActuals(actuals, formals);
		}
		
		if (callTracing) {
			printStartTrace();
		}

		Environment old = ctx.getCurrentEnvt();
		try {
			ctx.pushEnv();
			IValue result = invoke(actuals);

			Type actualTypesTuple;
			
			if (hasVarArgs) {
				actualTypesTuple = computeVarArgsActualTypes(actualTypes, formals);
			}
			else {
				actualTypesTuple = TF.tupleType(actualTypes);
			}

			Environment env = ctx.getCurrentEnvt();
			bindTypeParameters(actualTypesTuple, formals, env); 
			Type resultType = returnType.instantiate(env.getStore(), env.getTypeBindings());
			return ResultFactory.makeResult(resultType, result, eval);
		}
		finally {
			if (callTracing) {
				printEndTrace();
			}
			ctx.unwind(old);
		}
	}
	
	public IValue invoke(IValue[] actuals) {
		try {
			return (IValue) method.invoke(null, (Object[]) actuals);
		} catch (SecurityException e) {
			throw new ImplementationError("Unexpected security exception", e);
		} catch (IllegalArgumentException e) {
			throw new ImplementationError("An illegal argument was generated for a generated method", e);
		} catch (IllegalAccessException e) {
			throw new ImplementationError("Unexpected illegal access exception", e);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			
			if (targetException instanceof Throw) {
				Throw th = (Throw) targetException;
				((Throw) targetException).setLocation(eval.getCurrentAST().getLocation());
				((Throw) targetException).setTrace(eval.getStackTrace());
				throw th;
			}
			
			throw org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory.javaException(targetException.getMessage(), eval.getCurrentAST(), eval.getStackTrace());
		}
	}
	
	@Override
	public String toString() {
		return func.toString();
	}
	
}
