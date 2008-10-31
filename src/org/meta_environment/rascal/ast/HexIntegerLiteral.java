package org.meta_environment.rascal.ast;

public abstract class HexIntegerLiteral extends AbstractAST {
	static public class Ambiguity extends HexIntegerLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.HexIntegerLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends HexIntegerLiteral {
		/* [0] [xX] [0-9a-fA-F]+ -> HexIntegerLiteral */
	}
}
