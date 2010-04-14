package org.rascalmpl.interpreter.result;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.TypeEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Names;

public class JavaFunction extends NamedFunction {
	private final Method method;
	private final FunctionDeclaration func;
	
	public JavaFunction(Evaluator eval, FunctionDeclaration func, boolean varargs, Environment env, JavaBridge javaBridge) {
		super(func, eval,
				(FunctionType) new TypeEvaluator(env, eval.getHeap()).eval(func.getSignature()),
				Names.name(func.getSignature().getName()), 
				varargs, env);
		this.method = javaBridge.compileJavaMethod(func, env);
		this.func = func;
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals) {
		Type formals = getFormals();
		Type returnType = getReturnType();
		
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
			
			throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.javaException(targetException.getMessage(), eval.getCurrentAST(), eval.getStackTrace());
		}
	}
	
	@Override
	public String toString() {
		return func.toString();
	}
	
}
