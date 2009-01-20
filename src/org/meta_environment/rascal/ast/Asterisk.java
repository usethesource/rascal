package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Asterisk extends AbstractAST {
	static public class Ambiguity extends Asterisk {
		private final java.util.List<org.meta_environment.rascal.ast.Asterisk> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Asterisk> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitAsteriskAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Asterisk> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends Asterisk {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitAsteriskLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}