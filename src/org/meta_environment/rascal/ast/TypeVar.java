package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class TypeVar extends AbstractAST {
	static public class Ambiguity extends TypeVar {
		private final java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTypeVarAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.TypeVar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Bounded extends TypeVar {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Type bound;

		/* "&" name:Name "<:" bound:Type -> TypeVar {cons("Bounded")} */
		private Bounded() {
		}

		/* package */Bounded(ITree tree,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Type bound) {
			this.tree = tree;
			this.name = name;
			this.bound = bound;
		}

		private void $setBound(org.meta_environment.rascal.ast.Type x) {
			this.bound = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeVarBounded(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Type getBound() {
			return bound;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public boolean hasBound() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isBounded() {
			return true;
		}

		public Bounded setBound(org.meta_environment.rascal.ast.Type x) {
			final Bounded z = new Bounded();
			z.$setBound(x);
			return z;
		}

		public Bounded setName(org.meta_environment.rascal.ast.Name x) {
			final Bounded z = new Bounded();
			z.$setName(x);
			return z;
		}
	}

	static public class Free extends TypeVar {
		private org.meta_environment.rascal.ast.Name name;

		/* "&" name:Name -> TypeVar {cons("Free")} */
		private Free() {
		}

		/* package */Free(ITree tree, org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeVarFree(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean isFree() {
			return true;
		}

		public Free setName(org.meta_environment.rascal.ast.Name x) {
			final Free z = new Free();
			z.$setName(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.Type getBound() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasBound() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean isBounded() {
		return false;
	}

	public boolean isFree() {
		return false;
	}
}