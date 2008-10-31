package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Strategy extends AbstractAST {
	static public class Ambiguity extends Strategy {
		private final java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Strategy> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Strategy> getAlternatives() {
			return alternatives;
		}
	}

	static public class BottomUp extends Strategy {
		/* package */BottomUp(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStrategyBottomUp(this);
		}
	}

	static public class BottomUpBreak extends Strategy {
		/* package */BottomUpBreak(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStrategyBottomUpBreak(this);
		}
	}

	static public class Innermost extends Strategy {
		/* package */Innermost(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStrategyInnermost(this);
		}
	}

	static public class Outermost extends Strategy {
		/* package */Outermost(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStrategyOutermost(this);
		}
	}

	static public class TopDown extends Strategy {
		/* package */TopDown(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStrategyTopDown(this);
		}
	}

	static public class TopDownBreak extends Strategy {
		/* package */TopDownBreak(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStrategyTopDownBreak(this);
		}
	}
}
