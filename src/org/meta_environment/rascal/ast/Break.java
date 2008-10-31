package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Break extends AbstractAST
{
  public class WithLabel extends Break
  {
/* "break" label:Name ";" -> Break {cons("WithLabel")} */
    private WithLabel ()
    {
    }
    /*package */ WithLabel (ITree tree, Name label)
    {
      this.tree = tree;
      this.label = label;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBreakWithLabel (this);
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
    public WithLabel setLabel (Name x)
    {
      WithLabel z = new WithLabel ();
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
  public class NoLabel extends Break
  {
/* "break" ";" -> Break {cons("NoLabel")} */
    private NoLabel ()
    {
    }
    /*package */ NoLabel (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBreakNoLabel (this);
    }
  }
}
