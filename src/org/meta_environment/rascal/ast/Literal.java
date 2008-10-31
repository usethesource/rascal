package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Literal extends AbstractAST
{
  static public class RegExp extends Literal
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
  static public class Ambiguity extends Literal
  {
    private final java.util.List < Literal > alternatives;
    public Ambiguity (java.util.List < Literal > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Literal > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Symbol extends Literal
  {
/* symbolLiteral:SymbolLiteral -> Literal {cons("Symbol")} */
    private Symbol ()
    {
    }
    /*package */ Symbol (ITree tree, SymbolLiteral symbolLiteral)
    {
      this.tree = tree;
      this.symbolLiteral = symbolLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralSymbol (this);
    }
    private SymbolLiteral symbolLiteral;
    public SymbolLiteral getSymbolLiteral ()
    {
      return symbolLiteral;
    }
    private void $setSymbolLiteral (SymbolLiteral x)
    {
      this.symbolLiteral = x;
    }
    public Symbol setSymbolLiteral (SymbolLiteral x)
    {
      Symbol z = new Symbol ();
      z.$setSymbolLiteral (x);
      return z;
    }
  }
  static public class Boolean extends Literal
  {
/* booleanLiteral:BooleanLiteral -> Literal {cons("Boolean")} */
    private Boolean ()
    {
    }
    /*package */ Boolean (ITree tree, BooleanLiteral booleanLiteral)
    {
      this.tree = tree;
      this.booleanLiteral = booleanLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralBoolean (this);
    }
    private BooleanLiteral booleanLiteral;
    public BooleanLiteral getBooleanLiteral ()
    {
      return booleanLiteral;
    }
    private void $setBooleanLiteral (BooleanLiteral x)
    {
      this.booleanLiteral = x;
    }
    public Boolean setBooleanLiteral (BooleanLiteral x)
    {
      Boolean z = new Boolean ();
      z.$setBooleanLiteral (x);
      return z;
    }
  }
  static public class Integer extends Literal
  {
/* integerLiteral:IntegerLiteral -> Literal {cons("Integer")} */
    private Integer ()
    {
    }
    /*package */ Integer (ITree tree, IntegerLiteral integerLiteral)
    {
      this.tree = tree;
      this.integerLiteral = integerLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralInteger (this);
    }
    private IntegerLiteral integerLiteral;
    public IntegerLiteral getIntegerLiteral ()
    {
      return integerLiteral;
    }
    private void $setIntegerLiteral (IntegerLiteral x)
    {
      this.integerLiteral = x;
    }
    public Integer setIntegerLiteral (IntegerLiteral x)
    {
      Integer z = new Integer ();
      z.$setIntegerLiteral (x);
      return z;
    }
  }
  static public class Double extends Literal
  {
/* doubleLiteral:FloatingPointLiteral -> Literal {cons("Double")} */
    private Double ()
    {
    }
    /*package */ Double (ITree tree, FloatingPointLiteral doubleLiteral)
    {
      this.tree = tree;
      this.doubleLiteral = doubleLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralDouble (this);
    }
    private FloatingPointLiteral doubleLiteral;
    public FloatingPointLiteral getDoubleLiteral ()
    {
      return doubleLiteral;
    }
    private void $setDoubleLiteral (FloatingPointLiteral x)
    {
      this.doubleLiteral = x;
    }
    public Double setDoubleLiteral (FloatingPointLiteral x)
    {
      Double z = new Double ();
      z.$setDoubleLiteral (x);
      return z;
    }
  }
  static public class String extends Literal
  {
/* stringLiteral:StringLiteral -> Literal {cons("String")} */
    private String ()
    {
    }
    /*package */ String (ITree tree, StringLiteral stringLiteral)
    {
      this.tree = tree;
      this.stringLiteral = stringLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralString (this);
    }
    private StringLiteral stringLiteral;
    public StringLiteral getStringLiteral ()
    {
      return stringLiteral;
    }
    private void $setStringLiteral (StringLiteral x)
    {
      this.stringLiteral = x;
    }
    public String setStringLiteral (StringLiteral x)
    {
      String z = new String ();
      z.$setStringLiteral (x);
      return z;
    }
  }
}
