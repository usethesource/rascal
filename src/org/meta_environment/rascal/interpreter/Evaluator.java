package org.meta_environment.rascal.interpreter;

import java.util.ArrayList;
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
import org.meta_environment.rascal.ast.Assignable;
import org.meta_environment.rascal.ast.NullASTVisitor;
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
import org.meta_environment.rascal.ast.Statement.Assignment;
import org.meta_environment.rascal.ast.Statement.Expression;

public class Evaluator extends NullASTVisitor<IValue> {
	private IValueFactory vf;
	private final TypeFactory tf;
	private final Map<String,IValue> environment = new HashMap<String,IValue>();

	public Evaluator(IValueFactory f) {
		this.vf = f;
		tf = TypeFactory.getInstance();
	}

	@Override
	public IValue visitStatementAssignment(Assignment x) {
		Assignable a = x.getAssignable();
		org.meta_environment.rascal.ast.Assignment op = x.getOperator();
		IValue expr = x.getExpression().accept(this);
		
		if (op.isDefault()) {
			if (a.isVariable()) {
				environment.put(a.getQualifiedName().toString(), expr);
				return expr;
			}
		}
		
		return null;
	}
	
	@Override
	public IValue visitExpressionQualifiedName(
			org.meta_environment.rascal.ast.Expression.QualifiedName x) {
		IValue result = environment.get(x.getQualifiedName().toString());
		if (result != null) {
			return result;
		}
		else {
			throw new RascalTypeError("Uninitialized variable: " + x);
		}
	}
	
	@Override
	public IValue visitStatementExpression(Expression x) {
		return x.getExpression().accept(this);
	}
	
	@Override
	public IValue visitExpressionLiteral(Literal x) {
		return x.getLiteral().accept(this);
	}
	
	@Override
	public IValue visitLiteralInteger(Integer x) {
		return x.getIntegerLiteral().accept(this);
	}
	
	@Override
	public IValue visitLiteralDouble(Double x) {
		String str = x.getDoubleLiteral().toString();
		return vf.dubble(java.lang.Double.parseDouble(str));
	}
	
	@Override
	public IValue visitLiteralBoolean(Boolean x) {
		String str = x.getBooleanLiteral().toString();
		
		if (str.equals("true")) {
			return vf.bool(true);
		}
		else {
			return vf.bool(false);
		}
	}
	
	@Override
	public IValue visitLiteralString(
			org.meta_environment.rascal.ast.Literal.String x) {
		String str = x.getStringLiteral().toString();
		return vf.string(deescape(str));
	}
	
	private String deescape(String str) {
		// TODO implement this
		return str;
	}

	@Override
	public IValue visitIntegerLiteralDecimalIntegerLiteral(
			DecimalIntegerLiteral x) {
		String str = x.getDecimal().toString();
		return vf.integer(java.lang.Integer.parseInt(str));
	}
	
	@Override
	public IValue visitExpressionList(List x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x.getElements();
		java.util.List<IValue> results = new LinkedList<IValue>();
		Type elementType = evaluateElements(elements, results);
		
		ListType resultType = tf.listType(elementType);
		IListWriter w = resultType.writer(vf);
		w.appendAll(results);
		return w.done();
	}
	
	@Override
	public IValue visitExpressionNonEmptySet(NonEmptySet x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x.getElements();
		java.util.List<IValue> results = new LinkedList<IValue>();
		Type elementType = evaluateElements(elements, results);
		
		SetType resultType = tf.setType(elementType);
		ISetWriter w = resultType.writer(vf);
		w.insertAll(results);
		return w.done();
	}

	private Type evaluateElements(
			java.util.List<org.meta_environment.rascal.ast.Expression> elements,
			java.util.List<IValue> results) {
		Type elementType = tf.voidType();
		
		
		for (org.meta_environment.rascal.ast.Expression expr : elements) {
			IValue resultElem = expr.accept(this);
			elementType = elementType.lub(resultElem.getType());
			results.add(results.size(), resultElem);
		}
		return elementType;
	}
	
	@Override
	public IValue visitExpressionEmptySetOrBlock(EmptySetOrBlock x) {
		return vf.set(tf.voidType());
	}
	
	@Override
	public IValue visitExpressionTuple(Tuple x) {
		java.util.List<org.meta_environment.rascal.ast.Expression> elements = x.getElements();
		java.util.List<IValue> results = new ArrayList<IValue>();
		evaluateElements(elements, results);
		IValue[] resultArray = new IValue[results.size()];
		results.toArray(resultArray);
		return vf.tuple(resultArray);
	}
	
	
	public IValue visitExpressionAddition(Addition x) {
		IValue leftValue = x.getLhs().accept(this);
		IValue rightValue = x.getRhs().accept(this);
		Type leftType = leftValue.getType();
		Type rightType = rightValue.getType();
		
		if (leftType.isIntegerType() &&
				rightType.isIntegerType()) {
		  return vf.integer(((IInteger) leftValue).getValue() + ((IInteger) rightValue).getValue());
		}
		else if (leftType.isDoubleType() &&
				rightType.isDoubleType()) {
			return vf.dubble(((IDouble) leftValue).getValue() + ((IDouble) rightValue).getValue());
		}
		else if (leftType.isListType() && rightType.isListType()) {
			return ((IList) leftValue).concat((IList) rightValue);
		}
		else if (leftType.isSetType() && rightType.isSetType()) {
			return ((ISet) leftValue).union((ISet) rightValue);
		}
		else {
			throw new RascalTypeError("Operands of + have different types: " + leftType + ", " + rightType);
		}
	}
	@Override
	public IValue visitExpressionAnd(And x){
		IValue leftValue = x.getLhs().accept(this);
		IValue rightValue = x.getRhs().accept(this);
		Type leftType = leftValue.getType();
		Type rightType = rightValue.getType();
		if (leftType.isBoolType() &&
				rightType.isBoolType()) {
		  return vf.bool(((IBool) leftValue).getValue() && ((IBool) rightValue).getValue());
		} else {
			throw new RascalTypeError("Operands of && should be boolean instead of: " + leftType + ", " + rightType); 
		}
	}
	@Override
	public IValue visitExpressionOr(Or x){
		IValue leftValue = x.getLhs().accept(this);
		IValue rightValue = x.getRhs().accept(this);
		Type leftType = leftValue.getType();
		Type rightType = rightValue.getType();
		if (leftType.isBoolType() &&
				rightType.isBoolType()) {
		  return vf.bool(((IBool) leftValue).getValue() || ((IBool) rightValue).getValue());
		} else {
			throw new RascalTypeError("Operands of && should be boolean instead of: " + leftType + ", " + rightType); 
		}
	}
	
	@Override
	public IValue visitExpressionNegation(Negation x){
		IValue argValue = x.getArgument().accept(this);
		Type argType = argValue.getType();
		if (argType.isBoolType()) {
		  return vf.bool(!((IBool) argValue).getValue());
		} else {
			throw new RascalTypeError("Operand of ! should be boolean instead of: " + argType); 
		}
	}
}
