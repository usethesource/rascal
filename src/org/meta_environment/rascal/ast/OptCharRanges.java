package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class OptCharRanges extends AbstractAST
{
  static public class Absent extends OptCharRanges
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
  static public class Ambiguity extends OptCharRanges
  {
    private final java.util.List < OptCharRanges > alternatives;
    public Ambiguity (java.util.List < OptCharRanges > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < OptCharRanges > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Present extends OptCharRanges
  {
/* ranges:CharRanges -> OptCharRanges {cons("Present")} */
    private Present ()
    {
    }
    /*package */ Present (ITree tree,
			  org.meta_environment.rascal.ast.CharRanges ranges)
    {
      this.tree = tree;
      this.ranges = ranges;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitOptCharRangesPresent (this);
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
    public org.meta_environment.rascal.ast.Present setRanges (org.
							      meta_environment.
							      rascal.ast.
							      CharRanges x)
    {
      org.meta_environment.rascal.ast.Present z = new Present ();
      z.$setRanges (x);
      return z;
    }
  }
}
