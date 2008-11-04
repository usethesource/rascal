package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class NoElseMayFollow extends AbstractAST
{
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends NoElseMayFollow
  {
/*  -> NoElseMayFollow {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree)
    {
      this.tree = tree;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItNoElseMayFollowDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }
  }
  static public class Ambiguity extends NoElseMayFollow
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.NoElseMayFollow > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.NoElseMayFollow >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.NoElseMayFollow >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
