package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
public abstract class Renamings extends AbstractAST
{
  public class Default extends Renamings
  {
/* "renaming" renamings:{Renaming ","}+ -> Renamings {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Renaming > renamings)
    {
      this.tree = tree;
      this.renamings = renamings;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitRenamingsDefault (this);
    }
    private List < Renaming > renamings;
    public List < Renaming > getrenamings ()
    {
      return renamings;
    }
    private void $setrenamings (List < Renaming > x)
    {
      this.renamings = x;
    }
    public Default setrenamings (List < Renaming > x)
    {
      Default z = new Default ();
      z.$setrenamings (x);
      return z;
    }
  }
  public class Ambiguity extends Renamings
  {
    private final List < Renamings > alternatives;
    public Ambiguity (List < Renamings > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Renamings > getAlternatives ()
    {
      return alternatives;
    }
  }
}
