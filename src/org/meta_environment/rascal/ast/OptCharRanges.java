package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
      return visitor.visitAbsentOptCharRanges (this);
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
      return visitor.visitPresentOptCharRanges (this);
    }
    private CharRanges ranges;
    public CharRanges getranges ()
    {
      return ranges;
    }
    private void privateSetranges (CharRanges x)
    {
      this.ranges = x;
    }
    public Present setranges (CharRanges x)
    {
      z = new Present ();
      z.privateSetranges (x);
      return z;
    }
  }
}
