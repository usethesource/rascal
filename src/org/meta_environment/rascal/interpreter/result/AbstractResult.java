package org.meta_environment.rascal.interpreter.result;

import java.util.Iterator;

import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.errors.ImplementationError;

public abstract class AbstractResult implements Iterator<AbstractResult> {
	private Iterator<AbstractResult> iterator = null;
	private AbstractResult last = null;

	protected AbstractResult(Iterator<AbstractResult> iter) {
		this.iterator = iter;
	}
	
	protected AbstractResult() {

	}

	public abstract IValue getValue();
	
	
	//////// The iterator interface
	
	
	public boolean hasNext(){
		return iterator != null && iterator.hasNext();
	}
	
	public AbstractResult next(){
		if(iterator == null){
			new ImplementationError("next called on Result with null iterator");
		}
		return last = iterator.next();
	}

	public void remove() {
		throw new ImplementationError("remove() not implemented for (iterable) result");		
	}

	
	
	///////
	
	public AbstractResult add(AbstractResult result) {
		return null;
	}

	public AbstractResult subtract(AbstractResult result) {
		return null;
	}

	public AbstractResult multiply(AbstractResult result) {
		return null;
	}
	
	public AbstractResult divide(AbstractResult result) {
		return null;
	}


	
	////////////////

	protected AbstractResult addInteger(IntegerResult n) {
		throw new ImplementationError("NIY");
	}

	protected AbstractResult subtractInteger(IntegerResult integerResult) {
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

	protected AbstractResult subtractReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult multiplyReal(RealResult n) {
		// TODO Auto-generated method stub
		return null;
	}


	protected AbstractResult divideReal(RealResult realResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult divideInteger(IntegerResult integerResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addString(StringResult s) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addList(ListResult l) {
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


	protected AbstractResult multiplySet(SetResult setResult) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult addMap(MapResult m) {
		// TODO Auto-generated method stub
		return null;
	}

	protected AbstractResult subtractRelation(RelationResult relationResult) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
