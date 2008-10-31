package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Renamings extends AbstractAST
{
  static public class Default extends Renamings
  {
/* "renaming" renamings:{Renaming ","}+ -> Renamings {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree, java.util.List < Renaming > renamings)
    {
      this.tree = tree;
      this.renamings = renamings;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitRenamingsDefault (this);
    }
    private java.util.List < org.meta_environment.rascal.ast.Renaming >
      renamings;
    public java.util.List < org.meta_environment.rascal.ast.Renaming >
      getRenamings ()
    {
      return renamings;
    }
    private void $setRenamings (java.util.List <
				org.meta_environment.rascal.ast.Renaming > x)
    {
      this.renamings = x;
    }
    public Default setRenamings (java.util.List <
				 org.meta_environment.rascal.ast.Renaming > x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setRenamings (x);
      return z;
    }
  }
  static public class Ambiguity extends Renamings
  {
    public Renamings.Ambiguity makeRenamingsAmbiguity (java.util.List <
						       Renamings >
						       alternatives)
    {
      Renamings.Ambiguity amb = new Renamings.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Renamings.Ambiguity) table.get (amb);
    }
    private final java.util.List < Renamings > alternatives;
    public Ambiguity (java.util.List < Renamings > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Renamings > getAlternatives ()
    {
      return alternatives;
    }
  }
}
