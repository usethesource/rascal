package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public interface IASTVisItor
{
  public Body visItBodyToplevels (Body.Toplevels x);
  public StrChar visItStrCharnewline (StrChar.newline x);
  public Symbol visItSymbolCaseInsensitiveLiteral (Symbol.
						   CaseInsensitiveLiteral x);
  public Symbol visItSymbolLiteral (Symbol.Literal x);
  public Symbol visItSymbolLiftedSymbol (Symbol.LiftedSymbol x);
  public Symbol visItSymbolCharacterClass (Symbol.CharacterClass x);
  public Symbol visItSymbolAlternative (Symbol.Alternative x);
  public Symbol visItSymbolIterStarSep (Symbol.IterStarSep x);
  public Symbol visItSymbolIterSep (Symbol.IterSep x);
  public Symbol visItSymbolIterStar (Symbol.IterStar x);
  public Symbol visItSymbolIter (Symbol.Iter x);
  public Symbol visItSymbolOptional (Symbol.Optional x);
  public Symbol visItSymbolSequence (Symbol.Sequence x);
  public Symbol visItSymbolEmpty (Symbol.Empty x);
  public Symbol visItSymbolParameterizedSort (Symbol.ParameterizedSort x);
  public Symbol visItSymbolSort (Symbol.Sort x);
  public CharRange visItCharRangeRange (CharRange.Range x);
  public CharRange visItCharRangeCharacter (CharRange.Character x);
  public CharRanges visItCharRangesBracket (CharRanges.Bracket x);
  public CharRanges visItCharRangesConcatenate (CharRanges.Concatenate x);
  public CharRanges visItCharRangesRange (CharRanges.Range x);
  public OptCharRanges visItOptCharRangesPresent (OptCharRanges.Present x);
  public OptCharRanges visItOptCharRangesAbsent (OptCharRanges.Absent x);
  public CharClass visItCharClassUnion (CharClass.Union x);
  public CharClass visItCharClassIntersection (CharClass.Intersection x);
  public CharClass visItCharClassDifference (CharClass.Difference x);
  public CharClass visItCharClassComplement (CharClass.Complement x);
  public CharClass visItCharClassBracket (CharClass.Bracket x);
  public CharClass visItCharClassSimpleCharclass (CharClass.
						  SimpleCharclass x);
  public Character visItCharacterLabelStart (Character.LabelStart x);
  public Character visItCharacterBottom (Character.Bottom x);
  public Character visItCharacterEOF (Character.EOF x);
  public Character visItCharacterTop (Character.Top x);
  public Character visItCharacterShort (Character.Short x);
  public Character visItCharacterNumeric (Character.Numeric x);
  public BasicType visItBasicTypeLoc (BasicType.Loc x);
  public BasicType visItBasicTypeVoid (BasicType.Void x);
  public BasicType visItBasicTypeTerm (BasicType.Term x);
  public BasicType visItBasicTypeValue (BasicType.Value x);
  public BasicType visItBasicTypeString (BasicType.String x);
  public BasicType visItBasicTypeDouble (BasicType.Double x);
  public BasicType visItBasicTypeInt (BasicType.Int x);
  public BasicType visItBasicTypeBool (BasicType.Bool x);
  public TypeArg visItTypeArgNamed (TypeArg.Named x);
  public TypeArg visItTypeArgDefault (TypeArg.Default x);
  public StructuredType visItStructuredTypeTuple (StructuredType.Tuple x);
  public StructuredType visItStructuredTypeRelation (StructuredType.
						     Relation x);
  public StructuredType visItStructuredTypeMap (StructuredType.Map x);
  public StructuredType visItStructuredTypeSet (StructuredType.Set x);
  public StructuredType visItStructuredTypeLisT (StructuredType.LisT x);
  public FunctionType visItFunctionTypeTypeArguments (FunctionType.
						      TypeArguments x);
  public TypeVar visItTypeVarBounded (TypeVar.Bounded x);
  public TypeVar visItTypeVarFree (TypeVar.Free x);
  public UserType visItUserTypeParametric (UserType.Parametric x);
  public UserType visItUserTypeName (UserType.Name x);
  public DataTypeSelector visItDataTypeSelectorSelector (DataTypeSelector.
							 Selector x);
  public Type visItTypeSelector (Type.Selector x);
  public Type visItTypeSymbol (Type.Symbol x);
  public Type visItTypeUser (Type.User x);
  public Type visItTypeVariable (Type.Variable x);
  public Type visItTypeFunction (Type.Function x);
  public Type visItTypeStructured (Type.Structured x);
  public Type visItTypeBasic (Type.Basic x);
  public IntegerLiteral
    visItIntegerLiteralOctalIntegerLiteral (IntegerLiteral.
					    OctalIntegerLiteral x);
  public IntegerLiteral visItIntegerLiteralHexIntegerLiteral (IntegerLiteral.
							      HexIntegerLiteral
							      x);
  public IntegerLiteral
    visItIntegerLiteralDecimalIntegerLiteral (IntegerLiteral.
					      DecimalIntegerLiteral x);
  public LongLiteral visItLongLiteralOctalLongLiteral (LongLiteral.
						       OctalLongLiteral x);
  public LongLiteral visItLongLiteralHexLongLiteral (LongLiteral.
						     HexLongLiteral x);
  public LongLiteral visItLongLiteralDecimalLongLiteral (LongLiteral.
							 DecimalLongLiteral
							 x);
  public Expression visItExpressionVisIt (Expression.VisIt x);
  public Expression visItExpressionExisTs (Expression.ExisTs x);
  public Expression visItExpressionForAll (Expression.ForAll x);
  public Expression visItExpressionComprehension (Expression.Comprehension x);
  public Expression visItExpressionNoMatch (Expression.NoMatch x);
  public Expression visItExpressionMatch (Expression.Match x);
  public Expression visItExpressionTypedVariable (Expression.TypedVariable x);
  public Expression visItExpressionOperator (Expression.Operator x);
  public Expression visItExpressionIfThenElse (Expression.IfThenElse x);
  public Expression visItExpressionIfDefined (Expression.IfDefined x);
  public Expression visItExpressionOr (Expression.Or x);
  public Expression visItExpressionAnd (Expression.And x);
  public Expression visItExpressionIn (Expression.In x);
  public Expression visItExpressionNotIn (Expression.NotIn x);
  public Expression visItExpressionNonEquals (Expression.NonEquals x);
  public Expression visItExpressionEquals (Expression.Equals x);
  public Expression visItExpressionGreaterThanOrEq (Expression.
						    GreaterThanOrEq x);
  public Expression visItExpressionGreaterThan (Expression.GreaterThan x);
  public Expression visItExpressionLessThanOrEq (Expression.LessThanOrEq x);
  public Expression visItExpressionLessThan (Expression.LessThan x);
  public Expression visItExpressionRegExpNoMatch (Expression.RegExpNoMatch x);
  public Expression visItExpressionRegExpMatch (Expression.RegExpMatch x);
  public Expression visItExpressionSubstraction (Expression.Substraction x);
  public Expression visItExpressionAddition (Expression.Addition x);
  public Expression visItExpressionDivisIon (Expression.DivisIon x);
  public Expression visItExpressionIntersection (Expression.Intersection x);
  public Expression visItExpressionProduct (Expression.Product x);
  public Expression visItExpressionNegation (Expression.Negation x);
  public Expression visItExpressionAnnotation (Expression.Annotation x);
  public Expression visItExpressionTransitiveClosure (Expression.
						      TransitiveClosure x);
  public Expression visItExpressionTransitiveReflexiveClosure (Expression.
							       TransitiveReflexiveClosure
							       x);
  public Expression visItExpressionSubscript (Expression.Subscript x);
  public Expression visItExpressionFieldAccess (Expression.FieldAccess x);
  public Expression visItExpressionFieldUpdate (Expression.FieldUpdate x);
  public Expression visItExpressionStepRange (Expression.StepRange x);
  public Expression visItExpressionRange (Expression.Range x);
  public Expression visItExpressionClosureCall (Expression.ClosureCall x);
  public Expression visItExpressionBracket (Expression.Bracket x);
  public Expression visItExpressionClosure (Expression.Closure x);
  public Expression visItExpressionQualifiedName (Expression.QualifiedName x);
  public Expression visItExpressionAreaInFileLocation (Expression.
						       AreaInFileLocation x);
  public Expression visItExpressionAreaLocation (Expression.AreaLocation x);
  public Expression visItExpressionFileLocation (Expression.FileLocation x);
  public Expression visItExpressionArea (Expression.Area x);
  public Expression visItExpressionLocation (Expression.Location x);
  public Expression visItExpressionMapTuple (Expression.MapTuple x);
  public Expression visItExpressionTuple (Expression.Tuple x);
  public Expression visItExpressionSet (Expression.Set x);
  public Expression visItExpressionLisT (Expression.LisT x);
  public Expression visItExpressionCallOrTree (Expression.CallOrTree x);
  public Expression visItExpressionLiteral (Expression.Literal x);
  public Area visItAreaDefault (Area.Default x);
  public Tag visItTagDefault (Tag.Default x);
  public Tags visItTagsDefault (Tags.Default x);
  public Literal visItLiteralString (Literal.String x);
  public Literal visItLiteralDouble (Literal.Double x);
  public Literal visItLiteralInteger (Literal.Integer x);
  public Literal visItLiteralBoolean (Literal.Boolean x);
  public Literal visItLiteralSymbol (Literal.Symbol x);
  public Literal visItLiteralRegExp (Literal.RegExp x);
  public Bound visItBoundDefault (Bound.Default x);
  public Bound visItBoundEmpty (Bound.Empty x);
  public Statement visItStatementGlobalDirective (Statement.
						  GlobalDirective x);
  public Statement visItStatementVariableDeclaration (Statement.
						      VariableDeclaration x);
  public Statement visItStatementFunctionDeclaration (Statement.
						      FunctionDeclaration x);
  public Statement visItStatementBlock (Statement.Block x);
  public Statement visItStatementTryFinally (Statement.TryFinally x);
  public Statement visItStatementTry (Statement.Try x);
  public Statement visItStatementThrow (Statement.Throw x);
  public Statement visItStatementInsert (Statement.Insert x);
  public Statement visItStatementAssert (Statement.Assert x);
  public Statement visItStatementContinue (Statement.Continue x);
  public Statement visItStatementReturn (Statement.Return x);
  public Statement visItStatementFail (Statement.Fail x);
  public Statement visItStatementBreak (Statement.Break x);
  public Statement visItStatementAssignment (Statement.Assignment x);
  public Statement visItStatementVisIt (Statement.VisIt x);
  public Statement visItStatementExpression (Statement.Expression x);
  public Statement visItStatementSwitch (Statement.Switch x);
  public Statement visItStatementIfThen (Statement.IfThen x);
  public Statement visItStatementIfThenElse (Statement.IfThenElse x);
  public Statement visItStatementDoWhile (Statement.DoWhile x);
  public Statement visItStatementWhile (Statement.While x);
  public Statement visItStatementFirst (Statement.First x);
  public Statement visItStatementAll (Statement.All x);
  public Statement visItStatementFor (Statement.For x);
  public Statement visItStatementSolve (Statement.Solve x);
  public NoElseMayFollow visItNoElseMayFollowDefault (NoElseMayFollow.
						      Default x);
  public Assignable visItAssignableConstructor (Assignable.Constructor x);
  public Assignable visItAssignableTuple (Assignable.Tuple x);
  public Assignable visItAssignableAnnotation (Assignable.Annotation x);
  public Assignable visItAssignableIfDefined (Assignable.IfDefined x);
  public Assignable visItAssignableFieldAccess (Assignable.FieldAccess x);
  public Assignable visItAssignableSubscript (Assignable.Subscript x);
  public Assignable visItAssignableVariable (Assignable.Variable x);
  public Assignment visItAssignmentInteresection (Assignment.Interesection x);
  public Assignment visItAssignmentDivisIon (Assignment.DivisIon x);
  public Assignment visItAssignmentProduct (Assignment.Product x);
  public Assignment visItAssignmentSubstraction (Assignment.Substraction x);
  public Assignment visItAssignmentAddition (Assignment.Addition x);
  public Assignment visItAssignmentDefault (Assignment.Default x);
  public Label visItLabelDefault (Label.Default x);
  public Label visItLabelEmpty (Label.Empty x);
  public Break visItBreakNoLabel (Break.NoLabel x);
  public Break visItBreakWithLabel (Break.WithLabel x);
  public Fail visItFailNoLabel (Fail.NoLabel x);
  public Fail visItFailWithLabel (Fail.WithLabel x);
  public Return visItReturnNoExpression (Return.NoExpression x);
  public Return visItReturnWithExpression (Return.WithExpression x);
  public Catch visItCatchBinding (Catch.Binding x);
  public Catch visItCatchDefault (Catch.Default x);
  public Declarator visItDeclaratorDefault (Declarator.Default x);
  public LocalVariableDeclaration
    visItLocalVariableDeclarationDynamic (LocalVariableDeclaration.Dynamic x);
  public LocalVariableDeclaration
    visItLocalVariableDeclarationDefault (LocalVariableDeclaration.Default x);
  public Formal visItFormalTypeName (Formal.TypeName x);
  public Formals visItFormalsDefault (Formals.Default x);
  public Parameters visItParametersVarArgs (Parameters.VarArgs x);
  public Parameters visItParametersDefault (Parameters.Default x);
  public QualifiedName visItQualifiedNameDefault (QualifiedName.Default x);
  public Module visItModuleDefault (Module.Default x);
  public ModuleActuals visItModuleActualsDefault (ModuleActuals.Default x);
  public ImportedModule visItImportedModuleDefault (ImportedModule.Default x);
  public ImportedModule visItImportedModuleRenamings (ImportedModule.
						      Renamings x);
  public ImportedModule visItImportedModuleActuals (ImportedModule.Actuals x);
  public ImportedModule visItImportedModuleActualsRenaming (ImportedModule.
							    ActualsRenaming
							    x);
  public Renaming visItRenamingDefault (Renaming.Default x);
  public Renamings visItRenamingsDefault (Renamings.Default x);
  public Import visItImportExtend (Import.Extend x);
  public Import visItImportDefault (Import.Default x);
  public ModuleParameters visItModuleParametersDefault (ModuleParameters.
							Default x);
  public Header visItHeaderParameters (Header.Parameters x);
  public Header visItHeaderDefault (Header.Default x);
  public VisIbility visItVisIbilityPrivate (VisIbility.Private x);
  public VisIbility visItVisIbilityPublic (VisIbility.Public x);
  public Toplevel visItToplevelDefaultVisIbility (Toplevel.
						  DefaultVisIbility x);
  public Toplevel visItToplevelGivenVisIbility (Toplevel.GivenVisIbility x);
  public Declaration visItDeclarationTag (Declaration.Tag x);
  public Declaration visItDeclarationAnnotation (Declaration.Annotation x);
  public Declaration visItDeclarationRule (Declaration.Rule x);
  public Declaration visItDeclarationVariable (Declaration.Variable x);
  public Declaration visItDeclarationFunction (Declaration.Function x);
  public Declaration visItDeclarationData (Declaration.Data x);
  public Declaration visItDeclarationType (Declaration.Type x);
  public Declaration visItDeclarationView (Declaration.View x);
  public Alternative visItAlternativeNamedType (Alternative.NamedType x);
  public Variant visItVariantNillaryConstructor (Variant.
						 NillaryConstructor x);
  public Variant visItVariantNAryConstructor (Variant.NAryConstructor x);
  public Variant visItVariantAnonymousConstructor (Variant.
						   AnonymousConstructor x);
  public StandardOperator visItStandardOperatorNotIn (StandardOperator.
						      NotIn x);
  public StandardOperator visItStandardOperatorIn (StandardOperator.In x);
  public StandardOperator visItStandardOperatorNot (StandardOperator.Not x);
  public StandardOperator visItStandardOperatorOr (StandardOperator.Or x);
  public StandardOperator visItStandardOperatorAnd (StandardOperator.And x);
  public StandardOperator
    visItStandardOperatorGreaterThanOrEq (StandardOperator.GreaterThanOrEq x);
  public StandardOperator visItStandardOperatorGreaterThan (StandardOperator.
							    GreaterThan x);
  public StandardOperator visItStandardOperatorLessThanOrEq (StandardOperator.
							     LessThanOrEq x);
  public StandardOperator visItStandardOperatorLessThan (StandardOperator.
							 LessThan x);
  public StandardOperator visItStandardOperatorNotEquals (StandardOperator.
							  NotEquals x);
  public StandardOperator visItStandardOperatorEquals (StandardOperator.
						       Equals x);
  public StandardOperator visItStandardOperatorIntersection (StandardOperator.
							     Intersection x);
  public StandardOperator visItStandardOperatorDivisIon (StandardOperator.
							 DivisIon x);
  public StandardOperator visItStandardOperatorProduct (StandardOperator.
							Product x);
  public StandardOperator visItStandardOperatorSubstraction (StandardOperator.
							     Substraction x);
  public StandardOperator visItStandardOperatorAddition (StandardOperator.
							 Addition x);
  public FunctionName visItFunctionNameOperator (FunctionName.Operator x);
  public FunctionName visItFunctionNameName (FunctionName.Name x);
  public FunctionModifier visItFunctionModifierJava (FunctionModifier.Java x);
  public FunctionModifiers visItFunctionModifiersLisT (FunctionModifiers.
						       LisT x);
  public Signature visItSignatureWithThrows (Signature.WithThrows x);
  public Signature visItSignatureNoThrows (Signature.NoThrows x);
  public FunctionDeclaration
    visItFunctionDeclarationAbstract (FunctionDeclaration.Abstract x);
  public FunctionDeclaration
    visItFunctionDeclarationDefault (FunctionDeclaration.Default x);
  public FunctionBody visItFunctionBodyDefault (FunctionBody.Default x);
  public Variable visItVariableGivenInitialization (Variable.
						    GivenInitialization x);
  public Kind visItKindAll (Kind.All x);
  public Kind visItKindTag (Kind.Tag x);
  public Kind visItKindAnno (Kind.Anno x);
  public Kind visItKindType (Kind.Type x);
  public Kind visItKindView (Kind.View x);
  public Kind visItKindData (Kind.Data x);
  public Kind visItKindVariable (Kind.Variable x);
  public Kind visItKindFunction (Kind.Function x);
  public Kind visItKindModule (Kind.Module x);
  public ValueProducer visItValueProducerGivenStrategy (ValueProducer.
							GivenStrategy x);
  public ValueProducer visItValueProducerDefaultStrategy (ValueProducer.
							  DefaultStrategy x);
  public Generator visItGeneratorProducer (Generator.Producer x);
  public Generator visItGeneratorExpression (Generator.Expression x);
  public Strategy visItStrategyInnermost (Strategy.Innermost x);
  public Strategy visItStrategyOutermost (Strategy.Outermost x);
  public Strategy visItStrategyBottomUpBreak (Strategy.BottomUpBreak x);
  public Strategy visItStrategyBottomUp (Strategy.BottomUp x);
  public Strategy visItStrategyTopDownBreak (Strategy.TopDownBreak x);
  public Strategy visItStrategyTopDown (Strategy.TopDown x);
  public Comprehension visItComprehensionLisT (Comprehension.LisT x);
  public Comprehension visItComprehensionSet (Comprehension.Set x);
  public Match visItMatchArbitrary (Match.Arbitrary x);
  public Match visItMatchReplacing (Match.Replacing x);
  public Rule visItRuleNoGuard (Rule.NoGuard x);
  public Rule visItRuleWithGuard (Rule.WithGuard x);
  public Case visItCaseDefault (Case.Default x);
  public Case visItCaseRule (Case.Rule x);
  public VisIt visItVisItGivenStrategy (VisIt.GivenStrategy x);
  public VisIt visItVisItDefaultStrategy (VisIt.DefaultStrategy x);
}
