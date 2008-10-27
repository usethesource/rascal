package org.meta_environment.rascal.ast;
public abstract class StandardOperator extends AbstractAST
{
  public class Addition extends StandardOperator
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
      return visitor.visitAdditionStandardOperator (this);
    }
  }
  public class Substraction extends StandardOperator
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
      return visitor.visitSubstractionStandardOperator (this);
    }
  }
  public class Product extends StandardOperator
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
      return visitor.visitProductStandardOperator (this);
    }
  }
  public class Division extends StandardOperator
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
      return visitor.visitDivisionStandardOperator (this);
    }
  }
  public class Intersection extends StandardOperator
  {
    private Intersection ()
    {
    }
    /*package */ Intersection (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitIntersectionStandardOperator (this);
    }
  }
  public class Equals extends StandardOperator
  {
    private Equals ()
    {
    }
    /*package */ Equals (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitEqualsStandardOperator (this);
    }
  }
  public class NotEquals extends StandardOperator
  {
    private NotEquals ()
    {
    }
    /*package */ NotEquals (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNotEqualsStandardOperator (this);
    }
  }
  public class LessThan extends StandardOperator
  {
    private LessThan ()
    {
    }
    /*package */ LessThan (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLessThanStandardOperator (this);
    }
  }
  public class LessThanOrEq extends StandardOperator
  {
    private LessThanOrEq ()
    {
    }
    /*package */ LessThanOrEq (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitLessThanOrEqStandardOperator (this);
    }
  }
  public class GreaterThan extends StandardOperator
  {
    private GreaterThan ()
    {
    }
    /*package */ GreaterThan (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGreaterThanStandardOperator (this);
    }
  }
  public class GreaterThanOrEq extends StandardOperator
  {
    private GreaterThanOrEq ()
    {
    }
    /*package */ GreaterThanOrEq (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitGreaterThanOrEqStandardOperator (this);
    }
  }
  public class And extends StandardOperator
  {
    private And ()
    {
    }
    /*package */ And (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitAndStandardOperator (this);
    }
  }
  public class Or extends StandardOperator
  {
    private Or ()
    {
    }
    /*package */ Or (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitOrStandardOperator (this);
    }
  }
  public class Not extends StandardOperator
  {
    private Not ()
    {
    }
    /*package */ Not (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNotStandardOperator (this);
    }
  }
  public class In extends StandardOperator
  {
    private In ()
    {
    }
    /*package */ In (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitInStandardOperator (this);
    }
  }
  public class NotIn extends StandardOperator
  {
    private NotIn ()
    {
    }
    /*package */ NotIn (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IVisitor visitor)
    {
      return visitor.visitNotInStandardOperator (this);
    }
  }
}
