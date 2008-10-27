package org.meta_environment.rascal.ast;
public abstract class Condition extends AbstractAST
{
  public class Match extends Condition
  {
/* pattern:Pattern ":=" expression:Expression -> Condition {cons("Match")} */
    private Match ()
    {
    }
    /*package */ Match (ITree tree, Pattern pattern, Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitMatchCondition (this);
    }
    private final Pattern pattern;
    public Pattern getpattern ()
    {
      return pattern;
    }
    private void privateSetpattern (Pattern x)
    {
      this.pattern = x;
    }
    public Match setpattern (Pattern x)
    {
      z = new Match ();
      z.privateSetpattern (x);
      return z;
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Match setexpression (Expression x)
    {
      z = new Match ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class NoMatch extends Condition
  {
/* pattern:Pattern "!:=" expression:Expression -> Condition {cons("NoMatch")} */
    private NoMatch ()
    {
    }
    /*package */ NoMatch (ITree tree, Pattern pattern, Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNoMatchCondition (this);
    }
    private final Pattern pattern;
    public Pattern getpattern ()
    {
      return pattern;
    }
    private void privateSetpattern (Pattern x)
    {
      this.pattern = x;
    }
    public NoMatch setpattern (Pattern x)
    {
      z = new NoMatch ();
      z.privateSetpattern (x);
      return z;
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public NoMatch setexpression (Expression x)
    {
      z = new NoMatch ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Expression extends Condition
  {
/* expression:Expression -> Condition {cons("Expression")} */
    private Expression ()
    {
    }
    /*package */ Expression (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitExpressionCondition (this);
    }
    private final Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void privateSetexpression (Expression x)
    {
      this.expression = x;
    }
    public Expression setexpression (Expression x)
    {
      z = new Expression ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class Conjunction extends Condition
  {
/* lhs:Condition "," rhs:Condition -> Condition {left, cons("Conjunction")} */
    private Conjunction ()
    {
    }
    /*package */ Conjunction (ITree tree, Condition lhs, Condition rhs)
    {
      this.tree = tree;
      this.lhs = lhs;
      this.rhs = rhs;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitConjunctionCondition (this);
    }
    private final Condition lhs;
    public Condition getlhs ()
    {
      return lhs;
    }
    private void privateSetlhs (Condition x)
    {
      this.lhs = x;
    }
    public Conjunction setlhs (Condition x)
    {
      z = new Conjunction ();
      z.privateSetlhs (x);
      return z;
    }
    private final Condition rhs;
    public Condition getrhs ()
    {
      return rhs;
    }
    private void privateSetrhs (Condition x)
    {
      this.rhs = x;
    }
    public Conjunction setrhs (Condition x)
    {
      z = new Conjunction ();
      z.privateSetrhs (x);
      return z;
    }
  }
}
