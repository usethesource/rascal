package org.rascalmpl.interpreter.cursors;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.rascalmpl.value.IBool;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IDateTime;
import org.rascalmpl.value.IExternalValue;
import org.rascalmpl.value.IInteger;
import org.rascalmpl.value.IList;
import org.rascalmpl.value.IMap;
import org.rascalmpl.value.INode;
import org.rascalmpl.value.INumber;
import org.rascalmpl.value.IRational;
import org.rascalmpl.value.IReal;
import org.rascalmpl.value.ISet;
import org.rascalmpl.value.ISourceLocation;
import org.rascalmpl.value.IString;
import org.rascalmpl.value.IValue;
import org.rascalmpl.value.type.ITypeVisitor;
import org.rascalmpl.value.type.Type;

public class CursorFactory implements ITypeVisitor<IValue, RuntimeException> {

	public static IValue makeCursor(IValue value, Context ctx) {
		CursorFactory fact = new CursorFactory(value, ctx);
		return value.getType().accept(fact);
	}
	
	private static class AtomCursor extends Cursor implements InvocationHandler {
		public AtomCursor(IValue value) {
			super(value);
		}
		
		public AtomCursor(IValue value, Context ctx) {
			super(value, ctx);
		}
		
		@Override
		public boolean equals(Object obj) {
			return getWrappedValue().equals(obj);
		}
		

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String name = method.getName();
			if (name.equals("up") || name.equals("root") || name.equals("getCtx") || name.equals("getWrappedValue") || name.equals("toString")) {
				return method.invoke(this, args);
			}
			return method.invoke(getWrappedValue(), args);
		}
	}
	
	
	private static IValue atomCursor(Class<? extends IValue> cls, IValue value, Context ctx) {
		return (IValue) Proxy.newProxyInstance(CursorFactory.class.getClassLoader(),
				new Class[]{cls, ICursor.class}, new AtomCursor(value, ctx));
	}
	
	
	private IValue value;
	private Context ctx;

	private CursorFactory(IValue value, Context ctx) {
		this.value = value;
		this.ctx = ctx;
	}
	
	@Override
	public IValue visitReal(Type type) throws RuntimeException {
		return atomCursor(IReal.class, value, ctx);
	}

	@Override
	public IValue visitInteger(Type type) throws RuntimeException {
		return atomCursor(IInteger.class, value, ctx);
	}

	@Override
	public IValue visitRational(Type type) throws RuntimeException {
		return atomCursor(IRational.class, value, ctx);
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
		return atomCursor(INumber.class, value, ctx);
	}

	@Override
	public IValue visitAlias(Type type) throws RuntimeException {
		return type.getAliased().accept(this);
	}

	@Override
	public IValue visitSet(Type type) throws RuntimeException {
		return new SetCursor((ISet) value, ctx);
	}

	@Override
	public IValue visitSourceLocation(Type type) throws RuntimeException {
		return atomCursor(ISourceLocation.class, value, ctx);
	}

	@Override
	public IValue visitString(Type type) throws RuntimeException {
		return atomCursor(IString.class, value, ctx);
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
		return new TupleCursor(value, ctx);
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
		return atomCursor(IBool.class, value, ctx);
	}

	@Override
	public IValue visitParameter(Type type) throws RuntimeException {
		return value;
	}

	@Override
	public IValue visitExternal(Type type) throws RuntimeException {
		return atomCursor(IExternalValue.class, value, ctx);
	}

	@Override
	public IValue visitDateTime(Type type) throws RuntimeException {
		return atomCursor(IDateTime.class, value, ctx);
	}

}
