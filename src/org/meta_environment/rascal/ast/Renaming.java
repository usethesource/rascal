package org.meta_environment.rascal.ast;
public abstract class Renaming extends AbstractAST
{
  public class Renaming extends Renaming
  {
/* from:Name "=>" to:Name -> Renaming {cons("Renaming")} */
    private Renaming ()
    {
    }
    /*package */ Renaming (ITree tree, Name from, Name to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitRenamingRenaming (this);
    }
    private final Name from;
    public Name getfrom ()
    {
      return from;
    }
    private void privateSetfrom (Name x)
    {
      this.from = x;
    }
    public Renaming setfrom (Name x)
    {
      z = new Renaming ();
      z.privateSetfrom (x);
      return z;
    }
    private final Name to;
    public Name getto ()
    {
      return to;
    }
    private void privateSetto (Name x)
    {
      this.to = x;
    }
    public Renaming setto (Name x)
    {
      z = new Renaming ();
      z.privateSetto (x);
      return z;
    }
  }
}
