package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class NamedRegExp extends AbstractAST {
	static public class Ambiguity extends NamedRegExp {
		private final java.util.List<org.meta_environment.rascal.ast.NamedRegExp> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.NamedRegExp> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNamedRegExpAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.NamedRegExp> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends NamedRegExp {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNamedRegExpLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}