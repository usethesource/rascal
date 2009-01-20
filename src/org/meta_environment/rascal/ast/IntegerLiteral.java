package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class IntegerLiteral extends AbstractAST {
	static public class Ambiguity extends IntegerLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitIntegerLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class DecimalIntegerLiteral extends IntegerLiteral {
		private org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal;

		/*
		 * decimal:DecimalIntegerLiteral -> IntegerLiteral {prefer,
		 * cons("DecimalIntegerLiteral")}
		 */
		private DecimalIntegerLiteral() {
		}

		/* package */DecimalIntegerLiteral(ITree tree,
				org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal) {
			this.tree = tree;
			this.decimal = decimal;
		}

		private void $setDecimal(
				org.meta_environment.rascal.ast.DecimalIntegerLiteral x) {
			this.decimal = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitIntegerLiteralDecimalIntegerLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.DecimalIntegerLiteral getDecimal() {
			return decimal;
		}

		@Override
		public boolean hasDecimal() {
			return true;
		}

		@Override
		public boolean isDecimalIntegerLiteral() {
			return true;
		}

		public DecimalIntegerLiteral setDecimal(
				org.meta_environment.rascal.ast.DecimalIntegerLiteral x) {
			final DecimalIntegerLiteral z = new DecimalIntegerLiteral();
			z.$setDecimal(x);
			return z;
		}
	}

	static public class HexIntegerLiteral extends IntegerLiteral {
		private org.meta_environment.rascal.ast.HexIntegerLiteral hex;

		/*
		 * hex:HexIntegerLiteral -> IntegerLiteral {prefer,
		 * cons("HexIntegerLiteral")}
		 */
		private HexIntegerLiteral() {
		}

		/* package */HexIntegerLiteral(ITree tree,
				org.meta_environment.rascal.ast.HexIntegerLiteral hex) {
			this.tree = tree;
			this.hex = hex;
		}

		private void $setHex(org.meta_environment.rascal.ast.HexIntegerLiteral x) {
			this.hex = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitIntegerLiteralHexIntegerLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.HexIntegerLiteral getHex() {
			return hex;
		}

		@Override
		public boolean hasHex() {
			return true;
		}

		@Override
		public boolean isHexIntegerLiteral() {
			return true;
		}

		public HexIntegerLiteral setHex(
				org.meta_environment.rascal.ast.HexIntegerLiteral x) {
			final HexIntegerLiteral z = new HexIntegerLiteral();
			z.$setHex(x);
			return z;
		}
	}

	static public class OctalIntegerLiteral extends IntegerLiteral {
		private org.meta_environment.rascal.ast.OctalIntegerLiteral octal;

		/*
		 * octal:OctalIntegerLiteral -> IntegerLiteral {prefer,
		 * cons("OctalIntegerLiteral")}
		 */
		private OctalIntegerLiteral() {
		}

		/* package */OctalIntegerLiteral(ITree tree,
				org.meta_environment.rascal.ast.OctalIntegerLiteral octal) {
			this.tree = tree;
			this.octal = octal;
		}

		private void $setOctal(
				org.meta_environment.rascal.ast.OctalIntegerLiteral x) {
			this.octal = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitIntegerLiteralOctalIntegerLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.OctalIntegerLiteral getOctal() {
			return octal;
		}

		@Override
		public boolean hasOctal() {
			return true;
		}

		@Override
		public boolean isOctalIntegerLiteral() {
			return true;
		}

		public OctalIntegerLiteral setOctal(
				org.meta_environment.rascal.ast.OctalIntegerLiteral x) {
			final OctalIntegerLiteral z = new OctalIntegerLiteral();
			z.$setOctal(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.DecimalIntegerLiteral getDecimal() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.HexIntegerLiteral getHex() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.OctalIntegerLiteral getOctal() {
		throw new UnsupportedOperationException();
	}

	public boolean hasDecimal() {
		return false;
	}

	public boolean hasHex() {
		return false;
	}

	public boolean hasOctal() {
		return false;
	}

	public boolean isDecimalIntegerLiteral() {
		return false;
	}

	public boolean isHexIntegerLiteral() {
		return false;
	}

	public boolean isOctalIntegerLiteral() {
		return false;
	}
}