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
      Name z = new Name ();
      z.$setName (x);
      return z;
    }
  }
  static public class Ambiguity extends UserType
  {
    private final java.util.List < org.meta_environment.rascal.ast.UserType >
      alternatives;
    public Ambiguity (java.util.List <
		      org.meta_environment.rascal.ast.UserType > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < org.meta_environment.rascal.ast.UserType >
      getAlternatives ()
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
			     java.util.List <
			     org.meta_environment.rascal.ast.TypeVar >
			     parameters)
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
      Parametric z = new Parametric ();
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
      Parametric z = new Parametric ();
      z.$setParameters (x);
      return z;
    }
  }
}
