
package org.rascalmpl.ast;
import org.eclipse.imp.pdb.facts.INode;

public class ASTFactory {

      public LocationLiteral.Default makeLocationLiteralDefault(INode node , org.rascalmpl.ast.ProtocolPart protocolPart,  org.rascalmpl.ast.PathPart pathPart) {
         return new LocationLiteral.Default(node , protocolPart, pathPart);
      }

      public Tag.Default makeTagDefault(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.TagString contents) {
         return new Tag.Default(node , name, contents);
      }

      public Tag.Expression makeTagExpression(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression expression) {
         return new Tag.Expression(node , name, expression);
      }

      public Tag.Empty makeTagEmpty(INode node , org.rascalmpl.ast.Name name) {
         return new Tag.Empty(node , name);
      }

      public UserType.Parametric makeUserTypeParametric(INode node , org.rascalmpl.ast.QualifiedName name,  java.util.List<org.rascalmpl.ast.Type> parameters) {
         return new UserType.Parametric(node , name, parameters);
      }

      public UserType.Name makeUserTypeName(INode node , org.rascalmpl.ast.QualifiedName name) {
         return new UserType.Name(node , name);
      }

      public Range.FromTo makeRangeFromTo(INode node , org.rascalmpl.ast.Char start,  org.rascalmpl.ast.Char end) {
         return new Range.FromTo(node , start, end);
      }

      public Range.Character makeRangeCharacter(INode node , org.rascalmpl.ast.Char character) {
         return new Range.Character(node , character);
      }

      public StructuredType.Default makeStructuredTypeDefault(INode node , org.rascalmpl.ast.BasicType basicType,  java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
         return new StructuredType.Default(node , basicType, arguments);
      }

      public ProdModifier.Associativity makeProdModifierAssociativity(INode node , org.rascalmpl.ast.Assoc associativity) {
         return new ProdModifier.Associativity(node , associativity);
      }

      public ProdModifier.Tag makeProdModifierTag(INode node , org.rascalmpl.ast.Tag tag) {
         return new ProdModifier.Tag(node , tag);
      }

      public ProdModifier.Bracket makeProdModifierBracket(INode node ) {
         return new ProdModifier.Bracket(node );
      }

      public ProdModifier.Lexical makeProdModifierLexical(INode node ) {
         return new ProdModifier.Lexical(node );
      }

      public DataTypeSelector.Selector makeDataTypeSelectorSelector(INode node , org.rascalmpl.ast.QualifiedName sort,  org.rascalmpl.ast.Name production) {
         return new DataTypeSelector.Selector(node , sort, production);
      }

      public FunctionBody.Default makeFunctionBodyDefault(INode node , java.util.List<org.rascalmpl.ast.Statement> statements) {
         return new FunctionBody.Default(node , statements);
      }

      public Formals.Default makeFormalsDefault(INode node , java.util.List<org.rascalmpl.ast.Formal> formals) {
         return new Formals.Default(node , formals);
      }

      public Renaming.Default makeRenamingDefault(INode node , org.rascalmpl.ast.Name from,  org.rascalmpl.ast.Name to) {
         return new Renaming.Default(node , from, to);
      }

      public StringLiteral.NonInterpolated makeStringLiteralNonInterpolated(INode node , org.rascalmpl.ast.StringConstant constant) {
         return new StringLiteral.NonInterpolated(node , constant);
      }

      public StringLiteral.Template makeStringLiteralTemplate(INode node , org.rascalmpl.ast.PreStringChars pre,  org.rascalmpl.ast.StringTemplate template,  org.rascalmpl.ast.StringTail tail) {
         return new StringLiteral.Template(node , pre, template, tail);
      }

      public StringLiteral.Interpolated makeStringLiteralInterpolated(INode node , org.rascalmpl.ast.PreStringChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringTail tail) {
         return new StringLiteral.Interpolated(node , pre, expression, tail);
      }

      public Body.Toplevels makeBodyToplevels(INode node , java.util.List<org.rascalmpl.ast.Toplevel> toplevels) {
         return new Body.Toplevels(node , toplevels);
      }

      public Variant.NAryConstructor makeVariantNAryConstructor(INode node , org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
         return new Variant.NAryConstructor(node , name, arguments);
      }

      public StringTail.MidTemplate makeStringTailMidTemplate(INode node , org.rascalmpl.ast.MidStringChars mid,  org.rascalmpl.ast.StringTemplate template,  org.rascalmpl.ast.StringTail tail) {
         return new StringTail.MidTemplate(node , mid, template, tail);
      }

      public StringTail.MidInterpolated makeStringTailMidInterpolated(INode node , org.rascalmpl.ast.MidStringChars mid,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringTail tail) {
         return new StringTail.MidInterpolated(node , mid, expression, tail);
      }

      public StringTail.Post makeStringTailPost(INode node , org.rascalmpl.ast.PostStringChars post) {
         return new StringTail.Post(node , post);
      }

      public Test.Unlabeled makeTestUnlabeled(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Expression expression) {
         return new Test.Unlabeled(node , tags, expression);
      }

      public Test.Labeled makeTestLabeled(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringLiteral labeled) {
         return new Test.Labeled(node , tags, expression, labeled);
      }

      public ProtocolPart.NonInterpolated makeProtocolPartNonInterpolated(INode node , org.rascalmpl.ast.ProtocolChars protocolChars) {
         return new ProtocolPart.NonInterpolated(node , protocolChars);
      }

      public ProtocolPart.Interpolated makeProtocolPartInterpolated(INode node , org.rascalmpl.ast.PreProtocolChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.ProtocolTail tail) {
         return new ProtocolPart.Interpolated(node , pre, expression, tail);
      }

      public FunctionModifiers.List makeFunctionModifiersList(INode node , java.util.List<org.rascalmpl.ast.FunctionModifier> modifiers) {
         return new FunctionModifiers.List(node , modifiers);
      }

      public Command.Shell makeCommandShell(INode node , org.rascalmpl.ast.ShellCommand command) {
         return new Command.Shell(node , command);
      }

      public Command.Import makeCommandImport(INode node , org.rascalmpl.ast.Import imported) {
         return new Command.Import(node , imported);
      }

      public Command.Expression makeCommandExpression(INode node , org.rascalmpl.ast.Expression expression) {
         return new Command.Expression(node , expression);
      }

      public Command.Statement makeCommandStatement(INode node , org.rascalmpl.ast.Statement statement) {
         return new Command.Statement(node , statement);
      }

      public Command.Declaration makeCommandDeclaration(INode node , org.rascalmpl.ast.Declaration declaration) {
         return new Command.Declaration(node , declaration);
      }

      public Toplevel.GivenVisibility makeToplevelGivenVisibility(INode node , org.rascalmpl.ast.Declaration declaration) {
         return new Toplevel.GivenVisibility(node , declaration);
      }

      public Type.Basic makeTypeBasic(INode node , org.rascalmpl.ast.BasicType basic) {
         return new Type.Basic(node , basic);
      }

      public Type.Function makeTypeFunction(INode node , org.rascalmpl.ast.FunctionType function) {
         return new Type.Function(node , function);
      }

      public Type.Structured makeTypeStructured(INode node , org.rascalmpl.ast.StructuredType structured) {
         return new Type.Structured(node , structured);
      }

      public Type.Bracket makeTypeBracket(INode node , org.rascalmpl.ast.Type type) {
         return new Type.Bracket(node , type);
      }

      public Type.Variable makeTypeVariable(INode node , org.rascalmpl.ast.TypeVar typeVar) {
         return new Type.Variable(node , typeVar);
      }

      public Type.Selector makeTypeSelector(INode node , org.rascalmpl.ast.DataTypeSelector selector) {
         return new Type.Selector(node , selector);
      }

      public Type.Symbol makeTypeSymbol(INode node , org.rascalmpl.ast.Sym symbol) {
         return new Type.Symbol(node , symbol);
      }

      public Type.User makeTypeUser(INode node , org.rascalmpl.ast.UserType user) {
         return new Type.User(node , user);
      }

      public Label.Empty makeLabelEmpty(INode node ) {
         return new Label.Empty(node );
      }

      public Label.Default makeLabelDefault(INode node , org.rascalmpl.ast.Name name) {
         return new Label.Default(node , name);
      }

      public ModuleParameters.Default makeModuleParametersDefault(INode node , java.util.List<org.rascalmpl.ast.TypeVar> parameters) {
         return new ModuleParameters.Default(node , parameters);
      }

      public Expression.Anti makeExpressionAnti(INode node , org.rascalmpl.ast.Expression pattern) {
         return new Expression.Anti(node , pattern);
      }

      public Expression.Product makeExpressionProduct(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Product(node , lhs, rhs);
      }

      public Expression.Division makeExpressionDivision(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Division(node , lhs, rhs);
      }

      public Expression.Equivalence makeExpressionEquivalence(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Equivalence(node , lhs, rhs);
      }

      public Expression.Any makeExpressionAny(INode node , java.util.List<org.rascalmpl.ast.Expression> generators) {
         return new Expression.Any(node , generators);
      }

      public Expression.NonEquals makeExpressionNonEquals(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.NonEquals(node , lhs, rhs);
      }

      public Expression.Match makeExpressionMatch(INode node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Expression expression) {
         return new Expression.Match(node , pattern, expression);
      }

      public Expression.StepRange makeExpressionStepRange(INode node , org.rascalmpl.ast.Expression first,  org.rascalmpl.ast.Expression second,  org.rascalmpl.ast.Expression last) {
         return new Expression.StepRange(node , first, second, last);
      }

      public Expression.Composition makeExpressionComposition(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Composition(node , lhs, rhs);
      }

      public Expression.Enumerator makeExpressionEnumerator(INode node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Expression expression) {
         return new Expression.Enumerator(node , pattern, expression);
      }

      public Expression.Join makeExpressionJoin(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Join(node , lhs, rhs);
      }

      public Expression.NoMatch makeExpressionNoMatch(INode node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Expression expression) {
         return new Expression.NoMatch(node , pattern, expression);
      }

      public Expression.LessThanOrEq makeExpressionLessThanOrEq(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.LessThanOrEq(node , lhs, rhs);
      }

      public Expression.TypedVariable makeExpressionTypedVariable(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
         return new Expression.TypedVariable(node , type, name);
      }

      public Expression.IfDefinedOtherwise makeExpressionIfDefinedOtherwise(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.IfDefinedOtherwise(node , lhs, rhs);
      }

      public Expression.VoidClosure makeExpressionVoidClosure(INode node , org.rascalmpl.ast.Parameters parameters,  java.util.List<org.rascalmpl.ast.Statement> statements) {
         return new Expression.VoidClosure(node , parameters, statements);
      }

      public Expression.Comprehension makeExpressionComprehension(INode node , org.rascalmpl.ast.Comprehension comprehension) {
         return new Expression.Comprehension(node , comprehension);
      }

      public Expression.In makeExpressionIn(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.In(node , lhs, rhs);
      }

      public Expression.Or makeExpressionOr(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Or(node , lhs, rhs);
      }

      public Expression.Set makeExpressionSet(INode node , java.util.List<org.rascalmpl.ast.Expression> elements) {
         return new Expression.Set(node , elements);
      }

      public Expression.FieldAccess makeExpressionFieldAccess(INode node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name field) {
         return new Expression.FieldAccess(node , expression, field);
      }

      public Expression.All makeExpressionAll(INode node , java.util.List<org.rascalmpl.ast.Expression> generators) {
         return new Expression.All(node , generators);
      }

      public Expression.FieldProject makeExpressionFieldProject(INode node , org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Field> fields) {
         return new Expression.FieldProject(node , expression, fields);
      }

      public Expression.Equals makeExpressionEquals(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Equals(node , lhs, rhs);
      }

