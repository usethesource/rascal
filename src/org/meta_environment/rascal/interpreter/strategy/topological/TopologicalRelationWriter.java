package org.meta_environment.rascal.interpreter.strategy.topological;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;

public class TopologicalRelationWriter implements IRelationWriter {
	
	private IRelationWriter writer;
	private RelationContext context;
	
	public TopologicalRelationWriter(RelationContext context, IRelationWriter writer) {
		this.context = context;
		this.writer = writer;
	}

	public void delete(IValue v) {
		writer.delete(v);
	}

	public IRelation done() {
		return new TopologicalVisitableRelation(context, writer.done());
	}

	public void insert(IValue... v) throws FactTypeUseException {
		writer.insert(v);
	}

	public void insertAll(Iterable<IValue> collection)
			throws FactTypeUseException {
		writer.insertAll(collection);
	}

	public int size() {
		return writer.size();
	}

	

}
