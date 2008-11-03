package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Catch extends AbstractAST {
	static public class Ambiguity extends Catch {
		private final java.util.List<org.meta_environment.rascal.ast.Catch> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Catch> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Catch> getAlternatives() {
			return alternatives;
		}
	}

	static public class Binding extends Catch {
		private org.meta_environment.rascal.ast.Statement body;
		private org.meta_environment.rascal.ast.Name name;
		private org.meta_environment.rascal.ast.Type type;

		/*
		 * "catch" "(" type:Type name:Name ")" body:Statement -> Catch
		 * {cons("Binding")}
		 */
		private Binding() {
		}

		/* package */Binding(ITree tree,
				org.meta_environment.rascal.ast.Type type,
				org.meta_environment.rascal.ast.Name name,
				org.meta_environment.rascal.ast.Statement body) {
			this.tree = tree;
			this.type = type;
			this.name = name;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		private void $setName(org.meta_environment.rascal.ast.Name x) {
			this.name = x;
		}

		private void $setType(org.meta_environment.rascal.ast.Type x) {
			this.type = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCatchBinding(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getName() {
			return name;
		}

		@Override
		public org.meta_environment.rascal.ast.Type getType() {
			return type;
		}

		public Binding setBody(org.meta_environment.rascal.ast.Statement x) {
			Binding z = new Binding();
			z.$setBody(x);
			return z;
		}

		public Binding setName(org.meta_environment.rascal.ast.Name x) {
			Binding z = new Binding();
			z.$setName(x);
			return z;
		}

		public Binding setType(org.meta_environment.rascal.ast.Type x) {
			Binding z = new Binding();
			z.$setType(x);
			return z;
		}
	}

	static public class Default extends Catch {
		private org.meta_environment.rascal.ast.Statement body;

		/* "catch" body:Statement -> Catch {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Statement body) {
			this.tree = tree;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Statement x) {
			this.body = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCatchDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Statement getBody() {
			return body;
		}

		public Default setBody(org.meta_environment.rascal.ast.Statement x) {
			Default z = new Default();
			z.$setBody(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Statement getBody() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getName() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Type getType() {
		throw new UnsupportedOperationException();
	}
}
