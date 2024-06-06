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

import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.ast.FunctionDeclaration;
import org.rascalmpl.ast.FunctionDeclaration.Conditional;
import org.rascalmpl.ast.FunctionDeclaration.Default;
import org.rascalmpl.ast.KeywordFormal;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Parameters;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Type.Structured;
import org.rascalmpl.exceptions.ImplementationError;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.control_exceptions.Failure;
import org.rascalmpl.interpreter.control_exceptions.InterruptException;
import org.rascalmpl.interpreter.control_exceptions.MatchFailed;
import org.rascalmpl.interpreter.control_exceptions.Return;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.staticErrors.MissingReturn;
import org.rascalmpl.interpreter.staticErrors.UnexpectedType;
import org.rascalmpl.interpreter.staticErrors.UnguardedFail;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPattern;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.semantics.dynamic.Tree;
import org.rascalmpl.semantics.dynamic.Tree.Appl;

import io.usethesource.vallang.IBool;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.type.Type;

public class RascalFunction extends NamedFunction {
    private final List<Statement> body;
    private final boolean isVoidFunction;
    private final Stack<Accumulator> accumulators;
    private final List<Expression> formals;
    private final int indexedPosition;
    private final String indexedLabel;
    private final IConstructor indexedProduction;
    private final List<KeywordFormal> initializers;

    public RascalFunction(IEvaluator<Result<IValue>> eval, FunctionDeclaration.Default func, boolean varargs, Environment env, Stack<Accumulator> accumulators) {
        this(func, eval,
            Names.name(func.getSignature().getName()),
            func.getSignature().typeOf(env, eval, false),
            func.getSignature().typeOf(env, eval, false).instantiate(env.getDynamicTypeBindings()),
            getFormals(func),
            varargs, isDefault(func), hasTestMod(func.getSignature()),
            func.getBody().getStatements(), env, accumulators);
    }

    public RascalFunction(IEvaluator<Result<IValue>> eval, FunctionDeclaration.Expression func, boolean varargs,  Environment env,
        Stack<Accumulator> accumulators) {
        this(func, eval,
            Names.name(func.getSignature().getName()),
            func.getSignature().typeOf(env, eval, false), 
            func.getSignature().typeOf(env, eval, false).instantiate(env.getDynamicTypeBindings()),
            getFormals(func),
            varargs, isDefault(func), hasTestMod(func.getSignature()), 
            Arrays.asList(new Statement[] { ASTBuilder.makeStat("Return", func.getLocation(), ASTBuilder.makeStat("Expression", func.getLocation(), func.getExpression()))}),
            env, accumulators);
    }

    @SuppressWarnings("unchecked")
    public RascalFunction(AbstractAST ast, IEvaluator<Result<IValue>> eval, String name, Type staticType, Type dynamicType,
        List<KeywordFormal> initializers,
        boolean varargs, boolean isDefault, boolean isTest, List<Statement> body, Environment env, Stack<Accumulator> accumulators) {
        super(ast, eval, staticType, dynamicType, initializers, name, varargs, isDefault, isTest, env);
        this.body = body;
       
        this.isVoidFunction = this.staticFunctionType.getReturnType().isSubtypeOf(TF.voidType());
        this.accumulators = (Stack<Accumulator>) accumulators.clone();
        this.formals = cacheFormals();
        this.indexedPosition = computeIndexedPosition(ast);
        this.indexedLabel = computeIndexedLabel(indexedPosition, ast);
        this.indexedProduction = computeIndexedProduction(indexedPosition, ast);
        this.initializers = initializers;
    }

    @Override
    public RascalFunction cloneInto(Environment env) {
        AbstractAST clone = cloneAst();
        List<Statement> clonedBody = cloneBody();
        // TODO: accumulators are not cloned? @tvdstorm check this out:
        return new RascalFunction(clone, getEval(), getName(), getFunctionType(), getType(), initializers, hasVarArgs(), isDefault(), isTest(), clonedBody, env, accumulators);
    }

