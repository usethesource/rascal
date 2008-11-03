package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class IntegerLiteral extends AbstractAST {
	static public class Ambiguity extends IntegerLiteral {
		private final java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.IntegerLiteral> getAlternatives() {
			return alternatives;
		}
	}

	static public class DecimalIntegerLiteral extends IntegerLiteral {
		/* package */DecimalIntegerLiteral(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitIntegerLiteralDecimalIntegerLiteral(this);
		}
	}

	static public class HexIntegerLiteral extends IntegerLiteral {
		/* package */HexIntegerLiteral(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitIntegerLiteralHexIntegerLiteral(this);
		}
	}

	static public class OctalIntegerLiteral extends IntegerLiteral {
		/* package */OctalIntegerLiteral(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitIntegerLiteralOctalIntegerLiteral(this);
		}
	}
}
