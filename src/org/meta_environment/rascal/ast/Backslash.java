package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Backslash extends AbstractAST {
	static public class Ambiguity extends Backslash {
		private final java.util.List<org.meta_environment.rascal.ast.Backslash> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Backslash> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitBackslashAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Backslash> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends Backslash {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitBackslashLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}