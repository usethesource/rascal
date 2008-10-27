package org.meta_environment.rascal.ast;
public abstract class Assignment extends AbstractAST
{
  public class Default extends Assignment
  {
    private Default ()
    {
    }
    /*package */ Default (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDefaultAssignment (this);
    }
  }
  public class Addition extends Assignment
  {
    private Addition ()
    {
    }
    /*package */ Addition (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAdditionAssignment (this);
    }
  }
  public class Substraction extends Assignment
  {
    private Substraction ()
    {
    }
    /*package */ Substraction (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitSubstractionAssignment (this);
    }
  }
  public class Product extends Assignment
  {
    private Product ()
    {
    }
    /*package */ Product (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitProductAssignment (this);
    }
  }
  public class Division extends Assignment
  {
    private Division ()
    {
    }
    /*package */ Division (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitDivisionAssignment (this);
    }
  }
  public class Interesection extends Assignment
  {
    private Interesection ()
    {
    }
    /*package */ Interesection (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitInteresectionAssignment (this);
    }
  }
}
