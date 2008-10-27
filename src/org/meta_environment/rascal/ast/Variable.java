package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class Variable extends AbstractAST
{
  public class DefaultInitialization extends Variable
  {
/* name:Name tags:Tags -> Variable {cons("DefaultInitialization")} */
    private DefaultInitialization ()
    {
    }
    /*package */ DefaultInitialization (ITree tree, Name name, Tags tags)
    {
      this.tree = tree;
      this.name = name;
      this.tags = tags;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDefaultInitializationVariable (this);
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
    public DefaultInitialization setname (Name x)
    {
      z = new DefaultInitialization ();
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
    public DefaultInitialization settags (Tags x)
    {
      z = new DefaultInitialization ();
      z.privateSettags (x);
      return z;
    }
  }
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
      return visitor.visitGivenInitializationVariable (this);
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
      z = new GivenInitialization ();
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
      z = new GivenInitialization ();
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
      z = new GivenInitialization ();
      z.privateSetinitial (x);
      return z;
    }
  }
}
