package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Type extends AbstractAST
{
  public class Basic extends Type
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
    public BasicType getbasic ()
    {
      return basic;
    }
    private void $setbasic (BasicType x)
    {
      this.basic = x;
    }
    public Basic setbasic (BasicType x)
    {
      Basic z = new Basic ();
      z.$setbasic (x);
      return z;
    }
  }
  public class Ambiguity extends Type
  {
    private final List < Type > alternatives;
    public Ambiguity (List < Type > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Type > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Structured extends Type
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
    public StructuredType getstructured ()
    {
      return structured;
    }
    private void $setstructured (StructuredType x)
    {
      this.structured = x;
    }
    public Structured setstructured (StructuredType x)
    {
      Structured z = new Structured ();
      z.$setstructured (x);
      return z;
    }
  }
  public class Function extends Type
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
    public FunctionType getfunction ()
    {
      return function;
    }
    private void $setfunction (FunctionType x)
    {
      this.function = x;
    }
    public Function setfunction (FunctionType x)
    {
      Function z = new Function ();
      z.$setfunction (x);
      return z;
    }
  }
  public class Variable extends Type
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
    public TypeVar gettypeVar ()
    {
      return typeVar;
    }
    private void $settypeVar (TypeVar x)
    {
      this.typeVar = x;
    }
    public Variable settypeVar (TypeVar x)
    {
      Variable z = new Variable ();
      z.$settypeVar (x);
      return z;
    }
  }
  public class User extends Type
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
    public UserType getuser ()
    {
      return user;
    }
    private void $setuser (UserType x)
    {
      this.user = x;
    }
    public User setuser (UserType x)
    {
      User z = new User ();
      z.$setuser (x);
      return z;
    }
  }
  public class Symbol extends Type
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
    public Symbol getsymbol ()
    {
      return symbol;
    }
    private void $setsymbol (Symbol x)
    {
      this.symbol = x;
    }
    public Symbol setsymbol (Symbol x)
    {
      Symbol z = new Symbol ();
      z.$setsymbol (x);
      return z;
    }
  }
  public class Selector extends Type
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
    public DataTypeSelector getselector ()
    {
      return selector;
    }
    private void $setselector (DataTypeSelector x)
    {
      this.selector = x;
    }
    public Selector setselector (DataTypeSelector x)
    {
      Selector z = new Selector ();
      z.$setselector (x);
      return z;
    }
  }
}
