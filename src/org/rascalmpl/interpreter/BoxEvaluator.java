package org.rascalmpl.interpreter;

import java.util.Iterator;
import java.util.Stack;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Case;
import org.rascalmpl.ast.Catch;
import org.rascalmpl.ast.Declarator;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Formal;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.FunctionModifiers;
import org.rascalmpl.ast.LocalVariableDeclaration;
import org.rascalmpl.ast.MidStringChars;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Strategy;
import org.rascalmpl.ast.Variant;
import org.rascalmpl.ast.Alternative.Ambiguity;
import org.rascalmpl.ast.Alternative.NamedType;
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
import org.rascalmpl.ast.Asterisk.Lexical;
import org.rascalmpl.ast.BasicType.Bag;
import org.rascalmpl.ast.BasicType.Bool;
import org.rascalmpl.ast.BasicType.DateTime;
import org.rascalmpl.ast.BasicType.Int;
import org.rascalmpl.ast.BasicType.Lex;
import org.rascalmpl.ast.BasicType.List;
import org.rascalmpl.ast.BasicType.Loc;
import org.rascalmpl.ast.BasicType.Map;
import org.rascalmpl.ast.BasicType.Node;
import org.rascalmpl.ast.BasicType.Num;
import org.rascalmpl.ast.BasicType.Real;
import org.rascalmpl.ast.BasicType.ReifiedAdt;
import org.rascalmpl.ast.BasicType.ReifiedConstructor;
import org.rascalmpl.ast.BasicType.ReifiedFunction;
import org.rascalmpl.ast.BasicType.ReifiedNonTerminal;
import org.rascalmpl.ast.BasicType.ReifiedReifiedType;
import org.rascalmpl.ast.BasicType.ReifiedType;
import org.rascalmpl.ast.BasicType.Relation;
import org.rascalmpl.ast.BasicType.Set;
import org.rascalmpl.ast.BasicType.Value;
import org.rascalmpl.ast.BasicType.Void;
import org.rascalmpl.ast.Body.Anything;
import org.rascalmpl.ast.Body.Toplevels;
import org.rascalmpl.ast.Bound.Empty;
import org.rascalmpl.ast.Case.PatternWithAction;
import org.rascalmpl.ast.Catch.Binding;
import org.rascalmpl.ast.CharClass.Bracket;
import org.rascalmpl.ast.CharClass.Complement;
import org.rascalmpl.ast.CharClass.Difference;
import org.rascalmpl.ast.CharClass.SimpleCharclass;
import org.rascalmpl.ast.CharClass.Union;
import org.rascalmpl.ast.CharRange.Character;
import org.rascalmpl.ast.CharRange.Range;
import org.rascalmpl.ast.CharRanges.Concatenate;
import org.rascalmpl.ast.Character.Bottom;
import org.rascalmpl.ast.Character.EOF;
import org.rascalmpl.ast.Character.Numeric;
import org.rascalmpl.ast.Character.Short;
import org.rascalmpl.ast.Character.Top;
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
import org.rascalmpl.ast.Declaration.Rule;
import org.rascalmpl.ast.Declaration.Tag;
import org.rascalmpl.ast.Declaration.Test;
import org.rascalmpl.ast.Declaration.View;
import org.rascalmpl.ast.Expression.All;
import org.rascalmpl.ast.Expression.And;
import org.rascalmpl.ast.Expression.Anti;
import org.rascalmpl.ast.Expression.Any;
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
import org.rascalmpl.ast.Expression.Guarded;
import org.rascalmpl.ast.Expression.IfDefinedOtherwise;
import org.rascalmpl.ast.Expression.IfThenElse;
import org.rascalmpl.ast.Expression.Implication;
import org.rascalmpl.ast.Expression.In;
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
import org.rascalmpl.ast.Expression.SetAnnotation;
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
import org.rascalmpl.ast.Formal.TypeName;
import org.rascalmpl.ast.FunctionDeclaration.Abstract;
import org.rascalmpl.ast.FunctionModifier.Java;
import org.rascalmpl.ast.FunctionType.TypeArguments;
import org.rascalmpl.ast.Header.Parameters;
import org.rascalmpl.ast.Import.Extend;
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
import org.rascalmpl.ast.Literal.RegExp;
import org.rascalmpl.ast.LocalVariableDeclaration.Dynamic;
import org.rascalmpl.ast.LongLiteral.DecimalLongLiteral;
import org.rascalmpl.ast.LongLiteral.HexLongLiteral;
import org.rascalmpl.ast.LongLiteral.OctalLongLiteral;
import org.rascalmpl.ast.OptCharRanges.Absent;
import org.rascalmpl.ast.OptCharRanges.Present;
import org.rascalmpl.ast.Parameters.VarArgs;
import org.rascalmpl.ast.PathPart.Interpolated;
import org.rascalmpl.ast.PathPart.NonInterpolated;
import org.rascalmpl.ast.PathTail.Mid;
import org.rascalmpl.ast.PathTail.Post;
import org.rascalmpl.ast.PatternWithAction.Arbitrary;
import org.rascalmpl.ast.PatternWithAction.Replacing;
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
import org.rascalmpl.ast.StrChar.newline;
import org.rascalmpl.ast.Strategy.BottomUp;
import org.rascalmpl.ast.Strategy.BottomUpBreak;
import org.rascalmpl.ast.Strategy.Innermost;
import org.rascalmpl.ast.Strategy.Outermost;
import org.rascalmpl.ast.Strategy.TopDown;
import org.rascalmpl.ast.Strategy.TopDownBreak;
import org.rascalmpl.ast.StringLiteral.Template;
import org.rascalmpl.ast.StringTail.MidInterpolated;
import org.rascalmpl.ast.StringTail.MidTemplate;
import org.rascalmpl.ast.Symbol.Alternative;
import org.rascalmpl.ast.Symbol.CaseInsensitiveLiteral;
import org.rascalmpl.ast.Symbol.CharacterClass;
import org.rascalmpl.ast.Symbol.Iter;
import org.rascalmpl.ast.Symbol.IterSep;
import org.rascalmpl.ast.Symbol.IterStar;
import org.rascalmpl.ast.Symbol.IterStarSep;
import org.rascalmpl.ast.Symbol.Optional;
import org.rascalmpl.ast.Symbol.Sequence;
import org.rascalmpl.ast.Symbol.Sort;
import org.rascalmpl.ast.Test.Unlabeled;
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
import org.rascalmpl.values.uptr.TreeAdapter;

public class BoxEvaluator<IAbstractDataType> implements IEvaluator<IValue> {
	private AbstractAST currentAST;

	final private int UNITLENGTH = 40;

	final org.eclipse.imp.pdb.facts.type.Type typeL = BoxADT.EMPTY
			.getConstructorType();

	public TypeStore getTypeStore() {
		return BoxADT.getTypeStore();
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

	public IValue evalRascalModule(Module module) {
		return eX(module);
	}

	protected String getModuleName(Module module) {
		String name = module.getHeader().getName().toString();
		if (name.startsWith("\\")) {
			name = name.substring(1);
		}
		return name;
	}

	public IValue visitAlternativeAmbiguity(Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitAlternativeNamedType(NamedType x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitAssignableAmbiguity(
			org.rascalmpl.ast.Assignable.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitAssignableAnnotation(Annotation x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getReceiver()), BoxADT.AT, eX(x.getAnnotation()));
	}

	public IValue visitAssignableConstructor(Constructor x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getName()), BoxADT.LPAR, eXs(x.getArguments()),
				BoxADT.RPAR);
	}

