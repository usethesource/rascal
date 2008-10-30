package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.ITree; 
public class ASTFactory {
public Body.Ambiguity makeBodyAmbiguity(List<Body> alternatives) { 
Body.Ambiguity amb = new Body.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Body.Toplevels makeToplevelsBody(ITree tree, List<Toplevel> toplevels) { 
Body.Toplevels x = new Body.Toplevels(tree, toplevels);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Formal.Ambiguity makeFormalAmbiguity(List<Formal> alternatives) { 
Formal.Ambiguity amb = new Formal.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Formal.TypeName makeTypeNameFormal(ITree tree, Type type, Name name) { 
Formal.TypeName x = new Formal.TypeName(tree, type, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Formals.Ambiguity makeFormalsAmbiguity(List<Formals> alternatives) { 
Formals.Ambiguity amb = new Formals.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Formals.Formals makeFormalsFormals(ITree tree, List<Formal> formals) { 
Formals.Formals x = new Formals.Formals(tree, formals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Parameters.VarArgs makeVarArgsParameters(ITree tree, Formals formals) { 
Parameters.VarArgs x = new Parameters.VarArgs(tree, formals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Parameters.Ambiguity makeParametersAmbiguity(List<Parameters> alternatives) { 
Parameters.Ambiguity amb = new Parameters.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Parameters.Default makeDefaultParameters(ITree tree, Formals formals) { 
Parameters.Default x = new Parameters.Default(tree, formals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Visit makeVisitExpression(ITree tree, Visit visit) { 
Expression.Visit x = new Expression.Visit(tree, visit);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Forall makeForallExpression(ITree tree, ValueProducer producers, Expression expression) { 
Expression.Forall x = new Expression.Forall(tree, producers, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Exists makeExistsExpression(ITree tree, ValueProducer producer, Expression expression) { 
Expression.Exists x = new Expression.Exists(tree, producer, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Comprehension makeComprehensionExpression(ITree tree, Comprehension comprehension) { 
Expression.Comprehension x = new Expression.Comprehension(tree, comprehension);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.QualifiedName makeQualifiedNameExpression(ITree tree, QualifiedName qualifiedName) { 
Expression.QualifiedName x = new Expression.QualifiedName(tree, qualifiedName);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.AreaInFileLocation makeAreaInFileLocationExpression(ITree tree, Expression filename, Expression area) { 
Expression.AreaInFileLocation x = new Expression.AreaInFileLocation(tree, filename, area);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.AreaLocation makeAreaLocationExpression(ITree tree, Expression area) { 
Expression.AreaLocation x = new Expression.AreaLocation(tree, area);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.FileLocation makeFileLocationExpression(ITree tree, Expression filename) { 
Expression.FileLocation x = new Expression.FileLocation(tree, filename);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Area makeAreaExpression(ITree tree) { 
Expression.Area x = new Expression.Area(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Location makeLocationExpression(ITree tree) { 
Expression.Location x = new Expression.Location(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.MapTuple makeMapTupleExpression(ITree tree, Expression from, Expression to) { 
Expression.MapTuple x = new Expression.MapTuple(tree, from, to);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Tuple makeTupleExpression(ITree tree, Expression first, List<Expression> rest) { 
Expression.Tuple x = new Expression.Tuple(tree, first, rest);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Set makeSetExpression(ITree tree, List<Expression> elements) { 
Expression.Set x = new Expression.Set(tree, elements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.StepRange makeStepRangeExpression(ITree tree, Expression from, Expression by, Expression to) { 
Expression.StepRange x = new Expression.StepRange(tree, from, by, to);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Range makeRangeExpression(ITree tree, Expression from, Expression to) { 
Expression.Range x = new Expression.Range(tree, from, to);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.List makeListExpression(ITree tree, List<Expression> elements) { 
Expression.List x = new Expression.List(tree, elements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.CallOrTree makeCallOrTreeExpression(ITree tree, Name name, List<Expression> arguments) { 
Expression.CallOrTree x = new Expression.CallOrTree(tree, name, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Literal makeLiteralExpression(ITree tree, Literal literal) { 
Expression.Literal x = new Expression.Literal(tree, literal);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Operator makeOperatorExpression(ITree tree, StandardOperator operator) { 
Expression.Operator x = new Expression.Operator(tree, operator);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.IfThenElse makeIfThenElseExpression(ITree tree, Expression condition, Expression then, Expression else) { 
Expression.IfThenElse x = new Expression.IfThenElse(tree, condition, then, else);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.IfDefined makeIfDefinedExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.IfDefined x = new Expression.IfDefined(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Or makeOrExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Or x = new Expression.Or(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.And makeAndExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.And x = new Expression.And(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.In makeInExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.In x = new Expression.In(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.NotIn makeNotInExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.NotIn x = new Expression.NotIn(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.NonEquals makeNonEqualsExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.NonEquals x = new Expression.NonEquals(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Equals makeEqualsExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Equals x = new Expression.Equals(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.GreaterThanOrEq makeGreaterThanOrEqExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.GreaterThanOrEq x = new Expression.GreaterThanOrEq(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.GreaterThan makeGreaterThanExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.GreaterThan x = new Expression.GreaterThan(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.LessThanOrEq makeLessThanOrEqExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.LessThanOrEq x = new Expression.LessThanOrEq(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.LessThan makeLessThanExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.LessThan x = new Expression.LessThan(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.NoMatch makeNoMatchExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.NoMatch x = new Expression.NoMatch(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Match makeMatchExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Match x = new Expression.Match(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Substraction makeSubstractionExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Substraction x = new Expression.Substraction(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Addition makeAdditionExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Addition x = new Expression.Addition(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Division makeDivisionExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Division x = new Expression.Division(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Intersection makeIntersectionExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Intersection x = new Expression.Intersection(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Product makeProductExpression(ITree tree, Expression lhs, Expression rhs) { 
Expression.Product x = new Expression.Product(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Negation makeNegationExpression(ITree tree, Expression argument) { 
Expression.Negation x = new Expression.Negation(tree, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Annotation makeAnnotationExpression(ITree tree, Expression expression, Name name) { 
Expression.Annotation x = new Expression.Annotation(tree, expression, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.TransitiveClosure makeTransitiveClosureExpression(ITree tree, Expression argument) { 
Expression.TransitiveClosure x = new Expression.TransitiveClosure(tree, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.TransitiveReflexiveClosure makeTransitiveReflexiveClosureExpression(ITree tree, Expression argument) { 
Expression.TransitiveReflexiveClosure x = new Expression.TransitiveReflexiveClosure(tree, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Subscript makeSubscriptExpression(ITree tree, Expression expression, Expression subscript) { 
Expression.Subscript x = new Expression.Subscript(tree, expression, subscript);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.FieldAccess makeFieldAccessExpression(ITree tree, Expression expression, Name field) { 
Expression.FieldAccess x = new Expression.FieldAccess(tree, expression, field);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.FieldUpdate makeFieldUpdateExpression(ITree tree, Expression expression, Name key, Expression replacement) { 
Expression.FieldUpdate x = new Expression.FieldUpdate(tree, expression, key, replacement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.ClosureCall makeClosureCallExpression(ITree tree, Expression closure, List<Expression> arguments) { 
Expression.ClosureCall x = new Expression.ClosureCall(tree, closure, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Expression.Ambiguity makeExpressionAmbiguity(List<Expression> alternatives) { 
Expression.Ambiguity amb = new Expression.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Expression.Closure makeClosureExpression(ITree tree, Type type, List<Statement> statements) { 
Expression.Closure x = new Expression.Closure(tree, type, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public NatCon.Ambiguity makeNatConAmbiguity(List<NatCon> alternatives) { 
NatCon.Ambiguity amb = new NatCon.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public NatCon.digits makedigitsNatCon(ITree tree) { 
NatCon.digits x = new NatCon.digits(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SymbolLiteral.Ambiguity makeSymbolLiteralAmbiguity(List<SymbolLiteral> alternatives) { 
SymbolLiteral.Ambiguity amb = new SymbolLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Literal.String makeStringLiteral(ITree tree, StringLiteral string) { 
Literal.String x = new Literal.String(tree, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Literal.Double makeDoubleLiteral(ITree tree, FloatingPointLiteral double) { 
Literal.Double x = new Literal.Double(tree, double);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Literal.Integer makeIntegerLiteral(ITree tree, IntegerLiteral integer) { 
Literal.Integer x = new Literal.Integer(tree, integer);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Literal.Boolean makeBooleanLiteral(ITree tree, BooleanLiteral boolean) { 
Literal.Boolean x = new Literal.Boolean(tree, boolean);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Literal.Symbol makeSymbolLiteral(ITree tree, SymbolLiteral symbol) { 
Literal.Symbol x = new Literal.Symbol(tree, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Literal.Ambiguity makeLiteralAmbiguity(List<Literal> alternatives) { 
Literal.Ambiguity amb = new Literal.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Literal.RegExp makeRegExpLiteral(ITree tree, RegExpLiteral regExp) { 
Literal.RegExp x = new Literal.RegExp(tree, regExp);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Bound.Default makeDefaultBound(ITree tree, Expression expression) { 
Bound.Default x = new Bound.Default(tree, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Bound.Ambiguity makeBoundAmbiguity(List<Bound> alternatives) { 
Bound.Ambiguity amb = new Bound.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Bound.Empty makeEmptyBound(ITree tree) { 
Bound.Empty x = new Bound.Empty(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.GlobalDirective makeGlobalDirectiveStatement(ITree tree, Type type, List<QualifiedName> names) { 
Statement.GlobalDirective x = new Statement.GlobalDirective(tree, type, names);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.VariableDeclaration makeVariableDeclarationStatement(ITree tree, LocalVariableDeclaration declaration) { 
Statement.VariableDeclaration x = new Statement.VariableDeclaration(tree, declaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.FunctionDeclaration makeFunctionDeclarationStatement(ITree tree, FunctionDeclaration functionDeclaration) { 
Statement.FunctionDeclaration x = new Statement.FunctionDeclaration(tree, functionDeclaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Block makeBlockStatement(ITree tree, List<Statement> statements) { 
Statement.Block x = new Statement.Block(tree, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.TryFinally makeTryFinallyStatement(ITree tree, Statement body, List<Catch> handlers, Statement finallyBody) { 
Statement.TryFinally x = new Statement.TryFinally(tree, body, handlers, finallyBody);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Try makeTryStatement(ITree tree, Statement body, List<Catch> handlers) { 
Statement.Try x = new Statement.Try(tree, body, handlers);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Fail makeFailStatement(ITree tree) { 
Statement.Fail x = new Statement.Fail(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.ReturnVoid makeReturnVoidStatement(ITree tree) { 
Statement.ReturnVoid x = new Statement.ReturnVoid(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Throw makeThrowStatement(ITree tree, Expression expression) { 
Statement.Throw x = new Statement.Throw(tree, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Insert makeInsertStatement(ITree tree, Expression expression) { 
Statement.Insert x = new Statement.Insert(tree, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Return makeReturnStatement(ITree tree, Expression expression) { 
Statement.Return x = new Statement.Return(tree, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Assert makeAssertStatement(ITree tree, StringLiteral label, Expression expression) { 
Statement.Assert x = new Statement.Assert(tree, label, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Assignment makeAssignmentStatement(ITree tree, List<Assignable> assignables, Assignment operator, List<Expression> expressions) { 
Statement.Assignment x = new Statement.Assignment(tree, assignables, operator, expressions);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Visit makeVisitStatement(ITree tree, Visit visit) { 
Statement.Visit x = new Statement.Visit(tree, visit);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Expression makeExpressionStatement(ITree tree, Expression expression) { 
Statement.Expression x = new Statement.Expression(tree, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Switch makeSwitchStatement(ITree tree, Expression expression, List<Case> cases) { 
Statement.Switch x = new Statement.Switch(tree, expression, cases);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.IfThen makeIfThenStatement(ITree tree, Condition condition, Statement thenStatement) { 
Statement.IfThen x = new Statement.IfThen(tree, condition, thenStatement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.IfThenElse makeIfThenElseStatement(ITree tree, Condition condition, Statement thenStatement, Statement elseStatement) { 
Statement.IfThenElse x = new Statement.IfThenElse(tree, condition, thenStatement, elseStatement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.While makeWhileStatement(ITree tree, Expression condition, Statement body) { 
Statement.While x = new Statement.While(tree, condition, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.For makeForStatement(ITree tree, List<Generator> generators, Statement body) { 
Statement.For x = new Statement.For(tree, generators, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Statement.Ambiguity makeStatementAmbiguity(List<Statement> alternatives) { 
Statement.Ambiguity amb = new Statement.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Statement.Solve makeSolveStatement(ITree tree, Bound bound, Statement init, Statement body) { 
Statement.Solve x = new Statement.Solve(tree, bound, init, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Condition.Conjunction makeConjunctionCondition(ITree tree, Condition lhs, Condition rhs) { 
Condition.Conjunction x = new Condition.Conjunction(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Condition.Expression makeExpressionCondition(ITree tree, Expression expression) { 
Condition.Expression x = new Condition.Expression(tree, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Condition.NoMatch makeNoMatchCondition(ITree tree, Pattern pattern, Expression expression) { 
Condition.NoMatch x = new Condition.NoMatch(tree, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Condition.Ambiguity makeConditionAmbiguity(List<Condition> alternatives) { 
Condition.Ambiguity amb = new Condition.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Condition.Match makeMatchCondition(ITree tree, Pattern pattern, Expression expression) { 
Condition.Match x = new Condition.Match(tree, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public NoElseMayFollow.Ambiguity makeNoElseMayFollowAmbiguity(List<NoElseMayFollow> alternatives) { 
NoElseMayFollow.Ambiguity amb = new NoElseMayFollow.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Assignable.Constructor makeConstructorAssignable(ITree tree, Name name, List<Assignable> arguments) { 
Assignable.Constructor x = new Assignable.Constructor(tree, name, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignable.Tuple makeTupleAssignable(ITree tree, Assignable first, List<Assignable> rest) { 
Assignable.Tuple x = new Assignable.Tuple(tree, first, rest);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignable.Annotation makeAnnotationAssignable(ITree tree, Assignable receiver, Expression annotation) { 
Assignable.Annotation x = new Assignable.Annotation(tree, receiver, annotation);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignable.IfDefined makeIfDefinedAssignable(ITree tree, Assignable receiver, Expression condition) { 
Assignable.IfDefined x = new Assignable.IfDefined(tree, receiver, condition);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignable.FieldAccess makeFieldAccessAssignable(ITree tree, Assignable receiver, Name field) { 
Assignable.FieldAccess x = new Assignable.FieldAccess(tree, receiver, field);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignable.Subscript makeSubscriptAssignable(ITree tree, Assignable receiver, Expression subscript) { 
Assignable.Subscript x = new Assignable.Subscript(tree, receiver, subscript);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignable.Ambiguity makeAssignableAmbiguity(List<Assignable> alternatives) { 
Assignable.Ambiguity amb = new Assignable.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Assignable.Variable makeVariableAssignable(ITree tree, QualifiedName qualifiedName) { 
Assignable.Variable x = new Assignable.Variable(tree, qualifiedName);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignment.Interesection makeInteresectionAssignment(ITree tree) { 
Assignment.Interesection x = new Assignment.Interesection(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignment.Division makeDivisionAssignment(ITree tree) { 
Assignment.Division x = new Assignment.Division(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignment.Product makeProductAssignment(ITree tree) { 
Assignment.Product x = new Assignment.Product(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignment.Substraction makeSubstractionAssignment(ITree tree) { 
Assignment.Substraction x = new Assignment.Substraction(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignment.Addition makeAdditionAssignment(ITree tree) { 
Assignment.Addition x = new Assignment.Addition(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Assignment.Ambiguity makeAssignmentAmbiguity(List<Assignment> alternatives) { 
Assignment.Ambiguity amb = new Assignment.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Assignment.Default makeDefaultAssignment(ITree tree) { 
Assignment.Default x = new Assignment.Default(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Catch.BindingCatch makeBindingCatchCatch(ITree tree, Type type, Name name, Statement body) { 
Catch.BindingCatch x = new Catch.BindingCatch(tree, type, name, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Catch.Ambiguity makeCatchAmbiguity(List<Catch> alternatives) { 
Catch.Ambiguity amb = new Catch.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Catch.Catch makeCatchCatch(ITree tree, Statement body) { 
Catch.Catch x = new Catch.Catch(tree, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declarator.Ambiguity makeDeclaratorAmbiguity(List<Declarator> alternatives) { 
Declarator.Ambiguity amb = new Declarator.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Declarator.Default makeDefaultDeclarator(ITree tree, Type type, List<Variable> variables) { 
Declarator.Default x = new Declarator.Default(tree, type, variables);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public LocalVariableDeclaration.Dynamic makeDynamicLocalVariableDeclaration(ITree tree, Declarator declarator) { 
LocalVariableDeclaration.Dynamic x = new LocalVariableDeclaration.Dynamic(tree, declarator);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public LocalVariableDeclaration.Ambiguity makeLocalVariableDeclarationAmbiguity(List<LocalVariableDeclaration> alternatives) { 
LocalVariableDeclaration.Ambiguity amb = new LocalVariableDeclaration.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public LocalVariableDeclaration.Default makeDefaultLocalVariableDeclaration(ITree tree, Declarator declarator) { 
LocalVariableDeclaration.Default x = new LocalVariableDeclaration.Default(tree, declarator);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public RegExpLiteral.Ambiguity makeRegExpLiteralAmbiguity(List<RegExpLiteral> alternatives) { 
RegExpLiteral.Ambiguity amb = new RegExpLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public RegExpModifier.Ambiguity makeRegExpModifierAmbiguity(List<RegExpModifier> alternatives) { 
RegExpModifier.Ambiguity amb = new RegExpModifier.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Backslash.Ambiguity makeBackslashAmbiguity(List<Backslash> alternatives) { 
Backslash.Ambiguity amb = new Backslash.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public RegExp.Ambiguity makeRegExpAmbiguity(List<RegExp> alternatives) { 
RegExp.Ambiguity amb = new RegExp.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public NamedRegExp.Ambiguity makeNamedRegExpAmbiguity(List<NamedRegExp> alternatives) { 
NamedRegExp.Ambiguity amb = new NamedRegExp.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public NamedBackslash.Ambiguity makeNamedBackslashAmbiguity(List<NamedBackslash> alternatives) { 
NamedBackslash.Ambiguity amb = new NamedBackslash.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public StrChar.normal makenormalStrChar(ITree tree) { 
StrChar.normal x = new StrChar.normal(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StrChar.decimal makedecimalStrChar(ITree tree, List<get-sort-from-symbol([0-9])> a, List<get-sort-from-symbol([0-9])> b, List<get-sort-from-symbol([0-9])> c) { 
StrChar.decimal x = new StrChar.decimal(tree, a, b, c);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StrChar.backslash makebackslashStrChar(ITree tree) { 
StrChar.backslash x = new StrChar.backslash(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StrChar.quote makequoteStrChar(ITree tree) { 
StrChar.quote x = new StrChar.quote(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StrChar.tab maketabStrChar(ITree tree) { 
StrChar.tab x = new StrChar.tab(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StrChar.Ambiguity makeStrCharAmbiguity(List<StrChar> alternatives) { 
StrChar.Ambiguity amb = new StrChar.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public StrChar.newline makenewlineStrChar(ITree tree) { 
StrChar.newline x = new StrChar.newline(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StrCon.Ambiguity makeStrConAmbiguity(List<StrCon> alternatives) { 
StrCon.Ambiguity amb = new StrCon.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public StrCon.default makedefaultStrCon(ITree tree, List<StrChar> chars) { 
StrCon.default x = new StrCon.default(tree, chars);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Visibility.Private makePrivateVisibility(ITree tree) { 
Visibility.Private x = new Visibility.Private(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Visibility.Ambiguity makeVisibilityAmbiguity(List<Visibility> alternatives) { 
Visibility.Ambiguity amb = new Visibility.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Visibility.Public makePublicVisibility(ITree tree) { 
Visibility.Public x = new Visibility.Public(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Toplevel.DefaultVisibility makeDefaultVisibilityToplevel(ITree tree, Declaration declaration) { 
Toplevel.DefaultVisibility x = new Toplevel.DefaultVisibility(tree, declaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Toplevel.Ambiguity makeToplevelAmbiguity(List<Toplevel> alternatives) { 
Toplevel.Ambiguity amb = new Toplevel.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Toplevel.GivenVisibility makeGivenVisibilityToplevel(ITree tree, Visibility visibility, Declaration declaration) { 
Toplevel.GivenVisibility x = new Toplevel.GivenVisibility(tree, visibility, declaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Tag makeTagDeclaration(ITree tree, Kind kind, Name name, Tags tags, List<Type> types) { 
Declaration.Tag x = new Declaration.Tag(tree, kind, name, tags, types);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Annotation makeAnnotationDeclaration(ITree tree, Type type, Name name, Tags tags, List<Type> types) { 
Declaration.Annotation x = new Declaration.Annotation(tree, type, name, tags, types);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Rule makeRuleDeclaration(ITree tree, Name name, Tags tags, Rule rule) { 
Declaration.Rule x = new Declaration.Rule(tree, name, tags, rule);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Variable makeVariableDeclaration(ITree tree, Type type, List<Variable> variables) { 
Declaration.Variable x = new Declaration.Variable(tree, type, variables);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Function makeFunctionDeclaration(ITree tree, FunctionDeclaration functionDeclaration) { 
Declaration.Function x = new Declaration.Function(tree, functionDeclaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Data makeDataDeclaration(ITree tree, UserType user, Tags tags, List<Variant> variants) { 
Declaration.Data x = new Declaration.Data(tree, user, tags, variants);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Type makeTypeDeclaration(ITree tree, Type base, UserType user, Tags tags) { 
Declaration.Type x = new Declaration.Type(tree, base, user, tags);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Declaration.Ambiguity makeDeclarationAmbiguity(List<Declaration> alternatives) { 
Declaration.Ambiguity amb = new Declaration.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Declaration.View makeViewDeclaration(ITree tree, Name view, Name type, Tags tags, List<Alternative> alternatives) { 
Declaration.View x = new Declaration.View(tree, view, type, tags, alternatives);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Alternative.Ambiguity makeAlternativeAmbiguity(List<Alternative> alternatives) { 
Alternative.Ambiguity amb = new Alternative.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Alternative.NamedType makeNamedTypeAlternative(ITree tree, Name name, Type type) { 
Alternative.NamedType x = new Alternative.NamedType(tree, name, type);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Variant.NillaryConstructor makeNillaryConstructorVariant(ITree tree, Name name) { 
Variant.NillaryConstructor x = new Variant.NillaryConstructor(tree, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Variant.NAryConstructor makeNAryConstructorVariant(ITree tree, Name name, List<TypeArg> arguments) { 
Variant.NAryConstructor x = new Variant.NAryConstructor(tree, name, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Variant.Ambiguity makeVariantAmbiguity(List<Variant> alternatives) { 
Variant.Ambiguity amb = new Variant.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Variant.Type makeTypeVariant(ITree tree, Type type, Name name) { 
Variant.Type x = new Variant.Type(tree, type, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.NotIn makeNotInStandardOperator(ITree tree) { 
StandardOperator.NotIn x = new StandardOperator.NotIn(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.In makeInStandardOperator(ITree tree) { 
StandardOperator.In x = new StandardOperator.In(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Not makeNotStandardOperator(ITree tree) { 
StandardOperator.Not x = new StandardOperator.Not(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Or makeOrStandardOperator(ITree tree) { 
StandardOperator.Or x = new StandardOperator.Or(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.And makeAndStandardOperator(ITree tree) { 
StandardOperator.And x = new StandardOperator.And(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.GreaterThanOrEq makeGreaterThanOrEqStandardOperator(ITree tree) { 
StandardOperator.GreaterThanOrEq x = new StandardOperator.GreaterThanOrEq(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.GreaterThan makeGreaterThanStandardOperator(ITree tree) { 
StandardOperator.GreaterThan x = new StandardOperator.GreaterThan(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.LessThanOrEq makeLessThanOrEqStandardOperator(ITree tree) { 
StandardOperator.LessThanOrEq x = new StandardOperator.LessThanOrEq(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.LessThan makeLessThanStandardOperator(ITree tree) { 
StandardOperator.LessThan x = new StandardOperator.LessThan(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.NotEquals makeNotEqualsStandardOperator(ITree tree) { 
StandardOperator.NotEquals x = new StandardOperator.NotEquals(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Equals makeEqualsStandardOperator(ITree tree) { 
StandardOperator.Equals x = new StandardOperator.Equals(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Intersection makeIntersectionStandardOperator(ITree tree) { 
StandardOperator.Intersection x = new StandardOperator.Intersection(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Division makeDivisionStandardOperator(ITree tree) { 
StandardOperator.Division x = new StandardOperator.Division(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Product makeProductStandardOperator(ITree tree) { 
StandardOperator.Product x = new StandardOperator.Product(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Substraction makeSubstractionStandardOperator(ITree tree) { 
StandardOperator.Substraction x = new StandardOperator.Substraction(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StandardOperator.Ambiguity makeStandardOperatorAmbiguity(List<StandardOperator> alternatives) { 
StandardOperator.Ambiguity amb = new StandardOperator.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public StandardOperator.Addition makeAdditionStandardOperator(ITree tree) { 
StandardOperator.Addition x = new StandardOperator.Addition(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionName.Operator makeOperatorFunctionName(ITree tree, StandardOperator operator) { 
FunctionName.Operator x = new FunctionName.Operator(tree, operator);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionName.Ambiguity makeFunctionNameAmbiguity(List<FunctionName> alternatives) { 
FunctionName.Ambiguity amb = new FunctionName.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public FunctionName.Name makeNameFunctionName(ITree tree, Name name) { 
FunctionName.Name x = new FunctionName.Name(tree, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionModifier.Ambiguity makeFunctionModifierAmbiguity(List<FunctionModifier> alternatives) { 
FunctionModifier.Ambiguity amb = new FunctionModifier.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public FunctionModifier.Java makeJavaFunctionModifier(ITree tree) { 
FunctionModifier.Java x = new FunctionModifier.Java(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionModifiers.Ambiguity makeFunctionModifiersAmbiguity(List<FunctionModifiers> alternatives) { 
FunctionModifiers.Ambiguity amb = new FunctionModifiers.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public FunctionModifiers.List makeListFunctionModifiers(ITree tree, List<FunctionModifier> modifiers) { 
FunctionModifiers.List x = new FunctionModifiers.List(tree, modifiers);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Signature.WithThrows makeWithThrowsSignature(ITree tree, Type type, FunctionModifiers modifiers, FunctionName name, Parameters parameters, List<Type> exceptions) { 
Signature.WithThrows x = new Signature.WithThrows(tree, type, modifiers, name, parameters, exceptions);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Signature.Ambiguity makeSignatureAmbiguity(List<Signature> alternatives) { 
Signature.Ambiguity amb = new Signature.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Signature.NoThrows makeNoThrowsSignature(ITree tree, Type type, FunctionModifiers modifiers, FunctionName name, Parameters parameters) { 
Signature.NoThrows x = new Signature.NoThrows(tree, type, modifiers, name, parameters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionDeclaration.Abstract makeAbstractFunctionDeclaration(ITree tree, Signature signature, Tags tags) { 
FunctionDeclaration.Abstract x = new FunctionDeclaration.Abstract(tree, signature, tags);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionDeclaration.Ambiguity makeFunctionDeclarationAmbiguity(List<FunctionDeclaration> alternatives) { 
FunctionDeclaration.Ambiguity amb = new FunctionDeclaration.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public FunctionDeclaration.Default makeDefaultFunctionDeclaration(ITree tree, Signature signature, Tags tags, FunctionBody body) { 
FunctionDeclaration.Default x = new FunctionDeclaration.Default(tree, signature, tags, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionBody.Ambiguity makeFunctionBodyAmbiguity(List<FunctionBody> alternatives) { 
FunctionBody.Ambiguity amb = new FunctionBody.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public FunctionBody.Default makeDefaultFunctionBody(ITree tree, List<Statement> statements) { 
FunctionBody.Default x = new FunctionBody.Default(tree, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Variable.Ambiguity makeVariableAmbiguity(List<Variable> alternatives) { 
Variable.Ambiguity amb = new Variable.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Variable.GivenInitialization makeGivenInitializationVariable(ITree tree, Name name, Tags tags, Expression initial) { 
Variable.GivenInitialization x = new Variable.GivenInitialization(tree, name, tags, initial);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.All makeAllKind(ITree tree) { 
Kind.All x = new Kind.All(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.Tag makeTagKind(ITree tree) { 
Kind.Tag x = new Kind.Tag(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.Anno makeAnnoKind(ITree tree) { 
Kind.Anno x = new Kind.Anno(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.Type makeTypeKind(ITree tree) { 
Kind.Type x = new Kind.Type(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.View makeViewKind(ITree tree) { 
Kind.View x = new Kind.View(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.Data makeDataKind(ITree tree) { 
Kind.Data x = new Kind.Data(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.Variable makeVariableKind(ITree tree) { 
Kind.Variable x = new Kind.Variable(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.Function makeFunctionKind(ITree tree) { 
Kind.Function x = new Kind.Function(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Kind.Ambiguity makeKindAmbiguity(List<Kind> alternatives) { 
Kind.Ambiguity amb = new Kind.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Kind.Module makeModuleKind(ITree tree) { 
Kind.Module x = new Kind.Module(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Comment.Ambiguity makeCommentAmbiguity(List<Comment> alternatives) { 
Comment.Ambiguity amb = new Comment.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public CommentChar.Ambiguity makeCommentCharAmbiguity(List<CommentChar> alternatives) { 
CommentChar.Ambiguity amb = new CommentChar.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Asterisk.Ambiguity makeAsteriskAmbiguity(List<Asterisk> alternatives) { 
Asterisk.Ambiguity amb = new Asterisk.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public BasicType.Loc makeLocBasicType(ITree tree) { 
BasicType.Loc x = new BasicType.Loc(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public BasicType.Void makeVoidBasicType(ITree tree) { 
BasicType.Void x = new BasicType.Void(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public BasicType.Term makeTermBasicType(ITree tree) { 
BasicType.Term x = new BasicType.Term(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public BasicType.Value makeValueBasicType(ITree tree) { 
BasicType.Value x = new BasicType.Value(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public BasicType.String makeStringBasicType(ITree tree) { 
BasicType.String x = new BasicType.String(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public BasicType.Double makeDoubleBasicType(ITree tree) { 
BasicType.Double x = new BasicType.Double(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public BasicType.Int makeIntBasicType(ITree tree) { 
BasicType.Int x = new BasicType.Int(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public BasicType.Ambiguity makeBasicTypeAmbiguity(List<BasicType> alternatives) { 
BasicType.Ambiguity amb = new BasicType.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public BasicType.Bool makeBoolBasicType(ITree tree) { 
BasicType.Bool x = new BasicType.Bool(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public TypeArg.Named makeNamedTypeArg(ITree tree, Type type, Name name) { 
TypeArg.Named x = new TypeArg.Named(tree, type, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public TypeArg.Ambiguity makeTypeArgAmbiguity(List<TypeArg> alternatives) { 
TypeArg.Ambiguity amb = new TypeArg.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public TypeArg.Default makeDefaultTypeArg(ITree tree, Type type) { 
TypeArg.Default x = new TypeArg.Default(tree, type);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StructuredType.Tuple makeTupleStructuredType(ITree tree, TypeArg first, List<TypeArg> rest) { 
StructuredType.Tuple x = new StructuredType.Tuple(tree, first, rest);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StructuredType.Relation makeRelationStructuredType(ITree tree, TypeArg first, List<TypeArg> rest) { 
StructuredType.Relation x = new StructuredType.Relation(tree, first, rest);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StructuredType.Map makeMapStructuredType(ITree tree, TypeArg first, TypeArg second) { 
StructuredType.Map x = new StructuredType.Map(tree, first, second);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StructuredType.Set makeSetStructuredType(ITree tree, TypeArg typeArg) { 
StructuredType.Set x = new StructuredType.Set(tree, typeArg);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public StructuredType.Ambiguity makeStructuredTypeAmbiguity(List<StructuredType> alternatives) { 
StructuredType.Ambiguity amb = new StructuredType.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public StructuredType.List makeListStructuredType(ITree tree, TypeArg typeArg) { 
StructuredType.List x = new StructuredType.List(tree, typeArg);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public FunctionType.Ambiguity makeFunctionTypeAmbiguity(List<FunctionType> alternatives) { 
FunctionType.Ambiguity amb = new FunctionType.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public FunctionType.TypeArguments makeTypeArgumentsFunctionType(ITree tree, Type type, List<TypeArg> arguments) { 
FunctionType.TypeArguments x = new FunctionType.TypeArguments(tree, type, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public TypeVar.Bounded makeBoundedTypeVar(ITree tree, Name name, Type bound) { 
TypeVar.Bounded x = new TypeVar.Bounded(tree, name, bound);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public TypeVar.Ambiguity makeTypeVarAmbiguity(List<TypeVar> alternatives) { 
TypeVar.Ambiguity amb = new TypeVar.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public TypeVar.Free makeFreeTypeVar(ITree tree, Name name) { 
TypeVar.Free x = new TypeVar.Free(tree, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public UserType.Parametric makeParametricUserType(ITree tree, Name name, List<TypeVar> parameters) { 
UserType.Parametric x = new UserType.Parametric(tree, name, parameters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public UserType.Ambiguity makeUserTypeAmbiguity(List<UserType> alternatives) { 
UserType.Ambiguity amb = new UserType.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public UserType.Name makeNameUserType(ITree tree, Name name) { 
UserType.Name x = new UserType.Name(tree, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public DataTypeSelector.Ambiguity makeDataTypeSelectorAmbiguity(List<DataTypeSelector> alternatives) { 
DataTypeSelector.Ambiguity amb = new DataTypeSelector.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public DataTypeSelector.Selector makeSelectorDataTypeSelector(ITree tree, Name sort, Name production) { 
DataTypeSelector.Selector x = new DataTypeSelector.Selector(tree, sort, production);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Type.Selector makeSelectorType(ITree tree, DataTypeSelector selector) { 
Type.Selector x = new Type.Selector(tree, selector);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Type.Symbol makeSymbolType(ITree tree, Symbol symbol) { 
Type.Symbol x = new Type.Symbol(tree, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Type.User makeUserType(ITree tree, UserType user) { 
Type.User x = new Type.User(tree, user);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Type.Variable makeVariableType(ITree tree, TypeVar typeVar) { 
Type.Variable x = new Type.Variable(tree, typeVar);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Type.Function makeFunctionType(ITree tree, FunctionType function) { 
Type.Function x = new Type.Function(tree, function);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Type.Structured makeStructuredType(ITree tree, StructuredType structured) { 
Type.Structured x = new Type.Structured(tree, structured);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Type.Ambiguity makeTypeAmbiguity(List<Type> alternatives) { 
Type.Ambiguity amb = new Type.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Type.Basic makeBasicType(ITree tree, BasicType basic) { 
Type.Basic x = new Type.Basic(tree, basic);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Sort.more-chars makemore-charsSort(ITree tree, List<get-sort-from-symbol([A-Z])> head, List<get-sort-from-symbol([A-Za-z0-9\-]*)> middle, List<get-sort-from-symbol([A-Za-z0-9])> last) { 
Sort.more-chars x = new Sort.more-chars(tree, head, middle, last);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Sort.Ambiguity makeSortAmbiguity(List<Sort> alternatives) { 
Sort.Ambiguity amb = new Sort.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Sort.one-char makeone-charSort(ITree tree, List<get-sort-from-symbol([A-Z])> head) { 
Sort.one-char x = new Sort.one-char(tree, head);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.CaseInsensitiveLiteral makeCaseInsensitiveLiteralSymbol(ITree tree, SingleQuotedStrCon singelQuotedString) { 
Symbol.CaseInsensitiveLiteral x = new Symbol.CaseInsensitiveLiteral(tree, singelQuotedString);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.Literal makeLiteralSymbol(ITree tree, StrCon string) { 
Symbol.Literal x = new Symbol.Literal(tree, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.LiftedSymbol makeLiftedSymbolSymbol(ITree tree, Symbol symbol) { 
Symbol.LiftedSymbol x = new Symbol.LiftedSymbol(tree, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.CharacterClass makeCharacterClassSymbol(ITree tree, CharClass charClass) { 
Symbol.CharacterClass x = new Symbol.CharacterClass(tree, charClass);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.Alternative makeAlternativeSymbol(ITree tree, Symbol lhs, Symbol rhs) { 
Symbol.Alternative x = new Symbol.Alternative(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.IterStarSep makeIterStarSepSymbol(ITree tree, Symbol symbol, StrCon sep) { 
Symbol.IterStarSep x = new Symbol.IterStarSep(tree, symbol, sep);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.IterSep makeIterSepSymbol(ITree tree, Symbol symbol, StrCon sep) { 
Symbol.IterSep x = new Symbol.IterSep(tree, symbol, sep);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.IterStar makeIterStarSymbol(ITree tree, Symbol symbol) { 
Symbol.IterStar x = new Symbol.IterStar(tree, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.Iter makeIterSymbol(ITree tree, Symbol symbol) { 
Symbol.Iter x = new Symbol.Iter(tree, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.Optional makeOptionalSymbol(ITree tree, Symbol symbol) { 
Symbol.Optional x = new Symbol.Optional(tree, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.Sequence makeSequenceSymbol(ITree tree, Symbol head, List<Symbol> tail) { 
Symbol.Sequence x = new Symbol.Sequence(tree, head, tail);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.Empty makeEmptySymbol(ITree tree) { 
Symbol.Empty x = new Symbol.Empty(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.ParameterizedSort makeParameterizedSortSymbol(ITree tree, Sort sort, List<Symbol> parameters) { 
Symbol.ParameterizedSort x = new Symbol.ParameterizedSort(tree, sort, parameters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Symbol.Ambiguity makeSymbolAmbiguity(List<Symbol> alternatives) { 
Symbol.Ambiguity amb = new Symbol.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Symbol.Sort makeSortSymbol(ITree tree, Sort sort) { 
Symbol.Sort x = new Symbol.Sort(tree, sort);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SingleQuotedStrChar.normal makenormalSingleQuotedStrChar(ITree tree) { 
SingleQuotedStrChar.normal x = new SingleQuotedStrChar.normal(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SingleQuotedStrChar.decimal makedecimalSingleQuotedStrChar(ITree tree, List<get-sort-from-symbol([0-9])> a, List<get-sort-from-symbol([0-9])> b, List<get-sort-from-symbol([0-9])> c) { 
SingleQuotedStrChar.decimal x = new SingleQuotedStrChar.decimal(tree, a, b, c);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SingleQuotedStrChar.backslash makebackslashSingleQuotedStrChar(ITree tree) { 
SingleQuotedStrChar.backslash x = new SingleQuotedStrChar.backslash(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SingleQuotedStrChar.quote makequoteSingleQuotedStrChar(ITree tree) { 
SingleQuotedStrChar.quote x = new SingleQuotedStrChar.quote(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SingleQuotedStrChar.tab maketabSingleQuotedStrChar(ITree tree) { 
SingleQuotedStrChar.tab x = new SingleQuotedStrChar.tab(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SingleQuotedStrChar.Ambiguity makeSingleQuotedStrCharAmbiguity(List<SingleQuotedStrChar> alternatives) { 
SingleQuotedStrChar.Ambiguity amb = new SingleQuotedStrChar.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public SingleQuotedStrChar.newline makenewlineSingleQuotedStrChar(ITree tree) { 
SingleQuotedStrChar.newline x = new SingleQuotedStrChar.newline(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public SingleQuotedStrCon.Ambiguity makeSingleQuotedStrConAmbiguity(List<SingleQuotedStrCon> alternatives) { 
SingleQuotedStrCon.Ambiguity amb = new SingleQuotedStrCon.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public SingleQuotedStrCon.default makedefaultSingleQuotedStrCon(ITree tree, List<SingleQuotedStrChar> chars) { 
SingleQuotedStrCon.default x = new SingleQuotedStrCon.default(tree, chars);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharRange.Range makeRangeCharRange(ITree tree, Character start, Character end) { 
CharRange.Range x = new CharRange.Range(tree, start, end);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharRange.Ambiguity makeCharRangeAmbiguity(List<CharRange> alternatives) { 
CharRange.Ambiguity amb = new CharRange.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public CharRange.Character makeCharacterCharRange(ITree tree, Character character) { 
CharRange.Character x = new CharRange.Character(tree, character);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharRanges.Concatenate makeConcatenateCharRanges(ITree tree, CharRanges lhs, CharRanges rhs) { 
CharRanges.Concatenate x = new CharRanges.Concatenate(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharRanges.Ambiguity makeCharRangesAmbiguity(List<CharRanges> alternatives) { 
CharRanges.Ambiguity amb = new CharRanges.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public CharRanges.Range makeRangeCharRanges(ITree tree, CharRange range) { 
CharRanges.Range x = new CharRanges.Range(tree, range);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public OptCharRanges.Present makePresentOptCharRanges(ITree tree, CharRanges ranges) { 
OptCharRanges.Present x = new OptCharRanges.Present(tree, ranges);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public OptCharRanges.Ambiguity makeOptCharRangesAmbiguity(List<OptCharRanges> alternatives) { 
OptCharRanges.Ambiguity amb = new OptCharRanges.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public OptCharRanges.Absent makeAbsentOptCharRanges(ITree tree) { 
OptCharRanges.Absent x = new OptCharRanges.Absent(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharClass.Union makeUnionCharClass(ITree tree, CharClass lhs, CharClass rhs) { 
CharClass.Union x = new CharClass.Union(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharClass.Intersection makeIntersectionCharClass(ITree tree, CharClass lhs, CharClass rhs) { 
CharClass.Intersection x = new CharClass.Intersection(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharClass.Difference makeDifferenceCharClass(ITree tree, CharClass lhs, CharClass rhs) { 
CharClass.Difference x = new CharClass.Difference(tree, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharClass.Complement makeComplementCharClass(ITree tree, CharClass charClass) { 
CharClass.Complement x = new CharClass.Complement(tree, charClass);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public CharClass.Ambiguity makeCharClassAmbiguity(List<CharClass> alternatives) { 
CharClass.Ambiguity amb = new CharClass.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public CharClass.SimpleCharclass makeSimpleCharclassCharClass(ITree tree, OptCharRanges optionalCharRanges) { 
CharClass.SimpleCharclass x = new CharClass.SimpleCharclass(tree, optionalCharRanges);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public NumChar.Ambiguity makeNumCharAmbiguity(List<NumChar> alternatives) { 
NumChar.Ambiguity amb = new NumChar.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public NumChar.Digits makeDigitsNumChar(ITree tree, List<get-sort-from-symbol([0-9]+)> number) { 
NumChar.Digits x = new NumChar.Digits(tree, number);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ShortChar.Escaped makeEscapedShortChar(ITree tree, List<get-sort-from-symbol(~[\0-\31A-Za-mo-qsu-z0-9])> escape) { 
ShortChar.Escaped x = new ShortChar.Escaped(tree, escape);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ShortChar.Ambiguity makeShortCharAmbiguity(List<ShortChar> alternatives) { 
ShortChar.Ambiguity amb = new ShortChar.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public ShortChar.Regular makeRegularShortChar(ITree tree, List<get-sort-from-symbol([a-zA-Z0-9])> character) { 
ShortChar.Regular x = new ShortChar.Regular(tree, character);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Character.LabelStart makeLabelStartCharacter(ITree tree) { 
Character.LabelStart x = new Character.LabelStart(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Character.Bottom makeBottomCharacter(ITree tree) { 
Character.Bottom x = new Character.Bottom(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Character.EOF makeEOFCharacter(ITree tree) { 
Character.EOF x = new Character.EOF(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Character.Top makeTopCharacter(ITree tree) { 
Character.Top x = new Character.Top(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Character.Short makeShortCharacter(ITree tree, ShortChar short) { 
Character.Short x = new Character.Short(tree, short);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Character.Ambiguity makeCharacterAmbiguity(List<Character> alternatives) { 
Character.Ambiguity amb = new Character.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Character.Numeric makeNumericCharacter(ITree tree, NumChar numeric) { 
Character.Numeric x = new Character.Numeric(tree, numeric);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Module.Ambiguity makeModuleAmbiguity(List<Module> alternatives) { 
Module.Ambiguity amb = new Module.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Module.Module makeModuleModule(ITree tree, Header header, Body body) { 
Module.Module x = new Module.Module(tree, header, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ModuleWord.Ambiguity makeModuleWordAmbiguity(List<ModuleWord> alternatives) { 
ModuleWord.Ambiguity amb = new ModuleWord.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public ModuleWord.Word makeWordModuleWord(ITree tree, List<get-sort-from-symbol([A-Za-z0-9\_\-]+)> letters) { 
ModuleWord.Word x = new ModuleWord.Word(tree, letters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ModuleName.Path makePathModuleName(ITree tree, ModuleWord dirname, List<get-sort-from-symbol("/")> sep, ModuleName basename) { 
ModuleName.Path x = new ModuleName.Path(tree, dirname, sep, basename);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ModuleName.Root makeRootModuleName(ITree tree, List<get-sort-from-symbol("/")> sep, ModuleName basename) { 
ModuleName.Root x = new ModuleName.Root(tree, sep, basename);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ModuleName.Ambiguity makeModuleNameAmbiguity(List<ModuleName> alternatives) { 
ModuleName.Ambiguity amb = new ModuleName.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public ModuleName.Leaf makeLeafModuleName(ITree tree) { 
ModuleName.Leaf x = new ModuleName.Leaf(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ModuleActuals.Ambiguity makeModuleActualsAmbiguity(List<ModuleActuals> alternatives) { 
ModuleActuals.Ambiguity amb = new ModuleActuals.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public ModuleActuals.Actuals makeActualsModuleActuals(ITree tree, List<Type> types) { 
ModuleActuals.Actuals x = new ModuleActuals.Actuals(tree, types);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ImportedModule.Default makeDefaultImportedModule(ITree tree, ModuleName name) { 
ImportedModule.Default x = new ImportedModule.Default(tree, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ImportedModule.Renamings makeRenamingsImportedModule(ITree tree, ModuleName name, Renamings renamings) { 
ImportedModule.Renamings x = new ImportedModule.Renamings(tree, name, renamings);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ImportedModule.Actuals makeActualsImportedModule(ITree tree, ModuleName name, ModuleActuals actuals) { 
ImportedModule.Actuals x = new ImportedModule.Actuals(tree, name, actuals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ImportedModule.Ambiguity makeImportedModuleAmbiguity(List<ImportedModule> alternatives) { 
ImportedModule.Ambiguity amb = new ImportedModule.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public ImportedModule.ActualsRenaming makeActualsRenamingImportedModule(ITree tree, ModuleName name, ModuleActuals actuals, Renamings renamings) { 
ImportedModule.ActualsRenaming x = new ImportedModule.ActualsRenaming(tree, name, actuals, renamings);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Renaming.Ambiguity makeRenamingAmbiguity(List<Renaming> alternatives) { 
Renaming.Ambiguity amb = new Renaming.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Renaming.Renaming makeRenamingRenaming(ITree tree, Name from, Name to) { 
Renaming.Renaming x = new Renaming.Renaming(tree, from, to);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Renamings.Ambiguity makeRenamingsAmbiguity(List<Renamings> alternatives) { 
Renamings.Ambiguity amb = new Renamings.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Renamings.Renamings makeRenamingsRenamings(ITree tree, List<Renaming> renamings) { 
Renamings.Renamings x = new Renamings.Renamings(tree, renamings);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Import.Extend makeExtendImport(ITree tree, ImportedModule module) { 
Import.Extend x = new Import.Extend(tree, module);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Import.Ambiguity makeImportAmbiguity(List<Import> alternatives) { 
Import.Ambiguity amb = new Import.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Import.Import makeImportImport(ITree tree, ImportedModule module) { 
Import.Import x = new Import.Import(tree, module);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ModuleParameters.Ambiguity makeModuleParametersAmbiguity(List<ModuleParameters> alternatives) { 
ModuleParameters.Ambiguity amb = new ModuleParameters.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public ModuleParameters.ModuleParameters makeModuleParametersModuleParameters(ITree tree, List<TypeVar> parameters) { 
ModuleParameters.ModuleParameters x = new ModuleParameters.ModuleParameters(tree, parameters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Header.Parameters makeParametersHeader(ITree tree, ModuleName name, ModuleParameters params, Tags tags, List<Import> imports) { 
Header.Parameters x = new Header.Parameters(tree, name, params, tags, imports);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Header.Ambiguity makeHeaderAmbiguity(List<Header> alternatives) { 
Header.Ambiguity amb = new Header.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Header.Default makeDefaultHeader(ITree tree, ModuleName name, Tags tags, List<Import> imports) { 
Header.Default x = new Header.Default(tree, name, tags, imports);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Name.Ambiguity makeNameAmbiguity(List<Name> alternatives) { 
Name.Ambiguity amb = new Name.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public QualifiedName.Ambiguity makeQualifiedNameAmbiguity(List<QualifiedName> alternatives) { 
QualifiedName.Ambiguity amb = new QualifiedName.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public QualifiedName.Default makeDefaultQualifiedName(ITree tree, List<Name> names) { 
QualifiedName.Default x = new QualifiedName.Default(tree, names);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Area.Ambiguity makeAreaAmbiguity(List<Area> alternatives) { 
Area.Ambiguity amb = new Area.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Area.Area makeAreaArea(ITree tree, Expression beginLine, Expression beginColumn, Expression endLine, Expression endColumn, Expression offset, Expression length) { 
Area.Area x = new Area.Area(tree, beginLine, beginColumn, endLine, endColumn, offset, length);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public TagString.Ambiguity makeTagStringAmbiguity(List<TagString> alternatives) { 
TagString.Ambiguity amb = new TagString.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public TagChar.Ambiguity makeTagCharAmbiguity(List<TagChar> alternatives) { 
TagChar.Ambiguity amb = new TagChar.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Tag.Ambiguity makeTagAmbiguity(List<Tag> alternatives) { 
Tag.Ambiguity amb = new Tag.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Tag.Default makeDefaultTag(ITree tree, Name name) { 
Tag.Default x = new Tag.Default(tree, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Tags.Ambiguity makeTagsAmbiguity(List<Tags> alternatives) { 
Tags.Ambiguity amb = new Tags.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Tags.Default makeDefaultTags(ITree tree, List<Tag> annotations) { 
Tags.Default x = new Tags.Default(tree, annotations);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Pattern.Ambiguity makePatternAmbiguity(List<Pattern> alternatives) { 
Pattern.Ambiguity amb = new Pattern.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Pattern.TypedVariable makeTypedVariablePattern(ITree tree, Type type, Name name) { 
Pattern.TypedVariable x = new Pattern.TypedVariable(tree, type, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ValueProducer.GivenStrategy makeGivenStrategyValueProducer(ITree tree, Strategy strategy, Pattern pattern, Expression expression) { 
ValueProducer.GivenStrategy x = new ValueProducer.GivenStrategy(tree, strategy, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public ValueProducer.Ambiguity makeValueProducerAmbiguity(List<ValueProducer> alternatives) { 
ValueProducer.Ambiguity amb = new ValueProducer.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public ValueProducer.DefaultStrategy makeDefaultStrategyValueProducer(ITree tree, Pattern pattern, Expression expression) { 
ValueProducer.DefaultStrategy x = new ValueProducer.DefaultStrategy(tree, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Generator.Producer makeProducerGenerator(ITree tree, ValueProducer producer) { 
Generator.Producer x = new Generator.Producer(tree, producer);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Generator.Ambiguity makeGeneratorAmbiguity(List<Generator> alternatives) { 
Generator.Ambiguity amb = new Generator.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Generator.Expression makeExpressionGenerator(ITree tree, Expression expression) { 
Generator.Expression x = new Generator.Expression(tree, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Strategy.Innermost makeInnermostStrategy(ITree tree) { 
Strategy.Innermost x = new Strategy.Innermost(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Strategy.Outermost makeOutermostStrategy(ITree tree) { 
Strategy.Outermost x = new Strategy.Outermost(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Strategy.BottomUpBreak makeBottomUpBreakStrategy(ITree tree) { 
Strategy.BottomUpBreak x = new Strategy.BottomUpBreak(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Strategy.BottomUp makeBottomUpStrategy(ITree tree) { 
Strategy.BottomUp x = new Strategy.BottomUp(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Strategy.TopDownBreak makeTopDownBreakStrategy(ITree tree) { 
Strategy.TopDownBreak x = new Strategy.TopDownBreak(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Strategy.Ambiguity makeStrategyAmbiguity(List<Strategy> alternatives) { 
Strategy.Ambiguity amb = new Strategy.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Strategy.TopDown makeTopDownStrategy(ITree tree) { 
Strategy.TopDown x = new Strategy.TopDown(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Comprehension.List makeListComprehension(ITree tree, Expression result, List<Generator> generators) { 
Comprehension.List x = new Comprehension.List(tree, result, generators);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Comprehension.Ambiguity makeComprehensionAmbiguity(List<Comprehension> alternatives) { 
Comprehension.Ambiguity amb = new Comprehension.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Comprehension.Set makeSetComprehension(ITree tree, Expression result, List<Generator> generators) { 
Comprehension.Set x = new Comprehension.Set(tree, result, generators);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Match.Arbitrary makeArbitraryMatch(ITree tree, Pattern match, Statement statement) { 
Match.Arbitrary x = new Match.Arbitrary(tree, match, statement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Match.Ambiguity makeMatchAmbiguity(List<Match> alternatives) { 
Match.Ambiguity amb = new Match.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Match.Replacing makeReplacingMatch(ITree tree, Pattern match, Expression replacement) { 
Match.Replacing x = new Match.Replacing(tree, match, replacement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Rule.NoGuard makeNoGuardRule(ITree tree, Match match) { 
Rule.NoGuard x = new Rule.NoGuard(tree, match);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Rule.Ambiguity makeRuleAmbiguity(List<Rule> alternatives) { 
Rule.Ambiguity amb = new Rule.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Rule.WithGuard makeWithGuardRule(ITree tree, Type type, Match match) { 
Rule.WithGuard x = new Rule.WithGuard(tree, type, match);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Case.Default makeDefaultCase(ITree tree, Statement statement) { 
Case.Default x = new Case.Default(tree, statement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Case.Ambiguity makeCaseAmbiguity(List<Case> alternatives) { 
Case.Ambiguity amb = new Case.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Case.Rule makeRuleCase(ITree tree, Rule rule) { 
Case.Rule x = new Case.Rule(tree, rule);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Visit.GivenStrategy makeGivenStrategyVisit(ITree tree, Strategy strategy, Expression subject, List<Case> cases) { 
Visit.GivenStrategy x = new Visit.GivenStrategy(tree, strategy, subject, cases);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public Visit.Ambiguity makeVisitAmbiguity(List<Visit> alternatives) { 
Visit.Ambiguity amb = new Visit.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public Visit.DefaultStrategy makeDefaultStrategyVisit(ITree tree, Expression subject, List<Case> cases) { 
Visit.DefaultStrategy x = new Visit.DefaultStrategy(tree, subject, cases);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public UnicodeEscape.Ambiguity makeUnicodeEscapeAmbiguity(List<UnicodeEscape> alternatives) { 
UnicodeEscape.Ambiguity amb = new UnicodeEscape.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public DecimalIntegerLiteral.Ambiguity makeDecimalIntegerLiteralAmbiguity(List<DecimalIntegerLiteral> alternatives) { 
DecimalIntegerLiteral.Ambiguity amb = new DecimalIntegerLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public HexIntegerLiteral.Ambiguity makeHexIntegerLiteralAmbiguity(List<HexIntegerLiteral> alternatives) { 
HexIntegerLiteral.Ambiguity amb = new HexIntegerLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public OctalIntegerLiteral.Ambiguity makeOctalIntegerLiteralAmbiguity(List<OctalIntegerLiteral> alternatives) { 
OctalIntegerLiteral.Ambiguity amb = new OctalIntegerLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public DecimalLongLiteral.Ambiguity makeDecimalLongLiteralAmbiguity(List<DecimalLongLiteral> alternatives) { 
DecimalLongLiteral.Ambiguity amb = new DecimalLongLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public HexLongLiteral.Ambiguity makeHexLongLiteralAmbiguity(List<HexLongLiteral> alternatives) { 
HexLongLiteral.Ambiguity amb = new HexLongLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public OctalLongLiteral.Ambiguity makeOctalLongLiteralAmbiguity(List<OctalLongLiteral> alternatives) { 
OctalLongLiteral.Ambiguity amb = new OctalLongLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public FloatingPointLiteral.Ambiguity makeFloatingPointLiteralAmbiguity(List<FloatingPointLiteral> alternatives) { 
FloatingPointLiteral.Ambiguity amb = new FloatingPointLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public DoubleLiteral.Ambiguity makeDoubleLiteralAmbiguity(List<DoubleLiteral> alternatives) { 
DoubleLiteral.Ambiguity amb = new DoubleLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public BooleanLiteral.Ambiguity makeBooleanLiteralAmbiguity(List<BooleanLiteral> alternatives) { 
BooleanLiteral.Ambiguity amb = new BooleanLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public SingleCharacter.Ambiguity makeSingleCharacterAmbiguity(List<SingleCharacter> alternatives) { 
SingleCharacter.Ambiguity amb = new SingleCharacter.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public CharacterLiteral.Ambiguity makeCharacterLiteralAmbiguity(List<CharacterLiteral> alternatives) { 
CharacterLiteral.Ambiguity amb = new CharacterLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public EscapeSequence.Ambiguity makeEscapeSequenceAmbiguity(List<EscapeSequence> alternatives) { 
EscapeSequence.Ambiguity amb = new EscapeSequence.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public StringCharacter.Ambiguity makeStringCharacterAmbiguity(List<StringCharacter> alternatives) { 
StringCharacter.Ambiguity amb = new StringCharacter.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public StringLiteral.Ambiguity makeStringLiteralAmbiguity(List<StringLiteral> alternatives) { 
StringLiteral.Ambiguity amb = new StringLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public IntegerLiteral.OctalIntegerLiteral makeOctalIntegerLiteralIntegerLiteral(ITree tree) { 
IntegerLiteral.OctalIntegerLiteral x = new IntegerLiteral.OctalIntegerLiteral(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public IntegerLiteral.HexIntegerLiteral makeHexIntegerLiteralIntegerLiteral(ITree tree) { 
IntegerLiteral.HexIntegerLiteral x = new IntegerLiteral.HexIntegerLiteral(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public IntegerLiteral.Ambiguity makeIntegerLiteralAmbiguity(List<IntegerLiteral> alternatives) { 
IntegerLiteral.Ambiguity amb = new IntegerLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public IntegerLiteral.DecimalIntegerLiteral makeDecimalIntegerLiteralIntegerLiteral(ITree tree) { 
IntegerLiteral.DecimalIntegerLiteral x = new IntegerLiteral.DecimalIntegerLiteral(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public LongLiteral.OctalLongLiteral makeOctalLongLiteralLongLiteral(ITree tree) { 
LongLiteral.OctalLongLiteral x = new LongLiteral.OctalLongLiteral(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public LongLiteral.HexLongLiteral makeHexLongLiteralLongLiteral(ITree tree) { 
LongLiteral.HexLongLiteral x = new LongLiteral.HexLongLiteral(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
public LongLiteral.Ambiguity makeLongLiteralAmbiguity(List<LongLiteral> alternatives) { 
LongLiteral.Ambiguity amb = new LongLiteral.Ambiguity(alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return table.get(amb, amb); 
}
public LongLiteral.DecimalLongLiteral makeDecimalLongLiteralLongLiteral(ITree tree) { 
LongLiteral.DecimalLongLiteral x = new LongLiteral.DecimalLongLiteral(tree);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return table.get(x); 
}
}