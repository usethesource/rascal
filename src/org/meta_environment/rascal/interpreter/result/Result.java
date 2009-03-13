package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.meta_environment.ValueFactoryFactory;
import org.meta_environment.rascal.ast.AbstractAST;
import org.meta_environment.rascal.interpreter.asserts.ImplementationError;
import org.meta_environment.rascal.interpreter.staticErrors.UnexpectedTypeError;
import org.meta_environment.rascal.interpreter.staticErrors.UnsupportedOperationError;


public class Result  implements Iterator<Result>{
	protected Type type;          // The declared type of the Result
	
	public Type getType(){
		return type;
	}
	
	public void setType(Type t){
		type = t;
	}
	
	protected IValue value;        // The actual value of the Result
	
	public IValue getValue(){
		return value;
	}
	
	public void setValue(IValue v){
		value = v;
	}
	
	public Type getValueType(){
		//System.err.println("getValueType on Result(" + type + ", " + value +")");
		if(iterator != null)
			return TypeFactory.getInstance().boolType();
		else if(value != null)
			return value.getType();
		else
			return null;	
	}
	
	private boolean isPublic = false;
	
	protected Iterator<Result> iterator;     // An optional IValue iterator
	private Result last;          // The last IValue returned by the iterator

	protected Result() { }
	
	public Result(Type t, IValue v) {
		//System.err.println("Result(" + t + ", " + v + ")");
		type = t;
		value = v;
		if (value != null && !value.getType().isSubtypeOf(t)) {
			// TODO find an ast or a loc
			throw new UnexpectedTypeError(v.getType(), t, null);
		}
		iterator = null;
		last = null;
	}
	
	public Result(Iterator<Result> beval, boolean b){
		this(TypeFactory.getInstance().boolType(), ValueFactoryFactory.getValueFactory().bool(b));
		this.iterator = beval;
	}
	
	public Result(Iterator<Result> beval){
		this(beval, true);
	}
	
	
	public Type getDeclaredType(){
		return type;
	}
	
	public String toString() {
		return "EResult(" + type + ", " + value + ")";
	}
	
	public boolean isNormal() {
		return true;
	}
	
	public boolean isClosure() {
		return false;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean val) {
		isPublic = val;
	}
	
	public boolean isTrue(){
		if(iterator == null){
			if(type.isBoolType()){
				return ((IBool)value).getValue();
			} else {
				return false;
			}
		} else {
			return (last == null) || last.isTrue();
		}
	}
	
	public boolean hasNext(){
		return iterator != null && iterator.hasNext();
	}
	
	public Result next(){
		if(iterator == null){
			new ImplementationError("next called on Result with null iterator");
		}
		return last = iterator.next();
	}

	public void remove() {
		throw new ImplementationError("remove() not implemented for Result");
		
	}


	/*
	 * Double dispatch stuff.
	 */
	

	// TODO make these abstract
	protected AbstractResult addInteger(IntegerResult n) {
		throw new ImplementationError("NIY");
	}

	protected AbstractResult reverseSubtractInteger(IntegerResult integerResult) {
		return null;
	}

	public AbstractResult add(AbstractResult result) {
		// TODO throw type error
		return null;
	}

	public AbstractResult subtract(AbstractResult result) {
		// TODO throw type error
		return null;
	}

	public AbstractResult multiply(AbstractResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult multiplyInteger(IntegerResult integerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult reverseSubtractReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult multiplyReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	public AbstractResult divide(AbstractResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult reverseDivideReal(RealResult realResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult reverseDivideInteger(IntegerResult integerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addString(StringResult s) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addList(CollectionResult l) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addSet(SetResult s) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addRelation(RelationResult r) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addBool(BoolResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult subtractSet(SetResult s) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult illegalArguments(String op, AbstractAST ast, Type type1, Type type2) {
		throw new UnsupportedOperationError(op, type1, type2, ast);
	}

	protected void checkElementType(String op, AbstractAST ast, Type elemType) {
		if (!elemType.isSubtypeOf(getType().getElementType())) {
			illegalArguments(op, ast, getType(), elemType);
		}
	}

	protected AbstractResult multiplySet(SetResult setResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addMap(MapResult m) {
		// TODO Auto-generated method stub
		return null;
	}




}


