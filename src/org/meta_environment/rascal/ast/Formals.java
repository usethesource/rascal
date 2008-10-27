package org.meta_environment.rascal.ast;
public abstract class Formals extends AbstractAST
{
  public class Formals extends Formals
  {
/* formals:{Formal ","}* -> Formals {cons("Formals")} */
    private Formals ()
    {
    }
    /*package */ Formals (ITree tree, List < Formal > formals)
    {
      this.tree = tree;
      this.formals = formals;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitFormalsFormals (this);
    }
    private final List < Formal > formals;
    public List < Formal > getformals ()
    {
      return formals;
    }
    private void privateSetformals (List < Formal > x)
    {
      this.formals = x;
    }
    public Formals setformals (List < Formal > x)
    {
      z = new Formals ();
      z.privateSetformals (x);
      return z;
    }
  }
}
