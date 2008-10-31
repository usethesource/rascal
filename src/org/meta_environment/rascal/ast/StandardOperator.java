package org.meta_environment.rascal.ast;
import org.eclipse.imp.pdb.facts.ITree;
public abstract class StandardOperator extends AbstractAST
{
  static public class Addition extends StandardOperator
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
      return visitor.visitStandardOperatorAddition (this);
    }
  }
  public class Ambiguity extends StandardOperator
  {
    private final java.util.List < StandardOperator > alternatives;
    public Ambiguity (java.util.List < StandardOperator > alternatives)
    {
      this.alternatives =
	java.util.Collections.unmodifiableList (alternatives);
    }
    public java.util.List < StandardOperator > getAlternatives ()
    {
      return alternatives;
    }
  }
  static public class Substraction extends StandardOperator
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
      return visitor.visitStandardOperatorSubstraction (this);
    }
  }
  static public class Product extends StandardOperator
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
      return visitor.visitStandardOperatorProduct (this);
    }
  }
  static public class Division extends StandardOperator
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
      return visitor.visitStandardOperatorDivision (this);
    }
  }
  static public class Intersection extends StandardOperator
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
      return visitor.visitStandardOperatorIntersection (this);
    }
  }
  static public class Equals extends StandardOperator
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
      return visitor.visitStandardOperatorEquals (this);
    }
  }
  static public class NotEquals extends StandardOperator
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
      return visitor.visitStandardOperatorNotEquals (this);
    }
  }
  static public class LessThan extends StandardOperator
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
      return visitor.visitStandardOperatorLessThan (this);
    }
  }
  static public class LessThanOrEq extends StandardOperator
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
      return visitor.visitStandardOperatorLessThanOrEq (this);
    }
  }
  static public class GreaterThan extends StandardOperator
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
      return visitor.visitStandardOperatorGreaterThan (this);
    }
  }
  static public class GreaterThanOrEq extends StandardOperator
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
      return visitor.visitStandardOperatorGreaterThanOrEq (this);
    }
  }
  static public class And extends StandardOperator
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
      return visitor.visitStandardOperatorAnd (this);
    }
  }
  static public class Or extends StandardOperator
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
      return visitor.visitStandardOperatorOr (this);
    }
  }
  static public class Not extends StandardOperator
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
      return visitor.visitStandardOperatorNot (this);
    }
  }
  static public class In extends StandardOperator
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
      return visitor.visitStandardOperatorIn (this);
    }
  }
  static public class NotIn extends StandardOperator
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
      return visitor.visitStandardOperatorNotIn (this);
    }
  }
}
