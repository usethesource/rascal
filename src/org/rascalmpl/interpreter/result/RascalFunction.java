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
 *   * Wietse Venema - wietsevenema@gmail.com - CWI
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.interpreter.result;

import static org.rascalmpl.interpreter.result.ResultFactory.makeResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.FunctionDeclaration.Conditional;
import org.rascalmpl.ast.FunctionDeclaration.Default;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.Signature;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Tag;
import org.rascalmpl.ast.TagString;
import org.rascalmpl.ast.Tags;
import org.rascalmpl.ast.Type.Structured;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.staticErrors.MissingReturnError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UnguardedFailError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError;
import org.rascalmpl.interpreter.types.FunctionType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.semantics.dynamic.Tree;
import org.rascalmpl.semantics.dynamic.Tree.Appl;

public class RascalFunction extends NamedFunction {
	private final List<Statement> body;
	private final boolean isVoidFunction;
	private final Stack<Accumulator> accumulators;
	private final boolean isDefault;
	private final boolean isTest;
	private final boolean isStatic;
	private final String resourceScheme;
	private final List<Expression> formals;
	private final String firstOutermostLabel;
	private final IConstructor firstOutermostProduction;
	private final Map<String, String> tags;
	private static final String RESOURCE_TAG = "resource";


	public RascalFunction(IEvaluator<Result<IValue>> eval, FunctionDeclaration.Default func, boolean varargs, Environment env,
				Stack<Accumulator> accumulators) {
		this(func, eval,
				Names.name(func.getSignature().getName()),
				(FunctionType) func.getSignature().typeOf(env),
				varargs, isDefault(func),hasTestMod(func.getSignature()),
				func.getBody().getStatements(), env, accumulators);
	}

	public RascalFunction(IEvaluator<Result<IValue>> eval, FunctionDeclaration.Expression func, boolean varargs, Environment env,
			Stack<Accumulator> accumulators) {
		this(func, eval,
				Names.name(func.getSignature().getName()),
				(FunctionType) func.getSignature().typeOf(env), 
				varargs, isDefault(func), hasTestMod(func.getSignature()),
				Arrays.asList(new Statement[] { ASTBuilder.makeStat("Return", func.getLocation(), ASTBuilder.makeStat("Expression", func.getLocation(), func.getExpression()))}),
				env, accumulators);
	}

