package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Visit extends AbstractAST
{
  public class DefaultStrategy extends Visit
  {
/* "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit {cons("DefaultStrategy")} */
    private DefaultStrategy ()
    {
    }
    /*package */ DefaultStrategy (ITree tree, Expression subject,
				  java.util.List < Case > cases)
    {
      this.tree = tree;
      this.subject = subject;
      this.cases = cases;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVisitDefaultStrategy (this);
    }
    private Expression subject;
    public Expression getSubject ()
    {
      return subject;
    }
    private void $setSubject (Expression x)
    {
      this.subject = x;
    }
    public DefaultStrategy setSubject (Expression x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setSubject (x);
      return z;
    }
    private java.util.List < Case > cases;
    public java.util.List < Case > getCases ()
    {
      return cases;
    }
    private void $setCases (java.util.List < Case > x)
    {
      this.cases = x;
    }
    public DefaultStrategy setCases (java.util.List < Case > x)
    {
      DefaultStrategy z = new DefaultStrategy ();
      z.$setCases (x);
      return z;
    }
  }
  public class Ambiguity extends Visit
  {
    private final java.util.List < Visit > alternatives;
    public Ambiguity (java.util.List < Visit > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Visit > getAlternatives ()
    {
      return alternatives;
    }
  }
  public class GivenStrategy extends Visit
  {
/* strategy:Strategy "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit {cons("GivenStrategy")} */
    private GivenStrategy ()
    {
    }
    /*package */ GivenStrategy (ITree tree, Strategy strategy,
				Expression subject,
				java.util.List < Case > cases)
    {
      this.tree = tree;
      this.strategy = strategy;
      this.subject = subject;
      this.cases = cases;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVisitGivenStrategy (this);
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
    private Expression subject;
    public Expression getSubject ()
    {
      return subject;
    }
    private void $setSubject (Expression x)
    {
      this.subject = x;
    }
    public GivenStrategy setSubject (Expression x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setSubject (x);
      return z;
    }
    private java.util.List < Case > cases;
    public java.util.List < Case > getCases ()
    {
      return cases;
    }
    private void $setCases (java.util.List < Case > x)
    {
      this.cases = x;
    }
    public GivenStrategy setCases (java.util.List < Case > x)
    {
      GivenStrategy z = new GivenStrategy ();
      z.$setCases (x);
      return z;
    }
  }
}
