package org.rascalmpl.interpreter.debug;

import java.io.PrintWriter;
import java.net.URI;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.rascalmpl.ast.AbstractAST;
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
import org.rascalmpl.ast.Expression.Guarded;
import org.rascalmpl.ast.Expression.IfDefinedOtherwise;
import org.rascalmpl.ast.Expression.IfThenElse;
import org.rascalmpl.ast.Expression.Implication;
import org.rascalmpl.ast.Expression.In;
import org.rascalmpl.ast.Expression.Intersection;
import org.rascalmpl.ast.Expression.IsDefined;
import org.rascalmpl.ast.Expression.Join;
import org.rascalmpl.ast.Expression.LessThan;
import org.rascalmpl.ast.Expression.LessThanOrEq;
import org.rascalmpl.ast.Expression.Lexical;
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
import org.rascalmpl.interpreter.Evaluator;
import org.rascalmpl.interpreter.control_exceptions.QuitException;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.env.ModuleEnvironment;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.parser.LegacyRascalParser;

public class DebuggableEvaluator extends Evaluator {
	protected final IDebugger debugger;

	//when there is a suspend request from the debugger (caused by the pause button)
	private boolean suspendRequest;

	private DebugStepMode stepMode = DebugStepMode.NO_STEP;

	public DebuggableEvaluator(IValueFactory vf, PrintWriter stderr, PrintWriter stdout,
			ModuleEnvironment moduleEnvironment, IDebugger debugger) {
		super(vf, stderr, stdout, new LegacyRascalParser(), moduleEnvironment, new GlobalEnvironment());
		this.patternEvaluator = new DebuggingDecorator<IMatchingResult>(patternEvaluator, debugger);
		this.debugger = debugger;
	}

	@Override
	public Result<IValue> visitExpressionAnti(Anti x) {
		suspend(x);
		return super.visitExpressionAnti(x);
	}

	@Override
	public Result<IValue> visitExpressionAddition(Addition x) {
		suspend(x);
		return super.visitExpressionAddition(x);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Result<IValue> visitExpressionAll(All x) {
		suspend(x);
		return super.visitExpressionAll(x);
	}

	@Override
	public Result<IValue> visitExpressionAmbiguity(Ambiguity x) {
		suspend(x);
		return super.visitExpressionAmbiguity(x);
	}

	@Override
	public Result<IValue> visitExpressionAnd(And x) {
		suspend(x);
		return super.visitExpressionAnd(x);
	}


	@SuppressWarnings("unchecked")
	@Override
	public Result<IValue> visitExpressionAny(Any x) {
		suspend(x);
		return super.visitExpressionAny(x);
	}

	@Override
	public Result<IValue> visitExpressionBracket(Bracket x) {
		suspend(x);
		return super.visitExpressionBracket(x);
	}

	@Override
	public Result<IValue> visitExpressionCallOrTree(CallOrTree x) {
		suspend(x);
		if (stepMode.equals(DebugStepMode.STEP_OVER)) {
			/* desactivate the stepping mode when evaluating the call */
			setStepMode(DebugStepMode.NO_STEP);
			Result<IValue> res = super.visitExpressionCallOrTree(x);
			setStepMode(DebugStepMode.STEP_OVER);
			// suspend when returning to the calling statement
			suspend(x);
			return res;
		}
		
		Result<IValue> res = super.visitExpressionCallOrTree(x);
		// suspend when returning to the calling statement
		suspend(x);
		return res;
	}
	@Override
	public Result<IValue> visitExpressionClosure(Closure x) {
		suspend(x);
		return super.visitExpressionClosure(x);
	}

	@Override
	public Result<IValue> visitExpressionComposition(Composition x) {
		suspend(x);
		return super.visitExpressionComposition(x);
	}

	@Override
	public Result<IValue> visitExpressionReifiedType(ReifiedType x) {
		suspend(x);
		return super.visitExpressionReifiedType(x);
	}
	
	@Override
	public Result<IValue> visitExpressionReifyType(ReifyType x) {
		suspend(x);
		return super.visitExpressionReifyType(x);
	}
	
	@Override
	public Result<IValue> visitExpressionComprehension(Comprehension x) {
		suspend(x);
		return super.visitExpressionComprehension(x);
	}

	@Override
	public Result<IValue> visitExpressionDescendant(Descendant x) {
		suspend(x);
		return super.visitExpressionDescendant(x);
	}


	@Override
	public Result<IValue> visitExpressionDivision(Division x) {
		suspend(x);
		return super.visitExpressionDivision(x);
	}

	@Override
	public Result<IValue> visitExpressionEnumerator(Enumerator x) {
		suspend(x);
		return super.visitExpressionEnumerator(x);
	}

//	@Override
//	public Result<IValue> visitExpressionEnumeratorWithStrategy(
//			EnumeratorWithStrategy x) {
//		suspend(x);
//		return super.visitExpressionEnumeratorWithStrategy(x);
//	}

	@Override
	public Result<IValue> visitExpressionEquals(Equals x) {
		suspend(x);
		return super.visitExpressionEquals(x);
	}

	@Override
	public Result<IValue> visitExpressionEquivalence(Equivalence x) {
		suspend(x);
		return super.visitExpressionEquivalence(x);
	}

	@Override
	public Result<IValue> visitExpressionFieldAccess(FieldAccess x) {
		suspend(x);
		return super.visitExpressionFieldAccess(x);
	}

	@Override
	public Result<IValue> visitExpressionFieldProject(FieldProject x) {
		suspend(x);
		return super.visitExpressionFieldProject(x);
	}

	@Override
	public Result<IValue> visitExpressionFieldUpdate(FieldUpdate x) {
		suspend(x);
		return super.visitExpressionFieldUpdate(x);
	}

	@Override
	public Result<IValue> visitExpressionGetAnnotation(GetAnnotation x) {
		suspend(x);
		return super.visitExpressionGetAnnotation(x);
	}


	@Override
	public Result<IValue> visitExpressionGreaterThan(GreaterThan x) {
		suspend(x);
		return super.visitExpressionGreaterThan(x);
	}

	@Override
	public Result<IValue> visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		suspend(x);
		return super.visitExpressionGreaterThanOrEq(x);
	}