	public IValue visitAssignableFieldAccess(FieldAccess x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getReceiver()), BoxADT.DOT, eX(x.getField()));
	}

	public IValue visitAssignableIfDefinedOrDefault(IfDefinedOrDefault x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getReceiver()), BoxADT.QUESTIONMARK, eX(x
				.getDefaultExpression()));
	}

	public IValue visitAssignableSubscript(Subscript x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getReceiver()), BoxADT.LBRACK, eX(x.getSubscript()),
				BoxADT.RBRACK);
	}

	public IValue visitAssignableTuple(Tuple x) {
		// TODO Auto-generated method stub
		return H(0, BoxADT.LT, eXs(x.getElements()), BoxADT.GT);
	}

	public IValue visitAssignableVariable(
			org.rascalmpl.ast.Assignable.Variable x) {
		// TODO Auto-generated method stub
		return VAR(H(eX(x.getQualifiedName())));
	}

	public IValue visitAssignmentAddition(Addition x) {
		return L("+=");
	}

	public IValue visitAssignmentAmbiguity(
			org.rascalmpl.ast.Assignment.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitAssignmentDefault(Default x) {
		// TODO Auto-generated method stub
		return L("=");
	}

	public IValue visitAssignmentDivision(Division x) {
		// TODO Auto-generated method stub
		return L("/=");
	}

	public IValue visitAssignmentIfDefined(IfDefined x) {
		// TODO Auto-generated method stub
		return L("?=");
	}

	public IValue visitAssignmentIntersection(Intersection x) {
		// TODO Auto-generated method stub
		return L("&=");
	}

	public IValue visitAssignmentProduct(Product x) {
		// TODO Auto-generated method stub
		return L("*=");
	}

	public IValue visitAssignmentSubtraction(Subtraction x) {
		// TODO Auto-generated method stub
		return L("-=");
	}

	public IValue visitAsteriskAmbiguity(org.rascalmpl.ast.Asterisk.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitAsteriskLexical(Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBackslashAmbiguity(
			org.rascalmpl.ast.Backslash.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBackslashLexical(org.rascalmpl.ast.Backslash.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBasicTypeAmbiguity(
			org.rascalmpl.ast.BasicType.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBasicTypeBag(Bag x) {
		// TODO Auto-generated method stub
		return KW("bag");
	}

	public IValue visitBasicTypeBool(Bool x) {
		// TODO Auto-generated method stub
		return KW("bool");
	}

	public IValue visitBasicTypeDateTime(DateTime x) {
		// TODO Auto-generated method stub
		return KW("datetime");
	}

	public IValue visitBasicTypeInt(Int x) {
		// TODO Auto-generated method stub
		return KW("int");
	}

	public IValue visitBasicTypeLex(Lex x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBasicTypeList(List x) {
		// TODO Auto-generated method stub
		return KW("list");
	}

	public IValue visitBasicTypeLoc(Loc x) {
		// TODO Auto-generated method stub
		return KW("loc");
	}

	public IValue visitBasicTypeMap(Map x) {
		// TODO Auto-generated method stub
		return KW("map");
	}

	public IValue visitBasicTypeNode(Node x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBasicTypeReal(Real x) {
		// TODO Auto-generated method stub
		return KW("real");
	}

	public IValue visitBasicTypeReifiedAdt(ReifiedAdt x) {
		// TODO Auto-generated method stub
		return KW("adt");
	}

	public IValue visitBasicTypeReifiedConstructor(ReifiedConstructor x) {
		// TODO Auto-generated method stub
		return KW("constructor");
	}

	public IValue visitBasicTypeReifiedFunction(ReifiedFunction x) {
		// TODO Auto-generated method stub
		return KW("fun");
	}

	public IValue visitBasicTypeReifiedNonTerminal(ReifiedNonTerminal x) {
		// TODO Auto-generated method stub
		return KW("non-terminal");
	}

	public IValue visitBasicTypeReifiedReifiedType(ReifiedReifiedType x) {
		// TODO Auto-generated method stub
		return KW("reified");
	}

	public IValue visitBasicTypeReifiedType(ReifiedType x) {
		// TODO Auto-generated method stub
		return KW("type");
	}

	public IValue visitBasicTypeRelation(Relation x) {
		// TODO Auto-generated method stub
		return KW("rel");
	}

	public IValue visitBasicTypeSet(Set x) {
		// TODO Auto-generated method stub
		return KW("set");
	}

	public IValue visitBasicTypeString(org.rascalmpl.ast.BasicType.String x) {
		// TODO Auto-generated method stub
		return KW("str");
	}

	public IValue visitBasicTypeTuple(org.rascalmpl.ast.BasicType.Tuple x) {
		// TODO Auto-generated method stub
		return KW("tuple");
	}

	public IValue visitBasicTypeValue(Value x) {
		// TODO Auto-generated method stub
		return KW("value");
	}

	public IValue visitBasicTypeVoid(Void x) {
		// TODO Auto-generated method stub
		return KW("void");
	}

	public IValue visitBodyAmbiguity(org.rascalmpl.ast.Body.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBodyAnything(Anything x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBodyToplevels(Toplevels x) {
		// TODO Auto-generated method stub
		IList listToplevels = getTreeList(x, 0);
		// for (IValue q : z) {
		// for (IValue b : listToplevels) {
		// if (TreeAdapter.isCfOptLayout((IConstructor) b)) {
		// IList c = TreeAdapter.searchCategory((IConstructor) b,
		// "Comment");
		// if (!c.isEmpty()) {
		// System.err.println("QQ0:"+c);
		// for (IValue d:c)
		// System.err.println(TreeAdapter.yield((IConstructor) d));
		// }
		// }
		// }
		// }
		return V(eXs0(x.getToplevels(), listToplevels));
	}

	public IValue visitBooleanLiteralAmbiguity(
			org.rascalmpl.ast.BooleanLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBooleanLiteralLexical(
			org.rascalmpl.ast.BooleanLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return NM(x.getString());
	}

	public IValue visitBoundAmbiguity(org.rascalmpl.ast.Bound.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitBoundDefault(org.rascalmpl.ast.Bound.Default x) {
		// TODO Auto-generated method stub
		return list(BoxADT.SEMICOLON, eX(x.getExpression()));
	}

	public IValue visitBoundEmpty(Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCaseAmbiguity(org.rascalmpl.ast.Case.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCaseDefault(org.rascalmpl.ast.Case.Default x) {
		// TODO Auto-generated method stub
		return I(H(1, H(0, KW("default"), BoxADT.COLON), eX(x.getStatement())));
	}

	public IValue visitCasePatternWithAction(PatternWithAction x) {
		// TODO Auto-generated method stub
		return I(H(1, KW("case"), eX(x.getPatternWithAction())));
	}

	public IValue visitCatchAmbiguity(org.rascalmpl.ast.Catch.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCatchBinding(Binding x) {
		/** "catch" pattern:Expression ":" body:Statement -> Catch {cons("Binding")} */
		IValue pattern = eX(x.getPattern());
		org.rascalmpl.ast.Statement body = x.getBody();
		return H(1, KW("catch"), H(0, Block(body, pattern, BoxADT.COLON)));
	}

	public IValue visitCatchDefault(org.rascalmpl.ast.Catch.Default x) {
		/** "catch" ":" body:Statement -> Catch {cons("Default")} */
		// TODO Auto-generated method stub
		org.rascalmpl.ast.Statement body = x.getBody();
		return Block(body, KW("catch"), BoxADT.COLON);
	}

	public IValue visitCharClassAmbiguity(
			org.rascalmpl.ast.CharClass.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCharClassBracket(Bracket x) {
		// TODO Auto-generated method stub
		return x.getCharClass().accept(this);
	}

	public IValue visitCharClassComplement(Complement x) {
		// TODO Auto-generated method stub
		return H(BoxADT.CONGR, x.getCharClass().accept(this));
	}

	public IValue visitCharClassDifference(Difference x) {
		// TODO Auto-generated method stub
		return H(x.getLhs().accept(this), BoxADT.DIVIDE, x.getRhs()
				.accept(this));
	}

	public IValue visitCharClassIntersection(
			org.rascalmpl.ast.CharClass.Intersection x) {
		// TODO Auto-generated method stub

		return H(x.getLhs().accept(this), L("/\\"), x.getRhs().accept(this));
	}

	public IValue visitCharClassSimpleCharclass(SimpleCharclass x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCharClassUnion(Union x) {
		// TODO Auto-generated method stub
		return H(x.getLhs().accept(this), L("\\/"), x.getRhs().accept(this));
	}

	public IValue visitCharRangeAmbiguity(
			org.rascalmpl.ast.CharRange.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCharRangeCharacter(Character x) {
		// TODO Auto-generated method stub
		return x.accept(this);
	}

	public IValue visitCharRangeRange(Range x) {
		// TODO Auto-generated method stub
		return H(0, x.getStart().accept(this), BoxADT.MINUS, x.getEnd().accept(
				this));
	}

	public IValue visitCharRangesAmbiguity(
			org.rascalmpl.ast.CharRanges.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCharRangesBracket(org.rascalmpl.ast.CharRanges.Bracket x) {
		// TODO Auto-generated method stub
		return H(BoxADT.LPAR, x.getRanges().accept(this), BoxADT.RPAR);
	}

	public IValue visitCharRangesConcatenate(Concatenate x) {
		// TODO Auto-generated method stub
		return H(x.getLhs().accept(this), x.getRhs().accept(this));
	}

	public IValue visitCharRangesRange(org.rascalmpl.ast.CharRanges.Range x) {
		// TODO Auto-generated method stub
		return x.accept(this);
	}

	public IValue visitCharacterAmbiguity(
			org.rascalmpl.ast.Character.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCharacterBottom(Bottom x) {
		// TODO Auto-generated method stub
		return L("\\BOT");
	}

	public IValue visitCharacterEOF(EOF x) {
		// TODO Auto-generated method stub
		return L("\\EOF");
	}

	public IValue visitCharacterLiteralAmbiguity(
			org.rascalmpl.ast.CharacterLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCharacterLiteralLexical(
			org.rascalmpl.ast.CharacterLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitCharacterNumeric(Numeric x) {
		// TODO Auto-generated method stub
		return x.getNumChar().accept(this);
	}

	public IValue visitCharacterShort(Short x) {
		// TODO Auto-generated method stub
		return x.getShortChar().accept(this);
	}

	public IValue visitCharacterTop(Top x) {
		// TODO Auto-generated method stub
		return L("\\TOP");
	}

	public IValue visitCommandAmbiguity(org.rascalmpl.ast.Command.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommandDeclaration(Declaration x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommandExpression(org.rascalmpl.ast.Command.Expression x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommandImport(org.rascalmpl.ast.Command.Import x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommandLexical(org.rascalmpl.ast.Command.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommandShell(Shell x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommandStatement(Statement x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommentAmbiguity(org.rascalmpl.ast.Comment.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommentCharAmbiguity(
			org.rascalmpl.ast.CommentChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommentCharLexical(
			org.rascalmpl.ast.CommentChar.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitCommentLexical(org.rascalmpl.ast.Comment.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitComprehensionAmbiguity(
			org.rascalmpl.ast.Comprehension.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitComprehensionList(org.rascalmpl.ast.Comprehension.List x) {
		return Comprehension(x, BoxADT.LBRACK, BoxADT.RBRACK);
	}

	public IValue visitComprehensionMap(org.rascalmpl.ast.Comprehension.Map x) {
		// TODO Auto-generated method stub
		return ComprehensionMap(x, BoxADT.LPAR, BoxADT.RPAR);
	}

	public IValue visitComprehensionSet(org.rascalmpl.ast.Comprehension.Set x) {
		// TODO Auto-generated method stub
		return Comprehension(x, BoxADT.LBLOCK, BoxADT.RBLOCK);
	}

	public IValue visitDataTargetAmbiguity(
			org.rascalmpl.ast.DataTarget.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDataTargetEmpty(org.rascalmpl.ast.DataTarget.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDataTargetLabeled(Labeled x) {
		// TODO Auto-generated method stub
		return eX(x.getLabel());
	}

	public IValue visitDataTypeSelectorAmbiguity(
			org.rascalmpl.ast.DataTypeSelector.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDataTypeSelectorSelector(Selector x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDateAndTimeAmbiguity(
			org.rascalmpl.ast.DateAndTime.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDateAndTimeLexical(
			org.rascalmpl.ast.DateAndTime.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDatePartAmbiguity(org.rascalmpl.ast.DatePart.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDatePartLexical(org.rascalmpl.ast.DatePart.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralAmbiguity(
			org.rascalmpl.ast.DateTimeLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralDateAndTimeLiteral(DateAndTimeLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralDateLiteral(DateLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDateTimeLiteralTimeLiteral(TimeLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDecimalIntegerLiteralAmbiguity(
			org.rascalmpl.ast.DecimalIntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDecimalIntegerLiteralLexical(
			org.rascalmpl.ast.DecimalIntegerLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return NM(x.getString());
	}

	public IValue visitDecimalLongLiteralAmbiguity(
			org.rascalmpl.ast.DecimalLongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDecimalLongLiteralLexical(
			org.rascalmpl.ast.DecimalLongLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return NM(x.getString());
	}

	public IValue visitDeclarationAlias(Alias x) {
		// TODO Auto-generated method stub
		return H(KW("alias"), eX(x.getUser()), BoxADT.ASSIGN, eX(x.getBase()),
				BoxADT.semicolumn());
	}

	public IValue visitDeclarationAmbiguity(
			org.rascalmpl.ast.Declaration.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDeclarationAnnotation(
			org.rascalmpl.ast.Declaration.Annotation x) {
		IValue t1 = x.getAnnoType().accept(this), t2 = x.getOnType().accept(
				this);
		if (t1 == null || t2 == null)
			return L(x.getClass().toString());
		return H(KW("anno"), t1, t2, H(L("@"), L(x.getName().toString())));
		// TODO Auto-generated method stub
	}

	public IValue visitDeclarationData(Data x) {
		// TODO Auto-generated method stub
		IValue r = H(KW("data"), L(x.getUser().toString()));
		java.util.List<Variant> vs = x.getVariants();
		IList b = BoxADT.getEmptyList();
		for (Variant v : vs) {
			IValue t = v.accept(this);
			if (t == null)
				return L(x.getClass().toString());
			b = b.append(I(H(0, (b.isEmpty() ? L("=") : L("|")), t)));
		}
		return V((b.insert(r)).append(I(BoxADT.semicolumn())));
	}

	public IValue visitDeclarationFunction(Function x) {
		// TODO Auto-generated method stub
		return eX(x.getFunctionDeclaration());
	}

	public IValue visitDeclarationRule(Rule x) {
		// TODO Auto-generated method stub
		/** tags:Tags "rule" name:Name patternAction:PatternWithAction ";" -> Declaration {cons("Rule")} */
		return H(1,KW("rule"), eX(x.getName()), H(0, eX(x.getPatternAction()), BoxADT.SEMICOLON));
	}

	public IValue visitDeclarationTag(Tag x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDeclarationTest(Test x) {
		// TODO Auto-generated method stub
		return H(1, KW("test"), H(0, eX(x.getTest()), BoxADT.SEMICOLON));
	}

	public IValue visitDeclarationVariable(
			org.rascalmpl.ast.Declaration.Variable x) {
		// TODO Auto-generated method stub
		return HV(eX(x.getVisibility()), eX(x.getType()), I(eXs(x
				.getVariables())), BoxADT.semicolumn());
	}

	public IValue visitDeclarationView(View x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDeclaratorAmbiguity(
			org.rascalmpl.ast.Declarator.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitDeclaratorDefault(org.rascalmpl.ast.Declarator.Default x) {
		// TODO Auto-generated method stub
		return H(1, eX(x.getType()), H(eXs(x.getVariables())));
	}

	public IValue visitEscapeSequenceAmbiguity(
			org.rascalmpl.ast.EscapeSequence.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitEscapeSequenceLexical(
			org.rascalmpl.ast.EscapeSequence.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitEscapedNameAmbiguity(
			org.rascalmpl.ast.EscapedName.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitEscapedNameLexical(
			org.rascalmpl.ast.EscapedName.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionAddition(
			org.rascalmpl.ast.Expression.Addition x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.PLUS, eX(x.getRhs()));
	}

	public IValue visitExpressionAll(All x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionAmbiguity(
			org.rascalmpl.ast.Expression.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionAnd(And x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.AND, eX(x.getRhs()));
	}

	public IValue visitExpressionAnti(Anti x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionAny(Any x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionBracket(org.rascalmpl.ast.Expression.Bracket x) {
		// TODO Auto-generated method stub
		return list(BoxADT.LPAR, eX(x.getExpression()), BoxADT.RPAR);
	}

	public IValue visitExpressionCallOrTree(CallOrTree x) {
		// TODO Auto-generated method stub
		return HV(list(eX(x.getExpression()), BoxADT.LPAR, eXs(x.getArguments()),
				BoxADT.RPAR));
	}

	public IValue visitExpressionClosure(Closure x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionComposition(Composition x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), KW(" o "), eX(x.getRhs()));
	}

	public IValue visitExpressionComprehension(Comprehension x) {
		// TODO Auto-generated method stub
		return eX(x.getComprehension());
	}

	public IValue visitExpressionDescendant(Descendant x) {
		// TODO Auto-generated method stub
		return list(BoxADT.DIVIDE, eX(x.getPattern()));
	}

	public IValue visitExpressionDivision(
			org.rascalmpl.ast.Expression.Division x) {
		return list(eX(x.getLhs()), BoxADT.DIVIDE, eX(x.getRhs()));
	}

	public IValue visitExpressionEnumerator(Enumerator x) {
		// TODO Auto-generated method stub
		return list(eX(x.getPattern()), BoxADT.ELOF, eX(x.getExpression()));
	}

	public IValue visitExpressionEquals(Equals x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.EQUALS, eX(x.getRhs()));
	}

	public IValue visitExpressionEquivalence(Equivalence x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.EQUIVALENCE, eX(x.getRhs()));
	}

	public IValue visitExpressionFieldAccess(
			org.rascalmpl.ast.Expression.FieldAccess x) {
		// TODO Auto-generated method stub
		return list(eX(x.getExpression()), BoxADT.DOT, eX(x.getField()));
	}

	public IValue visitExpressionFieldProject(FieldProject x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionFieldUpdate(FieldUpdate x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionGetAnnotation(GetAnnotation x) {
		// TODO Auto-generated method stub
		return list(eX(x.getExpression()), BoxADT.AT, eX(x.getName()));
	}

	public IValue visitExpressionGreaterThan(GreaterThan x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.GT, eX(x.getRhs()));
	}

	public IValue visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.GE, eX(x.getRhs()));
	}

	public IValue visitExpressionGuarded(Guarded x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.QUESTIONMARK, eX(x.getRhs()));
	}

	public IValue visitExpressionIfThenElse(IfThenElse x) {
		// TODO Auto-generated method stub
		return list(eX(x.getCondition()), BoxADT.QUESTIONMARK, eX(x
				.getThenExp()), BoxADT.COLON, eX(x.getElseExp()));
	}

	public IValue visitExpressionImplication(Implication x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionIn(In x) {
		return list(eX(x.getLhs()), KW(" in "), eX(x.getRhs()));
	}

	public IValue visitExpressionIntersection(
			org.rascalmpl.ast.Expression.Intersection x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.INTERSECTION, eX(x.getRhs()));
	}

	public IValue visitExpressionIsDefined(IsDefined x) {
		// TODO Auto-generated method stub
		IList a = (IList) x.getArgument().accept(this);
		if (a != null)
			return a.append(BoxADT.QUESTIONMARK);
		return BoxADT.getList(BoxADT.QUESTIONMARK);
	}

	public IValue visitExpressionJoin(Join x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), KW(" join "), eX(x.getRhs()));
	}

	public IValue visitExpressionLessThan(LessThan x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.LT, eX(x.getRhs()));
	}

	public IValue visitExpressionLessThanOrEq(LessThanOrEq x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.LE, eX(x.getRhs()));
	}

	public IValue visitExpressionLexical(org.rascalmpl.ast.Expression.Lexical x) {
		// TODO Auto-generated method stu
		return list(L(x.getString()));
	}

	public IValue visitExpressionList(org.rascalmpl.ast.Expression.List x) {
		IList r = list(BoxADT.LBRACK, eXs(x.getElements()), BoxADT.RBRACK);
		return lengths(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionLiteral(Literal x) {
		// TODO Auto-generated method stub
		// System.err.println("visitExpressionLiteral:"+x.getLiteral().getClass());
		return list(eX(x.getLiteral()));
	}

	public IValue visitExpressionMap(org.rascalmpl.ast.Expression.Map x) {
		IList r = list(BoxADT.LPAR, eXs(x.getMappings()), BoxADT.RPAR);
		return lengths(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionMatch(Match x) {
		/** pattern:Expression ":=" expression:Expression -> Expression **/
		// TODO Auto-generated method stub
		return list(eX(x.getPattern()), BoxADT.ASSIGN, eX(x.getExpression()));
	}

	public IValue visitExpressionModulo(Modulo x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.MODULO, eX(x.getRhs()));
	}

	public IValue visitExpressionMultiVariable(MultiVariable x) {
		// TODO Auto-generated method stub
		return list(VAR(eX(x.getName())));
	}

	public IValue visitExpressionNegation(Negation x) {
		// TODO Auto-generated method stub
		return list(BoxADT.NEGATION, eX(x.getArgument()));
	}

	public IValue visitExpressionNegative(Negative x) {
		// TODO Auto-generated method stub
		return list(BoxADT.MINUS, eX(x.getArgument()));
	}

	public IValue visitExpressionNoMatch(NoMatch x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionNonEquals(NonEquals x) {
		return list(eX(x.getLhs()), BoxADT.NOTEQUALS, eX(x.getRhs()));
	}

	public IValue visitExpressionNotIn(NotIn x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), KW(" notin "), eX(x.getRhs()));
	}

	public IValue visitExpressionOr(Or x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.OR, eX(x.getRhs()));
	}

	public IValue visitExpressionProduct(org.rascalmpl.ast.Expression.Product x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.MULT, eX(x.getRhs()));
	}

	public IValue visitExpressionQualifiedName(QualifiedName x) {
		// TODO Auto-generated method stub
		return list(VAR(H(eX(x.getQualifiedName()))));
	}

	public IValue visitExpressionRange(org.rascalmpl.ast.Expression.Range x) {
		// TODO Auto-generated method stub
		return BoxADT.getList(eX(x.getFirst()), BoxADT.RANGE, eX(x.getLast()));
	}

	public IValue visitExpressionReifiedType(
			org.rascalmpl.ast.Expression.ReifiedType x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionReifyType(ReifyType x) {
		// TODO Auto-generated method stub
		return list(BoxADT.HASH, eX(x.getType()));
	}

	public IValue visitExpressionSet(org.rascalmpl.ast.Expression.Set x) {
		// TODO Auto-generated method stub
		IList r = list(BoxADT.LBLOCK, eXs(x.getElements()), BoxADT.RBLOCK);
		return lengths(r) < UNITLENGTH ? list(H(r)) : r;
	}

	public IValue visitExpressionSetAnnotation(SetAnnotation x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionStepRange(StepRange x) {
		// TODO Auto-generated method stub
		return list(eX(x.getFirst()), BoxADT.COMMA, eX(x.getSecond()),
				BoxADT.RANGE, eX(x.getLast()));

	}

	public IValue visitExpressionSubscript(
			org.rascalmpl.ast.Expression.Subscript x) {
		return list(eX(x.getExpression()), BoxADT.LBRACK,
				eXs(x.getSubscripts()), BoxADT.RBRACK);
	}

	public IValue visitExpressionSubtraction(
			org.rascalmpl.ast.Expression.Subtraction x) {
		// TODO Auto-generated method stub
		return list(eX(x.getLhs()), BoxADT.MINUS, eX(x.getRhs()));
	}

	public IValue visitExpressionTransitiveClosure(TransitiveClosure x) {
		// TODO Auto-generated method stub
		return list(eX(x.getArgument()), BoxADT.PLUS, BoxADT.SPACE);
	}

	public IValue visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		// TODO Auto-generated method stub
		return list(eX(x.getArgument()), BoxADT.MULT, BoxADT.SPACE);
	}

	public IValue visitExpressionTuple(org.rascalmpl.ast.Expression.Tuple x) {
		// TODO Auto-generated method stub
		IList r = list(BoxADT.LT, eXs(x.getElements()), BoxADT.GT);
		return lengths(r) < UNITLENGTH ? list(H(0, r)) : r;
	}

	public IValue visitExpressionTypedVariable(TypedVariable x) {
		// TODO Auto-generated method stub
		return list(H(1, eX(x.getType()), VAR(eX(x.getName()))));
	}

	public IValue visitExpressionTypedVariableBecomes(TypedVariableBecomes x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionVariableBecomes(VariableBecomes x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionVisit(Visit x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionVoidClosure(VoidClosure x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFieldAmbiguity(org.rascalmpl.ast.Field.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFieldIndex(Index x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFieldName(Name x) {
		// TODO Auto-generated method stub
		return x.accept(this);
	}

	public IValue visitFormalAmbiguity(org.rascalmpl.ast.Formal.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFormalTypeName(TypeName x) {
		// TODO Auto-generated method stub
		IValue r = x.getType().accept(this);
		if (r == null)
			return L(x.getClass().toString());
		return H(1, r, L(x.getName().toString()));
	}

	public IValue visitFormalsAmbiguity(org.rascalmpl.ast.Formals.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFormalsDefault(org.rascalmpl.ast.Formals.Default x) {
		// TODO Auto-generated method stub
		java.util.List<org.rascalmpl.ast.Formal> formals = x.getFormals();
		IList b = BoxADT.getEmptyList();
		for (Formal f : formals) {
			if (!b.isEmpty())
				b = b.append(BoxADT.comma());
			IValue r = f.accept(this);
			if (r != null)
				b = b.append(r);
		}
		return H(b);
	}

	public IValue visitFunctionBodyAmbiguity(
			org.rascalmpl.ast.FunctionBody.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFunctionBodyDefault(
			org.rascalmpl.ast.FunctionBody.Default x) {
		// TODO Auto-generated method stub
		java.util.List<org.rascalmpl.ast.Statement> statements = x
				.getStatements();
		IList b = BoxADT.getEmptyList();
		for (Iterator<org.rascalmpl.ast.Statement> iterator = statements
				.iterator(); iterator.hasNext();) {
			org.rascalmpl.ast.Statement statement = iterator.next();
			IValue t = statement.accept(this);
			if (t != null)
				b = b.append(I(t));
		}
		return b;
	}

	public IValue visitFunctionDeclarationAbstract(Abstract x) {
		/** tags:Tags visibility:Visibility signature:Signature ";" -> FunctionDeclaration {cons("Abstract")} */
		// TODO Auto-generated method stub	
		return H(1, eX(x.getVisibility()), H(0, eX(x.getSignature()), BoxADT.SEMICOLON));
	}

	public IValue visitFunctionDeclarationAmbiguity(
			org.rascalmpl.ast.FunctionDeclaration.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFunctionDeclarationDefault(
			org.rascalmpl.ast.FunctionDeclaration.Default x) {
		// TODO Auto-generated method stub
		IValue r = eX(x.getSignature());
		IList b;
		if (x.hasBody() && (b = (IList) x.getBody().accept(this)) != null) {
			b = b.append(I(BoxADT.RBLOCK));
			r = V(H(0, H(1, eX(x.getVisibility()), r), BoxADT.LBLOCK), V(b));
		}
		return r;
	}

	public IValue visitFunctionModifierAmbiguity(
			org.rascalmpl.ast.FunctionModifier.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFunctionModifierJava(Java x) {
		// TODO Auto-generated method stub
		return L("java");
	}

	public IValue visitFunctionModifiersAmbiguity(
			org.rascalmpl.ast.FunctionModifiers.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFunctionModifiersList(
			org.rascalmpl.ast.FunctionModifiers.List x) {
		// TODO Auto-generated method stub
		return H(eXs0(x.getModifiers()));
	}

	public IValue visitFunctionTypeAmbiguity(
			org.rascalmpl.ast.FunctionType.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitFunctionTypeTypeArguments(TypeArguments x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitHeaderAmbiguity(org.rascalmpl.ast.Header.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitHeaderDefault(org.rascalmpl.ast.Header.Default x) {
		IList l = getTreeList(x, 6);
		return V(getComment(x, 1), H(KW("module"), H(0, eX(x.getName()))),
				getComment(x, 5), eXs0(x.getImports(), l));
	}

	public IValue visitHeaderParameters(Parameters x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitHexIntegerLiteralAmbiguity(
			org.rascalmpl.ast.HexIntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitHexIntegerLiteralLexical(
			org.rascalmpl.ast.HexIntegerLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitHexLongLiteralAmbiguity(
			org.rascalmpl.ast.HexLongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitHexLongLiteralLexical(
			org.rascalmpl.ast.HexLongLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportAmbiguity(org.rascalmpl.ast.Import.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportDefault(org.rascalmpl.ast.Import.Default x) {
		// TODO Auto-generated method stub
		return H(KW("import"), L(x.getModule().getName().toString() + ";"));
	}

	public IValue visitImportExtend(Extend x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleActuals(Actuals x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleActualsRenaming(ActualsRenaming x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleAmbiguity(
			org.rascalmpl.ast.ImportedModule.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleDefault(
			org.rascalmpl.ast.ImportedModule.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitImportedModuleRenamings(Renamings x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitIntegerLiteralAmbiguity(
			org.rascalmpl.ast.IntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		// TODO Auto-generated method stub
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(x.toString()));
	}

	public IValue visitIntegerLiteralHexIntegerLiteral(HexIntegerLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitIntegerLiteralOctalIntegerLiteral(OctalIntegerLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitJustDateAmbiguity(org.rascalmpl.ast.JustDate.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitJustDateLexical(org.rascalmpl.ast.JustDate.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitJustTimeAmbiguity(org.rascalmpl.ast.JustTime.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitJustTimeLexical(org.rascalmpl.ast.JustTime.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindAlias(org.rascalmpl.ast.Kind.Alias x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindAll(org.rascalmpl.ast.Kind.All x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindAmbiguity(org.rascalmpl.ast.Kind.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindAnno(Anno x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindData(org.rascalmpl.ast.Kind.Data x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindFunction(org.rascalmpl.ast.Kind.Function x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindModule(org.rascalmpl.ast.Kind.Module x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindRule(org.rascalmpl.ast.Kind.Rule x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindTag(org.rascalmpl.ast.Kind.Tag x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindVariable(org.rascalmpl.ast.Kind.Variable x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitKindView(org.rascalmpl.ast.Kind.View x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLabelAmbiguity(org.rascalmpl.ast.Label.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLabelDefault(org.rascalmpl.ast.Label.Default x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getName()), BoxADT.COLON);
	}

	public IValue visitLabelEmpty(org.rascalmpl.ast.Label.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLiteralAmbiguity(org.rascalmpl.ast.Literal.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLiteralBoolean(Boolean x) {
		// TODO Auto-generated method stub
		return NM(x.getBooleanLiteral().toString());
	}

	public IValue visitLiteralDateTime(org.rascalmpl.ast.Literal.DateTime x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLiteralInteger(Integer x) {
		// TODO Auto-generated method stub
		return NM(x.getIntegerLiteral().getDecimal().toString());
	}

	public IValue visitLiteralLocation(Location x) {
		// TODO Auto-generated method stub
		return eX(x.getLocationLiteral());
	}

	public IValue visitLiteralReal(org.rascalmpl.ast.Literal.Real x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLiteralRegExp(RegExp x) {
		/** regExpLiteral:RegExpLiteral -> Literal {cons("RegExp")} */
		// TODO Auto-generated method stub
		return H(0, eX(x.getRegExpLiteral()));
	}

	public IValue visitLiteralString(org.rascalmpl.ast.Literal.String x) {
		// TODO Auto-generated method stub
		// System.err.println("VisitLiteral String:"+x.getStringLiteral().getClass());
		return H(0, eX(x.getStringLiteral()));
	}

	public IValue visitLocalVariableDeclarationAmbiguity(
			org.rascalmpl.ast.LocalVariableDeclaration.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLocalVariableDeclarationDefault(
			org.rascalmpl.ast.LocalVariableDeclaration.Default x) {
		// TODO Auto-generated method stub
		Declarator declarator = x.getDeclarator();
		return eX(declarator);
	}

	public IValue visitLocalVariableDeclarationDynamic(Dynamic x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLocationLiteralAmbiguity(
			org.rascalmpl.ast.LocationLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLocationLiteralDefault(
			org.rascalmpl.ast.LocationLiteral.Default x) {
		return list(BoxADT.VBAR, eX(x.getProtocolPart()), BoxADT.COLON, eX(x
				.getPathPart()), BoxADT.VBAR);
		// TODO Auto-generated method stub

	}

	public IValue visitLocationLiteralFile(
			org.rascalmpl.ast.LocationLiteral.File x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLongLiteralAmbiguity(
			org.rascalmpl.ast.LongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLongLiteralDecimalLongLiteral(DecimalLongLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLongLiteralHexLongLiteral(HexLongLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitLongLiteralOctalLongLiteral(OctalLongLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMappingAmbiguity(org.rascalmpl.ast.Mapping.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMappingDefault(org.rascalmpl.ast.Mapping.Default x) {
		// TODO Auto-generated method stub
		return H(0, eX(x.getFrom()), BoxADT.COLON, eX(x.getTo()));
	}

	public IValue visitMarkerAmbiguity(org.rascalmpl.ast.Marker.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMarkerLexical(org.rascalmpl.ast.Marker.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMidPathCharsAmbiguity(
			org.rascalmpl.ast.MidPathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMidPathCharsLexical(
			org.rascalmpl.ast.MidPathChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMidProtocolCharsAmbiguity(
			org.rascalmpl.ast.MidProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMidProtocolCharsLexical(
			org.rascalmpl.ast.MidProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMidStringCharsAmbiguity(
			org.rascalmpl.ast.MidStringChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitMidStringCharsLexical(
			org.rascalmpl.ast.MidStringChars.Lexical x) {
		// TODO Auto-generated method stub
		return split(x.getString());
	}

	public IValue visitModuleActualsAmbiguity(
			org.rascalmpl.ast.ModuleActuals.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitModuleActualsDefault(
			org.rascalmpl.ast.ModuleActuals.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitModuleAmbiguity(org.rascalmpl.ast.Module.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitModuleDefault(org.rascalmpl.ast.Module.Default x) {
		return V(eX(x.getHeader()), getComment(x, 1), eX(x.getBody()));
	}

	public IValue visitModuleParametersAmbiguity(
			org.rascalmpl.ast.ModuleParameters.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitModuleParametersDefault(
			org.rascalmpl.ast.ModuleParameters.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNameAmbiguity(org.rascalmpl.ast.Name.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNameLexical(org.rascalmpl.ast.Name.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitNamedBackslashAmbiguity(
			org.rascalmpl.ast.NamedBackslash.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNamedBackslashLexical(
			org.rascalmpl.ast.NamedBackslash.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNamedRegExpAmbiguity(
			org.rascalmpl.ast.NamedRegExp.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNamedRegExpLexical(
			org.rascalmpl.ast.NamedRegExp.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNoElseMayFollowAmbiguity(
			org.rascalmpl.ast.NoElseMayFollow.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNoElseMayFollowDefault(
			org.rascalmpl.ast.NoElseMayFollow.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNumCharAmbiguity(org.rascalmpl.ast.NumChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitNumCharLexical(org.rascalmpl.ast.NumChar.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitOctalIntegerLiteralAmbiguity(
			org.rascalmpl.ast.OctalIntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitOctalIntegerLiteralLexical(
			org.rascalmpl.ast.OctalIntegerLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitOctalLongLiteralAmbiguity(
			org.rascalmpl.ast.OctalLongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitOctalLongLiteralLexical(
			org.rascalmpl.ast.OctalLongLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitOptCharRangesAbsent(Absent x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitOptCharRangesAmbiguity(
			org.rascalmpl.ast.OptCharRanges.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitOptCharRangesPresent(Present x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitParametersAmbiguity(
			org.rascalmpl.ast.Parameters.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitParametersDefault(org.rascalmpl.ast.Parameters.Default x) {
		// TODO Auto-generated method stub
		return eX(x.getFormals());
	}

	public IValue visitParametersVarArgs(VarArgs x) {
		// TODO Auto-generated method stub
		return eX(x.getFormals());
	}

	public IValue visitPathCharsAmbiguity(
			org.rascalmpl.ast.PathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPathCharsLexical(org.rascalmpl.ast.PathChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitPathPartAmbiguity(org.rascalmpl.ast.PathPart.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPathPartInterpolated(Interpolated x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPathPartNonInterpolated(NonInterpolated x) {
		// TODO Auto-generated method stub
		return eX(x.getPathChars());
	}

	public IValue visitPathTailAmbiguity(org.rascalmpl.ast.PathTail.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPathTailMid(Mid x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPathTailPost(Post x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPatternWithActionAmbiguity(
			org.rascalmpl.ast.PatternWithAction.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPatternWithActionArbitrary(Arbitrary x) {
		return HV(0, eX(x.getPattern()), BoxADT.COLON, Block(x.getStatement()));
	}

	public IValue visitPatternWithActionReplacing(Replacing x) {
		// TODO Auto-generated method stub
		return HV(0, eX(x.getPattern()), L("=>"), eX(x.getReplacement()));
	}

	public IValue visitPostPathCharsAmbiguity(
			org.rascalmpl.ast.PostPathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPostPathCharsLexical(
			org.rascalmpl.ast.PostPathChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPostProtocolCharsAmbiguity(
			org.rascalmpl.ast.PostProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPostProtocolCharsLexical(
			org.rascalmpl.ast.PostProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPostStringCharsAmbiguity(
			org.rascalmpl.ast.PostStringChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPostStringCharsLexical(
			org.rascalmpl.ast.PostStringChars.Lexical x) {
		// TODO Auto-generated method stub
		return split(x.getString());
	}

	public IValue visitPrePathCharsAmbiguity(
			org.rascalmpl.ast.PrePathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPrePathCharsLexical(
			org.rascalmpl.ast.PrePathChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPreProtocolCharsAmbiguity(
			org.rascalmpl.ast.PreProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPreProtocolCharsLexical(
			org.rascalmpl.ast.PreProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPreStringCharsAmbiguity(
			org.rascalmpl.ast.PreStringChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitPreStringCharsLexical(
			org.rascalmpl.ast.PreStringChars.Lexical x) {
		// TODO Auto-generated method stub
		return split(x.getString());
	}

	public IValue visitProtocolCharsAmbiguity(
			org.rascalmpl.ast.ProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProtocolCharsLexical(
			org.rascalmpl.ast.ProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitProtocolPartAmbiguity(
			org.rascalmpl.ast.ProtocolPart.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProtocolPartInterpolated(
			org.rascalmpl.ast.ProtocolPart.Interpolated x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProtocolPartNonInterpolated(
			org.rascalmpl.ast.ProtocolPart.NonInterpolated x) {
		// TODO Auto-generated method stub
		return eX(x.getProtocolChars());
	}

	public IValue visitProtocolTailAmbiguity(
			org.rascalmpl.ast.ProtocolTail.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProtocolTailMid(org.rascalmpl.ast.ProtocolTail.Mid x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitProtocolTailPost(org.rascalmpl.ast.ProtocolTail.Post x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitQualifiedNameAmbiguity(
			org.rascalmpl.ast.QualifiedName.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitQualifiedNameDefault(
			org.rascalmpl.ast.QualifiedName.Default x) {
		// TODO Auto-generated method stub
		return eXs1(x.getNames());
	}

	public IValue visitRealLiteralAmbiguity(
			org.rascalmpl.ast.RealLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRealLiteralLexical(
			org.rascalmpl.ast.RealLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRegExpAmbiguity(org.rascalmpl.ast.RegExp.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRegExpLexical(org.rascalmpl.ast.RegExp.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRegExpLiteralAmbiguity(
			org.rascalmpl.ast.RegExpLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRegExpLiteralLexical(
			org.rascalmpl.ast.RegExpLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getString());
	}

	public IValue visitRegExpModifierAmbiguity(
			org.rascalmpl.ast.RegExpModifier.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRegExpModifierLexical(
			org.rascalmpl.ast.RegExpModifier.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRenamingAmbiguity(org.rascalmpl.ast.Renaming.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRenamingDefault(org.rascalmpl.ast.Renaming.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRenamingsAmbiguity(
			org.rascalmpl.ast.Renamings.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRenamingsDefault(org.rascalmpl.ast.Renamings.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitReplacementAmbiguity(
			org.rascalmpl.ast.Replacement.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitReplacementConditional(Conditional x) {
		// TODO Auto-generated method stub
		return H(1, KW("when"), eXs(x.getConditions()));
	}

	public IValue visitReplacementUnconditional(Unconditional x) {
		// TODO Auto-generated method stub
		return eX(x.getReplacementExpression());
	}

	public IValue visitRestAmbiguity(org.rascalmpl.ast.Rest.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRestLexical(org.rascalmpl.ast.Rest.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandAmbiguity(
			org.rascalmpl.ast.ShellCommand.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandEdit(Edit x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandHelp(Help x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandHistory(History x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandListDeclarations(ListDeclarations x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandListModules(ListModules x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandQuit(Quit x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandSetOption(SetOption x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandTest(org.rascalmpl.ast.ShellCommand.Test x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandUndeclare(Undeclare x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShellCommandUnimport(Unimport x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShortCharAmbiguity(
			org.rascalmpl.ast.ShortChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitShortCharLexical(org.rascalmpl.ast.ShortChar.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSignatureAmbiguity(
			org.rascalmpl.ast.Signature.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSignatureNoThrows(NoThrows x) {
		/** type:Type modifiers:FunctionModifiers name:Name parameters:Parameters **/
		// TODO Auto-generated method stub
		IValue t = H(0, eX(x.getName()), BoxADT.LPAR, eX(x.getParameters()), 
				BoxADT.RPAR);
		return H(1, eX(x.getType()), eX(x.getModifiers()), t);
	}

	public IValue visitSignatureWithThrows(WithThrows x) {
		/** type:Type modifiers:FunctionModifiers name:Name parameters:Parameters 
        "throws" exceptions:{Type ","}+ -> Signature {cons("WithThrows")} */
		// TODO Auto-generated method stub
		IValue t = H(0, eX(x.getName()), BoxADT.LPAR, eX(x.getParameters()), 
				BoxADT.RPAR);
		return H(1, eX(x.getType()), eX(x.getModifiers()),  t, KW("throws"),
				eXs(x.getExceptions()));
	}

	public IValue visitSingleCharacterAmbiguity(
			org.rascalmpl.ast.SingleCharacter.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSingleCharacterLexical(
			org.rascalmpl.ast.SingleCharacter.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSingleQuotedStrCharAmbiguity(
			org.rascalmpl.ast.SingleQuotedStrChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSingleQuotedStrCharLexical(
			org.rascalmpl.ast.SingleQuotedStrChar.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSingleQuotedStrConAmbiguity(
			org.rascalmpl.ast.SingleQuotedStrCon.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSingleQuotedStrConLexical(
			org.rascalmpl.ast.SingleQuotedStrCon.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStatementAmbiguity(
			org.rascalmpl.ast.Statement.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStatementAppend(Append x) {
		// TODO Auto-generated method stub
		return H(1, KW("append"), eX(x.getDataTarget()), eX(x.getStatement()));
	}

	public IValue visitStatementAssert(Assert x) {
		// TODO Auto-generated method stub
		return H(1, KW("assert"), HV(0, eX(x.getExpression())));
	}

	public IValue visitStatementAssertWithMessage(AssertWithMessage x) {
		// TODO Auto-generated method stub
		return H(1, KW("assert"), HV(0, eX(x.getExpression())), BoxADT.COLON,
				eX(x.getMessage()));
	}

	public IValue visitStatementAssignment(Assignment x) {
		// TODO Auto-generated method stub
		return H(1, eX(x.getAssignable()), eX(x.getOperator()), eX(x
				.getStatement()));
	}

	public IValue visitStatementBreak(Break x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStatementContinue(Continue x) {
		// TODO Auto-generated method stub
		return H(0, H(1, KW("continue"), eX(x.getTarget())), BoxADT.SEMICOLON);

	}

	public IValue visitStatementDoWhile(DoWhile x) {
		// TODO Auto-generated method stub
		IValue b = Block(x.getBody(), KW("do"));
		return H(b, KW("while"), BoxADT.LPAR, eX(x.getCondition()), BoxADT.RPAR);
	}

	public IValue visitStatementEmptyStatement(EmptyStatement x) {
		// TODO Auto-generated method stub
		return BoxADT.semicolumn();
	}

	public IValue visitStatementExpression(
			org.rascalmpl.ast.Statement.Expression x) {
		// TODO Auto-generated method stub
		return HV(0, eX(x.getExpression()), BoxADT.SEMICOLON);
	}

	public IValue visitStatementFail(Fail x) {
		// TODO Auto-generated method stub
		return H(0, H(1, KW("fail"), eX(x.getTarget())), BoxADT.SEMICOLON);
	}

	public IValue visitStatementFor(For x) {
		// TODO Auto-generated method stub
		org.rascalmpl.ast.Statement body = x.getBody();
		java.util.List<Expression> generators = x.getGenerators();
		IValue generator = eXs(generators);
		return Block(body, KW("for"), BoxADT.LPAR, HV(generator), BoxADT.RPAR);
	}

	public IValue visitStatementFunctionDeclaration(FunctionDeclaration x) {
		// TODO Auto-generated method stub
		return HV(eX(x.getFunctionDeclaration()));
	}

	public IValue visitStatementGlobalDirective(GlobalDirective x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStatementIfThen(IfThen x) {
		// TODO Auto-generated method stub
		org.rascalmpl.ast.Statement s = x.getThenStatement();
		IValue r = Block(s, KW("if"), BoxADT.LPAR, HV(eXs(x.getConditions())),
				BoxADT.RPAR);
		return r;
	}

	public IValue visitStatementIfThenElse(
			org.rascalmpl.ast.Statement.IfThenElse x) {
		org.rascalmpl.ast.Statement s1 = x.getThenStatement();
		org.rascalmpl.ast.Statement s2 = x.getElseStatement();
		// TODO Auto-generated method stub
		IValue b1 = Block(s1, KW("if"), BoxADT.LPAR,
				HV(eXs(x.getConditions())), BoxADT.RPAR), b2 = Block(s2,
				KW("else"));
		return HV(b1, b2);
	}

	public IValue visitStatementInsert(Insert x) {
		// TODO Auto-generated method stub
		return H(1, KW("insert"), eX(x.getDataTarget()), eX(x.getStatement()));
	}

	public IValue visitStatementNonEmptyBlock(
			org.rascalmpl.ast.Statement.NonEmptyBlock x) {
		// TODO Auto-generated method stub
		java.util.List<org.rascalmpl.ast.Statement> statements = x
				.getStatements();
		IList r = BoxADT.getEmptyList();
		for (Iterator<org.rascalmpl.ast.Statement> iterator = statements
				.iterator(); iterator.hasNext();) {
			org.rascalmpl.ast.Statement statement = iterator.next();
			IValue t = eX(statement);
			r = r.append(I(t));
		}
		return r;
	}

	public IValue visitStatementReturn(Return x) {
		// TODO Auto-generated method stub
		return H(1, KW("return"), eX(x.getStatement()));
	}

	public IValue visitStatementSolve(Solve x) {
		// TODO Auto-generated method stub
		return H(0, KW("solve"), BoxADT.LPAR, eXs(x.getVariables()),
				BoxADT.RPAR, eX(x.getBound()), Block(x.getBody()));
	}

	public IValue visitStatementSwitch(Switch x) {
		// TODO Auto-generated method stub
		java.util.List<Case> cases = x.getCases();
		IList header = list(KW("switch"), BoxADT.LPAR, eX(x.getExpression()),
				BoxADT.RPAR, BoxADT.LBLOCK);
		IList r = list(H(0, header));
		for (Case c : cases) {
			r = r.append(eX(c));
		}
		r = r.append(I(BoxADT.RBLOCK));
		return V(r);
	}

	public IValue visitStatementThrow(Throw x) {
		// TODO Auto-generated method stub
		return Block(x.getStatement(), KW("throw"));
	}

	public IValue visitStatementTry(Try x) {
		// TODO Auto-generated method stub
		java.util.List<Catch> handlers = x.getHandlers();
		IList r = list();
		for (Catch handler : handlers) {
			r = r.append(eX(handler));
		}
		return V(Block(x.getBody(), KW("try")), r);
	}

	public IValue visitStatementTryFinally(TryFinally x) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		java.util.List<Catch> handlers = x.getHandlers();
		IList r = list();
		for (Catch handler : handlers) {
			r = r.append(eX(handler));
		}
		return V(Block(x.getBody(), KW("try")), r, Block(x.getFinallyBody(),
				KW("finally")));
	}

	public IValue visitStatementVariableDeclaration(VariableDeclaration x) {
		// TODO Auto-generated method stub
		LocalVariableDeclaration declaration = x.getDeclaration();
		return H(0, eX(declaration), L(";"));
	}

	public IValue visitStatementVisit(org.rascalmpl.ast.Statement.Visit x) {
		// TODO Auto-generated method stub
		return H(1, eX(x.getLabel()), eX(x.getVisit()));
	}

	public IValue visitStatementWhile(While x) {
		// TODO Auto-generated method stub
		org.rascalmpl.ast.Statement body = x.getBody();
		java.util.List<Expression> generators = x.getConditions();
		IValue generator = eXs(generators);
		return Block(body, KW("while"), BoxADT.LPAR, HV(generator), BoxADT.RPAR);
	}

	public IValue visitStrCharAmbiguity(org.rascalmpl.ast.StrChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrCharLexical(org.rascalmpl.ast.StrChar.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrCharnewline(newline x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrConAmbiguity(org.rascalmpl.ast.StrCon.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrConLexical(org.rascalmpl.ast.StrCon.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrategyAmbiguity(org.rascalmpl.ast.Strategy.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrategyBottomUp(BottomUp x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrategyBottomUpBreak(BottomUpBreak x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrategyInnermost(Innermost x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrategyOutermost(Outermost x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrategyTopDown(TopDown x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStrategyTopDownBreak(TopDownBreak x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringCharacterAmbiguity(
			org.rascalmpl.ast.StringCharacter.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringCharacterLexical(
			org.rascalmpl.ast.StringCharacter.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringConstantAmbiguity(
			org.rascalmpl.ast.StringConstant.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringConstantLexical(
			org.rascalmpl.ast.StringConstant.Lexical x) {
		// TODO Auto-generated method stub
		// System.err.println("HELP visitStringConstantLexical:"+x.getString());
		return BoxADT.getList(L(x.getString()));
	}

	public IValue visitStringLiteralAmbiguity(
			org.rascalmpl.ast.StringLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringLiteralInterpolated(
			org.rascalmpl.ast.StringLiteral.Interpolated x) {
		// TODO Auto-generated method stub
		return list(eX(x.getPre()), eX(x.getExpression()), eX(x.getTail()));
	}

	public IValue visitStringLiteralNonInterpolated(
			org.rascalmpl.ast.StringLiteral.NonInterpolated x) {
		// TODO Auto-generated method stub
		// System.err.println("visitStringLiteralNonInterpolated:"+x.getConstant().getClass());
		return eX(x.getConstant());
	}

	public IValue visitStringLiteralTemplate(Template x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringMiddleAmbiguity(
			org.rascalmpl.ast.StringMiddle.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringMiddleInterpolated(
			org.rascalmpl.ast.StringMiddle.Interpolated x) {
		// TODO Auto-generated method stub
		return list(eX(x.getMid()), eX(x.getExpression()), eX(x.getTail()));
	}

	public IValue visitStringMiddleMid(org.rascalmpl.ast.StringMiddle.Mid x) {
		// TODO Auto-generated method stub
		return eX(x.getMid());
	}

	public IValue visitStringMiddleTemplate(
			org.rascalmpl.ast.StringMiddle.Template x) {
		// TODO Auto-generated method stub
		return list(eX(x.getMid()), eX(x.getTemplate()), eX(x.getTail()));
	}

	public IValue visitStringTailAmbiguity(
			org.rascalmpl.ast.StringTail.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringTailMidInterpolated(MidInterpolated x) {
		// TODO Auto-generated method stub
		return list(eX(x.getMid()), eX(x.getExpression()), eX(x.getTail()));
	}

	public IValue visitStringTailMidTemplate(MidTemplate x) {
		// TODO Auto-generated method stub
		return list(eX(x.getMid()), eX(x.getTemplate()), eX(x.getTail()));
	}

	public IValue visitStringTailPost(org.rascalmpl.ast.StringTail.Post x) {
		// TODO Auto-generated method stub
		return eX(x.getPost());
	}

	public IValue visitStringTemplateAmbiguity(
			org.rascalmpl.ast.StringTemplate.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStringTemplateDoWhile(
			org.rascalmpl.ast.StringTemplate.DoWhile x) {
		// TODO Auto-generated method stub
		/*
		 * "do" "{" preStats:Statement* body:StringMiddle postStats:Statement*
		 * "}" "while" "(" condition:Expression ")" -> StringTemplate
		 * {cons("DoWhile")}
		 */
		return list(KW("do"), BoxADT.LBLOCK, eXs0(x.getPreStats()), eX(x
				.getBody()), eXs0(x.getPostStats()), BoxADT.RBLOCK,
				KW("while"), BoxADT.LPAR, eX(x.getCondition()), BoxADT.RPAR);
	}

	public IValue visitStringTemplateFor(org.rascalmpl.ast.StringTemplate.For x) {
		// TODO Auto-generated method stub
		return list(KW("for"), BoxADT.LPAR, eXs(x.getGenerators()),
				BoxADT.RPAR, BoxADT.LBLOCK, eXs0(x.getPreStats()), eX(x
						.getBody()), eXs0(x.getPostStats()), BoxADT.RBLOCK);
	}

	public IValue visitStringTemplateIfThen(
			org.rascalmpl.ast.StringTemplate.IfThen x) {
		// TODO Auto-generated method stub
		return list(KW("if"), BoxADT.LPAR, eXs(x.getConditions()), BoxADT.RPAR,
				BoxADT.LBLOCK, eXs0(x.getPreStats()), eX(x.getBody()), eXs0(x
						.getPostStats()), BoxADT.RBLOCK);

	}

	public IValue visitStringTemplateIfThenElse(
			org.rascalmpl.ast.StringTemplate.IfThenElse x) {
		// TODO Auto-generated method stub
		return list(KW("if"), BoxADT.LPAR, eXs(x.getConditions()), BoxADT.RPAR,
				BoxADT.LBLOCK, eXs0(x.getPreStatsThen()),
				eX(x.getThenString()), eXs0(x.getPostStatsThen()),
				BoxADT.RBLOCK, KW("else"), BoxADT.LBLOCK, eXs0(x
						.getPreStatsElse()), eX(x.getElseString()), eXs0(x
						.getPostStatsElse()), BoxADT.RBLOCK);
	}

	public IValue visitStringTemplateWhile(
			org.rascalmpl.ast.StringTemplate.While x) {
		// TODO Auto-generated method stub
		return list(KW("while"), BoxADT.LPAR, eX(x.getCondition()),
				BoxADT.RPAR, BoxADT.LBLOCK, eXs0(x.getPreStats()), eX(x
						.getBody()), eXs0(x.getPostStats()), BoxADT.RBLOCK);
	}

	public IValue visitStructuredTypeAmbiguity(
			org.rascalmpl.ast.StructuredType.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitStructuredTypeDefault(
			org.rascalmpl.ast.StructuredType.Default x) {
		return H(0, eX(x.getBasicType()), BoxADT.LBRACK, eXs(x.getArguments()),
				BoxADT.RBRACK);
	}

	public IValue visitSymbolAlternative(Alternative x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolAmbiguity(org.rascalmpl.ast.Symbol.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolCaseInsensitiveLiteral(CaseInsensitiveLiteral x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolCharacterClass(CharacterClass x) {
		return L(x.getClass().toString());
	}

	public IValue visitSymbolEmpty(org.rascalmpl.ast.Symbol.Empty x) {
		return L(x.getClass().toString());
	}

	public IValue visitSymbolIter(Iter x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolIterSep(IterSep x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolIterStar(IterStar x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolIterStarSep(IterStarSep x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolLiteral(org.rascalmpl.ast.Symbol.Literal x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolOptional(Optional x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolSequence(Sequence x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitSymbolSort(Sort x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagAmbiguity(org.rascalmpl.ast.Tag.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagCharAmbiguity(org.rascalmpl.ast.TagChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagCharLexical(org.rascalmpl.ast.TagChar.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagDefault(org.rascalmpl.ast.Tag.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagEmpty(org.rascalmpl.ast.Tag.Empty x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagExpression(org.rascalmpl.ast.Tag.Expression x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagStringAmbiguity(
			org.rascalmpl.ast.TagString.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagStringLexical(org.rascalmpl.ast.TagString.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagsAmbiguity(org.rascalmpl.ast.Tags.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTagsDefault(org.rascalmpl.ast.Tags.Default x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTargetAmbiguity(org.rascalmpl.ast.Target.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTargetEmpty(org.rascalmpl.ast.Target.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTargetLabeled(org.rascalmpl.ast.Target.Labeled x) {
		// TODO Auto-generated method stub
		return eX(x.getName());
	}

	public IValue visitTestAmbiguity(org.rascalmpl.ast.Test.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTestLabeled(org.rascalmpl.ast.Test.Labeled x) {
		// TODO Auto-generated method stub
		return HV(0, eX(x.getExpression()), BoxADT.SEMICOLON,
				eX(x.getLabeled()));
	}

	public IValue visitTestUnlabeled(Unlabeled x) {
		// TODO Auto-generated method stub
		return HV(0, eX(x.getExpression()));
	}

	public IValue visitTimePartNoTZAmbiguity(
			org.rascalmpl.ast.TimePartNoTZ.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTimePartNoTZLexical(
			org.rascalmpl.ast.TimePartNoTZ.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTimeZonePartAmbiguity(
			org.rascalmpl.ast.TimeZonePart.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTimeZonePartLexical(
			org.rascalmpl.ast.TimeZonePart.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitToplevelAmbiguity(org.rascalmpl.ast.Toplevel.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitToplevelGivenVisibility(GivenVisibility x) {
		// TODO Auto-generated method stub
		return eX(x.getDeclaration());
	}

	public IValue visitTypeAmbiguity(org.rascalmpl.ast.Type.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeArgAmbiguity(org.rascalmpl.ast.TypeArg.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeArgDefault(org.rascalmpl.ast.TypeArg.Default x) {
		// TODO Auto-generated method stub
		return x.getType().accept(this);
	}

	public IValue visitTypeArgNamed(Named x) {
		// TODO Auto-generated method stub
		return H(1, eX(x.getType()), eX(x.getName()));
	}

	public IValue visitTypeBasic(Basic x) {
		// TODO Auto-generated method stub
		return KW(x.toString());
	}

	public IValue visitTypeBracket(org.rascalmpl.ast.Type.Bracket x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeFunction(org.rascalmpl.ast.Type.Function x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeSelector(org.rascalmpl.ast.Type.Selector x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeStructured(Structured x) {
		// TODO Auto-generated method stub
		return eX(x.getStructured());
	}

	public IValue visitTypeSymbol(Symbol x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeUser(User x) {
		// TODO Auto-generated method stub
		return L(x.toString());
	}

	public IValue visitTypeVarAmbiguity(org.rascalmpl.ast.TypeVar.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeVarBounded(Bounded x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitTypeVarFree(Free x) {
		// TODO Auto-generated method stub
		return H(0, BoxADT.AMPERSAND, eX(x.getName()));
	}

	public IValue visitTypeVariable(org.rascalmpl.ast.Type.Variable x) {
		// TODO Auto-generated method stub
		return eX(x.getTypeVar());
	}

	public IValue visitURLCharsAmbiguity(org.rascalmpl.ast.URLChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitURLCharsLexical(org.rascalmpl.ast.URLChars.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitUnicodeEscapeAmbiguity(
			org.rascalmpl.ast.UnicodeEscape.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitUnicodeEscapeLexical(
			org.rascalmpl.ast.UnicodeEscape.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitUserTypeAmbiguity(org.rascalmpl.ast.UserType.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitUserTypeName(org.rascalmpl.ast.UserType.Name x) {
		// TODO Auto-generated method stub
		return eX(x.getName());
	}

	public IValue visitUserTypeParametric(Parametric x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitVariableAmbiguity(org.rascalmpl.ast.Variable.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitVariableInitialized(Initialized x) {
		// TODO Auto-generated method stub
		return H(eX(x.getName()), L("="), HV(0, eX(x.getInitial())));
	}

	public IValue visitVariableUnInitialized(UnInitialized x) {
		// TODO Auto-generated method stub
		return eX(x.getName());
	}

	public IValue visitVariantAmbiguity(org.rascalmpl.ast.Variant.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitVariantNAryConstructor(NAryConstructor x) {
		// TODO Auto-generated method stub
		if (!x.getArguments().isEmpty())
			return I(H(0, eX(x.getName()), BoxADT.LPAR,
					HV(eXs(x.getArguments())), BoxADT.RPAR));
		else
			return I(eX(x.getName()));
	}

	public IValue visitVisibilityAmbiguity(
			org.rascalmpl.ast.Visibility.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitVisibilityDefault(org.rascalmpl.ast.Visibility.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVisibilityPrivate(Private x) {
		// TODO Auto-generated method stub
		return KW("private");
	}

	public IValue visitVisibilityPublic(Public x) {
		// TODO Auto-generated method stub
		return KW("public");
	}

	public IValue visitVisitAmbiguity(org.rascalmpl.ast.Visit.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitVisitDefaultStrategy(DefaultStrategy x) {
		// TODO Auto-generated method stub
		java.util.List<Case> cases = x.getCases();
		IList header = list(KW("visit"), BoxADT.LPAR, eX(x.getSubject()),
				BoxADT.RPAR, BoxADT.LBLOCK);
		IList r = list(H(0, header));
		for (Case c : cases) {
			r = r.append(eX(c));
		}
		r = r.append(I(BoxADT.RBLOCK));
		return V(r);
	}

	public IValue visitVisitGivenStrategy(GivenStrategy x) {
		// TODO Auto-generated method stub
		java.util.List<Case> cases = x.getCases();
		Strategy s = x.getStrategy();
		IList header = list(H(1, L(s.getClass().getSimpleName()), KW("visit")),
				BoxADT.LPAR, eX(x.getSubject()), BoxADT.RPAR, BoxADT.LBLOCK);
		IList r = list(H(0, header));
		for (Case c : cases) {
			r = r.append(eX(c));
		}
		r = r.append(I(BoxADT.RBLOCK));
		return V(r);
	}

	public Stack<Accumulator> getAccumulators() {
		// TODO Auto-generated method stub
		return null;
	}

	public Environment getCurrentEnvt() {
		// TODO Auto-generated method stub
		return null;
	}

	public Evaluator getEvaluator() {
		// TODO Auto-generated method stub
		return null;
	}

	public GlobalEnvironment getHeap() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStackTrace() {
		// TODO Auto-generated method stub
		return null;
	}

	public IStrategyContext getStrategyContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public IValueFactory getValueFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public void popStrategyContext() {
		// TODO Auto-generated method stub

	}

	public void pushEnv() {
		// TODO Auto-generated method stub

	}

	public void pushStrategyContext(IStrategyContext strategyContext) {
		// TODO Auto-generated method stub

	}

	public boolean runTests() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setAccumulators(Stack<Accumulator> accumulators) {
		// TODO Auto-generated method stub

	}

	public void setCurrentEnvt(Environment environment) {
		// TODO Auto-generated method stub

	}

	public void unwind(Environment old) {
		// TODO Auto-generated method stub

	}

	static IValue KW(String s) {
		return BoxADT.TAG.KW.create(BoxADT.TAG.L.create(s));
	}

	static IValue NM(String s) {
		return BoxADT.TAG.NM.create(BoxADT.TAG.L.create(s));
	}

	static IValue VAR(String s) {
		return BoxADT.TAG.VAR.create(BoxADT.TAG.L.create(s));
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

	static IValue I(IValue... t) {
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

	public IValue visitDeclarationDataAbstract(DataAbstract x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionIt(It x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitExpressionReducer(Reducer x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRascalReservedKeywordsAmbiguity(
			org.rascalmpl.ast.RascalReservedKeywords.Ambiguity x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	public IValue visitRascalReservedKeywordsLexical(
			org.rascalmpl.ast.RascalReservedKeywords.Lexical x) {
		// TODO Auto-generated method stub
		return L(x.getClass().toString());
	}

	// IValue EmptyBlock(org.rascalmpl.ast.Statement statement) {
	// IValue t = statement.accept(this);
	// if (t == null)
	// return visitStatementEmptyStatement(null);
	// return t;
	// }

	@SuppressWarnings("unchecked")
	IValue eXs(java.util.List conditions) {
		IList s = BoxADT.getEmptyList();
		// System.err.println("eXs0:"+conditions.size());
		for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
			AbstractAST expression = (AbstractAST) iterator.next();
			// System.err.println("eXs1:"+expression);
			if (expression == null)
				continue;
			IValue q = eX(expression);
			if (!s.isEmpty())
				s = s.append(BoxADT.COMMA);
			if (q.getType().isListType())
				s = s.concat((IList) q);
			else
				s = s.append(q);
		}
		// System.err.println("eXs2:"+s);
		return s;
	}

	IValue eXs1(java.util.List conditions) {
		IList s = BoxADT.getEmptyList();
		// System.err.println("eXs0:"+conditions.size());
		for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
			AbstractAST expression = (AbstractAST) iterator.next();
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
		return s;
	}

	@SuppressWarnings("unchecked")
	private IValue eXs0(java.util.List conditions) {
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

	@SuppressWarnings("unchecked")
	private IValue eXs0(java.util.List conditions, IList tree) {
		IList s = BoxADT.getEmptyList();
		// System.err.println("eXs0:"+conditions.size());
		int i = 1, n = tree.length();
		for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
			AbstractAST expression = (AbstractAST) iterator.next();
			// System.err.println("eXs1:"+expression);
			IValue q = eX(expression);
			if (q.getType().isListType())
				s = s.append(((IList) q).get(i));
			else
				s = s.append(q);
			if (i < n)
				s = s.concat(getComment(tree, i));
			i += 2;
		}
		// System.err.println("eXs2:"+s);
		return s;
	}

	private IValue Comprehension(org.rascalmpl.ast.Comprehension x,
			IValue start, IValue end) {
		return list(start, eXs(x.getResults()), BoxADT.VBAR, eXs(x
				.getGenerators()), end);
	}

	private IValue ComprehensionMap(org.rascalmpl.ast.Comprehension x,
			IValue start, IValue end) {
		return list(start, eX(x.getFrom()), BoxADT.COLON, eX(x.getTo()),
				BoxADT.VBAR, eXs(x.getGenerators()), end);
	}

	private IList list(IValue... t) {
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
				return V(a);
			} else {
				IList a = list(H(0, header));
				a = a.append(b);
				return H(1, a);
			}
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

	private int lengths(IList bl) {
		return contentBoxList(bl).length();
	}

	IList getComment(IList z, int ind) {
		IList c = TreeAdapter.searchCategory((IConstructor) z.get(ind),
				"Comment");
		IList r = list();
		for (IValue t : c) {
			String s = TreeAdapter.yield((IConstructor) t);
			if (s.endsWith("\n") && r.length() == c.length() - 1)
				s = s.substring(0, s.length() - 1);
			r = r.append(L(s));
		}
		if (c.length() >= 2)
			return list(H(0, r));
		else
			return r;
	}

	IList getComment(AbstractAST a, int ind) {
		IList z = TreeAdapter.getArgs((IConstructor) a.getTree());
		return getComment(z, ind);
	}

	private IList getTreeList(AbstractAST a, int ind) {
		IList z = TreeAdapter.getArgs((IConstructor) a.getTree());
		IList listToplevels = TreeAdapter.getArgs((IConstructor) z.get(ind));
		return listToplevels;
	}

	IValue split(String s) {
		// String[] t = s.split("\n");
		// IList r = list();
		// for (String a : t) {
		// r = r.append(L(a));
		// }
		// return r.length()>=2?H(r.get(0), V(r.sublist(1, r.length()-1))):r;
		// return r;
		return L(s);
	}

	public IValue visitBasicTypeNum(Num x) {
		// TODO Auto-generated method stub
		return null;
	}

}
