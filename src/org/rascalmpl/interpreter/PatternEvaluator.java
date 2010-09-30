package org.rascalmpl.interpreter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.BasicType;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Name;
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
import org.rascalmpl.ast.Expression.ReifiedType;
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
import org.rascalmpl.ast.Literal.Boolean;
import org.rascalmpl.ast.Literal.Integer;
import org.rascalmpl.ast.Literal.Real;
import org.rascalmpl.ast.Literal.RegExp;
import org.rascalmpl.ast.Literal.String;
import org.rascalmpl.ast.RegExp.Lexical;
import org.rascalmpl.interpreter.asserts.Ambiguous;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.matching.AntiPattern;
import org.rascalmpl.interpreter.matching.ConcreteApplicationPattern;
import org.rascalmpl.interpreter.matching.ConcreteListPattern;
import org.rascalmpl.interpreter.matching.ConcreteListVariablePattern;
import org.rascalmpl.interpreter.matching.DescendantPattern;
import org.rascalmpl.interpreter.matching.GuardedPattern;
import org.rascalmpl.interpreter.matching.IMatchingResult;
import org.rascalmpl.interpreter.matching.ListPattern;
import org.rascalmpl.interpreter.matching.LiteralPattern;
import org.rascalmpl.interpreter.matching.MultiVariablePattern;
import org.rascalmpl.interpreter.matching.NodePattern;
import org.rascalmpl.interpreter.matching.NotPattern;
import org.rascalmpl.interpreter.matching.QualifiedNamePattern;
import org.rascalmpl.interpreter.matching.RegExpPatternValue;
import org.rascalmpl.interpreter.matching.ReifiedTypePattern;
import org.rascalmpl.interpreter.matching.SetPattern;
import org.rascalmpl.interpreter.matching.TuplePattern;
import org.rascalmpl.interpreter.matching.TypedVariablePattern;
import org.rascalmpl.interpreter.matching.VariableBecomesPattern;
import org.rascalmpl.interpreter.result.AbstractFunction;
import org.rascalmpl.interpreter.result.OverloadedFunctionResult;
import org.rascalmpl.interpreter.result.Result;
import org.rascalmpl.interpreter.staticErrors.RedeclaredVariableError;
import org.rascalmpl.interpreter.staticErrors.SyntaxError;
import org.rascalmpl.interpreter.staticErrors.UninitializedVariableError;
import org.rascalmpl.interpreter.staticErrors.UnsupportedPatternError;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.interpreter.utils.Names;
import org.rascalmpl.uri.URIResolverRegistry;
import org.rascalmpl.values.uptr.TreeAdapter;

public class PatternEvaluator extends NullASTVisitor<IMatchingResult> implements IEvaluator<IMatchingResult> {
	private final IEvaluatorContext ctx;
	private boolean debug = false;
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public PatternEvaluator(IEvaluatorContext ctx){
		this.ctx = ctx;
	}

	public IValue call(java.lang.String name, IValue... args) {
		throw new ImplementationError("should not call call");
	}
	
