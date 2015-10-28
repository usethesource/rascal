package org.rascalmpl.value.impl.fast;

import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISetRelation;

public class RelationViewOnSet implements ISetRelation<ISet> {

	protected final ISet rel1;
	
	public RelationViewOnSet(ISet rel1) {
		this.rel1 = rel1;
	}
	
	@Override
	public ISet compose(ISetRelation<ISet> rel2) {
		return RelationalFunctionsOnSet.compose(rel1, rel2.asSet());
	}

	@Override
	public ISet closure() {
		return RelationalFunctionsOnSet.closure(rel1);
	}

	@Override
	public ISet closureStar() {
		return RelationalFunctionsOnSet.closureStar(rel1);
	}
	
	@Override
	public int arity() {
		return rel1.getElementType().getArity();
	}	
	
	@Override
	public ISet project(int... fieldIndexes) {
		return RelationalFunctionsOnSet.project(rel1, fieldIndexes);
	}

	@Override
	public ISet projectByFieldNames(String... fieldsNames) {
		return RelationalFunctionsOnSet.projectByFieldNames(rel1, fieldsNames);
	}

	@Override
	public ISet carrier() {
		return RelationalFunctionsOnSet.carrier(rel1);
	}

	@Override
	public ISet domain() {
		return RelationalFunctionsOnSet.domain(rel1);
	}

	@Override
	public ISet range() {
		return RelationalFunctionsOnSet.range(rel1);
	}

	@Override
	public ISet asSet() {
		return rel1;
	}
	
	@Override
	public String toString() {
		return rel1.toString();
	}

}
