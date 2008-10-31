package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Renaming extends AbstractAST
{
  static public class Default extends Renaming
  {
/* from:Name "=>" to:Name -> Renaming {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Name from,
			  org.meta_environment.rascal.ast.Name to)
    {
      this.tree = tree;
      this.from = from;
      this.to = to;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitRenamingDefault (this);
    }
    private org.meta_environment.rascal.ast.Name from;
    public org.meta_environment.rascal.ast.Name getFrom ()
    {
      return from;
    }
    private void $setFrom (org.meta_environment.rascal.ast.Name x)
    {
      this.from = x;
    }
    public Default setFrom (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setFrom (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Name to;
    public org.meta_environment.rascal.ast.Name getTo ()
    {
      return to;
    }
    private void $setTo (org.meta_environment.rascal.ast.Name x)
    {
      this.to = x;
    }
    public Default setTo (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setTo (x);
      return z;
    }
  }
  static public class Ambiguity extends Renaming
  {
    public Renaming.Ambiguity makeRenamingAmbiguity (java.util.List <
						     Renaming > alternatives)
    {
      Renaming.Ambiguity amb = new Renaming.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Renaming.Ambiguity) table.get (amb);
    }
    private final java.util.List < Renaming > alternatives;
    public Ambiguity (java.util.List < Renaming > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Renaming > getAlternatives ()
    {
      return alternatives;
    }
  }
}
