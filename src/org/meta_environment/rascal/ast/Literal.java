package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Literal extends AbstractAST
{
  static public class RegExp extends Literal
  {
/* regExpLiteral:RegExpLiteral -> Literal {cons("RegExp")} */
    private RegExp ()
    {
    }
    /*package */ RegExp (ITree tree,
			 org.meta_environment.rascal.ast.
			 RegExpLiteral regExpLiteral)
    {
      this.tree = tree;
      this.regExpLiteral = regExpLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralRegExp (this);
    }
    private org.meta_environment.rascal.ast.RegExpLiteral regExpLiteral;
    public org.meta_environment.rascal.ast.RegExpLiteral getRegExpLiteral ()
    {
      return regExpLiteral;
    }
    private void $setRegExpLiteral (org.meta_environment.rascal.ast.
				    RegExpLiteral x)
    {
      this.regExpLiteral = x;
    }
    public RegExp setRegExpLiteral (org.meta_environment.rascal.ast.
				    RegExpLiteral x)
    {
      RegExp z = new RegExp ();
      z.$setRegExpLiteral (x);
      return z;
    }
  }
  static public class Ambiguity extends Literal
  {
    private final java.util.List < org.meta_environment.rascal.ast.Literal >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Literal > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Literal >
      getAlternatives ()
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
    /*package */ Symbol (ITree tree,
			 org.meta_environment.rascal.ast.
			 SymbolLiteral symbolLiteral)
    {
      this.tree = tree;
      this.symbolLiteral = symbolLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralSymbol (this);
    }
    private org.meta_environment.rascal.ast.SymbolLiteral symbolLiteral;
    public org.meta_environment.rascal.ast.SymbolLiteral getSymbolLiteral ()
    {
      return symbolLiteral;
    }
    private void $setSymbolLiteral (org.meta_environment.rascal.ast.
				    SymbolLiteral x)
    {
      this.symbolLiteral = x;
    }
    public Symbol setSymbolLiteral (org.meta_environment.rascal.ast.
				    SymbolLiteral x)
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
    /*package */ Boolean (ITree tree,
			  org.meta_environment.rascal.ast.
			  BooleanLiteral booleanLiteral)
    {
      this.tree = tree;
      this.booleanLiteral = booleanLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralBoolean (this);
    }
    private org.meta_environment.rascal.ast.BooleanLiteral booleanLiteral;
    public org.meta_environment.rascal.ast.BooleanLiteral getBooleanLiteral ()
    {
      return booleanLiteral;
    }
    private void $setBooleanLiteral (org.meta_environment.rascal.ast.
				     BooleanLiteral x)
    {
      this.booleanLiteral = x;
    }
    public Boolean setBooleanLiteral (org.meta_environment.rascal.ast.
				      BooleanLiteral x)
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
    /*package */ Integer (ITree tree,
			  org.meta_environment.rascal.ast.
			  IntegerLiteral integerLiteral)
    {
      this.tree = tree;
      this.integerLiteral = integerLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralInteger (this);
    }
    private org.meta_environment.rascal.ast.IntegerLiteral integerLiteral;
    public org.meta_environment.rascal.ast.IntegerLiteral getIntegerLiteral ()
    {
      return integerLiteral;
    }
    private void $setIntegerLiteral (org.meta_environment.rascal.ast.
				     IntegerLiteral x)
    {
      this.integerLiteral = x;
    }
    public Integer setIntegerLiteral (org.meta_environment.rascal.ast.
				      IntegerLiteral x)
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
    /*package */ Double (ITree tree,
			 org.meta_environment.rascal.ast.
			 FloatingPointLiteral doubleLiteral)
    {
      this.tree = tree;
      this.doubleLiteral = doubleLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralDouble (this);
    }
    private org.meta_environment.rascal.ast.
      FloatingPointLiteral doubleLiteral;
    public org.meta_environment.rascal.ast.
      FloatingPointLiteral getDoubleLiteral ()
    {
      return doubleLiteral;
    }
    private void $setDoubleLiteral (org.meta_environment.rascal.ast.
				    FloatingPointLiteral x)
    {
      this.doubleLiteral = x;
    }
    public Double setDoubleLiteral (org.meta_environment.rascal.ast.
				    FloatingPointLiteral x)
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
    /*package */ String (ITree tree,
			 org.meta_environment.rascal.ast.
			 StringLiteral stringLiteral)
    {
      this.tree = tree;
      this.stringLiteral = stringLiteral;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLiteralString (this);
    }
    private org.meta_environment.rascal.ast.StringLiteral stringLiteral;
    public org.meta_environment.rascal.ast.StringLiteral getStringLiteral ()
    {
      return stringLiteral;
    }
    private void $setStringLiteral (org.meta_environment.rascal.ast.
				    StringLiteral x)
    {
      this.stringLiteral = x;
    }
    public String setStringLiteral (org.meta_environment.rascal.ast.
				    StringLiteral x)
    {
      String z = new String ();
      z.$setStringLiteral (x);
      return z;
    }
  }
}
