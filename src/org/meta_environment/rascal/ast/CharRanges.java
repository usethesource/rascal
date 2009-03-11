package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class CharRanges extends AbstractAST {
	public org.meta_environment.rascal.ast.CharRange getRange() {
		throw new UnsupportedOperationException();
	}

	public boolean hasRange() {
		return false;
	}

	public boolean isRange() {
		return false;
	}

	static public class Range extends CharRanges {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Range() {
		}

		/* package */Range(INode node,
				org.meta_environment.rascal.ast.CharRange range) {
			this.node = node;
			this.range = range;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharRangesRange(this);
		}

		@Override
		public boolean isRange() {
			return true;
		}

		@Override
		public boolean hasRange() {
			return true;
		}

		private org.meta_environment.rascal.ast.CharRange range;

		@Override
		public org.meta_environment.rascal.ast.CharRange getRange() {
			return range;
		}

		private void $setRange(org.meta_environment.rascal.ast.CharRange x) {
			this.range = x;
		}

		public Range setRange(org.meta_environment.rascal.ast.CharRange x) {
			Range z = new Range();
			z.$setRange(x);
			return z;
		}
	}

	static public class Ambiguity extends CharRanges {
		private final java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.CharRanges> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitCharRangesAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.CharRanges getLhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharRanges getRhs() {
		throw new UnsupportedOperationException();
	}

	public boolean hasLhs() {
		return false;
	}

	public boolean hasRhs() {
		return false;
	}

	public boolean isConcatenate() {
		return false;
	}

	static public class Concatenate extends CharRanges {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Concatenate() {
		}

		/* package */Concatenate(INode node,
				org.meta_environment.rascal.ast.CharRanges lhs,
				org.meta_environment.rascal.ast.CharRanges rhs) {
			this.node = node;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharRangesConcatenate(this);
		}

		@Override
		public boolean isConcatenate() {
			return true;
		}

		@Override
		public boolean hasLhs() {
			return true;
		}

		@Override
		public boolean hasRhs() {
			return true;
		}

		private org.meta_environment.rascal.ast.CharRanges lhs;

		@Override
		public org.meta_environment.rascal.ast.CharRanges getLhs() {
			return lhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.CharRanges x) {
			this.lhs = x;
		}

		public Concatenate setLhs(org.meta_environment.rascal.ast.CharRanges x) {
			Concatenate z = new Concatenate();
			z.$setLhs(x);
			return z;
		}

		private org.meta_environment.rascal.ast.CharRanges rhs;

		@Override
		public org.meta_environment.rascal.ast.CharRanges getRhs() {
			return rhs;
		}

		private void $setRhs(org.meta_environment.rascal.ast.CharRanges x) {
			this.rhs = x;
		}

		public Concatenate setRhs(org.meta_environment.rascal.ast.CharRanges x) {
			Concatenate z = new Concatenate();
			z.$setRhs(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.CharRanges getRanges() {
		throw new UnsupportedOperationException();
	}

	public boolean hasRanges() {
		return false;
	}

	public boolean isBracket() {
		return false;
	}

	static public class Bracket extends CharRanges {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private Bracket() {
		}

		/* package */Bracket(INode node,
				org.meta_environment.rascal.ast.CharRanges ranges) {
			this.node = node;
			this.ranges = ranges;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharRangesBracket(this);
		}

		@Override
		public boolean isBracket() {
			return true;
		}

		@Override
		public boolean hasRanges() {
			return true;
		}

		private org.meta_environment.rascal.ast.CharRanges ranges;

		@Override
		public org.meta_environment.rascal.ast.CharRanges getRanges() {
			return ranges;
		}

		private void $setRanges(org.meta_environment.rascal.ast.CharRanges x) {
			this.ranges = x;
		}

		public Bracket setRanges(org.meta_environment.rascal.ast.CharRanges x) {
			Bracket z = new Bracket();
			z.$setRanges(x);
			return z;
		}
	}
}