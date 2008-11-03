package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class NoElseMayFollow extends AbstractAST {
	static public class Ambiguity extends NoElseMayFollow {
		private final java.util.List<org.meta_environment.rascal.ast.NoElseMayFollow> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.NoElseMayFollow> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.NoElseMayFollow> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends NoElseMayFollow {
		private String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		public String getString() {
			return string;
		}
	}
}
