/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Bas Basten - Bas.Basten@cwi.nl (CWI)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Anastasia Izmaylova - A.Izmaylova@cwi.nl - CWI
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.semantics.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import org.rascalmpl.ast.Field;
import org.rascalmpl.ast.KeywordArgument_Expression;
import org.rascalmpl.ast.KeywordArguments_Expression;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.Label;
import org.rascalmpl.ast.Mapping_Expression;
import org.rascalmpl.ast.Name;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.exceptions.RascalStackOverflowError;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.TypeDeclarationEvaluator;
import org.rascalmpl.interpreter.callbacks.IConstructorDeclared;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.matching.AndResult;
import org.rascalmpl.interpreter.matching.AntiPattern;
import org.rascalmpl.interpreter.matching.BasicBooleanResult;
import org.rascalmpl.interpreter.matching.ConcreteListVariablePattern;
import org.rascalmpl.interpreter.matching.DescendantPattern;
import org.rascalmpl.interpreter.matching.EnumeratorResult;
import org.rascalmpl.interpreter.matching.EquivalenceResult;
import org.rascalmpl.interpreter.matching.GuardedPattern;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.ListPattern;
import org.rascalmpl.interpreter.matching.MatchResult;
import org.rascalmpl.interpreter.matching.MultiVariablePattern;
import org.rascalmpl.interpreter.matching.NegativePattern;
import org.rascalmpl.interpreter.matching.NodePattern;
import org.rascalmpl.interpreter.matching.NotResult;
import org.rascalmpl.interpreter.matching.OrResult;
import org.rascalmpl.interpreter.matching.QualifiedNamePattern;
import org.rascalmpl.interpreter.matching.ReifiedTypePattern;
import org.rascalmpl.interpreter.matching.SetPattern;
import org.rascalmpl.interpreter.matching.TuplePattern;
import org.rascalmpl.interpreter.matching.TypedMultiVariablePattern;
import org.rascalmpl.interpreter.matching.TypedVariablePattern;
import org.rascalmpl.interpreter.matching.VariableBecomesPattern;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.BoolResult;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.result.RascalFunction;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.result.ResultFactory;
import org.rascalmpl.interpreter.staticErrors.NonVoidTypeRequired;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UndeclaredField;
import org.rascalmpl.interpreter.staticErrors.UndeclaredVariable;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnguardedIt;
import org.rascalmpl.interpreter.staticErrors.UninitializedPatternMatch;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariable;
import org.rascalmpl.interpreter.staticErrors.UnsupportedOperation;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPattern;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.gtd.exception.ParseError;
import org.rascalmpl.semantics.dynamic.QualifiedName.Default;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.types.TypeReifier;
import org.rascalmpl.values.IRascalValueFactory;
import org.rascalmpl.values.RascalFunctionValueFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.functions.IFunction;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.SymbolAdapter;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.IListWriter;
import io.usethesource.vallang.IMap;
import io.usethesource.vallang.IMapWriter;
import io.usethesource.vallang.ISet;
import io.usethesource.vallang.ISetWriter;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IString;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;
import io.usethesource.vallang.type.TypeFactory;

public abstract class Expression extends org.rascalmpl.ast.Expression {
 // TODO inline this when we find out it works
    private final static boolean instantiateTypeParameters = true;

  private static final Name IT = ASTBuilder.makeLex("Name", null, "<it>");
	
	static public class Addition extends org.rascalmpl.ast.Expression.Addition {

		public Addition(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

	
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.add(right);

		}

	}

	static public class All extends org.rascalmpl.ast.Expression.All {

		public All(ISourceLocation __param1, IConstructor tree,
				java.util.List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			return new BasicBooleanResult(__eval, this);

		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Result interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);	
			
			java.util.List<org.rascalmpl.ast.Expression> producers = this
					.getGenerators();
			int size = producers.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();
			int i = 0;

			try {
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();
				gens[0] = producers.get(0).getBacktracker(__eval);
				gens[0].init();

				while (i >= 0 && i < size) {
					if (__eval.isInterrupted()) {
						throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
					}
					if (gens[i].hasNext()) {
						if (!gens[i].next()) {
							return new BoolResult(TF.boolType(), __eval
									.__getVf().bool(false), __eval);
						}

						if (i == size - 1) {
							__eval.unwind(olds[i]);
							__eval.pushEnv();
						} else {
							i++;
							gens[i] = producers.get(i).getBacktracker(__eval);
							gens[i].init();
							olds[i] = __eval.getCurrentEnvt();
							__eval.pushEnv();
						}
					} else {
						__eval.unwind(olds[i]);
						i--;
					}
				}
			} finally {
				__eval.unwind(old);
			}

