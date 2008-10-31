package org.meta_environment.rascal.ast;

public abstract class StrCon extends AbstractAST {
	static public class Ambiguity extends StrCon {
		private final java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.StrCon> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.StrCon> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends StrCon {
		/* [\"] chars:StrChar [\"] -> StrCon */
	}
}
