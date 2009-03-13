package org.meta_environment.rascal.interpreter.result;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.meta_environment.rascal.interpreter.staticErrors.UndeclaredFieldError;


import static org.meta_environment.rascal.interpreter.result.ResultFactory.makeResult;

public class ConstructorResult extends NodeResult {

	public ConstructorResult(Type type, IConstructor cons) {
		super(type, cons);
	}
	
	@Override
	public IConstructor getValue() {
		return (IConstructor)super.getValue();
	}

	@Override
	public <U extends IValue> AbstractResult<U> fieldAccess(String name, TypeStore store) {
		Type nodeType = getValue().getConstructorType();
		if (!getType().hasField(name, store)) {
			// TODO: add ast or location to result
			throw new UndeclaredFieldError(name, getType(), null);
		}
		if (!getValueType().hasField(name)) {
			throw new UndeclaredFieldError(name, getValueType(), null);
		}				
		int index = nodeType.getFieldIndex(name);
		return makeResult(nodeType.getFieldType(index), getValue().get(index));
	}
	
	@Override
	public <U extends IValue, V extends IValue> AbstractResult<U> compare(AbstractResult<V> result) {
		return result.compareConstructor(this);
	}
	
	//
	
	
	@Override
	protected <U extends IValue> AbstractResult<U> compareConstructor(ConstructorResult that) {
		// Note reversed args
		INode left = that.getValue();
		INode right = this.getValue();
		return makeIntegerResult(compareNodes(left, right));
	}
	
	private int compareNodes(INode left, INode right) {
		// NOTE: left and right are in normal (non-reversed) order
		int compare = left.getName().compareTo(right.getName());
		if (compare != 0){
			return compare;
		}
		compare = new Integer(left.arity()).compareTo(right.arity());
		if (compare != 0) {
			return compare;
		}
		return compareChildren(left, right);
	}
	
	private int compareChildren(INode left, INode right) {
		// NOTE: left and right are in normal (non-reversed) order
		int i = 0;
		for (IValue leftKid: left.getChildren()) {
			IValue rightKid = right.get(i);
			int compare = compareValues(leftKid, rightKid);
			if (compare != 0) {
				return compare;
			}
			i++;
		}
		return 0;
	}
	
}
