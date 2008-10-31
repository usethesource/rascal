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
    /*package */ Default (ITree tree, Formals formals)
    {
      this.tree = tree;
      this.formals = formals;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitParametersDefault (this);
    }
    private Formals formals;
    public Formals getFormals ()
    {
      return formals;
    }
    private void $setFormals (Formals x)
    {
      this.formals = x;
    }
    public Default setFormals (Formals x)
    {
      Default z = new Default ();
      z.$setFormals (x);
      return z;
    }
  }
  public class Ambiguity extends Parameters
  {
    private final java.util.List < Parameters > alternatives;
    public Ambiguity (java.util.List < Parameters > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Parameters > getAlternatives ()
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
    /*package */ VarArgs (ITree tree, Formals formals)
    {
      this.tree = tree;
      this.formals = formals;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitParametersVarArgs (this);
    }
    private Formals formals;
    public Formals getFormals ()
    {
      return formals;
    }
    private void $setFormals (Formals x)
    {
      this.formals = x;
    }
    public VarArgs setFormals (Formals x)
    {
      VarArgs z = new VarArgs ();
      z.$setFormals (x);
      return z;
    }
  }
}
