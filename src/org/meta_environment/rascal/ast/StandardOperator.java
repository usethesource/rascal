package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class StandardOperator extends AbstractAST {
	static public class Addition extends StandardOperator {
		/* package */Addition(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorAddition(this);
		}
	}

	static public class Ambiguity extends StandardOperator {
		private final java.util.List<org.meta_environment.rascal.ast.StandardOperator> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.StandardOperator> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.StandardOperator> getAlternatives() {
			return alternatives;
		}
	}

	static public class And extends StandardOperator {
		/* package */And(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorAnd(this);
		}
	}

	static public class Division extends StandardOperator {
		/* package */Division(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorDivision(this);
		}
	}

	static public class Equals extends StandardOperator {
		/* package */Equals(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorEquals(this);
		}
	}

	static public class GreaterThan extends StandardOperator {
		/* package */GreaterThan(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorGreaterThan(this);
		}
	}

	static public class GreaterThanOrEq extends StandardOperator {
		/* package */GreaterThanOrEq(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorGreaterThanOrEq(this);
		}
	}

	static public class In extends StandardOperator {
		/* package */In(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorIn(this);
		}
	}

	static public class Intersection extends StandardOperator {
		/* package */Intersection(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorIntersection(this);
		}
	}

	static public class LessThan extends StandardOperator {
		/* package */LessThan(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorLessThan(this);
		}
	}

	static public class LessThanOrEq extends StandardOperator {
		/* package */LessThanOrEq(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorLessThanOrEq(this);
		}
	}

	static public class Not extends StandardOperator {
		/* package */Not(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorNot(this);
		}
	}

	static public class NotEquals extends StandardOperator {
		/* package */NotEquals(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorNotEquals(this);
		}
	}

	static public class NotIn extends StandardOperator {
		/* package */NotIn(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorNotIn(this);
		}
	}

	static public class Or extends StandardOperator {
		/* package */Or(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorOr(this);
		}
	}

	static public class Product extends StandardOperator {
		/* package */Product(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorProduct(this);
		}
	}

	static public class Substraction extends StandardOperator {
		/* package */Substraction(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitStandardOperatorSubstraction(this);
		}
	}
}
