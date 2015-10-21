package org.rascalmpl.value.impl.fast;

import org.rascalmpl.value.IList;
import org.rascalmpl.value.IListRelation;

public class RelationViewOnList implements IListRelation<IList> {

	protected final IList rel1;
	
	public RelationViewOnList(IList rel1) {
		this.rel1 = rel1;
	}

	@Override
	public IList compose(IListRelation<IList> rel2) {
		return RelationalFunctionsOnList.compose(rel1, rel2.asList());
	}

	@Override
	public IList closure() {
		return RelationalFunctionsOnList.closure(rel1);
	}

	@Override
	public IList closureStar() {
		return RelationalFunctionsOnList.closureStar(rel1);
	}
	
	@Override
	public int arity() {
		return rel1.getElementType().getArity();
	}	
	
	@Override
	public IList project(int... fieldIndexes) {
		return RelationalFunctionsOnList.project(rel1, fieldIndexes);
	}

	@Override
	public IList projectByFieldNames(String... fieldsNames) {
		return RelationalFunctionsOnList.projectByFieldNames(rel1, fieldsNames);
	}

	@Override
	public IList carrier() {
		return RelationalFunctionsOnList.carrier(rel1);
	}

	@Override
	public IList domain() {
		return RelationalFunctionsOnList.domain(rel1);
	}

	@Override
	public IList range() {
		return RelationalFunctionsOnList.range(rel1);
	}

	@Override
	public IList asList() {
		return rel1;
	}

	@Override
	public String toString() {
		return rel1.toString();
	}	
	
}
