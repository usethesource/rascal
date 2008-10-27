package org.meta_environment.rascal.ast;
public abstract class LongLiteral extends AbstractAST
{
  public class DecimalLongLiteral extends LongLiteral
  {
/* DecimalLongLiteral -> LongLiteral {prefer, cons("DecimalLongLiteral")} */
    private DecimalLongLiteral ()
    {
    }
    /*package */ DecimalLongLiteral (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDecimalLongLiteralLongLiteral (this);
    }
  }
  public class HexLongLiteral extends LongLiteral
  {
/* HexLongLiteral -> LongLiteral {prefer, cons("HexLongLiteral")} */
    private HexLongLiteral ()
    {
    }
    /*package */ HexLongLiteral (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitHexLongLiteralLongLiteral (this);
    }
  }
  public class OctalLongLiteral extends LongLiteral
  {
/* OctalLongLiteral -> LongLiteral {prefer, cons("OctalLongLiteral")} */
    private OctalLongLiteral ()
    {
    }
    /*package */ OctalLongLiteral (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitOctalLongLiteralLongLiteral (this);
    }
  }
}
