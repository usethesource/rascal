package org.meta_environment.rascal.ast;

public abstract class StringLiteral extends AbstractAST {
	static public class Ambiguity extends StringLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.StringLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.StringLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.StringLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends StringLiteral {
		/* "\"" StringCharacter "\"" -> StringLiteral */
	}
}
