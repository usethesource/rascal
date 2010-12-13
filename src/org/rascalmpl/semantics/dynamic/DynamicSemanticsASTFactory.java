package org.rascalmpl.semantics.dynamic;

import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.ASTFactory;

public class DynamicSemanticsASTFactory extends ASTFactory {

	public org.rascalmpl.ast.LocationLiteral.Default makeLocationLiteralDefault(INode node, org.rascalmpl.ast.ProtocolPart protocolPart, org.rascalmpl.ast.PathPart pathPart) {
		return new LocationLiteral.Default(node, protocolPart, pathPart);
	}

	public org.rascalmpl.ast.Tag.Default makeTagDefault(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.TagString contents) {
		return new Tag.Default(node, name, contents);
	}

	public org.rascalmpl.ast.Tag.Expression makeTagExpression(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Expression expression) {
		return new Tag.Expression(node, name, expression);
	}

	public org.rascalmpl.ast.Tag.Empty makeTagEmpty(INode node, org.rascalmpl.ast.Name name) {
		return new Tag.Empty(node, name);
	}

	public org.rascalmpl.ast.UserType.Parametric makeUserTypeParametric(INode node, org.rascalmpl.ast.QualifiedName name, java.util.List<org.rascalmpl.ast.Type> parameters) {
		return new UserType.Parametric(node, name, parameters);
	}

	public org.rascalmpl.ast.UserType.Name makeUserTypeName(INode node, org.rascalmpl.ast.QualifiedName name) {
		return new UserType.Name(node, name);
	}

	public org.rascalmpl.ast.Range.FromTo makeRangeFromTo(INode node, org.rascalmpl.ast.Char start, org.rascalmpl.ast.Char end) {
		return new Range.FromTo(node, start, end);
	}

	public org.rascalmpl.ast.Range.Character makeRangeCharacter(INode node, org.rascalmpl.ast.Char character) {
		return new Range.Character(node, character);
	}

	public org.rascalmpl.ast.StructuredType.Default makeStructuredTypeDefault(INode node, org.rascalmpl.ast.BasicType basicType, java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
		return new StructuredType.Default(node, basicType, arguments);
	}

	public org.rascalmpl.ast.ProdModifier.Associativity makeProdModifierAssociativity(INode node, org.rascalmpl.ast.Assoc associativity) {
		return new ProdModifier.Associativity(node, associativity);
	}

	public org.rascalmpl.ast.ProdModifier.Tag makeProdModifierTag(INode node, org.rascalmpl.ast.Tag tag) {
		return new ProdModifier.Tag(node, tag);
	}

	public org.rascalmpl.ast.ProdModifier.Bracket makeProdModifierBracket(INode node) {
		return new ProdModifier.Bracket(node);
	}

	public org.rascalmpl.ast.ProdModifier.Lexical makeProdModifierLexical(INode node) {
		return new ProdModifier.Lexical(node);
	}

	public org.rascalmpl.ast.DataTypeSelector.Selector makeDataTypeSelectorSelector(INode node, org.rascalmpl.ast.QualifiedName sort, org.rascalmpl.ast.Name production) {
		return new DataTypeSelector.Selector(node, sort, production);
	}

	public org.rascalmpl.ast.FunctionBody.Default makeFunctionBodyDefault(INode node, java.util.List<org.rascalmpl.ast.Statement> statements) {
		return new FunctionBody.Default(node, statements);
	}

	public org.rascalmpl.ast.Formals.Default makeFormalsDefault(INode node, java.util.List<org.rascalmpl.ast.Formal> formals) {
		return new Formals.Default(node, formals);
	}

	public org.rascalmpl.ast.Renaming.Default makeRenamingDefault(INode node, org.rascalmpl.ast.Name from, org.rascalmpl.ast.Name to) {
		return new Renaming.Default(node, from, to);
	}

	public org.rascalmpl.ast.StringLiteral.NonInterpolated makeStringLiteralNonInterpolated(INode node, org.rascalmpl.ast.StringConstant constant) {
		return new StringLiteral.NonInterpolated(node, constant);
	}

	public org.rascalmpl.ast.StringLiteral.Template makeStringLiteralTemplate(INode node, org.rascalmpl.ast.PreStringChars pre, org.rascalmpl.ast.StringTemplate template,
			org.rascalmpl.ast.StringTail tail) {
		return new StringLiteral.Template(node, pre, template, tail);
	}

	public org.rascalmpl.ast.StringLiteral.Interpolated makeStringLiteralInterpolated(INode node, org.rascalmpl.ast.PreStringChars pre, org.rascalmpl.ast.Expression expression,
			org.rascalmpl.ast.StringTail tail) {
		return new StringLiteral.Interpolated(node, pre, expression, tail);
	}

	public org.rascalmpl.ast.Body.Toplevels makeBodyToplevels(INode node, java.util.List<org.rascalmpl.ast.Toplevel> toplevels) {
		return new Body.Toplevels(node, toplevels);
	}

	public org.rascalmpl.ast.Variant.NAryConstructor makeVariantNAryConstructor(INode node, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
		return new Variant.NAryConstructor(node, name, arguments);
	}

	public org.rascalmpl.ast.StringTail.MidTemplate makeStringTailMidTemplate(INode node, org.rascalmpl.ast.MidStringChars mid, org.rascalmpl.ast.StringTemplate template,
			org.rascalmpl.ast.StringTail tail) {
		return new StringTail.MidTemplate(node, mid, template, tail);
	}

	public org.rascalmpl.ast.StringTail.MidInterpolated makeStringTailMidInterpolated(INode node, org.rascalmpl.ast.MidStringChars mid, org.rascalmpl.ast.Expression expression,
			org.rascalmpl.ast.StringTail tail) {
		return new StringTail.MidInterpolated(node, mid, expression, tail);
	}

	public org.rascalmpl.ast.StringTail.Post makeStringTailPost(INode node, org.rascalmpl.ast.PostStringChars post) {
		return new StringTail.Post(node, post);
	}

	public org.rascalmpl.ast.Test.Unlabeled makeTestUnlabeled(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Expression expression) {
		return new Test.Unlabeled(node, tags, expression);
	}

	public org.rascalmpl.ast.Test.Labeled makeTestLabeled(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.StringLiteral labeled) {
		return new Test.Labeled(node, tags, expression, labeled);
	}

	public org.rascalmpl.ast.ProtocolPart.NonInterpolated makeProtocolPartNonInterpolated(INode node, org.rascalmpl.ast.ProtocolChars protocolChars) {
		return new ProtocolPart.NonInterpolated(node, protocolChars);
	}

	public org.rascalmpl.ast.ProtocolPart.Interpolated makeProtocolPartInterpolated(INode node, org.rascalmpl.ast.PreProtocolChars pre, org.rascalmpl.ast.Expression expression,
			org.rascalmpl.ast.ProtocolTail tail) {
		return new ProtocolPart.Interpolated(node, pre, expression, tail);
	}

	public org.rascalmpl.ast.FunctionModifiers.List makeFunctionModifiersList(INode node, java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers) {
		return new FunctionModifiers.List(node, modifiers);
	}

	public org.rascalmpl.ast.Command.Shell makeCommandShell(INode node, org.rascalmpl.ast.ShellCommand command) {
		return new Command.Shell(node, command);
	}

	public org.rascalmpl.ast.Command.Import makeCommandImport(INode node, org.rascalmpl.ast.Import imported) {
		return new Command.Import(node, imported);
	}

	public org.rascalmpl.ast.Command.Expression makeCommandExpression(INode node, org.rascalmpl.ast.Expression expression) {
		return new Command.Expression(node, expression);
	}

	public org.rascalmpl.ast.Command.Statement makeCommandStatement(INode node, org.rascalmpl.ast.Statement statement) {
		return new Command.Statement(node, statement);
	}

	public org.rascalmpl.ast.Command.Declaration makeCommandDeclaration(INode node, org.rascalmpl.ast.Declaration declaration) {
		return new Command.Declaration(node, declaration);
	}

	public org.rascalmpl.ast.Toplevel.GivenVisibility makeToplevelGivenVisibility(INode node, org.rascalmpl.ast.Declaration declaration) {
		return new Toplevel.GivenVisibility(node, declaration);
	}

	public org.rascalmpl.ast.Type.Basic makeTypeBasic(INode node, org.rascalmpl.ast.BasicType basic) {
		return new Type.Basic(node, basic);
	}

	public org.rascalmpl.ast.Type.Function makeTypeFunction(INode node, org.rascalmpl.ast.FunctionType function) {
		return new Type.Function(node, function);
	}

	public org.rascalmpl.ast.Type.Structured makeTypeStructured(INode node, org.rascalmpl.ast.StructuredType structured) {
		return new Type.Structured(node, structured);
	}

	public org.rascalmpl.ast.Type.Bracket makeTypeBracket(INode node, org.rascalmpl.ast.Type type) {
		return new Type.Bracket(node, type);
	}

	public org.rascalmpl.ast.Type.Variable makeTypeVariable(INode node, org.rascalmpl.ast.TypeVar typeVar) {
		return new Type.Variable(node, typeVar);
	}

	public org.rascalmpl.ast.Type.Selector makeTypeSelector(INode node, org.rascalmpl.ast.DataTypeSelector selector) {
		return new Type.Selector(node, selector);
	}

	public org.rascalmpl.ast.Type.Symbol makeTypeSymbol(INode node, org.rascalmpl.ast.Sym symbol) {
		return new Type.Symbol(node, symbol);
	}

	public org.rascalmpl.ast.Type.User makeTypeUser(INode node, org.rascalmpl.ast.UserType user) {
		return new Type.User(node, user);
	}

	public org.rascalmpl.ast.Label.Empty makeLabelEmpty(INode node) {
		return new Label.Empty(node);
	}

