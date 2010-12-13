package org.rascalmpl.interpreter;

import java.io.PrintWriter;
import java.lang.String;
import java.util.Stack;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.PatternEvaluator;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.uri.URIResolverRegistry;

public class BooleanEvaluator extends NullASTVisitor<IBooleanResult> implements IEvaluator<IBooleanResult> {
	private final IEvaluatorContext ctx;
	private final TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
	private final PatternEvaluator pe;

	public BooleanEvaluator(IEvaluatorContext ctx) {
		this.ctx = ctx;
		this.pe = new PatternEvaluator(ctx);
	}

	public TypeFactory __getTf() {
		return tf;
	}

	public PatternEvaluator __getPe() {
		return pe;
	}

	public IEvaluatorContext __getCtx() {
		return ctx;
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

	public boolean runTests() {
		return this.__getCtx().runTests();
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

	public IValue call(String name, IValue... args) {
		throw new ImplementationError("should not call call");
	}

	public URIResolverRegistry getResolverRegistry() {
		return this.__getCtx().getResolverRegistry();
	}

	public void interrupt() {
		// TODO Auto-generated method stub

	}

	public boolean isInterrupted() {
		return false;
	}

	public PrintWriter getStdErr() {
		return new PrintWriter(System.err);
	}

	public PrintWriter getStdOut() {
		return new PrintWriter(System.out);
	}

}
