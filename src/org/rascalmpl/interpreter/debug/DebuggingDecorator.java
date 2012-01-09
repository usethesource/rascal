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
 *   * Anya Helene Bagge - anya@ii.uib.no (Univ. Bergen)
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter.debug;

import java.io.PrintWriter;
import java.net.URI;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Statement;
import org.rascalmpl.ast.Expression.Addition;
import org.rascalmpl.ast.Expression.All;
import org.rascalmpl.ast.Expression.Ambiguity;
import org.rascalmpl.ast.Expression.And;
import org.rascalmpl.ast.Expression.Anti;
import org.rascalmpl.ast.Expression.Any;
import org.rascalmpl.ast.Expression.Bracket;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.Composition;
import org.rascalmpl.ast.Expression.Comprehension;
import org.rascalmpl.ast.Expression.Descendant;
import org.rascalmpl.ast.Expression.Division;
import org.rascalmpl.ast.Expression.Enumerator;
import org.rascalmpl.ast.Expression.Equals;
import org.rascalmpl.ast.Expression.Equivalence;
import org.rascalmpl.ast.Expression.FieldAccess;
import org.rascalmpl.ast.Expression.FieldProject;
import org.rascalmpl.ast.Expression.FieldUpdate;
import org.rascalmpl.ast.Expression.GetAnnotation;
import org.rascalmpl.ast.Expression.GreaterThan;
import org.rascalmpl.ast.Expression.GreaterThanOrEq;
import org.rascalmpl.ast.Expression.AsType;
import org.rascalmpl.ast.Expression.IfDefinedOtherwise;
import org.rascalmpl.ast.Expression.IfThenElse;
import org.rascalmpl.ast.Expression.Implication;
import org.rascalmpl.ast.Expression.In;
import org.rascalmpl.ast.Expression.Intersection;
import org.rascalmpl.ast.Expression.IsDefined;
import org.rascalmpl.ast.Expression.Join;
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
import org.rascalmpl.ast.Expression.NonEmptyBlock;
import org.rascalmpl.ast.Expression.NonEquals;
import org.rascalmpl.ast.Expression.NotIn;
import org.rascalmpl.ast.Expression.Or;
import org.rascalmpl.ast.Expression.Product;
import org.rascalmpl.ast.Expression.QualifiedName;
import org.rascalmpl.ast.Expression.Range;
import org.rascalmpl.ast.Expression.ReifiedType;
import org.rascalmpl.ast.Expression.ReifyType;
import org.rascalmpl.ast.Expression.Set;
import org.rascalmpl.ast.Expression.SetAnnotation;
import org.rascalmpl.ast.Expression.StepRange;
import org.rascalmpl.ast.Expression.Subscript;
import org.rascalmpl.ast.Expression.Subtraction;
import org.rascalmpl.ast.Expression.TransitiveClosure;
import org.rascalmpl.ast.Expression.TransitiveReflexiveClosure;
import org.rascalmpl.ast.Expression.Tuple;
import org.rascalmpl.ast.Expression.TypedVariable;
import org.rascalmpl.ast.Expression.TypedVariableBecomes;
import org.rascalmpl.ast.Expression.VariableBecomes;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.ast.Statement.Append;
import org.rascalmpl.ast.Statement.Assert;
import org.rascalmpl.ast.Statement.AssertWithMessage;
import org.rascalmpl.ast.Statement.Assignment;
import org.rascalmpl.ast.Statement.Break;
import org.rascalmpl.ast.Statement.Continue;
import org.rascalmpl.ast.Statement.DoWhile;
import org.rascalmpl.ast.Statement.EmptyStatement;
import org.rascalmpl.ast.Statement.Expression;
import org.rascalmpl.ast.Statement.Fail;
import org.rascalmpl.ast.Statement.For;
import org.rascalmpl.ast.Statement.FunctionDeclaration;
import org.rascalmpl.ast.Statement.GlobalDirective;
import org.rascalmpl.ast.Statement.IfThen;
import org.rascalmpl.ast.Statement.Insert;
import org.rascalmpl.ast.Statement.Return;
import org.rascalmpl.ast.Statement.Solve;
import org.rascalmpl.ast.Statement.Switch;
import org.rascalmpl.ast.Statement.Throw;
import org.rascalmpl.ast.Statement.Try;
import org.rascalmpl.ast.Statement.TryFinally;
import org.rascalmpl.ast.Statement.VariableDeclaration;
import org.rascalmpl.ast.Statement.While;
import org.rascalmpl.interpreter.Accumulator;
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.IEvaluator;
import org.rascalmpl.interpreter.IRascalMonitor;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.uri.URIResolverRegistry;

