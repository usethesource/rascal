package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Kind extends AbstractAST {
	static public class All extends Kind {
		/* package */All(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindAll(this);
		}

		@Override
		public boolean isAll() {
			return true;
		}
	}

	static public class Ambiguity extends Kind {
		private final java.util.List<org.meta_environment.rascal.ast.Kind> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Kind> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitKindAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Kind> getAlternatives() {
			return alternatives;
		}
	}

	static public class Anno extends Kind {
		/* package */Anno(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindAnno(this);
		}

		@Override
		public boolean isAnno() {
			return true;
		}
	}

	static public class Data extends Kind {
		/* package */Data(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindData(this);
		}

		@Override
		public boolean isData() {
			return true;
		}
	}

	static public class Function extends Kind {
		/* package */Function(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindFunction(this);
		}

		@Override
		public boolean isFunction() {
			return true;
		}
	}

	static public class Module extends Kind {
		/* package */Module(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindModule(this);
		}

		@Override
		public boolean isModule() {
			return true;
		}
	}

	static public class Tag extends Kind {
		/* package */Tag(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindTag(this);
		}

		@Override
		public boolean isTag() {
			return true;
		}
	}

	static public class Type extends Kind {
		/* package */Type(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindType(this);
		}

		@Override
		public boolean isType() {
			return true;
		}
	}

	static public class Variable extends Kind {
		/* package */Variable(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindVariable(this);
		}

		@Override
		public boolean isVariable() {
			return true;
		}
	}

	static public class View extends Kind {
		/* package */View(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitKindView(this);
		}

		@Override
		public boolean isView() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isAll() {
		return false;
	}

	public boolean isAnno() {
		return false;
	}

	public boolean isData() {
		return false;
	}

	public boolean isFunction() {
		return false;
	}

	public boolean isModule() {
		return false;
	}

	public boolean isTag() {
		return false;
	}

	public boolean isType() {
		return false;
	}

	public boolean isVariable() {
		return false;
	}

	public boolean isView() {
		return false;
	}
}