			return new BoolResult(TF.boolType(), __eval.__getVf().bool(true),
					__eval);

		}

	}

	static public class And extends org.rascalmpl.ast.Expression.And {

		public And(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new AndResult(__eval, this.getLhs().buildBacktracker(__eval), this.getRhs().buildBacktracker(__eval));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return evalBooleanExpression(this, __eval);
		}

	}

	static public class Anti extends org.rascalmpl.ast.Expression.Anti {

		public Anti(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext __eval, boolean bindTypeParameters) {
			IMatchingResult absPat = getPattern().buildMatcher(__eval, bindTypeParameters);
			return new AntiPattern(__eval, this, absPat);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TypeFactory.getInstance().voidType();
		}
	}

	static public class Any extends org.rascalmpl.ast.Expression.Any {

		public Any(ISourceLocation __param1, IConstructor tree,
				java.util.List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Result interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			java.util.List<org.rascalmpl.ast.Expression> generators = this
					.getGenerators();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];

			int i = 0;
			gens[0] = generators.get(0).getBacktracker(__eval);
			gens[0].init();
			while (i >= 0 && i < size) {
				if (__eval.isInterrupted()) {
					throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
				}
				if (gens[i].hasNext() && gens[i].next()) {
					if (i == size - 1) {
						return new BoolResult(TF.boolType(), __eval.__getVf()
								.bool(true), __eval);
					}

					i++;
					gens[i] = generators.get(i).getBacktracker(__eval);
					gens[i].init();
				} else {
					i--;
				}
			}
			return new BoolResult(TF.boolType(), __eval.__getVf().bool(false),
					__eval);

		}

	}

	static public class Bracket extends org.rascalmpl.ast.Expression.Bracket {

		public Bracket(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return this.getExpression().buildBacktracker(__eval);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext __eval, boolean bindTypeParameters) {
			return this.getExpression().buildMatcher(__eval, bindTypeParameters);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return this.getExpression().interpret(__eval);
		}
		
		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			return getExpression().isDefined(__eval);
		}
	}

	static public class CallOrTree extends
			org.rascalmpl.ast.Expression.CallOrTree {

		private Result<IValue> cachedPrefix = null;
		private boolean registeredCacheHandler = false;
		private Type cachedConstructorType = null;
		private boolean registeredTypeCacheHandler = false;

		public CallOrTree(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				java.util.List<org.rascalmpl.ast.Expression> __param3, KeywordArguments_Expression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			org.rascalmpl.ast.Expression nameExpr = getExpression();
		
			if (nameExpr.isQualifiedName()) {
				if (cachedConstructorType == null) {
					registerTypeCacheHandler(eval);
					cachedConstructorType  = computeConstructorType(eval, nameExpr);
				}
				 
				return new NodePattern(eval, this, null, nameExpr.getQualifiedName(), cachedConstructorType, visitArguments(eval, bindTypeParameters), visitKeywordArguments(eval, bindTypeParameters));
			}

			return new NodePattern(eval, this, nameExpr.buildMatcher(eval, bindTypeParameters), null, TF.nodeType(), visitArguments(eval, bindTypeParameters), visitKeywordArguments(eval, bindTypeParameters));
		}

		private java.util.Map<String, IMatchingResult> visitKeywordArguments(IEvaluatorContext eval, boolean bindTypeParameters) {
			java.util.Map<String,IMatchingResult> result = new HashMap<>();
			KeywordArguments_Expression keywordArgs;

			if (hasKeywordArguments() && (keywordArgs = getKeywordArguments()).isDefault()) {
				for (KeywordArgument_Expression kwa : keywordArgs.getKeywordArgumentList()) {
					result.put(Names.name(kwa.getName()), kwa.getExpression().buildMatcher(eval, bindTypeParameters));
				}
			}

			return result;
		}

    private Type computeConstructorType(IEvaluatorContext eval, org.rascalmpl.ast.Expression nameExpr) {
			java.util.List<AbstractFunction> functions = new LinkedList<AbstractFunction>();
			
			String cons = Names.consName(nameExpr.getQualifiedName());
			Type adt = eval.getCurrentEnvt().lookupAbstractDataType(Names.moduleName(nameExpr.getQualifiedName()));
			
			if (adt != null) {
				eval.getCurrentEnvt().getAllFunctions(adt, cons, functions);
			}
			else {
			  eval.getCurrentEnvt().getAllFunctions(cons, functions);
			}
			
			if (functions.isEmpty()) {
			  return null;
//				throw new UndeclaredVariable(Names.fullName(nameExpr.getQualifiedName()), this);
			}
			
			Type signature = getArgumentTypes(eval, false);
			Type constructorType = adt != null ? adt : TF.nodeType();
			
			for (AbstractFunction candidate : functions) {
				if (candidate.getReturnType().isAbstractData() && !candidate.getReturnType().isBottom() && candidate.mayMatch(signature)) {
					Type decl = eval.getCurrentEnvt().getConstructor(candidate.getReturnType(), cons, signature);
					if (decl != null) {
						constructorType = decl;
					}
				}
			}
			
			return constructorType;
		}

		private Type getArgumentTypes(IEvaluatorContext eval, boolean bindTypeParameters) {
			java.util.List<IMatchingResult> args = visitArguments(eval, bindTypeParameters);
			Type[] argTypes = new Type[args.size()];
			for (int i = 0; i < argTypes.length; i++) {
				argTypes[i] = args.get(i).getType(eval.getCurrentEnvt(), null);
			}
			Type signature = TF.tupleType(argTypes);
			return signature;
		}

		private void registerCacheHandler(IEvaluatorContext eval) {
			if (!registeredCacheHandler) {
				eval.getEvaluator().registerConstructorDeclaredListener(
						new IConstructorDeclared() {
							public void handleConstructorDeclaredEvent() {
								cachedPrefix = null;
								registeredCacheHandler = false;
							}
						});
				registeredCacheHandler = true;
			}
		}
		
		private void registerTypeCacheHandler(IEvaluatorContext eval) {
			if (!registeredTypeCacheHandler) {
				eval.getEvaluator().registerConstructorDeclaredListener(
						new IConstructorDeclared() {
							public void handleConstructorDeclaredEvent() {
								cachedConstructorType = null;
								registeredTypeCacheHandler = false;
							}
						});
				registeredTypeCacheHandler = true;
			}
		}
		
		private java.util.List<IMatchingResult> visitArguments(IEvaluatorContext eval, boolean bindTypeParameters) {
			return buildMatchers(getArguments(), eval, bindTypeParameters);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			eval.setCurrentAST(this);
			eval.notifyAboutSuspension(this);			

			try {
				if (eval.isInterrupted()) {
					throw new InterruptException(eval.getStackTrace(), eval.getCurrentAST().getLocation());
				}

				eval.setCurrentAST(this);

				Result<IValue> function = this.cachedPrefix;

				// If the name expression is just a name, enable caching of the name lookup result.
				// Also, if we have not yet registered a handler when we cache the result, do so now.
				if (function == null) {
					function = this.getExpression().interpret(eval);
					
					if (this.getExpression().isQualifiedName() && function instanceof ICallableValue && ((ICallableValue) function).isStatic()) {
						org.rascalmpl.ast.QualifiedName qname = this.getExpression().getQualifiedName();
						
						if (eval.getCurrentEnvt().isNameFinal(qname)) {
							this.cachedPrefix = function;
							registerCacheHandler(eval);
						}
					}
					else {
						cachedPrefix = null;
					}
				}

				java.util.List<org.rascalmpl.ast.Expression> args = this.getArguments();

				IValue[] actuals = new IValue[args.size()];
				Type[] types = new Type[args.size()];
				for (int i = 0; i < args.size(); i++) {
					Result<IValue> resultElem = args.get(i).interpret(eval);
					types[i] = resultElem.getStaticType();
					if (types[i].isBottom()) {
						throw new UninitializedPatternMatch("The argument is of the type 'void'", args.get(i));
					}
					actuals[i] = resultElem.getValue();
				}
				
			  java.util.Map<String,IValue> kwActuals = Collections.<String,IValue>emptyMap();
			  
				if (hasKeywordArguments()) {
				  KeywordArguments_Expression keywordArgs = this.getKeywordArguments();
				  Type kwFormals = function.getKeywordArgumentTypes(eval.getCurrentEnvt());
				
				  if (keywordArgs.isDefault()){
				    kwActuals = new HashMap<String,IValue>();

				    for (KeywordArgument_Expression kwa : keywordArgs.getKeywordArgumentList()){
				      Result<IValue> val = kwa.getExpression().interpret(eval);
				      String name = Names.name(kwa.getName());

				      if (kwFormals != null) {
				        if (kwFormals.hasField(name)) {
				          if (!val.getStaticType().isSubtypeOf(kwFormals.getFieldType(name))) {
				            throw new UnexpectedType(kwFormals.getFieldType(name), val.getStaticType(), this);
				          }
				        }
				        else {
				          eval.getMonitor().warning("calling function with extra unknown keyword argument: " +  name, getLocation());
				        }
				      }

				      kwActuals.put(name, val.getValue());
				    }
				  }
				}
				Result<IValue> res = null;
				try {
					res = function.call(types, actuals, kwActuals);
				}
				catch (Failure | MatchFailed e) {
				    throw RuntimeExceptionFactory.callFailed(eval.getValueFactory().list(actuals), eval.getCurrentAST(), eval.getStackTrace());
				}
				catch (StackOverflowError e) {
					// this should not use so much stack as to trigger another StackOverflowError
					throw new RascalStackOverflowError(this, eval.getCurrentEnvt());
				}
				return res;
			}
			finally {}
			
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			Type lambda = getExpression().typeOf(env, eval, instantiateTypeParameters);

			if (lambda.isString()) {
				return TF.nodeType();
			}
			
			if (lambda.isSourceLocation()) {
				return lambda;
			}
			
			if (lambda.isFunction()) {
				return lambda.getReturnType();
			}

			return TF.nodeType();
		}
	}

	static public class Closure extends org.rascalmpl.ast.Expression.Closure {

		public Closure(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2,
				Parameters __param3, java.util.List<Statement> __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Environment env = __eval.getCurrentEnvt();
			Parameters parameters = getParameters();
			Type formals = parameters.typeOf(env, __eval, instantiateTypeParameters);
			Type returnType = typeOf(env, __eval, instantiateTypeParameters);
			Type kwParams = TF.tupleEmpty();

			java.util.List<KeywordFormal> kwd = parameters.getKeywordFormals().hasKeywordFormalList() ? parameters.getKeywordFormals().getKeywordFormalList() : Collections.<KeywordFormal>emptyList();
			
			if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
				kwParams = TypeDeclarationEvaluator.computeKeywordParametersType(kwd, __eval);
			}

			return new RascalFunction(this, __eval, null,
					TF.functionType(returnType, formals, kwParams).instantiate(__eval.getCurrentEnvt().getStaticTypeBindings()),
					TF.functionType(returnType, formals, kwParams).instantiate(__eval.getCurrentEnvt().getDynamicTypeBindings()),
					kwd,
					this.getParameters()
					.isVarArgs(), false, false, this.getStatements(), env, __eval.__getAccumulators());
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getType().typeOf(env, eval, instantiateTypeParameters);
		}

	}

	static public class Composition extends
			org.rascalmpl.ast.Expression.Composition {

		public Composition(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);						
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.compose(right);

		}

	}

	static public class Comprehension extends
			org.rascalmpl.ast.Expression.Comprehension {

		public Comprehension(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Comprehension __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

	

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
						
			return this.getComprehension().interpret(__eval);

		}

	}

	static public class Concrete extends org.rascalmpl.ast.Expression.Concrete {
  
    public Concrete(ISourceLocation src, IConstructor node, org.rascalmpl.ast.Concrete concrete) {
      super(src, node, concrete);
    }
    
    @Override
    public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
       return RascalValueFactory.Tree;
    }

    @Override
    public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
      throw new SyntaxError("concrete syntax fragment", getLocation());
    }
    
    @Override
    public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
      throw new SyntaxError("concrete syntax fragment", getLocation());
    }
  }
	
	static public class Descendant extends
			org.rascalmpl.ast.Expression.Descendant {

		public Descendant(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			IMatchingResult absPat = this.getPattern().buildMatcher(eval, bindTypeParameters);
			return new DescendantPattern(eval, this, absPat);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TypeFactory.getInstance().valueType();
		}

	}

	static public class Division extends org.rascalmpl.ast.Expression.Division {

		public Division(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2, org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			throw new UnexpectedType(TF.boolType(), interpret(eval.getEvaluator()).getStaticType(), this);
		}

	

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);		
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.divide(right);

		}

	}

	static public class Enumerator extends
			org.rascalmpl.ast.Expression.Enumerator {

		public Enumerator(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new EnumeratorResult(eval, getPattern().buildMatcher(eval.getEvaluator(), false), getExpression());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Environment old = __eval.getCurrentEnvt();
			try {
				__eval.pushEnv();
				IBooleanResult gen = this.getBacktracker(__eval);
				gen.init();
				if (gen.hasNext() && gen.next()) {
					return org.rascalmpl.interpreter.result.ResultFactory.makeResult(TF.boolType(),
							VF.bool(true), __eval);
				}
				return org.rascalmpl.interpreter.result.ResultFactory.makeResult(TF.boolType(),
						VF.bool(false), __eval);
			} finally {
				__eval.unwind(old);
			}

		}

	}

	static public class Equals extends org.rascalmpl.ast.Expression.Equals {

		public Equals(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

	
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = left.equals(right);
			return makeResult(TF.boolType(), result.getValue(), __eval);
		}

	}

	static public class Equivalence extends
			org.rascalmpl.ast.Expression.Equivalence {

		public Equivalence(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new EquivalenceResult(eval, getLhs().buildBacktracker(eval), getRhs().buildBacktracker(eval));
		}


		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return evalBooleanExpression(this, __eval);
		}

	}

	static public class FieldAccess extends
			org.rascalmpl.ast.Expression.FieldAccess {

		public FieldAccess(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2, Name __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> expr = this.getExpression().interpret(__eval);
			String field = org.rascalmpl.interpreter.utils.Names.name(this.getField());

			return expr.fieldAccess(field, __eval.getCurrentEnvt().getStore());
		}
		
		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> expr = this.getExpression().interpret(__eval);
			return expr.isDefined(this.getField());
		}
	}

	static public class FieldProject extends
			org.rascalmpl.ast.Expression.FieldProject {

		public FieldProject(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				java.util.List<Field> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

	

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> base = this.getExpression().interpret(__eval);
			java.util.List<Field> fields = this.getFields();
			return base.fieldSelect(fields.toArray(new Field[0]));
		}
		
	}

	static public class FieldUpdate extends
			org.rascalmpl.ast.Expression.FieldUpdate {

		public FieldUpdate(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2, Name __param3,
				org.rascalmpl.ast.Expression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> expr = this.getExpression().interpret(__eval);
			Result<IValue> repl = this.getReplacement().interpret(__eval);
			String name = org.rascalmpl.interpreter.utils.Names.name(this
					.getKey());
			return expr.fieldUpdate(name, repl, __eval.getCurrentEnvt()
					.getStore());

		}

	}

	static public class GetAnnotation extends
			org.rascalmpl.ast.Expression.GetAnnotation {

		public GetAnnotation(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2, Name __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> base = this.getExpression().interpret(__eval);
			String annoName = org.rascalmpl.interpreter.utils.Names.name(this
					.getName());
			return base.getAnnotation(annoName, __eval.getCurrentEnvt());

		}

		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			Result<?> lhs = getExpression().interpret(__eval);
			Name annoName = getName();

			if (lhs.getValue().getType().isSubtypeOf(RascalValueFactory.Tree) && "loc".equals(Names.name(annoName))) {
				annoName = Names.toName("src", getName().getLocation());
			}

			return lhs.isDefined(annoName);
		}
	}

	static public class GreaterThan extends
			org.rascalmpl.ast.Expression.GreaterThan {

		public GreaterThan(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = left.greaterThan(right);
			return makeResult(TF.boolType(), result.getValue(), __eval);
		}

	}

	static public class GreaterThanOrEq extends
			org.rascalmpl.ast.Expression.GreaterThanOrEq {

		public GreaterThanOrEq(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = left.greaterThanOrEqual(right);
			return makeResult(TF.boolType(), result.getValue(), __eval);

		}

	}

	static public class AsType extends org.rascalmpl.ast.Expression.AsType {

		public AsType(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			Type type = getType().typeOf(eval.getCurrentEnvt(), eval.getEvaluator(), instantiateTypeParameters);
			IMatchingResult absPat = this.getArgument().buildMatcher(eval, bindTypeParameters);
			return new GuardedPattern(eval, this, type, absPat);
		}

		private boolean isBootstrapped(IEvaluatorContext eval) {
			Environment root = eval.getCurrentEnvt().getRoot();
			if (root instanceof ModuleEnvironment) {
				return ((ModuleEnvironment) root).getBootstrap();
			}
			return false;
		}

    	private ITree parseObject(IEvaluatorContext eval, IConstructor grammar, ISet filters, ISourceLocation location, char[] input,  boolean allowAmbiguity, boolean hasSideEffects) {
        	RascalFunctionValueFactory vf = eval.getFunctionValueFactory();
			IString str = vf.string(new String(input));
		
			if (isBootstrapped(eval)) {
				return (ITree) vf.bootstrapParsers().call(grammar, str, location);
			}
			else {
        		IFunction parser = vf.parser(grammar, vf.bool(allowAmbiguity), vf.bool(hasSideEffects), vf.bool(false), filters);
				return (ITree) parser.call(vf.string(new String(input)), location);
			}
    	}

		private ITree parseObject(IEvaluatorContext eval, IConstructor grammar, ISet filters, ISourceLocation location, boolean allowAmbiguity, boolean hasSideEffects) {
        	RascalFunctionValueFactory vf = eval.getFunctionValueFactory();
			
			if (isBootstrapped(eval)) {
				return (ITree) vf.bootstrapParsers().call(grammar, location, location);
			}
			else {
				IFunction parser = vf.parser(grammar, vf.bool(allowAmbiguity), vf.bool(hasSideEffects), vf.bool(false), filters);
        		return (ITree) parser.call(location, location);
			}
    	}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> result = this.getArgument().interpret(__eval);
			Type expected = getType().typeOf(__eval.getCurrentEnvt(), __eval, instantiateTypeParameters);

			if (!(expected instanceof NonTerminalType)) {
				throw new UnsupportedOperation("inline parsing", expected, this);
			}
			
			if (!result.getStaticType().isSubtypeOf(TF.stringType()) && !result.getStaticType().isSubtypeOf(TF.sourceLocationType())) {
				throw new UnsupportedOperation("inline parsing", result.getStaticType(), this);
			}
			
			IConstructor symbol = ((NonTerminalType) expected).getSymbol();
			if (!SymbolAdapter.isSort(symbol) && !SymbolAdapter.isLex(symbol) && !SymbolAdapter.isLayouts(symbol) && !SymbolAdapter.isStartSort(symbol)) {
				throw new UnsupportedOperation("inline parsing", expected, this);
			}

			__eval.__setInterrupt(false);
			try {
				IConstructor tree = null;
				
				IMap gr = isBootstrapped(__eval) ? __eval.getValueFactory().map() : (IMap) __eval.getEvaluator().getGrammar(__eval.getCurrentEnvt()).get("rules");
				IConstructor value = ((IRascalValueFactory) __eval.getValueFactory()).reifiedType(symbol, gr);
            
				if (result.getStaticType().isString()) {
					tree = parseObject(__eval, value, VF.set(), this.getLocation(),
						((IString) result.getValue()).getValue().toCharArray(), true, false);
				}
				else if (result.getStaticType().isSourceLocation()) {
					tree = parseObject(__eval, value, VF.set(), (ISourceLocation) result.getValue(), true, false);
				}
				
				assert tree != null; // because we checked earlier

				return org.rascalmpl.interpreter.result.ResultFactory
						.makeResult(expected, tree, __eval);
			}
			catch (ParseError e) {
				throw RuntimeExceptionFactory.parseError(getLocation(), this, __eval.getStackTrace());
			}
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getType().typeOf(env, eval, instantiateTypeParameters);
		}

	}

	static public class Has extends org.rascalmpl.ast.Expression.Has {

		public Has(ISourceLocation src, IConstructor node, org.rascalmpl.ast.Expression expression,
				Name name) {
			super(src, node, expression, name);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IBool> result = getExpression().interpret(__eval).has(getName());
			return makeResult(TF.boolType(), result.getValue(), __eval);
		}
	}

	static public class IfDefinedOtherwise extends
			org.rascalmpl.ast.Expression.IfDefinedOtherwise {

		public IfDefinedOtherwise(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);	
			
			if (getLhs().isDefined(__eval).getValue().getValue()) {
			    try {
			        return getLhs().interpret(__eval);
			    }
			    catch (UndeclaredField | Throw e) {
			        // TODO: this happens when annotations are simulated by kw fields, since there
			        // is not default value associated in this case
			        return getRhs().interpret(__eval);
			    }
			}
			else {
				return getRhs().interpret(__eval);
			}
		}

	}

	static public class IfThenElse extends
			org.rascalmpl.ast.Expression.IfThenElse {

		public IfThenElse(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3,
				org.rascalmpl.ast.Expression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Environment old = __eval.getCurrentEnvt();
			__eval.pushEnv();

			try {
				Result<IValue> cval = this.getCondition().interpret(__eval);

				if (!cval.getStaticType().isBool()) {
					throw new UnexpectedType(TF.boolType(),
							cval.getStaticType(), this);
				}

				if (cval.isTrue()) {
					return this.getThenExp().interpret(__eval);
				}

				return this.getElseExp().interpret(__eval);
			} finally {
				__eval.unwind(old);
			}

		}

	}

	static public class Implication extends
			org.rascalmpl.ast.Expression.Implication {

		public Implication(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new OrResult(eval, new NotResult(eval, getLhs().buildBacktracker(eval)), getRhs().buildBacktracker(eval));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return evalBooleanExpression(this, __eval);
		}

	}

	static public class In extends org.rascalmpl.ast.Expression.In {

		public In(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			return new BasicBooleanResult(__eval, this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = right.in(left);
			return makeResult(TF.boolType(), result.getValue(), __eval);
		}

	}

	static public class Intersection extends
			org.rascalmpl.ast.Expression.Intersection {

		public Intersection(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.intersect(right);

		}

	}

	static public class Is extends org.rascalmpl.ast.Expression.Is {

		public Is(ISourceLocation src, IConstructor node, org.rascalmpl.ast.Expression expression, Name name) {
			super(src, node, expression, name);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IBool> result = getExpression().interpret(__eval).is(getName());
			return makeResult(TF.boolType(), result.getValue(), __eval);
		}
	}

	static public class IsDefined extends
			org.rascalmpl.ast.Expression.IsDefined {

		public IsDefined(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return makeResult(TF.boolType(), getArgument().isDefined(__eval).getValue(), __eval);
		}

	}

	static public class It extends org.rascalmpl.ast.Expression.It {

		public It(ISourceLocation __param1, IConstructor tree) {
			super(__param1, tree);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> v = __eval.getCurrentEnvt().getVariable(IT);
			if (v == null) {
				throw new UnguardedIt(this);
			}
			return v;

		}
		
		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

	}

	static public class Join extends org.rascalmpl.ast.Expression.Join {

		public Join(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.join(right);

		}

	}

	static public class LessThan extends org.rascalmpl.ast.Expression.LessThan {

		public LessThan(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = left.lessThan(right);
			return makeResult(TF.boolType(), result.getValue(), __eval);
		}
	}

	static public class LessThanOrEq extends
			org.rascalmpl.ast.Expression.LessThanOrEq {

		public LessThanOrEq(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = left.lessThanOrEqual(right);
			return ResultFactory.makeResult(result.getStaticType(), result.getValue(),__eval);
		}

	}

	static public class List extends org.rascalmpl.ast.Expression.List {

		public List(ISourceLocation __param1, IConstructor tree,
				java.util.List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new ListPattern(eval, this, buildMatchers(getElements0(), eval, bindTypeParameters), bindTypeParameters);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			java.util.List<org.rascalmpl.ast.Expression> elements = getElements0();

			Type elementType = TF.voidType();
			java.util.List<IValue> results = new ArrayList<IValue>();

			for (org.rascalmpl.ast.Expression expr : elements) {
				boolean isSplicedElem = expr.isSplice() || expr.isSplicePlus();
				
				Result<IValue> resultElem = null;

				if (!isSplicedElem) {
					resultElem = expr.interpret(__eval);

					if (resultElem.getStaticType().isBottom()) {
						throw new NonVoidTypeRequired(expr);
					}

				}
			
				if (isSplicedElem){
				  resultElem = expr.getArgument().interpret(__eval);
				  if (resultElem.getStaticType().isBottom()) {
				    throw new NonVoidTypeRequired(expr);
				  }

				  if(resultElem.getStaticType().isList()|| resultElem.getStaticType().isSet()){
				    /*
				     * Splice elements in list
				     */
					elementType = elementType.lub(resultElem.getStaticType().getElementType());
				    for (IValue val : (Iterable<IValue>) resultElem.getValue()) {
				      results.add(val);
				    }
				    continue;
				  } 
				}

				elementType = elementType.lub(resultElem.getStaticType());
				results.add(results.size(), resultElem.getValue());
			}

			Type resultType = TF.listType(elementType);
			IListWriter w = __eval.__getVf().listWriter();
			w.appendAll(results);
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(resultType, w.done(), __eval);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			Type elementType = TF.voidType();

			for (org.rascalmpl.ast.Expression elt : getElements0()) {
				elementType = elementType.lub(elt.typeOf(env, eval, instantiateTypeParameters));
			}

			return TF.listType(elementType);
		}

	}

	static public class Literal extends org.rascalmpl.ast.Expression.Literal {

		public Literal(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Literal __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			if (this.getLiteral().isBoolean()) {
				return new BasicBooleanResult(eval, this);
			}
			throw new UnexpectedType(TF.boolType(), interpret(eval.getEvaluator()).getStaticType(), this);
		}

		
		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext __eval, boolean bindTypeParameters) {
			return this.getLiteral().buildMatcher(__eval, bindTypeParameters);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return this.getLiteral().interpret(__eval);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getLiteral().typeOf(env, eval, instantiateTypeParameters);
		}

	}

	static public class Map extends org.rascalmpl.ast.Expression.Map {

		public Map(ISourceLocation __param1, IConstructor tree, java.util.List<Mapping_Expression> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext __eval, boolean bindTypeParameters) {
			throw new ImplementationError("Map in pattern not yet implemented");
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			java.util.List<Mapping_Expression> mappings = this.getMappings();
			java.util.Map<IValue, IValue> seen = new HashMap<>();
			Type keyType = TF.voidType();
			Type valueType = TF.voidType();
			IMapWriter w = __eval.__getVf().mapWriter();

			for (Mapping_Expression mapping : mappings) {
				Result<IValue> keyResult = mapping.getFrom().interpret(__eval);
				Result<IValue> valueResult = mapping.getTo().interpret(__eval);
				
				if (keyResult.getStaticType().isBottom()) {
					throw new NonVoidTypeRequired(mapping.getFrom());
				}

				if (valueResult.getStaticType().isBottom()) {
					throw new NonVoidTypeRequired(mapping.getTo());
				}
				
				IValue key = keyResult.getValue();

				keyType = keyType.lub(keyResult.getStaticType());
				valueType = valueType.lub(valueResult.getStaticType());

				IValue keyValue = seen.get(key);
				
				if (keyValue != null) {
					throw org.rascalmpl.exceptions.RuntimeExceptionFactory
							.MultipleKey(keyResult.getValue(), keyValue, valueResult.getValue(), mapping.getFrom(), __eval
									.getStackTrace());
				}
				
				seen.put(key, valueResult.getValue());
				w.put(key, valueResult.getValue());
			}

			Type type = TF.mapType(keyType, valueType);
			
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(type, w.done(), __eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			Type keyType = TF.voidType();
			Type valueType = TF.valueType();
			
			for (Mapping_Expression me : getMappings()) {
				keyType = keyType.lub(me.getFrom().typeOf(env, eval, instantiateTypeParameters));
				valueType = valueType.lub(me.getTo().typeOf(env, eval, instantiateTypeParameters));
			}
			
			return TF.mapType(keyType, valueType);
		}

	}

	static public class Match extends org.rascalmpl.ast.Expression.Match {

		public Match(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new MatchResult(eval, getPattern(), true, getExpression());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return evalBooleanExpression(this, __eval);
		}

	}

	static public class Modulo extends org.rascalmpl.ast.Expression.Modulo {

		public Modulo(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			
			return left.modulo(right);

		}

	}
	
	static public class Remainder extends org.rascalmpl.ast.Expression.Remainder {

		public Remainder(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.remainder(right);

		}

	}

	static public class MultiVariable extends
			org.rascalmpl.ast.Expression.MultiVariable {

		public MultiVariable(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.QualifiedName __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new MultiVariablePattern(eval, this, getQualifiedName());
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);		
			__eval.warning("Var* is deprecated, use *Var or *Type Var instead", this.getLocation());
			System.err.println(this.getLocation() + ": Var* is deprecated, use *Var instead");
			
			Name name = this.getName();
			Result<IValue> variable = __eval.getCurrentEnvt().getVariable(name);

			if (variable == null) {
				throw new UndeclaredVariable(
						org.rascalmpl.interpreter.utils.Names.name(name), name);
			}

			if (variable.getValue() == null) {
				throw new UninitializedVariable(
						org.rascalmpl.interpreter.utils.Names.name(name), name);
			}

			return variable;
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			// we return the element type here, such that lub at a higher level
			// does the right thing!
			return getQualifiedName().typeOf(env, eval, instantiateTypeParameters);
		}

	}
	
	static public class Splice extends
	org.rascalmpl.ast.Expression.Splice {

		public Splice(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			org.rascalmpl.ast.Expression arg = this.getArgument();
			if (arg.hasType() && arg.hasName()) {
				Environment env = eval.getCurrentEnvt();
				Type type = arg.getType().typeOf(env, eval.getEvaluator(), instantiateTypeParameters);
				type = type.instantiate(env.getStaticTypeBindings());
				
				// TODO: Question, should we allow non terminal types in splices?
				if (type instanceof NonTerminalType) {
					throw new UnsupportedOperation("splicing match", type, this);
//					throw new ImplementationError(null);
				}				
				return new TypedMultiVariablePattern(eval, this, type, arg.getName(), bindTypeParameters);
			}

			if(arg.hasQualifiedName()){
				return new MultiVariablePattern(eval, this, arg.getQualifiedName());
			}

			throw new UnsupportedOperation("splice operator outside of list or set", this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Name name = this.getName();
			Result<IValue> variable = __eval.getCurrentEnvt().getVariable(name);

			if (variable == null) {
				throw new UndeclaredVariable(
						org.rascalmpl.interpreter.utils.Names.name(name), name);
			}

			if (variable.getValue() == null) {
				throw new UninitializedVariable(
						org.rascalmpl.interpreter.utils.Names.name(name), name);
			}

			return variable;
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			// we return the element type here, such that lub at a higher level
			// does the right thing!
			org.rascalmpl.ast.Expression arg = this.getArgument();
			if (arg.hasType() && arg.hasName()) {
				return arg.getType().typeOf(env, eval, instantiateTypeParameters);
			}
			if(arg.hasQualifiedName()){
				return arg.getQualifiedName().typeOf(env, eval, instantiateTypeParameters);
			}

			return arg.typeOf(env, eval, instantiateTypeParameters);
		}

	}

	static public class Negation extends org.rascalmpl.ast.Expression.Negation {

		public Negation(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new NotResult(eval, getArgument().buildBacktracker(eval));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return evalBooleanExpression(this, __eval);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return TF.boolType();
		}

	}

	static public class Negative extends org.rascalmpl.ast.Expression.Negative {

		public Negative(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);
		}
		
		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext __eval, boolean bindTypeParameters) {
			return new NegativePattern(__eval, this, getArgument().buildMatcher(__eval, bindTypeParameters));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> arg = this.getArgument().interpret(__eval);
			return arg.negative();
		}
	}

	static public class NoMatch extends org.rascalmpl.ast.Expression.NoMatch {

		public NoMatch(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new MatchResult(eval, getPattern(), false, getExpression());
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return evalBooleanExpression(this, __eval);
		}

	}

	static public class NonEmptyBlock extends
			org.rascalmpl.ast.Expression.NonEmptyBlock {
		public NonEmptyBlock(ISourceLocation __param1, IConstructor tree, java.util.List<Statement> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return ASTBuilder.make("Statement", "NonEmptyBlock", this.getLocation(),
					ASTBuilder.make("Label", "Empty", this.getLocation()),
					this.getStatements()).interpret(__eval);
		}
	}

	static public class NonEquals extends
			org.rascalmpl.ast.Expression.NonEquals {

		public NonEquals(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = left.nonEquals(right);
			return makeResult(result.getStaticType(), result.getValue(), __eval);
		}

	}

	static public class NotIn extends org.rascalmpl.ast.Expression.NotIn {

		public NotIn(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			Result<IBool> result = right.notIn(left);
			return makeResult(result.getStaticType(), result.getValue(), __eval);

		}

	}

	static public class Or extends org.rascalmpl.ast.Expression.Or {

		public Or(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new OrResult(eval, this.getLhs().buildBacktracker(eval), this.getRhs().buildBacktracker(eval));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			return evalBooleanExpression(this, __eval);
		}
	}

	static public class Product extends org.rascalmpl.ast.Expression.Product {

		public Product(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.multiply(right);
		}

	}

	static public class QualifiedName extends
			org.rascalmpl.ast.Expression.QualifiedName {

		public QualifiedName(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.QualifiedName __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			org.rascalmpl.ast.QualifiedName name = this.getQualifiedName();

			Result<IValue> r = eval.getEvaluator().getCurrentEnvt().getSimpleVariable(name);

			if (r != null) {
				if (r.getValue() != null) {
					// Previously declared and initialized variable
					return new QualifiedNamePattern(eval, this, name);
				}

				Type type = r.getStaticType();
				if (type instanceof NonTerminalType) {
					NonTerminalType cType = (NonTerminalType) type;
					if (cType.isConcreteListType()) {
						return new ConcreteListVariablePattern(eval, this, type, ((Default) name).lastName());
					}
				}

				return new QualifiedNamePattern(eval, this, name);
			}

			// TODO: I don't understand which feature this code implements
//			if (eval.getCurrentEnvt().isTreeConstructorName(name, signature)) {
//				return new NodePattern(eval, this, null, name, new ArrayList<IMatchingResult>());
//			}

			// Completely fresh variable
			return new QualifiedNamePattern(eval, this, name);
			// return new AbstractPatternTypedVariable(vf, env,
			// ev.tf.valueType(), name);

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			org.rascalmpl.ast.QualifiedName name = this.getQualifiedName();
			Result<IValue> variable = __eval.getCurrentEnvt().getVariable(name);

			if (variable == null) {
				throw new UndeclaredVariable(
						org.rascalmpl.interpreter.utils.Names.fullName(name),
						name);
			}

			if (variable.getValue() == null) {
				throw new UninitializedVariable(
						org.rascalmpl.interpreter.utils.Names.fullName(name),
						name);
			}

			return variable;

		}
		
		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			org.rascalmpl.ast.QualifiedName name = this.getQualifiedName();
			String fullName = Names.fullName(name);
			Result<IValue> variable = __eval.getCurrentEnvt().getSimpleVariable(AbstractFunction.makeIsSetKeywordParameterName(fullName));
			
			if (variable == null || variable.getValue() == null) {
				variable = __eval.getCurrentEnvt().getVariable(name);
				__eval.warning("deprecated feature: run-time check on variable initialization", getLocation());
				return org.rascalmpl.interpreter.result.ResultFactory.bool(variable != null && variable.getValue() != null, __eval);
				
				// TODO: replace above by this
				// it was not a keyword parameter
                // throw new UndeclaredKeywordParameter(__eval.getCurrentEnvt().getName(), fullName, this);
			}
			else {
				return ResultFactory.bool(((IBool) variable.getValue()).getValue(), __eval);
			}
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getQualifiedName().typeOf(env, eval, instantiateTypeParameters);
		}

	}

	static public class Range extends org.rascalmpl.ast.Expression.Range {

		public Range(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			// IListWriter w = vf.listWriter(tf.integerType());
			Result<IValue> from = this.getFirst().interpret(__eval);
			Result<IValue> to = this.getLast().interpret(__eval);
			return from.makeRange(to);

		}

	}

	static public class Reducer extends org.rascalmpl.ast.Expression.Reducer {

		public Reducer(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3,
				java.util.List<org.rascalmpl.ast.Expression> __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult getBacktracker(IEvaluatorContext ctx) {
		  return new BasicBooleanResult(ctx, this);
		}
		
		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return getBacktracker(eval);
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			org.rascalmpl.ast.Expression init = getInit();
			org.rascalmpl.ast.Expression result = getResult();
			java.util.List<org.rascalmpl.ast.Expression> generators = getGenerators();
			int size = generators.size();
			IBooleanResult[] gens = new IBooleanResult[size];
			Environment[] olds = new Environment[size];
			Environment old = __eval.getCurrentEnvt();
			int i = 0;

			Result<IValue> it = init.interpret(__eval);

			try {
				olds[0] = __eval.getCurrentEnvt();
				__eval.pushEnv();
				gens[0] = generators.get(0).getBacktracker(__eval);
				gens[0].init();

				while (i >= 0 && i < size) {
					if (__eval.isInterrupted())
						throw new InterruptException(__eval.getStackTrace(), __eval.getCurrentAST().getLocation());
					if (gens[i].hasNext() && gens[i].next()) {
						if (i == size - 1) {
							__eval.getCurrentEnvt().storeVariable(IT, it);
							it = result.interpret(__eval);
							__eval.unwind(olds[i]);
							__eval.pushEnv();
						} else {
							i++;
							gens[i] = generators.get(i).getBacktracker(__eval);
							gens[i].init();
							olds[i] = __eval.getCurrentEnvt();
							__eval.pushEnv();
						}
					} else {
						__eval.unwind(olds[i]);
						i--;
					}
				}
			} finally {
				__eval.unwind(old);
			}
			return it;

		}

	}

	static public class ReifiedType extends
			org.rascalmpl.ast.Expression.ReifiedType {
		private static final Type defType = TypeFactory.getInstance().mapType(RascalValueFactory.Symbol, RascalValueFactory.Production);
		
		public ReifiedType(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3
				) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new ReifiedTypePattern(eval, this, getSymbol().buildMatcher(eval, bindTypeParameters), getDefinitions().buildMatcher(eval, bindTypeParameters));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> symbol = getSymbol().interpret(__eval);
			Result<IValue> declarations = getDefinitions().interpret(__eval);
			
			if (!symbol.getStaticType().isSubtypeOf(RascalValueFactory.Symbol)) {
				throw new UnexpectedType(RascalValueFactory.Symbol, symbol.getStaticType(), getSymbol());
			}
			
			if (!declarations.getStaticType().isSubtypeOf(defType)) {
				throw new UnexpectedType(defType, declarations.getStaticType(), getSymbol());
			}
			
			
			IValue val = IRascalValueFactory.getInstance().reifiedType(
			        (IConstructor) symbol.getValue(), 
			        (IMap) declarations.getValue()
			        );

			Type typ = RascalValueFactory.Type.instantiate(
			        Collections.singletonMap(RascalValueFactory.TypeParam, TF.valueType()));
			
			return ResultFactory.makeResult(typ, val, __eval);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			// TODO: check if this would do it?
			return RascalTypeFactory.getInstance().reifiedType(TF.valueType());
		}

	}

	static public class ReifyType extends
			org.rascalmpl.ast.Expression.ReifyType {

		public ReifyType(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {
			eval.setCurrentAST(this);
			eval.notifyAboutSuspension(this);			

			Type t = getType().typeOf(eval.getCurrentEnvt(), eval, instantiateTypeParameters);
			IMap gr = eval.__getVf().mapWriter().done();
			
			if (!t.isTop()) {
			    // if t == value then let's not call the parser generator.
			    // the reason is that #value occurs in the parser generator itself
			    // so this would trigger an infinite cascade of parser generators loading
			    // each other
			    gr = (IMap) eval.getEvaluator().getGrammar(eval.getCurrentEnvt()).get("rules");
			}
			
			IConstructor value;
			
			if (t instanceof NonTerminalType) {
			   value = ((IRascalValueFactory) eval.getValueFactory()).reifiedType(((NonTerminalType) t).getSymbol(), gr);
			}
			else {
			    value = new TypeReifier(eval.__getVf()).typeToValue(t, eval.getCurrentEnvt().getStore(), gr);
			}
			
			// the static type of a reified type is always equal to its dynamic type
			return makeResult(value.getType(), value, eval);
		}
	}

	static public class Set extends org.rascalmpl.ast.Expression.Set {

		public Set(ISourceLocation __param1, IConstructor tree,
				java.util.List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new SetPattern(eval, this, buildMatchers(this.getElements0(), eval, bindTypeParameters), bindTypeParameters);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			java.util.List<org.rascalmpl.ast.Expression> elements = this
					.getElements0();

			Type elementType = TF.voidType();
			java.util.List<IValue> results = new ArrayList<IValue>();

			for (org.rascalmpl.ast.Expression expr : elements) {
				Result<IValue> resultElem;
				
				if(expr.isSplice() || expr.isSplicePlus()){
					resultElem = expr.getArgument().interpret(__eval);
					if (resultElem.getStaticType().isBottom()) {
						throw new NonVoidTypeRequired(expr.getArgument());
					}

					if (resultElem.getStaticType().isSet() || resultElem.getStaticType().isList()){
						/*
						 * Splice the elements in the set
						 * __eval.
						 */
						elementType = elementType.lub(resultElem.getStaticType().getElementType());
						for (IValue val : (Iterable<IValue>) resultElem.getValue()) {
							results.add(val);
						}
					continue;
					}
				} else {
					resultElem = expr.interpret(__eval);
					if (resultElem.getStaticType().isBottom()) {
						throw new NonVoidTypeRequired(expr);
					}
				}
				elementType = elementType.lub(resultElem.getStaticType());
				results.add(results.size(), resultElem.getValue());
			}
			
			Type resultType = TF.setType(elementType);
			ISetWriter w = __eval.__getVf().setWriter();
			w.insertAll(results);
			
			return makeResult(resultType, w.done(), __eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			Type elementType = TF.voidType();

			for (org.rascalmpl.ast.Expression elt : getElements0()) {
				Type eltType = elt.typeOf(env, eval, instantiateTypeParameters);
				
				// TODO: here we need to properly deal with splicing operators!!!
				if (eltType.isSet()) {
				  eltType = eltType.getElementType();
				}
        elementType = elementType.lub(eltType);
			}

			return TF.setType(elementType);
		}

	}

	static public class SetAnnotation extends
			org.rascalmpl.ast.Expression.SetAnnotation {

		public SetAnnotation(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2, Name __param3,
				org.rascalmpl.ast.Expression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> base = this.getExpression().interpret(__eval);
			String annoName = org.rascalmpl.interpreter.utils.Names.name(this
					.getName());
			Result<IValue> anno = this.getValue().interpret(__eval);
			return base.setAnnotation(annoName, anno, __eval.getCurrentEnvt());
		}

	}

	static public class StepRange extends
			org.rascalmpl.ast.Expression.StepRange {

		public StepRange(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3,
				org.rascalmpl.ast.Expression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

	

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> from = this.getFirst().interpret(__eval);
			Result<IValue> to = this.getLast().interpret(__eval);
			Result<IValue> second = this.getSecond().interpret(__eval);
			return from.makeStepRange(to, second);

		}

	}
	
	static public class Slice extends
	org.rascalmpl.ast.Expression.Slice {

		public Slice(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2, 
				org.rascalmpl.ast.OptionalExpression __param3, org.rascalmpl.ast.OptionalExpression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> expr = this.getExpression().interpret(__eval);
					
			Result<?> first = this.getOptFirst().hasExpression() ? this.getOptFirst().getExpression().interpret(__eval) : null;
			Result<?> last = this.getOptLast().hasExpression() ? this.getOptLast().getExpression().interpret(__eval) : null;
			
			return expr.slice(first, null, last);
		}
	}
	
	static public class SliceStep extends
	org.rascalmpl.ast.Expression.SliceStep {

		public SliceStep(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2, 
				org.rascalmpl.ast.OptionalExpression __param3, org.rascalmpl.ast.Expression __param4, org.rascalmpl.ast.OptionalExpression __param5) {
			super(__param1, tree, __param2, __param3, __param4, __param5);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			Result<IValue> expr = this.getExpression().interpret(__eval);
						
			Result<?> first = this.getOptFirst().hasExpression() ? this.getOptFirst().getExpression().interpret(__eval) : null;
			Result<?> second = this.getSecond().interpret(__eval);
			Result<?> last = this.getOptLast().hasExpression() ? this.getOptLast().getExpression().interpret(__eval) : null;
			
			return expr.slice(first, second, last);
		}
	}

	static public class Subscript extends
			org.rascalmpl.ast.Expression.Subscript {

		public Subscript(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Expression __param2,
				java.util.List<org.rascalmpl.ast.Expression> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext eval) {
			return new BasicBooleanResult(eval, this);
		}

		@Override
		public Result<IBool> isDefined(IEvaluator<Result<IValue>> __eval) {
			Result<IValue> expr = this.getExpression().interpret(__eval);
			int nSubs = this.getSubscripts().size();
			Result<?> subscripts[] = new Result<?>[nSubs];
			
			for (int i = 0; i < nSubs; i++) {
				org.rascalmpl.ast.Expression subsExpr = this.getSubscripts()
						.get(i);
				subscripts[i] = isWildCard(subsExpr) ? null
						: subsExpr.interpret(__eval);
			}
			
			return expr.isKeyDefined(subscripts);
		}
		
		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> expr = this.getExpression().interpret(__eval);
			int nSubs = this.getSubscripts().size();
			Result<?> subscripts[] = new Result<?>[nSubs];
			for (int i = 0; i < nSubs; i++) {
				org.rascalmpl.ast.Expression subsExpr = this.getSubscripts()
						.get(i);
				subscripts[i] = isWildCard(subsExpr) ? null
						: subsExpr.interpret(__eval);
			}
			return expr.subscript(subscripts);

		}
		
		private boolean isWildCard(org.rascalmpl.ast.Expression fieldName) {
			if (fieldName.isQualifiedName()) {
				return ((org.rascalmpl.semantics.dynamic.QualifiedName.Default) fieldName.getQualifiedName()).lastName().equals("_");
			}
			return false;
		}

	}

	static public class Subtraction extends
			org.rascalmpl.ast.Expression.Subtraction {

		public Subtraction(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			Result<IValue> left = this.getLhs().interpret(__eval);
			Result<IValue> right = this.getRhs().interpret(__eval);
			return left.subtract(right);

		}

	}

	static public class TransitiveClosure extends
			org.rascalmpl.ast.Expression.TransitiveClosure {

		public TransitiveClosure(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);
		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return this.getArgument().interpret(__eval).transitiveClosure();

		}

	}

	static public class TransitiveReflexiveClosure extends
			org.rascalmpl.ast.Expression.TransitiveReflexiveClosure {

		public TransitiveReflexiveClosure(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Expression __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			throw new UnexpectedType(TF.boolType(), this.interpret(__eval.getEvaluator()).getStaticType(), this);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return this.getArgument().interpret(__eval).transitiveReflexiveClosure();
		}

	}

	static public class Tuple extends org.rascalmpl.ast.Expression.Tuple {

		public Tuple(ISourceLocation __param1, IConstructor tree,
				java.util.List<org.rascalmpl.ast.Expression> __param2) {
			super(__param1, tree, __param2);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			return new TuplePattern(eval, this, buildMatchers(this.getElements0(), eval, bindTypeParameters));
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			

			var elements = this.getElements0();

			IValue[] values = new IValue[elements.size()];
			Type[] types = new Type[elements.size()];

			for (int i = 0; i < elements.size(); i++) {
				Result<IValue> resultElem = elements.get(i).interpret(__eval);
				
				if (resultElem.getStaticType().isBottom()) {
                    throw new UnexpectedType(TF.valueType(), TF.voidType(), this);
                }
				
				types[i] = resultElem.getStaticType();
				values[i] = resultElem.getValue();
			}

			// return makeResult(tf.tupleType(types),
			// applyRules(vf.tuple(values)));
			return org.rascalmpl.interpreter.result.ResultFactory.makeResult(TF
					.tupleType(types), __eval.__getVf().tuple(values), __eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			java.util.List<org.rascalmpl.ast.Expression> fields = getElements0();
			Type fieldTypes[] = new Type[fields.size()];

			for (int i = 0; i < fields.size(); i++) {
				fieldTypes[i] = fields.get(i).typeOf(env, eval, instantiateTypeParameters);
			}

			return TF.tupleType(fieldTypes);
		}
	}

	static public class TypedVariable extends org.rascalmpl.ast.Expression.TypedVariable {
		public TypedVariable(ISourceLocation __param1, IConstructor tree, org.rascalmpl.ast.Type __param2,
				Name __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {
			throw new UninitializedVariable(Names.name(getName()), this);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			Environment env = eval.getCurrentEnvt();
			Type type = getType().typeOf(env, eval.getEvaluator(), instantiateTypeParameters);

			return new TypedVariablePattern(eval, this, type, getName(), bindTypeParameters);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			// TODO: should allow qualified names in TypeVariables?!?
			Result<IValue> result = __eval.getCurrentEnvt().getFrameVariable(Names.name(this.getName()));

			if (result != null && result.getValue() != null) {
				return result;
			}

			throw new UninitializedVariable(Names.name(this.getName()), this);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getType().typeOf(env, eval, instantiateTypeParameters);
		}
	}

	static public class TypedVariableBecomes extends
			org.rascalmpl.ast.Expression.TypedVariableBecomes {

		public TypedVariableBecomes(ISourceLocation __param1, IConstructor tree,
				org.rascalmpl.ast.Type __param2, Name __param3,
				org.rascalmpl.ast.Expression __param4) {
			super(__param1, tree, __param2, __param3, __param4);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new SyntaxError("expression", this.getLocation());

		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			Type type = getType().typeOf(eval.getCurrentEnvt(), eval.getEvaluator(), instantiateTypeParameters);
			IMatchingResult pat = this.getPattern().buildMatcher(eval, bindTypeParameters);
			IMatchingResult var = new TypedVariablePattern(eval, this, type, this.getName(), bindTypeParameters);
			return new VariableBecomesPattern(eval, this, var, pat);

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return this.getPattern().interpret(__eval);

		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getType().typeOf(env, eval, instantiateTypeParameters);
		}

	}

	static public class VariableBecomes extends
			org.rascalmpl.ast.Expression.VariableBecomes {

		public VariableBecomes(ISourceLocation __param1, IConstructor tree, Name __param2,
				org.rascalmpl.ast.Expression __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IMatchingResult buildMatcher(IEvaluatorContext eval, boolean bindTypeParameters) {
			org.rascalmpl.ast.Expression pattern = this.getPattern();
			
			if (pattern instanceof Splice) {
			    throw new UnsupportedPattern("named splices (i.e. name:*pattern)", this);
			}
            IMatchingResult pat = pattern.buildMatcher(eval, bindTypeParameters);
			LinkedList<Name> names = new LinkedList<Name>();
			names.add(this.getName());
			IMatchingResult var = new QualifiedNamePattern(eval, this, ASTBuilder.<org.rascalmpl.ast.QualifiedName> make("QualifiedName", "Default", this.getLocation(), names));
			return new VariableBecomesPattern(eval, this, var, pat);

		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {
			
			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return this.getPattern().interpret(__eval);
		}

		@Override
		public Type typeOf(Environment env, IEvaluator<Result<IValue>> eval, boolean instantiateTypeParameters) {
			return getPattern().typeOf(env, eval, instantiateTypeParameters);
		}

	}

	static public class Visit extends org.rascalmpl.ast.Expression.Visit {

		public Visit(ISourceLocation __param1, IConstructor tree, Label __param2,
				org.rascalmpl.ast.Visit __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> __eval) {

			__eval.setCurrentAST(this);
			__eval.notifyAboutSuspension(this);			
			
			return this.getVisit().interpret(__eval);

		}

	}

	static public class VoidClosure extends
			org.rascalmpl.ast.Expression.VoidClosure {

		public VoidClosure(ISourceLocation __param1, IConstructor tree, Parameters __param2,
				java.util.List<Statement> __param3) {
			super(__param1, tree, __param2, __param3);
		}

		@Override
		public IBooleanResult buildBacktracker(IEvaluatorContext __eval) {

			throw new UnexpectedType(TF.boolType(), this
					.interpret(__eval.getEvaluator()).getStaticType(),
					this);

		}

		

		@Override
		public Result<IValue> interpret(IEvaluator<Result<IValue>> eval) {

			eval.setCurrentAST(this);
			eval.notifyAboutSuspension(this);			

			Parameters parameters = getParameters();
			Type formals = parameters.typeOf(eval.getCurrentEnvt(), eval, instantiateTypeParameters);
			Type kwParams = TF.tupleEmpty();
			java.util.List<KeywordFormal> kws = parameters.getKeywordFormals().hasKeywordFormalList() ? parameters.getKeywordFormals().getKeywordFormalList() : Collections.<KeywordFormal>emptyList();
			
			if (parameters.hasKeywordFormals() && parameters.getKeywordFormals().hasKeywordFormalList()) {
				kwParams = TypeDeclarationEvaluator.computeKeywordParametersType(kws, eval);
			}

			return new RascalFunction(this, 
				eval, 
				null, 
				TF.functionType(TF.voidType(), formals, kwParams), 
				TF.functionType(TF.voidType(), formals, kwParams).instantiate(eval.getCurrentEnvt().getDynamicTypeBindings()),
				kws, 
				this.getParameters().isVarArgs(), 
				false, 
				false, 
				this.getStatements0(), 
				eval.getCurrentEnvt(), 
				eval.__getAccumulators()
			);
		}
	}

	public Expression(ISourceLocation __param1, IConstructor tree) {
		super(__param1, tree);
	}
	
	private static java.util.List<IMatchingResult> buildMatchers(java.util.List<org.rascalmpl.ast.Expression> elements, IEvaluatorContext eval, boolean bindTypeParameters) {
		ArrayList<IMatchingResult> args = new ArrayList<IMatchingResult>(elements.size());

		int i = 0;
		for (org.rascalmpl.ast.Expression e : elements) {
			args.add(i++, e.buildMatcher(eval, bindTypeParameters));
		}
		
		return args;
	}
	
	private static Result<IValue> evalBooleanExpression(org.rascalmpl.ast.Expression x, IEvaluatorContext ctx) {
		IBooleanResult mp = x.getBacktracker(ctx);
		mp.init();
//		while (mp.hasNext()) {
//			if (ctx.isInterrupted())
//				throw new InterruptException(ctx.getStackTrace());
//			if (mp.next()) {
//				return ResultFactory.bool(true, ctx);
//			}
//		}
		return makeResult(TypeFactory.getInstance().boolType(), ctx.getValueFactory().bool(mp.hasNext() && mp.next()), ctx);
	}
}
