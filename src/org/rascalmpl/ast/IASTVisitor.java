/*******************************************************************************
 * Copyright (c) 2009-2015 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
 *   * Michael Steindorfer - Michael.Steindorfer@cwi.nl - CWI
 *******************************************************************************/
package org.rascalmpl.ast;

public interface IASTVisitor<T> {

  public T visitAssignableAnnotation(Assignable.Annotation x);

  public T visitAssignableBracket(Assignable.Bracket x);

  public T visitAssignableConstructor(Assignable.Constructor x);

  public T visitAssignableFieldAccess(Assignable.FieldAccess x);

  public T visitAssignableIfDefinedOrDefault(Assignable.IfDefinedOrDefault x);

  public T visitAssignableSlice(Assignable.Slice x);

  public T visitAssignableSliceStep(Assignable.SliceStep x);

  public T visitAssignableSubscript(Assignable.Subscript x);

  public T visitAssignableTuple(Assignable.Tuple x);

  public T visitAssignableVariable(Assignable.Variable x);

  public T visitAssignmentAddition(Assignment.Addition x);

  public T visitAssignmentAppend(Assignment.Append x);

  public T visitAssignmentDefault(Assignment.Default x);

  public T visitAssignmentDivision(Assignment.Division x);

  public T visitAssignmentIfDefined(Assignment.IfDefined x);

  public T visitAssignmentIntersection(Assignment.Intersection x);

  public T visitAssignmentProduct(Assignment.Product x);

  public T visitAssignmentSubtraction(Assignment.Subtraction x);

  public T visitAssocAssociative(Assoc.Associative x);

  public T visitAssocLeft(Assoc.Left x);

  public T visitAssocNonAssociative(Assoc.NonAssociative x);

  public T visitAssocRight(Assoc.Right x);

  public T visitBasicTypeBag(BasicType.Bag x);

  public T visitBasicTypeBool(BasicType.Bool x);

  public T visitBasicTypeDateTime(BasicType.DateTime x);

  public T visitBasicTypeInt(BasicType.Int x);

  public T visitBasicTypeList(BasicType.List x);

  public T visitBasicTypeListRelation(BasicType.ListRelation x);

  public T visitBasicTypeLoc(BasicType.Loc x);

  public T visitBasicTypeMap(BasicType.Map x);

  public T visitBasicTypeNode(BasicType.Node x);

  public T visitBasicTypeNum(BasicType.Num x);

  public T visitBasicTypeRational(BasicType.Rational x);

  public T visitBasicTypeReal(BasicType.Real x);

  public T visitBasicTypeRelation(BasicType.Relation x);

  public T visitBasicTypeSet(BasicType.Set x);

  public T visitBasicTypeString(BasicType.String x);

  public T visitBasicTypeTuple(BasicType.Tuple x);

  public T visitBasicTypeType(BasicType.Type x);

  public T visitBasicTypeValue(BasicType.Value x);

  public T visitBasicTypeVoid(BasicType.Void x);

  public T visitBodyToplevels(Body.Toplevels x);

  public T visitBoundDefault(Bound.Default x);

  public T visitBoundEmpty(Bound.Empty x);

  public T visitCaseDefault(Case.Default x);

  public T visitCasePatternWithAction(Case.PatternWithAction x);

  public T visitCatchBinding(Catch.Binding x);

  public T visitCatchDefault(Catch.Default x);

  public T visitClassBracket(Class.Bracket x);

  public T visitClassComplement(Class.Complement x);

  public T visitClassDifference(Class.Difference x);

  public T visitClassIntersection(Class.Intersection x);

  public T visitClassSimpleCharclass(Class.SimpleCharclass x);

  public T visitClassUnion(Class.Union x);

  public T visitCommandDeclaration(Command.Declaration x);

  public T visitCommandExpression(Command.Expression x);

  public T visitCommandImport(Command.Import x);

  public T visitCommandShell(Command.Shell x);

  public T visitCommandStatement(Command.Statement x);

