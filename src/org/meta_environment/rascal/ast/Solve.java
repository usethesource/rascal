package org.meta_environment.rascal.ast;
public abstract class Solve extends AbstractAST
{
  public class NoBound extends Solve
  {
/* "solve" body:Statement -> Solve {cons("NoBound")} */
    private NoBound ()
    {
    }
    /*package */ NoBound (ITree tree, Statement body)
    {
      this.tree = tree;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNoBoundSolve (this);
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public NoBound setbody (Statement x)
    {
      z = new NoBound ();
      z.privateSetbody (x);
      return z;
    }
  }
  public class WithBound extends Solve
  {
/* "solve" "(" bound:Expression ")" body:Statement -> Solve {cons("WithBound")} */
    private WithBound ()
    {
    }
    /*package */ WithBound (ITree tree, Expression bound, Statement body)
    {
      this.tree = tree;
      this.bound = bound;
      this.body = body;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitWithBoundSolve (this);
    }
    private final Expression bound;
    public Expression getbound ()
    {
      return bound;
    }
    private void privateSetbound (Expression x)
    {
      this.bound = x;
    }
    public WithBound setbound (Expression x)
    {
      z = new WithBound ();
      z.privateSetbound (x);
      return z;
    }
    private final Statement body;
    public Statement getbody ()
    {
      return body;
    }
    private void privateSetbody (Statement x)
    {
      this.body = x;
    }
    public WithBound setbody (Statement x)
    {
      z = new WithBound ();
      z.privateSetbody (x);
      return z;
    }
  }
}
