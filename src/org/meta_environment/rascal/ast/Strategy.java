package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Strategy extends AbstractAST {
	public boolean isTopDown() {
		return false;
	}

	static public class TopDown extends Strategy {
		/* "top-down" -> Strategy {cons("TopDown")} */
		private TopDown() {
		}

		/* package */TopDown(INode node) {
			this.node = node;
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

	static public class Ambiguity extends Strategy {
		private final java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Strategy> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitStrategyAmbiguity(this);
		}
	}

	public boolean isTopDownBreak() {
		return false;
	}

	static public class TopDownBreak extends Strategy {
		/* "top-down-break" -> Strategy {cons("TopDownBreak")} */
		private TopDownBreak() {
		}

		/* package */TopDownBreak(INode node) {
			this.node = node;
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

	static public class BottomUp extends Strategy {
		/* "bottom-up" -> Strategy {cons("BottomUp")} */
		private BottomUp() {
		}

		/* package */BottomUp(INode node) {
			this.node = node;
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

	public boolean isBottomUpBreak() {
		return false;
	}

	static public class BottomUpBreak extends Strategy {
		/* "bottom-up-break" -> Strategy {cons("BottomUpBreak")} */
		private BottomUpBreak() {
		}

		/* package */BottomUpBreak(INode node) {
			this.node = node;
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

	public boolean isOutermost() {
		return false;
	}

	static public class Outermost extends Strategy {
		/* "outermost" -> Strategy {cons("Outermost")} */
		private Outermost() {
		}

		/* package */Outermost(INode node) {
			this.node = node;
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

	public boolean isInnermost() {
		return false;
	}

	static public class Innermost extends Strategy {
		/* "innermost" -> Strategy {cons("Innermost")} */
		private Innermost() {
		}

		/* package */Innermost(INode node) {
			this.node = node;
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
}