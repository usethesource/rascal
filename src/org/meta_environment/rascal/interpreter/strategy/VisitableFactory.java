package org.meta_environment.rascal.interpreter.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.meta_environment.rascal.interpreter.strategy.topological.RelationContext;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitable;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitableConstructor;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitableList;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitableMap;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitableNode;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitableRelation;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitableSet;
import org.meta_environment.rascal.interpreter.strategy.topological.TopologicalVisitableTuple;

public class VisitableFactory {

	public static IVisitable makeVisitable(IValue iValue) {
		if (iValue instanceof IVisitable) {
			return (IVisitable) iValue;
		} else if (iValue instanceof IConstructor) {
			return new VisitableConstructor((IConstructor) iValue);
		} else if (iValue instanceof INode) {
			return new VisitableNode((INode) iValue);
		} else if (iValue instanceof ITuple) {
			return new VisitableTuple((ITuple) iValue);
		} else if (iValue instanceof IMap) {
			return new VisitableMap((IMap) iValue);
		} else if (iValue instanceof IRelation) {
			return new VisitableRelation((IRelation) iValue);
		} else if (iValue instanceof IList) {
			return new VisitableList((IList) iValue);
		} else if (iValue instanceof ISet) {
			return new VisitableSet((ISet) iValue);
		} else if (iValue instanceof ISourceLocation || iValue instanceof IExternalValue || iValue instanceof IBool || iValue instanceof IInteger || iValue instanceof ISourceLocation || iValue instanceof IReal || iValue instanceof IString) {
			return new VisitableConstant(iValue);
		}
		return null;
	}

	public static TopologicalVisitable<?> makeTopologicalVisitable(RelationContext context, IValue iValue) {
		HashMap<IValue, LinkedList<IValue>> adjacencies = computeAdjacencies(context.getRelation());
		List<TopologicalVisitable<?>> successors = new ArrayList<TopologicalVisitable<?>>();
		if (adjacencies.get(iValue) != null) {
			for (IValue s: adjacencies.get(iValue)) {
				successors.add(makeTopologicalVisitable(context, s));
			}
		}
		if (iValue instanceof TopologicalVisitable<?>) {
			return (TopologicalVisitable<?>) iValue;
		} else if (iValue instanceof IConstructor) {
			return new TopologicalVisitableConstructor(context, (IConstructor) iValue, successors);
		} else if (iValue instanceof INode) {
			return new TopologicalVisitableNode(context, (INode) iValue, successors);
		} else if (iValue instanceof ITuple) {
			return new TopologicalVisitableTuple(context, (ITuple) iValue, successors);
		} else if (iValue instanceof IMap) {
			return new TopologicalVisitableMap(context, (IMap) iValue, successors);
		} else if (iValue instanceof IRelation) {
			return new TopologicalVisitableRelation(context, (IRelation) iValue, successors);
		} else if (iValue instanceof IList) {
			return new TopologicalVisitableList(context, (IList) iValue, successors);
		} else if (iValue instanceof ISet) {
			return new TopologicalVisitableSet(context, (ISet) iValue, successors);
		} else if (iValue instanceof ISourceLocation || iValue instanceof IExternalValue || iValue instanceof IBool || iValue instanceof IInteger || iValue instanceof ISourceLocation || iValue instanceof IReal || iValue instanceof IString) {
			return new TopologicalVisitable<IValue>(context, iValue, successors);
		}
		return null;
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


}
