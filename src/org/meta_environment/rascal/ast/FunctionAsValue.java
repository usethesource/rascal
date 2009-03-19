package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class FunctionAsValue extends AbstractAST {
	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	static public class Default extends FunctionAsValue {
		/* "#" name:Name -> FunctionAsValue {cons("Default")} */
		private Default() {
		}

		/* package */Default(INode node,
				org.meta_environment.rascal.ast.Name name) {
			this.node = node;
			this.name = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFunctionAsValueDefault(this);
		}

		@Override
		public boolean isDefault() {
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

		public Default setName(org.meta_environment.rascal.ast.Name x) {
			Default z = new Default();
			z.$setName(x);
			return z;
		}
	}

	static public class Ambiguity extends FunctionAsValue {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionAsValue> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.FunctionAsValue> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionAsValue> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFunctionAsValueAmbiguity(this);
		}
	}
}