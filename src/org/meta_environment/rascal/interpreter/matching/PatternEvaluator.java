package org.meta_environment.rascal.interpreter.matching;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Expression.Addition;
import org.meta_environment.rascal.ast.Expression.All;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Anti;
import org.meta_environment.rascal.ast.Expression.Any;
import org.meta_environment.rascal.ast.Expression.Bracket;
import org.meta_environment.rascal.ast.Expression.CallOrTree;
import org.meta_environment.rascal.ast.Expression.Closure;
import org.meta_environment.rascal.ast.Expression.Composition;
import org.meta_environment.rascal.ast.Expression.Comprehension;
import org.meta_environment.rascal.ast.Expression.Descendant;
import org.meta_environment.rascal.ast.Expression.Enumerator;
import org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy;
import org.meta_environment.rascal.ast.Expression.Equals;
import org.meta_environment.rascal.ast.Expression.Equivalence;
import org.meta_environment.rascal.ast.Expression.FieldProject;
import org.meta_environment.rascal.ast.Expression.FieldUpdate;
import org.meta_environment.rascal.ast.Expression.GetAnnotation;
import org.meta_environment.rascal.ast.Expression.GreaterThan;
import org.meta_environment.rascal.ast.Expression.GreaterThanOrEq;
import org.meta_environment.rascal.ast.Expression.Guarded;
import org.meta_environment.rascal.ast.Expression.IfThenElse;
import org.meta_environment.rascal.ast.Expression.Implication;
import org.meta_environment.rascal.ast.Expression.In;
import org.meta_environment.rascal.ast.Expression.LessThan;
import org.meta_environment.rascal.ast.Expression.LessThanOrEq;
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
import org.meta_environment.rascal.ast.Expression.OperatorAsValue;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Range;
import org.meta_environment.rascal.ast.Expression.Set;
import org.meta_environment.rascal.ast.Expression.SetAnnotation;
import org.meta_environment.rascal.ast.Expression.StepRange;
import org.meta_environment.rascal.ast.Expression.TransitiveClosure;
import org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.Expression.TypedVariable;
import org.meta_environment.rascal.ast.Expression.TypedVariableBecomes;
import org.meta_environment.rascal.ast.Expression.VariableBecomes;
import org.meta_environment.rascal.ast.Expression.Visit;
import org.meta_environment.rascal.ast.Expression.VoidClosure;
import org.meta_environment.rascal.interpreter.EvaluatorContext;
import org.meta_environment.rascal.interpreter.Names;
import org.meta_environment.rascal.interpreter.TypeEvaluator;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.ConcreteSyntaxType;
import org.meta_environment.rascal.interpreter.env.Environment;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.AmbiguousConcretePattern;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedPatternError;

public class PatternEvaluator extends NullASTVisitor<AbstractPattern> {
	private IValueFactory vf;
	private Environment env;
	private EvaluatorContext ctx;
	private Environment scope;

	public PatternEvaluator(IValueFactory vf, Environment env, Environment scope, EvaluatorContext ctx){
		this.vf = vf;
		this.env = env;
		this.ctx = ctx;
		this.scope = scope;
	}
	
	public boolean isPattern(org.meta_environment.rascal.ast.Expression pat){
		return (pat.isLiteral() && ! pat.getLiteral().isRegExp()) || 
		       pat.isCallOrTree() || pat.isList() || 
		       pat.isSet() || pat.isMap() || pat.isTuple() ||
		       pat.isQualifiedName() || pat.isTypedVariable() ||
		       pat.isVariableBecomes() || pat.isTypedVariableBecomes() ||
		       pat.isAnti() || pat.isDescendant();
	}
	
	@Override
	public AbstractPattern visitExpressionLiteral(Literal x) {
		return new LiteralPattern(vf, ctx, x.getLiteral().accept(ctx.getEvaluator()).getValue());
	}
	
	private boolean isConcreteSyntaxAppl(CallOrTree tree){
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return Names.name(Names.lastName(tree.getExpression().getQualifiedName())).equals("appl");
	}
	