	public org.rascalmpl.ast.Label.Default makeLabelDefault(INode node, org.rascalmpl.ast.Name name) {
		return new Label.Default(node, name);
	}

	public org.rascalmpl.ast.ModuleParameters.Default makeModuleParametersDefault(INode node, java.util.List<org.rascalmpl.ast.TypeVar> parameters) {
		return new ModuleParameters.Default(node, parameters);
	}

	public org.rascalmpl.ast.Expression.Anti makeExpressionAnti(INode node, org.rascalmpl.ast.Expression pattern) {
		return new Expression.Anti(node, pattern);
	}

	public org.rascalmpl.ast.Expression.Product makeExpressionProduct(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Product(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Division makeExpressionDivision(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Division(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Equivalence makeExpressionEquivalence(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Equivalence(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Any makeExpressionAny(INode node, java.util.List<org.rascalmpl.ast.Expression> generators) {
		return new Expression.Any(node, generators);
	}

	public org.rascalmpl.ast.Expression.NonEquals makeExpressionNonEquals(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.NonEquals(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Match makeExpressionMatch(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Expression expression) {
		return new Expression.Match(node, pattern, expression);
	}

	public org.rascalmpl.ast.Expression.StepRange makeExpressionStepRange(INode node, org.rascalmpl.ast.Expression first, org.rascalmpl.ast.Expression second, org.rascalmpl.ast.Expression last) {
		return new Expression.StepRange(node, first, second, last);
	}

	public org.rascalmpl.ast.Expression.Composition makeExpressionComposition(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Composition(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Enumerator makeExpressionEnumerator(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Expression expression) {
		return new Expression.Enumerator(node, pattern, expression);
	}

	public org.rascalmpl.ast.Expression.Join makeExpressionJoin(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Join(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.NoMatch makeExpressionNoMatch(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Expression expression) {
		return new Expression.NoMatch(node, pattern, expression);
	}

	public org.rascalmpl.ast.Expression.LessThanOrEq makeExpressionLessThanOrEq(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.LessThanOrEq(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.TypedVariable makeExpressionTypedVariable(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.Name name) {
		return new Expression.TypedVariable(node, type, name);
	}

	public org.rascalmpl.ast.Expression.IfDefinedOtherwise makeExpressionIfDefinedOtherwise(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.IfDefinedOtherwise(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.VoidClosure makeExpressionVoidClosure(INode node, org.rascalmpl.ast.Parameters parameters, java.util.List<org.rascalmpl.ast.Statement> statements) {
		return new Expression.VoidClosure(node, parameters, statements);
	}

	public org.rascalmpl.ast.Expression.Comprehension makeExpressionComprehension(INode node, org.rascalmpl.ast.Comprehension comprehension) {
		return new Expression.Comprehension(node, comprehension);
	}

	public org.rascalmpl.ast.Expression.In makeExpressionIn(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.In(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Or makeExpressionOr(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Or(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Set makeExpressionSet(INode node, java.util.List<org.rascalmpl.ast.Expression> elements) {
		return new Expression.Set(node, elements);
	}

	public org.rascalmpl.ast.Expression.FieldAccess makeExpressionFieldAccess(INode node, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.Name field) {
		return new Expression.FieldAccess(node, expression, field);
	}

	public org.rascalmpl.ast.Expression.All makeExpressionAll(INode node, java.util.List<org.rascalmpl.ast.Expression> generators) {
		return new Expression.All(node, generators);
	}

	public org.rascalmpl.ast.Expression.FieldProject makeExpressionFieldProject(INode node, org.rascalmpl.ast.Expression expression, java.util.List<org.rascalmpl.ast.Field> fields) {
		return new Expression.FieldProject(node, expression, fields);
	}

	public org.rascalmpl.ast.Expression.Equals makeExpressionEquals(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Equals(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Addition makeExpressionAddition(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Addition(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Implication makeExpressionImplication(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Implication(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.ReifiedType makeExpressionReifiedType(INode node, org.rascalmpl.ast.BasicType basicType, java.util.List<org.rascalmpl.ast.Expression> arguments) {
		return new Expression.ReifiedType(node, basicType, arguments);
	}

	public org.rascalmpl.ast.Expression.Bracket makeExpressionBracket(INode node, org.rascalmpl.ast.Expression expression) {
		return new Expression.Bracket(node, expression);
	}

	public org.rascalmpl.ast.Expression.GreaterThan makeExpressionGreaterThan(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.GreaterThan(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Subscript makeExpressionSubscript(INode node, org.rascalmpl.ast.Expression expression, java.util.List<org.rascalmpl.ast.Expression> subscripts) {
		return new Expression.Subscript(node, expression, subscripts);
	}

	public org.rascalmpl.ast.Expression.IfThenElse makeExpressionIfThenElse(INode node, org.rascalmpl.ast.Expression condition, org.rascalmpl.ast.Expression thenExp,
			org.rascalmpl.ast.Expression elseExp) {
		return new Expression.IfThenElse(node, condition, thenExp, elseExp);
	}

	public org.rascalmpl.ast.Expression.Modulo makeExpressionModulo(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Modulo(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.TransitiveClosure makeExpressionTransitiveClosure(INode node, org.rascalmpl.ast.Expression argument) {
		return new Expression.TransitiveClosure(node, argument);
	}

	public org.rascalmpl.ast.Expression.Subtraction makeExpressionSubtraction(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Subtraction(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.ReifyType makeExpressionReifyType(INode node, org.rascalmpl.ast.Type type) {
		return new Expression.ReifyType(node, type);
	}

	public org.rascalmpl.ast.Expression.NonEmptyBlock makeExpressionNonEmptyBlock(INode node, java.util.List<org.rascalmpl.ast.Statement> statements) {
		return new Expression.NonEmptyBlock(node, statements);
	}

	public org.rascalmpl.ast.Expression.CallOrTree makeExpressionCallOrTree(INode node, org.rascalmpl.ast.Expression expression, java.util.List<org.rascalmpl.ast.Expression> arguments) {
		return new Expression.CallOrTree(node, expression, arguments);
	}

	public org.rascalmpl.ast.Expression.Descendant makeExpressionDescendant(INode node, org.rascalmpl.ast.Expression pattern) {
		return new Expression.Descendant(node, pattern);
	}

	public org.rascalmpl.ast.Expression.Range makeExpressionRange(INode node, org.rascalmpl.ast.Expression first, org.rascalmpl.ast.Expression last) {
		return new Expression.Range(node, first, last);
	}

	public org.rascalmpl.ast.Expression.GetAnnotation makeExpressionGetAnnotation(INode node, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.Name name) {
		return new Expression.GetAnnotation(node, expression, name);
	}

	public org.rascalmpl.ast.Expression.Guarded makeExpressionGuarded(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.Expression pattern) {
		return new Expression.Guarded(node, type, pattern);
	}

	public org.rascalmpl.ast.Expression.VariableBecomes makeExpressionVariableBecomes(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Expression pattern) {
		return new Expression.VariableBecomes(node, name, pattern);
	}

	public org.rascalmpl.ast.Expression.GreaterThanOrEq makeExpressionGreaterThanOrEq(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.GreaterThanOrEq(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Intersection makeExpressionIntersection(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.Intersection(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.Tuple makeExpressionTuple(INode node, java.util.List<org.rascalmpl.ast.Expression> elements) {
		return new Expression.Tuple(node, elements);
	}

	public org.rascalmpl.ast.Expression.FieldUpdate makeExpressionFieldUpdate(INode node, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.Name key, org.rascalmpl.ast.Expression replacement) {
		return new Expression.FieldUpdate(node, expression, key, replacement);
	}

	public org.rascalmpl.ast.Expression.MultiVariable makeExpressionMultiVariable(INode node, org.rascalmpl.ast.QualifiedName qualifiedName) {
		return new Expression.MultiVariable(node, qualifiedName);
	}

	public org.rascalmpl.ast.Expression.Negation makeExpressionNegation(INode node, org.rascalmpl.ast.Expression argument) {
		return new Expression.Negation(node, argument);
	}

	public org.rascalmpl.ast.Expression.Literal makeExpressionLiteral(INode node, org.rascalmpl.ast.Literal literal) {
		return new Expression.Literal(node, literal);
	}

	public org.rascalmpl.ast.Expression.IsDefined makeExpressionIsDefined(INode node, org.rascalmpl.ast.Expression argument) {
		return new Expression.IsDefined(node, argument);
	}

	public org.rascalmpl.ast.Expression.Closure makeExpressionClosure(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.Parameters parameters,
			java.util.List<org.rascalmpl.ast.Statement> statements) {
		return new Expression.Closure(node, type, parameters, statements);
	}

	public org.rascalmpl.ast.Expression.List makeExpressionList(INode node, java.util.List<org.rascalmpl.ast.Expression> elements) {
		return new Expression.List(node, elements);
	}

	public org.rascalmpl.ast.Expression.LessThan makeExpressionLessThan(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.LessThan(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.NotIn makeExpressionNotIn(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.NotIn(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.It makeExpressionIt(INode node) {
		return new Expression.It(node);
	}

	public org.rascalmpl.ast.Expression.And makeExpressionAnd(INode node, org.rascalmpl.ast.Expression lhs, org.rascalmpl.ast.Expression rhs) {
		return new Expression.And(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Expression.QualifiedName makeExpressionQualifiedName(INode node, org.rascalmpl.ast.QualifiedName qualifiedName) {
		return new Expression.QualifiedName(node, qualifiedName);
	}

	public org.rascalmpl.ast.Expression.Negative makeExpressionNegative(INode node, org.rascalmpl.ast.Expression argument) {
		return new Expression.Negative(node, argument);
	}

	public org.rascalmpl.ast.Expression.Reducer makeExpressionReducer(INode node, org.rascalmpl.ast.Expression init, org.rascalmpl.ast.Expression result,
			java.util.List<org.rascalmpl.ast.Expression> generators) {
		return new Expression.Reducer(node, init, result, generators);
	}

	public org.rascalmpl.ast.Expression.TransitiveReflexiveClosure makeExpressionTransitiveReflexiveClosure(INode node, org.rascalmpl.ast.Expression argument) {
		return new Expression.TransitiveReflexiveClosure(node, argument);
	}

	public org.rascalmpl.ast.Expression.Map makeExpressionMap(INode node, java.util.List<org.rascalmpl.ast.Mapping_Expression> mappings) {
		return new Expression.Map(node, mappings);
	}

	public org.rascalmpl.ast.Expression.Visit makeExpressionVisit(INode node, org.rascalmpl.ast.Label label, org.rascalmpl.ast.Visit visit) {
		return new Expression.Visit(node, label, visit);
	}

	public org.rascalmpl.ast.Expression.SetAnnotation makeExpressionSetAnnotation(INode node, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Expression value) {
		return new Expression.SetAnnotation(node, expression, name, value);
	}

	public org.rascalmpl.ast.Expression.TypedVariableBecomes makeExpressionTypedVariableBecomes(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.Name name,
			org.rascalmpl.ast.Expression pattern) {
		return new Expression.TypedVariableBecomes(node, type, name, pattern);
	}

	public org.rascalmpl.ast.Module.Default makeModuleDefault(INode node, org.rascalmpl.ast.Header header, org.rascalmpl.ast.Body body) {
		return new Module.Default(node, header, body);
	}

	public org.rascalmpl.ast.DateTimeLiteral.DateAndTimeLiteral makeDateTimeLiteralDateAndTimeLiteral(INode node, org.rascalmpl.ast.DateAndTime dateAndTime) {
		return new DateTimeLiteral.DateAndTimeLiteral(node, dateAndTime);
	}

	public org.rascalmpl.ast.DateTimeLiteral.TimeLiteral makeDateTimeLiteralTimeLiteral(INode node, org.rascalmpl.ast.JustTime time) {
		return new DateTimeLiteral.TimeLiteral(node, time);
	}

	public org.rascalmpl.ast.DateTimeLiteral.DateLiteral makeDateTimeLiteralDateLiteral(INode node, org.rascalmpl.ast.JustDate date) {
		return new DateTimeLiteral.DateLiteral(node, date);
	}

	public org.rascalmpl.ast.Declaration.Alias makeDeclarationAlias(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.UserType user,
			org.rascalmpl.ast.Type base) {
		return new Declaration.Alias(node, tags, visibility, user, base);
	}

	public org.rascalmpl.ast.Declaration.Data makeDeclarationData(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.UserType user,
			java.util.List<org.rascalmpl.ast.Variant> variants) {
		return new Declaration.Data(node, tags, visibility, user, variants);
	}

	public org.rascalmpl.ast.Declaration.Annotation makeDeclarationAnnotation(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Type annoType,
			org.rascalmpl.ast.Type onType, org.rascalmpl.ast.Name name) {
		return new Declaration.Annotation(node, tags, visibility, annoType, onType, name);
	}

	public org.rascalmpl.ast.Declaration.Function makeDeclarationFunction(INode node, org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
		return new Declaration.Function(node, functionDeclaration);
	}

	public org.rascalmpl.ast.Declaration.Rule makeDeclarationRule(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Name name, org.rascalmpl.ast.PatternWithAction patternAction) {
		return new Declaration.Rule(node, tags, name, patternAction);
	}

	public org.rascalmpl.ast.Declaration.DataAbstract makeDeclarationDataAbstract(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.UserType user) {
		return new Declaration.DataAbstract(node, tags, visibility, user);
	}

	public org.rascalmpl.ast.Declaration.Variable makeDeclarationVariable(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Type type,
			java.util.List<org.rascalmpl.ast.Variable> variables) {
		return new Declaration.Variable(node, tags, visibility, type, variables);
	}

	public org.rascalmpl.ast.Declaration.Test makeDeclarationTest(INode node, org.rascalmpl.ast.Test test) {
		return new Declaration.Test(node, test);
	}

	public org.rascalmpl.ast.Declaration.Tag makeDeclarationTag(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Kind kind,
			org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Type> types) {
		return new Declaration.Tag(node, tags, visibility, kind, name, types);
	}

	public org.rascalmpl.ast.Declaration.View makeDeclarationView(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility, org.rascalmpl.ast.Name view,
			org.rascalmpl.ast.Name superType, java.util.List<org.rascalmpl.ast.Alternative> alts) {
		return new Declaration.View(node, tags, visibility, view, superType, alts);
	}

	public org.rascalmpl.ast.LongLiteral.OctalLongLiteral makeLongLiteralOctalLongLiteral(INode node, org.rascalmpl.ast.OctalLongLiteral octalLong) {
		return new LongLiteral.OctalLongLiteral(node, octalLong);
	}

	public org.rascalmpl.ast.LongLiteral.DecimalLongLiteral makeLongLiteralDecimalLongLiteral(INode node, org.rascalmpl.ast.DecimalLongLiteral decimalLong) {
		return new LongLiteral.DecimalLongLiteral(node, decimalLong);
	}

	public org.rascalmpl.ast.LongLiteral.HexLongLiteral makeLongLiteralHexLongLiteral(INode node, org.rascalmpl.ast.HexLongLiteral hexLong) {
		return new LongLiteral.HexLongLiteral(node, hexLong);
	}

	public org.rascalmpl.ast.Sym.StartOfLine makeSymStartOfLine(INode node) {
		return new Sym.StartOfLine(node);
	}

	public org.rascalmpl.ast.Sym.Nonterminal makeSymNonterminal(INode node, org.rascalmpl.ast.Nonterminal nonterminal) {
		return new Sym.Nonterminal(node, nonterminal);
	}

	public org.rascalmpl.ast.Sym.Optional makeSymOptional(INode node, org.rascalmpl.ast.Sym symbol) {
		return new Sym.Optional(node, symbol);
	}

	public org.rascalmpl.ast.Sym.Parameter makeSymParameter(INode node, org.rascalmpl.ast.Nonterminal nonterminal) {
		return new Sym.Parameter(node, nonterminal);
	}

	public org.rascalmpl.ast.Sym.CaseInsensitiveLiteral makeSymCaseInsensitiveLiteral(INode node, org.rascalmpl.ast.CaseInsensitiveStringConstant cistring) {
		return new Sym.CaseInsensitiveLiteral(node, cistring);
	}

	public org.rascalmpl.ast.Sym.CharacterClass makeSymCharacterClass(INode node, org.rascalmpl.ast.Class charClass) {
		return new Sym.CharacterClass(node, charClass);
	}

	public org.rascalmpl.ast.Sym.Labeled makeSymLabeled(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.NonterminalLabel label) {
		return new Sym.Labeled(node, symbol, label);
	}

	public org.rascalmpl.ast.Sym.IterStar makeSymIterStar(INode node, org.rascalmpl.ast.Sym symbol) {
		return new Sym.IterStar(node, symbol);
	}

	public org.rascalmpl.ast.Sym.Parametrized makeSymParametrized(INode node, org.rascalmpl.ast.ParameterizedNonterminal pnonterminal, java.util.List<org.rascalmpl.ast.Sym> parameters) {
		return new Sym.Parametrized(node, pnonterminal, parameters);
	}

	public org.rascalmpl.ast.Sym.Iter makeSymIter(INode node, org.rascalmpl.ast.Sym symbol) {
		return new Sym.Iter(node, symbol);
	}

	public org.rascalmpl.ast.Sym.EndOfLine makeSymEndOfLine(INode node) {
		return new Sym.EndOfLine(node);
	}

	public org.rascalmpl.ast.Sym.IterStarSep makeSymIterStarSep(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.StringConstant sep) {
		return new Sym.IterStarSep(node, symbol, sep);
	}

	public org.rascalmpl.ast.Sym.Literal makeSymLiteral(INode node, org.rascalmpl.ast.StringConstant string) {
		return new Sym.Literal(node, string);
	}

	public org.rascalmpl.ast.Sym.Column makeSymColumn(INode node, org.rascalmpl.ast.IntegerLiteral column) {
		return new Sym.Column(node, column);
	}

	public org.rascalmpl.ast.Sym.IterSep makeSymIterSep(INode node, org.rascalmpl.ast.Sym symbol, org.rascalmpl.ast.StringConstant sep) {
		return new Sym.IterSep(node, symbol, sep);
	}

	public org.rascalmpl.ast.SyntaxDefinition.Language makeSyntaxDefinitionLanguage(INode node, org.rascalmpl.ast.Start start, org.rascalmpl.ast.Sym defined, org.rascalmpl.ast.Prod production) {
		return new SyntaxDefinition.Language(node, start, defined, production);
	}

	public org.rascalmpl.ast.SyntaxDefinition.Layout makeSyntaxDefinitionLayout(INode node, org.rascalmpl.ast.Sym defined, org.rascalmpl.ast.Prod production) {
		return new SyntaxDefinition.Layout(node, defined, production);
	}

	public org.rascalmpl.ast.Assoc.Right makeAssocRight(INode node) {
		return new Assoc.Right(node);
	}

	public org.rascalmpl.ast.Assoc.NonAssociative makeAssocNonAssociative(INode node) {
		return new Assoc.NonAssociative(node);
	}

	public org.rascalmpl.ast.Assoc.Left makeAssocLeft(INode node) {
		return new Assoc.Left(node);
	}

	public org.rascalmpl.ast.Assoc.Associative makeAssocAssociative(INode node) {
		return new Assoc.Associative(node);
	}

	public org.rascalmpl.ast.Renamings.Default makeRenamingsDefault(INode node, java.util.List<org.rascalmpl.ast.Renaming> renamings) {
		return new Renamings.Default(node, renamings);
	}

	public org.rascalmpl.ast.Formal.TypeName makeFormalTypeName(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.Name name) {
		return new Formal.TypeName(node, type, name);
	}

	public org.rascalmpl.ast.Class.Union makeClassUnion(INode node, org.rascalmpl.ast.Class lhs, org.rascalmpl.ast.Class rhs) {
		return new Class.Union(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Class.Difference makeClassDifference(INode node, org.rascalmpl.ast.Class lhs, org.rascalmpl.ast.Class rhs) {
		return new Class.Difference(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Class.SimpleCharclass makeClassSimpleCharclass(INode node, java.util.List<org.rascalmpl.ast.Range> ranges) {
		return new Class.SimpleCharclass(node, ranges);
	}

	public org.rascalmpl.ast.Class.Intersection makeClassIntersection(INode node, org.rascalmpl.ast.Class lhs, org.rascalmpl.ast.Class rhs) {
		return new Class.Intersection(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Class.Complement makeClassComplement(INode node, org.rascalmpl.ast.Class charClass) {
		return new Class.Complement(node, charClass);
	}

	public org.rascalmpl.ast.Class.Bracket makeClassBracket(INode node, org.rascalmpl.ast.Class charclass) {
		return new Class.Bracket(node, charclass);
	}

	public org.rascalmpl.ast.Bound.Empty makeBoundEmpty(INode node) {
		return new Bound.Empty(node);
	}

	public org.rascalmpl.ast.Bound.Default makeBoundDefault(INode node, org.rascalmpl.ast.Expression expression) {
		return new Bound.Default(node, expression);
	}

	public org.rascalmpl.ast.PreModule.Default makePreModuleDefault(INode node, org.rascalmpl.ast.Header header) {
		return new PreModule.Default(node, header);
	}

	public org.rascalmpl.ast.NoElseMayFollow.Default makeNoElseMayFollowDefault(INode node) {
		return new NoElseMayFollow.Default(node);
	}

	public org.rascalmpl.ast.Comprehension.Set makeComprehensionSet(INode node, java.util.List<org.rascalmpl.ast.Expression> results, java.util.List<org.rascalmpl.ast.Expression> generators) {
		return new Comprehension.Set(node, results, generators);
	}

	public org.rascalmpl.ast.Comprehension.Map makeComprehensionMap(INode node, org.rascalmpl.ast.Expression from, org.rascalmpl.ast.Expression to,
			java.util.List<org.rascalmpl.ast.Expression> generators) {
		return new Comprehension.Map(node, from, to, generators);
	}

	public org.rascalmpl.ast.Comprehension.List makeComprehensionList(INode node, java.util.List<org.rascalmpl.ast.Expression> results, java.util.List<org.rascalmpl.ast.Expression> generators) {
		return new Comprehension.List(node, results, generators);
	}

	public org.rascalmpl.ast.PathTail.Mid makePathTailMid(INode node, org.rascalmpl.ast.MidPathChars mid, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.PathTail tail) {
		return new PathTail.Mid(node, mid, expression, tail);
	}

	public org.rascalmpl.ast.PathTail.Post makePathTailPost(INode node, org.rascalmpl.ast.PostPathChars post) {
		return new PathTail.Post(node, post);
	}

	public org.rascalmpl.ast.Literal.Location makeLiteralLocation(INode node, org.rascalmpl.ast.LocationLiteral locationLiteral) {
		return new Literal.Location(node, locationLiteral);
	}

	public org.rascalmpl.ast.Literal.String makeLiteralString(INode node, org.rascalmpl.ast.StringLiteral stringLiteral) {
		return new Literal.String(node, stringLiteral);
	}

	public org.rascalmpl.ast.Literal.RegExp makeLiteralRegExp(INode node, org.rascalmpl.ast.RegExpLiteral regExpLiteral) {
		return new Literal.RegExp(node, regExpLiteral);
	}

	public org.rascalmpl.ast.Literal.Real makeLiteralReal(INode node, org.rascalmpl.ast.RealLiteral realLiteral) {
		return new Literal.Real(node, realLiteral);
	}

	public org.rascalmpl.ast.Literal.Boolean makeLiteralBoolean(INode node, org.rascalmpl.ast.BooleanLiteral booleanLiteral) {
		return new Literal.Boolean(node, booleanLiteral);
	}

	public org.rascalmpl.ast.Literal.DateTime makeLiteralDateTime(INode node, org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral) {
		return new Literal.DateTime(node, dateTimeLiteral);
	}

	public org.rascalmpl.ast.Literal.Integer makeLiteralInteger(INode node, org.rascalmpl.ast.IntegerLiteral integerLiteral) {
		return new Literal.Integer(node, integerLiteral);
	}

	public org.rascalmpl.ast.Strategy.Outermost makeStrategyOutermost(INode node) {
		return new Strategy.Outermost(node);
	}

	public org.rascalmpl.ast.Strategy.TopDownBreak makeStrategyTopDownBreak(INode node) {
		return new Strategy.TopDownBreak(node);
	}

	public org.rascalmpl.ast.Strategy.Innermost makeStrategyInnermost(INode node) {
		return new Strategy.Innermost(node);
	}

	public org.rascalmpl.ast.Strategy.BottomUpBreak makeStrategyBottomUpBreak(INode node) {
		return new Strategy.BottomUpBreak(node);
	}

	public org.rascalmpl.ast.Strategy.BottomUp makeStrategyBottomUp(INode node) {
		return new Strategy.BottomUp(node);
	}

	public org.rascalmpl.ast.Strategy.TopDown makeStrategyTopDown(INode node) {
		return new Strategy.TopDown(node);
	}

	public org.rascalmpl.ast.DataTarget.Empty makeDataTargetEmpty(INode node) {
		return new DataTarget.Empty(node);
	}

	public org.rascalmpl.ast.DataTarget.Labeled makeDataTargetLabeled(INode node, org.rascalmpl.ast.Name label) {
		return new DataTarget.Labeled(node, label);
	}

	public org.rascalmpl.ast.Case.PatternWithAction makeCasePatternWithAction(INode node, org.rascalmpl.ast.PatternWithAction patternWithAction) {
		return new Case.PatternWithAction(node, patternWithAction);
	}

	public org.rascalmpl.ast.Case.Default makeCaseDefault(INode node, org.rascalmpl.ast.Statement statement) {
		return new Case.Default(node, statement);
	}

	public org.rascalmpl.ast.Catch.Default makeCatchDefault(INode node, org.rascalmpl.ast.Statement body) {
		return new Catch.Default(node, body);
	}

	public org.rascalmpl.ast.Catch.Binding makeCatchBinding(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Statement body) {
		return new Catch.Binding(node, pattern, body);
	}

	public org.rascalmpl.ast.Field.Name makeFieldName(INode node, org.rascalmpl.ast.Name fieldName) {
		return new Field.Name(node, fieldName);
	}

	public org.rascalmpl.ast.Field.Index makeFieldIndex(INode node, org.rascalmpl.ast.IntegerLiteral fieldIndex) {
		return new Field.Index(node, fieldIndex);
	}

	public org.rascalmpl.ast.TypeVar.Free makeTypeVarFree(INode node, org.rascalmpl.ast.Name name) {
		return new TypeVar.Free(node, name);
	}

	public org.rascalmpl.ast.TypeVar.Bounded makeTypeVarBounded(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Type bound) {
		return new TypeVar.Bounded(node, name, bound);
	}

	public org.rascalmpl.ast.Alternative.NamedType makeAlternativeNamedType(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Type type) {
		return new Alternative.NamedType(node, name, type);
	}

	public org.rascalmpl.ast.ModuleActuals.Default makeModuleActualsDefault(INode node, java.util.List<org.rascalmpl.ast.Type> types) {
		return new ModuleActuals.Default(node, types);
	}

	public org.rascalmpl.ast.Target.Empty makeTargetEmpty(INode node) {
		return new Target.Empty(node);
	}

	public org.rascalmpl.ast.Target.Labeled makeTargetLabeled(INode node, org.rascalmpl.ast.Name name) {
		return new Target.Labeled(node, name);
	}

	public org.rascalmpl.ast.StringTemplate.IfThen makeStringTemplateIfThen(INode node, java.util.List<org.rascalmpl.ast.Expression> conditions, java.util.List<org.rascalmpl.ast.Statement> preStats,
			org.rascalmpl.ast.StringMiddle body, java.util.List<org.rascalmpl.ast.Statement> postStats) {
		return new StringTemplate.IfThen(node, conditions, preStats, body, postStats);
	}

	public org.rascalmpl.ast.StringTemplate.IfThenElse makeStringTemplateIfThenElse(INode node, java.util.List<org.rascalmpl.ast.Expression> conditions,
			java.util.List<org.rascalmpl.ast.Statement> preStatsThen, org.rascalmpl.ast.StringMiddle thenString, java.util.List<org.rascalmpl.ast.Statement> postStatsThen,
			java.util.List<org.rascalmpl.ast.Statement> preStatsElse, org.rascalmpl.ast.StringMiddle elseString, java.util.List<org.rascalmpl.ast.Statement> postStatsElse) {
		return new StringTemplate.IfThenElse(node, conditions, preStatsThen, thenString, postStatsThen, preStatsElse, elseString, postStatsElse);
	}

	public org.rascalmpl.ast.StringTemplate.While makeStringTemplateWhile(INode node, org.rascalmpl.ast.Expression condition, java.util.List<org.rascalmpl.ast.Statement> preStats,
			org.rascalmpl.ast.StringMiddle body, java.util.List<org.rascalmpl.ast.Statement> postStats) {
		return new StringTemplate.While(node, condition, preStats, body, postStats);
	}

	public org.rascalmpl.ast.StringTemplate.DoWhile makeStringTemplateDoWhile(INode node, java.util.List<org.rascalmpl.ast.Statement> preStats, org.rascalmpl.ast.StringMiddle body,
			java.util.List<org.rascalmpl.ast.Statement> postStats, org.rascalmpl.ast.Expression condition) {
		return new StringTemplate.DoWhile(node, preStats, body, postStats, condition);
	}

	public org.rascalmpl.ast.StringTemplate.For makeStringTemplateFor(INode node, java.util.List<org.rascalmpl.ast.Expression> generators, java.util.List<org.rascalmpl.ast.Statement> preStats,
			org.rascalmpl.ast.StringMiddle body, java.util.List<org.rascalmpl.ast.Statement> postStats) {
		return new StringTemplate.For(node, generators, preStats, body, postStats);
	}

	public org.rascalmpl.ast.Visit.DefaultStrategy makeVisitDefaultStrategy(INode node, org.rascalmpl.ast.Expression subject, java.util.List<org.rascalmpl.ast.Case> cases) {
		return new Visit.DefaultStrategy(node, subject, cases);
	}

	public org.rascalmpl.ast.Visit.GivenStrategy makeVisitGivenStrategy(INode node, org.rascalmpl.ast.Strategy strategy, org.rascalmpl.ast.Expression subject,
			java.util.List<org.rascalmpl.ast.Case> cases) {
		return new Visit.GivenStrategy(node, strategy, subject, cases);
	}

	public org.rascalmpl.ast.Visibility.Public makeVisibilityPublic(INode node) {
		return new Visibility.Public(node);
	}

	public org.rascalmpl.ast.Visibility.Default makeVisibilityDefault(INode node) {
		return new Visibility.Default(node);
	}

	public org.rascalmpl.ast.Visibility.Private makeVisibilityPrivate(INode node) {
		return new Visibility.Private(node);
	}

	public org.rascalmpl.ast.Tags.Default makeTagsDefault(INode node, java.util.List<org.rascalmpl.ast.Tag> tags) {
		return new Tags.Default(node, tags);
	}

	public org.rascalmpl.ast.Kind.Module makeKindModule(INode node) {
		return new Kind.Module(node);
	}

	public org.rascalmpl.ast.Kind.Rule makeKindRule(INode node) {
		return new Kind.Rule(node);
	}

	public org.rascalmpl.ast.Kind.Variable makeKindVariable(INode node) {
		return new Kind.Variable(node);
	}

	public org.rascalmpl.ast.Kind.Anno makeKindAnno(INode node) {
		return new Kind.Anno(node);
	}

	public org.rascalmpl.ast.Kind.Function makeKindFunction(INode node) {
		return new Kind.Function(node);
	}

	public org.rascalmpl.ast.Kind.Data makeKindData(INode node) {
		return new Kind.Data(node);
	}

	public org.rascalmpl.ast.Kind.Tag makeKindTag(INode node) {
		return new Kind.Tag(node);
	}

	public org.rascalmpl.ast.Kind.View makeKindView(INode node) {
		return new Kind.View(node);
	}

	public org.rascalmpl.ast.Kind.Alias makeKindAlias(INode node) {
		return new Kind.Alias(node);
	}

	public org.rascalmpl.ast.Kind.All makeKindAll(INode node) {
		return new Kind.All(node);
	}

	public org.rascalmpl.ast.FunctionType.TypeArguments makeFunctionTypeTypeArguments(INode node, org.rascalmpl.ast.Type type, java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
		return new FunctionType.TypeArguments(node, type, arguments);
	}

	public org.rascalmpl.ast.Prod.All makeProdAll(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		return new Prod.All(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Prod.Follow makeProdFollow(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		return new Prod.Follow(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Prod.First makeProdFirst(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		return new Prod.First(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Prod.Unlabeled makeProdUnlabeled(INode node, java.util.List<org.rascalmpl.ast.ProdModifier> modifiers, java.util.List<org.rascalmpl.ast.Sym> args) {
		return new Prod.Unlabeled(node, modifiers, args);
	}

	public org.rascalmpl.ast.Prod.Labeled makeProdLabeled(INode node, java.util.List<org.rascalmpl.ast.ProdModifier> modifiers, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Sym> args) {
		return new Prod.Labeled(node, modifiers, name, args);
	}

	public org.rascalmpl.ast.Prod.Action makeProdAction(INode node, org.rascalmpl.ast.Prod prod, org.rascalmpl.ast.LanguageAction action) {
		return new Prod.Action(node, prod, action);
	}

	public org.rascalmpl.ast.Prod.Reference makeProdReference(INode node, org.rascalmpl.ast.Name referenced) {
		return new Prod.Reference(node, referenced);
	}

	public org.rascalmpl.ast.Prod.Reject makeProdReject(INode node, org.rascalmpl.ast.Prod lhs, org.rascalmpl.ast.Prod rhs) {
		return new Prod.Reject(node, lhs, rhs);
	}

	public org.rascalmpl.ast.Prod.Others makeProdOthers(INode node) {
		return new Prod.Others(node);
	}

	public org.rascalmpl.ast.Prod.AssociativityGroup makeProdAssociativityGroup(INode node, org.rascalmpl.ast.Assoc associativity, org.rascalmpl.ast.Prod group) {
		return new Prod.AssociativityGroup(node, associativity, group);
	}

	public org.rascalmpl.ast.LocalVariableDeclaration.Default makeLocalVariableDeclarationDefault(INode node, org.rascalmpl.ast.Declarator declarator) {
		return new LocalVariableDeclaration.Default(node, declarator);
	}

	public org.rascalmpl.ast.LocalVariableDeclaration.Dynamic makeLocalVariableDeclarationDynamic(INode node, org.rascalmpl.ast.Declarator declarator) {
		return new LocalVariableDeclaration.Dynamic(node, declarator);
	}

	public org.rascalmpl.ast.BasicType.Map makeBasicTypeMap(INode node) {
		return new BasicType.Map(node);
	}

	public org.rascalmpl.ast.BasicType.Relation makeBasicTypeRelation(INode node) {
		return new BasicType.Relation(node);
	}

	public org.rascalmpl.ast.BasicType.Real makeBasicTypeReal(INode node) {
		return new BasicType.Real(node);
	}

	public org.rascalmpl.ast.BasicType.List makeBasicTypeList(INode node) {
		return new BasicType.List(node);
	}

	public org.rascalmpl.ast.BasicType.Lex makeBasicTypeLex(INode node) {
		return new BasicType.Lex(node);
	}

	public org.rascalmpl.ast.BasicType.ReifiedAdt makeBasicTypeReifiedAdt(INode node) {
		return new BasicType.ReifiedAdt(node);
	}

	public org.rascalmpl.ast.BasicType.ReifiedReifiedType makeBasicTypeReifiedReifiedType(INode node) {
		return new BasicType.ReifiedReifiedType(node);
	}

	public org.rascalmpl.ast.BasicType.DateTime makeBasicTypeDateTime(INode node) {
		return new BasicType.DateTime(node);
	}

	public org.rascalmpl.ast.BasicType.Void makeBasicTypeVoid(INode node) {
		return new BasicType.Void(node);
	}

	public org.rascalmpl.ast.BasicType.ReifiedTypeParameter makeBasicTypeReifiedTypeParameter(INode node) {
		return new BasicType.ReifiedTypeParameter(node);
	}

	public org.rascalmpl.ast.BasicType.ReifiedFunction makeBasicTypeReifiedFunction(INode node) {
		return new BasicType.ReifiedFunction(node);
	}

	public org.rascalmpl.ast.BasicType.String makeBasicTypeString(INode node) {
		return new BasicType.String(node);
	}

	public org.rascalmpl.ast.BasicType.ReifiedNonTerminal makeBasicTypeReifiedNonTerminal(INode node) {
		return new BasicType.ReifiedNonTerminal(node);
	}

	public org.rascalmpl.ast.BasicType.Value makeBasicTypeValue(INode node) {
		return new BasicType.Value(node);
	}

	public org.rascalmpl.ast.BasicType.ReifiedType makeBasicTypeReifiedType(INode node) {
		return new BasicType.ReifiedType(node);
	}

	public org.rascalmpl.ast.BasicType.Int makeBasicTypeInt(INode node) {
		return new BasicType.Int(node);
	}

	public org.rascalmpl.ast.BasicType.Bag makeBasicTypeBag(INode node) {
		return new BasicType.Bag(node);
	}

	public org.rascalmpl.ast.BasicType.Tuple makeBasicTypeTuple(INode node) {
		return new BasicType.Tuple(node);
	}

	public org.rascalmpl.ast.BasicType.Bool makeBasicTypeBool(INode node) {
		return new BasicType.Bool(node);
	}

	public org.rascalmpl.ast.BasicType.Num makeBasicTypeNum(INode node) {
		return new BasicType.Num(node);
	}

	public org.rascalmpl.ast.BasicType.Loc makeBasicTypeLoc(INode node) {
		return new BasicType.Loc(node);
	}

	public org.rascalmpl.ast.BasicType.Set makeBasicTypeSet(INode node) {
		return new BasicType.Set(node);
	}

	public org.rascalmpl.ast.BasicType.ReifiedConstructor makeBasicTypeReifiedConstructor(INode node) {
		return new BasicType.ReifiedConstructor(node);
	}

	public org.rascalmpl.ast.BasicType.Node makeBasicTypeNode(INode node) {
		return new BasicType.Node(node);
	}

	public org.rascalmpl.ast.ProtocolTail.Post makeProtocolTailPost(INode node, org.rascalmpl.ast.PostProtocolChars post) {
		return new ProtocolTail.Post(node, post);
	}

	public org.rascalmpl.ast.ProtocolTail.Mid makeProtocolTailMid(INode node, org.rascalmpl.ast.MidProtocolChars mid, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.ProtocolTail tail) {
		return new ProtocolTail.Mid(node, mid, expression, tail);
	}

	public org.rascalmpl.ast.QualifiedName.Default makeQualifiedNameDefault(INode node, java.util.List<org.rascalmpl.ast.Name> names) {
		return new QualifiedName.Default(node, names);
	}

	public org.rascalmpl.ast.Start.Absent makeStartAbsent(INode node) {
		return new Start.Absent(node);
	}

	public org.rascalmpl.ast.Start.Present makeStartPresent(INode node) {
		return new Start.Present(node);
	}

	public org.rascalmpl.ast.Parameters.VarArgs makeParametersVarArgs(INode node, org.rascalmpl.ast.Formals formals) {
		return new Parameters.VarArgs(node, formals);
	}

	public org.rascalmpl.ast.Parameters.Default makeParametersDefault(INode node, org.rascalmpl.ast.Formals formals) {
		return new Parameters.Default(node, formals);
	}

	public org.rascalmpl.ast.Mapping_Expression.Default makeMapping_ExpressionDefault(INode node, org.rascalmpl.ast.Expression from, org.rascalmpl.ast.Expression to) {
		return new Mapping_Expression.Default(node, from, to);
	}

	public org.rascalmpl.ast.Replacement.Unconditional makeReplacementUnconditional(INode node, org.rascalmpl.ast.Expression replacementExpression) {
		return new Replacement.Unconditional(node, replacementExpression);
	}

	public org.rascalmpl.ast.Replacement.Conditional makeReplacementConditional(INode node, org.rascalmpl.ast.Expression replacementExpression, java.util.List<org.rascalmpl.ast.Expression> conditions) {
		return new Replacement.Conditional(node, replacementExpression, conditions);
	}

	public org.rascalmpl.ast.Assignment.Addition makeAssignmentAddition(INode node) {
		return new Assignment.Addition(node);
	}

	public org.rascalmpl.ast.Assignment.IfDefined makeAssignmentIfDefined(INode node) {
		return new Assignment.IfDefined(node);
	}

	public org.rascalmpl.ast.Assignment.Division makeAssignmentDivision(INode node) {
		return new Assignment.Division(node);
	}

	public org.rascalmpl.ast.Assignment.Product makeAssignmentProduct(INode node) {
		return new Assignment.Product(node);
	}

	public org.rascalmpl.ast.Assignment.Intersection makeAssignmentIntersection(INode node) {
		return new Assignment.Intersection(node);
	}

	public org.rascalmpl.ast.Assignment.Subtraction makeAssignmentSubtraction(INode node) {
		return new Assignment.Subtraction(node);
	}

	public org.rascalmpl.ast.Assignment.Default makeAssignmentDefault(INode node) {
		return new Assignment.Default(node);
	}

	public org.rascalmpl.ast.Header.Parameters makeHeaderParameters(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.ModuleParameters params,
			java.util.List<org.rascalmpl.ast.Import> imports) {
		return new Header.Parameters(node, tags, name, params, imports);
	}

	public org.rascalmpl.ast.Header.Default makeHeaderDefault(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.QualifiedName name, java.util.List<org.rascalmpl.ast.Import> imports) {
		return new Header.Default(node, tags, name, imports);
	}

	public org.rascalmpl.ast.LanguageAction.Build makeLanguageActionBuild(INode node, org.rascalmpl.ast.Expression expression) {
		return new LanguageAction.Build(node, expression);
	}

	public org.rascalmpl.ast.LanguageAction.Action makeLanguageActionAction(INode node, java.util.List<org.rascalmpl.ast.Statement> statements) {
		return new LanguageAction.Action(node, statements);
	}

	public org.rascalmpl.ast.IntegerLiteral.OctalIntegerLiteral makeIntegerLiteralOctalIntegerLiteral(INode node, org.rascalmpl.ast.OctalIntegerLiteral octal) {
		return new IntegerLiteral.OctalIntegerLiteral(node, octal);
	}

	public org.rascalmpl.ast.IntegerLiteral.HexIntegerLiteral makeIntegerLiteralHexIntegerLiteral(INode node, org.rascalmpl.ast.HexIntegerLiteral hex) {
		return new IntegerLiteral.HexIntegerLiteral(node, hex);
	}

	public org.rascalmpl.ast.IntegerLiteral.DecimalIntegerLiteral makeIntegerLiteralDecimalIntegerLiteral(INode node, org.rascalmpl.ast.DecimalIntegerLiteral decimal) {
		return new IntegerLiteral.DecimalIntegerLiteral(node, decimal);
	}

	public org.rascalmpl.ast.PatternWithAction.Replacing makePatternWithActionReplacing(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Replacement replacement) {
		return new PatternWithAction.Replacing(node, pattern, replacement);
	}

	public org.rascalmpl.ast.PatternWithAction.Arbitrary makePatternWithActionArbitrary(INode node, org.rascalmpl.ast.Expression pattern, org.rascalmpl.ast.Statement statement) {
		return new PatternWithAction.Arbitrary(node, pattern, statement);
	}

	public org.rascalmpl.ast.Import.Extend makeImportExtend(INode node, org.rascalmpl.ast.ImportedModule module) {
		return new Import.Extend(node, module);
	}

	public org.rascalmpl.ast.Import.Default makeImportDefault(INode node, org.rascalmpl.ast.ImportedModule module) {
		return new Import.Default(node, module);
	}

	public org.rascalmpl.ast.Import.Syntax makeImportSyntax(INode node, org.rascalmpl.ast.SyntaxDefinition syntax) {
		return new Import.Syntax(node, syntax);
	}

	public org.rascalmpl.ast.FunctionModifier.Java makeFunctionModifierJava(INode node) {
		return new FunctionModifier.Java(node);
	}

	public org.rascalmpl.ast.ShellCommand.Unimport makeShellCommandUnimport(INode node, org.rascalmpl.ast.QualifiedName name) {
		return new ShellCommand.Unimport(node, name);
	}

	public org.rascalmpl.ast.ShellCommand.Quit makeShellCommandQuit(INode node) {
		return new ShellCommand.Quit(node);
	}

	public org.rascalmpl.ast.ShellCommand.Edit makeShellCommandEdit(INode node, org.rascalmpl.ast.QualifiedName name) {
		return new ShellCommand.Edit(node, name);
	}

	public org.rascalmpl.ast.ShellCommand.ListModules makeShellCommandListModules(INode node) {
		return new ShellCommand.ListModules(node);
	}

	public org.rascalmpl.ast.ShellCommand.History makeShellCommandHistory(INode node) {
		return new ShellCommand.History(node);
	}

	public org.rascalmpl.ast.ShellCommand.ListDeclarations makeShellCommandListDeclarations(INode node) {
		return new ShellCommand.ListDeclarations(node);
	}

	public org.rascalmpl.ast.ShellCommand.Help makeShellCommandHelp(INode node) {
		return new ShellCommand.Help(node);
	}

	public org.rascalmpl.ast.ShellCommand.SetOption makeShellCommandSetOption(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.Expression expression) {
		return new ShellCommand.SetOption(node, name, expression);
	}

	public org.rascalmpl.ast.ShellCommand.Undeclare makeShellCommandUndeclare(INode node, org.rascalmpl.ast.QualifiedName name) {
		return new ShellCommand.Undeclare(node, name);
	}

	public org.rascalmpl.ast.ShellCommand.Test makeShellCommandTest(INode node) {
		return new ShellCommand.Test(node);
	}

	public org.rascalmpl.ast.Declarator.Default makeDeclaratorDefault(INode node, org.rascalmpl.ast.Type type, java.util.List<org.rascalmpl.ast.Variable> variables) {
		return new Declarator.Default(node, type, variables);
	}

	public org.rascalmpl.ast.Variable.Initialized makeVariableInitialized(INode node, org.rascalmpl.ast.Name name, org.rascalmpl.ast.Expression initial) {
		return new Variable.Initialized(node, name, initial);
	}

	public org.rascalmpl.ast.Variable.UnInitialized makeVariableUnInitialized(INode node, org.rascalmpl.ast.Name name) {
		return new Variable.UnInitialized(node, name);
	}

	public org.rascalmpl.ast.StringMiddle.Interpolated makeStringMiddleInterpolated(INode node, org.rascalmpl.ast.MidStringChars mid, org.rascalmpl.ast.Expression expression,
			org.rascalmpl.ast.StringMiddle tail) {
		return new StringMiddle.Interpolated(node, mid, expression, tail);
	}

	public org.rascalmpl.ast.StringMiddle.Template makeStringMiddleTemplate(INode node, org.rascalmpl.ast.MidStringChars mid, org.rascalmpl.ast.StringTemplate template,
			org.rascalmpl.ast.StringMiddle tail) {
		return new StringMiddle.Template(node, mid, template, tail);
	}

	public org.rascalmpl.ast.StringMiddle.Mid makeStringMiddleMid(INode node, org.rascalmpl.ast.MidStringChars mid) {
		return new StringMiddle.Mid(node, mid);
	}

	public org.rascalmpl.ast.Signature.WithThrows makeSignatureWithThrows(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.FunctionModifiers modifiers, org.rascalmpl.ast.Name name,
			org.rascalmpl.ast.Parameters parameters, java.util.List<org.rascalmpl.ast.Type> exceptions) {
		return new Signature.WithThrows(node, type, modifiers, name, parameters, exceptions);
	}

	public org.rascalmpl.ast.Signature.NoThrows makeSignatureNoThrows(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.FunctionModifiers modifiers, org.rascalmpl.ast.Name name,
			org.rascalmpl.ast.Parameters parameters) {
		return new Signature.NoThrows(node, type, modifiers, name, parameters);
	}

	public org.rascalmpl.ast.TypeArg.Named makeTypeArgNamed(INode node, org.rascalmpl.ast.Type type, org.rascalmpl.ast.Name name) {
		return new TypeArg.Named(node, type, name);
	}

	public org.rascalmpl.ast.TypeArg.Default makeTypeArgDefault(INode node, org.rascalmpl.ast.Type type) {
		return new TypeArg.Default(node, type);
	}

	public org.rascalmpl.ast.FunctionDeclaration.Abstract makeFunctionDeclarationAbstract(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility,
			org.rascalmpl.ast.Signature signature) {
		return new FunctionDeclaration.Abstract(node, tags, visibility, signature);
	}

	public org.rascalmpl.ast.FunctionDeclaration.Default makeFunctionDeclarationDefault(INode node, org.rascalmpl.ast.Tags tags, org.rascalmpl.ast.Visibility visibility,
			org.rascalmpl.ast.Signature signature, org.rascalmpl.ast.FunctionBody body) {
		return new FunctionDeclaration.Default(node, tags, visibility, signature, body);
	}

	public org.rascalmpl.ast.PathPart.NonInterpolated makePathPartNonInterpolated(INode node, org.rascalmpl.ast.PathChars pathChars) {
		return new PathPart.NonInterpolated(node, pathChars);
	}

	public org.rascalmpl.ast.PathPart.Interpolated makePathPartInterpolated(INode node, org.rascalmpl.ast.PrePathChars pre, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.PathTail tail) {
		return new PathPart.Interpolated(node, pre, expression, tail);
	}

	public org.rascalmpl.ast.ImportedModule.Renamings makeImportedModuleRenamings(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.Renamings renamings) {
		return new ImportedModule.Renamings(node, name, renamings);
	}

	public org.rascalmpl.ast.ImportedModule.ActualsRenaming makeImportedModuleActualsRenaming(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.ModuleActuals actuals,
			org.rascalmpl.ast.Renamings renamings) {
		return new ImportedModule.ActualsRenaming(node, name, actuals, renamings);
	}

	public org.rascalmpl.ast.ImportedModule.Default makeImportedModuleDefault(INode node, org.rascalmpl.ast.QualifiedName name) {
		return new ImportedModule.Default(node, name);
	}

	public org.rascalmpl.ast.ImportedModule.Actuals makeImportedModuleActuals(INode node, org.rascalmpl.ast.QualifiedName name, org.rascalmpl.ast.ModuleActuals actuals) {
		return new ImportedModule.Actuals(node, name, actuals);
	}

	public org.rascalmpl.ast.Statement.VariableDeclaration makeStatementVariableDeclaration(INode node, org.rascalmpl.ast.LocalVariableDeclaration declaration) {
		return new Statement.VariableDeclaration(node, declaration);
	}

	public org.rascalmpl.ast.Statement.GlobalDirective makeStatementGlobalDirective(INode node, org.rascalmpl.ast.Type type, java.util.List<org.rascalmpl.ast.QualifiedName> names) {
		return new Statement.GlobalDirective(node, type, names);
	}

	public org.rascalmpl.ast.Statement.For makeStatementFor(INode node, org.rascalmpl.ast.Label label, java.util.List<org.rascalmpl.ast.Expression> generators, org.rascalmpl.ast.Statement body) {
		return new Statement.For(node, label, generators, body);
	}

	public org.rascalmpl.ast.Statement.Solve makeStatementSolve(INode node, java.util.List<org.rascalmpl.ast.QualifiedName> variables, org.rascalmpl.ast.Bound bound, org.rascalmpl.ast.Statement body) {
		return new Statement.Solve(node, variables, bound, body);
	}

	public org.rascalmpl.ast.Statement.While makeStatementWhile(INode node, org.rascalmpl.ast.Label label, java.util.List<org.rascalmpl.ast.Expression> conditions, org.rascalmpl.ast.Statement body) {
		return new Statement.While(node, label, conditions, body);
	}

	public org.rascalmpl.ast.Statement.AssertWithMessage makeStatementAssertWithMessage(INode node, org.rascalmpl.ast.Expression expression, org.rascalmpl.ast.Expression message) {
		return new Statement.AssertWithMessage(node, expression, message);
	}

	public org.rascalmpl.ast.Statement.Expression makeStatementExpression(INode node, org.rascalmpl.ast.Expression expression) {
		return new Statement.Expression(node, expression);
	}

	public org.rascalmpl.ast.Statement.DoWhile makeStatementDoWhile(INode node, org.rascalmpl.ast.Label label, org.rascalmpl.ast.Statement body, org.rascalmpl.ast.Expression condition) {
		return new Statement.DoWhile(node, label, body, condition);
	}

	public org.rascalmpl.ast.Statement.Assignment makeStatementAssignment(INode node, org.rascalmpl.ast.Assignable assignable, org.rascalmpl.ast.Assignment operator,
			org.rascalmpl.ast.Statement statement) {
		return new Statement.Assignment(node, assignable, operator, statement);
	}

	public org.rascalmpl.ast.Statement.Return makeStatementReturn(INode node, org.rascalmpl.ast.Statement statement) {
		return new Statement.Return(node, statement);
	}

	public org.rascalmpl.ast.Statement.Fail makeStatementFail(INode node, org.rascalmpl.ast.Target target) {
		return new Statement.Fail(node, target);
	}

	public org.rascalmpl.ast.Statement.Break makeStatementBreak(INode node, org.rascalmpl.ast.Target target) {
		return new Statement.Break(node, target);
	}

	public org.rascalmpl.ast.Statement.IfThenElse makeStatementIfThenElse(INode node, org.rascalmpl.ast.Label label, java.util.List<org.rascalmpl.ast.Expression> conditions,
			org.rascalmpl.ast.Statement thenStatement, org.rascalmpl.ast.Statement elseStatement) {
		return new Statement.IfThenElse(node, label, conditions, thenStatement, elseStatement);
	}

	public org.rascalmpl.ast.Statement.IfThen makeStatementIfThen(INode node, org.rascalmpl.ast.Label label, java.util.List<org.rascalmpl.ast.Expression> conditions,
			org.rascalmpl.ast.Statement thenStatement, org.rascalmpl.ast.NoElseMayFollow noElseMayFollow) {
		return new Statement.IfThen(node, label, conditions, thenStatement, noElseMayFollow);
	}

	public org.rascalmpl.ast.Statement.FunctionDeclaration makeStatementFunctionDeclaration(INode node, org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
		return new Statement.FunctionDeclaration(node, functionDeclaration);
	}

	public org.rascalmpl.ast.Statement.Switch makeStatementSwitch(INode node, org.rascalmpl.ast.Label label, org.rascalmpl.ast.Expression expression, java.util.List<org.rascalmpl.ast.Case> cases) {
		return new Statement.Switch(node, label, expression, cases);
	}

	public org.rascalmpl.ast.Statement.Append makeStatementAppend(INode node, org.rascalmpl.ast.DataTarget dataTarget, org.rascalmpl.ast.Statement statement) {
		return new Statement.Append(node, dataTarget, statement);
	}

	public org.rascalmpl.ast.Statement.Insert makeStatementInsert(INode node, org.rascalmpl.ast.DataTarget dataTarget, org.rascalmpl.ast.Statement statement) {
		return new Statement.Insert(node, dataTarget, statement);
	}

	public org.rascalmpl.ast.Statement.Throw makeStatementThrow(INode node, org.rascalmpl.ast.Statement statement) {
		return new Statement.Throw(node, statement);
	}

	public org.rascalmpl.ast.Statement.NonEmptyBlock makeStatementNonEmptyBlock(INode node, org.rascalmpl.ast.Label label, java.util.List<org.rascalmpl.ast.Statement> statements) {
		return new Statement.NonEmptyBlock(node, label, statements);
	}

	public org.rascalmpl.ast.Statement.TryFinally makeStatementTryFinally(INode node, org.rascalmpl.ast.Statement body, java.util.List<org.rascalmpl.ast.Catch> handlers,
			org.rascalmpl.ast.Statement finallyBody) {
		return new Statement.TryFinally(node, body, handlers, finallyBody);
	}

	public org.rascalmpl.ast.Statement.Assert makeStatementAssert(INode node, org.rascalmpl.ast.Expression expression) {
		return new Statement.Assert(node, expression);
	}

	public org.rascalmpl.ast.Statement.EmptyStatement makeStatementEmptyStatement(INode node) {
		return new Statement.EmptyStatement(node);
	}

	public org.rascalmpl.ast.Statement.Try makeStatementTry(INode node, org.rascalmpl.ast.Statement body, java.util.List<org.rascalmpl.ast.Catch> handlers) {
		return new Statement.Try(node, body, handlers);
	}

	public org.rascalmpl.ast.Statement.Visit makeStatementVisit(INode node, org.rascalmpl.ast.Label label, org.rascalmpl.ast.Visit visit) {
		return new Statement.Visit(node, label, visit);
	}

	public org.rascalmpl.ast.Statement.Continue makeStatementContinue(INode node, org.rascalmpl.ast.Target target) {
		return new Statement.Continue(node, target);
	}

	public org.rascalmpl.ast.Assignable.Tuple makeAssignableTuple(INode node, java.util.List<org.rascalmpl.ast.Assignable> elements) {
		return new Assignable.Tuple(node, elements);
	}

	public org.rascalmpl.ast.Assignable.Variable makeAssignableVariable(INode node, org.rascalmpl.ast.QualifiedName qualifiedName) {
		return new Assignable.Variable(node, qualifiedName);
	}

	public org.rascalmpl.ast.Assignable.IfDefinedOrDefault makeAssignableIfDefinedOrDefault(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Expression defaultExpression) {
		return new Assignable.IfDefinedOrDefault(node, receiver, defaultExpression);
	}

	public org.rascalmpl.ast.Assignable.Subscript makeAssignableSubscript(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Expression subscript) {
		return new Assignable.Subscript(node, receiver, subscript);
	}

	public org.rascalmpl.ast.Assignable.Bracket makeAssignableBracket(INode node, org.rascalmpl.ast.Assignable arg) {
		return new Assignable.Bracket(node, arg);
	}

	public org.rascalmpl.ast.Assignable.FieldAccess makeAssignableFieldAccess(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Name field) {
		return new Assignable.FieldAccess(node, receiver, field);
	}

	public org.rascalmpl.ast.Assignable.Constructor makeAssignableConstructor(INode node, org.rascalmpl.ast.Name name, java.util.List<org.rascalmpl.ast.Assignable> arguments) {
		return new Assignable.Constructor(node, name, arguments);
	}

	public org.rascalmpl.ast.Assignable.Annotation makeAssignableAnnotation(INode node, org.rascalmpl.ast.Assignable receiver, org.rascalmpl.ast.Name annotation) {
		return new Assignable.Annotation(node, receiver, annotation);
	}

	public org.rascalmpl.ast.RegExpLiteral.Lexical makeRegExpLiteralLexical(INode node, String string) {
		return new RegExpLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.PreProtocolChars.Lexical makePreProtocolCharsLexical(INode node, String string) {
		return new PreProtocolChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.NamedRegExp.Lexical makeNamedRegExpLexical(INode node, String string) {
		return new NamedRegExp.Lexical(node, string);
	}

	public org.rascalmpl.ast.OctalEscapeSequence.Lexical makeOctalEscapeSequenceLexical(INode node, String string) {
		return new OctalEscapeSequence.Lexical(node, string);
	}

	public org.rascalmpl.ast.PostStringChars.Lexical makePostStringCharsLexical(INode node, String string) {
		return new PostStringChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.HexIntegerLiteral.Lexical makeHexIntegerLiteralLexical(INode node, String string) {
		return new HexIntegerLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.OctalLongLiteral.Lexical makeOctalLongLiteralLexical(INode node, String string) {
		return new OctalLongLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.Char.Lexical makeCharLexical(INode node, String string) {
		return new Char.Lexical(node, string);
	}

	public org.rascalmpl.ast.PrePathChars.Lexical makePrePathCharsLexical(INode node, String string) {
		return new PrePathChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.MidPathChars.Lexical makeMidPathCharsLexical(INode node, String string) {
		return new MidPathChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.PostProtocolChars.Lexical makePostProtocolCharsLexical(INode node, String string) {
		return new PostProtocolChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.NonterminalLabel.Lexical makeNonterminalLabelLexical(INode node, String string) {
		return new NonterminalLabel.Lexical(node, string);
	}

	public org.rascalmpl.ast.DecimalIntegerLiteral.Lexical makeDecimalIntegerLiteralLexical(INode node, String string) {
		return new DecimalIntegerLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.TagString.Lexical makeTagStringLexical(INode node, String string) {
		return new TagString.Lexical(node, string);
	}

	public org.rascalmpl.ast.Word.Lexical makeWordLexical(INode node, String string) {
		return new Word.Lexical(node, string);
	}

	public org.rascalmpl.ast.LAYOUT.Lexical makeLAYOUTLexical(INode node, String string) {
		return new LAYOUT.Lexical(node, string);
	}

	public org.rascalmpl.ast.Nonterminal.Lexical makeNonterminalLexical(INode node, String string) {
		return new Nonterminal.Lexical(node, string);
	}

	public org.rascalmpl.ast.CommentChar.Lexical makeCommentCharLexical(INode node, String string) {
		return new CommentChar.Lexical(node, string);
	}

	public org.rascalmpl.ast.Comment.Lexical makeCommentLexical(INode node, String string) {
		return new Comment.Lexical(node, string);
	}

	public org.rascalmpl.ast.StrChar.Lexical makeStrCharLexical(INode node, String string) {
		return new StrChar.Lexical(node, string);
	}

	public org.rascalmpl.ast.ProtocolChars.Lexical makeProtocolCharsLexical(INode node, String string) {
		return new ProtocolChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.MidStringChars.Lexical makeMidStringCharsLexical(INode node, String string) {
		return new MidStringChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.RegExpModifier.Lexical makeRegExpModifierLexical(INode node, String string) {
		return new RegExpModifier.Lexical(node, string);
	}

	public org.rascalmpl.ast.EscapedName.Lexical makeEscapedNameLexical(INode node, String string) {
		return new EscapedName.Lexical(node, string);
	}

	public org.rascalmpl.ast.RegExp.Lexical makeRegExpLexical(INode node, String string) {
		return new RegExp.Lexical(node, string);
	}

	public org.rascalmpl.ast.SingleQuotedStrChar.Lexical makeSingleQuotedStrCharLexical(INode node, String string) {
		return new SingleQuotedStrChar.Lexical(node, string);
	}

	public org.rascalmpl.ast.RealLiteral.Lexical makeRealLiteralLexical(INode node, String string) {
		return new RealLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.DatePart.Lexical makeDatePartLexical(INode node, String string) {
		return new DatePart.Lexical(node, string);
	}

	public org.rascalmpl.ast.StringConstant.Lexical makeStringConstantLexical(INode node, String string) {
		return new StringConstant.Lexical(node, string);
	}

	public org.rascalmpl.ast.ParameterizedNonterminal.Lexical makeParameterizedNonterminalLexical(INode node, String string) {
		return new ParameterizedNonterminal.Lexical(node, string);
	}

	public org.rascalmpl.ast.TagChar.Lexical makeTagCharLexical(INode node, String string) {
		return new TagChar.Lexical(node, string);
	}

	public org.rascalmpl.ast.JustTime.Lexical makeJustTimeLexical(INode node, String string) {
		return new JustTime.Lexical(node, string);
	}

	public org.rascalmpl.ast.StringCharacter.Lexical makeStringCharacterLexical(INode node, String string) {
		return new StringCharacter.Lexical(node, string);
	}

	public org.rascalmpl.ast.URLChars.Lexical makeURLCharsLexical(INode node, String string) {
		return new URLChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.TimeZonePart.Lexical makeTimeZonePartLexical(INode node, String string) {
		return new TimeZonePart.Lexical(node, string);
	}

	public org.rascalmpl.ast.PreStringChars.Lexical makePreStringCharsLexical(INode node, String string) {
		return new PreStringChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.Backslash.Lexical makeBackslashLexical(INode node, String string) {
		return new Backslash.Lexical(node, string);
	}

	public org.rascalmpl.ast.CaseInsensitiveStringConstant.Lexical makeCaseInsensitiveStringConstantLexical(INode node, String string) {
		return new CaseInsensitiveStringConstant.Lexical(node, string);
	}

	public org.rascalmpl.ast.ShortChar.Lexical makeShortCharLexical(INode node, String string) {
		return new ShortChar.Lexical(node, string);
	}

	public org.rascalmpl.ast.NamedBackslash.Lexical makeNamedBackslashLexical(INode node, String string) {
		return new NamedBackslash.Lexical(node, string);
	}

	public org.rascalmpl.ast.MidProtocolChars.Lexical makeMidProtocolCharsLexical(INode node, String string) {
		return new MidProtocolChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.NumChar.Lexical makeNumCharLexical(INode node, String string) {
		return new NumChar.Lexical(node, string);
	}

	public org.rascalmpl.ast.JustDate.Lexical makeJustDateLexical(INode node, String string) {
		return new JustDate.Lexical(node, string);
	}

	public org.rascalmpl.ast.PostPathChars.Lexical makePostPathCharsLexical(INode node, String string) {
		return new PostPathChars.Lexical(node, string);
	}

	public org.rascalmpl.ast.TimePartNoTZ.Lexical makeTimePartNoTZLexical(INode node, String string) {
		return new TimePartNoTZ.Lexical(node, string);
	}

	public org.rascalmpl.ast.DecimalLongLiteral.Lexical makeDecimalLongLiteralLexical(INode node, String string) {
		return new DecimalLongLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.SingleQuotedStrCon.Lexical makeSingleQuotedStrConLexical(INode node, String string) {
		return new SingleQuotedStrCon.Lexical(node, string);
	}

	public org.rascalmpl.ast.Name.Lexical makeNameLexical(INode node, String string) {
		return new Name.Lexical(node, string);
	}

	public org.rascalmpl.ast.StrCon.Lexical makeStrConLexical(INode node, String string) {
		return new StrCon.Lexical(node, string);
	}

	public org.rascalmpl.ast.BooleanLiteral.Lexical makeBooleanLiteralLexical(INode node, String string) {
		return new BooleanLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.DateAndTime.Lexical makeDateAndTimeLexical(INode node, String string) {
		return new DateAndTime.Lexical(node, string);
	}

	public org.rascalmpl.ast.Asterisk.Lexical makeAsteriskLexical(INode node, String string) {
		return new Asterisk.Lexical(node, string);
	}

	public org.rascalmpl.ast.UnicodeEscape.Lexical makeUnicodeEscapeLexical(INode node, String string) {
		return new UnicodeEscape.Lexical(node, string);
	}

	public org.rascalmpl.ast.OctalIntegerLiteral.Lexical makeOctalIntegerLiteralLexical(INode node, String string) {
		return new OctalIntegerLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.HexLongLiteral.Lexical makeHexLongLiteralLexical(INode node, String string) {
		return new HexLongLiteral.Lexical(node, string);
	}

	public org.rascalmpl.ast.PathChars.Lexical makePathCharsLexical(INode node, String string) {
		return new PathChars.Lexical(node, string);
	}

}