  public T visitCommandsCommandlist(Commands.Commandlist x);

  public T visitCommonKeywordParametersAbsent(CommonKeywordParameters.Absent x);

  public T visitCommonKeywordParametersPresent(CommonKeywordParameters.Present x);

  public T visitComprehensionList(Comprehension.List x);

  public T visitComprehensionMap(Comprehension.Map x);

  public T visitComprehensionSet(Comprehension.Set x);

  public T visitConcreteHoleOne(ConcreteHole.One x);

  public T visitDataTargetEmpty(DataTarget.Empty x);

  public T visitDataTargetLabeled(DataTarget.Labeled x);

  public T visitDataTypeSelectorSelector(DataTypeSelector.Selector x);

  public T visitDateTimeLiteralDateAndTimeLiteral(DateTimeLiteral.DateAndTimeLiteral x);

  public T visitDateTimeLiteralDateLiteral(DateTimeLiteral.DateLiteral x);

  public T visitDateTimeLiteralTimeLiteral(DateTimeLiteral.TimeLiteral x);

  public T visitDeclarationAlias(Declaration.Alias x);

  public T visitDeclarationAnnotation(Declaration.Annotation x);

  public T visitDeclarationData(Declaration.Data x);

  public T visitDeclarationDataAbstract(Declaration.DataAbstract x);

  public T visitDeclarationFunction(Declaration.Function x);

  public T visitDeclarationTag(Declaration.Tag x);

  public T visitDeclarationVariable(Declaration.Variable x);

  public T visitDeclaratorDefault(Declarator.Default x);

  public T visitEvalCommandDeclaration(EvalCommand.Declaration x);

  public T visitEvalCommandImport(EvalCommand.Import x);

  public T visitEvalCommandOutput(EvalCommand.Output x);

  public T visitEvalCommandStatement(EvalCommand.Statement x);

  public T visitExpressionAddition(Expression.Addition x);

  public T visitExpressionAll(Expression.All x);

  public T visitExpressionAnd(Expression.And x);

  public T visitExpressionAnti(Expression.Anti x);

  public T visitExpressionAny(Expression.Any x);

  public T visitExpressionAppendAfter(Expression.AppendAfter x);

  public T visitExpressionAsType(Expression.AsType x);

  public T visitExpressionBracket(Expression.Bracket x);

  public T visitExpressionCallOrTree(Expression.CallOrTree x);

  public T visitExpressionClosure(Expression.Closure x);

  public T visitExpressionComposition(Expression.Composition x);

  public T visitExpressionComprehension(Expression.Comprehension x);

  public T visitExpressionConcrete(Expression.Concrete x);

  public T visitExpressionDescendant(Expression.Descendant x);

  public T visitExpressionDivision(Expression.Division x);

  public T visitExpressionEnumerator(Expression.Enumerator x);

  public T visitExpressionEquals(Expression.Equals x);

  public T visitExpressionEquivalence(Expression.Equivalence x);

  public T visitExpressionFieldAccess(Expression.FieldAccess x);

  public T visitExpressionFieldProject(Expression.FieldProject x);

  public T visitExpressionFieldUpdate(Expression.FieldUpdate x);

  public T visitExpressionGetAnnotation(Expression.GetAnnotation x);

  public T visitExpressionGreaterThan(Expression.GreaterThan x);

  public T visitExpressionGreaterThanOrEq(Expression.GreaterThanOrEq x);

  public T visitExpressionHas(Expression.Has x);

  public T visitExpressionIfDefinedOtherwise(Expression.IfDefinedOtherwise x);

  public T visitExpressionIfThenElse(Expression.IfThenElse x);

  public T visitExpressionImplication(Expression.Implication x);

  public T visitExpressionIn(Expression.In x);

  public T visitExpressionInsertBefore(Expression.InsertBefore x);

  public T visitExpressionIntersection(Expression.Intersection x);

  public T visitExpressionIs(Expression.Is x);

  public T visitExpressionIsDefined(Expression.IsDefined x);

  public T visitExpressionIt(Expression.It x);

