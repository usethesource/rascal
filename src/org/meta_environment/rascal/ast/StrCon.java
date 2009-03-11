package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class StrCon extends AbstractAST {
	static public class Lexical extends StrCon {
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
			return v.visitStrConLexical(this);
		}
	}

	static public class Ambiguity extends StrCon {
		private final java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.StrCon> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStrConAmbiguity(this);
		}
	}
}