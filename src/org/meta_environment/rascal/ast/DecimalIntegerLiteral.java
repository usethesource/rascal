package org.meta_environment.rascal.ast;

public abstract class DecimalIntegerLiteral extends AbstractAST {
	static public class Ambiguity extends DecimalIntegerLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.DecimalIntegerLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.DecimalIntegerLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.DecimalIntegerLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends DecimalIntegerLiteral {
		/* "0" -> DecimalIntegerLiteral */
	}
}
