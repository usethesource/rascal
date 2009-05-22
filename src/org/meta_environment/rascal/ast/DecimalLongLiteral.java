package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class DecimalLongLiteral extends AbstractAST {
	static public class Lexical extends DecimalLongLiteral {
		private String string;

		public Lexical(INode node, String string) {
			this.node = node;
			this.string = string;
		}

		public String getString() {
			return string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDecimalLongLiteralLexical(this);
		}
	}

	static public class Ambiguity extends DecimalLongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDecimalLongLiteralAmbiguity(this);
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}