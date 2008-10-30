package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Body extends AbstractAST
{
  public class Toplevels extends Body
  {
/* toplevels:Toplevel* -> Body {cons("Toplevels")} */
    private Toplevels ()
    {
    }
    /*package */ Toplevels (ITree tree, java.util.List < Toplevel > toplevels)
    {
      this.tree = tree;
    params2statements (java.util.List < Toplevel > toplevels)}
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBodyToplevels (this);
    }
    private java.util.List < Toplevel > toplevels;
    public java.util.List < Toplevel > getToplevels ()
    {
      return toplevels;
    }
    private void $setToplevels (java.util.List < Toplevel > x)
    {
      this.toplevels = x;
    }
    public Toplevels setToplevels (java.util.List < Toplevel > x)
    {
      Toplevels z = new Toplevels ();
      z.$setToplevels (x);
      return z;
    }
  }
  public class Ambiguity extends Body
  {
    private final java.util.List < Body > alternatives;
    public Ambiguity (java.util.List < Body > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Body > getAlternatives ()
    {
      return alternatives;
    }
  }
}
