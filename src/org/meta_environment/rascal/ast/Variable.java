package org.meta_environment.rascal.ast;
public abstract class Variable extends AbstractAST
{
  public class DefaultInitialization extends Variable
  {
    private Name name;
    private Tags tags;

    private DefaultInitialization ()
    {
    }
    /*package */ DefaultInitialization (ITree tree, Name name, Tags tags)
    {
      this.tree = tree;
      this.name = name;
      this.tags = tags;
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
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public DefaultInitialization settags (Tags x)
    {
      z = new DefaultInitialization ();
      z.privateSettags (x);
      return z;
    }
  }
  public class GivenInitialization extends Variable
  {
    private Name name;
    private Tags tags;
    private Expression initial;

    private GivenInitialization ()
    {
    }
    /*package */ GivenInitialization (ITree tree, Name name, Tags tags,
				      Expression initial)
    {
      this.tree = tree;
      this.name = name;
      this.tags = tags;
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
    private final Tags tags;
    public Tags gettags ()
    {
      return tags;
    }
    private void privateSettags (Tags x)
    {
      this.tags = x;
    }
    public GivenInitialization settags (Tags x)
    {
      z = new GivenInitialization ();
      z.privateSettags (x);
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
