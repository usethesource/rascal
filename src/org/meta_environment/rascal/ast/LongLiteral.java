package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class LongLiteral extends AbstractAST
{
  static public class DecimalLongLiteral extends LongLiteral
  {
/* DecimalLongLiteral -> LongLiteral {prefer, cons("DecimalLongLiteral")} */
    private DecimalLongLiteral ()
    {
    }
    /*package */ DecimalLongLiteral (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLongLiteralDecimalLongLiteral (this);
    }
  }
  public class Ambiguity extends LongLiteral
  {
    private final java.util.List < LongLiteral > alternatives;
    public Ambiguity (java.util.List < LongLiteral > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < LongLiteral > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class HexLongLiteral extends LongLiteral
  {
/* HexLongLiteral -> LongLiteral {prefer, cons("HexLongLiteral")} */
    private HexLongLiteral ()
    {
    }
    /*package */ HexLongLiteral (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLongLiteralHexLongLiteral (this);
    }
  }
  static public class OctalLongLiteral extends LongLiteral
  {
/* OctalLongLiteral -> LongLiteral {prefer, cons("OctalLongLiteral")} */
    private OctalLongLiteral ()
    {
    }
    /*package */ OctalLongLiteral (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLongLiteralOctalLongLiteral (this);
    }
  }
}
