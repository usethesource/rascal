package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Literal extends AbstractAST {
	static public class Ambiguity extends Literal {
		private final java.util.List<org.meta_environment.rascal.ast.Literal> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Literal> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Literal> getAlternatives() {
			return alternatives;
		}
	}

	static public class Boolean extends Literal {
		private org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral;

		/* booleanLiteral:BooleanLiteral -> Literal {cons("Boolean")} */
		private Boolean() {
		}

		/* package */Boolean(ITree tree,
				org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral) {
			this.tree = tree;
			this.booleanLiteral = booleanLiteral;
		}

		private void $setBooleanLiteral(
				org.meta_environment.rascal.ast.BooleanLiteral x) {
			this.booleanLiteral = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLiteralBoolean(this);
		}

		@Override
		public org.meta_environment.rascal.ast.BooleanLiteral getBooleanLiteral() {
			return booleanLiteral;
		}

		@Override
		public boolean hasBooleanLiteral() {
			return true;
		}

		@Override
		public boolean isBoolean() {
			return true;
		}

		public Boolean setBooleanLiteral(
				org.meta_environment.rascal.ast.BooleanLiteral x) {
			Boolean z = new Boolean();
			z.$setBooleanLiteral(x);
			return z;
		}
	}

	static public class Double extends Literal {
		private org.meta_environment.rascal.ast.DoubleLiteral doubleLiteral;

		/* doubleLiteral:DoubleLiteral -> Literal {cons("Double")} */
		private Double() {
		}

		/* package */Double(ITree tree,
				org.meta_environment.rascal.ast.DoubleLiteral doubleLiteral) {
			this.tree = tree;
			this.doubleLiteral = doubleLiteral;
		}

		private void $setDoubleLiteral(
				org.meta_environment.rascal.ast.DoubleLiteral x) {
			this.doubleLiteral = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLiteralDouble(this);
		}

		@Override
		public org.meta_environment.rascal.ast.DoubleLiteral getDoubleLiteral() {
			return doubleLiteral;
		}

		@Override
		public boolean hasDoubleLiteral() {
			return true;
		}

		@Override
		public boolean isDouble() {
			return true;
		}

		public Double setDoubleLiteral(
				org.meta_environment.rascal.ast.DoubleLiteral x) {
			Double z = new Double();
			z.$setDoubleLiteral(x);
			return z;
		}
	}

	static public class Integer extends Literal {
		private org.meta_environment.rascal.ast.IntegerLiteral integerLiteral;

		/* integerLiteral:IntegerLiteral -> Literal {cons("Integer")} */
		private Integer() {
		}

		/* package */Integer(ITree tree,
				org.meta_environment.rascal.ast.IntegerLiteral integerLiteral) {
			this.tree = tree;
			this.integerLiteral = integerLiteral;
		}

		private void $setIntegerLiteral(
				org.meta_environment.rascal.ast.IntegerLiteral x) {
			this.integerLiteral = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLiteralInteger(this);
		}

		@Override
		public org.meta_environment.rascal.ast.IntegerLiteral getIntegerLiteral() {
			return integerLiteral;
		}

		@Override
		public boolean hasIntegerLiteral() {
			return true;
		}

		@Override
		public boolean isInteger() {
			return true;
		}

		public Integer setIntegerLiteral(
				org.meta_environment.rascal.ast.IntegerLiteral x) {
			Integer z = new Integer();
			z.$setIntegerLiteral(x);
			return z;
		}
	}

	static public class RegExp extends Literal {
		private org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral;

		/* regExpLiteral:RegExpLiteral -> Literal {cons("RegExp")} */
		private RegExp() {
		}

		/* package */RegExp(ITree tree,
				org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral) {
			this.tree = tree;
			this.regExpLiteral = regExpLiteral;
		}

		private void $setRegExpLiteral(
				org.meta_environment.rascal.ast.RegExpLiteral x) {
			this.regExpLiteral = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLiteralRegExp(this);
		}

		@Override
		public org.meta_environment.rascal.ast.RegExpLiteral getRegExpLiteral() {
			return regExpLiteral;
		}

		@Override
		public boolean hasRegExpLiteral() {
			return true;
		}

		@Override
		public boolean isRegExp() {
			return true;
		}

		public RegExp setRegExpLiteral(
				org.meta_environment.rascal.ast.RegExpLiteral x) {
			RegExp z = new RegExp();
			z.$setRegExpLiteral(x);
			return z;
		}
	}

	static public class String extends Literal {
		private org.meta_environment.rascal.ast.StringLiteral stringLiteral;

		/* stringLiteral:StringLiteral -> Literal {cons("String")} */
		private String() {
		}

		/* package */String(ITree tree,
				org.meta_environment.rascal.ast.StringLiteral stringLiteral) {
			this.tree = tree;
			this.stringLiteral = stringLiteral;
		}

		private void $setStringLiteral(
				org.meta_environment.rascal.ast.StringLiteral x) {
			this.stringLiteral = x;
		}

		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLiteralString(this);
		}

		@Override
		public org.meta_environment.rascal.ast.StringLiteral getStringLiteral() {
			return stringLiteral;
		}

		@Override
		public boolean hasStringLiteral() {
			return true;
		}

		@Override
		public boolean isString() {
			return true;
		}

		public String setStringLiteral(
				org.meta_environment.rascal.ast.StringLiteral x) {
			String z = new String();
			z.$setStringLiteral(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.BooleanLiteral getBooleanLiteral() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.DoubleLiteral getDoubleLiteral() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.IntegerLiteral getIntegerLiteral() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.RegExpLiteral getRegExpLiteral() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.StringLiteral getStringLiteral() {
		throw new UnsupportedOperationException();
	}

	public boolean hasBooleanLiteral() {
		return false;
	}

	public boolean hasDoubleLiteral() {
		return false;
	}

	public boolean hasIntegerLiteral() {
		return false;
	}

	public boolean hasRegExpLiteral() {
		return false;
	}

	public boolean hasStringLiteral() {
		return false;
	}

	public boolean isBoolean() {
		return false;
	}

	public boolean isDouble() {
		return false;
	}

	public boolean isInteger() {
		return false;
	}

	public boolean isRegExp() {
		return false;
	}

	public boolean isString() {
		return false;
	}
}
