package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class CharRanges extends AbstractAST {
	static public class Ambiguity extends CharRanges {
		private final java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.CharRanges> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharRangesBracket(this);
		}

		public org.meta_environment.rascal.ast.CharRanges getRanges() {
			return ranges;
		}

		public Bracket setRanges(org.meta_environment.rascal.ast.CharRanges x) {
			Bracket z = new Bracket();
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

		public IVisitable accept(IASTVisitor visitor) {
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

		public Concatenate setLhs(org.meta_environment.rascal.ast.CharRanges x) {
			Concatenate z = new Concatenate();
			z.$setLhs(x);
			return z;
		}

		public Concatenate setRhs(org.meta_environment.rascal.ast.CharRanges x) {
			Concatenate z = new Concatenate();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharRangesRange(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharRange getRange() {
			return range;
		}

		public Range setRange(org.meta_environment.rascal.ast.CharRange x) {
			Range z = new Range();
			z.$setRange(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.CharRanges getLhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharRange getRange() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharRanges getRhs() {
		throw new UnsupportedOperationException();
	}
}