      public Expression.Addition makeExpressionAddition(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Addition(node , lhs, rhs);
      }

      public Expression.Implication makeExpressionImplication(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Implication(node , lhs, rhs);
      }

      public Expression.ReifiedType makeExpressionReifiedType(INode node , org.rascalmpl.ast.BasicType basicType,  java.util.List<org.rascalmpl.ast.Expression> arguments) {
         return new Expression.ReifiedType(node , basicType, arguments);
      }

      public Expression.Bracket makeExpressionBracket(INode node , org.rascalmpl.ast.Expression expression) {
         return new Expression.Bracket(node , expression);
      }

      public Expression.GreaterThan makeExpressionGreaterThan(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.GreaterThan(node , lhs, rhs);
      }

      public Expression.Subscript makeExpressionSubscript(INode node , org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Expression> subscripts) {
         return new Expression.Subscript(node , expression, subscripts);
      }

      public Expression.IfThenElse makeExpressionIfThenElse(INode node , org.rascalmpl.ast.Expression condition,  org.rascalmpl.ast.Expression thenExp,  org.rascalmpl.ast.Expression elseExp) {
         return new Expression.IfThenElse(node , condition, thenExp, elseExp);
      }

      public Expression.Modulo makeExpressionModulo(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Modulo(node , lhs, rhs);
      }

      public Expression.TransitiveClosure makeExpressionTransitiveClosure(INode node , org.rascalmpl.ast.Expression argument) {
         return new Expression.TransitiveClosure(node , argument);
      }

      public Expression.Subtraction makeExpressionSubtraction(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Subtraction(node , lhs, rhs);
      }

      public Expression.ReifyType makeExpressionReifyType(INode node , org.rascalmpl.ast.Type type) {
         return new Expression.ReifyType(node , type);
      }

      public Expression.NonEmptyBlock makeExpressionNonEmptyBlock(INode node , java.util.List<org.rascalmpl.ast.Statement> statements) {
         return new Expression.NonEmptyBlock(node , statements);
      }

      public Expression.CallOrTree makeExpressionCallOrTree(INode node , org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Expression> arguments) {
         return new Expression.CallOrTree(node , expression, arguments);
      }

      public Expression.Descendant makeExpressionDescendant(INode node , org.rascalmpl.ast.Expression pattern) {
         return new Expression.Descendant(node , pattern);
      }

      public Expression.Range makeExpressionRange(INode node , org.rascalmpl.ast.Expression first,  org.rascalmpl.ast.Expression last) {
         return new Expression.Range(node , first, last);
      }

      public Expression.GetAnnotation makeExpressionGetAnnotation(INode node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name name) {
         return new Expression.GetAnnotation(node , expression, name);
      }

      public Expression.Guarded makeExpressionGuarded(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Expression pattern) {
         return new Expression.Guarded(node , type, pattern);
      }

      public Expression.VariableBecomes makeExpressionVariableBecomes(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression pattern) {
         return new Expression.VariableBecomes(node , name, pattern);
      }

      public Expression.GreaterThanOrEq makeExpressionGreaterThanOrEq(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.GreaterThanOrEq(node , lhs, rhs);
      }

      public Expression.Intersection makeExpressionIntersection(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.Intersection(node , lhs, rhs);
      }

      public Expression.Tuple makeExpressionTuple(INode node , java.util.List<org.rascalmpl.ast.Expression> elements) {
         return new Expression.Tuple(node , elements);
      }

      public Expression.FieldUpdate makeExpressionFieldUpdate(INode node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name key,  org.rascalmpl.ast.Expression replacement) {
         return new Expression.FieldUpdate(node , expression, key, replacement);
      }

      public Expression.MultiVariable makeExpressionMultiVariable(INode node , org.rascalmpl.ast.QualifiedName qualifiedName) {
         return new Expression.MultiVariable(node , qualifiedName);
      }

      public Expression.Negation makeExpressionNegation(INode node , org.rascalmpl.ast.Expression argument) {
         return new Expression.Negation(node , argument);
      }

      public Expression.Literal makeExpressionLiteral(INode node , org.rascalmpl.ast.Literal literal) {
         return new Expression.Literal(node , literal);
      }

      public Expression.IsDefined makeExpressionIsDefined(INode node , org.rascalmpl.ast.Expression argument) {
         return new Expression.IsDefined(node , argument);
      }

      public Expression.Closure makeExpressionClosure(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Parameters parameters,  java.util.List<org.rascalmpl.ast.Statement> statements) {
         return new Expression.Closure(node , type, parameters, statements);
      }

      public Expression.List makeExpressionList(INode node , java.util.List<org.rascalmpl.ast.Expression> elements) {
         return new Expression.List(node , elements);
      }

      public Expression.LessThan makeExpressionLessThan(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.LessThan(node , lhs, rhs);
      }

      public Expression.NotIn makeExpressionNotIn(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.NotIn(node , lhs, rhs);
      }

      public Expression.It makeExpressionIt(INode node ) {
         return new Expression.It(node );
      }

      public Expression.And makeExpressionAnd(INode node , org.rascalmpl.ast.Expression lhs,  org.rascalmpl.ast.Expression rhs) {
         return new Expression.And(node , lhs, rhs);
      }

      public Expression.QualifiedName makeExpressionQualifiedName(INode node , org.rascalmpl.ast.QualifiedName qualifiedName) {
         return new Expression.QualifiedName(node , qualifiedName);
      }

      public Expression.Negative makeExpressionNegative(INode node , org.rascalmpl.ast.Expression argument) {
         return new Expression.Negative(node , argument);
      }

      public Expression.Reducer makeExpressionReducer(INode node , org.rascalmpl.ast.Expression init,  org.rascalmpl.ast.Expression result,  java.util.List<org.rascalmpl.ast.Expression> generators) {
         return new Expression.Reducer(node , init, result, generators);
      }

      public Expression.TransitiveReflexiveClosure makeExpressionTransitiveReflexiveClosure(INode node , org.rascalmpl.ast.Expression argument) {
         return new Expression.TransitiveReflexiveClosure(node , argument);
      }

      public Expression.Map makeExpressionMap(INode node , java.util.List<org.rascalmpl.ast.Mapping_Expression> mappings) {
         return new Expression.Map(node , mappings);
      }

      public Expression.Visit makeExpressionVisit(INode node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Visit visit) {
         return new Expression.Visit(node , label, visit);
      }

      public Expression.SetAnnotation makeExpressionSetAnnotation(INode node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression value) {
         return new Expression.SetAnnotation(node , expression, name, value);
      }

      public Expression.TypedVariableBecomes makeExpressionTypedVariableBecomes(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression pattern) {
         return new Expression.TypedVariableBecomes(node , type, name, pattern);
      }

      public Module.Default makeModuleDefault(INode node , org.rascalmpl.ast.Header header,  org.rascalmpl.ast.Body body) {
         return new Module.Default(node , header, body);
      }

      public DateTimeLiteral.DateAndTimeLiteral makeDateTimeLiteralDateAndTimeLiteral(INode node , org.rascalmpl.ast.DateAndTime dateAndTime) {
         return new DateTimeLiteral.DateAndTimeLiteral(node , dateAndTime);
      }

      public DateTimeLiteral.TimeLiteral makeDateTimeLiteralTimeLiteral(INode node , org.rascalmpl.ast.JustTime time) {
         return new DateTimeLiteral.TimeLiteral(node , time);
      }

      public DateTimeLiteral.DateLiteral makeDateTimeLiteralDateLiteral(INode node , org.rascalmpl.ast.JustDate date) {
         return new DateTimeLiteral.DateLiteral(node , date);
      }

      public Declaration.Alias makeDeclarationAlias(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user,  org.rascalmpl.ast.Type base) {
         return new Declaration.Alias(node , tags, visibility, user, base);
      }

      public Declaration.Data makeDeclarationData(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user,  java.util.List<org.rascalmpl.ast.Variant> variants) {
         return new Declaration.Data(node , tags, visibility, user, variants);
      }

      public Declaration.Annotation makeDeclarationAnnotation(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Type annoType,  org.rascalmpl.ast.Type onType,  org.rascalmpl.ast.Name name) {
         return new Declaration.Annotation(node , tags, visibility, annoType, onType, name);
      }

      public Declaration.Function makeDeclarationFunction(INode node , org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
         return new Declaration.Function(node , functionDeclaration);
      }

      public Declaration.Rule makeDeclarationRule(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.PatternWithAction patternAction) {
         return new Declaration.Rule(node , tags, name, patternAction);
      }

      public Declaration.DataAbstract makeDeclarationDataAbstract(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.UserType user) {
         return new Declaration.DataAbstract(node , tags, visibility, user);
      }

      public Declaration.Variable makeDeclarationVariable(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.Variable> variables) {
         return new Declaration.Variable(node , tags, visibility, type, variables);
      }

      public Declaration.Test makeDeclarationTest(INode node , org.rascalmpl.ast.Test test) {
         return new Declaration.Test(node , test);
      }

      public Declaration.Tag makeDeclarationTag(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Kind kind,  org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Type> types) {
         return new Declaration.Tag(node , tags, visibility, kind, name, types);
      }

      public Declaration.View makeDeclarationView(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Name view,  org.rascalmpl.ast.Name superType,  java.util.List<org.rascalmpl.ast.Alternative> alts) {
         return new Declaration.View(node , tags, visibility, view, superType, alts);
      }

      public LongLiteral.OctalLongLiteral makeLongLiteralOctalLongLiteral(INode node , org.rascalmpl.ast.OctalLongLiteral octalLong) {
         return new LongLiteral.OctalLongLiteral(node , octalLong);
      }

      public LongLiteral.DecimalLongLiteral makeLongLiteralDecimalLongLiteral(INode node , org.rascalmpl.ast.DecimalLongLiteral decimalLong) {
         return new LongLiteral.DecimalLongLiteral(node , decimalLong);
      }

      public LongLiteral.HexLongLiteral makeLongLiteralHexLongLiteral(INode node , org.rascalmpl.ast.HexLongLiteral hexLong) {
         return new LongLiteral.HexLongLiteral(node , hexLong);
      }

      public Sym.StartOfLine makeSymStartOfLine(INode node ) {
         return new Sym.StartOfLine(node );
      }

      public Sym.Nonterminal makeSymNonterminal(INode node , org.rascalmpl.ast.Nonterminal nonterminal) {
         return new Sym.Nonterminal(node , nonterminal);
      }

      public Sym.Optional makeSymOptional(INode node , org.rascalmpl.ast.Sym symbol) {
         return new Sym.Optional(node , symbol);
      }

      public Sym.Parameter makeSymParameter(INode node , org.rascalmpl.ast.Nonterminal nonterminal) {
         return new Sym.Parameter(node , nonterminal);
      }

      public Sym.CaseInsensitiveLiteral makeSymCaseInsensitiveLiteral(INode node , org.rascalmpl.ast.CaseInsensitiveStringConstant cistring) {
         return new Sym.CaseInsensitiveLiteral(node , cistring);
      }

      public Sym.CharacterClass makeSymCharacterClass(INode node , org.rascalmpl.ast.Class charClass) {
         return new Sym.CharacterClass(node , charClass);
      }

      public Sym.Labeled makeSymLabeled(INode node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.NonterminalLabel label) {
         return new Sym.Labeled(node , symbol, label);
      }

      public Sym.IterStar makeSymIterStar(INode node , org.rascalmpl.ast.Sym symbol) {
         return new Sym.IterStar(node , symbol);
      }

      public Sym.Parametrized makeSymParametrized(INode node , org.rascalmpl.ast.ParameterizedNonterminal pnonterminal,  java.util.List<org.rascalmpl.ast.Sym> parameters) {
         return new Sym.Parametrized(node , pnonterminal, parameters);
      }

