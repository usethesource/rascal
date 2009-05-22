package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Module extends AbstractAST {
	public org.meta_environment.rascal.ast.Header getHeader() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Body getBody() {
		throw new UnsupportedOperationException();
	}

	public boolean hasHeader() {
		return false;
	}

	public boolean hasBody() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	static public class Default extends Module {
		/* header:Header body:Body -> Module {cons("Default")} */
		private Default() {
			super();
		}

		public Default(INode node,
				org.meta_environment.rascal.ast.Header header,
				org.meta_environment.rascal.ast.Body body) {
			this.node = node;
			this.header = header;
			this.body = body;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitModuleDefault(this);
		}

		@Override
		public boolean isDefault() {
			return true;
		}

		@Override
		public boolean hasHeader() {
			return true;
		}

		@Override
		public boolean hasBody() {
			return true;
		}

		private org.meta_environment.rascal.ast.Header header;

		@Override
		public org.meta_environment.rascal.ast.Header getHeader() {
			return header;
		}

		private void $setHeader(org.meta_environment.rascal.ast.Header x) {
			this.header = x;
		}

		public Default setHeader(org.meta_environment.rascal.ast.Header x) {
			Default z = new Default();
			z.$setHeader(x);
			return z;
		}

		private org.meta_environment.rascal.ast.Body body;

		@Override
		public org.meta_environment.rascal.ast.Body getBody() {
			return body;
		}

		private void $setBody(org.meta_environment.rascal.ast.Body x) {
			this.body = x;
		}

		public Default setBody(org.meta_environment.rascal.ast.Body x) {
			Default z = new Default();
			z.$setBody(x);
			return z;
		}
	}

	static public class Ambiguity extends Module {
		private final java.util.List<org.meta_environment.rascal.ast.Module> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Module> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Module> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitModuleAmbiguity(this);
		}
	}
}