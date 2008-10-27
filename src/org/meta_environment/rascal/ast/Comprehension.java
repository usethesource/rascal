package org.meta_environment.rascal.ast;
public abstract class Comprehension extends AbstractAST
{
  public class Set extends Comprehension
  {
    private Expression result;
    private List < Generator > generators;

    private Set ()
    {
    }
    /*package */ Set (ITree tree, Expression result,
		      List < Generator > generators)
    {
      this.tree = tree;
      this.result = result;
      this.generators = generators;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSetComprehension (this);
    }
    private final Expression result;
    public Expression getresult ()
    {
      return result;
    }
    private void privateSetresult (Expression x)
    {
      this.result = x;
    }
    public Set setresult (Expression x)
    {
      z = new Set ();
      z.privateSetresult (x);
      return z;
    }
    private final List < Generator > generators;
    public List < Generator > getgenerators ()
    {
      return generators;
    }
    private void privateSetgenerators (List < Generator > x)
    {
      this.generators = x;
    }
    public Set setgenerators (List < Generator > x)
    {
      z = new Set ();
      z.privateSetgenerators (x);
      return z;
    }
  }
  public class List extends Comprehension
  {
    private Expression result;
    private List < Generator > generators;

    private List ()
    {
    }
    /*package */ List (ITree tree, Expression result,
		       List < Generator > generators)
    {
      this.tree = tree;
      this.result = result;
      this.generators = generators;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitListComprehension (this);
    }
    private final Expression result;
    public Expression getresult ()
    {
      return result;
    }
    private void privateSetresult (Expression x)
    {
      this.result = x;
    }
    public List setresult (Expression x)
    {
      z = new List ();
      z.privateSetresult (x);
      return z;
    }
    private final List < Generator > generators;
    public List < Generator > getgenerators ()
    {
      return generators;
    }
    private void privateSetgenerators (List < Generator > x)
    {
      this.generators = x;
    }
    public List setgenerators (List < Generator > x)
    {
      z = new List ();
      z.privateSetgenerators (x);
      return z;
    }
  }
}
