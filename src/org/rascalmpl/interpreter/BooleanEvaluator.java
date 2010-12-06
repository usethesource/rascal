package org.rascalmpl.interpreter;

import java.util.Stack;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Expression.Addition;
import org.rascalmpl.ast.Expression.All;
import org.rascalmpl.ast.Expression.And;
import org.rascalmpl.ast.Expression.Anti;
import org.rascalmpl.ast.Expression.Any;
import org.rascalmpl.ast.Expression.Bracket;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.Composition;
import org.rascalmpl.ast.Expression.Comprehension;
import org.rascalmpl.ast.Expression.Descendant;
import org.rascalmpl.ast.Expression.Enumerator;
import org.rascalmpl.ast.Expression.Equals;
import org.rascalmpl.ast.Expression.Equivalence;
import org.rascalmpl.ast.Expression.FieldProject;
import org.rascalmpl.ast.Expression.FieldUpdate;
import org.rascalmpl.ast.Expression.GetAnnotation;
import org.rascalmpl.ast.Expression.GreaterThan;
import org.rascalmpl.ast.Expression.GreaterThanOrEq;
import org.rascalmpl.ast.Expression.Guarded;
import org.rascalmpl.ast.Expression.IfThenElse;
import org.rascalmpl.ast.Expression.Implication;
import org.rascalmpl.ast.Expression.In;
import org.rascalmpl.ast.Expression.IsDefined;
import org.rascalmpl.ast.Expression.LessThan;
import org.rascalmpl.ast.Expression.LessThanOrEq;
import org.rascalmpl.ast.Expression.List;
import org.rascalmpl.ast.Expression.Literal;
import org.rascalmpl.ast.Expression.Map;
import org.rascalmpl.ast.Expression.Match;
import org.rascalmpl.ast.Expression.Modulo;
import org.rascalmpl.ast.Expression.MultiVariable;
import org.rascalmpl.ast.Expression.Negation;
import org.rascalmpl.ast.Expression.Negative;
import org.rascalmpl.ast.Expression.NoMatch;
import org.rascalmpl.ast.Expression.NonEquals;
import org.rascalmpl.ast.Expression.NotIn;
import org.rascalmpl.ast.Expression.Or;
import org.rascalmpl.ast.Expression.QualifiedName;
import org.rascalmpl.ast.Expression.Range;
import org.rascalmpl.ast.Expression.Set;
import org.rascalmpl.ast.Expression.SetAnnotation;
import org.rascalmpl.ast.Expression.StepRange;
import org.rascalmpl.ast.Expression.TransitiveClosure;
import org.rascalmpl.ast.Expression.TransitiveReflexiveClosure;
import org.rascalmpl.ast.Expression.Tuple;
import org.rascalmpl.ast.Expression.TypedVariable;
import org.rascalmpl.ast.Expression.TypedVariableBecomes;
import org.rascalmpl.ast.Expression.VariableBecomes;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.matching.AndResult;
import org.rascalmpl.interpreter.matching.BasicBooleanResult;
import org.rascalmpl.interpreter.matching.EnumeratorResult;
import org.rascalmpl.interpreter.matching.EquivalenceResult;
import org.rascalmpl.interpreter.matching.IBooleanResult;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.MatchResult;
import org.rascalmpl.interpreter.matching.NotResult;
import org.rascalmpl.interpreter.matching.OrResult;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UnexpectedTypeError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.uri.URIResolverRegistry;

public class BooleanEvaluator extends org.rascalmpl.ast.NullASTVisitor<org.rascalmpl.interpreter.matching.IBooleanResult> implements org.rascalmpl.interpreter.IEvaluator<org.rascalmpl.interpreter.matching.IBooleanResult>{
	private final org.rascalmpl.interpreter.IEvaluatorContext ctx;
	private final org.eclipse.imp.pdb.facts.type.TypeFactory tf = org.eclipse.imp.pdb.facts.type.TypeFactory.getInstance();
	private final org.rascalmpl.interpreter.PatternEvaluator pe;

	public BooleanEvaluator(org.rascalmpl.interpreter.IEvaluatorContext ctx) {
		this.ctx = ctx;
		this.pe = new org.rascalmpl.interpreter.PatternEvaluator(ctx);
	}

	public org.eclipse.imp.pdb.facts.type.TypeFactory __getTf() {
		return tf;
	}

	public org.rascalmpl.interpreter.PatternEvaluator __getPe() {
		return pe;
	}

	public org.rascalmpl.interpreter.IEvaluatorContext __getCtx() {
		return ctx;
	}

	public org.rascalmpl.ast.AbstractAST getCurrentAST() {
		return this.__getCtx().getCurrentAST();
	}

	public org.rascalmpl.interpreter.env.Environment getCurrentEnvt() {
		return this.__getCtx().getCurrentEnvt();
	}

	public org.rascalmpl.interpreter.Evaluator getEvaluator() {
		return this.__getCtx().getEvaluator();
	}

	public org.rascalmpl.interpreter.env.GlobalEnvironment getHeap() {
		return this.__getCtx().getHeap();
	}

	public java.lang.String getStackTrace() {
		return this.__getCtx().getStackTrace();
	}

	public void pushEnv() {
		this.__getCtx().pushEnv();		
	}

	public boolean runTests() {
		return this.__getCtx().runTests();
	}

	public void setCurrentEnvt(org.rascalmpl.interpreter.env.Environment environment) {
		this.__getCtx().setCurrentEnvt(environment);
	}

	public void unwind(org.rascalmpl.interpreter.env.Environment old) {
		this.__getCtx().unwind(old);
	}

	public void setCurrentAST(org.rascalmpl.ast.AbstractAST ast) {
		this.__getCtx().setCurrentAST(ast);		
	}

	public org.eclipse.imp.pdb.facts.IValueFactory getValueFactory() {
		return this.__getCtx().getValueFactory();
	}

	public org.rascalmpl.interpreter.strategy.IStrategyContext getStrategyContext() {
		return this.__getCtx().getStrategyContext();
	}

	public void pushStrategyContext(org.rascalmpl.interpreter.strategy.IStrategyContext strategyContext) {
		this.__getCtx().pushStrategyContext(strategyContext);
	}

	public void popStrategyContext() {
		this.__getCtx().popStrategyContext();
	}

	public java.util.Stack<org.rascalmpl.interpreter.Accumulator> getAccumulators() {
		return this.__getCtx().getAccumulators();
	}

	public void setAccumulators(java.util.Stack<org.rascalmpl.interpreter.Accumulator> accumulators) {
		this.__getCtx().setAccumulators(accumulators);
	}

	public org.eclipse.imp.pdb.facts.IValue call(java.lang.String name, org.eclipse.imp.pdb.facts.IValue... args) {
		throw new org.rascalmpl.interpreter.asserts.ImplementationError("should not call call");
	}

	public org.rascalmpl.uri.URIResolverRegistry getResolverRegistry() {
		return this.__getCtx().getResolverRegistry();
	}

	public void interrupt() {
		// TODO Auto-generated method stub
		
	}

	public boolean isInterrupted() {
		return false;
	}

}
