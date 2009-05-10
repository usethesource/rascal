package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class SYMBOL extends AbstractAST {
	public org.meta_environment.rascal.ast.QualifiedName getName() {
		throw new UnsupportedOperationException();
	}

	public boolean hasName() {
		return false;
	}

	public boolean isMetaVariable() {
		return false;
	}

	static public class MetaVariable extends SYMBOL {
		/*
		 * "<" name:QualifiedName ">" -> SYMBOL {cons("MetaVariable"),
		 * category("MetaVariable")}
		 */
		private MetaVariable() {
		}

		/* package */MetaVariable(INode node,
				org.meta_environment.rascal.ast.QualifiedName name) {
			this.node = node;
			this.name = name;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitSYMBOLMetaVariable(this);
		}

		@Override
		public boolean isMetaVariable() {
			return true;
		}

		@Override
		public boolean hasName() {
			return true;
		}

		private org.meta_environment.rascal.ast.QualifiedName name;

		@Override
		public org.meta_environment.rascal.ast.QualifiedName getName() {
			return name;
		}

		private void $setName(org.meta_environment.rascal.ast.QualifiedName x) {
			this.name = x;
		}

		public MetaVariable setName(
				org.meta_environment.rascal.ast.QualifiedName x) {
			MetaVariable z = new MetaVariable();
			z.$setName(x);
			return z;
		}
	}

	static public class Ambiguity extends SYMBOL {
		private final java.util.List<org.meta_environment.rascal.ast.SYMBOL> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.SYMBOL> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.SYMBOL> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitSYMBOLAmbiguity(this);
		}
	}
}