package org.meta_environment.rascal.ast;
public abstract class Visibility extends AbstractAST
{
  public class Public extends Visibility
  {
    private Public ()
    {
    }
    /*package */ Public (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitPublicVisibility (this);
    }
  }
  public class Private extends Visibility
  {
    private Private ()
    {
    }
    /*package */ Private (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitPrivateVisibility (this);
    }
  }
}
