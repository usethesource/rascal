package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Area extends AbstractAST {
	static public class Ambiguity extends Area {
		private final java.util.List<org.meta_environment.rascal.ast.Area> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Area> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Area> getAlternatives() {
			return alternatives;
		}
	}

	static public class Default extends Area {
		private org.meta_environment.rascal.ast.Expression beginColumn;
		private org.meta_environment.rascal.ast.Expression beginLine;
		private org.meta_environment.rascal.ast.Expression endColumn;
		private org.meta_environment.rascal.ast.Expression endLine;
		private org.meta_environment.rascal.ast.Expression length;
		private org.meta_environment.rascal.ast.Expression offset;

		/*
		 * "area" "(" beginLine:Expression "," beginColumn:Expression ","
		 * endLine:Expression "," endColumn:Expression "," offset:Expression ","
		 * length:Expression ")" -> Area {cons("Default")}
		 */
		private Default() {
		}

		/* package */Default(ITree tree,
				org.meta_environment.rascal.ast.Expression beginLine,
				org.meta_environment.rascal.ast.Expression beginColumn,
				org.meta_environment.rascal.ast.Expression endLine,
				org.meta_environment.rascal.ast.Expression endColumn,
				org.meta_environment.rascal.ast.Expression offset,
				org.meta_environment.rascal.ast.Expression length) {
			this.tree = tree;
			this.beginLine = beginLine;
			this.beginColumn = beginColumn;
			this.endLine = endLine;
			this.endColumn = endColumn;
			this.offset = offset;
			this.length = length;
		}

		private void $setBeginColumn(
				org.meta_environment.rascal.ast.Expression x) {
			this.beginColumn = x;
		}

		private void $setBeginLine(org.meta_environment.rascal.ast.Expression x) {
			this.beginLine = x;
		}

		private void $setEndColumn(org.meta_environment.rascal.ast.Expression x) {
			this.endColumn = x;
		}

		private void $setEndLine(org.meta_environment.rascal.ast.Expression x) {
			this.endLine = x;
		}

		private void $setLength(org.meta_environment.rascal.ast.Expression x) {
			this.length = x;
		}

		private void $setOffset(org.meta_environment.rascal.ast.Expression x) {
			this.offset = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitAreaDefault(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getBeginColumn() {
			return beginColumn;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getBeginLine() {
			return beginLine;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getEndColumn() {
			return endColumn;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getEndLine() {
			return endLine;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getLength() {
			return length;
		}

		@Override
		public org.meta_environment.rascal.ast.Expression getOffset() {
			return offset;
		}

		public Default setBeginColumn(
				org.meta_environment.rascal.ast.Expression x) {
			Default z = new Default();
			z.$setBeginColumn(x);
			return z;
		}

		public Default setBeginLine(org.meta_environment.rascal.ast.Expression x) {
			Default z = new Default();
			z.$setBeginLine(x);
			return z;
		}

		public Default setEndColumn(org.meta_environment.rascal.ast.Expression x) {
			Default z = new Default();
			z.$setEndColumn(x);
			return z;
		}

		public Default setEndLine(org.meta_environment.rascal.ast.Expression x) {
			Default z = new Default();
			z.$setEndLine(x);
			return z;
		}

		public Default setLength(org.meta_environment.rascal.ast.Expression x) {
			Default z = new Default();
			z.$setLength(x);
			return z;
		}

		public Default setOffset(org.meta_environment.rascal.ast.Expression x) {
			Default z = new Default();
			z.$setOffset(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Expression getBeginColumn() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getBeginLine() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getEndColumn() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getEndLine() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getLength() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Expression getOffset() {
		throw new UnsupportedOperationException();
	}
}
