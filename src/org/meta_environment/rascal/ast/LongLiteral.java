package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLongLiteralDecimalLongLiteral (this);
    }
  }
  public class Ambiguity extends LongLiteral
  {
    private final List < LongLiteral > alternatives;
    public Ambiguity (List < LongLiteral > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < LongLiteral > getAlternatives ()
    {
      return alternatives;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLongLiteralHexLongLiteral (this);
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLongLiteralOctalLongLiteral (this);
    }
  }
}