  public T visitExpressionJoin(Expression.Join x);

  public T visitExpressionLessThan(Expression.LessThan x);

  public T visitExpressionLessThanOrEq(Expression.LessThanOrEq x);

  public T visitExpressionList(Expression.List x);

  public T visitExpressionLiteral(Expression.Literal x);

  public T visitExpressionMap(Expression.Map x);

  public T visitExpressionMatch(Expression.Match x);

  public T visitExpressionModulo(Expression.Modulo x);

  public T visitExpressionMultiVariable(Expression.MultiVariable x);

  public T visitExpressionNegation(Expression.Negation x);

  public T visitExpressionNegative(Expression.Negative x);

  public T visitExpressionNoMatch(Expression.NoMatch x);

  public T visitExpressionNonEmptyBlock(Expression.NonEmptyBlock x);

  public T visitExpressionNonEquals(Expression.NonEquals x);

  public T visitExpressionNotIn(Expression.NotIn x);

  public T visitExpressionOr(Expression.Or x);

  public T visitExpressionProduct(Expression.Product x);

  public T visitExpressionQualifiedName(Expression.QualifiedName x);

  public T visitExpressionRange(Expression.Range x);

  public T visitExpressionReducer(Expression.Reducer x);

  public T visitExpressionReifiedType(Expression.ReifiedType x);

  public T visitExpressionReifyType(Expression.ReifyType x);

  public T visitExpressionRemainder(Expression.Remainder x);

  public T visitExpressionSet(Expression.Set x);

  public T visitExpressionSetAnnotation(Expression.SetAnnotation x);

  public T visitExpressionSlice(Expression.Slice x);

  public T visitExpressionSliceStep(Expression.SliceStep x);

  public T visitExpressionSplice(Expression.Splice x);

  public T visitExpressionSplicePlus(Expression.SplicePlus x);

  public T visitExpressionStepRange(Expression.StepRange x);

  public T visitExpressionSubscript(Expression.Subscript x);

  public T visitExpressionSubtraction(Expression.Subtraction x);

  public T visitExpressionTransitiveClosure(Expression.TransitiveClosure x);

  public T visitExpressionTransitiveReflexiveClosure(Expression.TransitiveReflexiveClosure x);

  public T visitExpressionTuple(Expression.Tuple x);

  public T visitExpressionTuple(Expression.Tuple x);

  public T visitExpressionTypedVariable(Expression.TypedVariable x);

  public T visitExpressionTypedVariableBecomes(Expression.TypedVariableBecomes x);

  public T visitExpressionVariableBecomes(Expression.VariableBecomes x);

  public T visitExpressionVisit(Expression.Visit x);

  public T visitExpressionVoidClosure(Expression.VoidClosure x);

  public T visitFieldIndex(Field.Index x);

  public T visitFieldName(Field.Name x);

  public T visitFormalsDefault(Formals.Default x);

  public T visitFunctionBodyDefault(FunctionBody.Default x);

  public T visitFunctionDeclarationAbstract(FunctionDeclaration.Abstract x);

  public T visitFunctionDeclarationConditional(FunctionDeclaration.Conditional x);

  public T visitFunctionDeclarationDefault(FunctionDeclaration.Default x);

  public T visitFunctionDeclarationExpression(FunctionDeclaration.Expression x);

  public T visitFunctionModifierDefault(FunctionModifier.Default x);

  public T visitFunctionModifierJava(FunctionModifier.Java x);

  public T visitFunctionModifierTest(FunctionModifier.Test x);

  public T visitFunctionModifiersModifierlist(FunctionModifiers.Modifierlist x);

  public T visitFunctionTypeTypeArguments(FunctionType.TypeArguments x);

  public T visitHeaderDefault(Header.Default x);

  public T visitHeaderParameters(Header.Parameters x);

  public T visitImportDefault(Import.Default x);

  public T visitImportExtend(Import.Extend x);

  public T visitImportExternal(Import.External x);

  public T visitImportSyntax(Import.Syntax x);

