package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Visit extends AbstractAST
{
  static public class DefaultStrategy extends Visit
  {
/* "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit {cons("DefaultStrategy")} */
    private DefaultStrategy ()
    {
    }
    /*package */ DefaultStrategy (ITree tree,
				  org.meta_environment.rascal.ast.
				  Expression subject,
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
    private org.meta_environment.rascal.ast.Expression subject;
    public org.meta_environment.rascal.ast.Expression getSubject ()
    {
      return subject;
    }
    private void $setSubject (org.meta_environment.rascal.ast.Expression x)
    {
      this.subject = x;
    }
    public DefaultStrategy setSubject (org.meta_environment.rascal.ast.
				       Expression x)
    {
      org.meta_environment.rascal.ast.DefaultStrategy z =
	new DefaultStrategy ();
      z.$setSubject (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Case > cases;
    public java.util.List < org.meta_environment.rascal.ast.Case > getCases ()
    {
      return cases;
    }
    private void $setCases (java.util.List <
			    org.meta_environment.rascal.ast.Case > x)
    {
      this.cases = x;
    }
    public DefaultStrategy setCases (java.util.List <
				     org.meta_environment.rascal.ast.Case > x)
    {
      org.meta_environment.rascal.ast.DefaultStrategy z =
	new DefaultStrategy ();
      z.$setCases (x);
      return z;
    }
  }
  static public class Ambiguity extends Visit
  {
    public Visit.Ambiguity makeVisitAmbiguity (java.util.List < Visit >
					       alternatives)
    {
      Visit.Ambiguity amb = new Visit.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Visit.Ambiguity) table.get (amb);
    }
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
  static public class GivenStrategy extends Visit
  {
/* strategy:Strategy "visit" "(" subject:Expression ")" "{" cases:Case+ "}" -> Visit {cons("GivenStrategy")} */
    private GivenStrategy ()
    {
    }
    /*package */ GivenStrategy (ITree tree,
				org.meta_environment.rascal.ast.
				Strategy strategy,
				org.meta_environment.rascal.ast.
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
      org.meta_environment.rascal.ast.GivenStrategy z = new GivenStrategy ();
      z.$setStrategy (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression subject;
    public org.meta_environment.rascal.ast.Expression getSubject ()
    {
      return subject;
    }
    private void $setSubject (org.meta_environment.rascal.ast.Expression x)
    {
      this.subject = x;
    }
    public GivenStrategy setSubject (org.meta_environment.rascal.ast.
				     Expression x)
    {
      org.meta_environment.rascal.ast.GivenStrategy z = new GivenStrategy ();
      z.$setSubject (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.Case > cases;
    public java.util.List < org.meta_environment.rascal.ast.Case > getCases ()
    {
      return cases;
    }
    private void $setCases (java.util.List <
			    org.meta_environment.rascal.ast.Case > x)
    {
      this.cases = x;
    }
    public GivenStrategy setCases (java.util.List <
				   org.meta_environment.rascal.ast.Case > x)
    {
      org.meta_environment.rascal.ast.GivenStrategy z = new GivenStrategy ();
      z.$setCases (x);
      return z;
    }
  }
}
