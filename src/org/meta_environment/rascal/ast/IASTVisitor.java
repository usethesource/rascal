package org.meta_environment.rascal.ast;
public interface IASTVisitor
{
  public Body visitToplevelsBody (Body.Toplevels x);
  public Visibility visitPrivateVisibility (Visibility.Private x);
  public Visibility visitPublicVisibility (Visibility.Public x);
  public Toplevel visitDefaultVisibilityToplevel (Toplevel.
						  DefaultVisibility x);
  public Toplevel visitGivenVisibilityToplevel (Toplevel.GivenVisibility x);
  public Declaration visitTagDeclaration (Declaration.Tag x);
  public Declaration visitAnnotationDeclaration (Declaration.Annotation x);
  public Declaration visitRuleDeclaration (Declaration.Rule x);
  public Declaration visitVariableDeclaration (Declaration.Variable x);
  public Declaration visitFunctionDeclaration (Declaration.Function x);
  public Declaration visitDataDeclaration (Declaration.Data x);
  public Declaration visitTypeDeclaration (Declaration.Type x);
  public Declaration visitViewDeclaration (Declaration.View x);
  public Alternative visitNamedTypeAlternative (Alternative.NamedType x);
  public Variant visitNillaryConstructorVariant (Variant.
						 NillaryConstructor x);
  public Variant visitNAryConstructorVariant (Variant.NAryConstructor x);
  public Variant visitTypeVariant (Variant.Type x);
  public StandardOperator visitNotInStandardOperator (StandardOperator.
						      NotIn x);
  public StandardOperator visitInStandardOperator (StandardOperator.In x);
  public StandardOperator visitNotStandardOperator (StandardOperator.Not x);
  public StandardOperator visitOrStandardOperator (StandardOperator.Or x);
  public StandardOperator visitAndStandardOperator (StandardOperator.And x);
  public StandardOperator
    visitGreaterThanOrEqStandardOperator (StandardOperator.GreaterThanOrEq x);
  public StandardOperator visitGreaterThanStandardOperator (StandardOperator.
							    GreaterThan x);
  public StandardOperator visitLessThanOrEqStandardOperator (StandardOperator.
							     LessThanOrEq x);
  public StandardOperator visitLessThanStandardOperator (StandardOperator.
							 LessThan x);
  public StandardOperator visitNotEqualsStandardOperator (StandardOperator.
							  NotEquals x);
  public StandardOperator visitEqualsStandardOperator (StandardOperator.
						       Equals x);
  public StandardOperator visitIntersectionStandardOperator (StandardOperator.
							     Intersection x);
  public StandardOperator visitDivisionStandardOperator (StandardOperator.
							 Division x);
  public StandardOperator visitProductStandardOperator (StandardOperator.
							Product x);
  public StandardOperator visitSubstractionStandardOperator (StandardOperator.
							     Substraction x);
  public StandardOperator visitAdditionStandardOperator (StandardOperator.
							 Addition x);
  public FunctionName visitOperatorFunctionName (FunctionName.Operator x);
  public FunctionName visitNameFunctionName (FunctionName.Name x);
  public FunctionModifier visitJavaFunctionModifier (FunctionModifier.Java x);
  public FunctionModifiers visitListFunctionModifiers (FunctionModifiers.
						       List x);
  public Signature visitWithThrowsSignature (Signature.WithThrows x);
  public Signature visitNoThrowsSignature (Signature.NoThrows x);
  public FunctionDeclaration
    visitAbstractFunctionDeclaration (FunctionDeclaration.Abstract x);
  public FunctionDeclaration
    visitDefaultFunctionDeclaration (FunctionDeclaration.Default x);
  public FunctionBody visitDefaultFunctionBody (FunctionBody.Default x);
  public Variable visitGivenInitializationVariable (Variable.
						    GivenInitialization x);
  public Variable visitDefaultInitializationVariable (Variable.
						      DefaultInitialization
						      x);
  public Kind visitAllKind (Kind.All x);
  public Kind visitTagKind (Kind.Tag x);
  public Kind visitAnnoKind (Kind.Anno x);
  public Kind visitTypeKind (Kind.Type x);
  public Kind visitViewKind (Kind.View x);
  public Kind visitDataKind (Kind.Data x);
  public Kind visitVariableKind (Kind.Variable x);
  public Kind visitFunctionKind (Kind.Function x);
  public Kind visitModuleKind (Kind.Module x);
  public Pattern visitTypedVariablePattern (Pattern.TypedVariable x);
  public ValueProducer visitGivenStrategyValueProducer (ValueProducer.
							GivenStrategy x);
  public ValueProducer visitDefaultStrategyValueProducer (ValueProducer.
							  DefaultStrategy x);
  public Generator visitProducerGenerator (Generator.Producer x);
  public Generator visitExpressionGenerator (Generator.Expression x);
  public Strategy visitInnermostStrategy (Strategy.Innermost x);
  public Strategy visitOutermostStrategy (Strategy.Outermost x);
  public Strategy visitBottomUpBreakStrategy (Strategy.BottomUpBreak x);
  public Strategy visitBottomUpStrategy (Strategy.BottomUp x);
  public Strategy visitTopDownBreakStrategy (Strategy.TopDownBreak x);
  public Strategy visitTopDownStrategy (Strategy.TopDown x);
  public Expression visitOperatorExpression (Expression.Operator x);
  public Expression visitIfThenElseExpression (Expression.IfThenElse x);
  public Expression visitIfDefinedExpression (Expression.IfDefined x);
  public Expression visitOrExpression (Expression.Or x);
  public Expression visitAndExpression (Expression.And x);
  public Expression visitInExpression (Expression.In x);
  public Expression visitNotInExpression (Expression.NotIn x);
  public Expression visitNonEqualsExpression (Expression.NonEquals x);
  public Expression visitEqualsExpression (Expression.Equals x);
  public Expression visitGreaterThanOrEqExpression (Expression.
						    GreaterThanOrEq x);
  public Expression visitGreaterThanExpression (Expression.GreaterThan x);
  public Expression visitLessThanOrEqExpression (Expression.LessThanOrEq x);
  public Expression visitLessThanExpression (Expression.LessThan x);
  public Expression visitNoMatchExpression (Expression.NoMatch x);
  public Expression visitMatchExpression (Expression.Match x);
  public Expression visitSubstractionExpression (Expression.Substraction x);
  public Expression visitAdditionExpression (Expression.Addition x);
  public Expression visitDivisionExpression (Expression.Division x);
  public Expression visitIntersectionExpression (Expression.Intersection x);
  public Expression visitProductExpression (Expression.Product x);
  public Expression visitNegationExpression (Expression.Negation x);
  public Expression visitAnnotationExpression (Expression.Annotation x);
  public Expression visitTransitiveClosureExpression (Expression.
						      TransitiveClosure x);
  public Expression visitTransitiveReflexiveClosureExpression (Expression.
							       TransitiveReflexiveClosure
							       x);
  public Expression visitSubscriptExpression (Expression.Subscript x);
  public Expression visitFieldAccessExpression (Expression.FieldAccess x);
  public Expression visitClosureCallExpression (Expression.ClosureCall x);
  public Expression visitClosureExpression (Expression.Closure x);
  public Expression visitQualifiedNameExpression (Expression.QualifiedName x);
  public Expression visitAreaInFileLocationExpression (Expression.
						       AreaInFileLocation x);
  public Expression visitAreaLocationExpression (Expression.AreaLocation x);
  public Expression visitFileLocationExpression (Expression.FileLocation x);
  public Expression visitAreaExpression (Expression.Area x);
  public Expression visitLocationExpression (Expression.Location x);
  public Expression visitMapTupleExpression (Expression.MapTuple x);
  public Expression visitTupleExpression (Expression.Tuple x);
  public Expression visitSetExpression (Expression.Set x);
  public Expression visitStepRangeExpression (Expression.StepRange x);
  public Expression visitRangeExpression (Expression.Range x);
  public Expression visitListExpression (Expression.List x);
  public Expression visitCallOrTreeExpression (Expression.CallOrTree x);
  public Expression visitLiteralExpression (Expression.Literal x);
  public Expression visitVisitExpression (Expression.Visit x);
  public Expression visitForallExpression (Expression.Forall x);
  public Expression visitExistsExpression (Expression.Exists x);
  public Expression visitComprehensionExpression (Expression.Comprehension x);
  public Comprehension visitListComprehension (Comprehension.List x);
  public Comprehension visitSetComprehension (Comprehension.Set x);
  public Match visitArbitraryMatch (Match.Arbitrary x);
  public Match visitReplacingMatch (Match.Replacing x);
  public Rule visitNoGuardRule (Rule.NoGuard x);
  public Rule visitWithGuardRule (Rule.WithGuard x);
  public Case visitDefaultCase (Case.Default x);
  public Case visitRuleCase (Case.Rule x);
  public Visit visitGivenStrategyVisit (Visit.GivenStrategy x);
  public Visit visitDefaultStrategyVisit (Visit.DefaultStrategy x);
  public Symbol visitCaseInsensitiveLiteralSymbol (Symbol.
						   CaseInsensitiveLiteral x);
  public Symbol visitLiteralSymbol (Symbol.Literal x);
  public Symbol visitLiftedSymbolSymbol (Symbol.LiftedSymbol x);
  public Symbol visitCharacterClassSymbol (Symbol.CharacterClass x);
  public Symbol visitAlternativeSymbol (Symbol.Alternative x);
  public Symbol visitIterStarSepSymbol (Symbol.IterStarSep x);
  public Symbol visitIterSepSymbol (Symbol.IterSep x);
  public Symbol visitIterStarSymbol (Symbol.IterStar x);
  public Symbol visitIterSymbol (Symbol.Iter x);
  public Symbol visitOptionalSymbol (Symbol.Optional x);
  public Symbol visitSequenceSymbol (Symbol.Sequence x);
  public Symbol visitEmptySymbol (Symbol.Empty x);
  public Symbol visitParameterizedSortSymbol (Symbol.ParameterizedSort x);
  public Symbol visitSortSymbol (Symbol.Sort x);
  public CharRange visitRangeCharRange (CharRange.Range x);
  public CharRange visitCharacterCharRange (CharRange.Character x);
  public CharRanges visitConcatenateCharRanges (CharRanges.Concatenate x);
  public CharRanges visitRangeCharRanges (CharRanges.Range x);
  public OptCharRanges visitPresentOptCharRanges (OptCharRanges.Present x);
  public OptCharRanges visitAbsentOptCharRanges (OptCharRanges.Absent x);
  public CharClass visitUnionCharClass (CharClass.Union x);
  public CharClass visitIntersectionCharClass (CharClass.Intersection x);
  public CharClass visitDifferenceCharClass (CharClass.Difference x);
  public CharClass visitComplementCharClass (CharClass.Complement x);
  public CharClass visitSimpleCharclassCharClass (CharClass.
						  SimpleCharclass x);
  public Character visitLabelStartCharacter (Character.LabelStart x);
  public Character visitBottomCharacter (Character.Bottom x);
  public Character visitEOFCharacter (Character.EOF x);
  public Character visitTopCharacter (Character.Top x);
  public Character visitShortCharacter (Character.Short x);
  public Character visitNumericCharacter (Character.Numeric x);
  public Annotation visitDefaultAnnotation (Annotation.Default x);
  public Annotations visitDefaultAnnotations (Annotations.Default x);
  public Solve visitWithBoundSolve (Solve.WithBound x);
  public Solve visitNoBoundSolve (Solve.NoBound x);
  public Statement visitFunctionDeclarationStatement (Statement.
						      FunctionDeclaration x);
  public Statement visitBlockStatement (Statement.Block x);
  public Statement visitTryFinallyStatement (Statement.TryFinally x);
  public Statement visitTryStatement (Statement.Try x);
  public Statement visitFailStatement (Statement.Fail x);
  public Statement visitReturnVoidStatement (Statement.ReturnVoid x);
  public Statement visitThrowStatement (Statement.Throw x);
  public Statement visitInsertStatement (Statement.Insert x);
  public Statement visitReturnStatement (Statement.Return x);
  public Statement visitAssertStatement (Statement.Assert x);
  public Statement visitAssignmentStatement (Statement.Assignment x);
  public Statement visitVisitStatement (Statement.Visit x);
  public Statement visitExpressionStatement (Statement.Expression x);
  public Statement visitVariableDeclarationStatement (Statement.
						      VariableDeclaration x);
  public Statement visitSwitchStatement (Statement.Switch x);
  public Statement visitIfThenStatement (Statement.IfThen x);
  public Statement visitIfThenElseStatement (Statement.IfThenElse x);
  public Statement visitWhileStatement (Statement.While x);
  public Statement visitForStatement (Statement.For x);
  public Statement visitSolveStatement (Statement.Solve x);
  public Condition visitConjunctionCondition (Condition.Conjunction x);
  public Condition visitExpressionCondition (Condition.Expression x);
  public Condition visitNoMatchCondition (Condition.NoMatch x);
  public Condition visitMatchCondition (Condition.Match x);
  public Assignable visitAnnotationAssignable (Assignable.Annotation x);
  public Assignable visitIfDefinedAssignable (Assignable.IfDefined x);
  public Assignable visitFieldAccessAssignable (Assignable.FieldAccess x);
  public Assignable visitSubscriptAssignable (Assignable.Subscript x);
  public Assignable visitVariableAssignable (Assignable.Variable x);
  public Assignment visitInteresectionAssignment (Assignment.Interesection x);
  public Assignment visitDivisionAssignment (Assignment.Division x);
  public Assignment visitProductAssignment (Assignment.Product x);
  public Assignment visitSubstractionAssignment (Assignment.Substraction x);
  public Assignment visitAdditionAssignment (Assignment.Addition x);
  public Assignment visitDefaultAssignment (Assignment.Default x);
  public Catch visitBindingCatchCatch (Catch.BindingCatch x);
  public Catch visitCatchCatch (Catch.Catch x);
  public Scope visitDynamicScope (Scope.Dynamic x);
  public Scope visitGlobalScope (Scope.Global x);
  public LocalVariableDeclaration
    visitGivenScopeLocalVariableDeclaration (LocalVariableDeclaration.
					     GivenScope x);
  public LocalVariableDeclaration
    visitDefaultScopeLocalVariableDeclaration (LocalVariableDeclaration.
					       DefaultScope x);
  public Module visitModuleModule (Module.Module x);
  public ModuleActuals visitActualsModuleActuals (ModuleActuals.Actuals x);
  public ImportedModule visitDefaultImportedModule (ImportedModule.Default x);
  public ImportedModule visitRenamingsImportedModule (ImportedModule.
						      Renamings x);
  public ImportedModule visitActualsImportedModule (ImportedModule.Actuals x);
  public ImportedModule visitActualsRenamingImportedModule (ImportedModule.
							    ActualsRenaming
							    x);
  public Renaming visitRenamingRenaming (Renaming.Renaming x);
  public Renamings visitRenamingsRenamings (Renamings.Renamings x);
  public Import visitExtendImport (Import.Extend x);
  public Import visitImportImport (Import.Import x);
  public ModuleParameters
    visitModuleParametersModuleParameters (ModuleParameters.
					   ModuleParameters x);
  public Header visitParametersHeader (Header.Parameters x);
  public Header visitDefaultHeader (Header.Default x);
  public IntegerLiteral
    visitOctalIntegerLiteralIntegerLiteral (IntegerLiteral.
					    OctalIntegerLiteral x);
  public IntegerLiteral visitHexIntegerLiteralIntegerLiteral (IntegerLiteral.
							      HexIntegerLiteral
							      x);
  public IntegerLiteral
    visitDecimalIntegerLiteralIntegerLiteral (IntegerLiteral.
					      DecimalIntegerLiteral x);
  public LongLiteral visitOctalLongLiteralLongLiteral (LongLiteral.
						       OctalLongLiteral x);
  public LongLiteral visitHexLongLiteralLongLiteral (LongLiteral.
						     HexLongLiteral x);
  public LongLiteral visitDecimalLongLiteralLongLiteral (LongLiteral.
							 DecimalLongLiteral
							 x);
  public Area visitAreaArea (Area.Area x);
  public Literal visitStringLiteral (Literal.String x);
  public Literal visitDoubleLiteral (Literal.Double x);
  public Literal visitIntegerLiteral (Literal.Integer x);
  public Literal visitBooleanLiteral (Literal.Boolean x);
  public Literal visitSymbolLiteral (Literal.Symbol x);
  public Literal visitRegExpLiteral (Literal.RegExp x);
  public BasicType visitLocBasicType (BasicType.Loc x);
  public BasicType visitVoidBasicType (BasicType.Void x);
  public BasicType visitTermBasicType (BasicType.Term x);
  public BasicType visitValueBasicType (BasicType.Value x);
  public BasicType visitStringBasicType (BasicType.String x);
  public BasicType visitDoubleBasicType (BasicType.Double x);
  public BasicType visitIntBasicType (BasicType.Int x);
  public BasicType visitBoolBasicType (BasicType.Bool x);
  public TypeArg visitNamedTypeArg (TypeArg.Named x);
  public TypeArg visitDefaultTypeArg (TypeArg.Default x);
  public StructuredType visitTupleStructuredType (StructuredType.Tuple x);
  public StructuredType visitRelationStructuredType (StructuredType.
						     Relation x);
  public StructuredType visitMapStructuredType (StructuredType.Map x);
  public StructuredType visitSetStructuredType (StructuredType.Set x);
  public StructuredType visitListStructuredType (StructuredType.List x);
  public FunctionType visitTypeArgumentsFunctionType (FunctionType.
						      TypeArguments x);
  public TypeVar visitBoundedTypeVar (TypeVar.Bounded x);
  public TypeVar visitFreeTypeVar (TypeVar.Free x);
  public UserType visitParametricUserType (UserType.Parametric x);
  public UserType visitNameUserType (UserType.Name x);
  public DataTypeSelector visitSelectorDataTypeSelector (DataTypeSelector.
							 Selector x);
  public Type visitSelectorType (Type.Selector x);
  public Type visitSymbolType (Type.Symbol x);
  public Type visitUserType (Type.User x);
  public Type visitVariableType (Type.Variable x);
  public Type visitFunctionType (Type.Function x);
  public Type visitStructuredType (Type.Structured x);
  public Type visitBasicType (Type.Basic x);
  public Formal visitTypeNameFormal (Formal.TypeName x);
  public Formals visitFormalsFormals (Formals.Formals x);
  public Parameters visitVarArgsParameters (Parameters.VarArgs x);
  public Parameters visitDefaultParameters (Parameters.Default x);
  public QualifiedName visitDefaultQualifiedName (QualifiedName.Default x);
}