  public T visitImportedModuleActuals(ImportedModule.Actuals x);

  public T visitImportedModuleActualsRenaming(ImportedModule.ActualsRenaming x);

  public T visitImportedModuleDefault(ImportedModule.Default x);

  public T visitImportedModuleRenamings(ImportedModule.Renamings x);

  public T visitIntegerLiteralDecimalIntegerLiteral(IntegerLiteral.DecimalIntegerLiteral x);

  public T visitIntegerLiteralHexIntegerLiteral(IntegerLiteral.HexIntegerLiteral x);

  public T visitIntegerLiteralOctalIntegerLiteral(IntegerLiteral.OctalIntegerLiteral x);

  public T visitKeywordArgument_ExpressionDefault(KeywordArgument_Expression.Default x);

  public T visitKeywordArguments_ExpressionDefault(KeywordArguments_Expression.Default x);

  public T visitKeywordArguments_ExpressionNone(KeywordArguments_Expression.None x);

  public T visitKeywordFormalDefault(KeywordFormal.Default x);

  public T visitKeywordFormalsDefault(KeywordFormals.Default x);

  public T visitKeywordFormalsNone(KeywordFormals.None x);

  public T visitKindAlias(Kind.Alias x);

  public T visitKindAll(Kind.All x);

  public T visitKindAnno(Kind.Anno x);

  public T visitKindData(Kind.Data x);

  public T visitKindFunction(Kind.Function x);

  public T visitKindModule(Kind.Module x);

  public T visitKindTag(Kind.Tag x);

  public T visitKindVariable(Kind.Variable x);

  public T visitKindView(Kind.View x);

  public T visitLabelDefault(Label.Default x);

  public T visitLabelEmpty(Label.Empty x);

  public T visitLiteralBoolean(Literal.Boolean x);

  public T visitLiteralDateTime(Literal.DateTime x);

  public T visitLiteralInteger(Literal.Integer x);

  public T visitLiteralLocation(Literal.Location x);

  public T visitLiteralRational(Literal.Rational x);

  public T visitLiteralReal(Literal.Real x);

  public T visitLiteralRegExp(Literal.RegExp x);

  public T visitLiteralString(Literal.String x);

  public T visitLocalVariableDeclarationDefault(LocalVariableDeclaration.Default x);

  public T visitLocalVariableDeclarationDynamic(LocalVariableDeclaration.Dynamic x);

  public T visitLocationLiteralDefault(LocationLiteral.Default x);

  public T visitMapping_ExpressionDefault(Mapping_Expression.Default x);

  public T visitModuleDefault(Module.Default x);

  public T visitModuleActualsDefault(ModuleActuals.Default x);

  public T visitModuleParametersDefault(ModuleParameters.Default x);

  public T visitOptionalExpressionExpression(OptionalExpression.Expression x);

  public T visitOptionalExpressionNoExpression(OptionalExpression.NoExpression x);

  public T visitParametersDefault(Parameters.Default x);

  public T visitParametersVarArgs(Parameters.VarArgs x);

  public T visitPathPartInterpolated(PathPart.Interpolated x);

  public T visitPathPartNonInterpolated(PathPart.NonInterpolated x);

  public T visitPathTailMid(PathTail.Mid x);

  public T visitPathTailPost(PathTail.Post x);

  public T visitPatternWithActionArbitrary(PatternWithAction.Arbitrary x);

  public T visitPatternWithActionReplacing(PatternWithAction.Replacing x);

  public T visitProdAll(Prod.All x);

  public T visitProdAssociativityGroup(Prod.AssociativityGroup x);

  public T visitProdFirst(Prod.First x);

  public T visitProdLabeled(Prod.Labeled x);

  public T visitProdReference(Prod.Reference x);

  public T visitProdUnlabeled(Prod.Unlabeled x);

  public T visitProdModifierAssociativity(ProdModifier.Associativity x);

  public T visitProdModifierBracket(ProdModifier.Bracket x);

  public T visitProdModifierTag(ProdModifier.Tag x);

