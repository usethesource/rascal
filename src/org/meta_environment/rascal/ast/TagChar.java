package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class TagChar extends AbstractAST {
	static public class Ambiguity extends TagChar {
		private final java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTagCharAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.TagChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends TagChar {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTagCharLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}