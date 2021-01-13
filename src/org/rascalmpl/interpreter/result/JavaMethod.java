/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.StackTrace;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.StaticError;
import org.rascalmpl.interpreter.utils.JavaBridge;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.uri.URIUtil;

import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class JavaMethod extends NamedFunction {
	private final Object instance;
	private final Method method;
	private final boolean hasReflectiveAccess;
	private final JavaBridge javaBridge;
	
	public JavaMethod(IEvaluator<Result<IValue>> eval, FunctionDeclaration func, boolean varargs, Environment env, JavaBridge javaBridge){
		this(eval, 
			func.getSignature().typeOf(env, eval, false), 
			func.getSignature().typeOf(env, eval, false).instantiate(env.getDynamicTypeBindings()),
			func, 
			hasTestMod(func.getSignature()), 
			isDefault(func), 
			varargs, 
			env, 
			javaBridge
		);
	}
	
	@Override
	public Type getFormals() {
		// get formals is overridden because we can provide labeled parameters for JavaMethods (no pattern matching)
		FunctionDeclaration func = (FunctionDeclaration) getAst();
		
		List<Expression> formals = func.getSignature().getParameters().getFormals().getFormals();
		int arity = formals.size();
		Type[] types = new Type[arity];
		String[] labels = new String[arity];
		Type ft = getFunctionType();
		
		for (int i = 0; i < arity; i++) {
			types[i] = ft.getFieldType(i);
			assert formals.get(i).isTypedVariable(); // Java methods only have normal parameters as in `int i, int j`
			labels[i] = Names.name(formals.get(i).getName());
		}
		
		return TF.tupleType(types, labels);
	}
	
	/*
	 *  This one is to be called by cloneInto only, to avoid
	 *  looking into the environment again for obtaining the type.
	 *  (cloneInto is called when a moduleEnv is extended, so it
	 *  might not be finished yet, and hence not have all the
	 *  require types.
	 */
	private JavaMethod(IEvaluator<Result<IValue>> eval, Type staticType, Type dynamicType, FunctionDeclaration func, boolean varargs, boolean isTest, boolean isDefault, Environment env, JavaBridge javaBridge){
		super(func, eval, staticType, dynamicType, getFormals(func), Names.name(func.getSignature().getName()), isDefault, isTest,  varargs, env);
		this.javaBridge = javaBridge;
		this.hasReflectiveAccess = hasReflectiveAccess(func);
		this.instance = javaBridge.getJavaClassInstance(func, eval.getMonitor(), env.getStore(), eval.getOutPrinter(), eval.getErrorPrinter(), eval.getStdOut(), eval.getStdErr(), eval.getInput(), eval);
		this.method = javaBridge.lookupJavaMethod(eval, func, env, hasReflectiveAccess);
	}

	@Override
	public JavaMethod cloneInto(Environment env) {
		JavaMethod jm = new JavaMethod(getEval(), getFunctionType(), getType(), (FunctionDeclaration)getAst(), isDefault, isTest, hasVarArgs, env, javaBridge);
		jm.setPublic(isPublic());
		return jm;
	}
	
	@Override
	public boolean isStatic() {
		return true;
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
	public Result<IValue> call(Type[] actualStaticTypes, IValue[] actuals, Map<String, IValue> keyArgValues) {
		Result<IValue> resultValue = getMemoizedResult(actuals, keyArgValues);
		
		if (resultValue !=  null) {
			return resultValue;
		}
		Type actualTypesTuple;
		Type formals = getFormals();

		
		if (hasVarArgs) {
            actuals = computeVarArgsActuals(actuals, formals);
            actualTypesTuple = computeVarArgsActualTypes(actualStaticTypes, formals);
		}
		else {
		    actualTypesTuple = TF.tupleType(actualStaticTypes);
		}
		
		if (!actualTypesTuple.isSubtypeOf(formals)) {
			// resolve overloading
			throw new MatchFailed();
		}

		Object[] oActuals = addKeywordActuals(actuals, formals, keyArgValues);

		if (hasReflectiveAccess) {
			oActuals = addCtxActual(oActuals);
		}

		if (callTracing) {
			printStartTrace(actuals);
		}

		Environment old = ctx.getCurrentEnvt();

		try {
			ctx.pushEnv(getName());

			Environment env = ctx.getCurrentEnvt();
			Map<Type, Type> renamings = new HashMap<>();
			Map<Type, Type> dynamicRenamings = new HashMap<>();

			bindTypeParameters(actualTypesTuple, actuals, formals, renamings, dynamicRenamings, env); 
			
			IValue result = invoke(oActuals);
			
			Type resultType = getReturnType().instantiate(env.getStaticTypeBindings());
			resultType = unrenameType(renamings, resultType);
			
			resultValue = ResultFactory.makeResult(resultType, result, eval);
			resultValue = storeMemoizedResult(actuals, keyArgValues, resultValue);
			printEndTrace(resultValue.value);
			
			if (!getReturnType().isBottom() && getReturnType().instantiate(env.getStaticTypeBindings()).isBottom()) {
			    // type parameterized functions are not allowed to return void,
			    // so if they do not throw an exception, we now do so automatically
			    throw new MatchFailed();
			}
			
			return resultValue;
		}
		catch (Throwable e) {
			printExcept(e);
			throw e;
		}
		finally {
		   
		    
			if (callTracing) {
				callNesting--;
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
	
	protected Object[] addKeywordActuals(Object[] oldActuals, Type formals, Map<String, IValue> keyArgValues) {
		if (!getFunctionType().hasKeywordParameters()) {
			return oldActuals;
		}
		
		Environment env = new Environment(declarationEnvironment, URIUtil.rootLocation("initializer"), "keyword parameter initializer");
		Environment old = ctx.getCurrentEnvt();
		
		try {
			// we set up an environment to hold the positional parameter values
			ctx.setCurrentEnvt(env);
			for (int i = 0; i < formals.getArity(); i++) {
				String fieldName = formals.getFieldName(i);
				Type fieldType = formals.getFieldType(i);
				env.declareVariable(fieldType, fieldName);
				env.storeLocalVariable(fieldName, ResultFactory.makeResult(fieldType, (IValue) oldActuals[i], ctx));
			}
		
			// then we initialize the keyword parameters in this environment
			Type kwType = getFunctionType().getKeywordParameterTypes();
			int amountOfKWArguments =  kwType.getArity();
			Object[] newActuals = new Object[oldActuals.length + amountOfKWArguments];
			System.arraycopy(oldActuals, 0, newActuals, 0, oldActuals.length);
			bindKeywordArgs(keyArgValues);
			
			// then we add the resulting values in order to the actual parameter array for the Java method
			for (int i = 0; i < amountOfKWArguments; i++) {
				newActuals[oldActuals.length + i] = env.getFrameVariable(kwType.getFieldName(i)).getValue();
			}
			
			return newActuals;
		}
		finally {
			ctx.setCurrentEnvt(old);
		}
	}
	

	public IValue invoke(Object[] oActuals) {
		try {
			return (IValue) method.invoke(instance, oActuals);
		}
		catch (InvocationTargetException e) {
			Throwable targetException = e.getTargetException();
			
			if (targetException instanceof Throw) {
				Throw th = (Throw) targetException;
				
				StackTrace trace = new StackTrace();
				trace.addAll(th.getTrace());
				
				ISourceLocation loc = th.getLocation();
				if (loc == null || loc.getScheme().equals("TODO")) {
				  loc = getAst().getLocation();
				}
				trace.add(loc, null);

				th.setLocation(loc);
				trace.addAll(eval.getStackTrace());
				th.setTrace(trace.freeze());
				throw th;
			}
			else if (targetException instanceof StaticError) {
				throw (StaticError) targetException;
			}
			else if (targetException instanceof ImplementationError) {
			  throw (ImplementationError) targetException;
			}

			if (ctx.getConfiguration().printErrors()) {
				targetException.printStackTrace();
			}
			
			throw RuntimeExceptionFactory.javaException(e.getTargetException(), getAst(), eval.getStackTrace());
		}
		catch (Throwable e) {
		    if(ctx.getConfiguration().printErrors()){
		        e.printStackTrace();
		    }

		    throw RuntimeExceptionFactory.javaException(e, getAst(), eval.getStackTrace());
		}
	}
}