      public Sym.Iter makeSymIter(INode node , org.rascalmpl.ast.Sym symbol) {
         return new Sym.Iter(node , symbol);
      }

      public Sym.EndOfLine makeSymEndOfLine(INode node ) {
         return new Sym.EndOfLine(node );
      }

      public Sym.IterStarSep makeSymIterStarSep(INode node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.StringConstant sep) {
         return new Sym.IterStarSep(node , symbol, sep);
      }

      public Sym.Literal makeSymLiteral(INode node , org.rascalmpl.ast.StringConstant string) {
         return new Sym.Literal(node , string);
      }

      public Sym.Column makeSymColumn(INode node , org.rascalmpl.ast.IntegerLiteral column) {
         return new Sym.Column(node , column);
      }

      public Sym.IterSep makeSymIterSep(INode node , org.rascalmpl.ast.Sym symbol,  org.rascalmpl.ast.StringConstant sep) {
         return new Sym.IterSep(node , symbol, sep);
      }

      public SyntaxDefinition.Language makeSyntaxDefinitionLanguage(INode node , org.rascalmpl.ast.Start start,  org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
         return new SyntaxDefinition.Language(node , start, defined, production);
      }

      public SyntaxDefinition.Layout makeSyntaxDefinitionLayout(INode node , org.rascalmpl.ast.Sym defined,  org.rascalmpl.ast.Prod production) {
         return new SyntaxDefinition.Layout(node , defined, production);
      }

      public Assoc.Right makeAssocRight(INode node ) {
         return new Assoc.Right(node );
      }

      public Assoc.NonAssociative makeAssocNonAssociative(INode node ) {
         return new Assoc.NonAssociative(node );
      }

      public Assoc.Left makeAssocLeft(INode node ) {
         return new Assoc.Left(node );
      }

      public Assoc.Associative makeAssocAssociative(INode node ) {
         return new Assoc.Associative(node );
      }

      public Renamings.Default makeRenamingsDefault(INode node , java.util.List<org.rascalmpl.ast.Renaming> renamings) {
         return new Renamings.Default(node , renamings);
      }

      public Formal.TypeName makeFormalTypeName(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
         return new Formal.TypeName(node , type, name);
      }

      public Class.Union makeClassUnion(INode node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
         return new Class.Union(node , lhs, rhs);
      }

      public Class.Difference makeClassDifference(INode node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
         return new Class.Difference(node , lhs, rhs);
      }

      public Class.SimpleCharclass makeClassSimpleCharclass(INode node , java.util.List<org.rascalmpl.ast.Range> ranges) {
         return new Class.SimpleCharclass(node , ranges);
      }

      public Class.Intersection makeClassIntersection(INode node , org.rascalmpl.ast.Class lhs,  org.rascalmpl.ast.Class rhs) {
         return new Class.Intersection(node , lhs, rhs);
      }

      public Class.Complement makeClassComplement(INode node , org.rascalmpl.ast.Class charClass) {
         return new Class.Complement(node , charClass);
      }

      public Class.Bracket makeClassBracket(INode node , org.rascalmpl.ast.Class charclass) {
         return new Class.Bracket(node , charclass);
      }

      public Bound.Empty makeBoundEmpty(INode node ) {
         return new Bound.Empty(node );
      }

      public Bound.Default makeBoundDefault(INode node , org.rascalmpl.ast.Expression expression) {
         return new Bound.Default(node , expression);
      }

      public PreModule.Default makePreModuleDefault(INode node , org.rascalmpl.ast.Header header) {
         return new PreModule.Default(node , header);
      }

      public NoElseMayFollow.Default makeNoElseMayFollowDefault(INode node ) {
         return new NoElseMayFollow.Default(node );
      }

      public Comprehension.Set makeComprehensionSet(INode node , java.util.List<org.rascalmpl.ast.Expression> results,  java.util.List<org.rascalmpl.ast.Expression> generators) {
         return new Comprehension.Set(node , results, generators);
      }

      public Comprehension.Map makeComprehensionMap(INode node , org.rascalmpl.ast.Expression from,  org.rascalmpl.ast.Expression to,  java.util.List<org.rascalmpl.ast.Expression> generators) {
         return new Comprehension.Map(node , from, to, generators);
      }

      public Comprehension.List makeComprehensionList(INode node , java.util.List<org.rascalmpl.ast.Expression> results,  java.util.List<org.rascalmpl.ast.Expression> generators) {
         return new Comprehension.List(node , results, generators);
      }

      public PathTail.Mid makePathTailMid(INode node , org.rascalmpl.ast.MidPathChars mid,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.PathTail tail) {
         return new PathTail.Mid(node , mid, expression, tail);
      }

      public PathTail.Post makePathTailPost(INode node , org.rascalmpl.ast.PostPathChars post) {
         return new PathTail.Post(node , post);
      }

      public Literal.Location makeLiteralLocation(INode node , org.rascalmpl.ast.LocationLiteral locationLiteral) {
         return new Literal.Location(node , locationLiteral);
      }

      public Literal.String makeLiteralString(INode node , org.rascalmpl.ast.StringLiteral stringLiteral) {
         return new Literal.String(node , stringLiteral);
      }

      public Literal.RegExp makeLiteralRegExp(INode node , org.rascalmpl.ast.RegExpLiteral regExpLiteral) {
         return new Literal.RegExp(node , regExpLiteral);
      }

      public Literal.Real makeLiteralReal(INode node , org.rascalmpl.ast.RealLiteral realLiteral) {
         return new Literal.Real(node , realLiteral);
      }

      public Literal.Boolean makeLiteralBoolean(INode node , org.rascalmpl.ast.BooleanLiteral booleanLiteral) {
         return new Literal.Boolean(node , booleanLiteral);
      }

      public Literal.DateTime makeLiteralDateTime(INode node , org.rascalmpl.ast.DateTimeLiteral dateTimeLiteral) {
         return new Literal.DateTime(node , dateTimeLiteral);
      }

      public Literal.Integer makeLiteralInteger(INode node , org.rascalmpl.ast.IntegerLiteral integerLiteral) {
         return new Literal.Integer(node , integerLiteral);
      }

      public Strategy.Outermost makeStrategyOutermost(INode node ) {
         return new Strategy.Outermost(node );
      }

      public Strategy.TopDownBreak makeStrategyTopDownBreak(INode node ) {
         return new Strategy.TopDownBreak(node );
      }

      public Strategy.Innermost makeStrategyInnermost(INode node ) {
         return new Strategy.Innermost(node );
      }

      public Strategy.BottomUpBreak makeStrategyBottomUpBreak(INode node ) {
         return new Strategy.BottomUpBreak(node );
      }

      public Strategy.BottomUp makeStrategyBottomUp(INode node ) {
         return new Strategy.BottomUp(node );
      }

      public Strategy.TopDown makeStrategyTopDown(INode node ) {
         return new Strategy.TopDown(node );
      }

      public DataTarget.Empty makeDataTargetEmpty(INode node ) {
         return new DataTarget.Empty(node );
      }

      public DataTarget.Labeled makeDataTargetLabeled(INode node , org.rascalmpl.ast.Name label) {
         return new DataTarget.Labeled(node , label);
      }

      public Case.PatternWithAction makeCasePatternWithAction(INode node , org.rascalmpl.ast.PatternWithAction patternWithAction) {
         return new Case.PatternWithAction(node , patternWithAction);
      }

      public Case.Default makeCaseDefault(INode node , org.rascalmpl.ast.Statement statement) {
         return new Case.Default(node , statement);
      }

      public Catch.Default makeCatchDefault(INode node , org.rascalmpl.ast.Statement body) {
         return new Catch.Default(node , body);
      }

      public Catch.Binding makeCatchBinding(INode node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Statement body) {
         return new Catch.Binding(node , pattern, body);
      }

      public Field.Name makeFieldName(INode node , org.rascalmpl.ast.Name fieldName) {
         return new Field.Name(node , fieldName);
      }

      public Field.Index makeFieldIndex(INode node , org.rascalmpl.ast.IntegerLiteral fieldIndex) {
         return new Field.Index(node , fieldIndex);
      }

      public TypeVar.Free makeTypeVarFree(INode node , org.rascalmpl.ast.Name name) {
         return new TypeVar.Free(node , name);
      }

      public TypeVar.Bounded makeTypeVarBounded(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Type bound) {
         return new TypeVar.Bounded(node , name, bound);
      }

      public Alternative.NamedType makeAlternativeNamedType(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Type type) {
         return new Alternative.NamedType(node , name, type);
      }

      public ModuleActuals.Default makeModuleActualsDefault(INode node , java.util.List<org.rascalmpl.ast.Type> types) {
         return new ModuleActuals.Default(node , types);
      }

      public Target.Empty makeTargetEmpty(INode node ) {
         return new Target.Empty(node );
      }

      public Target.Labeled makeTargetLabeled(INode node , org.rascalmpl.ast.Name name) {
         return new Target.Labeled(node , name);
      }

      public StringTemplate.IfThen makeStringTemplateIfThen(INode node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
         return new StringTemplate.IfThen(node , conditions, preStats, body, postStats);
      }

      public StringTemplate.IfThenElse makeStringTemplateIfThenElse(INode node , java.util.List<org.rascalmpl.ast.Expression> conditions,  java.util.List<org.rascalmpl.ast.Statement> preStatsThen,  org.rascalmpl.ast.StringMiddle thenString,  java.util.List<org.rascalmpl.ast.Statement> postStatsThen,  java.util.List<org.rascalmpl.ast.Statement> preStatsElse,  org.rascalmpl.ast.StringMiddle elseString,  java.util.List<org.rascalmpl.ast.Statement> postStatsElse) {
         return new StringTemplate.IfThenElse(node , conditions, preStatsThen, thenString, postStatsThen, preStatsElse, elseString, postStatsElse);
      }

      public StringTemplate.While makeStringTemplateWhile(INode node , org.rascalmpl.ast.Expression condition,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
         return new StringTemplate.While(node , condition, preStats, body, postStats);
      }

      public StringTemplate.DoWhile makeStringTemplateDoWhile(INode node , java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats,  org.rascalmpl.ast.Expression condition) {
         return new StringTemplate.DoWhile(node , preStats, body, postStats, condition);
      }

      public StringTemplate.For makeStringTemplateFor(INode node , java.util.List<org.rascalmpl.ast.Expression> generators,  java.util.List<org.rascalmpl.ast.Statement> preStats,  org.rascalmpl.ast.StringMiddle body,  java.util.List<org.rascalmpl.ast.Statement> postStats) {
         return new StringTemplate.For(node , generators, preStats, body, postStats);
      }

      public Visit.DefaultStrategy makeVisitDefaultStrategy(INode node , org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
         return new Visit.DefaultStrategy(node , subject, cases);
      }

      public Visit.GivenStrategy makeVisitGivenStrategy(INode node , org.rascalmpl.ast.Strategy strategy,  org.rascalmpl.ast.Expression subject,  java.util.List<org.rascalmpl.ast.Case> cases) {
         return new Visit.GivenStrategy(node , strategy, subject, cases);
      }

      public Visibility.Public makeVisibilityPublic(INode node ) {
         return new Visibility.Public(node );
      }

      public Visibility.Default makeVisibilityDefault(INode node ) {
         return new Visibility.Default(node );
      }

      public Visibility.Private makeVisibilityPrivate(INode node ) {
         return new Visibility.Private(node );
      }

      public Tags.Default makeTagsDefault(INode node , java.util.List<org.rascalmpl.ast.Tag> tags) {
         return new Tags.Default(node , tags);
      }

      public Kind.Module makeKindModule(INode node ) {
         return new Kind.Module(node );
      }

