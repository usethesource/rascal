package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class BasicType extends AbstractAST {
	public boolean isBool() {
		return false;
	}

	static public class Bool extends BasicType {
		/* "bool" -> BasicType {cons("Bool")} */
		private Bool() {
			super();
		}

		public Bool(INode node) {
			this.node = node;
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

	static public class Ambiguity extends BasicType {
		private final java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.BasicType> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.BasicType> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitBasicTypeAmbiguity(this);
		}
	}

	public boolean isInt() {
		return false;
	}

	static public class Int extends BasicType {
		/* "int" -> BasicType {cons("Int")} */
		private Int() {
			super();
		}

		public Int(INode node) {
			this.node = node;
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

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public boolean isReal() {
		return false;
	}

	static public class Real extends BasicType {
		/* "real" -> BasicType {cons("Real")} */
		private Real() {
			super();
		}

		public Real(INode node) {
			this.node = node;
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

	public boolean isString() {
		return false;
	}

	static public class String extends BasicType {
		/* "str" -> BasicType {cons("String")} */
		private String() {
			super();
		}

		public String(INode node) {
			this.node = node;
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

	public boolean isValue() {
		return false;
	}

	static public class Value extends BasicType {
		/* "value" -> BasicType {cons("Value")} */
		private Value() {
			super();
		}

		public Value(INode node) {
			this.node = node;
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

	public boolean isNode() {
		return false;
	}

	static public class Node extends BasicType {
		/* "node" -> BasicType {cons("Node")} */
		private Node() {
			super();
		}

		public Node(INode node) {
			this.node = node;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitBasicTypeNode(this);
		}

		@Override
		public boolean isNode() {
			return true;
		}
	}

	public boolean isVoid() {
		return false;
	}

	static public class Void extends BasicType {
		/* "void" -> BasicType {cons("Void")} */
		private Void() {
			super();
		}

		public Void(INode node) {
			this.node = node;
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

	public boolean isLoc() {
		return false;
	}

	static public class Loc extends BasicType {
		/* "loc" -> BasicType {cons("Loc")} */
		private Loc() {
			super();
		}

		public Loc(INode node) {
			this.node = node;
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

	public boolean isArea() {
		return false;
	}

	static public class Area extends BasicType {
		/* "area" -> BasicType {cons("Area")} */
		private Area() {
			super();
		}

		public Area(INode node) {
			this.node = node;
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
}