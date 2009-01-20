package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class RealLiteral extends AbstractAST {
	static public class Ambiguity extends RealLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.RealLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.RealLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRealLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.RealLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends RealLiteral {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRealLiteralLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}