      public Kind.Rule makeKindRule(INode node ) {
         return new Kind.Rule(node );
      }

      public Kind.Variable makeKindVariable(INode node ) {
         return new Kind.Variable(node );
      }

      public Kind.Anno makeKindAnno(INode node ) {
         return new Kind.Anno(node );
      }

      public Kind.Function makeKindFunction(INode node ) {
         return new Kind.Function(node );
      }

      public Kind.Data makeKindData(INode node ) {
         return new Kind.Data(node );
      }

      public Kind.Tag makeKindTag(INode node ) {
         return new Kind.Tag(node );
      }

      public Kind.View makeKindView(INode node ) {
         return new Kind.View(node );
      }

      public Kind.Alias makeKindAlias(INode node ) {
         return new Kind.Alias(node );
      }

      public Kind.All makeKindAll(INode node ) {
         return new Kind.All(node );
      }

      public FunctionType.TypeArguments makeFunctionTypeTypeArguments(INode node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.TypeArg> arguments) {
         return new FunctionType.TypeArguments(node , type, arguments);
      }

      public Prod.All makeProdAll(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
         return new Prod.All(node , lhs, rhs);
      }

      public Prod.Follow makeProdFollow(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
         return new Prod.Follow(node , lhs, rhs);
      }

      public Prod.First makeProdFirst(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
         return new Prod.First(node , lhs, rhs);
      }

      public Prod.Unlabeled makeProdUnlabeled(INode node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  java.util.List<org.rascalmpl.ast.Sym> args) {
         return new Prod.Unlabeled(node , modifiers, args);
      }

      public Prod.Labeled makeProdLabeled(INode node , java.util.List<org.rascalmpl.ast.ProdModifier> modifiers,  org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Sym> args) {
         return new Prod.Labeled(node , modifiers, name, args);
      }

      public Prod.Action makeProdAction(INode node , org.rascalmpl.ast.Prod prod,  org.rascalmpl.ast.LanguageAction action) {
         return new Prod.Action(node , prod, action);
      }

      public Prod.Reference makeProdReference(INode node , org.rascalmpl.ast.Name referenced) {
         return new Prod.Reference(node , referenced);
      }

      public Prod.Reject makeProdReject(INode node , org.rascalmpl.ast.Prod lhs,  org.rascalmpl.ast.Prod rhs) {
         return new Prod.Reject(node , lhs, rhs);
      }

      public Prod.Others makeProdOthers(INode node ) {
         return new Prod.Others(node );
      }

      public Prod.AssociativityGroup makeProdAssociativityGroup(INode node , org.rascalmpl.ast.Assoc associativity,  org.rascalmpl.ast.Prod group) {
         return new Prod.AssociativityGroup(node , associativity, group);
      }

      public LocalVariableDeclaration.Default makeLocalVariableDeclarationDefault(INode node , org.rascalmpl.ast.Declarator declarator) {
         return new LocalVariableDeclaration.Default(node , declarator);
      }

      public LocalVariableDeclaration.Dynamic makeLocalVariableDeclarationDynamic(INode node , org.rascalmpl.ast.Declarator declarator) {
         return new LocalVariableDeclaration.Dynamic(node , declarator);
      }

      public BasicType.Map makeBasicTypeMap(INode node ) {
         return new BasicType.Map(node );
      }

      public BasicType.Relation makeBasicTypeRelation(INode node ) {
         return new BasicType.Relation(node );
      }

      public BasicType.Real makeBasicTypeReal(INode node ) {
         return new BasicType.Real(node );
      }

      public BasicType.List makeBasicTypeList(INode node ) {
         return new BasicType.List(node );
      }

      public BasicType.Lex makeBasicTypeLex(INode node ) {
         return new BasicType.Lex(node );
      }

      public BasicType.ReifiedAdt makeBasicTypeReifiedAdt(INode node ) {
         return new BasicType.ReifiedAdt(node );
      }

      public BasicType.ReifiedReifiedType makeBasicTypeReifiedReifiedType(INode node ) {
         return new BasicType.ReifiedReifiedType(node );
      }

      public BasicType.DateTime makeBasicTypeDateTime(INode node ) {
         return new BasicType.DateTime(node );
      }

      public BasicType.Void makeBasicTypeVoid(INode node ) {
         return new BasicType.Void(node );
      }

      public BasicType.ReifiedTypeParameter makeBasicTypeReifiedTypeParameter(INode node ) {
         return new BasicType.ReifiedTypeParameter(node );
      }

      public BasicType.ReifiedFunction makeBasicTypeReifiedFunction(INode node ) {
         return new BasicType.ReifiedFunction(node );
      }

      public BasicType.String makeBasicTypeString(INode node ) {
         return new BasicType.String(node );
      }

      public BasicType.ReifiedNonTerminal makeBasicTypeReifiedNonTerminal(INode node ) {
         return new BasicType.ReifiedNonTerminal(node );
      }

      public BasicType.Value makeBasicTypeValue(INode node ) {
         return new BasicType.Value(node );
      }

      public BasicType.ReifiedType makeBasicTypeReifiedType(INode node ) {
         return new BasicType.ReifiedType(node );
      }

      public BasicType.Int makeBasicTypeInt(INode node ) {
         return new BasicType.Int(node );
      }

      public BasicType.Bag makeBasicTypeBag(INode node ) {
         return new BasicType.Bag(node );
      }

      public BasicType.Tuple makeBasicTypeTuple(INode node ) {
         return new BasicType.Tuple(node );
      }

      public BasicType.Bool makeBasicTypeBool(INode node ) {
         return new BasicType.Bool(node );
      }

      public BasicType.Num makeBasicTypeNum(INode node ) {
         return new BasicType.Num(node );
      }

      public BasicType.Loc makeBasicTypeLoc(INode node ) {
         return new BasicType.Loc(node );
      }

      public BasicType.Set makeBasicTypeSet(INode node ) {
         return new BasicType.Set(node );
      }

      public BasicType.ReifiedConstructor makeBasicTypeReifiedConstructor(INode node ) {
         return new BasicType.ReifiedConstructor(node );
      }

      public BasicType.Node makeBasicTypeNode(INode node ) {
         return new BasicType.Node(node );
      }

      public ProtocolTail.Post makeProtocolTailPost(INode node , org.rascalmpl.ast.PostProtocolChars post) {
         return new ProtocolTail.Post(node , post);
      }

      public ProtocolTail.Mid makeProtocolTailMid(INode node , org.rascalmpl.ast.MidProtocolChars mid,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.ProtocolTail tail) {
         return new ProtocolTail.Mid(node , mid, expression, tail);
      }

      public QualifiedName.Default makeQualifiedNameDefault(INode node , java.util.List<org.rascalmpl.ast.Name> names) {
         return new QualifiedName.Default(node , names);
      }

      public Start.Absent makeStartAbsent(INode node ) {
         return new Start.Absent(node );
      }

      public Start.Present makeStartPresent(INode node ) {
         return new Start.Present(node );
      }

      public Parameters.VarArgs makeParametersVarArgs(INode node , org.rascalmpl.ast.Formals formals) {
         return new Parameters.VarArgs(node , formals);
      }

      public Parameters.Default makeParametersDefault(INode node , org.rascalmpl.ast.Formals formals) {
         return new Parameters.Default(node , formals);
      }

      public Mapping_Expression.Default makeMapping_ExpressionDefault(INode node , org.rascalmpl.ast.Expression from,  org.rascalmpl.ast.Expression to) {
         return new Mapping_Expression.Default(node , from, to);
      }

      public Replacement.Unconditional makeReplacementUnconditional(INode node , org.rascalmpl.ast.Expression replacementExpression) {
         return new Replacement.Unconditional(node , replacementExpression);
      }

      public Replacement.Conditional makeReplacementConditional(INode node , org.rascalmpl.ast.Expression replacementExpression,  java.util.List<org.rascalmpl.ast.Expression> conditions) {
         return new Replacement.Conditional(node , replacementExpression, conditions);
      }

      public Assignment.Addition makeAssignmentAddition(INode node ) {
         return new Assignment.Addition(node );
      }

      public Assignment.IfDefined makeAssignmentIfDefined(INode node ) {
         return new Assignment.IfDefined(node );
      }

      public Assignment.Division makeAssignmentDivision(INode node ) {
         return new Assignment.Division(node );
      }

      public Assignment.Product makeAssignmentProduct(INode node ) {
         return new Assignment.Product(node );
      }

      public Assignment.Intersection makeAssignmentIntersection(INode node ) {
         return new Assignment.Intersection(node );
      }

      public Assignment.Subtraction makeAssignmentSubtraction(INode node ) {
         return new Assignment.Subtraction(node );
      }

      public Assignment.Default makeAssignmentDefault(INode node ) {
         return new Assignment.Default(node );
      }

      public Header.Parameters makeHeaderParameters(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleParameters params,  java.util.List<org.rascalmpl.ast.Import> imports) {
         return new Header.Parameters(node , tags, name, params, imports);
      }

      public Header.Default makeHeaderDefault(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.QualifiedName name,  java.util.List<org.rascalmpl.ast.Import> imports) {
         return new Header.Default(node , tags, name, imports);
      }

      public LanguageAction.Build makeLanguageActionBuild(INode node , org.rascalmpl.ast.Expression expression) {
         return new LanguageAction.Build(node , expression);
      }

      public LanguageAction.Action makeLanguageActionAction(INode node , java.util.List<org.rascalmpl.ast.Statement> statements) {
         return new LanguageAction.Action(node , statements);
      }

      public IntegerLiteral.OctalIntegerLiteral makeIntegerLiteralOctalIntegerLiteral(INode node , org.rascalmpl.ast.OctalIntegerLiteral octal) {
         return new IntegerLiteral.OctalIntegerLiteral(node , octal);
      }

      public IntegerLiteral.HexIntegerLiteral makeIntegerLiteralHexIntegerLiteral(INode node , org.rascalmpl.ast.HexIntegerLiteral hex) {
         return new IntegerLiteral.HexIntegerLiteral(node , hex);
      }

      public IntegerLiteral.DecimalIntegerLiteral makeIntegerLiteralDecimalIntegerLiteral(INode node , org.rascalmpl.ast.DecimalIntegerLiteral decimal) {
         return new IntegerLiteral.DecimalIntegerLiteral(node , decimal);
      }

      public PatternWithAction.Replacing makePatternWithActionReplacing(INode node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Replacement replacement) {
         return new PatternWithAction.Replacing(node , pattern, replacement);
      }

      public PatternWithAction.Arbitrary makePatternWithActionArbitrary(INode node , org.rascalmpl.ast.Expression pattern,  org.rascalmpl.ast.Statement statement) {
         return new PatternWithAction.Arbitrary(node , pattern, statement);
      }

      public Import.Extend makeImportExtend(INode node , org.rascalmpl.ast.ImportedModule module) {
         return new Import.Extend(node , module);
      }

      public Import.Default makeImportDefault(INode node , org.rascalmpl.ast.ImportedModule module) {
         return new Import.Default(node , module);
      }

      public Import.Syntax makeImportSyntax(INode node , org.rascalmpl.ast.SyntaxDefinition syntax) {
         return new Import.Syntax(node , syntax);
      }

      public FunctionModifier.Java makeFunctionModifierJava(INode node ) {
         return new FunctionModifier.Java(node );
      }

      public ShellCommand.Unimport makeShellCommandUnimport(INode node , org.rascalmpl.ast.QualifiedName name) {
         return new ShellCommand.Unimport(node , name);
      }

      public ShellCommand.Quit makeShellCommandQuit(INode node ) {
         return new ShellCommand.Quit(node );
      }

      public ShellCommand.Edit makeShellCommandEdit(INode node , org.rascalmpl.ast.QualifiedName name) {
         return new ShellCommand.Edit(node , name);
      }

