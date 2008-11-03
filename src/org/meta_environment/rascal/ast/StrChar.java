package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class StrChar extends AbstractAST {
	static public class Ambiguity extends StrChar {
		private final java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.StrChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.StrChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends StrChar {
		private String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		public String getString() {
			return string;
		}
	}

	static public class newline extends StrChar {
		/* package */newline(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStrCharnewline(this);
		}
	}
}
