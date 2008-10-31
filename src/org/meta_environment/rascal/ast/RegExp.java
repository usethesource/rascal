package org.meta_environment.rascal.ast;

public abstract class RegExp extends AbstractAST {
	static public class Ambiguity extends RegExp {
		private final java.util.List<org.meta_environment.rascal.ast.RegExp> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.RegExp> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.RegExp> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends RegExp {
		/* Backslash -> RegExp */
	}
}
