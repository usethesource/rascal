package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Field extends AbstractAST {
	static public class Ambiguity extends Field {
		private final java.util.List<org.meta_environment.rascal.ast.Field> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.Field> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitFieldAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.Field> getAlternatives() {
			return alternatives;
		}
	}

	static public class Index extends Field {
		private org.meta_environment.rascal.ast.IntegerLiteral fieldIndex;

		/* fieldIndex:IntegerLiteral -> Field {cons("Index")} */
		private Index() {
		}

		/* package */Index(ITree tree,
				org.meta_environment.rascal.ast.IntegerLiteral fieldIndex) {
			this.tree = tree;
			this.fieldIndex = fieldIndex;
		}

		private void $setFieldIndex(
				org.meta_environment.rascal.ast.IntegerLiteral x) {
			this.fieldIndex = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFieldIndex(this);
		}

		@Override
		public org.meta_environment.rascal.ast.IntegerLiteral getFieldIndex() {
			return fieldIndex;
		}

		@Override
		public boolean hasFieldIndex() {
			return true;
		}

		@Override
		public boolean isIndex() {
			return true;
		}

		public Index setFieldIndex(
				org.meta_environment.rascal.ast.IntegerLiteral x) {
			final Index z = new Index();
			z.$setFieldIndex(x);
			return z;
		}
	}

	static public class Name extends Field {
		private org.meta_environment.rascal.ast.Name fieldName;

		/* fieldName:Name -> Field {cons("Name")} */
		private Name() {
		}

		/* package */Name(ITree tree,
				org.meta_environment.rascal.ast.Name fieldName) {
			this.tree = tree;
			this.fieldName = fieldName;
		}

		private void $setFieldName(org.meta_environment.rascal.ast.Name x) {
			this.fieldName = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitFieldName(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getFieldName() {
			return fieldName;
		}

		@Override
		public boolean hasFieldName() {
			return true;
		}

		@Override
		public boolean isName() {
			return true;
		}

		public Name setFieldName(org.meta_environment.rascal.ast.Name x) {
			final Name z = new Name();
			z.$setFieldName(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.IntegerLiteral getFieldIndex() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getFieldName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFieldIndex() {
		return false;
	}

	public boolean hasFieldName() {
		return false;
	}

	public boolean isIndex() {
		return false;
	}

	public boolean isName() {
		return false;
	}
}