public class DebuggingDecorator<T> extends NullASTVisitor<T> implements IEvaluator<T> {
	protected final IDebugger debugger;

	//when there is a suspend request from the debugger (caused by the pause button)
	private boolean suspendRequest;

	private DebugStepMode stepMode = DebugStepMode.NO_STEP;
	private final IEvaluator<T> evaluator;

	public DebuggingDecorator(IEvaluator<T> evaluator, IDebugger debugger) {
		this.evaluator =evaluator;
		this.debugger = debugger;
	}

	public ASTBuilder getBuilder() {
		return evaluator.getBuilder();
	}
	
	public IValue call(IRascalMonitor monitor, String name, IValue... args) {
		return evaluator.getEvaluator().call(monitor, name, args);
	}
	
	/* used for pattern-matching evaluation */
	@Override
	public T visitRegExpLiteralLexical(
			org.rascalmpl.ast.RegExpLiteral.Lexical x) {
		suspend(x);
		return evaluator.visitRegExpLiteralLexical(x);
	}

	@Override
	public T visitExpressionAnti(Anti x) {
		suspend(x);
		return evaluator.visitExpressionAnti(x);
	}

	@Override
	public T visitExpressionAddition(Addition x) {
		suspend(x);
		return evaluator.visitExpressionAddition(x);
	}

	@Override
	public T visitExpressionAll(All x) {
		suspend(x);
		return evaluator.visitExpressionAll(x);
	}

	@Override
	public T visitExpressionAmbiguity(Ambiguity x) {
		suspend(x);
		return evaluator.visitExpressionAmbiguity(x);
	}

	@Override
	public T visitExpressionAnd(And x) {
		suspend(x);
		return evaluator.visitExpressionAnd(x);
	}


	@Override
	public T visitExpressionAny(Any x) {
		suspend(x);
		return evaluator.visitExpressionAny(x);
	}

	@Override
	public T visitExpressionBracket(Bracket x) {
		suspend(x);
		return evaluator.visitExpressionBracket(x);
	}

	@Override
	public T visitExpressionCallOrTree(CallOrTree x) {
		suspend(x);
		if (stepMode.equals(DebugStepMode.STEP_OVER)) {
			/* desactivate the stepping mode when evaluating the call */
			setStepMode(DebugStepMode.NO_STEP);
			T res = evaluator.visitExpressionCallOrTree(x);
			setStepMode(DebugStepMode.STEP_OVER);
			// suspend when returning to the calling statement
			suspend(x);
			return res;
		}

		T res = evaluator.visitExpressionCallOrTree(x);
		// suspend when returning to the calling statement
		suspend(x);
		return res;
	}
	@Override
	public T visitExpressionClosure(Closure x) {
		suspend(x);
		return evaluator.visitExpressionClosure(x);
	}

	@Override
	public T visitExpressionComposition(Composition x) {
		suspend(x);
		return evaluator.visitExpressionComposition(x);
	}

	@Override
	public T visitExpressionComprehension(Comprehension x) {
		suspend(x);
		return evaluator.visitExpressionComprehension(x);
	}

	@Override
	public T visitExpressionDescendant(Descendant x) {
		suspend(x);
		return evaluator.visitExpressionDescendant(x);
	}


	@Override
	public T visitExpressionDivision(Division x) {
		suspend(x);
		return evaluator.visitExpressionDivision(x);
	}

