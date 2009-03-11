package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Formal extends AbstractAST {
	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasType() {
		return false;
	}

	public boolean hasName() {
		return false;
	}

	public boolean isTypeName() {
		return false;
	}

	static public class TypeName extends Formal {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private TypeName() {
		}

		/* package */TypeName(INode node,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name) {
			this.node = node;
			this.type = type;
			this.name = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFormalTypeName(this);
		}

		@Override
		public boolean isTypeName() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		private org.meta_environment.rascal.ast.Type type;

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public TypeName setType(org.meta_environment.rascal.ast.Type x) {
			TypeName z = new TypeName();
			z.$setType(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Name name;

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public TypeName setName(org.meta_environment.rascal.ast.Name x) {
			TypeName z = new TypeName();
			z.$setName(x);
			return z;
		}
	}

	static public class Ambiguity extends Formal {
		private final java.util.List<org.meta_environment.rascal.ast.Formal> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Formal> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Formal> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFormalAmbiguity(this);
		}
	}
}