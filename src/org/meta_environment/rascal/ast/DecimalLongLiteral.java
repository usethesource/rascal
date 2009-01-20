package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class DecimalLongLiteral extends AbstractAST {
	static public class Ambiguity extends DecimalLongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDecimalLongLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends DecimalLongLiteral {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDecimalLongLiteralLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}