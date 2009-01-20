package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class CommentChar extends AbstractAST {
	static public class Ambiguity extends CommentChar {
		private final java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.CommentChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCommentCharAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.CommentChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends CommentChar {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCommentCharLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}