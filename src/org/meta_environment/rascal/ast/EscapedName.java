package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class EscapedName extends AbstractAST {
	static public class Lexical extends EscapedName {
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
			return v.visitEscapedNameLexical(this);
		}
	}

	static public class Ambiguity extends EscapedName {
		private final java.util.List<org.meta_environment.rascal.ast.EscapedName> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.EscapedName> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.EscapedName> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitEscapedNameAmbiguity(this);
		}
	}
}