package org.meta_environment.rascal.ast;

import org.eclipse.imp.pdb.facts.ITree;

public abstract class Type extends AbstractAST {
	static public class Ambiguity extends Type {
		private final java.util.List<org.meta_environment.rascal.ast.Type> alternatives;

		public Ambiguity(
				java.util.List<org.meta_environment.rascal.ast.Type> alternatives) {
			this.alternatives = java.util.Collections
					.unmodifiableList(alternatives);
		}

		public java.util.List<org.meta_environment.rascal.ast.Type> getAlternatives() {
			return alternatives;
		}
	}

	static public class Basic extends Type {
		private org.meta_environment.rascal.ast.BasicType basic;

		/* basic:BasicType -> Type {cons("Basic")} */
		private Basic() {
		}

		/* package */Basic(ITree tree,
				org.meta_environment.rascal.ast.BasicType basic) {
			this.tree = tree;
			this.basic = basic;
		}

		private void $setBasic(org.meta_environment.rascal.ast.BasicType x) {
			this.basic = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeBasic(this);
		}

		@Override
		public org.meta_environment.rascal.ast.BasicType getBasic() {
			return basic;
		}

		public Basic setBasic(org.meta_environment.rascal.ast.BasicType x) {
			Basic z = new Basic();
			z.$setBasic(x);
			return z;
		}
	}

	static public class Function extends Type {
		private org.meta_environment.rascal.ast.FunctionType function;

		/* function:FunctionType -> Type {cons("Function")} */
		private Function() {
		}

		/* package */Function(ITree tree,
				org.meta_environment.rascal.ast.FunctionType function) {
			this.tree = tree;
			this.function = function;
		}

		private void $setFunction(org.meta_environment.rascal.ast.FunctionType x) {
			this.function = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeFunction(this);
		}

		@Override
		public org.meta_environment.rascal.ast.FunctionType getFunction() {
			return function;
		}

		public Function setFunction(
				org.meta_environment.rascal.ast.FunctionType x) {
			Function z = new Function();
			z.$setFunction(x);
			return z;
		}
	}

	static public class Selector extends Type {
		private org.meta_environment.rascal.ast.DataTypeSelector selector;

		/* selector:DataTypeSelector -> Type {cons("Selector")} */
		private Selector() {
		}

		/* package */Selector(ITree tree,
				org.meta_environment.rascal.ast.DataTypeSelector selector) {
			this.tree = tree;
			this.selector = selector;
		}

		private void $setSelector(
				org.meta_environment.rascal.ast.DataTypeSelector x) {
			this.selector = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeSelector(this);
		}

		@Override
		public org.meta_environment.rascal.ast.DataTypeSelector getSelector() {
			return selector;
		}

		public Selector setSelector(
				org.meta_environment.rascal.ast.DataTypeSelector x) {
			Selector z = new Selector();
			z.$setSelector(x);
			return z;
		}
	}

	static public class Structured extends Type {
		private org.meta_environment.rascal.ast.StructuredType structured;

		/* structured:StructuredType -> Type {cons("Structured")} */
		private Structured() {
		}

		/* package */Structured(ITree tree,
				org.meta_environment.rascal.ast.StructuredType structured) {
			this.tree = tree;
			this.structured = structured;
		}

		private void $setStructured(
				org.meta_environment.rascal.ast.StructuredType x) {
			this.structured = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeStructured(this);
		}

		@Override
		public org.meta_environment.rascal.ast.StructuredType getStructured() {
			return structured;
		}

		public Structured setStructured(
				org.meta_environment.rascal.ast.StructuredType x) {
			Structured z = new Structured();
			z.$setStructured(x);
			return z;
		}
	}

	static public class Symbol extends Type {
		private org.meta_environment.rascal.ast.Symbol symbol;

		/* symbol:Symbol -> Type {cons("Symbol")} */
		private Symbol() {
		}

		/* package */Symbol(ITree tree,
				org.meta_environment.rascal.ast.Symbol symbol) {
			this.tree = tree;
			this.symbol = symbol;
		}

		private void $setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			this.symbol = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeSymbol(this);
		}

		@Override
		public org.meta_environment.rascal.ast.Symbol getSymbol() {
			return symbol;
		}

		public Symbol setSymbol(org.meta_environment.rascal.ast.Symbol x) {
			Symbol z = new Symbol();
			z.$setSymbol(x);
			return z;
		}
	}

	static public class User extends Type {
		private org.meta_environment.rascal.ast.UserType user;

		/* user:UserType -> Type {cons("User")} */
		private User() {
		}

		/* package */User(ITree tree,
				org.meta_environment.rascal.ast.UserType user) {
			this.tree = tree;
			this.user = user;
		}

		private void $setUser(org.meta_environment.rascal.ast.UserType x) {
			this.user = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeUser(this);
		}

		@Override
		public org.meta_environment.rascal.ast.UserType getUser() {
			return user;
		}

		public User setUser(org.meta_environment.rascal.ast.UserType x) {
			User z = new User();
			z.$setUser(x);
			return z;
		}
	}

	static public class Variable extends Type {
		private org.meta_environment.rascal.ast.TypeVar typeVar;

		/* typeVar:TypeVar -> Type {cons("Variable")} */
		private Variable() {
		}

		/* package */Variable(ITree tree,
				org.meta_environment.rascal.ast.TypeVar typeVar) {
			this.tree = tree;
			this.typeVar = typeVar;
		}

		private void $setTypeVar(org.meta_environment.rascal.ast.TypeVar x) {
			this.typeVar = x;
		}

		public IVisitable accept(IASTVisitor visitor) {
			return visitor.visitTypeVariable(this);
		}

		@Override
		public org.meta_environment.rascal.ast.TypeVar getTypeVar() {
			return typeVar;
		}

		public Variable setTypeVar(org.meta_environment.rascal.ast.TypeVar x) {
			Variable z = new Variable();
			z.$setTypeVar(x);
			return z;
		}
	}

	public org.meta_environment.rascal.ast.BasicType getBasic() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.FunctionType getFunction() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.DataTypeSelector getSelector() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.StructuredType getStructured() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.Symbol getSymbol() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.TypeVar getTypeVar() {
		throw new UnsupportedOperationException();
	}

	public org.meta_environment.rascal.ast.UserType getUser() {
		throw new UnsupportedOperationException();
	}
}
