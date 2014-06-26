package org.rascalmpl.library.util;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.type.Type;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.type.TypeStore;
import org.rascalmpl.interpreter.cursors.Context;
import org.rascalmpl.interpreter.cursors.CursorFactory;
import org.rascalmpl.interpreter.cursors.ICursor;
import org.rascalmpl.interpreter.cursors.InvertorContext;
import org.rascalmpl.interpreter.cursors.TopContext;
import org.rascalmpl.interpreter.result.ICallableValue;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class Cursors {

	
	private static final TypeStore cursors = new TypeStore();
	
	private static final TypeFactory tf = TypeFactory.getInstance();
	
	public static final Type Nav = tf.abstractDataType(cursors, "Nav");
	public static final Type Nav_field = tf.constructor(cursors, Nav, "field", tf.stringType(), "name");
	public static final Type Nav_subscript= tf.constructor(cursors, Nav, "subscript", tf.integerType(), "index");
	public static final Type Nav_lookup = tf.constructor(cursors, Nav, "lookup", tf.valueType(), "key");
	
	
	public static final Type Path = tf.aliasType(cursors, "Path", tf.listType(Nav));

	private final IValueFactory vf;
	
	public Cursors(IValueFactory vf) {
		this.vf = vf;
	}

	public IValue makeCursor(IValue v) {
		IValue c = CursorFactory.makeCursor(v, new TopContext());
		return c;
	}

	public IValue update(IValue cursor, IValue v) {
		checkCursorness("first", cursor);
		return CursorFactory.makeCursor(v, ((ICursor) cursor).getCtx());
	}

	public IValue compute(IValue cursor, IValue to, IValue from) {
		checkCursorness("first", cursor);
		checkUnaryFunction("second", to);
		checkUnaryFunction("third", from);
		ICursor c = (ICursor)cursor;
		Context ctx = new InvertorContext(c.getCtx(), (ICallableValue)from);
		ICallableValue f = (ICallableValue)to;
		IValue computed = f.call(new Type[] {c.getWrappedValue().getType()}, new IValue[] { c.getWrappedValue() }, null).getValue();
		return CursorFactory.makeCursor(computed, ctx);
	}

	

	public IValue getRoot(IValue typ, IValue cursor) {
		checkCursorness("first", cursor);
		return ((ICursor) cursor).root();
	}
	
	public IList toPath(IValue cursor) {
		checkCursorness("first", cursor);
		return ((ICursor)cursor).getCtx().toPath(vf);
	}

	private static void checkCursorness(String arg, IValue cursor) {
		if (!(cursor instanceof ICursor)) {
			throw RuntimeExceptionFactory.illegalArgument(cursor, null, null,
					arg + " argument should be a cursor");
		}
	}

	private static void checkUnaryFunction(String arg, IValue f) {
//		if (!(f instanceof ICallableValue) || ((ICallableValue)f).getArity() != 1) {
//			throw RuntimeExceptionFactory.illegalArgument(f, null, null,
//					arg + " argument should be a unary function");
//		}
	}
}
