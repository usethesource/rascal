package org.meta_environment.rascal.ast; 
import org.eclipse.imp.pdb.facts.INode; 
public abstract class Type extends AbstractAST { 
  public org.meta_environment.rascal.ast.Symbol getSymbol() { throw new UnsupportedOperationException(); }
public boolean hasSymbol() { return false; }
public boolean isSymbol() { return false; }
static public class Symbol extends Type {
/** symbol:Symbol -> Type {cons("Symbol")} */
	public Symbol(INode node, org.meta_environment.rascal.ast.Symbol symbol) {
		this.node = node;
		this.symbol = symbol;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeSymbol(this);
	}

	public boolean isSymbol() { return true; }

	public boolean hasSymbol() { return true; }

private final org.meta_environment.rascal.ast.Symbol symbol;
	public org.meta_environment.rascal.ast.Symbol getSymbol() { return symbol; }	
}
static public class Ambiguity extends Type {
  private final java.util.List<org.meta_environment.rascal.ast.Type> alternatives;
  public Ambiguity(INode node, java.util.List<org.meta_environment.rascal.ast.Type> alternatives) {
	this.alternatives = java.util.Collections.unmodifiableList(alternatives);
         this.node = node;
  }
  public java.util.List<org.meta_environment.rascal.ast.Type> getAlternatives() {
	return alternatives;
  }
  
  public <T> T accept(IASTVisitor<T> v) {
     return v.visitTypeAmbiguity(this);
  }
} 
public org.meta_environment.rascal.ast.BasicType getBasic() { throw new UnsupportedOperationException(); }
public boolean hasBasic() { return false; }
public boolean isBasic() { return false; }
static public class Basic extends Type {
/** basic:BasicType -> Type {cons("Basic")} */
	public Basic(INode node, org.meta_environment.rascal.ast.BasicType basic) {
		this.node = node;
		this.basic = basic;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeBasic(this);
	}

	public boolean isBasic() { return true; }

	public boolean hasBasic() { return true; }

private final org.meta_environment.rascal.ast.BasicType basic;
	public org.meta_environment.rascal.ast.BasicType getBasic() { return basic; }	
} public abstract <T> T accept(IASTVisitor<T> visitor); public org.meta_environment.rascal.ast.StructuredType getStructured() { throw new UnsupportedOperationException(); }
public boolean hasStructured() { return false; }
public boolean isStructured() { return false; }
static public class Structured extends Type {
/** structured:StructuredType -> Type {cons("Structured")} */
	public Structured(INode node, org.meta_environment.rascal.ast.StructuredType structured) {
		this.node = node;
		this.structured = structured;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeStructured(this);
	}

	public boolean isStructured() { return true; }

	public boolean hasStructured() { return true; }

private final org.meta_environment.rascal.ast.StructuredType structured;
	public org.meta_environment.rascal.ast.StructuredType getStructured() { return structured; }	
} 
public org.meta_environment.rascal.ast.FunctionType getFunction() { throw new UnsupportedOperationException(); }
public boolean hasFunction() { return false; }
public boolean isFunction() { return false; }
static public class Function extends Type {
/** function:FunctionType -> Type {cons("Function")} */
	public Function(INode node, org.meta_environment.rascal.ast.FunctionType function) {
		this.node = node;
		this.function = function;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeFunction(this);
	}

	public boolean isFunction() { return true; }

	public boolean hasFunction() { return true; }

private final org.meta_environment.rascal.ast.FunctionType function;
	public org.meta_environment.rascal.ast.FunctionType getFunction() { return function; }	
} 
public org.meta_environment.rascal.ast.TypeVar getTypeVar() { throw new UnsupportedOperationException(); }
public boolean hasTypeVar() { return false; }
public boolean isVariable() { return false; }
static public class Variable extends Type {
/** typeVar:TypeVar -> Type {cons("Variable")} */
	public Variable(INode node, org.meta_environment.rascal.ast.TypeVar typeVar) {
		this.node = node;
		this.typeVar = typeVar;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeVariable(this);
	}

	public boolean isVariable() { return true; }

	public boolean hasTypeVar() { return true; }

private final org.meta_environment.rascal.ast.TypeVar typeVar;
	public org.meta_environment.rascal.ast.TypeVar getTypeVar() { return typeVar; }	
} 
public org.meta_environment.rascal.ast.UserType getUser() { throw new UnsupportedOperationException(); }
public boolean hasUser() { return false; }
public boolean isUser() { return false; }
static public class User extends Type {
/** user:UserType -> Type {cons("User")} */
	public User(INode node, org.meta_environment.rascal.ast.UserType user) {
		this.node = node;
		this.user = user;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeUser(this);
	}

	public boolean isUser() { return true; }

	public boolean hasUser() { return true; }

private final org.meta_environment.rascal.ast.UserType user;
	public org.meta_environment.rascal.ast.UserType getUser() { return user; }	
} 
public org.meta_environment.rascal.ast.DataTypeSelector getSelector() { throw new UnsupportedOperationException(); }
public boolean hasSelector() { return false; }
public boolean isSelector() { return false; }
static public class Selector extends Type {
/** selector:DataTypeSelector -> Type {cons("Selector")} */
	public Selector(INode node, org.meta_environment.rascal.ast.DataTypeSelector selector) {
		this.node = node;
		this.selector = selector;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeSelector(this);
	}

	public boolean isSelector() { return true; }

	public boolean hasSelector() { return true; }

private final org.meta_environment.rascal.ast.DataTypeSelector selector;
	public org.meta_environment.rascal.ast.DataTypeSelector getSelector() { return selector; }	
} 
public org.meta_environment.rascal.ast.Type getType() { throw new UnsupportedOperationException(); }
public boolean hasType() { return false; }
public boolean isBracket() { return false; }
static public class Bracket extends Type {
/** "(" type:Type ")" -> Type {bracket, cons("Bracket")} */
	public Bracket(INode node, org.meta_environment.rascal.ast.Type type) {
		this.node = node;
		this.type = type;
	}
	public <T> T accept(IASTVisitor<T> visitor) {
		return visitor.visitTypeBracket(this);
	}

	public boolean isBracket() { return true; }

	public boolean hasType() { return true; }

private final org.meta_environment.rascal.ast.Type type;
	public org.meta_environment.rascal.ast.Type getType() { return type; }	
}
}