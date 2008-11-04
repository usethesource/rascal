package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Variable extends AbstractAST
{
  public org.meta_environment.rascal.ast.Name getName ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Tags getTags ()
  {
    throw new UnsupportedOperationException ();
  }
  public org.meta_environment.rascal.ast.Expression getInitial ()
  {
    throw new UnsupportedOperationException ();
  }
  public boolean hasName ()
  {
    return false;
  }
  public boolean hasTags ()
  {
    return false;
  }
  public boolean hasInitial ()
  {
    return false;
  }
  public boolean isGivenInitialization ()
  {
    return false;
  }
  static public class GivenInitialization extends Variable
  {
/* name:Name tags:Tags "=" initial:Expression -> Variable {cons("GivenInitialization")} */
    private GivenInitialization ()
    {
    }
    /*package */ GivenInitialization (ITree tree,
				      org.meta_environment.rascal.ast.
				      Name name,
				      org.meta_environment.rascal.ast.
				      Tags tags,
				      org.meta_environment.rascal.ast.
				      Expression initial)
    {
      this.tree = tree;
      this.name = name;
      this.tags = tags;
      this.initial = initial;
    }
    public IVisItable accept (IASTVisItor visItor)
    {
      return visItor.visItVariableGivenInitialization (this);
    }

    public boolean isGivenInitialization ()
    {
      return true;
    }

    public boolean hasName ()
    {
      return true;
    }
    public boolean hasTags ()
    {
      return true;
    }
    public boolean hasInitial ()
    {
      return true;
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
    public GivenInitialization setName (org.meta_environment.rascal.ast.
					Name x)
    {
      GivenInitialization z = new GivenInitialization ();
      z.$setName (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Tags tags;
    public org.meta_environment.rascal.ast.Tags getTags ()
    {
      return tags;
    }
    private void $setTags (org.meta_environment.rascal.ast.Tags x)
    {
      this.tags = x;
    }
    public GivenInitialization setTags (org.meta_environment.rascal.ast.
					Tags x)
    {
      GivenInitialization z = new GivenInitialization ();
      z.$setTags (x);
      return z;
    }
    private org.meta_environment.rascal.ast.Expression initial;
    public org.meta_environment.rascal.ast.Expression getInitial ()
    {
      return initial;
    }
    private void $setInitial (org.meta_environment.rascal.ast.Expression x)
    {
      this.initial = x;
    }
    public GivenInitialization setInitial (org.meta_environment.rascal.ast.
					   Expression x)
    {
      GivenInitialization z = new GivenInitialization ();
      z.$setInitial (x);
      return z;
    }
  }
  static public class Ambiguity extends Variable
  {
    private final java.util.LisT < org.meta_environment.rascal.ast.Variable >
      alternatives;
    public Ambiguity (java.util.LisT <
		      org.meta_environment.rascal.ast.Variable > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableLisT (alternatives);
    }
    public java.util.LisT < org.meta_environment.rascal.ast.Variable >
      getAlternatives ()
    {
      return alternatives;
    }
  }
}
