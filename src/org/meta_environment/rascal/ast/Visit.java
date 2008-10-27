package org.meta_environment.rascal.ast;
public abstract class Visit extends AbstractAST
{
  public class DefaultStrategy extends Visit
  {
    private Expression subject;
    private List < Case > cases;

    private DefaultStrategy ()
    {
    }
    /*package */ DefaultStrategy (ITree tree, Expression subject,
				  List < Case > cases)
    {
      this.tree = tree;
      this.subject = subject;
      this.cases = cases;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultStrategyVisit (this);
    }
    private final Expression subject;
    public Expression getsubject ()
    {
      return subject;
    }
    private void privateSetsubject (Expression x)
    {
      this.subject = x;
    }
    public DefaultStrategy setsubject (Expression x)
    {
      z = new DefaultStrategy ();
      z.privateSetsubject (x);
      return z;
    }
    private final List < Case > cases;
    public List < Case > getcases ()
    {
      return cases;
    }
    private void privateSetcases (List < Case > x)
    {
      this.cases = x;
    }
    public DefaultStrategy setcases (List < Case > x)
    {
      z = new DefaultStrategy ();
      z.privateSetcases (x);
      return z;
    }
  }
  public class GivenStrategy extends Visit
  {
    private Strategy strategy;
    private Expression subject;
    private List < Case > cases;

    private GivenStrategy ()
    {
    }
    /*package */ GivenStrategy (ITree tree, Strategy strategy,
				Expression subject, List < Case > cases)
    {
      this.tree = tree;
      this.strategy = strategy;
      this.subject = subject;
      this.cases = cases;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGivenStrategyVisit (this);
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
    private final Expression subject;
    public Expression getsubject ()
    {
      return subject;
    }
    private void privateSetsubject (Expression x)
    {
      this.subject = x;
    }
    public GivenStrategy setsubject (Expression x)
    {
      z = new GivenStrategy ();
      z.privateSetsubject (x);
      return z;
    }
    private final List < Case > cases;
    public List < Case > getcases ()
    {
      return cases;
    }
    private void privateSetcases (List < Case > x)
    {
      this.cases = x;
    }
    public GivenStrategy setcases (List < Case > x)
    {
      z = new GivenStrategy ();
      z.privateSetcases (x);
      return z;
    }
  }
}
