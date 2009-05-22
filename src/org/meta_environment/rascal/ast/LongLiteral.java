package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class LongLiteral extends AbstractAST {
	public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() {
		throw new UnsupportedOperationException();
	}

	public boolean hasDecimalLong() {
		return false;
	}

	public boolean isDecimalLongLiteral() {
		return false;
	}

	static public class DecimalLongLiteral extends LongLiteral {
		/*
		 * decimalLong:DecimalLongLiteral -> LongLiteral {prefer,
		 * cons("DecimalLongLiteral")}
		 */
		private DecimalLongLiteral() {
			super();
		}

		public DecimalLongLiteral(INode node,
				org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong) {
			this.node = node;
			this.decimalLong = decimalLong;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLongLiteralDecimalLongLiteral(this);
		}

		@Override
		public boolean isDecimalLongLiteral() {
			return true;
		}

		@Override
		public boolean hasDecimalLong() {
			return true;
		}

		private org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong;

		@Override
		public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() {
			return decimalLong;
		}

		private void $setDecimalLong(
				org.meta_environment.rascal.ast.DecimalLongLiteral x) {
			this.decimalLong = x;
		}

		public DecimalLongLiteral setDecimalLong(
				org.meta_environment.rascal.ast.DecimalLongLiteral x) {
			DecimalLongLiteral z = new DecimalLongLiteral();
			z.$setDecimalLong(x);
			return z;
		}
	}

	static public class Ambiguity extends LongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.LongLiteral> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitLongLiteralAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() {
		throw new UnsupportedOperationException();
	}

	public boolean hasHexLong() {
		return false;
	}

	public boolean isHexLongLiteral() {
		return false;
	}

	static public class HexLongLiteral extends LongLiteral {
		/*
		 * hexLong:HexLongLiteral -> LongLiteral {prefer,
		 * cons("HexLongLiteral")}
		 */
		private HexLongLiteral() {
			super();
		}

		public HexLongLiteral(INode node,
				org.meta_environment.rascal.ast.HexLongLiteral hexLong) {
			this.node = node;
			this.hexLong = hexLong;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLongLiteralHexLongLiteral(this);
		}

		@Override
		public boolean isHexLongLiteral() {
			return true;
		}

		@Override
		public boolean hasHexLong() {
			return true;
		}

		private org.meta_environment.rascal.ast.HexLongLiteral hexLong;

		@Override
		public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() {
			return hexLong;
		}

		private void $setHexLong(
				org.meta_environment.rascal.ast.HexLongLiteral x) {
			this.hexLong = x;
		}

		public HexLongLiteral setHexLong(
				org.meta_environment.rascal.ast.HexLongLiteral x) {
			HexLongLiteral z = new HexLongLiteral();
			z.$setHexLong(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() {
		throw new UnsupportedOperationException();
	}

	public boolean hasOctalLong() {
		return false;
	}

	public boolean isOctalLongLiteral() {
		return false;
	}

	static public class OctalLongLiteral extends LongLiteral {
		/*
		 * octalLong:OctalLongLiteral -> LongLiteral {prefer,
		 * cons("OctalLongLiteral")}
		 */
		private OctalLongLiteral() {
			super();
		}

		public OctalLongLiteral(INode node,
				org.meta_environment.rascal.ast.OctalLongLiteral octalLong) {
			this.node = node;
			this.octalLong = octalLong;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLongLiteralOctalLongLiteral(this);
		}

		@Override
		public boolean isOctalLongLiteral() {
			return true;
		}

		@Override
		public boolean hasOctalLong() {
			return true;
		}

		private org.meta_environment.rascal.ast.OctalLongLiteral octalLong;

		@Override
		public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() {
			return octalLong;
		}

		private void $setOctalLong(
				org.meta_environment.rascal.ast.OctalLongLiteral x) {
			this.octalLong = x;
		}

		public OctalLongLiteral setOctalLong(
				org.meta_environment.rascal.ast.OctalLongLiteral x) {
			OctalLongLiteral z = new OctalLongLiteral();
			z.$setOctalLong(x);
			return z;
		}
	}
}