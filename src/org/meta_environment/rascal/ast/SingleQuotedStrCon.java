package org.meta_environment.rascal.ast;

public abstract class SingleQuotedStrCon extends AbstractAST {
	static public class Ambiguity extends SingleQuotedStrCon {
		private final java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrCon> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends SingleQuotedStrCon {
		/* [\'] chars:SingleQuotedStrChar [\'] -> SingleQuotedStrCon */
	}
}
