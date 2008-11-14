package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class CharRanges extends AbstractAST
{
  public org.meta_environment.rascal.ast.CharRange getRange ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasRange ()
  {
    return false;
  }
  public boolean isRange ()
  {
    return false;
  }
  static public class Range extends CharRanges
  {
/* range:CharRange -> CharRanges {cons("Range")} */
    private Range ()
    {
    }
    /*package */ Range (ITree tree,
			org.meta_environment.rascal.ast.CharRange range)
    {
      this.tree = tree;
      this.range = range;
    }
    public < T > T accept (IASTVisitor < T > visitor)
    {
      return visitor.visitCharRangesRange (this);
    }

    public boolean isRange ()
    {
      return true;
    }

    public boolean hasRange ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.CharRange range;
    public org.meta_environment.rascal.ast.CharRange getRange ()
    {
      return range;
    }
    private void $setRange (org.meta_environment.rascal.ast.CharRange x)
    {
      this.range = x;
    }
    public Range setRange (org.meta_environment.rascal.ast.CharRange x)
    {
      Range z = new Range ();
      z.$setRange (x);
      return z;
    }
  }
  static public class Ambiguity extends CharRanges
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.CharRanges > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.CharRanges >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.CharRanges >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public org.meta_environment.rascal.ast.CharRanges getLhs ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.CharRanges getRhs ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasLhs ()
  {
    return false;
  }
  public boolean hasRhs ()
  {
    return false;
  }
  public boolean isConcatenate ()
  {
    return false;
  }
  static public class Concatenate extends CharRanges
  {
/* lhs:CharRanges rhs:CharRanges -> CharRanges {cons("Concatenate"), right, memo} */
    private Concatenate ()
    {
    }
    /*package */ Concatenate (ITree tree,
			      org.meta_environment.rascal.ast.CharRanges lhs,
			      org.meta_environment.rascal.ast.CharRanges rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public < T > T accept (IASTVisitor < T > visitor)
    {
      return visitor.visitCharRangesConcatenate (this);
    }

    public boolean isConcatenate ()
    {
      return true;
    }

    public boolean hasLhs ()
    {
      return true;
    }
    public boolean hasRhs ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.CharRanges lhs;
    public org.meta_environment.rascal.ast.CharRanges getLhs ()
    {
      return lhs;
    }
    private void $setLhs (org.meta_environment.rascal.ast.CharRanges x)
    {
      this.lhs = x;
    }
    public Concatenate setLhs (org.meta_environment.rascal.ast.CharRanges x)
    {
      Concatenate z = new Concatenate ();
      z.$setLhs (x);
      return z;
    }
    private org.meta_environment.rascal.ast.CharRanges rhs;
    public org.meta_environment.rascal.ast.CharRanges getRhs ()
    {
      return rhs;
    }
    private void $setRhs (org.meta_environment.rascal.ast.CharRanges x)
    {
      this.rhs = x;
    }
    public Concatenate setRhs (org.meta_environment.rascal.ast.CharRanges x)
    {
      Concatenate z = new Concatenate ();
      z.$setRhs (x);
      return z;
    }
  }
  public org.meta_environment.rascal.ast.CharRanges getRanges ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasRanges ()
  {
    return false;
  }
  public boolean isBracket ()
  {
    return false;
  }
  static public class Bracket extends CharRanges
  {
/* "(" ranges:CharRanges ")" -> CharRanges {bracket, cons("Bracket")} */
    private Bracket ()
    {
    }
    /*package */ Bracket (ITree tree,
			  org.meta_environment.rascal.ast.CharRanges ranges)
    {
      this.tree = tree;
      this.ranges = ranges;
    }
    public < T > T accept (IASTVisitor < T > visitor)
    {
      return visitor.visitCharRangesBracket (this);
    }

    public boolean isBracket ()
    {
      return true;
    }

    public boolean hasRanges ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.CharRanges ranges;
    public org.meta_environment.rascal.ast.CharRanges getRanges ()
    {
      return ranges;
    }
    private void $setRanges (org.meta_environment.rascal.ast.CharRanges x)
    {
      this.ranges = x;
    }
    public Bracket setRanges (org.meta_environment.rascal.ast.CharRanges x)
    {
      Bracket z = new Bracket ();
      z.$setRanges (x);
      return z;
    }
  }
}
