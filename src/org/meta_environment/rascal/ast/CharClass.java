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
		/* package */Bracket(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharClassBracket(this);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharClassComplement(this);
		}

		public org.meta_environment.rascal.ast.CharClass getCharClass() {
			return charClass;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharClassDifference(this);
		}

		public org.meta_environment.rascal.ast.CharClass getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.CharClass getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharClassIntersection(this);
		}

		public org.meta_environment.rascal.ast.CharClass getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.CharClass getRhs() {
			return rhs;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharClassSimpleCharclass(this);
		}

		public org.meta_environment.rascal.ast.OptCharRanges getOptionalCharRanges() {
			return optionalCharRanges;
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitCharClassUnion(this);
		}

		public org.meta_environment.rascal.ast.CharClass getLhs() {
			return lhs;
		}

		public org.meta_environment.rascal.ast.CharClass getRhs() {
			return rhs;
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
}
