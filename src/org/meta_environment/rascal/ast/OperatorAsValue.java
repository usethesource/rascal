package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class OperatorAsValue extends AbstractAST {
	public boolean isAddition() {
		return false;
	}

	static public class Addition extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Addition() {
		}

		/* package */Addition(INode node) {
			this.node = node;
		}

		@Override
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
				INode node,
				java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.OperatorAsValue> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitOperatorAsValueAmbiguity(this);
		}
	}

	public boolean isSubtraction() {
		return false;
	}

	static public class Subtraction extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Subtraction() {
		}

		/* package */Subtraction(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueSubtraction(this);
		}

		@Override
		public boolean isSubtraction() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isProduct() {
		return false;
	}

	static public class Product extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Product() {
		}

		/* package */Product(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueProduct(this);
		}

		@Override
		public boolean isProduct() {
			return true;
		}
	}

	public boolean isDivision() {
		return false;
	}

	static public class Division extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Division() {
		}

		/* package */Division(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueDivision(this);
		}

		@Override
		public boolean isDivision() {
			return true;
		}
	}

	public boolean isIntersection() {
		return false;
	}

	static public class Intersection extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Intersection() {
		}

		/* package */Intersection(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueIntersection(this);
		}

		@Override
		public boolean isIntersection() {
			return true;
		}
	}

	public boolean isEquals() {
		return false;
	}

	static public class Equals extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Equals() {
		}

		/* package */Equals(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueEquals(this);
		}

		@Override
		public boolean isEquals() {
			return true;
		}
	}

	public boolean isNotEquals() {
		return false;
	}

	static public class NotEquals extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private NotEquals() {
		}

		/* package */NotEquals(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueNotEquals(this);
		}

		@Override
		public boolean isNotEquals() {
			return true;
		}
	}

	public boolean isLessThan() {
		return false;
	}

	static public class LessThan extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private LessThan() {
		}

		/* package */LessThan(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueLessThan(this);
		}

		@Override
		public boolean isLessThan() {
			return true;
		}
	}

	public boolean isLessThanOrEq() {
		return false;
	}

	static public class LessThanOrEq extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private LessThanOrEq() {
		}

		/* package */LessThanOrEq(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueLessThanOrEq(this);
		}

		@Override
		public boolean isLessThanOrEq() {
			return true;
		}
	}

	public boolean isGreaterThan() {
		return false;
	}

	static public class GreaterThan extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private GreaterThan() {
		}

		/* package */GreaterThan(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueGreaterThan(this);
		}

		@Override
		public boolean isGreaterThan() {
			return true;
		}
	}

	public boolean isGreaterThanOrEq() {
		return false;
	}

	static public class GreaterThanOrEq extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private GreaterThanOrEq() {
		}

		/* package */GreaterThanOrEq(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueGreaterThanOrEq(this);
		}

		@Override
		public boolean isGreaterThanOrEq() {
			return true;
		}
	}

	public boolean isAnd() {
		return false;
	}

	static public class And extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private And() {
		}

		/* package */And(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueAnd(this);
		}

		@Override
		public boolean isAnd() {
			return true;
		}
	}

	public boolean isOr() {
		return false;
	}

	static public class Or extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Or() {
		}

		/* package */Or(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueOr(this);
		}

		@Override
		public boolean isOr() {
			return true;
		}
	}

	public boolean isNot() {
		return false;
	}

	static public class Not extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Not() {
		}

		/* package */Not(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueNot(this);
		}

		@Override
		public boolean isNot() {
			return true;
		}
	}

	public boolean isIn() {
		return false;
	}

	static public class In extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private In() {
		}

		/* package */In(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueIn(this);
		}

		@Override
		public boolean isIn() {
			return true;
		}
	}

	public boolean isNotIn() {
		return false;
	}

	static public class NotIn extends OperatorAsValue {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private NotIn() {
		}

		/* package */NotIn(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOperatorAsValueNotIn(this);
		}

		@Override
		public boolean isNotIn() {
			return true;
		}
	}
}