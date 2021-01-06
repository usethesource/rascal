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
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.result;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.KeywordFormals;
import org.rascalmpl.debug.IRascalMonitor;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UnexpectedKeywordArgumentType;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.utils.LimitedResultWriter;
import org.rascalmpl.interpreter.utils.LimitedResultWriter.IOLimitReachedException;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.types.FunctionType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.uri.URIUtil;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IExternalValue;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.IWithKeywordParameters;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.exceptions.IllegalOperationException;
import io.usethesource.vallang.io.StandardTextWriter;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.visitors.IValueVisitor;

abstract public class AbstractFunction extends Result<IValue> implements IExternalValue, ICallableValue, IFunction {
	protected static final TypeFactory TF = TypeFactory.getInstance();
    
	protected final Environment declarationEnvironment;
	protected final IEvaluator<Result<IValue>> eval;
    
	protected final FunctionType functionType;
	protected final boolean hasVarArgs;
	protected boolean hasKeyArgs;
	protected final Map<String, Expression> keywordParameterDefaults = new HashMap<>();
	
	protected final AbstractAST ast;
	protected final IValueFactory vf;
	
	protected static int callNesting = 0;
	protected static boolean callTracing = false;
	
	// TODO: change arguments of these constructors to use EvaluatorContexts
	public AbstractFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, FunctionType functionType, List<KeywordFormal> initializers, boolean varargs, Environment env) {
		super(functionType, null, eval);
		this.ast = ast;
		this.functionType = functionType;
		this.eval = eval;
		this.hasVarArgs = varargs;
		this.hasKeyArgs = functionType.hasKeywordParameters();
		this.declarationEnvironment = env;
		this.vf = eval.getValueFactory();
		
		for (KeywordFormal init : initializers) {
			String label = Names.name(init.getName());
			keywordParameterDefaults.put(label, init.getExpression());
		}
	}
	
	@Override
	public IConstructor encodeAsConstructor() {
	    AbstractAST ast2 = getAst();
        return getValueFactory().constructor(RascalValueFactory.Function_Function,
	            ast2 != null ? ast2.getLocation() : URIUtil.correctLocation("unknown", "", ""));
	}

	protected static List<KeywordFormal> getFormals(FunctionDeclaration func) {
		KeywordFormals keywordFormals = func.getSignature().getParameters().getKeywordFormals();
		return keywordFormals.hasKeywordFormalList() ? keywordFormals.getKeywordFormalList() : Collections.<KeywordFormal>emptyList();
	}
	
	public boolean isTest() {
		return false;
	}
	
	@Override
	public Type getKeywordArgumentTypes(Environment env) {
	  return functionType.getKeywordParameterTypes();
	}

	public boolean hasKeywordParameter(String label) {
		return functionType.hasKeywordParameter(label);
	}
	
	@Override
	public int getArity() {
		return functionType.getArgumentTypes().getArity();
	}
	
	public static void setCallTracing(boolean value){
		callTracing = value;
	}
	
	public boolean isPatternDispatched() {
		return false;
	}
	
	public boolean isConcretePatternDispatched() {
		return false;
	}
    
	public Type getFormals() {
		return functionType.getArgumentTypes();
	}

	public AbstractAST getAst() {
		return ast;
	}
	
	public boolean hasTag(String key) {
		return false;
	}

	public IValue getTag(String key) {
		return null;
	}

	public String getIndexedLabel() {
		return null;
	}
	
	public int getIndexedArgumentPosition() {
		return -1;
	}
	
	public IConstructor getIndexedProduction() {
		return null;
	}
	
	@Override
	public IValue getValue() {
		return this;
	}

	public Environment getEnv() {
		return declarationEnvironment;
	}
	
	public boolean mayMatch(Type actuals) {
		if (actuals.isSubtypeOf(getFormals())) {
			return true;
		}
		
		if (hasVarArgs) {
			return mayMatchVarArgsFunction(actuals);
		}
		
		return false;
	}
	
	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}
	
	@Override
	public boolean hasKeywordArguments() {
		return hasKeyArgs;
	}
	
	public Map<String, Expression> getKeywordParameterDefaults(){
		return keywordParameterDefaults;
	}
	
	@Override
	public Result<IValue> call(IRascalMonitor monitor, Type[] argTypes,  IValue[] argValues, Map<String, IValue> keyArgValues) {
		IRascalMonitor old = ctx.getEvaluator().setMonitor(monitor);
		try {
			return call(argTypes,argValues,  keyArgValues);
		}
		finally {
			ctx.getEvaluator().setMonitor(old);
		}
	}
	
	@Override
	public <T extends IValue> T call(Map<String, IValue> keywordParameters, IValue... parameters) {
	    synchronized (ctx.getEvaluator()) {
	        return ICallableValue.super.call(keywordParameters, parameters);
	    }
	}
	
	@Override
    public <T extends IValue> T monitoredCall(IRascalMonitor monitor, Map<String, IValue> keywordParameters, IValue... parameters) {
           synchronized (ctx.getEvaluator()) {
                return ICallableValue.super.monitoredCall(monitor, keywordParameters, parameters);
            }
    }
	
	@Override
	public <T extends IValue> T monitoredCall(IRascalMonitor monitor, IValue... parameters) {
	    synchronized (ctx.getEvaluator()) {
	        return ICallableValue.super.monitoredCall(monitor, Collections.emptyMap(), parameters);
	    }
	}
	
	private boolean mayMatchVarArgsFunction(Type actuals) {
		int arity = getFormals().getArity();
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(getFormals().getFieldType(i))) {
				return false;
			}
		}
		
		if (i > actuals.getArity()) {
			return false;
		}

		Type elementType = getFormals().getFieldType(i).getElementType();

		for (; i < actuals.getArity(); i++) {
			if (!actuals.getFieldType(i).isSubtypeOf(elementType)) {
				return false;
			}
		}
		
		return true;
	}
	
	public abstract boolean isDefault();

	
	private void printNesting(StringBuilder b) {
		for (int i = 0; i < callNesting; i++) {
			b.append('>');
		}
	}
	
	private String strval(IValue value) {
		Writer w = new LimitedResultWriter(50);
		try {
			new StandardTextWriter(true, 2).write(value, w);
			return w.toString();
		} 
		catch (IOLimitReachedException e) {
			return w.toString();
		}
		catch (IOException e) {
			return "...";
		}
	}
	protected void printHeader(StringBuilder b, IValue[] actuals) {
		b.append(moduleName());
		b.append("::");
		b.append(getName());
		b.append('(');
		Type formals = getFormals();
		int n = Math.min(formals.getArity(), actuals.length);
		for (int i = 0; i < n; i++) {
		    b.append(strval(actuals[i]));
		    
		    if (i < formals.getArity() - 1) {
		    	b.append(", ");
		    }
		}
		b.append(')');
	}

	
	protected void printStartTrace(IValue[] actuals) {
		StringBuilder b = new StringBuilder();
		b.append("call  >");
		printNesting(b);
		printHeader(b, actuals);
		eval.getOutPrinter().println(b.toString());
		eval.getOutPrinter().flush();
		callNesting++;
	}

	private String moduleName() {
		String name = getEnv().getName();
		int ind = name.lastIndexOf("::");
		if (ind != -1) {
			name = name.substring(ind + 2);
		}
		return name;
	}
	
	protected void printExcept(Throwable e) {
		if (callTracing) {
			StringBuilder b = new StringBuilder();
			b.append("except>");
			printNesting(b);
			b.append(moduleName());
			b.append("::");
			b.append(getName());
			b.append(": ");
			String msg = e.getMessage();
			b.append(msg == null ? e.getClass().getSimpleName() : msg);
			eval.getOutPrinter().println(b.toString());
			eval.getOutPrinter().flush();
		}
	}
	
	protected void printEndTrace(IValue result) {
		if (callTracing) {
			StringBuilder b = new StringBuilder();
			b.append("return>");
			printNesting(b);
			b.append(moduleName());
			b.append("::");
			b.append(getName());
			if (result != null) {
				b.append(":");
				b.append(strval(result));
			}
			
			eval.getOutPrinter().println(b);
			eval.getOutPrinter().flush();
		}
	}
	
	protected Type bindTypeParameters(Type actualStaticTypes, IValue[] actuals, Type formals, Map<Type, Type> renamings, Environment env) {
		try {
		    if (actualStaticTypes.isOpen()) {
			    // we have to make the environment hygenic now, because the caller scope
			    // may have the same type variable names as the current scope
			    actualStaticTypes = renameType(actualStaticTypes, renamings);
			}
			
			Map<Type, Type> staticBindings = new HashMap<Type, Type>();
			
			try {
			    if (formals.match(actualStaticTypes, staticBindings)) {
			        env.storeStaticTypeBindings(staticBindings);
			        // formal parameters do not have to match the static types, they only have to match the dynamic types
			        // so continue even if the static types do not match. 
			    }
			} catch (FactTypeUseException e) {
			    // this can happen if static types collide
			}
			
			Map<Type, Type> dynamicBindings = new HashMap<Type, Type>();
			
			for (int i = 0; i < formals.getArity(); i++) {
			    if (!formals.getFieldType(i).match(renameType(actuals[i].getType(), renamings), dynamicBindings)) {
			        throw new MatchFailed();
			    }
			}
			env.storeDynamicTypeBindings(dynamicBindings);
			
			
			return actualStaticTypes;
		}
		catch (FactTypeUseException e) {
			throw new UnexpectedType(formals, actualStaticTypes, ast);
		}
	}

	protected Type unrenameType(Map<Type, Type> renamings, Type resultType) {
	    if (resultType.isOpen()) {
	        // first reverse the renamings
	        Map<Type, Type> unrenamings = new HashMap<>();

	        for (Entry<Type, Type> entry : renamings.entrySet()) {
	            unrenamings.put(entry.getValue(), entry.getKey());
	        }
	        // then undo the renamings
	        resultType = resultType.instantiate(unrenamings);
	    }
	    return resultType;
	}
	  
    protected Type renameType(Type actualTypes, Map<Type, Type> renamings) {
        actualTypes.match(getTypeFactory().voidType(), renamings);
        
        // rename all the bound type parameters
        for (Entry<Type,Type> entry : renamings.entrySet()) {
            Type key = entry.getKey();
            renamings.put(key, getTypeFactory().parameterType(key.getName() + ":" + UUID.randomUUID().toString(), key.getBound()));
        }
        actualTypes = actualTypes.instantiate(renamings);
        return actualTypes;
    }	
	
	protected IValue[] computeVarArgsActuals(IValue[] actuals, Type formals) {
		int arity = formals.getArity();
		IValue[] newActuals = new IValue[arity];
		int i;
		
		if (formals.getArity() == actuals.length && actuals[actuals.length - 1].getType().isSubtypeOf(formals.getFieldType(formals.getArity() - 1))) {
			// variable length argument is provided as a list
			return actuals;
		}

		for (i = 0; i < arity - 1; i++) {
			newActuals[i] = actuals[i];
		}
		
		Type lub = TF.voidType();
		for (int j = i; j < actuals.length; j++) {
			lub = lub.lub(actuals[j].getType());
		}
		
		IListWriter list = vf.listWriter();
		list.insertAt(0, actuals, i, actuals.length - arity + 1);
		newActuals[i] = list.done();
		return newActuals;
	}
	
	protected Type computeVarArgsActualTypes(Type actualTypes, Type formals) {
		if (actualTypes.isSubtypeOf(formals)) {
			// the argument is already provided as a list
			return actualTypes;
		}
		
		int arity = formals.getArity();
		Type[] types = new Type[arity];
		java.lang.String[] labels = new java.lang.String[arity];
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			types[i] = actualTypes.getFieldType(i);
			labels[i] = formals.getFieldName(i);
		}
		
		Type lub = TF.voidType();
		for (int j = i; j < actualTypes.getArity(); j++) {
			lub = lub.lub(actualTypes.getFieldType(j));
		}
		
		types[i] = TF.listType(lub);
		labels[i] = formals.getFieldName(i);
		
		return TF.tupleType(types, labels);
	}

	protected Type computeVarArgsActualTypes(Type[] actualTypes, Type formals) {
		Type actualTuple = TF.tupleType(actualTypes);
		if (actualTuple.isSubtypeOf(formals)) {
			// the argument is already provided as a list
			return actualTuple;
		}
		
		int arity = formals.getArity();
		Type[] types = new Type[arity];
		int i;
		
		for (i = 0; i < arity - 1; i++) {
			types[i] = actualTypes[i];
		}
		
		Type lub = TF.voidType();
		for (int j = i; j < actualTypes.length; j++) {
			lub = lub.lub(actualTypes[j]);
		}
		
		types[i] = TF.listType(lub);
		
		return TF.tupleType(types);
	}

	@Override
	public <T, E extends Throwable> T accept(IValueVisitor<T,E> v) throws E {
		return v.visitExternal(this);
	}

	@Override
    public boolean match(IValue other) {
        return other == this;
    }

	public boolean isIdentical(IValue other) throws FactTypeUseException {
		return other == this;
	}
	
	
	@Override
	public <V extends IValue> LessThanOrEqualResult lessThanOrEqual(Result<V> that) {
	  return super.lessThanOrEqual(that);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> add(Result<V> that) {
		return that.addFunctionNonDeterministic(this);
	}
	
	@Override
	public OverloadedFunction addFunctionNonDeterministic(AbstractFunction that) {
		return (new OverloadedFunction(this)).add(that);
	}

	@Override
	public OverloadedFunction addFunctionNonDeterministic(OverloadedFunction that) {
		return (new OverloadedFunction(this)).join(that);
	}

	@Override
	public ComposedFunctionResult addFunctionNonDeterministic(ComposedFunctionResult that) {
		return new ComposedFunctionResult.NonDeterministic(that, this, ctx);
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> compose(Result<V> right) {
		return right.composeFunction(this);
	}
	
	@Override
	public ComposedFunctionResult composeFunction(AbstractFunction that) {
		return new ComposedFunctionResult(that, this, ctx);
	}
	
	@Override
	public ComposedFunctionResult composeFunction(OverloadedFunction that) {
		return new ComposedFunctionResult(that, this, ctx);
	}
	
	@Override
	public ComposedFunctionResult composeFunction(ComposedFunctionResult that) {
		return new ComposedFunctionResult(that, this, ctx);
	}
	
	@Override
	public String toString() {
		return getHeader() + ";";
	}
	
	public String getHeader(){
		String sep = "";
		String strFormals = "";
		for(Type tp : getFormals()){
			strFormals = strFormals + sep + tp;
			sep = ", ";
		}
		
		String name = getName();
		if (name == null) {
			name = "";
		}
		
		
		String kwFormals = "";
		
		return getReturnType() + " " + name + "(" + strFormals + kwFormals + ")";
	}
	
	public FunctionType getFunctionType() {
		return (FunctionType) getStaticType();
	}
	
	/* test if a function is of type T(T) for a given T */
	public boolean isTypePreserving() {
		Type t = getReturnType();
		return getFunctionType().equivalent(RascalTypeFactory.getInstance().functionType(t,t, TF.voidType()));
	}
	
	public String getName() {
		return "";
	}
 
	public Type getReturnType() {
		return functionType.getReturnType();
	}

	@Override
	public IEvaluator<Result<IValue>> getEval() {
		return eval;
	}
	
	@Override
	public int hashCode() {
		return 7 + (ast != null ? ast.hashCode() * 23 : 23);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() == getClass()) {
			AbstractFunction other = (AbstractFunction) obj;
			return other.declarationEnvironment == declarationEnvironment && other.ast.equals(ast);
		}
		
		return false;
	}
	
	public String getResourceScheme() {
		return null;
	}
	
	public boolean hasResourceScheme() {
		return false;
	}
	
	public String getResolverScheme() {
		return null;
	}
	
	public boolean hasResolverScheme() {
		return false;
	}

	@Override
	public boolean mayHaveKeywordParameters() {
	  return false; // this is not a data value
	}
	
	@Override
	public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
	  // this is not a data value
	  throw new IllegalOperationException(
        "Facade cannot be viewed as with keyword parameters.", getStaticType());
	}

	protected void bindKeywordArgs(Map<String, IValue> keyArgValues) {
	    Environment env = ctx.getCurrentEnvt();

	    if (functionType.hasKeywordParameters()) {
	        for (String kwparam : functionType.getKeywordParameterTypes().getFieldNames()){
	            Type kwType = functionType.getKeywordParameterType(kwparam);
	            String isSetName = makeIsSetKeywordParameterName(kwparam);
	
	            env.declareVariable(TF.boolType(), isSetName);
	            
	            if (keyArgValues != null && keyArgValues.containsKey(kwparam)){
	                IValue r = keyArgValues.get(kwparam);
	
	                if(!r.getType().isSubtypeOf(kwType)) {
	                    throw new UnexpectedKeywordArgumentType(kwparam, kwType, r.getType(), ctx.getCurrentAST());
	                }
	
	                env.declareVariable(kwType, kwparam);
	                env.storeVariable(kwparam, ResultFactory.makeResult(kwType, r, ctx));
	                env.storeVariable(isSetName, ResultFactory.makeResult(TF.boolType(), getValueFactory().bool(true), ctx));
	            } 
	            else {
	                env.declareVariable(kwType, kwparam);
	                Expression def = getKeywordParameterDefaults().get(kwparam);
	                Result<IValue> kwResult = def.interpret(eval);
	                env.storeVariable(kwparam, ResultFactory.makeResult(kwType, kwResult.getValue(), ctx));
	                env.storeVariable(isSetName, ResultFactory.makeResult(TF.boolType(), getValueFactory().bool(false), ctx));
	            }
	        }
	    }
	    // TODO: what if the caller provides more arguments then are declared? They are
	    // silently lost here.
	}

	public static String makeIsSetKeywordParameterName(String kwparam) {
		return "$" + kwparam + "isSet";
	}
}
