package org.meta_environment.rascal.interpreter.strategy;

import java.net.URI;

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

public class VisitableFactory implements IValueFactory {


	private IValueFactory factory;

	public VisitableFactory(IValueFactory factory) {
		this.factory = factory;
	}


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


	public IBool bool(boolean value) {
		return bool(value);
	}


	public IConstructor constructor(Type constructor, IValue... children)
	throws FactTypeUseException {
		return new VisitableConstructor(factory.constructor(constructor, children));
	}


	public IConstructor constructor(Type constructor) {
		return new VisitableConstructor(factory.constructor(constructor));
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
		return new VisitableList(factory.list(elems));
	}


	public IList list(Type eltType) {
		return new VisitableList(factory.list(eltType));
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
		return factory.relation(elems);
	}


	public IRelation relation(Type tupleType) {
		return factory.relation(tupleType);
	}


	public IRelationWriter relationWriter(Type type) {
		return factory.relationWriter(type);
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
