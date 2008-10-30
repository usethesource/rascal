package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Break extends AbstractAST
{
  public class Labeled extends Break
  {
/* "break" label:Name ";" -> Break {cons("Labeled")} */
    private Labeled ()
    {
    }
    /*package */ Labeled (ITree tree, Name label)
    {
      this.tree = tree;
      this.label = label;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBreakLabeled (this);
    }
    private Name label;
    public Name getLabel ()
    {
      return label;
    }
    private void $setLabel (Name x)
    {
      this.label = x;
    }
    public Labeled setLabel (Name x)
    {
      Labeled z = new Labeled ();
      z.$setLabel (x);
      return z;
    }
  }
  public class Ambiguity extends Break
  {
    private final java.util.List < Break > alternatives;
    public Ambiguity (java.util.List < Break > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Break > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Unlabeled extends Break
  {
/* "break" ";" -> Break {cons("Unlabeled")} */
    private Unlabeled ()
    {
    }
    /*package */ Unlabeled (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBreakUnlabeled (this);
    }
  }
}
