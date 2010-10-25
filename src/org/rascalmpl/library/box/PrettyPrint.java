package org.rascalmpl.library.box;

import org.eclipse.imp.pdb.facts.ISourceLocation;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;

public class PrettyPrint {
	private final IValueFactory values;

	public PrettyPrint(IValueFactory values) {
		this.values = values;
	}
	
	public IValue toLatex(ISourceLocation q) {
		String s = new MakeBox().toPrint("toLatex", q.getURI());
		return values.string(s);
	}
	
}
