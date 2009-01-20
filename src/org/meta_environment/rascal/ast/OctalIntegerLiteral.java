package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class OctalIntegerLiteral extends AbstractAST {
	static public class Ambiguity extends OctalIntegerLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitOctalIntegerLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.OctalIntegerLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends OctalIntegerLiteral {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitOctalIntegerLiteralLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}