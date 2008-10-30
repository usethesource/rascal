package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Variable extends AbstractAST
{
  public class GivenInitialization extends Variable
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
      GivenInitialization z = new GivenInitialization ();
      z.privateSetname (x);
      return z;
    }
    private Tags tags;
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
      GivenInitialization z = new GivenInitialization ();
      z.privateSettags (x);
      return z;
    }
    private Expression initial;
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
      GivenInitialization z = new GivenInitialization ();
      z.privateSetinitial (x);
      return z;
    }
  }
  public class Ambiguity extends Variable
  {
    private final List < Variable > alternatives;
    public Ambiguity (List < Variable > alternatives)
    {
      this.alternatives = Collections.immutableList (alternatives);
    }
    public List < Variable > getAlternatives ()
    {
      return alternatives;
    }
  }
}
