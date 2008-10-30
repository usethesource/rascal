package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
      return visitor.visitTagsDefault (this);
    }
    private List < Tag > annotations;
    public List < Tag > getannotations ()
    {
      return annotations;
    }
    private void $setannotations (List < Tag > x)
    {
      this.annotations = x;
    }
    public Default setannotations (List < Tag > x)
    {
      Default z = new Default ();
      z.$setannotations (x);
      return z;
    }
  }
  public class Ambiguity extends Tags
  {
    private final List < Tags > alternatives;
    public Ambiguity (List < Tags > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Tags > getAlternatives ()
    {
      return alternatives;
    }
  }
}
