package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class IntegerLiteral extends AbstractAST {
	public org.meta_environment.rascal.ast.DecimalIntegerLiteral getDecimal() {
		throw new UnsupportedOperationException();
	}

	public boolean hasDecimal() {
		return false;
	}

	public boolean isDecimalIntegerLiteral() {
		return false;
	}

	static public class DecimalIntegerLiteral extends IntegerLiteral {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private DecimalIntegerLiteral() {
		}

		/* package */DecimalIntegerLiteral(INode node,
				org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal) {
			this.node = node;
			this.decimal = decimal;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitIntegerLiteralDecimalIntegerLiteral(this);
		}

		@Override
		public boolean isDecimalIntegerLiteral() {
			return true;
		}

		@Override
		public boolean hasDecimal() {
			return true;
		}

		private org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal;

		@Override
		public org.meta_environment.rascal.ast.DecimalIntegerLiteral getDecimal() {
			return decimal;
		}

		private void $setDecimal(
				org.meta_environment.rascal.ast.DecimalIntegerLiteral x) {
			this.decimal = x;
		}

		public DecimalIntegerLiteral setDecimal(
				org.meta_environment.rascal.ast.DecimalIntegerLiteral x) {
			DecimalIntegerLiteral z = new DecimalIntegerLiteral();
			z.$setDecimal(x);
			return z;
		}
	}

	static public class Ambiguity extends IntegerLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitIntegerLiteralAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.HexIntegerLiteral getHex() {
		throw new UnsupportedOperationException();
	}

	public boolean hasHex() {
		return false;
	}

	public boolean isHexIntegerLiteral() {
		return false;
	}

	static public class HexIntegerLiteral extends IntegerLiteral {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private HexIntegerLiteral() {
		}

		/* package */HexIntegerLiteral(INode node,
				org.meta_environment.rascal.ast.HexIntegerLiteral hex) {
			this.node = node;
			this.hex = hex;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitIntegerLiteralHexIntegerLiteral(this);
		}

		@Override
		public boolean isHexIntegerLiteral() {
			return true;
		}

		@Override
		public boolean hasHex() {
			return true;
		}

		private org.meta_environment.rascal.ast.HexIntegerLiteral hex;

		@Override
		public org.meta_environment.rascal.ast.HexIntegerLiteral getHex() {
			return hex;
		}

		private void $setHex(org.meta_environment.rascal.ast.HexIntegerLiteral x) {
			this.hex = x;
		}

		public HexIntegerLiteral setHex(
				org.meta_environment.rascal.ast.HexIntegerLiteral x) {
			HexIntegerLiteral z = new HexIntegerLiteral();
			z.$setHex(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.OctalIntegerLiteral getOctal() {
		throw new UnsupportedOperationException();
	}

	public boolean hasOctal() {
		return false;
	}

	public boolean isOctalIntegerLiteral() {
		return false;
	}

	static public class OctalIntegerLiteral extends IntegerLiteral {
		/** &syms -> &sort {&attr*1, cons(&strcon), &attr*2} */
		private OctalIntegerLiteral() {
		}

		/* package */OctalIntegerLiteral(INode node,
				org.meta_environment.rascal.ast.OctalIntegerLiteral octal) {
			this.node = node;
			this.octal = octal;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitIntegerLiteralOctalIntegerLiteral(this);
		}

		@Override
		public boolean isOctalIntegerLiteral() {
			return true;
		}

		@Override
		public boolean hasOctal() {
			return true;
		}

		private org.meta_environment.rascal.ast.OctalIntegerLiteral octal;

		@Override
		public org.meta_environment.rascal.ast.OctalIntegerLiteral getOctal() {
			return octal;
		}

		private void $setOctal(
				org.meta_environment.rascal.ast.OctalIntegerLiteral x) {
			this.octal = x;
		}

		public OctalIntegerLiteral setOctal(
				org.meta_environment.rascal.ast.OctalIntegerLiteral x) {
			OctalIntegerLiteral z = new OctalIntegerLiteral();
			z.$setOctal(x);
			return z;
		}
	}
}