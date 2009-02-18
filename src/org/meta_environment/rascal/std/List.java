package org.meta_environment.rascal.std;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;

public class List {

	public static IValue head(IList l) {
		return l.get(0);
	}
}
