package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Module extends AbstractAST {
	static public class Ambiguity extends Module {
		private final java.util.List<org.meta_environment.rascal.ast.Module> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Module> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Module> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Module {
		private org.meta_environment.rascal.ast.Body body;
		private org.meta_environment.rascal.ast.Header header;

		/* header:Header body:Body -> Module {cons("Default")} */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Header header,
				org.meta_environment.rascal.ast.Body body) {
			this.tree = tree;
			this.header = header;
			this.body = body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Body x) {
			this.body = x;
		}

		private void $setHeader(org.meta_environment.rascal.ast.Header x) {
			this.header = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitModuleDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Body getBody() {
			return body;
		}

		@Override
		public org.meta_environment.rascal.ast.Header getHeader() {
			return header;
		}

		@Override
		public boolean hasBody() {
			return true;
		}

		@Override
		public boolean hasHeader() {
			return true;
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		public Default setBody(org.meta_environment.rascal.ast.Body x) {
			Default z = new Default();
			z.$setBody(x);
			return z;
		}

		public Default setHeader(org.meta_environment.rascal.ast.Header x) {
			Default z = new Default();
			z.$setHeader(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Body getBody() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Header getHeader() {
		throw new UnsupportedOperationException();
	}

	public boolean hasBody() {
		return false;
	}

	public boolean hasHeader() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}
}
