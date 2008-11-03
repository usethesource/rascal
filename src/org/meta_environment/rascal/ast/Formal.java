package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Formal extends AbstractAST {
	static public class Ambiguity extends Formal {
		private final java.util.List<org.meta_environment.rascal.ast.Formal> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Formal> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Formal> getAlternatives() {
			return alternatives;
		}
	}

	static public class TypeName extends Formal {
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Type type;

		/* type:Type name:Name -> Formal {cons("TypeName")} */
		private TypeName() {
		}

		/* package */TypeName(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.type = type;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFormalTypeName(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public TypeName setName(org.meta_environment.rascal.ast.Name x) {
			TypeName z = new TypeName();
			z.$setName(x);
			return z;
		}

		public TypeName setType(org.meta_environment.rascal.ast.Type x) {
			TypeName z = new TypeName();
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
}
