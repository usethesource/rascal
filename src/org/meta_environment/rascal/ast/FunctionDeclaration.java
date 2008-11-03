package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class FunctionDeclaration extends AbstractAST {
	static public class Abstract extends FunctionDeclaration {
		private org.meta_environment.rascal.ast.Signature signature;
		private org.meta_environment.rascal.ast.Tags tags;

		/*
		 * signature:Signature tags:Tags -> FunctionDeclaration
		 * {cons("Abstract")}
		 */
		private Abstract() {
		}

		/* package */Abstract(ITree tree,
				org.meta_environment.rascal.ast.Signature signature,
				org.meta_environment.rascal.ast.Tags tags) {
			this.tree = tree;
			this.signature = signature;
			this.tags = tags;
		}

		private void $setSignature(org.meta_environment.rascal.ast.Signature x) {
			this.signature = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFunctionDeclarationAbstract(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Signature getSignature() {
			return signature;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public Abstract setSignature(org.meta_environment.rascal.ast.Signature x) {
			Abstract z = new Abstract();
			z.$setSignature(x);
			return z;
		}

		public Abstract setTags(org.meta_environment.rascal.ast.Tags x) {
			Abstract z = new Abstract();
			z.$setTags(x);
			return z;
		}
	}

	static public class Ambiguity extends FunctionDeclaration {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends FunctionDeclaration {
		private org.meta_environment.rascal.ast.FunctionBody body;
		private org.meta_environment.rascal.ast.Signature signature;
		private org.meta_environment.rascal.ast.Tags tags;

		/*
		 * signature:Signature tags:Tags body:FunctionBody ->
		 * FunctionDeclaration {cons("Default")}
		 */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Signature signature,
				org.meta_environment.rascal.ast.Tags tags,
				org.meta_environment.rascal.ast.FunctionBody body) {
			this.tree = tree;
			this.signature = signature;
			this.tags = tags;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.FunctionBody x) {
			this.body = x;
		}

		private void $setSignature(org.meta_environment.rascal.ast.Signature x) {
			this.signature = x;
		}

		private void $setTags(org.meta_environment.rascal.ast.Tags x) {
			this.tags = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitFunctionDeclarationDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.FunctionBody getBody() {
			return body;
		}

		@Override
		public org.meta_environment.rascal.ast.Signature getSignature() {
			return signature;
		}

		@Override
		public org.meta_environment.rascal.ast.Tags getTags() {
			return tags;
		}

		public Default setBody(org.meta_environment.rascal.ast.FunctionBody x) {
			Default z = new Default();
			z.$setBody(x);
			return z;
		}

		public Default setSignature(org.meta_environment.rascal.ast.Signature x) {
			Default z = new Default();
			z.$setSignature(x);
			return z;
		}

		public Default setTags(org.meta_environment.rascal.ast.Tags x) {
			Default z = new Default();
			z.$setTags(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.FunctionBody getBody() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Signature getSignature() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Tags getTags() {
		throw new UnsupportedOperationException();
	}
}
