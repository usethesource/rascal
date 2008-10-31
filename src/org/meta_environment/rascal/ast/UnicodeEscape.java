package org.meta_environment.rascal.ast;

public abstract class UnicodeEscape extends AbstractAST {
	static public class Ambiguity extends UnicodeEscape {
		private final java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.UnicodeEscape> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends UnicodeEscape {
		/*
		 * "\\" [u]+ [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] ->
		 * UnicodeEscape
		 */
	}
}
