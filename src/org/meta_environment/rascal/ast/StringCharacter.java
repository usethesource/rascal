package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class StringCharacter extends AbstractAST {
	static public class Ambiguity extends StringCharacter {
		private final java.util.List<org.meta_environment.rascal.ast.StringCharacter> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.StringCharacter> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.StringCharacter> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends StringCharacter {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		public String getString() {
			return string;
		}
	}
}
