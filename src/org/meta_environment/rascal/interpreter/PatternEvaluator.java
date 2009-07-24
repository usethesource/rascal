package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.meta_environment.rascal.ast.Expression;
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
import org.meta_environment.rascal.ast.Literal.Boolean;
import org.meta_environment.rascal.ast.Literal.Integer;
import org.meta_environment.rascal.ast.Literal.Real;
import org.meta_environment.rascal.ast.Literal.RegExp;
import org.meta_environment.rascal.ast.Literal.String;
import org.meta_environment.rascal.ast.RegExp.Lexical;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.env.ConcreteSyntaxType;
import org.meta_environment.rascal.interpreter.matching.AntiPattern;
import org.meta_environment.rascal.interpreter.matching.ConcreteApplicationPattern;
import org.meta_environment.rascal.interpreter.matching.ConcreteListPattern;
import org.meta_environment.rascal.interpreter.matching.ConcreteListVariablePattern;
import org.meta_environment.rascal.interpreter.matching.DescendantPattern;
import org.meta_environment.rascal.interpreter.matching.EnumeratorResult;
import org.meta_environment.rascal.interpreter.matching.GuardedPattern;
import org.meta_environment.rascal.interpreter.matching.IBooleanResult;
import org.meta_environment.rascal.interpreter.matching.IMatchingResult;
import org.meta_environment.rascal.interpreter.matching.ListPattern;
import org.meta_environment.rascal.interpreter.matching.LiteralPattern;
import org.meta_environment.rascal.interpreter.matching.MatchResult;
import org.meta_environment.rascal.interpreter.matching.MultiVariablePattern;
import org.meta_environment.rascal.interpreter.matching.NodePattern;
import org.meta_environment.rascal.interpreter.matching.NotResult;
import org.meta_environment.rascal.interpreter.matching.QualifiedNamePattern;
import org.meta_environment.rascal.interpreter.matching.RegExpPatternValue;
import org.meta_environment.rascal.interpreter.matching.SetPattern;
import org.meta_environment.rascal.interpreter.matching.TuplePattern;
import org.meta_environment.rascal.interpreter.matching.TypedVariableBecomesPattern;
import org.meta_environment.rascal.interpreter.matching.TypedVariablePattern;
import org.meta_environment.rascal.interpreter.matching.VariableBecomesPattern;
import org.meta_environment.rascal.interpreter.result.Result;
import org.meta_environment.rascal.interpreter.staticErrors.AmbiguousConcretePattern;
import org.meta_environment.rascal.interpreter.staticErrors.RedeclaredVariableError;
import org.meta_environment.rascal.interpreter.staticErrors.SyntaxError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedPatternError;
import org.meta_environment.rascal.interpreter.utils.Names;

public class PatternEvaluator extends NullASTVisitor<IBooleanResult> {
	private IValueFactory vf;
	private EvaluatorContext ctx;
	private boolean debug = false;

	public PatternEvaluator(IValueFactory vf, EvaluatorContext ctx){
		this.vf = vf;
		this.ctx = ctx;
	}

