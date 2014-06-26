package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IValue;

public interface ICursor {
	IValue root();
	IValue getWrappedValue();
	Context getCtx();
	IValue up();
}
