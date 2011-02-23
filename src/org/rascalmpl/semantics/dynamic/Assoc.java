package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;

public abstract class Assoc extends org.rascalmpl.ast.Assoc {

	public Assoc(INode __param1) {
		super(__param1);
	}

	static public class Ambiguity extends org.rascalmpl.ast.Assoc.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Assoc> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Right extends org.rascalmpl.ast.Assoc.Right {

		public Right(INode __param1) {
			super(__param1);
		}


	}

	static public class Associative extends org.rascalmpl.ast.Assoc.Associative {

		public Associative(INode __param1) {
			super(__param1);
		}


	}

	static public class NonAssociative extends org.rascalmpl.ast.Assoc.NonAssociative {

		public NonAssociative(INode __param1) {
			super(__param1);
		}


	}

	static public class Left extends org.rascalmpl.ast.Assoc.Left {

		public Left(INode __param1) {
			super(__param1);
		}


	}
}
