package org.rascalmpl.cursors;

import org.eclipse.imp.pdb.facts.IValue;

public interface ICursor {
	IValue root();
	Context getCtx();
	IValue up();
}