	@SuppressWarnings("unchecked")
	public RascalFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, String name, FunctionType functionType,
			boolean varargs, boolean isDefault, boolean isTest, List<Statement> body, Environment env, Stack<Accumulator> accumulators) {
		super(ast, eval, functionType, name, varargs, env);
		this.body = body;
		this.isDefault = isDefault;
		this.isVoidFunction = this.functionType.getReturnType().isSubtypeOf(TF.voidType());
		this.accumulators = (Stack<Accumulator>) accumulators.clone();
		this.formals = cacheFormals();
		this.firstOutermostLabel = computeFirstOutermostLabel(ast);
		this.firstOutermostProduction = computeFirstOutermostProduction(ast);
		this.isStatic = env.isRootScope() && eval.__getRootScope() != env;
		this.isTest = isTest;
		
		if (ast instanceof FunctionDeclaration) {
			tags = parseTags((FunctionDeclaration) ast);
			String resourceScheme = RascalFunction.getResourceScheme((FunctionDeclaration)ast);
			if (resourceScheme.equals("")) {
					this.resourceScheme = null;
			} else { 
				this.resourceScheme = resourceScheme;
			}
		} else {
			tags = new HashMap<String, String>();
			this.resourceScheme = null;
		}
	}
	
	private Map<String, String> parseTags(FunctionDeclaration declaration) {
		Map<String, String> result = new HashMap<String, String>();
		Tags tags = declaration.getTags();
		if (tags.hasTags()) {
			for (Tag tag : tags.getTags()) {
				if(tag.hasContents()){
					String key = Names.name(tag.getName());
					String value = ((TagString.Lexical) tag.getContents()).getString();
					if (value.length() > 2 && value.startsWith("{")) {
						value = value.substring(1, value.length() - 1);
					}
					result.put(key, value);
				}
			}
		}
		return result;
	}

	@Override
	public String getTag(String key) {
		return tags.get(key);
	}

	@Override
	public boolean hasTag(String key) {
		return tags.containsKey(key);
	}

	private static String getResourceScheme(FunctionDeclaration declaration) {
		Tags tags = declaration.getTags();
		
		if (tags.hasTags()) {
			for (Tag tag : tags.getTags()) {
				if (Names.name(tag.getName()).equals(RESOURCE_TAG)) {
					String contents = ((TagString.Lexical) tag.getContents()).getString();
					
					if (contents.length() > 2 && contents.startsWith("{")) {
						contents = contents.substring(1, contents.length() - 1);
					}
					return contents;
				}
			}
		}
		
		return "";
	}
	
	private String computeFirstOutermostLabel(AbstractAST ast) {
		return ast.accept(new NullASTVisitor<String>() {
			@Override
			public String visitFunctionDeclarationDefault(Default x) {
				return extract(x);
			}

			@Override
			public String visitFunctionDeclarationExpression(
					org.rascalmpl.ast.FunctionDeclaration.Expression x) {
				return extract(x);
			}
			
			
			@Override
			public String visitFunctionDeclarationConditional(Conditional x) {
				return extract(x);
			}
			
			private String extract(FunctionDeclaration x) {
				List<Expression> formals = x.getSignature().getParameters().getFormals().getFormals();
				return processFormals(formals);
			}
			
			private String processFormals(List<Expression> formals) {
				if (formals.size() > 0) {
					Expression first = formals.get(0);
					
					if (first.isAsType()) {
						first = first.getArgument();
					}
					else if (first.isTypedVariableBecomes() || first.isVariableBecomes()) {
						first = first.getPattern();
					}
					
					if (first.isCallOrTree() && first.getExpression().isQualifiedName()) {
						return ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) first.getExpression().getQualifiedName()).lastName();
					}
				}

				return null;
			}
		});
	}


	@Override
	public String getFirstOutermostConstructorLabel() {
		return firstOutermostLabel;
	}
	
	@Override
	public IConstructor getFirstOutermostProduction() {
		return firstOutermostProduction;
	}
	
	private List<Expression> cacheFormals() throws ImplementationError {
		Parameters params;
		List<Expression> formals;
		
		if (ast instanceof FunctionDeclaration) {
			params = ((FunctionDeclaration) ast).getSignature().getParameters();
		}
		else if (ast instanceof Closure) {
			params = ((Closure) ast).getParameters();
		}
		else if (ast instanceof VoidClosure) {
			params = ((VoidClosure) ast).getParameters();
		}
		else {
			throw new ImplementationError("Unexpected kind of Rascal function: " + ast);
		}
		
		formals = params.getFormals().getFormals();
		
		if (params.isVarArgs() && formals.size() > 0) {
			// deal with varags, change the last argument to a list if its not a pattern
			Expression last = formals.get(formals.size() - 1);
			if (last.isTypedVariable()) {
				org.rascalmpl.ast.Type oldType = last.getType();
				ISourceLocation origin = last.getLocation();
				Structured newType = ASTBuilder.make("Type","Structured", origin, ASTBuilder.make("StructuredType",origin, ASTBuilder.make("BasicType","List", origin), Arrays.asList(ASTBuilder.make("TypeArg","Default", origin,oldType))));
				last = ASTBuilder.make("Expression","TypedVariable",origin, newType, last.getName());
				formals = replaceLast(formals, last);
			}
			else if (last.isQualifiedName()) {
				ISourceLocation origin = last.getLocation();
				org.rascalmpl.ast.Type newType = ASTBuilder.make("Type","Structured",origin, ASTBuilder.make("StructuredType",origin, ASTBuilder.make("BasicType","List", origin), Arrays.asList(ASTBuilder.make("TypeArg",origin, ASTBuilder.make("Type","Basic", origin, ASTBuilder.make("BasicType","Value", origin))))));
				last = ASTBuilder.makeExp("TypedVariable", origin, newType, Names.lastName(last.getQualifiedName()));
				formals = replaceLast(formals, last);
			}
			else {
				throw new UnsupportedPatternError("...", last);
			}
		}
		
		return formals;
	}
	
	@Override
	public boolean isStatic() {
		return isStatic;
	}
	
	@Override
	public boolean isTest() {
		return isTest;
	}
	
	private static boolean hasTestMod(Signature sig) {
		for (FunctionModifier m : sig.getModifiers().getModifiers()) {
			if (m.isTest()) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isAnonymous() {
		return getName() == null;
	}

	private static boolean isDefault(FunctionDeclaration func) {
		List<FunctionModifier> mods = func.getSignature().getModifiers().getModifiers();
		for (FunctionModifier m : mods) {
			if (m.isDefault()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isDefault() {
		return isDefault;
	}
	
	@Override
	public Result<IValue> call(Type[] actualTypes, IValue[] actuals) {
		Environment old = ctx.getCurrentEnvt();
		AbstractAST oldAST = ctx.getCurrentAST();
		Stack<Accumulator> oldAccus = ctx.getAccumulators();

		try {
			String label = isAnonymous() ? "Anonymous Function" : name;
			Environment environment = new Environment(declarationEnvironment, ctx.getCurrentEnvt(), ctx.getCurrentAST().getLocation(), ast.getLocation(), label);
			ctx.setCurrentEnvt(environment);
			
			IMatchingResult[] matchers = prepareFormals(ctx);
			ctx.setAccumulators(accumulators);
			ctx.pushEnv();

			Type actualTypesTuple = TF.tupleType(actualTypes);
			if (hasVarArgs) {
				actuals = computeVarArgsActuals(actuals, getFormals());
				actualTypesTuple = computeVarArgsActualTypes(actualTypes, getFormals());
			}

			int size = actuals.length;
			Environment[] olds = new Environment[size];
			int i = 0;
			
			
			if (size == 0) {
				try {
					return runBody();
				}
				catch (Return e) {
					return computeReturn(e);
				}
			}
			
			matchers[0].initMatch(makeResult(actualTypesTuple.getFieldType(0), actuals[0], ctx));
			olds[0] = ctx.getCurrentEnvt();
			ctx.pushEnv();

			// pattern matching requires backtracking due to list, set and map matching and
			// non-linear use of variables between formal parameters of a function...
			
			while (i >= 0 && i < size) {
				if (ctx.isInterrupted()) { 
					throw new InterruptException(ctx.getStackTrace(), ctx.getCurrentAST().getLocation());
				}
				if (matchers[i].hasNext() && matchers[i].next()) {
					if (i == size - 1) {
						// formals are now bound by side effect of the pattern matcher
						try {
							return runBody();
						}
						catch (Failure e) {
							// backtrack current pattern assignment
							if (!e.hasLabel() || e.hasLabel() && e.getLabel().equals(getName())) {
								continue;
							}
							else {
								throw new UnguardedFailError(getAst(), e);
							}
//							ctx.unwind(olds[i]);
//							i--;
//							ctx.pushEnv();
						}
					}
					else {
						i++;
						matchers[i].initMatch(makeResult(actualTypesTuple.getFieldType(i), actuals[i], ctx));
						olds[i] = ctx.getCurrentEnvt();
						ctx.pushEnv();
					}
				} else {
					ctx.unwind(olds[i]);
					i--;
					ctx.pushEnv();
				}
			}
			
			// backtrack to other function body
			throw new MatchFailed();
		}
		catch (Return e) {
			return computeReturn(e);
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

	private Result<IValue> runBody() {
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

	private Result<IValue> computeReturn(Return e) {
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
	
	private IMatchingResult[] prepareFormals(IEvaluatorContext ctx) {
		int size = formals.size();
		IMatchingResult[] matchers = new IMatchingResult[size];
		
		for (int i = 0; i < size; i++) {
			matchers[i] = formals.get(i).getMatcher(ctx);
		}
		
		return matchers;
	}

	private List<Expression> replaceLast(List<Expression> formals,
			Expression last) {
		List<Expression> tmp = new ArrayList<Expression>(formals.size());
		tmp.addAll(formals);
		tmp.set(formals.size() - 1, last);
		formals = tmp;
		return formals;
	}
	
	@Override
	public <U extends IValue, V extends IValue> Result<U> equals(Result<V> that) {
		return that.equalToRascalFunction(this);
	}
	
	@Override
	public <U extends IValue> Result<U> equalToRascalFunction(RascalFunction that) {
		return ResultFactory.bool((this == that), ctx);
	}


	private IConstructor computeFirstOutermostProduction(AbstractAST ast) {
		return ast.accept(new NullASTVisitor<IConstructor>() {
			@Override
			public IConstructor visitFunctionDeclarationDefault(Default x) {
				return extract(x);
			}
	
			@Override
			public IConstructor visitFunctionDeclarationExpression(
					org.rascalmpl.ast.FunctionDeclaration.Expression x) {
				return extract(x);
			}
			
			
			@Override
			public IConstructor visitFunctionDeclarationConditional(Conditional x) {
				return extract(x);
			}
			
			private IConstructor extract(FunctionDeclaration x) {
				List<Expression> formals = x.getSignature().getParameters().getFormals().getFormals();
				return processFormals(formals);
			}
			
			private IConstructor processFormals(List<Expression> formals) {
				if (formals.size() > 0) {
					Expression first = formals.get(0);
					
					if (first.isAsType()) {
						first = first.getArgument();
					}
					else if (first.isTypedVariableBecomes() || first.isVariableBecomes()) {
						first = first.getPattern();
					}
					
					if (first instanceof Tree.Appl) {
						Tree.Appl appl = (Appl) first;
						return appl.getProduction();
					}
				}
	
				return null;
			}
		});
	}
	
	@Override
	protected Type computeVarArgsActualTypes(Type[] actualTypes, Type formals) {
		if (formals.getArity() == actualTypes.length 
				&& actualTypes[actualTypes.length - 1].isSubtypeOf(formals.getFieldType(formals.getArity() - 1))) {
			// variable length argument is provided as a list
			return TF.tupleType(actualTypes);
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
	public String getResourceScheme() {
		return this.resourceScheme;
	}
	
	@Override
	public boolean hasResourceScheme() {
		return this.resourceScheme != null;
	}
}
