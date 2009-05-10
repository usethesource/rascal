package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Pattern extends AbstractAST {
	public org.meta_environment.rascal.ast.SYMBOL getSymbol() {
		throw new UnsupportedOperationException();
	}

	public boolean hasSymbol() {
		return false;
	}

	public boolean isConcreteQuoted() {
		return false;
	}

	static public class ConcreteQuoted extends Pattern {
		/* "[|" symbol:SYMBOL "|]" -> Pattern {cons("ConcreteQuoted")} */
		private ConcreteQuoted() {
		}

		/* package */ConcreteQuoted(INode node,
				org.meta_environment.rascal.ast.SYMBOL symbol) {
			this.node = node;
			this.symbol = symbol;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitPatternConcreteQuoted(this);
		}

		@Override
		public boolean isConcreteQuoted() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		private org.meta_environment.rascal.ast.SYMBOL symbol;

		@Override
		public org.meta_environment.rascal.ast.SYMBOL getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.SYMBOL x) {
			this.symbol = x;
		}

		public ConcreteQuoted setSymbol(org.meta_environment.rascal.ast.SYMBOL x) {
			ConcreteQuoted z = new ConcreteQuoted();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class Ambiguity extends Pattern {
		private final java.util.List<org.meta_environment.rascal.ast.Pattern> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Pattern> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Pattern> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitPatternAmbiguity(this);
		}
	}

	public boolean isConcreteUnquoted() {
		return false;
	}

	static public class ConcreteUnquoted extends Pattern {
		/* symbol:SYMBOL -> Pattern {cons("ConcreteUnquoted")} */
		private ConcreteUnquoted() {
		}

		/* package */ConcreteUnquoted(INode node,
				org.meta_environment.rascal.ast.SYMBOL symbol) {
			this.node = node;
			this.symbol = symbol;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitPatternConcreteUnquoted(this);
		}

		@Override
		public boolean isConcreteUnquoted() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		private org.meta_environment.rascal.ast.SYMBOL symbol;

		@Override
		public org.meta_environment.rascal.ast.SYMBOL getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.SYMBOL x) {
			this.symbol = x;
		}

		public ConcreteUnquoted setSymbol(
				org.meta_environment.rascal.ast.SYMBOL x) {
			ConcreteUnquoted z = new ConcreteUnquoted();
			z.$setSymbol(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}