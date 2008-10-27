package org.meta_environment.rascal.ast;
public abstract class OptCharRanges extends AbstractAST
{
  public class Absent extends OptCharRanges
  {
    private Absent ()
    {
    }
    /*package */ Absent (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAbsentOptCharRanges (this);
    }
  }
  public class Present extends OptCharRanges
  {
    private CharRanges ranges;

    private Present ()
    {
    }
    /*package */ Present (ITree tree, CharRanges ranges)
    {
      this.tree = tree;
      this.ranges = ranges;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitPresentOptCharRanges (this);
    }
    private final CharRanges ranges;
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
