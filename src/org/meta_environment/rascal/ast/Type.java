package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Type extends AbstractAST
{
  static public class Basic extends Type
  {
/* basic:BasicType -> Type {cons("Basic")} */
    private Basic ()
    {
    }
    /*package */ Basic (ITree tree,
			org.meta_environment.rascal.ast.BasicType basic)
    {
      this.tree = tree;
      this.basic = basic;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeBasic (this);
    }
    private org.meta_environment.rascal.ast.BasicType basic;
    public org.meta_environment.rascal.ast.BasicType getBasic ()
    {
      return basic;
    }
    private void $setBasic (org.meta_environment.rascal.ast.BasicType x)
    {
      this.basic = x;
    }
    public Basic setBasic (org.meta_environment.rascal.ast.BasicType x)
    {
      org.meta_environment.rascal.ast.Basic z = new Basic ();
      z.$setBasic (x);
      return z;
    }
  }
  static public class Ambiguity extends Type
  {
    private final java.util.List < org.meta_environment.rascal.ast.Type >
      alternatives;
    public Ambiguity (java.util.List < org.meta_environment.rascal.ast.Type >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Type >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Structured extends Type
  {
/* structured:StructuredType -> Type {cons("Structured")} */
    private Structured ()
    {
    }
    /*package */ Structured (ITree tree,
			     org.meta_environment.rascal.ast.
			     StructuredType structured)
    {
      this.tree = tree;
      this.structured = structured;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeStructured (this);
    }
    private org.meta_environment.rascal.ast.StructuredType structured;
    public org.meta_environment.rascal.ast.StructuredType getStructured ()
    {
      return structured;
    }
    private void $setStructured (org.meta_environment.rascal.ast.
				 StructuredType x)
    {
      this.structured = x;
    }
    public Structured setStructured (org.meta_environment.rascal.ast.
				     StructuredType x)
    {
      org.meta_environment.rascal.ast.Structured z = new Structured ();
      z.$setStructured (x);
      return z;
    }
  }
  static public class Function extends Type
  {
/* function:FunctionType -> Type {cons("Function")} */
    private Function ()
    {
    }
    /*package */ Function (ITree tree,
			   org.meta_environment.rascal.ast.
			   FunctionType function)
    {
      this.tree = tree;
      this.function = function;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeFunction (this);
    }
    private org.meta_environment.rascal.ast.FunctionType function;
    public org.meta_environment.rascal.ast.FunctionType getFunction ()
    {
      return function;
    }
    private void $setFunction (org.meta_environment.rascal.ast.FunctionType x)
    {
      this.function = x;
    }
    public Function setFunction (org.meta_environment.rascal.ast.
				 FunctionType x)
    {
      org.meta_environment.rascal.ast.Function z = new Function ();
      z.$setFunction (x);
      return z;
    }
  }
  static public class Variable extends Type
  {
/* typeVar:TypeVar -> Type {cons("Variable")} */
    private Variable ()
    {
    }
    /*package */ Variable (ITree tree,
			   org.meta_environment.rascal.ast.TypeVar typeVar)
    {
      this.tree = tree;
      this.typeVar = typeVar;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeVariable (this);
    }
    private org.meta_environment.rascal.ast.TypeVar typeVar;
    public org.meta_environment.rascal.ast.TypeVar getTypeVar ()
    {
      return typeVar;
    }
    private void $setTypeVar (org.meta_environment.rascal.ast.TypeVar x)
    {
      this.typeVar = x;
    }
    public Variable setTypeVar (org.meta_environment.rascal.ast.TypeVar x)
    {
      org.meta_environment.rascal.ast.Variable z = new Variable ();
      z.$setTypeVar (x);
      return z;
    }
  }
  static public class User extends Type
  {
/* user:UserType -> Type {cons("User")} */
    private User ()
    {
    }
    /*package */ User (ITree tree,
		       org.meta_environment.rascal.ast.UserType user)
    {
      this.tree = tree;
      this.user = user;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeUser (this);
    }
    private org.meta_environment.rascal.ast.UserType user;
    public org.meta_environment.rascal.ast.UserType getUser ()
    {
      return user;
    }
    private void $setUser (org.meta_environment.rascal.ast.UserType x)
    {
      this.user = x;
    }
    public User setUser (org.meta_environment.rascal.ast.UserType x)
    {
      org.meta_environment.rascal.ast.User z = new User ();
      z.$setUser (x);
      return z;
    }
  }
  static public class Symbol extends Type
  {
/* symbol:Symbol -> Type {cons("Symbol")} */
    private Symbol ()
    {
    }
    /*package */ Symbol (ITree tree,
			 org.meta_environment.rascal.ast.Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeSymbol (this);
    }
    private org.meta_environment.rascal.ast.Symbol symbol;
    public org.meta_environment.rascal.ast.Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      this.symbol = x;
    }
    public Symbol setSymbol (org.meta_environment.rascal.ast.Symbol x)
    {
      org.meta_environment.rascal.ast.Symbol z = new Symbol ();
      z.$setSymbol (x);
      return z;
    }
  }
  static public class Selector extends Type
  {
/* selector:DataTypeSelector -> Type {cons("Selector")} */
    private Selector ()
    {
    }
    /*package */ Selector (ITree tree,
			   org.meta_environment.rascal.ast.
			   DataTypeSelector selector)
    {
      this.tree = tree;
      this.selector = selector;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeSelector (this);
    }
    private org.meta_environment.rascal.ast.DataTypeSelector selector;
    public org.meta_environment.rascal.ast.DataTypeSelector getSelector ()
    {
      return selector;
    }
    private void $setSelector (org.meta_environment.rascal.ast.
			       DataTypeSelector x)
    {
      this.selector = x;
    }
    public Selector setSelector (org.meta_environment.rascal.ast.
				 DataTypeSelector x)
    {
      org.meta_environment.rascal.ast.Selector z = new Selector ();
      z.$setSelector (x);
      return z;
    }
  }
}
