package org.meta_environment.rascal.ast;
public abstract class FunctionBody extends AbstractAST
{
  public class Default extends FunctionBody
  {
    private List < Statement > statements;

    private Default ()
    {
    }
    /*package */ Default (ITree tree, List < Statement > statements)
    {
      this.tree = tree;
      this.statements = statements;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultFunctionBody (this);
    }
    private final List < Statement > statements;
    public List < Statement > getstatements ()
    {
      return statements;
    }
    private void privateSetstatements (List < Statement > x)
    {
      this.statements = x;
    }
    public Default setstatements (List < Statement > x)
    {
      z = new Default ();
      z.privateSetstatements (x);
      return z;
    }
  }
}
