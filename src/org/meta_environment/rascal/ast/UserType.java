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
    /*package */ Name (ITree tree, org.meta_environment.rascal.ast.Name name)
    {
      this.tree = tree;
      this.name = name;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitUserTypeName (this);
    }
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public Name setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Name z = new Name ();
      z.$setName (x);
      return z;
    }
  }
  static public class Ambiguity extends UserType
  {
    public UserType.Ambiguity makeUserTypeAmbiguity (java.util.List <
						     UserType > alternatives)
    {
      UserType.Ambiguity amb = new UserType.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (UserType.Ambiguity) table.get (amb);
    }
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
    /*package */ Parametric (ITree tree,
			     org.meta_environment.rascal.ast.Name name,
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
    private org.meta_environment.rascal.ast.Name name;
    public org.meta_environment.rascal.ast.Name getName ()
    {
      return name;
    }
    private void $setName (org.meta_environment.rascal.ast.Name x)
    {
      this.name = x;
    }
    public Parametric setName (org.meta_environment.rascal.ast.Name x)
    {
      org.meta_environment.rascal.ast.Parametric z = new Parametric ();
      z.$setName (x);
      return z;
    }
    private java.util.List < org.meta_environment.rascal.ast.TypeVar >
      parameters;
    public java.util.List < org.meta_environment.rascal.ast.TypeVar >
      getParameters ()
    {
      return parameters;
    }
    private void $setParameters (java.util.List <
				 org.meta_environment.rascal.ast.TypeVar > x)
    {
      this.parameters = x;
    }
    public Parametric setParameters (java.util.List <
				     org.meta_environment.rascal.ast.TypeVar >
				     x)
    {
      org.meta_environment.rascal.ast.Parametric z = new Parametric ();
      z.$setParameters (x);
      return z;
    }
  }
}
