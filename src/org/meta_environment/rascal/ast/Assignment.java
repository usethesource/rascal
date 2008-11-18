package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Assignment extends AbstractAST {
	static public class Addition extends Assignment {
		/* package */Addition(ITree tree) {
			this.tree = tree;
		}

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
				java.util.List<org.meta_environment.rascal.ast.Assignment> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Assignment> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Assignment {
		/* package */Default(ITree tree) {
			this.tree = tree;
		}

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

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentDivision(this);
		}

		@Override
		public boolean isDivision() {
			return true;
		}
	}

	static public class Interesection extends Assignment {
		/* package */Interesection(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentInteresection(this);
		}

		@Override
		public boolean isInteresection() {
			return true;
		}
	}

	static public class Product extends Assignment {
		/* package */Product(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentProduct(this);
		}

		@Override
		public boolean isProduct() {
			return true;
		}
	}

	static public class Substraction extends Assignment {
		/* package */Substraction(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitAssignmentSubstraction(this);
		}

		@Override
		public boolean isSubstraction() {
			return true;
		}
	}

	public boolean isAddition() {
		return false;
	}

	public boolean isDefault() {
		return false;
	}

	public boolean isDivision() {
		return false;
	}

	public boolean isInteresection() {
		return false;
	}

	public boolean isProduct() {
		return false;
	}

	public boolean isSubstraction() {
		return false;
	}
}
