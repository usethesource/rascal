package org.rascalmpl.interpreter;

import java.util.Stack;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.ast.AbstractAST;
import org.rascalmpl.ast.Body;
import org.rascalmpl.ast.Expression;
import org.rascalmpl.ast.Formal;
import org.rascalmpl.ast.FunctionModifier;
import org.rascalmpl.ast.FunctionModifiers;
import org.rascalmpl.ast.Import;
import org.rascalmpl.ast.Mapping;
import org.rascalmpl.ast.Module;
import org.rascalmpl.ast.Toplevel;
import org.rascalmpl.ast.Type;
import org.rascalmpl.ast.TypeArg;
import org.rascalmpl.ast.Variable;
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
import org.rascalmpl.interpreter.env.Environment;
import org.rascalmpl.interpreter.env.GlobalEnvironment;
import org.rascalmpl.interpreter.strategy.IStrategyContext;

public class BoxEvaluator implements IEvaluator<IValue> {
	private AbstractAST currentAST;

	// final private BoxADT b = new BoxADT();

	public TypeStore getTypeStore() {
		return BoxADT.getTypeStore();
	}

	// public TreeEvaluator(PrintWriter stderr, PrintWriter stdout) {
	// this.stderr = stderr;
	// this.stdout = stdout;
	// }

	public void setCurrentAST(AbstractAST currentAST) {
		this.currentAST = currentAST;
	}

	public AbstractAST getCurrentAST() {
		return currentAST;
	}

	public IValue evalRascalModule(Module module) {
		return module.accept(this);
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
		return null;
	}

