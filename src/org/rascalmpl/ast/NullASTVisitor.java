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

public class NullASTVisitor<T> implements IASTVisitor<T> {

  public T visitAssignableAnnotation(Assignable.Annotation x) { 
    return null; 
  }

  public T visitAssignableBracket(Assignable.Bracket x) { 
    return null; 
  }

  public T visitAssignableConstructor(Assignable.Constructor x) { 
    return null; 
  }

  public T visitAssignableFieldAccess(Assignable.FieldAccess x) { 
    return null; 
  }

  public T visitAssignableIfDefinedOrDefault(Assignable.IfDefinedOrDefault x) { 
    return null; 
  }

  public T visitAssignableSlice(Assignable.Slice x) { 
    return null; 
  }

  public T visitAssignableSliceStep(Assignable.SliceStep x) { 
    return null; 
  }

  public T visitAssignableSubscript(Assignable.Subscript x) { 
    return null; 
  }

  public T visitAssignableTuple(Assignable.Tuple x) { 
    return null; 
  }

  public T visitAssignableVariable(Assignable.Variable x) { 
    return null; 
  }

  public T visitAssignmentAddition(Assignment.Addition x) { 
    return null; 
  }

  public T visitAssignmentAppend(Assignment.Append x) { 
    return null; 
  }

  public T visitAssignmentDefault(Assignment.Default x) { 
    return null; 
  }

  public T visitAssignmentDivision(Assignment.Division x) { 
    return null; 
  }

  public T visitAssignmentIfDefined(Assignment.IfDefined x) { 
    return null; 
  }

  public T visitAssignmentIntersection(Assignment.Intersection x) { 
    return null; 
  }

  public T visitAssignmentProduct(Assignment.Product x) { 
    return null; 
  }

  public T visitAssignmentSubtraction(Assignment.Subtraction x) { 
    return null; 
  }

  public T visitAssocAssociative(Assoc.Associative x) { 
    return null; 
  }

  public T visitAssocLeft(Assoc.Left x) { 
    return null; 
  }

  public T visitAssocNonAssociative(Assoc.NonAssociative x) { 
    return null; 
  }

  public T visitAssocRight(Assoc.Right x) { 
    return null; 
  }

  public T visitBasicTypeBag(BasicType.Bag x) { 
    return null; 
  }

  public T visitBasicTypeBool(BasicType.Bool x) { 
    return null; 
  }

  public T visitBasicTypeDateTime(BasicType.DateTime x) { 
    return null; 
  }

  public T visitBasicTypeInt(BasicType.Int x) { 
    return null; 
  }

  public T visitBasicTypeList(BasicType.List x) { 
    return null; 
  }

  public T visitBasicTypeListRelation(BasicType.ListRelation x) { 
    return null; 
  }

  public T visitBasicTypeLoc(BasicType.Loc x) { 
    return null; 
  }

  public T visitBasicTypeMap(BasicType.Map x) { 
    return null; 
  }

  public T visitBasicTypeNode(BasicType.Node x) { 
    return null; 
  }

  public T visitBasicTypeNum(BasicType.Num x) { 
    return null; 
  }

  public T visitBasicTypeRational(BasicType.Rational x) { 
    return null; 
  }

  public T visitBasicTypeReal(BasicType.Real x) { 
    return null; 
  }

  public T visitBasicTypeRelation(BasicType.Relation x) { 
    return null; 
  }

  public T visitBasicTypeSet(BasicType.Set x) { 
    return null; 
  }

  public T visitBasicTypeString(BasicType.String x) { 
    return null; 
  }

  public T visitBasicTypeTuple(BasicType.Tuple x) { 
    return null; 
  }

  public T visitBasicTypeType(BasicType.Type x) { 
    return null; 
  }

  public T visitBasicTypeValue(BasicType.Value x) { 
    return null; 
  }

  public T visitBasicTypeVoid(BasicType.Void x) { 
    return null; 
  }

  public T visitBodyToplevels(Body.Toplevels x) { 
    return null; 
  }

  public T visitBoundDefault(Bound.Default x) { 
    return null; 
  }

  public T visitBoundEmpty(Bound.Empty x) { 
    return null; 
  }

  public T visitCaseDefault(Case.Default x) { 
    return null; 
  }

  public T visitCasePatternWithAction(Case.PatternWithAction x) { 
    return null; 
  }

  public T visitCatchBinding(Catch.Binding x) { 
    return null; 
  }

  public T visitCatchDefault(Catch.Default x) { 
    return null; 
  }

  public T visitClassBracket(Class.Bracket x) { 
    return null; 
  }

  public T visitClassComplement(Class.Complement x) { 
    return null; 
  }

  public T visitClassDifference(Class.Difference x) { 
    return null; 
  }

  public T visitClassIntersection(Class.Intersection x) { 
    return null; 
  }

  public T visitClassSimpleCharclass(Class.SimpleCharclass x) { 
    return null; 
  }

