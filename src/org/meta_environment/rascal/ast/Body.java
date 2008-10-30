package org.meta_environment.rascal.ast;
import java.util.List;

import org.eclipse.imp.pdb.facts.ITree;
public abstract class Body extends AbstractAST
{
  public class Toplevels extends Body
  {
/* toplevels:Toplevel* -> Body {cons("Toplevels")} */
    private Toplevels ()
    {
    }
    /*package */ Toplevels (ITree tree, List < Toplevel > toplevels)
    {
      this.tree = tree;
      this.toplevels = toplevels;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBodyToplevels (this);
    }
    private List < Toplevel > toplevels;
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
      Toplevels z = new Toplevels ();
      z.privateSettoplevels (x);
      return z;
    }
  }
  public class Ambiguity extends Body
  {
    private final List < Body > alternatives;
    public Ambiguity (List < Body > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < Body > getAlternatives ()
    {
      return alternatives;
    }
  }
}
