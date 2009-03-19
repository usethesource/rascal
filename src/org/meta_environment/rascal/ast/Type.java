package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.INode;

public abstract class Type extends AbstractAST {
	public org.meta_environment.rascal.ast.BasicType getBasic() {
		throw new UnsupportedOperationException();
	}

	public boolean hasBasic() {
		return false;
	}

	public boolean isBasic() {
		return false;
	}

	static public class Basic extends Type {
		/* basic:BasicType -> Type {cons("Basic")} */
		private Basic() {
		}

		/* package */Basic(INode node,
				org.meta_environment.rascal.ast.BasicType basic) {
			this.node = node;
			this.basic = basic;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeBasic(this);
		}

		@Override
		public boolean isBasic() {
			return true;
		}

		@Override
		public boolean hasBasic() {
			return true;
		}

		private org.meta_environment.rascal.ast.BasicType basic;

		@Override
		public org.meta_environment.rascal.ast.BasicType getBasic() {
			return basic;
		}

		private void $setBasic(org.meta_environment.rascal.ast.BasicType x) {
			this.basic = x;
		}

		public Basic setBasic(org.meta_environment.rascal.ast.BasicType x) {
			Basic z = new Basic();
			z.$setBasic(x);
			return z;
		}
	}

	static public class Ambiguity extends Type {
		private final java.util.List<org.meta_environment.rascal.ast.Type> alternatives;

		public Ambiguity(
				INode node,
				java.util.List<org.meta_environment.rascal.ast.Type> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
			this.node = node;
		}

		public java.util.List<org.meta_environment.rascal.ast.Type> getAlternatives() {
			return alternatives;
		}

		@Override
		public <T> T accept(IASTVisitor<T> v) {
			return v.visitTypeAmbiguity(this);
		}
	}

	public org.meta_environment.rascal.ast.StructuredType getStructured() {
		throw new UnsupportedOperationException();
	}

	public boolean hasStructured() {
		return false;
	}

	public boolean isStructured() {
		return false;
	}

	static public class Structured extends Type {
		/* structured:StructuredType -> Type {cons("Structured")} */
		private Structured() {
		}

		/* package */Structured(INode node,
				org.meta_environment.rascal.ast.StructuredType structured) {
			this.node = node;
			this.structured = structured;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeStructured(this);
		}

		@Override
		public boolean isStructured() {
			return true;
		}

		@Override
		public boolean hasStructured() {
			return true;
		}

		private org.meta_environment.rascal.ast.StructuredType structured;

		@Override
		public org.meta_environment.rascal.ast.StructuredType getStructured() {
			return structured;
		}

		private void $setStructured(
				org.meta_environment.rascal.ast.StructuredType x) {
			this.structured = x;
		}

		public Structured setStructured(
				org.meta_environment.rascal.ast.StructuredType x) {
			Structured z = new Structured();
			z.$setStructured(x);
			return z;
		}
	}

	@Override
	public abstract <T> T accept(IASTVisitor<T> visitor);

	public org.meta_environment.rascal.ast.FunctionType getFunction() {
		throw new UnsupportedOperationException();
	}

	public boolean hasFunction() {
		return false;
	}

	public boolean isFunction() {
		return false;
	}

	static public class Function extends Type {
		/* function:FunctionType -> Type {cons("Function")} */
		private Function() {
		}

		/* package */Function(INode node,
				org.meta_environment.rascal.ast.FunctionType function) {
			this.node = node;
			this.function = function;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeFunction(this);
		}

		@Override
		public boolean isFunction() {
			return true;
		}

		@Override
		public boolean hasFunction() {
			return true;
		}

		private org.meta_environment.rascal.ast.FunctionType function;

		@Override
		public org.meta_environment.rascal.ast.FunctionType getFunction() {
			return function;
		}

		private void $setFunction(org.meta_environment.rascal.ast.FunctionType x) {
			this.function = x;
		}

