package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class FunctionBody extends AbstractAST {
	static public class Ambiguity extends FunctionBody {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionBody> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.FunctionBody> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionBody> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends FunctionBody {
		private java.util.List<org.meta_environment.rascal.ast.Statement> statements;

		/* "{" statements:Statement "}" -> FunctionBody {cons("Default")} */
		private Default() {
		}

		/* package */Default(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Statement> statements) {
			this.tree = tree;
			this.statements = statements;
		}

		private void $setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			this.statements = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFunctionBodyDefault(this);
		}

		@Override
		public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
			return statements;
		}

		public Default setStatements(
				java.util.List<org.meta_environment.rascal.ast.Statement> x) {
			Default z = new Default();
			z.$setStatements(x);
			return z;
		}
	}

	public java.util.List<org.meta_environment.rascal.ast.Statement> getStatements() {
		throw new UnsupportedOperationException();
	}
}
