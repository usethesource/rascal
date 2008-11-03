package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class SymbolLiteral extends AbstractAST {
	static public class Ambiguity extends SymbolLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.SymbolLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.SymbolLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.SymbolLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends SymbolLiteral {
		private String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		public String getString() {
			return string;
		}
	}
}
