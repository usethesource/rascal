package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Rule extends AbstractAST
{
  public class WithGuard extends Rule
  {
/* "[" type:Type "]" match:Match -> Rule {cons("WithGuard")} */
    private WithGuard ()
    {
    }
    /*package */ WithGuard (ITree tree, Type type, Match match)
    {
      this.tree = tree;
      this.type = type;
      this.match = match;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitWithGuardRule (this);
    }
    private Type type;
    public Type gettype ()
    {
      return type;
    }
    private void privateSettype (Type x)
    {
      this.type = x;
    }
    public WithGuard settype (Type x)
    {
      WithGuard z = new WithGuard ();
      z.privateSettype (x);
      return z;
    }
    private Match match;
    public Match getmatch ()
    {
      return match;
    }
    private void privateSetmatch (Match x)
    {
      this.match = x;
    }
    public WithGuard setmatch (Match x)
    {
      WithGuard z = new WithGuard ();
      z.privateSetmatch (x);
      return z;
    }
  }
  public class NoGuard extends Rule
  {
/* match:Match -> Rule {cons("NoGuard")} */
    private NoGuard ()
    {
    }
    /*package */ NoGuard (ITree tree, Match match)
    {
      this.tree = tree;
      this.match = match;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNoGuardRule (this);
    }
    private Match match;
    public Match getmatch ()
    {
      return match;
    }
    private void privateSetmatch (Match x)
    {
      this.match = x;
    }
    public NoGuard setmatch (Match x)
    {
      NoGuard z = new NoGuard ();
      z.privateSetmatch (x);
      return z;
    }
  }
}
