package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class NumChar extends AbstractAST {
	static public class Ambiguity extends NumChar {
		private final java.util.List<org.meta_environment.rascal.ast.NumChar> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.NumChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNumCharAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.NumChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends NumChar {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNumCharLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}