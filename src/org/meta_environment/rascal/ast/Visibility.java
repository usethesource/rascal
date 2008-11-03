package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Visibility extends AbstractAST {
	static public class Ambiguity extends Visibility {
		private final java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Visibility> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Visibility> getAlternatives() {
			return alternatives;
		}
	}

	static public class Private extends Visibility {
		/* package */Private(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitVisibilityPrivate(this);
		}
	}

	static public class Public extends Visibility {
		/* package */Public(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitVisibilityPublic(this);
		}
	}
}