  public T visitProtocolPartInterpolated(ProtocolPart.Interpolated x);

  public T visitProtocolPartNonInterpolated(ProtocolPart.NonInterpolated x);

  public T visitProtocolTailMid(ProtocolTail.Mid x);

  public T visitProtocolTailPost(ProtocolTail.Post x);

  public T visitQualifiedNameDefault(QualifiedName.Default x);

  public T visitRangeCharacter(Range.Character x);

  public T visitRangeFromTo(Range.FromTo x);

  public T visitRenamingDefault(Renaming.Default x);

  public T visitRenamingsDefault(Renamings.Default x);

  public T visitReplacementConditional(Replacement.Conditional x);

  public T visitReplacementUnconditional(Replacement.Unconditional x);

  public T visitShellCommandClear(ShellCommand.Clear x);

  public T visitShellCommandEdit(ShellCommand.Edit x);

  public T visitShellCommandHelp(ShellCommand.Help x);

  public T visitShellCommandHistory(ShellCommand.History x);

  public T visitShellCommandListDeclarations(ShellCommand.ListDeclarations x);

  public T visitShellCommandListModules(ShellCommand.ListModules x);

  public T visitShellCommandQuit(ShellCommand.Quit x);

  public T visitShellCommandSetOption(ShellCommand.SetOption x);

  public T visitShellCommandTest(ShellCommand.Test x);

  public T visitShellCommandUndeclare(ShellCommand.Undeclare x);

  public T visitShellCommandUnimport(ShellCommand.Unimport x);

  public T visitSignatureNoThrows(Signature.NoThrows x);

  public T visitSignatureWithThrows(Signature.WithThrows x);

  public T visitStartAbsent(Start.Absent x);

  public T visitStartPresent(Start.Present x);

  public T visitStatementAppend(Statement.Append x);

  public T visitStatementAssert(Statement.Assert x);

  public T visitStatementAssertWithMessage(Statement.AssertWithMessage x);

  public T visitStatementAssignment(Statement.Assignment x);

  public T visitStatementBreak(Statement.Break x);

  public T visitStatementContinue(Statement.Continue x);

  public T visitStatementDoWhile(Statement.DoWhile x);

  public T visitStatementEmptyStatement(Statement.EmptyStatement x);

  public T visitStatementExpression(Statement.Expression x);

  public T visitStatementFail(Statement.Fail x);

  public T visitStatementFilter(Statement.Filter x);

  public T visitStatementFor(Statement.For x);

  public T visitStatementFunctionDeclaration(Statement.FunctionDeclaration x);

  public T visitStatementGlobalDirective(Statement.GlobalDirective x);

  public T visitStatementIfThen(Statement.IfThen x);

  public T visitStatementIfThenElse(Statement.IfThenElse x);

  public T visitStatementInsert(Statement.Insert x);

  public T visitStatementNonEmptyBlock(Statement.NonEmptyBlock x);

  public T visitStatementReturn(Statement.Return x);

  public T visitStatementSolve(Statement.Solve x);

  public T visitStatementSwitch(Statement.Switch x);

  public T visitStatementThrow(Statement.Throw x);

  public T visitStatementTry(Statement.Try x);

  public T visitStatementTryFinally(Statement.TryFinally x);

  public T visitStatementVariableDeclaration(Statement.VariableDeclaration x);

  public T visitStatementVisit(Statement.Visit x);

  public T visitStatementWhile(Statement.While x);

  public T visitStrategyBottomUp(Strategy.BottomUp x);

  public T visitStrategyBottomUpBreak(Strategy.BottomUpBreak x);

  public T visitStrategyInnermost(Strategy.Innermost x);

  public T visitStrategyOutermost(Strategy.Outermost x);

  public T visitStrategyTopDown(Strategy.TopDown x);

  public T visitStrategyTopDownBreak(Strategy.TopDownBreak x);

  public T visitStringLiteralInterpolated(StringLiteral.Interpolated x);

