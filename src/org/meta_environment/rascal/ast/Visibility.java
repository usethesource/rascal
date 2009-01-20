package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Visibility extends AbstractAST {
	static public class Ambiguity extends Visibility {
		private final java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitVisibilityAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Visibility> getAlternatives() {
			return alternatives;
		}
	}

	static public class Private extends Visibility {
		/* package */Private(ITree tree) {
			this.tree = tree;
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

	static public class Public extends Visibility {
		/* package */Public(ITree tree) {
			this.tree = tree;
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

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isPrivate() {
		return false;
	}

	public boolean isPublic() {
		return false;
	}
}