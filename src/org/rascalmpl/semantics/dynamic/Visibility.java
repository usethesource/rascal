package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;

public abstract class Visibility extends org.rascalmpl.ast.Visibility {

	public Visibility(INode __param1) {
		super(__param1);
	}

	static public class Private extends org.rascalmpl.ast.Visibility.Private {

		public Private(INode __param1) {
			super(__param1);
		}


	}

	static public class Default extends org.rascalmpl.ast.Visibility.Default {

		public Default(INode __param1) {
			super(__param1);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Visibility.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Visibility> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Public extends org.rascalmpl.ast.Visibility.Public {

		public Public(INode __param1) {
			super(__param1);
		}


	}
}
