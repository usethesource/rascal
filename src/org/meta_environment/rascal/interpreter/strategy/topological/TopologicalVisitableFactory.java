package org.meta_environment.rascal.interpreter.strategy.topological;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IExternalValue;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IListWriter;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IMapWriter;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IRelationWriter;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ISetWriter;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.type.Type;

public class TopologicalVisitableFactory implements IValueFactory {

	private IValueFactory factory;
	private RelationContext context;

	public TopologicalVisitableFactory(RelationContext context, IValueFactory factory) {
		this.factory = factory;
		this.context = context;
	}

	public static TopologicalVisitable<?> makeTopologicalVisitable(RelationContext context, IValue iValue) {
		if (iValue instanceof TopologicalVisitable<?>) {
			return (TopologicalVisitable<?>) iValue;
		} else if (context.getRelation().equals(iValue)) {
			// special case for the root of the context
			IRelation relation = (IRelation) iValue;
			return new TopologicalVisitableRelation(context, relation, computeRoots(context, relation));
		} else {
			HashMap<IValue, LinkedList<IValue>> adjacencies = computeAdjacencies(context.getRelation());
			List<TopologicalVisitable<?>> successors = new ArrayList<TopologicalVisitable<?>>();
			if (adjacencies.get(iValue) != null) {
				for (IValue s: adjacencies.get(iValue)) {
					successors.add(makeTopologicalVisitable(context, s));
				}
			}
			if (iValue instanceof IConstructor) {
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
		}
		return null;
	}


	private static List<TopologicalVisitable<?>> computeRoots(RelationContext context, IRelation relation) {
		ISet roots = relation.domain().subtract(relation.range());
		List<TopologicalVisitable<?>> res = new ArrayList<TopologicalVisitable<?>>();
		for (IValue v: roots) {
			res.add(makeTopologicalVisitable(context, v));
		}
		return res;
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


	public IBool bool(boolean value) {
		return factory.bool(value);
	}


	public IConstructor constructor(Type constructor, IValue... children)
	throws FactTypeUseException {
		return (IConstructor) context.getCurrentNode().setValue(factory.constructor(constructor, children));
	}


	public IConstructor constructor(Type constructor) {
		return (IConstructor) context.getCurrentNode().setValue(factory.constructor(constructor));
	}


	public IInteger integer(byte[] a) {
		return factory.integer(a);
	}


	public IInteger integer(int i) {
		return factory.integer(i);
	}


	public IInteger integer(String i) throws NumberFormatException {
		return factory.integer(i);
	}


	public IList list(IValue... elems) {
		return new TopologicalVisitableList(context, factory.list(elems));
	}


	public IList list(Type eltType) {
		return new TopologicalVisitableList(context, factory.list(eltType));
	}


	public IListWriter listWriter(Type eltType) {
		return factory.listWriter(eltType);
	}


	public IMap map(Type key, Type value) {
		return factory.map(key, value);
	}


	public IMapWriter mapWriter(Type key, Type value) {
		return factory.mapWriter(key, value);
	}


	public INode node(String name, IValue... children) {
		return factory.node(name, children);
	}


	public INode node(String name) {
		return factory.node(name);
	}


	public IReal real(double d) {
		return factory.real(d);
	}


	public IReal real(String s) throws NumberFormatException {
		return factory.real(s);
	}


	public IRelation relation(IValue... elems) {
		return new TopologicalVisitableRelation(context, factory.relation(elems));
	}


	public IRelation relation(Type tupleType) {
		return new TopologicalVisitableRelation(context, factory.relation(tupleType));
	}


	public IRelationWriter relationWriter(Type type) {
		return new TopologicalRelationWriter(context, factory.relationWriter(type));
	}


	public ISet set(IValue... elems) {
		return factory.set(elems);
	}


	public ISet set(Type eltType) {
		return factory.set(eltType);
	}


	public ISetWriter setWriter(Type eltType) {
		return factory.setWriter(eltType);
	}


	public ISourceLocation sourceLocation(String path, int offset, int length,
			int beginLine, int endLine, int beginCol, int endCol) {
		return factory.sourceLocation(path, offset, length, beginLine, endLine,
				beginCol, endCol);
	}


	public ISourceLocation sourceLocation(String path) {
		return factory.sourceLocation(path);
	}


	public ISourceLocation sourceLocation(URI uri, int offset, int length,
			int beginLine, int endLine, int beginCol, int endCol) {
		return factory.sourceLocation(uri, offset, length, beginLine, endLine,
				beginCol, endCol);
	}


	public ISourceLocation sourceLocation(URI uri) {
		return factory.sourceLocation(uri);
	}


	public IString string(String s) {
		return factory.string(s);
	}


	public ITuple tuple() {
		return factory.tuple();
	}


	public ITuple tuple(IValue... args) {
		return factory.tuple(args);
	}


}
