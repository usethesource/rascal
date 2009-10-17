package org.meta_environment.rascal.interpreter.debug;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.Expression.Addition;
import org.meta_environment.rascal.ast.Expression.All;
import org.meta_environment.rascal.ast.Expression.Ambiguity;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Anti;
import org.meta_environment.rascal.ast.Expression.Any;
import org.meta_environment.rascal.ast.Expression.Bracket;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.Closure;
import org.meta_environment.rascal.ast.Expression.Composition;
import org.meta_environment.rascal.ast.Expression.Comprehension;
import org.meta_environment.rascal.ast.Expression.Descendant;
import org.meta_environment.rascal.ast.Expression.Division;
import org.meta_environment.rascal.ast.Expression.Enumerator;
import org.meta_environment.rascal.ast.Expression.Equals;
import org.meta_environment.rascal.ast.Expression.Equivalence;
import org.meta_environment.rascal.ast.Expression.FieldAccess;
import org.meta_environment.rascal.ast.Expression.FieldProject;
import org.meta_environment.rascal.ast.Expression.FieldUpdate;
import org.meta_environment.rascal.ast.Expression.GetAnnotation;
import org.meta_environment.rascal.ast.Expression.GreaterThan;
import org.meta_environment.rascal.ast.Expression.GreaterThanOrEq;
import org.meta_environment.rascal.ast.Expression.Guarded;
import org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise;
import org.meta_environment.rascal.ast.Expression.IfThenElse;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.In;
import org.meta_environment.rascal.ast.Expression.Intersection;
import org.meta_environment.rascal.ast.Expression.IsDefined;
import org.meta_environment.rascal.ast.Expression.Join;
import org.meta_environment.rascal.ast.Expression.LessThan;
import org.meta_environment.rascal.ast.Expression.LessThanOrEq;
import org.meta_environment.rascal.ast.Expression.Lexical;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Map;
import org.meta_environment.rascal.ast.Expression.Match;
import org.meta_environment.rascal.ast.Expression.Modulo;
import org.meta_environment.rascal.ast.Expression.MultiVariable;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.Negative;
import org.meta_environment.rascal.ast.Expression.NoMatch;
import org.meta_environment.rascal.ast.Expression.NonEmptyBlock;
import org.meta_environment.rascal.ast.Expression.NonEquals;
import org.meta_environment.rascal.ast.Expression.NotIn;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.Product;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Range;
import org.meta_environment.rascal.ast.Expression.ReifiedType;
import org.meta_environment.rascal.ast.Expression.ReifyType;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.SetAnnotation;
import org.meta_environment.rascal.ast.Expression.StepRange;
import org.meta_environment.rascal.ast.Expression.Subscript;
import org.meta_environment.rascal.ast.Expression.Subtraction;
import org.meta_environment.rascal.ast.Expression.TransitiveClosure;
import org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.ast.Expression.TypedVariableBecomes;
import org.meta_environment.rascal.ast.Expression.VariableBecomes;
import org.meta_environment.rascal.ast.Expression.VoidClosure;
import org.meta_environment.rascal.ast.Statement.Append;
import org.meta_environment.rascal.ast.Statement.Assert;
import org.meta_environment.rascal.ast.Statement.AssertWithMessage;
import org.meta_environment.rascal.ast.Statement.Assignment;
import org.meta_environment.rascal.ast.Statement.Break;
import org.meta_environment.rascal.ast.Statement.Continue;
import org.meta_environment.rascal.ast.Statement.DoWhile;
import org.meta_environment.rascal.ast.Statement.EmptyStatement;
import org.meta_environment.rascal.ast.Statement.Expression;
import org.meta_environment.rascal.ast.Statement.Fail;
import org.meta_environment.rascal.ast.Statement.For;
import org.meta_environment.rascal.ast.Statement.FunctionDeclaration;
import org.meta_environment.rascal.ast.Statement.GlobalDirective;
import org.meta_environment.rascal.ast.Statement.IfThen;
import org.meta_environment.rascal.ast.Statement.Insert;
import org.meta_environment.rascal.ast.Statement.Return;
import org.meta_environment.rascal.ast.Statement.Solve;
import org.meta_environment.rascal.ast.Statement.Switch;
import org.meta_environment.rascal.ast.Statement.Throw;
import org.meta_environment.rascal.ast.Statement.Try;
import org.meta_environment.rascal.ast.Statement.TryFinally;
import org.meta_environment.rascal.ast.Statement.VariableDeclaration;
import org.meta_environment.rascal.ast.Statement.While;
import org.meta_environment.rascal.interpreter.Evaluator;
import org.meta_environment.rascal.interpreter.IEvaluator;
import org.meta_environment.rascal.interpreter.control_exceptions.QuitException;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.env.GlobalEnvironment;
import org.meta_environment.rascal.interpreter.strategy.IStrategyContext;
import org.meta_environment.rascal.parser.ConsoleParser;

public class DebuggingDecorator<T> extends NullASTVisitor<T> implements IEvaluator<T> {

	private final ConsoleParser parser;

	protected final IDebugger debugger;

	//when there is a suspend request from the debugger (caused by the pause button)
	private boolean suspendRequest;

	private DebugStepMode stepMode = DebugStepMode.NO_STEP;
	private final IEvaluator<T> evaluator;

	public DebuggingDecorator(IEvaluator<T> evaluator, ConsoleParser consoleParser,
			IDebugger debugger) {
		this.evaluator =evaluator;
		this.parser = consoleParser;
		this.debugger = debugger;
	}

	/* used for pattern-matching evaluation */
	@Override
	public T visitRegExpLiteralLexical(
			org.meta_environment.rascal.ast.RegExpLiteral.Lexical x) {
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
	public T visitExpressionGuarded(Guarded x) {
		suspend(x);
		return evaluator.visitExpressionGuarded(x);
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
	public T visitExpressionLexical(Lexical x) {
		suspend(x);
		return evaluator.visitExpressionLexical(x);
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
			org.meta_environment.rascal.ast.Statement.Ambiguity x) {
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
			org.meta_environment.rascal.ast.Statement.IfThenElse x) {
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
			org.meta_environment.rascal.ast.Statement.Visit x) {
		suspend(x);
		return evaluator.visitStatementVisit(x);
	}

	@Override
	public T visitExpressionVisit(
			org.meta_environment.rascal.ast.Expression.Visit x) {
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

	public IConstructor parseCommand(String command) throws IOException {
		return parser.parseCommand(command);
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

	public boolean runTests() {
		return evaluator.runTests();
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

	public void setStrategyContext(IStrategyContext strategyContext) {
		evaluator.setStrategyContext(strategyContext);		
	}


}
