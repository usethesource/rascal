package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class UserType extends AbstractAST
{
  public class Name extends UserType
  {
/* name:Name -> UserType {prefer, cons("Name")} */
    private Name ()
    {
    }
    /*package */ Name (ITree tree, Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNameUserType (this);
    }
    private Name name;
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
/* name:Name "[" parameters:{TypeVar ","}+ "]" -> UserType {cons("Parametric")} */
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitParametricUserType (this);
    }
    private Name name;
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
    private List < TypeVar > parameters;
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
      Parametric z = new Parametric ();
      z.privateSetparameters (x);
      return z;
    }
  }
}
