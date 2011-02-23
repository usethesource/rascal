package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.Expression;

public abstract class Bound extends org.rascalmpl.ast.Bound {

	public Bound(INode __param1) {
		super(__param1);
	}

	static public class Empty extends org.rascalmpl.ast.Bound.Empty {

		public Empty(INode __param1) {
			super(__param1);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Bound.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Bound> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Default extends org.rascalmpl.ast.Bound.Default {

		public Default(INode __param1, Expression __param2) {
			super(__param1, __param2);
		}


	}
}
