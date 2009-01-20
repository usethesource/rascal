package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Alternative extends AbstractAST {
	static public class Ambiguity extends Alternative {
		private final java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Alternative> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitAlternativeAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Alternative> getAlternatives() {
			return alternatives;
		}
	}

	static public class NamedType extends Alternative {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Type type;

		/* name:Name type:Type -> Alternative {cons("NamedType")} */
		private NamedType() {
		}

		/* package */NamedType(ITree tree,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Type type) {
			this.tree = tree;
			this.name = name;
			this.type = type;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAlternativeNamedType(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isNamedType() {
			return true;
		}

		public NamedType setName(org.meta_environment.rascal.ast.Name x) {
			final NamedType z = new NamedType();
			z.$setName(x);
			return z;
		}

		public NamedType setType(org.meta_environment.rascal.ast.Type x) {
			final NamedType z = new NamedType();
			z.$setType(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean isNamedType() {
		return false;
	}
}