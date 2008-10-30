package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class OptCharRanges extends AbstractAST
{
  public class Absent extends OptCharRanges
  {
/*  -> OptCharRanges {cons("Absent")} */
    private Absent ()
    {
    }
    /*package */ Absent (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitOptCharRangesAbsent (this);
    }
  }
  public class Ambiguity extends OptCharRanges
  {
    private final java.util.List < OptCharRanges > alternatives;
    public Ambiguity (java.util.List < OptCharRanges > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < OptCharRanges > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Present extends OptCharRanges
  {
/* ranges:CharRanges -> OptCharRanges {cons("Present")} */
    private Present ()
    {
    }
    /*package */ Present (ITree tree, CharRanges ranges)
    {
      this.tree = tree;
      this.ranges = ranges;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitOptCharRangesPresent (this);
    }
    private CharRanges ranges;
    public CharRanges getRanges ()
    {
      return ranges;
    }
    private void $setRanges (CharRanges x)
    {
      this.ranges = x;
    }
    public Present setRanges (CharRanges x)
    {
      Present z = new Present ();
      z.$setRanges (x);
      return z;
    }
  }
}
