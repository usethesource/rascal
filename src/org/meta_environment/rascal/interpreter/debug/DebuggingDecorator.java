package org.meta_environment.rascal.interpreter.debug;

import java.io.IOException;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
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
import org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy;
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
import org.meta_environment.rascal.ast.Expression.Location;
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
import org.meta_environment.rascal.ast.Expression.OperatorAsValue;
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
import org.meta_environment.rascal.ast.Expression.Visit;
import org.meta_environment.rascal.ast.Expression.VoidClosure;
import org.meta_environment.rascal.ast.Statement.Assert;
import org.meta_environment.rascal.ast.Statement.AssertWithMessage;
import org.meta_environment.rascal.ast.Statement.Assignment;
import org.meta_environment.rascal.ast.Statement.Block;
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
import org.meta_environment.rascal.interpreter.IDebugger;
import org.meta_environment.rascal.interpreter.control_exceptions.QuitException;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.parser.ConsoleParser;

public class DebuggingDecorator extends NullASTVisitor<Result<IValue>> {


	private final IDebugger debugger;
	private final ConsoleParser parser;
	private final NullASTVisitor<Result<IValue>> visitor;
	private boolean suspendRequest;
	private boolean statementStepMode;
	private boolean expressionStepMode;
	private boolean stepOver;

	public DebuggingDecorator(NullASTVisitor<Result<IValue>> visitor,
			ConsoleParser consoleParser,
			IDebugger debugger) {
		super();
		this.parser = consoleParser;
		this.visitor = visitor;
		this.debugger = debugger;
	}


	@Override
	public Result<IValue> visitExpressionAnti(Anti x) {
		suspendExpression(x);
		return visitor.visitExpressionAnti(x);
	}

	@Override
	public Result<IValue> visitExpressionAddition(Addition x) {
		suspendExpression(x);
		return visitor.visitExpressionAddition(x);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<IValue> visitExpressionAll(All x) {
		suspendExpression(x);
		return visitor.visitExpressionAll(x);
	}

	@Override
	public Result<IValue> visitExpressionAmbiguity(Ambiguity x) {
		suspendExpression(x);
		return visitor.visitExpressionAmbiguity(x);
	}

	@Override
	public Result<IValue> visitExpressionAnd(And x) {
		suspendExpression(x);
		return visitor.visitExpressionAnd(x);
	}


	@SuppressWarnings("unchecked")
	@Override
	public Result<IValue> visitExpressionAny(Any x) {
		suspendExpression(x);
		return visitor.visitExpressionAny(x);
	}

	@Override
	public Result<IValue> visitExpressionBracket(Bracket x) {
		suspendExpression(x);
		return visitor.visitExpressionBracket(x);
	}

	@Override
	public Result<IValue> visitExpressionCallOrTree(CallOrTree x) {
		suspendExpression(x);
		if (stepOver) {
			/* desactivate the stepping mode when evaluating the call */
			boolean oldStatementStepMode = statementStepMode;
			boolean oldExpressionStepMode = expressionStepMode;
			setStatementStepMode(false);
			setExpressionStepMode(false);
			Result<IValue> res = visitor.visitExpressionCallOrTree(x);
			setStatementStepMode(oldStatementStepMode);
			setExpressionStepMode(oldExpressionStepMode);
			return res;
		} else {
			return visitor.visitExpressionCallOrTree(x);
		} 
	}
	@Override
	public Result<IValue> visitExpressionClosure(Closure x) {
		suspendExpression(x);
		return visitor.visitExpressionClosure(x);
	}

	@Override
	public Result<IValue> visitExpressionComposition(Composition x) {
		suspendExpression(x);
		return visitor.visitExpressionComposition(x);
	}

	@Override
	public Result<IValue> visitExpressionComprehension(Comprehension x) {
		suspendExpression(x);
		return visitor.visitExpressionComprehension(x);
	}

	@Override
	public Result<IValue> visitExpressionDescendant(Descendant x) {
		suspendExpression(x);
		return visitor.visitExpressionDescendant(x);
	}


	@Override
	public Result<IValue> visitExpressionDivision(Division x) {
		suspendExpression(x);
		return visitor.visitExpressionDivision(x);
	}

	@Override
	public Result<IValue> visitExpressionEnumerator(Enumerator x) {
		suspendExpression(x);
		return visitor.visitExpressionEnumerator(x);
	}

	@Override
	public Result<IValue> visitExpressionEnumeratorWithStrategy(
			EnumeratorWithStrategy x) {
		suspendExpression(x);
		return visitor.visitExpressionEnumeratorWithStrategy(x);
	}

	@Override
	public Result<IValue> visitExpressionEquals(Equals x) {
		suspendExpression(x);
		return visitor.visitExpressionEquals(x);
	}

	@Override
	public Result<IValue> visitExpressionEquivalence(Equivalence x) {
		suspendExpression(x);
		return visitor.visitExpressionEquivalence(x);
	}

	@Override
	public Result<IValue> visitExpressionFieldAccess(FieldAccess x) {
		suspendExpression(x);
		return visitor.visitExpressionFieldAccess(x);
	}

	@Override
	public Result<IValue> visitExpressionFieldProject(FieldProject x) {
		suspendExpression(x);
		return visitor.visitExpressionFieldProject(x);
	}

	@Override
	public Result<IValue> visitExpressionFieldUpdate(FieldUpdate x) {
		suspendExpression(x);
		return visitor.visitExpressionFieldUpdate(x);
	}

	@Override
	public Result<IValue> visitExpressionGetAnnotation(GetAnnotation x) {
		suspendExpression(x);
		return visitor.visitExpressionGetAnnotation(x);
	}


	@Override
	public Result<IValue> visitExpressionGreaterThan(GreaterThan x) {
		suspendExpression(x);
		return visitor.visitExpressionGreaterThan(x);
	}

	@Override
	public Result<IValue> visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		suspendExpression(x);
		return visitor.visitExpressionGreaterThanOrEq(x);
	}


