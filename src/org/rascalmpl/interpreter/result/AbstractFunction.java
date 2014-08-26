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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IAnnotatable;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.IWithKeywordParameters;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.exceptions.IllegalOperationException;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.eclipse.imp.pdb.facts.util.AbstractSpecialisedImmutableMap;
import org.eclipse.imp.pdb.facts.util.ImmutableMap;
import org.eclipse.imp.pdb.facts.visitors.IValueVisitor;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.KeywordFormals;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.staticErrors.UnexpectedKeywordArgumentType;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.types.RascalTypeFactory;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.uri.URIUtil;

abstract public class AbstractFunction extends Result<IValue> implements IExternalValue, ICallableValue {
	protected static final TypeFactory TF = TypeFactory.getInstance();
    
	protected final Environment declarationEnvironment;
	protected final IEvaluator<Result<IValue>> eval;
    
	protected final FunctionType functionType;
	protected final boolean hasVarArgs;
	protected boolean hasKeyArgs;
	protected final Map<String, Expression> keywordParameterDefaults = new HashMap<>();
	
	protected final static TypeStore hiddenStore = new TypeStore();

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

	protected static List<KeywordFormal> getFormals(FunctionDeclaration func) {
		KeywordFormals keywordFormals = func.getSignature().getParameters().getKeywordFormals();
		return keywordFormals.hasKeywordFormalList() ? keywordFormals.getKeywordFormalList() : Collections.<KeywordFormal>emptyList();
	}
	
	public boolean isTest() {
		return false;
	}
	
	@Override
	public Type getKeywordArgumentTypes() {
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

	public String getFirstOutermostConstructorLabel() {
		return null;
	}
	
	public IConstructor getFirstOutermostProduction() {
		return null;
	}
	
	@Override
	public IValue getValue() {
		return this;
	}

	public Environment getEnv() {
		return declarationEnvironment;
	}
	
	public boolean match(Type actuals) {
		if (actuals.isSubtypeOf(getFormals())) {
			return true;
		}
		
		if (hasVarArgs) {
			return matchVarArgsFunction(actuals);
		}
		
		return false;
	}
	
	@Override
	public boolean hasVarArgs() {
		return hasVarArgs;
	}
	
	@Override
	public boolean hasKeywordArgs() {
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

	private boolean matchVarArgsFunction(Type actuals) {
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
	
	protected void printHeader(StringBuilder b) {
		b.append(getReturnType());
		b.append(' ');
		b.append(getName());
		b.append('(');
		Type formals = getFormals();
		for (int i = 0; i < formals.getArity(); i++) {
			b.append(formals.getFieldType(i));
		    b.append(' ');
		    b.append(formals.getFieldName(i));
		    
		    if (i < formals.getArity() - 1) {
		    	b.append(',');
		    }
		}
		b.append(')');
	}

	
	protected void printStartTrace() {
		StringBuilder b = new StringBuilder();
		b.append("call  >");
		printNesting(b);
		printHeader(b);
		eval.getStdOut().println(b.toString());
		eval.getStdOut().flush();
		callNesting++;
	}

	protected void printFinally() {
		callNesting--;
		StringBuilder b = new StringBuilder();
		b.append("except>");
		printNesting(b);
		printHeader(b);
		eval.getStdOut().println(b.toString());
		eval.getStdOut().flush();
	}
	
	protected void printEndTrace() {
		callNesting--;
		StringBuilder b = new StringBuilder();
		b.append("return>");
		printNesting(b);
		printHeader(b);
		eval.getStdOut().println(b);
		eval.getStdOut().flush();
	}
	
	protected void bindTypeParameters(Type actualTypes, Type formals, Environment env) {
		try {
			Map<Type, Type> bindings = new HashMap<Type, Type>();
			if (!formals.match(actualTypes, bindings)) {
				throw new MatchFailed();
			}
			env.storeTypeBindings(bindings);
		}
		catch (FactTypeUseException e) {
			throw new UnexpectedType(formals, actualTypes, ast);
		}
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
	public boolean isEqual(IValue other) throws FactTypeUseException {
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
		return (FunctionType) getType();
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
		return 7 + (declarationEnvironment != null ? declarationEnvironment.hashCode() * 17: 17) + (ast != null ? ast.hashCode() * 23 : 23);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
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
	public boolean isAnnotatable() {
		return false;
	}

	@Override
	public IAnnotatable<? extends IValue> asAnnotatable() {
		throw new IllegalOperationException(
				"Cannot be viewed as annotatable.", getType());
	}

	@Override
	public boolean mayHaveKeywordParameters() {
	  return false; // this is not a data value
	}
	
	@Override
	public IWithKeywordParameters<? extends IValue> asWithKeywordParameters() {
	  // this is not a data value
	  throw new IllegalOperationException(
        "Facade cannot be viewed as with keyword parameters.", getType());
	}

	protected void bindKeywordArgs(Map<String, IValue> keyArgValues) {
	    Environment env = ctx.getCurrentEnvt();
	
	    if (functionType.hasKeywordParameters()) {
	        for (String kwparam : functionType.getKeywordParameterTypes().getFieldNames()){
	            Type kwType = functionType.getKeywordParameterType(kwparam);
	
	            if (keyArgValues.containsKey(kwparam)){
	                IValue r = keyArgValues.get(kwparam);
	
	                if(!r.getType().isSubtypeOf(kwType)) {
	                    throw new UnexpectedKeywordArgumentType(kwparam, kwType, r.getType(), ctx.getCurrentAST());
	                }
	
	                env.declareVariable(kwType, kwparam);
	                env.storeVariable(kwparam, ResultFactory.makeResult(kwType, r, ctx));
	            } 
	            else {
	                env.declareVariable(kwType, kwparam);
	                Expression def = getKeywordParameterDefaults().get(kwparam);
	                Result<IValue> kwResult = def.interpret(eval);
	                env.storeVariable(kwparam, kwResult);
	            }
	        }
	    }
	    // TODO: what if the caller provides more arguments then are declared? They are
	    // silently lost here.
	}
	
	public Map<String, IValue> computeKeywordArgs(IValue[] oldActuals, Map<String, IValue> keyArgValues) {
		Environment env = new Environment(vf.sourceLocation(URIUtil.rootScheme("initializer")), "keyword parameter initializer");
		Environment old = ctx.getCurrentEnvt();
		Type formals = getFunctionType().getArgumentTypes();
		
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
			bindKeywordArgs(keyArgValues);

			ImmutableMap<String, IValue> result = AbstractSpecialisedImmutableMap.mapOf();
			for (int i = 0; i < kwType.getArity(); i++) {
				String fieldName = kwType.getFieldName(i);
				result = result.__put(fieldName, env.getVariable(fieldName).getValue());
			}
			
			return result;
		}
		finally {
			ctx.setCurrentEnvt(old);
		}
	}
}