      public ShellCommand.ListModules makeShellCommandListModules(INode node ) {
         return new ShellCommand.ListModules(node );
      }

      public ShellCommand.History makeShellCommandHistory(INode node ) {
         return new ShellCommand.History(node );
      }

      public ShellCommand.ListDeclarations makeShellCommandListDeclarations(INode node ) {
         return new ShellCommand.ListDeclarations(node );
      }

      public ShellCommand.Help makeShellCommandHelp(INode node ) {
         return new ShellCommand.Help(node );
      }

      public ShellCommand.SetOption makeShellCommandSetOption(INode node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.Expression expression) {
         return new ShellCommand.SetOption(node , name, expression);
      }

      public ShellCommand.Undeclare makeShellCommandUndeclare(INode node , org.rascalmpl.ast.QualifiedName name) {
         return new ShellCommand.Undeclare(node , name);
      }

      public ShellCommand.Test makeShellCommandTest(INode node ) {
         return new ShellCommand.Test(node );
      }

      public Declarator.Default makeDeclaratorDefault(INode node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.Variable> variables) {
         return new Declarator.Default(node , type, variables);
      }

      public Variable.Initialized makeVariableInitialized(INode node , org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Expression initial) {
         return new Variable.Initialized(node , name, initial);
      }

      public Variable.UnInitialized makeVariableUnInitialized(INode node , org.rascalmpl.ast.Name name) {
         return new Variable.UnInitialized(node , name);
      }

      public StringMiddle.Interpolated makeStringMiddleInterpolated(INode node , org.rascalmpl.ast.MidStringChars mid,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.StringMiddle tail) {
         return new StringMiddle.Interpolated(node , mid, expression, tail);
      }

      public StringMiddle.Template makeStringMiddleTemplate(INode node , org.rascalmpl.ast.MidStringChars mid,  org.rascalmpl.ast.StringTemplate template,  org.rascalmpl.ast.StringMiddle tail) {
         return new StringMiddle.Template(node , mid, template, tail);
      }

      public StringMiddle.Mid makeStringMiddleMid(INode node , org.rascalmpl.ast.MidStringChars mid) {
         return new StringMiddle.Mid(node , mid);
      }

      public Signature.WithThrows makeSignatureWithThrows(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.FunctionModifiers modifiers,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Parameters parameters,  java.util.List<org.rascalmpl.ast.Type> exceptions) {
         return new Signature.WithThrows(node , type, modifiers, name, parameters, exceptions);
      }

      public Signature.NoThrows makeSignatureNoThrows(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.FunctionModifiers modifiers,  org.rascalmpl.ast.Name name,  org.rascalmpl.ast.Parameters parameters) {
         return new Signature.NoThrows(node , type, modifiers, name, parameters);
      }

      public TypeArg.Named makeTypeArgNamed(INode node , org.rascalmpl.ast.Type type,  org.rascalmpl.ast.Name name) {
         return new TypeArg.Named(node , type, name);
      }

      public TypeArg.Default makeTypeArgDefault(INode node , org.rascalmpl.ast.Type type) {
         return new TypeArg.Default(node , type);
      }

      public FunctionDeclaration.Abstract makeFunctionDeclarationAbstract(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Signature signature) {
         return new FunctionDeclaration.Abstract(node , tags, visibility, signature);
      }

      public FunctionDeclaration.Default makeFunctionDeclarationDefault(INode node , org.rascalmpl.ast.Tags tags,  org.rascalmpl.ast.Visibility visibility,  org.rascalmpl.ast.Signature signature,  org.rascalmpl.ast.FunctionBody body) {
         return new FunctionDeclaration.Default(node , tags, visibility, signature, body);
      }

      public PathPart.NonInterpolated makePathPartNonInterpolated(INode node , org.rascalmpl.ast.PathChars pathChars) {
         return new PathPart.NonInterpolated(node , pathChars);
      }

      public PathPart.Interpolated makePathPartInterpolated(INode node , org.rascalmpl.ast.PrePathChars pre,  org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.PathTail tail) {
         return new PathPart.Interpolated(node , pre, expression, tail);
      }

      public ImportedModule.Renamings makeImportedModuleRenamings(INode node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.Renamings renamings) {
         return new ImportedModule.Renamings(node , name, renamings);
      }

      public ImportedModule.ActualsRenaming makeImportedModuleActualsRenaming(INode node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleActuals actuals,  org.rascalmpl.ast.Renamings renamings) {
         return new ImportedModule.ActualsRenaming(node , name, actuals, renamings);
      }

      public ImportedModule.Default makeImportedModuleDefault(INode node , org.rascalmpl.ast.QualifiedName name) {
         return new ImportedModule.Default(node , name);
      }

      public ImportedModule.Actuals makeImportedModuleActuals(INode node , org.rascalmpl.ast.QualifiedName name,  org.rascalmpl.ast.ModuleActuals actuals) {
         return new ImportedModule.Actuals(node , name, actuals);
      }

      public Statement.VariableDeclaration makeStatementVariableDeclaration(INode node , org.rascalmpl.ast.LocalVariableDeclaration declaration) {
         return new Statement.VariableDeclaration(node , declaration);
      }

      public Statement.GlobalDirective makeStatementGlobalDirective(INode node , org.rascalmpl.ast.Type type,  java.util.List<org.rascalmpl.ast.QualifiedName> names) {
         return new Statement.GlobalDirective(node , type, names);
      }

      public Statement.For makeStatementFor(INode node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> generators,  org.rascalmpl.ast.Statement body) {
         return new Statement.For(node , label, generators, body);
      }

      public Statement.Solve makeStatementSolve(INode node , java.util.List<org.rascalmpl.ast.QualifiedName> variables,  org.rascalmpl.ast.Bound bound,  org.rascalmpl.ast.Statement body) {
         return new Statement.Solve(node , variables, bound, body);
      }

      public Statement.While makeStatementWhile(INode node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement body) {
         return new Statement.While(node , label, conditions, body);
      }

      public Statement.AssertWithMessage makeStatementAssertWithMessage(INode node , org.rascalmpl.ast.Expression expression,  org.rascalmpl.ast.Expression message) {
         return new Statement.AssertWithMessage(node , expression, message);
      }

      public Statement.Expression makeStatementExpression(INode node , org.rascalmpl.ast.Expression expression) {
         return new Statement.Expression(node , expression);
      }

      public Statement.DoWhile makeStatementDoWhile(INode node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Statement body,  org.rascalmpl.ast.Expression condition) {
         return new Statement.DoWhile(node , label, body, condition);
      }

      public Statement.Assignment makeStatementAssignment(INode node , org.rascalmpl.ast.Assignable assignable,  org.rascalmpl.ast.Assignment operator,  org.rascalmpl.ast.Statement statement) {
         return new Statement.Assignment(node , assignable, operator, statement);
      }

      public Statement.Return makeStatementReturn(INode node , org.rascalmpl.ast.Statement statement) {
         return new Statement.Return(node , statement);
      }

      public Statement.Fail makeStatementFail(INode node , org.rascalmpl.ast.Target target) {
         return new Statement.Fail(node , target);
      }

      public Statement.Break makeStatementBreak(INode node , org.rascalmpl.ast.Target target) {
         return new Statement.Break(node , target);
      }

      public Statement.IfThenElse makeStatementIfThenElse(INode node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement thenStatement,  org.rascalmpl.ast.Statement elseStatement) {
         return new Statement.IfThenElse(node , label, conditions, thenStatement, elseStatement);
      }

      public Statement.IfThen makeStatementIfThen(INode node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Expression> conditions,  org.rascalmpl.ast.Statement thenStatement,  org.rascalmpl.ast.NoElseMayFollow noElseMayFollow) {
         return new Statement.IfThen(node , label, conditions, thenStatement, noElseMayFollow);
      }

      public Statement.FunctionDeclaration makeStatementFunctionDeclaration(INode node , org.rascalmpl.ast.FunctionDeclaration functionDeclaration) {
         return new Statement.FunctionDeclaration(node , functionDeclaration);
      }

      public Statement.Switch makeStatementSwitch(INode node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Expression expression,  java.util.List<org.rascalmpl.ast.Case> cases) {
         return new Statement.Switch(node , label, expression, cases);
      }

      public Statement.Append makeStatementAppend(INode node , org.rascalmpl.ast.DataTarget dataTarget,  org.rascalmpl.ast.Statement statement) {
         return new Statement.Append(node , dataTarget, statement);
      }

      public Statement.Insert makeStatementInsert(INode node , org.rascalmpl.ast.DataTarget dataTarget,  org.rascalmpl.ast.Statement statement) {
         return new Statement.Insert(node , dataTarget, statement);
      }

      public Statement.Throw makeStatementThrow(INode node , org.rascalmpl.ast.Statement statement) {
         return new Statement.Throw(node , statement);
      }

      public Statement.NonEmptyBlock makeStatementNonEmptyBlock(INode node , org.rascalmpl.ast.Label label,  java.util.List<org.rascalmpl.ast.Statement> statements) {
         return new Statement.NonEmptyBlock(node , label, statements);
      }

      public Statement.TryFinally makeStatementTryFinally(INode node , org.rascalmpl.ast.Statement body,  java.util.List<org.rascalmpl.ast.Catch> handlers,  org.rascalmpl.ast.Statement finallyBody) {
         return new Statement.TryFinally(node , body, handlers, finallyBody);
      }

      public Statement.Assert makeStatementAssert(INode node , org.rascalmpl.ast.Expression expression) {
         return new Statement.Assert(node , expression);
      }

      public Statement.EmptyStatement makeStatementEmptyStatement(INode node ) {
         return new Statement.EmptyStatement(node );
      }

      public Statement.Try makeStatementTry(INode node , org.rascalmpl.ast.Statement body,  java.util.List<org.rascalmpl.ast.Catch> handlers) {
         return new Statement.Try(node , body, handlers);
      }

      public Statement.Visit makeStatementVisit(INode node , org.rascalmpl.ast.Label label,  org.rascalmpl.ast.Visit visit) {
         return new Statement.Visit(node , label, visit);
      }

      public Statement.Continue makeStatementContinue(INode node , org.rascalmpl.ast.Target target) {
         return new Statement.Continue(node , target);
      }

      public Assignable.Tuple makeAssignableTuple(INode node , java.util.List<org.rascalmpl.ast.Assignable> elements) {
         return new Assignable.Tuple(node , elements);
      }

      public Assignable.Variable makeAssignableVariable(INode node , org.rascalmpl.ast.QualifiedName qualifiedName) {
         return new Assignable.Variable(node , qualifiedName);
      }

      public Assignable.IfDefinedOrDefault makeAssignableIfDefinedOrDefault(INode node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Expression defaultExpression) {
         return new Assignable.IfDefinedOrDefault(node , receiver, defaultExpression);
      }

      public Assignable.Subscript makeAssignableSubscript(INode node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Expression subscript) {
         return new Assignable.Subscript(node , receiver, subscript);
      }

      public Assignable.Bracket makeAssignableBracket(INode node , org.rascalmpl.ast.Assignable arg) {
         return new Assignable.Bracket(node , arg);
      }

      public Assignable.FieldAccess makeAssignableFieldAccess(INode node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Name field) {
         return new Assignable.FieldAccess(node , receiver, field);
      }

      public Assignable.Constructor makeAssignableConstructor(INode node , org.rascalmpl.ast.Name name,  java.util.List<org.rascalmpl.ast.Assignable> arguments) {
         return new Assignable.Constructor(node , name, arguments);
      }

      public Assignable.Annotation makeAssignableAnnotation(INode node , org.rascalmpl.ast.Assignable receiver,  org.rascalmpl.ast.Name annotation) {
         return new Assignable.Annotation(node , receiver, annotation);
      }



