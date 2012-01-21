/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
 *   * Tijs van der Storm - Tijs.van.der.Storm@cwi.nl
 *   * Bert Lisser - Bert.Lisser@cwi.nl (CWI)
 *   * Mark Hills - Mark.Hills@cwi.nl (CWI)
 *   * Arnold Lankamp - Arnold.Lankamp@cwi.nl
*******************************************************************************/
package org.rascalmpl.interpreter;

import java.util.Iterator;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.*;
import org.rascalmpl.ast.Assignable.Annotation;
import org.rascalmpl.ast.Assignable.Constructor;
import org.rascalmpl.ast.Assignable.FieldAccess;
import org.rascalmpl.ast.Assignable.IfDefinedOrDefault;
import org.rascalmpl.ast.Assignable.Subscript;
import org.rascalmpl.ast.Assignable.Tuple;
import org.rascalmpl.ast.Assignment.Addition;
import org.rascalmpl.ast.Assignment.Default;
import org.rascalmpl.ast.Assignment.Division;
import org.rascalmpl.ast.Assignment.IfDefined;
import org.rascalmpl.ast.Assignment.Intersection;
import org.rascalmpl.ast.Assignment.Product;
import org.rascalmpl.ast.Assignment.Subtraction;
import org.rascalmpl.ast.Assoc.Associative;
import org.rascalmpl.ast.Assoc.Left;
import org.rascalmpl.ast.Assoc.NonAssociative;
import org.rascalmpl.ast.Assoc.Right;
import org.rascalmpl.ast.BasicType.Bag;
import org.rascalmpl.ast.BasicType.Bool;
import org.rascalmpl.ast.BasicType.DateTime;
import org.rascalmpl.ast.BasicType.Int;
import org.rascalmpl.ast.BasicType.List;
import org.rascalmpl.ast.BasicType.Loc;
import org.rascalmpl.ast.BasicType.Map;
import org.rascalmpl.ast.BasicType.Node;
import org.rascalmpl.ast.BasicType.Num;
import org.rascalmpl.ast.BasicType.Real;
import org.rascalmpl.ast.BasicType.Relation;
import org.rascalmpl.ast.BasicType.Set;
import org.rascalmpl.ast.BasicType.Value;
import org.rascalmpl.ast.BasicType.Void;
import org.rascalmpl.ast.Body.Toplevels;
import org.rascalmpl.ast.Bound.Empty;
import org.rascalmpl.ast.Case.PatternWithAction;
import org.rascalmpl.ast.Catch.Binding;
import org.rascalmpl.ast.Command.Declaration;
import org.rascalmpl.ast.Command.Shell;
import org.rascalmpl.ast.Command.Statement;
import org.rascalmpl.ast.DataTarget.Labeled;
import org.rascalmpl.ast.DataTypeSelector.Selector;
import org.rascalmpl.ast.DateTimeLiteral.DateAndTimeLiteral;
import org.rascalmpl.ast.DateTimeLiteral.DateLiteral;
import org.rascalmpl.ast.DateTimeLiteral.TimeLiteral;
import org.rascalmpl.ast.Declaration.Alias;
import org.rascalmpl.ast.Declaration.Data;
import org.rascalmpl.ast.Declaration.DataAbstract;
import org.rascalmpl.ast.Declaration.Function;
import org.rascalmpl.ast.Declaration.Tag;
import org.rascalmpl.ast.EvalCommand.Import;
import org.rascalmpl.ast.Expression.All;
import org.rascalmpl.ast.Expression.And;
import org.rascalmpl.ast.Expression.Anti;
import org.rascalmpl.ast.Expression.Any;
import org.rascalmpl.ast.Expression.AppendAfter;
import org.rascalmpl.ast.Expression.AsType;
import org.rascalmpl.ast.Expression.CallOrTree;
import org.rascalmpl.ast.Expression.Closure;
import org.rascalmpl.ast.Expression.Composition;
import org.rascalmpl.ast.Expression.Comprehension;
import org.rascalmpl.ast.Expression.Descendant;
import org.rascalmpl.ast.Expression.Enumerator;
import org.rascalmpl.ast.Expression.Equals;
import org.rascalmpl.ast.Expression.Equivalence;
import org.rascalmpl.ast.Expression.FieldProject;
import org.rascalmpl.ast.Expression.FieldUpdate;
import org.rascalmpl.ast.Expression.GetAnnotation;
import org.rascalmpl.ast.Expression.GreaterThan;
import org.rascalmpl.ast.Expression.GreaterThanOrEq;
import org.rascalmpl.ast.Expression.Has;
import org.rascalmpl.ast.Expression.IfDefinedOtherwise;
import org.rascalmpl.ast.Expression.IfThenElse;
import org.rascalmpl.ast.Expression.Implication;
import org.rascalmpl.ast.Expression.In;
import org.rascalmpl.ast.Expression.InsertBefore;
import org.rascalmpl.ast.Expression.Is;
import org.rascalmpl.ast.Expression.IsDefined;
import org.rascalmpl.ast.Expression.It;
import org.rascalmpl.ast.Expression.Join;
import org.rascalmpl.ast.Expression.LessThan;
import org.rascalmpl.ast.Expression.LessThanOrEq;
import org.rascalmpl.ast.Expression.Literal;
import org.rascalmpl.ast.Expression.Match;
import org.rascalmpl.ast.Expression.Modulo;
import org.rascalmpl.ast.Expression.MultiVariable;
import org.rascalmpl.ast.Expression.Negation;
import org.rascalmpl.ast.Expression.Negative;
import org.rascalmpl.ast.Expression.NoMatch;
import org.rascalmpl.ast.Expression.NonEmptyBlock;
import org.rascalmpl.ast.Expression.NonEquals;
import org.rascalmpl.ast.Expression.NotIn;
import org.rascalmpl.ast.Expression.Or;
import org.rascalmpl.ast.Expression.QualifiedName;
import org.rascalmpl.ast.Expression.Reducer;
import org.rascalmpl.ast.Expression.ReifyType;
import org.rascalmpl.ast.Expression.Remainder;
import org.rascalmpl.ast.Expression.SetAnnotation;
import org.rascalmpl.ast.Expression.Splice;
import org.rascalmpl.ast.Expression.SplicePlus;
import org.rascalmpl.ast.Expression.StepRange;
import org.rascalmpl.ast.Expression.TransitiveClosure;
import org.rascalmpl.ast.Expression.TransitiveReflexiveClosure;
import org.rascalmpl.ast.Expression.TypedVariable;
import org.rascalmpl.ast.Expression.TypedVariableBecomes;
import org.rascalmpl.ast.Expression.VariableBecomes;
import org.rascalmpl.ast.Expression.Visit;
import org.rascalmpl.ast.Expression.VoidClosure;
import org.rascalmpl.ast.Field.Index;
import org.rascalmpl.ast.Field.Name;
import org.rascalmpl.ast.FunctionDeclaration.Abstract;
import org.rascalmpl.ast.FunctionModifier.Java;
import org.rascalmpl.ast.FunctionModifier.Test;
import org.rascalmpl.ast.FunctionType.TypeArguments;
import org.rascalmpl.ast.Header.Parameters;
import org.rascalmpl.ast.Import.Extend;
import org.rascalmpl.ast.Import.Syntax;
import org.rascalmpl.ast.ImportedModule.Actuals;
import org.rascalmpl.ast.ImportedModule.ActualsRenaming;
import org.rascalmpl.ast.ImportedModule.Renamings;
import org.rascalmpl.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.rascalmpl.ast.IntegerLiteral.HexIntegerLiteral;
import org.rascalmpl.ast.IntegerLiteral.OctalIntegerLiteral;
import org.rascalmpl.ast.Kind.Anno;
import org.rascalmpl.ast.Literal.Boolean;
import org.rascalmpl.ast.Literal.Integer;
import org.rascalmpl.ast.Literal.Location;
import org.rascalmpl.ast.Literal.Rational;
import org.rascalmpl.ast.Literal.RegExp;
import org.rascalmpl.ast.LocalVariableDeclaration.Dynamic;
import org.rascalmpl.ast.Parameters.VarArgs;
import org.rascalmpl.ast.PathPart.Interpolated;
import org.rascalmpl.ast.PathPart.NonInterpolated;
import org.rascalmpl.ast.PathTail.Mid;
import org.rascalmpl.ast.PathTail.Post;
import org.rascalmpl.ast.PatternWithAction.Arbitrary;
import org.rascalmpl.ast.PatternWithAction.Replacing;
import org.rascalmpl.ast.Prod.AssociativityGroup;
import org.rascalmpl.ast.Prod.First;
import org.rascalmpl.ast.Prod.Others;
import org.rascalmpl.ast.Prod.Reference;
import org.rascalmpl.ast.ProdModifier.Associativity;
import org.rascalmpl.ast.Range.FromTo;
import org.rascalmpl.ast.RationalLiteral.Ambiguity;
import org.rascalmpl.ast.RationalLiteral.Lexical;
import org.rascalmpl.ast.Replacement.Conditional;
import org.rascalmpl.ast.Replacement.Unconditional;
import org.rascalmpl.ast.ShellCommand.Edit;
import org.rascalmpl.ast.ShellCommand.Help;
import org.rascalmpl.ast.ShellCommand.History;
import org.rascalmpl.ast.ShellCommand.ListDeclarations;
import org.rascalmpl.ast.ShellCommand.ListModules;
import org.rascalmpl.ast.ShellCommand.Quit;
import org.rascalmpl.ast.ShellCommand.SetOption;
import org.rascalmpl.ast.ShellCommand.Undeclare;
import org.rascalmpl.ast.ShellCommand.Unimport;
import org.rascalmpl.ast.Signature.NoThrows;
import org.rascalmpl.ast.Signature.WithThrows;
import org.rascalmpl.ast.Statement.Append;
import org.rascalmpl.ast.Statement.Assert;
import org.rascalmpl.ast.Statement.AssertWithMessage;
import org.rascalmpl.ast.Statement.Assignment;
import org.rascalmpl.ast.Statement.Break;
import org.rascalmpl.ast.Statement.Continue;
import org.rascalmpl.ast.Statement.DoWhile;
import org.rascalmpl.ast.Statement.EmptyStatement;
import org.rascalmpl.ast.Statement.Fail;
import org.rascalmpl.ast.Statement.Filter;
import org.rascalmpl.ast.Statement.For;
import org.rascalmpl.ast.Statement.FunctionDeclaration;
import org.rascalmpl.ast.Statement.GlobalDirective;
import org.rascalmpl.ast.Statement.IfThen;
import org.rascalmpl.ast.Statement.Insert;
import org.rascalmpl.ast.Statement.Return;
import org.rascalmpl.ast.Statement.Solve;
import org.rascalmpl.ast.Statement.Switch;
import org.rascalmpl.ast.Statement.Throw;
import org.rascalmpl.ast.Statement.Try;
import org.rascalmpl.ast.Statement.TryFinally;
import org.rascalmpl.ast.Statement.VariableDeclaration;
import org.rascalmpl.ast.Statement.While;
import org.rascalmpl.ast.Strategy.BottomUp;
import org.rascalmpl.ast.Strategy.BottomUpBreak;
import org.rascalmpl.ast.Strategy.Innermost;
import org.rascalmpl.ast.Strategy.Outermost;
import org.rascalmpl.ast.Strategy.TopDown;
import org.rascalmpl.ast.Strategy.TopDownBreak;
import org.rascalmpl.ast.StringLiteral.Template;
import org.rascalmpl.ast.StringTail.MidInterpolated;
import org.rascalmpl.ast.StringTail.MidTemplate;
import org.rascalmpl.ast.Sym.Alternative;
import org.rascalmpl.ast.Sym.Column;
import org.rascalmpl.ast.Sym.EndOfLine;
import org.rascalmpl.ast.Sym.Nonterminal;
import org.rascalmpl.ast.Sym.NotFollow;
import org.rascalmpl.ast.Sym.NotPrecede;
import org.rascalmpl.ast.Sym.Parameter;
import org.rascalmpl.ast.Sym.Parametrized;
import org.rascalmpl.ast.Sym.Precede;
import org.rascalmpl.ast.Sym.Sequence;
import org.rascalmpl.ast.Sym.Start;
import org.rascalmpl.ast.Sym.StartOfLine;
import org.rascalmpl.ast.Sym.Unequal;
import org.rascalmpl.ast.SyntaxDefinition.Keyword;
import org.rascalmpl.ast.SyntaxDefinition.Language;
import org.rascalmpl.ast.SyntaxDefinition.Layout;
import org.rascalmpl.ast.Toplevel.GivenVisibility;
import org.rascalmpl.ast.Type.Basic;
import org.rascalmpl.ast.Type.Structured;
import org.rascalmpl.ast.Type.Symbol;
import org.rascalmpl.ast.Type.User;
import org.rascalmpl.ast.TypeArg.Named;
import org.rascalmpl.ast.TypeVar.Bounded;
import org.rascalmpl.ast.TypeVar.Free;
import org.rascalmpl.ast.UserType.Parametric;
import org.rascalmpl.ast.Variable.Initialized;
import org.rascalmpl.ast.Variable.UnInitialized;
import org.rascalmpl.ast.Variant.NAryConstructor;
import org.rascalmpl.ast.Visibility.Private;
import org.rascalmpl.ast.Visibility.Public;
import org.rascalmpl.ast.Visit.DefaultStrategy;
import org.rascalmpl.ast.Visit.GivenStrategy;
import org.rascalmpl.interpreter.asserts.ImplementationError;
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.strategy.IStrategyContext;
import org.rascalmpl.interpreter.types.NonTerminalType;
import org.rascalmpl.values.uptr.TreeAdapter;