    private AbstractAST cloneAst() {
        return (AbstractAST) getAst().clone();
    }

    private List<Statement> cloneBody() {
        return getAst().clone(body);
    }

    private String computeIndexedLabel(int pos, AbstractAST ast) {
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
                if (formals.size() > pos) {
                    Expression first = formals.get(pos);

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
    public String getIndexedLabel() {
        return indexedLabel;
    }

    @Override
    public int getIndexedArgumentPosition() {
        return indexedPosition;
    }

    @Override
    public IConstructor getIndexedProduction() {
        return indexedProduction;
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
                throw new UnsupportedPattern("...", last);
            }
        }

        return formals;
    }

    @Override
    public boolean isStatic() {
        return isStatic;
    }

    public boolean isAnonymous() {
        return getName() == null;
    }

    @Override
    public Result<IValue> call(Type[] actualStaticTypes, IValue[] actuals, Map<String, IValue> keyArgValues) {
        Result<IValue> result = getMemoizedResult(actuals, keyArgValues);
        if (result != null) { 
            return result;
        }

        Environment old = ctx.getCurrentEnvt();
        AbstractAST currentAST = ctx.getCurrentAST();
        AbstractAST oldAST = currentAST;
        Stack<Accumulator> oldAccus = ctx.getAccumulators();
        Map<Type, Type> renamings = new HashMap<>();
        Map<Type, Type> dynamicRenamings = new HashMap<>();

        try {
            ctx.setCurrentAST(ast);
            if (eval.getCallTracing()) {
                printStartTrace(actuals);
            }

            String label = isAnonymous() ? "Anonymous Function" : name;
            Environment environment = new Environment(declarationEnvironment, ctx.getCurrentEnvt(), currentAST != null ? currentAST.getLocation() : null, ast.getLocation(), label);
            environment.markAsFunctionFrame();
            ctx.setCurrentEnvt(environment);

            IMatchingResult[] matchers = prepareFormals(ctx);
            ctx.setAccumulators(accumulators);
            ctx.pushEnv();

            Type actualStaticTypesTuple = TF.tupleType(actualStaticTypes);
            if (hasVarArgs) {
                actuals = computeVarArgsActuals(actuals, getFormals());
                actualStaticTypesTuple = computeVarArgsActualTypes(actualStaticTypes, getFormals());
            }
         
            int size = actuals.length;
            Environment[] olds = new Environment[size];
            int i = 0;

            if (!hasVarArgs && size != this.formals.size()) {
                throw new MatchFailed();
            }
            
            actualStaticTypesTuple = bindTypeParameters(actualStaticTypesTuple, actuals, getFormals(), renamings, dynamicRenamings, environment);

            if (size == 0) {
                try {
                    bindKeywordArgs(keyArgValues);
//                    checkReturnTypeIsNotVoid(formals, actuals);
                    result = runBody();
                    result = storeMemoizedResult(actuals,keyArgValues, result);
                    if (eval.getCallTracing()) {
                        printEndTrace(result.getValue());
                    }
                    return result;
                }
                catch (Return e) {
                    checkReturnTypeIsNotVoid(formals, actuals, renamings);
                    result = computeReturn(e, renamings, dynamicRenamings);
                    storeMemoizedResult(actuals,keyArgValues, result);
                    return result;
                }
            }

            matchers[0].initMatch(makeResult(actualStaticTypesTuple.getFieldType(0), actuals[0], ctx));
            olds[0] = ctx.getCurrentEnvt();
            ctx.pushEnv();

            // pattern matching requires backtracking due to list, set and map matching and
            // non-linear use of variables between formal parameters of a function...

            while (i >= 0 && i < size) {
                if (ctx.isInterrupted()) { 
                    throw new InterruptException(ctx.getStackTrace(), currentAST.getLocation());
                }
                if (matchers[i].hasNext() && matchers[i].next()) {
                    if (i == size - 1) {
                        // formals are now bound by side effect of the pattern matcher
                        try {
                            bindKeywordArgs(keyArgValues);

                            result = runBody();
                            storeMemoizedResult(actuals, keyArgValues, result);
                            return result;
                        }
                        catch (Failure e) {
                            // backtrack current pattern assignment
                            if (!e.hasLabel() || e.hasLabel() && e.getLabel().equals(getName())) {
                                continue;
                            }
                            else {
                                throw new UnguardedFail(getAst(), e);
                            }
                        }
                    }
                    else {
                        i++;
                        matchers[i].initMatch(makeResult(actualStaticTypesTuple.getFieldType(i), actuals[i], ctx));
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
            checkReturnTypeIsNotVoid(formals, actuals, renamings);
            
            result = computeReturn(e, renamings, dynamicRenamings);
            storeMemoizedResult(actuals, keyArgValues, result);
            if (eval.getCallTracing()) {
                printEndTrace(result.getValue());
            }
            return result;
        } 
        catch (Throwable e) {
            if (eval.getCallTracing()) {
                printExcept(e);
            }
            throw e;
        }
        finally {
            if (eval.getCallTracing()) {
                eval.decCallNesting();
            }
            ctx.setCurrentEnvt(old);
            ctx.setAccumulators(oldAccus);
            ctx.setCurrentAST(oldAST);
        }
    }

    private Result<IValue> runBody() {
        for (Statement stat: body) {
            eval.setCurrentAST(stat);
            stat.interpret(eval);
        }

        if(!isVoidFunction){
            throw new MissingReturn(ast);
        }

        return makeResult(TF.voidType(), null, eval);
    }

    private Result<IValue> computeReturn(Return e, Map<Type, Type> renamings, Map<Type, Type> dynamicRenamings) {
        Result<IValue> result = e.getValue();
        Type returnType = getReturnType();

        // only use static checks here
        if(!result.getStaticType().isSubtypeOf(getReturnType())){
            throw new UnexpectedType(returnType, result.getStaticType(), e.getLocation());
        }

        if (!returnType.isBottom() && result.getStaticType().isBottom()) {
            throw new UnexpectedType(returnType, result.getStaticType(), e.getLocation());
        }

        // here we instantiate type parameters for computing a return value type.
        // such that at least local type parameters do not leak into the caller's scope

        Map<Type, Type> bindings = ctx.getCurrentEnvt().getStaticTypeBindings();
        Type instantiatedReturnType = returnType.instantiate(bindings);

        if (instantiatedReturnType.isOpen()) {
            instantiatedReturnType = unrenameType(renamings, instantiatedReturnType);
        }

        return makeResult(instantiatedReturnType, result.getValue(), eval);
    }

    private IMatchingResult[] prepareFormals(IEvaluatorContext ctx) {
        int size = formals.size();
        IMatchingResult[] matchers = new IMatchingResult[size];

        for (int i = 0; i < size; i++) {
            matchers[i] = formals.get(i).getMatcher(ctx, true);
        }

        return matchers;
    }

    private List<Expression> replaceLast(List<Expression> formals, Expression last) {
        List<Expression> tmp = new ArrayList<Expression>(formals.size());
        tmp.addAll(formals);
        tmp.set(formals.size() - 1, last);
        formals = tmp;
        return formals;
    }

    @Override
    public <V extends IValue> Result<IBool> equals(Result<V> that) {
        return that.equalToRascalFunction(this);
    }

    @Override
    public Result<IBool> equalToRascalFunction(RascalFunction that) {
        return ResultFactory.bool((this == that), ctx);
    }


    private IConstructor computeIndexedProduction(int pos, AbstractAST ast) {
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
                if (formals.size() > pos) {
                    Expression first = formals.get(pos);

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


}
