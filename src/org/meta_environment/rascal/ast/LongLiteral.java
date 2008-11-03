package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class LongLiteral extends AbstractAST {
	static public class Ambiguity extends LongLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.LongLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitLongLiteralDecimalLongLiteral(this);
		}

		public org.meta_environment.rascal.ast.DecimalLongLiteral getDecimalLong() {
			return decimalLong;
		}

		public DecimalLongLiteral setDecimalLong(
				org.meta_environment.rascal.ast.DecimalLongLiteral x) {
			DecimalLongLiteral z = new DecimalLongLiteral();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitLongLiteralHexLongLiteral(this);
		}

		public org.meta_environment.rascal.ast.HexLongLiteral getHexLong() {
			return hexLong;
		}

		public HexLongLiteral setHexLong(
				org.meta_environment.rascal.ast.HexLongLiteral x) {
			HexLongLiteral z = new HexLongLiteral();
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

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitLongLiteralOctalLongLiteral(this);
		}

		public org.meta_environment.rascal.ast.OctalLongLiteral getOctalLong() {
			return octalLong;
		}

		public OctalLongLiteral setOctalLong(
				org.meta_environment.rascal.ast.OctalLongLiteral x) {
			OctalLongLiteral z = new OctalLongLiteral();
			z.$setOctalLong(x);
			return z;
		}
	}
}
