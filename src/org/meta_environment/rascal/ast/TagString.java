package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class TagString extends AbstractAST {
	static public class Ambiguity extends TagString {
		private final java.util.List<org.meta_environment.rascal.ast.TagString> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.TagString> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTagStringAmbiguity(this);
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

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTagStringLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}