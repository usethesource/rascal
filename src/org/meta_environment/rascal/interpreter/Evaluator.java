package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.ListType;
import org.eclipse.imp.pdb.facts.type.SetType;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.ast.Assignable;
import org.meta_environment.rascal.ast.Declaration;
import org.meta_environment.rascal.ast.Declarator;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.Declaration.Variable;
import org.meta_environment.rascal.ast.Expression.Addition;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.EmptySetOrBlock;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.NonEmptySet;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.meta_environment.rascal.ast.Literal.Boolean;
import org.meta_environment.rascal.ast.Literal.Double;
import org.meta_environment.rascal.ast.Literal.Integer;
import org.meta_environment.rascal.ast.LocalVariableDeclaration.Default;
import org.meta_environment.rascal.ast.Statement.Assignment;
import org.meta_environment.rascal.ast.Statement.Expression;
import org.meta_environment.rascal.ast.Statement.IfThen;
import org.meta_environment.rascal.ast.Statement.IfThenElse;
import org.meta_environment.rascal.ast.Statement.VariableDeclaration;
import org.meta_environment.rascal.ast.Statement.While;
import org.meta_environment.rascal.ast.Variable.Initialized;

class EResult {
	protected Type type;
	protected IValue value;

	public EResult(Type t, IValue v) {
		type = t;
		value = v;
		if (value != null && !value.getType().isSubtypeOf(t)) {
			throw new RascalTypeError("Value " + v + " is not a subtype of "
					+ t);
		}
	}

	public String toString() {
		return "EResult(" + type + ", " + value + ")";
	}
}

public class Evaluator extends NullASTVisitor<EResult> {
	private IValueFactory vf;
	private final TypeFactory tf;
	private final TypeEvaluator te = new TypeEvaluator();
	private final Map<String, EResult> environment = new HashMap<String, EResult>();

	public Evaluator(IValueFactory f) {
		this.vf = f;
		tf = TypeFactory.getInstance();
	}

	private EResult result(Type t, IValue v) {
		return new EResult(t, v);
	}

	private EResult result(IValue v) {
		return new EResult(v.getType(), v);
	}

	public IValue eval(Statement S) {
		EResult r = S.accept(this);

		return S.accept(this).value;
	}

	@Override
	public EResult visitStatementVariableDeclaration(VariableDeclaration x) {
		return x.getDeclaration().accept(this);
	}

	@Override
	public EResult visitLocalVariableDeclarationDefault(Default x) {
		return x.getDeclarator().accept(this);
	}