	@Override
	public IBooleanResult visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}
	
	@Override
	public IBooleanResult visitLiteralBoolean(Boolean x) {
		return new LiteralPattern(vf, ctx, x.accept(ctx.getEvaluator()).getValue());
	}

	@Override
	public IBooleanResult visitLiteralInteger(Integer x) {
		return new LiteralPattern(vf, ctx, x.accept(ctx.getEvaluator()).getValue());
	}
	
	@Override
	public IBooleanResult visitLiteralReal(Real x) {
		return new LiteralPattern(vf, ctx, x.accept(ctx.getEvaluator()).getValue());
	}
	
	@Override
	public IBooleanResult visitLiteralString(String x) {
		return new LiteralPattern(vf, ctx, x.accept(ctx.getEvaluator()).getValue());
	}
	
	@Override
	public IBooleanResult visitLiteralRegExp(RegExp x) {
		return x.getRegExpLiteral().accept(this);
	}

	@Override
	public IBooleanResult visitRegExpLexical(Lexical x) {
		if(debug)System.err.println("visitRegExpLexical: " + x.getString());
		return new RegExpPatternValue(vf, new EvaluatorContext(ctx.getEvaluator(), x), x.getString());
	}
	
	@Override
	public IBooleanResult visitRegExpLiteralLexical(
			org.meta_environment.rascal.ast.RegExpLiteral.Lexical x) {
		if(debug)System.err.println("visitRegExpLiteralLexical: " + x.getString());

		java.lang.String subjectPat = x.getString();
		Character modifier = null;
		
		if(subjectPat.charAt(0) != '/'){
			throw new SyntaxError("Malformed Regular expression: " + subjectPat, x.getLocation());
		}
		
		int start = 1;
		int end = subjectPat.length()-1;
		if(subjectPat.charAt(end) != '/'){
			modifier = Character.valueOf(subjectPat.charAt(end));
			end--;
		}
		if(subjectPat.charAt(end) != '/'){
			throw new SyntaxError("Regular expression does not end with /", x.getLocation());
		}
		
		/*
		 * Find all pattern variables. Take escaped \< characters into account.
		 */
		Pattern replacePat = Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)\\s*:\\s*([^>]*)>");
		Matcher m = replacePat.matcher(subjectPat);
		
		java.lang.String resultRegExp = "";
		java.util.List<java.lang.String> names = new ArrayList<java.lang.String>();

		while(m.find()){
			java.lang.String varName = m.group(1);
			if(names.contains(varName))
				throw new RedeclaredVariableError(varName, x);
			names.add(varName);
			resultRegExp += subjectPat.substring(start, m.start(0)) + "(" + m.group(2) + ")";
			start = m.end(0);
		}
		resultRegExp += subjectPat.substring(start, end);
		/*
		 * Replace in the final regexp all occurrences of \< by <
		 */
		resultRegExp = resultRegExp.replaceAll("(\\\\<)", "<");
		if(debug)System.err.println("resultRegExp: " + resultRegExp);
		return new RegExpPatternValue(vf, ctx, x, resultRegExp, modifier, names);
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
	public IMatchingResult visitExpressionCallOrTree(CallOrTree x) {
		Expression nameExpr = x.getExpression();

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

		if (nameExpr.isQualifiedName() && ctx.getCurrentEnvt().getVariable(nameExpr.getQualifiedName()) == null) {
			return new NodePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), null, nameExpr.getQualifiedName(), visitArguments(x));
		}
		else {
			return new NodePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), (IMatchingResult) nameExpr.accept(this), null, visitArguments(x));
		}
	}
	
	private java.util.List<IMatchingResult> visitArguments(CallOrTree x){

		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x.getArguments();
		ArrayList<IMatchingResult> args = new java.util.ArrayList<IMatchingResult>(elements.size());
		
		int i = 0;
		for(org.meta_environment.rascal.ast.Expression e : elements){
			args.add(i++, (IMatchingResult) e.accept(this));
		}
		return args;
	}
	
	
	private java.util.List<IMatchingResult> visitElements(java.util.List<org.meta_environment.rascal.ast.Expression> elements){
		ArrayList<IMatchingResult> args = new java.util.ArrayList<IMatchingResult>(elements.size());
		
		int i = 0;
		for(org.meta_environment.rascal.ast.Expression e : elements){
			args.add(i++, (IMatchingResult) e.accept(this));
		}
		return args;
	}
	
	@Override
	public IBooleanResult visitExpressionList(List x) {
		return new ListPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public IBooleanResult visitExpressionSet(Set x) {
		return new SetPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public IBooleanResult visitExpressionTuple(Tuple x) {
		return new TuplePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), visitElements(x.getElements()));
	}
	
	@Override
	public IBooleanResult visitExpressionMap(Map x) {
		throw new ImplementationError("Map in pattern not yet implemented");
	}
	
	@Override
	public IBooleanResult visitExpressionQualifiedName(QualifiedName x) {
		org.meta_environment.rascal.ast.QualifiedName name = x.getQualifiedName();
		Type signature = ctx.getEvaluator().tf.tupleType(new Type[0]);

		Result<IValue> r = ctx.getEvaluator().getCurrentEnvt().getVariable(name);

		if (r != null) {
			if (r.getValue() != null) {
				// Previously declared and initialized variable
				return new QualifiedNamePattern(vf, new EvaluatorContext(ctx.getEvaluator(), name), name);
			}
			
			Type type = r.getType();
			if (type instanceof ConcreteSyntaxType) {
				ConcreteSyntaxType cType = (ConcreteSyntaxType) type;
				if (cType.isConcreteListType()) {
					return new ConcreteListVariablePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName());
				}
			}
			
			return new TypedVariablePattern(vf, new EvaluatorContext(ctx.getEvaluator(), name), type,name);
		}
		
		if (ctx.getCurrentEnvt().isTreeConstructorName(name, signature)) {
			return new NodePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), null, name,
					new java.util.ArrayList<IMatchingResult>());
		}
		
		// Completely fresh variable
		return new QualifiedNamePattern(vf, new EvaluatorContext(ctx.getEvaluator(), name), name);
		//return new AbstractPatternTypedVariable(vf, env, ev.tf.valueType(), name);
	}
	
	@Override
	public IBooleanResult visitExpressionTypedVariable(TypedVariable x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type = te.eval(x.getType(), ctx.getCurrentEnvt());
		
		if (type instanceof ConcreteSyntaxType) {
			ConcreteSyntaxType cType = (ConcreteSyntaxType) type;
			if (cType.isConcreteListType()) {
				return new ConcreteListVariablePattern(vf,  new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName());
			}
		}
		return new TypedVariablePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName());
	}
	
	@Override
	public IBooleanResult visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type =  te.eval(x.getType(), ctx.getCurrentEnvt());
		IMatchingResult pat = (IMatchingResult) x.getPattern().accept(this);
		return new TypedVariableBecomesPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x.getName()), type, x.getName(), pat);
	}
	
	@Override
	public IMatchingResult visitExpressionVariableBecomes(
			VariableBecomes x) {
		IMatchingResult pat = (IMatchingResult) x.getPattern().accept(this);
		return new VariableBecomesPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x.getName()), x.getName(), pat);
	}
	
	@Override
	public IMatchingResult visitExpressionGuarded(Guarded x) {
		TypeEvaluator te = TypeEvaluator.getInstance();
		Type type =  te.eval(x.getType(), ctx.getCurrentEnvt());
		IMatchingResult absPat = (IMatchingResult) x.getPattern().accept(this);
		return new GuardedPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), type, absPat);
	}
	
	@Override
	public IMatchingResult visitExpressionAnti(Anti x) {
		IMatchingResult absPat = (IMatchingResult) x.getPattern().accept(this);
		return new AntiPattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), absPat);
	}
	
	@Override
	public IBooleanResult visitExpressionMultiVariable(MultiVariable x) {
		return new MultiVariablePattern(vf, new EvaluatorContext(ctx.getEvaluator(), x), x.getQualifiedName());
	}
	
	@Override
	public IMatchingResult visitExpressionDescendant(Descendant x) {
		IMatchingResult absPat = (IMatchingResult) x.getPattern().accept(this);
		return new DescendantPattern(vf,ctx, absPat);
	}
	
	/*
	 * The following constructs are not allowed in patterns
	 */
	
	@Override
	public IBooleanResult visitExpressionAddition(Addition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public IBooleanResult visitExpressionAll(All x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionAmbiguity(
			org.meta_environment.rascal.ast.Expression.Ambiguity x) {
		throw new ImplementationError("Ambiguity in expression: " + x);
	}
	@Override
	public IBooleanResult visitExpressionAnd(And x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionAny(Any x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public IBooleanResult visitExpressionBracket(Bracket x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public IBooleanResult visitExpressionClosure(Closure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionComposition(Composition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionComprehension(Comprehension x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionDivision(
			org.meta_environment.rascal.ast.Expression.Division x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionEquals(Equals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionEquivalence(Equivalence x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionFieldAccess(
			org.meta_environment.rascal.ast.Expression.FieldAccess x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionFieldProject(FieldProject x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionFieldUpdate(FieldUpdate x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionGetAnnotation(GetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionGreaterThan(GreaterThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public IBooleanResult visitExpressionIfThenElse(IfThenElse x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionImplication(Implication x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
	@Override
	public IBooleanResult visitExpressionIn(In x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionIntersection(
			org.meta_environment.rascal.ast.Expression.Intersection x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionLessThan(LessThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionLessThanOrEq(LessThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionLexical(
			org.meta_environment.rascal.ast.Expression.Lexical x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionMatch(Match x) {
		return new MatchResult(vf, ctx, (IMatchingResult) x.getPattern().accept(this), true, x.getExpression());
	}
	
	@Override
	public IBooleanResult visitExpressionModulo(Modulo x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionNegation(Negation x) {
		return new NotResult(vf, ctx, x.getArgument().accept(this));
	}
	@Override
	public IBooleanResult visitExpressionNegative(Negative x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionNoMatch(NoMatch x) {
		return new MatchResult(vf, ctx, (IMatchingResult) x.getPattern().accept(this), false, x.getExpression());
	}
	
	@Override
	public IBooleanResult visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionNonEquals(NonEquals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionNotIn(NotIn x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionOperatorAsValue(OperatorAsValue x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionOr(Or x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionProduct(
			org.meta_environment.rascal.ast.Expression.Product x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionRange(Range x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionSetAnnotation(SetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionStepRange(StepRange x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionSubscript(
			org.meta_environment.rascal.ast.Expression.Subscript x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionSubtraction(
			org.meta_environment.rascal.ast.Expression.Subtraction x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionTransitiveClosure(TransitiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionEnumerator(Enumerator x) {
		return new EnumeratorResult(vf, ctx, (IMatchingResult) x.getPattern().accept(this), null, x.getExpression());
	}
	@Override
	public IBooleanResult visitExpressionEnumeratorWithStrategy(
			EnumeratorWithStrategy x) {
		return new EnumeratorResult(vf, ctx, (IMatchingResult) x.getPattern().accept(this), x.getStrategy(), x.getExpression());
	}
	
	@Override
	public IBooleanResult visitExpressionVisit(Visit x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IBooleanResult visitExpressionVoidClosure(VoidClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	
}
