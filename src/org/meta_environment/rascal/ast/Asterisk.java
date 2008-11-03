package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Asterisk extends AbstractAST {
	static public class Ambiguity extends Asterisk {
		private final java.util.List<org.meta_environment.rascal.ast.Asterisk> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Asterisk> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Asterisk> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends Asterisk {
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
