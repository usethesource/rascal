package org.meta_environment.rascal.interpreter.env;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.reference.ValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.FunctionDeclaration;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.JavaBridge;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.exceptions.RascalBug;
import org.meta_environment.rascal.interpreter.exceptions.RascalException;

public class JavaFunction extends Lambda {
	private final Method method;
	private FunctionDeclaration func;
	
	@SuppressWarnings("unchecked")
	public JavaFunction(Evaluator eval, FunctionDeclaration func, Environment env, JavaBridge javaBridge) {
		super(eval, func.getSignature().getType().accept(TE),
				Names.name(func.getSignature().getName()),
				func.getSignature().getParameters().accept(TE), Collections.EMPTY_LIST, env);
		this.method = javaBridge.compileJavaMethod(func);
		this.func = func;
	}
	
	@Override
	public Result call(IValue[] actuals, Type actualTypes) {
		GlobalEnvironment global = GlobalEnvironment.getInstance();
		try {
			global.pushFrame();
			
			if (hasVarArgs) {
				actuals = computeVarArgsActuals(actuals, formals);
			}
			
			IValue result = invoke(actuals);
			
			if (hasVarArgs) {
				actualTypes = computeVarArgsActualTypes(actualTypes, formals);
			}
			
			bindTypeParameters(actualTypes, formals); 
			Type resultType = returnType.instantiate(global.getTypeBindings());
			return new Result(resultType, result);
		}
		finally {
			global.popFrame();
		}
	}
	
	public IValue invoke(IValue[] actuals) {
		try {
			return (IValue) method.invoke(null, (Object[]) actuals);
		} catch (SecurityException e) {
			throw new RascalBug("Unexpected security exception", e);
		} catch (IllegalArgumentException e) {
			throw new RascalBug("An illegal argument was generated for a generated method", e);
		} catch (IllegalAccessException e) {
			throw new RascalBug("Unexpected illegal access exception", e);
		} catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			
			if (targetException instanceof RascalException) {
				throw (RascalException) targetException;
			}
			else {
				throw new RascalException(ValueFactory.getInstance(), targetException.getMessage());
			}
		}
	}
	
	@Override
	public String toString() {
		return func.toString();
	}
	
}
