package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class StrCon extends AbstractAST {
	static public class Ambiguity extends StrCon {
		private final java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStrConAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.StrCon> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends StrCon {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStrConLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}