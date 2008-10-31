package org.meta_environment.rascal.ast;

public abstract class SingleCharacter extends AbstractAST {
	static public class Ambiguity extends SingleCharacter {
		private final java.util.List<org.meta_environment.rascal.ast.SingleCharacter> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.SingleCharacter> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.SingleCharacter> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends SingleCharacter {
		/* UnicodeEscape -> SingleCharacter */
	}
}
