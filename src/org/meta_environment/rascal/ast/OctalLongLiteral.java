package org.meta_environment.rascal.ast;

public abstract class OctalLongLiteral extends AbstractAST {
	static public class Ambiguity extends OctalLongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.OctalLongLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends OctalLongLiteral {
		/* [0] [0-7]+ [lL] -> OctalLongLiteral */
	}
}