	@Override
	public T visitExpressionEnumerator(Enumerator x) {
		suspend(x);
		return evaluator.visitExpressionEnumerator(x);
	}

	//	@Override
	//	public T visitExpressionEnumeratorWithStrategy(
	//			EnumeratorWithStrategy x) {
	//		suspend(x);
	//		return evaluator.visitExpressionEnumeratorWithStrategy(x);
	//	}

	@Override
	public T visitExpressionEquals(Equals x) {
		suspend(x);
		return evaluator.visitExpressionEquals(x);
	}

	@Override
	public T visitExpressionEquivalence(Equivalence x) {
		suspend(x);
		return evaluator.visitExpressionEquivalence(x);
	}

	@Override
	public T visitExpressionFieldAccess(FieldAccess x) {
		suspend(x);
		return evaluator.visitExpressionFieldAccess(x);
	}

	@Override
	public T visitExpressionFieldProject(FieldProject x) {
		suspend(x);
		return evaluator.visitExpressionFieldProject(x);
	}

	@Override
	public T visitExpressionFieldUpdate(FieldUpdate x) {
		suspend(x);
		return evaluator.visitExpressionFieldUpdate(x);
	}

	@Override
	public T visitExpressionGetAnnotation(GetAnnotation x) {
		suspend(x);
		return evaluator.visitExpressionGetAnnotation(x);
	}


	@Override
	public T visitExpressionGreaterThan(GreaterThan x) {
		suspend(x);
		return evaluator.visitExpressionGreaterThan(x);
	}