  public T visitClassUnion(Class.Union x) { 
    return null; 
  }

  public T visitCommandDeclaration(Command.Declaration x) { 
    return null; 
  }

  public T visitCommandExpression(Command.Expression x) { 
    return null; 
  }

  public T visitCommandImport(Command.Import x) { 
    return null; 
  }

  public T visitCommandShell(Command.Shell x) { 
    return null; 
  }

  public T visitCommandStatement(Command.Statement x) { 
    return null; 
  }

  public T visitCommandsCommandlist(Commands.Commandlist x) { 
    return null; 
  }

  public T visitCommonKeywordParametersAbsent(CommonKeywordParameters.Absent x) { 
    return null; 
  }

  public T visitCommonKeywordParametersPresent(CommonKeywordParameters.Present x) { 
    return null; 
  }

  public T visitComprehensionList(Comprehension.List x) { 
    return null; 
  }

  public T visitComprehensionMap(Comprehension.Map x) { 
    return null; 
  }

  public T visitComprehensionSet(Comprehension.Set x) { 
    return null; 
  }

  public T visitConcreteHoleOne(ConcreteHole.One x) { 
    return null; 
  }

  public T visitDataTargetEmpty(DataTarget.Empty x) { 
    return null; 
  }

  public T visitDataTargetLabeled(DataTarget.Labeled x) { 
    return null; 
  }

  public T visitDataTypeSelectorSelector(DataTypeSelector.Selector x) { 
    return null; 
  }

  public T visitDateTimeLiteralDateAndTimeLiteral(DateTimeLiteral.DateAndTimeLiteral x) { 
    return null; 
  }

  public T visitDateTimeLiteralDateLiteral(DateTimeLiteral.DateLiteral x) { 
    return null; 
  }

  public T visitDateTimeLiteralTimeLiteral(DateTimeLiteral.TimeLiteral x) { 
    return null; 
  }

  public T visitDeclarationAlias(Declaration.Alias x) { 
    return null; 
  }

  public T visitDeclarationAnnotation(Declaration.Annotation x) { 
    return null; 
  }

  public T visitDeclarationData(Declaration.Data x) { 
    return null; 
  }

  public T visitDeclarationDataAbstract(Declaration.DataAbstract x) { 
    return null; 
  }

  public T visitDeclarationFunction(Declaration.Function x) { 
    return null; 
  }

  public T visitDeclarationTag(Declaration.Tag x) { 
    return null; 
  }

  public T visitDeclarationVariable(Declaration.Variable x) { 
    return null; 
  }

  public T visitDeclaratorDefault(Declarator.Default x) { 
    return null; 
  }

  public T visitEvalCommandDeclaration(EvalCommand.Declaration x) { 
    return null; 
  }

  public T visitEvalCommandImport(EvalCommand.Import x) { 
    return null; 
  }

  public T visitEvalCommandOutput(EvalCommand.Output x) { 
    return null; 
  }

  public T visitEvalCommandStatement(EvalCommand.Statement x) { 
    return null; 
  }

  public T visitExpressionAddition(Expression.Addition x) { 
    return null; 
  }

  public T visitExpressionAll(Expression.All x) { 
    return null; 
  }

  public T visitExpressionAnd(Expression.And x) { 
    return null; 
  }

  public T visitExpressionAnti(Expression.Anti x) { 
    return null; 
  }

  public T visitExpressionAny(Expression.Any x) { 
    return null; 
  }

  public T visitExpressionAppendAfter(Expression.AppendAfter x) { 
    return null; 
  }

  public T visitExpressionAsType(Expression.AsType x) { 
    return null; 
  }

  public T visitExpressionBracket(Expression.Bracket x) { 
    return null; 
  }

  public T visitExpressionCallOrTree(Expression.CallOrTree x) { 
    return null; 
  }

  public T visitExpressionClosure(Expression.Closure x) { 
    return null; 
  }

  public T visitExpressionComposition(Expression.Composition x) { 
    return null; 
  }

  public T visitExpressionComprehension(Expression.Comprehension x) { 
    return null; 
  }

  public T visitExpressionConcrete(Expression.Concrete x) { 
    return null; 
  }

  public T visitExpressionDescendant(Expression.Descendant x) { 
    return null; 
  }

  public T visitExpressionDivision(Expression.Division x) { 
    return null; 
  }

  public T visitExpressionEnumerator(Expression.Enumerator x) { 
    return null; 
  }

  public T visitExpressionEquals(Expression.Equals x) { 
    return null; 
  }

  public T visitExpressionEquivalence(Expression.Equivalence x) { 
    return null; 
  }

  public T visitExpressionFieldAccess(Expression.FieldAccess x) { 
    return null; 
  }

  public T visitExpressionFieldProject(Expression.FieldProject x) { 
    return null; 
  }

  public T visitExpressionFieldUpdate(Expression.FieldUpdate x) { 
    return null; 
  }

