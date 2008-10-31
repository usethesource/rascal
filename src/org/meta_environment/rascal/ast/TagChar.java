package org.meta_environment.rascal.ast;

public abstract class TagChar extends AbstractAST {
	static public class Ambiguity extends TagChar {
		private final java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.TagChar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.TagChar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends TagChar {
		/* ~[\}] -> TagChar */
	}
}