	public IValue visitAlternativeNamedType(NamedType x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableAmbiguity(
			org.rascalmpl.ast.Assignable.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableAnnotation(Annotation x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableConstructor(Constructor x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableFieldAccess(FieldAccess x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableIfDefinedOrDefault(IfDefinedOrDefault x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableSubscript(Subscript x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableTuple(Tuple x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignableVariable(
			org.rascalmpl.ast.Assignable.Variable x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentAddition(Addition x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentAmbiguity(
			org.rascalmpl.ast.Assignment.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentDefault(Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentDivision(Division x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentIfDefined(IfDefined x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentIntersection(Intersection x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentProduct(Product x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAssignmentSubtraction(Subtraction x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAsteriskAmbiguity(
			org.rascalmpl.ast.Asterisk.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitAsteriskLexical(Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBackslashAmbiguity(
			org.rascalmpl.ast.Backslash.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBackslashLexical(
			org.rascalmpl.ast.Backslash.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeAmbiguity(
			org.rascalmpl.ast.BasicType.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeBag(Bag x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeBool(Bool x) {
		// TODO Auto-generated method stub
		return BoxADT.L("bool");
	}

	public IValue visitBasicTypeDateTime(DateTime x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeInt(Int x) {
		// TODO Auto-generated method stub
		return BoxADT.L("int");
	}

	public IValue visitBasicTypeLex(Lex x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeList(List x) {
		// TODO Auto-generated method stub
		return BoxADT.L("list");
	}

	public IValue visitBasicTypeLoc(Loc x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeMap(Map x) {
		// TODO Auto-generated method stub
		return BoxADT.L("map");
	}

	public IValue visitBasicTypeNode(Node x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeReal(Real x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeReifiedAdt(ReifiedAdt x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeReifiedConstructor(ReifiedConstructor x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeReifiedFunction(ReifiedFunction x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeReifiedNonTerminal(ReifiedNonTerminal x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeReifiedReifiedType(ReifiedReifiedType x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeReifiedType(ReifiedType x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeRelation(Relation x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeSet(Set x) {
		// TODO Auto-generated method stub
		return BoxADT.L("set");
	}

	public IValue visitBasicTypeString(
			org.rascalmpl.ast.BasicType.String x) {
		// TODO Auto-generated method stub
		return BoxADT.L("str");
	}

	public IValue visitBasicTypeTuple(
			org.rascalmpl.ast.BasicType.Tuple x) {
		// TODO Auto-generated method stub
		return BoxADT.L("tuple");
	}

	public IValue visitBasicTypeValue(Value x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBasicTypeVoid(Void x) {
		// TODO Auto-generated method stub
		return BoxADT.L("void");
	}

	public IValue visitBodyAmbiguity(
			org.rascalmpl.ast.Body.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBodyAnything(Anything x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBodyToplevels(Toplevels x) {
		// TODO Auto-generated method stub
		IList a = BoxADT.getEmptyList();
		for (Toplevel tl : x.getToplevels()) {
			org.rascalmpl.ast.Declaration decl = tl
					.getDeclaration();
			IValue r = decl.accept(this);
			if (r != null)
				a = a.append(r);
		}
		return BoxADT.TAG.V.create(a);
	}

	public IValue visitBooleanLiteralAmbiguity(
			org.rascalmpl.ast.BooleanLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBooleanLiteralLexical(
			org.rascalmpl.ast.BooleanLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBoundAmbiguity(
			org.rascalmpl.ast.Bound.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBoundDefault(
			org.rascalmpl.ast.Bound.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitBoundEmpty(Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCaseAmbiguity(
			org.rascalmpl.ast.Case.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCaseDefault(
			org.rascalmpl.ast.Case.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCasePatternWithAction(PatternWithAction x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCatchAmbiguity(
			org.rascalmpl.ast.Catch.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCatchBinding(Binding x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCatchDefault(
			org.rascalmpl.ast.Catch.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharClassAmbiguity(
			org.rascalmpl.ast.CharClass.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharClassBracket(Bracket x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharClassComplement(Complement x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharClassDifference(Difference x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharClassIntersection(
			org.rascalmpl.ast.CharClass.Intersection x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharClassSimpleCharclass(SimpleCharclass x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharClassUnion(Union x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharRangeAmbiguity(
			org.rascalmpl.ast.CharRange.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharRangeCharacter(Character x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharRangeRange(Range x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharRangesAmbiguity(
			org.rascalmpl.ast.CharRanges.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharRangesBracket(
			org.rascalmpl.ast.CharRanges.Bracket x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharRangesConcatenate(Concatenate x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharRangesRange(
			org.rascalmpl.ast.CharRanges.Range x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterAmbiguity(
			org.rascalmpl.ast.Character.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterBottom(Bottom x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterEOF(EOF x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterLiteralAmbiguity(
			org.rascalmpl.ast.CharacterLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterLiteralLexical(
			org.rascalmpl.ast.CharacterLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterNumeric(Numeric x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterShort(Short x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCharacterTop(Top x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommandAmbiguity(
			org.rascalmpl.ast.Command.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommandDeclaration(Declaration x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommandExpression(
			org.rascalmpl.ast.Command.Expression x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommandImport(
			org.rascalmpl.ast.Command.Import x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommandLexical(
			org.rascalmpl.ast.Command.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommandShell(Shell x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommandStatement(Statement x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommentAmbiguity(
			org.rascalmpl.ast.Comment.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommentCharAmbiguity(
			org.rascalmpl.ast.CommentChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommentCharLexical(
			org.rascalmpl.ast.CommentChar.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitCommentLexical(
			org.rascalmpl.ast.Comment.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitComprehensionAmbiguity(
			org.rascalmpl.ast.Comprehension.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitComprehensionList(
			org.rascalmpl.ast.Comprehension.List x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitComprehensionMap(
			org.rascalmpl.ast.Comprehension.Map x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitComprehensionSet(
			org.rascalmpl.ast.Comprehension.Set x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDataTargetAmbiguity(
			org.rascalmpl.ast.DataTarget.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDataTargetEmpty(
			org.rascalmpl.ast.DataTarget.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDataTargetLabeled(Labeled x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDataTypeSelectorAmbiguity(
			org.rascalmpl.ast.DataTypeSelector.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDataTypeSelectorSelector(Selector x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDateAndTimeAmbiguity(
			org.rascalmpl.ast.DateAndTime.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDateAndTimeLexical(
			org.rascalmpl.ast.DateAndTime.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDatePartAmbiguity(
			org.rascalmpl.ast.DatePart.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDatePartLexical(
			org.rascalmpl.ast.DatePart.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDateTimeLiteralAmbiguity(
			org.rascalmpl.ast.DateTimeLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDateTimeLiteralDateAndTimeLiteral(DateAndTimeLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDateTimeLiteralDateLiteral(DateLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDateTimeLiteralTimeLiteral(TimeLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDecimalIntegerLiteralAmbiguity(
			org.rascalmpl.ast.DecimalIntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDecimalIntegerLiteralLexical(
			org.rascalmpl.ast.DecimalIntegerLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDecimalLongLiteralAmbiguity(
			org.rascalmpl.ast.DecimalLongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDecimalLongLiteralLexical(
			org.rascalmpl.ast.DecimalLongLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDeclarationAlias(Alias x) {
		// TODO Auto-generated method stub
		IValue user = x.getUser().accept(this), base = x.getBase().accept(this);
		if (user != null && base != null) {
			return BoxADT.TAG.H.create(BoxADT.KW("alias"), user, BoxADT.ASSIGN,
					base, BoxADT.semicolumn());
		}
		return null;
	}

	public IValue visitDeclarationAmbiguity(
			org.rascalmpl.ast.Declaration.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDeclarationAnnotation(
			org.rascalmpl.ast.Declaration.Annotation x) {
		IValue t1 = x.getAnnoType().accept(this), t2 = x.getOnType().accept(
				this);
		if (t1 == null || t2 == null)
			return null;
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
				return null;
			b = b.append(I(H((b.isEmpty() ? L("=") : L("|")), t)));
		}
		return V((b.insert(r)).append(BoxADT.semicolumn()));
	}

	public IValue visitDeclarationFunction(Function x) {
		// TODO Auto-generated method stub
		return x.getFunctionDeclaration().accept(this);
	}

	public IValue visitDeclarationRule(Rule x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDeclarationTag(Tag x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDeclarationTest(Test x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDeclarationVariable(
			org.rascalmpl.ast.Declaration.Variable x) {
		// TODO Auto-generated method stub
		Type tp = x.getType();
		if (BoxADT.DEBUG) System.err
				.println("visitDeclarationVariable:" + x.getType().getClass());
		IValue typ = tp.accept(this);
		if (typ != null) {
			java.util.List<Variable> vs = x.getVariables();
			IList b = BoxADT.getEmptyList();
			for (Variable v : vs) {
				IValue r = v.accept(this);
				if (r == null)
					return null;
				if (!b.isEmpty())
					b = b.append(BoxADT.comma());
				b = b.append(r);
			}
			IValue v = x.getVisibility().accept(this);
			IValue r = BoxADT.TAG.H.create(b);
			if (v == null)
				return BoxADT.TAG.H.create(typ, r, BoxADT.semicolumn());
			else
				return BoxADT.TAG.H.create(v, typ, r, BoxADT.semicolumn());
		}
		return null;
	}

	public IValue visitDeclarationView(View x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDeclaratorAmbiguity(
			org.rascalmpl.ast.Declarator.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitDeclaratorDefault(
			org.rascalmpl.ast.Declarator.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitEscapeSequenceAmbiguity(
			org.rascalmpl.ast.EscapeSequence.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitEscapeSequenceLexical(
			org.rascalmpl.ast.EscapeSequence.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitEscapedNameAmbiguity(
			org.rascalmpl.ast.EscapedName.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitEscapedNameLexical(
			org.rascalmpl.ast.EscapedName.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionAddition(
			org.rascalmpl.ast.Expression.Addition x) {
		// TODO Auto-generated method stub
		IList a1 = (IList) x.getLhs().accept(this);
		IList a2 = (IList) x.getRhs().accept(this);
		if (a1 != null && a2 != null) {
			a1 = a1.append(BoxADT.PLUS);
			a1 = a1.concat(a2);
			return a1;
		}
		return null;
	}

	public IValue visitExpressionAll(All x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionAmbiguity(
			org.rascalmpl.ast.Expression.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionAnd(And x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionAnti(Anti x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionAny(Any x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionBracket(
			org.rascalmpl.ast.Expression.Bracket x) {
		IList r = (IList) x.getExpression().getExpression().accept(this);
		// TODO Auto-generated method stub
		return r.insert(BoxADT.LPAR).append(BoxADT.RPAR);
	}

	public IValue visitExpressionCallOrTree(CallOrTree x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionClosure(Closure x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionComposition(Composition x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionComprehension(Comprehension x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionDescendant(Descendant x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionDivision(
			org.rascalmpl.ast.Expression.Division x) {
		// TODO Auto-generated method stub
		IList a1 = (IList) x.getLhs().accept(this);
		IList a2 = (IList) x.getRhs().accept(this);
		if (a1 != null && a2 != null) {
			a1 = a1.append(BoxADT.DIVIDE);
			a1 = a1.concat(a2);
			return a1;
		}
		return null;
	}

	public IValue visitExpressionEnumerator(Enumerator x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionEquals(Equals x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionEquivalence(Equivalence x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionFieldAccess(
			org.rascalmpl.ast.Expression.FieldAccess x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionFieldProject(FieldProject x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionFieldUpdate(FieldUpdate x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionGetAnnotation(GetAnnotation x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionGreaterThan(GreaterThan x) {
		// TODO Auto-generated method stub
		IList a1 = (IList) x.getLhs().accept(this);
		IList a2 = (IList) x.getRhs().accept(this);
		if (a1 != null && a2 != null) {
			a1 = a1.append(BoxADT.GT);
			a1 = a1.concat(a2);
			return a1;
		}
		return null;
	}

	public IValue visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		// TODO Auto-generated method stub
		IList a1 = (IList) x.getLhs().accept(this);
		IList a2 = (IList) x.getRhs().accept(this);
		if (a1 != null && a2 != null) {
			a1 = a1.append(BoxADT.GE);
			a1 = a1.concat(a2);
			return a1;
		}
		return null;
	}

	public IValue visitExpressionGuarded(Guarded x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionIfDefinedOtherwise(IfDefinedOtherwise x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionIfThenElse(IfThenElse x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionImplication(Implication x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionIn(In x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionIntersection(
			org.rascalmpl.ast.Expression.Intersection x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionIsDefined(IsDefined x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionJoin(Join x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionLessThan(LessThan x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionLessThanOrEq(LessThanOrEq x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionLexical(
			org.rascalmpl.ast.Expression.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionList(
			org.rascalmpl.ast.Expression.List x) {
		// TODO Auto-generated method stub
		java.util.List<Expression> ms = x.getElements();
		IList b = BoxADT.getEmptyList();
		for (Expression m : ms) {
			if (!b.isEmpty())
				b = b.append(BoxADT.comma());
			IValue r = m.accept(this);
			if (r == null)
				return null;
			b = b.append(r);
		}
		b = (b.insert(BoxADT.LBRACK)).append(BoxADT.RBRACK);
		return b;
	}

	public IValue visitExpressionLiteral(Literal x) {
		// TODO Auto-generated method stub
		return x.getLiteral().accept(this);
	}

	public IValue visitExpressionMap(
			org.rascalmpl.ast.Expression.Map x) {
		// TODO Auto-generated method stub
		java.util.List<Mapping> ms = x.getMappings();
		IList b = BoxADT.getEmptyList();
		for (Mapping m : ms) {
			if (!b.isEmpty())
				b = b.append(BoxADT.comma());
			IValue r = H(((IList) m.getFrom().accept(this)).get(0), L(":"),
					((IList) m.getTo().accept(this)).get(0));
			b = b.append(r);
		}
		b = b.insert(BoxADT.LPAR);
		b = b.append(BoxADT.RPAR);
		return b;
	}

	public IValue visitExpressionMatch(Match x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionModulo(Modulo x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionMultiVariable(MultiVariable x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionNegation(Negation x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionNegative(Negative x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionNoMatch(NoMatch x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionNonEquals(NonEquals x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionNotIn(NotIn x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionOr(Or x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionProduct(
			org.rascalmpl.ast.Expression.Product x) {
		// TODO Auto-generated method stub
		IList a1 = (IList) x.getLhs().accept(this);
		IList a2 = (IList) x.getRhs().accept(this);
		if (a1 != null && a2 != null) {
			a1 = a1.append(BoxADT.MULT);
			a1 = a1.concat(a2);
			return a1;
		}
		return null;
	}

	public IValue visitExpressionQualifiedName(QualifiedName x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionRange(
			org.rascalmpl.ast.Expression.Range x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionReifiedType(
			org.rascalmpl.ast.Expression.ReifiedType x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionReifyType(ReifyType x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionSet(
			org.rascalmpl.ast.Expression.Set x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionSetAnnotation(SetAnnotation x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionStepRange(StepRange x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionSubscript(
			org.rascalmpl.ast.Expression.Subscript x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionSubtraction(
			org.rascalmpl.ast.Expression.Subtraction x) {
		// TODO Auto-generated method stub
		IList a1 = (IList) x.getLhs().accept(this);
		IList a2 = (IList) x.getRhs().accept(this);
		if (a1 != null && a2 != null) {
			a1 = a1.append(BoxADT.MINUS);
			a1 = a1.concat(a2);
			return a1;
		}
		return null;
	}

	public IValue visitExpressionTransitiveClosure(TransitiveClosure x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionTransitiveReflexiveClosure(
			TransitiveReflexiveClosure x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionTuple(
			org.rascalmpl.ast.Expression.Tuple x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionTypedVariable(TypedVariable x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionTypedVariableBecomes(TypedVariableBecomes x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionVariableBecomes(VariableBecomes x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionVisit(Visit x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionVoidClosure(VoidClosure x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFieldAmbiguity(
			org.rascalmpl.ast.Field.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFieldIndex(Index x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFieldName(Name x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFormalAmbiguity(
			org.rascalmpl.ast.Formal.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFormalTypeName(TypeName x) {
		// TODO Auto-generated method stub
		IValue r = x.getType().accept(this);
		if (r==null) return null;
		return H(1, r, L(x.getName().toString()));
	}

	public IValue visitFormalsAmbiguity(
			org.rascalmpl.ast.Formals.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFormalsDefault(
			org.rascalmpl.ast.Formals.Default x) {
		// TODO Auto-generated method stub
		java.util.List<org.rascalmpl.ast.Formal> formals = 
			x.getFormals();
		IList b = BoxADT.getEmptyList();
		for (Formal f:formals) {
			if (!b.isEmpty()) b=b.append(BoxADT.comma());
			IValue r = f.accept(this);
			if (r==null) return null;
			b = b.append(r);
		}
		return H(b);
	}

	public IValue visitFunctionBodyAmbiguity(
			org.rascalmpl.ast.FunctionBody.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionBodyDefault(
			org.rascalmpl.ast.FunctionBody.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionDeclarationAbstract(Abstract x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionDeclarationAmbiguity(
			org.rascalmpl.ast.FunctionDeclaration.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionDeclarationDefault(
			org.rascalmpl.ast.FunctionDeclaration.Default x) {
		// TODO Auto-generated method stub
		return x.getSignature().accept(this);
	}

	public IValue visitFunctionModifierAmbiguity(
			org.rascalmpl.ast.FunctionModifier.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionModifierJava(Java x) {
		// TODO Auto-generated method stub
		return L("java");
	}

	public IValue visitFunctionModifiersAmbiguity(
			org.rascalmpl.ast.FunctionModifiers.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionModifiersList(
			org.rascalmpl.ast.FunctionModifiers.List x) {
		// TODO Auto-generated method stub
		IList b = BoxADT.getEmptyList();
		java.util.List<FunctionModifier> ms = x.getModifiers();
		for (FunctionModifier m: ms) {
			b = b.append(m.accept(this));
		}
		return H(b);
	}

	public IValue visitFunctionTypeAmbiguity(
			org.rascalmpl.ast.FunctionType.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitFunctionTypeTypeArguments(TypeArguments x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitHeaderAmbiguity(
			org.rascalmpl.ast.Header.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitHeaderDefault(
			org.rascalmpl.ast.Header.Default x) {
		java.util.List<Import> imports = x.getImports();
		IValueFactory vf = BoxADT.getValueFactory();
		IList a = vf.list();
		for (Import i : imports) {
			a = a.append(i.accept(this));
		}
		return BoxADT.TAG.V.create(a);
	}

	public IValue visitHeaderParameters(Parameters x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitHexIntegerLiteralAmbiguity(
			org.rascalmpl.ast.HexIntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitHexIntegerLiteralLexical(
			org.rascalmpl.ast.HexIntegerLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitHexLongLiteralAmbiguity(
			org.rascalmpl.ast.HexLongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitHexLongLiteralLexical(
			org.rascalmpl.ast.HexLongLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitImportAmbiguity(
			org.rascalmpl.ast.Import.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitImportDefault(
			org.rascalmpl.ast.Import.Default x) {
		// TODO Auto-generated method stub
		return H(KW("import"), L(x.getModule().getName().toString() + ";"));
	}

	public IValue visitImportExtend(Extend x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitImportedModuleActuals(Actuals x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitImportedModuleActualsRenaming(ActualsRenaming x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitImportedModuleAmbiguity(
			org.rascalmpl.ast.ImportedModule.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitImportedModuleDefault(
			org.rascalmpl.ast.ImportedModule.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitImportedModuleRenamings(Renamings x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitIntegerLiteralAmbiguity(
			org.rascalmpl.ast.IntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		// TODO Auto-generated method stub
		return BoxADT.TAG.NUM.create(BoxADT.TAG.L.create(x.toString()));
	}

	public IValue visitIntegerLiteralHexIntegerLiteral(HexIntegerLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitIntegerLiteralOctalIntegerLiteral(OctalIntegerLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitJustDateAmbiguity(
			org.rascalmpl.ast.JustDate.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitJustDateLexical(
			org.rascalmpl.ast.JustDate.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitJustTimeAmbiguity(
			org.rascalmpl.ast.JustTime.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitJustTimeLexical(
			org.rascalmpl.ast.JustTime.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindAlias(org.rascalmpl.ast.Kind.Alias x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindAll(org.rascalmpl.ast.Kind.All x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindAmbiguity(
			org.rascalmpl.ast.Kind.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindAnno(Anno x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindData(org.rascalmpl.ast.Kind.Data x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindFunction(
			org.rascalmpl.ast.Kind.Function x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindModule(org.rascalmpl.ast.Kind.Module x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindRule(org.rascalmpl.ast.Kind.Rule x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindTag(org.rascalmpl.ast.Kind.Tag x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindVariable(
			org.rascalmpl.ast.Kind.Variable x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitKindView(org.rascalmpl.ast.Kind.View x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLabelAmbiguity(
			org.rascalmpl.ast.Label.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLabelDefault(
			org.rascalmpl.ast.Label.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLabelEmpty(org.rascalmpl.ast.Label.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLiteralAmbiguity(
			org.rascalmpl.ast.Literal.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLiteralBoolean(Boolean x) {
		// TODO Auto-generated method stub
		return BoxADT.getList(BoxADT.TAG.L.create(x.toString()));
	}

	public IValue visitLiteralDateTime(
			org.rascalmpl.ast.Literal.DateTime x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLiteralInteger(Integer x) {
		// TODO Auto-generated method stub
		return BoxADT.getList(BoxADT.TAG.NUM.create(BoxADT.TAG.L.create(x
				.toString())));
	}

	public IValue visitLiteralLocation(Location x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLiteralReal(
			org.rascalmpl.ast.Literal.Real x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLiteralRegExp(RegExp x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLiteralString(
			org.rascalmpl.ast.Literal.String x) {
		// TODO Auto-generated method stub
		return BoxADT.getList(BoxADT.TAG.L.create(x.toString()));
	}

	public IValue visitLocalVariableDeclarationAmbiguity(
			org.rascalmpl.ast.LocalVariableDeclaration.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLocalVariableDeclarationDefault(
			org.rascalmpl.ast.LocalVariableDeclaration.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLocalVariableDeclarationDynamic(Dynamic x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLocationLiteralAmbiguity(
			org.rascalmpl.ast.LocationLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLocationLiteralDefault(
			org.rascalmpl.ast.LocationLiteral.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLocationLiteralFile(
			org.rascalmpl.ast.LocationLiteral.File x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLongLiteralAmbiguity(
			org.rascalmpl.ast.LongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLongLiteralDecimalLongLiteral(DecimalLongLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLongLiteralHexLongLiteral(HexLongLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitLongLiteralOctalLongLiteral(OctalLongLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMappingAmbiguity(
			org.rascalmpl.ast.Mapping.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMappingDefault(
			org.rascalmpl.ast.Mapping.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMarkerAmbiguity(
			org.rascalmpl.ast.Marker.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMarkerLexical(
			org.rascalmpl.ast.Marker.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMidPathCharsAmbiguity(
			org.rascalmpl.ast.MidPathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMidPathCharsLexical(
			org.rascalmpl.ast.MidPathChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMidProtocolCharsAmbiguity(
			org.rascalmpl.ast.MidProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMidProtocolCharsLexical(
			org.rascalmpl.ast.MidProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMidStringCharsAmbiguity(
			org.rascalmpl.ast.MidStringChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitMidStringCharsLexical(
			org.rascalmpl.ast.MidStringChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitModuleActualsAmbiguity(
			org.rascalmpl.ast.ModuleActuals.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitModuleActualsDefault(
			org.rascalmpl.ast.ModuleActuals.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitModuleAmbiguity(
			org.rascalmpl.ast.Module.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitModuleDefault(
			org.rascalmpl.ast.Module.Default x) {
		// TODO Auto-generated method stub
		// return b.create(BoxADT.TAG.KW, b.createLabel("module"));
		IValue moduleName = BoxADT.TAG.L.create(x.getHeader().getName()
				.toString());
		IValue t = x.getHeader().accept(this);
		Body body = x.getBody();
		return BoxADT.TAG.V.create(BoxADT.TAG.H.create(BoxADT.TAG.KW
				.create(BoxADT.TAG.L.create("module")), moduleName), t, body
				.accept(this));
	}

	public IValue visitModuleParametersAmbiguity(
			org.rascalmpl.ast.ModuleParameters.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitModuleParametersDefault(
			org.rascalmpl.ast.ModuleParameters.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNameAmbiguity(
			org.rascalmpl.ast.Name.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNameLexical(
			org.rascalmpl.ast.Name.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNamedBackslashAmbiguity(
			org.rascalmpl.ast.NamedBackslash.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNamedBackslashLexical(
			org.rascalmpl.ast.NamedBackslash.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNamedRegExpAmbiguity(
			org.rascalmpl.ast.NamedRegExp.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNamedRegExpLexical(
			org.rascalmpl.ast.NamedRegExp.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNoElseMayFollowAmbiguity(
			org.rascalmpl.ast.NoElseMayFollow.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNoElseMayFollowDefault(
			org.rascalmpl.ast.NoElseMayFollow.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNumCharAmbiguity(
			org.rascalmpl.ast.NumChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitNumCharLexical(
			org.rascalmpl.ast.NumChar.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitOctalIntegerLiteralAmbiguity(
			org.rascalmpl.ast.OctalIntegerLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitOctalIntegerLiteralLexical(
			org.rascalmpl.ast.OctalIntegerLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitOctalLongLiteralAmbiguity(
			org.rascalmpl.ast.OctalLongLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitOctalLongLiteralLexical(
			org.rascalmpl.ast.OctalLongLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitOptCharRangesAbsent(Absent x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitOptCharRangesAmbiguity(
			org.rascalmpl.ast.OptCharRanges.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitOptCharRangesPresent(Present x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitParametersAmbiguity(
			org.rascalmpl.ast.Parameters.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitParametersDefault(
			org.rascalmpl.ast.Parameters.Default x) {
		// TODO Auto-generated method stub
		return x.getFormals().accept(this);
	}

	public IValue visitParametersVarArgs(VarArgs x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathCharsAmbiguity(
			org.rascalmpl.ast.PathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathCharsLexical(
			org.rascalmpl.ast.PathChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathPartAmbiguity(
			org.rascalmpl.ast.PathPart.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathPartInterpolated(Interpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathPartNonInterpolated(NonInterpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathTailAmbiguity(
			org.rascalmpl.ast.PathTail.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathTailMid(Mid x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPathTailPost(Post x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPatternWithActionAmbiguity(
			org.rascalmpl.ast.PatternWithAction.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPatternWithActionArbitrary(Arbitrary x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPatternWithActionReplacing(Replacing x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPostPathCharsAmbiguity(
			org.rascalmpl.ast.PostPathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPostPathCharsLexical(
			org.rascalmpl.ast.PostPathChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPostProtocolCharsAmbiguity(
			org.rascalmpl.ast.PostProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPostProtocolCharsLexical(
			org.rascalmpl.ast.PostProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPostStringCharsAmbiguity(
			org.rascalmpl.ast.PostStringChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPostStringCharsLexical(
			org.rascalmpl.ast.PostStringChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPrePathCharsAmbiguity(
			org.rascalmpl.ast.PrePathChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPrePathCharsLexical(
			org.rascalmpl.ast.PrePathChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPreProtocolCharsAmbiguity(
			org.rascalmpl.ast.PreProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPreProtocolCharsLexical(
			org.rascalmpl.ast.PreProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPreStringCharsAmbiguity(
			org.rascalmpl.ast.PreStringChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitPreStringCharsLexical(
			org.rascalmpl.ast.PreStringChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolCharsAmbiguity(
			org.rascalmpl.ast.ProtocolChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolCharsLexical(
			org.rascalmpl.ast.ProtocolChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolPartAmbiguity(
			org.rascalmpl.ast.ProtocolPart.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolPartInterpolated(
			org.rascalmpl.ast.ProtocolPart.Interpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolPartNonInterpolated(
			org.rascalmpl.ast.ProtocolPart.NonInterpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolTailAmbiguity(
			org.rascalmpl.ast.ProtocolTail.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolTailMid(
			org.rascalmpl.ast.ProtocolTail.Mid x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitProtocolTailPost(
			org.rascalmpl.ast.ProtocolTail.Post x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitQualifiedNameAmbiguity(
			org.rascalmpl.ast.QualifiedName.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitQualifiedNameDefault(
			org.rascalmpl.ast.QualifiedName.Default x) {
		// TODO Auto-generated method stub
		// java.util.List<org.rascalmpl.ast.Name> names =
		// x.getNames();
		// org.rascalmpl.ast.Name name = names.get(0);
		return BoxADT.TAG.L.create(x.toString());
	}

	public IValue visitRealLiteralAmbiguity(
			org.rascalmpl.ast.RealLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRealLiteralLexical(
			org.rascalmpl.ast.RealLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRegExpAmbiguity(
			org.rascalmpl.ast.RegExp.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRegExpLexical(
			org.rascalmpl.ast.RegExp.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRegExpLiteralAmbiguity(
			org.rascalmpl.ast.RegExpLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRegExpLiteralLexical(
			org.rascalmpl.ast.RegExpLiteral.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRegExpModifierAmbiguity(
			org.rascalmpl.ast.RegExpModifier.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRegExpModifierLexical(
			org.rascalmpl.ast.RegExpModifier.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRenamingAmbiguity(
			org.rascalmpl.ast.Renaming.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRenamingDefault(
			org.rascalmpl.ast.Renaming.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRenamingsAmbiguity(
			org.rascalmpl.ast.Renamings.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRenamingsDefault(
			org.rascalmpl.ast.Renamings.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitReplacementAmbiguity(
			org.rascalmpl.ast.Replacement.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitReplacementConditional(Conditional x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitReplacementUnconditional(Unconditional x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRestAmbiguity(
			org.rascalmpl.ast.Rest.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRestLexical(
			org.rascalmpl.ast.Rest.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandAmbiguity(
			org.rascalmpl.ast.ShellCommand.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandEdit(Edit x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandHelp(Help x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandHistory(History x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandListDeclarations(ListDeclarations x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandListModules(ListModules x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandQuit(Quit x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandSetOption(SetOption x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandTest(
			org.rascalmpl.ast.ShellCommand.Test x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandUndeclare(Undeclare x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShellCommandUnimport(Unimport x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShortCharAmbiguity(
			org.rascalmpl.ast.ShortChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitShortCharLexical(
			org.rascalmpl.ast.ShortChar.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSignatureAmbiguity(
			org.rascalmpl.ast.Signature.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSignatureNoThrows(NoThrows x) {
		// TODO Auto-generated method stub
		FunctionModifiers modifiers = x.getModifiers();
		IList b = (IList) BoxADT.getEmptyList();
		IValue r;
		if (modifiers!=null && (r= modifiers.accept(this))!=null) {
			   b=b.append(r);
		}
		r = x.getType().accept(this);
		if (r==null) return null;
		b=b.append(r);
		IList c = (IList) BoxADT.getEmptyList();
		c=c.append(L(x.getName().toString()));
		c=c.append(BoxADT.LPAR);
		r = x.getParameters().accept(this);
		if (r==null) return null;
		c=c.append(r);
		c=c.append(BoxADT.RPAR);
		b = b.append(H(0, c));
		return H(b);
	}

	public IValue visitSignatureWithThrows(WithThrows x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSingleCharacterAmbiguity(
			org.rascalmpl.ast.SingleCharacter.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSingleCharacterLexical(
			org.rascalmpl.ast.SingleCharacter.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSingleQuotedStrCharAmbiguity(
			org.rascalmpl.ast.SingleQuotedStrChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSingleQuotedStrCharLexical(
			org.rascalmpl.ast.SingleQuotedStrChar.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSingleQuotedStrConAmbiguity(
			org.rascalmpl.ast.SingleQuotedStrCon.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSingleQuotedStrConLexical(
			org.rascalmpl.ast.SingleQuotedStrCon.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementAmbiguity(
			org.rascalmpl.ast.Statement.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementAppend(Append x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementAssert(Assert x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementAssertWithMessage(AssertWithMessage x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementAssignment(Assignment x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementBreak(Break x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementContinue(Continue x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementDoWhile(DoWhile x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementEmptyStatement(EmptyStatement x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementExpression(
			org.rascalmpl.ast.Statement.Expression x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementFail(Fail x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementFor(For x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementFunctionDeclaration(FunctionDeclaration x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementGlobalDirective(GlobalDirective x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementIfThen(IfThen x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementIfThenElse(
			org.rascalmpl.ast.Statement.IfThenElse x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementInsert(Insert x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementNonEmptyBlock(
			org.rascalmpl.ast.Statement.NonEmptyBlock x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementReturn(Return x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementSolve(Solve x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementSwitch(Switch x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementThrow(Throw x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementTry(Try x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementTryFinally(TryFinally x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementVariableDeclaration(VariableDeclaration x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementVisit(
			org.rascalmpl.ast.Statement.Visit x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStatementWhile(While x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrCharAmbiguity(
			org.rascalmpl.ast.StrChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrCharLexical(
			org.rascalmpl.ast.StrChar.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrCharnewline(newline x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrConAmbiguity(
			org.rascalmpl.ast.StrCon.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrConLexical(
			org.rascalmpl.ast.StrCon.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrategyAmbiguity(
			org.rascalmpl.ast.Strategy.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrategyBottomUp(BottomUp x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrategyBottomUpBreak(BottomUpBreak x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrategyInnermost(Innermost x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrategyOutermost(Outermost x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrategyTopDown(TopDown x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStrategyTopDownBreak(TopDownBreak x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringCharacterAmbiguity(
			org.rascalmpl.ast.StringCharacter.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringCharacterLexical(
			org.rascalmpl.ast.StringCharacter.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringConstantAmbiguity(
			org.rascalmpl.ast.StringConstant.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringConstantLexical(
			org.rascalmpl.ast.StringConstant.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringLiteralAmbiguity(
			org.rascalmpl.ast.StringLiteral.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringLiteralInterpolated(
			org.rascalmpl.ast.StringLiteral.Interpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringLiteralNonInterpolated(
			org.rascalmpl.ast.StringLiteral.NonInterpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringLiteralTemplate(Template x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringMiddleAmbiguity(
			org.rascalmpl.ast.StringMiddle.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringMiddleInterpolated(
			org.rascalmpl.ast.StringMiddle.Interpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringMiddleMid(
			org.rascalmpl.ast.StringMiddle.Mid x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringMiddleTemplate(
			org.rascalmpl.ast.StringMiddle.Template x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTailAmbiguity(
			org.rascalmpl.ast.StringTail.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTailMidInterpolated(MidInterpolated x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTailMidTemplate(MidTemplate x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTailPost(
			org.rascalmpl.ast.StringTail.Post x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTemplateAmbiguity(
			org.rascalmpl.ast.StringTemplate.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTemplateDoWhile(
			org.rascalmpl.ast.StringTemplate.DoWhile x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTemplateFor(
			org.rascalmpl.ast.StringTemplate.For x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTemplateIfThen(
			org.rascalmpl.ast.StringTemplate.IfThen x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTemplateIfThenElse(
			org.rascalmpl.ast.StringTemplate.IfThenElse x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStringTemplateWhile(
			org.rascalmpl.ast.StringTemplate.While x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStructuredTypeAmbiguity(
			org.rascalmpl.ast.StructuredType.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitStructuredTypeDefault(
			org.rascalmpl.ast.StructuredType.Default x) {
		IValue bt = x.getBasicType().accept(this);
		IList b = BoxADT.getEmptyList();
		if (bt != null) {
			java.util.List<TypeArg> ta = x.getArguments();
			for (TypeArg q : ta) {
				IValue t = q.accept(this);
				if (t == null)
					return null;
				if (!b.isEmpty())
					b = b.append(BoxADT.comma());
				b = b.append(t);
			}
			return H(0, bt, BoxADT.LBRACK, H(b), BoxADT.RBRACK);
		}
		return null;
	}

	public IValue visitSymbolAlternative(Alternative x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolAmbiguity(
			org.rascalmpl.ast.Symbol.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolCaseInsensitiveLiteral(CaseInsensitiveLiteral x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolCharacterClass(CharacterClass x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolEmpty(
			org.rascalmpl.ast.Symbol.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolIter(Iter x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolIterSep(IterSep x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolIterStar(IterStar x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolIterStarSep(IterStarSep x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolLiteral(
			org.rascalmpl.ast.Symbol.Literal x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolOptional(Optional x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolSequence(Sequence x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitSymbolSort(Sort x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagAmbiguity(
			org.rascalmpl.ast.Tag.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagCharAmbiguity(
			org.rascalmpl.ast.TagChar.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagCharLexical(
			org.rascalmpl.ast.TagChar.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagDefault(org.rascalmpl.ast.Tag.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagEmpty(org.rascalmpl.ast.Tag.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagExpression(
			org.rascalmpl.ast.Tag.Expression x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagStringAmbiguity(
			org.rascalmpl.ast.TagString.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagStringLexical(
			org.rascalmpl.ast.TagString.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagsAmbiguity(
			org.rascalmpl.ast.Tags.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTagsDefault(
			org.rascalmpl.ast.Tags.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTargetAmbiguity(
			org.rascalmpl.ast.Target.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTargetEmpty(
			org.rascalmpl.ast.Target.Empty x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTargetLabeled(
			org.rascalmpl.ast.Target.Labeled x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTestAmbiguity(
			org.rascalmpl.ast.Test.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTestLabeled(
			org.rascalmpl.ast.Test.Labeled x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTestUnlabeled(Unlabeled x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTimePartNoTZAmbiguity(
			org.rascalmpl.ast.TimePartNoTZ.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTimePartNoTZLexical(
			org.rascalmpl.ast.TimePartNoTZ.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTimeZonePartAmbiguity(
			org.rascalmpl.ast.TimeZonePart.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTimeZonePartLexical(
			org.rascalmpl.ast.TimeZonePart.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitToplevelAmbiguity(
			org.rascalmpl.ast.Toplevel.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitToplevelGivenVisibility(GivenVisibility x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeAmbiguity(
			org.rascalmpl.ast.Type.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeArgAmbiguity(
			org.rascalmpl.ast.TypeArg.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeArgDefault(
			org.rascalmpl.ast.TypeArg.Default x) {
		// TODO Auto-generated method stub
		return x.getType().accept(this);
	}

	public IValue visitTypeArgNamed(Named x) {
		// TODO Auto-generated method stub
		return H(x.getType().accept(this), L(x.getName().toString()));
	}

	public IValue visitTypeBasic(Basic x) {
		// TODO Auto-generated method stub
		return KW(x.toString());
	}

	public IValue visitTypeBracket(
			org.rascalmpl.ast.Type.Bracket x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeFunction(
			org.rascalmpl.ast.Type.Function x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeSelector(
			org.rascalmpl.ast.Type.Selector x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeStructured(Structured x) {
		// TODO Auto-generated method stub
		return x.getStructured().accept(this);
	}

	public IValue visitTypeSymbol(Symbol x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeUser(User x) {
		// TODO Auto-generated method stub
		return L(x.toString());
	}

	public IValue visitTypeVarAmbiguity(
			org.rascalmpl.ast.TypeVar.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeVarBounded(Bounded x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeVarFree(Free x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitTypeVariable(
			org.rascalmpl.ast.Type.Variable x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitURLCharsAmbiguity(
			org.rascalmpl.ast.URLChars.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitURLCharsLexical(
			org.rascalmpl.ast.URLChars.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitUnicodeEscapeAmbiguity(
			org.rascalmpl.ast.UnicodeEscape.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitUnicodeEscapeLexical(
			org.rascalmpl.ast.UnicodeEscape.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitUserTypeAmbiguity(
			org.rascalmpl.ast.UserType.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitUserTypeName(
			org.rascalmpl.ast.UserType.Name x) {
		// TODO Auto-generated method stub
		return BoxADT.TAG.L.create(x.getName().toString());
	}

	public IValue visitUserTypeParametric(Parametric x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVariableAmbiguity(
			org.rascalmpl.ast.Variable.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVariableInitialized(Initialized x) {
		// TODO Auto-generated method stub
		Expression e = x.getInitial();
		IValue r = e.accept(this);
		if (r != null && r.getType().isListType()) {
			r = BoxADT.TAG.HV.create(r);
			return BoxADT.TAG.H.create(BoxADT.TAG.L.create(x.getName()
					.toString()
					+ "="), r);
		} else
			return null;
	}

	public IValue visitVariableUnInitialized(UnInitialized x) {
		// TODO Auto-generated method stub
		return BoxADT.TAG.L.create(x.getName().toString());
	}

	public IValue visitVariantAmbiguity(
			org.rascalmpl.ast.Variant.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVariantNAryConstructor(NAryConstructor x) {
		// TODO Auto-generated method stub
		java.util.List<TypeArg> typs = x.getArguments();
		IList b = BoxADT.getEmptyList();
		for (TypeArg typ : typs) {
			IValue t = typ.accept(this);
			if (t == null)
				return null;
			if (!b.isEmpty())
				b = b.append(BoxADT.comma());
			b = b.append(t);
		}
		if (!typs.isEmpty())
			b = b.insert(L("("));
		b = b.insert(L(x.getName().toString()));
		if (!typs.isEmpty())
			b = b.append(L(")"));
		return I(H(b));
	}

	public IValue visitVisibilityAmbiguity(
			org.rascalmpl.ast.Visibility.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVisibilityDefault(
			org.rascalmpl.ast.Visibility.Default x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVisibilityPrivate(Private x) {
		// TODO Auto-generated method stub
		return BoxADT.TAG.L.create("private");
	}

	public IValue visitVisibilityPublic(Public x) {
		// TODO Auto-generated method stub
		return BoxADT.TAG.L.create("public");
	}

	public IValue visitVisitAmbiguity(
			org.rascalmpl.ast.Visit.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVisitDefaultStrategy(DefaultStrategy x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitVisitGivenStrategy(GivenStrategy x) {
		// TODO Auto-generated method stub
		return null;
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

	static IValue NUM(String s) {
		return BoxADT.TAG.NUM.create(BoxADT.TAG.L.create(s));
	}

	static IValue VAR(String s) {
		return BoxADT.TAG.VAR.create(BoxADT.TAG.L.create(s));
	}

	static IValue L(String s) {
		return BoxADT.TAG.L.create(s);
	}

	static IValue H(IValue ... t) {
		return BoxADT.H(t);
	}

	static IValue H(int hspace, IValue ... t) {
		return BoxADT.H(hspace, t);
	}
	static IValue V(IValue ... t) {
		return BoxADT.V(t);
	}

	static IValue I(IValue ... t) {
		return BoxADT.TAG.I.create(t);
	}

	static IValue HV(IValue ... t) {
		return BoxADT.TAG.HV.create(t);
	}
	
	static IValue HOV(IValue ... t) {
		return BoxADT.TAG.HOV.create(t);
	}


	public IValue visitDeclarationDataAbstract(DataAbstract x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionIt(It x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitExpressionReducer(Reducer x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRascalReservedKeywordsAmbiguity(
			org.rascalmpl.ast.RascalReservedKeywords.Ambiguity x) {
		// TODO Auto-generated method stub
		return null;
	}

	public IValue visitRascalReservedKeywordsLexical(
			org.rascalmpl.ast.RascalReservedKeywords.Lexical x) {
		// TODO Auto-generated method stub
		return null;
	}
}
