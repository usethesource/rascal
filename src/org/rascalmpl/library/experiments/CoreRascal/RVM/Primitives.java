package org.rascalmpl.library.experiments.CoreRascal.RVM;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IValue;

public class Primitives {

	public static IValue subscript_list_int(IList lst, IInteger n){
		return lst.get(n.intValue());
	}

	public static IList addition_list_list(IList lst1, IList lst2) {
		return lst1.concat(lst2);
	}

	public static Object subscript_map(IMap map, IValue val) {
		return map.get(val);
	}

	public static Object subscript2(IValue iValue, IValue idx1, IValue idx2) {
		return null;
	}

}
