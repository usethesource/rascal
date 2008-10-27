package org.meta_environment.rascal.ast;
public abstract class Tags extends AbstractAST
{
  public class Default extends Tags
  {
    private List < Tag > annotations;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Tag > annotations)
    {
      this.tree = tree;
      this.annotations = annotations;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultTags (this);
    }
    private final List < Tag > annotations;
    public List < Tag > getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (List < Tag > x)
    {
      this.annotations = x;
    }
    public Default setannotations (List < Tag > x)
    {
      z = new Default ();
      z.privateSetannotations (x);
      return z;
    }
  }
}