  public T visitExpressionGetAnnotation(Expression.GetAnnotation x) { 
    return null; 
  }

  public T visitExpressionGreaterThan(Expression.GreaterThan x) { 
    return null; 
  }

  public T visitExpressionGreaterThanOrEq(Expression.GreaterThanOrEq x) { 
    return null; 
  }

  public T visitExpressionHas(Expression.Has x) { 
    return null; 
  }

  public T visitExpressionIfDefinedOtherwise(Expression.IfDefinedOtherwise x) { 
    return null; 
  }

  public T visitExpressionIfThenElse(Expression.IfThenElse x) { 
    return null; 
  }

  public T visitExpressionImplication(Expression.Implication x) { 
    return null; 
  }

  public T visitExpressionIn(Expression.In x) { 
    return null; 
  }

  public T visitExpressionInsertBefore(Expression.InsertBefore x) { 
    return null; 
  }

  public T visitExpressionIntersection(Expression.Intersection x) { 
    return null; 
  }

  public T visitExpressionIs(Expression.Is x) { 
    return null; 
  }

  public T visitExpressionIsDefined(Expression.IsDefined x) { 
    return null; 
  }

  public T visitExpressionIt(Expression.It x) { 
    return null; 
  }

  public T visitExpressionJoin(Expression.Join x) { 
    return null; 
  }

  public T visitExpressionLessThan(Expression.LessThan x) { 
    return null; 
  }

  public T visitExpressionLessThanOrEq(Expression.LessThanOrEq x) { 
    return null; 
  }

  public T visitExpressionList(Expression.List x) { 
    return null; 
  }

  public T visitExpressionLiteral(Expression.Literal x) { 
    return null; 
  }

  public T visitExpressionMap(Expression.Map x) { 
    return null; 
  }

  public T visitExpressionMatch(Expression.Match x) { 
    return null; 
  }

  public T visitExpressionModulo(Expression.Modulo x) { 
    return null; 
  }

  public T visitExpressionMultiVariable(Expression.MultiVariable x) { 
    return null; 
  }

  public T visitExpressionNegation(Expression.Negation x) { 
    return null; 
  }

  public T visitExpressionNegative(Expression.Negative x) { 
    return null; 
  }

  public T visitExpressionNoMatch(Expression.NoMatch x) { 
    return null; 
  }

  public T visitExpressionNonEmptyBlock(Expression.NonEmptyBlock x) { 
    return null; 
  }

  public T visitExpressionNonEquals(Expression.NonEquals x) { 
    return null; 
  }

  public T visitExpressionNotIn(Expression.NotIn x) { 
    return null; 
  }

  public T visitExpressionOr(Expression.Or x) { 
    return null; 
  }

  public T visitExpressionProduct(Expression.Product x) { 
    return null; 
  }

  public T visitExpressionQualifiedName(Expression.QualifiedName x) { 
    return null; 
  }

  public T visitExpressionRange(Expression.Range x) { 
    return null; 
  }

  public T visitExpressionReducer(Expression.Reducer x) { 
    return null; 
  }

  public T visitExpressionReifiedType(Expression.ReifiedType x) { 
    return null; 
  }

  public T visitExpressionReifyType(Expression.ReifyType x) { 
    return null; 
  }

  public T visitExpressionRemainder(Expression.Remainder x) { 
    return null; 
  }

  public T visitExpressionSet(Expression.Set x) { 
    return null; 
  }

  public T visitExpressionSetAnnotation(Expression.SetAnnotation x) { 
    return null; 
  }

  public T visitExpressionSlice(Expression.Slice x) { 
    return null; 
  }

  public T visitExpressionSliceStep(Expression.SliceStep x) { 
    return null; 
  }

  public T visitExpressionSplice(Expression.Splice x) { 
    return null; 
  }

  public T visitExpressionSplicePlus(Expression.SplicePlus x) { 
    return null; 
  }

  public T visitExpressionStepRange(Expression.StepRange x) { 
    return null; 
  }

  public T visitExpressionSubscript(Expression.Subscript x) { 
    return null; 
  }

  public T visitExpressionSubtraction(Expression.Subtraction x) { 
    return null; 
  }

  public T visitExpressionTransitiveClosure(Expression.TransitiveClosure x) { 
    return null; 
  }

  public T visitExpressionTransitiveReflexiveClosure(Expression.TransitiveReflexiveClosure x) { 
    return null; 
  }

  public T visitExpressionTuple(Expression.Tuple x) { 
    return null; 
  }

  public T visitExpressionTuple(Expression.Tuple x) { 
    return null; 
  }

  public T visitExpressionTypedVariable(Expression.TypedVariable x) { 
    return null; 
  }

  public T visitExpressionTypedVariableBecomes(Expression.TypedVariableBecomes x) { 
    return null; 
  }

  public T visitExpressionVariableBecomes(Expression.VariableBecomes x) { 
    return null; 
  }

