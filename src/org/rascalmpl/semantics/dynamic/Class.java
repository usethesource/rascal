package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;
import org.rascalmpl.ast.NullASTVisitor;
import org.rascalmpl.ast.Range;

public abstract class Class extends org.rascalmpl.ast.Class {

	public Class(INode __param1) {
		super(__param1);
	}

	static public class SimpleCharclass extends org.rascalmpl.ast.Class.SimpleCharclass {

		public SimpleCharclass(INode __param1, List<Range> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Complement extends org.rascalmpl.ast.Class.Complement {

		public Complement(INode __param1, org.rascalmpl.ast.Class __param2) {
			super(__param1, __param2);
		}


	}

	static public class Intersection extends org.rascalmpl.ast.Class.Intersection {

		public Intersection(INode __param1, org.rascalmpl.ast.Class __param2, org.rascalmpl.ast.Class __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Bracket extends org.rascalmpl.ast.Class.Bracket {

		public Bracket(INode __param1, org.rascalmpl.ast.Class __param2) {
			super(__param1, __param2);
		}


	}

	static public class Union extends org.rascalmpl.ast.Class.Union {

		public Union(INode __param1, org.rascalmpl.ast.Class __param2, org.rascalmpl.ast.Class __param3) {
			super(__param1, __param2, __param3);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Class.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Class> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Difference extends org.rascalmpl.ast.Class.Difference {

		public Difference(INode __param1, org.rascalmpl.ast.Class __param2, org.rascalmpl.ast.Class __param3) {
			super(__param1, __param2, __param3);
		}


	}
}
