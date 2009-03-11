package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Comment extends AbstractAST {
	static public class Lexical extends Comment {
		private String string;

		/* package */Lexical(INode node, String string) {
			this.node = node;
			this.string = string;
		}

		public String getString() {
			return string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCommentLexical(this);
		}
	}

	static public class Ambiguity extends Comment {
		private final java.util.List<org.meta_environment.rascal.ast.Comment> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Comment> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Comment> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCommentAmbiguity(this);
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}