	@Override
	public IMatchingResult visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}

	@Override
	public IMatchingResult visitLiteralBoolean(Boolean x) {
		return new LiteralPattern(ctx, x, x.accept(ctx.getEvaluator()).getValue());
	}

	@Override
	public IMatchingResult visitLiteralInteger(Integer x) {
		return new LiteralPattern(ctx, x, x.accept(ctx.getEvaluator()).getValue());
	}

	@Override
	public IMatchingResult visitLiteralReal(Real x) {
		return new LiteralPattern(ctx, x, x.accept(ctx.getEvaluator()).getValue());
	}

	@Override
	public IMatchingResult visitLiteralString(String x) {
		return new LiteralPattern(ctx, x, x.accept(ctx.getEvaluator()).getValue());
	}

	@Override
	public IMatchingResult visitLiteralRegExp(RegExp x) {
		return x.getRegExpLiteral().accept(this);
	}

	@Override
	public IMatchingResult visitRegExpLexical(Lexical x) {
		if(debug)System.err.println("visitRegExpLexical: " + x.getString());
		return new RegExpPatternValue(ctx, x, x.getString(), Collections.<java.lang.String>emptyList());
	}

	/*
	 * Get the value of a variable as string
	 */

	private java.lang.String getValueAsString(java.lang.String varName){
		Environment env = ctx.getCurrentEnvt();
		Result<IValue> res = env.getVariable(varName);
		if(res != null && res.getValue() != null){
			if(res.getType().isStringType()) return ((IString)res.getValue()).getValue(); 
			
			return res.getValue().toString();	
		}
		
		throw new UninitializedVariableError(varName, ctx.getCurrentAST());  
	}

	/*
	 * Interpolate all occurrences of <X> by the value of X
	 */
	private java.lang.String interpolate(java.lang.String re){
		Pattern replacePat = Pattern.compile("(?<!\\\\)<([a-zA-Z0-9]+)>");
		Matcher m = replacePat.matcher(re);
		StringBuffer result = new StringBuffer();
		int start = 0;
		while(m.find()){
			result.append(re.substring(start, m.start(0))).
			append(getValueAsString(m.group(1))); // TODO: escape special chars?
			start = m.end(0);
		}
		result.append(re.substring(start,re.length()));

		if(debug)System.err.println("interpolate: " + re + " -> " + result);
		return result.toString();
	}

	/*
	 * Compile a Rascal regular expression into a Java regexp by appropriately replacing all
	 * pattern variables by java regexp code.
	 */
	@Override
	public IMatchingResult visitRegExpLiteralLexical(
			org.rascalmpl.ast.RegExpLiteral.Lexical x) {
		if(debug)System.err.println("visitRegExpLiteralLexical: " + x.getString());

		java.lang.String subjectPat = x.getString();

		if(subjectPat.charAt(0) != '/'){
			throw new SyntaxError("Malformed Regular expression: " + subjectPat, x.getLocation());
		}

		int start = 1;
		int end = subjectPat.length()-1;

		while(end > 0 && subjectPat.charAt(end) != '/'){
			end--;
		}
		java.lang.String modifiers = subjectPat.substring(end+1);

		if(subjectPat.charAt(end) != '/'){
			throw new SyntaxError("Regular expression does not end with /", x.getLocation());
		}

		// The resulting regexp that we are constructing
		StringBuffer resultRegExp = new StringBuffer();

		if(modifiers.length() > 0)
			resultRegExp.append("(?").append(modifiers).append(")");

		/*
		 * Find all pattern variables. There are two cases:
		 * (1) <X:regexp>
		 *     - a true pattern variable that will match regexp. Introduces a new local variable.
		 *     - regexp may contain references to variables <V> in the surrounding scope (but not to
		 *       pattern variables!) These values are interpolated in regexp
		 * (2) <X>
		 *     - if x did not occur earlier in the pattern, we do a string interpolation of the current value of X.
		 *     - otherwise x should have been introduced before by a pattern variable and we ensure at match time
		 *       that both values are the same (non-linear pattern).
		 * We take escaped \< characters into account.
		 */

		java.lang.String Name = "[a-zA-Z0-9]+";
		java.lang.String NR1 = "[^\\\\<>]";
		java.lang.String NR2 = "(?:\\\\[\\\\<>])";
		java.lang.String NR3 = "(?:\\\\)";
		java.lang.String NR4 = "(?:<" + Name + ">)";

		java.lang.String NamedRegexp = "(?:" + NR1 + "|" + NR2 + "|" + NR3 + "|" + NR4 + ")";

		java.lang.String RE = "(?:(?<!\\\\)|(?<=\\\\\\\\))<(" + Name + ")(?:\\s*:\\s*(" + NamedRegexp + "*))?" + ">";
		//                               |                         |
		//                       group   1                         2
		//                               variable name             regular expression to be matched

		Pattern replacePat = Pattern.compile(RE);

		Matcher m = replacePat.matcher(subjectPat);

		// List of variable introductions
		java.util.List<java.lang.String> patternVars = new ArrayList<java.lang.String>();

		while(m.find()){
			java.lang.String varName = m.group(1);

			resultRegExp.append(subjectPat.substring(start, m.start(0))); // add regexp before < ... > 

			if (m.end(2) > -1){       /* case (1): <X:regexp> */

				if(patternVars.contains(varName))
					throw new RedeclaredVariableError(varName, x);
				patternVars.add(varName);
				resultRegExp.append("(").append(interpolate(m.group(2))).append(")");
			} else {                   /* case (2): <X> */
				int varIndex = patternVars.indexOf(varName);
				if(varIndex >= 0){
					/* Generate reference to previous occurrence */
					resultRegExp.append("(?:\\").append(1+varIndex).append(")");
				} else {	
					resultRegExp.append(getValueAsString(varName)); // TODO: escape special chars?
				} 
			}
			start = m.end(0);
		}
		resultRegExp.append(subjectPat.substring(start, end));
		return new RegExpPatternValue(ctx, x, resultRegExp.toString(), patternVars);
	}


	private boolean isConcreteSyntaxAppl(CallOrTree tree){
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return Names.name(Names.lastName(tree.getExpression().getQualifiedName())).equals("appl") && tree._getType() instanceof NonTerminalType;
	}

	private boolean isConcreteSyntaxAmb(CallOrTree tree){
		if (!tree.getExpression().isQualifiedName()) {
			return false;
		}
		return Names.name(Names.lastName(tree.getExpression().getQualifiedName())).equals("amb") && tree._getType() instanceof NonTerminalType;
	}
	

	private boolean isConcreteSyntaxList(CallOrTree tree){
		return isConcreteSyntaxAppl(tree) && isConcreteListProd((CallOrTree) tree.getArguments().get(0)) && tree._getType() instanceof NonTerminalType;
	}

	private boolean isConcreteListProd(CallOrTree prod){
		if (!prod.getExpression().isQualifiedName()) {
			return false;
		}
		return Names.name(Names.lastName(prod.getExpression().getQualifiedName())).equals("list");
	}

	@Override
	public IMatchingResult visitExpressionReifiedType(ReifiedType x) {
		BasicType basic = x.getBasicType();
		java.util.List<IMatchingResult> args = visitElements(x.getArguments());

		return new ReifiedTypePattern(ctx, x, basic, args);
	}

	@Override
	public IMatchingResult visitExpressionCallOrTree(CallOrTree x) {
		Expression nameExpr = x.getExpression();
		if(isConcreteSyntaxList(x)) {
			List args = (List)x.getArguments().get(1);
			// TODO what if somebody writes a variable in  the list production itself?
			return new ConcreteListPattern(ctx, x,
					visitElements(args.getElements()));
		}
		if(isConcreteSyntaxAppl(x)){
			if (TreeAdapter.isLexical((IConstructor) x.getTree())) {
				return new ConcreteApplicationPattern(ctx, x, visitConcreteLexicalArguments(x));
			}
			else {
				return new ConcreteApplicationPattern(ctx, x, visitConcreteArguments(x));
			}
		}
		if (isConcreteSyntaxAmb(x)) {
			new Ambiguous((IConstructor) x.getTree());
			//			return new AbstractPatternConcreteAmb(vf, new EvaluatorContext(ctx.getEvaluator(), x), x, visitArguments(x));
		}

		if (nameExpr.isQualifiedName()) {
			Result<IValue> prefix = ctx.getCurrentEnvt().getVariable(nameExpr.getQualifiedName());

			// TODO: get rid of this if-then-else by introducing subclasses for NodePattern for each case.
			if (nameExpr.isQualifiedName() && prefix == null) {
				return new NodePattern(ctx, x, null, nameExpr.getQualifiedName(), visitArguments(x));
			}
			else if (nameExpr.isQualifiedName() && ((prefix instanceof AbstractFunction) || prefix instanceof OverloadedFunctionResult)) {
				return new NodePattern(ctx, x, null,nameExpr.getQualifiedName(), visitArguments(x));
			}
		}

		return new NodePattern(ctx, x, nameExpr.accept(this), null, visitArguments(x));
	}

	private java.util.List<IMatchingResult> visitArguments(CallOrTree x){
		java.util.List<org.rascalmpl.ast.Expression> elements = x.getArguments();
		return visitElements(elements);
	}

	private java.util.List<IMatchingResult> visitConcreteLexicalArguments(CallOrTree x){
        Expression args = x.getArguments().get(1);
        
		java.util.List<org.rascalmpl.ast.Expression> elements = args.getElements();
		return visitElements(elements);
	}

	
	private java.util.List<IMatchingResult> visitConcreteArguments(CallOrTree x){
        Expression args = x.getArguments().get(1);
        
		java.util.List<org.rascalmpl.ast.Expression> elements = args.getElements();
		return visitConcreteElements(elements);
	}
	
	private java.util.List<IMatchingResult> visitConcreteElements(java.util.List<org.rascalmpl.ast.Expression> elements){
		int n = elements.size();
		ArrayList<IMatchingResult> args = new java.util.ArrayList<IMatchingResult>((n + 1) / 2);

		for (int i = 0; i < n; i += 2) { // skip layout elements
			org.rascalmpl.ast.Expression e = elements.get(i);
			args.add(e.accept(this));
		}
		return args;
	}


	private java.util.List<IMatchingResult> visitElements(java.util.List<org.rascalmpl.ast.Expression> elements){
		ArrayList<IMatchingResult> args = new java.util.ArrayList<IMatchingResult>(elements.size());

		int i = 0;
		for(org.rascalmpl.ast.Expression e : elements){
			args.add(i++, e.accept(this));
		}
		return args;
	}

	@Override
	public IMatchingResult visitExpressionList(List x) {
		return new ListPattern(ctx, x, visitElements(x.getElements()));
	}

	@Override
	public IMatchingResult visitExpressionSet(Set x) {
		return new SetPattern(ctx, x, visitElements(x.getElements()));
	}

	@Override
	public IMatchingResult visitExpressionTuple(Tuple x) {
		return new TuplePattern(ctx, x, visitElements(x.getElements()));
	}

	@Override
	public IMatchingResult visitExpressionMap(Map x) {
		throw new ImplementationError("Map in pattern not yet implemented");
	}

	@Override
	public IMatchingResult visitExpressionQualifiedName(QualifiedName x) {
		org.rascalmpl.ast.QualifiedName name = x.getQualifiedName();
		Type signature = tf.tupleType(new Type[0]);

		Result<IValue> r = ctx.getEvaluator().getCurrentEnvt().getVariable(name);

		if (r != null) {
			if (r.getValue() != null) {
				// Previously declared and initialized variable
				return new QualifiedNamePattern(ctx, x, name);
			}

			Type type = r.getType();
			if (type instanceof NonTerminalType) {
				NonTerminalType cType = (NonTerminalType) type;
				if (cType.isConcreteListType()) {
					return new ConcreteListVariablePattern(ctx, x, type, Names.lastName(name));
				}
			}

			return new QualifiedNamePattern(ctx, x,name);
		}

		if (ctx.getCurrentEnvt().isTreeConstructorName(name, signature)) {
			return new NodePattern(ctx, x, null, name,
					new java.util.ArrayList<IMatchingResult>());
		}

		// Completely fresh variable
		return new QualifiedNamePattern(ctx, x, name);
		//return new AbstractPatternTypedVariable(vf, env, ev.tf.valueType(), name);
	}

	@Override
	public IMatchingResult visitExpressionTypedVariable(TypedVariable x) {
		Type type = new TypeEvaluator(ctx.getCurrentEnvt(), ctx.getHeap()).eval(x.getType());

		if (type instanceof NonTerminalType) {
			NonTerminalType cType = (NonTerminalType) type;
			if (cType.isConcreteListType()) {
				return new ConcreteListVariablePattern(ctx, x, type, x.getName());
			}
		}
		return new TypedVariablePattern(ctx, x, type, x.getName());
	}

	@Override
	public IMatchingResult visitExpressionTypedVariableBecomes(
			TypedVariableBecomes x) {
		Type type = new TypeEvaluator(ctx.getCurrentEnvt(), ctx.getHeap()).eval(x.getType());
		IMatchingResult pat = x.getPattern().accept(this);
		IMatchingResult var = new TypedVariablePattern(ctx, x, type, x.getName());
		return new VariableBecomesPattern(ctx, x, var, pat);
	}

	@Override
	public IMatchingResult visitExpressionVariableBecomes(
			VariableBecomes x) {
		IMatchingResult pat = x.getPattern().accept(this);
		LinkedList<Name> names = new LinkedList<Name>();
		names.add(x.getName());
		IMatchingResult var = new QualifiedNamePattern(ctx, x, new org.rascalmpl.ast.QualifiedName.Default(x.getTree(), names));
		return new VariableBecomesPattern(ctx, x, var, pat);
	}

	@Override
	public IMatchingResult visitExpressionGuarded(Guarded x) {
		Type type =  new TypeEvaluator(ctx.getCurrentEnvt(), ctx.getHeap()).eval(x.getType());
		IMatchingResult absPat = x.getPattern().accept(this);
		return new GuardedPattern(ctx, x, type, absPat);
	}

	@Override
	public IMatchingResult visitExpressionAnti(Anti x) {
		IMatchingResult absPat = x.getPattern().accept(this);
		return new AntiPattern(ctx, x, absPat);
	}

	@Override
	public IMatchingResult visitExpressionMultiVariable(MultiVariable x) {
		return new MultiVariablePattern(ctx, x, x.getQualifiedName());
	}

	@Override
	public IMatchingResult visitExpressionDescendant(Descendant x) {
		IMatchingResult absPat = x.getPattern().accept(this);
		return new DescendantPattern(ctx, x, absPat);
	}

	/*
	 * The following constructs are not allowed in patterns
	 */

	@Override
	public IMatchingResult visitExpressionAddition(Addition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionAll(All x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionAmbiguity(
			org.rascalmpl.ast.Expression.Ambiguity x) {
		throw new Ambiguous((IConstructor) x.getTree());
	}
	@Override
	public IMatchingResult visitExpressionAnd(And x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionAny(Any x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionBracket(Bracket x) {
		return x.getExpression().accept(this);
	}

	@Override
	public IMatchingResult visitExpressionClosure(Closure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionComposition(Composition x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionComprehension(Comprehension x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionDivision(
			org.rascalmpl.ast.Expression.Division x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionEquals(Equals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionEquivalence(Equivalence x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionFieldAccess(
			org.rascalmpl.ast.Expression.FieldAccess x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionFieldProject(FieldProject x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionFieldUpdate(FieldUpdate x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionGetAnnotation(GetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionGreaterThan(GreaterThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionIfThenElse(IfThenElse x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionImplication(Implication x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionIn(In x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionIntersection(
			org.rascalmpl.ast.Expression.Intersection x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionLessThan(LessThan x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionLessThanOrEq(LessThanOrEq x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionLexical(
			org.rascalmpl.ast.Expression.Lexical x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionMatch(Match x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionModulo(Modulo x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionNegation(Negation x) {
		return new NotPattern(ctx, x, x.getArgument().accept(this));
	}

	@Override
	public IMatchingResult visitExpressionNegative(Negative x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionNoMatch(NoMatch x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}

	@Override
	public IMatchingResult visitExpressionNonEquals(NonEquals x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionNotIn(NotIn x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionOr(Or x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionProduct(
			org.rascalmpl.ast.Expression.Product x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionRange(Range x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionSetAnnotation(SetAnnotation x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionStepRange(StepRange x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionSubscript(
			org.rascalmpl.ast.Expression.Subscript x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionSubtraction(
			org.rascalmpl.ast.Expression.Subtraction x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionTransitiveClosure(TransitiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	@Override
	public IMatchingResult visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
	}
	/*
	@Override
	public IMatchingResult visitExpressionEnumerator(Enumerator x) {
		return new EnumeratorResult(ctx, x, x.getPattern().accept(this), null, x.getExpression());
	}
	@Override
	public IMatchingResult visitExpressionEnumeratorWithStrategy(
			EnumeratorWithStrategy x) {
		return new EnumeratorResult(ctx, x, x.getPattern().accept(this), x.getStrategy(), x.getExpression());
	}
	 */

//	@Override
//	public IMatchingResult visitExpressionVisit(Visit x) {
//		throw new UnsupportedPatternError(x.toString(), x);
//	}
	
	@Override
	public IMatchingResult visitExpressionVoidClosure(VoidClosure x) {
		throw new UnsupportedPatternError(x.toString(), x);
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

	public URIResolverRegistry getResolverRegistry() {
		return ctx.getResolverRegistry();
	}

	public void interrupt() {
		
	}

	public boolean isInterrupted() {
		return false;
	}


}
