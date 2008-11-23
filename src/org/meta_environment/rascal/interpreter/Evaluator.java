package org.meta_environment.rascal.interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.text.html.ListView;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IDouble;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.IString;
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
import org.meta_environment.rascal.ast.Generator;
import org.meta_environment.rascal.ast.NullASTVisitor;
import org.meta_environment.rascal.ast.Statement;
import org.meta_environment.rascal.ast.ValueProducer;
import org.meta_environment.rascal.ast.Declaration.Variable;
import org.meta_environment.rascal.ast.Expression.Addition;
import org.meta_environment.rascal.ast.Expression.And;
import org.meta_environment.rascal.ast.Expression.Comprehension;
import org.meta_environment.rascal.ast.Expression.EmptySetOrBlock;
import org.meta_environment.rascal.ast.Expression.GreaterThan;
import org.meta_environment.rascal.ast.Expression.GreaterThanOrEq;
import org.meta_environment.rascal.ast.Expression.LessThan;
import org.meta_environment.rascal.ast.Expression.LessThanOrEq;
import org.meta_environment.rascal.ast.Expression.List;
import org.meta_environment.rascal.ast.Expression.Literal;
import org.meta_environment.rascal.ast.Expression.Negation;
import org.meta_environment.rascal.ast.Expression.NonEmptyBlock;
import org.meta_environment.rascal.ast.Expression.NonEmptySet;
import org.meta_environment.rascal.ast.Expression.Or;
import org.meta_environment.rascal.ast.Expression.QualifiedName;
import org.meta_environment.rascal.ast.Expression.Subscript;
import org.meta_environment.rascal.ast.Expression.Subtraction;
import org.meta_environment.rascal.ast.Expression.Tuple;
import org.meta_environment.rascal.ast.IntegerLiteral.DecimalIntegerLiteral;
import org.meta_environment.rascal.ast.Literal.Boolean;
import org.meta_environment.rascal.ast.Literal.Double;
import org.meta_environment.rascal.ast.Literal.Integer;
import org.meta_environment.rascal.ast.LocalVariableDeclaration.Default;
import org.meta_environment.rascal.ast.Statement.Assignment;
import org.meta_environment.rascal.ast.Statement.Block;
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
	
	private EResult notImplemented(String s){
		throw new RascalTypeError(s + " not yet implemented");
	}

	public IValue eval(Statement S) {
		EResult r = S.accept(this);
        if(r != null){
        	return r.value;
        } else {
        	return vf.bool(false);  //TODO: void
        }
	}
	
	// Variable Declarations -----------------------------------------------

	@Override
	public EResult visitLocalVariableDeclarationDefault(Default x) {
		return x.getDeclarator().accept(this);
	}

	@Override
	public EResult visitDeclaratorDefault(
			org.meta_environment.rascal.ast.Declarator.Default x) {
		Type declaredType = x.getType().accept(te);
		EResult r = result(vf.bool(false)); // TODO: void

		for (org.meta_environment.rascal.ast.Variable var : x.getVariables()) {
			String name = var.getName().toString();
			if (var.isUnInitialized()) {  // variable declaration without initialization
				r = result(declaredType, null);
				environment.put(name, r);
				System.err.println("put(" + name + ", " + r + ")");
			} else {                     // variable declaration with initialization
				EResult v = var.getInitial().accept(this);
				if(v.type.isSubtypeOf(declaredType)){
					r = result(declaredType, v.value);
					environment.put(name, r);
					System.err.println("put(" + name + ", " + r + ")");
				} else {
					throw new RascalTypeError("variable " + name + ", declared type " + declaredType + " incompatible with initial type " + v.type);
				}
			}
		}
		return r;
	}
	
	
   // Statements ---------------------------------------------------------
	
	@Override
	public EResult visitStatementVariableDeclaration(VariableDeclaration x) {
		return x.getDeclaration().accept(this);
	}
	
	@Override
	public EResult visitStatementExpression(Expression x) {
		return x.getExpression().accept(this);
	}
	
	private EResult assignVariable(String name, EResult right){
		EResult previous = environment.get(name);
		if (previous != null) {
			if (right.type.isSubtypeOf(previous.type)) {
				right.type = previous.type;
			} else {
				throw new RascalTypeError("Variable " + name
						+ " has type " + previous.type
						+ "; cannot assign value of type " + right.type);
			}
		}
		environment.put(name, right);
		System.err.println("put(" + name + ", " + right + ")");
		return right;
	}
	
	//TODO Missing function in PDB
	private IList putList(IList L, int i, IValue v){
		IList res = vf.list(L.get(0).getType());
		for(int k = 0; k < L.length(); k++){
			if(k == i){
				res = res.append(v);
			} else {
				res = res.append(L.get(k));
			}
		}
		return res;
	}
	
	private int getValidIndex(EResult subs){
		if(!subs.type.isSubtypeOf(tf.integerType())){
			throw new RascalTypeError("subscript should have type int instead of " + subs.type);
		}
		return ((IInteger) subs.value).getValue();
	}
	
	private Type checkValidListSubscription(EResult previous, EResult subs, int index){
		if (previous != null) {
			if(previous.type.isListType()){
				Type elementType = ((ListType) previous.type).getElementType();
				if((index < 0) || index >= ((IList) previous.value).length()){
					throw new RascalTypeError("subscript " + index + " out of bounds");
				}
				return elementType;
			} else {
				notImplemented("index in assignment");
			}
		} else {
			throw new RascalTypeError("subscription for unnitialized variable ");
		}
		return null;
	}
	
	private Type checkValidListSubscription(String name, EResult subs, int index){
		EResult previous = environment.get(name);
		return checkValidListSubscription(previous, subs, index);
	}
	
	private EResult assignSubscriptedVariable(
			String name, EResult subs, EResult right) {
		
		int index = getValidIndex(subs);
		EResult previous = environment.get(name);
		
		if (previous != null) {
			if(previous.type.isListType()){
				Type elementType = checkValidListSubscription(name, subs, index);
				if (right.type.isSubtypeOf(elementType)) {
					right.type = elementType;
				} else {
					throw new RascalTypeError("subscripted variable " + name
							+ " has element type " + elementType
							+ "; cannot assign value of type " + right.type);
				}
				IValue newValue = putList(((IList) previous.value), index, right.value);
				EResult nw = result(elementType, newValue);
				environment.put(name, nw);
				System.err.println("put(" + name + ", " + nw + ")");
				return nw;
			} else {
				notImplemented("index in assignment");
			}
	} else {
			throw new RascalTypeError("cannot assign to unnitialized subscripted variable " + name);
		}
		return null;
	}
	
	private EResult assign(Assignable a, EResult right){

		if (a.isVariable()) {
			return assignVariable(a.getQualifiedName().toString(), right);		
		}
		else if(a.isSubscript()){
			EResult subs = a.getSubscript().accept(this);
			return assignSubscriptedVariable(a.getReceiver().getQualifiedName().toString(), subs, right);
		}
		return result(vf.bool(false)); //TODO void
	}
	
	@Override
	public EResult visitExpressionSubscript(Subscript x) {
		EResult subs = x.getSubscript().accept(this);
		org.meta_environment.rascal.ast.Expression expr = x.getExpression();
		int index = getValidIndex(subs);
		if(expr.isQualifiedName()){
			String name = expr.getQualifiedName().toString();
			Type elementType = checkValidListSubscription(name, subs, index);
			return result(((IList)environment.get(name).value).get(index));
		} else if(expr.isSubscript()){
			EResult r = expr.accept(this);
			Type elemenType = checkValidListSubscription(r, subs, index);
			return result(((IList) r.value).get(index));
		}
		return null;
	}

	@Override
	public EResult visitStatementAssignment(Assignment x) {
		Assignable a = x.getAssignable();
		org.meta_environment.rascal.ast.Assignment op = x.getOperator();
		EResult right = x.getExpression().accept(this);

		if (op.isDefault()) {
			return assign(a, right);
		}
		return result(vf.bool(false)); //TODO void
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
	
	// Expressions -----------------------------------------------------------

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
	public EResult visitExpressionNonEmptyBlock(NonEmptyBlock x) {
		EResult r = result(vf.bool(false)); //TODO void
		for(Statement stat : x.getStatements()){
			r = stat.accept(this);
		}
		return r;
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
	
    @Override
	public EResult visitExpressionAddition(Addition x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(vf.integer(((IInteger) left.value).getValue()
					+ ((IInteger) right.value).getValue()));
		} else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(vf.dubble(((IDouble) left.value).getValue()
					+ ((IDouble) right.value).getValue()));
		} else if (left.type.isStringType() && right.type.isStringType()) {
			return result(vf.string(((IString) left.value).getValue()
					+ ((IString) right.value).getValue()));
		} else if (left.type.isListType() && right.type.isListType()) {
			Type resultType = left.type.lub(right.type);
			return result(resultType, ((IList) left.value)
					.concat((IList) right.value));
		} else if (left.type.isSetType() && right.type.isSetType()) {
			Type resultType = left.type.lub(right.type);
			return result(resultType, ((ISet) left.value)
					.union((ISet) right.value));
		} else if (left.type.isMapType() && right.type.isMapType()) {
			notImplemented("+ on map");
		} else if (left.type.isRelationType() && right.type.isRelationType()) {
			Type resultType = left.type.lub(right.type);
			return result(resultType, ((ISet) left.value)
					.union((ISet) right.value));
		} else {
			throw new RascalTypeError("Operands of + have different types: "
					+ left.type + ", " + right.type);
		}
		return result(vf.bool(false));
	}
    
	public EResult visitExpressionSubtraction(Subtraction x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);

		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			return result(vf.integer(((IInteger) left.value).getValue()
					- ((IInteger) right.value).getValue()));
		} else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			return result(vf.dubble(((IDouble) left.value).getValue()
					- ((IDouble) right.value).getValue()));
		//} else if (left.type.isStringType() && right.type.isStringType()) {
		//	return result(vf.string(((IString) left.value).getValue()
		//			+ ((IString) right.value).getValue()));
		} else if (left.type.isListType() && right.type.isListType()) {
			notImplemented("- on list");
		} else if (left.type.isSetType() && right.type.isSetType()) {
			Type resultType = left.type.lub(right.type);
			return result(resultType, ((ISet) left.value)
					.subtract((ISet) right.value));
		} else if (left.type.isMapType() && right.type.isMapType()) {
			notImplemented("- on map");
		} else if (left.type.isRelationType() && right.type.isRelationType()) {
			Type resultType = left.type.lub(right.type);
			return result(resultType, ((ISet) left.value)
					.subtract((ISet) right.value));
		} else {
			throw new RascalTypeError("Operands of - have different types: "
					+ left.type + ", " + right.type);
		}
		return result(vf.bool(false));
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
	
	private boolean equals(EResult left, EResult right){
		if (left.type.isSubtypeOf(right.type)
				|| right.type.isSubtypeOf(left.type)) {
			return left.value.equals(right.value);
		} else {
			throw new RascalTypeError(
					"Operands of == should have equal types instead of: "
							+ left.type + ", " + right.type);
		}
	}

	@Override
	public EResult visitExpressionEquals(
			org.meta_environment.rascal.ast.Expression.Equals x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		
		return result(vf.bool(equals(left, right)));
	}

	@Override
	public EResult visitExpressionNonEquals(
			org.meta_environment.rascal.ast.Expression.NonEquals x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		
		return result(vf.bool(!equals(left, right)));
	}
	
	private int compare(EResult left, EResult right){
		if (left.type.isIntegerType() && right.type.isIntegerType()) {
			int l = ((IInteger) left.value).getValue();
			int r = ((IInteger) right.value).getValue();
			return l < r ? -1 : (l == r ? 0 : 1);
		} else if (left.type.isDoubleType() && right.type.isDoubleType()) {
			double l = ((IDouble) left.value).getValue();
			double r = ((IDouble) right.value).getValue();
			return l < r ? -1 : (l == r ? 0 : 1);
		} else if (left.type.isStringType() && right.type.isStringType()) {
			return ((IString) left.value).getValue().compareTo(
					((IString) right.value).getValue());
		} else if (left.type.isListType() && right.type.isListType()) {
			notImplemented("< on list");
		} else if (left.type.isSetType() && right.type.isSetType()) {
			notImplemented("< on set");
		} else if (left.type.isMapType() && right.type.isMapType()) {
			notImplemented("< on map");
		} else if (left.type.isRelationType() && right.type.isRelationType()) {
			notImplemented("< on relation");
		} else {
			throw new RascalTypeError("Operands of comparison have different types: "
					+ left.type + ", " + right.type);
		}
		return 0;
	}
	
	@Override
	public EResult visitExpressionLessThan(LessThan x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) < 0));
	}
	
	@Override
	public EResult visitExpressionLessThanOrEq(LessThanOrEq x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) <= 0));
	}
	@Override
	public EResult visitExpressionGreaterThan(GreaterThan x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) > 0));
	}
	
	@Override
	public EResult visitExpressionGreaterThanOrEq(GreaterThanOrEq x) {
		EResult left = x.getLhs().accept(this);
		EResult right = x.getRhs().accept(this);
		
		return result(vf.bool(compare(left, right) >= 0));
	}
	
	// Comprehensions ----------------------------------------------------
	
	@Override
	public EResult visitExpressionComprehension(Comprehension x) {
		return x.getComprehension().accept(this);
	}
	
	private class GeneratorEvaluator {
		private boolean isValueProducer;
		private boolean firstTime = true;
		private org.meta_environment.rascal.ast.Expression expr;
		private org.meta_environment.rascal.ast.Expression pat;
		private org.meta_environment.rascal.ast.Expression patexpr;
		private Evaluator evaluator;
		private IList listvalue;
		private int current = 0;

		GeneratorEvaluator(Generator g, Evaluator ev){
			evaluator = ev;
			if(g.isProducer()){
				isValueProducer = true;
				ValueProducer vp = g.getProducer();
				pat = vp.getPattern();
				patexpr = vp.getExpression();
				EResult r = patexpr.accept(ev);
				if(r.type.isListType()){
					listvalue = (IList) r.value;
				} else {
					throw new RascalTypeError("expression in generator should be of type list");
				}
			} else {
				isValueProducer = false;
				expr = g.getExpression();
			}
		}
		
		public boolean match(org.meta_environment.rascal.ast.Expression p, IValue v){
			if(p.isQualifiedName()){
				evaluator.assignVariable(p.getQualifiedName().toString(), result(v));
				return true;
			}
			throw new RascalTypeError("unimplemented pattern in match");
		}

		public boolean getNext(){
			if(isValueProducer){
				 while(current < listvalue.length()) {
					if(match(pat, listvalue.get(current))){
						current++;
						return true;
					}
					current++;
				}
				return false;
			} else {
				if(firstTime){
					/* Evaluate expression only once */
					firstTime = false;
					EResult v = expr.accept(evaluator);
					if(v.type.isBoolType()){
						return v.value.equals(vf.bool(true));
					} else {
						throw new RascalTypeError("Expression as generator should have type bool");
					}
				} else {
					return false;
				}
			}
		}
	}
	
	@Override
	public EResult visitComprehensionList(
			org.meta_environment.rascal.ast.Comprehension.List x) {
		org.meta_environment.rascal.ast.Expression resultExpr = x.getResult();
		java.util.List<Generator> generators = x.getGenerators();
		int size = generators.size();
		GeneratorEvaluator[] gens = new GeneratorEvaluator[size];
		IList res = null;
		Type elementType = tf.valueType();
		
		int i = 0;
		gens[0] = new GeneratorEvaluator(generators.get(0), this);
		while(i >= 0 && i < size){		
			if(gens[i].getNext()){
				if(i == size - 1){
					EResult r = resultExpr.accept(this);
					if(res == null){
						res = vf.list(r.value);
						elementType = r.type;
					} else {
						if(r.type.isSubtypeOf(elementType)){
							res = res.append(r.value);
						} else {
							throw new RascalTypeError("Cannot add value of type " + r.type + " to comprehension with element type " + elementType);
						}
					}
				} else {
					i++;
					gens[i] = new GeneratorEvaluator(generators.get(i), this);
				}
			} else {
				i--;
			}
		}
		return result((res == null) ? vf.list() : res);
	}
	
}
