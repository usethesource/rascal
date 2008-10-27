package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StandardOperator extends AbstractAST
{
  public class Addition extends StandardOperator
  {
/* "+" -> StandardOperator {cons("Addition")} */
    private Addition ()
    {
    }
    /*package */ Addition (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAdditionStandardOperator (this);
    }
  }
  public class Substraction extends StandardOperator
  {
/* "-" -> StandardOperator {cons("Substraction")} */
    private Substraction ()
    {
    }
    /*package */ Substraction (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitSubstractionStandardOperator (this);
    }
  }
  public class Product extends StandardOperator
  {
/* "*" -> StandardOperator {cons("Product")} */
    private Product ()
    {
    }
    /*package */ Product (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitProductStandardOperator (this);
    }
  }
  public class Division extends StandardOperator
  {
/* "/" -> StandardOperator {cons("Division")} */
    private Division ()
    {
    }
    /*package */ Division (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitDivisionStandardOperator (this);
    }
  }
  public class Intersection extends StandardOperator
  {
/* "&" -> StandardOperator {cons("Intersection")} */
    private Intersection ()
    {
    }
    /*package */ Intersection (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitIntersectionStandardOperator (this);
    }
  }
  public class Equals extends StandardOperator
  {
/* "==" -> StandardOperator {cons("Equals")} */
    private Equals ()
    {
    }
    /*package */ Equals (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitEqualsStandardOperator (this);
    }
  }
  public class NotEquals extends StandardOperator
  {
/* "!=" -> StandardOperator {cons("NotEquals")} */
    private NotEquals ()
    {
    }
    /*package */ NotEquals (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNotEqualsStandardOperator (this);
    }
  }
  public class LessThan extends StandardOperator
  {
/* "<" -> StandardOperator {cons("LessThan")} */
    private LessThan ()
    {
    }
    /*package */ LessThan (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLessThanStandardOperator (this);
    }
  }
  public class LessThanOrEq extends StandardOperator
  {
/* "<=" -> StandardOperator {cons("LessThanOrEq")} */
    private LessThanOrEq ()
    {
    }
    /*package */ LessThanOrEq (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitLessThanOrEqStandardOperator (this);
    }
  }
  public class GreaterThan extends StandardOperator
  {
/* ">" -> StandardOperator {cons("GreaterThan")} */
    private GreaterThan ()
    {
    }
    /*package */ GreaterThan (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitGreaterThanStandardOperator (this);
    }
  }
  public class GreaterThanOrEq extends StandardOperator
  {
/* ">=" -> StandardOperator {cons("GreaterThanOrEq")} */
    private GreaterThanOrEq ()
    {
    }
    /*package */ GreaterThanOrEq (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitGreaterThanOrEqStandardOperator (this);
    }
  }
  public class And extends StandardOperator
  {
/* "&&" -> StandardOperator {cons("And")} */
    private And ()
    {
    }
    /*package */ And (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitAndStandardOperator (this);
    }
  }
  public class Or extends StandardOperator
  {
/* "||" -> StandardOperator {cons("Or")} */
    private Or ()
    {
    }
    /*package */ Or (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitOrStandardOperator (this);
    }
  }
  public class Not extends StandardOperator
  {
/* "!" -> StandardOperator {cons("Not")} */
    private Not ()
    {
    }
    /*package */ Not (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNotStandardOperator (this);
    }
  }
  public class In extends StandardOperator
  {
/* "in" -> StandardOperator {cons("In")} */
    private In ()
    {
    }
    /*package */ In (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitInStandardOperator (this);
    }
  }
  public class NotIn extends StandardOperator
  {
/* "notin" -> StandardOperator {cons("NotIn")} */
    private NotIn ()
    {
    }
    /*package */ NotIn (ITree tree)
    {
      this.tree = tree;
    }
    public IVisitable accept (IASTVisitor visitor)
    {
      return visitor.visitNotInStandardOperator (this);
    }
  }
}
