package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNoBoundSolve (this);
    }
    private Statement body;
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
      NoBound z = new NoBound ();
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitWithBoundSolve (this);
    }
    private Expression bound;
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
      WithBound z = new WithBound ();
      z.privateSetbound (x);
      return z;
    }
    private Statement body;
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
      WithBound z = new WithBound ();
      z.privateSetbody (x);
      return z;
    }
  }
}
