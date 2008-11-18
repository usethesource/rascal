package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class TagString extends AbstractAST {
	static public class Ambiguity extends TagString {
		private final java.util.List<org.meta_environment.rascal.ast.TagString> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.TagString> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.TagString> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends TagString {
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
