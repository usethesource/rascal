package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class UserType extends AbstractAST {
	static public class Ambiguity extends UserType {
		private final java.util.List<org.meta_environment.rascal.ast.UserType> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.UserType> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.UserType> getAlternatives() {
			return alternatives;
		}
	}

	static public class Name extends UserType {
		private org.meta_environment.rascal.ast.Name name;

		/* name:Name -> UserType {prefer, cons("Name")} */
		private Name() {
		}

		/* package */Name(ITree tree, org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitUserTypeName(this);
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
		public boolean isName() {
			return true;
		}

		public Name setName(org.meta_environment.rascal.ast.Name x) {
			Name z = new Name();
			z.$setName(x);
			return z;
		}
	}

	static public class Parametric extends UserType {
		private org.meta_environment.rascal.ast.Name name;
		private java.util.List<org.meta_environment.rascal.ast.TypeVar> parameters;

		/*
		 * name:Name "[" parameters:{TypeVar ","}+ "]" -> UserType
		 * {cons("Parametric")}
		 */
		private Parametric() {
		}

		/* package */Parametric(
				ITree tree,
				org.meta_environment.rascal.ast.Name name,
				java.util.List<org.meta_environment.rascal.ast.TypeVar> parameters) {
			this.tree = tree;
			this.name = name;
			this.parameters = parameters;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setParameters(
				java.util.List<org.meta_environment.rascal.ast.TypeVar> x) {
			this.parameters = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitUserTypeParametric(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.TypeVar> getParameters() {
			return parameters;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasParameters() {
			return true;
		}

		@Override
		public boolean isParametric() {
			return true;
		}

		public Parametric setName(org.meta_environment.rascal.ast.Name x) {
			Parametric z = new Parametric();
			z.$setName(x);
			return z;
		}

		public Parametric setParameters(
				java.util.List<org.meta_environment.rascal.ast.TypeVar> x) {
			Parametric z = new Parametric();
			z.$setParameters(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public java.util.List<org.meta_environment.rascal.ast.TypeVar> getParameters() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean hasParameters() {
		return false;
	}

	public boolean isName() {
		return false;
	}

	public boolean isParametric() {
		return false;
	}
}
