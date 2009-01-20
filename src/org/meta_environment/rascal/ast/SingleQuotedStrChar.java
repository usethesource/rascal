package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class SingleQuotedStrChar extends AbstractAST {
	static public class Ambiguity extends SingleQuotedStrChar {
		private final java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSingleQuotedStrCharAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends SingleQuotedStrChar {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSingleQuotedStrCharLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}