	private boolean isConcreteSyntaxAmb(CallOrTree tree){
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return Names.name(Names.lastName(tree.getExpression().getQualifiedName())).equals("amb");
	}
	
	private boolean isConcreteSyntaxList(CallOrTree tree){
		return isConcreteSyntaxAppl(tree) && isConcreteListProd((CallOrTree) tree.getArguments().get(0));
	}
	
	private boolean isConcreteListProd(CallOrTree prod){
		if (!prod.getExpression().isQualifiedName()) {
			return false;
		}
		return Names.name(Names.lastName(prod.getExpression().getQualifiedName())).equals("list");
	}
	
	@Override
	public AbstractPattern visitExpressionCallOrTree(CallOrTree x) {
	org.meta_environment.rascal.ast.QualifiedName N = x.getExpression().getQualifiedName();

	// TODO: allow binding and matching on the name of the constructor
	
		if(isConcreteSyntaxList(x)) {
			List args = (List)x.getArguments().get(1);
			// TODO what if somebody writes a variable in  the list production itself?
			return new ConcreteListPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), x,
					visitElements(args.getElements()));
		}
		if(isConcreteSyntaxAppl(x)){
			return new ConcreteApplicationPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), x, visitArguments(x));
		}
		if (isConcreteSyntaxAmb(x)) {
			throw new AmbiguousConcretePattern(x);
//			return new AbstractPatternConcreteAmb(vf, new EvaluatorContext(ctx.getEvaluator(), x), x, visitArguments(x));
		}
		
		return new NodePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), N, visitArguments(x));
	}
	
	private java.util.List<AbstractPattern> visitArguments(CallOrTree x){

		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x.getArguments();
		ArrayList<AbstractPattern> args = new java.util.ArrayList<AbstractPattern>(elements.size());
		
		int i = 0;
		for(org.meta_environment.rascal.ast.Expression e : elements){
			args.add(i++, e.accept(this));
		}
		return args;
	}
	
	
	private java.util.List<AbstractPattern> visitElements(java.util.List<org.meta_environment.rascal.ast.Expression> elements){
		ArrayList<AbstractPattern> args = new java.util.ArrayList<AbstractPattern>(elements.size());
		
		int i = 0;
		for(org.meta_environment.rascal.ast.Expression e : elements){
			args.add(i++, e.accept(this));
		}
		return args;
	}
	
	@Override
	public AbstractPattern visitExpressionList(List x) {
		return new ListPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionSet(Set x) {
		return new SetPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionTuple(Tuple x) {
		return new TuplePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public AbstractPattern visitExpressionMap(Map x) {
		throw new ImplementationError("Map in pattern not yet implemented");
	}
	
	@Override
	public AbstractPattern visitExpressionQualifiedName(QualifiedName x) {
		org.meta_environment.rascal.ast.QualifiedName name = x.getQualifiedName();
		Type signature = ctx.getEvaluator().tf.tupleType(new Type[0]);

		Result<IValue> r = ctx.getEvaluator().getCurrentEnvt().getVariable(name);

		if (r != null) {
			if (r.getValue() != null) {
				// Previously declared and initialized variable
				return new QualifiedNamePattern(vf, env, new EvaluatorContext(ctx.getEvaluator(), name), name);
			}
			
			Type type = r.getType();
			if (type instanceof ConcreteSyntaxType) {
				ConcreteSyntaxType cType = (ConcreteSyntaxType) type;
				if (cType.isConcreteListType()) {
					return new ConcreteListVariablePattern(vf, env,  new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName());
				}
			}
			
			return new TypedVariablePattern(vf, env, new EvaluatorContext(ctx.getEvaluator(), name), type,name);
		}
		if (scope.isTreeConstructorName(name, signature)) {
			return new NodePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), name,
					new java.util.ArrayList<AbstractPattern>());
		}
		// Completely fresh variable
		return new QualifiedNamePattern(vf, env, new EvaluatorContext(ctx.getEvaluator(), name), name);
		//return new AbstractPatternTypedVariable(vf, env, ev.tf.valueType(), name);
	}
	
	@Override
	public AbstractPattern visitExpressionTypedVariable(TypedVariable x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type = te.eval(x.getType(), env);
		
		if (type instanceof ConcreteSyntaxType) {
			ConcreteSyntaxType cType = (ConcreteSyntaxType) type;
			if (cType.isConcreteListType()) {
				return new ConcreteListVariablePattern(vf, env,  new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName());
			}
		}
		return new TypedVariablePattern(vf, env,  new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName());
	}
	
	@Override
	public AbstractPattern visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type =  te.eval(x.getType(), env);
		MatchPattern pat = x.getPattern().accept(this);
		return new TypedVariableBecomesPattern(vf, env, new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName(), pat);
	}
	
	@Override
	public AbstractPattern visitExpressionVariableBecomes(
			VariableBecomes x) {
		MatchPattern pat = x.getPattern().accept(this);
		return new VariableBecomesPattern(vf, env, new EvaluatorContext(ctx.getEvaluator(), x.getName()), x.getName(), pat);
	}
	
	@Override
	public AbstractPattern visitExpressionGuarded(Guarded x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type =  te.eval(x.getType(), env);
		AbstractPattern absPat = x.getPattern().accept(this);
		return new GuardedPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), type, absPat);
	}
	
	@Override
	public AbstractPattern visitExpressionAnti(Anti x) {
		AbstractPattern absPat = x.getPattern().accept(this);
		return new AntiPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), absPat);
	}
	
	@Override
	public AbstractPattern visitExpressionMultiVariable(MultiVariable x) {
		return new MultiVariablePattern(vf, env, new EvaluatorContext(ctx.getEvaluator(), x), x.getQualifiedName());
	}
	
	@Override
	public AbstractPattern visitExpressionDescendant(Descendant x) {
		AbstractPattern absPat = x.getPattern().accept(this);
		return new DescendantPattern(vf,ctx, absPat);
	}
	
	/*
	 * The following constructs are not allowed in patterns
	 */
	
	@Override
	public AbstractPattern visitExpressionAddition(Addition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public AbstractPattern visitExpressionAll(All x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionAmbiguity(
			org.meta_environment.rascal.ast.Expression.Ambiguity x) {
		throw new ImplementationError("Ambiguity in expression: " + x);
	}
	@Override
	public AbstractPattern visitExpressionAnd(And x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionAny(Any x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public AbstractPattern visitExpressionBracket(Bracket x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionClosure(Closure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionComposition(Composition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionComprehension(Comprehension x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionDivision(
			org.meta_environment.rascal.ast.Expression.Division x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEquals(Equals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEquivalence(Equivalence x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionFieldAccess(
			org.meta_environment.rascal.ast.Expression.FieldAccess x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionFieldProject(FieldProject x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionFieldUpdate(FieldUpdate x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionGetAnnotation(GetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionGreaterThan(GreaterThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public AbstractPattern visitExpressionIfThenElse(IfThenElse x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionImplication(Implication x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionIn(In x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionIntersection(
			org.meta_environment.rascal.ast.Expression.Intersection x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionLessThan(LessThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionLessThanOrEq(LessThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionLexical(
			org.meta_environment.rascal.ast.Expression.Lexical x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionMatch(Match x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionModulo(Modulo x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNegation(Negation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNegative(Negative x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNoMatch(NoMatch x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNonEquals(NonEquals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionNotIn(NotIn x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionOperatorAsValue(OperatorAsValue x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionOr(Or x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionProduct(
			org.meta_environment.rascal.ast.Expression.Product x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionRange(Range x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionSetAnnotation(SetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionStepRange(StepRange x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionSubscript(
			org.meta_environment.rascal.ast.Expression.Subscript x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionSubtraction(
			org.meta_environment.rascal.ast.Expression.Subtraction x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionTransitiveClosure(TransitiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEnumerator(Enumerator x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionEnumeratorWithStrategy(
			EnumeratorWithStrategy x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionVisit(Visit x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public AbstractPattern visitExpressionVoidClosure(VoidClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
}
