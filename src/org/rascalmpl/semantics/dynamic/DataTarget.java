package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Name;

public abstract class DataTarget extends org.rascalmpl.ast.DataTarget {

	public DataTarget(INode __param1) {
		super(__param1);
	}

	static public class Labeled extends org.rascalmpl.ast.DataTarget.Labeled {

		public Labeled(INode __param1, Name __param2) {
			super(__param1, __param2);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.DataTarget.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.DataTarget> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Empty extends org.rascalmpl.ast.DataTarget.Empty {

		public Empty(INode __param1) {
			super(__param1);
		}


	}
}
