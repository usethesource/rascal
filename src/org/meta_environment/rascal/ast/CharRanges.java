package org.meta_environment.rascal.ast;
public abstract class CharRanges extends AbstractAST
{
  public class Range extends CharRanges
  {
    private CharRange range;

    private Range ()
    {
    }
    /*package */ Range (ITree tree, CharRange range)
    {
      this.tree = tree;
      this.range = range;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitRangeCharRanges (this);
    }
    private final CharRange range;
    public CharRange getrange ()
    {
      return range;
    }
    private void privateSetrange (CharRange x)
    {
      this.range = x;
    }
    public Range setrange (CharRange x)
    {
      z = new Range ();
      z.privateSetrange (x);
      return z;
    }
  }
  public class Concatenate extends CharRanges
  {
    private CharRanges lhs;
    private CharRanges rhs;

    private Concatenate ()
    {
    }
    /*package */ Concatenate (ITree tree, CharRanges lhs, CharRanges rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitConcatenateCharRanges (this);
    }
    private final CharRanges lhs;
    public CharRanges getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (CharRanges x)
    {
      this.lhs = x;
    }
    public Concatenate setlhs (CharRanges x)
    {
      z = new Concatenate ();
      z.privateSetlhs (x);
      return z;
    }
    private final CharRanges rhs;
    public CharRanges getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (CharRanges x)
    {
      this.rhs = x;
    }
    public Concatenate setrhs (CharRanges x)
    {
      z = new Concatenate ();
      z.privateSetrhs (x);
      return z;
    }
  }
  prod2class ("(" CharRanges ")"->CharRanges
	      {
	      bracket}
)}
