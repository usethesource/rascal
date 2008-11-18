package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class CharClass extends AbstractAST {
	static public class Ambiguity extends CharClass {
		private final java.util.List<org.meta_environment.rascal.ast.CharClass> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.CharClass> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.CharClass> getAlternatives() {
			return alternatives;
		}
	}

	static public class Bracket extends CharClass {
		private org.meta_environment.rascal.ast.CharClass charClass;

		/*
		 * "(" charClass:CharClass ")" -> CharClass {bracket, cons("Bracket"),
		 * avoid}
		 */
		private Bracket() {
		}

		/* package */Bracket(ITree tree,
				org.meta_environment.rascal.ast.CharClass charClass) {
			this.tree = tree;
			this.charClass = charClass;
		}

		private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) {
			this.charClass = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharClassBracket(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getCharClass() {
			return charClass;
		}

		@Override
		public boolean hasCharClass() {
			return true;
		}

		@Override
		public boolean isBracket() {
			return true;
		}

		public Bracket setCharClass(org.meta_environment.rascal.ast.CharClass x) {
			Bracket z = new Bracket();
			z.$setCharClass(x);
			return z;
		}
	}

	static public class Complement extends CharClass {
		private org.meta_environment.rascal.ast.CharClass charClass;

		/* "~" charClass:CharClass -> CharClass {cons("Complement")} */
		private Complement() {
		}

		/* package */Complement(ITree tree,
				org.meta_environment.rascal.ast.CharClass charClass) {
			this.tree = tree;
			this.charClass = charClass;
		}

		private void $setCharClass(org.meta_environment.rascal.ast.CharClass x) {
			this.charClass = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharClassComplement(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getCharClass() {
			return charClass;
		}

		@Override
		public boolean hasCharClass() {
			return true;
		}

		@Override
		public boolean isComplement() {
			return true;
		}

		public Complement setCharClass(
				org.meta_environment.rascal.ast.CharClass x) {
			Complement z = new Complement();
			z.$setCharClass(x);
			return z;
		}
	}

	static public class Difference extends CharClass {
		private org.meta_environment.rascal.ast.CharClass lhs;
		private org.meta_environment.rascal.ast.CharClass rhs;

		/*
		 * lhs:CharClass "/" rhs:CharClass -> CharClass {cons("Difference"),
		 * left, memo}
		 */
		private Difference() {
		}

		/* package */Difference(ITree tree,
				org.meta_environment.rascal.ast.CharClass lhs,
				org.meta_environment.rascal.ast.CharClass rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.CharClass x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.CharClass x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharClassDifference(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getRhs() {
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
		public boolean isDifference() {
			return true;
		}

		public Difference setLhs(org.meta_environment.rascal.ast.CharClass x) {
			Difference z = new Difference();
			z.$setLhs(x);
			return z;
		}

		public Difference setRhs(org.meta_environment.rascal.ast.CharClass x) {
			Difference z = new Difference();
			z.$setRhs(x);
			return z;
		}
	}

	static public class Intersection extends CharClass {
		private org.meta_environment.rascal.ast.CharClass lhs;
		private org.meta_environment.rascal.ast.CharClass rhs;

		/*
		 * lhs:CharClass "/\\" rhs:CharClass -> CharClass {cons("Intersection"),
		 * left, memo}
		 */
		private Intersection() {
		}

		/* package */Intersection(ITree tree,
				org.meta_environment.rascal.ast.CharClass lhs,
				org.meta_environment.rascal.ast.CharClass rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.CharClass x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.CharClass x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharClassIntersection(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getRhs() {
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
		public boolean isIntersection() {
			return true;
		}

		public Intersection setLhs(org.meta_environment.rascal.ast.CharClass x) {
			Intersection z = new Intersection();
			z.$setLhs(x);
			return z;
		}

		public Intersection setRhs(org.meta_environment.rascal.ast.CharClass x) {
			Intersection z = new Intersection();
			z.$setRhs(x);
			return z;
		}
	}

	static public class SimpleCharclass extends CharClass {
		private org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges;

		/*
		 * "[" optionalCharRanges:OptCharRanges "]" -> CharClass
		 * {cons("SimpleCharclass")}
		 */
		private SimpleCharclass() {
		}

		/* package */SimpleCharclass(ITree tree,
				org.meta_environment.rascal.ast.OptCharRanges optionalCharRanges) {
			this.tree = tree;
			this.optionalCharRanges = optionalCharRanges;
		}

		private void $setOptionalCharRanges(
				org.meta_environment.rascal.ast.OptCharRanges x) {
			this.optionalCharRanges = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharClassSimpleCharclass(this);
		}

		@Override
		public org.meta_environment.rascal.ast.OptCharRanges getOptionalCharRanges() {
			return optionalCharRanges;
		}

		@Override
		public boolean hasOptionalCharRanges() {
			return true;
		}

		@Override
		public boolean isSimpleCharclass() {
			return true;
		}

		public SimpleCharclass setOptionalCharRanges(
				org.meta_environment.rascal.ast.OptCharRanges x) {
			SimpleCharclass z = new SimpleCharclass();
			z.$setOptionalCharRanges(x);
			return z;
		}
	}

	static public class Union extends CharClass {
		private org.meta_environment.rascal.ast.CharClass lhs;
		private org.meta_environment.rascal.ast.CharClass rhs;

		/* lhs:CharClass "\\/" rhs:CharClass -> CharClass {cons("Union"), left} */
		private Union() {
		}

		/* package */Union(ITree tree,
				org.meta_environment.rascal.ast.CharClass lhs,
				org.meta_environment.rascal.ast.CharClass rhs) {
			this.tree = tree;
			this.lhs = lhs;
			this.rhs = rhs;
		}

		private void $setLhs(org.meta_environment.rascal.ast.CharClass x) {
			this.lhs = x;
		}

		private void $setRhs(org.meta_environment.rascal.ast.CharClass x) {
			this.rhs = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitCharClassUnion(this);
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getLhs() {
			return lhs;
		}

		@Override
		public org.meta_environment.rascal.ast.CharClass getRhs() {
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
		public boolean isUnion() {
			return true;
		}

		public Union setLhs(org.meta_environment.rascal.ast.CharClass x) {
			Union z = new Union();
			z.$setLhs(x);
			return z;
		}

		public Union setRhs(org.meta_environment.rascal.ast.CharClass x) {
			Union z = new Union();
			z.$setRhs(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.CharClass getCharClass() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharClass getLhs() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.OptCharRanges getOptionalCharRanges() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.CharClass getRhs() {
		throw new UnsupportedOperationException();
	}

	public boolean hasCharClass() {
		return false;
	}

	public boolean hasLhs() {
		return false;
	}

	public boolean hasOptionalCharRanges() {
		return false;
	}

	public boolean hasRhs() {
		return false;
	}

	public boolean isBracket() {
		return false;
	}

	public boolean isComplement() {
		return false;
	}

	public boolean isDifference() {
		return false;
	}

	public boolean isIntersection() {
		return false;
	}

	public boolean isSimpleCharclass() {
		return false;
	}

	public boolean isUnion() {
		return false;
	}
}
