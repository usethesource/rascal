package org.meta_environment.rascal.ast;

public abstract class Name extends AbstractAST {
	static public class Ambiguity extends Name {
		private final java.util.List<org.meta_environment.rascal.ast.Name> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Name> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Name> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends Name {
		/* [\\]? [A-Za-z\_] [A-Za-z0-9\_\-] -> Name */
	}
}
