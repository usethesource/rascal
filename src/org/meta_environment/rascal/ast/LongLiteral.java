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
		/* package */DecimalLongLiteral(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitLongLiteralDecimalLongLiteral(this);
		}
	}

	static public class HexLongLiteral extends LongLiteral {
		/* package */HexLongLiteral(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitLongLiteralHexLongLiteral(this);
		}
	}

	static public class OctalLongLiteral extends LongLiteral {
		/* package */OctalLongLiteral(ITree tree) {
			this.tree = tree;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitLongLiteralOctalLongLiteral(this);
		}
	}
}
