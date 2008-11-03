package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class FunctionName extends AbstractAST {
	static public class Ambiguity extends FunctionName {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionName> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.FunctionName> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionName> getAlternatives() {
			return alternatives;
		}
	}

	static public class Name extends FunctionName {
		private org.meta_environment.rascal.ast.Name name;

		/* name:Name -> FunctionName {cons("Name")} */
		private Name() {
		}

		/* package */Name(ITree tree, org.meta_environment.rascal.ast.Name name) {
			this.tree = tree;
			this.name = name;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFunctionNameName(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		public Name setName(org.meta_environment.rascal.ast.Name x) {
			Name z = new Name();
			z.$setName(x);
			return z;
		}
	}

	static public class Operator extends FunctionName {
		private org.meta_environment.rascal.ast.StandardOperator operator;

		/* operator:StandardOperator -> FunctionName {cons("Operator")} */
		private Operator() {
		}

		/* package */Operator(ITree tree,
				org.meta_environment.rascal.ast.StandardOperator operator) {
			this.tree = tree;
			this.operator = operator;
		}

		private void $setOperator(
				org.meta_environment.rascal.ast.StandardOperator x) {
			this.operator = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFunctionNameOperator(this);
		}

		@Override
		public org.meta_environment.rascal.ast.StandardOperator getOperator() {
			return operator;
		}

		public Operator setOperator(
				org.meta_environment.rascal.ast.StandardOperator x) {
			Operator z = new Operator();
			z.$setOperator(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.StandardOperator getOperator() {
		throw new UnsupportedOperationException();
	}
}
