package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Literal extends AbstractAST
{
  public class RegExp extends Literal
  {
/* regExp:RegExpLiteral -> Literal {cons("RegExp")} */
    private RegExp ()
    {
    }
    /*package */ RegExp (ITree tree, RegExpLiteral regExp)
    {
      this.tree = tree;
      this.regExp = regExp;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralRegExp (this);
    }
    private RegExpLiteral regExp;
    public RegExpLiteral getRegExp ()
    {
      return regExp;
    }
    private void $setRegExp (RegExpLiteral x)
    {
      this.regExp = x;
    }
    public RegExp setRegExp (RegExpLiteral x)
    {
      RegExp z = new RegExp ();
      z.$setRegExp (x);
      return z;
    }
  }
  public class Ambiguity extends Literal
  {
    private final java.util.List < Literal > alternatives;
    public Ambiguity (java.util.List < Literal > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Literal > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Symbol extends Literal
  {
/* symbol:SymbolLiteral -> Literal {cons("Symbol")} */
    private Symbol ()
    {
    }
    /*package */ Symbol (ITree tree, SymbolLiteral symbol)
    {
      this.tree = tree;
      this.symbol = symbol;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralSymbol (this);
    }
    private SymbolLiteral symbol;
    public SymbolLiteral getSymbol ()
    {
      return symbol;
    }
    private void $setSymbol (SymbolLiteral x)
    {
      this.symbol = x;
    }
    public Symbol setSymbol (SymbolLiteral x)
    {
      Symbol z = new Symbol ();
      z.$setSymbol (x);
      return z;
    }
  }
  public class Boolean extends Literal
  {
/* boolean:BooleanLiteral -> Literal {cons("Boolean")} */
    private Boolean ()
    {
    }
    /*package */ Boolean (ITree tree, BooleanLiteral boolean)
    {
      this.tree = tree;
      this.boolean = boolean;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralBoolean (this);
    }
    private BooleanLiteral boolean;
    public BooleanLiteral getBoolean ()
    {
      return boolean;
    }
    private void $setBoolean (BooleanLiteral x)
    {
      this.boolean = x;
    }
    public Boolean setBoolean (BooleanLiteral x)
    {
      Boolean z = new Boolean ();
      z.$setBoolean (x);
      return z;
    }
  }
  public class Integer extends Literal
  {
/* integer:IntegerLiteral -> Literal {cons("Integer")} */
    private Integer ()
    {
    }
    /*package */ Integer (ITree tree, IntegerLiteral integer)
    {
      this.tree = tree;
      this.integer = integer;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralInteger (this);
    }
    private IntegerLiteral integer;
    public IntegerLiteral getInteger ()
    {
      return integer;
    }
    private void $setInteger (IntegerLiteral x)
    {
      this.integer = x;
    }
    public Integer setInteger (IntegerLiteral x)
    {
      Integer z = new Integer ();
      z.$setInteger (x);
      return z;
    }
  }
  public class Double extends Literal
  {
/* double:FloatingPointLiteral -> Literal {cons("Double")} */
    private Double ()
    {
    }
    /*package */ Double (ITree tree, FloatingPointLiteral double)
    {
      this.tree = tree;
      this.double = double;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralDouble (this);
    }
    private FloatingPointLiteral double;
    public FloatingPointLiteral getDouble ()
    {
      return double;
    }
    private void $setDouble (FloatingPointLiteral x)
    {
      this.double = x;
    }
    public Double setDouble (FloatingPointLiteral x)
    {
      Double z = new Double ();
      z.$setDouble (x);
      return z;
    }
  }
  public class String extends Literal
  {
/* string:StringLiteral -> Literal {cons("String")} */
    private String ()
    {
    }
    /*package */ String (ITree tree, StringLiteral string)
    {
      this.tree = tree;
      this.string = string;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralString (this);
    }
    private StringLiteral string;
    public StringLiteral getString ()
    {
      return string;
    }
    private void $setString (StringLiteral x)
    {
      this.string = x;
    }
    public String setString (StringLiteral x)
    {
      String z = new String ();
      z.$setString (x);
      return z;
    }
  }
}
