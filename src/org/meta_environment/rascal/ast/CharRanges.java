package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class CharRanges extends AbstractAST
{
  public class Range extends CharRanges
  {
/* range:CharRange -> CharRanges {cons("Range")} */
    private Range ()
    {
    }
    /*package */ Range (ITree tree, CharRange range)
    {
      this.tree = tree;
      this.range = range;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharRangesRange (this);
    }
    private CharRange range;
    public CharRange getRange ()
    {
      return range;
    }
    private void $setRange (CharRange x)
    {
      this.range = x;
    }
    public Range setRange (CharRange x)
    {
      Range z = new Range ();
      z.$setRange (x);
      return z;
    }
  }
  public class Ambiguity extends CharRanges
  {
    private final java.util.List < CharRanges > alternatives;
    public Ambiguity (java.util.List < CharRanges > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < CharRanges > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Concatenate extends CharRanges
  {
/* lhs:CharRanges rhs:CharRanges -> CharRanges {cons("Concatenate"), right, memo} */
    private Concatenate ()
    {
    }
    /*package */ Concatenate (ITree tree, CharRanges lhs, CharRanges rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharRangesConcatenate (this);
    }
    private CharRanges lhs;
    public CharRanges getLhs ()
    {
      return lhs;
    }
    private void $setLhs (CharRanges x)
    {
      this.lhs = x;
    }
    public Concatenate setLhs (CharRanges x)
    {
      Concatenate z = new Concatenate ();
      z.$setLhs (x);
      return z;
    }
    private CharRanges rhs;
    public CharRanges getRhs ()
    {
      return rhs;
    }
    private void $setRhs (CharRanges x)
    {
      this.rhs = x;
    }
    public Concatenate setRhs (CharRanges x)
    {
      Concatenate z = new Concatenate ();
      z.$setRhs (x);
      return z;
    }
  }
  public class Bracket extends CharRanges
  {
/* "(" CharRanges ")" -> CharRanges {bracket} */
    private Bracket ()
    {
    }
    /*package */ Bracket (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharRangesBracket (this);
    }
  }
}
