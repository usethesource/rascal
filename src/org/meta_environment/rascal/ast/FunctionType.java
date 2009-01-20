package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class FunctionType extends AbstractAST {
	static public class Ambiguity extends FunctionType {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionType> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.FunctionType> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFunctionTypeAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionType> getAlternatives() {
			return alternatives;
		}
	}

	static public class TypeArguments extends FunctionType {
		private org.meta_environment.rascal.ast.Type type;
		private java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments;

		/*
		 * type:Type "(" arguments:{TypeArg ","} ")" -> FunctionType
		 * {cons("TypeArguments")}
		 */
		private TypeArguments() {
		}

		/* package */TypeArguments(
				ITree tree,
				org.meta_environment.rascal.ast.Type type,
				java.util.List<org.meta_environment.rascal.ast.TypeArg> arguments) {
			this.tree = tree;
			this.type = type;
			this.arguments = arguments;
		}

		private void $setArguments(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			this.arguments = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFunctionTypeTypeArguments(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() {
			return arguments;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		@Override
		public boolean hasArguments() {
			return true;
		}

		@Override
		public boolean hasType() {
			return true;
		}

		@Override
		public boolean isTypeArguments() {
			return true;
		}

		public TypeArguments setArguments(
				java.util.List<org.meta_environment.rascal.ast.TypeArg> x) {
			final TypeArguments z = new TypeArguments();
			z.$setArguments(x);
			return z;
		}

		public TypeArguments setType(org.meta_environment.rascal.ast.Type x) {
			final TypeArguments z = new TypeArguments();
			z.$setType(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.TypeArg> getArguments() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}

	public boolean hasArguments() {
		return false;
	}

	public boolean hasType() {
		return false;
	}

	public boolean isTypeArguments() {
		return false;
	}
}