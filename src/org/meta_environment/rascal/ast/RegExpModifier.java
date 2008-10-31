package org.meta_environment.rascal.ast;

public abstract class RegExpModifier extends AbstractAST {
	static public class Ambiguity extends RegExpModifier {
		private final java.util.List<org.meta_environment.rascal.ast.RegExpModifier> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.RegExpModifier> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.RegExpModifier> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends RegExpModifier {
		/* [imsd] -> RegExpModifier */
	}
}
