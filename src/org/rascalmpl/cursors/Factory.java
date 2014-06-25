package org.rascalmpl.cursors;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.imp.pdb.facts.IBool;
import org.eclipse.imp.pdb.facts.IDateTime;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.INumber;
import org.eclipse.imp.pdb.facts.IRational;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;

public class Factory {
	
	static class AtomCursor extends Cursor implements InvocationHandler {
		public AtomCursor(IValue value) {
			super(value);
		}
		
		public AtomCursor(IValue value, Context ctx) {
			super(value, ctx);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (method.getName().equals("up") || method.getName().equals("root") || method.getName().equals("getCtx")) {
				return method.invoke(this, args);
			}
			return method.invoke(getValue(), args);
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends IValue> T atomCursor(Class<T> cls, T value, Context ctx) {
		return (T) Proxy.newProxyInstance(Factory.class.getClassLoader(),new Class[]{cls, ICursor.class}, new AtomCursor(value, ctx));
	}
	
	public static IInteger integerCursor(IInteger value, Context ctx) {
		return atomCursor(IInteger.class, value, ctx);
	}

	public static IReal realCursor(IReal value, Context ctx) {
		return atomCursor(IReal.class, value, ctx);
	}

	public static IRational rationalCursor(IRational value, Context ctx) {
		return atomCursor(IRational.class, value, ctx);
	}
	
	public static IBool boolCursor(IBool value, Context ctx) {
		return atomCursor(IBool.class, value, ctx);
	}
	
	public static IString stringCursor(IString value, Context ctx) {
		return atomCursor(IString.class, value, ctx);
	}

	public static ISourceLocation sourceLocationCursor(ISourceLocation value, Context ctx) {
		return atomCursor(ISourceLocation.class, value, ctx);
	}

	public static IDateTime dateTimeCursor(IDateTime value, Context ctx) {
		return atomCursor(IDateTime.class, value, ctx);
	}

	public static INumber numberCursor(INumber value, Context ctx) {
		return atomCursor(INumber.class, value, ctx);
	}
	

}
