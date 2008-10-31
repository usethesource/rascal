package org.meta_environment.rascal.ast;

public abstract class NamedRegExp extends AbstractAST {
	static public class Ambiguity extends NamedRegExp {
		private final java.util.List<org.meta_environment.rascal.ast.NamedRegExp> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.NamedRegExp> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.NamedRegExp> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends NamedRegExp {
		/* ~[\>\\] -> NamedRegExp */
	}
}
