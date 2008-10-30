package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
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
      return visitor.visitRuleWithGuard (this);
    }
    private Type type;
    public Type getType ()
    {
      return type;
    }
    private void $setType (Type x)
    {
      this.type = x;
    }
    public WithGuard setType (Type x)
    {
      WithGuard z = new WithGuard ();
      z.$setType (x);
      return z;
    }
    private Match match;
    public Match getMatch ()
    {
      return match;
    }
    private void $setMatch (Match x)
    {
      this.match = x;
    }
    public WithGuard setMatch (Match x)
    {
      WithGuard z = new WithGuard ();
      z.$setMatch (x);
      return z;
    }
  }
  public class Ambiguity extends Rule
  {
    private final java.util.List < Rule > alternatives;
    public Ambiguity (java.util.List < Rule > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Rule > getAlternatives ()
    {
      return alternatives;
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
      return visitor.visitRuleNoGuard (this);
    }
    private Match match;
    public Match getMatch ()
    {
      return match;
    }
    private void $setMatch (Match x)
    {
      this.match = x;
    }
    public NoGuard setMatch (Match x)
    {
      NoGuard z = new NoGuard ();
      z.$setMatch (x);
      return z;
    }
  }
}