  public T visitExpressionVisit(Expression.Visit x) { 
    return null; 
  }

  public T visitExpressionVoidClosure(Expression.VoidClosure x) { 
    return null; 
  }

  public T visitFieldIndex(Field.Index x) { 
    return null; 
  }

  public T visitFieldName(Field.Name x) { 
    return null; 
  }

  public T visitFormalsDefault(Formals.Default x) { 
    return null; 
  }

  public T visitFunctionBodyDefault(FunctionBody.Default x) { 
    return null; 
  }

  public T visitFunctionDeclarationAbstract(FunctionDeclaration.Abstract x) { 
    return null; 
  }

  public T visitFunctionDeclarationConditional(FunctionDeclaration.Conditional x) { 
    return null; 
  }

  public T visitFunctionDeclarationDefault(FunctionDeclaration.Default x) { 
    return null; 
  }

  public T visitFunctionDeclarationExpression(FunctionDeclaration.Expression x) { 
    return null; 
  }

  public T visitFunctionModifierDefault(FunctionModifier.Default x) { 
    return null; 
  }

  public T visitFunctionModifierJava(FunctionModifier.Java x) { 
    return null; 
  }

  public T visitFunctionModifierTest(FunctionModifier.Test x) { 
    return null; 
  }

  public T visitFunctionModifiersModifierlist(FunctionModifiers.Modifierlist x) { 
    return null; 
  }

  public T visitFunctionTypeTypeArguments(FunctionType.TypeArguments x) { 
    return null; 
  }

  public T visitHeaderDefault(Header.Default x) { 
    return null; 
  }

  public T visitHeaderParameters(Header.Parameters x) { 
    return null; 
  }

  public T visitImportDefault(Import.Default x) { 
    return null; 
  }

  public T visitImportExtend(Import.Extend x) { 
    return null; 
  }

  public T visitImportExternal(Import.External x) { 
    return null; 
  }

  public T visitImportSyntax(Import.Syntax x) { 
    return null; 
  }

  public T visitImportedModuleActuals(ImportedModule.Actuals x) { 
    return null; 
  }

  public T visitImportedModuleActualsRenaming(ImportedModule.ActualsRenaming x) { 
    return null; 
  }

  public T visitImportedModuleDefault(ImportedModule.Default x) { 
    return null; 
  }

  public T visitImportedModuleRenamings(ImportedModule.Renamings x) { 
    return null; 
  }

  public T visitIntegerLiteralDecimalIntegerLiteral(IntegerLiteral.DecimalIntegerLiteral x) { 
    return null; 
  }

  public T visitIntegerLiteralHexIntegerLiteral(IntegerLiteral.HexIntegerLiteral x) { 
    return null; 
  }

  public T visitIntegerLiteralOctalIntegerLiteral(IntegerLiteral.OctalIntegerLiteral x) { 
    return null; 
  }

  public T visitKeywordArgument_ExpressionDefault(KeywordArgument_Expression.Default x) { 
    return null; 
  }

  public T visitKeywordArguments_ExpressionDefault(KeywordArguments_Expression.Default x) { 
    return null; 
  }

  public T visitKeywordArguments_ExpressionNone(KeywordArguments_Expression.None x) { 
    return null; 
  }

  public T visitKeywordFormalDefault(KeywordFormal.Default x) { 
    return null; 
  }

  public T visitKeywordFormalsDefault(KeywordFormals.Default x) { 
    return null; 
  }

  public T visitKeywordFormalsNone(KeywordFormals.None x) { 
    return null; 
  }

  public T visitKindAlias(Kind.Alias x) { 
    return null; 
  }

  public T visitKindAll(Kind.All x) { 
    return null; 
  }

  public T visitKindAnno(Kind.Anno x) { 
    return null; 
  }

  public T visitKindData(Kind.Data x) { 
    return null; 
  }

  public T visitKindFunction(Kind.Function x) { 
    return null; 
  }

  public T visitKindModule(Kind.Module x) { 
    return null; 
  }

  public T visitKindTag(Kind.Tag x) { 
    return null; 
  }

  public T visitKindVariable(Kind.Variable x) { 
    return null; 
  }

  public T visitKindView(Kind.View x) { 
    return null; 
  }

  public T visitLabelDefault(Label.Default x) { 
    return null; 
  }

  public T visitLabelEmpty(Label.Empty x) { 
    return null; 
  }

  public T visitLiteralBoolean(Literal.Boolean x) { 
    return null; 
  }

  public T visitLiteralDateTime(Literal.DateTime x) { 
    return null; 
  }

  public T visitLiteralInteger(Literal.Integer x) { 
    return null; 
  }

  public T visitLiteralLocation(Literal.Location x) { 
    return null; 
  }

  public T visitLiteralRational(Literal.Rational x) { 
    return null; 
  }

  public T visitLiteralReal(Literal.Real x) { 
    return null; 
  }

