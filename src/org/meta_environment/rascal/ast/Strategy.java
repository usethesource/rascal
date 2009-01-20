package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Strategy extends AbstractAST {
	static public class Ambiguity extends Strategy {
		private final java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStrategyAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Strategy> getAlternatives() {
			return alternatives;
		}
	}

	static public class BottomUp extends Strategy {
		/* package */BottomUp(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitStrategyBottomUp(this);
		}

		@Override
		public boolean isBottomUp() {
			return true;
		}
	}

	static public class BottomUpBreak extends Strategy {
		/* package */BottomUpBreak(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitStrategyBottomUpBreak(this);
		}

		@Override
		public boolean isBottomUpBreak() {
			return true;
		}
	}

	static public class Innermost extends Strategy {
		/* package */Innermost(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitStrategyInnermost(this);
		}

		@Override
		public boolean isInnermost() {
			return true;
		}
	}

	static public class Outermost extends Strategy {
		/* package */Outermost(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitStrategyOutermost(this);
		}

		@Override
		public boolean isOutermost() {
			return true;
		}
	}

	static public class TopDown extends Strategy {
		/* package */TopDown(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitStrategyTopDown(this);
		}

		@Override
		public boolean isTopDown() {
			return true;
		}
	}

	static public class TopDownBreak extends Strategy {
		/* package */TopDownBreak(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitStrategyTopDownBreak(this);
		}

		@Override
		public boolean isTopDownBreak() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isBottomUp() {
		return false;
	}

	public boolean isBottomUpBreak() {
		return false;
	}

	public boolean isInnermost() {
		return false;
	}

	public boolean isOutermost() {
		return false;
	}

	public boolean isTopDown() {
		return false;
	}

	public boolean isTopDownBreak() {
		return false;
	}
}