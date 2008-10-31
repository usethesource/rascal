package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Variable extends AbstractAST
{
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
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitVariableGivenInitialization (this);
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
      org.meta_environment.rascal.ast.GivenInitialization z =
	new GivenInitialization ();
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
      org.meta_environment.rascal.ast.GivenInitialization z =
	new GivenInitialization ();
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
      org.meta_environment.rascal.ast.GivenInitialization z =
	new GivenInitialization ();
      z.$setInitial (x);
      return z;
    }
  }
  static public class Ambiguity extends Variable
  {
    public Variable.Ambiguity makeVariableAmbiguity (java.util.List <
						     Variable > alternatives)
    {
      Variable.Ambiguity amb = new Variable.Ambiguity (alternatives);
      if (!table.containsKey (amb))
	{
	  table.put (amb, amb);
	}
      return (Variable.Ambiguity) table.get (amb);
    }
    private final java.util.List < Variable > alternatives;
    public Ambiguity (java.util.List < Variable > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < Variable > getAlternatives ()
    {
      return alternatives;
    }
  }
}