  public T visitLiteralRegExp(Literal.RegExp x) { 
    return null; 
  }

  public T visitLiteralString(Literal.String x) { 
    return null; 
  }

  public T visitLocalVariableDeclarationDefault(LocalVariableDeclaration.Default x) { 
    return null; 
  }

  public T visitLocalVariableDeclarationDynamic(LocalVariableDeclaration.Dynamic x) { 
    return null; 
  }

  public T visitLocationLiteralDefault(LocationLiteral.Default x) { 
    return null; 
  }

  public T visitMapping_ExpressionDefault(Mapping_Expression.Default x) { 
    return null; 
  }

  public T visitModuleDefault(Module.Default x) { 
    return null; 
  }

  public T visitModuleActualsDefault(ModuleActuals.Default x) { 
    return null; 
  }

  public T visitModuleParametersDefault(ModuleParameters.Default x) { 
    return null; 
  }

  public T visitOptionalExpressionExpression(OptionalExpression.Expression x) { 
    return null; 
  }

  public T visitOptionalExpressionNoExpression(OptionalExpression.NoExpression x) { 
    return null; 
  }

  public T visitParametersDefault(Parameters.Default x) { 
    return null; 
  }

  public T visitParametersVarArgs(Parameters.VarArgs x) { 
    return null; 
  }

  public T visitPathPartInterpolated(PathPart.Interpolated x) { 
    return null; 
  }

  public T visitPathPartNonInterpolated(PathPart.NonInterpolated x) { 
    return null; 
  }

  public T visitPathTailMid(PathTail.Mid x) { 
    return null; 
  }

  public T visitPathTailPost(PathTail.Post x) { 
    return null; 
  }

  public T visitPatternWithActionArbitrary(PatternWithAction.Arbitrary x) { 
    return null; 
  }

  public T visitPatternWithActionReplacing(PatternWithAction.Replacing x) { 
    return null; 
  }

  public T visitProdAll(Prod.All x) { 
    return null; 
  }

  public T visitProdAssociativityGroup(Prod.AssociativityGroup x) { 
    return null; 
  }

  public T visitProdFirst(Prod.First x) { 
    return null; 
  }

  public T visitProdLabeled(Prod.Labeled x) { 
    return null; 
  }

  public T visitProdReference(Prod.Reference x) { 
    return null; 
  }

  public T visitProdUnlabeled(Prod.Unlabeled x) { 
    return null; 
  }

  public T visitProdModifierAssociativity(ProdModifier.Associativity x) { 
    return null; 
  }

  public T visitProdModifierBracket(ProdModifier.Bracket x) { 
    return null; 
  }

  public T visitProdModifierTag(ProdModifier.Tag x) { 
    return null; 
  }

  public T visitProtocolPartInterpolated(ProtocolPart.Interpolated x) { 
    return null; 
  }

  public T visitProtocolPartNonInterpolated(ProtocolPart.NonInterpolated x) { 
    return null; 
  }

  public T visitProtocolTailMid(ProtocolTail.Mid x) { 
    return null; 
  }

  public T visitProtocolTailPost(ProtocolTail.Post x) { 
    return null; 
  }

  public T visitQualifiedNameDefault(QualifiedName.Default x) { 
    return null; 
  }

  public T visitRangeCharacter(Range.Character x) { 
    return null; 
  }

  public T visitRangeFromTo(Range.FromTo x) { 
    return null; 
  }

  public T visitRenamingDefault(Renaming.Default x) { 
    return null; 
  }

  public T visitRenamingsDefault(Renamings.Default x) { 
    return null; 
  }

  public T visitReplacementConditional(Replacement.Conditional x) { 
    return null; 
  }

  public T visitReplacementUnconditional(Replacement.Unconditional x) { 
    return null; 
  }

  public T visitShellCommandClear(ShellCommand.Clear x) { 
    return null; 
  }

  public T visitShellCommandEdit(ShellCommand.Edit x) { 
    return null; 
  }

  public T visitShellCommandHelp(ShellCommand.Help x) { 
    return null; 
  }

  public T visitShellCommandHistory(ShellCommand.History x) { 
    return null; 
  }

  public T visitShellCommandListDeclarations(ShellCommand.ListDeclarations x) { 
    return null; 
  }

  public T visitShellCommandListModules(ShellCommand.ListModules x) { 
    return null; 
  }

  public T visitShellCommandQuit(ShellCommand.Quit x) { 
    return null; 
  }

  public T visitShellCommandSetOption(ShellCommand.SetOption x) { 
    return null; 
  }

  public T visitShellCommandTest(ShellCommand.Test x) { 
    return null; 
  }

  public T visitShellCommandUndeclare(ShellCommand.Undeclare x) { 
    return null; 
  }

  public T visitShellCommandUnimport(ShellCommand.Unimport x) { 
    return null; 
  }

