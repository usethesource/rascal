package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class TypeVar extends AbstractAST {
	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isFree() {
		return false;
	}

	static public class Free extends TypeVar {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Free() {
		}

		/* package */Free(INode node, org.meta_environment.rascal.ast.Name name) {
			this.node = node;
			this.name = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeVarFree(this);
		}

		@Override
		public boolean isFree() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public Free setName(org.meta_environment.rascal.ast.Name x) {
			Free z = new Free();
			z.$setName(x);
			return z;
		}
	}

	static public class Ambiguity extends TypeVar {
		private final java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.TypeVar> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTypeVarAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.Type getBound() {
		throw new UnsupportedOperationException();
	}

	public boolean hasBound() {
		return false;
	}

	public boolean isBounded() {
		return false;
	}

	static public class Bounded extends TypeVar {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Bounded() {
		}

		/* package */Bounded(INode node,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Type bound) {
			this.node = node;
			this.name = name;
			this.bound = bound;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeVarBounded(this);
		}

		@Override
		public boolean isBounded() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasBound() {
			return true;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public Bounded setName(org.meta_environment.rascal.ast.Name x) {
			Bounded z = new Bounded();
			z.$setName(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Type bound;

		@Override
		public org.meta_environment.rascal.ast.Type getBound() {
			return bound;
		}

		private void $setBound(org.meta_environment.rascal.ast.Type x) {
			this.bound = x;
		}

		public Bounded setBound(org.meta_environment.rascal.ast.Type x) {
			Bounded z = new Bounded();
			z.$setBound(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}