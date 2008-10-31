package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Break extends AbstractAST
{
  static public class WithLabel extends Break
  {
/* "break" label:Name ";" -> Break {cons("WithLabel")} */
    private WithLabel ()
    {
    }
    /*package */ WithLabel (ITree tree,
			    org.meta_environment.rascal.ast.Name label)
    {
      this.tree = tree;
      this.label = label;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitBreakWithLabel (this);
    }
    private org.meta_environment.rascal.ast.Name label;
    public org.meta_environment.rascal.ast.Name getLabel ()
    {
      return label;
    }
    private void $setLabel (org.meta_environment.rascal.ast.Name x)
    {
      this.label = x;
    }
    public WithLabel setLabel (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.WithLabel z = new WithLabel ();
      z.$setLabel (x);
      return z;
    }
  }
  static public class Ambiguity extends Break
  {
    public Break.Ambiguity makeBreakAmbiguity (java.util.List < Break >
					       alternatives)
    {
      Break.Ambiguity amb = new Break.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Break.Ambiguity) table.get (amb);
    }
    private final java.util.List < Break > alternatives;
    public Ambiguity (java.util.List < Break > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Break > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class NoLabel extends Break
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
