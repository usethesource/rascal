package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Parameters extends AbstractAST
{
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitParametersDefault (this);
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
      org.meta_environment.rascal.ast.Default z = new Default ();
      z.$setFormals (x);
      return z;
    }
  }
  static public class Ambiguity extends Parameters
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.Parameters > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.Parameters >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.Parameters >
      getAlternatives ()
    {
      return alternatives;
    }
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitParametersVarArgs (this);
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
      org.meta_environment.rascal.ast.VarArgs z = new VarArgs ();
      z.$setFormals (x);
      return z;
    }
  }
}
