package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
import java.util.List;
import java.util.Collections;
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
      return visitor.visitUserTypeName (this);
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public Name setname (Name x)
    {
      Name z = new Name ();
      z.$setname (x);
      return z;
    }
  }
  public class Ambiguity extends UserType
  {
    private final List < UserType > alternatives;
    public Ambiguity (List < UserType > alternatives)
    {
      this.alternatives = Collections.unmodifiableList (alternatives);
    }
    public List < UserType > getAlternatives ()
    {
      return alternatives;
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
      return visitor.visitUserTypeParametric (this);
    }
    private Name name;
    public Name getname ()
    {
      return name;
    }
    private void $setname (Name x)
    {
      this.name = x;
    }
    public Parametric setname (Name x)
    {
      Parametric z = new Parametric ();
      z.$setname (x);
      return z;
    }
    private List < TypeVar > parameters;
    public List < TypeVar > getparameters ()
    {
      return parameters;
    }
    private void $setparameters (List < TypeVar > x)
    {
      this.parameters = x;
    }
    public Parametric setparameters (List < TypeVar > x)
    {
      Parametric z = new Parametric ();
      z.$setparameters (x);
      return z;
    }
  }
}
