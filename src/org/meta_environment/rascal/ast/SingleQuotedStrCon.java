package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class SingleQuotedStrCon extends AbstractAST {
	static public class Ambiguity extends SingleQuotedStrCon {
		private final java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSingleQuotedStrConAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends SingleQuotedStrCon {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSingleQuotedStrConLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}