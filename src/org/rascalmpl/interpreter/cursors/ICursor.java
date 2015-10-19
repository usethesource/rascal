package org.rascalmpl.interpreter.cursors;

import org.rascalmpl.value.IValue;

public interface ICursor extends IValue {
	IValue root();
	IValue getWrappedValue();
	Context getCtx();
	IValue up();
}
