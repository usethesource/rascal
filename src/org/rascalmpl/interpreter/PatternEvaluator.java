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
 *   * Anya Helene Bagge - A.H.S.Bagge@cwi.nl (Univ. Bergen)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.uri.URIResolverRegistry;

public class PatternEvaluator extends NullASTVisitor<IMatchingResult> implements IEvaluator<IMatchingResult> {
	private final IEvaluatorContext ctx;
	private boolean debug = false;
	private static final TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();

	public PatternEvaluator(IEvaluatorContext ctx) {
		this.ctx = ctx;
	}

	public static TypeFactory __getTf() {
		return tf;
	}

	public void __setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean __getDebug() {
		return debug;
	}

	public IEvaluatorContext __getCtx() {
		return ctx;
	}

	public IValue call(String name, IValue... args) {
		throw new ImplementationError("should not call call");
	}

	public String getValueAsString(String varName) {
		Environment env = this.__getCtx().getCurrentEnvt();
		Result<IValue> res = env.getVariable(varName);
		if (res != null && res.getValue() != null) {
			if (res.getType().isStringType())
				return ((IString) res.getValue()).getValue();

			return res.getValue().toString();
		}

		throw new UninitializedVariableError(varName, this.__getCtx().getCurrentAST());
	}

	/*
	 * Interpolate all occurrences of <X> by the value of X
	 */
	public String interpolate(String re) {
		Pattern replacePat = java.util.regex.Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)>");
		Matcher m = replacePat.matcher(re);
		StringBuffer result = new StringBuffer();
		int start = 0;
		while (m.find()) {
			result.append(re.substring(start, m.start(0))).append(this.getValueAsString(m.group(1))); // TODO:
																										// escape
																										// special
																										// chars?
			start = m.end(0);
		}
		result.append(re.substring(start, re.length()));

		if (this.__getDebug())
			System.err.println("interpolate: " + re + " -> " + result);
		return result.toString();
	}

	
	public List<IMatchingResult> visitArguments(CallOrTree x) {
		List<Expression> elements = x.getArguments();
		return this.visitElements(elements);
	}

	public List<IMatchingResult> visitConcreteLexicalArguments(CallOrTree x) {
		Expression args = x.getArguments().get(1);

		List<Expression> elements = args.getElements();
		return this.visitElements(elements);
	}

	public List<IMatchingResult> visitConcreteArguments(CallOrTree x) {
		Expression args = x.getArguments().get(1);

		List<Expression> elements = args.getElements();
		return this.visitConcreteElements(elements);
	}

	private List<IMatchingResult> visitConcreteElements(List<Expression> elements) {
		int n = elements.size();
		ArrayList<IMatchingResult> args = new ArrayList<IMatchingResult>((n + 1) / 2);

		for (int i = 0; i < n; i += 2) { // skip layout elements
			Expression e = elements.get(i);
			args.add(e.buildMatcher(this));
		}
		return args;
	}

	public List<IMatchingResult> visitElements(List<Expression> elements) {
		ArrayList<IMatchingResult> args = new ArrayList<IMatchingResult>(elements.size());

		int i = 0;
		for (Expression e : elements) {
			args.add(i++, e.buildMatcher(this));
		}
		return args;
	}

	public AbstractAST getCurrentAST() {
		return this.__getCtx().getCurrentAST();
	}

	public Environment getCurrentEnvt() {
		return this.__getCtx().getCurrentEnvt();
	}

	public Evaluator getEvaluator() {
		return this.__getCtx().getEvaluator();
	}

	public GlobalEnvironment getHeap() {
		return this.__getCtx().getHeap();
	}

	public String getStackTrace() {
		return this.__getCtx().getStackTrace();
	}

	public void pushEnv() {
		this.__getCtx().pushEnv();
	}

	public boolean runTests(IRascalMonitor monitor) {
		return this.__getCtx().runTests(monitor);
	}

	public void setCurrentEnvt(Environment environment) {
		this.__getCtx().setCurrentEnvt(environment);
	}

	public void unwind(Environment old) {
		this.__getCtx().unwind(old);
	}

	public void setCurrentAST(AbstractAST ast) {
		this.__getCtx().setCurrentAST(ast);
	}

	public IValueFactory getValueFactory() {
		return this.__getCtx().getValueFactory();
	}

	public IStrategyContext getStrategyContext() {
		return this.__getCtx().getStrategyContext();
	}

	public void pushStrategyContext(IStrategyContext strategyContext) {
		this.__getCtx().pushStrategyContext(strategyContext);
	}

	public void popStrategyContext() {
		this.__getCtx().popStrategyContext();
	}

	public Stack<Accumulator> getAccumulators() {
		return this.__getCtx().getAccumulators();
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		this.__getCtx().setAccumulators(accumulators);
	}

	public URIResolverRegistry getResolverRegistry() {
		return this.__getCtx().getResolverRegistry();
	}

	public void interrupt() {

	}

	public boolean isInterrupted() {
		return false;
	}

	public PrintWriter getStdErr() {
		return null;
	}

	public PrintWriter getStdOut() {
		return null;
	}

	public ASTBuilder getBuilder() {
		return __getCtx().getBuilder();
	}

	public int endJob(boolean succeeded) {
		return ctx.endJob(succeeded);
	}

	public void event(int inc) {
		ctx.event(inc);
	}

	public void event(String name, int inc) {
		ctx.event(name, inc);
	}

	public void event(String name) {
		ctx.event(name);
	}

	public void startJob(String name, int workShare, int totalWork) {
		ctx.startJob(name, workShare, totalWork);
	}

	public void startJob(String name, int totalWork) {
		ctx.startJob(name, totalWork);
	}
	
	public void startJob(String name) {
		ctx.startJob(name);
		
	}

	public void todo(int work) {
		ctx.todo(work);
	}
}
