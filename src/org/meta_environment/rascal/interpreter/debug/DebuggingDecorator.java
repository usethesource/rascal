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
import org.meta_environment.rascal.ast.Expression.NonEquals;
import org.meta_environment.rascal.ast.Expression.NotIn;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.Product;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Range;
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
import org.meta_environment.rascal.parser.ConsoleParser;

public class DebuggingDecorator<T> extends NullASTVisitor<T> implements IEvaluator<T> {

	private final IDebugger debugger;
	private final ConsoleParser parser;
	private final IEvaluator<T> evaluator;
	private boolean suspendRequest;
	private boolean statementStepMode;
	private boolean expressionStepMode;
	private boolean stepOver;

	public DebuggingDecorator(IEvaluator<T> evaluator,
			ConsoleParser consoleParser,
			IDebugger debugger) {
		super();
		this.parser = consoleParser;
		this.evaluator = evaluator;
		this.debugger = debugger;
	}


	@Override
	public T visitExpressionAnti(Anti x) {
		suspendExpression(x);
		return evaluator.visitExpressionAnti(x);
	}

	@Override
	public T visitExpressionAddition(Addition x) {
		suspendExpression(x);
		return evaluator.visitExpressionAddition(x);
	}

	@Override
	public T visitExpressionAll(All x) {
		suspendExpression(x);
		return evaluator.visitExpressionAll(x);
	}

	@Override
	public T visitExpressionAmbiguity(Ambiguity x) {
		suspendExpression(x);
		return evaluator.visitExpressionAmbiguity(x);
	}

	@Override
	public T visitExpressionAnd(And x) {
		suspendExpression(x);
		return evaluator.visitExpressionAnd(x);
	}

	@Override
	public T visitExpressionAny(Any x) {
		suspendExpression(x);
		return evaluator.visitExpressionAny(x);
	}

	@Override
	public T visitExpressionBracket(Bracket x) {
		suspendExpression(x);
		return evaluator.visitExpressionBracket(x);
	}

	@Override
	public T visitExpressionCallOrTree(CallOrTree x) {
		suspendExpression(x);
		if (stepOver) {
			/* desactivate the stepping mode when evaluating the call */
			boolean oldStatementStepMode = statementStepMode;
			boolean oldExpressionStepMode = expressionStepMode;
			setStatementStepMode(false);
			setExpressionStepMode(false);
			T res = evaluator.visitExpressionCallOrTree(x);
			setStatementStepMode(oldStatementStepMode);
			setExpressionStepMode(oldExpressionStepMode);
			return res;
		}
		
		return evaluator.visitExpressionCallOrTree(x);
	}
	@Override
	public T visitExpressionClosure(Closure x) {
		suspendExpression(x);
		return evaluator.visitExpressionClosure(x);
	}

	@Override
	public T visitExpressionComposition(Composition x) {
		suspendExpression(x);
		return evaluator.visitExpressionComposition(x);
	}

	@Override
	public T visitExpressionComprehension(Comprehension x) {
		suspendExpression(x);
		return evaluator.visitExpressionComprehension(x);
	}

	@Override
	public T visitExpressionDescendant(Descendant x) {
		suspendExpression(x);
		return evaluator.visitExpressionDescendant(x);
	}


	@Override
	public T visitExpressionDivision(Division x) {
		suspendExpression(x);
		return evaluator.visitExpressionDivision(x);
	}

	@Override
	public T visitExpressionEnumerator(Enumerator x) {
		suspendExpression(x);
		return evaluator.visitExpressionEnumerator(x);
	}

//	@Override
//	public T visitExpressionEnumeratorWithStrategy(
//			EnumeratorWithStrategy x) {
//		suspendExpression(x);
//		return evaluator.visitExpressionEnumeratorWithStrategy(x);
//	}

	@Override
	public T visitExpressionEquals(Equals x) {
		suspendExpression(x);
		return evaluator.visitExpressionEquals(x);
	}

	@Override
	public T visitExpressionEquivalence(Equivalence x) {
		suspendExpression(x);
		return evaluator.visitExpressionEquivalence(x);
	}

	@Override
	public T visitExpressionFieldAccess(FieldAccess x) {
		suspendExpression(x);
		return evaluator.visitExpressionFieldAccess(x);
	}

