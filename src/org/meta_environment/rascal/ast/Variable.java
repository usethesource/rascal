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
    /*package */ GivenInitialization (ITree tree, Name name, Tags tags,
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
    private Name name;
    public Name getName ()
    {
      return name;
    }
    private void $setName (Name x)
    {
      this.name = x;
    }
    public GivenInitialization setName (Name x)
    {
      GivenInitialization z = new GivenInitialization ();
      z.$setName (x);
      return z;
    }
    private Tags tags;
    public Tags getTags ()
    {
      return tags;
    }
    private void $setTags (Tags x)
    {
      this.tags = x;
    }
    public GivenInitialization setTags (Tags x)
    {
      GivenInitialization z = new GivenInitialization ();
      z.$setTags (x);
      return z;
    }
    private Expression initial;
    public Expression getInitial ()
    {
      return initial;
    }
    private void $setInitial (Expression x)
    {
      this.initial = x;
    }
    public GivenInitialization setInitial (Expression x)
    {
      GivenInitialization z = new GivenInitialization ();
      z.$setInitial (x);
      return z;
    }
  }
  public class Ambiguity extends Variable
  {
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
