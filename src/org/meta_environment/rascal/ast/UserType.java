package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class UserType extends AbstractAST
{
  static public class Name extends UserType
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
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Name setName (Name x)
    {
      Name z = new Name ();
      z.$setName (x);
      return z;
    }
  }
  public class Ambiguity extends UserType
  {
    private final java.util.List < UserType > alternatives;
    public Ambiguity (java.util.List < UserType > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < UserType > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Parametric extends UserType
  {
/* name:Name "[" parameters:{TypeVar ","}+ "]" -> UserType {cons("Parametric")} */
    private Parametric ()
    {
    }
    /*package */ Parametric (ITree tree, Name name,
			     java.util.List < TypeVar > parameters)
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
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public Parametric setName (Name x)
    {
      Parametric z = new Parametric ();
      z.$setName (x);
      return z;
    }
    private java.util.List < TypeVar > parameters;
    public java.util.List < TypeVar > getParameters ()
    {
      return parameters;
    }
    private void $setParameters (java.util.List < TypeVar > x)
    {
      this.parameters = x;
    }
    public Parametric setParameters (java.util.List < TypeVar > x)
    {
      Parametric z = new Parametric ();
      z.$setParameters (x);
      return z;
    }
  }
}