	@Override
	public T visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		suspend(x);
		return evaluator.visitExpressionGreaterThanOrEq(x);
	}


	@Override
	public T visitExpressionAsType(AsType x) {
		suspend(x);
		return evaluator.visitExpressionAsType(x);
	}

	@Override
	public T visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		suspend(x);
		return evaluator.visitExpressionIfDefinedOtherwise(x);
	}




	@Override
	public T visitExpressionIfThenElse(IfThenElse x) {
		suspend(x);
		return evaluator.visitExpressionIfThenElse(x);
	}


	@Override
	public T visitExpressionImplication(Implication x) {
		suspend(x);
		return evaluator.visitExpressionImplication(x);
	}

	@Override
	public T visitExpressionIn(In x) {
		suspend(x);
		return evaluator.visitExpressionIn(x);
	}

	@Override
	public T visitExpressionIntersection(Intersection x) {
		suspend(x);
		return evaluator.visitExpressionIntersection(x);
	}

	@Override
	public T visitExpressionIsDefined(IsDefined x) {
		suspend(x);
		return evaluator.visitExpressionIsDefined(x);
	}

	@Override
	public T visitExpressionJoin(Join x) {
		suspend(x);
		return evaluator.visitExpressionJoin(x);
	}

	@Override
	public T visitExpressionLessThan(LessThan x) {
		suspend(x);
		return evaluator.visitExpressionLessThan(x);
	}

	@Override
	public T visitExpressionLessThanOrEq(LessThanOrEq x) {
		suspend(x);
		return evaluator.visitExpressionLessThanOrEq(x);
	}

	@Override
	public T visitExpressionList(List x) {
		suspend(x);
		return evaluator.visitExpressionList(x);
	}

	@Override
	public T visitExpressionLiteral(Literal x) {
		suspend(x);
		return evaluator.visitExpressionLiteral(x);
	}

	@Override
	public T visitExpressionMap(Map x) {
		suspend(x);
		return evaluator.visitExpressionMap(x);
	}

	@Override
	public T visitExpressionMatch(Match x) {
		suspend(x);
		return evaluator.visitExpressionMatch(x);
	}

	@Override
	public T visitExpressionModulo(Modulo x) {
		suspend(x);
		return evaluator.visitExpressionModulo(x);
	}

	@Override
	public T visitExpressionMultiVariable(MultiVariable x) {
		suspend(x);
		return evaluator.visitExpressionMultiVariable(x);
	}

	@Override
	public T visitExpressionNegation(Negation x) {
		suspend(x);
		return evaluator.visitExpressionNegation(x);
	}

	@Override
	public T visitExpressionNegative(Negative x) {
		suspend(x);
		return evaluator.visitExpressionNegative(x);
	}

	@Override
	public T visitExpressionNoMatch(NoMatch x) {
		suspend(x);
		return evaluator.visitExpressionNoMatch(x);
	}

	@Override
	public T visitExpressionNonEquals(NonEquals x) {
		suspend(x);
		return evaluator.visitExpressionNonEquals(x);
	}

	@Override
	public T visitExpressionNotIn(NotIn x) {
		suspend(x);
		return evaluator.visitExpressionNotIn(x);
	}

	@Override
	public T visitExpressionOr(Or x) {
		suspend(x);
		return evaluator.visitExpressionOr(x);
	}

	@Override
	public T visitExpressionProduct(Product x) {
		suspend(x);
		return evaluator.visitExpressionProduct(x);
	}

	@Override
	public T visitExpressionQualifiedName(QualifiedName x) {
		suspend(x);
		return evaluator.visitExpressionQualifiedName(x);
	}

	@Override
	public T visitExpressionRange(Range x) {
		suspend(x);
		return evaluator.visitExpressionRange(x);
	}

	@Override
	public T visitExpressionSet(Set x) {
		suspend(x);
		return evaluator.visitExpressionSet(x);
	}

	@Override
	public T visitExpressionSetAnnotation(SetAnnotation x) {
		suspend(x);
		return evaluator.visitExpressionSetAnnotation(x);
	}

	@Override
	public T visitExpressionStepRange(StepRange x) {
		suspend(x);
		return evaluator.visitExpressionStepRange(x);
	}

	@Override
	public T visitExpressionSubscript(Subscript x) {
		suspend(x);
		return evaluator.visitExpressionSubscript(x);
	}

	@Override
	public T visitExpressionSubtraction(Subtraction x) {
		suspend(x);
		return evaluator.visitExpressionSubtraction(x);
	}

	@Override
	public T visitExpressionTransitiveClosure(TransitiveClosure x) {
		suspend(x);
		return evaluator.visitExpressionTransitiveClosure(x);
	}

	@Override
	public T visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		suspend(x);
		return evaluator.visitExpressionTransitiveReflexiveClosure(x);
	}


	@Override
	public T visitExpressionTuple(Tuple x) {
		suspend(x);
		return evaluator.visitExpressionTuple(x);
	}

	@Override
	public T visitExpressionTypedVariable(TypedVariable x) {
		suspend(x);
		return evaluator.visitExpressionTypedVariable(x);
	}

	@Override
	public T visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		suspend(x);
		return evaluator.visitExpressionTypedVariableBecomes(x);
	}

	@Override
	public T visitExpressionVariableBecomes(VariableBecomes x) {
		suspend(x);
		return evaluator.visitExpressionVariableBecomes(x);
	}

	@Override
	public T visitExpressionVoidClosure(VoidClosure x) {
		suspend(x);
		return evaluator.visitExpressionVoidClosure(x);
	}

	@Override
	public T visitStatementAmbiguity(
			org.rascalmpl.ast.Statement.Ambiguity x) {
		suspend(x);
		return evaluator.visitStatementAmbiguity(x);
	}

	@Override
	public T visitStatementAssert(Assert x) {
		suspend(x);
		return evaluator.visitStatementAssert(x);
	}

	@Override
	public T visitStatementAssertWithMessage(AssertWithMessage x) {
		suspend(x);
		return evaluator.visitStatementAssertWithMessage(x);
	}

	@Override
	public T visitStatementAssignment(Assignment x) {
		suspend(x);
		return evaluator.visitStatementAssignment(x);
	}

	@Override
	public T visitStatementNonEmptyBlock(Statement.NonEmptyBlock x) {
		/* no need to supend on a block */
		//suspend(x);
		return evaluator.visitStatementNonEmptyBlock(x);
	}

	@Override
	public T visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		/* no need to supend on a block */
		//suspend(x);
		return evaluator.visitExpressionNonEmptyBlock(x);
	}

	@Override
	public T visitStatementBreak(Break x) {
		suspend(x);
		return evaluator.visitStatementBreak(x);
	}

	@Override
	public T visitStatementContinue(Continue x) {
		suspend(x);
		return evaluator.visitStatementContinue(x);
	}

	@Override
	public T visitStatementDoWhile(DoWhile x) {
		suspend(x);
		return evaluator.visitStatementDoWhile(x);
	}

	@Override
	public T visitStatementEmptyStatement(EmptyStatement x) {
		suspend(x);
		return evaluator.visitStatementEmptyStatement(x);
	}

	@Override
	public T visitStatementExpression(Expression x) {
		/**		
		 //should avoid to stop twice when stepping into
		if (! stepMode.equals(DebugStepMode.STEP_INTO)) {
			suspend(x);
		}
		 */
		return evaluator.visitStatementExpression(x);
	}

	@Override
	public T visitStatementFail(Fail x) {
		suspend(x);
		return evaluator.visitStatementFail(x);
	}

	@Override
	public T visitStatementFor(For x) {
		suspend(x);
		return evaluator.visitStatementFor(x);
	}

	@Override
	public T visitStatementFunctionDeclaration(
			FunctionDeclaration x) {
		suspend(x);
		return evaluator.visitStatementFunctionDeclaration(x);
	}

	@Override
	public T visitStatementGlobalDirective(GlobalDirective x) {
		suspend(x);
		return evaluator.visitStatementGlobalDirective(x);
	}

	@Override
	public T visitStatementIfThen(IfThen x) {
		suspend(x);
		return evaluator.visitStatementIfThen(x);
	}

	@Override
	public T visitStatementIfThenElse(
			org.rascalmpl.ast.Statement.IfThenElse x) {
		suspend(x);
		return evaluator.visitStatementIfThenElse(x);
	}

	@Override
	public T visitStatementInsert(Insert x) {
		suspend(x);
		return evaluator.visitStatementInsert(x);
	}

	@Override
	public T visitStatementReturn(Return x) {
		suspend(x);
		return evaluator.visitStatementReturn(x);
	}

	@Override
	public T visitStatementSolve(Solve x) {
		suspend(x);
		return evaluator.visitStatementSolve(x);
	}

	@Override
	public T visitStatementSwitch(Switch x) {
		suspend(x);
		return evaluator.visitStatementSwitch(x);
	}

	@Override
	public T visitStatementThrow(Throw x) {
		suspend(x);
		return evaluator.visitStatementThrow(x);
	}

	@Override
	public T visitStatementTry(Try x) {
		suspend(x);
		return evaluator.visitStatementTry(x);
	}

	@Override
	public T visitStatementTryFinally(TryFinally x) {
		suspend(x);
		return evaluator.visitStatementTryFinally(x);
	}

	@Override
	public T visitStatementVariableDeclaration(
			VariableDeclaration x) {
		suspend(x);
		return evaluator.visitStatementVariableDeclaration(x);
	}

	@Override
	public T visitStatementVisit(
			org.rascalmpl.ast.Statement.Visit x) {
		suspend(x);
		return evaluator.visitStatementVisit(x);
	}

	@Override
	public T visitExpressionVisit(
			org.rascalmpl.ast.Expression.Visit x) {
		suspend(x);
		return evaluator.visitExpressionVisit(x);
	}

	@Override
	public T visitStatementWhile(While x) {
		suspend(x);
		return evaluator.visitStatementWhile(x);
	}
	@Override
	public T visitExpressionReifiedType(ReifiedType x) {
		suspend(x);

		return evaluator.visitExpressionReifiedType(x);
	}
	@Override
	public T visitExpressionReifyType(ReifyType x) {
		suspend(x);

		return evaluator.visitExpressionReifyType(x);
	}
	@Override
	public T visitStatementAppend(Append x) {
		suspend(x);

		return evaluator.visitStatementAppend(x);
	}


	private void suspend(AbstractAST x) {
		if (debugger.isTerminated()) {
			//can happen when we evaluating a loop for example and the debugger is stopped
			throw new QuitException();
		}
		if(suspendRequest) {
			setCurrentAST(x);
			debugger.notifySuspend(DebugSuspendMode.CLIENT_REQUEST);
			suspendRequest = false;
		} else if (debugger.isStepping()) {
			switch (stepMode) {
			case STEP_INTO:
				setCurrentAST(x);
				debugger.notifySuspend(DebugSuspendMode.STEP_END);
				break;
			case STEP_OVER:
				if (x instanceof Statement) {
					setCurrentAST(x);
					debugger.notifySuspend(DebugSuspendMode.STEP_END);
				}
			default:
				break;
			}
		} else if (debugger.hasEnabledBreakpoint(getCurrentAST().getLocation())) {
			setCurrentAST(x);
			debugger.notifySuspend(DebugSuspendMode.BREAKPOINT);
		}
	}

	/** 
	 * this method is called when the debugger send a suspend request 
	 * correspond to a suspend event from the client
	 * 
	 * */
	public void suspendRequest() {
		// the evaluator will suspend itself at the next call of suspend or suspend Expression
		suspendRequest = true;
	}

	public void setStepMode(DebugStepMode mode) {
		stepMode = mode;
	}

	public IDebugger getDebugger() {
		return debugger;
	}

	public IConstructor parseCommand(IRascalMonitor monitor, String command){
		return evaluator.getEvaluator().parseCommand(monitor, command, URI.create("debug:///"));
	}

	public AbstractAST getCurrentAST() {
		return evaluator.getCurrentAST();
	}

	public Environment getCurrentEnvt() {
		return evaluator.getCurrentEnvt();
	}

	public Evaluator getEvaluator() {
		return evaluator.getEvaluator();
	}

	public GlobalEnvironment getHeap() {
		return evaluator.getHeap();
	}

	public String getStackTrace() {
		return evaluator.getStackTrace();
	}

	public IValueFactory getValueFactory() {
		return evaluator.getValueFactory();
	}

	public void pushEnv() {
		evaluator.pushEnv();
	}

	public boolean runTests(IRascalMonitor monitor) {
		return evaluator.runTests(monitor);
	}

	public void setCurrentAST(AbstractAST ast) {
		evaluator.setCurrentAST(ast);
	}

	public void setCurrentEnvt(Environment environment) {
		evaluator.setCurrentEnvt(environment);
	}

	public void unwind(Environment old) {
		evaluator.unwind(old);
	}

	public IStrategyContext getStrategyContext() {
		return evaluator.getStrategyContext();
	}
	
	public void pushStrategyContext(IStrategyContext strategyContext){
		evaluator.pushStrategyContext(strategyContext);
	}
	
	public void popStrategyContext() {
		evaluator.popStrategyContext();
	}

	public Stack<Accumulator> getAccumulators() {
		return evaluator.getAccumulators();
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		evaluator.setAccumulators(accumulators);
	}

	public URIResolverRegistry getResolverRegistry() {
		return evaluator.getResolverRegistry();
	}

	public void interrupt() {
		evaluator.interrupt();
	}

	public boolean isInterrupted() {
		return evaluator.isInterrupted();
	}

	public PrintWriter getStdErr() {
		return null;
	}

	public PrintWriter getStdOut() {
		return null;
	}
	
	public int endJob(boolean succeeded) {
		return evaluator.endJob(succeeded);
	}

	public void event(int inc) {
		evaluator.event(inc);
	}

	public void event(String name, int inc) {
		evaluator.event(name, inc);
	}

	public void event(String name) {
		evaluator.event(name);
	}

	public void startJob(String name, int workShare, int totalWork) {
		evaluator.startJob(name, workShare, totalWork);
	}

	public void startJob(String name, int totalWork) {
		evaluator.startJob(name, totalWork);
	}

	public void startJob(String name) {
		evaluator.startJob(name);
		
	}

	public void todo(int work) {
		evaluator.todo(work);
	}
	
	public boolean isCanceled() {
		return evaluator.isCanceled();
	}

}