	@Override
	public T visitExpressionFieldProject(FieldProject x) {
		suspendExpression(x);
		return evaluator.visitExpressionFieldProject(x);
	}

	@Override
	public T visitExpressionFieldUpdate(FieldUpdate x) {
		suspendExpression(x);
		return evaluator.visitExpressionFieldUpdate(x);
	}

	@Override
	public T visitExpressionGetAnnotation(GetAnnotation x) {
		suspendExpression(x);
		return evaluator.visitExpressionGetAnnotation(x);
	}


	@Override
	public T visitExpressionGreaterThan(GreaterThan x) {
		suspendExpression(x);
		return evaluator.visitExpressionGreaterThan(x);
	}

	@Override
	public T visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		suspendExpression(x);
		return evaluator.visitExpressionGreaterThanOrEq(x);
	}


	@Override
	public T visitExpressionGuarded(Guarded x) {
		suspendExpression(x);
		return evaluator.visitExpressionGuarded(x);
	}

	@Override
	public T visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		suspendExpression(x);
		return evaluator.visitExpressionIfDefinedOtherwise(x);
	}




	@Override
	public T visitExpressionIfThenElse(IfThenElse x) {
		suspendExpression(x);
		return evaluator.visitExpressionIfThenElse(x);
	}


	@Override
	public T visitExpressionImplication(Implication x) {
		suspendExpression(x);
		return evaluator.visitExpressionImplication(x);
	}

	@Override
	public T visitExpressionIn(In x) {
		suspendExpression(x);
		return evaluator.visitExpressionIn(x);
	}

	@Override
	public T visitExpressionIntersection(Intersection x) {
		suspendExpression(x);
		return evaluator.visitExpressionIntersection(x);
	}

	@Override
	public T visitExpressionIsDefined(IsDefined x) {
		suspendExpression(x);
		return evaluator.visitExpressionIsDefined(x);
	}

	@Override
	public T visitExpressionJoin(Join x) {
		suspendExpression(x);
		return evaluator.visitExpressionJoin(x);
	}

	@Override
	public T visitExpressionLessThan(LessThan x) {
		suspendExpression(x);
		return evaluator.visitExpressionLessThan(x);
	}

	@Override
	public T visitExpressionLessThanOrEq(LessThanOrEq x) {
		suspendExpression(x);
		return evaluator.visitExpressionLessThanOrEq(x);
	}

	@Override
	public T visitExpressionLexical(Lexical x) {
		suspendExpression(x);
		return evaluator.visitExpressionLexical(x);
	}

	@Override
	public T visitExpressionList(List x) {
		suspendExpression(x);
		return evaluator.visitExpressionList(x);
	}

	@Override
	public T visitExpressionLiteral(Literal x) {
		suspendExpression(x);
		return evaluator.visitExpressionLiteral(x);
	}

	@Override
	public T visitExpressionMap(Map x) {
		suspendExpression(x);
		return evaluator.visitExpressionMap(x);
	}

	@Override
	public T visitExpressionMatch(Match x) {
		suspendExpression(x);
		return evaluator.visitExpressionMatch(x);
	}

	@Override
	public T visitExpressionModulo(Modulo x) {
		suspendExpression(x);
		return evaluator.visitExpressionModulo(x);
	}

	@Override
	public T visitExpressionMultiVariable(MultiVariable x) {
		suspendExpression(x);
		return evaluator.visitExpressionMultiVariable(x);
	}

	@Override
	public T visitExpressionNegation(Negation x) {
		suspendExpression(x);
		return evaluator.visitExpressionNegation(x);
	}

	@Override
	public T visitExpressionNegative(Negative x) {
		suspendExpression(x);
		return evaluator.visitExpressionNegative(x);
	}

	@Override
	public T visitExpressionNoMatch(NoMatch x) {
		suspendExpression(x);
		return evaluator.visitExpressionNoMatch(x);
	}

	@Override
	public T visitExpressionNonEquals(NonEquals x) {
		suspendExpression(x);
		return evaluator.visitExpressionNonEquals(x);
	}

	@Override
	public T visitExpressionNotIn(NotIn x) {
		suspendExpression(x);
		return evaluator.visitExpressionNotIn(x);
	}

	@Override
	public T visitExpressionOr(Or x) {
		suspendExpression(x);
		return evaluator.visitExpressionOr(x);
	}

	@Override
	public T visitExpressionProduct(Product x) {
		suspendExpression(x);
		return evaluator.visitExpressionProduct(x);
	}

	@Override
	public T visitExpressionQualifiedName(QualifiedName x) {
		suspendExpression(x);
		return evaluator.visitExpressionQualifiedName(x);
	}

	@Override
	public T visitExpressionRange(Range x) {
		suspendExpression(x);
		return evaluator.visitExpressionRange(x);
	}

	@Override
	public T visitExpressionSet(Set x) {
		suspendExpression(x);
		return evaluator.visitExpressionSet(x);
	}

	@Override
	public T visitExpressionSetAnnotation(SetAnnotation x) {
		suspendExpression(x);
		return evaluator.visitExpressionSetAnnotation(x);
	}

	@Override
	public T visitExpressionStepRange(StepRange x) {
		suspendExpression(x);
		return evaluator.visitExpressionStepRange(x);
	}

	@Override
	public T visitExpressionSubscript(Subscript x) {
		suspendExpression(x);
		return evaluator.visitExpressionSubscript(x);
	}

	@Override
	public T visitExpressionSubtraction(Subtraction x) {
		suspendExpression(x);
		return evaluator.visitExpressionSubtraction(x);
	}

	@Override
	public T visitExpressionTransitiveClosure(TransitiveClosure x) {
		suspendExpression(x);
		return evaluator.visitExpressionTransitiveClosure(x);
	}

	@Override
	public T visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		suspendExpression(x);
		return evaluator.visitExpressionTransitiveReflexiveClosure(x);
	}


	@Override
	public T visitExpressionTuple(Tuple x) {
		suspendExpression(x);
		return evaluator.visitExpressionTuple(x);
	}

	@Override
	public T visitExpressionTypedVariable(TypedVariable x) {
		suspendExpression(x);
		return evaluator.visitExpressionTypedVariable(x);
	}

	@Override
	public T visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		suspendExpression(x);
		return evaluator.visitExpressionTypedVariableBecomes(x);
	}

	@Override
	public T visitExpressionVariableBecomes(VariableBecomes x) {
		suspendExpression(x);
		return evaluator.visitExpressionVariableBecomes(x);
	}

	@Override
	public T visitStatementVisit(org.meta_environment.rascal.ast.Statement.Visit x) {
		suspendStatement(x);
		return evaluator.visitStatementVisit(x);
	}

	@Override
	public T visitExpressionVoidClosure(VoidClosure x) {
		suspendExpression(x);
		return evaluator.visitExpressionVoidClosure(x);
	}

	@Override
	public T visitStatementAmbiguity(
			org.meta_environment.rascal.ast.Statement.Ambiguity x) {
		suspendStatement(x);
		return evaluator.visitStatementAmbiguity(x);
	}

	@Override
	public T visitStatementAssert(Assert x) {
		suspendStatement(x);
		return evaluator.visitStatementAssert(x);
	}

	@Override
	public T visitStatementAssertWithMessage(AssertWithMessage x) {
		suspendStatement(x);
		return evaluator.visitStatementAssertWithMessage(x);
	}

	@Override
	public T visitStatementAssignment(Assignment x) {
		suspendStatement(x);
		return evaluator.visitStatementAssignment(x);
	}
	@Override
	public T visitStatementNonEmptyBlock(Statement.NonEmptyBlock x) {
		/* no need to supend on a block */
		//suspendStatement(x);
		return evaluator.visitStatementNonEmptyBlock(x);
	}
	@Override
	public T visitStatementBreak(Break x) {
		suspendStatement(x);
		return evaluator.visitStatementBreak(x);
	}

	@Override
	public T visitStatementContinue(Continue x) {
		suspendStatement(x);
		return evaluator.visitStatementContinue(x);
	}

	@Override
	public T visitStatementDoWhile(DoWhile x) {
		suspendStatement(x);
		return evaluator.visitStatementDoWhile(x);
	}

	@Override
	public T visitStatementEmptyStatement(EmptyStatement x) {
		suspendStatement(x);
		return evaluator.visitStatementEmptyStatement(x);
	}

	@Override
	public T visitStatementExpression(Expression x) {
		//do not need to call suspendStatement if expressionMode is enabled
		if (! expressionStepModeEnabled()) {
			suspendStatement(x);
		}
		return evaluator.visitStatementExpression(x);
	}

	@Override
	public T visitStatementFail(Fail x) {
		suspendStatement(x);
		return evaluator.visitStatementFail(x);
	}

	@Override
	public T visitStatementFor(For x) {
		suspendStatement(x);
		return evaluator.visitStatementFor(x);
	}

	@Override
	public T visitStatementFunctionDeclaration(
			FunctionDeclaration x) {
		suspendStatement(x);
		return evaluator.visitStatementFunctionDeclaration(x);
	}

	@Override
	public T visitStatementGlobalDirective(GlobalDirective x) {
		suspendStatement(x);
		return evaluator.visitStatementGlobalDirective(x);
	}

	@Override
	public T visitStatementIfThen(IfThen x) {
		suspendStatement(x);
		return evaluator.visitStatementIfThen(x);
	}

	@Override
	public T visitStatementIfThenElse(
			org.meta_environment.rascal.ast.Statement.IfThenElse x) {
		suspendStatement(x);
		return evaluator.visitStatementIfThenElse(x);
	}

	@Override
	public T visitStatementInsert(Insert x) {
		suspendStatement(x);
		return evaluator.visitStatementInsert(x);
	}

	@Override
	public T visitStatementReturn(Return x) {
		suspendStatement(x);
		return evaluator.visitStatementReturn(x);
	}

	@Override
	public T visitStatementSolve(Solve x) {
		suspendStatement(x);
		return evaluator.visitStatementSolve(x);
	}

	@Override
	public T visitStatementSwitch(Switch x) {
		suspendStatement(x);
		return evaluator.visitStatementSwitch(x);
	}

	@Override
	public T visitStatementThrow(Throw x) {
		suspendStatement(x);
		return evaluator.visitStatementThrow(x);
	}

	@Override
	public T visitStatementTry(Try x) {
		suspendStatement(x);
		return evaluator.visitStatementTry(x);
	}

	@Override
	public T visitStatementTryFinally(TryFinally x) {
		suspendStatement(x);
		return evaluator.visitStatementTryFinally(x);
	}

	@Override
	public T visitStatementVariableDeclaration(
			VariableDeclaration x) {
		suspendStatement(x);
		return evaluator.visitStatementVariableDeclaration(x);
	}

	@Override
	public T visitStatementWhile(While x) {
		suspendStatement(x);
		return evaluator.visitStatementWhile(x);
	}


	private void suspendExpression(org.meta_environment.rascal.ast.Expression x) {
		suspend(x, expressionStepMode);
	}

	private void suspendStatement(Statement x) {
		suspend(x, statementStepMode);
	}

	private void suspend(AbstractAST x, boolean mode) {
		if (debugger.isTerminated()) {
			//can happen when we evaluating a loop for example and the debugger is stopped
			throw new QuitException();
		}
		if(suspendRequest) {
			setCurrentAST(x);
			debugger.notifySuspend(DebugSuspendMode.CLIENT_REQUEST);
			suspendRequest = false;
		} else if (mode && debugger.isStepping()) {
			setCurrentAST(x);
			debugger.notifySuspend(DebugSuspendMode.STEP_END);
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
		// the evaluator will suspend itself at the next call of suspendStatement or suspend Expression
		suspendRequest = true;
	}

	public boolean expressionStepModeEnabled() {
		return expressionStepMode;
	}

	public void setExpressionStepMode(boolean value) {
		expressionStepMode = value;
	}

	public boolean statementStepModeEnabled() {
		return statementStepMode;
	}

	public void setStatementStepMode(boolean value) {
		statementStepMode = value;
	}

	public void setStepOver(boolean value) {
		stepOver = value;
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

	public java.lang.String getStackTrace() {
		return evaluator.getStackTrace();
	}

	public void pushEnv() {
		evaluator.pushEnv();		
	}

	public void runTests() {
		evaluator.runTests();
	}

	public void setCurrentEnvt(Environment environment) {
		evaluator.setCurrentEnvt(environment);
	}

	public void unwind(Environment old) {
		evaluator.unwind(old);
	}


	public void setCurrentAST(AbstractAST ast) {
		evaluator.setCurrentAST(ast);
	}


	public IValueFactory getIValueFactory() {
		return evaluator.getIValueFactory();
	}

}
