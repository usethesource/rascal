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
    /*package */ Basic (ITree tree, BasicType basic)
    {
      this.tree = tree;
      this.basic = basic;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeBasic (this);
    }
    private BasicType basic;
    public BasicType getBasic ()
    {
      return basic;
    }
    private void $setBasic (BasicType x)
    {
      this.basic = x;
    }
    public Basic setBasic (BasicType x)
    {
      Basic z = new Basic ();
      z.$setBasic (x);
      return z;
    }
  }
  public class Ambiguity extends Type
  {
    private final java.util.List < Type > alternatives;
    public Ambiguity (java.util.List < Type > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Type > getAlternatives ()
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
    /*package */ Structured (ITree tree, StructuredType structured)
    {
      this.tree = tree;
      this.structured = structured;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeStructured (this);
    }
    private StructuredType structured;
    public StructuredType getStructured ()
    {
      return structured;
    }
    private void $setStructured (StructuredType x)
    {
      this.structured = x;
    }
    public Structured setStructured (StructuredType x)
    {
      Structured z = new Structured ();
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
    /*package */ Function (ITree tree, FunctionType function)
    {
      this.tree = tree;
      this.function = function;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeFunction (this);
    }
    private FunctionType function;
    public FunctionType getFunction ()
    {
      return function;
    }
    private void $setFunction (FunctionType x)
    {
      this.function = x;
    }
    public Function setFunction (FunctionType x)
    {
      Function z = new Function ();
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
    /*package */ Variable (ITree tree, TypeVar typeVar)
    {
      this.tree = tree;
      this.typeVar = typeVar;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeVariable (this);
    }
    private TypeVar typeVar;
    public TypeVar getTypeVar ()
    {
      return typeVar;
    }
    private void $setTypeVar (TypeVar x)
    {
      this.typeVar = x;
    }
    public Variable setTypeVar (TypeVar x)
    {
      Variable z = new Variable ();
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
    /*package */ User (ITree tree, UserType user)
    {
      this.tree = tree;
      this.user = user;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeUser (this);
    }
    private UserType user;
    public UserType getUser ()
    {
      return user;
    }
    private void $setUser (UserType x)
    {
      this.user = x;
    }
    public User setUser (UserType x)
    {
      User z = new User ();
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
    /*package */ Symbol (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeSymbol (this);
    }
    private Symbol symbol;
    public Symbol getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Symbol setSymbol (Symbol x)
    {
      Symbol z = new Symbol ();
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
    /*package */ Selector (ITree tree, DataTypeSelector selector)
    {
      this.tree = tree;
      this.selector = selector;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitTypeSelector (this);
    }
    private DataTypeSelector selector;
    public DataTypeSelector getSelector ()
    {
      return selector;
    }
    private void $setSelector (DataTypeSelector x)
    {
      this.selector = x;
    }
    public Selector setSelector (DataTypeSelector x)
    {
      Selector z = new Selector ();
      z.$setSelector (x);
      return z;
    }
  }
}
