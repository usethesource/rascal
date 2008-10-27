package org.meta_environment.rascal.ast;
public abstract class ValueProducer extends AbstractAST
{
  public class DefaultStrategy extends ValueProducer
  {
/* pattern:Pattern ":" expression:Expression -> ValueProducer {cons("DefaultStrategy")} */
    private DefaultStrategy ()
    {
    }
    /*package */ DefaultStrategy (ITree tree, Pattern pattern,
				  Expression expression)
    {
      this.tree = tree;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultStrategyValueProducer (this);
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
    public DefaultStrategy setpattern (Pattern x)
    {
      z = new DefaultStrategy ();
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
    public DefaultStrategy setexpression (Expression x)
    {
      z = new DefaultStrategy ();
      z.privateSetexpression (x);
      return z;
    }
  }
  public class GivenStrategy extends ValueProducer
  {
/* strategy:Strategy pattern:Pattern ":" expression:Expression -> ValueProducer {cons("GivenStrategy")} */
    private GivenStrategy ()
    {
    }
    /*package */ GivenStrategy (ITree tree, Strategy strategy,
				Pattern pattern, Expression expression)
    {
      this.tree = tree;
      this.strategy = strategy;
      this.pattern = pattern;
      this.expression = expression;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGivenStrategyValueProducer (this);
    }
    private final Strategy strategy;
    public Strategy getstrategy ()
    {
      return strategy;
    }
    private void privateSetstrategy (Strategy x)
    {
      this.strategy = x;
    }
    public GivenStrategy setstrategy (Strategy x)
    {
      z = new GivenStrategy ();
      z.privateSetstrategy (x);
      return z;
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
    public GivenStrategy setpattern (Pattern x)
    {
      z = new GivenStrategy ();
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
    public GivenStrategy setexpression (Expression x)
    {
      z = new GivenStrategy ();
      z.privateSetexpression (x);
      return z;
    }
  }
}
