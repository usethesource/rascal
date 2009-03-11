package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Visibility extends AbstractAST {
	public boolean isPublic() {
		return false;
	}

	static public class Public extends Visibility {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Public() {
		}

		/* package */Public(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVisibilityPublic(this);
		}

		@Override
		public boolean isPublic() {
			return true;
		}
	}

	static public class Ambiguity extends Visibility {
		private final java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Visibility> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitVisibilityAmbiguity(this);
		}
	}

	public boolean isPrivate() {
		return false;
	}

	static public class Private extends Visibility {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Private() {
		}

		/* package */Private(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitVisibilityPrivate(this);
		}

		@Override
		public boolean isPrivate() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);
}