package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class LongLiteral extends AbstractAST {
	static public class Ambiguity extends LongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitLongLiteralAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.LongLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class DecimalLongLiteral extends LongLiteral {
		private org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong;

		/*
		 * decimalLong:DecimalLongLiteral -> LongLiteral {prefer,
		 * cons("DecimalLongLiteral")}
		 */
		private DecimalLongLiteral() {
		}

		/* package */DecimalLongLiteral(ITree tree,
				org.meta_environment.rascal.ast.DecimalLongLiteral decimalLong) {
			this.tree = tree;
			this.decimalLong = decimalLong;
		}

		private void $setDecimalLong(
				org.meta_environment.rascal.ast.DecimalLongLiteral x) {
			this.decimalLong = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLongLiteralDecimalLongLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() {
			return decimalLong;
		}

		@Override
		public boolean hasDecimalLong() {
			return true;
		}

		@Override
		public boolean isDecimalLongLiteral() {
			return true;
		}

		public DecimalLongLiteral setDecimalLong(
				org.meta_environment.rascal.ast.DecimalLongLiteral x) {
			final DecimalLongLiteral z = new DecimalLongLiteral();
			z.$setDecimalLong(x);
			return z;
		}
	}

	static public class HexLongLiteral extends LongLiteral {
		private org.meta_environment.rascal.ast.HexLongLiteral hexLong;

		/*
		 * hexLong:HexLongLiteral -> LongLiteral {prefer,
		 * cons("HexLongLiteral")}
		 */
		private HexLongLiteral() {
		}

		/* package */HexLongLiteral(ITree tree,
				org.meta_environment.rascal.ast.HexLongLiteral hexLong) {
			this.tree = tree;
			this.hexLong = hexLong;
		}

		private void $setHexLong(
				org.meta_environment.rascal.ast.HexLongLiteral x) {
			this.hexLong = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLongLiteralHexLongLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() {
			return hexLong;
		}

		@Override
		public boolean hasHexLong() {
			return true;
		}

		@Override
		public boolean isHexLongLiteral() {
			return true;
		}

		public HexLongLiteral setHexLong(
				org.meta_environment.rascal.ast.HexLongLiteral x) {
			final HexLongLiteral z = new HexLongLiteral();
			z.$setHexLong(x);
			return z;
		}
	}

	static public class OctalLongLiteral extends LongLiteral {
		private org.meta_environment.rascal.ast.OctalLongLiteral octalLong;

		/*
		 * octalLong:OctalLongLiteral -> LongLiteral {prefer,
		 * cons("OctalLongLiteral")}
		 */
		private OctalLongLiteral() {
		}

		/* package */OctalLongLiteral(ITree tree,
				org.meta_environment.rascal.ast.OctalLongLiteral octalLong) {
			this.tree = tree;
			this.octalLong = octalLong;
		}

		private void $setOctalLong(
				org.meta_environment.rascal.ast.OctalLongLiteral x) {
			this.octalLong = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitLongLiteralOctalLongLiteral(this);
		}

		@Override
		public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() {
			return octalLong;
		}

		@Override
		public boolean hasOctalLong() {
			return true;
		}

		@Override
		public boolean isOctalLongLiteral() {
			return true;
		}

		public OctalLongLiteral setOctalLong(
				org.meta_environment.rascal.ast.OctalLongLiteral x) {
			final OctalLongLiteral z = new OctalLongLiteral();
			z.$setOctalLong(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() {
		throw new UnsupportedOperationException();
	}

	public boolean hasDecimalLong() {
		return false;
	}

	public boolean hasHexLong() {
		return false;
	}

	public boolean hasOctalLong() {
		return false;
	}

	public boolean isDecimalLongLiteral() {
		return false;
	}

	public boolean isHexLongLiteral() {
		return false;
	}

	public boolean isOctalLongLiteral() {
		return false;
	}
}