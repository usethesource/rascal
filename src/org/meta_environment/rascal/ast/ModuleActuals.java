package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ModuleActuals extends AbstractAST
{
  public java.util.LisT < org.meta_environment.rascal.ast.Type > getTypes ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasTypes ()
  {
    return false;
  }
  public boolean isDefault ()
  {
    return false;
  }
  static public class Default extends ModuleActuals
  {
/* "[" types:{Type ","}+ "]" -> ModuleActuals {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  java.util.LisT <
			  org.meta_environment.rascal.ast.Type > types)
    {
      this.tree = tree;
      this.types = types;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItModuleActualsDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasTypes ()
    {
      return true;
    }

    private java.util.LisT < org.meta_environment.rascal.ast.Type > types;
    public java.util.LisT < org.meta_environment.rascal.ast.Type > getTypes ()
    {
      return types;
    }
    private void $setTypes (java.util.LisT <
			    org.meta_environment.rascal.ast.Type > x)
    {
      this.types = x;
    }
    public Default setTypes (java.util.LisT <
			     org.meta_environment.rascal.ast.Type > x)
    {
      Default z = new Default ();
      z.$setTypes (x);
      return z;
    }
  }
  static public class Ambiguity extends ModuleActuals
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.ModuleActuals > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.ModuleActuals >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.ModuleActuals >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
