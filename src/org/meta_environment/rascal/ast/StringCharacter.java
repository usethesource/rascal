package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class StringCharacter extends AbstractAST {
	static public class Ambiguity extends StringCharacter {
		private final java.util.List<org.meta_environment.rascal.ast.StringCharacter> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.StringCharacter> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStringCharacterAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.StringCharacter> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends StringCharacter {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStringCharacterLexical(this);
		}

		public String getString() {
			return string;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}