  public RegExpLiteral.Lexical makeRegExpLiteralLexical(INode node, String string) {
    return new RegExpLiteral.Lexical(node, string); 
  }

  public PreProtocolChars.Lexical makePreProtocolCharsLexical(INode node, String string) {
    return new PreProtocolChars.Lexical(node, string); 
  }

  public NamedRegExp.Lexical makeNamedRegExpLexical(INode node, String string) {
    return new NamedRegExp.Lexical(node, string); 
  }

  public OctalEscapeSequence.Lexical makeOctalEscapeSequenceLexical(INode node, String string) {
    return new OctalEscapeSequence.Lexical(node, string); 
  }

  public PostStringChars.Lexical makePostStringCharsLexical(INode node, String string) {
    return new PostStringChars.Lexical(node, string); 
  }

  public HexIntegerLiteral.Lexical makeHexIntegerLiteralLexical(INode node, String string) {
    return new HexIntegerLiteral.Lexical(node, string); 
  }

  public OctalLongLiteral.Lexical makeOctalLongLiteralLexical(INode node, String string) {
    return new OctalLongLiteral.Lexical(node, string); 
  }

  public Char.Lexical makeCharLexical(INode node, String string) {
    return new Char.Lexical(node, string); 
  }

  public PrePathChars.Lexical makePrePathCharsLexical(INode node, String string) {
    return new PrePathChars.Lexical(node, string); 
  }

  public MidPathChars.Lexical makeMidPathCharsLexical(INode node, String string) {
    return new MidPathChars.Lexical(node, string); 
  }

  public PostProtocolChars.Lexical makePostProtocolCharsLexical(INode node, String string) {
    return new PostProtocolChars.Lexical(node, string); 
  }

  public NonterminalLabel.Lexical makeNonterminalLabelLexical(INode node, String string) {
    return new NonterminalLabel.Lexical(node, string); 
  }

  public DecimalIntegerLiteral.Lexical makeDecimalIntegerLiteralLexical(INode node, String string) {
    return new DecimalIntegerLiteral.Lexical(node, string); 
  }

  public TagString.Lexical makeTagStringLexical(INode node, String string) {
    return new TagString.Lexical(node, string); 
  }

  public Word.Lexical makeWordLexical(INode node, String string) {
    return new Word.Lexical(node, string); 
  }

  public LAYOUT.Lexical makeLAYOUTLexical(INode node, String string) {
    return new LAYOUT.Lexical(node, string); 
  }

  public Nonterminal.Lexical makeNonterminalLexical(INode node, String string) {
    return new Nonterminal.Lexical(node, string); 
  }

  public CommentChar.Lexical makeCommentCharLexical(INode node, String string) {
    return new CommentChar.Lexical(node, string); 
  }

  public Comment.Lexical makeCommentLexical(INode node, String string) {
    return new Comment.Lexical(node, string); 
  }

  public StrChar.Lexical makeStrCharLexical(INode node, String string) {
    return new StrChar.Lexical(node, string); 
  }

  public ProtocolChars.Lexical makeProtocolCharsLexical(INode node, String string) {
    return new ProtocolChars.Lexical(node, string); 
  }

  public MidStringChars.Lexical makeMidStringCharsLexical(INode node, String string) {
    return new MidStringChars.Lexical(node, string); 
  }

  public RegExpModifier.Lexical makeRegExpModifierLexical(INode node, String string) {
    return new RegExpModifier.Lexical(node, string); 
  }

  public EscapedName.Lexical makeEscapedNameLexical(INode node, String string) {
    return new EscapedName.Lexical(node, string); 
  }

  public RegExp.Lexical makeRegExpLexical(INode node, String string) {
    return new RegExp.Lexical(node, string); 
  }

  public SingleQuotedStrChar.Lexical makeSingleQuotedStrCharLexical(INode node, String string) {
    return new SingleQuotedStrChar.Lexical(node, string); 
  }

  public RealLiteral.Lexical makeRealLiteralLexical(INode node, String string) {
    return new RealLiteral.Lexical(node, string); 
  }

  public DatePart.Lexical makeDatePartLexical(INode node, String string) {
    return new DatePart.Lexical(node, string); 
  }

  public StringConstant.Lexical makeStringConstantLexical(INode node, String string) {
    return new StringConstant.Lexical(node, string); 
  }

  public ParameterizedNonterminal.Lexical makeParameterizedNonterminalLexical(INode node, String string) {
    return new ParameterizedNonterminal.Lexical(node, string); 
  }

  public TagChar.Lexical makeTagCharLexical(INode node, String string) {
    return new TagChar.Lexical(node, string); 
  }

  public JustTime.Lexical makeJustTimeLexical(INode node, String string) {
    return new JustTime.Lexical(node, string); 
  }

  public StringCharacter.Lexical makeStringCharacterLexical(INode node, String string) {
    return new StringCharacter.Lexical(node, string); 
  }

  public URLChars.Lexical makeURLCharsLexical(INode node, String string) {
    return new URLChars.Lexical(node, string); 
  }

  public TimeZonePart.Lexical makeTimeZonePartLexical(INode node, String string) {
    return new TimeZonePart.Lexical(node, string); 
  }

  public PreStringChars.Lexical makePreStringCharsLexical(INode node, String string) {
    return new PreStringChars.Lexical(node, string); 
  }

  public Backslash.Lexical makeBackslashLexical(INode node, String string) {
    return new Backslash.Lexical(node, string); 
  }

  public CaseInsensitiveStringConstant.Lexical makeCaseInsensitiveStringConstantLexical(INode node, String string) {
    return new CaseInsensitiveStringConstant.Lexical(node, string); 
  }

  public ShortChar.Lexical makeShortCharLexical(INode node, String string) {
    return new ShortChar.Lexical(node, string); 
  }

  public NamedBackslash.Lexical makeNamedBackslashLexical(INode node, String string) {
    return new NamedBackslash.Lexical(node, string); 
  }

  public MidProtocolChars.Lexical makeMidProtocolCharsLexical(INode node, String string) {
    return new MidProtocolChars.Lexical(node, string); 
  }

  public NumChar.Lexical makeNumCharLexical(INode node, String string) {
    return new NumChar.Lexical(node, string); 
  }

  public JustDate.Lexical makeJustDateLexical(INode node, String string) {
    return new JustDate.Lexical(node, string); 
  }

  public PostPathChars.Lexical makePostPathCharsLexical(INode node, String string) {
    return new PostPathChars.Lexical(node, string); 
  }

  public TimePartNoTZ.Lexical makeTimePartNoTZLexical(INode node, String string) {
    return new TimePartNoTZ.Lexical(node, string); 
  }

  public DecimalLongLiteral.Lexical makeDecimalLongLiteralLexical(INode node, String string) {
    return new DecimalLongLiteral.Lexical(node, string); 
  }

  public SingleQuotedStrCon.Lexical makeSingleQuotedStrConLexical(INode node, String string) {
    return new SingleQuotedStrCon.Lexical(node, string); 
  }

  public Name.Lexical makeNameLexical(INode node, String string) {
    return new Name.Lexical(node, string); 
  }

  public StrCon.Lexical makeStrConLexical(INode node, String string) {
    return new StrCon.Lexical(node, string); 
  }

  public BooleanLiteral.Lexical makeBooleanLiteralLexical(INode node, String string) {
    return new BooleanLiteral.Lexical(node, string); 
  }

  public DateAndTime.Lexical makeDateAndTimeLexical(INode node, String string) {
    return new DateAndTime.Lexical(node, string); 
  }

  public Asterisk.Lexical makeAsteriskLexical(INode node, String string) {
    return new Asterisk.Lexical(node, string); 
  }

  public UnicodeEscape.Lexical makeUnicodeEscapeLexical(INode node, String string) {
    return new UnicodeEscape.Lexical(node, string); 
  }

  public OctalIntegerLiteral.Lexical makeOctalIntegerLiteralLexical(INode node, String string) {
    return new OctalIntegerLiteral.Lexical(node, string); 
  }

  public HexLongLiteral.Lexical makeHexLongLiteralLexical(INode node, String string) {
    return new HexLongLiteral.Lexical(node, string); 
  }

  public PathChars.Lexical makePathCharsLexical(INode node, String string) {
    return new PathChars.Lexical(node, string); 
  }



  public RegExpLiteral.Ambiguity makeRegExpLiteralAmbiguity(INode node, java.util.List<RegExpLiteral> alternatives) {
    return new RegExpLiteral.Ambiguity(node, alternatives);
  }

  public LocationLiteral.Ambiguity makeLocationLiteralAmbiguity(INode node, java.util.List<LocationLiteral> alternatives) {
    return new LocationLiteral.Ambiguity(node, alternatives);
  }

  public Tag.Ambiguity makeTagAmbiguity(INode node, java.util.List<Tag> alternatives) {
    return new Tag.Ambiguity(node, alternatives);
  }

  public PreProtocolChars.Ambiguity makePreProtocolCharsAmbiguity(INode node, java.util.List<PreProtocolChars> alternatives) {
    return new PreProtocolChars.Ambiguity(node, alternatives);
  }

  public NamedRegExp.Ambiguity makeNamedRegExpAmbiguity(INode node, java.util.List<NamedRegExp> alternatives) {
    return new NamedRegExp.Ambiguity(node, alternatives);
  }

  public OctalEscapeSequence.Ambiguity makeOctalEscapeSequenceAmbiguity(INode node, java.util.List<OctalEscapeSequence> alternatives) {
    return new OctalEscapeSequence.Ambiguity(node, alternatives);
  }

  public PostStringChars.Ambiguity makePostStringCharsAmbiguity(INode node, java.util.List<PostStringChars> alternatives) {
    return new PostStringChars.Ambiguity(node, alternatives);
  }

  public HexIntegerLiteral.Ambiguity makeHexIntegerLiteralAmbiguity(INode node, java.util.List<HexIntegerLiteral> alternatives) {
    return new HexIntegerLiteral.Ambiguity(node, alternatives);
  }

  public UserType.Ambiguity makeUserTypeAmbiguity(INode node, java.util.List<UserType> alternatives) {
    return new UserType.Ambiguity(node, alternatives);
  }

  public OctalLongLiteral.Ambiguity makeOctalLongLiteralAmbiguity(INode node, java.util.List<OctalLongLiteral> alternatives) {
    return new OctalLongLiteral.Ambiguity(node, alternatives);
  }

  public Range.Ambiguity makeRangeAmbiguity(INode node, java.util.List<Range> alternatives) {
    return new Range.Ambiguity(node, alternatives);
  }

  public Char.Ambiguity makeCharAmbiguity(INode node, java.util.List<Char> alternatives) {
    return new Char.Ambiguity(node, alternatives);
  }

  public PrePathChars.Ambiguity makePrePathCharsAmbiguity(INode node, java.util.List<PrePathChars> alternatives) {
    return new PrePathChars.Ambiguity(node, alternatives);
  }

  public StructuredType.Ambiguity makeStructuredTypeAmbiguity(INode node, java.util.List<StructuredType> alternatives) {
    return new StructuredType.Ambiguity(node, alternatives);
  }

  public ProdModifier.Ambiguity makeProdModifierAmbiguity(INode node, java.util.List<ProdModifier> alternatives) {
    return new ProdModifier.Ambiguity(node, alternatives);
  }

  public MidPathChars.Ambiguity makeMidPathCharsAmbiguity(INode node, java.util.List<MidPathChars> alternatives) {
    return new MidPathChars.Ambiguity(node, alternatives);
  }

  public DataTypeSelector.Ambiguity makeDataTypeSelectorAmbiguity(INode node, java.util.List<DataTypeSelector> alternatives) {
    return new DataTypeSelector.Ambiguity(node, alternatives);
  }

