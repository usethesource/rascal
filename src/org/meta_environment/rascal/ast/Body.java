package org.meta_environment.rascal.ast;
public abstract class Body extends AbstractAST
{
  public class Toplevels extends Body
  {
    private List < Toplevel > toplevels;

    private Toplevels ()
    {
    }
    /*package */ Toplevels (ITree tree, List < Toplevel > toplevels)
    {
      this.tree = tree;
      this.toplevels = toplevels;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitToplevelsBody (this);
    }
    private final List < Toplevel > toplevels;
    public List < Toplevel > gettoplevels ()
    {
      return toplevels;
    }
    private void privateSettoplevels (List < Toplevel > x)
    {
      this.toplevels = x;
    }
    public Toplevels settoplevels (List < Toplevel > x)
    {
      z = new Toplevels ();
      z.privateSettoplevels (x);
      return z;
    }
  }
}
