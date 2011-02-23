package org.rascalmpl.semantics.dynamic;

import java.util.List;
import org.eclipse.imp.pdb.facts.INode;

public abstract class Kind extends org.rascalmpl.ast.Kind {

	public Kind(INode __param1) {
		super(__param1);
	}

	static public class Data extends org.rascalmpl.ast.Kind.Data {

		public Data(INode __param1) {
			super(__param1);
		}


	}

	static public class Tag extends org.rascalmpl.ast.Kind.Tag {

		public Tag(INode __param1) {
			super(__param1);
		}


	}

	static public class Function extends org.rascalmpl.ast.Kind.Function {

		public Function(INode __param1) {
			super(__param1);
		}


	}

	static public class Anno extends org.rascalmpl.ast.Kind.Anno {

		public Anno(INode __param1) {
			super(__param1);
		}


	}

	static public class Ambiguity extends org.rascalmpl.ast.Kind.Ambiguity {

		public Ambiguity(INode __param1, List<org.rascalmpl.ast.Kind> __param2) {
			super(__param1, __param2);
		}


	}

	static public class Variable extends org.rascalmpl.ast.Kind.Variable {

		public Variable(INode __param1) {
			super(__param1);
		}


	}

	static public class Module extends org.rascalmpl.ast.Kind.Module {

		public Module(INode __param1) {
			super(__param1);
		}


	}

	static public class Rule extends org.rascalmpl.ast.Kind.Rule {

		public Rule(INode __param1) {
			super(__param1);
		}


	}

	static public class All extends org.rascalmpl.ast.Kind.All {

		public All(INode __param1) {
			super(__param1);
		}


	}

	static public class Alias extends org.rascalmpl.ast.Kind.Alias {

		public Alias(INode __param1) {
			super(__param1);
		}


	}

	static public class View extends org.rascalmpl.ast.Kind.View {

		public View(INode __param1) {
			super(__param1);
		}


	}
}