	@Override
	public Result<IValue> visitExpressionGuarded(Guarded x) {
		suspendExpression(x);
		return visitor.visitExpressionGuarded(x);
	}

	@Override
	public Result<IValue> visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		suspendExpression(x);
		return visitor.visitExpressionIfDefinedOtherwise(x);
	}




	@Override
	public Result<IValue> visitExpressionIfThenElse(IfThenElse x) {
		suspendExpression(x);
		return visitor.visitExpressionIfThenElse(x);
	}


	@Override
	public Result<IValue> visitExpressionImplication(Implication x) {
		suspendExpression(x);
		return visitor.visitExpressionImplication(x);
	}

	@Override
	public Result<IValue> visitExpressionIn(In x) {
		suspendExpression(x);
		return visitor.visitExpressionIn(x);
	}

	@Override
	public Result<IValue> visitExpressionIntersection(Intersection x) {
		suspendExpression(x);
		return visitor.visitExpressionIntersection(x);
	}

	@Override
	public Result<IValue> visitExpressionIsDefined(IsDefined x) {
		suspendExpression(x);
		return visitor.visitExpressionIsDefined(x);
	}

	@Override
	public Result<IValue> visitExpressionJoin(Join x) {
		suspendExpression(x);
		return visitor.visitExpressionJoin(x);
	}

	@Override
	public Result<IValue> visitExpressionLessThan(LessThan x) {
		suspendExpression(x);
		return visitor.visitExpressionLessThan(x);
	}

	@Override
	public Result<IValue> visitExpressionLessThanOrEq(LessThanOrEq x) {
		suspendExpression(x);
		return visitor.visitExpressionLessThanOrEq(x);
	}

	@Override
	public Result<IValue> visitExpressionLexical(Lexical x) {
		suspendExpression(x);
		return visitor.visitExpressionLexical(x);
	}

	@Override
	public Result<IValue> visitExpressionList(List x) {
		suspendExpression(x);
		return visitor.visitExpressionList(x);
	}

	@Override
	public Result<IValue> visitExpressionLiteral(Literal x) {
		suspendExpression(x);
		return visitor.visitExpressionLiteral(x);
	}

	@Override
	public Result<IValue> visitExpressionLocation(Location x) {
		suspendExpression(x);
		return visitor.visitExpressionLocation(x);
	}

	@Override
	public Result<IValue> visitExpressionMap(Map x) {
		suspendExpression(x);
		return visitor.visitExpressionMap(x);
	}

	@Override
	public Result<IValue> visitExpressionMatch(Match x) {
		suspendExpression(x);
		return visitor.visitExpressionMatch(x);
	}

	@Override
	public Result<IValue> visitExpressionModulo(Modulo x) {
		suspendExpression(x);
		return visitor.visitExpressionModulo(x);
	}

	@Override
	public Result<IValue> visitExpressionMultiVariable(MultiVariable x) {
		suspendExpression(x);
		return visitor.visitExpressionMultiVariable(x);
	}

	@Override
	public Result<IValue> visitExpressionNegation(Negation x) {
		suspendExpression(x);
		return visitor.visitExpressionNegation(x);
	}

	@Override
	public Result<IValue> visitExpressionNegative(Negative x) {
		suspendExpression(x);
		return visitor.visitExpressionNegative(x);
	}

	@Override
	public Result<IValue> visitExpressionNoMatch(NoMatch x) {
		suspendExpression(x);
		return visitor.visitExpressionNoMatch(x);
	}

	@Override
	public Result<IValue> visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		suspendExpression(x);
		return visitor.visitExpressionNonEmptyBlock(x);
	}

	@Override
	public Result<IValue> visitExpressionNonEquals(NonEquals x) {
		suspendExpression(x);
		return visitor.visitExpressionNonEquals(x);
	}

	@Override
	public Result<IValue> visitExpressionNotIn(NotIn x) {
		suspendExpression(x);
		return visitor.visitExpressionNotIn(x);
	}

	@Override
	public Result<IValue> visitExpressionOperatorAsValue(OperatorAsValue x) {
		suspendExpression(x);
		return visitor.visitExpressionOperatorAsValue(x);
	}
	@Override
	public Result<IValue> visitExpressionOr(Or x) {
		suspendExpression(x);
		return visitor.visitExpressionOr(x);
	}

	@Override
	public Result<IValue> visitExpressionProduct(Product x) {
		suspendExpression(x);
		return visitor.visitExpressionProduct(x);
	}

	@Override
	public Result<IValue> visitExpressionQualifiedName(QualifiedName x) {
		suspendExpression(x);
		return visitor.visitExpressionQualifiedName(x);
	}

	@Override
	public Result<IValue> visitExpressionRange(Range x) {
		suspendExpression(x);
		return visitor.visitExpressionRange(x);
	}

	@Override
	public Result<IValue> visitExpressionSet(Set x) {
		suspendExpression(x);
		return visitor.visitExpressionSet(x);
	}

	@Override
	public Result<IValue> visitExpressionSetAnnotation(SetAnnotation x) {
		suspendExpression(x);
		return visitor.visitExpressionSetAnnotation(x);
	}

	@Override
	public Result<IValue> visitExpressionStepRange(StepRange x) {
		suspendExpression(x);
		return visitor.visitExpressionStepRange(x);
	}

	@Override
	public Result<IValue> visitExpressionSubscript(Subscript x) {
		suspendExpression(x);
		return visitor.visitExpressionSubscript(x);
	}

	@Override
	public Result<IValue> visitExpressionSubtraction(Subtraction x) {
		suspendExpression(x);
		return visitor.visitExpressionSubtraction(x);
	}

	@Override
	public Result<IValue> visitExpressionTransitiveClosure(TransitiveClosure x) {
		suspendExpression(x);
		return visitor.visitExpressionTransitiveClosure(x);
	}

	@Override
	public Result<IValue> visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		suspendExpression(x);
		return visitor.visitExpressionTransitiveReflexiveClosure(x);
	}


	@Override
	public Result<IValue> visitExpressionTuple(Tuple x) {
		suspendExpression(x);
		return visitor.visitExpressionTuple(x);
	}

	@Override
	public Result<IValue> visitExpressionTypedVariable(TypedVariable x) {
		suspendExpression(x);
		return visitor.visitExpressionTypedVariable(x);
	}

	@Override
	public Result<IValue> visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		suspendExpression(x);
		return visitor.visitExpressionTypedVariableBecomes(x);
	}

	@Override
	public Result<IValue> visitExpressionVariableBecomes(VariableBecomes x) {
		suspendExpression(x);
		return visitor.visitExpressionVariableBecomes(x);
	}

	@Override
	public Result<IValue> visitExpressionVisit(Visit x) {
		suspendExpression(x);
		return visitor.visitExpressionVisit(x);
	}

	@Override
	public Result<IValue> visitExpressionVoidClosure(VoidClosure x) {
		suspendExpression(x);
		return visitor.visitExpressionVoidClosure(x);
	}

	@Override
	public Result<IValue> visitStatementAmbiguity(
			org.meta_environment.rascal.ast.Statement.Ambiguity x) {
		suspendStatement(x);
		return visitor.visitStatementAmbiguity(x);
	}

	@Override
	public Result<IValue> visitStatementAssert(Assert x) {
		suspendStatement(x);
		return visitor.visitStatementAssert(x);
	}

	@Override
	public Result<IValue> visitStatementAssertWithMessage(AssertWithMessage x) {
		suspendStatement(x);
		return visitor.visitStatementAssertWithMessage(x);
	}

	@Override
	public Result<IValue> visitStatementAssignment(Assignment x) {
		suspendStatement(x);
		return visitor.visitStatementAssignment(x);
	}
	@Override
	public Result<IValue> visitStatementBlock(Block x) {
		/* no need to supend on a block */
		//suspendStatement(x);
		return visitor.visitStatementBlock(x);
	}
	@Override
	public Result<IValue> visitStatementBreak(Break x) {
		suspendStatement(x);
		return visitor.visitStatementBreak(x);
	}

	@Override
	public Result<IValue> visitStatementContinue(Continue x) {
		suspendStatement(x);
		return visitor.visitStatementContinue(x);
	}

	@Override
	public Result<IValue> visitStatementDoWhile(DoWhile x) {
		suspendStatement(x);
		return visitor.visitStatementDoWhile(x);
	}

	@Override
	public Result<IValue> visitStatementEmptyStatement(EmptyStatement x) {
		suspendStatement(x);
		return visitor.visitStatementEmptyStatement(x);
	}

	@Override
	public Result<IValue> visitStatementExpression(Expression x) {
		//do not need to call suspendStatement if expressionMode is enabled
		if (! expressionStepModeEnabled()) {
			suspendStatement(x);
		}
		return visitor.visitStatementExpression(x);
	}

	@Override
	public Result<IValue> visitStatementFail(Fail x) {
		suspendStatement(x);
		return visitor.visitStatementFail(x);
	}

	@Override
	public Result<IValue> visitStatementFor(For x) {
		suspendStatement(x);
		return visitor.visitStatementFor(x);
	}

	@Override
	public Result<IValue> visitStatementFunctionDeclaration(
			FunctionDeclaration x) {
		suspendStatement(x);
		return visitor.visitStatementFunctionDeclaration(x);
	}

	@Override
	public Result<IValue> visitStatementGlobalDirective(GlobalDirective x) {
		suspendStatement(x);
		return visitor.visitStatementGlobalDirective(x);
	}

	@Override
	public Result<IValue> visitStatementIfThen(IfThen x) {
		suspendStatement(x);
		return visitor.visitStatementIfThen(x);
	}

	@Override
	public Result<IValue> visitStatementIfThenElse(
			org.meta_environment.rascal.ast.Statement.IfThenElse x) {
		suspendStatement(x);
		return visitor.visitStatementIfThenElse(x);
	}

	@Override
	public Result<IValue> visitStatementInsert(Insert x) {
		suspendStatement(x);
		return visitor.visitStatementInsert(x);
	}

	@Override
	public Result<IValue> visitStatementReturn(Return x) {
		suspendStatement(x);
		return visitor.visitStatementReturn(x);
	}

	@Override
	public Result<IValue> visitStatementSolve(Solve x) {
		suspendStatement(x);
		return visitor.visitStatementSolve(x);
	}

	@Override
	public Result<IValue> visitStatementSwitch(Switch x) {
		suspendStatement(x);
		return visitor.visitStatementSwitch(x);
	}

	@Override
	public Result<IValue> visitStatementThrow(Throw x) {
		suspendStatement(x);
		return visitor.visitStatementThrow(x);
	}

	@Override
	public Result<IValue> visitStatementTry(Try x) {
		suspendStatement(x);
		return visitor.visitStatementTry(x);
	}

	@Override
	public Result<IValue> visitStatementTryFinally(TryFinally x) {
		suspendStatement(x);
		return visitor.visitStatementTryFinally(x);
	}

	@Override
	public Result<IValue> visitStatementVariableDeclaration(
			VariableDeclaration x) {
		suspendStatement(x);
		return visitor.visitStatementVariableDeclaration(x);
	}

	@Override
	public Result<IValue> visitStatementVisit(
			org.meta_environment.rascal.ast.Statement.Visit x) {
		suspendStatement(x);
		return visitor.visitStatementVisit(x);
	}

	@Override
	public Result<IValue> visitStatementWhile(While x) {
		suspendStatement(x);
		return visitor.visitStatementWhile(x);
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

	private AbstractAST getCurrentAST() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setCurrentAST(AbstractAST x) {
		// TODO Auto-generated method stub

	}


}
