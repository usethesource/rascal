package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class RegExpModifier extends AbstractAST {
	static public class Ambiguity extends RegExpModifier {
		private final java.util.List<org.meta_environment.rascal.ast.RegExpModifier> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.RegExpModifier> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRegExpModifierAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.RegExpModifier> getAlternatives() {
			return alternatives;
		}
	}

	static public class Lexical extends RegExpModifier {
		private final String string;

		/* package */Lexical(ITree tree, String string) {
			this.tree = tree;
			this.string = string;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitRegExpModifierLexical(this);
		}

		public String getString() {
			return string;
		}
	}
}