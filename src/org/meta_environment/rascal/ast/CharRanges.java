package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class CharRanges extends AbstractAST
{
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharRangesRange (this);
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
      org.meta_environment.rascal.ast.Range z = new Range ();
      z.$setRange (x);
      return z;
    }
  }
  static public class Ambiguity extends CharRanges
  {
    public CharRanges.Ambiguity makeCharRangesAmbiguity (java.util.List <
							 CharRanges >
							 alternatives)
    {
      CharRanges.Ambiguity amb = new CharRanges.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (CharRanges.Ambiguity) table.get (amb);
    }
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitCharRangesConcatenate (this);
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
      org.meta_environment.rascal.ast.Concatenate z = new Concatenate ();
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
      org.meta_environment.rascal.ast.Concatenate z = new Concatenate ();
      z.$setRhs (x);
      return z;
    }
  }
  static public class Bracket extends CharRanges
  {
/* "(" CharRanges ")" -> CharRanges {bracket} */
    public CharRanges.Bracket makeCharRangesBracket (ITree tree)
    {
      CharRanges.Bracket x = new CharRanges.Bracket (tree);
      if (!table.containsKey (x))
	{
	  table.put (x, x);
	}
      return (CharRanges.Bracket) table.get (x);
    }

    public CharRanges visitCharRangesBracket (CharRanges.Bracket x);
    public CharRanges visitCharRangesBracket (CharRanges.Bracket x)
    {
      return x;
    }

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
