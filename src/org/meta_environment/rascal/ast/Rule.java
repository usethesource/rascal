package org.meta_environment.rascal.ast;
public abstract class Rule extends AbstractAST
{
  public class WithGuard extends Rule
  {
    private Type type;
    private Match match;

    private WithGuard ()
    {
    }
    /*package */ WithGuard (ITree tree, Type type, Match match)
    {
      this.tree = tree;
      this.type = type;
      this.match = match;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitWithGuardRule (this);
    }
    private final Type type;
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
      z = new WithGuard ();
      z.privateSettype (x);
      return z;
    }
    private final Match match;
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
      z = new WithGuard ();
      z.privateSetmatch (x);
      return z;
    }
  }
  public class NoGuard extends Rule
  {
    private Match match;

    private NoGuard ()
    {
    }
    /*package */ NoGuard (ITree tree, Match match)
    {
      this.tree = tree;
      this.match = match;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNoGuardRule (this);
    }
    private final Match match;
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
      z = new NoGuard ();
      z.privateSetmatch (x);
      return z;
    }
  }
}
