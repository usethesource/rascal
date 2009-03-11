package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class CharacterLiteral extends AbstractAST {
	static public class Lexical extends CharacterLiteral {
		private String string;

		/* package */Lexical(INode node, String string) {
			this.node = node;
			this.string = string;
		}

		public String getString() {
			return string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCharacterLiteralLexical(this);
		}
	}

	static public class Ambiguity extends CharacterLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCharacterLiteralAmbiguity(this);
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}