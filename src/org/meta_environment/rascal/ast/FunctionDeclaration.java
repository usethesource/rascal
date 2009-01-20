package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class FunctionDeclaration extends AbstractAST {
	static public class Abstract extends FunctionDeclaration {
		private org.meta_environment.rascal.ast.Signature signature;
		private org.meta_environment.rascal.ast.Tags tags;

		/*
		 * signature:Signature tags:Tags ";" -> FunctionDeclaration
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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
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

		@Override
		public boolean hasSignature() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean isAbstract() {
			return true;
		}

		public Abstract setSignature(org.meta_environment.rascal.ast.Signature x) {
			final Abstract z = new Abstract();
			z.$setSignature(x);
			return z;
		}

		public Abstract setTags(org.meta_environment.rascal.ast.Tags x) {
			final Abstract z = new Abstract();
			z.$setTags(x);
			return z;
		}
	}

	static public class Ambiguity extends FunctionDeclaration {
		private final java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFunctionDeclarationAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.FunctionDeclaration> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends FunctionDeclaration {
		private org.meta_environment.rascal.ast.Signature signature;
		private org.meta_environment.rascal.ast.Tags tags;
		private org.meta_environment.rascal.ast.FunctionBody body;

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

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
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

		@Override
		public boolean hasBody() {
			return true;
		}

		@Override
		public boolean hasSignature() {
			return true;
		}

		@Override
		public boolean hasTags() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setBody(org.meta_environment.rascal.ast.FunctionBody x) {
			final Default z = new Default();
			z.$setBody(x);
			return z;
		}

		public Default setSignature(org.meta_environment.rascal.ast.Signature x) {
			final Default z = new Default();
			z.$setSignature(x);
			return z;
		}

		public Default setTags(org.meta_environment.rascal.ast.Tags x) {
			final Default z = new Default();
			z.$setTags(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.FunctionBody getBody() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Signature getSignature() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Tags getTags() {
		throw new UnsupportedOperationException();
	}

	public boolean hasBody() {
		return false;
	}

	public boolean hasSignature() {
		return false;
	}

	public boolean hasTags() {
		return false;
	}

	public boolean isAbstract() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}