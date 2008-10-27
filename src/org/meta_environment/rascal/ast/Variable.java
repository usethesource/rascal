package org.meta_environment.rascal.ast;
public abstract class Variable extends AbstractAST
{
  public class DefaultInitialization extends Variable
  {
    private Name name;
    private Annotations annotations;

    private DefaultInitialization ()
    {
    }
    /*package */ DefaultInitialization (ITree tree, Name name,
					Annotations annotations)
    {
      this.tree = tree;
      this.name = name;
      this.annotations = annotations;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultInitializationVariable (this);
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public DefaultInitialization setname (Name x)
    {
      z = new DefaultInitialization ();
      z.privateSetname (x);
      return z;
    }
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public DefaultInitialization setannotations (Annotations x)
    {
      z = new DefaultInitialization ();
      z.privateSetannotations (x);
      return z;
    }
  }
  public class GivenInitialization extends Variable
  {
    private Name name;
    private Annotations annotations;
    private Expression initial;

    private GivenInitialization ()
    {
    }
    /*package */ GivenInitialization (ITree tree, Name name,
				      Annotations annotations,
				      Expression initial)
    {
      this.tree = tree;
      this.name = name;
      this.annotations = annotations;
      this.initial = initial;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGivenInitializationVariable (this);
    }
    private final Name name;
    public Name getname ()
    {
      return name;
    }
    private void privateSetname (Name x)
    {
      this.name = x;
    }
    public GivenInitialization setname (Name x)
    {
      z = new GivenInitialization ();
      z.privateSetname (x);
      return z;
    }
    private final Annotations annotations;
    public Annotations getannotations ()
    {
      return annotations;
    }
    private void privateSetannotations (Annotations x)
    {
      this.annotations = x;
    }
    public GivenInitialization setannotations (Annotations x)
    {
      z = new GivenInitialization ();
      z.privateSetannotations (x);
      return z;
    }
    private final Expression initial;
    public Expression getinitial ()
    {
      return initial;
    }
    private void privateSetinitial (Expression x)
    {
      this.initial = x;
    }
    public GivenInitialization setinitial (Expression x)
    {
      z = new GivenInitialization ();
      z.privateSetinitial (x);
      return z;
    }
  }
}
