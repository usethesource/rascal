package org.meta_environment.rascal.interpreter.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.impl.fast.ValueFactory;

public class VisitableRelationNode extends AbstractVisitable {

	private IRelation relation;

	public VisitableRelationNode(IRelation root, IValue value, List<Visitable> children) {
		super(value, children);
		this.relation = root;
	}

	@Override
	public Visitable set(int i, Visitable newChild)
			throws IndexOutOfBoundsException {
		if (i >= arity()) throw new IndexOutOfBoundsException();
		relation = relation.delete(ValueFactory.getInstance().tuple(getValue(),get(i).getValue()));
		children.set(i,newChild);
		relation = relation.insert(ValueFactory.getInstance().tuple(getValue(),get(i).getValue()));
		return this;
	}
	
	
	public static VisitableRelationNode makeVisitableRelationNode(IRelation relation, IValue node) {
		HashMap<IValue, LinkedList<IValue>> adjacencies = computeAdjacencies(relation);
		List<Visitable> successors = new ArrayList<Visitable>();
		for (IValue s: adjacencies.get(node)) {
			successors.add(makeVisitableRelationNode(relation, s));
		}
		return new VisitableRelationNode(relation, node, successors);
	}
	
	private static HashMap<IValue, LinkedList<IValue>> computeAdjacencies(IRelation relation) {
		HashMap<IValue, LinkedList<IValue>> adjacencies = new HashMap<IValue, LinkedList<IValue>> ();
		for(IValue v : relation){
			ITuple tup = (ITuple) v;
			IValue from = tup.get(0);
			IValue to = tup.get(1);
			LinkedList<IValue> children = adjacencies.get(from);
			if(children == null)
				children = new LinkedList<IValue>();
			children.add(to);
			adjacencies.put(from, children);
		}  
		return adjacencies;
	}
	
	public IRelation getRelation() {
		return relation;
	}

}