  public T visitStringLiteralNonInterpolated(StringLiteral.NonInterpolated x);

  public T visitStringLiteralTemplate(StringLiteral.Template x);

  public T visitStringMiddleInterpolated(StringMiddle.Interpolated x);

  public T visitStringMiddleMid(StringMiddle.Mid x);

  public T visitStringMiddleTemplate(StringMiddle.Template x);

  public T visitStringTailMidInterpolated(StringTail.MidInterpolated x);

  public T visitStringTailMidTemplate(StringTail.MidTemplate x);

  public T visitStringTailPost(StringTail.Post x);

  public T visitStringTemplateDoWhile(StringTemplate.DoWhile x);

  public T visitStringTemplateFor(StringTemplate.For x);

  public T visitStringTemplateIfThen(StringTemplate.IfThen x);

  public T visitStringTemplateIfThenElse(StringTemplate.IfThenElse x);

  public T visitStringTemplateWhile(StringTemplate.While x);

  public T visitStructuredTypeDefault(StructuredType.Default x);

  public T visitSymAlternative(Sym.Alternative x);

  public T visitSymCaseInsensitiveLiteral(Sym.CaseInsensitiveLiteral x);

  public T visitSymCharacterClass(Sym.CharacterClass x);

  public T visitSymColumn(Sym.Column x);

  public T visitSymEmpty(Sym.Empty x);

  public T visitSymEndOfLine(Sym.EndOfLine x);

  public T visitSymExcept(Sym.Except x);

  public T visitSymFollow(Sym.Follow x);

  public T visitSymIter(Sym.Iter x);

  public T visitSymIterSep(Sym.IterSep x);

  public T visitSymIterStar(Sym.IterStar x);

  public T visitSymIterStarSep(Sym.IterStarSep x);

  public T visitSymLabeled(Sym.Labeled x);

  public T visitSymLiteral(Sym.Literal x);

  public T visitSymNonterminal(Sym.Nonterminal x);

  public T visitSymNotFollow(Sym.NotFollow x);

  public T visitSymNotPrecede(Sym.NotPrecede x);

  public T visitSymOptional(Sym.Optional x);

  public T visitSymParameter(Sym.Parameter x);

  public T visitSymParametrized(Sym.Parametrized x);

  public T visitSymPrecede(Sym.Precede x);

  public T visitSymSequence(Sym.Sequence x);

  public T visitSymStart(Sym.Start x);

  public T visitSymStartOfLine(Sym.StartOfLine x);

  public T visitSymUnequal(Sym.Unequal x);

  public T visitSyntaxDefinitionKeyword(SyntaxDefinition.Keyword x);

  public T visitSyntaxDefinitionLanguage(SyntaxDefinition.Language x);

  public T visitSyntaxDefinitionLayout(SyntaxDefinition.Layout x);

  public T visitSyntaxDefinitionLexical(SyntaxDefinition.Lexical x);

  public T visitTagDefault(Tag.Default x);

  public T visitTagEmpty(Tag.Empty x);

  public T visitTagExpression(Tag.Expression x);

  public T visitTagsDefault(Tags.Default x);

  public T visitTargetEmpty(Target.Empty x);

  public T visitTargetLabeled(Target.Labeled x);

  public T visitToplevelGivenVisibility(Toplevel.GivenVisibility x);

  public T visitTypeBasic(Type.Basic x);

  public T visitTypeBracket(Type.Bracket x);

  public T visitTypeFunction(Type.Function x);

  public T visitTypeSelector(Type.Selector x);

  public T visitTypeStructured(Type.Structured x);

  public T visitTypeSymbol(Type.Symbol x);

  public T visitTypeUser(Type.User x);

  public T visitTypeVariable(Type.Variable x);

  public T visitTypeArgDefault(TypeArg.Default x);

  public T visitTypeArgNamed(TypeArg.Named x);

  public T visitTypeVarBounded(TypeVar.Bounded x);

  public T visitTypeVarFree(TypeVar.Free x);

  public T visitUserTypeName(UserType.Name x);

