package org.meta_environment.rascal.ast;
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultParameters (this);
    }
    private final Formals formals;
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
      z = new Default ();
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
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitVarArgsParameters (this);
    }
    private final Formals formals;
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
      z = new VarArgs ();
      z.privateSetformals (x);
      return z;
    }
  }
}
