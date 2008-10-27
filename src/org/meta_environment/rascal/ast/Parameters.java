package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Parameters extends AbstractAST
{
  public class Default extends Parameters
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
      return visitor.visitDefaultParameters (this);
    }
    private Formals formals;
    public Formals getformals ()
    {
      return formals;
    }
    private void privateSetformals (Formals x)
    {
      this.formals = x;
    }
    public Default setformals (Formals x)
    {
      Default z = new Default ();
      z.privateSetformals (x);
      return z;
    }
  }
  public class VarArgs extends Parameters
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
      return visitor.visitVarArgsParameters (this);
    }
    private Formals formals;
    public Formals getformals ()
    {
      return formals;
    }
    private void privateSetformals (Formals x)
    {
      this.formals = x;
    }
    public VarArgs setformals (Formals x)
    {
      VarArgs z = new VarArgs ();
      z.privateSetformals (x);
      return z;
    }
  }
}
