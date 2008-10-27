package org.meta_environment.rascal.ast;
public abstract class Type extends AbstractAST
{
  public class Basic extends Type
  {
    private BasicType basic;

    private Basic ()
    {
    }
    /*package */ Basic (ITree tree, BasicType basic)
    {
      this.tree = tree;
      this.basic = basic;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBasicType (this);
    }
    private final BasicType basic;
    public BasicType getbasic ()
    {
      return basic;
    }
    private void privateSetbasic (BasicType x)
    {
      this.basic = x;
    }
    public Basic setbasic (BasicType x)
    {
      z = new Basic ();
      z.privateSetbasic (x);
      return z;
    }
  }
  public class Structured extends Type
  {
    private StructuredType structured;

    private Structured ()
    {
    }
    /*package */ Structured (ITree tree, StructuredType structured)
    {
      this.tree = tree;
      this.structured = structured;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitStructuredType (this);
    }
    private final StructuredType structured;
    public StructuredType getstructured ()
    {
      return structured;
    }
    private void privateSetstructured (StructuredType x)
    {
      this.structured = x;
    }
    public Structured setstructured (StructuredType x)
    {
      z = new Structured ();
      z.privateSetstructured (x);
      return z;
    }
  }
  public class Function extends Type
  {
    private FunctionType function;

    private Function ()
    {
    }
    /*package */ Function (ITree tree, FunctionType function)
    {
      this.tree = tree;
      this.function = function;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFunctionType (this);
    }
    private final FunctionType function;
    public FunctionType getfunction ()
    {
      return function;
    }
    private void privateSetfunction (FunctionType x)
    {
      this.function = x;
    }
    public Function setfunction (FunctionType x)
    {
      z = new Function ();
      z.privateSetfunction (x);
      return z;
    }
  }
  public class Variable extends Type
  {
    private TypeVar typeVar;

    private Variable ()
    {
    }
    /*package */ Variable (ITree tree, TypeVar typeVar)
    {
      this.tree = tree;
      this.typeVar = typeVar;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitVariableType (this);
    }
    private final TypeVar typeVar;
    public TypeVar gettypeVar ()
    {
      return typeVar;
    }
    private void privateSettypeVar (TypeVar x)
    {
      this.typeVar = x;
    }
    public Variable settypeVar (TypeVar x)
    {
      z = new Variable ();
      z.privateSettypeVar (x);
      return z;
    }
  }
  public class User extends Type
  {
    private UserType user;

    private User ()
    {
    }
    /*package */ User (ITree tree, UserType user)
    {
      this.tree = tree;
      this.user = user;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitUserType (this);
    }
    private final UserType user;
    public UserType getuser ()
    {
      return user;
    }
    private void privateSetuser (UserType x)
    {
      this.user = x;
    }
    public User setuser (UserType x)
    {
      z = new User ();
      z.privateSetuser (x);
      return z;
    }
  }
  public class Symbol extends Type
  {
    private Symbol symbol;

    private Symbol ()
    {
    }
    /*package */ Symbol (ITree tree, Symbol symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSymbolType (this);
    }
    private final Symbol symbol;
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Symbol setsymbol (Symbol x)
    {
      z = new Symbol ();
      z.privateSetsymbol (x);
      return z;
    }
  }
  public class Selector extends Type
  {
    private DataTypeSelector selector;

    private Selector ()
    {
    }
    /*package */ Selector (ITree tree, DataTypeSelector selector)
    {
      this.tree = tree;
      this.selector = selector;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSelectorType (this);
    }
    private final DataTypeSelector selector;
    public DataTypeSelector getselector ()
    {
      return selector;
    }
    private void privateSetselector (DataTypeSelector x)
    {
      this.selector = x;
    }
    public Selector setselector (DataTypeSelector x)
    {
      z = new Selector ();
      z.privateSetselector (x);
      return z;
    }
  }
}
