package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class DataTypeSelector extends AbstractAST {
	static public class Ambiguity extends DataTypeSelector {
		private final java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.DataTypeSelector> getAlternatives() {
			return alternatives;
		}
	}

	static public class Selector extends DataTypeSelector {
		private org.meta_environment.rascal.ast.Name production;
		private org.meta_environment.rascal.ast.Name sort;

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

		public IVisitable accept(IASTVisitor visitor) {
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

		public Selector setProduction(org.meta_environment.rascal.ast.Name x) {
			Selector z = new Selector();
			z.$setProduction(x);
			return z;
		}

		public Selector setSort(org.meta_environment.rascal.ast.Name x) {
			Selector z = new Selector();
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
}