  public T visitSignatureNoThrows(Signature.NoThrows x) { 
    return null; 
  }

  public T visitSignatureWithThrows(Signature.WithThrows x) { 
    return null; 
  }

  public T visitStartAbsent(Start.Absent x) { 
    return null; 
  }

  public T visitStartPresent(Start.Present x) { 
    return null; 
  }

  public T visitStatementAppend(Statement.Append x) { 
    return null; 
  }

  public T visitStatementAssert(Statement.Assert x) { 
    return null; 
  }

  public T visitStatementAssertWithMessage(Statement.AssertWithMessage x) { 
    return null; 
  }

  public T visitStatementAssignment(Statement.Assignment x) { 
    return null; 
  }

  public T visitStatementBreak(Statement.Break x) { 
    return null; 
  }

  public T visitStatementContinue(Statement.Continue x) { 
    return null; 
  }

  public T visitStatementDoWhile(Statement.DoWhile x) { 
    return null; 
  }

  public T visitStatementEmptyStatement(Statement.EmptyStatement x) { 
    return null; 
  }

  public T visitStatementExpression(Statement.Expression x) { 
    return null; 
  }

  public T visitStatementFail(Statement.Fail x) { 
    return null; 
  }

  public T visitStatementFilter(Statement.Filter x) { 
    return null; 
  }

  public T visitStatementFor(Statement.For x) { 
    return null; 
  }

  public T visitStatementFunctionDeclaration(Statement.FunctionDeclaration x) { 
    return null; 
  }

  public T visitStatementGlobalDirective(Statement.GlobalDirective x) { 
    return null; 
  }

  public T visitStatementIfThen(Statement.IfThen x) { 
    return null; 
  }

  public T visitStatementIfThenElse(Statement.IfThenElse x) { 
    return null; 
  }

  public T visitStatementInsert(Statement.Insert x) { 
    return null; 
  }

  public T visitStatementNonEmptyBlock(Statement.NonEmptyBlock x) { 
    return null; 
  }

  public T visitStatementReturn(Statement.Return x) { 
    return null; 
  }

  public T visitStatementSolve(Statement.Solve x) { 
    return null; 
  }

  public T visitStatementSwitch(Statement.Switch x) { 
    return null; 
  }

  public T visitStatementThrow(Statement.Throw x) { 
    return null; 
  }

  public T visitStatementTry(Statement.Try x) { 
    return null; 
  }

  public T visitStatementTryFinally(Statement.TryFinally x) { 
    return null; 
  }

  public T visitStatementVariableDeclaration(Statement.VariableDeclaration x) { 
    return null; 
  }

  public T visitStatementVisit(Statement.Visit x) { 
    return null; 
  }

  public T visitStatementWhile(Statement.While x) { 
    return null; 
  }

  public T visitStrategyBottomUp(Strategy.BottomUp x) { 
    return null; 
  }

  public T visitStrategyBottomUpBreak(Strategy.BottomUpBreak x) { 
    return null; 
  }

  public T visitStrategyInnermost(Strategy.Innermost x) { 
    return null; 
  }

  public T visitStrategyOutermost(Strategy.Outermost x) { 
    return null; 
  }

  public T visitStrategyTopDown(Strategy.TopDown x) { 
    return null; 
  }

  public T visitStrategyTopDownBreak(Strategy.TopDownBreak x) { 
    return null; 
  }

  public T visitStringLiteralInterpolated(StringLiteral.Interpolated x) { 
    return null; 
  }

  public T visitStringLiteralNonInterpolated(StringLiteral.NonInterpolated x) { 
    return null; 
  }

  public T visitStringLiteralTemplate(StringLiteral.Template x) { 
    return null; 
  }

  public T visitStringMiddleInterpolated(StringMiddle.Interpolated x) { 
    return null; 
  }

  public T visitStringMiddleMid(StringMiddle.Mid x) { 
    return null; 
  }

  public T visitStringMiddleTemplate(StringMiddle.Template x) { 
    return null; 
  }

  public T visitStringTailMidInterpolated(StringTail.MidInterpolated x) { 
    return null; 
  }

  public T visitStringTailMidTemplate(StringTail.MidTemplate x) { 
    return null; 
  }

  public T visitStringTailPost(StringTail.Post x) { 
    return null; 
  }

  public T visitStringTemplateDoWhile(StringTemplate.DoWhile x) { 
    return null; 
  }

  public T visitStringTemplateFor(StringTemplate.For x) { 
    return null; 
  }

  public T visitStringTemplateIfThen(StringTemplate.IfThen x) { 
    return null; 
  }

  public T visitStringTemplateIfThenElse(StringTemplate.IfThenElse x) { 
    return null; 
  }

  public T visitStringTemplateWhile(StringTemplate.While x) { 
    return null; 
  }

  public T visitStructuredTypeDefault(StructuredType.Default x) { 
    return null; 
  }

