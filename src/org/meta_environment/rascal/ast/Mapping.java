package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Mapping extends AbstractAST {
	static public class Ambiguity extends Mapping {
		private final java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Mapping> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitMappingAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Mapping> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Mapping {
		private org.meta_environment.rascal.ast.Expression from;
		private org.meta_environment.rascal.ast.Expression to;

		/* from:Expression ":" to:Expression -> Mapping {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Expression from,
				org.meta_environment.rascal.ast.Expression to) {
			this.tree = tree;
			this.from = from;
			this.to = to;
		}

		private void $setFrom(org.meta_environment.rascal.ast.Expression x) {
			this.from = x;
		}

		private void $setTo(org.meta_environment.rascal.ast.Expression x) {
			this.to = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitMappingDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getFrom() {
			return from;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getTo() {
			return to;
		}

		@Override
		public boolean hasFrom() {
			return true;
		}

		@Override
		public boolean hasTo() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setFrom(org.meta_environment.rascal.ast.Expression x) {
			final Default z = new Default();
			z.$setFrom(x);
			return z;
		}

		public Default setTo(org.meta_environment.rascal.ast.Expression x) {
			final Default z = new Default();
			z.$setTo(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Expression getFrom() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getTo() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFrom() {
		return false;
	}

	public boolean hasTo() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}