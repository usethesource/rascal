package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class HexIntegerLiteral extends AbstractAST {
	static public class Ambiguity extends HexIntegerLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitHexIntegerLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends HexIntegerLiteral {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitHexIntegerLiteralLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}