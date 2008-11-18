package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class OptCharRanges extends AbstractAST {
	static public class Absent extends OptCharRanges {
		/* package */Absent(ITree tree) {
			this.tree = tree;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOptCharRangesAbsent(this);
		}

		@Override
		public boolean isAbsent() {
			return true;
		}
	}

	static public class Ambiguity extends OptCharRanges {
		private final java.util.List<org.meta_environment.rascal.ast.OptCharRanges> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.OptCharRanges> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.OptCharRanges> getAlternatives() {
			return alternatives;
		}
	}

	static public class Present extends OptCharRanges {
		private org.meta_environment.rascal.ast.CharRanges ranges;

		/* ranges:CharRanges -> OptCharRanges {cons("Present")} */
		private Present() {
		}

		/* package */Present(ITree tree,
				org.meta_environment.rascal.ast.CharRanges ranges) {
			this.tree = tree;
			this.ranges = ranges;
		}

		private void $setRanges(org.meta_environment.rascal.ast.CharRanges x) {
			this.ranges = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitOptCharRangesPresent(this);
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
		public boolean isPresent() {
			return true;
		}

		public Present setRanges(org.meta_environment.rascal.ast.CharRanges x) {
			Present z = new Present();
			z.$setRanges(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.CharRanges getRanges() {
		throw new UnsupportedOperationException();
	}

	public boolean hasRanges() {
		return false;
	}

	public boolean isAbsent() {
		return false;
	}

	public boolean isPresent() {
		return false;
	}
}