		public Function setFunction(
				org.meta_environment.rascal.ast.FunctionType x) {
			Function z = new Function();
			z.$setFunction(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.TypeVar getTypeVar() {
		throw new UnsupportedOperationException();
	}

	public boolean hasTypeVar() {
		return false;
	}

	public boolean isVariable() {
		return false;
	}

	static public class Variable extends Type {
		/* typeVar:TypeVar -> Type {cons("Variable")} */
		private Variable() {
		}

		/* package */Variable(INode node,
				org.meta_environment.rascal.ast.TypeVar typeVar) {
			this.node = node;
			this.typeVar = typeVar;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeVariable(this);
		}

		@Override
		public boolean isVariable() {
			return true;
		}

		@Override
		public boolean hasTypeVar() {
			return true;
		}

		private org.meta_environment.rascal.ast.TypeVar typeVar;

		@Override
		public org.meta_environment.rascal.ast.TypeVar getTypeVar() {
			return typeVar;
		}

		private void $setTypeVar(org.meta_environment.rascal.ast.TypeVar x) {
			this.typeVar = x;
		}

		public Variable setTypeVar(org.meta_environment.rascal.ast.TypeVar x) {
			Variable z = new Variable();
			z.$setTypeVar(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.UserType getUser() {
		throw new UnsupportedOperationException();
	}

	public boolean hasUser() {
		return false;
	}

	public boolean isUser() {
		return false;
	}

	static public class User extends Type {
		/* user:UserType -> Type {cons("User")} */
		private User() {
		}

		/* package */User(INode node,
				org.meta_environment.rascal.ast.UserType user) {
			this.node = node;
			this.user = user;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeUser(this);
		}

		@Override
		public boolean isUser() {
			return true;
		}

		@Override
		public boolean hasUser() {
			return true;
		}

		private org.meta_environment.rascal.ast.UserType user;

		@Override
		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		private void $setUser(org.meta_environment.rascal.ast.UserType x) {
			this.user = x;
		}

		public User setUser(org.meta_environment.rascal.ast.UserType x) {
			User z = new User();
			z.$setUser(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.Symbol getSymbol() {
		throw new UnsupportedOperationException();
	}

	public boolean hasSymbol() {
		return false;
	}

	public boolean isSymbol() {
		return false;
	}

	static public class Symbol extends Type {
		/* symbol:Symbol -> Type {cons("Symbol")} */
		private Symbol() {
		}

		/* package */Symbol(INode node,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.node = node;
			this.symbol = symbol;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeSymbol(this);
		}

		@Override
		public boolean isSymbol() {
			return true;
		}

		@Override
		public boolean hasSymbol() {
			return true;
		}

		private org.meta_environment.rascal.ast.Symbol symbol;

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public Symbol setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			Symbol z = new Symbol();
			z.$setSymbol(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.DataTypeSelector getSelector() {
		throw new UnsupportedOperationException();
	}

	public boolean hasSelector() {
		return false;
	}

	public boolean isSelector() {
		return false;
	}

	static public class Selector extends Type {
		/* selector:DataTypeSelector -> Type {cons("Selector")} */
		private Selector() {
		}

		/* package */Selector(INode node,
				org.meta_environment.rascal.ast.DataTypeSelector selector) {
			this.node = node;
			this.selector = selector;
		}

		@Override
		public <T> T accept(IASTVisitor<T> visitor) {
			return visitor.visitTypeSelector(this);
		}

		@Override
		public boolean isSelector() {
			return true;
		}

		@Override
		public boolean hasSelector() {
			return true;
		}

		private org.meta_environment.rascal.ast.DataTypeSelector selector;

		@Override
		public org.meta_environment.rascal.ast.DataTypeSelector getSelector() {
			return selector;
		}

		private void $setSelector(
				org.meta_environment.rascal.ast.DataTypeSelector x) {
			this.selector = x;
		}

		public Selector setSelector(
				org.meta_environment.rascal.ast.DataTypeSelector x) {
			Selector z = new Selector();
			z.$setSelector(x);
			return z;
		}
	}
}