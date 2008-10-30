package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
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
    public Pattern getpattern ()
    {
      return pattern;
    }
    private void $setpattern (Pattern x)
    {
      this.pattern = x;
    }
    public DefaultStrategy setpattern (Pattern x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setpattern (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public DefaultStrategy setexpression (Expression x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setexpression (x);
      return z;
    }
  }
  public class Ambiguity extends ValueProducer
  {
    private final List < ValueProducer > alternatives;
    public Ambiguity (List < ValueProducer > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < ValueProducer > getAlternatives ()
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
    public Strategy getstrategy ()
    {
      return strategy;
    }
    private void $setstrategy (Strategy x)
    {
      this.strategy = x;
    }
    public GivenStrategy setstrategy (Strategy x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setstrategy (x);
      return z;
    }
    private Pattern pattern;
    public Pattern getpattern ()
    {
      return pattern;
    }
    private void $setpattern (Pattern x)
    {
      this.pattern = x;
    }
    public GivenStrategy setpattern (Pattern x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setpattern (x);
      return z;
    }
    private Expression expression;
    public Expression getexpression ()
    {
      return expression;
    }
    private void $setexpression (Expression x)
    {
      this.expression = x;
    }
    public GivenStrategy setexpression (Expression x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setexpression (x);
      return z;
    }
  }
}
