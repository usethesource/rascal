package org.meta_environment.rascal.ast;
public abstract class Literal extends AbstractAST
{
  public class RegExp extends Literal
  {
    private RegExpLiteral regExp;

    private RegExp ()
    {
    }
    /*package */ RegExp (ITree tree, RegExpLiteral regExp)
    {
      this.tree = tree;
      this.regExp = regExp;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitRegExpLiteral (this);
    }
    private final RegExpLiteral regExp;
    public RegExpLiteral getregExp ()
    {
      return regExp;
    }
    private void privateSetregExp (RegExpLiteral x)
    {
      this.regExp = x;
    }
    public RegExp setregExp (RegExpLiteral x)
    {
      z = new RegExp ();
      z.privateSetregExp (x);
      return z;
    }
  }
  public class Symbol extends Literal
  {
    private SymbolLiteral symbol;

    private Symbol ()
    {
    }
    /*package */ Symbol (ITree tree, SymbolLiteral symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSymbolLiteral (this);
    }
    private final SymbolLiteral symbol;
    public SymbolLiteral getsymbol ()
    {
      return symbol;
    }
    private void privateSetsymbol (SymbolLiteral x)
    {
      this.symbol = x;
    }
    public Symbol setsymbol (SymbolLiteral x)
    {
      z = new Symbol ();
      z.privateSetsymbol (x);
      return z;
    }
  }
  public class Boolean extends Literal
  {
    private BooleanLiteral boolean;

    private Boolean ()
    {
    }
    /*package */ Boolean (ITree tree, BooleanLiteral boolean)
    {
      this.tree = tree;
      this.boolean = boolean;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitBooleanLiteral (this);
    }
    private final BooleanLiteral boolean;
    public BooleanLiteral getboolean ()
    {
      return boolean;
    }
    private void privateSetboolean (BooleanLiteral x)
    {
      this.boolean = x;
    }
    public Boolean setboolean (BooleanLiteral x)
    {
      z = new Boolean ();
      z.privateSetboolean (x);
      return z;
    }
  }
  public class Integer extends Literal
  {
    private IntegerLiteral integer;

    private Integer ()
    {
    }
    /*package */ Integer (ITree tree, IntegerLiteral integer)
    {
      this.tree = tree;
      this.integer = integer;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIntegerLiteral (this);
    }
    private final IntegerLiteral integer;
    public IntegerLiteral getinteger ()
    {
      return integer;
    }
    private void privateSetinteger (IntegerLiteral x)
    {
      this.integer = x;
    }
    public Integer setinteger (IntegerLiteral x)
    {
      z = new Integer ();
      z.privateSetinteger (x);
      return z;
    }
  }
  public class Double extends Literal
  {
    private FloatingPointLiteral double;

    private Double ()
    {
    }
    /*package */ Double (ITree tree, FloatingPointLiteral double)
    {
      this.tree = tree;
      this.double = double;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDoubleLiteral (this);
    }
    private final FloatingPointLiteral double;
    public FloatingPointLiteral getdouble ()
    {
      return double;
    }
    private void privateSetdouble (FloatingPointLiteral x)
    {
      this.double = x;
    }
    public Double setdouble (FloatingPointLiteral x)
    {
      z = new Double ();
      z.privateSetdouble (x);
      return z;
    }
  }
  public class String extends Literal
  {
    private StringLiteral string;

    private String ()
    {
    }
    /*package */ String (ITree tree, StringLiteral string)
    {
      this.tree = tree;
      this.string = string;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitStringLiteral (this);
    }
    private final StringLiteral string;
    public StringLiteral getstring ()
    {
      return string;
    }
    private void privateSetstring (StringLiteral x)
    {
      this.string = x;
    }
    public String setstring (StringLiteral x)
    {
      z = new String ();
      z.privateSetstring (x);
      return z;
    }
  }
}
