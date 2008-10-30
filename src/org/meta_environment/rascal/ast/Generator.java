package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.Collections;
public abstract class Generator extends AbstractAST
{
  public class Expression extends Generator
  {
/* expression:Expression -> Generator {cons("Expression")} */
    private Expression ()
    {
    }
    /*package */ Expression (ITree tree, Expression expression)
    {
      this.tree = tree;
      this.expression = expression;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitGeneratorExpression (this);
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
    public Expression setExpression (Expression x)
    {
      Expression z = new Expression ();
      z.$setExpression (x);
      return z;
    }
  }
  public class Ambiguity extends Generator
  {
    private final java.util.List < Generator > alternatives;
    public Ambiguity (java.util.List < Generator > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Generator > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class Producer extends Generator
  {
/* producer:ValueProducer -> Generator {cons("Producer")} */
    private Producer ()
    {
    }
    /*package */ Producer (ITree tree, ValueProducer producer)
    {
      this.tree = tree;
      this.producer = producer;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitGeneratorProducer (this);
    }
    private ValueProducer producer;
    public ValueProducer getProducer ()
    {
      return producer;
    }
    private void $setProducer (ValueProducer x)
    {
      this.producer = x;
    }
    public Producer setProducer (ValueProducer x)
    {
      Producer z = new Producer ();
      z.$setProducer (x);
      return z;
    }
  }
}
