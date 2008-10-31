package org.meta_environment.rascal.ast;

public abstract class DecimalLongLiteral extends AbstractAST {
	static public class Ambiguity extends DecimalLongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.DecimalLongLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends DecimalLongLiteral {
		/* "0" [lL] -> DecimalLongLiteral */
	}
}
