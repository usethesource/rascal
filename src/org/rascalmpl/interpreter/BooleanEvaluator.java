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

public class BooleanEvaluator extends NullASTVisitor<IBooleanResult> implements IEvaluator<IBooleanResult>{
	private final IEvaluatorContext ctx;
	private final TypeFactory tf = TypeFactory.getInstance();
	private final PatternEvaluator pe;

	public BooleanEvaluator(IEvaluatorContext ctx) {
		this.ctx = ctx;
		this.pe = new PatternEvaluator(ctx);
	}

	@Override
	public IBooleanResult visitExpressionLiteral(Literal x) {
		if (x.getLiteral().isBoolean()) {
			return new BasicBooleanResult(ctx, x);
		}
		throw new UnexpectedTypeError(tf.boolType(), x.accept(ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionCallOrTree(CallOrTree x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionList(List x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionSet(Set x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionTuple(Tuple x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionMap(Map x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionQualifiedName(QualifiedName x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionTypedVariable(TypedVariable x) {
		throw new UninitializedVariableError(x.toString(), x);
	}

	@Override
	public IBooleanResult visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		throw new SyntaxError(x.toString(), x.getLocation());
	}

	@Override
	public IMatchingResult visitExpressionVariableBecomes(VariableBecomes x) {
		throw new SyntaxError(x.toString(), x.getLocation());
	}

	@Override
	public IMatchingResult visitExpressionGuarded(Guarded x) {
		throw new SyntaxError(x.toString(), x.getLocation());
	}

	@Override
	public IMatchingResult visitExpressionAnti(Anti x) {
		throw new SyntaxError(x.toString(), x.getLocation());
	}

	@Override
	public IBooleanResult visitExpressionMultiVariable(MultiVariable x) {
		throw new SyntaxError(x.toString(), x.getLocation());
	}

	@Override
	public IMatchingResult visitExpressionDescendant(Descendant x) {
		throw new SyntaxError(x.toString(), x.getLocation());
	}

	@Override
	public IBooleanResult visitExpressionAddition(Addition x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionAll(All x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionAmbiguity(
			org.rascalmpl.ast.Expression.Ambiguity x) {
		throw new ImplementationError("Ambiguity in expression: " + x);
	}

	@Override
	public IBooleanResult visitExpressionAnd(And x) {
		return new AndResult(ctx, x.getLhs().accept(this), x.getRhs()
				.accept(this));
	}

	@Override
	public IBooleanResult visitExpressionAny(Any x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionBracket(Bracket x) {
		return x.getExpression().accept(this);
	}

	@Override
	public IBooleanResult visitExpressionClosure(Closure x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionComposition(Composition x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionComprehension(Comprehension x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionDivision(
			org.rascalmpl.ast.Expression.Division x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionEquals(Equals x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionEquivalence(Equivalence x) {
		return new EquivalenceResult(ctx, x.getLhs().accept(this), x.getRhs().accept(this));
	}

	@Override
	public IBooleanResult visitExpressionFieldAccess(
			org.rascalmpl.ast.Expression.FieldAccess x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionFieldProject(FieldProject x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionFieldUpdate(FieldUpdate x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionGetAnnotation(GetAnnotation x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionGreaterThan(GreaterThan x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionIfThenElse(IfThenElse x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionImplication(Implication x) {
		return new OrResult(ctx, new NotResult(ctx, x.getLhs().accept(
				this)), x.getRhs().accept(this));
	}

	@Override
	public IBooleanResult visitExpressionIn(In x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionIntersection(
			org.rascalmpl.ast.Expression.Intersection x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionLessThan(LessThan x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionLessThanOrEq(LessThanOrEq x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionMatch(Match x) {
		return new MatchResult(ctx, x.getPattern(), true, x.getExpression());
	}

	@Override
	public IBooleanResult visitExpressionModulo(Modulo x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionNegation(Negation x) {
		return new NotResult(ctx, x.getArgument().accept(this));
	}

	@Override
	public IBooleanResult visitExpressionIsDefined(IsDefined x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionNegative(Negative x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionNoMatch(NoMatch x) {
		return new MatchResult(ctx, x.getPattern(), false, x.getExpression());
	}

//	@Override
//	public IBooleanResult visitExpressionNonEmptyBlock(NonEmptyBlock x) {
//		throw new UnexpectedTypeError(tf.boolType(), x.accept(
//				ctx.getEvaluator()).getType(), x);
//	}

	@Override
	public IBooleanResult visitExpressionNonEquals(NonEquals x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionNotIn(NotIn x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionOr(Or x) {
		return new OrResult(ctx, x.getLhs().accept(this), x.getRhs()
				.accept(this));
	}

	@Override
	public IBooleanResult visitExpressionProduct(
			org.rascalmpl.ast.Expression.Product x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionRange(Range x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionSetAnnotation(SetAnnotation x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionStepRange(StepRange x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionSubscript(
			org.rascalmpl.ast.Expression.Subscript x) {
		return new BasicBooleanResult(ctx, x);
	}

	@Override
	public IBooleanResult visitExpressionSubtraction(
			org.rascalmpl.ast.Expression.Subtraction x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionTransitiveClosure(TransitiveClosure x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);}

	@Override
	public IBooleanResult visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	@Override
	public IBooleanResult visitExpressionEnumerator(Enumerator x) {
		return new EnumeratorResult(ctx, x.getPattern().accept(pe), x.getExpression());
	}

	
//	@Override
//	public IBooleanResult visitExpressionVisit(Visit x) {
//		return new BasicBooleanResult(ctx, x);
//	}

	@Override
	public IBooleanResult visitExpressionVoidClosure(VoidClosure x) {
		throw new UnexpectedTypeError(tf.boolType(), x.accept(
				ctx.getEvaluator()).getType(), x);
	}

	public AbstractAST getCurrentAST() {
		return ctx.getCurrentAST();
	}

	public Environment getCurrentEnvt() {
		return ctx.getCurrentEnvt();
	}

	public Evaluator getEvaluator() {
		return ctx.getEvaluator();
	}

	public GlobalEnvironment getHeap() {
		return ctx.getHeap();
	}

	public java.lang.String getStackTrace() {
		return ctx.getStackTrace();
	}

	public void pushEnv() {
		ctx.pushEnv();		
	}

	public boolean runTests() {
		return ctx.runTests();
	}

	public void setCurrentEnvt(Environment environment) {
		ctx.setCurrentEnvt(environment);
	}

	public void unwind(Environment old) {
		ctx.unwind(old);
	}

	public void setCurrentAST(AbstractAST ast) {
		ctx.setCurrentAST(ast);		
	}

	public IValueFactory getValueFactory() {
		return ctx.getValueFactory();
	}

	public IStrategyContext getStrategyContext() {
		return ctx.getStrategyContext();
	}

	public void pushStrategyContext(IStrategyContext strategyContext) {
		ctx.pushStrategyContext(strategyContext);
	}

	public void popStrategyContext() {
		ctx.popStrategyContext();
	}

	public Stack<Accumulator> getAccumulators() {
		return ctx.getAccumulators();
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		ctx.setAccumulators(accumulators);
	}

	public IValue call(String name, IValue... args) {
		throw new ImplementationError("should not call call");
	}

	public URIResolverRegistry getResolverRegistry() {
		return ctx.getResolverRegistry();
	}

	public void interrupt() {
		// TODO Auto-generated method stub
		
	}

	public boolean isInterrupted() {
		return false;
	}

}
