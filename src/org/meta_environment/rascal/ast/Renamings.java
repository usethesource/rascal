package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Renamings extends AbstractAST
{
  public class Default extends Renamings
  {
/* "renaming" renamings:{Renaming ","}+ -> Renamings {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, java.util.List < Renaming > renamings)
    {
      this.tree = tree;
      this.renamings = renamings;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitRenamingsDefault (this);
    }
    private java.util.List < Renaming > renamings;
    public java.util.List < Renaming > getRenamings ()
    {
      return renamings;
    }
    private void $setRenamings (java.util.List < Renaming > x)
    {
      this.renamings = x;
    }
    public Default setRenamings (java.util.List < Renaming > x)
    {
      Default z = new Default ();
      z.$setRenamings (x);
      return z;
    }
  }
  public class Ambiguity extends Renamings
  {
    private final java.util.List < Renamings > alternatives;
    public Ambiguity (java.util.List < Renamings > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Renamings > getAlternatives ()
    {
      return alternatives;
    }
  }
}
