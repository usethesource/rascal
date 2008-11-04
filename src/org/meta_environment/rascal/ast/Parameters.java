package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Parameters extends AbstractAST
{
  public org.meta_environment.rascal.ast.Formals getFormals ()
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
  static public class Default extends Parameters
  {
/* "(" formals:Formals ")" -> Parameters {cons("Default")} */
    private Default ()
    {
    }
    /*package */ Default (ITree tree,
			  org.meta_environment.rascal.ast.Formals formals)
    {
      this.tree = tree;
      this.formals = formals;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItParametersDefault (this);
    }

    public boolean isDefault ()
    {
      return true;
    }

    public boolean hasFormals ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Formals formals;
    public org.meta_environment.rascal.ast.Formals getFormals ()
    {
      return formals;
    }
    private void $setFormals (org.meta_environment.rascal.ast.Formals x)
    {
      this.formals = x;
    }
    public Default setFormals (org.meta_environment.rascal.ast.Formals x)
    {
      Default z = new Default ();
      z.$setFormals (x);
      return z;
    }
  }
  static public class Ambiguity extends Parameters
  {
    private final java.util.LisT <
      org.meta_environment.rascal.ast.Parameters > alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.Parameters >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Parameters >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  public boolean isVarArgs ()
  {
    return false;
  }
  static public class VarArgs extends Parameters
  {
/* "(" formals:Formals "..." ")" -> Parameters {cons("VarArgs")} */
    private VarArgs ()
    {
    }
    /*package */ VarArgs (ITree tree,
			  org.meta_environment.rascal.ast.Formals formals)
    {
      this.tree = tree;
      this.formals = formals;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItParametersVarArgs (this);
    }

    public boolean isVarArgs ()
    {
      return true;
    }

    public boolean hasFormals ()
    {
      return true;
    }

    private org.meta_environment.rascal.ast.Formals formals;
    public org.meta_environment.rascal.ast.Formals getFormals ()
    {
      return formals;
    }
    private void $setFormals (org.meta_environment.rascal.ast.Formals x)
    {
      this.formals = x;
    }
    public VarArgs setFormals (org.meta_environment.rascal.ast.Formals x)
    {
      VarArgs z = new VarArgs ();
      z.$setFormals (x);
      return z;
    }
  }
}