	@Override
	public Result<IValue> visitExpressionGuarded(Guarded x) {
		suspend(x);
		return super.visitExpressionGuarded(x);
	}

	@Override
	public Result<IValue> visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		suspend(x);
		return super.visitExpressionIfDefinedOtherwise(x);
	}




	@Override
	public Result<IValue> visitExpressionIfThenElse(IfThenElse x) {
		suspend(x);
		return super.visitExpressionIfThenElse(x);
	}


	@Override
	public Result<IValue> visitExpressionImplication(Implication x) {
		suspend(x);
		return super.visitExpressionImplication(x);
	}

	@Override
	public Result<IValue> visitExpressionIn(In x) {
		suspend(x);
		return super.visitExpressionIn(x);
	}

	@Override
	public Result<IValue> visitExpressionIntersection(Intersection x) {
		suspend(x);
		return super.visitExpressionIntersection(x);
	}

	@Override
	public Result<IValue> visitExpressionIsDefined(IsDefined x) {
		suspend(x);
		return super.visitExpressionIsDefined(x);
	}

	@Override
	public Result<IValue> visitExpressionJoin(Join x) {
		suspend(x);
		return super.visitExpressionJoin(x);
	}

	@Override
	public Result<IValue> visitExpressionLessThan(LessThan x) {
		suspend(x);
		return super.visitExpressionLessThan(x);
	}

	@Override
	public Result<IValue> visitExpressionLessThanOrEq(LessThanOrEq x) {
		suspend(x);
		return super.visitExpressionLessThanOrEq(x);
	}

	@Override
	public Result<IValue> visitExpressionLexical(Lexical x) {
		suspend(x);
		return super.visitExpressionLexical(x);
	}

	@Override
	public Result<IValue> visitExpressionList(List x) {
		suspend(x);
		return super.visitExpressionList(x);
	}

	@Override
	public Result<IValue> visitExpressionLiteral(Literal x) {
		suspend(x);
		return super.visitExpressionLiteral(x);
	}

	@Override
	public Result<IValue> visitExpressionMap(Map x) {
		suspend(x);
		return super.visitExpressionMap(x);
	}

	@Override
	public Result<IValue> visitExpressionMatch(Match x) {
		suspend(x);
		return super.visitExpressionMatch(x);
	}

	@Override
	public Result<IValue> visitExpressionModulo(Modulo x) {
		suspend(x);
		return super.visitExpressionModulo(x);
	}

	@Override
	public Result<IValue> visitExpressionMultiVariable(MultiVariable x) {
		suspend(x);
		return super.visitExpressionMultiVariable(x);
	}

	@Override
	public Result<IValue> visitExpressionNegation(Negation x) {
		suspend(x);
		return super.visitExpressionNegation(x);
	}

	@Override
	public Result<IValue> visitExpressionNegative(Negative x) {
		suspend(x);
		return super.visitExpressionNegative(x);
	}

	@Override
	public Result<IValue> visitExpressionNoMatch(NoMatch x) {
		suspend(x);
		return super.visitExpressionNoMatch(x);
	}

	@Override
	public Result<IValue> visitExpressionNonEquals(NonEquals x) {
		suspend(x);
		return super.visitExpressionNonEquals(x);
	}

	@Override
	public Result<IValue> visitExpressionNotIn(NotIn x) {
		suspend(x);
		return super.visitExpressionNotIn(x);
	}

	@Override
	public Result<IValue> visitExpressionOr(Or x) {
		suspend(x);
		return super.visitExpressionOr(x);
	}

	@Override
	public Result<IValue> visitExpressionProduct(Product x) {
		suspend(x);
		return super.visitExpressionProduct(x);
	}

	@Override
	public Result<IValue> visitExpressionQualifiedName(QualifiedName x) {
		suspend(x);
		return super.visitExpressionQualifiedName(x);
	}

	@Override
	public Result<IValue> visitExpressionRange(Range x) {
		suspend(x);
		return super.visitExpressionRange(x);
	}

	@Override
	public Result<IValue> visitExpressionSet(Set x) {
		suspend(x);
		return super.visitExpressionSet(x);
	}

	@Override
	public Result<IValue> visitExpressionSetAnnotation(SetAnnotation x) {
		suspend(x);
		return super.visitExpressionSetAnnotation(x);
	}

	@Override
	public Result<IValue> visitExpressionStepRange(StepRange x) {
		suspend(x);
		return super.visitExpressionStepRange(x);
	}

	@Override
	public Result<IValue> visitExpressionSubscript(Subscript x) {
		suspend(x);
		return super.visitExpressionSubscript(x);
	}

	@Override
	public Result<IValue> visitExpressionSubtraction(Subtraction x) {
		suspend(x);
		return super.visitExpressionSubtraction(x);
	}

	@Override
	public Result<IValue> visitExpressionTransitiveClosure(TransitiveClosure x) {
		suspend(x);
		return super.visitExpressionTransitiveClosure(x);
	}

	@Override
	public Result<IValue> visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		suspend(x);
		return super.visitExpressionTransitiveReflexiveClosure(x);
	}


	@Override
	public Result<IValue> visitExpressionTuple(Tuple x) {
		suspend(x);
		return super.visitExpressionTuple(x);
	}

	@Override
	public Result<IValue> visitExpressionTypedVariable(TypedVariable x) {
		suspend(x);
		return super.visitExpressionTypedVariable(x);
	}

	@Override
	public Result<IValue> visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		suspend(x);
		return super.visitExpressionTypedVariableBecomes(x);
	}

	@Override
	public Result<IValue> visitExpressionVariableBecomes(VariableBecomes x) {
		suspend(x);
		return super.visitExpressionVariableBecomes(x);
	}

	@Override
	public Result<IValue> visitExpressionVoidClosure(VoidClosure x) {
		suspend(x);
		return super.visitExpressionVoidClosure(x);
	}

	@Override
	public Result<IValue> visitStatementAmbiguity(
			org.rascalmpl.ast.Statement.Ambiguity x) {
		suspend(x);
		return super.visitStatementAmbiguity(x);
	}

	@Override
	public Result<IValue> visitStatementAssert(Assert x) {
		suspend(x);
		return super.visitStatementAssert(x);
	}

	@Override
	public Result<IValue> visitStatementAssertWithMessage(AssertWithMessage x) {
		suspend(x);
		return super.visitStatementAssertWithMessage(x);
	}

	@Override
	public Result<IValue> visitStatementAssignment(Assignment x) {
		suspend(x);
		return super.visitStatementAssignment(x);
	}
	
	@Override
	public Result<IValue> visitStatementNonEmptyBlock(Statement.NonEmptyBlock x) {
		/* no need to supend on a block */
		//suspend(x);
		return super.visitStatementNonEmptyBlock(x);
	}
	
	@Override
	public Result<IValue> visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		/* no need to supend on a block */
		//suspend(x);
		return super.visitExpressionNonEmptyBlock(x);
	}

	@Override
	public Result<IValue> visitStatementBreak(Break x) {
		suspend(x);
		return super.visitStatementBreak(x);
	}

	@Override
	public Result<IValue> visitStatementContinue(Continue x) {
		suspend(x);
		return super.visitStatementContinue(x);
	}

	@Override
	public Result<IValue> visitStatementDoWhile(DoWhile x) {
		suspend(x);
		return super.visitStatementDoWhile(x);
	}

	@Override
	public Result<IValue> visitStatementEmptyStatement(EmptyStatement x) {
		suspend(x);
		return super.visitStatementEmptyStatement(x);
	}

	@Override
	public Result<IValue> visitStatementExpression(Expression x) {
		/**		
		 //should avoid to stop twice when stepping into
		if (! stepMode.equals(DebugStepMode.STEP_INTO)) {
			suspend(x);
		}
		*/
		return super.visitStatementExpression(x);
	}

	@Override
	public Result<IValue> visitStatementFail(Fail x) {
		suspend(x);
		return super.visitStatementFail(x);
	}

	@Override
	public Result<IValue> visitStatementFor(For x) {
		suspend(x);
		return super.visitStatementFor(x);
	}

	@Override
	public Result<IValue> visitStatementFunctionDeclaration(
			FunctionDeclaration x) {
		suspend(x);
		return super.visitStatementFunctionDeclaration(x);
	}

	@Override
	public Result<IValue> visitStatementGlobalDirective(GlobalDirective x) {
		suspend(x);
		return super.visitStatementGlobalDirective(x);
	}

	@Override
	public Result<IValue> visitStatementIfThen(IfThen x) {
		suspend(x);
		return super.visitStatementIfThen(x);
	}

	@Override
	public Result<IValue> visitStatementIfThenElse(
			org.rascalmpl.ast.Statement.IfThenElse x) {
		suspend(x);
		return super.visitStatementIfThenElse(x);
	}

	@Override
	public Result<IValue> visitStatementInsert(Insert x) {
		suspend(x);
		return super.visitStatementInsert(x);
	}

	@Override
	public Result<IValue> visitStatementReturn(Return x) {
		suspend(x);
		return super.visitStatementReturn(x);
	}

	@Override
	public Result<IValue> visitStatementSolve(Solve x) {
		suspend(x);
		return super.visitStatementSolve(x);
	}

	@Override
	public Result<IValue> visitStatementSwitch(Switch x) {
		suspend(x);
		return super.visitStatementSwitch(x);
	}

	@Override
	public Result<IValue> visitStatementThrow(Throw x) {
		suspend(x);
		return super.visitStatementThrow(x);
	}

	@Override
	public Result<IValue> visitStatementTry(Try x) {
		suspend(x);
		return super.visitStatementTry(x);
	}

	@Override
	public Result<IValue> visitStatementTryFinally(TryFinally x) {
		suspend(x);
		return super.visitStatementTryFinally(x);
	}

	@Override
	public Result<IValue> visitStatementVariableDeclaration(
			VariableDeclaration x) {
		suspend(x);
		return super.visitStatementVariableDeclaration(x);
	}

	@Override
	public Result<IValue> visitStatementVisit(
			org.rascalmpl.ast.Statement.Visit x) {
		suspend(x);
		return super.visitStatementVisit(x);
	}

	@Override
	public Result<IValue> visitExpressionVisit(
			org.rascalmpl.ast.Expression.Visit x) {
		suspend(x);
		return super.visitExpressionVisit(x);
	}

	@Override
	public Result<IValue> visitStatementWhile(While x) {
		suspend(x);
		return super.visitStatementWhile(x);
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

	public IConstructor parseCommand(String command){
		return parseCommand(command, URI.create("debug:///"));
	}

}