public class BoxEvaluator implements IASTVisitor<IValue> {
	private AbstractAST currentAST;

	private boolean isFunctionName;

	final private int SMALLCOMMENTSIZE = 40;

	final private int UNITLENGTH = 70;

	final private int SIGNIFICANT = 20;

	final org.eclipse.imp.pdb.facts.type.Type typeL = BoxADT.EMPTY
			.getConstructorType();

	public static TypeStore getTypeStore() {
		return BoxADT.getTypeStore();
	}

	public static Type getType() {
		return BoxADT.getBox();
	}

	// public TreeEvaluator(PrintWriter stderr, PrintWriter stdout) {
	// this.stderr = stderr;
	// this.stdout = stdout;
	// }

	public IValue call(String name, IValue... args) {
		throw new ImplementationError("should not call call");
	}

	public void setCurrentAST(AbstractAST currentAST) {
		this.currentAST = currentAST;
	}

	public AbstractAST getCurrentAST() {
		return currentAST;
	}

	public IValue evalRascalModule(Module module, IList ls) {
		return V(0, getComment(ls, 0), eX(module), getComment(ls, 2));
	}

	protected String getModuleName(Module module) {
		String name = module.getHeader().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		return name;
	}

	public IValue visitAssignableAmbiguity(
			org.rascalmpl.ast.Assignable.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitAssignableAnnotation(Annotation x) {
		return H(0, eX(x.getReceiver()), BoxADT.AT, eX(x.getAnnotation()));
	}

	public IValue visitAssignableConstructor(Constructor x) {
		return H(0, eX(x.getName()), BoxADT.LPAR,
				eXs(x.getArguments(), null, null), BoxADT.RPAR);
	}

	public IValue visitAssignableFieldAccess(FieldAccess x) {
		return H(0, eX(x.getReceiver()), BoxADT.DOT, eX(x.getField()));
	}

	public IValue visitAssignableIfDefinedOrDefault(IfDefinedOrDefault x) {
		return H(0, eX(x.getReceiver()), BoxADT.QUESTIONMARK,
				eX(x.getDefaultExpression()));
	}

	public IValue visitAssignableSubscript(Subscript x) {
		return H(0, eX(x.getReceiver()), BoxADT.LBRACK, eX(x.getSubscript()),
				BoxADT.RBRACK);
	}

	public IValue visitAssignableTuple(Tuple x) {
		return H(0, BoxADT.LT, eXs(x.getElements(), null, null), BoxADT.GT);
	}

	public IValue visitAssignableVariable(
			org.rascalmpl.ast.Assignable.Variable x) {
		return H(eX(x.getQualifiedName()));
	}

	public IValue visitAssignmentAddition(Addition x) {
		return L("+=");
	}

	public IValue visitAssignmentAmbiguity(
			org.rascalmpl.ast.Assignment.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitAssignmentDefault(Default x) {
		return L("=");
	}

	public IValue visitAssignmentDivision(Division x) {
		return L("/=");
	}

	public IValue visitAssignmentIfDefined(IfDefined x) {
		return L("?=");
	}

	public IValue visitAssignmentIntersection(Intersection x) {
		return L("&=");
	}

	public IValue visitAssignmentProduct(Product x) {
		return L("*=");
	}

	public IValue visitAssignmentSubtraction(Subtraction x) {
		return L("-=");
	}

	public IValue visitBackslashAmbiguity(
			org.rascalmpl.ast.Backslash.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitBackslashLexical(org.rascalmpl.ast.Backslash.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitBasicTypeAmbiguity(
			org.rascalmpl.ast.BasicType.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitBasicTypeBag(Bag x) {
		return KW("bag");
	}

	public IValue visitBasicTypeBool(Bool x) {
		return KW("bool");
	}

	public IValue visitBasicTypeDateTime(DateTime x) {
		return KW("datetime");
	}

	public IValue visitBasicTypeInt(Int x) {
		return KW("int");
	}

	public IValue visitBasicTypeList(List x) {
		return KW("list");
	}

	public IValue visitBasicTypeLoc(Loc x) {
		return KW("loc");
	}

	public IValue visitBasicTypeMap(Map x) {
		return KW("map");
	}

	public IValue visitBasicTypeNode(Node x) {
		return L(x.getClass().toString());
	}

	public IValue visitBasicTypeReal(Real x) {
		return KW("real");
	}

	public IValue visitBasicTypeReifiedType(Type x) {
		return KW("type");
	}

	public IValue visitBasicTypeRelation(Relation x) {
		return KW("rel");
	}

	public IValue visitBasicTypeSet(Set x) {
		return KW("set");
	}

	public IValue visitBasicTypeString(org.rascalmpl.ast.BasicType.String x) {
		return KW("str");
	}

	public IValue visitBasicTypeTuple(org.rascalmpl.ast.BasicType.Tuple x) {
		return KW("tuple");
	}

	public IValue visitBasicTypeValue(Value x) {
		return KW("value");
	}

	public IValue visitBasicTypeVoid(Void x) {
		return KW("void");
	}

	public IValue visitBodyAmbiguity(org.rascalmpl.ast.Body.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitBodyToplevels(Toplevels x) {
		IList l = getTreeList(x, 0);
		return V(1, eXs0(x.getToplevels(), l));
	}

	public IValue visitBooleanLiteralAmbiguity(
			org.rascalmpl.ast.BooleanLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitBooleanLiteralLexical(
			org.rascalmpl.ast.BooleanLiteral.Lexical x) {
		return NM(x.getString());
	}

	public IValue visitBoundAmbiguity(org.rascalmpl.ast.Bound.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitBoundDefault(org.rascalmpl.ast.Bound.Default x) {
		return list(BoxADT.SEMICOLON, eX(x.getExpression()));
	}

	public IValue visitBoundEmpty(Empty x) {
		return L(x.getClass().toString());
	}

	public IValue visitCaseAmbiguity(org.rascalmpl.ast.Case.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitCaseDefault(org.rascalmpl.ast.Case.Default x) {
		/** "default" ":" statement:Statement */
		return cStat(x, "default", null, BoxADT.COLON, null, null,
				eX(x.getStatement()));
	}

	public IValue visitCasePatternWithAction(PatternWithAction x) {
		/* "case" patternWithAction:PatternWithAction */
		return H(1, KW("case"), getComment(x, 1),
				HOV(0, eX(x.getPatternWithAction())));
	}

	public IValue visitCatchAmbiguity(org.rascalmpl.ast.Catch.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitCatchBinding(Binding x) {
		/**
		 * "catch" pattern:Expression ":" body:Statement -> Catch
		 * {cons("Binding")}
		 */
		return cStat(x, "catch", null, eX(x.getPattern()), BoxADT.COLON, null,
				eX(x.getBody()));
	}

	public IValue visitCatchDefault(org.rascalmpl.ast.Catch.Default x) {
		/** "catch" ":" body:Statement -> Catch {cons("Default")} */
		return cStat(x, "catch", null, BoxADT.COLON, null, null,
				eX(x.getBody()));
	}

	public IValue visitCommandAmbiguity(org.rascalmpl.ast.Command.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitCommandDeclaration(Declaration x) {
		return L(x.getClass().toString());
	}

	public IValue visitCommandExpression(org.rascalmpl.ast.Command.Expression x) {
		return L(x.getClass().toString());
	}

	public IValue visitCommandImport(org.rascalmpl.ast.Command.Import x) {
		return L(x.getClass().toString());
	}

	public IValue visitCommandShell(Shell x) {
		return L(x.getClass().toString());
	}

	public IValue visitCommandStatement(Statement x) {
		return L(x.getClass().toString());
	}

	public IValue visitCommentAmbiguity(org.rascalmpl.ast.Comment.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitCommentLexical(org.rascalmpl.ast.Comment.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitComprehensionAmbiguity(
			org.rascalmpl.ast.Comprehension.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitComprehensionList(org.rascalmpl.ast.Comprehension.List x) {
		return Comprehension(x, BoxADT.LBRACK, BoxADT.RBRACK);
	}

	public IValue visitComprehensionMap(org.rascalmpl.ast.Comprehension.Map x) {
		return ComprehensionMap(x, BoxADT.LPAR, BoxADT.RPAR);
	}

	public IValue visitComprehensionSet(org.rascalmpl.ast.Comprehension.Set x) {
		return Comprehension(x, BoxADT.LBLOCK, BoxADT.RBLOCK);
	}

	public IValue visitDataTargetAmbiguity(
			org.rascalmpl.ast.DataTarget.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDataTargetEmpty(org.rascalmpl.ast.DataTarget.Empty x) {
		return BoxADT.EMPTY;
	}

	public IValue visitDataTargetLabeled(Labeled x) {
		return eX(x.getLabel());
	}

	public IValue visitDataTypeSelectorAmbiguity(
			org.rascalmpl.ast.DataTypeSelector.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDataTypeSelectorSelector(Selector x) {
		return L(x.getClass().toString());
	}

	public IValue visitDateAndTimeAmbiguity(
			org.rascalmpl.ast.DateAndTime.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDateAndTimeLexical(
			org.rascalmpl.ast.DateAndTime.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitDatePartAmbiguity(org.rascalmpl.ast.DatePart.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDatePartLexical(org.rascalmpl.ast.DatePart.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralAmbiguity(
			org.rascalmpl.ast.DateTimeLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralDateAndTimeLiteral(DateAndTimeLiteral x) {
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralDateLiteral(DateLiteral x) {
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralTimeLiteral(TimeLiteral x) {
		return L(x.getClass().toString());
	}

	public IValue visitDecimalIntegerLiteralAmbiguity(
			org.rascalmpl.ast.DecimalIntegerLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDecimalIntegerLiteralLexical(
			org.rascalmpl.ast.DecimalIntegerLiteral.Lexical x) {
		return NM(x.getString());
	}

	public IValue visitDeclarationAlias(Alias x) {
		return list(
				eX(x.getTags()),
				H(1,
						eX(x.getVisibility()),
						KW("alias"),
						HOV(true, eX(x.getUser()), BoxADT.ASSIGN,
								H(0, eX(x.getBase()), BoxADT.semicolumn()))));
	}

	public IValue visitDeclarationAmbiguity(
			org.rascalmpl.ast.Declaration.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDeclarationAnnotation(
			org.rascalmpl.ast.Declaration.Annotation x) {
		return list(
				eX(x.getTags()),
				H(KW("anno"),
						eX(x.getAnnoType()),
						H(0, eX(x.getOnType()), BoxADT.AT, eX(x.getName()),
								BoxADT.SEMICOLON)));
	}

	public IValue visitDeclarationData(Data x) {
		/*
		 * Tags tags Visibility visibility "data" UserType user "=" {Variant
		 * "|"}+ variants ";"
		 */
		IValue r = H(1, eX(x.getVisibility()), getComment(x, 3), KW("data"),
				getComment(x, 5), eX(x.getUser()));
		java.util.List<Variant> vs = x.getVariants();
		IList b = BoxADT.getEmptyList();
		for (Variant v : vs) {
			IValue t = eX(v);
			b = b.append(I(H(0, (b.isEmpty() ? L("=") : L("|")), t)));
		}
		IList c = getComment(x, 11);
		b = b.concat(c);
		return V(0, eX(x.getTags()), getComment(x, 1),
				b.insert(r).append(I(BoxADT.semicolumn())));
	}

	public IValue visitDeclarationFunction(Function x) {
		return eX(x.getFunctionDeclaration());
	}

	public IValue visitDeclarationTag(Tag x) {
		return L(x.getClass().toString());
	}

	public IValue visitDeclarationVariable(
			org.rascalmpl.ast.Declaration.Variable x) {
		IList l = getTreeList(x, 4);
		return list(
				eX(x.getTags()),
				H(0,
						cList(H(1, eX(x.getVisibility()), getComment(x, 1),
								eX(x.getType())), getComment(x, 3),
								eXs0(x.getVariables(), l)), BoxADT.semicolumn()));
	}

	public IValue visitDeclaratorAmbiguity(
			org.rascalmpl.ast.Declarator.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitDeclaratorDefault(org.rascalmpl.ast.Declarator.Default x) {
		// IList l = getTreeList(x, 2);
		return cList(eX(x.getType()), getComment(x, 1),
		// eXs(x.getVariables(), l)); goed
				eXs(x.getVariables()));
	}

	public IValue visitExpressionAddition(
			org.rascalmpl.ast.Expression.Addition x) {
		return list(eX(x.getLhs()), BoxADT.PLUS, eX(x.getRhs()));
	}

	public IValue visitExpressionAll(All x) {
		/** "all" "(" generators:{Expression ","}+ ")" */
		return cStat(x, null, "all", eXs(x.getGenerators()), null);
	}

	public IValue visitExpressionAmbiguity(
			org.rascalmpl.ast.Expression.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitExpressionAnd(And x) {
		return list(eX(x.getLhs()), BoxADT.AND, eX(x.getRhs()));
	}

	public IValue visitExpressionAnti(Anti x) {
		return L(x.getClass().toString());
	}

	public IValue visitExpressionAny(Any x) {
		return cStat(x, null, "any", eXs(x.getGenerators()), null);
	}

	public IValue visitExpressionBracket(org.rascalmpl.ast.Expression.Bracket x) {
		return HOV(0, H(BoxADT.LPAR, eX(x.getExpression()), BoxADT.RPAR));
	}

	public IValue visitExpressionCallOrTree(CallOrTree x) {
		/**
		 * expression:Expression "(" arguments:{Expression ","}* ")" ->
		 * Expression {cons("CallOrTree")}
		 */
		if (x._getType() != null && x._getType() instanceof NonTerminalType) {
			// System.err.println("Backquote! " + x);
			return ESC("`" + x.toString() + "`");
		}
		isFunctionName = true;
		IValue t = H(0, eX(x.getExpression()), BoxADT.LPAR);
		isFunctionName = false;
		IValue r = eXs(x.getArguments(), null, null);
		// return makeHOV(list(t, r), true);
		return HOV(0, true, list(t, r), BoxADT.RPAR);
	}

	public IValue visitExpressionClosure(Closure x) {
		/**
		 * type:Type parameters:Parameters "{" statements:Statement+ "}" ->
		 * Expression {cons("Closure")}
		 */
		// TODO Auto-generated method stub
		return HOV(
				0,
				true,
				H(1, eX(x.getType()),
						H(0, BoxADT.LPAR, eX(x.getParameters()), BoxADT.RPAR, BoxADT.LBLOCK)),
				eXs0(x.getStatements()), BoxADT.RBLOCK);
	}

	public IValue visitExpressionComposition(Composition x) {
		return list(eX(x.getLhs()), KW(" o "), eX(x.getRhs()));
	}

	public IValue visitExpressionComprehension(Comprehension x) {
		return eX(x.getComprehension());
	}

	public IValue visitExpressionDescendant(Descendant x) {
		return list(BoxADT.DIVIDE, eX(x.getPattern()));
	}

	public IValue visitExpressionDivision(
			org.rascalmpl.ast.Expression.Division x) {
		return list(eX(x.getLhs()), BoxADT.DIVIDE, eX(x.getRhs()));
	}

	public IValue visitExpressionEnumerator(Enumerator x) {
		/* pattern:Expression "<-" expression:Expression */
		return list(eX(x.getPattern()), BoxADT.ELOF, eX(x.getExpression()));
	}

	public IValue visitExpressionEquals(Equals x) {
		return list(eX(x.getLhs()), BoxADT.EQUALS, eX(x.getRhs()));
	}

	public IValue visitExpressionEquivalence(Equivalence x) {
		return list(eX(x.getLhs()), BoxADT.EQUIVALENCE, eX(x.getRhs()));
	}

	public IValue visitExpressionFieldAccess(
			org.rascalmpl.ast.Expression.FieldAccess x) {
		return list(eX(x.getExpression()), BoxADT.DOT, eX(x.getField()));
	}

	public IValue visitExpressionFieldProject(FieldProject x) {
		/** expression:Expression "<" fields:{Field ","}+ ">" */
		return list(eX(x.getExpression()), BoxADT.LT,
				eXs(x.getFields(), null, null), BoxADT.GT);
	}

	public IValue visitExpressionFieldUpdate(FieldUpdate x) {
		/* expression:Expression "[" key:Name "=" replacement:Expression "]" */
		return list(eX(x.getExpression()), BoxADT.LBRACK, eX(x.getKey()),
				BoxADT.ASSIGN, eX(x.getReplacement()), BoxADT.RBRACK);
	}

	public IValue visitExpressionGetAnnotation(GetAnnotation x) {
		return list(eX(x.getExpression()), BoxADT.AT, eX(x.getName()));
	}

	public IValue visitExpressionGreaterThan(GreaterThan x) {
		return list(eX(x.getLhs()), BoxADT.GT, eX(x.getRhs()));
	}

	public IValue visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		return list(eX(x.getLhs()), BoxADT.GE, eX(x.getRhs()));
	}

	public IValue visitExpressionAsType(AsType x) {
		return L(x.getClass().toString());
	}

	public IValue visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		return list(eX(x.getLhs()), BoxADT.QUESTIONMARK, eX(x.getRhs()));
	}

	public IValue visitExpressionIfThenElse(IfThenElse x) {
		return list(eX(x.getCondition()), BoxADT.QUESTIONMARK,
				eX(x.getThenExp()), BoxADT.COLON, eX(x.getElseExp()));
	}

	public IValue visitExpressionImplication(Implication x) {
		/* lhs:Expression "==>" rhs:Expression */
		return list(eX(x.getLhs()), L("==>"), eX(x.getRhs()));
	}

	public IValue visitExpressionIn(In x) {
		return list(eX(x.getLhs()), KW(" in "), eX(x.getRhs()));
	}

	public IValue visitExpressionIntersection(
			org.rascalmpl.ast.Expression.Intersection x) {
		return list(eX(x.getLhs()), BoxADT.INTERSECTION, eX(x.getRhs()));
	}

	public IValue visitExpressionIsDefined(IsDefined x) {
		/* argument:Expression "?" */
		return list(eX(x.getArgument()), BoxADT.QUESTIONMARK);
	}

	public IValue visitExpressionJoin(Join x) {
		return list(eX(x.getLhs()), KW(" join "), eX(x.getRhs()));
	}

	public IValue visitExpressionLessThan(LessThan x) {
		return list(eX(x.getLhs()), BoxADT.LT, eX(x.getRhs()));
	}

	public IValue visitExpressionLessThanOrEq(LessThanOrEq x) {
		return list(eX(x.getLhs()), BoxADT.LE, eX(x.getRhs()));
	}

	public IValue visitExpressionList(org.rascalmpl.ast.Expression.List x) {
		IList r = list(eXs(x.getElements(), BoxADT.LBRACK, BoxADT.RBRACK));
		// System.err.println(""+width(r)+"<"+UNITLENGTH);
		return width(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionLiteral(Literal x) {
		IValue r = eX(x.getLiteral());
		// System.err.println("visitExpressionLiteral:"+r);
		return list(r);
	}

	public IValue visitExpressionMap(org.rascalmpl.ast.Expression.Map x) {
		IList r = list(eXs(x.getMappings(), BoxADT.LPAR, BoxADT.RPAR));
		return width(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionMatch(Match x) {
		/** pattern:Expression ":=" expression:Expression -> Expression **/
		// TODO Auto-generated method stub
		IList r = list(eX(x.getPattern()), L(":="), eX(x.getExpression()));
		return width(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionModulo(Modulo x) {
		return list(eX(x.getLhs()), BoxADT.MODULO, eX(x.getRhs()));
	}

	public IValue visitExpressionMultiVariable(MultiVariable x) {
		return list(eX(x.getQualifiedName()));
	}

	public IValue visitExpressionNegation(Negation x) {
		return list(BoxADT.NEGATION, eX(x.getArgument()));
	}

	public IValue visitExpressionNegative(Negative x) {
		return list(BoxADT.MINUS, eX(x.getArgument()));
	}

	public IValue visitExpressionNoMatch(NoMatch x) {
		IList r = list(eX(x.getPattern()), L("!:="), eX(x.getExpression()));
		return width(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		/* "{" statements:Statement+ "}" */
		return HOV(0, BoxADT.LBLOCK, eXs0(x.getStatements()), BoxADT.RBLOCK);
	}

	public IValue visitExpressionNonEquals(NonEquals x) {
		return list(eX(x.getLhs()), BoxADT.NOTEQUALS, eX(x.getRhs()));
	}

	public IValue visitExpressionNotIn(NotIn x) {
		return list(eX(x.getLhs()), KW(" notin "), eX(x.getRhs()));
	}

	public IValue visitExpressionOr(Or x) {
		return list(eX(x.getLhs()), BoxADT.OR, eX(x.getRhs()));
	}

	public IValue visitExpressionProduct(org.rascalmpl.ast.Expression.Product x) {
		return list(eX(x.getLhs()), BoxADT.MULT, eX(x.getRhs()));
	}

	public IValue visitExpressionQualifiedName(QualifiedName x) {
		return list(H(
				0,
				isFunctionName ? eX(x.getQualifiedName()) : eX(x
						.getQualifiedName())));
	}

	public IValue visitExpressionRange(org.rascalmpl.ast.Expression.Range x) {
		/* "[" first:Expression ".." last:Expression "]" */
		return list(BoxADT.LBRACK, eX(x.getFirst()), BoxADT.RANGE,
				eX(x.getLast()), BoxADT.RBRACK);
	}

	public IValue visitExpressionReifiedType(
			org.rascalmpl.ast.Expression.ReifiedType x) {
		return L(x.getClass().toString());
	}

	public IValue visitExpressionReifyType(ReifyType x) {
		return list(BoxADT.HASH, eX(x.getType()));
	}

	public IValue visitExpressionSet(org.rascalmpl.ast.Expression.Set x) {
		IList r = list(eXs(x.getElements(), BoxADT.LBLOCK, BoxADT.RBLOCK));
		return width(r) < UNITLENGTH ? list(H(r)) : r;
	}

	public IValue visitExpressionSetAnnotation(SetAnnotation x) {
		/** expression:Expression "[" "@" name:Name "=" value: Expression "]"} */

		return list(eX(x.getExpression()), BoxADT.LBRACK,
				H(0, BoxADT.AT, eX(x.getName())), BoxADT.RBRACK, BoxADT.ASSIGN,
				eX(x.getValue()));
	}

	public IValue visitExpressionStepRange(StepRange x) {
		return list(BoxADT.LBRACK, eX(x.getFirst()), BoxADT.COMMA,
				eX(x.getSecond()), BoxADT.RANGE, eX(x.getLast()), BoxADT.RBRACK);

	}

	public IValue visitExpressionSubscript(
			org.rascalmpl.ast.Expression.Subscript x) {
		return list(eX(x.getExpression()), BoxADT.LBRACK,
				eXs(x.getSubscripts(), null, null), BoxADT.RBRACK);
	}

	public IValue visitExpressionSubtraction(
			org.rascalmpl.ast.Expression.Subtraction x) {
		return list(eX(x.getLhs()), BoxADT.MINUS, eX(x.getRhs()));
	}

	public IValue visitExpressionTransitiveClosure(TransitiveClosure x) {
		return list(eX(x.getArgument()), BoxADT.PLUS, BoxADT.SPACE);
	}

	public IValue visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		return list(eX(x.getArgument()), BoxADT.MULT, BoxADT.SPACE);
	}

	public IValue visitExpressionTuple(org.rascalmpl.ast.Expression.Tuple x) {
		IList r = list(eXs(x.getElements(), BoxADT.LT, BoxADT.GT));
		return width(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionTypedVariable(TypedVariable x) {
		return list(H(1, eX(x.getType()), eX(x.getName())));
	}

	public IValue visitExpressionTypedVariableBecomes(TypedVariableBecomes x) {
		/* type:Type name:Name ":" pattern:Expression */
		return list(eX(x.getType()), BoxADT.SPACE, eX(x.getName()),
				BoxADT.COLON, eX(x.getPattern()));
	}

	public IValue visitExpressionVariableBecomes(VariableBecomes x) {
		/* name:Name ":" pattern:Expression */
		return H(0, eX(x.getName()), BoxADT.COLON, eX(x.getPattern()));
	}

	public IValue visitExpressionVisit(Visit x) {
		return L(x.getClass().toString());
	}

	public IValue visitExpressionVoidClosure(VoidClosure x) {
		return L(x.getClass().toString());
	}

	public IValue visitFieldAmbiguity(org.rascalmpl.ast.Field.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitFieldIndex(Index x) {
		return eX(x.getFieldIndex());
	}

	public IValue visitFieldName(Name x) {
		return eX(x.getFieldName());
	}

	public IValue visitFormalsAmbiguity(org.rascalmpl.ast.Formals.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitFormalsDefault(org.rascalmpl.ast.Formals.Default x) {
		return eXs(x.getFormals());
	}

	public IValue visitFunctionBodyAmbiguity(
			org.rascalmpl.ast.FunctionBody.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitFunctionBodyDefault(
			org.rascalmpl.ast.FunctionBody.Default x) {
		IList l = getTreeList(x, 2);
		return list(getComment(x, 1), eXs0(x.getStatements(), l),
				getComment(x, 3));
	}

	public IValue visitFunctionDeclarationAbstract(Abstract x) {
		/**
		 * tags:Tags visibility:Visibility signature:Signature ";" ->
		 * FunctionDeclaration {cons("Abstract")}
		 */
		// TODO Auto-generated method stub
		return V(
				0,
				eX(x.getTags()),
				H(1, eX(x.getVisibility()),
						H(0, eX(x.getSignature()), BoxADT.SEMICOLON)));
	}

	public IValue visitFunctionDeclarationAmbiguity(
			org.rascalmpl.ast.FunctionDeclaration.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitFunctionDeclarationDefault(
			org.rascalmpl.ast.FunctionDeclaration.Default x) {
		/**
		 * tags:Tags visibility:Visibility signature:Signature body:FunctionBody
		 */
		IList c1 = getComment(x, 5);
		return V(
				0,
				eX(x.getTags()),
				getComment(x, 1),
				cStat((c1.isEmpty() ? eX(x.getSignature()) : V(0,
						eX(x.getSignature()), c1)), eX(x.getBody())));
	}

	public IValue visitFunctionModifierAmbiguity(
			org.rascalmpl.ast.FunctionModifier.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitFunctionModifierJava(Java x) {
		return L("java");
	}

	public IValue visitFunctionModifiersAmbiguity(
			org.rascalmpl.ast.FunctionModifiers.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IList visitFunctionModifiersList(
			org.rascalmpl.ast.FunctionModifiers.List x) {
		return eXs0(x.getModifiers());
	}

	public IValue visitFunctionTypeAmbiguity(
			org.rascalmpl.ast.FunctionType.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitFunctionTypeTypeArguments(TypeArguments x) {
		/**
		 * type:Type "(" arguments:{TypeArg ","}* ")" -> FunctionType
		 * {cons("TypeArguments")}
		 */
		return list(eX(x.getType()), BoxADT.LPAR,
				eXs(x.getArguments(), null, null), BoxADT.RPAR);
	}

	public IValue visitHeaderAmbiguity(org.rascalmpl.ast.Header.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitHeaderDefault(org.rascalmpl.ast.Header.Default x) {
		/* "tags", "name","params", "imports" */
		IList l = getTreeList(x, 6);
		IValue r = V(eX(x.getTags()), getComment(x, 1),
				H(KW("module"), getComment(x, 3), H(0, eX(x.getName()))),
				getComment(x, 5), V(0, eXs0(x.getImports(), l)));
		return r;
	}

	public IValue visitHeaderParameters(Parameters x) {
		return L(x.getClass().toString());
	}

	public IValue visitHexIntegerLiteralAmbiguity(
			org.rascalmpl.ast.HexIntegerLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitHexIntegerLiteralLexical(
			org.rascalmpl.ast.HexIntegerLiteral.Lexical x) {
		/* lex [0] [X x] [0-9 A-F a-f]+ */
		return NM(x.getString());
	}

	public IValue visitImportAmbiguity(org.rascalmpl.ast.Import.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitImportDefault(org.rascalmpl.ast.Import.Default x) {
		return H(KW("import"), L(x.getModule().getName().toString() + ";"));
	}

	public IValue visitImportExtend(Extend x) {
		return H(KW("extend"), L(x.getModule().getName().toString() + ";"));
	}

	public IValue visitImportedModuleActuals(Actuals x) {
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleActualsRenaming(ActualsRenaming x) {
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleAmbiguity(
			org.rascalmpl.ast.ImportedModule.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleDefault(
			org.rascalmpl.ast.ImportedModule.Default x) {
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleRenamings(Renamings x) {
		return L(x.getClass().toString());
	}

	public IValue visitIntegerLiteralAmbiguity(
			org.rascalmpl.ast.IntegerLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(x.toString()));
	}

	public IValue visitIntegerLiteralHexIntegerLiteral(HexIntegerLiteral x) {
		return eX(x.getHex());
	}

	public IValue visitIntegerLiteralOctalIntegerLiteral(OctalIntegerLiteral x) {
		return eX(x.getOctal());
	}

	public IValue visitJustDateAmbiguity(org.rascalmpl.ast.JustDate.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitJustDateLexical(org.rascalmpl.ast.JustDate.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitJustTimeAmbiguity(org.rascalmpl.ast.JustTime.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitJustTimeLexical(org.rascalmpl.ast.JustTime.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindAlias(org.rascalmpl.ast.Kind.Alias x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindAll(org.rascalmpl.ast.Kind.All x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindAmbiguity(org.rascalmpl.ast.Kind.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindAnno(Anno x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindData(org.rascalmpl.ast.Kind.Data x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindFunction(org.rascalmpl.ast.Kind.Function x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindModule(org.rascalmpl.ast.Kind.Module x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindTag(org.rascalmpl.ast.Kind.Tag x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindVariable(org.rascalmpl.ast.Kind.Variable x) {
		return L(x.getClass().toString());
	}

	public IValue visitKindView(org.rascalmpl.ast.Kind.View x) {
		return L(x.getClass().toString());
	}

	public IValue visitLabelAmbiguity(org.rascalmpl.ast.Label.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitLabelDefault(org.rascalmpl.ast.Label.Default x) {
		return H(0, eX(x.getName()), BoxADT.COLON);
	}

	public IValue visitLabelEmpty(org.rascalmpl.ast.Label.Empty x) {
		return BoxADT.EMPTY;
	}

	public IValue visitLiteralAmbiguity(org.rascalmpl.ast.Literal.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitLiteralBoolean(Boolean x) {
		return eX(x.getBooleanLiteral());
	}

	public IValue visitLiteralDateTime(org.rascalmpl.ast.Literal.DateTime x) {
		return L(x.getClass().toString());
	}

	public IValue visitLiteralInteger(Integer x) {
		return eX(x.getIntegerLiteral());
	}

	public IValue visitLiteralLocation(Location x) {
		return eX(x.getLocationLiteral());
	}

	public IValue visitLiteralReal(org.rascalmpl.ast.Literal.Real x) {
		/** realLiteral:RealLiteral -> Literal {cons("Real")} */
		return eX(x.getRealLiteral());
	}

	public IValue visitLiteralRegExp(RegExp x) {
		/** regExpLiteral:RegExpLiteral -> Literal {cons("RegExp")} */
		// TODO Auto-generated method stub
		return H(0, eX(x.getRegExpLiteral()));
	}

	public IValue visitLiteralString(org.rascalmpl.ast.Literal.String x) {
		// System.err.println("VisitLiteral String:"+x.getStringLiteral().toString());
		return V(0, eX(x.getStringLiteral()));
	}

	public IValue visitLocalVariableDeclarationAmbiguity(
			org.rascalmpl.ast.LocalVariableDeclaration.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitLocalVariableDeclarationDefault(
			org.rascalmpl.ast.LocalVariableDeclaration.Default x) {
		Declarator declarator = x.getDeclarator();
		return eX(declarator);
	}

	public IValue visitLocalVariableDeclarationDynamic(Dynamic x) {
		/* dynamic" Declarator declarator */
		return H(1, KW("dynamic"), getComment(x, 1), eX(x.getDeclarator()));
	}

	public IValue visitLocationLiteralAmbiguity(
			org.rascalmpl.ast.LocationLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitLocationLiteralDefault(
			org.rascalmpl.ast.LocationLiteral.Default x) {
		/* protocolPart:ProtocolPart pathPart:PathPart */
		return list(eX(x.getProtocolPart()), eX(x.getPathPart()));
		// TODO Auto-generated method stub

	}

	public IValue visitMapping_ExpressionDefault(
			org.rascalmpl.ast.Mapping_Expression.Default x) {
		return H(0, eX(x.getFrom()), BoxADT.COLON, eX(x.getTo()));
	}

	public IValue visitMidPathCharsAmbiguity(
			org.rascalmpl.ast.MidPathChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitMidPathCharsLexical(
			org.rascalmpl.ast.MidPathChars.Lexical x) {
		return split(x.toString());
	}

	public IValue visitMidProtocolCharsAmbiguity(
			org.rascalmpl.ast.MidProtocolChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitMidProtocolCharsLexical(
			org.rascalmpl.ast.MidProtocolChars.Lexical x) {
		return split(x.toString());
	}

	public IValue visitMidStringCharsAmbiguity(
			org.rascalmpl.ast.MidStringChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitMidStringCharsLexical(
			org.rascalmpl.ast.MidStringChars.Lexical x) {
		return split(x.getString());
	}

	public IValue visitModuleActualsAmbiguity(
			org.rascalmpl.ast.ModuleActuals.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitModuleActualsDefault(
			org.rascalmpl.ast.ModuleActuals.Default x) {
		return L(x.getClass().toString());
	}

	public IValue visitModuleAmbiguity(org.rascalmpl.ast.Module.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitModuleDefault(org.rascalmpl.ast.Module.Default x) {
		return V(eX(x.getHeader()), getComment(x, 1), eX(x.getBody()));
	}

	public IValue visitModuleParametersAmbiguity(
			org.rascalmpl.ast.ModuleParameters.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitModuleParametersDefault(
			org.rascalmpl.ast.ModuleParameters.Default x) {
		return L(x.getClass().toString());
	}

	public IValue visitNameAmbiguity(org.rascalmpl.ast.Name.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitNameLexical(org.rascalmpl.ast.Name.Lexical x) {
		return L(x.getString());
	}

	public IValue visitNamedBackslashAmbiguity(
			org.rascalmpl.ast.NamedBackslash.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitNamedBackslashLexical(
			org.rascalmpl.ast.NamedBackslash.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitNamedRegExpAmbiguity(
			org.rascalmpl.ast.NamedRegExp.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitNamedRegExpLexical(
			org.rascalmpl.ast.NamedRegExp.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitOctalIntegerLiteralAmbiguity(
			org.rascalmpl.ast.OctalIntegerLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitOctalIntegerLiteralLexical(
			org.rascalmpl.ast.OctalIntegerLiteral.Lexical x) {
		return NM(x.getString());
	}

	public IValue visitParametersAmbiguity(
			org.rascalmpl.ast.Parameters.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitParametersDefault(org.rascalmpl.ast.Parameters.Default x) {
		return eX(x.getFormals());
	}

	public IValue visitParametersVarArgs(VarArgs x) {
		return eX(x.getFormals());
	}

	public IValue visitPathCharsAmbiguity(
			org.rascalmpl.ast.PathChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPathCharsLexical(org.rascalmpl.ast.PathChars.Lexical x) {
		return split(x.getString());
	}

	public IValue visitPathPartAmbiguity(org.rascalmpl.ast.PathPart.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPathPartInterpolated(Interpolated x) {
		return list(eX(x.getPre()), eX(x.getExpression()), eX(x.getTail()));
	}

	public IValue visitPathPartNonInterpolated(NonInterpolated x) {
		return eX(x.getPathChars());
	}

	public IValue visitPathTailAmbiguity(org.rascalmpl.ast.PathTail.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPathTailMid(Mid x) {
		return list(eX(x.getMid()), eX(x.getExpression()), eX(x.getTail()));
	}

	public IValue visitPathTailPost(Post x) {
		return eX(x.getPost());
	}

	public IValue visitPatternWithActionAmbiguity(
			org.rascalmpl.ast.PatternWithAction.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPatternWithActionArbitrary(Arbitrary x) {
		/* pattern:Expression ":" statement:Statement */
		return cStat(x, (String) null, null, null, eX(x.getPattern()),
				BoxADT.COLON, eX(x.getStatement()));
	}

	public IValue visitPatternWithActionReplacing(Replacing x) {
		// return list(HV(0, eX(x.getPattern())), L("=>"), eX(x
		// .getReplacement()));
		return cStat(x, (String) null, null, null, eX(x.getPattern()), L("=>"),
				eX(x.getReplacement()));
	}

	public IValue visitPostPathCharsAmbiguity(
			org.rascalmpl.ast.PostPathChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPostPathCharsLexical(
			org.rascalmpl.ast.PostPathChars.Lexical x) {
		return L(x.getString());
	}

	public IValue visitPostProtocolCharsAmbiguity(
			org.rascalmpl.ast.PostProtocolChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPostProtocolCharsLexical(
			org.rascalmpl.ast.PostProtocolChars.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitPostStringCharsAmbiguity(
			org.rascalmpl.ast.PostStringChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPostStringCharsLexical(
			org.rascalmpl.ast.PostStringChars.Lexical x) {
		return split(x.getString());
	}

	public IValue visitPrePathCharsAmbiguity(
			org.rascalmpl.ast.PrePathChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPrePathCharsLexical(
			org.rascalmpl.ast.PrePathChars.Lexical x) {
		return split(x.getString());
	}

	public IValue visitPreProtocolCharsAmbiguity(
			org.rascalmpl.ast.PreProtocolChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPreProtocolCharsLexical(
			org.rascalmpl.ast.PreProtocolChars.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitPreStringCharsAmbiguity(
			org.rascalmpl.ast.PreStringChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitPreStringCharsLexical(
			org.rascalmpl.ast.PreStringChars.Lexical x) {
		return split(x.getString());
	}

	public IValue visitProtocolCharsAmbiguity(
			org.rascalmpl.ast.ProtocolChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitProtocolCharsLexical(
			org.rascalmpl.ast.ProtocolChars.Lexical x) {
		return L(x.getString());
	}

	public IValue visitProtocolPartAmbiguity(
			org.rascalmpl.ast.ProtocolPart.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitProtocolPartInterpolated(
			org.rascalmpl.ast.ProtocolPart.Interpolated x) {
		return L(x.getClass().toString());
	}

	public IValue visitProtocolPartNonInterpolated(
			org.rascalmpl.ast.ProtocolPart.NonInterpolated x) {
		return eX(x.getProtocolChars());
	}

	public IValue visitProtocolTailAmbiguity(
			org.rascalmpl.ast.ProtocolTail.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitProtocolTailMid(org.rascalmpl.ast.ProtocolTail.Mid x) {
		return L(x.getClass().toString());
	}

	public IValue visitProtocolTailPost(org.rascalmpl.ast.ProtocolTail.Post x) {
		return L(x.getClass().toString());
	}

	public IValue visitQualifiedNameAmbiguity(
			org.rascalmpl.ast.QualifiedName.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitQualifiedNameDefault(
			org.rascalmpl.ast.QualifiedName.Default x) {
		/** names:{Name "::"}+ -> QualifiedName {cons("Default")} */
		// TODO Auto-generated method stub
		return eXs1(x.getNames());
	}

	public IValue visitRealLiteralAmbiguity(
			org.rascalmpl.ast.RealLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitRealLiteralLexical(
			org.rascalmpl.ast.RealLiteral.Lexical x) {
		return L(x.getString());
	}

	public IValue visitRegExpAmbiguity(org.rascalmpl.ast.RegExp.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitRegExpLexical(org.rascalmpl.ast.RegExp.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitRegExpLiteralAmbiguity(
			org.rascalmpl.ast.RegExpLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitRegExpLiteralLexical(
			org.rascalmpl.ast.RegExpLiteral.Lexical x) {
		return L(x.getString());
	}

	public IValue visitRegExpModifierAmbiguity(
			org.rascalmpl.ast.RegExpModifier.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitRegExpModifierLexical(
			org.rascalmpl.ast.RegExpModifier.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitRenamingAmbiguity(org.rascalmpl.ast.Renaming.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitRenamingDefault(org.rascalmpl.ast.Renaming.Default x) {
		return L(x.getClass().toString());
	}

	public IValue visitRenamingsAmbiguity(
			org.rascalmpl.ast.Renamings.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitRenamingsDefault(org.rascalmpl.ast.Renamings.Default x) {

		return L(x.getClass().toString());
	}

	public IValue visitReplacementAmbiguity(
			org.rascalmpl.ast.Replacement.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitReplacementConditional(Conditional x) {
		/* replacementExpression:Expression "when" conditions:{Expression ","} */
		return H(1, eX(x.getReplacementExpression()), KW("when"),
				eXs(x.getConditions()));
	}

	public IValue visitReplacementUnconditional(Unconditional x) {
		return eX(x.getReplacementExpression());
	}

	public IValue visitShellCommandAmbiguity(
			org.rascalmpl.ast.ShellCommand.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandEdit(Edit x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandHelp(Help x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandHistory(History x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandListDeclarations(ListDeclarations x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandListModules(ListModules x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandQuit(Quit x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandSetOption(SetOption x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandTest(org.rascalmpl.ast.ShellCommand.Test x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandUndeclare(Undeclare x) {
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandUnimport(Unimport x) {
		return L(x.getClass().toString());
	}

	public IValue visitSignatureAmbiguity(
			org.rascalmpl.ast.Signature.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitSignatureNoThrows(NoThrows x) {
		/**
		 * type:Type modifiers:FunctionModifiers name:Name parameters:Parameters
		 **/
		IValue t = HOV(0, true, H(0, eX(x.getName()), BoxADT.LPAR),
				eX(x.getParameters()), BoxADT.RPAR);
		return H(1, eX(x.getType()), eX(x.getModifiers()), t);
	}

	public IValue visitSignatureWithThrows(WithThrows x) {
		/**
		 * type:Type modifiers:FunctionModifiers name:Name parameters:Parameters
		 * "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")}
		 */
		IValue t = HOV(0, true, H(0, eX(x.getName()), BoxADT.LPAR),
				eX(x.getParameters()), BoxADT.RPAR);
		return H(1, eX(x.getType()), eX(x.getModifiers()), t, KW("throws"),
				eXs(x.getExceptions(), null, null));
	}

	// public IValue visitSingleQuotedStrCharAmbiguity(
	// org.rascalmpl.ast.SingleQuotedStrChar.Ambiguity x) {
	// return L(x.getClass().toString());
	// }
	//
	// public IValue visitSingleQuotedStrCharLexical(
	// org.rascalmpl.ast.SingleQuotedStrChar.Lexical x) {
	// return L(x.getClass().toString());
	// }
	//
	// public IValue visitSingleQuotedStrConAmbiguity(
	// org.rascalmpl.ast.SingleQuotedStrCon.Ambiguity x) {
	// return L(x.getClass().toString());
	// }
	//
	// public IValue visitSingleQuotedStrConLexical(
	// org.rascalmpl.ast.SingleQuotedStrCon.Lexical x) {
	// return L(x.getClass().toString());
	// }

	public IValue visitStatementAmbiguity(
			org.rascalmpl.ast.Statement.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStatementAppend(Append x) {
		return H(1, KW("append"), eX(x.getDataTarget()), eX(x.getStatement()));
	}

	public IValue visitStatementAssert(Assert x) {
		return H(1, KW("assert"), HV(0, eX(x.getExpression())));
	}

	public IValue visitStatementAssertWithMessage(AssertWithMessage x) {
		return H(1, KW("assert"), HV(0, eX(x.getExpression())), BoxADT.COLON,
				eX(x.getMessage()));
	}

	public IValue visitStatementAssignment(Assignment x) {
		return HOV(
				1,
				true,
				H(1, eX(x.getAssignable()), getComment(x, 1),
						eX(x.getOperator())), getComment(x, 3),
				eX(x.getStatement()));
	}

	public IValue visitStatementBreak(Break x) {
		return H(0, H(1, KW("break"), eX(x.getTarget())), BoxADT.SEMICOLON);
	}

	public IValue visitStatementContinue(Continue x) {
		return H(0, H(1, KW("continue"), eX(x.getTarget())), BoxADT.SEMICOLON);

	}

	public IValue visitStatementDoWhile(DoWhile x) {
		/**
		 * label:Label "do" body:Statement "while" "(" condition:Expression ")"
		 * ";"
		 */
		return H(
				1,
				cStat(x, eX(x.getLabel()), "do", null, eX(x.getBody())),
				H(0, cStat(x, null, "while", eX(x.getCondition()), null),
						BoxADT.SEMICOLON));
	}

	public IValue visitStatementEmptyStatement(EmptyStatement x) {
		return BoxADT.semicolumn();
	}

	public IValue visitStatementExpression(
			org.rascalmpl.ast.Statement.Expression x) {
		return HV(0, eX(x.getExpression()), getComment(x, 1), BoxADT.SEMICOLON);
	}

	public IValue visitStatementFail(Fail x) {
		return H(0, H(1, KW("fail"), eX(x.getTarget())), BoxADT.SEMICOLON);
	}

	public IValue visitStatementFor(For x) {
		return cStat(x, eX(x.getLabel()), "for", eXs(x.getGenerators()),
				eX(x.getBody()));
	}

	public IValue visitStatementFunctionDeclaration(FunctionDeclaration x) {
		return HV(eX(x.getFunctionDeclaration()));
	}

	public IValue visitStatementGlobalDirective(GlobalDirective x) {
		return L(x.getClass().toString());
	}

	public IValue visitStatementIfThen(IfThen x) {
		/**
		 * label:Label "if" "(" conditions:{Expression ","}+ ")"
		 * thenStatement:Statement noElseMayFollow:NoElseMayFollow
		 */
		return cStat(x, eX(x.getLabel()), "if", eXs(x.getConditions()),
				eX(x.getThenStatement()));
	}

	public IValue visitStatementIfThenElse(
			org.rascalmpl.ast.Statement.IfThenElse x) {
		return HOV(
				0,
				cStat(x, eX(x.getLabel()), "if", eXs(x.getConditions()),
						eX(x.getThenStatement())),
				cStat(x, "else", eX(x.getElseStatement())));
	}

	public IValue visitStatementInsert(Insert x) {
		return H(1, KW("insert"), eX(x.getDataTarget()), eX(x.getStatement()));
	}

	public IValue visitStatementNonEmptyBlock(
			org.rascalmpl.ast.Statement.NonEmptyBlock x) {
		/** label:Label "{" statements:Statement+ "}" */
		IList l = getTreeList(x, 4);
		return list(eX(x.getLabel()), getComment(x, 1), BoxADT.LBLOCK,
				getComment(x, 3), eXs0(x.getStatements(), l), getComment(x, 5),
				BoxADT.RBLOCK);
	}

	public IValue visitStatementReturn(Return x) {
		return HOV(1, true, KW("return"), getComment(x, 1),
				eX(x.getStatement()));
	}

	public IValue visitStatementSolve(Solve x) {
		/*
		 * "solve" "(" variables:{QualifiedName ","}+ bound:Bound ")"
		 * body:Statement}
		 */
		return cStat(x, null, "solve",
				list(eXs(x.getVariables()), eX(x.getBound())), eX(x.getBody()));
	}

	public IValue visitStatementSwitch(Switch x) {
		IList l = getTreeList(x, 12);
		return cStat(
				x,
				eX(x.getLabel()),
				"switch",
				eX(x.getExpression()),
				list(BoxADT.LBLOCK, getComment(x, 11), eXs0(x.getCases(), l),
						getComment(x, 13), BoxADT.RBLOCK));
	}

	public IValue visitStatementThrow(Throw x) {
		return Block(x.getStatement(), KW("throw"));
	}

	public IValue visitStatementTry(Try x) {
		/* "try" body:Statement handlers:Catch+ */
		java.util.List<Catch> handlers = x.getHandlers();
		IList r = list();
		for (Catch handler : handlers) {
			r = r.append(eX(handler));
		}
		return V(cStat(x, "try", eX(x.getBody())), r);
	}

	public IValue visitStatementTryFinally(TryFinally x) {
		java.util.List<Catch> handlers = x.getHandlers();
		IList r = list();
		for (Catch handler : handlers) {
			r = r.append(eX(handler));
		}
		return V(cStat(x, "try", eX(x.getBody())), r,
				cStat(x, null, "finally", null, eX(x.getFinallyBody())));
	}

	public IValue visitStatementVariableDeclaration(VariableDeclaration x) {
		LocalVariableDeclaration declaration = x.getDeclaration();
		return H(0, eX(declaration), L(";"));
	}

	public IValue visitStatementVisit(org.rascalmpl.ast.Statement.Visit x) {
		return H(1, eX(x.getLabel()), getComment(x, 1), eX(x.getVisit()));
	}

	public IValue visitStatementWhile(While x) {
		return cStat(x, eX(x.getLabel()), "while", eXs(x.getConditions()),
				eX(x.getBody()));
	}

	public IValue visitStrategyAmbiguity(org.rascalmpl.ast.Strategy.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStrategyBottomUp(BottomUp x) {
		return KW("bottom-up");
	}

	public IValue visitStrategyBottomUpBreak(BottomUpBreak x) {
		return KW("bottom-up-break");
	}

	public IValue visitStrategyInnermost(Innermost x) {
		return KW("innermost");
	}

	public IValue visitStrategyOutermost(Outermost x) {
		return KW("outermost");
	}

	public IValue visitStrategyTopDown(TopDown x) {
		return KW("top-down");
	}

	public IValue visitStrategyTopDownBreak(TopDownBreak x) {
		return KW("top-down-break");
	}

	public IValue visitStringCharacterAmbiguity(
			org.rascalmpl.ast.StringCharacter.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStringCharacterLexical(
			org.rascalmpl.ast.StringCharacter.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitStringConstantAmbiguity(
			org.rascalmpl.ast.StringConstant.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStringConstantLexical(
			org.rascalmpl.ast.StringConstant.Lexical x) {
		return STRING(x.getString());
	}

	public IValue visitStringLiteralAmbiguity(
			org.rascalmpl.ast.StringLiteral.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStringLiteralInterpolated(
			org.rascalmpl.ast.StringLiteral.Interpolated x) {
		// return V(0, H(0, list(eX(x.getPre())), eX(x.getExpression())),
		// System.err.println("stringLiteral:"+x.getTail().toString());
		IValue r = STRING(V(
				0,
				cTempl(eX(x.getPre()), HV(eX(x.getExpression())),
						eX(x.getTail()))));
		return r;
	}

	public IValue visitStringLiteralNonInterpolated(
			org.rascalmpl.ast.StringLiteral.NonInterpolated x) {
		// .err.println("visitStringLiteralNonInterpolated:"+x.getConstant().getClass());
		return STRING(eX(x.getConstant()));
	}

	public IValue visitStringLiteralTemplate(Template x) {
		return list(eX(x.getPre()), eX(x.getTemplate()), eX(x.getTail()));
	}

	public IValue visitStringMiddleAmbiguity(
			org.rascalmpl.ast.StringMiddle.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStringMiddleInterpolated(
			org.rascalmpl.ast.StringMiddle.Interpolated x) {
		return list(eX(x.getMid()), HV(eX(x.getExpression())), eX(x.getTail()));
	}

	public IValue visitStringMiddleMid(org.rascalmpl.ast.StringMiddle.Mid x) {
		return eX(x.getMid());
	}

	public IValue visitStringMiddleTemplate(
			org.rascalmpl.ast.StringMiddle.Template x) {
		return cTempl(eX(x.getMid()), eX(x.getTemplate()), eX(x.getTail()));
	}

	public IValue visitStringTailAmbiguity(
			org.rascalmpl.ast.StringTail.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStringTailMidInterpolated(MidInterpolated x) {
		// System.err.println("isStringTailMidTemplate:"+
		// x.getTail().toString());
		// return list(eX(x.getMid()), eX(x.getExpression()), eX(x.getTail()));
		// System.err.println("VISIT:" + x.getClass());
		// System.err.println("Mid:" + x.getMid() + " " +
		// x.getMid().getClass());
		// System.err.println("Expression:" + x.getExpression() + " "
		// + x.getExpression().getClass());
		// System.err
		// .println("Tail:" + x.getTail() + " " + x.getTail().getClass());
		return cTempl(eX(x.getMid()), HV(eX(x.getExpression())),
				eX(x.getTail()));
	}

	public IValue visitStringTailMidTemplate(MidTemplate x) {
		// System.err.println("isStringTailMidTemplate:"+
		// x.getTail().toString());
		// return H(eX(x.getMid()), eX(x.getTemplate()), eX(x.getTail()));
		return cTempl(eX(x.getMid()), eX(x.getTemplate()), eX(x.getTail()));
	}

	public IValue visitStringTailPost(org.rascalmpl.ast.StringTail.Post x) {
		return eX(x.getPost());
	}

	public IValue visitStringTemplateAmbiguity(
			org.rascalmpl.ast.StringTemplate.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStringTemplateDoWhile(
			org.rascalmpl.ast.StringTemplate.DoWhile x) {
		return list(L("do"), BoxADT.LBLOCK, eXs0(x.getPreStats()),
				eX(x.getBody()), eXs0(x.getPostStats()), BoxADT.RBLOCK,
				L("while"), BoxADT.LPAR, eX(x.getCondition()), BoxADT.RPAR);
	}

	public IValue visitStringTemplateFor(org.rascalmpl.ast.StringTemplate.For x) {
		return list(
				H(0, L("for"), BoxADT.LPAR, eXs(x.getGenerators(), null, null),
						BoxADT.RPAR, BoxADT.LBLOCK, eXs0(x.getPreStats())),
				HV(0, eX(x.getBody()), eXs0(x.getPostStats()), BoxADT.RBLOCK));
	}

	public IValue visitStringTemplateIfThen(
			org.rascalmpl.ast.StringTemplate.IfThen x) {
		return list(L("if"), BoxADT.LPAR, eXs(x.getConditions(), null, null),
				BoxADT.RPAR, BoxADT.LBLOCK, eXs0(x.getPreStats()),
				eX(x.getBody()), eXs0(x.getPostStats()), BoxADT.RBLOCK);

	}

	public IValue visitStringTemplateIfThenElse(
			org.rascalmpl.ast.StringTemplate.IfThenElse x) {
		return list(L("if"), BoxADT.LPAR, eXs(x.getConditions(), null, null),
				BoxADT.RPAR, BoxADT.LBLOCK, eXs0(x.getPreStatsThen()),
				eX(x.getThenString()), eXs0(x.getPostStatsThen()),
				BoxADT.RBLOCK, L("else"), BoxADT.LBLOCK,
				eXs0(x.getPreStatsElse()), eX(x.getElseString()),
				eXs0(x.getPostStatsElse()), BoxADT.RBLOCK);
	}

	public IValue visitStringTemplateWhile(
			org.rascalmpl.ast.StringTemplate.While x) {
		return list(L("while"), BoxADT.LPAR, eX(x.getCondition()), BoxADT.RPAR,
				BoxADT.LBLOCK, eXs0(x.getPreStats()), eX(x.getBody()),
				eXs0(x.getPostStats()), BoxADT.RBLOCK);
	}

	public IValue visitStructuredTypeAmbiguity(
			org.rascalmpl.ast.StructuredType.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitStructuredTypeDefault(
			org.rascalmpl.ast.StructuredType.Default x) {
		return H(0, eX(x.getBasicType()),
				eXs(x.getArguments(), BoxADT.LBRACK, BoxADT.RBRACK));
	}

	public IValue visitTagAmbiguity(org.rascalmpl.ast.Tag.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTagDefault(org.rascalmpl.ast.Tag.Default x) {
		/** "@" name:Name contents:TagString */
		return H(0, BoxADT.AT, eX(x.getName()), eX(x.getContents()));
	}

	public IValue visitTagEmpty(org.rascalmpl.ast.Tag.Empty x) {
		return H(0, BoxADT.AT, eX(x.getName()));
	}

	public IValue visitTagExpression(org.rascalmpl.ast.Tag.Expression x) {
		/* "@" name:Name "=" expression:Expression */
		return H(0, BoxADT.AT, eX(x.getName()), BoxADT.EQUALS,
				eX(x.getExpression()));
	}

	public IValue visitTagStringAmbiguity(
			org.rascalmpl.ast.TagString.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTagStringLexical(org.rascalmpl.ast.TagString.Lexical x) {
		return STRING(x.getString());
	}

	public IValue visitTagsAmbiguity(org.rascalmpl.ast.Tags.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTagsDefault(org.rascalmpl.ast.Tags.Default x) {
		IList l = getTreeList(x, 0);
		return eXs0(x.getTags(), l);
	}

	public IValue visitTargetAmbiguity(org.rascalmpl.ast.Target.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTargetEmpty(org.rascalmpl.ast.Target.Empty x) {
		return L("");
	}

	public IValue visitTargetLabeled(org.rascalmpl.ast.Target.Labeled x) {
		return eX(x.getName());
	}

	public IValue visitTimePartNoTZAmbiguity(
			org.rascalmpl.ast.TimePartNoTZ.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTimePartNoTZLexical(
			org.rascalmpl.ast.TimePartNoTZ.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitTimeZonePartAmbiguity(
			org.rascalmpl.ast.TimeZonePart.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTimeZonePartLexical(
			org.rascalmpl.ast.TimeZonePart.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitToplevelAmbiguity(org.rascalmpl.ast.Toplevel.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitToplevelGivenVisibility(GivenVisibility x) {
		return eX(x.getDeclaration());
	}

	public IValue visitTypeAmbiguity(org.rascalmpl.ast.Type.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTypeArgAmbiguity(org.rascalmpl.ast.TypeArg.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTypeArgDefault(org.rascalmpl.ast.TypeArg.Default x) {
		return x.getType().accept(this);
	}

	public IValue visitTypeArgNamed(Named x) {
		return H(1, eX(x.getType()), eX(x.getName()));
	}

	public IValue visitTypeBasic(Basic x) {
		return KW(x.toString());
	}

	public IValue visitTypeBracket(org.rascalmpl.ast.Type.Bracket x) {
		return L(x.getClass().toString());
	}

	public IValue visitTypeFunction(org.rascalmpl.ast.Type.Function x) {
		return eX(x.getFunction());
	}

	public IValue visitTypeSelector(org.rascalmpl.ast.Type.Selector x) {
		return L(x.getClass().toString());
	}

	public IValue visitTypeStructured(Structured x) {
		return eX(x.getStructured());
	}

	public IValue visitTypeSymbol(Symbol x) {
		return L(x.getClass().toString());
	}

	public IValue visitTypeUser(User x) {
		return eX(x.getUser());
	}

	public IValue visitTypeVarAmbiguity(org.rascalmpl.ast.TypeVar.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitTypeVarBounded(Bounded x) {
		/** "&" name:Name "<:" bound:Type **/
		// TODO Auto-generated method stub
		return list(BoxADT.AMPERSAND, eX(x.getName()), L("<:"),
				eX(x.getBound()));
	}

	public IValue visitTypeVarFree(Free x) {
		return H(0, BoxADT.AMPERSAND, eX(x.getName()));
	}

	public IValue visitTypeVariable(org.rascalmpl.ast.Type.Variable x) {
		return eX(x.getTypeVar());
	}

	public IValue visitURLCharsAmbiguity(org.rascalmpl.ast.URLChars.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitURLCharsLexical(org.rascalmpl.ast.URLChars.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitUnicodeEscapeAmbiguity(
			org.rascalmpl.ast.UnicodeEscape.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitUnicodeEscapeLexical(
			org.rascalmpl.ast.UnicodeEscape.Lexical x) {
		return L(x.getClass().toString());
	}

	public IValue visitUserTypeAmbiguity(org.rascalmpl.ast.UserType.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitUserTypeName(org.rascalmpl.ast.UserType.Name x) {
		return eX(x.getName());
	}

	public IValue visitUserTypeParametric(Parametric x) {
		/**
		 * name:Name "[" parameters:{Type ","}+ "]" -> UserType
		 * {cons("Parametric")}
		 */
		return HOV(0, true, H(0, eX(x.getName()), BoxADT.LBRACK),
				eXs(x.getParameters()), BoxADT.RBRACK);
	}

	public IValue visitVariableAmbiguity(org.rascalmpl.ast.Variable.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitVariableInitialized(Initialized x) {
		return cList(H(eX(x.getName()), getComment(x, 1), L("=")),
				getComment(x, 3), eX(x.getInitial()));
	}

	public IValue visitVariableUnInitialized(UnInitialized x) {
		return eX(x.getName());
	}

	public IValue visitVariantAmbiguity(org.rascalmpl.ast.Variant.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitVariantNAryConstructor(NAryConstructor x) {
		/* name:Name "(" arguments:{TypeArg ","}* ")" */
		// if (!x.getArguments().isEmpty())
		return I(H(0, eX(x.getName()), BoxADT.LPAR,
				HV(eXs(x.getArguments(), null, null)), BoxADT.RPAR));

		// return I(eX(x.getName()));
	}

	public IValue visitVisibilityAmbiguity(
			org.rascalmpl.ast.Visibility.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitVisibilityDefault(org.rascalmpl.ast.Visibility.Default x) {
		return null;
	}

	public IValue visitVisibilityPrivate(Private x) {
		return KW("private");
	}

	public IValue visitVisibilityPublic(Public x) {
		return KW("public");
	}

	public IValue visitVisitAmbiguity(org.rascalmpl.ast.Visit.Ambiguity x) {
		return L(x.getClass().toString());
	}

	public IValue visitVisitDefaultStrategy(DefaultStrategy x) {
		/* "visit" "(" Expression subject ")" "{" Case+ cases "}" */
		IList l = getTreeList(x, 10);
		return cStat(
				x,
				null,
				"visit",
				eX(x.getSubject()),
				list(BoxADT.LBLOCK, getComment(x, 9), eXs0(x.getCases(), l),
						getComment(x, 11), BoxADT.RBLOCK));
	}

	public IValue visitVisitGivenStrategy(GivenStrategy x) {
		// java.util.List<Case> cases = x.getCases();
		// Strategy s = x.getStrategy();
		// IList header = list(H(1, , KW("visit")),
		// BoxADT.LPAR, eX(x.getSubject()), BoxADT.RPAR, BoxADT.LBLOCK);
		// IList r = list(H(0, header));
		// for (Case c : cases) {
		// r = r.append(eX(c));
		// }
		// r = r.append(I(BoxADT.RBLOCK));
		IList l = getTreeList(x, 12);
		return V(
				0,
				eX(x.getStrategy()),
				cStat(x,
						null,
						"visit",
						eX(x.getSubject()),
						list(BoxADT.LBLOCK, getComment(x, 11),
								eXs0(x.getCases(), l), getComment(x, 13),
								BoxADT.RBLOCK)));
	}

	public Stack<Accumulator> getAccumulators() {
		return null;
	}

	public Environment getCurrentEnvt() {
		return null;
	}

	public Evaluator getEvaluator() {
		return null;
	}

	public GlobalEnvironment getHeap() {
		return null;
	}

	public String getStackTrace() {
		return null;
	}

	public IStrategyContext getStrategyContext() {
		return null;
	}

	public IValueFactory getValueFactory() {
		return null;
	}

	public void popStrategyContext() {

	}

	public void pushEnv() {

	}

	public void pushStrategyContext(IStrategyContext strategyContext) {

	}

	public boolean runTests() {
		return false;
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {

	}

	public void setCurrentEnvt(Environment environment) {

	}

	public void unwind(Environment old) {

	}

	static IValue KW(String s) {
		if (s == null)
			return null;
		return BoxADT.TAG.KW.create(BoxADT.TAG.L.create(s));
	}

	static IValue NM(String s) {
		if (s == null)
			return null;
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(s));
	}

	static IValue VAR(String s) {
		if (s == null)
			return null;
		return BoxADT.TAG.VAR.create(BoxADT.TAG.L.create(s));
	}

	static IValue STRING(String s) {
		if (s == null)
			return null;
		return BoxADT.TAG.STRING.create(BoxADT.TAG.L.create(s));
	}

	static IValue COMM(String s) {
		if (s == null)
			return null;
		return BoxADT.TAG.COMM.create(BoxADT.TAG.L.create(s));
	}

	static IValue ESC(String s) {
		if (s == null)
			return null;
		return BoxADT.TAG.ESC.create(BoxADT.TAG.L.create(s));
	}

	static IValue KW(IValue s) {
		return BoxADT.TAG.KW.create(s);
	}

	static IValue NM(IValue s) {
		return BoxADT.TAG.NM.create(s);
	}

	static IValue VAR(IValue s) {
		return BoxADT.TAG.VAR.create(s);
	}

	static IValue STRING(IValue s) {
		return BoxADT.TAG.STRING.create(s);
	}

	static IValue COMM(IValue s) {
		return BoxADT.TAG.COMM.create(s);
	}

	static IValue ESC(IValue s) {
		return BoxADT.TAG.ESC.create(s);
	}

	static IValue L(String s) {
		return BoxADT.TAG.L.create(s);
	}

	static IValue H(IValue... t) {
		return BoxADT.H(t);
	}

	static IValue H(int hspace, IValue... t) {
		return BoxADT.H(hspace, t);
	}

	static IValue V(IValue... t) {
		return BoxADT.V(t);
	}

	static IValue V(boolean indent, IValue... t) {
		return BoxADT.V(indent, t);
	}

	static IValue V(int vspace, IValue... t) {
		return BoxADT.V(vspace, t);
	}

	static IValue I(IValue... t) {
		// return V(t);
		return BoxADT.I(t);
	}

	static IValue HV(IValue... t) {
		return BoxADT.HV(t);
	}

	static IValue HV(int hspace, IValue... t) {
		return BoxADT.HV(hspace, t);
	}

	static IValue HOV(IValue... t) {
		return BoxADT.HOV(t);
	}

	static IValue HOV(boolean indent, IValue... t) {
		return BoxADT.HOV(indent, t);
	}

	static IValue HOV(int hspace, boolean indent, IValue... t) {
		return BoxADT.HOV(hspace, indent, t);
	}

	static IValue HV(int hspace, boolean indent, IValue... t) {
		return BoxADT.HV(hspace, indent, t);
	}

	static IValue HOV(int hspace, IValue... t) {
		return BoxADT.HOV(hspace, t);
	}

	public IValue visitDeclarationDataAbstract(DataAbstract x) {
		/* Tags tags Visibility visibility "data" UserType user ";" */
		return V(
				0,
				eX(x.getTags()),
				getComment(x, 1),
				H(1,
						eX(x.getVisibility()),
						getComment(x, 3),
						KW("data"),
						getComment(x, 5),
						H(0, eX(x.getUser()), getComment(x, 7),
								BoxADT.SEMICOLON)));

	}

	public IValue visitExpressionIt(It x) {
		return KW("it");
	}

	public IValue visitExpressionReducer(Reducer x) {
		/*
		 * "(" Expression init "|" Expression result "|" {Expression ","}+
		 * generators ")"
		 */

		return list(BoxADT.LPAR, eX(x.getInit()), BoxADT.VBAR,
				eX(x.getResult()), BoxADT.VBAR, eXs(x.getGenerators()),
				BoxADT.RPAR);
	}

	// IValue EmptyBlock(org.rascalmpl.ast.Statement statement) {
	// IValue t = statement.accept(this);
	// if (t == null)
	// return visitStatementEmptyStatement(null);
	// return t;
	// }

//	private boolean isListElement(AbstractAST expression) {
//		return expression instanceof Expression.List
//				|| expression instanceof Expression.Set
//				|| expression instanceof Expression.Tuple
//				|| expression instanceof Expression.Map;
//	}

	@SuppressWarnings({"rawtypes" })
	private IValue eXs(java.util.List conditions, IValue prefix, IValue suffix) {
		IList s = BoxADT.getEmptyList();
		// System.err.println("eXs0:"+conditions.size());
		// boolean listElement = false;
		if (conditions.size() > 0)
			for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
				AbstractAST expression = (AbstractAST) iterator.next();
				// System.err.println("eXs1:"+expression);
				if (expression == null)
					continue;
				IValue q = eX(expression);
				// if (!listElement
				// && (isListElement(expression) || width(q) > SIGNIFICANT))
				// listElement = true;
				if (s.isEmpty()) {
					if (prefix != null)
						s = s.append(H(0, prefix, q));
					else
						s = s.append(H(0, q));
				} else
					s = s.append(H(0, BoxADT.COMMA, q));
			}
		else if (prefix != null)
			s = s.append(prefix);
		if (suffix != null)
			s = s.append(suffix);
		return s.isEmpty() ? s : HOV(s);
		// return listElement ? makeHOV(s, false) : s;
	}

	/**
	 * @param expressions
	 * @return list of expressions separated by ','
	 */
	@SuppressWarnings("rawtypes")
	private IValue eXs(java.util.List expressions) {
		return eXs(expressions, null, null);
	}

	@SuppressWarnings("unchecked")
	private IValue eXs1(@SuppressWarnings("rawtypes") java.util.List conditions) {
		IList s = BoxADT.getEmptyList();
		// System.err.println("eXs0:"+conditions.size());
		for (Iterator<AbstractAST> iterator = conditions.iterator(); iterator
				.hasNext();) {
			AbstractAST expression = iterator.next();
			// System.err.println("eXs1:"+expression);
			if (expression == null)
				continue;
			IValue q = eX(expression);
			if (!s.isEmpty())
				s = s.append(BoxADT.COLONCOLON);
			if (q.getType().isListType())
				s = s.concat((IList) q);
			else
				s = s.append(q);
		}
		// System.err.println("eXs2:"+s);
		return H(0, s);
	}

	@SuppressWarnings("rawtypes")
	private IList eXs0(java.util.List conditions) {
		IList s = BoxADT.getEmptyList();
		// System.err.println("eXs0:"+conditions.size());
		for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
			AbstractAST expression = (AbstractAST) iterator.next();
			// System.err.println("eXs1:"+expression);
			if (expression == null)
				continue;
			IValue q = eX(expression);
			if (q.getType().isListType())
				s = s.concat((IList) q);
			else
				s = s.append(q);
		}
		// System.err.println("eXs2:"+s);
		return s;
	}

	@SuppressWarnings("rawtypes")
	private IValue eXs0(java.util.List conditions, IList tree) {
		IList s = BoxADT.getEmptyList();
		int i = 1, n = tree.length();
		for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
			AbstractAST expression = (AbstractAST) iterator.next();
			// System.err.println("eXs0:"+expression);
			IValue q = eX(expression);
			if (q.getType().isListType())
				s = s.concat(((IList) q));
			else
				s = s.append(q);
			if (i < n) {
				IList t = getComment(tree, i);
				final int m = width(t);
				if (m > 0 && m < SMALLCOMMENTSIZE) {
					IValue g = s.get(s.length() - 1);
					s = s.delete(s.length() - 1);
					s = s.append(H(1, g, t));
				} else
					s = s.concat(t);
			}
			i += 2;
		}
		// System.err.println("eXs2:"+s);
		return s;
	}

	private IValue Comprehension(org.rascalmpl.ast.Comprehension x,
			IValue start, IValue end) {
		return list(start, eXs(x.getResults(), null, null),
				eXs(x.getGenerators(), BoxADT.VBAR, end));
	}

	private IValue ComprehensionMap(org.rascalmpl.ast.Comprehension x,
			IValue start, IValue end) {
		return list(start, eX(x.getFrom()), BoxADT.COLON, eX(x.getTo()),
				eXs(x.getGenerators(), BoxADT.VBAR, end));
	}

	private static IList list(IValue... t) {
		return BoxADT.getList(t);
	}

	IValue Block(org.rascalmpl.ast.Statement body, IValue... start) {
		IValue b = eX(body);
		if (b != null) {
			boolean isList = b.getType().isListType();
			IList header = list(start);
			if (isList) {
				header = header.append(BoxADT.LBLOCK);
				IList a = list(H(0, header));
				a = a.concat((IList) b);
				a = a.append(I(BoxADT.RBLOCK));
				return V(0, a);
			}

			if (width(header) < SIGNIFICANT) {
				IList a = list(H(0, header));
				a = a.append(b);
				return H(1, a);
			}

			return V(0, H(0, header), I(b));
		}
		return H(L("???"));
	}

	private IValue eX(AbstractAST x) {
		// System.err.println("eX:" + x.getClass());
		return x.accept(this);
	}

	private String contentBoxList(IList bl) {
		StringBuffer r = new StringBuffer();
		for (IValue x : bl) {
			if (x.getType().isAbstractDataType()) {
				IConstructor b = (IConstructor) x;
				r.append(contentBox(b));
			}
		}
		return r.toString();
	}

	private String contentBox(IConstructor b) {
		IValue v = b.get(0);
		if (v.getType().isStringType()) {
			return ((IString) v).getValue();
		}
		if (v.getType().isListType()) {
			return contentBoxList((IList) v);
		}
		return contentBox((IConstructor) v);
	}

	// private int lengths(IList bl) {
	// return contentBoxList(bl).length();
	// }

	private IList getComment(IList z, int ind) {
		IList c = TreeAdapter.searchCategory((IConstructor) z.get(ind),
				"Comment");
		IList r = list();
		for (IValue t : c) {
			String s = TreeAdapter.yield((IConstructor) t);
			if (s.endsWith("\n") && r.length() == c.length() - 1)
				s = s.substring(0, s.length() - 1);
			if (s.startsWith("//"))
				r = r.append(V(0, COMM(s), BoxADT.EMPTY));
			else
				r = r.append(COMM(s));
		}
		if (c.length() >= 2)
			return list(H(0, r));

		return r;
	}

	IList getComment(AbstractAST a, int ind) {
		IList z = TreeAdapter.getArgs(a.getTree());
		return getComment(z, ind);
	}

	private IList getTreeList(AbstractAST a, int ind) {
		IList z = TreeAdapter.getArgs(a.getTree());
		IList listToplevels = TreeAdapter.getArgs((IConstructor) z.get(ind));
		return listToplevels;
	}

	private IValue split(String x) {
		String[] s = x.split("\n");
		// System.err.println("visitStrin:" + s.length + " " + x);
		if (s.length == 1)
			return BoxADT.getList(L(x));

		IValue[] v = new IValue[s.length];
		for (int i = 0; i < v.length; i++)
			v[i] = L(s[i]);
		return list(v);
	}

	public IValue visitBasicTypeNum(Num x) {
		return KW("num");
	}

	private int widthC(IConstructor b) {
		if (b.arity() > 0) {
			IValue v = b.get(0);
			if (v.getType().isStringType())
				return ((IString) v).getValue().length();
			if (v.getType().isListType())
				return widthL((IList) v);
			return widthC((IConstructor) v);
		}
		return 0;
	}

	private int widthL(IList bs) {
		int r = 0;
		for (IValue b : bs) {
			r += widthC((IConstructor) b);
		}
		return r;
	}

	private int width(IValue bs) {
		if (bs.getType().isListType()) {
			return widthL((IList) bs);
		}
		return widthC((IConstructor) bs);
	}

	private IValue head(IValue statement, boolean always) {
		if (statement == null)
			return null;
		if (always && !statement.getType().isListType())
			return statement;
		if (statement.getType().isListType()) {
			if (((IList) statement).length() == 0)
				return null;
			IValue f = ((IList) statement).get(0);
			return always || f.isEqual(BoxADT.LBLOCK) ? f : null;
		}
		return null;
	}

	private IValue tail(IValue statement, boolean always) {
		if (statement == null)
			return null;
		if (always && !statement.getType().isListType())
			return null;
		if (statement.getType().isListType()) {
			IValue f = ((IList) statement).get(0);
			IValue r = ((IList) statement).delete(0);
			return always || f.isEqual(BoxADT.LBLOCK) ? r
					: HV((IList) statement);
		}
		return statement;
	}

	/* cStat(x, null, "try", null, eX(x.getBody())) */
	/*
	 * cStat(x, null, "visit", eX(x.getSubject()), list(BoxADT.LBLOCK,
	 * eXs0(x.getCases()), BoxADT.RBLOCK));
	 */
	private IValue cStat(AbstractAST x, IValue label, String name, IValue exs,
			IValue body) {
		return cStat(x, name, null, BoxADT.LPAR, exs, BoxADT.RPAR, body);
	}

	private IValue cStat(AbstractAST x, String name, IValue body) {
		return cStat(x, name, null, null, null, null, body);
	}

	private IValue cStat(IValue hBox, IValue body) {
		return HOV(1, true, H(1, hBox, BoxADT.LBLOCK), body, BoxADT.RBLOCK);
	}

	/* cStat(x, null, "try", null, eX(x.getBody())) */
	/*
	 * cStat(x, "default", null, BoxADT.COLON, null, null,
	 * eX(x.getStatement()));
	 */

	private IValue cStat(AbstractAST x, String name, IValue label, IValue left,
			IValue exs, IValue right, IValue body) {
		final IValue t;
		final int i = (name == null) ? 1 : (label == null) ? 3 : 5;
		if (exs != null && left != null) {
			t = list(left, getComment(x, i), HV(exs), getComment(x, i + 2),
					right, right != null && body != null ? getComment(x, i + 4)
							: null);
		} else if (exs != null && left == null)
			t = list((name != null ? BoxADT.SPACE : left), HV(exs),
					getComment(x, i), right,
					right != null && body != null ? getComment(x, i + 2) : null);
		else if (exs == null && left != null)
			t = list(left, getComment(x, i), right, right != null
					&& body != null ? getComment(x, i + 2) : null);
		else
			t = null;
		final IValue q;
		if (name != null) {
			if (label != null)
				q = H(1, label, getComment(x, 1), KW(name), getComment(x, 3),
						t == null ? BoxADT.EMPTY : H(0, t), head(body, false));
			else
				q = H(1, KW(name), getComment(x, 1), t == null ? BoxADT.EMPTY
						: H(0, t), head(body, false));
		} else
			q = H(1, t == null ? BoxADT.EMPTY : H(0, t), head(body, false));
		return HOV(1, name != null, q, tail(body, false));
	}

	private IValue cList(IValue hBox, IValue comment, IValue body) {

		IValue r = HOV(0, hBox != null,
				H(1, hBox, comment, H(0, head(body, true))), tail(body, true));
		return r;
	}

	private IValue cTempl(IValue left, IValue exs, IValue body) {
		IList t;
		if (left.getType().isListType()) {
			IList q = (IList) left;
			t = list(q.sublist(0, q.length() - 1),
					H(q.get(q.length() - 1), exs, head(body, true)));
		} else
			t = list(left, H(exs, head(body, true)));
		// if (body.getType().isListType()) {
		// System.err.println("size:"+((IList) body).length());
		// // if (((IList) body).length()>0) {
		// // System.err.println(((IList) body).get(0));
		// // }
		// } else {
		// System.err.println("cTemp:"+body.getType());
		// // System.err.println(body);
		// }
		return list(t, tail(body, true));
	}

	public IValue visitAssocAmbiguity(org.rascalmpl.ast.Assoc.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitAssocLeft(Left x) {
		// TODO Auto-generated method stub
		return KW("left");
	}

	public IValue visitAssocRight(Right x) {
		// TODO Auto-generated method stub
		return KW("right");
	}

	public IValue visitCharAmbiguity(org.rascalmpl.ast.Char.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCharLexical(Char.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitClassAmbiguity(org.rascalmpl.ast.Class.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitClassBracket(org.rascalmpl.ast.Class.Bracket x) {
		// TODO Auto-generated method stub
		return H(BoxADT.LBRACK, eX(x.getCharclass()), BoxADT.RBRACK);
	}

	public IValue visitClassComplement(org.rascalmpl.ast.Class.Complement x) {
		// TODO Auto-generated method stub
		return H(0, BoxADT.NOT, eX(x.getCharClass()));
	}

	public IValue visitClassDifference(org.rascalmpl.ast.Class.Difference x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitClassIntersection(org.rascalmpl.ast.Class.Intersection x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitClassSimpleCharclass(
			org.rascalmpl.ast.Class.SimpleCharclass x) {
		// TODO Auto-generated method stub
		return eXs0(x.getRanges());
	}

	public IValue visitClassUnion(org.rascalmpl.ast.Class.Union x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportSyntax(Syntax x) {
		// TODO Auto-generated method stub
		return eX(x.getSyntax());
		// return eX(x.getSyntax());
	}

	// public IValue visitLanguageActionBuild(Build x) {
	// // TODO Auto-generated method stub
	// return L(x.getClass().toString());
	// }

	public IValue visitNonterminalAmbiguity(
			org.rascalmpl.ast.Nonterminal.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNonterminalLabelAmbiguity(
			org.rascalmpl.ast.NonterminalLabel.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNonterminalLabelLexical(
			org.rascalmpl.ast.NonterminalLabel.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitNonterminalLexical(
			org.rascalmpl.ast.Nonterminal.Lexical x) {
		// TODO Auto-generated method stub
		return ESC(x.getString());
	}

	public IValue visitProdAll(org.rascalmpl.ast.Prod.All x) {
		// TODO Auto-generated method stub
		IList ms = null;
		if (x.hasModifiers()) {
			ms = eXs0(x.getModifiers());
		}
		IValue v = eX(x.getRhs());
		if (v.getType().isListType()) {
			IList vs = (IList) v;
			if (vs.length() > 0)
				return list(ms, eX(x.getLhs()), H(1, BoxADT.VBAR, vs.get(0)),
						vs.delete(0));

		}
		return list(ms, eX(x.getLhs()), H(1, BoxADT.VBAR, v));
	}

	public IValue visitProdAmbiguity(org.rascalmpl.ast.Prod.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProdAssociativityGroup(AssociativityGroup x) {
		// TODO Auto-generated method stub
		IList ms = null;
		if (x.hasModifiers()) {
			ms = eXs0(x.getModifiers());
		}
		IValue v = eX(x.getGroup());
		if (v.getType().isListType()) {
			IList vs = (IList) v;
			if (vs.length() > 0)
				return list(ms,
						H(1, eX(x.getAssociativity()), BoxADT.LPAR, vs.get(0)),
						vs.delete(0), BoxADT.RPAR);

		}
		return H(1, ms, eX(x.getAssociativity()), v);
	}

	public IValue visitProdFirst(First x) {
		IList ms = null;
		if (x.hasModifiers()) {
			ms = eXs0(x.getModifiers());
		}
		IValue v = eX(x.getRhs());
		if (v.getType().isListType()) {
			IList vs = (IList) v;

			if (vs.length() > 0)
				return list(ms, eX(x.getLhs()), H(1, L(">"), vs.get(0)),
						vs.delete(0));

		}
		return list(ms, eX(x.getLhs()), H(1, L(">"), v));

	}

	public IValue visitProdLabeled(org.rascalmpl.ast.Prod.Labeled x) {
		/* "modifiers", "name", "args" */
		IList ms = null;
		if (x.hasModifiers()) {
			ms = eXs0(x.getModifiers());
		}
		IList l = getTreeList(x, 4);
		return H(1, ms, H(0, eX(x.getName()), BoxADT.COLON),
				eXs0(x.getArgs(), l));
	}

	public IValue visitProdModifierAmbiguity(
			org.rascalmpl.ast.ProdModifier.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProdModifierAssociativity(Associativity x) {
		// TODO Auto-generated method stub
		return eX(x.getAssociativity());
	}

	public IValue visitProdModifierBracket(
			org.rascalmpl.ast.ProdModifier.Bracket x) {
		// TODO Auto-generated method stub
		return KW("bracket");
	}

	public IValue visitProdOthers(Others x) {
		// TODO Auto-generated method stub
		return L("...");
	}

	public IValue visitProdReference(Reference x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProdUnlabeled(org.rascalmpl.ast.Prod.Unlabeled x) {
		// TODO Auto-generated method stub
		IList ms = null;
		if (x.hasModifiers()) {
			ms = eXs0(x.getModifiers());
		}
		IList l = getTreeList(x, 2);
		return H(1, ms, eXs0(x.getArgs(), l));
	}

	public IValue visitRangeAmbiguity(org.rascalmpl.ast.Range.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRangeCharacter(org.rascalmpl.ast.Range.Character x) {
		// TODO Auto-generated method stub
		return eX(x.getCharacter());
	}

	public IValue visitStartAmbiguity(org.rascalmpl.ast.Start.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymAmbiguity(org.rascalmpl.ast.Sym.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymCaseInsensitiveLiteral(
			org.rascalmpl.ast.Sym.CaseInsensitiveLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymCharacterClass(org.rascalmpl.ast.Sym.CharacterClass x) {
		// TODO Auto-generated method stub
		return H(0, BoxADT.LBRACK, eX(x.getCharClass()), BoxADT.RBRACK);
	}

	public IValue visitSymColumn(Column x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymEndOfLine(EndOfLine x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymIter(org.rascalmpl.ast.Sym.Iter x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getSymbol()), BoxADT.PLUS);
	}

	public IValue visitSymIterSep(org.rascalmpl.ast.Sym.IterSep x) {
		// TODO Auto-generated method stub
		return H(0, BoxADT.LBLOCK, eX(x.getSymbol()), eX(x.getSep()),
				BoxADT.RBLOCK, BoxADT.PLUS);
	}

	public IValue visitSymIterStar(org.rascalmpl.ast.Sym.IterStar x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getSymbol()), BoxADT.MULT);
	}

	public IValue visitSymIterStarSep(org.rascalmpl.ast.Sym.IterStarSep x) {
		// TODO Auto-generated method stub
		return H(0, BoxADT.LBLOCK, eX(x.getSymbol()), eX(x.getSep()),
				BoxADT.RBLOCK, BoxADT.MULT);
	}

	public IValue visitSymLabeled(org.rascalmpl.ast.Sym.Labeled x) {
		// TODO Auto-generated method stub
		return H(1, eX(x.getSymbol()), eX(x.getLabel()));
	}

	public IValue visitSymLiteral(org.rascalmpl.ast.Sym.Literal x) {
		// TODO Auto-generated method stub
		return eX(x.getString());
	}

	public IValue visitSymNonterminal(Nonterminal x) {
		// TODO Auto-generated method stub
		return eX(x.getNonterminal());
	}

	public IValue visitSymOptional(org.rascalmpl.ast.Sym.Optional x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getSymbol()), BoxADT.QUESTIONMARK);
	}

	public IValue visitSymParametrized(Parametrized x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getNonterminal()), BoxADT.LBRACK,
				eXs(x.getParameters()), BoxADT.RBRACK);
	}

	public IValue visitSymStartOfLine(StartOfLine x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSyntaxDefinitionAmbiguity(
			org.rascalmpl.ast.SyntaxDefinition.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSyntaxDefinitionLanguage(Language x) {
		// TODO Auto-generated method stub
		if (!x.hasProduction())
			return L(x.getClass().toString());
		IValue w = eX(x.getProduction());
		if (w.getType().isListType()) {
			IList vs = (IList) w;
			if (vs.length() > 0) {
					IValue v = H(1, BoxADT.ASSIGN, vs.get(0));
					vs = vs.delete(0);
					return V(
							0,
							H(0,
									H(1, eX(x.getStart()), KW("syntax"),
											eX(x.getDefined())),
									V(v, vs, BoxADT.SEMICOLON)));
			}
		}
		return V(
				0,
				H(1, eX(x.getStart()), KW("syntax"), eX(x.getDefined()),
						BoxADT.ASSIGN, w, BoxADT.SEMICOLON), BoxADT.SPACE);
	}

	public IValue visitSyntaxDefinitionLayout(Layout x) {
		// TODO Auto-generated method stub
		if (!x.hasProduction())
			return L(x.getClass().toString());
		IValue w = eX(x.getProduction());
		if (w.getType().isListType()) {
			IList vs = (IList) w;
			if (vs.length() > 0) {
					IValue v = H(1, BoxADT.ASSIGN, vs.get(0));
					vs = vs.delete(0);
					return V(
							0,
							H(0, H(1, KW("layout"), eX(x.getDefined())),
									V(v, vs, BoxADT.SEMICOLON)));
			}
		}
		return V(
				0,
				H(1, KW("layout"), eX(x.getDefined()), BoxADT.ASSIGN, w,
						BoxADT.SEMICOLON), BoxADT.SPACE);

	}

	public IValue visitAssocAssociative(Associative x) {
		// TODO Auto-generated method stub
		return KW("assoc");
	}

	public IValue visitAssocNonAssociative(NonAssociative x) {
		// TODO Auto-generated method stub
		return KW("non-assoc");
	}

	public IValue visitCaseInsensitiveStringConstantAmbiguity(
			org.rascalmpl.ast.CaseInsensitiveStringConstant.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCaseInsensitiveStringConstantLexical(
			org.rascalmpl.ast.CaseInsensitiveStringConstant.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRangeFromTo(FromTo x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getStart()), BoxADT.MINUS, eX(x.getEnd()));
	}

	public IValue visitStartAbsent(org.rascalmpl.ast.Start.Absent x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStartPresent(org.rascalmpl.ast.Start.Present x) {
		// TODO Auto-generated method stub
		return KW("start");
	}

//	public IValue visitParameterizedNonterminalAmbiguity(
//			org.rascalmpl.ast.ParameterizedNonterminal.Ambiguity x) {
//		// TODO Auto-generated method stub
//		return L(x.getClass().toString());
//	}
//
//	public IValue visitParameterizedNonterminalLexical(
//			org.rascalmpl.ast.ParameterizedNonterminal.Lexical x) {
//		// TODO Auto-generated method stub
//		return ESC(x.getString());
//	}

	public IValue visitSymParameter(Parameter x) {
		// TODO Auto-generated method stub
		return H(0, BoxADT.AMPERSAND, eX(x.getNonterminal()));
	}

	public IValue visitAssignableBracket(org.rascalmpl.ast.Assignable.Bracket x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLAYOUTAmbiguity(org.rascalmpl.ast.LAYOUT.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLAYOUTLexical(org.rascalmpl.ast.LAYOUT.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMapping_ExpressionAmbiguity(
			org.rascalmpl.ast.Mapping_Expression.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPreModuleAmbiguity(
			org.rascalmpl.ast.PreModule.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPreModuleDefault(org.rascalmpl.ast.PreModule.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProdModifierTag(org.rascalmpl.ast.ProdModifier.Tag x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionHas(Has x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionIs(Is x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFunctionModifierDefault(
			org.rascalmpl.ast.FunctionModifier.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFunctionDeclarationExpression(
			org.rascalmpl.ast.FunctionDeclaration.Expression x) {
		// TODO Auto-generated method stub
		// Tags Visibility Signature "=" Expression ";"
		return V(
				0,
				eX(x.getTags()),
				H(1, eX(x.getVisibility()), H(0, eX(x.getSignature())),
						BoxADT.ASSIGN,
						H(0, eX(x.getExpression()), BoxADT.SEMICOLON)));
	}

	public IValue visitSyntaxDefinitionKeyword(Keyword x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSyntaxDefinitionLexical(
			org.rascalmpl.ast.SyntaxDefinition.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymNotFollow(NotFollow x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymSequence(Sequence x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymAlternative(Alternative x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymStart(Start x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymNotPrecede(NotPrecede x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymEmpty(org.rascalmpl.ast.Sym.Empty x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymFollow(org.rascalmpl.ast.Sym.Follow x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymUnequal(Unequal x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitSymPrecede(Precede x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());

	}

	public IValue visitCommandsAmbiguity(org.rascalmpl.ast.Commands.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IValue visitCommandsList(org.rascalmpl.ast.Commands.List x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionDeclarationConditional(
			org.rascalmpl.ast.FunctionDeclaration.Conditional x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitFunctionModifierTest(Test x) {
		return L("test");
	}

	@Override
	public IValue visitLiteralRational(Rational x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitStatementFilter(Filter x) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public IValue visitRationalLiteralLexical(Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitRationalLiteralAmbiguity(Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitBasicTypeRational(org.rascalmpl.ast.BasicType.Rational x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitExpressionAppendAfter(AppendAfter x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitExpressionSplice(Splice x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitExpressionRemainder(Remainder x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitExpressionInsertBefore(InsertBefore x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitExpressionSplicePlus(SplicePlus x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitAssignmentAppend(org.rascalmpl.ast.Assignment.Append x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitRestLexical(org.rascalmpl.ast.Rest.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitRestAmbiguity(org.rascalmpl.ast.Rest.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitBasicTypeType(org.rascalmpl.ast.BasicType.Type x) {
		return KW("type");
	}

	@Override
	public IValue visitEvalCommandImport(Import x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitEvalCommandStatement(
			org.rascalmpl.ast.EvalCommand.Statement x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitEvalCommandDeclaration(
			org.rascalmpl.ast.EvalCommand.Declaration x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IValue visitEvalCommandAmbiguity(
			org.rascalmpl.ast.EvalCommand.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

}
