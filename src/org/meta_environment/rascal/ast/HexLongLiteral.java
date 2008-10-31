package org.meta_environment.rascal.ast;

public abstract class HexLongLiteral extends AbstractAST {
	static public class Ambiguity extends HexLongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.HexLongLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends HexLongLiteral {
		/* [0] [xX] [0-9a-fA-F]+ [lL] -> HexLongLiteral */
	}
}
