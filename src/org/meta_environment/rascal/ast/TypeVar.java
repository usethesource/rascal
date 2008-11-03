package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class TypeVar extends AbstractAST {
	static public class Ambiguity extends TypeVar {
		private final java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.TypeVar> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.TypeVar> getAlternatives() {
			return alternatives;
		}
	}

	static public class Bounded extends TypeVar {
		private org.meta_environment.rascal.ast.Type bound;
		private org.meta_environment.rascal.ast.Name name;

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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeVarBounded(this);
		}

		public org.meta_environment.rascal.ast.Type getBound() {
			return bound;
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public Bounded setBound(org.meta_environment.rascal.ast.Type x) {
			Bounded z = new Bounded();
			z.$setBound(x);
			return z;
		}

		public Bounded setName(org.meta_environment.rascal.ast.Name x) {
			Bounded z = new Bounded();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeVarFree(this);
		}

		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public Free setName(org.meta_environment.rascal.ast.Name x) {
			Free z = new Free();
			z.$setName(x);
			return z;
		}
	}
}
