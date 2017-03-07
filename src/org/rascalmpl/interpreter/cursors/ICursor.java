package org.rascalmpl.interpreter.cursors;

import io.usethesource.vallang.IValue;

public interface ICursor extends IValue {
	IValue root();
	IValue getWrappedValue();
	Context getCtx();
	IValue up();
}
