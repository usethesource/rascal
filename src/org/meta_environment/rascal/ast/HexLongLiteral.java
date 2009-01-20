package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class HexLongLiteral extends AbstractAST {
	static public class Ambiguity extends HexLongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitHexLongLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends HexLongLiteral {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitHexLongLiteralLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}