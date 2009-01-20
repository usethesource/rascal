package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Assignment extends AbstractAST {
	static public class Addition extends Assignment {
		/* package */Addition(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentAddition(this);
		}

		@Override
		public boolean isAddition() {
			return true;
		}
	}

	static public class Ambiguity extends Assignment {
		private final java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitAssignmentAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Assignment> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Assignment {
		/* package */Default(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentDefault(this);
		}

		@Override
		public boolean isDefault() {
			return true;
		}
	}

	static public class Division extends Assignment {
		/* package */Division(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentDivision(this);
		}

		@Override
		public boolean isDivision() {
			return true;
		}
	}

	static public class Intersection extends Assignment {
		/* package */Intersection(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentIntersection(this);
		}

		@Override
		public boolean isIntersection() {
			return true;
		}
	}

	static public class Product extends Assignment {
		/* package */Product(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentProduct(this);
		}

		@Override
		public boolean isProduct() {
			return true;
		}
	}

	static public class Subtraction extends Assignment {
		/* package */Subtraction(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentSubtraction(this);
		}

		@Override
		public boolean isSubtraction() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isAddition() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isDivision() {
		return false;
	}

	public boolean isIntersection() {
		return false;
	}

	public boolean isProduct() {
		return false;
	}

	public boolean isSubtraction() {
		return false;
	}
}