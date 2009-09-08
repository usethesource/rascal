package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.INode;

public class ASTFactory {

public org.meta_environment.rascal.ast.Formal.Ambiguity makeFormalAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> alternatives) { 
return new org.meta_environment.rascal.ast.Formal.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Formal.TypeName makeFormalTypeName(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Formal.TypeName(node, type, name); 
}
public org.meta_environment.rascal.ast.Formals.Ambiguity makeFormalsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Formals> alternatives) { 
return new org.meta_environment.rascal.ast.Formals.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Formals.Default makeFormalsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Formal> formals) { 
return new org.meta_environment.rascal.ast.Formals.Default(node, formals); 
}
public org.meta_environment.rascal.ast.Parameters.VarArgs makeParametersVarArgs(INode node, org.meta_environment.rascal.ast.Formals formals) { 
return new org.meta_environment.rascal.ast.Parameters.VarArgs(node, formals); 
}
public org.meta_environment.rascal.ast.Parameters.Ambiguity makeParametersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Parameters> alternatives) { 
return new org.meta_environment.rascal.ast.Parameters.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Parameters.Default makeParametersDefault(INode node, org.meta_environment.rascal.ast.Formals formals) { 
return new org.meta_environment.rascal.ast.Parameters.Default(node, formals); 
}
public org.meta_environment.rascal.ast.Expression.Anti makeExpressionAnti(INode node, org.meta_environment.rascal.ast.Expression pattern) { 
return new org.meta_environment.rascal.ast.Expression.Anti(node, pattern); 
}
public org.meta_environment.rascal.ast.Expression.Guarded makeExpressionGuarded(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Expression pattern) { 
return new org.meta_environment.rascal.ast.Expression.Guarded(node, type, pattern); 
}
public org.meta_environment.rascal.ast.Expression.TypedVariableBecomes makeExpressionTypedVariableBecomes(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression pattern) { 
return new org.meta_environment.rascal.ast.Expression.TypedVariableBecomes(node, type, name, pattern); 
}
public org.meta_environment.rascal.ast.Expression.VariableBecomes makeExpressionVariableBecomes(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression pattern) { 
return new org.meta_environment.rascal.ast.Expression.VariableBecomes(node, name, pattern); 
}
public org.meta_environment.rascal.ast.Expression.Descendant makeExpressionDescendant(INode node, org.meta_environment.rascal.ast.Expression pattern) { 
return new org.meta_environment.rascal.ast.Expression.Descendant(node, pattern); 
}
public org.meta_environment.rascal.ast.Expression.MultiVariable makeExpressionMultiVariable(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
return new org.meta_environment.rascal.ast.Expression.MultiVariable(node, qualifiedName); 
}
public org.meta_environment.rascal.ast.Expression.TypedVariable makeExpressionTypedVariable(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Expression.TypedVariable(node, type, name); 
}
public org.meta_environment.rascal.ast.Expression.Map makeExpressionMap(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> mappings) { 
return new org.meta_environment.rascal.ast.Expression.Map(node, mappings); 
}
public org.meta_environment.rascal.ast.Expression.Tuple makeExpressionTuple(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
return new org.meta_environment.rascal.ast.Expression.Tuple(node, elements); 
}
public org.meta_environment.rascal.ast.Expression.Set makeExpressionSet(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
return new org.meta_environment.rascal.ast.Expression.Set(node, elements); 
}
public org.meta_environment.rascal.ast.Expression.List makeExpressionList(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> elements) { 
return new org.meta_environment.rascal.ast.Expression.List(node, elements); 
}
public org.meta_environment.rascal.ast.Expression.QualifiedName makeExpressionQualifiedName(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
return new org.meta_environment.rascal.ast.Expression.QualifiedName(node, qualifiedName); 
}
public org.meta_environment.rascal.ast.Expression.Literal makeExpressionLiteral(INode node, org.meta_environment.rascal.ast.Literal literal) { 
return new org.meta_environment.rascal.ast.Expression.Literal(node, literal); 
}
public org.meta_environment.rascal.ast.Expression.Any makeExpressionAny(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
return new org.meta_environment.rascal.ast.Expression.Any(node, generators); 
}
public org.meta_environment.rascal.ast.Expression.All makeExpressionAll(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
return new org.meta_environment.rascal.ast.Expression.All(node, generators); 
}
public org.meta_environment.rascal.ast.Expression.Comprehension makeExpressionComprehension(INode node, org.meta_environment.rascal.ast.Comprehension comprehension) { 
return new org.meta_environment.rascal.ast.Expression.Comprehension(node, comprehension); 
}
public org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy makeExpressionEnumeratorWithStrategy(INode node, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Expression.EnumeratorWithStrategy(node, strategy, pattern, expression); 
}
public org.meta_environment.rascal.ast.Expression.Enumerator makeExpressionEnumerator(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Expression.Enumerator(node, pattern, expression); 
}
public org.meta_environment.rascal.ast.Expression.NoMatch makeExpressionNoMatch(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Expression.NoMatch(node, pattern, expression); 
}
public org.meta_environment.rascal.ast.Expression.Match makeExpressionMatch(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Expression.Match(node, pattern, expression); 
}
public org.meta_environment.rascal.ast.Expression.Visit makeExpressionVisit(INode node, org.meta_environment.rascal.ast.Visit visit) { 
return new org.meta_environment.rascal.ast.Expression.Visit(node, visit); 
}
public org.meta_environment.rascal.ast.Expression.Lexical makeExpressionLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Expression.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Expression.Or makeExpressionOr(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Or(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.And makeExpressionAnd(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.And(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Equivalence makeExpressionEquivalence(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Equivalence(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Implication makeExpressionImplication(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Implication(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise makeExpressionIfDefinedOtherwise(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.IfDefinedOtherwise(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.IfThenElse makeExpressionIfThenElse(INode node, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Expression thenExp, org.meta_environment.rascal.ast.Expression elseExp) { 
return new org.meta_environment.rascal.ast.Expression.IfThenElse(node, condition, thenExp, elseExp); 
}
public org.meta_environment.rascal.ast.Expression.NonEquals makeExpressionNonEquals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.NonEquals(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Equals makeExpressionEquals(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Equals(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.GreaterThanOrEq makeExpressionGreaterThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.GreaterThanOrEq(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.GreaterThan makeExpressionGreaterThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.GreaterThan(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.LessThanOrEq makeExpressionLessThanOrEq(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.LessThanOrEq(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.LessThan makeExpressionLessThan(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.LessThan(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.In makeExpressionIn(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.In(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.NotIn makeExpressionNotIn(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.NotIn(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Subtraction makeExpressionSubtraction(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Subtraction(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Addition makeExpressionAddition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Addition(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Intersection makeExpressionIntersection(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Intersection(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Modulo makeExpressionModulo(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Modulo(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Division makeExpressionDivision(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Division(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Join makeExpressionJoin(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Join(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Product makeExpressionProduct(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Product(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.Composition makeExpressionComposition(INode node, org.meta_environment.rascal.ast.Expression lhs, org.meta_environment.rascal.ast.Expression rhs) { 
return new org.meta_environment.rascal.ast.Expression.Composition(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Expression.SetAnnotation makeExpressionSetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression value) { 
return new org.meta_environment.rascal.ast.Expression.SetAnnotation(node, expression, name, value); 
}
public org.meta_environment.rascal.ast.Expression.GetAnnotation makeExpressionGetAnnotation(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Expression.GetAnnotation(node, expression, name); 
}
public org.meta_environment.rascal.ast.Expression.TransitiveClosure makeExpressionTransitiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) { 
return new org.meta_environment.rascal.ast.Expression.TransitiveClosure(node, argument); 
}
public org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure makeExpressionTransitiveReflexiveClosure(INode node, org.meta_environment.rascal.ast.Expression argument) { 
return new org.meta_environment.rascal.ast.Expression.TransitiveReflexiveClosure(node, argument); 
}
public org.meta_environment.rascal.ast.Expression.Negative makeExpressionNegative(INode node, org.meta_environment.rascal.ast.Expression argument) { 
return new org.meta_environment.rascal.ast.Expression.Negative(node, argument); 
}
public org.meta_environment.rascal.ast.Expression.Negation makeExpressionNegation(INode node, org.meta_environment.rascal.ast.Expression argument) { 
return new org.meta_environment.rascal.ast.Expression.Negation(node, argument); 
}
public org.meta_environment.rascal.ast.Expression.IsDefined makeExpressionIsDefined(INode node, org.meta_environment.rascal.ast.Expression argument) { 
return new org.meta_environment.rascal.ast.Expression.IsDefined(node, argument); 
}
public org.meta_environment.rascal.ast.Expression.Subscript makeExpressionSubscript(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Expression> subscripts) { 
return new org.meta_environment.rascal.ast.Expression.Subscript(node, expression, subscripts); 
}
public org.meta_environment.rascal.ast.Expression.FieldProject makeExpressionFieldProject(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Field> fields) { 
return new org.meta_environment.rascal.ast.Expression.FieldProject(node, expression, fields); 
}
public org.meta_environment.rascal.ast.Expression.FieldAccess makeExpressionFieldAccess(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name field) { 
return new org.meta_environment.rascal.ast.Expression.FieldAccess(node, expression, field); 
}
public org.meta_environment.rascal.ast.Expression.FieldUpdate makeExpressionFieldUpdate(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.Name key, org.meta_environment.rascal.ast.Expression replacement) { 
return new org.meta_environment.rascal.ast.Expression.FieldUpdate(node, expression, key, replacement); 
}
public org.meta_environment.rascal.ast.Expression.CallOrTree makeExpressionCallOrTree(INode node, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) { 
return new org.meta_environment.rascal.ast.Expression.CallOrTree(node, expression, arguments); 
}
public org.meta_environment.rascal.ast.Expression.ReifiedType makeExpressionReifiedType(INode node, org.meta_environment.rascal.ast.BasicType basicType, java.util.List<org.meta_environment.rascal.ast.Expression> arguments) { 
return new org.meta_environment.rascal.ast.Expression.ReifiedType(node, basicType, arguments); 
}
public org.meta_environment.rascal.ast.Expression.ReifyType makeExpressionReifyType(INode node, org.meta_environment.rascal.ast.Type type) { 
return new org.meta_environment.rascal.ast.Expression.ReifyType(node, type); 
}
public org.meta_environment.rascal.ast.Expression.StepRange makeExpressionStepRange(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression second, org.meta_environment.rascal.ast.Expression last) { 
return new org.meta_environment.rascal.ast.Expression.StepRange(node, first, second, last); 
}
public org.meta_environment.rascal.ast.Expression.Range makeExpressionRange(INode node, org.meta_environment.rascal.ast.Expression first, org.meta_environment.rascal.ast.Expression last) { 
return new org.meta_environment.rascal.ast.Expression.Range(node, first, last); 
}
public org.meta_environment.rascal.ast.Expression.Bracket makeExpressionBracket(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Expression.Bracket(node, expression); 
}
public org.meta_environment.rascal.ast.Expression.NonEmptyBlock makeExpressionNonEmptyBlock(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
return new org.meta_environment.rascal.ast.Expression.NonEmptyBlock(node, statements); 
}
public org.meta_environment.rascal.ast.Expression.VoidClosure makeExpressionVoidClosure(INode node, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
return new org.meta_environment.rascal.ast.Expression.VoidClosure(node, parameters, statements); 
}
public org.meta_environment.rascal.ast.Expression.Ambiguity makeExpressionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> alternatives) { 
return new org.meta_environment.rascal.ast.Expression.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Expression.Closure makeExpressionClosure(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
return new org.meta_environment.rascal.ast.Expression.Closure(node, type, parameters, statements); 
}
public org.meta_environment.rascal.ast.ProtocolTail.Post makeProtocolTailPost(INode node, org.meta_environment.rascal.ast.PostProtocolChars post) { 
return new org.meta_environment.rascal.ast.ProtocolTail.Post(node, post); 
}
public org.meta_environment.rascal.ast.ProtocolTail.Ambiguity makeProtocolTailAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ProtocolTail> alternatives) { 
return new org.meta_environment.rascal.ast.ProtocolTail.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ProtocolTail.Mid makeProtocolTailMid(INode node, org.meta_environment.rascal.ast.MidProtocolChars mid, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.ProtocolTail tail) { 
return new org.meta_environment.rascal.ast.ProtocolTail.Mid(node, mid, expression, tail); 
}
public org.meta_environment.rascal.ast.ProtocolPart.NonInterpolated makeProtocolPartNonInterpolated(INode node, org.meta_environment.rascal.ast.ProtocolChars protocolChars) { 
return new org.meta_environment.rascal.ast.ProtocolPart.NonInterpolated(node, protocolChars); 
}
public org.meta_environment.rascal.ast.ProtocolPart.Ambiguity makeProtocolPartAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ProtocolPart> alternatives) { 
return new org.meta_environment.rascal.ast.ProtocolPart.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ProtocolPart.Interpolated makeProtocolPartInterpolated(INode node, org.meta_environment.rascal.ast.PreProtocolChars pre, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.ProtocolTail tail) { 
return new org.meta_environment.rascal.ast.ProtocolPart.Interpolated(node, pre, expression, tail); 
}
public org.meta_environment.rascal.ast.PathTail.Post makePathTailPost(INode node, org.meta_environment.rascal.ast.PostPathChars post) { 
return new org.meta_environment.rascal.ast.PathTail.Post(node, post); 
}
public org.meta_environment.rascal.ast.PathTail.Ambiguity makePathTailAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PathTail> alternatives) { 
return new org.meta_environment.rascal.ast.PathTail.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PathTail.Mid makePathTailMid(INode node, org.meta_environment.rascal.ast.MidPathChars mid, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.PathTail tail) { 
return new org.meta_environment.rascal.ast.PathTail.Mid(node, mid, expression, tail); 
}
public org.meta_environment.rascal.ast.PathPart.NonInterpolated makePathPartNonInterpolated(INode node, org.meta_environment.rascal.ast.PathChars pathChars) { 
return new org.meta_environment.rascal.ast.PathPart.NonInterpolated(node, pathChars); 
}
public org.meta_environment.rascal.ast.PathPart.Ambiguity makePathPartAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PathPart> alternatives) { 
return new org.meta_environment.rascal.ast.PathPart.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PathPart.Interpolated makePathPartInterpolated(INode node, org.meta_environment.rascal.ast.PrePathChars pre, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.PathTail tail) { 
return new org.meta_environment.rascal.ast.PathPart.Interpolated(node, pre, expression, tail); 
}
public org.meta_environment.rascal.ast.Field.Index makeFieldIndex(INode node, org.meta_environment.rascal.ast.IntegerLiteral fieldIndex) { 
return new org.meta_environment.rascal.ast.Field.Index(node, fieldIndex); 
}
public org.meta_environment.rascal.ast.Field.Ambiguity makeFieldAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Field> alternatives) { 
return new org.meta_environment.rascal.ast.Field.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Field.Name makeFieldName(INode node, org.meta_environment.rascal.ast.Name fieldName) { 
return new org.meta_environment.rascal.ast.Field.Name(node, fieldName); 
}
public org.meta_environment.rascal.ast.Marker.Ambiguity makeMarkerAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Marker> alternatives) { 
return new org.meta_environment.rascal.ast.Marker.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Marker.Lexical makeMarkerLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Marker.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Rest.Ambiguity makeRestAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Rest> alternatives) { 
return new org.meta_environment.rascal.ast.Rest.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Rest.Lexical makeRestLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Rest.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Body.Toplevels makeBodyToplevels(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> toplevels) { 
return new org.meta_environment.rascal.ast.Body.Toplevels(node, toplevels); 
}
public org.meta_environment.rascal.ast.Body.Ambiguity makeBodyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Body> alternatives) { 
return new org.meta_environment.rascal.ast.Body.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Body.Anything makeBodyAnything(INode node, org.meta_environment.rascal.ast.Marker marker, org.meta_environment.rascal.ast.Rest rest) { 
return new org.meta_environment.rascal.ast.Body.Anything(node, marker, rest); 
}
public org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity makeUnicodeEscapeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> alternatives) { 
return new org.meta_environment.rascal.ast.UnicodeEscape.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.UnicodeEscape.Lexical makeUnicodeEscapeLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.UnicodeEscape.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity makeDecimalIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DecimalIntegerLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.DecimalIntegerLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical makeDecimalIntegerLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.DecimalIntegerLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity makeHexIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.HexIntegerLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical makeHexIntegerLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.HexIntegerLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity makeOctalIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.OctalIntegerLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical makeOctalIntegerLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.OctalIntegerLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity makeDecimalLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.DecimalLongLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical makeDecimalLongLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.DecimalLongLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity makeHexLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.HexLongLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.HexLongLiteral.Lexical makeHexLongLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.HexLongLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity makeOctalLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.OctalLongLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.OctalLongLiteral.Lexical makeOctalLongLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.OctalLongLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.RealLiteral.Ambiguity makeRealLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RealLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.RealLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.RealLiteral.Lexical makeRealLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.RealLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity makeBooleanLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.BooleanLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.BooleanLiteral.Lexical makeBooleanLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.BooleanLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.SingleCharacter.Ambiguity makeSingleCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleCharacter> alternatives) { 
return new org.meta_environment.rascal.ast.SingleCharacter.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.SingleCharacter.Lexical makeSingleCharacterLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.SingleCharacter.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity makeCharacterLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.CharacterLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.CharacterLiteral.Lexical makeCharacterLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.CharacterLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.EscapeSequence.Ambiguity makeEscapeSequenceAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.EscapeSequence> alternatives) { 
return new org.meta_environment.rascal.ast.EscapeSequence.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.EscapeSequence.Lexical makeEscapeSequenceLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.EscapeSequence.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.StringCharacter.Ambiguity makeStringCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringCharacter> alternatives) { 
return new org.meta_environment.rascal.ast.StringCharacter.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.StringCharacter.Lexical makeStringCharacterLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.StringCharacter.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.StringLiteral.Ambiguity makeStringLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StringLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.StringLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.StringLiteral.Lexical makeStringLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.StringLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.ProtocolChars.Ambiguity makeProtocolCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ProtocolChars> alternatives) { 
return new org.meta_environment.rascal.ast.ProtocolChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ProtocolChars.Lexical makeProtocolCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.ProtocolChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.PreProtocolChars.Ambiguity makePreProtocolCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PreProtocolChars> alternatives) { 
return new org.meta_environment.rascal.ast.PreProtocolChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PreProtocolChars.Lexical makePreProtocolCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.PreProtocolChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.MidProtocolChars.Ambiguity makeMidProtocolCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.MidProtocolChars> alternatives) { 
return new org.meta_environment.rascal.ast.MidProtocolChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.MidProtocolChars.Lexical makeMidProtocolCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.MidProtocolChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.PostProtocolChars.Ambiguity makePostProtocolCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PostProtocolChars> alternatives) { 
return new org.meta_environment.rascal.ast.PostProtocolChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PostProtocolChars.Lexical makePostProtocolCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.PostProtocolChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.PrePathChars.Ambiguity makePrePathCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PrePathChars> alternatives) { 
return new org.meta_environment.rascal.ast.PrePathChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PrePathChars.Lexical makePrePathCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.PrePathChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.MidPathChars.Ambiguity makeMidPathCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.MidPathChars> alternatives) { 
return new org.meta_environment.rascal.ast.MidPathChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.MidPathChars.Lexical makeMidPathCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.MidPathChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.PostPathChars.Ambiguity makePostPathCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PostPathChars> alternatives) { 
return new org.meta_environment.rascal.ast.PostPathChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PostPathChars.Lexical makePostPathCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.PostPathChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.PathChars.Ambiguity makePathCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PathChars> alternatives) { 
return new org.meta_environment.rascal.ast.PathChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PathChars.Lexical makePathCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.PathChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.LocationLiteral.Ambiguity makeLocationLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LocationLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.LocationLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.LocationLiteral.Default makeLocationLiteralDefault(INode node, org.meta_environment.rascal.ast.ProtocolPart protocolPart, org.meta_environment.rascal.ast.PathPart pathPart) { 
return new org.meta_environment.rascal.ast.LocationLiteral.Default(node, protocolPart, pathPart); 
}
public org.meta_environment.rascal.ast.URLChars.Ambiguity makeURLCharsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.URLChars> alternatives) { 
return new org.meta_environment.rascal.ast.URLChars.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.URLChars.Lexical makeURLCharsLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.URLChars.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral makeIntegerLiteralOctalIntegerLiteral(INode node, org.meta_environment.rascal.ast.OctalIntegerLiteral octal) { 
return new org.meta_environment.rascal.ast.IntegerLiteral.OctalIntegerLiteral(node, octal); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral makeIntegerLiteralHexIntegerLiteral(INode node, org.meta_environment.rascal.ast.HexIntegerLiteral hex) { 
return new org.meta_environment.rascal.ast.IntegerLiteral.HexIntegerLiteral(node, hex); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity makeIntegerLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.IntegerLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral makeIntegerLiteralDecimalIntegerLiteral(INode node, org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal) { 
return new org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral(node, decimal); 
}
public org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral makeLongLiteralOctalLongLiteral(INode node, org.meta_environment.rascal.ast.OctalLongLiteral octalLong) { 
return new org.meta_environment.rascal.ast.LongLiteral.OctalLongLiteral(node, octalLong); 
}
public org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral makeLongLiteralHexLongLiteral(INode node, org.meta_environment.rascal.ast.HexLongLiteral hexLong) { 
return new org.meta_environment.rascal.ast.LongLiteral.HexLongLiteral(node, hexLong); 
}
public org.meta_environment.rascal.ast.LongLiteral.Ambiguity makeLongLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.LongLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral makeLongLiteralDecimalLongLiteral(INode node, org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong) { 
return new org.meta_environment.rascal.ast.LongLiteral.DecimalLongLiteral(node, decimalLong); 
}
public org.meta_environment.rascal.ast.BasicType.ReifiedReifiedType makeBasicTypeReifiedReifiedType(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.ReifiedReifiedType(node); 
}
public org.meta_environment.rascal.ast.BasicType.ReifiedNonTerminal makeBasicTypeReifiedNonTerminal(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.ReifiedNonTerminal(node); 
}
public org.meta_environment.rascal.ast.BasicType.ReifiedFunction makeBasicTypeReifiedFunction(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.ReifiedFunction(node); 
}
public org.meta_environment.rascal.ast.BasicType.ReifiedConstructor makeBasicTypeReifiedConstructor(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.ReifiedConstructor(node); 
}
public org.meta_environment.rascal.ast.BasicType.ReifiedAdt makeBasicTypeReifiedAdt(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.ReifiedAdt(node); 
}
public org.meta_environment.rascal.ast.BasicType.ReifiedType makeBasicTypeReifiedType(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.ReifiedType(node); 
}
public org.meta_environment.rascal.ast.BasicType.Lex makeBasicTypeLex(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Lex(node); 
}
public org.meta_environment.rascal.ast.BasicType.Tuple makeBasicTypeTuple(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Tuple(node); 
}
public org.meta_environment.rascal.ast.BasicType.Relation makeBasicTypeRelation(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Relation(node); 
}
public org.meta_environment.rascal.ast.BasicType.Map makeBasicTypeMap(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Map(node); 
}
public org.meta_environment.rascal.ast.BasicType.Bag makeBasicTypeBag(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Bag(node); 
}
public org.meta_environment.rascal.ast.BasicType.Set makeBasicTypeSet(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Set(node); 
}
public org.meta_environment.rascal.ast.BasicType.List makeBasicTypeList(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.List(node); 
}
public org.meta_environment.rascal.ast.BasicType.Loc makeBasicTypeLoc(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Loc(node); 
}
public org.meta_environment.rascal.ast.BasicType.Void makeBasicTypeVoid(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Void(node); 
}
public org.meta_environment.rascal.ast.BasicType.Node makeBasicTypeNode(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Node(node); 
}
public org.meta_environment.rascal.ast.BasicType.Value makeBasicTypeValue(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Value(node); 
}
public org.meta_environment.rascal.ast.BasicType.String makeBasicTypeString(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.String(node); 
}
public org.meta_environment.rascal.ast.BasicType.Real makeBasicTypeReal(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Real(node); 
}
public org.meta_environment.rascal.ast.BasicType.Int makeBasicTypeInt(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Int(node); 
}
public org.meta_environment.rascal.ast.BasicType.Ambiguity makeBasicTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives) { 
return new org.meta_environment.rascal.ast.BasicType.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.BasicType.Bool makeBasicTypeBool(INode node) { 
return new org.meta_environment.rascal.ast.BasicType.Bool(node); 
}
public org.meta_environment.rascal.ast.TypeArg.Named makeTypeArgNamed(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.TypeArg.Named(node, type, name); 
}
public org.meta_environment.rascal.ast.TypeArg.Ambiguity makeTypeArgAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TypeArg> alternatives) { 
return new org.meta_environment.rascal.ast.TypeArg.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.TypeArg.Default makeTypeArgDefault(INode node, org.meta_environment.rascal.ast.Type type) { 
return new org.meta_environment.rascal.ast.TypeArg.Default(node, type); 
}
public org.meta_environment.rascal.ast.StructuredType.Ambiguity makeStructuredTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StructuredType> alternatives) { 
return new org.meta_environment.rascal.ast.StructuredType.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.StructuredType.Default makeStructuredTypeDefault(INode node, org.meta_environment.rascal.ast.BasicType basicType, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
return new org.meta_environment.rascal.ast.StructuredType.Default(node, basicType, arguments); 
}
public org.meta_environment.rascal.ast.FunctionType.Ambiguity makeFunctionTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionType> alternatives) { 
return new org.meta_environment.rascal.ast.FunctionType.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.FunctionType.TypeArguments makeFunctionTypeTypeArguments(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
return new org.meta_environment.rascal.ast.FunctionType.TypeArguments(node, type, arguments); 
}
public org.meta_environment.rascal.ast.TypeVar.Bounded makeTypeVarBounded(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type bound) { 
return new org.meta_environment.rascal.ast.TypeVar.Bounded(node, name, bound); 
}
public org.meta_environment.rascal.ast.TypeVar.Ambiguity makeTypeVarAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives) { 
return new org.meta_environment.rascal.ast.TypeVar.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.TypeVar.Free makeTypeVarFree(INode node, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.TypeVar.Free(node, name); 
}
public org.meta_environment.rascal.ast.UserType.Parametric makeUserTypeParametric(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Type> parameters) { 
return new org.meta_environment.rascal.ast.UserType.Parametric(node, name, parameters); 
}
public org.meta_environment.rascal.ast.UserType.Ambiguity makeUserTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.UserType> alternatives) { 
return new org.meta_environment.rascal.ast.UserType.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.UserType.Name makeUserTypeName(INode node, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.UserType.Name(node, name); 
}
public org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity makeDataTypeSelectorAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives) { 
return new org.meta_environment.rascal.ast.DataTypeSelector.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.DataTypeSelector.Selector makeDataTypeSelectorSelector(INode node, org.meta_environment.rascal.ast.Name sort, org.meta_environment.rascal.ast.Name production) { 
return new org.meta_environment.rascal.ast.DataTypeSelector.Selector(node, sort, production); 
}
public org.meta_environment.rascal.ast.Type.Symbol makeTypeSymbol(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
return new org.meta_environment.rascal.ast.Type.Symbol(node, symbol); 
}
public org.meta_environment.rascal.ast.Type.Bracket makeTypeBracket(INode node, org.meta_environment.rascal.ast.Type type) { 
return new org.meta_environment.rascal.ast.Type.Bracket(node, type); 
}
public org.meta_environment.rascal.ast.Type.Selector makeTypeSelector(INode node, org.meta_environment.rascal.ast.DataTypeSelector selector) { 
return new org.meta_environment.rascal.ast.Type.Selector(node, selector); 
}
public org.meta_environment.rascal.ast.Type.User makeTypeUser(INode node, org.meta_environment.rascal.ast.UserType user) { 
return new org.meta_environment.rascal.ast.Type.User(node, user); 
}
public org.meta_environment.rascal.ast.Type.Variable makeTypeVariable(INode node, org.meta_environment.rascal.ast.TypeVar typeVar) { 
return new org.meta_environment.rascal.ast.Type.Variable(node, typeVar); 
}
public org.meta_environment.rascal.ast.Type.Function makeTypeFunction(INode node, org.meta_environment.rascal.ast.FunctionType function) { 
return new org.meta_environment.rascal.ast.Type.Function(node, function); 
}
public org.meta_environment.rascal.ast.Type.Structured makeTypeStructured(INode node, org.meta_environment.rascal.ast.StructuredType structured) { 
return new org.meta_environment.rascal.ast.Type.Structured(node, structured); 
}
public org.meta_environment.rascal.ast.Type.Ambiguity makeTypeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Type> alternatives) { 
return new org.meta_environment.rascal.ast.Type.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Type.Basic makeTypeBasic(INode node, org.meta_environment.rascal.ast.BasicType basic) { 
return new org.meta_environment.rascal.ast.Type.Basic(node, basic); 
}
public org.meta_environment.rascal.ast.Bound.Default makeBoundDefault(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Bound.Default(node, expression); 
}
public org.meta_environment.rascal.ast.Bound.Ambiguity makeBoundAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Bound> alternatives) { 
return new org.meta_environment.rascal.ast.Bound.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Bound.Empty makeBoundEmpty(INode node) { 
return new org.meta_environment.rascal.ast.Bound.Empty(node); 
}
public org.meta_environment.rascal.ast.Statement.GlobalDirective makeStatementGlobalDirective(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.QualifiedName> names) { 
return new org.meta_environment.rascal.ast.Statement.GlobalDirective(node, type, names); 
}
public org.meta_environment.rascal.ast.Statement.VariableDeclaration makeStatementVariableDeclaration(INode node, org.meta_environment.rascal.ast.LocalVariableDeclaration declaration) { 
return new org.meta_environment.rascal.ast.Statement.VariableDeclaration(node, declaration); 
}
public org.meta_environment.rascal.ast.Statement.FunctionDeclaration makeStatementFunctionDeclaration(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) { 
return new org.meta_environment.rascal.ast.Statement.FunctionDeclaration(node, functionDeclaration); 
}
public org.meta_environment.rascal.ast.Statement.Block makeStatementBlock(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
return new org.meta_environment.rascal.ast.Statement.Block(node, label, statements); 
}
public org.meta_environment.rascal.ast.Statement.TryFinally makeStatementTryFinally(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers, org.meta_environment.rascal.ast.Statement finallyBody) { 
return new org.meta_environment.rascal.ast.Statement.TryFinally(node, body, handlers, finallyBody); 
}
public org.meta_environment.rascal.ast.Statement.Try makeStatementTry(INode node, org.meta_environment.rascal.ast.Statement body, java.util.List<org.meta_environment.rascal.ast.Catch> handlers) { 
return new org.meta_environment.rascal.ast.Statement.Try(node, body, handlers); 
}
public org.meta_environment.rascal.ast.Statement.Throw makeStatementThrow(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Statement.Throw(node, expression); 
}
public org.meta_environment.rascal.ast.Statement.Insert makeStatementInsert(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Statement.Insert(node, expression); 
}
public org.meta_environment.rascal.ast.Statement.AssertWithMessage makeStatementAssertWithMessage(INode node, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringLiteral message) { 
return new org.meta_environment.rascal.ast.Statement.AssertWithMessage(node, expression, message); 
}
public org.meta_environment.rascal.ast.Statement.Assert makeStatementAssert(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Statement.Assert(node, expression); 
}
public org.meta_environment.rascal.ast.Statement.Continue makeStatementContinue(INode node) { 
return new org.meta_environment.rascal.ast.Statement.Continue(node); 
}
public org.meta_environment.rascal.ast.Statement.Return makeStatementReturn(INode node, org.meta_environment.rascal.ast.Return ret) { 
return new org.meta_environment.rascal.ast.Statement.Return(node, ret); 
}
public org.meta_environment.rascal.ast.Statement.Fail makeStatementFail(INode node, org.meta_environment.rascal.ast.Fail fail) { 
return new org.meta_environment.rascal.ast.Statement.Fail(node, fail); 
}
public org.meta_environment.rascal.ast.Statement.Break makeStatementBreak(INode node, org.meta_environment.rascal.ast.Break brk) { 
return new org.meta_environment.rascal.ast.Statement.Break(node, brk); 
}
public org.meta_environment.rascal.ast.Statement.Assignment makeStatementAssignment(INode node, org.meta_environment.rascal.ast.Assignable assignable, org.meta_environment.rascal.ast.Assignment operator, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Statement.Assignment(node, assignable, operator, expression); 
}
public org.meta_environment.rascal.ast.Statement.Visit makeStatementVisit(INode node, org.meta_environment.rascal.ast.Visit visit) { 
return new org.meta_environment.rascal.ast.Statement.Visit(node, visit); 
}
public org.meta_environment.rascal.ast.Statement.Expression makeStatementExpression(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Statement.Expression(node, expression); 
}
public org.meta_environment.rascal.ast.Statement.EmptyStatement makeStatementEmptyStatement(INode node) { 
return new org.meta_environment.rascal.ast.Statement.EmptyStatement(node); 
}
public org.meta_environment.rascal.ast.Statement.Switch makeStatementSwitch(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression expression, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
return new org.meta_environment.rascal.ast.Statement.Switch(node, label, expression, cases); 
}
public org.meta_environment.rascal.ast.Statement.IfThen makeStatementIfThen(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.NoElseMayFollow noElseMayFollow) { 
return new org.meta_environment.rascal.ast.Statement.IfThen(node, label, conditions, thenStatement, noElseMayFollow); 
}
public org.meta_environment.rascal.ast.Statement.IfThenElse makeStatementIfThenElse(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> conditions, org.meta_environment.rascal.ast.Statement thenStatement, org.meta_environment.rascal.ast.Statement elseStatement) { 
return new org.meta_environment.rascal.ast.Statement.IfThenElse(node, label, conditions, thenStatement, elseStatement); 
}
public org.meta_environment.rascal.ast.Statement.DoWhile makeStatementDoWhile(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Statement body, org.meta_environment.rascal.ast.Expression condition) { 
return new org.meta_environment.rascal.ast.Statement.DoWhile(node, label, body, condition); 
}
public org.meta_environment.rascal.ast.Statement.While makeStatementWhile(INode node, org.meta_environment.rascal.ast.Label label, org.meta_environment.rascal.ast.Expression condition, org.meta_environment.rascal.ast.Statement body) { 
return new org.meta_environment.rascal.ast.Statement.While(node, label, condition, body); 
}
public org.meta_environment.rascal.ast.Statement.For makeStatementFor(INode node, org.meta_environment.rascal.ast.Label label, java.util.List<org.meta_environment.rascal.ast.Expression> generators, org.meta_environment.rascal.ast.Statement body) { 
return new org.meta_environment.rascal.ast.Statement.For(node, label, generators, body); 
}
public org.meta_environment.rascal.ast.Statement.Ambiguity makeStatementAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> alternatives) { 
return new org.meta_environment.rascal.ast.Statement.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Statement.Solve makeStatementSolve(INode node, java.util.List<org.meta_environment.rascal.ast.QualifiedName> variables, org.meta_environment.rascal.ast.Bound bound, org.meta_environment.rascal.ast.Statement body) { 
return new org.meta_environment.rascal.ast.Statement.Solve(node, variables, bound, body); 
}
public org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity makeNoElseMayFollowAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NoElseMayFollow> alternatives) { 
return new org.meta_environment.rascal.ast.NoElseMayFollow.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.NoElseMayFollow.Default makeNoElseMayFollowDefault(INode node) { 
return new org.meta_environment.rascal.ast.NoElseMayFollow.Default(node); 
}
public org.meta_environment.rascal.ast.Assignable.Constructor makeAssignableConstructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Assignable> arguments) { 
return new org.meta_environment.rascal.ast.Assignable.Constructor(node, name, arguments); 
}
public org.meta_environment.rascal.ast.Assignable.Tuple makeAssignableTuple(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> elements) { 
return new org.meta_environment.rascal.ast.Assignable.Tuple(node, elements); 
}
public org.meta_environment.rascal.ast.Assignable.Annotation makeAssignableAnnotation(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name annotation) { 
return new org.meta_environment.rascal.ast.Assignable.Annotation(node, receiver, annotation); 
}
public org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault makeAssignableIfDefinedOrDefault(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression defaultExpression) { 
return new org.meta_environment.rascal.ast.Assignable.IfDefinedOrDefault(node, receiver, defaultExpression); 
}
public org.meta_environment.rascal.ast.Assignable.FieldAccess makeAssignableFieldAccess(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Name field) { 
return new org.meta_environment.rascal.ast.Assignable.FieldAccess(node, receiver, field); 
}
public org.meta_environment.rascal.ast.Assignable.Subscript makeAssignableSubscript(INode node, org.meta_environment.rascal.ast.Assignable receiver, org.meta_environment.rascal.ast.Expression subscript) { 
return new org.meta_environment.rascal.ast.Assignable.Subscript(node, receiver, subscript); 
}
public org.meta_environment.rascal.ast.Assignable.Ambiguity makeAssignableAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignable> alternatives) { 
return new org.meta_environment.rascal.ast.Assignable.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Assignable.Variable makeAssignableVariable(INode node, org.meta_environment.rascal.ast.QualifiedName qualifiedName) { 
return new org.meta_environment.rascal.ast.Assignable.Variable(node, qualifiedName); 
}
public org.meta_environment.rascal.ast.Assignment.IfDefined makeAssignmentIfDefined(INode node) { 
return new org.meta_environment.rascal.ast.Assignment.IfDefined(node); 
}
public org.meta_environment.rascal.ast.Assignment.Intersection makeAssignmentIntersection(INode node) { 
return new org.meta_environment.rascal.ast.Assignment.Intersection(node); 
}
public org.meta_environment.rascal.ast.Assignment.Division makeAssignmentDivision(INode node) { 
return new org.meta_environment.rascal.ast.Assignment.Division(node); 
}
public org.meta_environment.rascal.ast.Assignment.Product makeAssignmentProduct(INode node) { 
return new org.meta_environment.rascal.ast.Assignment.Product(node); 
}
public org.meta_environment.rascal.ast.Assignment.Subtraction makeAssignmentSubtraction(INode node) { 
return new org.meta_environment.rascal.ast.Assignment.Subtraction(node); 
}
public org.meta_environment.rascal.ast.Assignment.Addition makeAssignmentAddition(INode node) { 
return new org.meta_environment.rascal.ast.Assignment.Addition(node); 
}
public org.meta_environment.rascal.ast.Assignment.Ambiguity makeAssignmentAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives) { 
return new org.meta_environment.rascal.ast.Assignment.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Assignment.Default makeAssignmentDefault(INode node) { 
return new org.meta_environment.rascal.ast.Assignment.Default(node); 
}
public org.meta_environment.rascal.ast.Label.Default makeLabelDefault(INode node, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Label.Default(node, name); 
}
public org.meta_environment.rascal.ast.Label.Ambiguity makeLabelAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Label> alternatives) { 
return new org.meta_environment.rascal.ast.Label.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Label.Empty makeLabelEmpty(INode node) { 
return new org.meta_environment.rascal.ast.Label.Empty(node); 
}
public org.meta_environment.rascal.ast.Break.NoLabel makeBreakNoLabel(INode node) { 
return new org.meta_environment.rascal.ast.Break.NoLabel(node); 
}
public org.meta_environment.rascal.ast.Break.Ambiguity makeBreakAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Break> alternatives) { 
return new org.meta_environment.rascal.ast.Break.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Break.WithLabel makeBreakWithLabel(INode node, org.meta_environment.rascal.ast.Name label) { 
return new org.meta_environment.rascal.ast.Break.WithLabel(node, label); 
}
public org.meta_environment.rascal.ast.Fail.NoLabel makeFailNoLabel(INode node) { 
return new org.meta_environment.rascal.ast.Fail.NoLabel(node); 
}
public org.meta_environment.rascal.ast.Fail.Ambiguity makeFailAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Fail> alternatives) { 
return new org.meta_environment.rascal.ast.Fail.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Fail.WithLabel makeFailWithLabel(INode node, org.meta_environment.rascal.ast.Name label) { 
return new org.meta_environment.rascal.ast.Fail.WithLabel(node, label); 
}
public org.meta_environment.rascal.ast.Return.NoExpression makeReturnNoExpression(INode node) { 
return new org.meta_environment.rascal.ast.Return.NoExpression(node); 
}
public org.meta_environment.rascal.ast.Return.Ambiguity makeReturnAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Return> alternatives) { 
return new org.meta_environment.rascal.ast.Return.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Return.WithExpression makeReturnWithExpression(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Return.WithExpression(node, expression); 
}
public org.meta_environment.rascal.ast.Catch.Binding makeCatchBinding(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement body) { 
return new org.meta_environment.rascal.ast.Catch.Binding(node, pattern, body); 
}
public org.meta_environment.rascal.ast.Catch.Ambiguity makeCatchAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Catch> alternatives) { 
return new org.meta_environment.rascal.ast.Catch.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Catch.Default makeCatchDefault(INode node, org.meta_environment.rascal.ast.Statement body) { 
return new org.meta_environment.rascal.ast.Catch.Default(node, body); 
}
public org.meta_environment.rascal.ast.Declarator.Ambiguity makeDeclaratorAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declarator> alternatives) { 
return new org.meta_environment.rascal.ast.Declarator.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Declarator.Default makeDeclaratorDefault(INode node, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) { 
return new org.meta_environment.rascal.ast.Declarator.Default(node, type, variables); 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic makeLocalVariableDeclarationDynamic(INode node, org.meta_environment.rascal.ast.Declarator declarator) { 
return new org.meta_environment.rascal.ast.LocalVariableDeclaration.Dynamic(node, declarator); 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity makeLocalVariableDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.LocalVariableDeclaration> alternatives) { 
return new org.meta_environment.rascal.ast.LocalVariableDeclaration.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.LocalVariableDeclaration.Default makeLocalVariableDeclarationDefault(INode node, org.meta_environment.rascal.ast.Declarator declarator) { 
return new org.meta_environment.rascal.ast.LocalVariableDeclaration.Default(node, declarator); 
}
public org.meta_environment.rascal.ast.Mapping.Ambiguity makeMappingAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives) { 
return new org.meta_environment.rascal.ast.Mapping.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Mapping.Default makeMappingDefault(INode node, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to) { 
return new org.meta_environment.rascal.ast.Mapping.Default(node, from, to); 
}
public org.meta_environment.rascal.ast.Strategy.Innermost makeStrategyInnermost(INode node) { 
return new org.meta_environment.rascal.ast.Strategy.Innermost(node); 
}
public org.meta_environment.rascal.ast.Strategy.Outermost makeStrategyOutermost(INode node) { 
return new org.meta_environment.rascal.ast.Strategy.Outermost(node); 
}
public org.meta_environment.rascal.ast.Strategy.BottomUpBreak makeStrategyBottomUpBreak(INode node) { 
return new org.meta_environment.rascal.ast.Strategy.BottomUpBreak(node); 
}
public org.meta_environment.rascal.ast.Strategy.BottomUp makeStrategyBottomUp(INode node) { 
return new org.meta_environment.rascal.ast.Strategy.BottomUp(node); 
}
public org.meta_environment.rascal.ast.Strategy.TopDownBreak makeStrategyTopDownBreak(INode node) { 
return new org.meta_environment.rascal.ast.Strategy.TopDownBreak(node); 
}
public org.meta_environment.rascal.ast.Strategy.Ambiguity makeStrategyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives) { 
return new org.meta_environment.rascal.ast.Strategy.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Strategy.TopDown makeStrategyTopDown(INode node) { 
return new org.meta_environment.rascal.ast.Strategy.TopDown(node); 
}
public org.meta_environment.rascal.ast.Comprehension.Map makeComprehensionMap(INode node, org.meta_environment.rascal.ast.Expression from, org.meta_environment.rascal.ast.Expression to, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
return new org.meta_environment.rascal.ast.Comprehension.Map(node, from, to, generators); 
}
public org.meta_environment.rascal.ast.Comprehension.List makeComprehensionList(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> results, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
return new org.meta_environment.rascal.ast.Comprehension.List(node, results, generators); 
}
public org.meta_environment.rascal.ast.Comprehension.Ambiguity makeComprehensionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Comprehension> alternatives) { 
return new org.meta_environment.rascal.ast.Comprehension.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Comprehension.Set makeComprehensionSet(INode node, java.util.List<org.meta_environment.rascal.ast.Expression> results, java.util.List<org.meta_environment.rascal.ast.Expression> generators) { 
return new org.meta_environment.rascal.ast.Comprehension.Set(node, results, generators); 
}
public org.meta_environment.rascal.ast.Replacement.Conditional makeReplacementConditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression, java.util.List<org.meta_environment.rascal.ast.Expression> conditions) { 
return new org.meta_environment.rascal.ast.Replacement.Conditional(node, replacementExpression, conditions); 
}
public org.meta_environment.rascal.ast.Replacement.Ambiguity makeReplacementAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Replacement> alternatives) { 
return new org.meta_environment.rascal.ast.Replacement.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Replacement.Unconditional makeReplacementUnconditional(INode node, org.meta_environment.rascal.ast.Expression replacementExpression) { 
return new org.meta_environment.rascal.ast.Replacement.Unconditional(node, replacementExpression); 
}
public org.meta_environment.rascal.ast.PatternWithAction.Arbitrary makePatternWithActionArbitrary(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Statement statement) { 
return new org.meta_environment.rascal.ast.PatternWithAction.Arbitrary(node, pattern, statement); 
}
public org.meta_environment.rascal.ast.PatternWithAction.Ambiguity makePatternWithActionAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.PatternWithAction> alternatives) { 
return new org.meta_environment.rascal.ast.PatternWithAction.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.PatternWithAction.Replacing makePatternWithActionReplacing(INode node, org.meta_environment.rascal.ast.Expression pattern, org.meta_environment.rascal.ast.Replacement replacement) { 
return new org.meta_environment.rascal.ast.PatternWithAction.Replacing(node, pattern, replacement); 
}
public org.meta_environment.rascal.ast.Case.Default makeCaseDefault(INode node, org.meta_environment.rascal.ast.Statement statement) { 
return new org.meta_environment.rascal.ast.Case.Default(node, statement); 
}
public org.meta_environment.rascal.ast.Case.Ambiguity makeCaseAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Case> alternatives) { 
return new org.meta_environment.rascal.ast.Case.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Case.PatternWithAction makeCasePatternWithAction(INode node, org.meta_environment.rascal.ast.PatternWithAction patternWithAction) { 
return new org.meta_environment.rascal.ast.Case.PatternWithAction(node, patternWithAction); 
}
public org.meta_environment.rascal.ast.Visit.GivenStrategy makeVisitGivenStrategy(INode node, org.meta_environment.rascal.ast.Strategy strategy, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
return new org.meta_environment.rascal.ast.Visit.GivenStrategy(node, strategy, subject, cases); 
}
public org.meta_environment.rascal.ast.Visit.Ambiguity makeVisitAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Visit> alternatives) { 
return new org.meta_environment.rascal.ast.Visit.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Visit.DefaultStrategy makeVisitDefaultStrategy(INode node, org.meta_environment.rascal.ast.Expression subject, java.util.List<org.meta_environment.rascal.ast.Case> cases) { 
return new org.meta_environment.rascal.ast.Visit.DefaultStrategy(node, subject, cases); 
}
public org.meta_environment.rascal.ast.Module.Ambiguity makeModuleAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Module> alternatives) { 
return new org.meta_environment.rascal.ast.Module.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Module.Default makeModuleDefault(INode node, org.meta_environment.rascal.ast.Header header, org.meta_environment.rascal.ast.Body body) { 
return new org.meta_environment.rascal.ast.Module.Default(node, header, body); 
}
public org.meta_environment.rascal.ast.ModuleActuals.Ambiguity makeModuleActualsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ModuleActuals> alternatives) { 
return new org.meta_environment.rascal.ast.ModuleActuals.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ModuleActuals.Default makeModuleActualsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Type> types) { 
return new org.meta_environment.rascal.ast.ModuleActuals.Default(node, types); 
}
public org.meta_environment.rascal.ast.ImportedModule.Default makeImportedModuleDefault(INode node, org.meta_environment.rascal.ast.QualifiedName name) { 
return new org.meta_environment.rascal.ast.ImportedModule.Default(node, name); 
}
public org.meta_environment.rascal.ast.ImportedModule.Renamings makeImportedModuleRenamings(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Renamings renamings) { 
return new org.meta_environment.rascal.ast.ImportedModule.Renamings(node, name, renamings); 
}
public org.meta_environment.rascal.ast.ImportedModule.Actuals makeImportedModuleActuals(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals) { 
return new org.meta_environment.rascal.ast.ImportedModule.Actuals(node, name, actuals); 
}
public org.meta_environment.rascal.ast.ImportedModule.Ambiguity makeImportedModuleAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ImportedModule> alternatives) { 
return new org.meta_environment.rascal.ast.ImportedModule.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming makeImportedModuleActualsRenaming(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleActuals actuals, org.meta_environment.rascal.ast.Renamings renamings) { 
return new org.meta_environment.rascal.ast.ImportedModule.ActualsRenaming(node, name, actuals, renamings); 
}
public org.meta_environment.rascal.ast.Renaming.Ambiguity makeRenamingAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Renaming> alternatives) { 
return new org.meta_environment.rascal.ast.Renaming.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Renaming.Default makeRenamingDefault(INode node, org.meta_environment.rascal.ast.Name from, org.meta_environment.rascal.ast.Name to) { 
return new org.meta_environment.rascal.ast.Renaming.Default(node, from, to); 
}
public org.meta_environment.rascal.ast.Renamings.Ambiguity makeRenamingsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Renamings> alternatives) { 
return new org.meta_environment.rascal.ast.Renamings.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Renamings.Default makeRenamingsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Renaming> renamings) { 
return new org.meta_environment.rascal.ast.Renamings.Default(node, renamings); 
}
public org.meta_environment.rascal.ast.Import.Extend makeImportExtend(INode node, org.meta_environment.rascal.ast.ImportedModule module) { 
return new org.meta_environment.rascal.ast.Import.Extend(node, module); 
}
public org.meta_environment.rascal.ast.Import.Ambiguity makeImportAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Import> alternatives) { 
return new org.meta_environment.rascal.ast.Import.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Import.Default makeImportDefault(INode node, org.meta_environment.rascal.ast.ImportedModule module) { 
return new org.meta_environment.rascal.ast.Import.Default(node, module); 
}
public org.meta_environment.rascal.ast.ModuleParameters.Ambiguity makeModuleParametersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ModuleParameters> alternatives) { 
return new org.meta_environment.rascal.ast.ModuleParameters.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ModuleParameters.Default makeModuleParametersDefault(INode node, java.util.List<org.meta_environment.rascal.ast.TypeVar> parameters) { 
return new org.meta_environment.rascal.ast.ModuleParameters.Default(node, parameters); 
}
public org.meta_environment.rascal.ast.Header.Parameters makeHeaderParameters(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.ModuleParameters params, java.util.List<org.meta_environment.rascal.ast.Import> imports) { 
return new org.meta_environment.rascal.ast.Header.Parameters(node, tags, name, params, imports); 
}
public org.meta_environment.rascal.ast.Header.Ambiguity makeHeaderAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Header> alternatives) { 
return new org.meta_environment.rascal.ast.Header.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Header.Default makeHeaderDefault(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.QualifiedName name, java.util.List<org.meta_environment.rascal.ast.Import> imports) { 
return new org.meta_environment.rascal.ast.Header.Default(node, tags, name, imports); 
}
public org.meta_environment.rascal.ast.StrChar.Lexical makeStrCharLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.StrChar.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.StrChar.Ambiguity makeStrCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives) { 
return new org.meta_environment.rascal.ast.StrChar.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.StrChar.newline makeStrCharnewline(INode node) { 
return new org.meta_environment.rascal.ast.StrChar.newline(node); 
}
public org.meta_environment.rascal.ast.StrCon.Ambiguity makeStrConAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives) { 
return new org.meta_environment.rascal.ast.StrCon.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.StrCon.Lexical makeStrConLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.StrCon.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity makeSingleQuotedStrCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) { 
return new org.meta_environment.rascal.ast.SingleQuotedStrChar.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical makeSingleQuotedStrCharLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.SingleQuotedStrChar.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity makeSingleQuotedStrConAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> alternatives) { 
return new org.meta_environment.rascal.ast.SingleQuotedStrCon.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical makeSingleQuotedStrConLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.SingleQuotedStrCon.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Symbol.Sort makeSymbolSort(INode node, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Symbol.Sort(node, name); 
}
public org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral makeSymbolCaseInsensitiveLiteral(INode node, org.meta_environment.rascal.ast.SingleQuotedStrCon singelQuotedString) { 
return new org.meta_environment.rascal.ast.Symbol.CaseInsensitiveLiteral(node, singelQuotedString); 
}
public org.meta_environment.rascal.ast.Symbol.Literal makeSymbolLiteral(INode node, org.meta_environment.rascal.ast.StrCon string) { 
return new org.meta_environment.rascal.ast.Symbol.Literal(node, string); 
}
public org.meta_environment.rascal.ast.Symbol.CharacterClass makeSymbolCharacterClass(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
return new org.meta_environment.rascal.ast.Symbol.CharacterClass(node, charClass); 
}
public org.meta_environment.rascal.ast.Symbol.Alternative makeSymbolAlternative(INode node, org.meta_environment.rascal.ast.Symbol lhs, org.meta_environment.rascal.ast.Symbol rhs) { 
return new org.meta_environment.rascal.ast.Symbol.Alternative(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.Symbol.IterStarSep makeSymbolIterStarSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) { 
return new org.meta_environment.rascal.ast.Symbol.IterStarSep(node, symbol, sep); 
}
public org.meta_environment.rascal.ast.Symbol.IterSep makeSymbolIterSep(INode node, org.meta_environment.rascal.ast.Symbol symbol, org.meta_environment.rascal.ast.StrCon sep) { 
return new org.meta_environment.rascal.ast.Symbol.IterSep(node, symbol, sep); 
}
public org.meta_environment.rascal.ast.Symbol.IterStar makeSymbolIterStar(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
return new org.meta_environment.rascal.ast.Symbol.IterStar(node, symbol); 
}
public org.meta_environment.rascal.ast.Symbol.Iter makeSymbolIter(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
return new org.meta_environment.rascal.ast.Symbol.Iter(node, symbol); 
}
public org.meta_environment.rascal.ast.Symbol.Optional makeSymbolOptional(INode node, org.meta_environment.rascal.ast.Symbol symbol) { 
return new org.meta_environment.rascal.ast.Symbol.Optional(node, symbol); 
}
public org.meta_environment.rascal.ast.Symbol.Sequence makeSymbolSequence(INode node, org.meta_environment.rascal.ast.Symbol head, java.util.List<org.meta_environment.rascal.ast.Symbol> tail) { 
return new org.meta_environment.rascal.ast.Symbol.Sequence(node, head, tail); 
}
public org.meta_environment.rascal.ast.Symbol.Ambiguity makeSymbolAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Symbol> alternatives) { 
return new org.meta_environment.rascal.ast.Symbol.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Symbol.Empty makeSymbolEmpty(INode node) { 
return new org.meta_environment.rascal.ast.Symbol.Empty(node); 
}
public org.meta_environment.rascal.ast.CharRange.Range makeCharRangeRange(INode node, org.meta_environment.rascal.ast.Character start, org.meta_environment.rascal.ast.Character end) { 
return new org.meta_environment.rascal.ast.CharRange.Range(node, start, end); 
}
public org.meta_environment.rascal.ast.CharRange.Ambiguity makeCharRangeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharRange> alternatives) { 
return new org.meta_environment.rascal.ast.CharRange.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.CharRange.Character makeCharRangeCharacter(INode node, org.meta_environment.rascal.ast.Character character) { 
return new org.meta_environment.rascal.ast.CharRange.Character(node, character); 
}
public org.meta_environment.rascal.ast.CharRanges.Bracket makeCharRangesBracket(INode node, org.meta_environment.rascal.ast.CharRanges ranges) { 
return new org.meta_environment.rascal.ast.CharRanges.Bracket(node, ranges); 
}
public org.meta_environment.rascal.ast.CharRanges.Concatenate makeCharRangesConcatenate(INode node, org.meta_environment.rascal.ast.CharRanges lhs, org.meta_environment.rascal.ast.CharRanges rhs) { 
return new org.meta_environment.rascal.ast.CharRanges.Concatenate(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.CharRanges.Ambiguity makeCharRangesAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives) { 
return new org.meta_environment.rascal.ast.CharRanges.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.CharRanges.Range makeCharRangesRange(INode node, org.meta_environment.rascal.ast.CharRange range) { 
return new org.meta_environment.rascal.ast.CharRanges.Range(node, range); 
}
public org.meta_environment.rascal.ast.OptCharRanges.Present makeOptCharRangesPresent(INode node, org.meta_environment.rascal.ast.CharRanges ranges) { 
return new org.meta_environment.rascal.ast.OptCharRanges.Present(node, ranges); 
}
public org.meta_environment.rascal.ast.OptCharRanges.Ambiguity makeOptCharRangesAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.OptCharRanges> alternatives) { 
return new org.meta_environment.rascal.ast.OptCharRanges.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.OptCharRanges.Absent makeOptCharRangesAbsent(INode node) { 
return new org.meta_environment.rascal.ast.OptCharRanges.Absent(node); 
}
public org.meta_environment.rascal.ast.CharClass.Union makeCharClassUnion(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
return new org.meta_environment.rascal.ast.CharClass.Union(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.CharClass.Intersection makeCharClassIntersection(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
return new org.meta_environment.rascal.ast.CharClass.Intersection(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.CharClass.Difference makeCharClassDifference(INode node, org.meta_environment.rascal.ast.CharClass lhs, org.meta_environment.rascal.ast.CharClass rhs) { 
return new org.meta_environment.rascal.ast.CharClass.Difference(node, lhs, rhs); 
}
public org.meta_environment.rascal.ast.CharClass.Complement makeCharClassComplement(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
return new org.meta_environment.rascal.ast.CharClass.Complement(node, charClass); 
}
public org.meta_environment.rascal.ast.CharClass.Bracket makeCharClassBracket(INode node, org.meta_environment.rascal.ast.CharClass charClass) { 
return new org.meta_environment.rascal.ast.CharClass.Bracket(node, charClass); 
}
public org.meta_environment.rascal.ast.CharClass.Ambiguity makeCharClassAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CharClass> alternatives) { 
return new org.meta_environment.rascal.ast.CharClass.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.CharClass.SimpleCharclass makeCharClassSimpleCharclass(INode node, org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges) { 
return new org.meta_environment.rascal.ast.CharClass.SimpleCharclass(node, optionalCharRanges); 
}
public org.meta_environment.rascal.ast.NumChar.Ambiguity makeNumCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NumChar> alternatives) { 
return new org.meta_environment.rascal.ast.NumChar.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.NumChar.Lexical makeNumCharLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.NumChar.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.ShortChar.Ambiguity makeShortCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ShortChar> alternatives) { 
return new org.meta_environment.rascal.ast.ShortChar.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ShortChar.Lexical makeShortCharLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.ShortChar.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Character.Bottom makeCharacterBottom(INode node) { 
return new org.meta_environment.rascal.ast.Character.Bottom(node); 
}
public org.meta_environment.rascal.ast.Character.EOF makeCharacterEOF(INode node) { 
return new org.meta_environment.rascal.ast.Character.EOF(node); 
}
public org.meta_environment.rascal.ast.Character.Top makeCharacterTop(INode node) { 
return new org.meta_environment.rascal.ast.Character.Top(node); 
}
public org.meta_environment.rascal.ast.Character.Short makeCharacterShort(INode node, org.meta_environment.rascal.ast.ShortChar shortChar) { 
return new org.meta_environment.rascal.ast.Character.Short(node, shortChar); 
}
public org.meta_environment.rascal.ast.Character.Ambiguity makeCharacterAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Character> alternatives) { 
return new org.meta_environment.rascal.ast.Character.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Character.Numeric makeCharacterNumeric(INode node, org.meta_environment.rascal.ast.NumChar numChar) { 
return new org.meta_environment.rascal.ast.Character.Numeric(node, numChar); 
}
public org.meta_environment.rascal.ast.Name.Ambiguity makeNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Name> alternatives) { 
return new org.meta_environment.rascal.ast.Name.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Name.Lexical makeNameLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Name.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.EscapedName.Ambiguity makeEscapedNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.EscapedName> alternatives) { 
return new org.meta_environment.rascal.ast.EscapedName.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.EscapedName.Lexical makeEscapedNameLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.EscapedName.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.QualifiedName.Ambiguity makeQualifiedNameAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.QualifiedName> alternatives) { 
return new org.meta_environment.rascal.ast.QualifiedName.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.QualifiedName.Default makeQualifiedNameDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Name> names) { 
return new org.meta_environment.rascal.ast.QualifiedName.Default(node, names); 
}
public org.meta_environment.rascal.ast.Command.Lexical makeCommandLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Command.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Command.Import makeCommandImport(INode node, org.meta_environment.rascal.ast.Import imported) { 
return new org.meta_environment.rascal.ast.Command.Import(node, imported); 
}
public org.meta_environment.rascal.ast.Command.Declaration makeCommandDeclaration(INode node, org.meta_environment.rascal.ast.Declaration declaration) { 
return new org.meta_environment.rascal.ast.Command.Declaration(node, declaration); 
}
public org.meta_environment.rascal.ast.Command.Expression makeCommandExpression(INode node, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Command.Expression(node, expression); 
}
public org.meta_environment.rascal.ast.Command.Statement makeCommandStatement(INode node, org.meta_environment.rascal.ast.Statement statement) { 
return new org.meta_environment.rascal.ast.Command.Statement(node, statement); 
}
public org.meta_environment.rascal.ast.Command.Ambiguity makeCommandAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Command> alternatives) { 
return new org.meta_environment.rascal.ast.Command.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Command.Shell makeCommandShell(INode node, org.meta_environment.rascal.ast.ShellCommand command) { 
return new org.meta_environment.rascal.ast.Command.Shell(node, command); 
}
public org.meta_environment.rascal.ast.ShellCommand.SetOption makeShellCommandSetOption(INode node, org.meta_environment.rascal.ast.QualifiedName name, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.ShellCommand.SetOption(node, name, expression); 
}
public org.meta_environment.rascal.ast.ShellCommand.History makeShellCommandHistory(INode node) { 
return new org.meta_environment.rascal.ast.ShellCommand.History(node); 
}
public org.meta_environment.rascal.ast.ShellCommand.Undeclare makeShellCommandUndeclare(INode node, org.meta_environment.rascal.ast.QualifiedName name) { 
return new org.meta_environment.rascal.ast.ShellCommand.Undeclare(node, name); 
}
public org.meta_environment.rascal.ast.ShellCommand.Unimport makeShellCommandUnimport(INode node, org.meta_environment.rascal.ast.QualifiedName name) { 
return new org.meta_environment.rascal.ast.ShellCommand.Unimport(node, name); 
}
public org.meta_environment.rascal.ast.ShellCommand.Test makeShellCommandTest(INode node) { 
return new org.meta_environment.rascal.ast.ShellCommand.Test(node); 
}
public org.meta_environment.rascal.ast.ShellCommand.ListDeclarations makeShellCommandListDeclarations(INode node) { 
return new org.meta_environment.rascal.ast.ShellCommand.ListDeclarations(node); 
}
public org.meta_environment.rascal.ast.ShellCommand.ListModules makeShellCommandListModules(INode node) { 
return new org.meta_environment.rascal.ast.ShellCommand.ListModules(node); 
}
public org.meta_environment.rascal.ast.ShellCommand.Edit makeShellCommandEdit(INode node, org.meta_environment.rascal.ast.QualifiedName name) { 
return new org.meta_environment.rascal.ast.ShellCommand.Edit(node, name); 
}
public org.meta_environment.rascal.ast.ShellCommand.Quit makeShellCommandQuit(INode node) { 
return new org.meta_environment.rascal.ast.ShellCommand.Quit(node); 
}
public org.meta_environment.rascal.ast.ShellCommand.Ambiguity makeShellCommandAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.ShellCommand> alternatives) { 
return new org.meta_environment.rascal.ast.ShellCommand.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.ShellCommand.Help makeShellCommandHelp(INode node) { 
return new org.meta_environment.rascal.ast.ShellCommand.Help(node); 
}
public org.meta_environment.rascal.ast.TagString.Ambiguity makeTagStringAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TagString> alternatives) { 
return new org.meta_environment.rascal.ast.TagString.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.TagString.Lexical makeTagStringLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.TagString.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.TagChar.Ambiguity makeTagCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives) { 
return new org.meta_environment.rascal.ast.TagChar.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.TagChar.Lexical makeTagCharLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.TagChar.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Tag.Empty makeTagEmpty(INode node, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Tag.Empty(node, name); 
}
public org.meta_environment.rascal.ast.Tag.Expression makeTagExpression(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Tag.Expression(node, name, expression); 
}
public org.meta_environment.rascal.ast.Tag.Ambiguity makeTagAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> alternatives) { 
return new org.meta_environment.rascal.ast.Tag.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Tag.Default makeTagDefault(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.TagString contents) { 
return new org.meta_environment.rascal.ast.Tag.Default(node, name, contents); 
}
public org.meta_environment.rascal.ast.Tags.Ambiguity makeTagsAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Tags> alternatives) { 
return new org.meta_environment.rascal.ast.Tags.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Tags.Default makeTagsDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Tag> tags) { 
return new org.meta_environment.rascal.ast.Tags.Default(node, tags); 
}
public org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity makeRegExpLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExpLiteral> alternatives) { 
return new org.meta_environment.rascal.ast.RegExpLiteral.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.RegExpLiteral.Lexical makeRegExpLiteralLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.RegExpLiteral.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.RegExpModifier.Ambiguity makeRegExpModifierAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExpModifier> alternatives) { 
return new org.meta_environment.rascal.ast.RegExpModifier.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.RegExpModifier.Lexical makeRegExpModifierLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.RegExpModifier.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Backslash.Ambiguity makeBackslashAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Backslash> alternatives) { 
return new org.meta_environment.rascal.ast.Backslash.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Backslash.Lexical makeBackslashLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Backslash.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.RegExp.Ambiguity makeRegExpAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.RegExp> alternatives) { 
return new org.meta_environment.rascal.ast.RegExp.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.RegExp.Lexical makeRegExpLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.RegExp.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.NamedRegExp.Ambiguity makeNamedRegExpAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NamedRegExp> alternatives) { 
return new org.meta_environment.rascal.ast.NamedRegExp.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.NamedRegExp.Lexical makeNamedRegExpLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.NamedRegExp.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.NamedBackslash.Ambiguity makeNamedBackslashAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.NamedBackslash> alternatives) { 
return new org.meta_environment.rascal.ast.NamedBackslash.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.NamedBackslash.Lexical makeNamedBackslashLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.NamedBackslash.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Visibility.Default makeVisibilityDefault(INode node) { 
return new org.meta_environment.rascal.ast.Visibility.Default(node); 
}
public org.meta_environment.rascal.ast.Visibility.Private makeVisibilityPrivate(INode node) { 
return new org.meta_environment.rascal.ast.Visibility.Private(node); 
}
public org.meta_environment.rascal.ast.Visibility.Ambiguity makeVisibilityAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives) { 
return new org.meta_environment.rascal.ast.Visibility.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Visibility.Public makeVisibilityPublic(INode node) { 
return new org.meta_environment.rascal.ast.Visibility.Public(node); 
}
public org.meta_environment.rascal.ast.Toplevel.Ambiguity makeToplevelAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Toplevel> alternatives) { 
return new org.meta_environment.rascal.ast.Toplevel.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Toplevel.GivenVisibility makeToplevelGivenVisibility(INode node, org.meta_environment.rascal.ast.Declaration declaration) { 
return new org.meta_environment.rascal.ast.Toplevel.GivenVisibility(node, declaration); 
}
public org.meta_environment.rascal.ast.Declaration.Tag makeDeclarationTag(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Kind kind, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.Type> types) { 
return new org.meta_environment.rascal.ast.Declaration.Tag(node, tags, visibility, kind, name, types); 
}
public org.meta_environment.rascal.ast.Declaration.Annotation makeDeclarationAnnotation(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Type annoType, org.meta_environment.rascal.ast.Type onType, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Declaration.Annotation(node, tags, visibility, annoType, onType, name); 
}
public org.meta_environment.rascal.ast.Declaration.Rule makeDeclarationRule(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.PatternWithAction patternAction) { 
return new org.meta_environment.rascal.ast.Declaration.Rule(node, tags, name, patternAction); 
}
public org.meta_environment.rascal.ast.Declaration.Variable makeDeclarationVariable(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Type type, java.util.List<org.meta_environment.rascal.ast.Variable> variables) { 
return new org.meta_environment.rascal.ast.Declaration.Variable(node, tags, visibility, type, variables); 
}
public org.meta_environment.rascal.ast.Declaration.Function makeDeclarationFunction(INode node, org.meta_environment.rascal.ast.FunctionDeclaration functionDeclaration) { 
return new org.meta_environment.rascal.ast.Declaration.Function(node, functionDeclaration); 
}
public org.meta_environment.rascal.ast.Declaration.Test makeDeclarationTest(INode node, org.meta_environment.rascal.ast.Test test) { 
return new org.meta_environment.rascal.ast.Declaration.Test(node, test); 
}
public org.meta_environment.rascal.ast.Declaration.Data makeDeclarationData(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.UserType user, java.util.List<org.meta_environment.rascal.ast.Variant> variants) { 
return new org.meta_environment.rascal.ast.Declaration.Data(node, tags, visibility, user, variants); 
}
public org.meta_environment.rascal.ast.Declaration.Alias makeDeclarationAlias(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.UserType user, org.meta_environment.rascal.ast.Type base) { 
return new org.meta_environment.rascal.ast.Declaration.Alias(node, tags, visibility, user, base); 
}
public org.meta_environment.rascal.ast.Declaration.Ambiguity makeDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Declaration> alternatives) { 
return new org.meta_environment.rascal.ast.Declaration.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Declaration.View makeDeclarationView(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Name view, org.meta_environment.rascal.ast.Name superType, java.util.List<org.meta_environment.rascal.ast.Alternative> alts) { 
return new org.meta_environment.rascal.ast.Declaration.View(node, tags, visibility, view, superType, alts); 
}
public org.meta_environment.rascal.ast.Alternative.Ambiguity makeAlternativeAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives) { 
return new org.meta_environment.rascal.ast.Alternative.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Alternative.NamedType makeAlternativeNamedType(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Type type) { 
return new org.meta_environment.rascal.ast.Alternative.NamedType(node, name, type); 
}
public org.meta_environment.rascal.ast.Variant.Ambiguity makeVariantAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variant> alternatives) { 
return new org.meta_environment.rascal.ast.Variant.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Variant.NAryConstructor makeVariantNAryConstructor(INode node, org.meta_environment.rascal.ast.Name name, java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) { 
return new org.meta_environment.rascal.ast.Variant.NAryConstructor(node, name, arguments); 
}
public org.meta_environment.rascal.ast.Test.Labeled makeTestLabeled(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Expression expression, org.meta_environment.rascal.ast.StringLiteral labeled) { 
return new org.meta_environment.rascal.ast.Test.Labeled(node, tags, expression, labeled); 
}
public org.meta_environment.rascal.ast.Test.Ambiguity makeTestAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Test> alternatives) { 
return new org.meta_environment.rascal.ast.Test.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Test.Unlabeled makeTestUnlabeled(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Expression expression) { 
return new org.meta_environment.rascal.ast.Test.Unlabeled(node, tags, expression); 
}
public org.meta_environment.rascal.ast.FunctionModifier.Ambiguity makeFunctionModifierAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> alternatives) { 
return new org.meta_environment.rascal.ast.FunctionModifier.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.FunctionModifier.Java makeFunctionModifierJava(INode node) { 
return new org.meta_environment.rascal.ast.FunctionModifier.Java(node); 
}
public org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity makeFunctionModifiersAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifiers> alternatives) { 
return new org.meta_environment.rascal.ast.FunctionModifiers.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.FunctionModifiers.List makeFunctionModifiersList(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionModifier> modifiers) { 
return new org.meta_environment.rascal.ast.FunctionModifiers.List(node, modifiers); 
}
public org.meta_environment.rascal.ast.Signature.WithThrows makeSignatureWithThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters, java.util.List<org.meta_environment.rascal.ast.Type> exceptions) { 
return new org.meta_environment.rascal.ast.Signature.WithThrows(node, type, modifiers, name, parameters, exceptions); 
}
public org.meta_environment.rascal.ast.Signature.Ambiguity makeSignatureAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Signature> alternatives) { 
return new org.meta_environment.rascal.ast.Signature.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Signature.NoThrows makeSignatureNoThrows(INode node, org.meta_environment.rascal.ast.Type type, org.meta_environment.rascal.ast.FunctionModifiers modifiers, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Parameters parameters) { 
return new org.meta_environment.rascal.ast.Signature.NoThrows(node, type, modifiers, name, parameters); 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Abstract makeFunctionDeclarationAbstract(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Signature signature) { 
return new org.meta_environment.rascal.ast.FunctionDeclaration.Abstract(node, tags, visibility, signature); 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity makeFunctionDeclarationAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives) { 
return new org.meta_environment.rascal.ast.FunctionDeclaration.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.FunctionDeclaration.Default makeFunctionDeclarationDefault(INode node, org.meta_environment.rascal.ast.Tags tags, org.meta_environment.rascal.ast.Visibility visibility, org.meta_environment.rascal.ast.Signature signature, org.meta_environment.rascal.ast.FunctionBody body) { 
return new org.meta_environment.rascal.ast.FunctionDeclaration.Default(node, tags, visibility, signature, body); 
}
public org.meta_environment.rascal.ast.FunctionBody.Ambiguity makeFunctionBodyAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.FunctionBody> alternatives) { 
return new org.meta_environment.rascal.ast.FunctionBody.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.FunctionBody.Default makeFunctionBodyDefault(INode node, java.util.List<org.meta_environment.rascal.ast.Statement> statements) { 
return new org.meta_environment.rascal.ast.FunctionBody.Default(node, statements); 
}
public org.meta_environment.rascal.ast.Variable.Initialized makeVariableInitialized(INode node, org.meta_environment.rascal.ast.Name name, org.meta_environment.rascal.ast.Expression initial) { 
return new org.meta_environment.rascal.ast.Variable.Initialized(node, name, initial); 
}
public org.meta_environment.rascal.ast.Variable.Ambiguity makeVariableAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Variable> alternatives) { 
return new org.meta_environment.rascal.ast.Variable.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Variable.UnInitialized makeVariableUnInitialized(INode node, org.meta_environment.rascal.ast.Name name) { 
return new org.meta_environment.rascal.ast.Variable.UnInitialized(node, name); 
}
public org.meta_environment.rascal.ast.Kind.All makeKindAll(INode node) { 
return new org.meta_environment.rascal.ast.Kind.All(node); 
}
public org.meta_environment.rascal.ast.Kind.Tag makeKindTag(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Tag(node); 
}
public org.meta_environment.rascal.ast.Kind.Anno makeKindAnno(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Anno(node); 
}
public org.meta_environment.rascal.ast.Kind.Alias makeKindAlias(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Alias(node); 
}
public org.meta_environment.rascal.ast.Kind.View makeKindView(INode node) { 
return new org.meta_environment.rascal.ast.Kind.View(node); 
}
public org.meta_environment.rascal.ast.Kind.Data makeKindData(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Data(node); 
}
public org.meta_environment.rascal.ast.Kind.Variable makeKindVariable(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Variable(node); 
}
public org.meta_environment.rascal.ast.Kind.Rule makeKindRule(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Rule(node); 
}
public org.meta_environment.rascal.ast.Kind.Function makeKindFunction(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Function(node); 
}
public org.meta_environment.rascal.ast.Kind.Ambiguity makeKindAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Kind> alternatives) { 
return new org.meta_environment.rascal.ast.Kind.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Kind.Module makeKindModule(INode node) { 
return new org.meta_environment.rascal.ast.Kind.Module(node); 
}
public org.meta_environment.rascal.ast.Comment.Ambiguity makeCommentAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Comment> alternatives) { 
return new org.meta_environment.rascal.ast.Comment.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Comment.Lexical makeCommentLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Comment.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.CommentChar.Ambiguity makeCommentCharAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives) { 
return new org.meta_environment.rascal.ast.CommentChar.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.CommentChar.Lexical makeCommentCharLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.CommentChar.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Asterisk.Ambiguity makeAsteriskAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Asterisk> alternatives) { 
return new org.meta_environment.rascal.ast.Asterisk.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Asterisk.Lexical makeAsteriskLexical(INode node, String string) { 
return new org.meta_environment.rascal.ast.Asterisk.Lexical(node, string); 
}
public org.meta_environment.rascal.ast.Literal.Location makeLiteralLocation(INode node, org.meta_environment.rascal.ast.LocationLiteral locationLiteral) { 
return new org.meta_environment.rascal.ast.Literal.Location(node, locationLiteral); 
}
public org.meta_environment.rascal.ast.Literal.String makeLiteralString(INode node, org.meta_environment.rascal.ast.StringLiteral stringLiteral) { 
return new org.meta_environment.rascal.ast.Literal.String(node, stringLiteral); 
}
public org.meta_environment.rascal.ast.Literal.Real makeLiteralReal(INode node, org.meta_environment.rascal.ast.RealLiteral realLiteral) { 
return new org.meta_environment.rascal.ast.Literal.Real(node, realLiteral); 
}
public org.meta_environment.rascal.ast.Literal.Integer makeLiteralInteger(INode node, org.meta_environment.rascal.ast.IntegerLiteral integerLiteral) { 
return new org.meta_environment.rascal.ast.Literal.Integer(node, integerLiteral); 
}
public org.meta_environment.rascal.ast.Literal.Boolean makeLiteralBoolean(INode node, org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral) { 
return new org.meta_environment.rascal.ast.Literal.Boolean(node, booleanLiteral); 
}
public org.meta_environment.rascal.ast.Literal.Ambiguity makeLiteralAmbiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Literal> alternatives) { 
return new org.meta_environment.rascal.ast.Literal.Ambiguity(node, alternatives); 
}
public org.meta_environment.rascal.ast.Literal.RegExp makeLiteralRegExp(INode node, org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral) { 
return new org.meta_environment.rascal.ast.Literal.RegExp(node, regExpLiteral); 
}
}