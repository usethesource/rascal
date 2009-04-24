package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public class ASTFactory {
java.util.Map<AbstractAST,AbstractAST> table = new java.util.Hashtable<AbstractAST,AbstractAST>();

public org.meta_environment.rascal.ast.Body.Ambiguity makeBodyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Body> alternatives) { 
org.meta_environment.rascal.ast.Body.Ambiguity amb = new org.meta_environment.rascal.ast.Body.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Body.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Body.Toplevels makeBodyToplevels(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels) { 
org.meta_environment.rascal.ast.Body.Toplevels x = new org.meta_environment.rascal.ast.Body.Toplevels(node, toplevels);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Body.Toplevels)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Lexical makeExpressionLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Expression.Lexical x = new org.meta_environment.rascal.ast.Expression.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Or makeExpressionOr(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Or x = new org.meta_environment.rascal.ast.Expression.Or(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Or)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.And makeExpressionAnd(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.And x = new org.meta_environment.rascal.ast.Expression.And(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.And)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Equivalence makeExpressionEquivalence(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Equivalence x = new org.meta_environment.rascal.ast.Expression.Equivalence(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Equivalence)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Implication makeExpressionImplication(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Implication x = new org.meta_environment.rascal.ast.Expression.Implication(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Implication)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.NonEquals makeExpressionNonEquals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.NonEquals x = new org.meta_environment.rascal.ast.Expression.NonEquals(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.NonEquals)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.GreaterThanOrEq makeExpressionGreaterThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.GreaterThanOrEq x = new org.meta_environment.rascal.ast.Expression.GreaterThanOrEq(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.GreaterThanOrEq)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.GreaterThan makeExpressionGreaterThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.GreaterThan x = new org.meta_environment.rascal.ast.Expression.GreaterThan(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.GreaterThan)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.LessThanOrEq makeExpressionLessThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.LessThanOrEq x = new org.meta_environment.rascal.ast.Expression.LessThanOrEq(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.LessThanOrEq)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.LessThan makeExpressionLessThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.LessThan x = new org.meta_environment.rascal.ast.Expression.LessThan(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.LessThan)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.In makeExpressionIn(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.In x = new org.meta_environment.rascal.ast.Expression.In(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.In)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.NotIn makeExpressionNotIn(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.NotIn x = new org.meta_environment.rascal.ast.Expression.NotIn(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.NotIn)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Subtraction makeExpressionSubtraction(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Subtraction x = new org.meta_environment.rascal.ast.Expression.Subtraction(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Subtraction)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Addition makeExpressionAddition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Addition x = new org.meta_environment.rascal.ast.Expression.Addition(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Addition)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Intersection makeExpressionIntersection(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Intersection x = new org.meta_environment.rascal.ast.Expression.Intersection(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Intersection)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Modulo makeExpressionModulo(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Modulo x = new org.meta_environment.rascal.ast.Expression.Modulo(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Modulo)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Division makeExpressionDivision(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Division x = new org.meta_environment.rascal.ast.Expression.Division(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Division)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Join makeExpressionJoin(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Join x = new org.meta_environment.rascal.ast.Expression.Join(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Join)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Product makeExpressionProduct(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Product x = new org.meta_environment.rascal.ast.Expression.Product(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Product)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Composition makeExpressionComposition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Composition x = new org.meta_environment.rascal.ast.Expression.Composition(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Composition)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.SetAnnotation makeExpressionSetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression value) { 
org.meta_environment.rascal.ast.Expression.SetAnnotation x = new org.meta_environment.rascal.ast.Expression.SetAnnotation(node, expression, name, value);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.SetAnnotation)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.GetAnnotation makeExpressionGetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Expression.GetAnnotation x = new org.meta_environment.rascal.ast.Expression.GetAnnotation(node, expression, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.GetAnnotation)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.TransitiveClosure makeExpressionTransitiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.TransitiveClosure x = new org.meta_environment.rascal.ast.Expression.TransitiveClosure(node, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.TransitiveClosure)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure makeExpressionTransitiveReflexiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure x = new org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure(node, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Negative makeExpressionNegative(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.Negative x = new org.meta_environment.rascal.ast.Expression.Negative(node, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Negative)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Negation makeExpressionNegation(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.Negation x = new org.meta_environment.rascal.ast.Expression.Negation(node, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Negation)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.IsDefined makeExpressionIsDefined(INode node, org.meta_environment.rascal.ast.Expression argument) { 
org.meta_environment.rascal.ast.Expression.IsDefined x = new org.meta_environment.rascal.ast.Expression.IsDefined(node, argument);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.IsDefined)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Subscript makeExpressionSubscript(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Expression> subscripts) { 
org.meta_environment.rascal.ast.Expression.Subscript x = new org.meta_environment.rascal.ast.Expression.Subscript(node, expression, subscripts);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Subscript)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.FieldProject makeExpressionFieldProject(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Field> fields) { 
org.meta_environment.rascal.ast.Expression.FieldProject x = new org.meta_environment.rascal.ast.Expression.FieldProject(node, expression, fields);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.FieldProject)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.FieldAccess makeExpressionFieldAccess(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name field) { 
org.meta_environment.rascal.ast.Expression.FieldAccess x = new org.meta_environment.rascal.ast.Expression.FieldAccess(node, expression, field);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.FieldAccess)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.FieldUpdate makeExpressionFieldUpdate(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name key, org.meta_environment.rascal.ast.Expression replacement) { 
org.meta_environment.rascal.ast.Expression.FieldUpdate x = new org.meta_environment.rascal.ast.Expression.FieldUpdate(node, expression, key, replacement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.FieldUpdate)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.ClosureCall makeExpressionClosureCall(INode node, org.meta_environment.rascal.ast.ClosureAsFunction closure, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) { 
org.meta_environment.rascal.ast.Expression.ClosureCall x = new org.meta_environment.rascal.ast.Expression.ClosureCall(node, closure, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.ClosureCall)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.FunctionAsValue makeExpressionFunctionAsValue(INode node, org.meta_environment.rascal.ast.FunctionAsValue function) { 
org.meta_environment.rascal.ast.Expression.FunctionAsValue x = new org.meta_environment.rascal.ast.Expression.FunctionAsValue(node, function);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.FunctionAsValue)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.OperatorAsValue makeExpressionOperatorAsValue(INode node, org.meta_environment.rascal.ast.OperatorAsValue operator) { 
org.meta_environment.rascal.ast.Expression.OperatorAsValue x = new org.meta_environment.rascal.ast.Expression.OperatorAsValue(node, operator);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.OperatorAsValue)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.StepRange makeExpressionStepRange(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression second, org.meta_environment.rascal.ast.Expression last) { 
org.meta_environment.rascal.ast.Expression.StepRange x = new org.meta_environment.rascal.ast.Expression.StepRange(node, first, second, last);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.StepRange)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Range makeExpressionRange(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression last) { 
org.meta_environment.rascal.ast.Expression.Range x = new org.meta_environment.rascal.ast.Expression.Range(node, first, last);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Range)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Bracket makeExpressionBracket(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.Bracket x = new org.meta_environment.rascal.ast.Expression.Bracket(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Bracket)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.VoidClosure makeExpressionVoidClosure(INode node, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Expression.VoidClosure x = new org.meta_environment.rascal.ast.Expression.VoidClosure(node, parameters, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.VoidClosure)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Closure makeExpressionClosure(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Expression.Closure x = new org.meta_environment.rascal.ast.Expression.Closure(node, type, parameters, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Closure)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Any makeExpressionAny(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Expression.Any x = new org.meta_environment.rascal.ast.Expression.Any(node, generators);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Any)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.All makeExpressionAll(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Expression.All x = new org.meta_environment.rascal.ast.Expression.All(node, generators);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.All)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Comprehension makeExpressionComprehension(INode node, org.meta_environment.rascal.ast.Comprehension comprehension) { 
org.meta_environment.rascal.ast.Expression.Comprehension x = new org.meta_environment.rascal.ast.Expression.Comprehension(node, comprehension);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Comprehension)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Equals makeExpressionEquals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.Equals x = new org.meta_environment.rascal.ast.Expression.Equals(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Equals)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy makeExpressionEnumeratorWithStrategy(INode node, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy x = new org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy(node, strategy, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Enumerator makeExpressionEnumerator(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.Enumerator x = new org.meta_environment.rascal.ast.Expression.Enumerator(node, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Enumerator)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.NoMatch makeExpressionNoMatch(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.NoMatch x = new org.meta_environment.rascal.ast.Expression.NoMatch(node, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.NoMatch)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Match makeExpressionMatch(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Expression.Match x = new org.meta_environment.rascal.ast.Expression.Match(node, pattern, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Match)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise makeExpressionIfDefinedOtherwise(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise x = new org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.IfThenElse makeExpressionIfThenElse(INode node, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Expression thenExp, org.meta_environment.rascal.ast.Expression elseExp) { 
org.meta_environment.rascal.ast.Expression.IfThenElse x = new org.meta_environment.rascal.ast.Expression.IfThenElse(node, condition, thenExp, elseExp);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.IfThenElse)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.TypedVariable makeExpressionTypedVariable(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Expression.TypedVariable x = new org.meta_environment.rascal.ast.Expression.TypedVariable(node, type, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.TypedVariable)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.QualifiedName makeExpressionQualifiedName(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
org.meta_environment.rascal.ast.Expression.QualifiedName x = new org.meta_environment.rascal.ast.Expression.QualifiedName(node, qualifiedName);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.QualifiedName)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Location makeExpressionLocation(INode node, org.meta_environment.rascal.ast.URL url, org.meta_environment.rascal.ast.Expression offset, org.meta_environment.rascal.ast.Expression length, org.meta_environment.rascal.ast.Expression beginLine, org.meta_environment.rascal.ast.Expression beginColumn, org.meta_environment.rascal.ast.Expression endLine, org.meta_environment.rascal.ast.Expression endColumn) { 
org.meta_environment.rascal.ast.Expression.Location x = new org.meta_environment.rascal.ast.Expression.Location(node, url, offset, length, beginLine, beginColumn, endLine, endColumn);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Location)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Map makeExpressionMap(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> mappings) { 
org.meta_environment.rascal.ast.Expression.Map x = new org.meta_environment.rascal.ast.Expression.Map(node, mappings);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Map)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Tuple makeExpressionTuple(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
org.meta_environment.rascal.ast.Expression.Tuple x = new org.meta_environment.rascal.ast.Expression.Tuple(node, elements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Tuple)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Set makeExpressionSet(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
org.meta_environment.rascal.ast.Expression.Set x = new org.meta_environment.rascal.ast.Expression.Set(node, elements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Set)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.List makeExpressionList(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
org.meta_environment.rascal.ast.Expression.List x = new org.meta_environment.rascal.ast.Expression.List(node, elements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.List)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.CallOrTree makeExpressionCallOrTree(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) { 
org.meta_environment.rascal.ast.Expression.CallOrTree x = new org.meta_environment.rascal.ast.Expression.CallOrTree(node, qualifiedName, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.CallOrTree)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Literal makeExpressionLiteral(INode node, org.meta_environment.rascal.ast.Literal literal) { 
org.meta_environment.rascal.ast.Expression.Literal x = new org.meta_environment.rascal.ast.Expression.Literal(node, literal);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Literal)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Visit makeExpressionVisit(INode node, org.meta_environment.rascal.ast.Visit visit) { 
org.meta_environment.rascal.ast.Expression.Visit x = new org.meta_environment.rascal.ast.Expression.Visit(node, visit);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.Visit)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.NonEmptyBlock makeExpressionNonEmptyBlock(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Expression.NonEmptyBlock x = new org.meta_environment.rascal.ast.Expression.NonEmptyBlock(node, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.NonEmptyBlock)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.TypedVariableBecomes makeExpressionTypedVariableBecomes(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression pattern) { 
org.meta_environment.rascal.ast.Expression.TypedVariableBecomes x = new org.meta_environment.rascal.ast.Expression.TypedVariableBecomes(node, type, name, pattern);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.TypedVariableBecomes)table.get(x); 
}
public org.meta_environment.rascal.ast.Expression.Ambiguity makeExpressionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> alternatives) { 
org.meta_environment.rascal.ast.Expression.Ambiguity amb = new org.meta_environment.rascal.ast.Expression.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Expression.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Expression.VariableBecomes makeExpressionVariableBecomes(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression pattern) { 
org.meta_environment.rascal.ast.Expression.VariableBecomes x = new org.meta_environment.rascal.ast.Expression.VariableBecomes(node, name, pattern);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Expression.VariableBecomes)table.get(x); 
}
public org.meta_environment.rascal.ast.Comment.Ambiguity makeCommentAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Comment> alternatives) { 
org.meta_environment.rascal.ast.Comment.Ambiguity amb = new org.meta_environment.rascal.ast.Comment.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Comment.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Comment.Lexical makeCommentLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Comment.Lexical x = new org.meta_environment.rascal.ast.Comment.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Comment.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.CommentChar.Ambiguity makeCommentCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives) { 
org.meta_environment.rascal.ast.CommentChar.Ambiguity amb = new org.meta_environment.rascal.ast.CommentChar.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.CommentChar.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.CommentChar.Lexical makeCommentCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.CommentChar.Lexical x = new org.meta_environment.rascal.ast.CommentChar.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CommentChar.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Asterisk.Ambiguity makeAsteriskAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Asterisk> alternatives) { 
org.meta_environment.rascal.ast.Asterisk.Ambiguity amb = new org.meta_environment.rascal.ast.Asterisk.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Asterisk.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Asterisk.Lexical makeAsteriskLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Asterisk.Lexical x = new org.meta_environment.rascal.ast.Asterisk.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Asterisk.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Name.Ambiguity makeNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Name> alternatives) { 
org.meta_environment.rascal.ast.Name.Ambiguity amb = new org.meta_environment.rascal.ast.Name.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Name.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Name.Lexical makeNameLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Name.Lexical x = new org.meta_environment.rascal.ast.Name.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Name.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.EscapedName.Ambiguity makeEscapedNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.EscapedName> alternatives) { 
org.meta_environment.rascal.ast.EscapedName.Ambiguity amb = new org.meta_environment.rascal.ast.EscapedName.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.EscapedName.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.EscapedName.Lexical makeEscapedNameLexical(INode node, String string) { 
org.meta_environment.rascal.ast.EscapedName.Lexical x = new org.meta_environment.rascal.ast.EscapedName.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.EscapedName.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.QualifiedName.Ambiguity makeQualifiedNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives) { 
org.meta_environment.rascal.ast.QualifiedName.Ambiguity amb = new org.meta_environment.rascal.ast.QualifiedName.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.QualifiedName.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.QualifiedName.Default makeQualifiedNameDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Name> names) { 
org.meta_environment.rascal.ast.QualifiedName.Default x = new org.meta_environment.rascal.ast.QualifiedName.Default(node, names);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.QualifiedName.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Bound.Default makeBoundDefault(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Bound.Default x = new org.meta_environment.rascal.ast.Bound.Default(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Bound.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Bound.Ambiguity makeBoundAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Bound> alternatives) { 
org.meta_environment.rascal.ast.Bound.Ambiguity amb = new org.meta_environment.rascal.ast.Bound.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Bound.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Bound.Empty makeBoundEmpty(INode node) { 
org.meta_environment.rascal.ast.Bound.Empty x = new org.meta_environment.rascal.ast.Bound.Empty(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Bound.Empty)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.GlobalDirective makeStatementGlobalDirective(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.QualifiedName> names) { 
org.meta_environment.rascal.ast.Statement.GlobalDirective x = new org.meta_environment.rascal.ast.Statement.GlobalDirective(node, type, names);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.GlobalDirective)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.VariableDeclaration makeStatementVariableDeclaration(INode node, org.meta_environment.rascal.ast.LocalVariableDeclaration declaration) { 
org.meta_environment.rascal.ast.Statement.VariableDeclaration x = new org.meta_environment.rascal.ast.Statement.VariableDeclaration(node, declaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.VariableDeclaration)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.FunctionDeclaration makeStatementFunctionDeclaration(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) { 
org.meta_environment.rascal.ast.Statement.FunctionDeclaration x = new org.meta_environment.rascal.ast.Statement.FunctionDeclaration(node, functionDeclaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.FunctionDeclaration)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Block makeStatementBlock(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.Statement.Block x = new org.meta_environment.rascal.ast.Statement.Block(node, label, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Block)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.TryFinally makeStatementTryFinally(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers, org.meta_environment.rascal.ast.Statement finallyBody) { 
org.meta_environment.rascal.ast.Statement.TryFinally x = new org.meta_environment.rascal.ast.Statement.TryFinally(node, body, handlers, finallyBody);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.TryFinally)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Try makeStatementTry(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers) { 
org.meta_environment.rascal.ast.Statement.Try x = new org.meta_environment.rascal.ast.Statement.Try(node, body, handlers);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Try)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Throw makeStatementThrow(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Throw x = new org.meta_environment.rascal.ast.Statement.Throw(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Throw)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Insert makeStatementInsert(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Insert x = new org.meta_environment.rascal.ast.Statement.Insert(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Insert)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.AssertWithMessage makeStatementAssertWithMessage(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringLiteral message) { 
org.meta_environment.rascal.ast.Statement.AssertWithMessage x = new org.meta_environment.rascal.ast.Statement.AssertWithMessage(node, expression, message);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.AssertWithMessage)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Assert makeStatementAssert(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Assert x = new org.meta_environment.rascal.ast.Statement.Assert(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Assert)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Continue makeStatementContinue(INode node) { 
org.meta_environment.rascal.ast.Statement.Continue x = new org.meta_environment.rascal.ast.Statement.Continue(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Continue)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Return makeStatementReturn(INode node, org.meta_environment.rascal.ast.Return ret) { 
org.meta_environment.rascal.ast.Statement.Return x = new org.meta_environment.rascal.ast.Statement.Return(node, ret);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Return)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Fail makeStatementFail(INode node, org.meta_environment.rascal.ast.Fail fail) { 
org.meta_environment.rascal.ast.Statement.Fail x = new org.meta_environment.rascal.ast.Statement.Fail(node, fail);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Fail)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Break makeStatementBreak(INode node, org.meta_environment.rascal.ast.Break brk) { 
org.meta_environment.rascal.ast.Statement.Break x = new org.meta_environment.rascal.ast.Statement.Break(node, brk);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Break)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Assignment makeStatementAssignment(INode node, org.meta_environment.rascal.ast.Assignable assignable, org.meta_environment.rascal.ast.Assignment operator, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Assignment x = new org.meta_environment.rascal.ast.Statement.Assignment(node, assignable, operator, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Assignment)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Visit makeStatementVisit(INode node, org.meta_environment.rascal.ast.Visit visit) { 
org.meta_environment.rascal.ast.Statement.Visit x = new org.meta_environment.rascal.ast.Statement.Visit(node, visit);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Visit)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Expression makeStatementExpression(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Statement.Expression x = new org.meta_environment.rascal.ast.Statement.Expression(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Expression)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.EmptyStatement makeStatementEmptyStatement(INode node) { 
org.meta_environment.rascal.ast.Statement.EmptyStatement x = new org.meta_environment.rascal.ast.Statement.EmptyStatement(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.EmptyStatement)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Switch makeStatementSwitch(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
org.meta_environment.rascal.ast.Statement.Switch x = new org.meta_environment.rascal.ast.Statement.Switch(node, label, expression, cases);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Switch)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.IfThen makeStatementIfThen(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.NoElseMayFollow noElseMayFollow) { 
org.meta_environment.rascal.ast.Statement.IfThen x = new org.meta_environment.rascal.ast.Statement.IfThen(node, label, conditions, thenStatement, noElseMayFollow);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.IfThen)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.IfThenElse makeStatementIfThenElse(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.Statement elseStatement) { 
org.meta_environment.rascal.ast.Statement.IfThenElse x = new org.meta_environment.rascal.ast.Statement.IfThenElse(node, label, conditions, thenStatement, elseStatement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.IfThenElse)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.DoWhile makeStatementDoWhile(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Statement body, org.meta_environment.rascal.ast.Expression condition) { 
org.meta_environment.rascal.ast.Statement.DoWhile x = new org.meta_environment.rascal.ast.Statement.DoWhile(node, label, body, condition);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.DoWhile)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.While makeStatementWhile(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Statement.While x = new org.meta_environment.rascal.ast.Statement.While(node, label, condition, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.While)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.For makeStatementFor(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> generators, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Statement.For x = new org.meta_environment.rascal.ast.Statement.For(node, label, generators, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.For)table.get(x); 
}
public org.meta_environment.rascal.ast.Statement.Ambiguity makeStatementAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> alternatives) { 
org.meta_environment.rascal.ast.Statement.Ambiguity amb = new org.meta_environment.rascal.ast.Statement.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Statement.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Statement.Solve makeStatementSolve(INode node, java.util.List<org.meta_environment.rascal.ast.Declarator> declarations, org.meta_environment.rascal.ast.Bound bound, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Statement.Solve x = new org.meta_environment.rascal.ast.Statement.Solve(node, declarations, bound, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Statement.Solve)table.get(x); 
}
public org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity makeNoElseMayFollowAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NoElseMayFollow> alternatives) { 
org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity amb = new org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.NoElseMayFollow.Default makeNoElseMayFollowDefault(INode node) { 
org.meta_environment.rascal.ast.NoElseMayFollow.Default x = new org.meta_environment.rascal.ast.NoElseMayFollow.Default(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.NoElseMayFollow.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignable.Constructor makeAssignableConstructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Assignable> arguments) { 
org.meta_environment.rascal.ast.Assignable.Constructor x = new org.meta_environment.rascal.ast.Assignable.Constructor(node, name, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignable.Constructor)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignable.Tuple makeAssignableTuple(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> elements) { 
org.meta_environment.rascal.ast.Assignable.Tuple x = new org.meta_environment.rascal.ast.Assignable.Tuple(node, elements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignable.Tuple)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignable.Annotation makeAssignableAnnotation(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name annotation) { 
org.meta_environment.rascal.ast.Assignable.Annotation x = new org.meta_environment.rascal.ast.Assignable.Annotation(node, receiver, annotation);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignable.Annotation)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault makeAssignableIfDefinedOrDefault(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression defaultExpression) { 
org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault x = new org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault(node, receiver, defaultExpression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignable.FieldAccess makeAssignableFieldAccess(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name field) { 
org.meta_environment.rascal.ast.Assignable.FieldAccess x = new org.meta_environment.rascal.ast.Assignable.FieldAccess(node, receiver, field);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignable.FieldAccess)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignable.Subscript makeAssignableSubscript(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression subscript) { 
org.meta_environment.rascal.ast.Assignable.Subscript x = new org.meta_environment.rascal.ast.Assignable.Subscript(node, receiver, subscript);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignable.Subscript)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignable.Ambiguity makeAssignableAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives) { 
org.meta_environment.rascal.ast.Assignable.Ambiguity amb = new org.meta_environment.rascal.ast.Assignable.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Assignable.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Assignable.Variable makeAssignableVariable(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
org.meta_environment.rascal.ast.Assignable.Variable x = new org.meta_environment.rascal.ast.Assignable.Variable(node, qualifiedName);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignable.Variable)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignment.IfDefined makeAssignmentIfDefined(INode node) { 
org.meta_environment.rascal.ast.Assignment.IfDefined x = new org.meta_environment.rascal.ast.Assignment.IfDefined(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignment.IfDefined)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignment.Intersection makeAssignmentIntersection(INode node) { 
org.meta_environment.rascal.ast.Assignment.Intersection x = new org.meta_environment.rascal.ast.Assignment.Intersection(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignment.Intersection)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignment.Division makeAssignmentDivision(INode node) { 
org.meta_environment.rascal.ast.Assignment.Division x = new org.meta_environment.rascal.ast.Assignment.Division(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignment.Division)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignment.Product makeAssignmentProduct(INode node) { 
org.meta_environment.rascal.ast.Assignment.Product x = new org.meta_environment.rascal.ast.Assignment.Product(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignment.Product)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignment.Subtraction makeAssignmentSubtraction(INode node) { 
org.meta_environment.rascal.ast.Assignment.Subtraction x = new org.meta_environment.rascal.ast.Assignment.Subtraction(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignment.Subtraction)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignment.Addition makeAssignmentAddition(INode node) { 
org.meta_environment.rascal.ast.Assignment.Addition x = new org.meta_environment.rascal.ast.Assignment.Addition(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignment.Addition)table.get(x); 
}
public org.meta_environment.rascal.ast.Assignment.Ambiguity makeAssignmentAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives) { 
org.meta_environment.rascal.ast.Assignment.Ambiguity amb = new org.meta_environment.rascal.ast.Assignment.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Assignment.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Assignment.Default makeAssignmentDefault(INode node) { 
org.meta_environment.rascal.ast.Assignment.Default x = new org.meta_environment.rascal.ast.Assignment.Default(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Assignment.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Label.Default makeLabelDefault(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Label.Default x = new org.meta_environment.rascal.ast.Label.Default(node, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Label.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Label.Ambiguity makeLabelAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Label> alternatives) { 
org.meta_environment.rascal.ast.Label.Ambiguity amb = new org.meta_environment.rascal.ast.Label.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Label.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Label.Empty makeLabelEmpty(INode node) { 
org.meta_environment.rascal.ast.Label.Empty x = new org.meta_environment.rascal.ast.Label.Empty(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Label.Empty)table.get(x); 
}
public org.meta_environment.rascal.ast.Break.NoLabel makeBreakNoLabel(INode node) { 
org.meta_environment.rascal.ast.Break.NoLabel x = new org.meta_environment.rascal.ast.Break.NoLabel(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Break.NoLabel)table.get(x); 
}
public org.meta_environment.rascal.ast.Break.Ambiguity makeBreakAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Break> alternatives) { 
org.meta_environment.rascal.ast.Break.Ambiguity amb = new org.meta_environment.rascal.ast.Break.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Break.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Break.WithLabel makeBreakWithLabel(INode node, org.meta_environment.rascal.ast.Name label) { 
org.meta_environment.rascal.ast.Break.WithLabel x = new org.meta_environment.rascal.ast.Break.WithLabel(node, label);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Break.WithLabel)table.get(x); 
}
public org.meta_environment.rascal.ast.Fail.NoLabel makeFailNoLabel(INode node) { 
org.meta_environment.rascal.ast.Fail.NoLabel x = new org.meta_environment.rascal.ast.Fail.NoLabel(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Fail.NoLabel)table.get(x); 
}
public org.meta_environment.rascal.ast.Fail.Ambiguity makeFailAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Fail> alternatives) { 
org.meta_environment.rascal.ast.Fail.Ambiguity amb = new org.meta_environment.rascal.ast.Fail.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Fail.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Fail.WithLabel makeFailWithLabel(INode node, org.meta_environment.rascal.ast.Name label) { 
org.meta_environment.rascal.ast.Fail.WithLabel x = new org.meta_environment.rascal.ast.Fail.WithLabel(node, label);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Fail.WithLabel)table.get(x); 
}
public org.meta_environment.rascal.ast.Return.NoExpression makeReturnNoExpression(INode node) { 
org.meta_environment.rascal.ast.Return.NoExpression x = new org.meta_environment.rascal.ast.Return.NoExpression(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Return.NoExpression)table.get(x); 
}
public org.meta_environment.rascal.ast.Return.Ambiguity makeReturnAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Return> alternatives) { 
org.meta_environment.rascal.ast.Return.Ambiguity amb = new org.meta_environment.rascal.ast.Return.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Return.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Return.WithExpression makeReturnWithExpression(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.Return.WithExpression x = new org.meta_environment.rascal.ast.Return.WithExpression(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Return.WithExpression)table.get(x); 
}
public org.meta_environment.rascal.ast.Catch.Binding makeCatchBinding(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Catch.Binding x = new org.meta_environment.rascal.ast.Catch.Binding(node, pattern, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Catch.Binding)table.get(x); 
}
public org.meta_environment.rascal.ast.Catch.Ambiguity makeCatchAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Catch> alternatives) { 
org.meta_environment.rascal.ast.Catch.Ambiguity amb = new org.meta_environment.rascal.ast.Catch.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Catch.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Catch.Default makeCatchDefault(INode node, org.meta_environment.rascal.ast.Statement body) { 
org.meta_environment.rascal.ast.Catch.Default x = new org.meta_environment.rascal.ast.Catch.Default(node, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Catch.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Declarator.Ambiguity makeDeclaratorAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declarator> alternatives) { 
org.meta_environment.rascal.ast.Declarator.Ambiguity amb = new org.meta_environment.rascal.ast.Declarator.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Declarator.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Declarator.Default makeDeclaratorDefault(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) { 
org.meta_environment.rascal.ast.Declarator.Default x = new org.meta_environment.rascal.ast.Declarator.Default(node, type, variables);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declarator.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic makeLocalVariableDeclarationDynamic(INode node, org.meta_environment.rascal.ast.Declarator declarator) { 
org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic x = new org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic(node, declarator);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic)table.get(x); 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity makeLocalVariableDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> alternatives) { 
org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity amb = new org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Default makeLocalVariableDeclarationDefault(INode node, org.meta_environment.rascal.ast.Declarator declarator) { 
org.meta_environment.rascal.ast.LocalVariableDeclaration.Default x = new org.meta_environment.rascal.ast.LocalVariableDeclaration.Default(node, declarator);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.LocalVariableDeclaration.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Area makeBasicTypeArea(INode node) { 
org.meta_environment.rascal.ast.BasicType.Area x = new org.meta_environment.rascal.ast.BasicType.Area(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Area)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Loc makeBasicTypeLoc(INode node) { 
org.meta_environment.rascal.ast.BasicType.Loc x = new org.meta_environment.rascal.ast.BasicType.Loc(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Loc)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Void makeBasicTypeVoid(INode node) { 
org.meta_environment.rascal.ast.BasicType.Void x = new org.meta_environment.rascal.ast.BasicType.Void(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Void)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Node makeBasicTypeNode(INode node) { 
org.meta_environment.rascal.ast.BasicType.Node x = new org.meta_environment.rascal.ast.BasicType.Node(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Node)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Value makeBasicTypeValue(INode node) { 
org.meta_environment.rascal.ast.BasicType.Value x = new org.meta_environment.rascal.ast.BasicType.Value(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Value)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.String makeBasicTypeString(INode node) { 
org.meta_environment.rascal.ast.BasicType.String x = new org.meta_environment.rascal.ast.BasicType.String(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.String)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Real makeBasicTypeReal(INode node) { 
org.meta_environment.rascal.ast.BasicType.Real x = new org.meta_environment.rascal.ast.BasicType.Real(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Real)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Int makeBasicTypeInt(INode node) { 
org.meta_environment.rascal.ast.BasicType.Int x = new org.meta_environment.rascal.ast.BasicType.Int(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Int)table.get(x); 
}
public org.meta_environment.rascal.ast.BasicType.Ambiguity makeBasicTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives) { 
org.meta_environment.rascal.ast.BasicType.Ambiguity amb = new org.meta_environment.rascal.ast.BasicType.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.BasicType.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.BasicType.Bool makeBasicTypeBool(INode node) { 
org.meta_environment.rascal.ast.BasicType.Bool x = new org.meta_environment.rascal.ast.BasicType.Bool(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BasicType.Bool)table.get(x); 
}
public org.meta_environment.rascal.ast.TypeArg.Named makeTypeArgNamed(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.TypeArg.Named x = new org.meta_environment.rascal.ast.TypeArg.Named(node, type, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.TypeArg.Named)table.get(x); 
}
public org.meta_environment.rascal.ast.TypeArg.Ambiguity makeTypeArgAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives) { 
org.meta_environment.rascal.ast.TypeArg.Ambiguity amb = new org.meta_environment.rascal.ast.TypeArg.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.TypeArg.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.TypeArg.Default makeTypeArgDefault(INode node, org.meta_environment.rascal.ast.Type type) { 
org.meta_environment.rascal.ast.TypeArg.Default x = new org.meta_environment.rascal.ast.TypeArg.Default(node, type);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.TypeArg.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.StructuredType.Lex makeStructuredTypeLex(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.Lex x = new org.meta_environment.rascal.ast.StructuredType.Lex(node, typeArg);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StructuredType.Lex)table.get(x); 
}
public org.meta_environment.rascal.ast.StructuredType.Tuple makeStructuredTypeTuple(INode node, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.StructuredType.Tuple x = new org.meta_environment.rascal.ast.StructuredType.Tuple(node, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StructuredType.Tuple)table.get(x); 
}
public org.meta_environment.rascal.ast.StructuredType.Relation makeStructuredTypeRelation(INode node, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.StructuredType.Relation x = new org.meta_environment.rascal.ast.StructuredType.Relation(node, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StructuredType.Relation)table.get(x); 
}
public org.meta_environment.rascal.ast.StructuredType.Map makeStructuredTypeMap(INode node, org.meta_environment.rascal.ast.TypeArg first, org.meta_environment.rascal.ast.TypeArg second) { 
org.meta_environment.rascal.ast.StructuredType.Map x = new org.meta_environment.rascal.ast.StructuredType.Map(node, first, second);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StructuredType.Map)table.get(x); 
}
public org.meta_environment.rascal.ast.StructuredType.Bag makeStructuredTypeBag(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.Bag x = new org.meta_environment.rascal.ast.StructuredType.Bag(node, typeArg);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StructuredType.Bag)table.get(x); 
}
public org.meta_environment.rascal.ast.StructuredType.Set makeStructuredTypeSet(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.Set x = new org.meta_environment.rascal.ast.StructuredType.Set(node, typeArg);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StructuredType.Set)table.get(x); 
}
public org.meta_environment.rascal.ast.StructuredType.Ambiguity makeStructuredTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StructuredType> alternatives) { 
org.meta_environment.rascal.ast.StructuredType.Ambiguity amb = new org.meta_environment.rascal.ast.StructuredType.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.StructuredType.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.StructuredType.List makeStructuredTypeList(INode node, org.meta_environment.rascal.ast.TypeArg typeArg) { 
org.meta_environment.rascal.ast.StructuredType.List x = new org.meta_environment.rascal.ast.StructuredType.List(node, typeArg);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StructuredType.List)table.get(x); 
}
public org.meta_environment.rascal.ast.FunctionType.Ambiguity makeFunctionTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionType> alternatives) { 
org.meta_environment.rascal.ast.FunctionType.Ambiguity amb = new org.meta_environment.rascal.ast.FunctionType.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.FunctionType.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.FunctionType.TypeArguments makeFunctionTypeTypeArguments(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.FunctionType.TypeArguments x = new org.meta_environment.rascal.ast.FunctionType.TypeArguments(node, type, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.FunctionType.TypeArguments)table.get(x); 
}
public org.meta_environment.rascal.ast.TypeVar.Bounded makeTypeVarBounded(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type bound) { 
org.meta_environment.rascal.ast.TypeVar.Bounded x = new org.meta_environment.rascal.ast.TypeVar.Bounded(node, name, bound);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.TypeVar.Bounded)table.get(x); 
}
public org.meta_environment.rascal.ast.TypeVar.Ambiguity makeTypeVarAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives) { 
org.meta_environment.rascal.ast.TypeVar.Ambiguity amb = new org.meta_environment.rascal.ast.TypeVar.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.TypeVar.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.TypeVar.Free makeTypeVarFree(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.TypeVar.Free x = new org.meta_environment.rascal.ast.TypeVar.Free(node, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.TypeVar.Free)table.get(x); 
}
public org.meta_environment.rascal.ast.UserType.Parametric makeUserTypeParametric(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Type> parameters) { 
org.meta_environment.rascal.ast.UserType.Parametric x = new org.meta_environment.rascal.ast.UserType.Parametric(node, name, parameters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.UserType.Parametric)table.get(x); 
}
public org.meta_environment.rascal.ast.UserType.Ambiguity makeUserTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.UserType> alternatives) { 
org.meta_environment.rascal.ast.UserType.Ambiguity amb = new org.meta_environment.rascal.ast.UserType.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.UserType.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.UserType.Name makeUserTypeName(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.UserType.Name x = new org.meta_environment.rascal.ast.UserType.Name(node, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.UserType.Name)table.get(x); 
}
public org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity makeDataTypeSelectorAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives) { 
org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity amb = new org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.DataTypeSelector.Selector makeDataTypeSelectorSelector(INode node, org.meta_environment.rascal.ast.Name sort, org.meta_environment.rascal.ast.Name production) { 
org.meta_environment.rascal.ast.DataTypeSelector.Selector x = new org.meta_environment.rascal.ast.DataTypeSelector.Selector(node, sort, production);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.DataTypeSelector.Selector)table.get(x); 
}
public org.meta_environment.rascal.ast.Type.Selector makeTypeSelector(INode node, org.meta_environment.rascal.ast.DataTypeSelector selector) { 
org.meta_environment.rascal.ast.Type.Selector x = new org.meta_environment.rascal.ast.Type.Selector(node, selector);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Type.Selector)table.get(x); 
}
public org.meta_environment.rascal.ast.Type.Symbol makeTypeSymbol(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Type.Symbol x = new org.meta_environment.rascal.ast.Type.Symbol(node, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Type.Symbol)table.get(x); 
}
public org.meta_environment.rascal.ast.Type.User makeTypeUser(INode node, org.meta_environment.rascal.ast.UserType user) { 
org.meta_environment.rascal.ast.Type.User x = new org.meta_environment.rascal.ast.Type.User(node, user);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Type.User)table.get(x); 
}
public org.meta_environment.rascal.ast.Type.Variable makeTypeVariable(INode node, org.meta_environment.rascal.ast.TypeVar typeVar) { 
org.meta_environment.rascal.ast.Type.Variable x = new org.meta_environment.rascal.ast.Type.Variable(node, typeVar);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Type.Variable)table.get(x); 
}
public org.meta_environment.rascal.ast.Type.Function makeTypeFunction(INode node, org.meta_environment.rascal.ast.FunctionType function) { 
org.meta_environment.rascal.ast.Type.Function x = new org.meta_environment.rascal.ast.Type.Function(node, function);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Type.Function)table.get(x); 
}
public org.meta_environment.rascal.ast.Type.Structured makeTypeStructured(INode node, org.meta_environment.rascal.ast.StructuredType structured) { 
org.meta_environment.rascal.ast.Type.Structured x = new org.meta_environment.rascal.ast.Type.Structured(node, structured);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Type.Structured)table.get(x); 
}
public org.meta_environment.rascal.ast.Type.Ambiguity makeTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Type> alternatives) { 
org.meta_environment.rascal.ast.Type.Ambiguity amb = new org.meta_environment.rascal.ast.Type.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Type.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Type.Basic makeTypeBasic(INode node, org.meta_environment.rascal.ast.BasicType basic) { 
org.meta_environment.rascal.ast.Type.Basic x = new org.meta_environment.rascal.ast.Type.Basic(node, basic);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Type.Basic)table.get(x); 
}
public org.meta_environment.rascal.ast.Module.Ambiguity makeModuleAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Module> alternatives) { 
org.meta_environment.rascal.ast.Module.Ambiguity amb = new org.meta_environment.rascal.ast.Module.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Module.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Module.Default makeModuleDefault(INode node, org.meta_environment.rascal.ast.Header header, org.meta_environment.rascal.ast.Body body) { 
org.meta_environment.rascal.ast.Module.Default x = new org.meta_environment.rascal.ast.Module.Default(node, header, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Module.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.ModuleActuals.Ambiguity makeModuleActualsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ModuleActuals> alternatives) { 
org.meta_environment.rascal.ast.ModuleActuals.Ambiguity amb = new org.meta_environment.rascal.ast.ModuleActuals.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.ModuleActuals.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.ModuleActuals.Default makeModuleActualsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Type> types) { 
org.meta_environment.rascal.ast.ModuleActuals.Default x = new org.meta_environment.rascal.ast.ModuleActuals.Default(node, types);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ModuleActuals.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.ImportedModule.Default makeImportedModuleDefault(INode node, org.meta_environment.rascal.ast.QualifiedName name) { 
org.meta_environment.rascal.ast.ImportedModule.Default x = new org.meta_environment.rascal.ast.ImportedModule.Default(node, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ImportedModule.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.ImportedModule.Renamings makeImportedModuleRenamings(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Renamings renamings) { 
org.meta_environment.rascal.ast.ImportedModule.Renamings x = new org.meta_environment.rascal.ast.ImportedModule.Renamings(node, name, renamings);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ImportedModule.Renamings)table.get(x); 
}
public org.meta_environment.rascal.ast.ImportedModule.Actuals makeImportedModuleActuals(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals) { 
org.meta_environment.rascal.ast.ImportedModule.Actuals x = new org.meta_environment.rascal.ast.ImportedModule.Actuals(node, name, actuals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ImportedModule.Actuals)table.get(x); 
}
public org.meta_environment.rascal.ast.ImportedModule.Ambiguity makeImportedModuleAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ImportedModule> alternatives) { 
org.meta_environment.rascal.ast.ImportedModule.Ambiguity amb = new org.meta_environment.rascal.ast.ImportedModule.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.ImportedModule.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming makeImportedModuleActualsRenaming(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals, org.meta_environment.rascal.ast.Renamings renamings) { 
org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming x = new org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming(node, name, actuals, renamings);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming)table.get(x); 
}
public org.meta_environment.rascal.ast.Renaming.Ambiguity makeRenamingAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Renaming> alternatives) { 
org.meta_environment.rascal.ast.Renaming.Ambiguity amb = new org.meta_environment.rascal.ast.Renaming.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Renaming.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Renaming.Default makeRenamingDefault(INode node, org.meta_environment.rascal.ast.Name from, org.meta_environment.rascal.ast.Name to) { 
org.meta_environment.rascal.ast.Renaming.Default x = new org.meta_environment.rascal.ast.Renaming.Default(node, from, to);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Renaming.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Renamings.Ambiguity makeRenamingsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Renamings> alternatives) { 
org.meta_environment.rascal.ast.Renamings.Ambiguity amb = new org.meta_environment.rascal.ast.Renamings.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Renamings.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Renamings.Default makeRenamingsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Renaming> renamings) { 
org.meta_environment.rascal.ast.Renamings.Default x = new org.meta_environment.rascal.ast.Renamings.Default(node, renamings);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Renamings.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Import.Extend makeImportExtend(INode node, org.meta_environment.rascal.ast.ImportedModule module) { 
org.meta_environment.rascal.ast.Import.Extend x = new org.meta_environment.rascal.ast.Import.Extend(node, module);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Import.Extend)table.get(x); 
}
public org.meta_environment.rascal.ast.Import.Ambiguity makeImportAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Import> alternatives) { 
org.meta_environment.rascal.ast.Import.Ambiguity amb = new org.meta_environment.rascal.ast.Import.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Import.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Import.Default makeImportDefault(INode node, org.meta_environment.rascal.ast.ImportedModule module) { 
org.meta_environment.rascal.ast.Import.Default x = new org.meta_environment.rascal.ast.Import.Default(node, module);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Import.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.ModuleParameters.Ambiguity makeModuleParametersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ModuleParameters> alternatives) { 
org.meta_environment.rascal.ast.ModuleParameters.Ambiguity amb = new org.meta_environment.rascal.ast.ModuleParameters.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.ModuleParameters.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.ModuleParameters.Default makeModuleParametersDefault(INode node, java.util.List<org.meta_environment.rascal.ast.TypeVar> parameters) { 
org.meta_environment.rascal.ast.ModuleParameters.Default x = new org.meta_environment.rascal.ast.ModuleParameters.Default(node, parameters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ModuleParameters.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Header.Parameters makeHeaderParameters(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleParameters params, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Import> imports) { 
org.meta_environment.rascal.ast.Header.Parameters x = new org.meta_environment.rascal.ast.Header.Parameters(node, name, params, tags, imports);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Header.Parameters)table.get(x); 
}
public org.meta_environment.rascal.ast.Header.Ambiguity makeHeaderAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Header> alternatives) { 
org.meta_environment.rascal.ast.Header.Ambiguity amb = new org.meta_environment.rascal.ast.Header.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Header.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Header.Default makeHeaderDefault(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Import> imports) { 
org.meta_environment.rascal.ast.Header.Default x = new org.meta_environment.rascal.ast.Header.Default(node, name, tags, imports);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Header.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.StrChar.Lexical makeStrCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StrChar.Lexical x = new org.meta_environment.rascal.ast.StrChar.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StrChar.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.StrChar.Ambiguity makeStrCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives) { 
org.meta_environment.rascal.ast.StrChar.Ambiguity amb = new org.meta_environment.rascal.ast.StrChar.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.StrChar.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.StrChar.newline makeStrCharnewline(INode node) { 
org.meta_environment.rascal.ast.StrChar.newline x = new org.meta_environment.rascal.ast.StrChar.newline(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StrChar.newline)table.get(x); 
}
public org.meta_environment.rascal.ast.StrCon.Ambiguity makeStrConAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives) { 
org.meta_environment.rascal.ast.StrCon.Ambiguity amb = new org.meta_environment.rascal.ast.StrCon.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.StrCon.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.StrCon.Lexical makeStrConLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StrCon.Lexical x = new org.meta_environment.rascal.ast.StrCon.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StrCon.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity makeSingleQuotedStrCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) { 
org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity amb = new org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical makeSingleQuotedStrCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical x = new org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity makeSingleQuotedStrConAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> alternatives) { 
org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity amb = new org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical makeSingleQuotedStrConLexical(INode node, String string) { 
org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical x = new org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral makeSymbolCaseInsensitiveLiteral(INode node, org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString) { 
org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral x = new org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral(node, singelQuotedString);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.Literal makeSymbolLiteral(INode node, org.meta_environment.rascal.ast.StrCon string) { 
org.meta_environment.rascal.ast.Symbol.Literal x = new org.meta_environment.rascal.ast.Symbol.Literal(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.Literal)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.LiftedSymbol makeSymbolLiftedSymbol(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Symbol.LiftedSymbol x = new org.meta_environment.rascal.ast.Symbol.LiftedSymbol(node, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.LiftedSymbol)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.CharacterClass makeSymbolCharacterClass(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
org.meta_environment.rascal.ast.Symbol.CharacterClass x = new org.meta_environment.rascal.ast.Symbol.CharacterClass(node, charClass);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.CharacterClass)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.Alternative makeSymbolAlternative(INode node, org.meta_environment.rascal.ast.Symbol lhs, org.meta_environment.rascal.ast.Symbol rhs) { 
org.meta_environment.rascal.ast.Symbol.Alternative x = new org.meta_environment.rascal.ast.Symbol.Alternative(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.Alternative)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.IterStarSep makeSymbolIterStarSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) { 
org.meta_environment.rascal.ast.Symbol.IterStarSep x = new org.meta_environment.rascal.ast.Symbol.IterStarSep(node, symbol, sep);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.IterStarSep)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.IterSep makeSymbolIterSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) { 
org.meta_environment.rascal.ast.Symbol.IterSep x = new org.meta_environment.rascal.ast.Symbol.IterSep(node, symbol, sep);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.IterSep)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.IterStar makeSymbolIterStar(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Symbol.IterStar x = new org.meta_environment.rascal.ast.Symbol.IterStar(node, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.IterStar)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.Iter makeSymbolIter(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Symbol.Iter x = new org.meta_environment.rascal.ast.Symbol.Iter(node, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.Iter)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.Optional makeSymbolOptional(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
org.meta_environment.rascal.ast.Symbol.Optional x = new org.meta_environment.rascal.ast.Symbol.Optional(node, symbol);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.Optional)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.Sequence makeSymbolSequence(INode node, org.meta_environment.rascal.ast.Symbol head, java.util.List<org.meta_environment.rascal.ast.Symbol> tail) { 
org.meta_environment.rascal.ast.Symbol.Sequence x = new org.meta_environment.rascal.ast.Symbol.Sequence(node, head, tail);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.Sequence)table.get(x); 
}
public org.meta_environment.rascal.ast.Symbol.Ambiguity makeSymbolAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives) { 
org.meta_environment.rascal.ast.Symbol.Ambiguity amb = new org.meta_environment.rascal.ast.Symbol.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Symbol.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Symbol.Empty makeSymbolEmpty(INode node) { 
org.meta_environment.rascal.ast.Symbol.Empty x = new org.meta_environment.rascal.ast.Symbol.Empty(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Symbol.Empty)table.get(x); 
}
public org.meta_environment.rascal.ast.CharRange.Range makeCharRangeRange(INode node, org.meta_environment.rascal.ast.Character start, org.meta_environment.rascal.ast.Character end) { 
org.meta_environment.rascal.ast.CharRange.Range x = new org.meta_environment.rascal.ast.CharRange.Range(node, start, end);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharRange.Range)table.get(x); 
}
public org.meta_environment.rascal.ast.CharRange.Ambiguity makeCharRangeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharRange> alternatives) { 
org.meta_environment.rascal.ast.CharRange.Ambiguity amb = new org.meta_environment.rascal.ast.CharRange.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.CharRange.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.CharRange.Character makeCharRangeCharacter(INode node, org.meta_environment.rascal.ast.Character character) { 
org.meta_environment.rascal.ast.CharRange.Character x = new org.meta_environment.rascal.ast.CharRange.Character(node, character);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharRange.Character)table.get(x); 
}
public org.meta_environment.rascal.ast.CharRanges.Bracket makeCharRangesBracket(INode node, org.meta_environment.rascal.ast.CharRanges ranges) { 
org.meta_environment.rascal.ast.CharRanges.Bracket x = new org.meta_environment.rascal.ast.CharRanges.Bracket(node, ranges);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharRanges.Bracket)table.get(x); 
}
public org.meta_environment.rascal.ast.CharRanges.Concatenate makeCharRangesConcatenate(INode node, org.meta_environment.rascal.ast.CharRanges lhs, org.meta_environment.rascal.ast.CharRanges rhs) { 
org.meta_environment.rascal.ast.CharRanges.Concatenate x = new org.meta_environment.rascal.ast.CharRanges.Concatenate(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharRanges.Concatenate)table.get(x); 
}
public org.meta_environment.rascal.ast.CharRanges.Ambiguity makeCharRangesAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives) { 
org.meta_environment.rascal.ast.CharRanges.Ambiguity amb = new org.meta_environment.rascal.ast.CharRanges.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.CharRanges.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.CharRanges.Range makeCharRangesRange(INode node, org.meta_environment.rascal.ast.CharRange range) { 
org.meta_environment.rascal.ast.CharRanges.Range x = new org.meta_environment.rascal.ast.CharRanges.Range(node, range);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharRanges.Range)table.get(x); 
}
public org.meta_environment.rascal.ast.OptCharRanges.Present makeOptCharRangesPresent(INode node, org.meta_environment.rascal.ast.CharRanges ranges) { 
org.meta_environment.rascal.ast.OptCharRanges.Present x = new org.meta_environment.rascal.ast.OptCharRanges.Present(node, ranges);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OptCharRanges.Present)table.get(x); 
}
public org.meta_environment.rascal.ast.OptCharRanges.Ambiguity makeOptCharRangesAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OptCharRanges> alternatives) { 
org.meta_environment.rascal.ast.OptCharRanges.Ambiguity amb = new org.meta_environment.rascal.ast.OptCharRanges.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.OptCharRanges.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.OptCharRanges.Absent makeOptCharRangesAbsent(INode node) { 
org.meta_environment.rascal.ast.OptCharRanges.Absent x = new org.meta_environment.rascal.ast.OptCharRanges.Absent(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OptCharRanges.Absent)table.get(x); 
}
public org.meta_environment.rascal.ast.CharClass.Union makeCharClassUnion(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
org.meta_environment.rascal.ast.CharClass.Union x = new org.meta_environment.rascal.ast.CharClass.Union(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharClass.Union)table.get(x); 
}
public org.meta_environment.rascal.ast.CharClass.Intersection makeCharClassIntersection(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
org.meta_environment.rascal.ast.CharClass.Intersection x = new org.meta_environment.rascal.ast.CharClass.Intersection(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharClass.Intersection)table.get(x); 
}
public org.meta_environment.rascal.ast.CharClass.Difference makeCharClassDifference(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
org.meta_environment.rascal.ast.CharClass.Difference x = new org.meta_environment.rascal.ast.CharClass.Difference(node, lhs, rhs);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharClass.Difference)table.get(x); 
}
public org.meta_environment.rascal.ast.CharClass.Complement makeCharClassComplement(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
org.meta_environment.rascal.ast.CharClass.Complement x = new org.meta_environment.rascal.ast.CharClass.Complement(node, charClass);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharClass.Complement)table.get(x); 
}
public org.meta_environment.rascal.ast.CharClass.Bracket makeCharClassBracket(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
org.meta_environment.rascal.ast.CharClass.Bracket x = new org.meta_environment.rascal.ast.CharClass.Bracket(node, charClass);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharClass.Bracket)table.get(x); 
}
public org.meta_environment.rascal.ast.CharClass.Ambiguity makeCharClassAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharClass> alternatives) { 
org.meta_environment.rascal.ast.CharClass.Ambiguity amb = new org.meta_environment.rascal.ast.CharClass.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.CharClass.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.CharClass.SimpleCharclass makeCharClassSimpleCharclass(INode node, org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges) { 
org.meta_environment.rascal.ast.CharClass.SimpleCharclass x = new org.meta_environment.rascal.ast.CharClass.SimpleCharclass(node, optionalCharRanges);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharClass.SimpleCharclass)table.get(x); 
}
public org.meta_environment.rascal.ast.NumChar.Ambiguity makeNumCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NumChar> alternatives) { 
org.meta_environment.rascal.ast.NumChar.Ambiguity amb = new org.meta_environment.rascal.ast.NumChar.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.NumChar.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.NumChar.Lexical makeNumCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.NumChar.Lexical x = new org.meta_environment.rascal.ast.NumChar.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.NumChar.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.ShortChar.Ambiguity makeShortCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ShortChar> alternatives) { 
org.meta_environment.rascal.ast.ShortChar.Ambiguity amb = new org.meta_environment.rascal.ast.ShortChar.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.ShortChar.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.ShortChar.Lexical makeShortCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.ShortChar.Lexical x = new org.meta_environment.rascal.ast.ShortChar.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ShortChar.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Character.LabelStart makeCharacterLabelStart(INode node) { 
org.meta_environment.rascal.ast.Character.LabelStart x = new org.meta_environment.rascal.ast.Character.LabelStart(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Character.LabelStart)table.get(x); 
}
public org.meta_environment.rascal.ast.Character.Bottom makeCharacterBottom(INode node) { 
org.meta_environment.rascal.ast.Character.Bottom x = new org.meta_environment.rascal.ast.Character.Bottom(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Character.Bottom)table.get(x); 
}
public org.meta_environment.rascal.ast.Character.EOF makeCharacterEOF(INode node) { 
org.meta_environment.rascal.ast.Character.EOF x = new org.meta_environment.rascal.ast.Character.EOF(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Character.EOF)table.get(x); 
}
public org.meta_environment.rascal.ast.Character.Top makeCharacterTop(INode node) { 
org.meta_environment.rascal.ast.Character.Top x = new org.meta_environment.rascal.ast.Character.Top(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Character.Top)table.get(x); 
}
public org.meta_environment.rascal.ast.Character.Short makeCharacterShort(INode node, org.meta_environment.rascal.ast.ShortChar shortChar) { 
org.meta_environment.rascal.ast.Character.Short x = new org.meta_environment.rascal.ast.Character.Short(node, shortChar);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Character.Short)table.get(x); 
}
public org.meta_environment.rascal.ast.Character.Ambiguity makeCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Character> alternatives) { 
org.meta_environment.rascal.ast.Character.Ambiguity amb = new org.meta_environment.rascal.ast.Character.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Character.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Character.Numeric makeCharacterNumeric(INode node, org.meta_environment.rascal.ast.NumChar numChar) { 
org.meta_environment.rascal.ast.Character.Numeric x = new org.meta_environment.rascal.ast.Character.Numeric(node, numChar);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Character.Numeric)table.get(x); 
}
public org.meta_environment.rascal.ast.Command.Import makeCommandImport(INode node, org.meta_environment.rascal.ast.Import imported) { 
org.meta_environment.rascal.ast.Command.Import x = new org.meta_environment.rascal.ast.Command.Import(node, imported);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Command.Import)table.get(x); 
}
public org.meta_environment.rascal.ast.Command.Declaration makeCommandDeclaration(INode node, org.meta_environment.rascal.ast.Declaration declaration) { 
org.meta_environment.rascal.ast.Command.Declaration x = new org.meta_environment.rascal.ast.Command.Declaration(node, declaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Command.Declaration)table.get(x); 
}
public org.meta_environment.rascal.ast.Command.Statement makeCommandStatement(INode node, org.meta_environment.rascal.ast.Statement statement) { 
org.meta_environment.rascal.ast.Command.Statement x = new org.meta_environment.rascal.ast.Command.Statement(node, statement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Command.Statement)table.get(x); 
}
public org.meta_environment.rascal.ast.Command.Ambiguity makeCommandAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Command> alternatives) { 
org.meta_environment.rascal.ast.Command.Ambiguity amb = new org.meta_environment.rascal.ast.Command.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Command.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Command.Shell makeCommandShell(INode node, org.meta_environment.rascal.ast.ShellCommand command) { 
org.meta_environment.rascal.ast.Command.Shell x = new org.meta_environment.rascal.ast.Command.Shell(node, command);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Command.Shell)table.get(x); 
}
public org.meta_environment.rascal.ast.ShellCommand.History makeShellCommandHistory(INode node) { 
org.meta_environment.rascal.ast.ShellCommand.History x = new org.meta_environment.rascal.ast.ShellCommand.History(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ShellCommand.History)table.get(x); 
}
public org.meta_environment.rascal.ast.ShellCommand.Edit makeShellCommandEdit(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.ShellCommand.Edit x = new org.meta_environment.rascal.ast.ShellCommand.Edit(node, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ShellCommand.Edit)table.get(x); 
}
public org.meta_environment.rascal.ast.ShellCommand.Quit makeShellCommandQuit(INode node) { 
org.meta_environment.rascal.ast.ShellCommand.Quit x = new org.meta_environment.rascal.ast.ShellCommand.Quit(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ShellCommand.Quit)table.get(x); 
}
public org.meta_environment.rascal.ast.ShellCommand.Ambiguity makeShellCommandAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives) { 
org.meta_environment.rascal.ast.ShellCommand.Ambiguity amb = new org.meta_environment.rascal.ast.ShellCommand.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.ShellCommand.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.ShellCommand.Help makeShellCommandHelp(INode node) { 
org.meta_environment.rascal.ast.ShellCommand.Help x = new org.meta_environment.rascal.ast.ShellCommand.Help(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ShellCommand.Help)table.get(x); 
}
public org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity makeRegExpLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExpLiteral> alternatives) { 
org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.RegExpLiteral.Lexical makeRegExpLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RegExpLiteral.Lexical x = new org.meta_environment.rascal.ast.RegExpLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.RegExpLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.RegExpModifier.Ambiguity makeRegExpModifierAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExpModifier> alternatives) { 
org.meta_environment.rascal.ast.RegExpModifier.Ambiguity amb = new org.meta_environment.rascal.ast.RegExpModifier.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.RegExpModifier.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.RegExpModifier.Lexical makeRegExpModifierLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RegExpModifier.Lexical x = new org.meta_environment.rascal.ast.RegExpModifier.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.RegExpModifier.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Backslash.Ambiguity makeBackslashAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Backslash> alternatives) { 
org.meta_environment.rascal.ast.Backslash.Ambiguity amb = new org.meta_environment.rascal.ast.Backslash.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Backslash.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Backslash.Lexical makeBackslashLexical(INode node, String string) { 
org.meta_environment.rascal.ast.Backslash.Lexical x = new org.meta_environment.rascal.ast.Backslash.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Backslash.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.RegExp.Ambiguity makeRegExpAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExp> alternatives) { 
org.meta_environment.rascal.ast.RegExp.Ambiguity amb = new org.meta_environment.rascal.ast.RegExp.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.RegExp.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.RegExp.Lexical makeRegExpLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RegExp.Lexical x = new org.meta_environment.rascal.ast.RegExp.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.RegExp.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.NamedRegExp.Ambiguity makeNamedRegExpAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NamedRegExp> alternatives) { 
org.meta_environment.rascal.ast.NamedRegExp.Ambiguity amb = new org.meta_environment.rascal.ast.NamedRegExp.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.NamedRegExp.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.NamedRegExp.Lexical makeNamedRegExpLexical(INode node, String string) { 
org.meta_environment.rascal.ast.NamedRegExp.Lexical x = new org.meta_environment.rascal.ast.NamedRegExp.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.NamedRegExp.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.NamedBackslash.Ambiguity makeNamedBackslashAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NamedBackslash> alternatives) { 
org.meta_environment.rascal.ast.NamedBackslash.Ambiguity amb = new org.meta_environment.rascal.ast.NamedBackslash.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.NamedBackslash.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.NamedBackslash.Lexical makeNamedBackslashLexical(INode node, String string) { 
org.meta_environment.rascal.ast.NamedBackslash.Lexical x = new org.meta_environment.rascal.ast.NamedBackslash.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.NamedBackslash.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Mapping.Ambiguity makeMappingAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives) { 
org.meta_environment.rascal.ast.Mapping.Ambiguity amb = new org.meta_environment.rascal.ast.Mapping.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Mapping.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Mapping.Default makeMappingDefault(INode node, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to) { 
org.meta_environment.rascal.ast.Mapping.Default x = new org.meta_environment.rascal.ast.Mapping.Default(node, from, to);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Mapping.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.URLLiteral.Ambiguity makeURLLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.URLLiteral> alternatives) { 
org.meta_environment.rascal.ast.URLLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.URLLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.URLLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.URLLiteral.Lexical makeURLLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.URLLiteral.Lexical x = new org.meta_environment.rascal.ast.URLLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.URLLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.URL.Ambiguity makeURLAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.URL> alternatives) { 
org.meta_environment.rascal.ast.URL.Ambiguity amb = new org.meta_environment.rascal.ast.URL.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.URL.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.URL.Default makeURLDefault(INode node, org.meta_environment.rascal.ast.URLLiteral urlliteral) { 
org.meta_environment.rascal.ast.URL.Default x = new org.meta_environment.rascal.ast.URL.Default(node, urlliteral);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.URL.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.TagString.Ambiguity makeTagStringAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TagString> alternatives) { 
org.meta_environment.rascal.ast.TagString.Ambiguity amb = new org.meta_environment.rascal.ast.TagString.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.TagString.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.TagString.Lexical makeTagStringLexical(INode node, String string) { 
org.meta_environment.rascal.ast.TagString.Lexical x = new org.meta_environment.rascal.ast.TagString.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.TagString.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.TagChar.Ambiguity makeTagCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives) { 
org.meta_environment.rascal.ast.TagChar.Ambiguity amb = new org.meta_environment.rascal.ast.TagChar.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.TagChar.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.TagChar.Lexical makeTagCharLexical(INode node, String string) { 
org.meta_environment.rascal.ast.TagChar.Lexical x = new org.meta_environment.rascal.ast.TagChar.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.TagChar.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.Tag.Ambiguity makeTagAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> alternatives) { 
org.meta_environment.rascal.ast.Tag.Ambiguity amb = new org.meta_environment.rascal.ast.Tag.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Tag.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Tag.Default makeTagDefault(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.TagString contents) { 
org.meta_environment.rascal.ast.Tag.Default x = new org.meta_environment.rascal.ast.Tag.Default(node, name, contents);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Tag.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Tags.Ambiguity makeTagsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tags> alternatives) { 
org.meta_environment.rascal.ast.Tags.Ambiguity amb = new org.meta_environment.rascal.ast.Tags.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Tags.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Tags.Default makeTagsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> annotations) { 
org.meta_environment.rascal.ast.Tags.Default x = new org.meta_environment.rascal.ast.Tags.Default(node, annotations);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Tags.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Strategy.Innermost makeStrategyInnermost(INode node) { 
org.meta_environment.rascal.ast.Strategy.Innermost x = new org.meta_environment.rascal.ast.Strategy.Innermost(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Strategy.Innermost)table.get(x); 
}
public org.meta_environment.rascal.ast.Strategy.Outermost makeStrategyOutermost(INode node) { 
org.meta_environment.rascal.ast.Strategy.Outermost x = new org.meta_environment.rascal.ast.Strategy.Outermost(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Strategy.Outermost)table.get(x); 
}
public org.meta_environment.rascal.ast.Strategy.BottomUpBreak makeStrategyBottomUpBreak(INode node) { 
org.meta_environment.rascal.ast.Strategy.BottomUpBreak x = new org.meta_environment.rascal.ast.Strategy.BottomUpBreak(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Strategy.BottomUpBreak)table.get(x); 
}
public org.meta_environment.rascal.ast.Strategy.BottomUp makeStrategyBottomUp(INode node) { 
org.meta_environment.rascal.ast.Strategy.BottomUp x = new org.meta_environment.rascal.ast.Strategy.BottomUp(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Strategy.BottomUp)table.get(x); 
}
public org.meta_environment.rascal.ast.Strategy.TopDownBreak makeStrategyTopDownBreak(INode node) { 
org.meta_environment.rascal.ast.Strategy.TopDownBreak x = new org.meta_environment.rascal.ast.Strategy.TopDownBreak(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Strategy.TopDownBreak)table.get(x); 
}
public org.meta_environment.rascal.ast.Strategy.Ambiguity makeStrategyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives) { 
org.meta_environment.rascal.ast.Strategy.Ambiguity amb = new org.meta_environment.rascal.ast.Strategy.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Strategy.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Strategy.TopDown makeStrategyTopDown(INode node) { 
org.meta_environment.rascal.ast.Strategy.TopDown x = new org.meta_environment.rascal.ast.Strategy.TopDown(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Strategy.TopDown)table.get(x); 
}
public org.meta_environment.rascal.ast.Comprehension.Map makeComprehensionMap(INode node, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Comprehension.Map x = new org.meta_environment.rascal.ast.Comprehension.Map(node, from, to, generators);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Comprehension.Map)table.get(x); 
}
public org.meta_environment.rascal.ast.Comprehension.List makeComprehensionList(INode node, org.meta_environment.rascal.ast.Expression result, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Comprehension.List x = new org.meta_environment.rascal.ast.Comprehension.List(node, result, generators);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Comprehension.List)table.get(x); 
}
public org.meta_environment.rascal.ast.Comprehension.Ambiguity makeComprehensionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Comprehension> alternatives) { 
org.meta_environment.rascal.ast.Comprehension.Ambiguity amb = new org.meta_environment.rascal.ast.Comprehension.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Comprehension.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Comprehension.Set makeComprehensionSet(INode node, org.meta_environment.rascal.ast.Expression result, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
org.meta_environment.rascal.ast.Comprehension.Set x = new org.meta_environment.rascal.ast.Comprehension.Set(node, result, generators);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Comprehension.Set)table.get(x); 
}
public org.meta_environment.rascal.ast.Replacement.Conditional makeReplacementConditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression, java.util.List<org.meta_environment.rascal.ast.Expression> conditions) { 
org.meta_environment.rascal.ast.Replacement.Conditional x = new org.meta_environment.rascal.ast.Replacement.Conditional(node, replacementExpression, conditions);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Replacement.Conditional)table.get(x); 
}
public org.meta_environment.rascal.ast.Replacement.Ambiguity makeReplacementAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives) { 
org.meta_environment.rascal.ast.Replacement.Ambiguity amb = new org.meta_environment.rascal.ast.Replacement.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Replacement.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Replacement.Unconditional makeReplacementUnconditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression) { 
org.meta_environment.rascal.ast.Replacement.Unconditional x = new org.meta_environment.rascal.ast.Replacement.Unconditional(node, replacementExpression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Replacement.Unconditional)table.get(x); 
}
public org.meta_environment.rascal.ast.Rule.Guarded makeRuleGuarded(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Rule rule) { 
org.meta_environment.rascal.ast.Rule.Guarded x = new org.meta_environment.rascal.ast.Rule.Guarded(node, type, rule);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Rule.Guarded)table.get(x); 
}
public org.meta_environment.rascal.ast.Rule.Arbitrary makeRuleArbitrary(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement statement) { 
org.meta_environment.rascal.ast.Rule.Arbitrary x = new org.meta_environment.rascal.ast.Rule.Arbitrary(node, pattern, statement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Rule.Arbitrary)table.get(x); 
}
public org.meta_environment.rascal.ast.Rule.Ambiguity makeRuleAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Rule> alternatives) { 
org.meta_environment.rascal.ast.Rule.Ambiguity amb = new org.meta_environment.rascal.ast.Rule.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Rule.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Rule.Replacing makeRuleReplacing(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Replacement replacement) { 
org.meta_environment.rascal.ast.Rule.Replacing x = new org.meta_environment.rascal.ast.Rule.Replacing(node, pattern, replacement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Rule.Replacing)table.get(x); 
}
public org.meta_environment.rascal.ast.Case.Default makeCaseDefault(INode node, org.meta_environment.rascal.ast.Statement statement) { 
org.meta_environment.rascal.ast.Case.Default x = new org.meta_environment.rascal.ast.Case.Default(node, statement);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Case.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Case.Ambiguity makeCaseAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Case> alternatives) { 
org.meta_environment.rascal.ast.Case.Ambiguity amb = new org.meta_environment.rascal.ast.Case.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Case.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Case.Rule makeCaseRule(INode node, org.meta_environment.rascal.ast.Rule rule) { 
org.meta_environment.rascal.ast.Case.Rule x = new org.meta_environment.rascal.ast.Case.Rule(node, rule);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Case.Rule)table.get(x); 
}
public org.meta_environment.rascal.ast.Visit.GivenStrategy makeVisitGivenStrategy(INode node, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
org.meta_environment.rascal.ast.Visit.GivenStrategy x = new org.meta_environment.rascal.ast.Visit.GivenStrategy(node, strategy, subject, cases);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Visit.GivenStrategy)table.get(x); 
}
public org.meta_environment.rascal.ast.Visit.Ambiguity makeVisitAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Visit> alternatives) { 
org.meta_environment.rascal.ast.Visit.Ambiguity amb = new org.meta_environment.rascal.ast.Visit.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Visit.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Visit.DefaultStrategy makeVisitDefaultStrategy(INode node, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
org.meta_environment.rascal.ast.Visit.DefaultStrategy x = new org.meta_environment.rascal.ast.Visit.DefaultStrategy(node, subject, cases);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Visit.DefaultStrategy)table.get(x); 
}
public org.meta_environment.rascal.ast.Literal.String makeLiteralString(INode node, org.meta_environment.rascal.ast.StringLiteral stringLiteral) { 
org.meta_environment.rascal.ast.Literal.String x = new org.meta_environment.rascal.ast.Literal.String(node, stringLiteral);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Literal.String)table.get(x); 
}
public org.meta_environment.rascal.ast.Literal.Real makeLiteralReal(INode node, org.meta_environment.rascal.ast.RealLiteral realLiteral) { 
org.meta_environment.rascal.ast.Literal.Real x = new org.meta_environment.rascal.ast.Literal.Real(node, realLiteral);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Literal.Real)table.get(x); 
}
public org.meta_environment.rascal.ast.Literal.Integer makeLiteralInteger(INode node, org.meta_environment.rascal.ast.IntegerLiteral integerLiteral) { 
org.meta_environment.rascal.ast.Literal.Integer x = new org.meta_environment.rascal.ast.Literal.Integer(node, integerLiteral);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Literal.Integer)table.get(x); 
}
public org.meta_environment.rascal.ast.Literal.Boolean makeLiteralBoolean(INode node, org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral) { 
org.meta_environment.rascal.ast.Literal.Boolean x = new org.meta_environment.rascal.ast.Literal.Boolean(node, booleanLiteral);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Literal.Boolean)table.get(x); 
}
public org.meta_environment.rascal.ast.Literal.Ambiguity makeLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Literal> alternatives) { 
org.meta_environment.rascal.ast.Literal.Ambiguity amb = new org.meta_environment.rascal.ast.Literal.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Literal.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Literal.RegExp makeLiteralRegExp(INode node, org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral) { 
org.meta_environment.rascal.ast.Literal.RegExp x = new org.meta_environment.rascal.ast.Literal.RegExp(node, regExpLiteral);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Literal.RegExp)table.get(x); 
}
public org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity makeUnicodeEscapeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> alternatives) { 
org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity amb = new org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.UnicodeEscape.Lexical makeUnicodeEscapeLexical(INode node, String string) { 
org.meta_environment.rascal.ast.UnicodeEscape.Lexical x = new org.meta_environment.rascal.ast.UnicodeEscape.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.UnicodeEscape.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity makeDecimalIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DecimalIntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical makeDecimalIntegerLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical x = new org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity makeHexIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical makeHexIntegerLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical x = new org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity makeOctalIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical makeOctalIntegerLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical x = new org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity makeDecimalLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives) { 
org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical makeDecimalLongLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical x = new org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity makeHexLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> alternatives) { 
org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.HexLongLiteral.Lexical makeHexLongLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.HexLongLiteral.Lexical x = new org.meta_environment.rascal.ast.HexLongLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.HexLongLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity makeOctalLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> alternatives) { 
org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.OctalLongLiteral.Lexical makeOctalLongLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.OctalLongLiteral.Lexical x = new org.meta_environment.rascal.ast.OctalLongLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OctalLongLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.RealLiteral.Ambiguity makeRealLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RealLiteral> alternatives) { 
org.meta_environment.rascal.ast.RealLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.RealLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.RealLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.RealLiteral.Lexical makeRealLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.RealLiteral.Lexical x = new org.meta_environment.rascal.ast.RealLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.RealLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity makeBooleanLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> alternatives) { 
org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.BooleanLiteral.Lexical makeBooleanLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.BooleanLiteral.Lexical x = new org.meta_environment.rascal.ast.BooleanLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.BooleanLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.SingleCharacter.Ambiguity makeSingleCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleCharacter> alternatives) { 
org.meta_environment.rascal.ast.SingleCharacter.Ambiguity amb = new org.meta_environment.rascal.ast.SingleCharacter.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.SingleCharacter.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.SingleCharacter.Lexical makeSingleCharacterLexical(INode node, String string) { 
org.meta_environment.rascal.ast.SingleCharacter.Lexical x = new org.meta_environment.rascal.ast.SingleCharacter.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.SingleCharacter.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity makeCharacterLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives) { 
org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.CharacterLiteral.Lexical makeCharacterLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.CharacterLiteral.Lexical x = new org.meta_environment.rascal.ast.CharacterLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.CharacterLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.EscapeSequence.Ambiguity makeEscapeSequenceAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.EscapeSequence> alternatives) { 
org.meta_environment.rascal.ast.EscapeSequence.Ambiguity amb = new org.meta_environment.rascal.ast.EscapeSequence.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.EscapeSequence.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.EscapeSequence.Lexical makeEscapeSequenceLexical(INode node, String string) { 
org.meta_environment.rascal.ast.EscapeSequence.Lexical x = new org.meta_environment.rascal.ast.EscapeSequence.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.EscapeSequence.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.StringCharacter.Ambiguity makeStringCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringCharacter> alternatives) { 
org.meta_environment.rascal.ast.StringCharacter.Ambiguity amb = new org.meta_environment.rascal.ast.StringCharacter.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.StringCharacter.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.StringCharacter.Lexical makeStringCharacterLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StringCharacter.Lexical x = new org.meta_environment.rascal.ast.StringCharacter.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StringCharacter.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.StringLiteral.Ambiguity makeStringLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringLiteral> alternatives) { 
org.meta_environment.rascal.ast.StringLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.StringLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.StringLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.StringLiteral.Lexical makeStringLiteralLexical(INode node, String string) { 
org.meta_environment.rascal.ast.StringLiteral.Lexical x = new org.meta_environment.rascal.ast.StringLiteral.Lexical(node, string);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.StringLiteral.Lexical)table.get(x); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral makeIntegerLiteralOctalIntegerLiteral(INode node, org.meta_environment.rascal.ast.OctalIntegerLiteral octal) { 
org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral x = new org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral(node, octal);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral)table.get(x); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral makeIntegerLiteralHexIntegerLiteral(INode node, org.meta_environment.rascal.ast.HexIntegerLiteral hex) { 
org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral x = new org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral(node, hex);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral)table.get(x); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity makeIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives) { 
org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral makeIntegerLiteralDecimalIntegerLiteral(INode node, org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal) { 
org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral x = new org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral(node, decimal);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral)table.get(x); 
}
public org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral makeLongLiteralOctalLongLiteral(INode node, org.meta_environment.rascal.ast.OctalLongLiteral octalLong) { 
org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral x = new org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral(node, octalLong);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral)table.get(x); 
}
public org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral makeLongLiteralHexLongLiteral(INode node, org.meta_environment.rascal.ast.HexLongLiteral hexLong) { 
org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral x = new org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral(node, hexLong);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral)table.get(x); 
}
public org.meta_environment.rascal.ast.LongLiteral.Ambiguity makeLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) { 
org.meta_environment.rascal.ast.LongLiteral.Ambiguity amb = new org.meta_environment.rascal.ast.LongLiteral.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.LongLiteral.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral makeLongLiteralDecimalLongLiteral(INode node, org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong) { 
org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral x = new org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral(node, decimalLong);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral)table.get(x); 
}
public org.meta_environment.rascal.ast.Visibility.Private makeVisibilityPrivate(INode node) { 
org.meta_environment.rascal.ast.Visibility.Private x = new org.meta_environment.rascal.ast.Visibility.Private(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Visibility.Private)table.get(x); 
}
public org.meta_environment.rascal.ast.Visibility.Ambiguity makeVisibilityAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives) { 
org.meta_environment.rascal.ast.Visibility.Ambiguity amb = new org.meta_environment.rascal.ast.Visibility.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Visibility.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Visibility.Public makeVisibilityPublic(INode node) { 
org.meta_environment.rascal.ast.Visibility.Public x = new org.meta_environment.rascal.ast.Visibility.Public(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Visibility.Public)table.get(x); 
}
public org.meta_environment.rascal.ast.Toplevel.DefaultVisibility makeToplevelDefaultVisibility(INode node, org.meta_environment.rascal.ast.Declaration declaration) { 
org.meta_environment.rascal.ast.Toplevel.DefaultVisibility x = new org.meta_environment.rascal.ast.Toplevel.DefaultVisibility(node, declaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Toplevel.DefaultVisibility)table.get(x); 
}
public org.meta_environment.rascal.ast.Toplevel.Ambiguity makeToplevelAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives) { 
org.meta_environment.rascal.ast.Toplevel.Ambiguity amb = new org.meta_environment.rascal.ast.Toplevel.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Toplevel.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Toplevel.GivenVisibility makeToplevelGivenVisibility(INode node, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Declaration declaration) { 
org.meta_environment.rascal.ast.Toplevel.GivenVisibility x = new org.meta_environment.rascal.ast.Toplevel.GivenVisibility(node, visibility, declaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Toplevel.GivenVisibility)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Tag makeDeclarationTag(INode node, org.meta_environment.rascal.ast.Kind kind, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Type> types) { 
org.meta_environment.rascal.ast.Declaration.Tag x = new org.meta_environment.rascal.ast.Declaration.Tag(node, kind, name, tags, types);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.Tag)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Annotation makeDeclarationAnnotation(INode node, org.meta_environment.rascal.ast.Type annoType, org.meta_environment.rascal.ast.Type onType, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.Declaration.Annotation x = new org.meta_environment.rascal.ast.Declaration.Annotation(node, annoType, onType, name, tags);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.Annotation)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Rule makeDeclarationRule(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Rule rule) { 
org.meta_environment.rascal.ast.Declaration.Rule x = new org.meta_environment.rascal.ast.Declaration.Rule(node, name, tags, rule);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.Rule)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Variable makeDeclarationVariable(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) { 
org.meta_environment.rascal.ast.Declaration.Variable x = new org.meta_environment.rascal.ast.Declaration.Variable(node, type, variables);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.Variable)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Function makeDeclarationFunction(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) { 
org.meta_environment.rascal.ast.Declaration.Function x = new org.meta_environment.rascal.ast.Declaration.Function(node, functionDeclaration);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.Function)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Data makeDeclarationData(INode node, org.meta_environment.rascal.ast.UserType user, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Variant> variants) { 
org.meta_environment.rascal.ast.Declaration.Data x = new org.meta_environment.rascal.ast.Declaration.Data(node, user, tags, variants);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.Data)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Alias makeDeclarationAlias(INode node, org.meta_environment.rascal.ast.UserType user, org.meta_environment.rascal.ast.Type base, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.Declaration.Alias x = new org.meta_environment.rascal.ast.Declaration.Alias(node, user, base, tags);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.Alias)table.get(x); 
}
public org.meta_environment.rascal.ast.Declaration.Ambiguity makeDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives) { 
org.meta_environment.rascal.ast.Declaration.Ambiguity amb = new org.meta_environment.rascal.ast.Declaration.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Declaration.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Declaration.View makeDeclarationView(INode node, org.meta_environment.rascal.ast.Name view, org.meta_environment.rascal.ast.Name superType, org.meta_environment.rascal.ast.Tags tags, java.util.List<org.meta_environment.rascal.ast.Alternative> alts) { 
org.meta_environment.rascal.ast.Declaration.View x = new org.meta_environment.rascal.ast.Declaration.View(node, view, superType, tags, alts);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Declaration.View)table.get(x); 
}
public org.meta_environment.rascal.ast.Alternative.Ambiguity makeAlternativeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives) { 
org.meta_environment.rascal.ast.Alternative.Ambiguity amb = new org.meta_environment.rascal.ast.Alternative.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Alternative.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Alternative.NamedType makeAlternativeNamedType(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type type) { 
org.meta_environment.rascal.ast.Alternative.NamedType x = new org.meta_environment.rascal.ast.Alternative.NamedType(node, name, type);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Alternative.NamedType)table.get(x); 
}
public org.meta_environment.rascal.ast.Variant.NillaryConstructor makeVariantNillaryConstructor(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Variant.NillaryConstructor x = new org.meta_environment.rascal.ast.Variant.NillaryConstructor(node, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Variant.NillaryConstructor)table.get(x); 
}
public org.meta_environment.rascal.ast.Variant.Ambiguity makeVariantAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variant> alternatives) { 
org.meta_environment.rascal.ast.Variant.Ambiguity amb = new org.meta_environment.rascal.ast.Variant.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Variant.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Variant.NAryConstructor makeVariantNAryConstructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
org.meta_environment.rascal.ast.Variant.NAryConstructor x = new org.meta_environment.rascal.ast.Variant.NAryConstructor(node, name, arguments);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Variant.NAryConstructor)table.get(x); 
}
public org.meta_environment.rascal.ast.FunctionModifier.Ambiguity makeFunctionModifierAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives) { 
org.meta_environment.rascal.ast.FunctionModifier.Ambiguity amb = new org.meta_environment.rascal.ast.FunctionModifier.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.FunctionModifier.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.FunctionModifier.Java makeFunctionModifierJava(INode node) { 
org.meta_environment.rascal.ast.FunctionModifier.Java x = new org.meta_environment.rascal.ast.FunctionModifier.Java(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.FunctionModifier.Java)table.get(x); 
}
public org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity makeFunctionModifiersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> alternatives) { 
org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity amb = new org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.FunctionModifiers.List makeFunctionModifiersList(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> modifiers) { 
org.meta_environment.rascal.ast.FunctionModifiers.List x = new org.meta_environment.rascal.ast.FunctionModifiers.List(node, modifiers);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.FunctionModifiers.List)table.get(x); 
}
public org.meta_environment.rascal.ast.Signature.WithThrows makeSignatureWithThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Type> exceptions) { 
org.meta_environment.rascal.ast.Signature.WithThrows x = new org.meta_environment.rascal.ast.Signature.WithThrows(node, type, modifiers, name, parameters, exceptions);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Signature.WithThrows)table.get(x); 
}
public org.meta_environment.rascal.ast.Signature.Ambiguity makeSignatureAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Signature> alternatives) { 
org.meta_environment.rascal.ast.Signature.Ambiguity amb = new org.meta_environment.rascal.ast.Signature.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Signature.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Signature.NoThrows makeSignatureNoThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters) { 
org.meta_environment.rascal.ast.Signature.NoThrows x = new org.meta_environment.rascal.ast.Signature.NoThrows(node, type, modifiers, name, parameters);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Signature.NoThrows)table.get(x); 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Abstract makeFunctionDeclarationAbstract(INode node, org.meta_environment.rascal.ast.Signature signature, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.FunctionDeclaration.Abstract x = new org.meta_environment.rascal.ast.FunctionDeclaration.Abstract(node, signature, tags);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.FunctionDeclaration.Abstract)table.get(x); 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity makeFunctionDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives) { 
org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity amb = new org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Default makeFunctionDeclarationDefault(INode node, org.meta_environment.rascal.ast.Signature signature, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.FunctionBody body) { 
org.meta_environment.rascal.ast.FunctionDeclaration.Default x = new org.meta_environment.rascal.ast.FunctionDeclaration.Default(node, signature, tags, body);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.FunctionDeclaration.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.FunctionBody.Ambiguity makeFunctionBodyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionBody> alternatives) { 
org.meta_environment.rascal.ast.FunctionBody.Ambiguity amb = new org.meta_environment.rascal.ast.FunctionBody.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.FunctionBody.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.FunctionBody.Default makeFunctionBodyDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
org.meta_environment.rascal.ast.FunctionBody.Default x = new org.meta_environment.rascal.ast.FunctionBody.Default(node, statements);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.FunctionBody.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Variable.Initialized makeVariableInitialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Expression initial) { 
org.meta_environment.rascal.ast.Variable.Initialized x = new org.meta_environment.rascal.ast.Variable.Initialized(node, name, tags, initial);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Variable.Initialized)table.get(x); 
}
public org.meta_environment.rascal.ast.Variable.Ambiguity makeVariableAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variable> alternatives) { 
org.meta_environment.rascal.ast.Variable.Ambiguity amb = new org.meta_environment.rascal.ast.Variable.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Variable.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Variable.UnInitialized makeVariableUnInitialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Tags tags) { 
org.meta_environment.rascal.ast.Variable.UnInitialized x = new org.meta_environment.rascal.ast.Variable.UnInitialized(node, name, tags);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Variable.UnInitialized)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.All makeKindAll(INode node) { 
org.meta_environment.rascal.ast.Kind.All x = new org.meta_environment.rascal.ast.Kind.All(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.All)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Tag makeKindTag(INode node) { 
org.meta_environment.rascal.ast.Kind.Tag x = new org.meta_environment.rascal.ast.Kind.Tag(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Tag)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Anno makeKindAnno(INode node) { 
org.meta_environment.rascal.ast.Kind.Anno x = new org.meta_environment.rascal.ast.Kind.Anno(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Anno)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Alias makeKindAlias(INode node) { 
org.meta_environment.rascal.ast.Kind.Alias x = new org.meta_environment.rascal.ast.Kind.Alias(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Alias)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.View makeKindView(INode node) { 
org.meta_environment.rascal.ast.Kind.View x = new org.meta_environment.rascal.ast.Kind.View(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.View)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Data makeKindData(INode node) { 
org.meta_environment.rascal.ast.Kind.Data x = new org.meta_environment.rascal.ast.Kind.Data(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Data)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Variable makeKindVariable(INode node) { 
org.meta_environment.rascal.ast.Kind.Variable x = new org.meta_environment.rascal.ast.Kind.Variable(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Variable)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Rule makeKindRule(INode node) { 
org.meta_environment.rascal.ast.Kind.Rule x = new org.meta_environment.rascal.ast.Kind.Rule(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Rule)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Function makeKindFunction(INode node) { 
org.meta_environment.rascal.ast.Kind.Function x = new org.meta_environment.rascal.ast.Kind.Function(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Function)table.get(x); 
}
public org.meta_environment.rascal.ast.Kind.Ambiguity makeKindAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Kind> alternatives) { 
org.meta_environment.rascal.ast.Kind.Ambiguity amb = new org.meta_environment.rascal.ast.Kind.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Kind.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Kind.Module makeKindModule(INode node) { 
org.meta_environment.rascal.ast.Kind.Module x = new org.meta_environment.rascal.ast.Kind.Module(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Kind.Module)table.get(x); 
}
public org.meta_environment.rascal.ast.Formal.Ambiguity makeFormalAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> alternatives) { 
org.meta_environment.rascal.ast.Formal.Ambiguity amb = new org.meta_environment.rascal.ast.Formal.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Formal.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Formal.TypeName makeFormalTypeName(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.Formal.TypeName x = new org.meta_environment.rascal.ast.Formal.TypeName(node, type, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Formal.TypeName)table.get(x); 
}
public org.meta_environment.rascal.ast.Formals.Ambiguity makeFormalsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formals> alternatives) { 
org.meta_environment.rascal.ast.Formals.Ambiguity amb = new org.meta_environment.rascal.ast.Formals.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Formals.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Formals.Default makeFormalsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> formals) { 
org.meta_environment.rascal.ast.Formals.Default x = new org.meta_environment.rascal.ast.Formals.Default(node, formals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Formals.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Parameters.VarArgs makeParametersVarArgs(INode node, org.meta_environment.rascal.ast.Formals formals) { 
org.meta_environment.rascal.ast.Parameters.VarArgs x = new org.meta_environment.rascal.ast.Parameters.VarArgs(node, formals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Parameters.VarArgs)table.get(x); 
}
public org.meta_environment.rascal.ast.Parameters.Ambiguity makeParametersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives) { 
org.meta_environment.rascal.ast.Parameters.Ambiguity amb = new org.meta_environment.rascal.ast.Parameters.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Parameters.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Parameters.Default makeParametersDefault(INode node, org.meta_environment.rascal.ast.Formals formals) { 
org.meta_environment.rascal.ast.Parameters.Default x = new org.meta_environment.rascal.ast.Parameters.Default(node, formals);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Parameters.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.NotIn makeOperatorAsValueNotIn(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.NotIn x = new org.meta_environment.rascal.ast.OperatorAsValue.NotIn(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.NotIn)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.In makeOperatorAsValueIn(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.In x = new org.meta_environment.rascal.ast.OperatorAsValue.In(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.In)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Not makeOperatorAsValueNot(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Not x = new org.meta_environment.rascal.ast.OperatorAsValue.Not(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Not)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Or makeOperatorAsValueOr(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Or x = new org.meta_environment.rascal.ast.OperatorAsValue.Or(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Or)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.And makeOperatorAsValueAnd(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.And x = new org.meta_environment.rascal.ast.OperatorAsValue.And(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.And)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq makeOperatorAsValueGreaterThanOrEq(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq x = new org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.GreaterThanOrEq)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan makeOperatorAsValueGreaterThan(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan x = new org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.GreaterThan)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq makeOperatorAsValueLessThanOrEq(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq x = new org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.LessThanOrEq)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.LessThan makeOperatorAsValueLessThan(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.LessThan x = new org.meta_environment.rascal.ast.OperatorAsValue.LessThan(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.LessThan)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.NotEquals makeOperatorAsValueNotEquals(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.NotEquals x = new org.meta_environment.rascal.ast.OperatorAsValue.NotEquals(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.NotEquals)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Equals makeOperatorAsValueEquals(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Equals x = new org.meta_environment.rascal.ast.OperatorAsValue.Equals(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Equals)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Intersection makeOperatorAsValueIntersection(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Intersection x = new org.meta_environment.rascal.ast.OperatorAsValue.Intersection(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Intersection)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Division makeOperatorAsValueDivision(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Division x = new org.meta_environment.rascal.ast.OperatorAsValue.Division(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Division)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Product makeOperatorAsValueProduct(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Product x = new org.meta_environment.rascal.ast.OperatorAsValue.Product(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Product)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Subtraction makeOperatorAsValueSubtraction(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Subtraction x = new org.meta_environment.rascal.ast.OperatorAsValue.Subtraction(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Subtraction)table.get(x); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity makeOperatorAsValueAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives) { 
org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity amb = new org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.OperatorAsValue.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.OperatorAsValue.Addition makeOperatorAsValueAddition(INode node) { 
org.meta_environment.rascal.ast.OperatorAsValue.Addition x = new org.meta_environment.rascal.ast.OperatorAsValue.Addition(node);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.OperatorAsValue.Addition)table.get(x); 
}
public org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity makeFunctionAsValueAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionAsValue> alternatives) { 
org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity amb = new org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.FunctionAsValue.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.FunctionAsValue.Default makeFunctionAsValueDefault(INode node, org.meta_environment.rascal.ast.Name name) { 
org.meta_environment.rascal.ast.FunctionAsValue.Default x = new org.meta_environment.rascal.ast.FunctionAsValue.Default(node, name);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.FunctionAsValue.Default)table.get(x); 
}
public org.meta_environment.rascal.ast.Field.Index makeFieldIndex(INode node, org.meta_environment.rascal.ast.IntegerLiteral fieldIndex) { 
org.meta_environment.rascal.ast.Field.Index x = new org.meta_environment.rascal.ast.Field.Index(node, fieldIndex);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Field.Index)table.get(x); 
}
public org.meta_environment.rascal.ast.Field.Ambiguity makeFieldAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Field> alternatives) { 
org.meta_environment.rascal.ast.Field.Ambiguity amb = new org.meta_environment.rascal.ast.Field.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.Field.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.Field.Name makeFieldName(INode node, org.meta_environment.rascal.ast.Name fieldName) { 
org.meta_environment.rascal.ast.Field.Name x = new org.meta_environment.rascal.ast.Field.Name(node, fieldName);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.Field.Name)table.get(x); 
}
public org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity makeClosureAsFunctionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ClosureAsFunction> alternatives) { 
org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity amb = new org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity(node, alternatives);
     if (!table.containsKey(amb)) {
        table.put(amb, amb);
     }
     return (org.meta_environment.rascal.ast.ClosureAsFunction.Ambiguity)table.get(amb); 
}
public org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated makeClosureAsFunctionEvaluated(INode node, org.meta_environment.rascal.ast.Expression expression) { 
org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated x = new org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated(node, expression);
		if (!table.containsKey(x)) { 
			table.put(x, x);
		}
		return (org.meta_environment.rascal.ast.ClosureAsFunction.Evaluated)table.get(x); 
}
}