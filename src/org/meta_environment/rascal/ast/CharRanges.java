package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class CharRanges extends AbstractAST {
	static public class Ambiguity extends CharRanges {
		private final java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCharRangesAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.CharRanges> getAlternatives() {
			return alternatives;
		}
	}

	static public class Bracket extends CharRanges {
		private org.meta_environment.rascal.ast.CharRanges ranges;

		/* "(" ranges:CharRanges ")" -> CharRanges {bracket, cons("Bracket")} */
		private Bracket() {
		}

		/* package */Bracket(ITree tree,
				org.meta_environment.rascal.ast.CharRanges ranges) {
			this.tree = tree;
			this.ranges = ranges;
		}

		private void $setRanges(org.meta_environment.rascal.ast.CharRanges x) {
			this.ranges = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharRangesBracket(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharRanges getRanges() {
			return ranges;
		}

		@Override
		public boolean hasRanges() {
			return true;
		}

		@Override
		public boolean isBracket() {
			return true;
		}

		public Bracket setRanges(org.meta_environment.rascal.ast.CharRanges x) {
			final Bracket z = new Bracket();
			z.$setRanges(x);
			return z;
		}
	}

	static public class Concatenate extends CharRanges {
		private org.meta_environment.rascal.ast.CharRanges lhs;
		private org.meta_environment.rascal.ast.CharRanges rhs;

		/*
		 * lhs:CharRanges rhs:CharRanges -> CharRanges {cons("Concatenate"),
		 * right, memo}
		 */
		private Concatenate() {
		}

		/* package */Concatenate(ITree tree,
				org.meta_environment.rascal.ast.CharRanges lhs,
				org.meta_environment.rascal.ast.CharRanges rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.CharRanges x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.CharRanges x) {
			this.rhs = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharRangesConcatenate(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharRanges getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.CharRanges getRhs() {
			return rhs;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		@Override
		public boolean isConcatenate() {
			return true;
		}

		public Concatenate setLhs(org.meta_environment.rascal.ast.CharRanges x) {
			final Concatenate z = new Concatenate();
			z.$setLhs(x);
			return z;
		}

		public Concatenate setRhs(org.meta_environment.rascal.ast.CharRanges x) {
			final Concatenate z = new Concatenate();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Range extends CharRanges {
		private org.meta_environment.rascal.ast.CharRange range;

		/* range:CharRange -> CharRanges {cons("Range")} */
		private Range() {
		}

		/* package */Range(ITree tree,
				org.meta_environment.rascal.ast.CharRange range) {
			this.tree = tree;
			this.range = range;
		}

		private void $setRange(org.meta_environment.rascal.ast.CharRange x) {
			this.range = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharRangesRange(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharRange getRange() {
			return range;
		}

		@Override
		public boolean hasRange() {
			return true;
		}

		@Override
		public boolean isRange() {
			return true;
		}

		public Range setRange(org.meta_environment.rascal.ast.CharRange x) {
			final Range z = new Range();
			z.$setRange(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.CharRanges getLhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharRange getRange() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharRanges getRanges() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharRanges getRhs() {
		throw new UnsupportedOperationException();
	}

	public boolean hasLhs() {
		return false;
	}

	public boolean hasRange() {
		return false;
	}

	public boolean hasRanges() {
		return false;
	}

	public boolean hasRhs() {
		return false;
	}

	public boolean isBracket() {
		return false;
	}

	public boolean isConcatenate() {
		return false;
	}

	public boolean isRange() {
		return false;
	}
}