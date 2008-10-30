package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitRenamingRenaming (this);
    }
    private Name from;
    public Name getfrom ()
    {
      return from;
    }
    private void $setfrom (Name x)
    {
      this.from = x;
    }
    public Renaming setfrom (Name x)
    {
      Renaming z = new Renaming ();
      z.$setfrom (x);
      return z;
    }
    private Name to;
    public Name getto ()
    {
      return to;
    }
    private void $setto (Name x)
    {
      this.to = x;
    }
    public Renaming setto (Name x)
    {
      Renaming z = new Renaming ();
      z.$setto (x);
      return z;
    }
  }
  public class Ambiguity extends Renaming
  {
    private final List < Renaming > alternatives;
    public Ambiguity (List < Renaming > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Renaming > getAlternatives ()
    {
      return alternatives;
    }
  }
}
