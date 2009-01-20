package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class BasicType extends AbstractAST {
	static public class Ambiguity extends BasicType {
		private final java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives;

		public Ambiguity(
				ITree tree,
				java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitBasicTypeAmbiguity(this);
		}

		public java.util.List<org.meta_environment.rascal.ast.BasicType> getAlternatives() {
			return alternatives;
		}
	}

	static public class Area extends BasicType {
		/* package */Area(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeArea(this);
		}

		@Override
		public boolean isArea() {
			return true;
		}
	}

	static public class Bool extends BasicType {
		/* package */Bool(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeBool(this);
		}

		@Override
		public boolean isBool() {
			return true;
		}
	}

	static public class Int extends BasicType {
		/* package */Int(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeInt(this);
		}

		@Override
		public boolean isInt() {
			return true;
		}
	}

	static public class Loc extends BasicType {
		/* package */Loc(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeLoc(this);
		}

		@Override
		public boolean isLoc() {
			return true;
		}
	}

	static public class Real extends BasicType {
		/* package */Real(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeReal(this);
		}

		@Override
		public boolean isReal() {
			return true;
		}
	}

	static public class String extends BasicType {
		/* package */String(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeString(this);
		}

		@Override
		public boolean isString() {
			return true;
		}
	}

	static public class Tree extends BasicType {
		/* package */Tree(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeTree(this);
		}

		@Override
		public boolean isTree() {
			return true;
		}
	}

	static public class Value extends BasicType {
		/* package */Value(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeValue(this);
		}

		@Override
		public boolean isValue() {
			return true;
		}
	}

	static public class Void extends BasicType {
		/* package */Void(ITree tree) {
			this.tree = tree;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeVoid(this);
		}

		@Override
		public boolean isVoid() {
			return true;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isArea() {
		return false;
	}

	public boolean isBool() {
		return false;
	}

	public boolean isInt() {
		return false;
	}

	public boolean isLoc() {
		return false;
	}

	public boolean isReal() {
		return false;
	}

	public boolean isString() {
		return false;
	}

	public boolean isTree() {
		return false;
	}

	public boolean isValue() {
		return false;
	}

	public boolean isVoid() {
		return false;
	}
}