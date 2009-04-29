package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class UserType extends AbstractAST {
	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isName() {
		return false;
	}

	static public class Name extends UserType {
		/* name:Name -> UserType {cons("Name")} */
		private Name() {
		}

		/* package */Name(INode node, org.meta_environment.rascal.ast.Name name) {
			this.node = node;
			this.name = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitUserTypeName(this);
		}

		@Override
		public boolean isName() {
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

		public Name setName(org.meta_environment.rascal.ast.Name x) {
			Name z = new Name();
			z.$setName(x);
			return z;
		}
	}

	static public class Ambiguity extends UserType {
		private final java.util.List<org.meta_environment.rascal.ast.UserType> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.UserType> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.UserType> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitUserTypeAmbiguity(this);
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.Type> getParameters() {
		throw new UnsupportedOperationException();
	}

	public boolean hasParameters() {
		return false;
	}

	public boolean isParametric() {
		return false;
	}

	static public class Parametric extends UserType {
		/*
		 * name:Name "[" parameters:{Type ","}+ "]" -> UserType
		 * {cons("Parametric")}
		 */
		private Parametric() {
		}

		/* package */Parametric(INode node,
				org.meta_environment.rascal.ast.Name name,
				java.util.List<org.meta_environment.rascal.ast.Type> parameters) {
			this.node = node;
			this.name = name;
			this.parameters = parameters;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitUserTypeParametric(this);
		}

		@Override
		public boolean isParametric() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		@Override
		public boolean hasParameters() {
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

		public Parametric setName(org.meta_environment.rascal.ast.Name x) {
			Parametric z = new Parametric();
			z.$setName(x);
			return z;
		}

		private java.util.List<org.meta_environment.rascal.ast.Type> parameters;

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Type> getParameters() {
			return parameters;
		}

		private void $setParameters(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			this.parameters = x;
		}

		public Parametric setParameters(
				java.util.List<org.meta_environment.rascal.ast.Type> x) {
			Parametric z = new Parametric();
			z.$setParameters(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}