package org.meta_environment.rascal.ast;

public abstract class CharacterLiteral extends AbstractAST {
	static public class Ambiguity extends CharacterLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.CharacterLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends CharacterLiteral {
		/* "'" SingleCharacter "'" -> CharacterLiteral */
	}
}
