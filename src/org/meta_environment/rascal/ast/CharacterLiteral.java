package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class CharacterLiteral extends AbstractAST {
	static public class Ambiguity extends CharacterLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCharacterLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends CharacterLiteral {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCharacterLiteralLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}