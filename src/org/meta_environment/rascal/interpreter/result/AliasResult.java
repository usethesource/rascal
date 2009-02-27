package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IValue;

public class AliasResult extends AbstractResult {

	private AbstractResult result;

	public AliasResult(AbstractResult result) {
		this.result = result;
	}
	
	@Override
	public IValue getValue() {
		return result.getValue();
	}
	
	
	/////// Alias result delegates to its aliasee
	
	public AbstractResult add(AbstractResult result) {
		return this.result.add(result);
	}

	public AbstractResult subtract(AbstractResult result) {
		return this.result.subtract(result);
	}

	public AbstractResult multiply(AbstractResult result) {
		return this.result.multiply(result);
	}
	
	public AbstractResult divide(AbstractResult result) {
		return this.result.divide(result);
	}

	public AbstractResult modulo(AbstractResult result) {
		return this.result.modulo(result);
	}

	public AbstractResult negative() {
		return this.result.negative();
	}

	public AbstractResult transitiveClosure() {
		return this.result.transitiveClosure();
	}

	public AbstractResult transitiveReflexiveClosure() {
		return this.result.transitiveReflexiveClosure();
	}

	public  AbstractResult in(AbstractResult result) {
		return this.result.in(result);
	}

	public AbstractResult notIn(AbstractResult result) {
		return this.result.notIn(result);
	}

	public AbstractResult intersect(AbstractResult result) {
		return this.result.intersect(result);
	}

	/////
	
	protected AbstractResult addInteger(IntegerResult n) {
		return result.addInteger(n);
	}

	protected AbstractResult subtractInteger(IntegerResult integerResult) {
		return result.subtractInteger(integerResult);
	}

	protected AbstractResult multiplyInteger(IntegerResult integerResult) {
		return result.multiplyInteger(integerResult);
	}

	protected AbstractResult divideInteger(IntegerResult integerResult) {
		return result.divideInteger(integerResult);
	}

	protected AbstractResult moduloInteger(IntegerResult integerResult) {
		return result.moduloInteger(integerResult);
	}

	protected AbstractResult addReal(RealResult n) {
		return result.addReal(n);
	}

	protected AbstractResult subtractReal(RealResult n) {
		return result.subtractReal(n);
	}

	protected AbstractResult multiplyReal(RealResult n) {
		return result.multiplyReal(n);
	}


	protected AbstractResult divideReal(RealResult realResult) {
		return result.divideReal(realResult);
	}


	protected AbstractResult moduloReal(RealResult realResult) {
		return result.moduloReal(realResult);
	}

	protected AbstractResult addString(StringResult s) {
		return result.addString(s);
	}

	protected AbstractResult addList(ListResult l) {
		return result.addList(l);
	}

	protected AbstractResult subtractList(ListResult s) {
		return result.subtractList(s);
	}

	protected AbstractResult inList(ListResult s) {
		return result.inList(s);
	}

	protected AbstractResult notInList(ListResult listResult) {
		return result.notInList(listResult);
	}

	protected AbstractResult addSet(SetResult s) {
		return result.addSet(s);
	}

	protected AbstractResult subtractSet(SetResult s) {
		return result.subtractSet(s);
	}
	
	
	protected AbstractResult multiplySet(SetResult setResult) {
		return result.multiplySet(setResult);
	}

	protected AbstractResult intersectSet(SetResult setResult) {
		return result.intersectSet(setResult);
	}

	protected AbstractResult inSet(SetResult s) {
		return result.inSet(s);
	}

	protected AbstractResult notInSet(SetResult setResult) {
		return result.notInSet(setResult);
	}

	protected AbstractResult addRelation(RelationResult r) {
		return result.addRelation(r);
	}

	protected AbstractResult subtractRelation(RelationResult relationResult) {
		return result.subtractRelation(relationResult);
	}

	protected AbstractResult addBool(BoolResult n) {
		return result.addBool(n);
	}


	protected AbstractResult addMap(MapResult m) {
		return result.addMap(m);
	}

	protected AbstractResult addTuple(TupleResult t) {
		return result.addTuple(t);
	}
	

}
