package org.meta_environment.rascal.ast;

public abstract class SingleQuotedStrChar extends AbstractAST {
	static public class Ambiguity extends SingleQuotedStrChar {
		private final java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.SingleQuotedStrChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends SingleQuotedStrChar {
		/* "\\n" -> SingleQuotedStrChar */
	}
}
