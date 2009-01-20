package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Name extends AbstractAST {
	static public class Ambiguity extends Name {
		private final java.util.List<org.meta_environment.rascal.ast.Name> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Name> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNameAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Name> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends Name {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNameLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}