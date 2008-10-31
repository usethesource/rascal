package org.meta_environment.rascal.ast;

public abstract class BooleanLiteral extends AbstractAST {
	static public class Ambiguity extends BooleanLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.BooleanLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends BooleanLiteral {
		/* "true" -> BooleanLiteral */
	}
}
