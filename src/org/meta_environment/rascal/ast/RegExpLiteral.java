package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class RegExpLiteral extends AbstractAST {
	static public class Ambiguity extends RegExpLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.RegExpLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.RegExpLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRegExpLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.RegExpLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends RegExpLiteral {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRegExpLiteralLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}