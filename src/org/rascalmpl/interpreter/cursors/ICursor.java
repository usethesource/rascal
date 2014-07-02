package org.rascalmpl.interpreter.cursors;

import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IWrapped;

public interface ICursor extends IWrapped {
	IValue root();
	IValue getWrappedValue();
	Context getCtx();
	IValue up();
}
