package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
      return visitor.visitParametersDefault (this);
    }
    private Formals formals;
    public Formals getformals ()
    {
      return formals;
    }
    private void $setformals (Formals x)
    {
      this.formals = x;
    }
    public Default setformals (Formals x)
    {
      Default z = new Default ();
      z.$setformals (x);
      return z;
    }
  }
  public class Ambiguity extends Parameters
  {
    private final List < Parameters > alternatives;
    public Ambiguity (List < Parameters > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < Parameters > getAlternatives ()
    {
      return alternatives;
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
      return visitor.visitParametersVarArgs (this);
    }
    private Formals formals;
    public Formals getformals ()
    {
      return formals;
    }
    private void $setformals (Formals x)
    {
      this.formals = x;
    }
    public VarArgs setformals (Formals x)
    {
      VarArgs z = new VarArgs ();
      z.$setformals (x);
      return z;
    }
  }
}
