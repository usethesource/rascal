package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IValue;

public interface ICursor extends IValue {
	IValue root();
	IValue getWrappedValue();
	Context getCtx();
	IValue up();
}
