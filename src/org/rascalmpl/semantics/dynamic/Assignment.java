package org.rascalmpl.semantics.dynamic;

import java.util.List;

import org.eclipse.imp.pdb.facts.ISourceLocation;

public abstract class Assignment extends org.rascalmpl.ast.Assignment {

	static public class Addition extends org.rascalmpl.ast.Assignment.Addition {

		public Addition(ISourceLocation __param1) {
			super(__param1);
		}

	}

	static public class Ambiguity extends
			org.rascalmpl.ast.Assignment.Ambiguity {

		public Ambiguity(ISourceLocation __param1,
				List<org.rascalmpl.ast.Assignment> __param2) {
			super(__param1, __param2);
		}

	}

	static public class Default extends org.rascalmpl.ast.Assignment.Default {

		public Default(ISourceLocation __param1) {
			super(__param1);
		}

	}

	static public class Division extends org.rascalmpl.ast.Assignment.Division {

		public Division(ISourceLocation __param1) {
			super(__param1);
		}

	}

	static public class IfDefined extends
			org.rascalmpl.ast.Assignment.IfDefined {

		public IfDefined(ISourceLocation __param1) {
			super(__param1);
		}

	}

	static public class Intersection extends
			org.rascalmpl.ast.Assignment.Intersection {

		public Intersection(ISourceLocation __param1) {
			super(__param1);
		}

	}

	static public class Product extends org.rascalmpl.ast.Assignment.Product {

		public Product(ISourceLocation __param1) {
			super(__param1);
		}

	}

	static public class Subtraction extends
			org.rascalmpl.ast.Assignment.Subtraction {

		public Subtraction(ISourceLocation __param1) {
			super(__param1);
		}

	}

	public Assignment(ISourceLocation __param1) {
		super(__param1);
	}
}
