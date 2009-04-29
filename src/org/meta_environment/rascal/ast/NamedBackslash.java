package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class NamedBackslash extends AbstractAST {
	static public class Lexical extends NamedBackslash {
		private String string;

		/* package */Lexical(INode node, String string) {
			this.node = node;
			this.string = string;
		}

		public String getString() {
			return string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNamedBackslashLexical(this);
		}
	}

	static public class Ambiguity extends NamedBackslash {
		private final java.util.List<org.meta_environment.rascal.ast.NamedBackslash> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.NamedBackslash> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.NamedBackslash> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitNamedBackslashAmbiguity(this);
		}
	}
}