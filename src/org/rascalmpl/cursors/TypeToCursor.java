package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.INode;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.ITypeVisitor;
import org.eclipse.imp.pdb.facts.type.Type;

import static org.rascalmpl.cursors.Factory.*;

public class TypeToCursor implements ITypeVisitor<IValue, RuntimeException> {

	public static IValue makeCursor(IValue value, Context ctx) {
		TypeToCursor t2c = new TypeToCursor(value, ctx);
		return value.getType().accept(t2c);
	}
	
	
	private IValue value;
	private Context ctx;

	private TypeToCursor(IValue value, Context ctx) {
		this.value = value;
		this.ctx = ctx;
	}
	
	@Override
	public IValue visitReal(Type type) throws RuntimeException {
		return realCursor((IReal)value, ctx);
	}

	@Override
	public IValue visitInteger(Type type) throws RuntimeException {
		return integerCursor((IInteger)value, ctx);
	}

	@Override
	public IValue visitRational(Type type) throws RuntimeException {
		return rationalCursor((IRational)value, ctx);
	}

	@Override
	public IValue visitList(Type type) throws RuntimeException {
		return new ListCursor((IList) value, ctx);
	}

	@Override
	public IValue visitMap(Type type) throws RuntimeException {
		return new MapCursor((IMap)value, ctx);
	}

	@Override
	public IValue visitNumber(Type type) throws RuntimeException {
		return numberCursor((INumber)value, ctx);
	}

	@Override
	public IValue visitAlias(Type type) throws RuntimeException {
		return type.getAliased().accept(this);
	}

	@Override
	public IValue visitSet(Type type) throws RuntimeException {
		return value;
	}

	@Override
	public IValue visitSourceLocation(Type type) throws RuntimeException {
		return sourceLocationCursor((ISourceLocation)value, ctx);
	}

	@Override
	public IValue visitString(Type type) throws RuntimeException {
		return stringCursor((IString)value, ctx);
	}

	@Override
	public IValue visitNode(Type type) throws RuntimeException {
		return new NodeCursor((INode)value, ctx);
	}

	@Override
	public IValue visitConstructor(Type type) throws RuntimeException {
		return new ConstructorCursor((IConstructor)value, ctx);
	}

	@Override
	public IValue visitAbstractData(Type type) throws RuntimeException {
		return new ConstructorCursor((IConstructor)value, ctx);
	}

	@Override
	public IValue visitTuple(Type type) throws RuntimeException {
		return value;
	}

	@Override
	public IValue visitValue(Type type) throws RuntimeException {
		return value;
	}

	@Override
	public IValue visitVoid(Type type) throws RuntimeException {
		return value;
	}

	@Override
	public IValue visitBool(Type type) throws RuntimeException {
		return boolCursor((IBool)value, ctx);
	}

	@Override
	public IValue visitParameter(Type type) throws RuntimeException {
		return value;
	}

	@Override
	public IValue visitExternal(Type type) throws RuntimeException {
		return value;
	}

	@Override
	public IValue visitDateTime(Type type) throws RuntimeException {
		return dateTimeCursor((IDateTime)value, ctx);
	}

}
