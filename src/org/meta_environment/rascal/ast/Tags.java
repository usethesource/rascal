package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Tags extends AbstractAST
{
  public class Default extends Tags
  {
/* annotations:Tag* -> Tags {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Tag > annotations)
    {
      this.tree = tree;
      this.annotations = annotations;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDefaultTags (this);
    }
    private List < Tag > annotations;
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
      Default z = new Default ();
      z.privateSetannotations (x);
      return z;
    }
  }
}
