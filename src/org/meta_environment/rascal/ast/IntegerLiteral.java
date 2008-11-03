package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class IntegerLiteral extends AbstractAST
{
  public org.meta_environment.rascal.ast.DecimalIntegerLiteral getDecimal ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class DecimalIntegerLiteral extends IntegerLiteral
  {
/* decimal:DecimalIntegerLiteral -> IntegerLiteral {prefer, cons("DecimalIntegerLiteral")} */
    private DecimalIntegerLiteral ()
    {
    }
    /*package */ DecimalIntegerLiteral (ITree tree,
					org.meta_environment.rascal.ast.
					DecimalIntegerLiteral decimal)
    {
      this.tree = tree;
      this.decimal = decimal;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitIntegerLiteralDecimalIntegerLiteral (this);
    }
    private org.meta_environment.rascal.ast.DecimalIntegerLiteral decimal;
    public org.meta_environment.rascal.ast.DecimalIntegerLiteral getDecimal ()
    {
      return decimal;
    }
    private void $setDecimal (org.meta_environment.rascal.ast.
			      DecimalIntegerLiteral x)
    {
      this.decimal = x;
    }
    public DecimalIntegerLiteral setDecimal (org.meta_environment.rascal.ast.
					     DecimalIntegerLiteral x)
    {
      DecimalIntegerLiteral z = new DecimalIntegerLiteral ();
      z.$setDecimal (x);
      return z;
    }
  }
  static public class Ambiguity extends IntegerLiteral
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.IntegerLiteral > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.IntegerLiteral >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.IntegerLiteral >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public org.meta_environment.rascal.ast.HexIntegerLiteral getHex ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class HexIntegerLiteral extends IntegerLiteral
  {
/* hex:HexIntegerLiteral -> IntegerLiteral {prefer, cons("HexIntegerLiteral")} */
    private HexIntegerLiteral ()
    {
    }
    /*package */ HexIntegerLiteral (ITree tree,
				    org.meta_environment.rascal.ast.
				    HexIntegerLiteral hex)
    {
      this.tree = tree;
      this.hex = hex;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitIntegerLiteralHexIntegerLiteral (this);
    }
    private org.meta_environment.rascal.ast.HexIntegerLiteral hex;
    public org.meta_environment.rascal.ast.HexIntegerLiteral getHex ()
    {
      return hex;
    }
    private void $setHex (org.meta_environment.rascal.ast.HexIntegerLiteral x)
    {
      this.hex = x;
    }
    public HexIntegerLiteral setHex (org.meta_environment.rascal.ast.
				     HexIntegerLiteral x)
    {
      HexIntegerLiteral z = new HexIntegerLiteral ();
      z.$setHex (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.OctalIntegerLiteral getOctal ()
  {
    throw new UnsupportedOperationException ();
  }
  static public class OctalIntegerLiteral extends IntegerLiteral
  {
/* octal:OctalIntegerLiteral -> IntegerLiteral {prefer, cons("OctalIntegerLiteral")} */
    private OctalIntegerLiteral ()
    {
    }
    /*package */ OctalIntegerLiteral (ITree tree,
				      org.meta_environment.rascal.ast.
				      OctalIntegerLiteral octal)
    {
      this.tree = tree;
      this.octal = octal;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitIntegerLiteralOctalIntegerLiteral (this);
    }
    private org.meta_environment.rascal.ast.OctalIntegerLiteral octal;
    public org.meta_environment.rascal.ast.OctalIntegerLiteral getOctal ()
    {
      return octal;
    }
    private void $setOctal (org.meta_environment.rascal.ast.
			    OctalIntegerLiteral x)
    {
      this.octal = x;
    }
    public OctalIntegerLiteral setOctal (org.meta_environment.rascal.ast.
					 OctalIntegerLiteral x)
    {
      OctalIntegerLiteral z = new OctalIntegerLiteral ();
      z.$setOctal (x);
      return z;
    }
  }
}
