package org.meta_environment.rascal.ast;
public abstract class Annotations extends AbstractAST
{
  public class Default extends Annotations
  {
    private List < Annotation > annotations;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Annotation > annotations)
    {
      this.tree = tree;
      this.annotations = annotations;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultAnnotations (this);
    }
    private final List < Annotation > annotations;
    public List < Annotation > getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (List < Annotation > x)
    {
      this.annotations = x;
    }
    public Default setannotations (List < Annotation > x)
    {
      z = new Default ();
      z.privateSetannotations (x);
      return z;
    }
  }
}
