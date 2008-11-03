package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Kind extends AbstractAST {
	static public class All extends Kind {
		/* package */All(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindAll(this);
		}
	}

	static public class Ambiguity extends Kind {
		private final java.util.List<org.meta_environment.rascal.ast.Kind> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Kind> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Kind> getAlternatives() {
			return alternatives;
		}
	}

	static public class Anno extends Kind {
		/* package */Anno(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindAnno(this);
		}
	}

	static public class Data extends Kind {
		/* package */Data(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindData(this);
		}
	}

	static public class Function extends Kind {
		/* package */Function(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindFunction(this);
		}
	}

	static public class Module extends Kind {
		/* package */Module(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindModule(this);
		}
	}

	static public class Tag extends Kind {
		/* package */Tag(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindTag(this);
		}
	}

	static public class Type extends Kind {
		/* package */Type(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindType(this);
		}
	}

	static public class Variable extends Kind {
		/* package */Variable(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindVariable(this);
		}
	}

	static public class View extends Kind {
		/* package */View(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitKindView(this);
		}
	}
}
