/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Emilie Balland - (CWI)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anya Helene Bagge - anya@ii.uib.no (UiB)
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.interpreter.Configuration;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Throw;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Names;

public class JavaMethod extends NamedFunction {
	private final Object instance;
	private final Method method;
	private final boolean hasReflectiveAccess;
	
	public JavaMethod(IEvaluator<Result<IValue>> eval, FunctionDeclaration func, boolean varargs, Environment env, JavaBridge javaBridge){
		super(func, eval, (FunctionType) func.getSignature().typeOf(env), Names.name(func.getSignature().getName()), varargs, env);
		
		this.hasReflectiveAccess = hasReflectiveAccess(func);
		this.instance = javaBridge.getJavaClassInstance(func);
		this.method = javaBridge.lookupJavaMethod(eval, func, env, hasReflectiveAccess);
	}
	
	@Override
	public boolean isStatic() {
		return true;
	}
	
	@Override
	public boolean isDefault() {
		return false;
	}
	
	private boolean hasReflectiveAccess(FunctionDeclaration func) {
		for (Tag tag : func.getTags().getTags()) {
			if (Names.name(tag.getName()).equals("reflect")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals) {
		Type actualTypesTuple;
		Type formals = getFormals();
		Object[] oActuals;

		if (hasVarArgs) {
			oActuals = computeVarArgsActuals(actuals, formals);
		}
		else {
			oActuals = actuals;
		}

		if (hasReflectiveAccess) {
			oActuals = addCtxActual(oActuals);
		}

		if (callTracing) {
			printStartTrace();
		}

		Environment old = ctx.getCurrentEnvt();

		try {
			ctx.pushEnv();

			if (hasVarArgs) {
				actualTypesTuple = computeVarArgsActualTypes(actualTypes, formals);
			}
			else {
				actualTypesTuple = TF.tupleType(actualTypes);
			}

			Environment env = ctx.getCurrentEnvt();
			bindTypeParameters(actualTypesTuple, formals, env); 
			
			IValue result = invoke(oActuals);
			
			Type resultType = getReturnType().instantiate(env.getTypeBindings());
			
			return ResultFactory.makeResult(resultType, result, eval);
		}
		catch (Throw t) {
			throw t;
		}
		finally {
			if (callTracing) {
				printEndTrace();
			}
			ctx.unwind(old);
		}
	}
	
	private Object[] addCtxActual(Object[] oActuals) {
		Object[] newActuals = new Object[oActuals.length + 1];
		System.arraycopy(oActuals, 0, newActuals, 0, oActuals.length);
		newActuals[oActuals.length] = ctx;
		return newActuals;
	}

	public IValue invoke(Object[] oActuals) {
		try {
			return (IValue) method.invoke(instance, oActuals);
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
				String trace = th.getTrace();
				if(trace == null)
					trace = "";
				ISourceLocation loc = th.getLocation();
				if(loc != null) {
					trace = "\t" + loc.getURI().getRawPath() + ":" + loc.getBeginLine() + "," + loc.getBeginColumn() + "\n" + trace;
				}
				trace = trace + "\t" + "somewhere in: " + method.toString() + "\n";
				((Throw) targetException).setLocation(eval.getCurrentAST().getLocation());
				((Throw) targetException).setTrace(trace + eval.getStackTrace());
				throw th;
			}
			else if (targetException instanceof StaticError) {
				throw (StaticError) targetException;
			}
			else if (targetException instanceof ImplementationError) {
				ImplementationError ex = (ImplementationError) targetException;
			    throw ex;
			}
			else if (targetException instanceof OutOfMemoryError) {
				throw new ImplementationError("out of memory", targetException);
			}
			
			if(Configuration.printErrors()){
				targetException.printStackTrace();
			}
			
			try {
				String msg = targetException.getMessage() != null ? targetException.getMessage() : targetException.getClass().getName();
				ByteArrayOutputStream trace = new ByteArrayOutputStream();
			
				StackTraceElement[] stackTrace = targetException.getStackTrace();
				if(stackTrace != null) {
					for (StackTraceElement elem : stackTrace) {
						if (elem.getMethodName().equals("invoke")) {
							break;
						}
						trace.write(("\n\t" +  elem.getClassName() + "." + elem.getMethodName() + "(" + elem.getFileName() + ":" + elem.getLineNumber() + ")").getBytes());
					}
				}
				String traceStr = trace.toString() + "\n" + eval.getStackTrace();
				throw org.rascalmpl.interpreter.utils.RuntimeExceptionFactory.javaException(msg, eval.getCurrentAST(), traceStr);
			} catch (IOException e1) {
				throw new ImplementationError("Could not create stack trace", e1);
			}
		}
	}
}