  public FunctionBody.Ambiguity makeFunctionBodyAmbiguity(INode node, java.util.List<FunctionBody> alternatives) {
    return new FunctionBody.Ambiguity(node, alternatives);
  }

  public Formals.Ambiguity makeFormalsAmbiguity(INode node, java.util.List<Formals> alternatives) {
    return new Formals.Ambiguity(node, alternatives);
  }

  public Renaming.Ambiguity makeRenamingAmbiguity(INode node, java.util.List<Renaming> alternatives) {
    return new Renaming.Ambiguity(node, alternatives);
  }

  public StringLiteral.Ambiguity makeStringLiteralAmbiguity(INode node, java.util.List<StringLiteral> alternatives) {
    return new StringLiteral.Ambiguity(node, alternatives);
  }

  public Body.Ambiguity makeBodyAmbiguity(INode node, java.util.List<Body> alternatives) {
    return new Body.Ambiguity(node, alternatives);
  }

  public PostProtocolChars.Ambiguity makePostProtocolCharsAmbiguity(INode node, java.util.List<PostProtocolChars> alternatives) {
    return new PostProtocolChars.Ambiguity(node, alternatives);
  }

  public NonterminalLabel.Ambiguity makeNonterminalLabelAmbiguity(INode node, java.util.List<NonterminalLabel> alternatives) {
    return new NonterminalLabel.Ambiguity(node, alternatives);
  }

  public Variant.Ambiguity makeVariantAmbiguity(INode node, java.util.List<Variant> alternatives) {
    return new Variant.Ambiguity(node, alternatives);
  }

  public StringTail.Ambiguity makeStringTailAmbiguity(INode node, java.util.List<StringTail> alternatives) {
    return new StringTail.Ambiguity(node, alternatives);
  }

  public Test.Ambiguity makeTestAmbiguity(INode node, java.util.List<Test> alternatives) {
    return new Test.Ambiguity(node, alternatives);
  }

  public ProtocolPart.Ambiguity makeProtocolPartAmbiguity(INode node, java.util.List<ProtocolPart> alternatives) {
    return new ProtocolPart.Ambiguity(node, alternatives);
  }

  public FunctionModifiers.Ambiguity makeFunctionModifiersAmbiguity(INode node, java.util.List<FunctionModifiers> alternatives) {
    return new FunctionModifiers.Ambiguity(node, alternatives);
  }

  public Command.Ambiguity makeCommandAmbiguity(INode node, java.util.List<Command> alternatives) {
    return new Command.Ambiguity(node, alternatives);
  }

  public Toplevel.Ambiguity makeToplevelAmbiguity(INode node, java.util.List<Toplevel> alternatives) {
    return new Toplevel.Ambiguity(node, alternatives);
  }

  public Type.Ambiguity makeTypeAmbiguity(INode node, java.util.List<Type> alternatives) {
    return new Type.Ambiguity(node, alternatives);
  }

  public Label.Ambiguity makeLabelAmbiguity(INode node, java.util.List<Label> alternatives) {
    return new Label.Ambiguity(node, alternatives);
  }

  public ModuleParameters.Ambiguity makeModuleParametersAmbiguity(INode node, java.util.List<ModuleParameters> alternatives) {
    return new ModuleParameters.Ambiguity(node, alternatives);
  }

  public DecimalIntegerLiteral.Ambiguity makeDecimalIntegerLiteralAmbiguity(INode node, java.util.List<DecimalIntegerLiteral> alternatives) {
    return new DecimalIntegerLiteral.Ambiguity(node, alternatives);
  }

  public Expression.Ambiguity makeExpressionAmbiguity(INode node, java.util.List<Expression> alternatives) {
    return new Expression.Ambiguity(node, alternatives);
  }

  public Module.Ambiguity makeModuleAmbiguity(INode node, java.util.List<Module> alternatives) {
    return new Module.Ambiguity(node, alternatives);
  }

  public TagString.Ambiguity makeTagStringAmbiguity(INode node, java.util.List<TagString> alternatives) {
    return new TagString.Ambiguity(node, alternatives);
  }

  public DateTimeLiteral.Ambiguity makeDateTimeLiteralAmbiguity(INode node, java.util.List<DateTimeLiteral> alternatives) {
    return new DateTimeLiteral.Ambiguity(node, alternatives);
  }

  public Word.Ambiguity makeWordAmbiguity(INode node, java.util.List<Word> alternatives) {
    return new Word.Ambiguity(node, alternatives);
  }

  public LAYOUT.Ambiguity makeLAYOUTAmbiguity(INode node, java.util.List<LAYOUT> alternatives) {
    return new LAYOUT.Ambiguity(node, alternatives);
  }

  public Declaration.Ambiguity makeDeclarationAmbiguity(INode node, java.util.List<Declaration> alternatives) {
    return new Declaration.Ambiguity(node, alternatives);
  }

  public Nonterminal.Ambiguity makeNonterminalAmbiguity(INode node, java.util.List<Nonterminal> alternatives) {
    return new Nonterminal.Ambiguity(node, alternatives);
  }

  public LongLiteral.Ambiguity makeLongLiteralAmbiguity(INode node, java.util.List<LongLiteral> alternatives) {
    return new LongLiteral.Ambiguity(node, alternatives);
  }

  public Sym.Ambiguity makeSymAmbiguity(INode node, java.util.List<Sym> alternatives) {
    return new Sym.Ambiguity(node, alternatives);
  }

  public CommentChar.Ambiguity makeCommentCharAmbiguity(INode node, java.util.List<CommentChar> alternatives) {
    return new CommentChar.Ambiguity(node, alternatives);
  }

  public SyntaxDefinition.Ambiguity makeSyntaxDefinitionAmbiguity(INode node, java.util.List<SyntaxDefinition> alternatives) {
    return new SyntaxDefinition.Ambiguity(node, alternatives);
  }

  public Assoc.Ambiguity makeAssocAmbiguity(INode node, java.util.List<Assoc> alternatives) {
    return new Assoc.Ambiguity(node, alternatives);
  }

  public Comment.Ambiguity makeCommentAmbiguity(INode node, java.util.List<Comment> alternatives) {
    return new Comment.Ambiguity(node, alternatives);
  }

  public Renamings.Ambiguity makeRenamingsAmbiguity(INode node, java.util.List<Renamings> alternatives) {
    return new Renamings.Ambiguity(node, alternatives);
  }

  public Formal.Ambiguity makeFormalAmbiguity(INode node, java.util.List<Formal> alternatives) {
    return new Formal.Ambiguity(node, alternatives);
  }

  public Class.Ambiguity makeClassAmbiguity(INode node, java.util.List<Class> alternatives) {
    return new Class.Ambiguity(node, alternatives);
  }

  public StrChar.Ambiguity makeStrCharAmbiguity(INode node, java.util.List<StrChar> alternatives) {
    return new StrChar.Ambiguity(node, alternatives);
  }

  public Bound.Ambiguity makeBoundAmbiguity(INode node, java.util.List<Bound> alternatives) {
    return new Bound.Ambiguity(node, alternatives);
  }

  public ProtocolChars.Ambiguity makeProtocolCharsAmbiguity(INode node, java.util.List<ProtocolChars> alternatives) {
    return new ProtocolChars.Ambiguity(node, alternatives);
  }

  public MidStringChars.Ambiguity makeMidStringCharsAmbiguity(INode node, java.util.List<MidStringChars> alternatives) {
    return new MidStringChars.Ambiguity(node, alternatives);
  }

  public PreModule.Ambiguity makePreModuleAmbiguity(INode node, java.util.List<PreModule> alternatives) {
    return new PreModule.Ambiguity(node, alternatives);
  }

  public NoElseMayFollow.Ambiguity makeNoElseMayFollowAmbiguity(INode node, java.util.List<NoElseMayFollow> alternatives) {
    return new NoElseMayFollow.Ambiguity(node, alternatives);
  }

  public Comprehension.Ambiguity makeComprehensionAmbiguity(INode node, java.util.List<Comprehension> alternatives) {
    return new Comprehension.Ambiguity(node, alternatives);
  }

  public RegExpModifier.Ambiguity makeRegExpModifierAmbiguity(INode node, java.util.List<RegExpModifier> alternatives) {
    return new RegExpModifier.Ambiguity(node, alternatives);
  }

  public EscapedName.Ambiguity makeEscapedNameAmbiguity(INode node, java.util.List<EscapedName> alternatives) {
    return new EscapedName.Ambiguity(node, alternatives);
  }

  public RegExp.Ambiguity makeRegExpAmbiguity(INode node, java.util.List<RegExp> alternatives) {
    return new RegExp.Ambiguity(node, alternatives);
  }

  public SingleQuotedStrChar.Ambiguity makeSingleQuotedStrCharAmbiguity(INode node, java.util.List<SingleQuotedStrChar> alternatives) {
    return new SingleQuotedStrChar.Ambiguity(node, alternatives);
  }

  public PathTail.Ambiguity makePathTailAmbiguity(INode node, java.util.List<PathTail> alternatives) {
    return new PathTail.Ambiguity(node, alternatives);
  }

  public Literal.Ambiguity makeLiteralAmbiguity(INode node, java.util.List<Literal> alternatives) {
    return new Literal.Ambiguity(node, alternatives);
  }

  public RealLiteral.Ambiguity makeRealLiteralAmbiguity(INode node, java.util.List<RealLiteral> alternatives) {
    return new RealLiteral.Ambiguity(node, alternatives);
  }

  public Strategy.Ambiguity makeStrategyAmbiguity(INode node, java.util.List<Strategy> alternatives) {
    return new Strategy.Ambiguity(node, alternatives);
  }

  public DataTarget.Ambiguity makeDataTargetAmbiguity(INode node, java.util.List<DataTarget> alternatives) {
    return new DataTarget.Ambiguity(node, alternatives);
  }

  public Case.Ambiguity makeCaseAmbiguity(INode node, java.util.List<Case> alternatives) {
    return new Case.Ambiguity(node, alternatives);
  }

  public DatePart.Ambiguity makeDatePartAmbiguity(INode node, java.util.List<DatePart> alternatives) {
    return new DatePart.Ambiguity(node, alternatives);
  }

  public Catch.Ambiguity makeCatchAmbiguity(INode node, java.util.List<Catch> alternatives) {
    return new Catch.Ambiguity(node, alternatives);
  }

  public Field.Ambiguity makeFieldAmbiguity(INode node, java.util.List<Field> alternatives) {
    return new Field.Ambiguity(node, alternatives);
  }

  public TypeVar.Ambiguity makeTypeVarAmbiguity(INode node, java.util.List<TypeVar> alternatives) {
    return new TypeVar.Ambiguity(node, alternatives);
  }

  public Alternative.Ambiguity makeAlternativeAmbiguity(INode node, java.util.List<Alternative> alternatives) {
    return new Alternative.Ambiguity(node, alternatives);
  }

  public ModuleActuals.Ambiguity makeModuleActualsAmbiguity(INode node, java.util.List<ModuleActuals> alternatives) {
    return new ModuleActuals.Ambiguity(node, alternatives);
  }

  public Target.Ambiguity makeTargetAmbiguity(INode node, java.util.List<Target> alternatives) {
    return new Target.Ambiguity(node, alternatives);
  }

  public StringConstant.Ambiguity makeStringConstantAmbiguity(INode node, java.util.List<StringConstant> alternatives) {
    return new StringConstant.Ambiguity(node, alternatives);
  }

  public StringTemplate.Ambiguity makeStringTemplateAmbiguity(INode node, java.util.List<StringTemplate> alternatives) {
    return new StringTemplate.Ambiguity(node, alternatives);
  }