	@Override
	public EResult visitDeclaratorDefault(
			org.meta_environment.rascal.ast.Declarator.Default x) {
		Type t = x.getType().accept(te);
		EResult r = result(vf.bool(false)); // TODO: this is arbitrary

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			String name = var.getName().toString();
			if (var.isUnInitialized()) {
				r = result(t, null);
				environment.put(name, r);
				System.err.println("put(" + name + ", " + r);
			} else {
				EResult v = var.getInitial().accept(this);
				r = result(t, v.value);
				environment.put(name, r);
				System.err.println("put(" + name + ", " + r);
			}
		}
		return r;
	}

	@Override
	public EResult visitStatementAssignment(Assignment x) {
		Assignable a = x.getAssignable();
		org.meta_environment.rascal.ast.Assignment op = x.getOperator();
		EResult expr = x.getExpression().accept(this);

		if (op.isDefault()) {
			if (a.isVariable()) {
				String name = a.getQualifiedName().toString();
				EResult previous = environment.get(name);
				if (previous != null) {
					if (expr.type.isSubtypeOf(previous.type)) {
						expr.type = previous.type;
					} else {
						throw new RascalTypeError("Variable " + name
								+ " has type " + previous.type
								+ "; cannot assign value of type " + expr.type);
					}
				}
				environment.put(name, expr);
				return expr;
			}
		}
		return null;
	}

	@Override
	public EResult visitStatementIfThenElse(IfThenElse x) {
		for (org.meta_environment.rascal.ast.Expression expr : x
				.getConditions()) {
			EResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
					return x.getElseStatement().accept(this);
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		}

		return x.getThenStatement().accept(this);
	}

	@Override
	public EResult visitStatementIfThen(IfThen x) {
		for (org.meta_environment.rascal.ast.Expression expr : x
				.getConditions()) {
			EResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
					return result(vf.bool(false)); // TODO arbitrary
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		}
		return x.getThenStatement().accept(this);
	}

	@Override
	public EResult visitStatementWhile(While x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		do {
			EResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
					return result(vf.bool(false)); // TODO arbitrary
				} else {
					x.getBody().accept(this);
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		} while (true);
	}
	
	//@Override
	public EResult visitStatementDoWhile(While x) {
		org.meta_environment.rascal.ast.Expression expr = x.getCondition();
		do {
			x.getBody().accept(this);
			EResult cval = expr.accept(this);
			if (cval.type.isBoolType()) {
				if (cval.value.equals(vf.bool(false))) {
					return result(vf.bool(false)); // TODO arbitrary
				}
			} else {
				throw new RascalTypeError("Condition " + expr + " has type "
						+ cval.type + " but should be bool");
			}
		} while (true);
	}

	@Override
	public EResult visitExpressionQualifiedName(
			org.meta_environment.rascal.ast.Expression.QualifiedName x) {
		EResult result = environment.get(x.getQualifiedName().toString());
		if (result != null && result.value != null) {
			return result;
		} else {
			throw new RascalTypeError("Uninitialized variable: " + x);
		}
	}

	@Override
	public EResult visitStatementExpression(Expression x) {
		return x.getExpression().accept(this);
	}

	@Override
	public EResult visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}

	@Override
	public EResult visitLiteralInteger(Integer x) {
		return x.getIntegerLiteral().accept(this);
	}

	@Override
	public EResult visitLiteralDouble(Double x) {
		String str = x.getDoubleLiteral().toString();
		return result(vf.dubble(java.lang.Double.parseDouble(str)));
	}

	@Override
	public EResult visitLiteralBoolean(Boolean x) {
		String str = x.getBooleanLiteral().toString();
		return result(vf.bool(str.equals("true")));
	}

	@Override
	public EResult visitLiteralString(
			org.meta_environment.rascal.ast.Literal.String x) {
		String str = x.getStringLiteral().toString();
		return result(vf.string(deescape(str)));
	}

	private String deescape(String str) {
		// TODO implement this
		return str;
	}

	@Override
	public EResult visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		String str = x.getDecimal().toString();
		return result(vf.integer(java.lang.Integer.parseInt(str)));
	}

	@Override
	public EResult visitExpressionList(List x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		java.util.List<IValue> results = new LinkedList<IValue>();
		Type elementType = evaluateElements(elements, results);

		ListType resultType = tf.listType(elementType);
		IListWriter w = resultType.writer(vf);
		w.appendAll(results);
		return result(resultType, w.done());
	}

	@Override
	public EResult visitExpressionNonEmptySet(NonEmptySet x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();
		java.util.List<IValue> results = new LinkedList<IValue>();
		Type elementType = evaluateElements(elements, results);

		SetType resultType = tf.setType(elementType);
		ISetWriter w = resultType.writer(vf);
		w.insertAll(results);
		return result(resultType, w.done());
	}

	private Type evaluateElements(
			java.util.List<org.meta_environment.rascal.ast.Expression> elements,
			java.util.List<IValue> results) {
		Type elementType = tf.voidType();

		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			EResult resultElem = expr.accept(this);
			elementType = elementType.lub(resultElem.type);
			results.add(results.size(), resultElem.value);
		}
		return elementType;
	}

	@Override
	public EResult visitExpressionEmptySetOrBlock(EmptySetOrBlock x) {
		return result(vf.set(tf.voidType()));
	}

	@Override
	public EResult visitExpressionTuple(Tuple x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x
				.getElements();

		IValue[] values = new IValue[elements.size()];
		Type[] types = new Type[elements.size()];

		for (int i = 0; i < elements.size(); i++) {
			EResult resultElem = elements.get(i).accept(this);
			types[i] = resultElem.type;
			values[i] = resultElem.value;
		}

		return result(tf.tupleType(types), vf.tuple(values));
	}

	public EResult visitExpressionAddition(Addition x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(vf.integer(((IInteger) left.value).getValue()
					+ ((IInteger) right.value).getValue()));
		} else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(vf.dubble(((IDouble) left.value).getValue()
					+ ((IDouble) right.value).getValue()));
		} else if (left.type.isListType() && right.type.isListType()) {
			Type resultType = left.type.lub(right.type);
			return result(resultType, ((IList) left.value)
					.concat((IList) right.value));
		} else if (left.type.isSetType() && right.type.isSetType()) {
			Type resultType = left.type.lub(right.type);
			return result(resultType, ((ISet) left.value)
					.union((ISet) right.value));
			// TODO map
			// TODO relation
		} else {
			throw new RascalTypeError("Operands of + have different types: "
					+ left.type + ", " + right.type);
		}
	}

	@Override
	public EResult visitExpressionOr(Or x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		if (left.type.isBoolType() && right.type.isBoolType()) {
			return result(vf.bool(((IBool) left.value).getValue()
					|| ((IBool) right.value).getValue()));
		} else {
			throw new RascalTypeError(
					"Operands of || should be boolean instead of: " + left.type
							+ ", " + right.type);
		}
	}

	@Override
	public EResult visitExpressionAnd(And x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		if (left.type.isBoolType() && right.type.isBoolType()) {
			return result(vf.bool(((IBool) left.value).getValue()
					&& ((IBool) right.value).getValue()));
		} else {
			throw new RascalTypeError(
					"Operands of && should be boolean instead of: " + left.type
							+ ", " + right.type);
		}
	}

	@Override
	public EResult visitExpressionNegation(Negation x) {
		EResult arg = x.getArgument().accept(this);
		if (arg.type.isBoolType()) {
			return result(vf.bool(!((IBool) arg.value).getValue()));
		} else {
			throw new RascalTypeError(
					"Operand of ! should be boolean instead of: " + arg.type);
		}
	}

	@Override
	public EResult visitExpressionEquals(
			org.meta_environment.rascal.ast.Expression.Equals x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);

		if (left.type.isSubtypeOf(right.type)
				|| right.type.isSubtypeOf(left.type)) {
			return result(vf.bool(left.value.equals(right.value)));
		} else {
			throw new RascalTypeError(
					"Operands of == should have equal types instead of: "
							+ left.type + ", " + right.type);
		}
	}

	@Override
	public EResult visitExpressionNonEquals(
			org.meta_environment.rascal.ast.Expression.NonEquals x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);

		if (left.type.isSubtypeOf(right.type)
				|| right.type.isSubtypeOf(left.type)) {
			return result(vf.bool(!left.value.equals(right.value)));
		} else {
			throw new RascalTypeError(
					"Operands of != should have equal types instead of: "
							+ left.type + ", " + right.type);
		}
	}
}