  public T visitUserTypeParametric(UserType.Parametric x);

  public T visitVariableInitialized(Variable.Initialized x);

  public T visitVariableUnInitialized(Variable.UnInitialized x);

  public T visitVariantNAryConstructor(Variant.NAryConstructor x);

  public T visitVisibilityDefault(Visibility.Default x);

  public T visitVisibilityPrivate(Visibility.Private x);

  public T visitVisibilityPublic(Visibility.Public x);

  public T visitVisitDefaultStrategy(Visit.DefaultStrategy x);

  public T visitVisitGivenStrategy(Visit.GivenStrategy x);


  public T visitBackslashLexical(Backslash.Lexical x);

  public T visitBooleanLiteralLexical(BooleanLiteral.Lexical x);

  public T visitCaseInsensitiveStringConstantLexical(CaseInsensitiveStringConstant.Lexical x);

  public T visitCharLexical(Char.Lexical x);

  public T visitCommentLexical(Comment.Lexical x);

  public T visitConcreteLexical(Concrete.Lexical x);

  public T visitConcretePartLexical(ConcretePart.Lexical x);

  public T visitDateAndTimeLexical(DateAndTime.Lexical x);

  public T visitDatePartLexical(DatePart.Lexical x);

  public T visitDecimalIntegerLiteralLexical(DecimalIntegerLiteral.Lexical x);

  public T visitHexIntegerLiteralLexical(HexIntegerLiteral.Lexical x);

  public T visitJustDateLexical(JustDate.Lexical x);

  public T visitJustTimeLexical(JustTime.Lexical x);

  public T visitLAYOUTLexical(LAYOUT.Lexical x);

  public T visitMidPathCharsLexical(MidPathChars.Lexical x);

  public T visitMidProtocolCharsLexical(MidProtocolChars.Lexical x);

  public T visitMidStringCharsLexical(MidStringChars.Lexical x);

  public T visitNameLexical(Name.Lexical x);

  public T visitNamedBackslashLexical(NamedBackslash.Lexical x);

  public T visitNamedRegExpLexical(NamedRegExp.Lexical x);

  public T visitNonterminalLexical(Nonterminal.Lexical x);

  public T visitNonterminalLabelLexical(NonterminalLabel.Lexical x);

  public T visitOctalIntegerLiteralLexical(OctalIntegerLiteral.Lexical x);

  public T visitOptionalCommaLexical(OptionalComma.Lexical x);

  public T visitOutputLexical(Output.Lexical x);

  public T visitPathCharsLexical(PathChars.Lexical x);

  public T visitPostPathCharsLexical(PostPathChars.Lexical x);

  public T visitPostProtocolCharsLexical(PostProtocolChars.Lexical x);

  public T visitPostStringCharsLexical(PostStringChars.Lexical x);

  public T visitPrePathCharsLexical(PrePathChars.Lexical x);

  public T visitPreProtocolCharsLexical(PreProtocolChars.Lexical x);

  public T visitPreStringCharsLexical(PreStringChars.Lexical x);

  public T visitProtocolCharsLexical(ProtocolChars.Lexical x);

  public T visitRationalLiteralLexical(RationalLiteral.Lexical x);

  public T visitRealLiteralLexical(RealLiteral.Lexical x);

  public T visitRegExpLexical(RegExp.Lexical x);

  public T visitRegExpLiteralLexical(RegExpLiteral.Lexical x);

  public T visitRegExpModifierLexical(RegExpModifier.Lexical x);

  public T visitStringCharacterLexical(StringCharacter.Lexical x);

  public T visitStringConstantLexical(StringConstant.Lexical x);

  public T visitTagStringLexical(TagString.Lexical x);

  public T visitTimePartNoTZLexical(TimePartNoTZ.Lexical x);

  public T visitTimeZonePartLexical(TimeZonePart.Lexical x);

  public T visitURLCharsLexical(URLChars.Lexical x);

  public T visitUnicodeEscapeLexical(UnicodeEscape.Lexical x);

}