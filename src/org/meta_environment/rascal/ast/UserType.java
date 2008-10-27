package org.meta_environment.rascal.ast;
public abstract class UserType extends AbstractAST
{
  public class Name extends UserType
  {
    private Name name;

    private Name ()
    {
    }
    /*package */ Name (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNameUserType (this);
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
    public Name setname (Name x)
    {
      z = new Name ();
      z.privateSetname (x);
      return z;
    }
  }
  public class Parametric extends UserType
  {
    private Name name;
    private List < TypeVar > parameters;

    private Parametric ()
    {
    }
    /*package */ Parametric (ITree tree, Name name,
			     List < TypeVar > parameters)
    {
      this.tree = tree;
      this.name = name;
      this.parameters = parameters;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitParametricUserType (this);
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
    public Parametric setname (Name x)
    {
      z = new Parametric ();
      z.privateSetname (x);
      return z;
    }
    private final List < TypeVar > parameters;
    public List < TypeVar > getparameters ()
    {
      return parameters;
    }
    private void privateSetparameters (List < TypeVar > x)
    {
      this.parameters = x;
    }
    public Parametric setparameters (List < TypeVar > x)
    {
      z = new Parametric ();
      z.privateSetparameters (x);
      return z;
    }
  }
}