  public Visit.Ambiguity makeVisitAmbiguity(INode node, java.util.List<Visit> alternatives) {
    return new Visit.Ambiguity(node, alternatives);
  }

  public ParameterizedNonterminal.Ambiguity makeParameterizedNonterminalAmbiguity(INode node, java.util.List<ParameterizedNonterminal> alternatives) {
    return new ParameterizedNonterminal.Ambiguity(node, alternatives);
  }

  public Visibility.Ambiguity makeVisibilityAmbiguity(INode node, java.util.List<Visibility> alternatives) {
    return new Visibility.Ambiguity(node, alternatives);
  }

  public TagChar.Ambiguity makeTagCharAmbiguity(INode node, java.util.List<TagChar> alternatives) {
    return new TagChar.Ambiguity(node, alternatives);
  }

  public Tags.Ambiguity makeTagsAmbiguity(INode node, java.util.List<Tags> alternatives) {
    return new Tags.Ambiguity(node, alternatives);
  }

  public JustTime.Ambiguity makeJustTimeAmbiguity(INode node, java.util.List<JustTime> alternatives) {
    return new JustTime.Ambiguity(node, alternatives);
  }

  public StringCharacter.Ambiguity makeStringCharacterAmbiguity(INode node, java.util.List<StringCharacter> alternatives) {
    return new StringCharacter.Ambiguity(node, alternatives);
  }

  public Kind.Ambiguity makeKindAmbiguity(INode node, java.util.List<Kind> alternatives) {
    return new Kind.Ambiguity(node, alternatives);
  }

  public FunctionType.Ambiguity makeFunctionTypeAmbiguity(INode node, java.util.List<FunctionType> alternatives) {
    return new FunctionType.Ambiguity(node, alternatives);
  }

  public Prod.Ambiguity makeProdAmbiguity(INode node, java.util.List<Prod> alternatives) {
    return new Prod.Ambiguity(node, alternatives);
  }

  public URLChars.Ambiguity makeURLCharsAmbiguity(INode node, java.util.List<URLChars> alternatives) {
    return new URLChars.Ambiguity(node, alternatives);
  }

  public LocalVariableDeclaration.Ambiguity makeLocalVariableDeclarationAmbiguity(INode node, java.util.List<LocalVariableDeclaration> alternatives) {
    return new LocalVariableDeclaration.Ambiguity(node, alternatives);
  }

  public TimeZonePart.Ambiguity makeTimeZonePartAmbiguity(INode node, java.util.List<TimeZonePart> alternatives) {
    return new TimeZonePart.Ambiguity(node, alternatives);
  }

  public BasicType.Ambiguity makeBasicTypeAmbiguity(INode node, java.util.List<BasicType> alternatives) {
    return new BasicType.Ambiguity(node, alternatives);
  }

  public PreStringChars.Ambiguity makePreStringCharsAmbiguity(INode node, java.util.List<PreStringChars> alternatives) {
    return new PreStringChars.Ambiguity(node, alternatives);
  }

  public ProtocolTail.Ambiguity makeProtocolTailAmbiguity(INode node, java.util.List<ProtocolTail> alternatives) {
    return new ProtocolTail.Ambiguity(node, alternatives);
  }

  public QualifiedName.Ambiguity makeQualifiedNameAmbiguity(INode node, java.util.List<QualifiedName> alternatives) {
    return new QualifiedName.Ambiguity(node, alternatives);
  }

  public Backslash.Ambiguity makeBackslashAmbiguity(INode node, java.util.List<Backslash> alternatives) {
    return new Backslash.Ambiguity(node, alternatives);
  }

  public CaseInsensitiveStringConstant.Ambiguity makeCaseInsensitiveStringConstantAmbiguity(INode node, java.util.List<CaseInsensitiveStringConstant> alternatives) {
    return new CaseInsensitiveStringConstant.Ambiguity(node, alternatives);
  }

  public Start.Ambiguity makeStartAmbiguity(INode node, java.util.List<Start> alternatives) {
    return new Start.Ambiguity(node, alternatives);
  }

  public ShortChar.Ambiguity makeShortCharAmbiguity(INode node, java.util.List<ShortChar> alternatives) {
    return new ShortChar.Ambiguity(node, alternatives);
  }

  public Parameters.Ambiguity makeParametersAmbiguity(INode node, java.util.List<Parameters> alternatives) {
    return new Parameters.Ambiguity(node, alternatives);
  }

  public NamedBackslash.Ambiguity makeNamedBackslashAmbiguity(INode node, java.util.List<NamedBackslash> alternatives) {
    return new NamedBackslash.Ambiguity(node, alternatives);
  }

  public MidProtocolChars.Ambiguity makeMidProtocolCharsAmbiguity(INode node, java.util.List<MidProtocolChars> alternatives) {
    return new MidProtocolChars.Ambiguity(node, alternatives);
  }

  public NumChar.Ambiguity makeNumCharAmbiguity(INode node, java.util.List<NumChar> alternatives) {
    return new NumChar.Ambiguity(node, alternatives);
  }

  public Mapping_Expression.Ambiguity makeMapping_ExpressionAmbiguity(INode node, java.util.List<Mapping_Expression> alternatives) {
    return new Mapping_Expression.Ambiguity(node, alternatives);
  }

  public JustDate.Ambiguity makeJustDateAmbiguity(INode node, java.util.List<JustDate> alternatives) {
    return new JustDate.Ambiguity(node, alternatives);
  }

  public PostPathChars.Ambiguity makePostPathCharsAmbiguity(INode node, java.util.List<PostPathChars> alternatives) {
    return new PostPathChars.Ambiguity(node, alternatives);
  }

  public TimePartNoTZ.Ambiguity makeTimePartNoTZAmbiguity(INode node, java.util.List<TimePartNoTZ> alternatives) {
    return new TimePartNoTZ.Ambiguity(node, alternatives);
  }

  public DecimalLongLiteral.Ambiguity makeDecimalLongLiteralAmbiguity(INode node, java.util.List<DecimalLongLiteral> alternatives) {
    return new DecimalLongLiteral.Ambiguity(node, alternatives);
  }

  public Replacement.Ambiguity makeReplacementAmbiguity(INode node, java.util.List<Replacement> alternatives) {
    return new Replacement.Ambiguity(node, alternatives);
  }

  public SingleQuotedStrCon.Ambiguity makeSingleQuotedStrConAmbiguity(INode node, java.util.List<SingleQuotedStrCon> alternatives) {
    return new SingleQuotedStrCon.Ambiguity(node, alternatives);
  }

  public Assignment.Ambiguity makeAssignmentAmbiguity(INode node, java.util.List<Assignment> alternatives) {
    return new Assignment.Ambiguity(node, alternatives);
  }

  public Name.Ambiguity makeNameAmbiguity(INode node, java.util.List<Name> alternatives) {
    return new Name.Ambiguity(node, alternatives);
  }

  public Header.Ambiguity makeHeaderAmbiguity(INode node, java.util.List<Header> alternatives) {
    return new Header.Ambiguity(node, alternatives);
  }

  public LanguageAction.Ambiguity makeLanguageActionAmbiguity(INode node, java.util.List<LanguageAction> alternatives) {
    return new LanguageAction.Ambiguity(node, alternatives);
  }

  public IntegerLiteral.Ambiguity makeIntegerLiteralAmbiguity(INode node, java.util.List<IntegerLiteral> alternatives) {
    return new IntegerLiteral.Ambiguity(node, alternatives);
  }

  public PatternWithAction.Ambiguity makePatternWithActionAmbiguity(INode node, java.util.List<PatternWithAction> alternatives) {
    return new PatternWithAction.Ambiguity(node, alternatives);
  }

  public Import.Ambiguity makeImportAmbiguity(INode node, java.util.List<Import> alternatives) {
    return new Import.Ambiguity(node, alternatives);
  }

  public FunctionModifier.Ambiguity makeFunctionModifierAmbiguity(INode node, java.util.List<FunctionModifier> alternatives) {
    return new FunctionModifier.Ambiguity(node, alternatives);
  }

  public StrCon.Ambiguity makeStrConAmbiguity(INode node, java.util.List<StrCon> alternatives) {
    return new StrCon.Ambiguity(node, alternatives);
  }

  public ShellCommand.Ambiguity makeShellCommandAmbiguity(INode node, java.util.List<ShellCommand> alternatives) {
    return new ShellCommand.Ambiguity(node, alternatives);
  }

  public Declarator.Ambiguity makeDeclaratorAmbiguity(INode node, java.util.List<Declarator> alternatives) {
    return new Declarator.Ambiguity(node, alternatives);
  }

  public Variable.Ambiguity makeVariableAmbiguity(INode node, java.util.List<Variable> alternatives) {
    return new Variable.Ambiguity(node, alternatives);
  }

  public BooleanLiteral.Ambiguity makeBooleanLiteralAmbiguity(INode node, java.util.List<BooleanLiteral> alternatives) {
    return new BooleanLiteral.Ambiguity(node, alternatives);
  }

  public StringMiddle.Ambiguity makeStringMiddleAmbiguity(INode node, java.util.List<StringMiddle> alternatives) {
    return new StringMiddle.Ambiguity(node, alternatives);
  }

  public Signature.Ambiguity makeSignatureAmbiguity(INode node, java.util.List<Signature> alternatives) {
    return new Signature.Ambiguity(node, alternatives);
  }

  public TypeArg.Ambiguity makeTypeArgAmbiguity(INode node, java.util.List<TypeArg> alternatives) {
    return new TypeArg.Ambiguity(node, alternatives);
  }

  public FunctionDeclaration.Ambiguity makeFunctionDeclarationAmbiguity(INode node, java.util.List<FunctionDeclaration> alternatives) {
    return new FunctionDeclaration.Ambiguity(node, alternatives);
  }

  public PathPart.Ambiguity makePathPartAmbiguity(INode node, java.util.List<PathPart> alternatives) {
    return new PathPart.Ambiguity(node, alternatives);
  }

  public DateAndTime.Ambiguity makeDateAndTimeAmbiguity(INode node, java.util.List<DateAndTime> alternatives) {
    return new DateAndTime.Ambiguity(node, alternatives);
  }

  public ImportedModule.Ambiguity makeImportedModuleAmbiguity(INode node, java.util.List<ImportedModule> alternatives) {
    return new ImportedModule.Ambiguity(node, alternatives);
  }

  public Asterisk.Ambiguity makeAsteriskAmbiguity(INode node, java.util.List<Asterisk> alternatives) {
    return new Asterisk.Ambiguity(node, alternatives);
  }

  public UnicodeEscape.Ambiguity makeUnicodeEscapeAmbiguity(INode node, java.util.List<UnicodeEscape> alternatives) {
    return new UnicodeEscape.Ambiguity(node, alternatives);
  }

  public Statement.Ambiguity makeStatementAmbiguity(INode node, java.util.List<Statement> alternatives) {
    return new Statement.Ambiguity(node, alternatives);
  }

  public OctalIntegerLiteral.Ambiguity makeOctalIntegerLiteralAmbiguity(INode node, java.util.List<OctalIntegerLiteral> alternatives) {
    return new OctalIntegerLiteral.Ambiguity(node, alternatives);
  }

  public Assignable.Ambiguity makeAssignableAmbiguity(INode node, java.util.List<Assignable> alternatives) {
    return new Assignable.Ambiguity(node, alternatives);
  }

  public HexLongLiteral.Ambiguity makeHexLongLiteralAmbiguity(INode node, java.util.List<HexLongLiteral> alternatives) {
    return new HexLongLiteral.Ambiguity(node, alternatives);
  }

  public PathChars.Ambiguity makePathCharsAmbiguity(INode node, java.util.List<PathChars> alternatives) {
    return new PathChars.Ambiguity(node, alternatives);
  }

}