  public T visitSymAlternative(Sym.Alternative x) { 
    return null; 
  }

  public T visitSymCaseInsensitiveLiteral(Sym.CaseInsensitiveLiteral x) { 
    return null; 
  }

  public T visitSymCharacterClass(Sym.CharacterClass x) { 
    return null; 
  }

  public T visitSymColumn(Sym.Column x) { 
    return null; 
  }

  public T visitSymEmpty(Sym.Empty x) { 
    return null; 
  }

  public T visitSymEndOfLine(Sym.EndOfLine x) { 
    return null; 
  }

  public T visitSymExcept(Sym.Except x) { 
    return null; 
  }

  public T visitSymFollow(Sym.Follow x) { 
    return null; 
  }

  public T visitSymIter(Sym.Iter x) { 
    return null; 
  }

  public T visitSymIterSep(Sym.IterSep x) { 
    return null; 
  }

  public T visitSymIterStar(Sym.IterStar x) { 
    return null; 
  }

  public T visitSymIterStarSep(Sym.IterStarSep x) { 
    return null; 
  }

  public T visitSymLabeled(Sym.Labeled x) { 
    return null; 
  }

  public T visitSymLiteral(Sym.Literal x) { 
    return null; 
  }

  public T visitSymNonterminal(Sym.Nonterminal x) { 
    return null; 
  }

  public T visitSymNotFollow(Sym.NotFollow x) { 
    return null; 
  }

  public T visitSymNotPrecede(Sym.NotPrecede x) { 
    return null; 
  }

  public T visitSymOptional(Sym.Optional x) { 
    return null; 
  }

  public T visitSymParameter(Sym.Parameter x) { 
    return null; 
  }

  public T visitSymParametrized(Sym.Parametrized x) { 
    return null; 
  }

  public T visitSymPrecede(Sym.Precede x) { 
    return null; 
  }

  public T visitSymSequence(Sym.Sequence x) { 
    return null; 
  }

  public T visitSymStart(Sym.Start x) { 
    return null; 
  }

  public T visitSymStartOfLine(Sym.StartOfLine x) { 
    return null; 
  }

  public T visitSymUnequal(Sym.Unequal x) { 
    return null; 
  }

  public T visitSyntaxDefinitionKeyword(SyntaxDefinition.Keyword x) { 
    return null; 
  }

  public T visitSyntaxDefinitionLanguage(SyntaxDefinition.Language x) { 
    return null; 
  }

  public T visitSyntaxDefinitionLayout(SyntaxDefinition.Layout x) { 
    return null; 
  }

  public T visitSyntaxDefinitionLexical(SyntaxDefinition.Lexical x) { 
    return null; 
  }

  public T visitTagDefault(Tag.Default x) { 
    return null; 
  }

  public T visitTagEmpty(Tag.Empty x) { 
    return null; 
  }

  public T visitTagExpression(Tag.Expression x) { 
    return null; 
  }

  public T visitTagsDefault(Tags.Default x) { 
    return null; 
  }

  public T visitTargetEmpty(Target.Empty x) { 
    return null; 
  }

  public T visitTargetLabeled(Target.Labeled x) { 
    return null; 
  }

  public T visitToplevelGivenVisibility(Toplevel.GivenVisibility x) { 
    return null; 
  }

  public T visitTypeBasic(Type.Basic x) { 
    return null; 
  }

  public T visitTypeBracket(Type.Bracket x) { 
    return null; 
  }

  public T visitTypeFunction(Type.Function x) { 
    return null; 
  }

  public T visitTypeSelector(Type.Selector x) { 
    return null; 
  }

  public T visitTypeStructured(Type.Structured x) { 
    return null; 
  }

  public T visitTypeSymbol(Type.Symbol x) { 
    return null; 
  }

  public T visitTypeUser(Type.User x) { 
    return null; 
  }

  public T visitTypeVariable(Type.Variable x) { 
    return null; 
  }

  public T visitTypeArgDefault(TypeArg.Default x) { 
    return null; 
  }

  public T visitTypeArgNamed(TypeArg.Named x) { 
    return null; 
  }

  public T visitTypeVarBounded(TypeVar.Bounded x) { 
    return null; 
  }

  public T visitTypeVarFree(TypeVar.Free x) { 
    return null; 
  }

  public T visitUserTypeName(UserType.Name x) { 
    return null; 
  }

  public T visitUserTypeParametric(UserType.Parametric x) { 
    return null; 
  }

  public T visitVariableInitialized(Variable.Initialized x) { 
    return null; 
  }

  public T visitVariableUnInitialized(Variable.UnInitialized x) { 
    return null; 
  }

  public T visitVariantNAryConstructor(Variant.NAryConstructor x) { 
    return null; 
  }

  public T visitVisibilityDefault(Visibility.Default x) { 
    return null; 
  }

  public T visitVisibilityPrivate(Visibility.Private x) { 
    return null; 
  }

