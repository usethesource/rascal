package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Formals extends AbstractAST
{
  public java.util.LisT < org.meta_environment.rascal.ast.Formal >
    getFormals ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasFormals ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends Formals
  {
/* formals:{Formal ","}* -> Formals {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Formal > formals)
    {
      this.tree = tree;
      this.formals = formals;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItFormalsDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasFormals ()
    {
      return true;
    }

    private java.util.LisT < org.meta_environment.rascal.ast.Formal > formals;
    public java.util.LisT < org.meta_environment.rascal.ast.Formal >
      getFormals ()
    {
      return formals;
    }
    private void $setFormals (java.util.LisT <
			      org.meta_environment.rascal.ast.Formal > x)
    {
      this.formals = x;
    }
    public Default setFormals (java.util.LisT <
			       org.meta_environment.rascal.ast.Formal > x)
    {
      Default z = new Default ();
      z.$setFormals (x);
      return z;
    }
  }
  static public class Ambiguity extends Formals
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Formals >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.Formals > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Formals >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
