package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class StrChar extends AbstractAST {
	static public class Ambiguity extends StrChar {
		private final java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStrCharAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.StrChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends StrChar {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStrCharLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	static public class newline extends StrChar {
		/* package */newline(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitStrCharnewline(this);
		}

		@Override
		public boolean isnewline() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isnewline() {
		return false;
	}
}