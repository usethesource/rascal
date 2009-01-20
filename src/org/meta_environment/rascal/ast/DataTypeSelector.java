package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class DataTypeSelector extends AbstractAST {
	static public class Ambiguity extends DataTypeSelector {
		private final java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitDataTypeSelectorAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> getAlternatives() {
			return alternatives;
		}
	}

	static public class Selector extends DataTypeSelector {
		private org.meta_environment.rascal.ast.Name sort;
		private org.meta_environment.rascal.ast.Name production;

		/* sort:Name "." production:Name -> DataTypeSelector {cons("Selector")} */
		private Selector() {
		}

		/* package */Selector(ITree tree,
				org.meta_environment.rascal.ast.Name sort,
				org.meta_environment.rascal.ast.Name production) {
			this.tree = tree;
			this.sort = sort;
			this.production = production;
		}

		private void $setProduction(org.meta_environment.rascal.ast.Name x) {
			this.production = x;
		}

		private void $setSort(org.meta_environment.rascal.ast.Name x) {
			this.sort = x;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitDataTypeSelectorSelector(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Name getProduction() {
			return production;
		}

		@Override
		public org.meta_environment.rascal.ast.Name getSort() {
			return sort;
		}

		@Override
		public boolean hasProduction() {
			return true;
		}

		@Override
		public boolean hasSort() {
			return true;
		}

		@Override
		public boolean isSelector() {
			return true;
		}

		public Selector setProduction(org.meta_environment.rascal.ast.Name x) {
			final Selector z = new Selector();
			z.$setProduction(x);
			return z;
		}

		public Selector setSort(org.meta_environment.rascal.ast.Name x) {
			final Selector z = new Selector();
			z.$setSort(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Name getProduction() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Name getSort() {
		throw new UnsupportedOperationException();
	}

	public boolean hasProduction() {
		return false;
	}

	public boolean hasSort() {
		return false;
	}

	public boolean isSelector() {
		return false;
	}
}