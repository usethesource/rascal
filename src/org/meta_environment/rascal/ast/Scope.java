package org.meta_environment.rascal.ast;
public abstract class Scope extends AbstractAST
{
  public class Global extends Scope
  {
    private Global ()
    {
    }
    /*package */ Global (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGlobalScope (this);
    }
  }
  public class Dynamic extends Scope
  {
    private Dynamic ()
    {
    }
    /*package */ Dynamic (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDynamicScope (this);
    }
  }
}