  public T visitVisibilityPublic(Visibility.Public x) { 
    return null; 
  }

  public T visitVisitDefaultStrategy(Visit.DefaultStrategy x) { 
    return null; 
  }

  public T visitVisitGivenStrategy(Visit.GivenStrategy x) { 
    return null; 
  }


  public T visitBackslashLexical(Backslash.Lexical x) { 
    return null; 
  }

  public T visitBooleanLiteralLexical(BooleanLiteral.Lexical x) { 
    return null; 
  }

  public T visitCaseInsensitiveStringConstantLexical(CaseInsensitiveStringConstant.Lexical x) { 
    return null; 
  }

  public T visitCharLexical(Char.Lexical x) { 
    return null; 
  }

  public T visitCommentLexical(Comment.Lexical x) { 
    return null; 
  }

  public T visitConcreteLexical(Concrete.Lexical x) { 
    return null; 
  }

  public T visitConcretePartLexical(ConcretePart.Lexical x) { 
    return null; 
  }

  public T visitDateAndTimeLexical(DateAndTime.Lexical x) { 
    return null; 
  }

  public T visitDatePartLexical(DatePart.Lexical x) { 
    return null; 
  }

  public T visitDecimalIntegerLiteralLexical(DecimalIntegerLiteral.Lexical x) { 
    return null; 
  }

  public T visitHexIntegerLiteralLexical(HexIntegerLiteral.Lexical x) { 
    return null; 
  }

  public T visitJustDateLexical(JustDate.Lexical x) { 
    return null; 
  }

  public T visitJustTimeLexical(JustTime.Lexical x) { 
    return null; 
  }

  public T visitLAYOUTLexical(LAYOUT.Lexical x) { 
    return null; 
  }

  public T visitMidPathCharsLexical(MidPathChars.Lexical x) { 
    return null; 
  }

  public T visitMidProtocolCharsLexical(MidProtocolChars.Lexical x) { 
    return null; 
  }

  public T visitMidStringCharsLexical(MidStringChars.Lexical x) { 
    return null; 
  }

  public T visitNameLexical(Name.Lexical x) { 
    return null; 
  }

  public T visitNamedBackslashLexical(NamedBackslash.Lexical x) { 
    return null; 
  }

  public T visitNamedRegExpLexical(NamedRegExp.Lexical x) { 
    return null; 
  }

  public T visitNonterminalLexical(Nonterminal.Lexical x) { 
    return null; 
  }

  public T visitNonterminalLabelLexical(NonterminalLabel.Lexical x) { 
    return null; 
  }

  public T visitOctalIntegerLiteralLexical(OctalIntegerLiteral.Lexical x) { 
    return null; 
  }

  public T visitOptionalCommaLexical(OptionalComma.Lexical x) { 
    return null; 
  }

  public T visitOutputLexical(Output.Lexical x) { 
    return null; 
  }

  public T visitPathCharsLexical(PathChars.Lexical x) { 
    return null; 
  }

  public T visitPostPathCharsLexical(PostPathChars.Lexical x) { 
    return null; 
  }

  public T visitPostProtocolCharsLexical(PostProtocolChars.Lexical x) { 
    return null; 
  }

  public T visitPostStringCharsLexical(PostStringChars.Lexical x) { 
    return null; 
  }

  public T visitPrePathCharsLexical(PrePathChars.Lexical x) { 
    return null; 
  }

  public T visitPreProtocolCharsLexical(PreProtocolChars.Lexical x) { 
    return null; 
  }

  public T visitPreStringCharsLexical(PreStringChars.Lexical x) { 
    return null; 
  }

  public T visitProtocolCharsLexical(ProtocolChars.Lexical x) { 
    return null; 
  }

  public T visitRationalLiteralLexical(RationalLiteral.Lexical x) { 
    return null; 
  }

  public T visitRealLiteralLexical(RealLiteral.Lexical x) { 
    return null; 
  }

  public T visitRegExpLexical(RegExp.Lexical x) { 
    return null; 
  }

  public T visitRegExpLiteralLexical(RegExpLiteral.Lexical x) { 
    return null; 
  }

  public T visitRegExpModifierLexical(RegExpModifier.Lexical x) { 
    return null; 
  }

  public T visitStringCharacterLexical(StringCharacter.Lexical x) { 
    return null; 
  }

  public T visitStringConstantLexical(StringConstant.Lexical x) { 
    return null; 
  }

  public T visitTagStringLexical(TagString.Lexical x) { 
    return null; 
  }

  public T visitTimePartNoTZLexical(TimePartNoTZ.Lexical x) { 
    return null; 
  }

  public T visitTimeZonePartLexical(TimeZonePart.Lexical x) { 
    return null; 
  }

  public T visitURLCharsLexical(URLChars.Lexical x) { 
    return null; 
  }

  public T visitUnicodeEscapeLexical(UnicodeEscape.Lexical x) { 
    return null; 
  }

}