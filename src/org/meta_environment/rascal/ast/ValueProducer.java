package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitValueProducerDefaultStrategy (this);
    }
    private Pattern pattern;
    public Pattern getPattern ()
    {
      return pattern;
    }
    private void $setPattern (Pattern x)
    {
      this.pattern = x;
    }
    public DefaultStrategy setPattern (Pattern x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setPattern (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public DefaultStrategy setExpression (Expression x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setExpression (x);
      return z;
    }
  }
  public class Ambiguity extends ValueProducer
  {
    private final java.util.List < ValueProducer > alternatives;
    public Ambiguity (java.util.List < ValueProducer > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < ValueProducer > getAlternatives ()
    {
      return alternatives;
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitValueProducerGivenStrategy (this);
    }
    private Strategy strategy;
    public Strategy getStrategy ()
    {
      return strategy;
    }
    private void $setStrategy (Strategy x)
    {
      this.strategy = x;
    }
    public GivenStrategy setStrategy (Strategy x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setStrategy (x);
      return z;
    }
    private Pattern pattern;
    public Pattern getPattern ()
    {
      return pattern;
    }
    private void $setPattern (Pattern x)
    {
      this.pattern = x;
    }
    public GivenStrategy setPattern (Pattern x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setPattern (x);
      return z;
    }
    private Expression expression;
    public Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (Expression x)
    {
      this.expression = x;
    }
    public GivenStrategy setExpression (Expression x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setExpression (x);
      return z;
    }
  }
}
