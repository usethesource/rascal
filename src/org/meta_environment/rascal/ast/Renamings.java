package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Renamings extends AbstractAST
{
  public java.util.LisT < org.meta_environment.rascal.ast.Renaming >
    getRenamings ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasRenamings ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends Renamings
  {
/* "renaming" renamings:{Renaming ","}+ -> Renamings {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Renaming >
			  renamings)
    {
      this.tree = tree;
      this.renamings = renamings;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItRenamingsDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasRenamings ()
    {
      return true;
    }

    private java.util.LisT < org.meta_environment.rascal.ast.Renaming >
      renamings;
    public java.util.LisT < org.meta_environment.rascal.ast.Renaming >
      getRenamings ()
    {
      return renamings;
    }
    private void $setRenamings (java.util.LisT <
				org.meta_environment.rascal.ast.Renaming > x)
    {
      this.renamings = x;
    }
    public Default setRenamings (java.util.LisT <
				 org.meta_environment.rascal.ast.Renaming > x)
    {
      Default z = new Default ();
      z.$setRenamings (x);
      return z;
    }
  }
  static public class Ambiguity extends Renamings
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Renamings >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.Renamings >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Renamings >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
