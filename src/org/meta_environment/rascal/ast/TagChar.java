package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class TagChar extends AbstractAST {
	static public class Lexical extends TagChar {
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
			return v.visitTagCharLexical(this);
		}
	}

	static public class Ambiguity extends TagChar {
		private final java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.TagChar> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTagCharAmbiguity(this);
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}