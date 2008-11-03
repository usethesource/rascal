package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class SingleQuotedStrChar extends AbstractAST {
	static public class Ambiguity extends SingleQuotedStrChar {
		private final java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends SingleQuotedStrChar {
		/* "\\n" -> SingleQuotedStrChar */
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
