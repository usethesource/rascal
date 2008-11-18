package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class OperatorAsValue extends AbstractAST {
	static public class Addition extends OperatorAsValue {
		/* package */Addition(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueAddition(this);
		}

		@Override
		public boolean isAddition() {
			return true;
		}
	}

	static public class Ambiguity extends OperatorAsValue {
		private final java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> getAlternatives() {
			return alternatives;
		}
	}

	static public class And extends OperatorAsValue {
		/* package */And(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueAnd(this);
		}

		@Override
		public boolean isAnd() {
			return true;
		}
	}

	static public class Division extends OperatorAsValue {
		/* package */Division(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueDivision(this);
		}

		@Override
		public boolean isDivision() {
			return true;
		}
	}

	static public class Equals extends OperatorAsValue {
		/* package */Equals(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueEquals(this);
		}

		@Override
		public boolean isEquals() {
			return true;
		}
	}

	static public class GreaterThan extends OperatorAsValue {
		/* package */GreaterThan(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueGreaterThan(this);
		}

		@Override
		public boolean isGreaterThan() {
			return true;
		}
	}

	static public class GreaterThanOrEq extends OperatorAsValue {
		/* package */GreaterThanOrEq(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueGreaterThanOrEq(this);
		}

		@Override
		public boolean isGreaterThanOrEq() {
			return true;
		}
	}

	static public class In extends OperatorAsValue {
		/* package */In(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueIn(this);
		}

		@Override
		public boolean isIn() {
			return true;
		}
	}

	static public class Intersection extends OperatorAsValue {
		/* package */Intersection(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueIntersection(this);
		}

		@Override
		public boolean isIntersection() {
			return true;
		}
	}

	static public class LessThan extends OperatorAsValue {
		/* package */LessThan(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueLessThan(this);
		}

		@Override
		public boolean isLessThan() {
			return true;
		}
	}

	static public class LessThanOrEq extends OperatorAsValue {
		/* package */LessThanOrEq(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueLessThanOrEq(this);
		}

		@Override
		public boolean isLessThanOrEq() {
			return true;
		}
	}

	static public class Not extends OperatorAsValue {
		/* package */Not(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueNot(this);
		}

		@Override
		public boolean isNot() {
			return true;
		}
	}

	static public class NotEquals extends OperatorAsValue {
		/* package */NotEquals(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueNotEquals(this);
		}

		@Override
		public boolean isNotEquals() {
			return true;
		}
	}

	static public class NotIn extends OperatorAsValue {
		/* package */NotIn(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueNotIn(this);
		}

		@Override
		public boolean isNotIn() {
			return true;
		}
	}

	static public class Or extends OperatorAsValue {
		/* package */Or(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueOr(this);
		}

		@Override
		public boolean isOr() {
			return true;
		}
	}

	static public class Product extends OperatorAsValue {
		/* package */Product(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueProduct(this);
		}

		@Override
		public boolean isProduct() {
			return true;
		}
	}

	static public class Substraction extends OperatorAsValue {
		/* package */Substraction(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueSubstraction(this);
		}

		@Override
		public boolean isSubstraction() {
			return true;
		}
	}

	public boolean isAddition() {
		return false;
	}

	public boolean isAnd() {
		return false;
	}

	public boolean isDivision() {
		return false;
	}

	public boolean isEquals() {
		return false;
	}

	public boolean isGreaterThan() {
		return false;
	}

	public boolean isGreaterThanOrEq() {
		return false;
	}

	public boolean isIn() {
		return false;
	}

	public boolean isIntersection() {
		return false;
	}

	public boolean isLessThan() {
		return false;
	}

	public boolean isLessThanOrEq() {
		return false;
	}

	public boolean isNot() {
		return false;
	}

	public boolean isNotEquals() {
		return false;
	}

	public boolean isNotIn() {
		return false;
	}

	public boolean isOr() {
		return false;
	}

	public boolean isProduct() {
		return false;
	}

	public boolean isSubstraction() {
		return false;
	}
}
