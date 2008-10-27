package org.meta_environment.rascal.ast;
public abstract class LongLiteral extends AbstractAST
{
  public class DecimalLongLiteral extends LongLiteral
  {
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
