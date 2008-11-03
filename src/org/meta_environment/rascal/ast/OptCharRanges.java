package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class OptCharRanges extends AbstractAST {
	static public class Absent extends OptCharRanges {
		/* package */Absent(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitOptCharRangesAbsent(this);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitOptCharRangesPresent(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharRanges getRanges() {
			return ranges;
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
}
