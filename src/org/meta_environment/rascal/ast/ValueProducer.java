package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class ValueProducer extends AbstractAST
{
  static public class DefaultStrategy extends ValueProducer
  {
/* pattern:Expression ":" expression:Expression -> ValueProducer {cons("DefaultStrategy")} */
    private DefaultStrategy ()
    {
    }
    /*package */ DefaultStrategy (ITree tree,
				  org.meta_environment.rascal.ast.
				  Expression pattern,
				  org.meta_environment.rascal.ast.
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
    private org.meta_environment.rascal.ast.Expression pattern;
    public org.meta_environment.rascal.ast.Expression getPattern ()
    {
      return pattern;
    }
    private void $setPattern (org.meta_environment.rascal.ast.Expression x)
    {
      this.pattern = x;
    }
    public DefaultStrategy setPattern (org.meta_environment.rascal.ast.
				       Expression x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setPattern (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public DefaultStrategy setExpression (org.meta_environment.rascal.ast.
					  Expression x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setExpression (x);
      return z;
    }
  }
  static public class Ambiguity extends ValueProducer
  {
    private final java.util.List <
      org.meta_environment.rascal.ast.ValueProducer > alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.ValueProducer >
		      alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.ValueProducer >
      getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class GivenStrategy extends ValueProducer
  {
/* strategy:Strategy pattern:Expression ":" expression:Expression -> ValueProducer {cons("GivenStrategy")} */
    private GivenStrategy ()
    {
    }
    /*package */ GivenStrategy (ITree tree,
				org.meta_environment.rascal.ast.
				Strategy strategy,
				org.meta_environment.rascal.ast.
				Expression pattern,
				org.meta_environment.rascal.ast.
				Expression expression)
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
    private org.meta_environment.rascal.ast.Strategy strategy;
    public org.meta_environment.rascal.ast.Strategy getStrategy ()
    {
      return strategy;
    }
    private void $setStrategy (org.meta_environment.rascal.ast.Strategy x)
    {
      this.strategy = x;
    }
    public GivenStrategy setStrategy (org.meta_environment.rascal.ast.
				      Strategy x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setStrategy (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression pattern;
    public org.meta_environment.rascal.ast.Expression getPattern ()
    {
      return pattern;
    }
    private void $setPattern (org.meta_environment.rascal.ast.Expression x)
    {
      this.pattern = x;
    }
    public GivenStrategy setPattern (org.meta_environment.rascal.ast.
				     Expression x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setPattern (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression expression;
    public org.meta_environment.rascal.ast.Expression getExpression ()
    {
      return expression;
    }
    private void $setExpression (org.meta_environment.rascal.ast.Expression x)
    {
      this.expression = x;
    }
    public GivenStrategy setExpression (org.meta_environment.rascal.ast.
					Expression x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setExpression (x);
      return z;
    }
  }
}
