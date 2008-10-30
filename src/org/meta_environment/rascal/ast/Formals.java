package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Formals extends AbstractAST
{
  public class Default extends Formals
  {
/* formals:{Formal ","}* -> Formals {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, java.util.List < Formal > formals)
    {
      this.tree = tree;
    params2statements (java.util.List < Formal > formals)}
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitFormalsDefault (this);
    }
    private java.util.List < Formal > formals;
    public java.util.List < Formal > getFormals ()
    {
      return formals;
    }
    private void $setFormals (java.util.List < Formal > x)
    {
      this.formals = x;
    }
    public Default setFormals (java.util.List < Formal > x)
    {
      Default z = new Default ();
      z.$setFormals (x);
      return z;
    }
  }
  public class Ambiguity extends Formals
  {
    private final java.util.List < Formals > alternatives;
    public Ambiguity (java.util.List < Formals > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Formals > getAlternatives ()
    {
      return alternatives;
    }
  }
}
