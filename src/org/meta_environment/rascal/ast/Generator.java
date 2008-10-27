package org.meta_environment.rascal.ast;
public abstract class Generator extends AbstractAST
{
  public class Expression extends Generator
  {
    private Expression expression;

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
      return visitor.visitExpressionGenerator (this);
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
  public class Producer extends Generator
  {
    private ValueProducer producer;

    private Producer ()
    {
    }
    /*package */ Producer (ITree tree, ValueProducer producer)
    {
      this.tree = tree;
      this.producer = producer;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitProducerGenerator (this);
    }
    private final ValueProducer producer;
    public ValueProducer getproducer ()
    {
      return producer;
    }
    private void privateSetproducer (ValueProducer x)
    {
      this.producer = x;
    }
    public Producer setproducer (ValueProducer x)
    {
      z = new Producer ();
      z.privateSetproducer (x);
      return z;
    }
  }
}
